package com.sowisetech.calc.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.response.AdvResponse;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.calc.model.Account;
import com.sowisetech.calc.model.AccountType;
import com.sowisetech.calc.model.CalcAnswer;
import com.sowisetech.calc.model.CalcQuery;
import com.sowisetech.calc.model.CashFlow;
import com.sowisetech.calc.model.CashFlowItem;
import com.sowisetech.calc.model.CashFlowItemType;
import com.sowisetech.calc.model.CashFlowSummary;
import com.sowisetech.calc.model.EmiCalculator;
import com.sowisetech.calc.model.EmiCapacity;
import com.sowisetech.calc.model.EmiChange;
import com.sowisetech.calc.model.FinancialPlanning;
import com.sowisetech.calc.model.Fund;
import com.sowisetech.calc.model.FutureValue;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.GoalPlanning;
import com.sowisetech.calc.model.Insurance;
import com.sowisetech.calc.model.InsuranceItem;
import com.sowisetech.calc.model.InterestChange;
import com.sowisetech.calc.model.InvestmentPlanning;
import com.sowisetech.calc.model.LoanPlanning;
import com.sowisetech.calc.model.Networth;
import com.sowisetech.calc.model.NetworthSummary;
import com.sowisetech.calc.model.PartialPayment;
import com.sowisetech.calc.model.Party;
import com.sowisetech.calc.model.Plan;
import com.sowisetech.calc.model.Priority;
import com.sowisetech.calc.model.PriorityItem;
import com.sowisetech.calc.model.Queries;
import com.sowisetech.calc.model.RateFinder;
import com.sowisetech.calc.model.RiskPortfolio;
import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskProfilePlanning;
import com.sowisetech.calc.model.RiskQuestionaire;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.TenureFinder;
import com.sowisetech.calc.model.Urgency;
import com.sowisetech.calc.request.CashFlowItemReq;
import com.sowisetech.calc.request.CashFlowRequest;
import com.sowisetech.calc.request.CashFlowRequestValidator;
import com.sowisetech.calc.request.CommentQueryRequest;
import com.sowisetech.calc.request.EmiCalculatorRequest;
import com.sowisetech.calc.request.EmiCalculatorRequestValidator;
import com.sowisetech.calc.request.EmiCapacityRequest;
import com.sowisetech.calc.request.EmiCapacityRequestValidator;
import com.sowisetech.calc.request.EmiChangeReq;
import com.sowisetech.calc.request.EmiChangeRequest;
import com.sowisetech.calc.request.EmiChangeRequestValidator;
import com.sowisetech.calc.request.EmiInterestChangeRequestValidator;
import com.sowisetech.calc.request.FundRequest;
import com.sowisetech.calc.request.FutureValueRequest;
import com.sowisetech.calc.request.FutureValueRequestValidator;
import com.sowisetech.calc.request.GoalRequest;
import com.sowisetech.calc.request.GoalRequestValidator;
import com.sowisetech.calc.request.CalcAnswerRequest;
import com.sowisetech.calc.request.CalcIdRequest;
import com.sowisetech.calc.request.InsuranceRequest;
import com.sowisetech.calc.request.InsuranceRequestValidator;
import com.sowisetech.calc.request.InterestChangeReq;
import com.sowisetech.calc.request.InterestChangeRequest;
import com.sowisetech.calc.request.InterestChangeRequestValidator;
import com.sowisetech.calc.request.ModeratePlanRequest;
import com.sowisetech.calc.request.NetworthReq;
import com.sowisetech.calc.request.NetworthRequest;
import com.sowisetech.calc.request.NetworthRequestValidator;
import com.sowisetech.calc.request.PartialPaymentReq;
import com.sowisetech.calc.request.PartialPaymentRequest;
import com.sowisetech.calc.request.PartialPaymentRequestValidator;
import com.sowisetech.calc.request.PartyIdRequest;
import com.sowisetech.calc.request.PlanRequest;
import com.sowisetech.calc.request.PlanRequestValidator;
import com.sowisetech.calc.request.PriorityReq;
import com.sowisetech.calc.request.PriorityRequest;
import com.sowisetech.calc.request.CalcQueryRequest;
import com.sowisetech.calc.request.RateFinderRequest;
import com.sowisetech.calc.request.RateFinderRequestValidator;
import com.sowisetech.calc.request.RiskProfileReq;
import com.sowisetech.calc.request.RiskProfileRequest;
import com.sowisetech.calc.request.SharedRequest;
import com.sowisetech.calc.request.TargetValueRequest;
import com.sowisetech.calc.request.TargetValueRequestValidator;
import com.sowisetech.calc.request.TenureFinderRequest;
import com.sowisetech.calc.request.TenureFinderRequestValidator;
import com.sowisetech.calc.response.AmortisationResponse;
import com.sowisetech.calc.response.AnswerRes;
import com.sowisetech.calc.response.CashFlowResponse;
import com.sowisetech.calc.response.EmiCalculatorResponse;
import com.sowisetech.calc.response.EmiCapacityResponse;
import com.sowisetech.calc.response.EmiChangeResponse;
import com.sowisetech.calc.response.InterestChangeResponse;
import com.sowisetech.calc.response.NetworthResponse;
import com.sowisetech.calc.response.PartialPaymentResponse;
import com.sowisetech.calc.response.QueryCommentList;
import com.sowisetech.calc.response.RateFinderResponse;
import com.sowisetech.calc.response.RefIdResponse;
import com.sowisetech.calc.response.CalcResponse;
import com.sowisetech.calc.response.CalcResponseData;
import com.sowisetech.calc.response.CalcResponseMessage;
import com.sowisetech.calc.response.RiskProfileResponse;
import com.sowisetech.calc.response.RiskQuestionaireResponse;
import com.sowisetech.calc.response.TotalValueResponse;
import com.sowisetech.calc.response.TenureFinderResponse;
import com.sowisetech.calc.service.CalcService;
import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcTableFields;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.common.util.MailConstants;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.common.util.SendMail;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.request.ArticleCommentRequest;
import com.sowisetech.forum.response.ArticleCommentList;
import com.sowisetech.forum.response.ArticlePostList;
import com.sowisetech.forum.response.ForumResponse;
import com.sowisetech.forum.util.ForumConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CalcController {

	@Autowired
	CalcAppMessages appMessages;
	@Autowired
	CalcTableFields calcTableFields;
	@Autowired
	private AdvTableFields advTableFields;
	@Autowired
	AdminSignin adminSignin;
	@Autowired
	PlanRequestValidator planRequestValidator;
	@Autowired
	GoalRequestValidator goalRequestValidator;
	@Autowired
	CashFlowRequestValidator cashFlowRequestValidator;
	@Autowired
	NetworthRequestValidator networthRequestValidator;
	@Autowired
	InsuranceRequestValidator insuranceRequestValidator;
	@Autowired
	FutureValueRequestValidator futureValueRequestValidator;
	@Autowired
	TargetValueRequestValidator targetValueRequestValidator;
	@Autowired
	RateFinderRequestValidator rateFinderRequestValidator;
	@Autowired
	TenureFinderRequestValidator tenureFinderRequestValidator;
	@Autowired
	EmiCalculatorRequestValidator emiCalculatorRequestValidator;
	@Autowired
	EmiCapacityRequestValidator emiCapacityRequestValidator;
	@Autowired
	InterestChangeRequestValidator interestChangeRequestValidator;
	@Autowired
	PartialPaymentRequestValidator partialPaymentRequestValidator;
	@Autowired
	EmiChangeRequestValidator emiChangeRequestValidator;
	@Autowired
	EmiInterestChangeRequestValidator emiInterestChangeRequestValidator;
	@Autowired
	CalcService calcService;
	@Autowired
	AdvisorService advisorService;
	@Autowired
	SendMail sendMail;
	@Autowired
	MailConstants mailConstants;
	@Autowired
	ScreenRightsConstants screenRightsConstants;
	@Autowired
	ScreenRightsCommon screenRightsCommon;
	@Autowired
	CommonService commonService;

	private static final Logger logger = LoggerFactory.getLogger(CalcController.class);

	@RequestMapping(value = "/calc-ecv", method = RequestMethod.GET)
	public ResponseEntity getCalcEcv() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Method to add plan to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>PlanRequest</code>
	 * @return ResponseEntity<> Success or Error Response
	 */
	@ApiOperation(value = "add plan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addPlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addPlan(@RequestBody PlanRequest planRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (planRequest != null) {
			int screenId = planRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Get partyId from request
		// Validating the request field values
		errors = planRequestValidator.validate(planRequest);
		if (errors.isEmpty() == true) {
			String referenceId = calcService.generatePlanReferenceId();
			// Changing the values from request to model by calling getValuePlanInfo
			Plan plan = getValuePlanInfo(planRequest);
			plan.setReferenceId(referenceId);
			// Add the plan into table
			logger.info("Adding plan into db");
			int result = calcService.addPlanInfo(plan);
			// String email = calcService.fetchEmailIdByPartyId(planRequest.getPartyId());
			// List<String> toUsers = new ArrayList<>();
			// toUsers.add(email);
			// sendMail.sendMailMessage(MailConstants.ADD_PLAN, toUsers,
			// mailConstants.getFromUser(), null, plan.getName(),
			// plan.getSelectedPlan());
			// Returning the response
			if (result == 0) {
				logger.info("Error occurred while adding data");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				RefIdResponse refIdResponse = new RefIdResponse();
				refIdResponse.setReferenceId(referenceId);
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
						appMessages.getPlan_added_successfully(), refIdResponse, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.error("Validation error");
			// if there is a error while validating request,return the errors
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private List<RoleFieldRights> rightsAuthentication(int screenId, HttpServletRequest request, String accessType) {
		List<Integer> roleScreenId = screenRightsCommon.screenRights(screenId, request, accessType);
		if (roleScreenId == null || roleScreenId.size() == 0) {
			return null;
		} else {
			List<RoleFieldRights> roleFieldRights = commonService.fetchFieldRights(roleScreenId);
			return roleFieldRights;
		}
	}

	/**
	 * Method to modify plan to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>PlanRequest</code>
	 * @return ResponseEntity<> Success or Error Response
	 */
	@ApiOperation(value = "modify plan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyPlan", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyPlan(@NonNull @RequestBody ModeratePlanRequest moderatePlanRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (moderatePlanRequest != null) {
			int screenId = moderatePlanRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String referenceId = moderatePlanRequest.getReferenceId();
		if (referenceId == null) {
			logger.info("referenceId is Mandatory");
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_refId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {

			logger.info("Fetching plan");
			// Plan plan = calcService.fetchPlanByReferenceId(referenceId);
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				logger.info("No Record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Modify the plan into table
				Plan planInfo = getModifiedPlanValue(moderatePlanRequest);
				logger.info("Modifying plan");
				int result = calcService.modifyPlanInfo(planInfo, referenceId);
				// Returning the response
				if (result == 0) {
					logger.info("Error occurred while adding data");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError_occured(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getPlan_moderate_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
	}

	private Plan getModifiedPlanValue(ModeratePlanRequest moderatePlanRequest) {
		Plan plan = new Plan();
		if (moderatePlanRequest != null && moderatePlanRequest.getName() != null) {
			plan.setName(moderatePlanRequest.getName());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getAge() != null) {
			plan.setAge(Integer.parseInt(moderatePlanRequest.getAge()));
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getSelectedPlan().size() != 0) {
			String selectedPlan = String.join(",", moderatePlanRequest.getSelectedPlan());
			plan.setSelectedPlan(selectedPlan);
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getSpouse() != null) {
			plan.setSpouse(moderatePlanRequest.getSpouse());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getFather() != null) {
			plan.setFather(moderatePlanRequest.getFather());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getMother() != null) {
			plan.setMother(moderatePlanRequest.getMother());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getChild1() != null) {
			plan.setChild1(moderatePlanRequest.getChild1());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getChild2() != null) {
			plan.setChild2(moderatePlanRequest.getChild2());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getChild3() != null) {
			plan.setChild3(moderatePlanRequest.getChild3());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getInLaws() != null) {
			plan.setInLaws(moderatePlanRequest.getInLaws());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getGrandParent() != null) {
			plan.setGrandParent(moderatePlanRequest.getGrandParent());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getSibilings() != null) {
			plan.setSibilings(moderatePlanRequest.getSibilings());
		}
		if (moderatePlanRequest != null && moderatePlanRequest.getOthers() != null) {
			plan.setOthers(moderatePlanRequest.getOthers());
		}
		return plan;
	}

	/**
	 * Method to remove plan to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>PlanRequest</code>
	 * @return ResponseEntity<> Success or Error Response
	 */
	// @CrossOrigin(origins = "*", allowedHeaders = "*")
	@ApiOperation(value = "delete the plan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removePlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removePlan(@NonNull @RequestBody CalcIdRequest calcIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (calcIdRequest != null) {
			int screenId = calcIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String referenceId = calcIdRequest.getId();
		if (referenceId == null) {
			logger.info("referenceId is Mandatory");
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_refId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {

			logger.info("Fetching plan by referenceId");
			// Plan plan = calcService.fetchPlanByReferenceId(referenceId);
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				logger.info("No Record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Remove the plan into table
				logger.info("Removing plan");
				int result = calcService.removePlanInfo(referenceId);
				// Returning the response
				if (result == 0) {
					logger.info("Error occurred while removing data");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
							appMessages.getError_occured_remove(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getPlan_delete_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
	}

	private CalcResponse responseWithData(long code, String message, Object data,
			List<RoleFieldRights> roleFieldRights) {
		CalcResponseMessage responseMessage = new CalcResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		CalcResponseData responseData = new CalcResponseData();
		responseData.setData(data);
		responseData.setRoleFieldRights(roleFieldRights);
		CalcResponse response = new CalcResponse();
		response.setResponseMessage(responseMessage);
		response.setResponseData(responseData);
		return response;
	}

	private CalcResponse messageResponse(long code, String message) {
		CalcResponseMessage responseMessage = new CalcResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		CalcResponse response = new CalcResponse();
		response.setResponseMessage(responseMessage);
		return response;
	}

	/**
	 * Method to plan to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>CalcIdRequest</code>
	 * @return ResponseEntity<> Success
	 */
	@ApiOperation(value = "fetch the plan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchPlan", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchPlanByReferenceId(@NonNull @RequestBody CalcIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		String id = idRequest.getId();
		if (id == null) {
			logger.info("referenceId is Mandatory");
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_refId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			// Fetch the plan from plan table by referenceId
			logger.info("Fetching plan by referenceId");
			Plan plan = calcService.fetchPlanByReferenceId(id);
			// If plan is available then return plan
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch plan by partyId
	 * 
	 * @param RequestBody
	 *            contains the <code>PartyIdRequest</code>
	 * @return ResponseEntity<> Success
	 */
	@ApiOperation(value = "fetch the plan by partyId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchPlanByPartyId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchPlanByPartyId(@NonNull @RequestBody PartyIdRequest partyIdRequest,
			HttpServletRequest request) {
		// int screenId = partyIdRequest.getScreenId();
		// if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
		// return rightsAuthentication(screenId, request,
		// screenRightsConstants.getView());
		// }
		List<RoleFieldRights> roleFieldRights = null;
		if (partyIdRequest != null) {
			int screenId = partyIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		// if (pageNum == 0) {
		// CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
		// appMessages.getInvalid_pagenum(), null,
		// null);
		// return ResponseEntity.ok().body(response);
		// } else {
		// if (pageNum == 1) {
		// } else {
		// pageNum = (pageNum - 1) * records + 1;
		// }
		long partyId = partyIdRequest.getPartyId();
		if (partyId == 0) {
			logger.info("partyId is Mandatory");
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_refId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			// List<Plan> plans = calcService.fetchTotalPlanByPartyId(partyId);
			logger.info("Fetching plan by partyId");
			List<Plan> planList = calcService.fetchPlanByPartyId(partyId);

			// If plan is available then return plan

			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), planList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);

			// }
			// }
		}
	}

	private Plan getValuePlanInfo(PlanRequest planRequest) {
		Plan plan = new Plan();
		if (planRequest != null && planRequest.getPartyId() != 0) {
			plan.setPartyId(planRequest.getPartyId());
		}
		if (planRequest != null && planRequest.getParentPartyId() != 0) {
			plan.setParentPartyId(planRequest.getParentPartyId());
		}
		if (planRequest != null && planRequest.getName() != null) {
			plan.setName(planRequest.getName());
		}
		if (planRequest != null && planRequest.getAge() != null) {
			plan.setAge(Integer.parseInt(planRequest.getAge()));
		}
		if (planRequest != null && planRequest.getSelectedPlan().size() != 0) {
			String selectedPlan = String.join(",", planRequest.getSelectedPlan());
			plan.setSelectedPlan(selectedPlan);
		}
		if (planRequest != null && planRequest.getSpouse() != null) {
			plan.setSpouse(planRequest.getSpouse());
		}
		if (planRequest != null && planRequest.getFather() != null) {
			plan.setFather(planRequest.getFather());
		}
		if (planRequest != null && planRequest.getMother() != null) {
			plan.setMother(planRequest.getMother());
		}
		if (planRequest != null && planRequest.getChild1() != null) {
			plan.setChild1(planRequest.getChild1());
		}
		if (planRequest != null && planRequest.getChild2() != null) {
			plan.setChild2(planRequest.getChild2());
		}
		if (planRequest != null && planRequest.getChild3() != null) {
			plan.setChild3(planRequest.getChild3());
		}
		if (planRequest != null && planRequest.getInLaws() != null) {
			plan.setInLaws(planRequest.getInLaws());
		}
		if (planRequest != null && planRequest.getGrandParent() != null) {
			plan.setGrandParent(planRequest.getGrandParent());
		}
		if (planRequest != null && planRequest.getSibilings() != null) {
			plan.setSibilings(planRequest.getSibilings());
		}
		if (planRequest != null && planRequest.getOthers() != null) {
			plan.setOthers(planRequest.getOthers());
		}
		return plan;
	}

	// FINANCIAL PLANNING
	// My Goal
	/**
	 * Method to add goal to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>GoalRequest</code>
	 * @return ResponseEntity<> List<Goal> or Error Response
	 */
	@ApiOperation(value = "calculate goal", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateGoal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateGoal(@NonNull @RequestBody GoalRequest goalRequest, HttpServletRequest request,
			@RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = goalRequest.getReferenceId();
		logger.info("Fetching plan by referenceId");
		// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
		if (token != null && referenceId != null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// Validating goalRequest
		if (goalRequest.getGoalName() != null && goalRequest.getTenure() != null && goalRequest.getTenureType() != null
				&& goalRequest.getGoalAmount() != null && goalRequest.getCurrentAmount() != null
				&& goalRequest.getInflationRate() != null && goalRequest.getGrowthRate() != null
				&& goalRequest.getAnnualInvestmentRate() != null) {
			errors = goalRequestValidator.validate(goalRequest);
			if (errors.isEmpty() == true) {
				int tenureZeroCheck = (Integer.parseInt(goalRequest.getTenure()));
				double goalAmt = Double.parseDouble(goalRequest.getGoalAmount());
				double growthRateZeroCheck = (Double.parseDouble(goalRequest.getGrowthRate()));
				if ((tenureZeroCheck != 0 && goalAmt != 0 && growthRateZeroCheck != 0)
						|| (tenureZeroCheck != 0 && goalAmt != 0) || (goalAmt != 0 && growthRateZeroCheck != 0)
						|| (growthRateZeroCheck != 0 && tenureZeroCheck != 0)) {
					if (tenureZeroCheck != 0 && goalAmt != 0 && growthRateZeroCheck != 0) {
						int tenure = 0;
						if (goalRequest.getTenureType().equals(CalcConstants.MONTH)) {
							tenure = (Integer.parseInt(goalRequest.getTenure())) / 12;
						} else {
							tenure = Integer.parseInt(goalRequest.getTenure());
						}
						double inflationRate = (Double.parseDouble(goalRequest.getInflationRate())) / 100;
						double currentAmt = Double.parseDouble(goalRequest.getCurrentAmount());
						double growthRate = (Double.parseDouble(goalRequest.getGrowthRate())) / 100;
						double returnRate = (Double.parseDouble(goalRequest.getGrowthRate()) / 100) / 12;
						double annualInvRate = (Double.parseDouble(goalRequest.getAnnualInvestmentRate())) / 100;

						// FutureCost
						double futureCost = calcService.calculateGoalFutureCost(goalAmt, inflationRate, tenure);
						// Current Investment
						double futureValue = calcService.calculateGoalCurrentInvestment(currentAmt, growthRate, tenure);
						// FinalCorpus
						double finalCorpus = calcService.calculateGoalFinalCorpus(futureCost, futureValue);
						// Monthly Required Investment
						double monthlyInv = calcService.calculateGoalMonthlyInvestment(growthRate, annualInvRate,
								finalCorpus, tenure, returnRate);
						// Yearly Required Investment
						double annualInv = calcService.calculateGoalAnnualyInvestment(growthRate, annualInvRate,
								finalCorpus, tenure);

						// Changing values from request to model by calling getValueGoalInfo
						Goal goal = getValueGoalInfo(goalRequest);
						goal.setReferenceId(goalRequest.getReferenceId());
						goal.setRateOfReturn(returnRate * 100);
						goal.setFinalCorpus(finalCorpus);
						goal.setFutureCost(roundingNumber(futureCost));
						goal.setFutureValue(roundingNumber(futureValue));
						goal.setMonthlyInv(roundingNumber(monthlyInv));
						goal.setAnnualInv(roundingNumber(annualInv));
						int result;
						if (token != null && referenceId != null) {
							logger.info("Fetching goal by referenceId and name");
							// if (calcService.fetchGoalByRefIdAndGoalName(referenceId,
							// goalRequest.getGoalName()) == null) {
							int goalById = calcService.checkGoalIsPresentByRefIdAndGoalName(referenceId,
									goalRequest.getGoalName());
							if (goalById == 0) {
								// If the goal is not available for the party, Then add as new record
								logger.info("Adding goal");
								result = calcService.addGoalInfo(goal);
							} else {
								// If the goal is already is present in table, update the goal
								logger.info("updating goal");
								result = calcService.updateGoalInfo(goal);
							}
							// Response
							// Fetch all goals by partyId
							if (result == 0) {
								// Return the response with all goals added by party
								logger.info("Error occurred while adding data");
								CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
										appMessages.getError_occured(), null, null);
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								logger.info("Fetching goal by referenceId");
								List<Goal> goalRes = calcService.fetchGoalByReferenceId(referenceId);
								CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
										appMessages.getGoal_calculated_successfully(), goalRes, roleFieldRights);
								return ResponseEntity.ok().body(response);
							}
						} else {
							logger.info("Fetching goal by referenceId");
							// List<Goal> goalRes = calcService.fetchGoalByReferenceId(referenceId);
							CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
									appMessages.getGoal_calculated_successfully(), goal, roleFieldRights);
							return ResponseEntity.ok().body(response);
						}
					} else {
						if (tenureZeroCheck == 0) {
							logger.info("Tenure Zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "TENURE", null, null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (goalAmt == 0) {
							logger.info("Goal amount zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "GOAL AMOUNT", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (growthRateZeroCheck == 0) {
							logger.info("Growth rate zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "GROWTH RATE", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					logger.info("Zero validation error");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
							appMessages.getZero_validation_error(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (errors.isEmpty() == false) {
				logger.info("Validation error");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		} else {
			// Return the validation errors
			logger.error("Some fields are empty");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getFields_cannot_be_empty(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Goal getValueGoalInfo(GoalRequest goalRequest) {
		Goal goal = new Goal();
		goal.setGoalName(goalRequest.getGoalName());
		goal.setTenure(Integer.parseInt(goalRequest.getTenure()));
		goal.setTenureType(goalRequest.getTenureType());
		goal.setGoalAmount(roundingNumber(Double.parseDouble(goalRequest.getGoalAmount())));
		goal.setInflationRate(Double.parseDouble(goalRequest.getInflationRate()));
		goal.setCurrentAmount(roundingNumber(Double.parseDouble(goalRequest.getCurrentAmount())));
		goal.setGrowthRate(Double.parseDouble(goalRequest.getGrowthRate()));
		goal.setAnnualInvestmentRate(Double.parseDouble(goalRequest.getAnnualInvestmentRate()));
		return goal;
	}

	// CashFlow
	/**
	 * Method to add cashflow to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>GoalRequest</code>
	 * @return ResponseEntity<> List<CashFlow>,CahsFlowSummary or Error Response
	 */
	@ApiOperation(value = "calculate cashflow", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateCashFlow", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateCashFlow(@NonNull @RequestBody CashFlowRequest cashFlowRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		if (token != null && cashFlowRequest.getReferenceId() != null) {
			List<RoleFieldRights> roleFieldRights = null;
			if (cashFlowRequest != null) {
				int screenId = cashFlowRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			String referenceId = cashFlowRequest.getReferenceId();
			// calcService.fetchPlanByReferenceId(referenceId) == null
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Validating cashFlow Request
				errors = cashFlowRequestValidator.validate(cashFlowRequest);
				if (errors.isEmpty() == true) {
					int result1 = 0;
					List<CashFlow> cashFlowList = getValueCashFlowInfo(cashFlowRequest);
					result1 = calcService.addAndModifyCashFlow(cashFlowList);
					if (result1 == 0) {
						logger.info("Error Occurred while adding data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
					// Recurring Expenditure
					List<Integer> recurringExpItemType = calcService.fetchRecurringExpenditureItemType();
					List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
					for (int typeId : recurringExpItemType) {
						cashFlowRecurringExp.addAll(calcService.fetchCashFlowByRefIdAndTypeId(referenceId, typeId));
					}
					double recurringExpenditure = 0;
					for (CashFlow cashFlow : cashFlowRecurringExp) {
						recurringExpenditure = recurringExpenditure + cashFlow.getActualAmt();
					}
					// Non-Recurring Expenditure
					// List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
					// cashFlowNonRecurringExp =
					// calcService.fetchNonRecurringExpenditureByRefId(referenceId);
					// double nonRecurringExpenditure = 0;
					// for (CashFlow cashFlow : cashFlowNonRecurringExp) {
					// nonRecurringExpenditure = nonRecurringExpenditure + cashFlow.getActualAmt();
					// }
					// Recurring Income
					List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
					cashFlowRecurringIncome = calcService.fetchRecurringIncomeByRefId(referenceId);
					double recurringIncome = 0;
					for (CashFlow cashFlow : cashFlowRecurringIncome) {
						recurringIncome = recurringIncome + cashFlow.getActualAmt();
					}
					// Non Recurring Income
					// List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
					// cashFlowNonRecurringIncome =
					// calcService.fetchNonRecurringIncomeByRefId(referenceId);
					// double nonRecurringIncome = 0;
					// for (CashFlow cashFlow : cashFlowNonRecurringIncome) {
					// nonRecurringIncome = nonRecurringIncome + cashFlow.getActualAmt();
					// }
					// CashFlow Recurring
					double cashFlowRecurring = 0;
					cashFlowRecurring = recurringIncome - recurringExpenditure;
					// CashFlow Non Recurring
					// double cashFlowNonRecurring = 0;
					// cashFlowNonRecurring = nonRecurringExpenditure - nonRecurringIncome;

					CashFlowSummary cashFlowSummary = new CashFlowSummary();
					cashFlowSummary.setMonthlyExpense(roundingNumber(recurringExpenditure));
					cashFlowSummary.setYearlyExpense(roundingNumber(recurringExpenditure * 12));
					// cashFlowSummary.setNonRecurExpense(roundingNumber(nonRecurringExpenditure));
					cashFlowSummary.setMonthlyIncome(roundingNumber(recurringIncome));
					cashFlowSummary.setYearlyIncome(roundingNumber(recurringIncome * 12));
					// cashFlowSummary.setNonRecurIncome(roundingNumber(nonRecurringIncome));
					cashFlowSummary.setMonthlyNetCashFlow(roundingNumber(cashFlowRecurring));
					cashFlowSummary.setYearlyNetCashFlow(roundingNumber(cashFlowRecurring * 12));
					// cashFlowSummary.setNonRecurCashflow(roundingNumber(cashFlowNonRecurring));
					cashFlowSummary.setReferenceId(referenceId);
					// Add cashflow summary into Table

					// cash flow summary
					// if cash flow summary is already present for this partyId then update it, or
					// add as new record
					int result2;
					logger.info("Fetching cashflow summary by referenceId");
					// if (calcService.fetchCashFlowSummaryByRefId(referenceId) != null) {
					int cashFlowSummaryByRefId = calcService.checkCashFlowSummaryIsPresent(referenceId);
					if (cashFlowSummaryByRefId != 0) {
						logger.info("updating cashFlowSummary");
						result2 = calcService.updateCashFlowSummary(cashFlowSummary);
					} else {
						logger.info("adding cashFlowSummary");
						result2 = calcService.addCashFlowSummary(cashFlowSummary);
					}
					if (result2 == 0) {
						logger.info("Error Occurred while adding data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured_adding_summary(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
					// Response
					// Response with all cashflow record for this partyId and also cashflow summary
					List<CashFlow> cashFlowListRes = fetchAllCashFlowByRefId(referenceId);
					CashFlowSummary cashFlowSummaryRes = calcService.fetchCashFlowSummaryByRefId(referenceId);

					CashFlowResponse cashFlowResponse = new CashFlowResponse();
					cashFlowResponse.setCashFlowList(cashFlowListRes);
					cashFlowResponse.setCashFlowSummary(cashFlowSummaryRes);

					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getCashflow_calculated_successfully(), cashFlowResponse, roleFieldRights);
					return ResponseEntity.ok().body(response);

				} else if (errors.isEmpty() == false) {
					// return validation errors
					logger.info("Validation errors");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} else if (token == null || cashFlowRequest.getReferenceId() == null) {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			errors = cashFlowRequestValidator.validate(cashFlowRequest);
			if (errors.isEmpty() == true) {
				List<Integer> recurringExpItemType = calcService.fetchRecurringExpenditureItemType();
				double recurringExpenditure = 0;
				double recurringIncome = 0;

				for (CashFlowItemReq cashFlowItemReq : cashFlowRequest.getCashFlowItemReq()) {
					int typeId = calcService.fetchCashFlowItemTypeIdByItemId(cashFlowItemReq.getCashFlowItemId());
					if (recurringExpItemType.contains(typeId)) {
						recurringExpenditure = recurringExpenditure
								+ Double.parseDouble(cashFlowItemReq.getActualAmt());
					} else {
						recurringIncome = recurringIncome + Double.parseDouble(cashFlowItemReq.getActualAmt());
					}
				}
				double cashFlowRecurring = 0;
				cashFlowRecurring = recurringIncome - recurringExpenditure;
				// CashFlow Non Recurring
				// double cashFlowNonRecurring = 0;
				// cashFlowNonRecurring = nonRecurringExpenditure - nonRecurringIncome;

				CashFlowSummary cashFlowSummary = new CashFlowSummary();
				cashFlowSummary.setMonthlyExpense(roundingNumber(recurringExpenditure));
				cashFlowSummary.setYearlyExpense(roundingNumber(recurringExpenditure * 12));
				// cashFlowSummary.setNonRecurExpense(roundingNumber(nonRecurringExpenditure));
				cashFlowSummary.setMonthlyIncome(roundingNumber(recurringIncome));
				cashFlowSummary.setYearlyIncome(roundingNumber(recurringIncome * 12));
				// cashFlowSummary.setNonRecurIncome(roundingNumber(nonRecurringIncome));
				cashFlowSummary.setMonthlyNetCashFlow(roundingNumber(cashFlowRecurring));
				cashFlowSummary.setYearlyNetCashFlow(roundingNumber(cashFlowRecurring * 12));
				// cashFlowSummary.setNonRecurCashflow(roundingNumber(cashFlowNonRecurring));

				List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
				List<CashFlowItem> cashFlowItemList = calcService.fetchCashFlowItemList();
				for (CashFlowItem cashFlowItem : cashFlowItemList) {
					CashFlowItemReq cashFlowItemReq = cashFlowRequest.getCashFlowItemReq().stream()
							.filter(cashflowreq -> cashFlowItem.getCashFlowItemId() == cashflowreq.getCashFlowItemId())
							.findAny().orElse(null);
					CashFlow cashFlow = new CashFlow();
					if (cashFlowItemReq != null) {
						cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
						cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
					}
					cashFlow.setDate(cashFlowRequest.getDate());
					cashFlow.setCashFlowItemId(cashFlowItem.getCashFlowItemId());
					cashFlow.setCashFlowItem(cashFlowItem.getCashFlowItem());
					cashFlow.setCashFlowItemTypeId(cashFlowItem.getCashFlowItemTypeId());
					CashFlowItemType cashFlowItemType = calcService
							.fetchCashFlowItemTypeByTypeId(cashFlowItem.getCashFlowItemTypeId());
					cashFlow.setCashFlowItemType(cashFlowItemType.getCashFlowItemType());
					cashFlowList.add(cashFlow);
				}
				CashFlowResponse cashFlowResponse = new CashFlowResponse();
				cashFlowResponse.setCashFlowList(cashFlowList);
				cashFlowResponse.setCashFlowSummary(cashFlowSummary);

				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
						appMessages.getCashflow_calculated_successfully(), cashFlowResponse, null);
				return ResponseEntity.ok().body(response);

			} else if (errors.isEmpty() == false) {
				// return validation errors
				logger.info("Validation errors");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private List<CashFlow> getValueCashFlowInfo(CashFlowRequest cashFlowRequest) {
		List<CashFlow> cashFlowList = new ArrayList<>();
		for (CashFlowItemReq cashFlowItemReq : cashFlowRequest.getCashFlowItemReq()) {
			CashFlow cashFlow = new CashFlow();
			if (cashFlowItemReq != null && cashFlowItemReq.getActualAmt() != null) {
				cashFlow.setActualAmt(roundingNumber(Double.parseDouble(cashFlowItemReq.getActualAmt())));
			}
			if (cashFlowItemReq != null && cashFlowItemReq.getBudgetAmt() != null) {
				cashFlow.setBudgetAmt(roundingNumber(Double.parseDouble(cashFlowItemReq.getBudgetAmt())));
			}
			if (cashFlowItemReq != null && cashFlowItemReq.getCashFlowItemId() != 0) {
				cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());
			}
			cashFlow.setReferenceId(cashFlowRequest.getReferenceId());
			cashFlow.setDate(cashFlowRequest.getDate());
			cashFlowList.add(cashFlow);
		}
		return cashFlowList;
	}

	// Networth
	/**
	 * Method to add Networth to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>NetworthRequest</code>
	 * @return ResponseEntity<> List<Networth>,NetworthSummary or Error Response
	 */
	@ApiOperation(value = "calculate Networth", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateNetworth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateNetworth(@NonNull @RequestBody NetworthRequest networthRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		if (token != null && networthRequest.getReferenceId() != null) {
			List<RoleFieldRights> roleFieldRights = null;
			if (networthRequest != null) {
				int screenId = networthRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			String referenceId = networthRequest.getReferenceId();
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int planByRefId = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (planByRefId == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Validating networth Request
				errors = networthRequestValidator.validate(networthRequest);
				if (errors.isEmpty() == true) {
					// Changing from request to model
					List<Networth> networthList = getValueNetwothInfo(networthRequest);
					int result1 = calcService.addAndModifyNetworth(networthList);
					if (result1 == 0) {
						logger.info("Error Occurred while adding data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
					List<Networth> networthAssetsList = calcService
							.fetchNetworthByAssets(networthRequest.getReferenceId());
					double totalCurrentAssets = 0;
					double totalFutureAssets = 0;
					// Calculate total assests for current and future
					for (Networth networth : networthAssetsList) {
						totalCurrentAssets = totalCurrentAssets + networth.getValue();
						totalFutureAssets = totalFutureAssets + networth.getFutureValue();
					}

					List<Networth> networthLiabilitiesList = calcService
							.fetchNetworthByLiabilities(networthRequest.getReferenceId());
					double totalCurrentLiabilities = 0;
					double totalFutureLiabilities = 0;
					// Calculate total liabilities for current and future
					for (Networth networth : networthLiabilitiesList) {
						totalCurrentLiabilities = totalCurrentLiabilities + networth.getValue();
						totalFutureLiabilities = totalFutureLiabilities + networth.getFutureValue();
					}
					// Calculate networth for current and future
					double currentNetworth = totalCurrentAssets - totalCurrentLiabilities;
					double futureNetworth = totalFutureAssets - totalFutureLiabilities;

					NetworthSummary networthSummary = new NetworthSummary();
					networthSummary.setReferenceId(networthRequest.getReferenceId());
					networthSummary.setCurrent_assetValue(roundingNumber(totalCurrentAssets));
					networthSummary.setCurrent_liability(roundingNumber(totalCurrentLiabilities));
					networthSummary.setFuture_assetValue(roundingNumber(totalFutureAssets));
					networthSummary.setFuture_liability(roundingNumber(totalFutureLiabilities));
					networthSummary.setNetworth(roundingNumber(currentNetworth));
					networthSummary.setFuture_networth(roundingNumber(futureNetworth));
					// Add networth summary
					// Networth Summary
					// If networth summary already present for partyId, then remove it and calculate
					// again
					int result2;
					logger.info("Fetching networth summary by referenceId");
					// if (calcService.fetchNetworthSummaryByRefId(referenceId) != null) {
					int networthSummaryByRefId = calcService.checkNetworthSummaryIsPresent(referenceId);
					if (networthSummaryByRefId != 0) {
						logger.info("updating networthSummary");
						result2 = calcService.updateNetworthSummary(networthSummary);
					} else {
						logger.info("adding networthSummary");
						result2 = calcService.addNetworthSummary(networthSummary);
					}
					if (result2 == 0) {
						logger.info("Error Occurred while adding data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured_adding_summary(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
					// Response
					// Response with all networth record for this partyId and also networth summary
					List<Networth> networthListRes = fetchAllNetworthByRefId(referenceId);
					NetworthSummary networthSummaryRes = calcService.fetchNetworthSummaryByRefId(referenceId);

					NetworthResponse networthResponse = new NetworthResponse();
					networthResponse.setNetworthList(networthListRes);
					networthResponse.setNetworthSummary(networthSummaryRes);
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getNetworth_calculated_successfully(), networthResponse, roleFieldRights);
					return ResponseEntity.ok().body(response);

				} else if (errors.isEmpty() == false) {
					logger.info("Validation errors");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		} else if (token == null || networthRequest.getReferenceId() == null) {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			errors = networthRequestValidator.validate(networthRequest);
			if (errors.isEmpty() == true) {

				double totalCurrentAssets = 0;
				double totalFutureAssets = 0;
				double totalCurrentLiabilities = 0;
				double totalFutureLiabilities = 0;
				// Calculate total assests for current and future
				for (NetworthReq networthReq : networthRequest.getNetworthReq()) {
					int typeIdAsset = calcService.fetchAccountTypeIdByAccountType(calcTableFields.getAssets());
					int typeIdLiability = calcService.fetchAccountTypeIdByAccountType(calcTableFields.getLiabilities());
					if (typeIdAsset == calcService.fetchAccountTypeIdByEntryId(networthReq.getAccountEntryId())) {
						totalCurrentAssets = totalCurrentAssets + Double.parseDouble(networthReq.getValue());
						if (networthReq.getFutureValue() != null) {
							totalFutureAssets = totalFutureAssets + Double.parseDouble(networthReq.getFutureValue());
						}
					} else if (typeIdLiability == calcService
							.fetchAccountTypeIdByEntryId(networthReq.getAccountEntryId())) {
						totalCurrentLiabilities = totalCurrentLiabilities + Double.parseDouble(networthReq.getValue());
						if (networthReq.getFutureValue() != null) {
							totalFutureLiabilities = totalFutureLiabilities
									+ Double.parseDouble(networthReq.getFutureValue());
						}
					}
				}
				double currentNetworth = totalCurrentAssets - totalCurrentLiabilities;
				double futureNetworth = totalFutureAssets - totalFutureLiabilities;

				NetworthSummary networthSummary = new NetworthSummary();
				networthSummary.setCurrent_assetValue(roundingNumber(totalCurrentAssets));
				networthSummary.setCurrent_liability(roundingNumber(totalCurrentLiabilities));
				networthSummary.setFuture_assetValue(roundingNumber(totalFutureAssets));
				networthSummary.setFuture_liability(roundingNumber(totalFutureLiabilities));
				networthSummary.setNetworth(roundingNumber(currentNetworth));
				networthSummary.setFuture_networth(roundingNumber(futureNetworth));
				List<Networth> networthList = new ArrayList<Networth>();
				List<Account> accountList = calcService.fetchAccountList();
				for (Account account : accountList) {
					NetworthReq networthReq = networthRequest.getNetworthReq().stream()
							.filter(networthreq -> account.getAccountEntryId() == networthreq.getAccountEntryId())
							.findAny().orElse(null);
					Networth networth1 = new Networth();
					if (networthReq != null) {
						networth1.setValue(Double.parseDouble(networthReq.getValue()));
						if (networthReq.getFutureValue() != null) {
							networth1.setFutureValue(Double.parseDouble(networthReq.getFutureValue()));
						}
					}
					networth1.setAccountEntryId(account.getAccountEntryId());
					networth1.setAccountEntry(account.getAccountEntry());
					networth1.setAccountTypeId(account.getAccountTypeId());
					AccountType accountType = calcService.fetchAccountTypeByTypeId(account.getAccountTypeId());
					networth1.setAccountType(accountType.getAccountType());
					networthList.add(networth1);
				}
				NetworthResponse networthResponse = new NetworthResponse();
				networthResponse.setNetworthList(networthList);
				networthResponse.setNetworthSummary(networthSummary);
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
						appMessages.getNetworth_calculated_successfully(), networthResponse, null);
				return ResponseEntity.ok().body(response);
			} else if (errors.isEmpty() == false) {
				logger.info("Validation errors");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private List<Networth> getValueNetwothInfo(NetworthRequest networthRequest) {
		List<Networth> networthList = new ArrayList<>();
		for (NetworthReq networthReq : networthRequest.getNetworthReq()) {
			Networth networth = new Networth();
			if (networthReq != null && networthReq.getAccountEntryId() != 0) {
				networth.setAccountEntryId(networthReq.getAccountEntryId());
			}
			if (networthReq != null && networthReq.getValue() != null) {
				networth.setValue(roundingNumber(Double.parseDouble(networthReq.getValue())));
			}
			if (networthReq != null && networthReq.getFutureValue() != null) {
				networth.setFutureValue(roundingNumber(Double.parseDouble(networthReq.getFutureValue())));
			}
			networth.setReferenceId(networthRequest.getReferenceId());
			networthList.add(networth);
		}
		return networthList;
	}

	// Priorities
	/**
	 * Method to add Priority to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>PriorityRequest</code>
	 * @return ResponseEntity<> List<Priority> or Error Response
	 */
	@ApiOperation(value = "calculate Priorities", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculatePriorities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculatePriorities(@NonNull @RequestBody PriorityRequest priorityRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		String referenceId = priorityRequest.getReferenceId();
		if (token != null && priorityRequest.getReferenceId() != null) {
			if (priorityRequest != null) {
				int screenId = priorityRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			// Validating priorityRequest
			List<Integer> priorityOrder = new ArrayList<Integer>();
			List<Priority> priorityList = new ArrayList<>();
			// Changing from request to model by calling getValuePriorityInfo
			List<Priority> priorityAddList = getValuePriorityInfo(priorityRequest);
			int result = calcService.addAndModifyPriorities(priorityAddList);
			if (result == 0) {
				logger.info("Error Occurred while adding priority");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			priorityList = calcService.fetchPriorityByRefId(referenceId);
			// sorting the priorityitems
			priorityOrder = getPriorityOrder(priorityList);
			// Set priority order and added into table
			int order = 0;
			int result2 = 0;
			for (int priorityItemId : priorityOrder) {
				// int priorityItemId = priorityEntry.getKey();
				order = order + 1;
				logger.info("updating priority order");
				result2 = calcService.updatePriorityOrder(referenceId, priorityItemId, order);
				if (result2 == 0) {
					logger.info("Error Occurred while updating priority order");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
							appMessages.getError_occured_update_priority_order(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			// Response
			// Response with all priority for this partyId
			logger.info("Fetching priority by referenceId");
			List<Priority> priorityListRes = fetchAllPriorityByRefId(referenceId);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getPriority_added_successfully(), priorityListRes, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else if (token == null || referenceId == null) {
			List<Integer> priorityOrder = new ArrayList<Integer>();
			List<Priority> priorityList = new ArrayList<>();
			List<Priority> priorityAddList = getValuePriorityInfo(priorityRequest);
			priorityList = priorityAddList;
			priorityOrder = getPriorityOrder(priorityList);
			int order = 0;
			for (int priorityItemId : priorityOrder) {
				Priority priority = priorityList.stream()
						.filter(priorityone -> priorityItemId == priorityone.getPriorityItemId()).findAny()
						.orElse(null);
				order = order + 1;
				priority.setPriorityOrder(order);
			}
			// Response setup
			List<Priority> priorityResList = new ArrayList<Priority>();
			List<PriorityItem> priorityItemList = calcService.fetchPriorityItemList();
			for (PriorityItem priorityItem : priorityItemList) {
				Priority priority = priorityList.stream()
						.filter(priorityone -> priorityItem.getPriorityItemId() == priorityone.getPriorityItemId())
						.findAny().orElse(null);
				Priority priority1 = new Priority();
				if (priority != null) {
					priority1.setPriorityOrder(priority.getPriorityOrder());
					List<Urgency> urgencyList = calcService.fetchUrgencyList();
					Urgency urgency = urgencyList.stream()
							.filter(urgencyone -> priority.getUrgencyId() == urgencyone.getUrgencyId()).findAny()
							.orElse(null);
					priority1.setUrgencyId(priority.getUrgencyId());
					priority1.setUrgency(urgency.getValue());
				}
				priority1.setPriorityItemId(priorityItem.getPriorityItemId());
				priority1.setPriorityItem(priorityItem.getPriorityItem());
				priorityResList.add(priority1);
			}
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getPriority_added_successfully(), priorityResList, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private List<Integer> getPriorityOrder(List<Priority> priorityList) {
		List<Integer> priorityOrder = new ArrayList<Integer>();
		// Priority Order
		// Order the priority by urgency and timeline
		HashMap<Integer, Integer> itemIdAndUrgency = new HashMap<>();
		for (Priority priority : priorityList) {
			itemIdAndUrgency.put(priority.getPriorityItemId(), priority.getUrgencyId());
		}
		// first order by urgency
		// fetch priority for urgency
		LinkedHashMap<Integer, Integer> sortedByUrgency = new LinkedHashMap<Integer, Integer>();
		itemIdAndUrgency.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEachOrdered(x -> sortedByUrgency.put(x.getKey(), x.getValue()));
		Iterator<Map.Entry<Integer, Integer>> iterator = sortedByUrgency.entrySet().iterator();
		// if one or more urgency is same then order by alphabetical order of
		// priorityItem
		while (iterator.hasNext()) {
			Map.Entry<Integer, Integer> entryForUrgency = iterator.next();
			List<Integer> urgencyKeys = new ArrayList<>();
			for (Entry<Integer, Integer> entryKey : sortedByUrgency.entrySet()) {
				if (Objects.equals(entryForUrgency.getValue(), entryKey.getValue())) {
					urgencyKeys.add(entryKey.getKey());
				}
			}
			// fetch priorityItem by priorityItemId
			HashMap<Integer, String> itemIdAndItem = new HashMap<>();
			for (int itemId : urgencyKeys) {
				PriorityItem priorityItem = calcService.fetchPriorityItemByItemId(itemId);
				itemIdAndItem.put(priorityItem.getPriorityItemId(), priorityItem.getPriorityItem());
			}
			// sorting by priorityItem
			LinkedHashMap<Integer, String> sortedByItemAlphabet = new LinkedHashMap<Integer, String>();
			itemIdAndItem.entrySet().stream().sorted(Map.Entry.comparingByValue())
					.forEachOrdered(x -> sortedByItemAlphabet.put(x.getKey(), x.getValue()));

			for (Entry<Integer, String> entryByItemAlphabet : sortedByItemAlphabet.entrySet()) {
				priorityOrder.add(entryByItemAlphabet.getKey());
			}
			for (int i = 1; i < urgencyKeys.size(); i++) {
				iterator.next();
			}
		}
		return priorityOrder;
	}

	private List<Priority> getValuePriorityInfo(PriorityRequest priorityRequest) {
		List<Priority> priorityList = new ArrayList<>();
		for (PriorityReq priorityReq : priorityRequest.getPriorityReq()) {
			if (priorityReq.getPriorityItemId() != 0 && priorityReq.getUrgencyId() != 0) {
				Priority priority = new Priority();
				priority.setPriorityItemId(priorityReq.getPriorityItemId());
				// priority.setTimeLine(Integer.parseInt(priorityReq.getTimeline()));
				// priority.setValue(roundingNumber(Double.parseDouble(priorityReq.getValue())));
				priority.setUrgencyId(priorityReq.getUrgencyId());
				priority.setReferenceId(priorityRequest.getReferenceId());
				priorityList.add(priority);
			}
		}
		return priorityList;
	}

	// Insurance
	/**
	 * Method to add Insurance to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>InsuranceRequest</code>
	 * @return ResponseEntity<> Insurance or Error Response
	 */
	@ApiOperation(value = "calculate Insurance", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateInsurance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateInsurance(@NonNull @RequestBody InsuranceRequest insuranceRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		if (token != null && insuranceRequest.getReferenceId() != null) {
			List<RoleFieldRights> roleFieldRights = null;
			if (insuranceRequest != null) {
				int screenId = insuranceRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			String referenceId = insuranceRequest.getReferenceId();
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Validate insurance request
				errors = insuranceRequestValidator.validate(insuranceRequest);
				if (errors.isEmpty() == true) {
					// Changing from request to model
					Insurance insurance = getValueInsuranceInfo(insuranceRequest);
					double requiredInsurance = 0;
					double annualIncome = Double.parseDouble(insuranceRequest.getAnnualIncome());
					double existingInsurance = Double.parseDouble(insuranceRequest.getExistingInsurance());

					if (insuranceRequest.getStability().equals(CalcConstants.STABLE)
							&& insuranceRequest.getPredictability().equals(CalcConstants.PREDICTABLE)) {
						requiredInsurance = annualIncome * 10;
					} else if (insuranceRequest.getStability().equals(CalcConstants.STABLE)
							&& insuranceRequest.getPredictability().equals(CalcConstants.UNPREDICTABLE)) {
						requiredInsurance = annualIncome * 15;
					} else if (insuranceRequest.getStability().equals(CalcConstants.FLUCTUATING)
							&& insuranceRequest.getPredictability().equals(CalcConstants.PREDICTABLE)) {
						requiredInsurance = annualIncome * 10;
					} else if (insuranceRequest.getStability().equals(CalcConstants.FLUCTUATING)
							&& insuranceRequest.getPredictability().equals(CalcConstants.UNPREDICTABLE)) {
						requiredInsurance = annualIncome * 15;
					}
					double additionalInsurance = requiredInsurance - existingInsurance;

					insurance.setReferenceId(referenceId);
					insurance.setRequiredInsurance(roundingNumber(requiredInsurance));
					insurance.setAdditionalInsurance(roundingNumber(additionalInsurance));
					int result = 0;
					logger.info("Fetching insurance by referenceId");
					// if (calcService.fetchInsuranceByRefId(referenceId) == null) {
					int insuranceByRefId = calcService.checkInsuranceByRefId(referenceId);
					if (insuranceByRefId == 0) {
						// If Insuarnce is not present then add as new record
						logger.info("adding insurance");
						result = calcService.addInsurance(insurance);
					} else {
						// If Insurance is already present for this partyId, then update it
						logger.info("updating insurance");
						result = calcService.updateInsurance(insurance);
					}
					if (result == 0) {
						logger.info("Error Occurred while adding data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						// Response
						// response with insurance added by this partyId
						InsuranceItem insuranceRes = calcService.fetchInsuranceItemByRefId(referenceId);
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getInsurance_calculated_successfully(), insuranceRes, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else if (errors.isEmpty() == false) {
					// return validation errors
					logger.info("Validation errors");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		} else if (token == null || insuranceRequest.getReferenceId() == null) {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			errors = insuranceRequestValidator.validate(insuranceRequest);
			if (errors.isEmpty() == true) {
				// Changing from request to model
				Insurance insurance = getValueInsuranceInfo(insuranceRequest);
				double requiredInsurance = 0;
				double annualIncome = Double.parseDouble(insuranceRequest.getAnnualIncome());
				double existingInsurance = Double.parseDouble(insuranceRequest.getExistingInsurance());

				if (insuranceRequest.getStability().equals(CalcConstants.STABLE)
						&& insuranceRequest.getPredictability().equals(CalcConstants.PREDICTABLE)) {
					requiredInsurance = annualIncome * 10;
				} else if (insuranceRequest.getStability().equals(CalcConstants.STABLE)
						&& insuranceRequest.getPredictability().equals(CalcConstants.UNPREDICTABLE)) {
					requiredInsurance = annualIncome * 15;
				} else if (insuranceRequest.getStability().equals(CalcConstants.FLUCTUATING)
						&& insuranceRequest.getPredictability().equals(CalcConstants.PREDICTABLE)) {
					requiredInsurance = annualIncome * 10;
				} else if (insuranceRequest.getStability().equals(CalcConstants.FLUCTUATING)
						&& insuranceRequest.getPredictability().equals(CalcConstants.UNPREDICTABLE)) {
					requiredInsurance = annualIncome * 15;
				}
				double additionalInsurance = requiredInsurance - existingInsurance;
				insurance.setRequiredInsurance(roundingNumber(requiredInsurance));
				insurance.setAdditionalInsurance(roundingNumber(additionalInsurance));
				InsuranceItem insuranceRes = calcService.fetchInsuranceItemWithoutLogin(insurance);
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
						appMessages.getInsurance_calculated_successfully(), insuranceRes, null);
				return ResponseEntity.ok().body(response);
			} else if (errors.isEmpty() == false) {
				logger.info("Validation errors");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Insurance getValueInsuranceInfo(InsuranceRequest insuranceRequest) {
		Insurance insurance = new Insurance();
		insurance.setAnnualIncome(roundingNumber(Double.parseDouble(insuranceRequest.getAnnualIncome())));
		insurance.setStability(insuranceRequest.getStability());
		insurance.setPredictability(insuranceRequest.getPredictability());
		insurance.setExistingInsurance(roundingNumber(Double.parseDouble(insuranceRequest.getExistingInsurance())));
		return insurance;
	}

	// Risk Profile
	/**
	 * Method to add RiskProfile to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the <code>RiskProfileRequest</code>
	 * @return ResponseEntity<> RiskProfile or Error Response
	 */
	@ApiOperation(value = "calculate RiskProfile", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateRiskProfile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateRiskProfile(@NonNull @RequestBody RiskProfileRequest riskProfileRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		String referenceId = riskProfileRequest.getReferenceId();
		List<RoleFieldRights> roleFieldRights = null;
		if (riskProfileRequest.getRiskProfileReq().size() == 0) {
			CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getFields_empty());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (token != null && riskProfileRequest.getReferenceId() != null) {
			roleFieldRights = null;
			if (riskProfileRequest != null) {
				int screenId = riskProfileRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			referenceId = riskProfileRequest.getReferenceId();
			logger.info("Fetching plan by referenceId");
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			// else {
			// Changing from request to model
			List<RiskProfile> riskProfileAddList = getValueRiskProfileInfo(riskProfileRequest);
			int result = calcService.addAndModifyRiskProfile(riskProfileAddList);
			if (result == 0) {
				logger.info("Error Occurred while adding data");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// calculate risksummary
			// logger.info("fetching riskProfile by referenceId");
			riskProfileList = calcService.fetchRiskProfileByRefId(referenceId);
		} else if (token == null || riskProfileRequest.getReferenceId() == null) {
			riskProfileList = getValueRiskProfileInfo(riskProfileRequest);
			for (RiskProfile riskprofile : riskProfileList) {
				riskprofile.setScore(calcService.fetchScoreByAnswerId(riskprofile));
			}

		}
		int score = 0;
		for (RiskProfile riskProfile : riskProfileList) {
			score = score + riskProfile.getScore();
		}
		String points = "";
		if (score <= 30) {
			points = calcTableFields.getThirty_or_less();
		} else if (score >= 31 && score <= 40) {
			points = calcTableFields.getThirtyone_to_fourty();
		} else if (score >= 41 && score <= 51) {
			points = calcTableFields.getFourtyone_to_fiftyone();
		} else if (score >= 52 && score <= 61) {
			points = calcTableFields.getFiftytwo_to_sixtyone();
		} else if (score >= 62) {
			points = calcTableFields.getSixtytwo_or_more();
		}
		RiskPortfolio riskPortFolio = calcService.fetchRiskPortfolioByPoints(points);
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId(referenceId);
		riskSummary.setBehaviour(riskPortFolio.getBehaviour());
		riskSummary.setEqty_alloc(riskPortFolio.getEquity());
		riskSummary.setDebt_alloc(riskPortFolio.getDebt());
		riskSummary.setCash_alloc(riskPortFolio.getCash());
		// RiskProfile Summary
		// If risk summary is already present then update it, or add as new record
		int result2;
		if (token != null && riskProfileRequest.getReferenceId() != null) {
			logger.info("fetching risksummary by referenceId");

			int riskSummaryByRefId = calcService.checkRiskSummaryIsPresent(referenceId);
			if (riskSummaryByRefId != 0) {
				logger.info("updating riskSummary");
				result2 = calcService.updateRiskSummary(riskSummary);
			} else {
				logger.info("adding riskSummary");
				result2 = calcService.addRiskSummary(riskSummary);
			}
			if (result2 == 0) {
				logger.info("Error Occurred while adding data");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
						appMessages.getError_occured_adding_summary(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// Response
			List<RiskProfile> riskProfileListRes = calcService.fetchRiskProfileByRefId(referenceId);
			RiskSummary riskSummaryRes = calcService.fetchRiskSummaryByRefId(referenceId);

			// Return response with all risk profile and risk summary for this partyId
			RiskProfileResponse riskProfileResponse = new RiskProfileResponse();
			riskProfileResponse.setRiskProfileList(riskProfileListRes);
			riskProfileResponse.setRiskSummary(riskSummaryRes);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getRiskprofile_added_successfully(), riskProfileResponse, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else if (token == null || riskProfileRequest.getReferenceId() == null) {
			// logger.info("inside");
			RiskProfileResponse riskProfileResponse = new RiskProfileResponse();
			riskProfileResponse.setRiskProfileList(riskProfileList);
			riskProfileResponse.setRiskSummary(riskSummary);

			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getRiskprofile_added_successfully(), riskProfileResponse, null);
			return ResponseEntity.ok().body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private List<RiskProfile> getValueRiskProfileInfo(RiskProfileRequest riskProfileRequest) {
		// System.out.println("req size " +
		// riskProfileRequest.getRiskProfileReq().size());
		List<RiskProfile> riskProfileList = new ArrayList<>();
		for (RiskProfileReq riskProfileReq : riskProfileRequest.getRiskProfileReq()) {
			RiskProfile riskProfile = new RiskProfile();
			riskProfile.setQuestionId(riskProfileReq.getQuestionId());
			riskProfile.setAnswerId(riskProfileReq.getAnswerId());
			riskProfile.setReferenceId(riskProfileRequest.getReferenceId());
			// System.out.println("get value");
			riskProfileList.add(riskProfile);
		}
		return riskProfileList;
	}

	private double roundingNumber(double value) {
		String str = String.format("%.2f", value);
		double roundedValue = Double.valueOf(str);
		return roundedValue;
	}

	// INVESTMENT PLANNING
	// Future Value
	/**
	 * Method to calculate future value
	 * 
	 * @param RequestBody
	 *            contains the <code>FutureValueRequest</code>
	 * @return ResponseEntity<> totalPayment or Error Response
	 */
	@ApiOperation(value = "calculate FutureValue", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/IP-FutureValue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateFutureValue(@NonNull @RequestBody FutureValueRequest futureValueRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		// Set Investment Type default to LUMSUM -- remove the line if SIP calculation
		// need
		futureValueRequest.setInvType(CalcConstants.LUMSUM);
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = futureValueRequest.getReferenceId();
		if (token != null && referenceId != null) {
			if (futureValueRequest != null) {
				int screenId = futureValueRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// Validating futureValue Request
		errors = futureValueRequestValidator.validate(futureValueRequest);
		if (errors.isEmpty() == true) {
			int durationZeroCheck = (Integer.parseInt(futureValueRequest.getDuration()));
			double invAmount = Double.parseDouble(futureValueRequest.getInvAmount());
			double annualGrowthZeroCheck = (Double.parseDouble(futureValueRequest.getAnnualGrowth()));
			if ((durationZeroCheck != 0 && invAmount != 0 && annualGrowthZeroCheck != 0)
					|| (durationZeroCheck != 0 && invAmount != 0) || (invAmount != 0 && annualGrowthZeroCheck != 0)
					|| (annualGrowthZeroCheck != 0 && durationZeroCheck != 0)) {
				if (durationZeroCheck != 0 && invAmount != 0 && annualGrowthZeroCheck != 0) {
					TotalValueResponse totResponse = new TotalValueResponse();
					// If invType is LUMSUM
					double annualGrowth = (Double.parseDouble(futureValueRequest.getAnnualGrowth())) / 100;
					int duration = 0;
					double totalPayment = 0;
					if (futureValueRequest.getDurationType().equals(CalcConstants.MONTH)) {
						duration = (Integer.parseInt(futureValueRequest.getDuration())) / 12;
					} else {
						duration = Integer.parseInt(futureValueRequest.getDuration());
					}
					// double yearlyIncrease =
					// (Double.parseDouble(futureValueRequest.getYearlyIncrease())) / 100;
					if (futureValueRequest.getInvType().equals(CalcConstants.LUMSUM)) {
						// double totalPayment = invAmount
						// * (Math.pow((1 + annualGrowth), duration) - Math.pow((1 + yearlyIncrease),
						// duration))
						// / (annualGrowth - yearlyIncrease);
						// totResponse.setTotalPayment(roundingNumber(totalPayment));
						totalPayment = invAmount * (Math.pow((1 + annualGrowth), duration));
						totResponse.setTotalPayment(roundingNumber(totalPayment));
					} else if (futureValueRequest.getInvType().equals(CalcConstants.SIP)) {
						// If invType is SIP
						// double invAmount = Double.parseDouble(futureValueRequest.getInvAmount());
						// double annualGrowth =
						// (Double.parseDouble(futureValueRequest.getAnnualGrowth())) / 100;
						// int duration = Integer.parseInt(futureValueRequest.getDuration());
						// double yearlyIncrease =
						// (Double.parseDouble(futureValueRequest.getYearlyIncrease())) / 100;
						// double totalPayment = invAmount
						// * (Math.pow((1 + annualGrowth), duration) - Math.pow((1 + yearlyIncrease),
						// duration))
						// / (annualGrowth - yearlyIncrease);
						// totResponse.setTotalPayment(roundingNumber(totalPayment));
						totalPayment = invAmount * (Math.pow((1 + annualGrowth), duration));
						totResponse.setTotalPayment(roundingNumber(totalPayment));
					}
					if (token != null && referenceId != null) {
						// Add into table
						FutureValue futureValue = getValueFutureValueInfo(futureValueRequest);
						futureValue.setReferenceId(futureValueRequest.getReferenceId());
						futureValue.setTotalPayment(totalPayment);
						int futureValueCalc = calcService
								.checkFutureValueIsPresent(futureValueRequest.getReferenceId());
						int result = 0;
						if (futureValueCalc == 0) {
							// If FutureValue is not present with this referenceId, then add as new record
							logger.info("adding FutureValue");
							result = calcService.addFutureValue(futureValue);
						} else {
							// If FutureValue is present with this partyId, then update it
							logger.info("updating FutureValue");
							result = calcService.updateFutureValue(futureValue);
						}
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getValue_calculated_successfully(), totResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);

					} else {
						// Response with result total response
						logger.info("Calculating future value");
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getValue_calculated_successfully(), totResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else {
					if (durationZeroCheck == 0) {
						logger.info("Duration Zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "DURATION", null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (invAmount == 0) {
						logger.info("Investment amount zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "INVESTMENT AMOUNT", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (annualGrowthZeroCheck == 0) {
						logger.info("Annual growth rate zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "ANNUAL GROWTH RATE",
								null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Zero validation error");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
						appMessages.getZero_validation_error(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// // Future Value Annuity
	// @RequestMapping(value = "/IP-FutureValue-SIP", method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> calculateFutureValueAnnuity(
	// @RequestBody FutureValueAnnuityRequest futureValueAnnuityRequest) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// errors =
	// futureValueAnnuityRequestValidator.validate(futureValueAnnuityRequest);
	// if (errors.isEmpty() == true) {
	// double invAmount =
	// Double.parseDouble(futureValueAnnuityRequest.getInvAmount());
	// double annualGrowth =
	// (Double.parseDouble(futureValueAnnuityRequest.getAnnualGrowth())) / 100;
	// int duration = Integer.parseInt(futureValueAnnuityRequest.getDuration());
	// double yearlyIncrease =
	// (Double.parseDouble(futureValueAnnuityRequest.getYearlyIncrease())) / 100;
	// double totalPayment = invAmount
	// * (Math.pow((1 + annualGrowth), duration) - Math.pow((1 + yearlyIncrease),
	// duration))
	// / (annualGrowth - yearlyIncrease);
	// TotalValueResponse totResponse = new TotalValueResponse();
	// totResponse.setTotalPayment(roundingNumber(totalPayment));
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.SUCCESS_CODE);
	// responseMessage.setResponseDescription(AppMessages.VALUE_CALCULATED_SUCCESSFULLY);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(totResponse);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return ResponseEntity.ok().body(response);
	//
	// } else if (errors.isEmpty() == false) {
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.ERROR_CODE);
	// responseMessage.setResponseDescription(AppMessages.ERROR);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(errors);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	//
	// return new ResponseEntity<String>(HttpStatus.OK);
	//
	// }
	//
	// // Future Value Annuity Due
	// @RequestMapping(value = "/calculateFutureValueAnnuityDue", method =
	// RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes =
	// MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> calculateFutureValueAnnuityDue(
	// @RequestBody FutureValueAnnuityDueRequest futureValueAnnuityDueRequest) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// errors =
	// futureValueAnnuityDueRequestValidator.validate(futureValueAnnuityDueRequest);
	// if (errors.isEmpty() == true) {
	// double invAmount =
	// Double.parseDouble(futureValueAnnuityDueRequest.getInvAmount());
	// double annualGrowth =
	// (Double.parseDouble(futureValueAnnuityDueRequest.getAnnualGrowth())) / 100;
	// int duration = Integer.parseInt(futureValueAnnuityDueRequest.getDuration());
	// double yearlyIncrease =
	// (Double.parseDouble(futureValueAnnuityDueRequest.getYearlyIncrease())) / 100;
	// double totalPayment = invAmount
	// * (Math.pow((1 + annualGrowth), duration) - Math.pow((1 + yearlyIncrease),
	// duration))
	// / (annualGrowth - yearlyIncrease);
	// TotalValueResponse totResponse = new TotalValueResponse();
	// totResponse.setTotalPayment(roundingNumber(totalPayment));
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.SUCCESS_CODE);
	// responseMessage.setResponseDescription(AppMessages.VALUE_CALCULATED_SUCCESSFULLY);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(totResponse);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return ResponseEntity.ok().body(response);
	// } else if (errors.isEmpty() == false) {
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.ERROR_CODE);
	// responseMessage.setResponseDescription(AppMessages.ERROR);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(errors);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	//
	// return new ResponseEntity<String>(HttpStatus.OK);
	//
	// }

	private FutureValue getValueFutureValueInfo(FutureValueRequest futureValueRequest) {
		FutureValue futureValue = new FutureValue();
		if (futureValueRequest != null && futureValueRequest.getInvAmount() != null) {
			futureValue.setInvAmount(roundingNumber(Double.parseDouble(futureValueRequest.getInvAmount())));
		}
		if (futureValueRequest != null && futureValueRequest.getInvType() != null) {
			futureValue.setInvType(futureValueRequest.getInvType());
		}

		if (futureValueRequest != null && futureValueRequest.getDuration() != null) {
			futureValue.setDuration(Integer.parseInt(futureValueRequest.getDuration()));
		}

		if (futureValueRequest != null && futureValueRequest.getDurationType() != null) {
			futureValue.setDurationType(futureValueRequest.getDurationType());
		}

		if (futureValueRequest != null && futureValueRequest.getAnnualGrowth() != null) {
			futureValue.setAnnualGrowth(roundingNumber(Double.parseDouble(futureValueRequest.getAnnualGrowth())));
		}

		return futureValue;
	}

	// Target Value
	/**
	 * Method to calculate TargetValue
	 * 
	 * @param RequestBody
	 *            contains the <code>TargetValueRequest</code>
	 * @return ResponseEntity<> totalPayment or Error Response
	 */
	@ApiOperation(value = "calculate TargetValue", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/IP-TargetValue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateTargetValue(@NonNull @RequestBody TargetValueRequest targetValueRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		// Set Investment Type default to LUMSUM -- remove the line if SIP calculation
		// need
		targetValueRequest.setInvType(CalcConstants.LUMSUM);
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = targetValueRequest.getReferenceId();
		if (token != null && referenceId != null) {
			if (targetValueRequest != null) {
				int screenId = targetValueRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// Vallidating target value request
		errors = targetValueRequestValidator.validate(targetValueRequest);
		double annuity = 0;
		if (errors.isEmpty() == true) {
			double futureValueZeroCheck = Double.parseDouble(targetValueRequest.getFutureValue());
			double rateOfIntrestZeroCheck = Double.parseDouble(targetValueRequest.getRateOfInterest());
			double durationZeroCheck = (Integer.parseInt(targetValueRequest.getDuration()));
			if ((futureValueZeroCheck != 0 && rateOfIntrestZeroCheck != 0 && durationZeroCheck != 0)
					|| (futureValueZeroCheck != 0 && rateOfIntrestZeroCheck != 0)
					|| (rateOfIntrestZeroCheck != 0 && durationZeroCheck != 0)
					|| (durationZeroCheck != 0 && futureValueZeroCheck != 0)) {
				if (futureValueZeroCheck != 0 && rateOfIntrestZeroCheck != 0 && durationZeroCheck != 0) {
					TotalValueResponse totResponse = new TotalValueResponse();
					// If invType is LUMSUM
					if (targetValueRequest.getInvType().equals(CalcConstants.LUMSUM)) {
						double futureValue = Double.parseDouble(targetValueRequest.getFutureValue());
						double rateOfIntrest = Double.parseDouble(targetValueRequest.getRateOfInterest()) / 100;
						int duration;
						if (targetValueRequest.getDurationType().equals(CalcConstants.MONTH)) {
							duration = (Integer.parseInt(targetValueRequest.getDuration())) / 12;
						} else {
							duration = Integer.parseInt(targetValueRequest.getDuration());
						}
						double totalPayment = futureValue / (Math.pow((1 + rateOfIntrest), duration));
						totResponse.setTotalPayment(roundingNumber(totalPayment));
					} else if (targetValueRequest.getInvType().equals(CalcConstants.SIP)) {
						// If invType is SIP
						double periodicAmt = Double.parseDouble(targetValueRequest.getFutureValue());
						double growthRate = Double.parseDouble(targetValueRequest.getRateOfInterest()) / 100;
						int duration = Integer.parseInt(targetValueRequest.getDuration());
						double durationInMonth;
						if (targetValueRequest.getDurationType().equals(CalcConstants.MONTH)) {
							durationInMonth = duration;
						} else {
							durationInMonth = duration * 12;
						}
						double monthlyGrowthRate = growthRate / 12;
						annuity = periodicAmt * ((1 - (Math.pow((1 + monthlyGrowthRate), (-durationInMonth)))))
								/ monthlyGrowthRate;
						totResponse.setTotalPayment(roundingNumber(annuity));
					}
					if (token != null && referenceId != null) {
						// Add into table
						TargetValue targetValue = getValueTargetValueInfo(targetValueRequest);
						targetValue.setReferenceId(targetValueRequest.getReferenceId());
						targetValue.setTotalPayment(totResponse.getTotalPayment());
						int targetValueCalc = calcService.checkTargetValueIsPresent(referenceId);
						int result = 0;
						if (targetValueCalc == 0) {
							// If TargetValue is not present with this referenceId, then add as new record
							logger.info("adding TargetValue");
							result = calcService.addTargetValue(targetValue);
						} else {
							// If TargetValue is present with this partyId, then update it
							logger.info("updating TargetValue");
							result = calcService.updateTargetValue(targetValue);
						}
						// Response with total payment result - WithLogin
						logger.info("Calculating target value");
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getValue_calculated_successfully(), totResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						// Response with total payment result - WithOutLogin
						logger.info("Calculating target value");
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getValue_calculated_successfully(), totResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else {
					if (durationZeroCheck == 0) {
						logger.info("Duration Zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "DURATION", null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (futureValueZeroCheck == 0) {
						logger.info("Future value zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "FUTURE VALUE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (rateOfIntrestZeroCheck == 0) {
						logger.info("Interest rate zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Zero validation error");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
						appMessages.getZero_validation_error(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// // Present value Annuity
	// @RequestMapping(value = "/IP-TargetValue-SIP", method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> calculatePresentValueAnnuity(
	// @RequestBody PresentValueAnnuityRequest presentValueAnnuityRequest) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// errors =
	// presentValueAnnuityRequestValidator.validate(presentValueAnnuityRequest);
	// if (errors.isEmpty() == true) {
	// double periodicAmt =
	// Double.parseDouble(presentValueAnnuityRequest.getPeriodicAmount());
	// double growthRate =
	// Double.parseDouble(presentValueAnnuityRequest.getAnnualGrowthRate()) / 100;
	// double duration =
	// Double.parseDouble(presentValueAnnuityRequest.getDuration());
	// double monthlyGrowthRate = growthRate / 12;
	// double durationInMonth = duration * 12;
	// double annuity = periodicAmt * ((1 - (Math.pow((1 + monthlyGrowthRate),
	// (-durationInMonth)))))
	// / monthlyGrowthRate;
	// TotalValueResponse totResponse = new TotalValueResponse();
	// totResponse.setTotalPayment(roundingNumber(annuity));
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.SUCCESS_CODE);
	// responseMessage.setResponseDescription(AppMessages.VALUE_CALCULATED_SUCCESSFULLY);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(totResponse);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return ResponseEntity.ok().body(response);
	// } else if (errors.isEmpty() == false) {
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.ERROR_CODE);
	// responseMessage.setResponseDescription(AppMessages.ERROR);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(errors);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	//
	// return new ResponseEntity<String>(HttpStatus.OK);
	//
	// }
	//
	// // Present Value Annuity Due
	// @RequestMapping(value = "/calculatePresentValueAnnuityDue", method =
	// RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes =
	// MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> calculatePresentValueDueAnnuity(
	// @RequestBody PresentValueAnnuityDueRequest presentValueAnnuityDueRequest) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// errors =
	// presentValueAnnuityDueReqValidator.validate(presentValueAnnuityDueRequest);
	// if (errors.isEmpty() == true) {
	// double periodicAmt =
	// Double.parseDouble(presentValueAnnuityDueRequest.getPeriodicAmount());
	// double growthRate =
	// Double.parseDouble(presentValueAnnuityDueRequest.getAnnualGrowthRate()) /
	// 100;
	// double duration =
	// Double.parseDouble(presentValueAnnuityDueRequest.getDuration());
	// double monthlyGrowthRate = growthRate / 12;
	// double durationInMonth = duration * 12;
	// double annuityDue = periodicAmt
	// * ((1 - Math.pow((1 + monthlyGrowthRate), (-durationInMonth))) /
	// monthlyGrowthRate)
	// * (1 + monthlyGrowthRate);
	// TotalValueResponse totResponse = new TotalValueResponse();
	// totResponse.setTotalPayment(roundingNumber(annuityDue));
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.SUCCESS_CODE);
	// responseMessage.setResponseDescription(AppMessages.VALUE_CALCULATED_SUCCESSFULLY);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(totResponse);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return ResponseEntity.ok().body(response);
	// } else if (errors.isEmpty() == false) {
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.ERROR_CODE);
	// responseMessage.setResponseDescription(AppMessages.ERROR);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(errors);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	//
	// return new ResponseEntity<String>(HttpStatus.OK);
	//
	// }

	private TargetValue getValueTargetValueInfo(TargetValueRequest targetValueRequest) {
		TargetValue targetValue = new TargetValue();
		if (targetValueRequest != null && targetValueRequest.getFutureValue() != null) {
			targetValue.setFutureValue(roundingNumber(Double.parseDouble(targetValueRequest.getFutureValue())));
		}
		if (targetValueRequest != null && targetValueRequest.getInvType() != null) {
			targetValue.setInvType(targetValueRequest.getInvType());
		}

		if (targetValueRequest != null && targetValueRequest.getRateOfInterest() != null) {
			targetValue.setRateOfInterest(roundingNumber(Double.parseDouble(targetValueRequest.getRateOfInterest())));
		}

		if (targetValueRequest != null && targetValueRequest.getDuration() != null) {
			targetValue.setDuration(Integer.parseInt(targetValueRequest.getDuration()));
		}

		if (targetValueRequest != null && targetValueRequest.getDurationType() != null) {
			targetValue.setDurationType(targetValueRequest.getDurationType());
		}

		return targetValue;
	}

	// RATE FINDER
	/**
	 * Method to calculate RateFinderRequest
	 * 
	 * @param RequestBody
	 *            contains the <code>RateFinderRequest</code>
	 * @return ResponseEntity<> rateOfInterest or Error Response
	 */
	@ApiOperation(value = "rate Finder", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/IP-RateFinder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> rateFinder(@NonNull @RequestBody RateFinderRequest rateFinderRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;

		// Set Investment Type default to LUMSUM -- remove the line if SIP calculation
		// need
		rateFinderRequest.setInvType(CalcConstants.LUMSUM);
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = rateFinderRequest.getReferenceId();
		if (token != null && referenceId != null) {
			if (rateFinderRequest != null) {
				int screenId = rateFinderRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// Validating rateFinder request
		errors = rateFinderRequestValidator.validate(rateFinderRequest);
		if (errors.isEmpty() == true) {
			double futureValueZeroCheck = Double.parseDouble(rateFinderRequest.getFutureValue());
			double presentValueZeroCheck = Double.parseDouble(rateFinderRequest.getPresentValue());
			double durationZeroCheck = (Double.parseDouble(rateFinderRequest.getDuration()));
			if ((futureValueZeroCheck != 0 && presentValueZeroCheck != 0 && durationZeroCheck != 0)
					|| (futureValueZeroCheck != 0 && presentValueZeroCheck != 0)
					|| (presentValueZeroCheck != 0 && durationZeroCheck != 0)
					|| (durationZeroCheck != 0 && futureValueZeroCheck != 0)) {
				if (futureValueZeroCheck != 0 && presentValueZeroCheck != 0 && durationZeroCheck != 0) {
					RateFinderResponse rateResponse = new RateFinderResponse();
					// If invType is LUMSUM
					double presentValue = Double.parseDouble(rateFinderRequest.getPresentValue());
					double futureValue = Double.parseDouble(rateFinderRequest.getFutureValue());
					double duration;
					double rate = 0;
					if (rateFinderRequest.getDurationType().equals(CalcConstants.MONTH)) {
						duration = (Double.parseDouble(rateFinderRequest.getDuration())) / 12;
					} else {
						duration = Double.parseDouble(rateFinderRequest.getDuration());
					}
					if (rateFinderRequest.getInvType().equals(CalcConstants.LUMSUM)) {
						rate = (Math.pow((futureValue / presentValue), (1 / duration)) - 1) * 100;
						rateResponse.setRateOfInterest(roundingNumber(rate));
					} else if (rateFinderRequest.getInvType().equals(CalcConstants.SIP)) {
						// If invType is SIP
						// double presentValue =
						// Double.parseDouble(rateFinderRequest.getPresentValue());
						// double futureValue = Double.parseDouble(rateFinderRequest.getFutureValue());
						// double duration = Double.parseDouble(rateFinderRequest.getDuration());
						rate = (Math.pow((futureValue / presentValue), (1 / duration)) - 1) * 100;
						rateResponse.setRateOfInterest(roundingNumber(rate));
					}
					if (token != null && referenceId != null) {
						// Add into table
						RateFinder rateFinder = getValueRateFinderInfo(rateFinderRequest);
						rateFinder.setReferenceId(referenceId);
						rateFinder.setRateOfInterest(rate);
						int rateFinderCalc = calcService.checkRateFinderIsPresent(referenceId);
						int result = 0;
						if (rateFinderCalc == 0) {
							// If RateFinder is not present with this referenceId, then add as new record
							logger.info("adding RateFinder");
							result = calcService.addRateFinder(rateFinder);
						} else {
							// If RateFinder is present with this partyId, then update it
							logger.info("updating RateFinder");
							result = calcService.updateRateFinder(rateFinder);
						}
						// Response with total payment result - WithLogin
						logger.info("Calculating rate finder");
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getRate_calculated_successfully(), rateResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
					// Return response with rateOfInterest
					logger.info("Calculating rate finder");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getRate_calculated_successfully(), rateResponse, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					if (durationZeroCheck == 0) {
						logger.info("Duration Zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "DURATION", null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (futureValueZeroCheck == 0) {
						logger.info("Future value zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "FUTURE VALUE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (presentValueZeroCheck == 0) {
						logger.info("Present value zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "PRESENT VALUE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Zero validation error");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
						appMessages.getZero_validation_error(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private RateFinder getValueRateFinderInfo(RateFinderRequest rateFinderRequest) {
		RateFinder rateFinder = new RateFinder();

		if (rateFinderRequest != null && rateFinderRequest.getInvType() != null) {
			rateFinder.setInvType(rateFinderRequest.getInvType());
		}

		if (rateFinderRequest != null && rateFinderRequest.getPresentValue() != null) {
			rateFinder.setPresentValue(roundingNumber(Double.parseDouble(rateFinderRequest.getPresentValue())));
		}

		if (rateFinderRequest != null && rateFinderRequest.getFutureValue() != null) {
			rateFinder.setFutureValue(roundingNumber(Double.parseDouble(rateFinderRequest.getFutureValue())));
		}

		if (rateFinderRequest != null && rateFinderRequest.getDuration() != null) {
			rateFinder.setDuration(Integer.parseInt(rateFinderRequest.getDuration()));
		}

		if (rateFinderRequest != null && rateFinderRequest.getDurationType() != null) {
			rateFinder.setDurationType(rateFinderRequest.getDurationType());
		}

		return rateFinder;
	}

	// // Rate Finder Annuity
	// @RequestMapping(value = "/IP-RateFinder-SIP", method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> rateFinderAnnuity(@RequestBody
	// RateFinderAnnuityRequest rateFinderAnnuityRequest) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	//
	// errors =
	// rateFinderAnnuityRequestValidator.validate(rateFinderAnnuityRequest);
	// if (errors.isEmpty() == true) {
	// double presentValue =
	// Double.parseDouble(rateFinderAnnuityRequest.getPresentValue());
	// double futureValue =
	// Double.parseDouble(rateFinderAnnuityRequest.getFutureValue());
	// double duration = Double.parseDouble(rateFinderAnnuityRequest.getDuration());
	// double rate = (Math.pow((futureValue / presentValue), (1 / duration)) - 1) *
	// 100;
	// RateFinderResponse rateResponse = new RateFinderResponse();
	// rateResponse.setRateOfInterest(roundingNumber(rate));
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.SUCCESS_CODE);
	// responseMessage.setResponseDescription(AppMessages.RATE_FOUND_SUCCESSFULLY);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(rateResponse);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return ResponseEntity.ok().body(response);
	// } else if (errors.isEmpty() == false) {
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.ERROR_CODE);
	// responseMessage.setResponseDescription(AppMessages.ERROR);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(errors);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// return new ResponseEntity<String>(HttpStatus.OK);
	// }

	// Tenure Finder
	/**
	 * Method to calculate TenureFinder
	 * 
	 * @param RequestBody
	 *            contains the <code>TenureFinderRequest</code>
	 * @return ResponseEntity<> tenure or Error Response
	 */
	@ApiOperation(value = "tenure Finder", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/IP-TenureFinder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> tenureFinder(@NonNull @RequestBody TenureFinderRequest tenureFinderRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;

		// Set Investment Type default to LUMSUM -- remove the line if SIP calculation
		// need
		tenureFinderRequest.setInvType(CalcConstants.LUMSUM);
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = tenureFinderRequest.getReferenceId();
		if (token != null && referenceId != null) {
			if (tenureFinderRequest != null) {
				int screenId = tenureFinderRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		// Validating tenureFinder request
		errors = tenureFinderRequestValidator.validate(tenureFinderRequest);
		if (errors.isEmpty() == true) {
			double futureValueZeroCheck = Double.parseDouble(tenureFinderRequest.getFutureValue());
			double presentValueZeroCheck = Double.parseDouble(tenureFinderRequest.getPresentValue());
			double rateZeroCheck = Double.parseDouble(tenureFinderRequest.getRateOfInterest());
			if ((futureValueZeroCheck != 0 && presentValueZeroCheck != 0 && rateZeroCheck != 0)
					|| (futureValueZeroCheck != 0 && presentValueZeroCheck != 0)
					|| (presentValueZeroCheck != 0 && rateZeroCheck != 0)
					|| (rateZeroCheck != 0 && futureValueZeroCheck != 0)) {
				if (futureValueZeroCheck != 0 && presentValueZeroCheck != 0 && rateZeroCheck != 0) {
					TenureFinderResponse tenureResponse = new TenureFinderResponse();
					double presentValue = Double.parseDouble(tenureFinderRequest.getPresentValue());
					double futureValue = Double.parseDouble(tenureFinderRequest.getFutureValue());
					double rate = Double.parseDouble(tenureFinderRequest.getRateOfInterest()) / 100;
					double tenure = 0;
					// If invType is LUMSUM
					if (tenureFinderRequest.getInvType().equals(CalcConstants.LUMSUM)) {
						tenure = Math.log(futureValue / presentValue) / Math.log(1 + rate);
						tenureResponse.setTenure(roundingNumber(tenure));
						// If invType is SIP
					} else if (tenureFinderRequest.getInvType().equals(CalcConstants.SIP)) {
						// double presentValue =
						// Double.parseDouble(tenureFinderRequest.getPresentValue());
						// double futureValue =
						// Double.parseDouble(tenureFinderRequest.getFutureValue());
						// double rate = Double.parseDouble(tenureFinderRequest.getRateOfInterest()) /
						// 100;
						tenure = Math.log(futureValue / presentValue) / Math.log(1 + rate);
						tenureResponse.setTenure(roundingNumber(tenure));
					}
					if (token != null && referenceId != null) {
						TenureFinder tenureFinder = getValueTenureFinderInfo(tenureFinderRequest);
						tenureFinder.setReferenceId(referenceId);
						tenureFinder.setTenure(tenure);
						int tenureFinderCalc = calcService.checkTenureFinderIsPresent(referenceId);
						int result = 0;
						if (tenureFinderCalc == 0) {
							// If TenureFinder is not present with this referenceId, then add as new record
							logger.info("adding TenureFinder");
							result = calcService.addTenureFinder(tenureFinder);
						} else {
							// If TenureFinder is present with this partyId, then update it
							logger.info("updating TenureFinder");
							result = calcService.updateTenureFinder(tenureFinder);
						}
						logger.info("Calculating tenure finder");
						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getTenure_calculated_successfully(), tenureResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
					// Return response with tenure
					logger.info("Calculating tenure finder");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getTenure_calculated_successfully(), tenureResponse, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					if (presentValueZeroCheck == 0) {
						logger.info("Duration Zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "PRESENT VALUE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (futureValueZeroCheck == 0) {
						logger.info("Future value zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "FUTURE VALUE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (rateZeroCheck == 0) {
						logger.info("Interest rate zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Zero validation error");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
						appMessages.getZero_validation_error(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private TenureFinder getValueTenureFinderInfo(TenureFinderRequest tenureFinderRequest) {
		TenureFinder tenureFinder = new TenureFinder();

		if (tenureFinderRequest != null && tenureFinderRequest.getInvType() != null) {
			tenureFinder.setInvType(tenureFinderRequest.getInvType());
		}

		if (tenureFinderRequest != null && tenureFinderRequest.getPresentValue() != null) {
			tenureFinder.setPresentValue(roundingNumber(Double.parseDouble(tenureFinderRequest.getPresentValue())));
		}

		if (tenureFinderRequest != null && tenureFinderRequest.getFutureValue() != null) {
			tenureFinder.setFutureValue(roundingNumber(Double.parseDouble(tenureFinderRequest.getFutureValue())));
		}

		if (tenureFinderRequest != null && tenureFinderRequest.getRateOfInterest() != null) {
			tenureFinder.setRateOfInterest(roundingNumber(Double.parseDouble(tenureFinderRequest.getRateOfInterest())));
		}

		return tenureFinder;
	}

	// // Tenure Finder Annuity
	// @RequestMapping(value = "/IP-TenureFinder-SIP", method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> tenureFinderAnnuity(@RequestBody
	// TenureFinderAnnuityRequest tenureFinderAnnuityRequest) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	//
	// errors =
	// tenureFinderAnnuityRequestValidator.validate(tenureFinderAnnuityRequest);
	// if (errors.isEmpty() == true) {
	// double presentValue =
	// Double.parseDouble(tenureFinderAnnuityRequest.getPresentValue());
	// double futureValue =
	// Double.parseDouble(tenureFinderAnnuityRequest.getFutureValue());
	// double rate =
	// Double.parseDouble(tenureFinderAnnuityRequest.getRateOfInterest()) / 100;
	// double tenure = Math.log(futureValue / presentValue) / Math.log(1 + rate);
	// TenureFinderResponse tenureResponse = new TenureFinderResponse();
	// tenureResponse.setTenure(roundingNumber(tenure));
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.SUCCESS_CODE);
	// responseMessage.setResponseDescription(AppMessages.TENURE_CALCULATED_SUCCESSFULLY);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(tenureResponse);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return ResponseEntity.ok().body(response);
	// } else if (errors.isEmpty() == false) {
	// ResponseMessage responseMessage = new ResponseMessage();
	// responseMessage.setResponseCode(CalcConstants.ERROR_CODE);
	// responseMessage.setResponseDescription(AppMessages.ERROR);
	// ResponseData responseData = new ResponseData();
	// responseData.setData(errors);
	// Response response = new Response();
	// response.setResponseMessage(responseMessage);
	// response.setResponseData(responseData);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// return new ResponseEntity<String>(HttpStatus.OK);
	// }

	// LOAN PLANNING
	// Emi Calculator
	/**
	 * Method for Emicalculator
	 * 
	 * @param RequestBody
	 *            contains the <code>EmiCalculatorRequest</code>
	 * @return ResponseEntity<> Success result or Error Response
	 */
	@ApiOperation(value = "calculate emi", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateEmi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> emiCalculator(@NonNull @RequestBody EmiCalculatorRequest emiCalculatorRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;

		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = emiCalculatorRequest.getReferenceId();
		// logger.info("Fetching plan by referenceId");
		// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
		if (token != null && referenceId != null) {
			if (emiCalculatorRequest != null) {
				int screenId = emiCalculatorRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// Validating emiCalculator request
		errors = emiCalculatorRequestValidator.validate(emiCalculatorRequest);
		if (errors.isEmpty() == true) {
			double loanAmtZeroCheck = Double.parseDouble(emiCalculatorRequest.getLoanAmount());
			int tenureZeroCheck = Integer.parseInt(emiCalculatorRequest.getTenure());
			double rateZeroCheck = Double.parseDouble(emiCalculatorRequest.getInterestRate());
			if ((loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0)
					|| (loanAmtZeroCheck != 0 && tenureZeroCheck != 0) || (tenureZeroCheck != 0 && rateZeroCheck != 0)
					|| (rateZeroCheck != 0 && loanAmtZeroCheck != 0)) {
				if (loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0) {
					double loanAmt = Double.parseDouble(emiCalculatorRequest.getLoanAmount());
					double tenure;
					if (emiCalculatorRequest.getTenureType().equals(CalcConstants.MONTH)) {
						// tenure = (Integer.parseInt(emiCalculatorRequest.getTenure())) / 12;
						tenure = (Double.parseDouble(emiCalculatorRequest.getTenure())) / 12;
					} else {
						tenure = Integer.parseInt(emiCalculatorRequest.getTenure());
					}
					double interestRate = Double.parseDouble(emiCalculatorRequest.getInterestRate());
					double monthlyInterestRate = interestRate / (12 * 100);
					double noOfInstallments = tenure * 12;
					double emi = (loanAmt * monthlyInterestRate * Math.pow((1 + monthlyInterestRate), noOfInstallments))
							/ (Math.pow((1 + monthlyInterestRate), noOfInstallments) - 1);
					double interestPayable = 0;
					double tempAmt = loanAmt;
					double totalPrincipal = 0;
					double closing = 0;
					double loanPaid = 0;
					String date = emiCalculatorRequest.getDate();
					DateFormat formatter = new SimpleDateFormat("MMM-yyyy");
					Date currentDate = null;
					try {
						currentDate = formatter.parse(date);
					} catch (ParseException e) {
						logger.error(e.getMessage());
					}
					// Amortisation
					List<AmortisationResponse> amortisationResponse = new ArrayList<AmortisationResponse>();

					for (int i = 0; i < noOfInstallments && closing >= 0; i++) {
						// OPENING//
						int months = i + 1;
						double opening = tempAmt;
						// INTEREST//
						double interest = opening * monthlyInterestRate;
						interestPayable = interestPayable + interest;
						// PRINCIPAL//
						if (i >= noOfInstallments) {
							totalPrincipal = 0;
							closing = 0;
							loanPaid = 0;
						} else {
							// PRINCIPAL//
							totalPrincipal = emi - interest;
							// CLOSING//
							closing = opening - totalPrincipal;
							// LOANPAID//

							// loanPaid = (interest / (interest + totalPrincipal)) * 100;
							loanPaid = closing / loanAmt * 100;
							tempAmt = closing;
						}
						// Calendar c = Calendar.getInstance();
						// c.setTime(currentDate);
						// c.add(Calendar.MONTH, 1);
						// String newDate = formatter.format(c.getTime());
						// Date nwDate = null;
						// try {
						// nwDate = formatter.parse(newDate);
						// } catch (ParseException e) {
						// e.printStackTrace();
						// }
						Calendar c = Calendar.getInstance();
						c.setTime(currentDate);
						String newDate = formatter.format(c.getTime());
						c.add(Calendar.MONTH, 1);
						Date nwDate = null;
						try {
							nwDate = formatter.parse(formatter.format(c.getTime()));
						} catch (ParseException e) {
							logger.error(e.getMessage());
						}

						currentDate = nwDate;
						AmortisationResponse amortisation = new AmortisationResponse();
						amortisation.setMonths(months);
						amortisation.setOpening(roundingNumber(opening));
						amortisation.setInterest(roundingNumber(interest));
						amortisation.setTotalPrincipal(roundingNumber(totalPrincipal));
						amortisation.setClosing(roundingNumber(closing));
						amortisation.setLoanPaid(roundingNumber(loanPaid));
						amortisation.setDate(newDate);
						amortisationResponse.add(amortisation);
					}
					double total = interestPayable + loanAmt;

					// Add into table
					EmiCalculator emiCalculator = getValueEmiCalculatorInfo(emiCalculatorRequest);
					emiCalculator.setReferenceId(emiCalculatorRequest.getReferenceId());
					emiCalculator.setDate(emiCalculatorRequest.getDate());
					emiCalculator.setTenure(Integer.parseInt(emiCalculatorRequest.getTenure()));
					emiCalculator.setTenureType(emiCalculatorRequest.getTenureType());
					int result = 0;
					// if (calcService.fetchEmiCalculatorByRefId(referenceId) == null) {
					if (token != null && referenceId != null) {
						logger.info("Fetching emiCalculator by referenceId");
						int emiCalc = calcService.checkEmiCalculatorIsPresent(referenceId);

						if (emiCalc == 0) {
							// If emiCalculator is not present with this partyId, then add as new record
							logger.info("adding emiCalculator");
							result = calcService.addEmiCalculator(emiCalculator);
						} else {
							// If emiCalculator is present with this partyId, then update it
							logger.info("updating emiCalculator");
							result = calcService.updateEmiCalculator(emiCalculator);
						}

						// Response
						// Response with Emi calculator result and amortisation
						EmiCalculatorResponse emiResponse = new EmiCalculatorResponse();
						emiResponse.setLoanAmount(roundingNumber(loanAmt));
						emiResponse.setTenure(tenure);
						emiResponse.setEmi(roundingNumber(emi));
						emiResponse.setInterestPayable(roundingNumber(interestPayable));
						emiResponse.setTotal(roundingNumber(total));
						emiResponse.setAmortisationResponse(amortisationResponse);
						emiResponse.setInterestRate(Double.parseDouble(emiCalculatorRequest.getInterestRate()));
						emiResponse.setLoanDate(emiCalculatorRequest.getDate());
						if (result == 0) {
							logger.info("Error Occurred while adding data");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getError_occured(), null, null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
									appMessages.getEmi_calculated_successfully(), emiResponse, roleFieldRights);
							return ResponseEntity.ok().body(response);
						}
					} else {
						// Response
						// Response with Emi calculator result and amortisation
						EmiCalculatorResponse emiResponse = new EmiCalculatorResponse();
						emiResponse.setLoanAmount(roundingNumber(loanAmt));
						emiResponse.setTenure(tenure);
						emiResponse.setEmi(roundingNumber(emi));
						emiResponse.setInterestPayable(roundingNumber(interestPayable));
						emiResponse.setTotal(roundingNumber(total));
						emiResponse.setAmortisationResponse(amortisationResponse);
						emiResponse.setInterestRate(Double.parseDouble(emiCalculatorRequest.getInterestRate()));
						emiResponse.setLoanDate(emiCalculatorRequest.getDate());

						CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
								appMessages.getEmi_calculated_successfully(), emiResponse, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else {
					if (loanAmtZeroCheck == 0) {
						logger.info("Duration Zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (tenureZeroCheck == 0) {
						logger.info("Future value zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "TENURE", null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else if (rateZeroCheck == 0) {
						logger.info("Interest rate zero validation error");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE", null,
								null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Zero validation error");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
						appMessages.getZero_validation_error(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	private EmiCalculator getValueEmiCalculatorInfo(EmiCalculatorRequest emiCalculatorRequest) {
		EmiCalculator emiCalculator = new EmiCalculator();
		if (emiCalculatorRequest != null && emiCalculatorRequest.getLoanAmount() != null) {
			emiCalculator.setLoanAmount(roundingNumber(Double.parseDouble(emiCalculatorRequest.getLoanAmount())));
		}
		if (emiCalculatorRequest != null && emiCalculatorRequest.getInterestRate() != null) {
			emiCalculator.setInterestRate(roundingNumber(Double.parseDouble(emiCalculatorRequest.getInterestRate())));
		}
		if (emiCalculatorRequest != null && emiCalculatorRequest.getTenure() != null) {
			emiCalculator.setTenure(Integer.parseInt(emiCalculatorRequest.getTenure()));
		}
		return emiCalculator;
	}

	// Emi Capacity
	/**
	 * Method for EmiCapacity
	 * 
	 * @param RequestBody
	 *            contains the <code>EmiCapacityRequest</code>
	 * @return ResponseEntity<> Success result or Error Response
	 */
	@ApiOperation(value = "calculate emiCapacity", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateEmiCapacity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> emiCapacity(@NonNull @RequestBody EmiCapacityRequest emiCapacityRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = emiCapacityRequest.getReferenceId();
		if (token != null && emiCapacityRequest.getReferenceId() != null) {
			if (emiCapacityRequest != null) {
				int screenId = emiCapacityRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			// String principle = emiCapacityRequest.getPricipal();
			// String interest = emiCapacityRequest.getInterest();
			logger.info("Fetching plan by referenceId");
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// Validating emiCapacityRequest
		errors = emiCapacityRequestValidator.validate(emiCapacityRequest);
		if (errors.isEmpty() == true) {
			double currentAge = Integer.parseInt(emiCapacityRequest.getCurrentAge());
			double retirementAge = Integer.parseInt(emiCapacityRequest.getRetirementAge());
			double termOfLoan = retirementAge - currentAge;
			String stability = emiCapacityRequest.getStability();
			String backUp = emiCapacityRequest.getBackUp();
			double netFamilyIncome = Double.parseDouble(emiCapacityRequest.getNetFamilyIncome());
			double existingEmi = Double.parseDouble(emiCapacityRequest.getExistingEmi());
			double houseHoldExp = Double.parseDouble(emiCapacityRequest.getHouseHoldExpense());
			double surplusMoney = netFamilyIncome - existingEmi - houseHoldExp;
			double surplus = 0;
			//
			// double principle = Integer.parseInt(emiCapacityRequest.getPriciple());
			// double interest = Integer.parseInt(emiCapacityRequest.getInterest());
			// double totalPayment = principle + interest;
			// double interestPayable = 0;
			// interestPayable = interestPayable + interest;

			if (stability.equals(CalcConstants.HIGH) && backUp.equals(CalcConstants.YES)) {
				surplus = surplusMoney * 100 / 100;
			} else if (stability.equals(CalcConstants.MEDIUM) && backUp.equals(CalcConstants.YES)) {
				surplus = surplusMoney * 90 / 100;

			} else if (stability.equals(CalcConstants.HIGH) && backUp.equals(CalcConstants.NO)) {
				surplus = surplusMoney * 90 / 100;
			} else if (stability.equals(CalcConstants.MEDIUM) && backUp.equals(CalcConstants.NO)) {
				surplus = surplusMoney * 80 / 100;
			}

			double additionalIncome = Double.parseDouble(emiCapacityRequest.getAdditionalIncome());
			double emiCapacity1 = surplus + additionalIncome;

			// Advisable Loan Amount

			double emiPayable = emiCapacity1;
			double rateOfInterestPerAnnum = Double.parseDouble(emiCapacityRequest.getInterestRate());
			double monthlyInterestRate = rateOfInterestPerAnnum / (12 * 100);
			double termInYear = 0;
			if (termOfLoan >= 20) {
				termInYear = 20;
			} else if (termOfLoan >= 15 && termOfLoan < 20) {
				termInYear = 15;
			} else if (termOfLoan < 15) {
				termInYear = termOfLoan;
			}
			double nfInstallments = termInYear * 12;
			double advisableLoanAmount = emiPayable / ((Math.pow((1 + monthlyInterestRate), nfInstallments)
					/ ((Math.pow((1 + monthlyInterestRate), nfInstallments) - 1)) * monthlyInterestRate));

			// Add into Table
			EmiCapacity emiCapacity = getValueEmiCapacityInfo(emiCapacityRequest);
			if (token != null && emiCapacityRequest.getReferenceId() != null) {
				emiCapacity.setReferenceId(emiCapacityRequest.getReferenceId());
				int result;
				logger.info("Fetching emiCapacity by referenceId");
				// if (calcService.fetchEmiCapacityByRefId(referenceId) == null) {
				int emiCapacityByRefId = calcService.checkemiCapacityIsPresent(referenceId);
				if (emiCapacityByRefId == 0) {
					// If emicapacity is not present, then add as new record
					logger.info("adding emiCapacity");
					result = calcService.addEmiCapacity(emiCapacity);
				} else {
					// If emicapacity is already present, then update it
					logger.info("updating emiCapacity");
					result = calcService.updateEmiCapacity(emiCapacity);
				}
				if (result == 0) {
					logger.info("Error Occurred while adding data");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError_occured(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			// Response
			EmiCapacityResponse emiResponse = new EmiCapacityResponse();
			emiResponse.setTermOfLoan(roundingNumber(termInYear));
			emiResponse.setSurplusMoney(roundingNumber(surplusMoney));
			emiResponse.setSurplus(roundingNumber(surplus));
			emiResponse.setEmiCapacity(roundingNumber(emiCapacity1));
			emiResponse.setEmiPayable(roundingNumber(emiPayable));
			emiResponse.setAdvisableLoanAmount(roundingNumber(advisableLoanAmount));
			// emiResponse.setTotalPayment(totalPayment);
			// emiResponse.setInterestPayable(interestPayable);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getEmi_calculated_successfully(), emiResponse, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private EmiCapacity getValueEmiCapacityInfo(EmiCapacityRequest emiCapacityRequest) {
		EmiCapacity emiCapacity = new EmiCapacity();
		if (emiCapacityRequest != null && emiCapacityRequest.getCurrentAge() != null) {
			emiCapacity.setCurrentAge(Integer.parseInt(emiCapacityRequest.getCurrentAge()));
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getRetirementAge() != null) {
			emiCapacity.setRetirementAge(Integer.parseInt(emiCapacityRequest.getRetirementAge()));
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getStability() != null) {
			emiCapacity.setStability(emiCapacityRequest.getStability());
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getBackUp() != null) {
			emiCapacity.setBackUp(emiCapacityRequest.getBackUp());
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getNetFamilyIncome() != null) {
			emiCapacity.setNetFamilyIncome(roundingNumber(Double.parseDouble(emiCapacityRequest.getNetFamilyIncome())));
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getExistingEmi() != null) {
			emiCapacity.setExistingEmi(roundingNumber(Double.parseDouble(emiCapacityRequest.getExistingEmi())));
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getHouseHoldExpense() != null) {
			emiCapacity
					.setHouseHoldExpense(roundingNumber(Double.parseDouble(emiCapacityRequest.getHouseHoldExpense())));
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getAdditionalIncome() != null) {
			emiCapacity
					.setAdditionalIncome(roundingNumber(Double.parseDouble(emiCapacityRequest.getAdditionalIncome())));
		}
		if (emiCapacityRequest != null && emiCapacityRequest.getInterestRate() != null) {
			emiCapacity.setInterestRate(roundingNumber(Double.parseDouble(emiCapacityRequest.getInterestRate())));
		}

		return emiCapacity;
	}

	// Partial payment
	/**
	 * Method for PartialPayment
	 * 
	 * @param RequestBody
	 *            contains the <code>PartialPaymentRequest</code>
	 * @return ResponseEntity<> Success result or Error Response
	 */
	@ApiOperation(value = "calculate PartialPayment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculatePartialPayment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculatePartialPayment(@NonNull @RequestBody PartialPaymentRequest partialPaymentRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = partialPaymentRequest.getReferenceId();
		if (token != null && partialPaymentRequest.getReferenceId() != null) {
			if (partialPaymentRequest != null) {
				int screenId = partialPaymentRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			logger.info("Fetching plan by referenceId");
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (partialPaymentRequest.getLoanAmount() != null && partialPaymentRequest.getInterestRate() != null
				&& partialPaymentRequest.getTenure() != null && partialPaymentRequest.getTenureType() != null
				&& partialPaymentRequest.getLoanDate() != null) {
			errors = partialPaymentRequestValidator.validate(partialPaymentRequest);
			if (errors.isEmpty() == true) {
				double loanAmtZeroCheck = Double.parseDouble(partialPaymentRequest.getLoanAmount());
				int tenureZeroCheck = Integer.parseInt(partialPaymentRequest.getTenure());
				double rateZeroCheck = Double.parseDouble(partialPaymentRequest.getInterestRate());
				if ((loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0)
						|| (loanAmtZeroCheck != 0 && tenureZeroCheck != 0)
						|| (tenureZeroCheck != 0 && rateZeroCheck != 0)
						|| (rateZeroCheck != 0 && loanAmtZeroCheck != 0)) {
					if (loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0) {
						double revisedTenure = 0;
						double loanAmt = Double.parseDouble(partialPaymentRequest.getLoanAmount());
						double annualRate = Double.parseDouble(partialPaymentRequest.getInterestRate()) / 100;
						double monthlyRate = annualRate / 12;
						double tenure;
						if (partialPaymentRequest.getTenureType().equals(CalcConstants.MONTH)) {
							tenure = (Double.parseDouble(partialPaymentRequest.getTenure())) / 12;
						} else {
							tenure = Integer.parseInt(partialPaymentRequest.getTenure());
						}
						double noOfInstall = tenure * 12;
						double noOfInstallYear = 12;
						double emi = (loanAmt * monthlyRate * (Math.pow((1 + monthlyRate), noOfInstall)))
								/ ((Math.pow((1 + monthlyRate), noOfInstall) - 1));
						String date = partialPaymentRequest.getLoanDate();
						String loanDate = convertMonthYearToDate(date);
						List<PartialPaymentReq> partialPaymentReq = partialPaymentRequest.getPartialPaymentReq();
						double loanAmtTemp = loanAmt;
						int result2;
						logger.info("Fetching partial payment");
						// if (calcService.fetchPartialPaymentByRefId(referenceId).size() != 0) {
						if (token != null && partialPaymentRequest.getReferenceId() != null) {
							int partial = calcService.checkpartialIsPresent(referenceId);
							if (partial != 0) {
								logger.info("removing partial payment by referenceId");
								int result1 = calcService.removePartialPaymentByRefId(referenceId);
								if (result1 == 0) {
									logger.info("Error Occurred while removing data");
									CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
											appMessages.getError_occured_remove(), null, null);
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							}
						}
						DateFormat formatter = new SimpleDateFormat("MMM-yyyy");
						Date currentDate = null;
						try {
							currentDate = formatter.parse(date);
						} catch (ParseException e) {
							logger.error(e.getMessage());
						}
						if (partialPaymentReq != null && partialPaymentReq.size() != 0) {
							for (int i = 0; i < partialPaymentReq.size(); i++) {
								int noOfPaid = 0;
								// int year1, month1, day1;
								String partPayDate = convertMonthYearToDate(partialPaymentReq.get(i).getPartPayDate());
								// Difference between dates
								if (i == 0) {
									noOfPaid = calculateMonth(loanDate, partPayDate);
								} else {
									String prevDate = convertMonthYearToDate(
											partialPaymentReq.get(i - 1).getPartPayDate());
									// Difference between dates
									noOfPaid = calculateMonth(prevDate, partPayDate);
								}
								// Partial Payment Result
								double partPayment = Double.parseDouble(partialPaymentReq.get(i).getPartPayAmount());
								double outPrincipal = (loanAmtTemp - (emi * noOfInstallYear / annualRate))
										* (Math.pow((1 + (annualRate / noOfInstallYear)), noOfPaid))
										+ (emi * noOfInstallYear / annualRate);
								double remainPrincipal = outPrincipal - partPayment;
								double noOfYearNeed = -Math
										.log(1 - (remainPrincipal * annualRate / (emi * noOfInstallYear)))
										/ (noOfInstallYear * Math.log((1 + (annualRate / noOfInstallYear))));
								// double noOfMonthNeed = noOfYearNeed * 12;
								loanAmtTemp = remainPrincipal;
								// Add Partial Payment Into Table
								PartialPayment partialPayment = getPartialPaymentValue(partialPaymentReq.get(i));
								partialPayment.setReferenceId(referenceId);
								partialPayment.setLoanAmount(roundingNumber(loanAmt));
								partialPayment
										.setInterestRate(Double.parseDouble(partialPaymentRequest.getInterestRate()));
								partialPayment.setTenure(Integer.parseInt(partialPaymentRequest.getTenure()));
								partialPayment.setTenureType(partialPaymentRequest.getTenureType());
								partialPayment.setRevisedTenure(noOfYearNeed);
								partialPayment.setLoanDate(date);
								revisedTenure = noOfYearNeed;
								if (token != null && partialPaymentRequest.getReferenceId() != null) {
									logger.info("adding partialPayment");
									result2 = calcService.addPartialPayment(partialPayment);
									if (result2 == 0) {
										logger.info("Error Occurred while adding data");
										CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
												appMessages.getError_occured(), null, null);
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
									}
								}
							}
							// Amortisation Schedule
							List<AmortisationResponse> amortisation = new ArrayList<AmortisationResponse>();

							double opening = 0;
							double interestPay = 0;
							double totalPrincipal = 0;
							double closing = 0;
							double loanPaid = 0;
							double temp = loanAmt;
							List<Integer> noOfPaids = new ArrayList<>();
							List<Double> partialPayment = new ArrayList<>();
							for (int i = 0; i < partialPaymentReq.size(); i++) {
								double partPayment = Double.parseDouble(partialPaymentReq.get(i).getPartPayAmount());
								String partPayDate = convertMonthYearToDate(partialPaymentReq.get(i).getPartPayDate());
								int noOfPaid;
								noOfPaid = calculateMonth(loanDate, partPayDate);
								partialPayment.add(partPayment);
								noOfPaids.add(noOfPaid);
							}
							Iterator<Integer> noOfPaidItr = noOfPaids.iterator();
							Iterator<Double> partPayItr = partialPayment.iterator();
							int noOfPaid = (int) noOfPaidItr.next();
							double partPayment = (double) partPayItr.next();
							for (int j = 0; j < noOfInstall && closing >= 0; j++) {
								if (j == noOfPaid) {
									opening = temp - partPayment;
									if (noOfPaidItr.hasNext() && partPayItr.hasNext()) {
										noOfPaid = (int) noOfPaidItr.next();
										partPayment = (double) partPayItr.next();
									}
								} else {
									if (temp < 0) {
										opening = 0;
									} else {
										opening = temp;
									}
								}
								double interest = opening * monthlyRate;
								interestPay = interestPay + interest;

								if (j >= noOfInstall) {
									totalPrincipal = 0;
									closing = 0;
								} else {
									totalPrincipal = emi - interest;
									closing = opening - totalPrincipal;

									// loanPaid = (interest / (interest + totalPrincipal)) * 100;
									loanPaid = closing / loanAmt * 100;
									temp = closing;
								}

								// Calendar c = Calendar.getInstance();
								// c.setTime(currentDate);
								// c.add(Calendar.MONTH, 1);
								// String newDate = formatter.format(c.getTime());
								// Date nwDate = null;
								// try {
								// nwDate = formatter.parse(newDate);
								// } catch (ParseException e) {
								// e.printStackTrace();
								// }
								Calendar c = Calendar.getInstance();
								c.setTime(currentDate);
								String newDate = formatter.format(c.getTime());
								c.add(Calendar.MONTH, 1);
								Date nwDate = null;
								try {
									nwDate = formatter.parse(formatter.format(c.getTime()));
								} catch (ParseException e) {
									logger.error(e.getMessage());
								}

								currentDate = nwDate;
								AmortisationResponse amortisationRes = new AmortisationResponse();
								amortisationRes.setMonths(j + 1);
								amortisationRes.setOpening(roundingNumber(opening));
								amortisationRes.setInterest(roundingNumber(interest));
								amortisationRes.setTotalPrincipal(roundingNumber(totalPrincipal));
								amortisationRes.setClosing(roundingNumber(closing));
								amortisationRes.setLoanPaid(roundingNumber(loanPaid));
								amortisationRes.setDate(newDate);
								amortisation.add(amortisationRes);

							}
							double total = (interestPay + loanAmt);
							// Response with result and amortisation schedule
							PartialPaymentResponse partialPayResponse = new PartialPaymentResponse();
							partialPayResponse.setLoanAmount(roundingNumber(loanAmt));
							partialPayResponse.setTenure(tenure);
							partialPayResponse.setEmi(roundingNumber(emi));
							partialPayResponse.setInterestPayable(roundingNumber(interestPay));
							partialPayResponse.setTotal(roundingNumber(total));
							partialPayResponse.setRevisedTenure(roundingNumber(revisedTenure));
							partialPayResponse.setAmortisation(amortisation);
							CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
									appMessages.getPartpayment_calculated_successfully(), partialPayResponse,
									roleFieldRights);
							return ResponseEntity.ok().body(response);
						} else {
							EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
							emiCalculatorRequest.setReferenceId(partialPaymentRequest.getReferenceId());
							emiCalculatorRequest.setLoanAmount(partialPaymentRequest.getLoanAmount());
							emiCalculatorRequest.setTenure(partialPaymentRequest.getTenure());
							emiCalculatorRequest.setTenureType(partialPaymentRequest.getTenureType());
							emiCalculatorRequest.setInterestRate(partialPaymentRequest.getInterestRate());
							emiCalculatorRequest.setDate(partialPaymentRequest.getLoanDate());
							return calculateEmiCalculator(CalcConstants.PARTIALPAYMENT, emiCalculatorRequest,
									roleFieldRights, appMessages.getPartpayment_calculated_successfully(), token);
						}
					} else {
						if (loanAmtZeroCheck == 0) {
							logger.info("Loan amount Zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (tenureZeroCheck == 0) {
							logger.info("Tenure zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "TENURE", null, null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (rateZeroCheck == 0) {
							logger.info("Interest rate zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					logger.info("Zero validation error");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
							appMessages.getZero_validation_error(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (errors.isEmpty() == false) {
				// return validation errors
				logger.error("validation errors");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			logger.error("some fields are empty");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getFields_cannot_be_empty(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private ResponseEntity<CalcResponse> calculateEmiCalculator(String calc, EmiCalculatorRequest emiCalculatorRequest,
			List<RoleFieldRights> roleFieldRights, String message, String token) {

		double loanAmt = Double.parseDouble(emiCalculatorRequest.getLoanAmount());
		double tenure;
		if (emiCalculatorRequest.getTenureType().equals(CalcConstants.MONTH)) {
			tenure = (Double.parseDouble(emiCalculatorRequest.getTenure())) / 12;
		} else {
			tenure = Integer.parseInt(emiCalculatorRequest.getTenure());
		}
		double interestRate = Double.parseDouble(emiCalculatorRequest.getInterestRate());
		double monthlyInterestRate = interestRate / (12 * 100);
		double noOfInstallments = tenure * 12;
		double emi = (loanAmt * monthlyInterestRate * Math.pow((1 + monthlyInterestRate), noOfInstallments))
				/ (Math.pow((1 + monthlyInterestRate), noOfInstallments) - 1);
		double interestPayable = 0;
		double tempAmt = loanAmt;
		double totalPrincipal = 0;
		double closing = 0;
		double loanPaid = 0;
		String date = emiCalculatorRequest.getDate();
		DateFormat formatter = new SimpleDateFormat("MMM-yyyy");
		Date currentDate = null;
		try {
			currentDate = formatter.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		// Amortisation
		List<AmortisationResponse> amortisationResponse = new ArrayList<AmortisationResponse>();

		for (int i = 0; i < noOfInstallments && closing >= 0; i++) {
			int months = i + 1;
			double opening = tempAmt;
			// INTEREST
			double interest = opening * monthlyInterestRate;
			interestPayable = interestPayable + interest;
			// PRINCIPAL
			if (i >= noOfInstallments) {
				totalPrincipal = 0;
				closing = 0;
				loanPaid = 0;
			} else {
				// PRINCIPAL
				totalPrincipal = emi - interest;
				// CLOSING
				closing = opening - totalPrincipal;
				// LOANPAID
				// loanPaid = (interest / (interest + totalPrincipal)) * 100;
				loanPaid = closing / loanAmt * 100;
				tempAmt = closing;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			String newDate = formatter.format(c.getTime());
			c.add(Calendar.MONTH, 1);
			Date nwDate = null;
			try {
				nwDate = formatter.parse(formatter.format(c.getTime()));
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}

			currentDate = nwDate;
			AmortisationResponse amortisation = new AmortisationResponse();
			amortisation.setMonths(months);
			amortisation.setOpening(roundingNumber(opening));
			amortisation.setInterest(roundingNumber(interest));
			amortisation.setTotalPrincipal(roundingNumber(totalPrincipal));
			amortisation.setClosing(roundingNumber(closing));
			amortisation.setLoanPaid(roundingNumber(loanPaid));
			amortisation.setDate(newDate);
			amortisationResponse.add(amortisation);
		}
		double total = interestPayable + loanAmt;

		// Add into table
		EmiCalculator emiCalculator = getValueEmiCalculatorInfo(emiCalculatorRequest);
		emiCalculator.setReferenceId(emiCalculatorRequest.getReferenceId());
		emiCalculator.setDate(emiCalculatorRequest.getDate());
		emiCalculator.setTenure(Integer.parseInt(emiCalculatorRequest.getTenure()));
		emiCalculator.setTenureType(emiCalculatorRequest.getTenureType());
		int result = 0;
		if (token != null && emiCalculatorRequest.getReferenceId() != null) {
			if (calc.equals(CalcConstants.PARTIALPAYMENT)) {
				logger.info("Fetching partial payment by referenceId");
				// if
				// (calcService.fetchPartialPaymentByRefId(emiCalculatorRequest.getReferenceId()).size()
				// != 0) {
				int partial = calcService.checkpartialIsPresent(emiCalculatorRequest.getReferenceId());
				if (partial != 0) {
					logger.info("removing partial payment by referenceId");
					int result1 = calcService.removePartialPaymentByRefId(emiCalculatorRequest.getReferenceId());
					if (result1 == 0) {
						logger.info("Error Occurred while removing data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured_remove(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}

				PartialPayment partialPayment = new PartialPayment();
				partialPayment.setReferenceId(emiCalculatorRequest.getReferenceId());
				partialPayment.setLoanAmount(roundingNumber(loanAmt));
				partialPayment.setInterestRate(Double.parseDouble(emiCalculatorRequest.getInterestRate()));
				partialPayment.setTenure(Integer.parseInt(emiCalculatorRequest.getTenure()));
				partialPayment.setTenureType(emiCalculatorRequest.getTenureType());
				partialPayment.setLoanDate(date);
				logger.info("adding partialPayment");
				result = calcService.addPartialPayment(partialPayment);
			} else if (calc.equals(CalcConstants.EMICHANGE)) {
				logger.info("Fetching emiChange by referenceId");
				// if
				// (calcService.fetchEmiChangeByRefId(emiCalculatorRequest.getReferenceId()).size()
				// != 0) {
				int emiChangeByRefId = calcService.checkEmiChangeIsPresent(emiCalculatorRequest.getReferenceId());
				if (emiChangeByRefId != 0) {
					logger.info("removing emiChange by referenceId");
					int result1 = calcService.removeEmiChangeByRefId(emiCalculatorRequest.getReferenceId());
					if (result1 == 0) {
						logger.info("Error Occurred while removing data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured_remove(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
				EmiChange emiChange = new EmiChange();
				emiChange.setReferenceId(emiCalculatorRequest.getReferenceId());
				emiChange.setLoanAmount(roundingNumber(loanAmt));
				emiChange.setInterestRate(Double.parseDouble(emiCalculatorRequest.getInterestRate()));
				emiChange.setTenure(Integer.parseInt(emiCalculatorRequest.getTenure()));
				emiChange.setTenureType(emiCalculatorRequest.getTenureType());
				emiChange.setLoanDate(date);
				logger.info("adding emiChange");
				result = calcService.addEmiChange(emiChange);
			} else if (calc.equals(CalcConstants.INTERESTCHANGE)) {
				logger.info("Fetching interestChange by referenceId");
				// if
				// (calcService.fetchInterestChangeByRefId(emiCalculatorRequest.getReferenceId()).size()
				// != 0) {
				int interestChangeByRefId = calcService
						.checkInterestChangeIsPresent(emiCalculatorRequest.getReferenceId());
				if (interestChangeByRefId != 0) {
					logger.info("removing interestChange by referenceId");
					int result1 = calcService.removeInterestChangeByRefId(emiCalculatorRequest.getReferenceId());
					if (result1 == 0) {
						logger.info("Error Occurred while removing data");
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getError_occured_remove(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
				InterestChange interestChange = new InterestChange();
				interestChange.setReferenceId(emiCalculatorRequest.getReferenceId());
				interestChange.setLoanAmount(roundingNumber(loanAmt));
				interestChange.setInterestRate(Double.parseDouble(emiCalculatorRequest.getInterestRate()));
				interestChange.setTenure(Integer.parseInt(emiCalculatorRequest.getTenure()));
				interestChange.setTenureType(emiCalculatorRequest.getTenureType());
				interestChange.setLoanDate(date);
				logger.info("adding interestChange");
				result = calcService.addInterestChange(interestChange);
			}
		}

		// Response
		// Response with Emi calculator result and amortisation
		EmiCalculatorResponse emiResponse = new EmiCalculatorResponse();
		emiResponse.setLoanAmount(roundingNumber(loanAmt));
		emiResponse.setTenure(tenure);
		emiResponse.setEmi(roundingNumber(emi));
		emiResponse.setInterestPayable(roundingNumber(interestPayable));
		emiResponse.setTotal(roundingNumber(total));
		emiResponse.setAmortisationResponse(amortisationResponse);
		if (token != null && emiCalculatorRequest.getReferenceId() != null) {
			if (result == 0) {
				logger.info("Error Occurred while adding data");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, message, emiResponse, roleFieldRights);
		return ResponseEntity.ok().body(response);

	}

	private String convertMonthYearToDate(String date) {
		SimpleDateFormat month_date = new SimpleDateFormat("MMM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date monthDate = null;
		try {
			monthDate = month_date.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		String newDate = sdf.format(monthDate);
		return newDate;
	}

	private PartialPayment getPartialPaymentValue(PartialPaymentReq partialPaymentReq) {
		PartialPayment partPayment = new PartialPayment();
		if (partialPaymentReq != null && partialPaymentReq.getPartPayDate() != null) {
			partPayment.setPartPayDate(partialPaymentReq.getPartPayDate());
		}
		if (partialPaymentReq != null && partialPaymentReq.getPartPayAmount() != null) {
			partPayment.setPartPayAmount(Double.parseDouble(partialPaymentReq.getPartPayAmount()));
		}
		return partPayment;
	}

	/**
	 * Method for EmiChange
	 * 
	 * @param RequestBody
	 *            contains the <code>EmiChangeRequest</code>
	 * @return ResponseEntity<> Success result or Error Response
	 */
	@ApiOperation(value = "calculate emiChange", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateEmiChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> emiChange(@NonNull @RequestBody EmiChangeRequest emiChangeRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = emiChangeRequest.getReferenceId();
		if (token != null && referenceId != null) {
			if (emiChangeRequest != null) {
				int screenId = emiChangeRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			logger.info("Fetching plan by referenceId");
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (emiChangeRequest.getLoanAmount() != null && emiChangeRequest.getInterestRate() != null
				&& emiChangeRequest.getTenure() != null && emiChangeRequest.getTenureType() != null
				&& emiChangeRequest.getLoanDate() != null) {
			// validating emiChange Request
			errors = emiChangeRequestValidator.validate(emiChangeRequest);
			if (errors.isEmpty() == true) {
				double loanAmtZeroCheck = Double.parseDouble(emiChangeRequest.getLoanAmount());
				int tenureZeroCheck = Integer.parseInt(emiChangeRequest.getTenure());
				double rateZeroCheck = Double.parseDouble(emiChangeRequest.getInterestRate());
				if ((loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0)
						|| (loanAmtZeroCheck != 0 && tenureZeroCheck != 0)
						|| (tenureZeroCheck != 0 && rateZeroCheck != 0)
						|| (rateZeroCheck != 0 && loanAmtZeroCheck != 0)) {
					if (loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0) {
						double revisedTenure = 0;
						double revisedEmi = 0;
						double loanAmount = Double.parseDouble(emiChangeRequest.getLoanAmount());
						double annualInterestRate = Double.parseDouble(emiChangeRequest.getInterestRate()) / 100;
						double monthlyInterestRate = annualInterestRate / 12;
						double tenure;
						if (emiChangeRequest.getTenureType().equals(CalcConstants.MONTH)) {
							tenure = (Double.parseDouble(emiChangeRequest.getTenure())) / 12;
						} else {
							tenure = Integer.parseInt(emiChangeRequest.getTenure());
						}
						double noOfInstallments = tenure * 12;
						double noOfInstallmentsPerYear = 12;
						// system date
						Date date = new Date();
						DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String currentDate = formatter.format(date);
						String emiChangedDate1 = null;
						String loanDate = convertMonthYearToDate(emiChangeRequest.getLoanDate());
						if (emiChangeRequest.getEmiChangeReq() != null
								&& emiChangeRequest.getEmiChangeReq().size() > 0) {
							for (EmiChangeReq emiChangeReq : emiChangeRequest.getEmiChangeReq()) {
								emiChangedDate1 = convertMonthYearToDate(emiChangeReq.getEmiChangedDate());
							}
						} else {
							emiChangedDate1 = currentDate;
						}
						int noOfPaymentsPaid = calculateMonth(loanDate, emiChangedDate1);

						// Find EMI
						double emi = (loanAmount * monthlyInterestRate
								* Math.pow(1 + monthlyInterestRate, noOfInstallments))
								/ (Math.pow(1 + monthlyInterestRate, noOfInstallments) - 1);
						// OutStanding Amount
						double afterNPayments = (loanAmount - (emi * noOfInstallmentsPerYear / annualInterestRate))
								* Math.pow((1 + (annualInterestRate / noOfInstallmentsPerYear)), noOfPaymentsPaid)
								+ (emi * noOfInstallmentsPerYear) / annualInterestRate;
						// Years for Pay Off
						// double noOfYearsNeed = -Math
						// .log(1 - (loanAmount * annualInterestRate / (emi * noOfInstallmentsPerYear)))
						// / (noOfInstallmentsPerYear
						// * (Math.log(1 + (annualInterestRate / noOfInstallmentsPerYear))));

						double emiCalc = emi; // Temp Variables
						double temp = afterNPayments;

						List<Double> emis = new ArrayList<>();
						List<Integer> dates = new ArrayList<>();
						emis.add(emi);

						// List<EmiChangeRes> emiChangeResList = new ArrayList<EmiChangeRes>();
						int result = 0;
						if (token != null && referenceId != null) {
							logger.info("Fetching emiChange by referenceId");
							// if (calcService.fetchEmiChangeByRefId(referenceId).size() != 0) {
							int emiChangeByRefId = calcService.checkEmiChangeIsPresent(referenceId);
							if (emiChangeByRefId != 0) {
								logger.info("removing emiChange by referenceId");
								int result1 = calcService.removeEmiChangeByRefId(referenceId);
								if (result1 == 0) {
									logger.info("Error Occurred while removing data");
									CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
											appMessages.getError_occured_remove(), null, null);
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							}
						}
						if (emiChangeRequest.getEmiChangeReq() != null
								&& emiChangeRequest.getEmiChangeReq().size() != 0) {
							for (EmiChangeReq emiChangeReq : emiChangeRequest.getEmiChangeReq()) {

								double prevEmi = emiCalc;
								double remLoanAmount = temp;
								double increaseEmi = Double.parseDouble(emiChangeReq.getIncreasedEmi());
								revisedEmi = increaseEmi + prevEmi;
								String emiChangedDate = convertMonthYearToDate(emiChangeReq.getEmiChangedDate());
								int paymentsPaid = calculateMonth(loanDate, emiChangedDate);

								// After increase Years for Pay Off
								double yearsNeedToPayOff = -Math.log(1
										- (remLoanAmount * annualInterestRate / (revisedEmi * noOfInstallmentsPerYear)))
										/ (noOfInstallmentsPerYear
												* (Math.log(1 + (annualInterestRate / noOfInstallmentsPerYear))));
								// FinalOutStanding Amount
								double afterNPayRemLoanAmt = (remLoanAmount
										- (revisedEmi * noOfInstallmentsPerYear / annualInterestRate))
										* Math.pow((1 + (annualInterestRate / noOfInstallmentsPerYear)), paymentsPaid)
										+ (revisedEmi * noOfInstallmentsPerYear) / annualInterestRate;
								revisedTenure = yearsNeedToPayOff;
								emiCalc = revisedEmi;
								temp = afterNPayRemLoanAmt;
								emis.add(revisedEmi);
								dates.add(paymentsPaid); // add

								// add into table
								EmiChange emiChange = getValueEmiChange(emiChangeRequest);
								emiChange.setIncreasedEmi(increaseEmi);
								emiChange.setRevisedTenure(revisedTenure);
								emiChange.setRevisedEmi(revisedEmi);
								emiChange.setEmiChangedDate(emiChangeReq.getEmiChangedDate());
								emiChange.setTenure(Integer.parseInt(emiChangeRequest.getTenure()));
								emiChange.setTenureType(emiChangeRequest.getTenureType());
								if (token != null && referenceId != null) {
									logger.info("adding emiChange");
									result = calcService.addEmiChange(emiChange);
									if (result == 0) {
										logger.info("Error Occurred while adding data");
										CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
												appMessages.getError_occured(), null, null);
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
									}
								}
							}

							// amortisation
							List<AmortisationResponse> amortisation = new ArrayList<AmortisationResponse>();
							double opening = loanAmount;
							double interestPay = 0;
							double totalPrincipal = 0;
							double closing = 0;
							double loanPaid = 0;
							double tempAmt = loanAmount;
							double total = 0;
							Iterator<Integer> nPaidItr = dates.iterator();
							Iterator<Double> emiItr = emis.iterator();
							int noOfPaid = (int) nPaidItr.next();
							double emi1 = (double) emiItr.next();
							DateFormat monthFormatter = new SimpleDateFormat("MMM-yyyy");
							Date givenDate = null;
							try {
								givenDate = monthFormatter.parse(emiChangeRequest.getLoanDate());
							} catch (ParseException e) {
								logger.error(e.getMessage());
							}
							for (int i = 0; i < noOfInstallments && closing >= 0; i++) {
								if (i == noOfPaid) {
									opening = tempAmt;
									if (emiItr.hasNext()) {
										emi1 = (double) emiItr.next();
									}
									if (nPaidItr.hasNext()) {
										noOfPaid = (int) nPaidItr.next();
									}
								} else {
									if (tempAmt < 0) {
										opening = 0;
									} else {
										opening = tempAmt;
									}
								}
								double interest = opening * monthlyInterestRate;
								interestPay = interestPay + interest;

								if (i >= noOfInstallments) {
									totalPrincipal = 0;
									closing = 0;
								} else {
									totalPrincipal = emi1 - interest;
									closing = opening - totalPrincipal;
									// loanPaid = (interest / (interest + totalPrincipal)) * 100;
									loanPaid = closing / loanAmount * 100;
									tempAmt = closing;
								}
								total = (interestPay + loanAmount);
								Calendar c = Calendar.getInstance();
								c.setTime(givenDate);
								String newDate = monthFormatter.format(c.getTime());
								c.add(Calendar.MONTH, 1);
								Date nwDate = null;
								try {
									nwDate = monthFormatter.parse(monthFormatter.format(c.getTime()));
								} catch (ParseException e) {
									logger.error(e.getMessage());
								}

								givenDate = nwDate;
								AmortisationResponse amort = new AmortisationResponse();
								amort.setMonths(i + 1);
								amort.setOpening(roundingNumber(opening));
								amort.setInterest(roundingNumber(interest));
								amort.setTotalPrincipal(roundingNumber(totalPrincipal));
								amort.setClosing(roundingNumber(closing));
								amort.setLoanPaid(roundingNumber(loanPaid));
								amort.setDate(newDate);
								amortisation.add(amort);
							}

							EmiChangeResponse emiResponse = new EmiChangeResponse();
							emiResponse.setLoanAmount(loanAmount);
							emiResponse.setTenure(tenure);
							emiResponse.setRevisedTenure(roundingNumber(revisedTenure));
							emiResponse.setRevisedEmi(roundingNumber(revisedEmi));
							emiResponse.setEmi(roundingNumber(emi));
							emiResponse.setInterestPayable(roundingNumber(interestPay));
							emiResponse.setTotal(roundingNumber(total));
							emiResponse.setAmortisation(amortisation);
							CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
									appMessages.getEmi_calculated_successfully(), emiResponse, roleFieldRights);
							return ResponseEntity.ok().body(response);
						} else {
							EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
							emiCalculatorRequest.setReferenceId(emiChangeRequest.getReferenceId());
							emiCalculatorRequest.setLoanAmount(emiChangeRequest.getLoanAmount());
							emiCalculatorRequest.setTenure(emiChangeRequest.getTenure());
							emiCalculatorRequest.setTenureType(emiChangeRequest.getTenureType());
							emiCalculatorRequest.setInterestRate(emiChangeRequest.getInterestRate());
							emiCalculatorRequest.setDate(emiChangeRequest.getLoanDate());
							return calculateEmiCalculator(CalcConstants.EMICHANGE, emiCalculatorRequest,
									roleFieldRights, appMessages.getEmi_calculated_successfully(), token);
						}
					} else {
						if (loanAmtZeroCheck == 0) {
							logger.info("Loan amount Zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (tenureZeroCheck == 0) {
							logger.info("Tenure zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "TENURE", null, null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (rateZeroCheck == 0) {
							logger.info("Interest rate zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					logger.info("Zero validation error");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
							appMessages.getZero_validation_error(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (errors.isEmpty() == false) {
				logger.info("Validation errors");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			logger.info("Some Fields are empty");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getFields_cannot_be_empty(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private EmiChange getValueEmiChange(EmiChangeRequest emiChangeRequest) {
		EmiChange emiChange = new EmiChange();
		if (emiChangeRequest != null && emiChangeRequest.getLoanAmount() != null) {
			emiChange.setLoanAmount(Double.parseDouble(emiChangeRequest.getLoanAmount()));
		}
		if (emiChangeRequest != null && emiChangeRequest.getReferenceId() != null) {
			emiChange.setReferenceId(emiChangeRequest.getReferenceId());
		}
		if (emiChangeRequest != null && emiChangeRequest.getInterestRate() != null) {
			emiChange.setInterestRate(Double.parseDouble(emiChangeRequest.getInterestRate()));
		}
		if (emiChangeRequest != null && emiChangeRequest.getTenure() != null) {
			emiChange.setTenure(Integer.parseInt(emiChangeRequest.getTenure()));
		}
		if (emiChangeRequest != null && emiChangeRequest.getLoanDate() != null) {
			emiChange.setLoanDate(emiChangeRequest.getLoanDate());
		}
		return emiChange;
	}

	/**
	 * Method for InterestChange
	 * 
	 * @param RequestBody
	 *            contains the <code>InterestChangeRequest</code>
	 * @return ResponseEntity<> Success result or Error Response
	 */
	@ApiOperation(value = "calculate interestChange", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/calculateInterestChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> interestChange(@NonNull @RequestBody InterestChangeRequest interestChangeRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String referenceId = interestChangeRequest.getReferenceId();
		if (token != null && interestChangeRequest.getReferenceId() != null) {
			if (interestChangeRequest != null) {
				int screenId = interestChangeRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}

			logger.info("Fetching plan by referenceId");
			// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
			int plan = calcService.checkPlanIsPresentByReferenceId(referenceId);
			if (plan == 0) {
				// If party is not available return no record found
				logger.info("No record Found");
				CalcResponse response = responseWithData(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (interestChangeRequest.getLoanAmount() != null && interestChangeRequest.getInterestRate() != null
				&& interestChangeRequest.getTenure() != null && interestChangeRequest.getTenureType() != null
				&& interestChangeRequest.getLoanDate() != null) {
			errors = interestChangeRequestValidator.validate(interestChangeRequest);
			if (errors.isEmpty() == true) {
				double loanAmtZeroCheck = Double.parseDouble(interestChangeRequest.getLoanAmount());
				int tenureZeroCheck = Integer.parseInt(interestChangeRequest.getTenure());
				double rateZeroCheck = Double.parseDouble(interestChangeRequest.getInterestRate());
				if ((loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0)
						|| (loanAmtZeroCheck != 0 && tenureZeroCheck != 0)
						|| (tenureZeroCheck != 0 && rateZeroCheck != 0)
						|| (rateZeroCheck != 0 && loanAmtZeroCheck != 0)) {

					if (loanAmtZeroCheck != 0 && tenureZeroCheck != 0 && rateZeroCheck != 0) {
						double revisedTenure = 0;
						double revisedEmi = 0;
						double loanAmount = Double.parseDouble(interestChangeRequest.getLoanAmount());
						double annualInterestRate = Double.parseDouble(interestChangeRequest.getInterestRate()) / 100;
						double monthlyInterestRate = annualInterestRate / 12;
						double tenure;
						if (interestChangeRequest.getTenureType().equals(CalcConstants.MONTH)) {
							tenure = (Double.parseDouble(interestChangeRequest.getTenure())) / 12;
						} else {
							tenure = Integer.parseInt(interestChangeRequest.getTenure());
						}
						double noOfInstallments = tenure * 12;
						double emi = (loanAmount * monthlyInterestRate
								* (Math.pow((1 + monthlyInterestRate), noOfInstallments))
								/ (Math.pow((1 + monthlyInterestRate), noOfInstallments) - 1));
						double noOfInstallmentsPerYear = 12;
						// system date
						Date date = new Date();
						DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String currentDate = formatter.format(date);
						String interestChangedDate1 = null;
						if (interestChangeRequest.getInterestChangeReq() != null
								&& interestChangeRequest.getInterestChangeReq().size() > 0) {
							for (InterestChangeReq interestChangeReq : interestChangeRequest.getInterestChangeReq()) {
								interestChangedDate1 = convertMonthYearToDate(
										interestChangeReq.getInterestChangedDate());
							}
						} else {
							interestChangedDate1 = currentDate;
						}
						String loanDate = convertMonthYearToDate(interestChangeRequest.getLoanDate());
						int noOfPaymentsPaid = calculateMonth(loanDate, interestChangedDate1);
						double afterNPayments = (loanAmount - (emi * noOfInstallmentsPerYear / annualInterestRate))
								* Math.pow((1 + (annualInterestRate / noOfInstallmentsPerYear)), noOfPaymentsPaid)
								+ (emi * noOfInstallmentsPerYear / annualInterestRate);
						// double noOfYearsNeed = -Math
						// .log(1 - (loanAmount * annualInterestRate / (emi * noOfInstallmentsPerYear)))
						// / (noOfInstallmentsPerYear
						// * (Math.log(1 + (annualInterestRate / noOfInstallmentsPerYear))));
						double temp = afterNPayments;

						List<AmortisationResponse> amortisationResponse = new ArrayList<AmortisationResponse>();

						List<Double> rates = new ArrayList<>();
						rates.add(monthlyInterestRate);
						List<Integer> dates = new ArrayList<>();
						int result = 0;
						if (token != null && interestChangeRequest.getReferenceId() != null) {
							logger.info("Fetching interestChange by referenceId");
							// if (calcService.fetchInterestChangeByRefId(referenceId).size() != 0) {
							int intChange = calcService.checkInterestChangeIsPresent(referenceId);
							if (intChange != 0) {
								logger.info("removing interestChange by referenceId");
								int result1 = calcService.removeInterestChangeByRefId(referenceId);
								if (result1 == 0) {
									logger.info("Error Occurred while removing data");
									CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
											appMessages.getError_occured_remove(), null, null);
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							}
						}
						// Change Interest
						if (interestChangeRequest.getInterestChangeReq() != null
								&& interestChangeRequest.getInterestChangeReq().size() != 0) {
							for (InterestChangeReq interestChangeReq : interestChangeRequest.getInterestChangeReq()) {

								double previousEmi = emi;
								double remLoanAmount = temp;
								double interestRate = Double.parseDouble(interestChangeReq.getChangedRate()) / 100;
								double mInterestRate = interestRate / 12;
								revisedEmi = previousEmi;
								String interestChangedDate = convertMonthYearToDate(
										interestChangeReq.getInterestChangedDate());
								int paymentsPaid = calculateMonth(loanDate, interestChangedDate);
								double yearsNeedToPayOff = -Math.log(
										1 - (remLoanAmount * interestRate / (revisedEmi * noOfInstallmentsPerYear)))
										/ (noOfInstallmentsPerYear
												* (Math.log(1 + (interestRate / noOfInstallmentsPerYear))));
								double afterNPayRemLoanAmt = (remLoanAmount
										- (revisedEmi * noOfInstallmentsPerYear / interestRate))
										* Math.pow((1 + (interestRate / noOfInstallmentsPerYear)), paymentsPaid)
										+ (revisedEmi * noOfInstallmentsPerYear / interestRate);

								temp = afterNPayRemLoanAmt;
								rates.add(mInterestRate);
								dates.add(paymentsPaid);
								revisedTenure = yearsNeedToPayOff;
								// Add into Table
								InterestChange interestChange = getValueInterestChangeInfo(interestChangeRequest);
								interestChange.setReferenceId(interestChangeRequest.getReferenceId());
								interestChange.setChangedRate(Double.parseDouble(interestChangeReq.getChangedRate()));
								interestChange.setInterestChangedDate(interestChangeReq.getInterestChangedDate());
								interestChange.setTenure(Integer.parseInt(interestChangeRequest.getTenure()));
								interestChange.setRevisedTenure(revisedTenure);
								interestChange.setTenureType(interestChangeRequest.getTenureType());
								if (token != null && interestChangeRequest.getReferenceId() != null) {
									logger.info("Adding interestChange");
									result = calcService.addInterestChange(interestChange);
									if (result == 0) {
										logger.info("Error Occurred while adding data");
										CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
												appMessages.getError_occured(), null, null);
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
									}
								}
							}
							// Amortisation Schedule
							double opening = loanAmount;
							double interestPay = 0;
							double totalPrincipal = 0;
							double closing = 0;
							double loanPaid = 0;
							double tempAmt = loanAmount;
							double total = 0;
							Iterator<Integer> nPaidItr = dates.iterator();
							Iterator<Double> rateItr = rates.iterator();
							int noOfPaid = (int) nPaidItr.next();
							double mRate = (double) rateItr.next();
							DateFormat monthFormatter = new SimpleDateFormat("MMM-yyyy");
							Date givenDate = null;
							try {
								givenDate = monthFormatter.parse(interestChangeRequest.getLoanDate());
							} catch (ParseException e) {
								logger.error(e.getMessage());
							}
							for (int i = 0; i < noOfInstallments && closing >= 0; i++) {
								if (i == noOfPaid) {
									opening = tempAmt;
									if (rateItr.hasNext()) {
										mRate = (double) rateItr.next();
									}
									if (nPaidItr.hasNext()) {
										noOfPaid = (int) nPaidItr.next();
									}
								} else {
									if (tempAmt < 0) {
										opening = 0;
									} else {
										opening = tempAmt;
									}
								}
								double interest = opening * mRate;
								interestPay = interestPay + interest;

								if (i >= noOfInstallments) {
									totalPrincipal = 0;
									closing = 0;
								} else {
									totalPrincipal = emi - interest;
									closing = opening - totalPrincipal;
									// loanPaid = (interest / (interest + totalPrincipal)) * 100;
									loanPaid = closing / loanAmount * 100;
									tempAmt = closing;
								}
								total = (interestPay + loanAmount);
								// Calendar c = Calendar.getInstance();
								// c.setTime(givenDate);
								// c.add(Calendar.MONTH, 1);
								// String newDate = monthFormatter.format(c.getTime());
								// Date nwDate = null;
								// try {
								// nwDate = monthFormatter.parse(newDate);
								// } catch (ParseException e) {
								// e.printStackTrace();
								// }
								Calendar c = Calendar.getInstance();
								c.setTime(givenDate);
								String newDate = monthFormatter.format(c.getTime());
								c.add(Calendar.MONTH, 1);
								Date nwDate = null;
								try {
									nwDate = monthFormatter.parse(monthFormatter.format(c.getTime()));
								} catch (ParseException e) {
									logger.error(e.getMessage());
								}
								givenDate = nwDate;
								AmortisationResponse amortisation = new AmortisationResponse();
								amortisation.setMonths(i + 1);
								amortisation.setOpening(roundingNumber(opening));
								amortisation.setInterest(roundingNumber(interest));
								amortisation.setTotalPrincipal(roundingNumber(totalPrincipal));
								amortisation.setClosing(roundingNumber(closing));
								amortisation.setLoanPaid(roundingNumber(loanPaid));
								amortisation.setDate(newDate);
								amortisationResponse.add(amortisation);
							}
							// response with result and amortisation schedule

							InterestChangeResponse intResponse = new InterestChangeResponse();
							intResponse.setLoanAmount(loanAmount);
							intResponse.setTenure(tenure);
							intResponse.setEmi(roundingNumber(emi));
							intResponse.setRevisedTenure(roundingNumber(revisedTenure));
							intResponse.setInterestPayable(roundingNumber(interestPay));
							intResponse.setTotal(roundingNumber(total));
							intResponse.setAmortisation(amortisationResponse);
							CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
									appMessages.getInt_change_calculated_successfully(), intResponse, roleFieldRights);
							return ResponseEntity.ok().body(response);
						} else {
							EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
							emiCalculatorRequest.setReferenceId(interestChangeRequest.getReferenceId());
							emiCalculatorRequest.setLoanAmount(interestChangeRequest.getLoanAmount());
							emiCalculatorRequest.setTenure(interestChangeRequest.getTenure());
							emiCalculatorRequest.setTenureType(interestChangeRequest.getTenureType());
							emiCalculatorRequest.setInterestRate(interestChangeRequest.getInterestRate());
							emiCalculatorRequest.setDate(interestChangeRequest.getLoanDate());
							return calculateEmiCalculator(CalcConstants.INTERESTCHANGE, emiCalculatorRequest,
									roleFieldRights, appMessages.getInt_change_calculated_successfully(), token);
						}
					} else {
						if (loanAmtZeroCheck == 0) {
							logger.info("Loan amount Zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (tenureZeroCheck == 0) {
							logger.info("Tenure zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "TENURE", null, null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else if (rateZeroCheck == 0) {
							logger.info("Interest rate zero validation error");
							CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
									appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE", null,
									null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					logger.info("Zero validation error");
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
							appMessages.getZero_validation_error(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (errors.isEmpty() == false) {
				logger.info("Validation errors");
				CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			logger.info("Some Fields are empty");
			CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getFields_cannot_be_empty(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private InterestChange getValueInterestChangeInfo(InterestChangeRequest interestChangeRequest) {
		InterestChange interestChange = new InterestChange();
		if (interestChangeRequest != null && interestChangeRequest.getLoanAmount() != null) {
			interestChange.setLoanAmount(roundingNumber(Double.parseDouble(interestChangeRequest.getLoanAmount())));
		}
		if (interestChangeRequest != null && interestChangeRequest.getInterestRate() != null) {
			interestChange.setInterestRate(Double.parseDouble(interestChangeRequest.getInterestRate()));
		}
		if (interestChangeRequest != null && interestChangeRequest.getTenure() != null) {
			interestChange.setTenure(Integer.parseInt(interestChangeRequest.getTenure()));
		}
		if (interestChangeRequest != null && interestChangeRequest.getLoanDate() != null) {
			interestChange.setLoanDate(interestChangeRequest.getLoanDate());
		}
		return interestChange;
	}

	// /**
	// * Method for EmiInterestChange
	// *
	// * @param RequestBody
	// * contains the <code>EmiInterestChangeRequest</code>
	// * @return ResponseEntity<> Success result or Error Response
	// */
	// @RequestMapping(value = "/calculateEmiInterestChange", method =
	// RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes =
	// MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> emiInterestChange(@RequestBody
	// EmiInterestChangeRequest emiInterestChangeRequest,
	// HttpServletRequest request) {
	// int screenId = emiInterestChangeRequest.getScreenId();
	// if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
	// return rightsAuthentication(screenId, request,
	// screenRightsConstants.getAdd());
	// }
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// String referenceId = emiInterestChangeRequest.getReferenceId();
	// if (calcService.fetchPlanByReferenceId(referenceId) == null) {
	// // If party is not available return no record found
	// logger.info("No record Found");
	// CalcResponse response = messageResponse(CalcConstants.NO_RECORD_FOUND,
	// appMessages.getNo_record_found());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// } else {
	// // validating emiInterestChange Request
	// errors =
	// emiInterestChangeRequestValidator.validate(emiInterestChangeRequest);
	// if (errors.isEmpty() == true) {
	// if (emiInterestChangeRequest.getLoanAmount() != null
	// && emiInterestChangeRequest.getInterestRate() != null
	// && emiInterestChangeRequest.getTenure() != null
	// && emiInterestChangeRequest.getTenureType() != null
	// && emiInterestChangeRequest.getReferenceId() != null
	// && emiInterestChangeRequest.getLoanDate() != null) {
	//
	// double loanAmount =
	// Double.parseDouble(emiInterestChangeRequest.getLoanAmount());
	// double annualInterestRate =
	// Double.parseDouble(emiInterestChangeRequest.getInterestRate()) / 100;
	// double monthlyInterestRate = annualInterestRate / 12;
	// double tenure;
	// if (emiInterestChangeRequest.getTenureType().equals(CalcConstants.MONTH)) {
	// tenure = (Double.parseDouble(emiInterestChangeRequest.getTenure())) / 12;
	// } else {
	// tenure = Double.parseDouble(emiInterestChangeRequest.getTenure());
	// }
	// double noOfInstallments = tenure * 12;
	// double noOfInstallmentsPerYear = 12;
	// // system date
	// Date date = new Date();
	// DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	// String currentDate = formatter.format(date);
	// String loanDate =
	// convertMonthYearToDate(emiInterestChangeRequest.getLoanDate());
	// int noOfPaymentsPaid = calculateMonth(loanDate, currentDate);
	// // Find EMI
	// double emi = (loanAmount * monthlyInterestRate
	// * Math.pow(1 + monthlyInterestRate, noOfInstallments))
	// / (Math.pow(1 + monthlyInterestRate, noOfInstallments) - 1);
	// // OutStanding Amount
	// double afterNPayments = (loanAmount - (emi * noOfInstallmentsPerYear /
	// annualInterestRate))
	// * Math.pow((1 + (annualInterestRate / noOfInstallmentsPerYear)),
	// noOfPaymentsPaid)
	// + (emi * noOfInstallmentsPerYear) / annualInterestRate;
	// // Years for Pay Off
	// // double noOfYearsNeed = -Math
	// // .log(1 - (loanAmount * annualInterestRate / (emi *
	// noOfInstallmentsPerYear)))
	// // / (noOfInstallmentsPerYear
	// // * (Math.log(1 + (annualInterestRate / noOfInstallmentsPerYear))));
	//
	// double emiCalc = emi; // Temp Variables
	// double temp = afterNPayments;
	//
	// List<Double> emis = new ArrayList<>();
	// List<Integer> dates = new ArrayList<>();
	// emis.add(emi);
	// List<Double> rates = new ArrayList<>();
	// rates.add(monthlyInterestRate);
	// int result = 0;
	// if (calcService.fetchEmiInterestChangeByRefId(referenceId).size() != 0) {
	// int result1 = calcService.removeEmiInterestChangeByRefId(referenceId);
	// if (result1 == 0) {
	// logger.info("Error Occurred while removing data");
	// CalcResponse response = messageResponse(CalcConstants.ERROR_CODE,
	// appMessages.getError_occured_remove());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// }
	// DateFormat monthFormatter = new SimpleDateFormat("MMM-yyyy");
	// for (EmiInterestChangeReq emiIntChangeReq :
	// emiInterestChangeRequest.getEmiInterestChangeReq()) {
	// double prevEmi = emiCalc;
	// double remLoanAmount = temp;
	// double changedRate = Double.parseDouble(emiIntChangeReq.getChangedRate()) /
	// 100;
	// double mRate = changedRate / 12.0;
	// double increaseEmi = Double.parseDouble(emiIntChangeReq.getIncreasedEmi());
	// double revisedEmi = increaseEmi + prevEmi;
	// String changedDate =
	// convertMonthYearToDate(emiIntChangeReq.getChangedDate());
	// int paymentsPaid = calculateMonth(loanDate, changedDate);
	//
	// // double yearsNeedToPayOff = -Math
	// // .log(1 - (remLoanAmount * changedRate / (revisedEmi *
	// // noOfInstallmentsPerYear)))
	// // / (noOfInstallmentsPerYear * (Math.log(1 + (changedRate /
	// // noOfInstallmentsPerYear))));
	// double afterNPayRemLoanAmt = (remLoanAmount
	// - (revisedEmi * noOfInstallmentsPerYear / changedRate))
	// * Math.pow((1 + (changedRate / noOfInstallmentsPerYear)), paymentsPaid)
	// + (revisedEmi * noOfInstallmentsPerYear) / changedRate;
	// emiCalc = revisedEmi;
	// temp = afterNPayRemLoanAmt;
	//
	// emis.add(revisedEmi);
	// dates.add((int) paymentsPaid);
	// rates.add(mRate);
	//
	// // Add into Table
	// EmiInterestChange emiInterestChange =
	// getValueEmiInterestChange(emiInterestChangeRequest);
	// emiInterestChange.setChangedRate(Double.parseDouble(emiIntChangeReq.getChangedRate()));
	// emiInterestChange.setIncreasedEmi(Double.parseDouble(emiIntChangeReq.getIncreasedEmi()));
	// emiInterestChange.setChangedDate(emiIntChangeReq.getChangedDate());
	// emiInterestChange.setTenure(Integer.parseInt(emiInterestChangeRequest.getTenure()));
	// emiInterestChange.setTenureType(emiInterestChangeRequest.getTenureType());
	// result = calcService.addEmiInterestChange(emiInterestChange);
	// if (result == 0) {
	// logger.info("Error Occurred while adding data");
	// CalcResponse response = messageResponse(CalcConstants.ERROR_CODE,
	// appMessages.getError_occured());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// }
	//
	// // amortisation
	// List<AmortisationResponse> amortisation = new
	// ArrayList<AmortisationResponse>();
	//
	// double opening = loanAmount;
	// double interestPay = 0;
	// double totalPrincipal = 0;
	// double closing = 0;
	// double loanPaid = 0;
	// double tempAmt = loanAmount;
	// double total = 0;
	// Iterator<Integer> nPaidItr = dates.iterator();
	// Iterator<Double> rateItr = rates.iterator();
	// Iterator<Double> emiItr = emis.iterator();
	// int nPaid = (int) nPaidItr.next();
	// double mRate = (double) rateItr.next();
	// double emi1 = (double) emiItr.next();
	// Date givenDate = null;
	// try {
	// givenDate = monthFormatter.parse(emiInterestChangeRequest.getLoanDate());
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// for (int i = 0; i < noOfInstallments; i++) {
	// if (i == nPaid) {
	// opening = tempAmt;
	// if (rateItr.hasNext()) {
	// mRate = (double) rateItr.next();
	// }
	// if (emiItr.hasNext()) {
	// emi1 = (double) emiItr.next();
	// }
	// if (nPaidItr.hasNext()) {
	// nPaid = (int) nPaidItr.next();
	// }
	// } else {
	// if (tempAmt < 0) {
	// opening = 0;
	// } else {
	// opening = tempAmt;
	// }
	// }
	// double interest = opening * mRate;
	// interestPay = interestPay + interest;
	//
	// if (i >= noOfInstallments) {
	// totalPrincipal = 0;
	// closing = 0;
	// } else {
	// totalPrincipal = emi1 - interest;
	// closing = opening - totalPrincipal;
	// loanPaid = (interest / (interest + totalPrincipal)) * 100;
	// tempAmt = closing;
	// }
	// total = (interestPay + loanAmount);
	// // Calendar c = Calendar.getInstance();
	// // c.setTime(givenDate);
	// // c.add(Calendar.MONTH, 1);
	// // String newDate = monthFormatter.format(c.getTime());
	// // Date nwDate = null;
	// // try {
	// // nwDate = monthFormatter.parse(newDate);
	// // } catch (ParseException e) {
	// // e.printStackTrace();
	// // }
	// Calendar c = Calendar.getInstance();
	// c.setTime(givenDate);
	// String newDate = monthFormatter.format(c.getTime());
	// c.add(Calendar.MONTH, 1);
	// Date nwDate = null;
	// try {
	// nwDate = monthFormatter.parse(monthFormatter.format(c.getTime()));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// givenDate = nwDate;
	// AmortisationResponse amort = new AmortisationResponse();
	// amort.setMonths(i + 1);
	// amort.setOpening(roundingNumber(opening));
	// amort.setInterest(roundingNumber(interest));
	// amort.setTotalPrincipal(roundingNumber(totalPrincipal));
	// amort.setClosing(roundingNumber(closing));
	// amort.setLoanPaid(roundingNumber(loanPaid));
	// amort.setDate(newDate);
	// amortisation.add(amort);
	// }
	//
	// EmiIntChangeResponse emiIntResponse = new EmiIntChangeResponse();
	// emiIntResponse.setLoanAmount(loanAmount);
	// emiIntResponse.setTenure(tenure);
	// emiIntResponse.setEmi(roundingNumber(emi));
	// emiIntResponse.setInterestPayable(roundingNumber(interestPay));
	// emiIntResponse.setTotal(roundingNumber(total));
	// emiIntResponse.setAmortisation(amortisation);
	//
	// // Response with result and amortisation schedule
	// CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
	// appMessages.getValue_calculated_successfully(), emiIntResponse);
	// return ResponseEntity.ok().body(response);
	// } else {
	// logger.info("Fields empty");
	// CalcResponse response = messageResponse(CalcConstants.ERROR_CODE,
	// appMessages.getFields_cannot_be_empty());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// } else if (errors.isEmpty() == false) {
	// logger.info("Validation error");
	// CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
	// appMessages.getError(), errors);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// }
	// return new ResponseEntity<>(HttpStatus.OK);
	//
	// }
	//
	// private EmiInterestChange getValueEmiInterestChange(EmiInterestChangeRequest
	// emiInterestChangeRequest) {
	// EmiInterestChange emiInterestChange = new EmiInterestChange();
	//
	// if (emiInterestChangeRequest != null &&
	// emiInterestChangeRequest.getReferenceId() != null) {
	// emiInterestChange.setReferenceId(emiInterestChangeRequest.getReferenceId());
	// }
	// if (emiInterestChangeRequest != null &&
	// emiInterestChangeRequest.getLoanAmount() != null) {
	// emiInterestChange.setLoanAmount(Double.parseDouble(emiInterestChangeRequest.getLoanAmount()));
	// }
	// if (emiInterestChangeRequest != null && emiInterestChangeRequest.getTenure()
	// != null) {
	// emiInterestChange.setTenure(Integer.parseInt(emiInterestChangeRequest.getTenure()));
	// }
	// if (emiInterestChangeRequest != null &&
	// emiInterestChangeRequest.getInterestRate() != null) {
	// emiInterestChange.setInterestRate(Double.parseDouble(emiInterestChangeRequest.getInterestRate()));
	// }
	// if (emiInterestChangeRequest != null &&
	// emiInterestChangeRequest.getLoanDate() != null) {
	// emiInterestChange.setLoanDate(emiInterestChangeRequest.getLoanDate());
	// }
	// return emiInterestChange;
	//
	// }

	private int calculateMonth(String date1, String date2) {
		int nPaid = 0;
		String[] loanDate1 = date1.split("-");
		int year1 = Integer.parseInt(loanDate1[2]);
		int month1 = Integer.parseInt(loanDate1[1]);
		int day1 = Integer.parseInt(loanDate1[0]);

		// String partPayDate = partialPaymentReq.get(i).getDate();
		String[] out2 = date2.split("-");
		int year2 = Integer.parseInt(out2[2]);
		int month2 = Integer.parseInt(out2[1]);
		int day2 = Integer.parseInt(out2[0]);
		if (day2 >= day1) {
			nPaid = 0 + (year2 - year1) * 12 + month2 - month1;
		} else {
			nPaid = -1 + (year2 - year1) * 12 + month2 - month1;
		}
		return nPaid;
	}

	/**
	 * Method to fetch FinancialPlanning
	 * 
	 * @param RequestBody
	 *            contains the <code>CalcIdRequest</code>
	 * @return ResponseEntity<> FinancialPlanning or Error Response
	 */
	@ApiOperation(value = "fetch financialPlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-financialPlanning", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchFinancialPlanningById(@NonNull @RequestBody CalcIdRequest idRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		String referenceId = idRequest.getId();
		if (token != null && referenceId != null) {
			if (idRequest != null) {
				int screenId = idRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			logger.info("Fetching plan by referenceId");
			long loggedPartyId = getSignedInParty();
			Plan plan = calcService.fetchPlanByReferenceId(referenceId);

			if (plan == null) {
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
						roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
			if (loggedPartyId != plan.getPartyId() && loggedPartyId != plan.getParentPartyId()) {
				int result = calcService.checkSharedAdvisor(referenceId, loggedPartyId);
				if (result == 0) {
					logger.error("This Plan not shared to the advisor");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getNot_shared_plan_advisor(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
		// Fetch goal, cashflow, cashflowsummary, networth,
		// networthsummary,priority,insurance, riskprofile, risksummary
		// For this partyId
		logger.info("Fetching financial-planning by referenceId");
		FinancialPlanning financialPlan = new FinancialPlanning();
		// financialPlan.setGoal(calcService.fetchGoalByReferenceId(referenceId));
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		cashFlowList = fetchAllCashFlowByRefId(referenceId);
		List<Networth> networthList = new ArrayList<Networth>();
		networthList = fetchAllNetworthByRefId(referenceId);
		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList = fetchAllPriorityByRefId(referenceId);
		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		logger.info("Fetching risk profile by referenceId");
		riskProfileList = calcService.fetchRiskProfileByRefId(referenceId);
		financialPlan.setCashFlow(cashFlowList);
		logger.info("Fetching cashflow summary by referenceId");
		financialPlan.setCashFlowSummary(calcService.fetchCashFlowSummaryByRefId(referenceId));
		financialPlan.setNetworth(networthList);
		logger.info("Fetching networth summary by referenceId");
		financialPlan.setNetworthSummary(calcService.fetchNetworthSummaryByRefId(referenceId));
		financialPlan.setPriority(priorityList);
		logger.info("Fetching insurance item by referenceId");
		financialPlan.setInsurance(calcService.fetchInsuranceItemByRefId(referenceId));
		// financialPlan.setRiskProfile(riskProfileList);
		// financialPlan.setRiskSummary(calcService.fetchRiskSummaryByRefId(referenceId));
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), financialPlan,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	// fetch Loan Planning

	/**
	 * Method to fetch Loan Planning
	 * 
	 * @param RequestBody
	 *            contains the <code>CalcIdRequest</code>
	 * @return ResponseEntity<> LoanPlanning or Error Response
	 */
	@ApiOperation(value = "fetch loanPlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-loanPlanning", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchLoanPlanningById(@NonNull @RequestBody CalcIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String referenceId = idRequest.getId();
		logger.info("Fetching plan by referenceId");
		long loggedPartyId = getSignedInParty();
		Plan plan = calcService.fetchPlanByReferenceId(referenceId);
		if (plan == null) {
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
					roleFieldRights);
			return ResponseEntity.ok().body(response);

		} else {
			if (loggedPartyId != plan.getPartyId() && loggedPartyId != plan.getParentPartyId()) {
				int result = calcService.checkSharedAdvisor(referenceId, loggedPartyId);
				if (result == 0) {
					logger.error("This Plan not shared to the advisor");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getNot_shared_plan_advisor(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
			// Fetch Emicalculator, emicapacity, partialpayment,emichange,intererstcahnge,
			// emiinterestchange
			logger.info("Fetching loan-planning by referenceId");
			LoanPlanning loanPlanning = new LoanPlanning();
			loanPlanning.setEmiCalculator(calcService.fetchEmiCalculatorByRefId(referenceId));
			loanPlanning.setEmiCapacity(calcService.fetchEmiCapacityByRefId(referenceId));
			loanPlanning.setPartialPayment(calcService.fetchPartialPaymentByRefId(referenceId));
			loanPlanning.setEmiChange(calcService.fetchEmiChangeByRefId(referenceId));
			loanPlanning.setInterestChange(calcService.fetchInterestChangeByRefId(referenceId));
			// loanPlanning.setEmiInterestChange(calcService.fetchEmiInterestChangeByRefId(referenceId));
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), loanPlanning,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	// fetch Investment Planning

	/**
	 * Method to fetch Investment Planning
	 * 
	 * @param RequestBody
	 *            contains the <code>CalcIdRequest</code>
	 * @return ResponseEntity<> InvestmentPlanning or Error Response
	 */
	@ApiOperation(value = "fetch Investment Planning", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-investmentPlanning", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchInvestmentPlanningById(@NonNull @RequestBody CalcIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String referenceId = idRequest.getId();
		logger.info("Fetching plan by referenceId");
		long loggedPartyId = getSignedInParty();
		Plan plan = calcService.fetchPlanByReferenceId(referenceId);
		if (plan == null) {
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
					roleFieldRights);
			return ResponseEntity.ok().body(response);

		} else {

			if (loggedPartyId != plan.getPartyId() && loggedPartyId != plan.getParentPartyId()) {
				int result = calcService.checkSharedAdvisor(referenceId, loggedPartyId);
				if (result == 0) {
					logger.error("This Plan not shared to the advisor");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getNot_shared_plan_advisor(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
			// Fetch FutureValue, TargetValue, RateFinder, TenureFinder
			logger.info("Fetching investment-planning by referenceId");
			InvestmentPlanning investmentPlanning = new InvestmentPlanning();
			investmentPlanning.setFutureValue(calcService.fetchFutureValueByRefId(referenceId));
			investmentPlanning.setTargetValue(calcService.fetchTargetValueByRefId(referenceId));
			investmentPlanning.setRateFinder(calcService.fetchRateFinderByRefId(referenceId));
			investmentPlanning.setTenureFinder(calcService.fetchTenureFinderByRefId(referenceId));
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(),
					investmentPlanning, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	// fetch Goal Planning

	/**
	 * Method to fetch Goal Planning
	 * 
	 * @param RequestBody
	 *            contains the <code>CalcIdRequest</code>
	 * @return ResponseEntity<> GoalPlanning or Error Response
	 */
	@ApiOperation(value = "fetch goalPlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-goalPlanning", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchGoalPlanningById(@NonNull @RequestBody CalcIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String referenceId = idRequest.getId();
		logger.info("Fetching plan by referenceId");
		long loggedPartyId = getSignedInParty();
		Plan plan = calcService.fetchPlanByReferenceId(referenceId);
		if (plan == null) {
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
					roleFieldRights);
			return ResponseEntity.ok().body(response);

		} else {
			if (loggedPartyId != plan.getPartyId() && loggedPartyId != plan.getParentPartyId()) {
				int result = calcService.checkSharedAdvisor(referenceId, loggedPartyId);
				if (result == 0) {
					logger.error("This Plan not shared to the advisor");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getNot_shared_plan_advisor(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
			// Fetch goals
			GoalPlanning goalPlanning = new GoalPlanning();
			logger.info("Fetching goal by referenceId");
			goalPlanning.setGoal(calcService.fetchGoalByReferenceId(referenceId));
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), goalPlanning,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	// fetch RiskProfile Planning

	/**
	 * Method to fetch RiskProfile Planning
	 * 
	 * @param RequestBody
	 *            contains the <code>CalcIdRequest</code>
	 * @return ResponseEntity<> RiskProfilePlanning or Error Response
	 */
	@ApiOperation(value = "fetch riskProfilePlan", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-riskProfilePlanning", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRiskProfilePlanningById(@NonNull @RequestBody CalcIdRequest idRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		String referenceId = idRequest.getId();
		if (token != null && referenceId != null) {
			if (idRequest != null) {
				int screenId = idRequest.getScreenId();
				if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
					roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
					if (roleFieldRights == null) {
						CalcResponse response = responseWithData(CalcConstants.ERROR_CODE,
								appMessages.getAccess_denied(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (screenId == 0) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			logger.info("Fetching plan by referenceId");
			long loggedPartyId = getSignedInParty();
			Plan plan = calcService.fetchPlanByReferenceId(referenceId);
			if (plan == null) {
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), plan,
						roleFieldRights);
				return ResponseEntity.ok().body(response);

			}
			if (loggedPartyId != plan.getPartyId() && loggedPartyId != plan.getParentPartyId()) {
				int result = calcService.checkSharedAdvisor(referenceId, loggedPartyId);
				if (result == 0) {
					logger.error("This Plan not shared to the advisor");
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getNot_shared_plan_advisor(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
		// Fetch RiskProfile
		logger.info("Fetching riskProfile-Planning by referenceId");
		RiskProfilePlanning riskProfilePlanning = new RiskProfilePlanning();
		riskProfilePlanning.setRiskProfile(calcService.fetchRiskProfileByRefId(referenceId));
		riskProfilePlanning.setRiskSummary(calcService.fetchRiskSummaryByRefId(referenceId));
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(),
				riskProfilePlanning, roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	// private List<RiskProfile> fetchAllRiskProfileByPartyId(long partyId) {
	// List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
	// List<RiskQuestionaire> riskQuestionaireList =
	// calcService.fetchRiskQuestionaireList();
	// for (RiskQuestionaire riskQuestionaire : riskQuestionaireList) {
	// RiskProfile riskProfile =
	// calcService.fetchRiskProfileByPartyIdAndAnswerId(partyId,
	// riskQuestionaire.getAnswerId());
	// if (riskProfile == null) {
	// RiskProfile riskProfile1 = new RiskProfile();
	// riskProfile1.setPartyId(partyId);
	// riskProfile1.setAnswerId(riskQuestionaire.getAnswerId());
	// riskProfile1.setAnswer(riskQuestionaire.getAnswer());
	// riskProfile1.setQuestionId(riskQuestionaire.getQuestionId());
	// riskProfile1.setQuestion(riskQuestionaire.getQuestion());
	// riskProfileList.add(riskProfile1);
	// } else {
	// riskProfileList.add(riskProfile);
	// }
	// }
	// return riskProfileList;
	// }

	private List<Networth> fetchAllNetworthByRefId(String referenceId) {
		List<Networth> networthList = new ArrayList<Networth>();
		List<Account> accountList = calcService.fetchAccountList();
		for (Account account : accountList) {
			Networth networth = calcService.fetchNetworthByRefIdAndEntryId(referenceId, account.getAccountEntryId());
			if (networth == null) {
				Networth networth1 = new Networth();
				networth1.setReferenceId(referenceId);
				networth1.setAccountEntryId(account.getAccountEntryId());
				networth1.setAccountEntry(account.getAccountEntry());
				networth1.setAccountTypeId(account.getAccountTypeId());
				AccountType accountType = calcService.fetchAccountTypeByTypeId(account.getAccountTypeId());
				networth1.setAccountType(accountType.getAccountType());
				networthList.add(networth1);
			} else {
				networthList.add(networth);
			}
		}
		return networthList;

	}

	private List<CashFlow> fetchAllCashFlowByRefId(String referenceId) {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		List<CashFlowItem> cashFlowItemList = calcService.fetchCashFlowItemList();
		for (CashFlowItem cashFlowItem : cashFlowItemList) {
			CashFlow cashFlow = calcService.fetchCashFlowByRefIdAndItemId(referenceId,
					cashFlowItem.getCashFlowItemId());
			if (cashFlow == null) {
				CashFlow cashFlow1 = new CashFlow();
				cashFlow1.setReferenceId(referenceId);
				cashFlow1.setCashFlowItemId(cashFlowItem.getCashFlowItemId());
				cashFlow1.setCashFlowItem(cashFlowItem.getCashFlowItem());
				cashFlow1.setCashFlowItemTypeId(cashFlowItem.getCashFlowItemTypeId());
				CashFlowItemType cashFlowItemType = calcService
						.fetchCashFlowItemTypeByTypeId(cashFlowItem.getCashFlowItemTypeId());
				cashFlow1.setCashFlowItemType(cashFlowItemType.getCashFlowItemType());
				cashFlowList.add(cashFlow1);
			} else {
				cashFlowList.add(cashFlow);
			}
		}
		return cashFlowList;
	}

	private List<Priority> fetchAllPriorityByRefId(String referenceId) {
		List<Priority> priorityList = new ArrayList<Priority>();
		List<PriorityItem> priorityItemList = calcService.fetchPriorityItemList();
		List<Urgency> urgencyList = calcService.fetchUrgencyList();
		Urgency urgency = new Urgency();
		for (Urgency urgency1 : urgencyList) {
			urgency.setUrgencyId(urgency1.getUrgencyId());
			urgency.setValue(urgency1.getValue());
		}
		for (PriorityItem priorityItem : priorityItemList) {
			Priority priority = calcService.fetchPriorityByRefIdAndItemId(referenceId,
					priorityItem.getPriorityItemId());
			if (priority == null) {
				Priority priority1 = new Priority();
				priority1.setReferenceId(referenceId);
				priority1.setPriorityItemId(priorityItem.getPriorityItemId());
				priority1.setPriorityItem(priorityItem.getPriorityItem());
				priorityList.add(priority1);
			} else {
				priorityList.add(priority);
			}
		}
		return priorityList;
	}

	// LookUp Table Fetch Services
	/**
	 * Method to fetch AccountType
	 * 
	 * @param null
	 * @return ResponseEntity<> List<AccountType> or Error Response
	 */
	@ApiOperation(value = "fetch accountType list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-accountType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAccountTypeList() {
		logger.info("Fetching account type list");
		List<AccountType> accountTypeList = calcService.fetchAccountTypeList();
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), accountTypeList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch Account
	 * 
	 * @param null
	 * @return ResponseEntity<> List<Account> or Error Response
	 */
	@ApiOperation(value = "fetch account list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAccountList() {
		List<Account> accountList = calcService.fetchAccountList();
		logger.info("Fetching account list");
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), accountList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch CashFlowItemType
	 * 
	 * @param null
	 * @return ResponseEntity<> List<CashFlowItemType> or Error Response
	 */
	@ApiOperation(value = "fetch cashFlowItemType list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-cashFlowItemType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchCashFlowItemTypeList() {
		List<CashFlowItemType> cashFlowItemTypeList = calcService.fetchCashFlowItemTypeList();
		logger.info("Fetching cashFlowItemType list");
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(),
				cashFlowItemTypeList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch CashFlowItem
	 * 
	 * @param null
	 * @return ResponseEntity<> List<CashFlowItem> or Error Response
	 */
	@ApiOperation(value = "fetch cashFlowItem list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-cashFlowItem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchCashFlowItemList() {
		List<CashFlowItem> cashFlowItemList = calcService.fetchCashFlowItemList();
		logger.info("Fetching cashFlowItem list");
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), cashFlowItemList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch PriorityItem
	 * 
	 * @param null
	 * @return ResponseEntity<> List<PriorityItem> or Error Response
	 */
	@ApiOperation(value = "fetch priorityItem list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-priorityItem", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchPriorityItemList() {
		List<PriorityItem> priorityItemList = calcService.fetchPriorityItemList();
		logger.info("Fetching priorityItem list");
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), priorityItemList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch Urgency
	 * 
	 * @param null
	 * @return ResponseEntity<> List<Urgency> or Error Response
	 */
	@ApiOperation(value = "fetch urgency list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-urgency", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchUrgencyList() {
		List<Urgency> urgencyList = calcService.fetchUrgencyList();
		logger.info("Fetching urgency list");
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), urgencyList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch RiskPortfolio
	 * 
	 * @param null
	 * @return ResponseEntity<> List<RiskPortfolio> or Error Response
	 */
	@ApiOperation(value = "fetch riskPortfolio list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-riskPortfolio", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRiskPortfolioList() {
		List<RiskPortfolio> riskPortfolioList = calcService.fetchRiskPortfolioList();
		logger.info("Fetching riskPortfolio list");
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(),
				riskPortfolioList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to fetch Question
	 * 
	 * @param null
	 * @return ResponseEntity<> List<String> or Error Response
	 */
	@ApiOperation(value = "fetch riskQuestionaire list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-riskQuestionaire", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRiskQuestionaireList() {
		logger.info("Fetching riskQuestionaire list");
		List<String> questionIds = calcService.fetchQuestionIdFromRiskQuestionaire();
		List<RiskQuestionaireResponse> riskQuestionaireResponseList = new ArrayList<>();
		if (questionIds != null && questionIds.size() != 0) {
			for (String questionId : questionIds) {
				List<RiskQuestionaire> riskQuestionaireList = calcService.fetchRiskQuestionaireByQuestionId(questionId);
				String question = calcService.fetchQuestionByQuestionId(questionId);
				RiskQuestionaireResponse riskQuestionaireResponse = new RiskQuestionaireResponse();
				riskQuestionaireResponse.setQuestionId(questionId);
				riskQuestionaireResponse.setQuestion(question);
				List<AnswerRes> answer = new ArrayList<>();
				if (riskQuestionaireList != null && riskQuestionaireList.size() != 0) {
					for (RiskQuestionaire riskQuestionaire : riskQuestionaireList) {
						AnswerRes ans = new AnswerRes();
						ans.setAnswerId(riskQuestionaire.getAnswerId());
						ans.setAnswer(riskQuestionaire.getAnswer());
						answer.add(ans);
					}
				}
				riskQuestionaireResponse.setAnswerRes(answer);
				riskQuestionaireResponseList.add(riskQuestionaireResponse);
			}
		}
		// Response like question and their options
		CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(),
				riskQuestionaireResponseList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to add Query to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>QueryRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of Query
	 *         addition or ErrorResponse
	 */
	@ApiOperation(value = "create calc query", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createCalcQuery", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createCalcQuery(@NonNull @RequestBody CalcQueryRequest queryRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (queryRequest != null) {
			int screenId = queryRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (queryRequest.getPostedToPartyId() == null || queryRequest.getPostedToPartyId().size() == 0
				|| queryRequest.getReferenceId() == null || queryRequest.getPlans().size() == 0) {
			logger.error("Mandatory fields");
			CalcResponse response = messageResponse(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_calcQuery());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			for (long partyId : queryRequest.getPostedToPartyId()) {
				int party = calcService.checkPartyIsPresent(partyId); // Checking the partyId is present
				if (party == 0) {
					logger.error("No record found");
					CalcResponse response = messageResponse(CalcConstants.NO_RECORD_FOUND,
							appMessages.getParty_not_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			List<CalcQuery> queryList = getValueQueryReq(queryRequest);// get value Method call for input
			boolean checked = queryRequest.isChecked();
			int result = calcService.createQuery(queryList, checked);
			if (result == 0) {
				logger.error("Error occured while adding data into table");
				CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// // Mail sending
			// for (CalcQuery calcQuery : queryList) {
			// Party party = calcService.fetchParty(calcQuery.getPostedToPartyId());
			// Advisor advisor = advisorService.fetchByAdvisorId(party.getRoleBasedId());
			// List<String> toUsers = new ArrayList<>();
			// toUsers.add(advisor.getEmailId());
			// sendMail.sendMailMessage(MailConstants.PLAN_SHARE, toUsers,
			// mailConstants.getFromUser(), null,
			// advisor.getName());
			// }
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getPlan_shared_successfully(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to add Query to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>QueryRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of Query
	 *         addition or ErrorResponse
	 */
	@ApiOperation(value = "create comment queries", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/commentQueries", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> commentQueries(@NonNull @RequestBody CommentQueryRequest commentQueryRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (commentQueryRequest != null) {
			int screenId = commentQueryRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		long calcQueryId = commentQueryRequest.getCalcQueryId();
		String query = commentQueryRequest.getQuery();
		long senderId = commentQueryRequest.getSenderId();
		if (calcQueryId != 0 && query != null && senderId != 0) {
			int calcQuery = calcService.checkCalcQueryIsPresent(calcQueryId);
			if (calcQuery == 0) {
				logger.info("No record found");
				CalcResponse response = messageResponse(CalcConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int party = calcService.checkPartyIsPresent(senderId);
				if (party == 0) {
					logger.info("No record found");
					CalcResponse response = messageResponse(CalcConstants.NO_RECORD_FOUND,
							appMessages.getParty_not_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					Queries queries = getValueQueries(commentQueryRequest);// get value Method call input values//
					int result = calcService.createCommentQueries(queries);
					if (result == 0) {
						logger.error("Error occured while adding data into table");
						CalcResponse response = messageResponse(CalcConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
					CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
							appMessages.getThread_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		} else {
			logger.error("some fields are empty");
			CalcResponse response = messageResponse(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_commentQuery());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	private Queries getValueQueries(CommentQueryRequest commentQueryRequest) {
		Queries queries = new Queries();
		// for (long postedToPartyId : commentQueryRequest.getPostedToPartyId()) {
		if (commentQueryRequest != null && commentQueryRequest.getQuery() != null) {
			queries.setQuery(commentQueryRequest.getQuery());
		}
		if (commentQueryRequest != null && commentQueryRequest.getCalcQueryId() != 0) {
			queries.setCalcQueryId(commentQueryRequest.getCalcQueryId());
		}
		if (commentQueryRequest != null && commentQueryRequest.getSenderId() != 0) {
			queries.setSenderId(commentQueryRequest.getSenderId());
		}
		if (commentQueryRequest != null && commentQueryRequest.getReferenceId() != null) {
			queries.setReferenceId(commentQueryRequest.getReferenceId());
		}
		if (commentQueryRequest != null && commentQueryRequest.getReceiverId() != 0) {
			queries.setReceiverId(commentQueryRequest.getReceiverId());
		}
		if (commentQueryRequest != null && commentQueryRequest.getPlans() != null) {
			queries.setPlans(commentQueryRequest.getPlans());
		}
		// queries.setPostedToPartyId(postedToPartyId);
		// }
		return queries;

	}

	private List<CalcQuery> getValueQueryReq(CalcQueryRequest queryRequest) {
		List<CalcQuery> calcThreadList = new ArrayList<CalcQuery>();
		for (long postedToPartyId : queryRequest.getPostedToPartyId()) {
			CalcQuery calcQuery = new CalcQuery();

			if (queryRequest != null && queryRequest.getPlans().size() != 0) {
				String selectedPlan = String.join(",", queryRequest.getPlans());
				calcQuery.setPlans(selectedPlan);
			}
			if (queryRequest != null && queryRequest.getReferenceId() != null) {
				calcQuery.setReferenceId(queryRequest.getReferenceId());
			}
			if (queryRequest != null && queryRequest.getUrl() != null) {
				calcQuery.setUrl(queryRequest.getUrl());
			}
			if (queryRequest != null && queryRequest.getPartyId() != 0) {
				calcQuery.setPartyId(queryRequest.getPartyId());
			}
			calcQuery.setPostedToPartyId(postedToPartyId);
			calcThreadList.add(calcQuery);
		}

		return calcThreadList;
	}

	/**
	 * Fetch all article comment by articleId
	 * 
	 * @return ResponseEntity<List<ArticleComment>> or ErrorResponse
	 * @param ForumIdRequest
	 * 
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch reply query comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchQueries", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchQueries(@NonNull @RequestBody CommentQueryRequest commentQueryRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (commentQueryRequest != null) {
			int screenId = commentQueryRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Fetching Comment");
		// long calcQueryId = commentQueryRequest.getCalcQueryId();
		long senderId = commentQueryRequest.getSenderId();
		long receiverId = commentQueryRequest.getReceiverId();
		String plans = commentQueryRequest.getPlans();
		if (senderId != 0 && receiverId != 0 && plans != null) {
			List<Queries> queriesList = calcService.fetchQueries(senderId, receiverId, plans);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), queriesList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			CalcResponse response = messageResponse(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_fetchQuery());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	/**
	 * Method to check shared advisor
	 * 
	 * @param requestEntity
	 *            contains the <code>SharedRequest</code>
	 * @return ResponseEntity<> Integer
	 */
	@ApiOperation(value = "check shared advisor ", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/checkSharedAdvisor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> checkSharedAdvisor(@NonNull @RequestBody SharedRequest sharedRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (sharedRequest != null) {
			int screenId = sharedRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(CalcConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (sharedRequest.getReferenceId() == null && sharedRequest.getPartyId() == 0) {
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_RefParty(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int result = calcService.checkSharedAdvisor(sharedRequest.getReferenceId(), sharedRequest.getPartyId());

			if (result != 0) {
				logger.info("This Plan already shared to the advisor");
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
						appMessages.getShared_plan_advisor(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.error("This Plan not shared to the advisor");
				CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
						appMessages.getNot_shared_plan_advisor(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	private long getSignedInParty() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
			return 0;
		} else {
			Party party = calcService.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
			return party.getPartyId();
		}
	}

	/**
	 * Method to fetch plan by partyId
	 * 
	 * @param RequestBody
	 *            contains the <code>PartyIdRequest</code>
	 * @return ResponseEntity<> Success
	 */
	@ApiOperation(value = "fetch the shared plan by postedPartyId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchSharedPlanByPostedPartyId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchSharedPlanByPostedPartyId(@NonNull @RequestBody PartyIdRequest partyIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (partyIdRequest != null) {
			int screenId = partyIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		long partyId = partyIdRequest.getPartyId();
		if (partyId != 0) {
			logger.info("Fetching plan by partyId");
			List<CalcQuery> planList = calcService.fetchSharedPlanByPostedPartyId(partyId);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), planList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("partyId is mandatory");
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_Party(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch plan by partyId
	 * 
	 * @param RequestBody
	 *            contains the <code>PartyIdRequest</code>
	 * @return ResponseEntity<> Success
	 */
	@ApiOperation(value = "fetch the shared plan by partyId and refId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchSharedByPartyIdAndRefId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchSharedByPartyIdAndRefId(@NonNull @RequestBody SharedRequest sharedRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (sharedRequest != null) {
			int screenId = sharedRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		long partyId = sharedRequest.getPartyId();
		String refId = sharedRequest.getReferenceId();
		if (refId == null && partyId == 0) {
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_RefParty(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching plan by partyId");
			List<CalcQuery> planList = calcService.fetchSharedPlanByPartyId(partyId, refId);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), planList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch plan by partyId
	 * 
	 * @param RequestBody
	 *            contains the <code>PartyIdRequest</code>
	 * @return ResponseEntity<> Success
	 */
	@ApiOperation(value = "fetch the shared plan by refId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchSharedByRefId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchSharedByRefId(@NonNull @RequestBody SharedRequest sharedRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (sharedRequest != null) {
			int screenId = sharedRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					CalcResponse response = responseWithData(CalcConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				CalcResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		// long partyId = sharedRequest.getPartyId();
		String refId = sharedRequest.getReferenceId();
		if (refId != null) {
			logger.info("Fetching plan by partyId");
			List<CalcQuery> planList = calcService.fetchSharedPlanByRefId(refId);
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE, appMessages.getSuccess(), planList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("partyId is mandatory");
			CalcResponse response = responseWithData(CalcConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_Party(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}
}
