package com.sowisetech.investor.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.forum.response.ForumResponse;
import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;
import com.sowisetech.investor.request.InvIdRequest;
import com.sowisetech.investor.request.InvInterestReq;
import com.sowisetech.investor.request.InvInterestRequest;
import com.sowisetech.investor.request.InvInterestRequestValidator;
import com.sowisetech.investor.request.InvestorRequest;
import com.sowisetech.investor.request.InvestorRequestValidator;
import com.sowisetech.investor.response.InvResponse;
import com.sowisetech.investor.response.InvResponseData;
import com.sowisetech.investor.response.InvResponseMessage;
import com.sowisetech.investor.response.InvTotalList;
import com.sowisetech.investor.service.InvestorService;
import com.sowisetech.investor.util.InvAppMessages;
import com.sowisetech.investor.util.InvestorConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InvestorController {

	@Autowired
	InvAppMessages appMessages;
	@Autowired
	InvInterestRequestValidator invInterestRequestValidator;
	@Autowired
	InvestorService investorService;
	@Autowired
	InvestorRequestValidator investorRequestValidator;
	@Autowired
	ScreenRightsConstants screenRightsConstants;
	@Autowired
	ScreenRightsCommon screenRightsCommon;
	@Autowired
	CommonService commonService;

	private static final Logger logger = LoggerFactory.getLogger(InvestorController.class);

	/**
	 * Extended Content Verification - Health check
	 * 
	 * @return ResponseEntity - HttpStatus.OK
	 */
	@RequestMapping(value = "/investor-ecv", method = RequestMethod.GET)
	public ResponseEntity getEcv() {
		logger.info("Investor module running.");
		// To view the log file location
		// System.out.println(System.getProperty("user.dir"));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * fetchInvestorList
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE with List<Investor> or
	 *         NO_RECORD_FOUND
	 * @param null
	 */
	@ApiOperation(value = "fetch investor list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllInvestor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchInvestorList(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int investorList = investorService.fetchTotalInvestorList();

		logger.info("Fetching Investor list");
		List<Investor> investors = investorService.fetchInvestorList(pageNum, records); // fetch
		InvTotalList invTotalList = new InvTotalList();
		if (investors != null) {
			invTotalList.setInvestors(investors);
			invTotalList.setTotalRecords(investorList);
		}
		InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE, appMessages.getSuccess(), invTotalList,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
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
	 * fetchByInvestorId
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE with Investor or
	 *         NO_RECORD_FOUND
	 * @param InvIdRequest
	 */
	@ApiOperation(value = "fetch investor by invId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchInvestor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchByInvestorId(@NonNull @RequestBody InvIdRequest invIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (invIdRequest != null) {
			int screenId = invIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String invId = invIdRequest.getInvId();
		if (invId == null) {
			logger.info("investorId is Mandatory");
			InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_invId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching investor by investorId");
			Investor investor = investorService.fetchByInvestorId(invId); // fetch investor by id
			InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE, appMessages.getSuccess(), investor,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * modifyInvestor
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE or ERROR_CODE
	 * @param InvestorRequest
	 */
	@ApiOperation(value = "modify investor", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyInvestor", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyInvestor(@NonNull @RequestBody InvestorRequest investorRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (investorRequest != null) {
			int screenId = investorRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String invId = investorRequest.getInvId();
		if (invId == null) {
			logger.info("investorId is Mandatory");
			InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_invId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			logger.info("Fetching investor by investorId");
			// Investor investor = investorService.fetchByInvestorId(invId);// fetch
			// investor by id
			int investor = investorService.CheckInvestorIsPresent(invId);// fetch investor by id
			if (investor == 0) {
				logger.info("No record Found");
				InvResponse response = responseWithData(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// validate investor request
				errors = investorRequestValidator.validate(investorRequest);
				if (errors.isEmpty() == true) {
					Investor inv = getValue(investorRequest); // get value Method call for input values
					logger.info("Updating investor");
					int result = investorService.modifyInvestor(invId, inv); // modify investor method call
					if (result != 0) {
						InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
								appMessages.getInvestor_updated_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.info("Error occured while adding data into table");
						InvResponse response = responseWithData(InvestorConstants.ERROR_CODE,
								appMessages.getError_occured(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (errors.isEmpty() == false) {
					logger.info("Validation Errors");
					InvResponse response = responseWithData(InvestorConstants.ERROR_CODE, appMessages.getError(),
							errors, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

	public Investor getValue(InvestorRequest investorRequest) {
		Investor inv = new Investor();
		if (investorRequest != null && investorRequest.getFullName() != null) {
			inv.setFullName(investorRequest.getFullName());
		}
		if (investorRequest != null && investorRequest.getDisplayName() != null) {
			inv.setDisplayName(investorRequest.getDisplayName());
		}
		if (investorRequest != null && investorRequest.getDob() != null) {
			inv.setDob(investorRequest.getDob());
		}
		if (investorRequest != null && investorRequest.getGender() != null) {
			inv.setGender(investorRequest.getGender());
		}
		// if (investorRequest != null && investorRequest.getEmailId() != null) {
		// inv.setEmailId(investorRequest.getEmailId());
		// }
		// if (investorRequest != null && investorRequest.getPhoneNumber() != null) {
		// inv.setPhoneNumber(investorRequest.getPhoneNumber());
		// }
		// if (investorRequest != null && investorRequest.getPassword() != null) {
		// inv.setPassword(investorRequest.getPassword());
		// }
		if (investorRequest != null && investorRequest.getPincode() != null) {
			inv.setPincode(investorRequest.getPincode());
		}
		if (investorRequest != null && investorRequest.getImagePath() != null) {
			inv.setImagePath(investorRequest.getImagePath());
		}
		return inv;
	}

	/**
	 * deleteInvestor
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE or ERROR_CODE
	 * @param InvIdRequest
	 */
	// TODO change RequestMethod.POST to DELETE
	@ApiOperation(value = "remove investor", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeInvestor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteInvestor(@NonNull @RequestBody InvIdRequest invIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (invIdRequest != null) {
			int screenId = invIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String invId = invIdRequest.getInvId();
		if (invId == null) {
			logger.info("investorId is Mandatory");
			InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_invId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching investor by investorId");
			// Investor investor = investorService.fetchByInvestorId(invId); // fetch
			// investor by id
			int investor = investorService.CheckInvestorIsPresent(invId); // fetch investor by id
			if (investor == 0) {
				logger.info("No record found");
				InvResponse response = responseWithData(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Modifying investor delete_flag as 'Y'");
				int result = investorService.removeInvestor(invId); // remove investor by id
				if (result != 0) {
					InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
							appMessages.getInvestor_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.info("Error Occured while removing data into table");
					InvResponse response = responseWithData(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured_remove(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * modifyInvInterest
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE or ERROR_CODE
	 * @param InvInterestRequest
	 */
	@ApiOperation(value = "modify investor interest", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyInvInterest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyInvInterest(@NonNull @RequestBody InvInterestReq invInterestReqList,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (invInterestReqList != null) {
			int screenId = invInterestReqList.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		List<InvInterestRequest> invInterestRequest = invInterestReqList.getInvInterestReq();
		String invId = invInterestReqList.getInvId();
		if (invId == null) {
			logger.info("investorId is Mandatory");
			InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_invId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			// To validate invInterestRequest //
			errors = invInterestRequestValidator.validate(invInterestRequest);
			if (errors.isEmpty() == true) {
				List<InvInterest> invInterestList = getValueInvInterest(invInterestReqList);
				int result = investorService.addAndModifyInvestorInterest(invId, invInterestList);
				if (result == 0) {
					logger.info("Error occured while adding data into table");
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
						appMessages.getInvestor_updated_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else if (errors.isEmpty() == false) {
				logger.info("Validation Errors");
				InvResponse response = responseWithData(InvestorConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	private List<InvInterest> getValueInvInterest(InvInterestReq invInterestReqList) {
		List<InvInterest> invInterestList = new ArrayList<InvInterest>();
		for (InvInterestRequest invInterestRequest : invInterestReqList.getInvInterestReq()) {
			InvInterest invInterest = new InvInterest();
			invInterest.setInterestId(invInterestRequest.getInterestId());
			if (invInterestRequest.getProdId() != 0) {
				invInterest.setProdId(invInterestRequest.getProdId());
			}
			if (invInterestRequest.getScale() != null) {
				invInterest.setScale(invInterestRequest.getScale());
			}
			invInterest.setInvId(invInterestReqList.getInvId());
			invInterestList.add(invInterest);
		}
		return invInterestList;
	}

	/**
	 * deleteInvInterest
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE or ERROR_CODE
	 * @param IdRequest
	 */
	// TODO change RequestMethod.POST to DELETE
	@ApiOperation(value = "remove investor interest", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeInvInterest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteInvInterest(@NonNull @RequestBody InvIdRequest invIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (invIdRequest != null) {
			int screenId = invIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				InvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long interestId = invIdRequest.getId();
		if (interestId == 0) {
			logger.info("interestId is Mandatory");
			InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_intId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int invInterest = investorService.CheckInvInterestIsPresent(interestId);
			if (invInterest == 0) {
				logger.info("No record found");
				InvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				// return ResponseEntity.ok().body(response);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// remove investor interest method call //
				logger.info("Modifying investorInterest delete_flag as 'Y'");
				int result = investorService.removeInvestorInterest(interestId);
				if (result != 0) {
					InvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
							appMessages.getInvestor_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.info("Error occured while removing data into table");
					InvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private InvResponse messageResponse(long code, String message) {
		InvResponseMessage invResponseMessage = new InvResponseMessage();
		invResponseMessage.setResponseCode(code);
		invResponseMessage.setResponseDescription(message);

		InvResponse invResponse = new InvResponse();
		invResponse.setResponseMessage(invResponseMessage);
		return invResponse;
	}

	private InvResponse responseWithData(long code, String message, Object data,
			List<RoleFieldRights> roleFieldRights) {
		InvResponseMessage invResponseMessage = new InvResponseMessage();
		invResponseMessage.setResponseCode(code);
		invResponseMessage.setResponseDescription(message);

		InvResponseData invResponseData = new InvResponseData();
		invResponseData.setData(data);
		invResponseData.setRoleFieldRights(roleFieldRights);

		InvResponse invResponse = new InvResponse();
		invResponse.setResponseData(invResponseData);
		invResponse.setResponseMessage(invResponseMessage);
		return invResponse;
	}

}
