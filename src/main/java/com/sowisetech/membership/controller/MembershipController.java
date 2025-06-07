package com.sowisetech.membership.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.sowisetech.advisor.security.RequestWrapper;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.InvoiceSubscription;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubscriptionPayment;
import com.sowisetech.membership.request.CancelSubRequest;
import com.sowisetech.membership.request.OrderRequest;
import com.sowisetech.membership.request.MembershipPlanRequest;
import com.sowisetech.membership.request.PauseAndResumeSubRequest;
import com.sowisetech.membership.request.PlanIdRequest;
import com.sowisetech.membership.request.SinglePaymentRequest;
import com.sowisetech.membership.request.StringIdRequest;
import com.sowisetech.membership.request.SubscriptionChargedRequest;
import com.sowisetech.membership.request.SubscriptionRequest;
import com.sowisetech.membership.request.UpdateSubRequest;
import com.sowisetech.membership.request.VerifyPaymentRequest;
import com.sowisetech.membership.response.MemberSubscriptionTotalList;
import com.sowisetech.membership.response.MembershipPlanTotalList;
import com.sowisetech.membership.response.MembershipResponse;
import com.sowisetech.membership.response.MembershipResponseData;
import com.sowisetech.membership.response.MembershipResponseMessage;
import com.sowisetech.membership.service.MembershipService;
import com.sowisetech.membership.service.RazorpayService;
import com.sowisetech.membership.util.MembershipAppMessages;
import com.sowisetech.membership.util.MembershipConstants;
import com.sowisetech.membership.util.RazorpayProperties;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MembershipController {

	@Autowired
	ScreenRightsConstants screenRightsConstants;
	@Autowired
	ScreenRightsCommon screenRightsCommon;
	@Autowired
	CommonService commonService;
	@Autowired
	MembershipAppMessages appMessages;
	@Autowired
	RazorpayService razorpayService;
	@Autowired
	MembershipService membershipService;
	@Autowired
	AdvisorService advisorService;
	@Autowired
	RazorpayProperties razorpayProperties;

	private static final Logger logger = LoggerFactory.getLogger(MembershipController.class);

	/**
	 * Extended Content Verification - Health check
	 * 
	 * @return ResponseEntity - HttpStatus.OK
	 */
	@GetMapping(value = "/membership-ecv")
	public ResponseEntity getEcv() {
		logger.info("Advisor module running..");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Create Membership Plan
	 * 
	 * @param RequestBody
	 *            contains MembershipPlanRequest
	 * @return ResponseEntity<MembershipPlan> or MembershipResponse
	 * 
	 */
	/*---Create Membership Plan ---*/
	@ApiOperation(value = "createMembershipPlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createMembershipPlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> createMembershipPlan(
			@NonNull @RequestBody MembershipPlanRequest membershipPlanRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (membershipPlanRequest != null) {
			int screenId = membershipPlanRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Create Membership Plan");
		MembershipPlan membershipPlan = razorpayService.createPlan(membershipPlanRequest);
		if (membershipPlan == null) {
			logger.error("Error Occured while creating plan in razorpay");
			MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
					appMessages.getError_occurred_plan());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		membershipPlan.setPlanName(membershipPlanRequest.getPlanName());
		membershipPlan.setContent(membershipPlanRequest.getContent());
		int result = membershipService.addMembershipPlan(membershipPlan);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
					appMessages.getError_occurred_plan());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("New membership plan added into table");
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getPlan_created(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * FetchAll Membership Plan
	 * 
	 * @param RequestBody
	 *            contains ScreenIdRequest
	 * @return ResponseEntity<List<MembershipPlan>> or MembershipResponse
	 * 
	 */
	/*---Fetch All Membership Plan ---*/
	@ApiOperation(value = "fetchAllMembershipPlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllMembershipPlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> fetchAllMembershipPlan(
			@NonNull @RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Fetch all Membership plan");
		int planList = membershipService.fetchMemPlanTotalList();
		// List<MembershipPlan> membershipPlans = razorpayService.fetchAllPlan();
		List<MembershipPlan> memberShipPlans = membershipService.fetchAllMembershipPlan();
		MembershipPlanTotalList membershipPlanTotalList = new MembershipPlanTotalList();
		if (memberShipPlans != null) {
			membershipPlanTotalList.setMemberShipPlans(memberShipPlans);
			membershipPlanTotalList.setTotalRecords(planList);
		}
		MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE, appMessages.getSuccess(),
				membershipPlanTotalList, roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch Membership Plan By plan_id
	 * 
	 * @param RequestBody
	 *            contains PlanIdRequest
	 * @return ResponseEntity<MembershipPlan> or MembershipResponse
	 * 
	 */
	/*---Fetch Membership Plan By Plan_id ---*/
	@ApiOperation(value = "fetchMembershipPlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchMembershipPlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> fetchMembershipPlan(@NonNull @RequestBody PlanIdRequest planIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (planIdRequest != null) {
			int screenId = planIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Fetch Membership plan");
		MembershipPlan plan = razorpayService.fetchPlanByPlanId(planIdRequest.getPlan_id());
		MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Create Subscription
	 * 
	 * @param RequestBody
	 *            contains SubscriptionRequest
	 * @return ResponseEntity<MemberSubscription> or MembershipResponse
	 * 
	 */
	/*---Create Subscription ---*/
	@ApiOperation(value = "createSubscription", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createSubscription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> createSubscription(
			@NonNull @RequestBody SubscriptionRequest subscriptionRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (subscriptionRequest != null) {
			int screenId = subscriptionRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int advisor = membershipService.checkAdvisorIsPresent(subscriptionRequest.getAdvId());
		if (advisor == 0) {
			logger.info("No record Found");
			MembershipResponse response = messageResponse(MembershipConstants.NO_RECORD_FOUND,
					appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			MemberSubscription memberSubscriptionTable = membershipService
					.fetchMemberSubByadvId(subscriptionRequest.getAdvId());
			if (memberSubscriptionTable != null) {
				logger.error("Subscription already created");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getSubscription_already_created());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			MemberSubscription memberSubscription = razorpayService.createSubscription(subscriptionRequest);
			if (memberSubscription == null) {
				logger.error("Error Occured while creating subscription in razorpay");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			logger.info("Add subscription");
			int result = membershipService.addSubscription(subscriptionRequest.getAdvId(), memberSubscription);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("New subscription added into table");
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getSubscription_created(), memberSubscription, null);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Cancel Subscription
	 * 
	 * @param RequestBody
	 *            contains CancelSubRequest
	 * @return ResponseEntity<MemberSubscription> or MembershipResponse
	 * 
	 */
	/*---Cancel Subscription ---*/
	@ApiOperation(value = "cancelSubscription", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/cancelSubscription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> cancelSubscription(
			@NonNull @RequestBody CancelSubRequest cancelSubRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (cancelSubRequest != null) {
			int screenId = cancelSubRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int advisor = membershipService.checkAdvisorIsPresent(cancelSubRequest.getAdvId());
		if (advisor == 0) {
			logger.info("No record Found");
			MembershipResponse response = messageResponse(MembershipConstants.NO_RECORD_FOUND,
					appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("Cancel subscription");
			MemberSubscription memberSubscription = razorpayService.cancelSubscription(cancelSubRequest);
			if (memberSubscription == null) {
				logger.error("Error Occured while canceling subscription in razorpay");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton_status());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			int result = membershipService.updateSubscription(cancelSubRequest.getAdvId(), memberSubscription);
			if (result == 0) {
				logger.error("Error Occured while canceling data into table");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton_status());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Subscription cancelled successfully");
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getCancel_subscription(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Update Subscription
	 * 
	 * @param RequestBody
	 *            contains UpdateSubRequest
	 * @return ResponseEntity<MemberSubscription> or MembershipResponse
	 * 
	 */
	/*---Update Subscription ---*/
	@ApiOperation(value = "updateSubscription", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/updateSubscription", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> updateSubscription(
			@NonNull @RequestBody UpdateSubRequest updateSubRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (updateSubRequest != null) {
			int screenId = updateSubRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int advisor = membershipService.checkAdvisorIsPresent(updateSubRequest.getAdvId());
		if (advisor == 0) {
			logger.info("No record Found");
			MembershipResponse response = messageResponse(MembershipConstants.NO_RECORD_FOUND,
					appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("Update subscription");
			MemberSubscription memberSubscription = razorpayService.updateSubscription(updateSubRequest);
			if (memberSubscription == null) {
				logger.error("Error Occured while updating subscription in razorpay");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton_status());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			int result = membershipService.updateSubscription(updateSubRequest.getAdvId(), memberSubscription);
			if (result == 0) {
				logger.error("Error Occured while updating data into table");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton_status());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Updated subscription details added into table");
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getUpdate_subscription(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Pause and Resume Subscription
	 * 
	 * @param RequestBody
	 *            contains PauseAndResumeSubRequest
	 * @return ResponseEntity<MemberSubscription> or MembershipResponse
	 * 
	 */
	/*---Pause and Resume Subscription ---*/
	@ApiOperation(value = "pauseAndResumeSubscription", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/pauseAndResumeSubscription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> pauseAndResumeSubscription(
			@NonNull @RequestBody PauseAndResumeSubRequest pauseAndResumeSubRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (pauseAndResumeSubRequest != null) {
			int screenId = pauseAndResumeSubRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int advisor = membershipService.checkAdvisorIsPresent(pauseAndResumeSubRequest.getAdvId());
		if (advisor == 0) {
			logger.info("No record Found");
			MembershipResponse response = messageResponse(MembershipConstants.NO_RECORD_FOUND,
					appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("Pause and Resume subscription");
			MemberSubscription membershipSub = razorpayService.pauseAndResumeSubscription(pauseAndResumeSubRequest);
			if (membershipSub == null) {
				logger.error("Error Occured while changing subscription status in razorpay");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton_status());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			logger.info("Update Subscription");
			int result = membershipService.updateSubscription(pauseAndResumeSubRequest.getAdvId(), membershipSub);
			if (result == 0) {
				logger.error("Error Occured while updating data into table");
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_subscripton_status());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Updated subscription status added into table");
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getSubscription_status_changed(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Fetch All Invoice
	 * 
	 * @param RequestBody
	 *            contains StringIdRequest
	 * @return ResponseEntity<InvoiceSubscription> or MembershipResponse
	 * 
	 */
	/*---Fetch All Invoices ---*/
	@ApiOperation(value = "fetchAllInvoice", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllInvoice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> fetchAllInvoice(@NonNull @RequestBody StringIdRequest subIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (subIdRequest != null) {
			int screenId = subIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (subIdRequest.getId() == null) {
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_fetchAllInvoice(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetch all invoice for subscription");
			List<String> invoice_id = membershipService.fetchInvoiceIdFromSubPayment(subIdRequest.getId());
			List<InvoiceSubscription> invoiceSubsList = razorpayService.fetchAllInvoiceSub(invoice_id);
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE, appMessages.getSuccess(),
					invoiceSubsList, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Web Hook Event update
	 * 
	 * @param RequestBody
	 *            contains SubscriptionChargedRequest
	 * @return ResponseEntity<MembershipResponse> or MembershipResponse
	 * 
	 */
	/*---Web Hook Event ---*/
	@RequestMapping(value = "/webHookEventUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> webHookEventUpdate(
			@NonNull @RequestBody SubscriptionChargedRequest subscriptionChargedRequest, HttpServletRequest request)
			throws IOException {
		String razorPaySign = request.getHeader("X-Razorpay-Signature");
		String body = ((RequestWrapper) request).getBody();
		logger.info("WebHook event : " + subscriptionChargedRequest.getEvent());
		boolean value = false;
		try {
			value = Utils.verifyWebhookSignature(body, razorPaySign, razorpayProperties.getWebhook_secret());
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		if (value == false) {
			logger.info("Failed to verify WebHook signature");
			MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
					appMessages.getFailed_to_verify_webhook_signature());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		logger.info("Verified WebHook signature successfully");
		int result = membershipService.updateSubscriptionWebHook(subscriptionChargedRequest);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
					appMessages.getFailed_to_verify_webhook_signature());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("New webhook details added into table");
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getSubscription_status_changed(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * fetchAll MemberSubscriptions
	 * 
	 * @param null
	 * @return ResponseEntity<List<MemberSubscription>> or MembershipResponse
	 * 
	 */
	/*---Fetch All Member Subscription ---*/
	@ApiOperation(value = "fetchAllMemberSubscriptions", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllMemberSubscriptions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> fetchAllSubscriptions(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest fetchAllRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (fetchAllRequest != null) {
			int screenId = fetchAllRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Fetch all Member Subscription");
		// List<MemberSubscription> memberSubscription =
		// razorpayService.fetchAllMemberSubscriptions();
		int planList = membershipService.fetchMemSubscriptionTotalList();
		List<MemberSubscription> memberSubscriptions = membershipService.fetchAllMemberSubscription(pageNum, records);
		MemberSubscriptionTotalList memberSubscriptionTotalList = new MemberSubscriptionTotalList();
		if (memberSubscriptions != null) {
			memberSubscriptionTotalList.setMemberSubscription(memberSubscriptions);
			memberSubscriptionTotalList.setTotalRecords(planList);
		}
		MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE, appMessages.getSuccess(),
				memberSubscriptionTotalList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * fetch MemberSubscription By Sub_id
	 * 
	 * @param RequestBody
	 *            contains SubscriptionRequest
	 * @return ResponseEntity<MemberSubscription> or MembershipResponse
	 * 
	 */
	/*---Fetch MemberSubscription By SubId ---*/
	@ApiOperation(value = "fetchMemberSubById", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchMemberSubById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> fetchMemberSubById(@NonNull @RequestBody StringIdRequest subIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (subIdRequest != null) {
			int screenId = subIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (subIdRequest.getId() == null) {
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_fetchMemberSubById(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetch Member By sub_id");
			String sub_id = subIdRequest.getId();
			MemberSubscription memberSubscription = razorpayService.fetchMemberSubById(sub_id);
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE, appMessages.getSuccess(),
					memberSubscription, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}
	/**
	 * fetch MemberSubscription By advId
	 * 
	 * @param RequestBody
	 *            contains SubscriptionRequest
	 * @return ResponseEntity<MemberSubscription> or MembershipResponse
	 * 
	 */
	/*---fetch MemberSubscription by AdvId ---*/
	@ApiOperation(value = "fetchMemberSubByAdvId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchMemberSubByAdvId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> fetchMemberSubByAdvId(
			@NonNull @RequestBody SubscriptionRequest subscriptionRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (subscriptionRequest != null) {
			int screenId = subscriptionRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (subscriptionRequest.getAdvId() == null) {
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_fetchMemberSubByAdvId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetch Member subscription By advId");
			String advId = subscriptionRequest.getAdvId();
			MemberSubscription memberSubscription = membershipService.fetchMemberSubByadvId(advId);
			if (memberSubscription == null) {
				memberSubscription = new MemberSubscription();
				SinglePayment singlePayment = membershipService.fetchMemberSinglePaySubByAdvId(advId,
						MembershipConstants.SINGLE_PAID_STATUS, MembershipConstants.SUB_TYPE);
				memberSubscription.setSinglePayment(singlePayment);
				if (singlePayment == null) {
					memberSubscription = null;
				}
			} else {
				memberSubscription.setType(MembershipConstants.SUB_TYPE);
			}
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE, appMessages.getSuccess(),
					memberSubscription, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Create single payment order
	 * 
	 * @param RequestBody
	 *            contains SinglePaymentRequest
	 * @return ResponseEntity<MembershipResponse> or MembershipResponse
	 * 
	 */
	/*---create single payment order ---*/
	@ApiOperation(value = "To create SinglePayment Order", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createSinglePaymentOrder", method = RequestMethod.POST)
	public ResponseEntity<MembershipResponse> createSinglePaymentOrder(
			@RequestBody SinglePaymentRequest singlePaymentRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (singlePaymentRequest != null) {
			int screenId = singlePaymentRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String type = singlePaymentRequest.getType();
		String planId = singlePaymentRequest.getPlan_id();
		String period = singlePaymentRequest.getPeriod();
		int totalCount = singlePaymentRequest.getTotal_count();
		if (type == null && planId == null && period == null && totalCount == 0) {
			logger.info("type & planId & period & totalCount is Mandatory");
			MembershipResponse response = messageResponse(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_type_planId_period__totalCount());
			return ResponseEntity.ok().body(response);
		} else {
			Party party = advisorService.fetchPartyByRoleBasedId(singlePaymentRequest.getId());
			if (party == null) {
				logger.info("No record Found");
				MembershipResponse response = responseWithData(MembershipConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.ok().body(response);
			}
			logger.info("Create Single Payment Order");
			SinglePayment singlePayment = razorpayService.createSinglePaymentOrder(singlePaymentRequest);

			if (singlePayment == null) {
				logger.error("Error Occured while adding data into table");
				MembershipResponse response = responseWithData(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_order(), singlePayment, null);
				return ResponseEntity.ok().body(response);
			}
			singlePayment.setType(singlePaymentRequest.getType());
			singlePayment.setPlan_id(singlePaymentRequest.getPlan_id());
			singlePayment.setPeriod(singlePaymentRequest.getPeriod());
			singlePayment.setTotal_count(singlePaymentRequest.getTotal_count());
			int result = membershipService.addSinglePaymentOrder(singlePayment);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				MembershipResponse response = responseWithData(MembershipConstants.ERROR_CODE,
						appMessages.getError_occurred_adding_table(), singlePayment, null);
				return ResponseEntity.ok().body(response);
			}
			logger.error("New order detail added into table");
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getSingle_pay_order_created(), singlePayment, null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Verify single payment
	 * 
	 * @param RequestBody
	 *            contains VerifyPaymentRequest
	 * @return ResponseEntity<MembershipResponse> or MembershipResponse
	 * 
	 */
	/*---verify single payment ---*/
	@ApiOperation(value = "To verify SinglePayment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/verifySinglePayment", method = RequestMethod.POST)
	public ResponseEntity<MembershipResponse> verifySinglePayment(
			@RequestBody VerifyPaymentRequest verifyPaymentRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (verifyPaymentRequest != null) {
			int screenId = verifyPaymentRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (verifyPaymentRequest.getRazorpay_payment_id() == null && verifyPaymentRequest.getSinglePaymentId() == 0
				&& verifyPaymentRequest.getType() == null) {
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_verifySinglePayment(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			SinglePayment singlePayment = membershipService
					.fetchSinglePaymentByPrimaryKey(verifyPaymentRequest.getSinglePaymentId());
			if (singlePayment == null) {
				logger.info("No record Found");
				MembershipResponse response = responseWithData(MembershipConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.ok().body(response);
			}
			boolean verified = razorpayService.verifySinglePayment(verifyPaymentRequest, singlePayment);
			if (verified) {
				logger.info("Verified Single Payment");
				SubscriptionPayment subscriptionPayment = razorpayService
						.fetchPaymentDetailsByPaymentId(verifyPaymentRequest.getRazorpay_payment_id());
				int result;
				if (verifyPaymentRequest.getType().equals(MembershipConstants.SUB_TYPE)) {
					ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
					Timestamp timestamp1 = Timestamp.valueOf(zdt.toLocalDateTime());
					Timestamp timestamp2 = null;
					if (singlePayment.getPeriod().equals("monthly")) {
						timestamp2 = Timestamp.valueOf(zdt.plusMonths(1).toLocalDateTime());
					} else if (singlePayment.getPeriod().equals("yearly")) {
						timestamp2 = Timestamp.valueOf(zdt.plusYears(1).toLocalDateTime());
					}
					singlePayment.setSubStartedAt(timestamp1);
					singlePayment.setSubEndAt(timestamp2);
					result = membershipService.updatePaymentDetails(subscriptionPayment,
							verifyPaymentRequest.getSinglePaymentId(), singlePayment);
				}
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getVerified_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Error occurred while verifying single payment");
				MembershipResponse response = responseWithData(MembershipConstants.ERROR_CODE,
						appMessages.getVerification_failed(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}

	}


	/**
	 * Verify subscription payment
	 * 
	 * @param RequestBody
	 *            contains VerifyPaymentRequest
	 * @return ResponseEntity<MembershipResponse> or MembershipResponse
	 * 
	 */
	/*---verify subscription payment ---*/
	@ApiOperation(value = "To verify Subscription Payment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/verifySubscriptionPayment", method = RequestMethod.POST)
	public ResponseEntity<MembershipResponse> verifySubscriptionPayment(
			@RequestBody VerifyPaymentRequest verifyPaymentRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (verifyPaymentRequest != null) {
			int screenId = verifyPaymentRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (verifyPaymentRequest.getRazorpay_payment_id() == null
				&& verifyPaymentRequest.getSubscription_id() == null) {
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_verifySubscriptionPayment(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			boolean verified = razorpayService.verifySubscriptionPayment(verifyPaymentRequest);
			if (verified) {
				logger.info("Verified Subscription Payment");
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getVerified_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Error occurred while verifying subscription payment");
				MembershipResponse response = responseWithData(MembershipConstants.ERROR_CODE,
						appMessages.getVerification_failed(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}

	}
	/**
	 * Create Order Number
	 * 
	 * @param RequestBody
	 *            contains OrderRequest
	 * @return ResponseEntity<MembershipResponse> or MembershipResponse
	 * 
	 */
	/*---create order number ---*/
	@ApiOperation(value = "createOrderNumber", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createOrderNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> createOrderNumber(@NonNull @RequestBody OrderRequest invoiceRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (invoiceRequest != null) {
			int screenId = invoiceRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String orderDetailId = invoiceRequest.getOrderDetailId();
		String type = invoiceRequest.getType();
		String roleBasedId = invoiceRequest.getRoleBasedId();
		if (orderDetailId == null && type == null && roleBasedId == null) {
			logger.info("orderDetailId & type & RoleBasedId is Mandatory");
			MembershipResponse response = messageResponse(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_orderDetailId_type_roleBasedId());
			return ResponseEntity.ok().body(response);
		} else {
			int advisor = membershipService.checkAdvisorIsPresent(invoiceRequest.getRoleBasedId());
			if (advisor == 0) {
				logger.info("No record Found");
				MembershipResponse response = messageResponse(MembershipConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Add order");
				String orderNumber = "";
				if (invoiceRequest.getType().equals(MembershipConstants.SUB_TYPE)) {
					orderNumber = membershipService.generateSubOrderNumber();
				} else if (invoiceRequest.getType().equals(MembershipConstants.PAY_TYPE)) {
					orderNumber = membershipService.generatePaymentOrderNumber();
				}
				OrderDetail invoice = getValueInvoiceSub(invoiceRequest);
				invoice.setOrderDetailId(orderNumber);
				invoice.setType(invoiceRequest.getType());
				OrderDetail orderDetail = membershipService.addOrder(invoice);
				logger.info("New order number added into table");
				MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
						appMessages.getOrder_num_created(), orderDetail, null);
				return ResponseEntity.ok().body(response);
			}
		}
	}


	/**
	 * update Order detail
	 * 
	 * @param RequestBody
	 *            contains OrderRequest
	 * @return ResponseEntity<MembershipResponse> or MembershipResponse
	 * 
	 */
	/*---update order detail as paid ---*/
	@ApiOperation(value = "updateOrderDetailAsPaid", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/updateOrderDetailAsPaid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<MembershipResponse> updateOrderDetail(@NonNull @RequestBody OrderRequest orderRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (orderRequest != null) {
			int screenId = orderRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				MembershipResponse response = messageResponse(MembershipConstants.ERROR_CODE,
						appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (orderRequest.getOrderDetailId() == null) {
			logger.info("Mandatory fields");
			MembershipResponse response = messageResponse(MembershipConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_updateOrder());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		OrderDetail orderDetail1 = membershipService.fetchOrderDetail(orderRequest.getOrderDetailId());
		if (orderDetail1 == null) {
			logger.info("No record Found");
			MembershipResponse response = messageResponse(MembershipConstants.NO_RECORD_FOUND,
					appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("updated orderDetail");
			OrderDetail orderDetail = getValueInvoiceSub(orderRequest);
			orderDetail.setStatus(MembershipConstants.PAID_STATUS);
			orderDetail.setOrderDetailId(orderRequest.getOrderDetailId());
			OrderDetail orderDetailResult = membershipService.updateOrderDetail(orderDetail);
			logger.info("Updated orderDetail status added into table");
			MembershipResponse response = responseWithData(MembershipConstants.SUCCESS_CODE,
					appMessages.getUpdate_order_detail_status_changed(), orderDetailResult, null);
			return ResponseEntity.ok().body(response);
		}
	}


	private OrderDetail getValueInvoiceSub(OrderRequest orderRequest) {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setRoleBasedId(orderRequest.getRoleBasedId());
		orderDetail.setRazorpay_payment_id(orderRequest.getRazorpay_payment_id());
		orderDetail.setEmailId(orderRequest.getEmailId());
		orderDetail.setPhoneNumber(orderRequest.getPhoneNumber());
		orderDetail.setName(orderRequest.getName());
		orderDetail.setRazorpay_plan_id(orderRequest.getPlan_id());
		orderDetail.setSubscription_id(orderRequest.getSubscription_id());
		orderDetail.setRazorpay_order_id(orderRequest.getRazorpay_order_id());
		return orderDetail;
	}

	private List<RoleFieldRights> rightsAuthentication(int screenId, HttpServletRequest request, String accessType) {
		List<RoleFieldRights> roleFieldRights = new ArrayList<>();
		List<Integer> roleScreenId = screenRightsCommon.screenRights(screenId, request, accessType);
		if (roleScreenId == null || roleScreenId.isEmpty()) {
			return roleFieldRights;
		} else {
			roleFieldRights = commonService.fetchFieldRights(roleScreenId);
			return roleFieldRights;
		}
	}

	private MembershipResponse responseWithData(long code, String message, Object data,
			List<RoleFieldRights> roleFieldRights) {
		MembershipResponseMessage responseMessage = new MembershipResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		MembershipResponseData responseData = new MembershipResponseData();
		responseData.setData(data);
		responseData.setRoleFieldRights(roleFieldRights);
		MembershipResponse response = new MembershipResponse();
		response.setResponseMessage(responseMessage);
		response.setResponseData(responseData);
		return response;
	}

	private MembershipResponse messageResponse(long code, String message) {
		MembershipResponseMessage responseMessage = new MembershipResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		MembershipResponse response = new MembershipResponse();
		response.setResponseMessage(responseMessage);
		return response;
	}

}
