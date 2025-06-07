package com.sowisetech.admin.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.sowisetech.admin.model.Account;
import com.sowisetech.admin.model.Acctype;
import com.sowisetech.admin.model.AdmFollower;
import com.sowisetech.admin.model.AdmPriority;
import com.sowisetech.admin.model.AdmRiskPortfolio;
import com.sowisetech.admin.model.Admin;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.ArticleStatus;
import com.sowisetech.admin.model.Brand;
import com.sowisetech.admin.model.CashFlowItem;
import com.sowisetech.admin.model.CashFlowItemType;
import com.sowisetech.admin.model.City;
import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.InsuranceItem;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.License;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Remuneration;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.ScreenFieldRights;
import com.sowisetech.admin.model.Service;
import com.sowisetech.admin.model.State;
import com.sowisetech.admin.model.Urgency;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.model.View;
import com.sowisetech.admin.request.AccountRequest;
import com.sowisetech.admin.request.AccountRequestValidator;
import com.sowisetech.admin.request.AcctypeRequest;
import com.sowisetech.admin.request.AdmAccTypeRequestValidator;
import com.sowisetech.admin.request.AdmAdvisorTypeRequestValidator;
import com.sowisetech.admin.model.UserType;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.model.Votetype;
import com.sowisetech.admin.model.Workflowstatus;
import com.sowisetech.admin.request.AdmArticleStatusRequestValidator;
import com.sowisetech.admin.request.AdmCashFlowItemTypeRequest;
import com.sowisetech.admin.request.AdmCashFlowItemTypeRequestValidator;
import com.sowisetech.admin.request.AdmFollowerRequest;
import com.sowisetech.admin.request.AdmFollowerRequestValidator;
import com.sowisetech.admin.request.AdmIdRequest;
import com.sowisetech.admin.request.AdmPriorityRequest;
import com.sowisetech.admin.request.AdmPriorityRequestValidator;
import com.sowisetech.admin.request.AdmProductRequestValidator;
import com.sowisetech.admin.request.AdmRemunerationRequestValidator;
import com.sowisetech.admin.request.AdmRiskPortfolioRequest;
import com.sowisetech.admin.request.AdmRoleRequest;
import com.sowisetech.admin.request.AdmRoleRequestValidator;
import com.sowisetech.admin.request.AdmStateRequestValidator;
import com.sowisetech.admin.request.AdmUserRoleRequest;
import com.sowisetech.admin.request.AdminIdRequest;
import com.sowisetech.admin.request.AdminRequest;
import com.sowisetech.admin.request.AdminRequestValidator;
import com.sowisetech.admin.request.AdvtypesRequest;
import com.sowisetech.admin.request.ArticleStatusRequest;
import com.sowisetech.admin.request.BrandRequest;
import com.sowisetech.admin.request.BrandRequestValidator;
import com.sowisetech.admin.request.CashFlowItemRequest;
import com.sowisetech.admin.request.CashFlowItemRequestValidator;
import com.sowisetech.admin.request.CityRequest;
import com.sowisetech.admin.request.CityRequestValidator;
import com.sowisetech.admin.request.FieldRightsRequest;
import com.sowisetech.admin.request.InsuranceItemRequest;
import com.sowisetech.admin.request.InsuranceItemRequestValidator;
import com.sowisetech.admin.request.LicenseRequest;
import com.sowisetech.admin.request.LicenseRequestValidator;
import com.sowisetech.admin.request.ModifyAdminRequest;
import com.sowisetech.admin.request.ModifyAdminRequestValidator;
import com.sowisetech.admin.request.ProductRequest;
import com.sowisetech.admin.request.RemunerationRequest;
import com.sowisetech.admin.request.RiskPortfolioRequestValitator;
import com.sowisetech.admin.request.ScreenFieldRightsRequest;
import com.sowisetech.admin.request.ScreenRightsRequest;
import com.sowisetech.admin.request.ServiceRequest;
import com.sowisetech.admin.request.ServiceRequestValidator;
import com.sowisetech.admin.request.StateRequest;
import com.sowisetech.admin.request.UrgencyRequest;
import com.sowisetech.admin.request.UrgencyRequestValidator;
import com.sowisetech.admin.request.VotetypeRequest;
import com.sowisetech.admin.request.VotetypeRequestValidator;
import com.sowisetech.admin.request.UserTypeRequest;
import com.sowisetech.admin.request.UserTypeRequestValidator;
import com.sowisetech.admin.request.ViewIdRequest;
import com.sowisetech.admin.request.ViewRequest;
import com.sowisetech.admin.request.WorkFlowStatusRequest;
import com.sowisetech.admin.request.WorkFlowStatusRequestValidator;
import com.sowisetech.admin.response.AdmResponse;
import com.sowisetech.admin.response.AdmResponseData;
import com.sowisetech.admin.response.AdmResponseMessage;
import com.sowisetech.admin.service.AdminService;
import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdminConstants;
import com.sowisetech.advisor.dao.AdvisorDao;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

	@Autowired
	AdminService adminService;
	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmRoleRequestValidator admRoleRequestValidator;
	@Autowired
	AdmAdvisorTypeRequestValidator admAdvisorTypeRequestValidator;
	@Autowired
	AdmPriorityRequestValidator admPriorityRequestValidator;
	@Autowired
	AdmFollowerRequestValidator admFollowerRequestValidator;
	@Autowired
	AdmAccTypeRequestValidator admAccTypeRequestValidator;
	@Autowired
	AdmRemunerationRequestValidator admRemunerationRequestValidator;
	@Autowired
	WorkFlowStatusRequestValidator workFlowStatusRequestValidator;
	@Autowired
	AdmStateRequestValidator admStateRequestValidator;
	@Autowired
	ServiceRequestValidator serviceRequestValidator;
	@Autowired
	VotetypeRequestValidator votetypeRequestValidator;
	@Autowired
	BrandRequestValidator brandRequestValidator;
	@Autowired
	AdmProductRequestValidator admProductRequestValidator;
	@Autowired
	RiskPortfolioRequestValitator riskPortfolioRequestValitator;
	@Autowired
	InsuranceItemRequestValidator insuranceItemRequestValidator;
	@Autowired
	LicenseRequestValidator licenseRequestValidator;
	@Autowired
	UrgencyRequestValidator urgencyRequestValidator;
	@Autowired
	AdmArticleStatusRequestValidator admArticleStatusRequestValidator;
	@Autowired
	AdmCashFlowItemTypeRequestValidator admCashFlowItemTypeRequestValidator;
	@Autowired
	CashFlowItemRequestValidator cashFlowItemRequestValidator;
	@Autowired
	CityRequestValidator cityRequestValidator;
	@Autowired
	AccountRequestValidator accountRequestValidator;
	@Autowired
	UserTypeRequestValidator userTypeRequestValidator;
	@Autowired
	ScreenRightsCommon screenRightsCommon;
	@Autowired
	ScreenRightsConstants screenRightsConstants;
	@Autowired
	CommonService commonService;
	@Autowired
	AdminRequestValidator adminRequestValidator;
	@Autowired
	ModifyAdminRequestValidator modifyAdminRequestValidator;
	private Object AdvtypesRequest;
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	private static final ProductRequest ProductRequest = null;
	private static final String WorkFlowStatusRequest = null;

	@RequestMapping(value = "/admin-ecv", method = RequestMethod.GET)
	public ResponseEntity getEcv() {
		logger.info("Advisor module running.");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * fetcAllAdmin
	 * 
	 * @return ResponseEntity<?> NO_RECORD_FOUND or SUCCESS_CODE with AdminList
	 * @param null
	 * 
	 **/
	@ApiOperation(value = "fetch admin list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllAdmin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetcAllAdmin(@RequestBody(required = false) ScreenIdRequest screenIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		logger.info("Fetching admin list");
		List<Admin> adminList = adminService.fetchAdminList(); // fetch
		if (adminList == null) {
			logger.info("No Record Found");
			AdmResponse response = messageResponse(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getSuccess(), adminList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * adminSignup
	 * 
	 * @param adminRequest
	 * @return ResponseEntity<?> SUCCESS_CODE or ERROR_CODE
	 */
	@RequestMapping(value = "/adminSignup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> adminSignup(@NonNull @RequestBody AdminRequest adminRequest) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validate request
		errors = adminRequestValidator.validate(adminRequest);
		if (errors.isEmpty() == true) {
			if (adminRequest.getEmailId() == null || adminRequest.getName() == null
					|| adminRequest.getPassword() == null) {
				logger.info("Admin is Empty");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getAdmin_is_empty());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Party party = adminService.fetchPartyByEmailId(adminRequest.getEmailId());
				int party = adminService.checkPartyIsPresent(adminRequest.getEmailId());
				if (party == 0) {
					Admin admin = getAdminValue(adminRequest);// get value Method call
					// Generate Admin Id
					logger.info("Generating admin id");
					admin.setAdminId(adminService.generateId());
					// add admin into table
					logger.info("Adding admin data into table");
					int result = adminService.addAdmin(admin);
					if (result != 0) {
						AdmResponse response = messageResponse(AdminConstants.SUCCESS_CODE,
								appMessages.getAdmin_added_successfully());
						return ResponseEntity.ok().body(response);
					} else {
						logger.info("Error Occurred while adding data");
						AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else {
					logger.info("Admin already present");
					AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
							appMessages.getAdmin_already_present());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		} else if (errors.isEmpty() == false) {
			logger.info("Validation errors");
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * modifyAdmin
	 * 
	 * @param adminRequest
	 * @return ResponseEntity<?> SUCCESS_CODE or ERROR_CODE
	 */
	@ApiOperation(value = "modify admin", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyAdmin", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAdmin(@NonNull @RequestBody ModifyAdminRequest modifyAdminRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (modifyAdminRequest != null) {
			int screenId = modifyAdminRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		logger.info("Fetching admin by adminId");
		// Admin admin = adminService.fetchByAdminId(modifyAdminRequest.getAdminId());
		int admin = adminService.checkAdminIsPresent(modifyAdminRequest.getAdminId());
		if (admin == 0) {
			logger.info("No Record Found");
			AdmResponse response = messageResponse(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			errors = modifyAdminRequestValidator.validate(modifyAdminRequest);
			if (errors.isEmpty() == true) {
				Admin admin1 = getModifyAdminValue(modifyAdminRequest);// get value Method call
				// modify admin in atble
				logger.info("Modifying admin data into table");
				int result = adminService.modifyAdmin(modifyAdminRequest.getAdminId(), admin1);
				if (result != 0) {
					AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
							appMessages.getAdmin_updated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.info("Error Occurred while adding data");
					AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (errors.isEmpty() == false) {
				logger.info("Validation errors");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	private Admin getModifyAdminValue(ModifyAdminRequest modifyAdminRequest) {
		Admin admin = new Admin();
		if (modifyAdminRequest != null && modifyAdminRequest.getName() != null) {
			admin.setName(modifyAdminRequest.getName());
		}
		return admin;
	}

	/**
	 * deleteAdmin
	 * 
	 * @param AdminIdRequest
	 * @return ResponseEntity<?> SUCCESS_CODE or ERROR_CODE
	 */
	@ApiOperation(value = "remove admin", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeAdmin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteAdmin(@NonNull @RequestBody AdminIdRequest idRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Fetching admin by adminId");
		// Admin admin = adminService.fetchByAdminId(idRequest.getAdminId());
		int admin = adminService.checkAdminIsPresent(idRequest.getAdminId());
		if (admin == 0) {
			logger.info("No Record Found");
			AdmResponse response = messageResponse(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			logger.info("Removing admin");
			int result = adminService.removeAdmin(idRequest.getAdminId());
			if (result != 0) {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_deleted_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Error Occured while removing data");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	private Admin getAdminValue(AdminRequest adminRequest) {
		Admin admin = new Admin();
		if (adminRequest != null && adminRequest.getName() != null) {
			admin.setName(adminRequest.getName());
		}
		if (adminRequest != null && adminRequest.getEmailId() != null) {
			admin.setEmailId(adminRequest.getEmailId());
		}
		if (adminRequest != null && adminRequest.getPassword() != null) {
			admin.setPassword(adminRequest.getPassword());
		}
		return admin;
	}

	@ApiOperation(value = "add screen-fields rights", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addScreenRightsFieldRights", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addScreenRightsFieldRights(
			@NonNull @RequestBody ScreenFieldRightsRequest screenFieldRightsRequest, HttpServletRequest request) {
		// check if the user_role_id is available
		int user_role_id = screenFieldRightsRequest.getUser_role_id();
		// User_role user_role = adminService.fetchUserRoleByUserRoleId(user_role_id);
		int user_role = adminService.checkUserRoleIsPresent(user_role_id);
		if (user_role == 0) {
			logger.info("No record found");
			AdmResponse response = responseWithData(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			List<ScreenFieldRights> screenFieldRights = getScreenFieldRights(screenFieldRightsRequest);
			int result = adminService.addScreenRightsFieldRights(screenFieldRights);
			if (result == 0) {
				logger.info("Error occured while adding data");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Data added");
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}
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

	@ApiOperation(value = "modify screen-field rights", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyScreenRightsFieldRights", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyScreenRightsFieldRights(
			@NonNull @RequestBody ScreenFieldRightsRequest screenFieldRightsRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenFieldRightsRequest != null) {
			int screenId = screenFieldRightsRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		// check if the user_role_id is available
		int user_role_id = screenFieldRightsRequest.getUser_role_id();
		// User_role user_role = adminService.fetchUserRoleByUserRoleId(user_role_id);
		int user_role = adminService.checkUserRoleIsPresent(user_role_id);
		if (user_role == 0) {
			logger.info("No record found");
			AdmResponse response = responseWithData(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			List<ScreenFieldRights> screenFieldRights = getScreenFieldRights(screenFieldRightsRequest);
			int result = adminService.modifyScreenRightsFieldRights(screenFieldRights,
					screenFieldRightsRequest.getUser_role_id());
			if (result != 0) {
				logger.info("Data added");
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Error occured while adding data");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	private List<ScreenFieldRights> getScreenFieldRights(ScreenFieldRightsRequest screenFieldRightsRequest) {
		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		for (ScreenRightsRequest screenRightsRequest : screenFieldRightsRequest.getScreenRightsRequest()) {
			ScreenFieldRights screenFieldRights = new ScreenFieldRights();
			screenFieldRights.setUser_role_id(screenFieldRightsRequest.getUser_role_id());
			screenFieldRights.setScreen_id(screenRightsRequest.getScreen_id());
			screenFieldRights.setAdd_rights(screenRightsRequest.getAdd_rights());
			screenFieldRights.setEdit_rights(screenRightsRequest.getEdit_rights());
			screenFieldRights.setView_rights(screenRightsRequest.getView_rights());
			screenFieldRights.setDelete_rights(screenRightsRequest.getDelete_rights());
			List<FieldRights> fieldRightsList = new ArrayList<FieldRights>();
			for (FieldRightsRequest fieldRightsRequest : screenRightsRequest.getFieldRightsRequests()) {
				FieldRights fieldRights = new FieldRights();
				fieldRights.setField_id(fieldRightsRequest.getField_id());
				fieldRights.setAdd_rights(fieldRightsRequest.getAdd_rights());
				fieldRights.setEdit_rights(fieldRightsRequest.getEdit_rights());
				fieldRights.setView_rights(fieldRightsRequest.getView_rights());
				fieldRightsList.add(fieldRights);
			}
			screenFieldRights.setFieldRights(fieldRightsList);
			screenFieldRightsList.add(screenFieldRights);
		}
		return screenFieldRightsList;
	}

	/**
	 * Method to add the Role
	 * 
	 * @param RequestBody
	 *            contains the AdmRoleRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         role or ErrorResponse
	 */

	// Add Role in role table
	@ApiOperation(value = "add admin role", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addAdmRole(@NonNull @RequestBody AdmRoleRequest admRoleRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating admRoleRequestValidator
		errors = admRoleRequestValidator.validate(admRoleRequest);
		if (errors.isEmpty() == true) {
			RoleAuth role = getValueRoleInfo(admRoleRequest);// get value Method call
			logger.info("Adding role into DB");
			int result = adminService.addRole(role);
			if (result == 0) {
				logger.info("Error occurred while adding data into table");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private RoleAuth getValueRoleInfo(AdmRoleRequest admRoleRequest) {
		RoleAuth role = new RoleAuth();
		role.setName(admRoleRequest.getName());
		role.setCreated_by(admRoleRequest.getCreated_by());
		role.setUpdated_by(admRoleRequest.getUpdated_by());
		return role;
	}

	/**
	 * Method to modify role
	 * 
	 * @param RequestBody
	 *            contains the AdmRoleRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         role or ErrorResponse
	 */
	@ApiOperation(value = "modify roleAuth", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyRole", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyRoleAuth(@NonNull @RequestBody AdmRoleRequest admRoleRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (admRoleRequest != null) {
			int screenId = admRoleRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		int id = admRoleRequest.getId();
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// RoleAuth roleauth = adminService.fetchRoleByRoleId(id);
		int roleauth = adminService.checkRoleIsPresent(id);
		if (roleauth == 0) {
			logger.info("No record found");
			AdmResponse response = responseWithData(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			// Validating admRoleRequestValidator
			errors = admRoleRequestValidator.validate(admRoleRequest);
			if (errors.isEmpty() == true) {
				RoleAuth role = getModifiedRoleValue(admRoleRequest);// get value Method call
				logger.info("Modifying role");
				int result = adminService.modifyRole(id, role);
				if (result == 0) {
					logger.info("Error occurred while adding data into table");
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError_occured(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
							appMessages.getAdmin_updated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			} else if (errors.isEmpty() == false) {
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	private RoleAuth getModifiedRoleValue(AdmRoleRequest admRoleRequest) {
		RoleAuth role = new RoleAuth();
		role.setName(admRoleRequest.getName());
		role.setUpdated_by(admRoleRequest.getUpdated_by());
		return role;
	}

	/**
	 * delete role
	 * 
	 * @return ResponseEntity<AdmResponse> SUCCESS_CODE or ERROR_CODE
	 * @param AdmIdRequest
	 */
	// TODO : need to change RequestMethod.DELETE
	@ApiOperation(value = "remove roleAuth", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeRoleAuth(@NonNull @RequestBody AdmIdRequest admIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (admIdRequest != null) {
			int screenId = admIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int roleId = admIdRequest.getId();
		// fetch role by id for delete
		// RoleAuth roleAuth = adminService.fetchRoleByRoleId(roleId);
		int roleAuth = adminService.checkRoleIsPresent(roleId);
		if (roleAuth == 0) {
			logger.info("No record found");
			AdmResponse response = responseWithData(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			// remove role method call //
			logger.info("Removing role");
			int result = adminService.removeRole(roleId);
			if (result != 0) {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_deleted_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Error occured while removing data into table");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE,
						appMessages.getError_occured_remove(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}
	}

	/**
	 * Method to add the User Role
	 * 
	 * @param RequestBody
	 *            contains the AdmUserRoleRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         user_role or ErrorResponse
	 */

	// Add Advisor Personal Information in advisor table
	@ApiOperation(value = "add user role", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addUserRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addUserRole(@NonNull @RequestBody AdmUserRoleRequest admUserRoleRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (admUserRoleRequest != null) {
			int screenId = admUserRoleRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// User_role uRole =
		// adminService.fetchUserRoleByUserIdAndRoleId(admUserRoleRequest.getUser_id(),
		// admUserRoleRequest.getRole_id());
		int uRole = adminService.checkUserRoleByUserIdAndRoleId(admUserRoleRequest.getUser_id(),
				admUserRoleRequest.getRole_id());
		if (uRole != 0) {
			logger.info("These roleId already present for the userId");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
					appMessages.getAlready_present_for_userId());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			User_role userRole = getValueUserRoleInfo(admUserRoleRequest);// get value Method call
			logger.info("Adding user role into DB");
			int result = adminService.addUserRole(userRole);
			if (result == 0) {
				logger.info("Error occurred while adding data into table");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError_occured(), null,
						null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	private User_role getValueUserRoleInfo(AdmUserRoleRequest admUserRoleRequest) {
		User_role userRole = new User_role();
		userRole.setUser_id(admUserRoleRequest.getUser_id());
		userRole.setRole_id(admUserRoleRequest.getRole_id());
		return userRole;
	}

	/**
	 * Method to modify user_role
	 * 
	 * @param RequestBody
	 *            contains the AdmUserRoleRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         user role or ErrorResponse
	 */
	@ApiOperation(value = "modify user role", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyUserRole", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyUserRole(@NonNull @RequestBody AdmUserRoleRequest admUserRoleRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (admUserRoleRequest != null) {
			int screenId = admUserRoleRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int id = admUserRoleRequest.getUser_role_id();
		// User_role userRole = adminService.fetchUserRoleByUserRoleId(id);
		int user_role = adminService.checkUserRoleIsPresent(id);
		if (user_role == 0) {
			logger.info("No record found");
			AdmResponse response = responseWithData(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			// User_role uRole =
			// adminService.fetchUserRoleByUserIdAndRoleId(admUserRoleRequest.getUser_id(),
			// admUserRoleRequest.getRole_id());
			int uRole = adminService.checkUserRoleByUserIdAndRoleId(admUserRoleRequest.getUser_id(),
					admUserRoleRequest.getRole_id());
			// System.out.println("UserRole ---" + uRole);
			if (uRole != 0) {
				logger.info("These roleId already present for the userId");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
						appMessages.getAlready_present_for_userId());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				User_role role = getModifiedRoleUserValue(admUserRoleRequest);// get value Method call
				logger.info("Modifying User_role");
				int result = adminService.modifyUserRole(id, role);
				if (result == 0) {
					logger.info("Error occurred while adding data into table");
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError_occured(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
							appMessages.getAdmin_updated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
	}

	private User_role getModifiedRoleUserValue(AdmUserRoleRequest admUserRoleRequest) {
		User_role userRole = new User_role();
		userRole.setUser_id(admUserRoleRequest.getUser_id());
		userRole.setRole_id(admUserRoleRequest.getRole_id());
		return userRole;
	}

	/**
	 * delete user_role
	 * 
	 * @return ResponseEntity<AdmResponse> SUCCESS_CODE or ERROR_CODE
	 * @param AdmIdRequest
	 */
	// TODO : need to change RequestMethod.DELETE
	@ApiOperation(value = "remove user role", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeUserRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeUserRole(@NonNull @RequestBody AdmIdRequest admIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (admIdRequest != null) {
			int screenId = admIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		int user_role_id = admIdRequest.getId();
		// fetch role by id for delete
		// User_role userRole = adminService.fetchUserRoleByUserRoleId(roleId);
		int userRole = adminService.checkUserRoleIsPresent(user_role_id);
		if (userRole == 0) {
			logger.info("No record found");
			AdmResponse response = responseWithData(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found(),
					null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			// remove user role method call //
			logger.info("Removed User_role");
			int result = adminService.removeUserRole(user_role_id);
			if (result != 0) {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_deleted_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Error occured while removing data into table");
				AdmResponse response = responseWithData(AdminConstants.ERROR_CODE,
						appMessages.getError_occured_remove(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}
	}

	@ApiOperation(value = "save addAcctype", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAcctype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> saveAddAcctype(@RequestBody AcctypeRequest acctypeRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating admRoleRequestValidator
		errors = admAccTypeRequestValidator.validate(acctypeRequest);
		if (errors.isEmpty() == true) {
			Acctype acctype = getValueOfAcctype(acctypeRequest);
			int result = adminService.saveAddAcctype(acctype);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getAdd_accountType(),
						null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Acctype getValueOfAcctype(AcctypeRequest acctypeRequest) {
		Acctype acctype = new Acctype();
		acctype.setAccTypeId(acctypeRequest.getAccTypeId());
		acctype.setAccType(acctypeRequest.getAccType());

		return acctype;
	}

	@ApiOperation(value = "remove acctype", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/RemoveAcctype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> RemoveAcctype(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		// Acctype acctype = getValueOfAcctype(acctypeRequest);
		int result = adminService.RemoveAcctype(admIdRequest.getId());

		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getRemove_accountType(),
					null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "modify acctype", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyAcctype", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAcctype(@RequestBody AcctypeRequest acctypeRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();

		// Validating admRoleRequestValidator
		errors = admAccTypeRequestValidator.validate(acctypeRequest);
		if (errors.isEmpty() == true) {

			Acctype acctype = getModifyOfAcctype(acctypeRequest);
			int result = adminService.modifyAcctype(acctype);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getModify_accountType(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Acctype getModifyOfAcctype(AcctypeRequest acctypeRequest) {
		Acctype acctype = new Acctype();
		acctype.setAccTypeId(acctypeRequest.getAccTypeId());
		acctype.setAccType(acctypeRequest.getAccType());

		return acctype;
	}

	@ApiOperation(value = "add products", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addProducts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addProducts(@RequestBody ProductRequest ProductRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admProductRequestValidator.validateProduct(ProductRequest);
		if (errors.isEmpty() == true) {
			Product product = getValueProduct(ProductRequest);
			int result = adminService.addProducts(product);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getAdmin_product(),
						null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Product getValueProduct(ProductRequest productRequest) {
		Product product = new Product();
		product.setProduct(productRequest.getProduct());
		return product;
	}

	@ApiOperation(value = "remove products", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeProducts", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeProducts(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		int result = adminService.removeProducts(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getRemove_product(), null,
					null);
			return ResponseEntity.ok().body(response);
		}

	}

	@ApiOperation(value = "modify products", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyProducts", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyProducts(@RequestBody ProductRequest ProductRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admProductRequestValidator.validateProduct(ProductRequest);
		if (errors.isEmpty() == true) {
			Product product = getModifiedProduct(ProductRequest);
			int result = adminService.modifyProduct(product);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getModify_product(),
						null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Product getModifiedProduct(ProductRequest productRequest) {
		Product product = new Product();
		product.setProdId(productRequest.getProdId());
		product.setProduct(productRequest.getProduct());
		return product;
	}

	@ApiOperation(value = "add articleStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addArticleStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addArticleStatus(@RequestBody ArticleStatusRequest articleStatusRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admArticleStatusRequestValidator.validate(articleStatusRequest);
		if (errors.isEmpty() == true) {
			ArticleStatus articleStatus = getValueArticle(articleStatusRequest);
			int result = adminService.addArticleStatus(articleStatus);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private ArticleStatus getValueArticle(ArticleStatusRequest articleStatusRequest) {
		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setDesc(articleStatusRequest.getDesc());
		return articleStatus;
	}

	@ApiOperation(value = "modify articleStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyArticleStatus", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyArticleStatus(@RequestBody ArticleStatusRequest articleStatusRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admArticleStatusRequestValidator.validate(articleStatusRequest);
		if (errors.isEmpty() == true) {
			ArticleStatus articleStatus = getModifiedArticle(articleStatusRequest);
			int result = adminService.modifyArticleStatus(articleStatus);

			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ApiOperation(value = "addRemuneration", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addRemuneration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addRemuneration(@RequestBody RemunerationRequest remunerationRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating admRoleRequestValidator
		errors = admRemunerationRequestValidator.validate(remunerationRequest);
		if (errors.isEmpty() == true) {
			Remuneration remuneration = getValueOfremuneration(remunerationRequest);
			int result = adminService.addRemuneration(remuneration);

			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private ArticleStatus getModifiedArticle(ArticleStatusRequest articleStatusRequest) {
		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(articleStatusRequest.getId());
		articleStatus.setDesc(articleStatusRequest.getDesc());
		return articleStatus;
	}

	@ApiOperation(value = "remove articleStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeArticleStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeArticleStatus(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		int result = adminService.removeArticleStatus(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}

	}

	@ApiOperation(value = "add cashFlowItemType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addCashFlowItemType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addCashFlowItemType(@RequestBody AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admCashFlowItemTypeRequestValidator.validate(admCashFlowItemTypeRequest);
		if (errors.isEmpty() == true) {
			CashFlowItemType cashFlowItemType = getValueCashFlowItem(admCashFlowItemTypeRequest);
			int result = adminService.addCashFlowItemType(cashFlowItemType);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Remuneration getValueOfremuneration(RemunerationRequest remunerationRequest) {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemuneration(remunerationRequest.getRemuneration());
		return remuneration;
	}

	@ApiOperation(value = "removeRemuneration", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeRemuneration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeRemuneration(@RequestBody RemunerationRequest remunerationRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating admRoleRequestValidator
		errors = admRemunerationRequestValidator.validate(remunerationRequest);
		if (errors.isEmpty() == true) {
			Remuneration remuneration = getValueOfremuneration(remunerationRequest);
			int result = adminService.removeRemuneration(remunerationRequest.getRemId());
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_deleted_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private CashFlowItemType getValueCashFlowItem(AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest) {
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemType(admCashFlowItemTypeRequest.getCashFlowItemType());
		return cashFlowItemType;
	}

	@ApiOperation(value = "modify cashFlowItemType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyCashFlowItemType", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyCashFlowItemType(@RequestBody AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admCashFlowItemTypeRequestValidator.validate(admCashFlowItemTypeRequest);
		if (errors.isEmpty() == true) {
			CashFlowItemType cashFlowItemType = getModifiedArticle(admCashFlowItemTypeRequest);
			int result = adminService.modifyCashFlowItemType(cashFlowItemType);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ApiOperation(value = "modifyRemuneration", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyRemuneration", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyRemuneration(@RequestBody RemunerationRequest remunerationRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating admRoleRequestValidator
		errors = admRemunerationRequestValidator.validate(remunerationRequest);
		if (errors.isEmpty() == true) {
			Remuneration remuneration = getValueOfmodifyremuneration(remunerationRequest);
			int result = adminService.modifyRemuneration(remuneration);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private CashFlowItemType getModifiedArticle(AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest) {
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(admCashFlowItemTypeRequest.getCashFlowItemTypeId());
		cashFlowItemType.setCashFlowItemType(admCashFlowItemTypeRequest.getCashFlowItemType());
		return cashFlowItemType;
	}

	@ApiOperation(value = "remove cashFlowItemType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeCashFlowItemType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeCashFlowItemType(@RequestBody AdmIdRequest admIdRequest,
			HttpServletRequest request) {
		int result = adminService.removeCashFlowItemType(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}

	}

	@ApiOperation(value = "add cashFlowItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addCashFlowItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addCashFlowItem(@RequestBody CashFlowItemRequest cashFlowItemRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = cashFlowItemRequestValidator.validate(cashFlowItemRequest);
		if (errors.isEmpty() == true) {
			CashFlowItem cashFlowItem = getValueCashFlowItem(cashFlowItemRequest);
			int result = adminService.addCashFlowItem(cashFlowItem);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Remuneration getValueOfmodifyremuneration(RemunerationRequest remunerationRequest) {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemId(remunerationRequest.getRemId());
		remuneration.setRemuneration(remunerationRequest.getRemuneration());
		return remuneration;
	}

	@ApiOperation(value = "addState", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addState", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addState(@RequestBody StateRequest stateRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admStateRequestValidator.validate(stateRequest);
		if (errors.isEmpty() == true) {
			State state = getValueState(stateRequest);
			int result = adminService.addState(state);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private CashFlowItem getValueCashFlowItem(CashFlowItemRequest cashFlowItemRequest) {
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItem(cashFlowItemRequest.getCashFlowItem());
		cashFlowItem.setCashFlowItemTypeId(cashFlowItemRequest.getCashFlowItemTypeId());
		return cashFlowItem;
	}

	@ApiOperation(value = "modify cashFlowItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyCashFlowItem", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyCashFlowItem(@RequestBody CashFlowItemRequest cashFlowItemRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = cashFlowItemRequestValidator.validate(cashFlowItemRequest);
		if (errors.isEmpty() == true) {
			CashFlowItem cashFlowItem = getModifyCashFlowItem(cashFlowItemRequest);
			int result = adminService.modifyCashFlowItem(cashFlowItem);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private CashFlowItem getModifyCashFlowItem(CashFlowItemRequest cashFlowItemRequest) {
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(cashFlowItemRequest.getCashFlowItemId());
		cashFlowItem.setCashFlowItemTypeId(cashFlowItemRequest.getCashFlowItemTypeId());
		cashFlowItem.setCashFlowItem(cashFlowItemRequest.getCashFlowItem());
		return cashFlowItem;
	}

	@ApiOperation(value = "remove cashFlowItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeCashFlowItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeCashFlowItem(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		int result = adminService.removeCashFlowItem(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	private State getValueState(StateRequest stateRequest) {
		State state = new State();
		state.setState(stateRequest.getState());
		return state;
	}

	@ApiOperation(value = "removeState", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeState", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeState(@RequestBody StateRequest StateRequest, HttpServletRequest request) {

		State state = getValueState(StateRequest);
		int result = adminService.removeState(StateRequest.stateId);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "add city", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addCity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addCity(@RequestBody CityRequest cityRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = cityRequestValidator.validate(cityRequest);
		if (errors.isEmpty() == true) {
			City city = getValueCity(cityRequest);
			int result = adminService.addCity(city);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private City getValueCity(CityRequest cityRequest) {
		City city = new City();
		city.setStateId(cityRequest.getStateId());
		city.setCity(cityRequest.getCity());
		city.setPincode(cityRequest.getPincode());
		city.setDistrict(cityRequest.getDistrict());
		return city;
	}

	@ApiOperation(value = "modify city", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyCity", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyCity(@RequestBody CityRequest cityRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = cityRequestValidator.validate(cityRequest);
		if (errors.isEmpty() == true) {
			City city = getModifyCity(cityRequest);
			int result = adminService.modifyCity(city);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private City getModifyCity(CityRequest cityRequest) {
		City city = new City();
		city.setCityId(cityRequest.getCityId());
		city.setStateId(cityRequest.getStateId());
		city.setCity(cityRequest.getCity());
		city.setPincode(cityRequest.getPincode());
		city.setDistrict(cityRequest.getDistrict());
		return city;
	}

	@ApiOperation(value = "remove city", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeCity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeCity(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		int result = adminService.removeCity(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {

			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "modifyState", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyState", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyState(@RequestBody StateRequest StateRequest, HttpServletRequest request) {
		State state = getModifiedState(StateRequest);
		int result = adminService.modifyState(state);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {

			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_updated_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}

	}

	private State getModifiedState(StateRequest stateRequest) {
		State state = new State();
		state.setStateId(stateRequest.getStateId());
		state.setState(stateRequest.getState());
		return state;
	}

	@ApiOperation(value = "addService", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addService", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addService(@RequestBody ServiceRequest serviceRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = serviceRequestValidator.validate(serviceRequest);
		if (errors.isEmpty() == true) {
			Service service = getValueService(serviceRequest);
			int result = adminService.addService(service);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Service getValueService(ServiceRequest serviceRequest) {
		Service service = new Service();
		service.setService(serviceRequest.getService());
		service.setProdId(serviceRequest.getProdId());

		return service;
	}

	@ApiOperation(value = "modify Service", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyService", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyService(@RequestBody ServiceRequest serviceRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = serviceRequestValidator.validate(serviceRequest);
		if (errors.isEmpty() == true) {
			Service service = getModifiedService(serviceRequest);
			int result = adminService.modifyService(service);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Service getModifiedService(ServiceRequest serviceRequest) {
		Service service = new Service();
		service.setServiceId(serviceRequest.getServiceId());
		service.setService(serviceRequest.getService());
		service.setProdId(serviceRequest.getProdId());
		return service;
	}

	@ApiOperation(value = "remove Service", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeService", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeService(@RequestBody ServiceRequest serviceRequest, HttpServletRequest request) {
		int result = adminService.removeService(serviceRequest.serviceId);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}

	}

	@ApiOperation(value = "add Workflowstatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addWorkflowstatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addWorkflowstatus(@RequestBody WorkFlowStatusRequest workFlowStatusRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = workFlowStatusRequestValidator.validate(workFlowStatusRequest);
		if (errors.isEmpty() == true) {
			Workflowstatus workflowstatus = getValueWorkflowstatus(workFlowStatusRequest);
			int result = adminService.addWorkflowstatus(workflowstatus);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Workflowstatus getValueWorkflowstatus(WorkFlowStatusRequest workFlowStatusRequest) {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setStatus(workFlowStatusRequest.getStatus());
		return workflowstatus;
	}

	@ApiOperation(value = "modify Workflowstatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyWorkflowstatus", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyWorkflowstatus(@RequestBody WorkFlowStatusRequest workFlowStatusRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = workFlowStatusRequestValidator.validate(workFlowStatusRequest);
		if (errors.isEmpty() == true) {
			Workflowstatus workflowstatus = getModifiedWorkflowstatus(workFlowStatusRequest);
			int result = adminService.modifyWorkflowstatus(workflowstatus);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Workflowstatus getModifiedWorkflowstatus(WorkFlowStatusRequest workFlowStatusRequest) {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setWorkflowId(workFlowStatusRequest.getWorkflowId());
		workflowstatus.setStatus(workFlowStatusRequest.getStatus());
		return workflowstatus;
	}

	// @ApiOperation(value = "add articleStatus", authorizations =
	// @Authorization(value = "Bearer"))
	// @RequestMapping(value = "/addArticleStatus", method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> addArticleStatus(@RequestBody ArticleStatusRequest
	// articleStatusRequest, HttpServletRequest request) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// errors = admProductRequestValidator.validateProduct(ProductRequest);
	// if (errors.isEmpty() == true) {
	// ArticleStatus articleStatus = getValueProduct(articleStatusRequest);
	// int result = adminService.addArticleStatus(articleStatus);
	// if (result == 0) {
	// logger.error("Error Occured while adding data into table");
	// AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
	// appMessages.getError_occured());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// } else {
	// AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
	// appMessages.getAdmin_product(),
	// null, null);
	// return ResponseEntity.ok().body(response);
	// }
	// } else if (errors.isEmpty() == false) {
	// AdmResponse response = responseWithData(AdminConstants.ERROR_CODE,
	// appMessages.getError(), errors, null);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// return new ResponseEntity<String>(HttpStatus.OK);

	// }
	//
	// private ArticleStatus getValueProduct(ArticleStatusRequest
	// articleStatusRequest) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@ApiOperation(value = "remove Workflowstatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeWorkFlowStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeWorkFlowStatus(@RequestBody WorkFlowStatusRequest workFlowStatusRequest,
			HttpServletRequest request) {
		int result = adminService.removeWorkFlowStatus(workFlowStatusRequest.workflowId);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}

	}

	// @ApiOperation(value = "add articleStatus", authorizations =
	// @Authorization(value = "Bearer"))
	// @RequestMapping(value = "/addArticleStatus", method = RequestMethod.POST,
	// produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	// public ResponseEntity<?> addArticleStatus(@RequestBody ArticleStatusRequest
	// articleStatusRequest, HttpServletRequest request) {
	// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
	// HashMap<String, String>>();
	// errors = admProductRequestValidator.validateProduct(ProductRequest);
	// if (errors.isEmpty() == true) {
	// ArticleStatus articleStatus = getValueProduct(articleStatusRequest);
	// int result = adminService.addArticleStatus(articleStatus);
	// if (result == 0) {
	// logger.error("Error Occured while adding data into table");
	// AdmResponse response = messageResponse(AdminConstants.ERROR_CODE,
	// appMessages.getError_occured());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// } else {
	// AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
	// appMessages.getAdmin_product(),
	// null, null);
	// return ResponseEntity.ok().body(response);
	// }
	// } else if (errors.isEmpty() == false) {
	// AdmResponse response = responseWithData(AdminConstants.ERROR_CODE,
	// appMessages.getError(), errors, null);
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	// }
	// return new ResponseEntity<String>(HttpStatus.OK);

	// }
	//
	// private ArticleStatus getValueProduct(ArticleStatusRequest
	// articleStatusRequest) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	private AdmResponse responseWithData(long code, String message, Object data,
			List<RoleFieldRights> roleFieldRights) {
		AdmResponseMessage responseMessage = new AdmResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		AdmResponseData responseData = new AdmResponseData();
		responseData.setData(data);
		responseData.setRoleFieldRights(roleFieldRights);
		AdmResponse response = new AdmResponse();
		response.setResponseMessage(responseMessage);
		response.setResponseData(responseData);
		return response;
	}

	private AdmResponse messageResponse(long code, String message) {
		AdmResponseMessage responseMessage = new AdmResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		AdmResponse response = new AdmResponse();
		response.setResponseMessage(responseMessage);
		return response;
	}

	@ApiOperation(value = "advisor types", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAdvisorTypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> advisorTypes(@RequestBody AdvtypesRequest advtypesRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admAdvisorTypeRequestValidator.validate(advtypesRequest);
		if (errors.isEmpty() == true) {
			Advtypes advtypes = getValueOfAdvtypes(advtypesRequest);
			int result = adminService.advisorTypes(advtypes);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Advtypes getValueOfAdvtypes(AdvtypesRequest advtypesRequest) {
		Advtypes advtypes = new Advtypes();
		advtypes.setAdvtype(advtypesRequest.getAdvType());
		return advtypes;
	}

	@ApiOperation(value = "remove advisor types", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeAdvisorTypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeAdvisorTypes(@RequestBody AdvtypesRequest advtypesRequest,
			HttpServletRequest request) {
		// List<RoleFieldRights> roleFieldRights = null;
		// int advType = advtypesRequest.getId();
		// fetch role by id for delete
		int result = adminService.removeAdvisorTypes(advtypesRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "modify advisor types", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyAdvisorTypes", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAdvisorTypes(@RequestBody AdvtypesRequest advtypesRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating admRoleRequestValidator
		errors = admAdvisorTypeRequestValidator.validate(advtypesRequest);
		if (errors.isEmpty() == true) {

			Advtypes advtypes = getValueOfModifyAdvtypes(advtypesRequest);
			int result = adminService.modifyAdvisorTypes(advtypes);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Advtypes getValueOfModifyAdvtypes(AdvtypesRequest advtypesRequest) {
		Advtypes advtypes = new Advtypes();
		advtypes.setAdvtype(advtypesRequest.getAdvType());
		advtypes.setId(advtypesRequest.getId());
		return advtypes;
	}

	@ApiOperation(value = "followers status", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addFollowerStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> followerStatus(@RequestBody AdmFollowerRequest admFollowerRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admFollowerRequestValidator.validate(admFollowerRequest);
		if (errors.isEmpty() == true) {
			AdmFollower admFollower = getValueOfAdmFollower(admFollowerRequest);
			int result = adminService.followerStatus(admFollower);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private AdmFollower getValueOfAdmFollower(AdmFollowerRequest admFollowerRequest) {
		AdmFollower admFollower = new AdmFollower();
		admFollower.setFollowerStatusId(admFollowerRequest.getFollowerStatusId());
		admFollower.setStatus(admFollowerRequest.getStatus());
		return admFollower;
	}

	@ApiOperation(value = "modify followerStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyFollowerStatus", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyFollowerStatus(@RequestBody AdmFollowerRequest admFollowerRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admFollowerRequestValidator.validate(admFollowerRequest);
		if (errors.isEmpty() == true) {
			AdmFollower admFollower = getValueOfModifyAdmFollower(admFollowerRequest);
			int result = adminService.modifyFollowerStatus(admFollower);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private AdmFollower getValueOfModifyAdmFollower(AdmFollowerRequest admFollowerRequest) {
		AdmFollower admFollower = new AdmFollower();
		admFollower.setStatus(admFollowerRequest.getStatus());
		admFollower.setFollowerStatusId(admFollowerRequest.getFollowerStatusId());
		return admFollower;
	}

	@ApiOperation(value = "remove followerStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeFollowerStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeFollowerStatus(@RequestBody AdmFollowerRequest admFollowerRequest,
			HttpServletRequest request) {
		// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
		// HashMap<String, String>>();
		// errors = admAdvisorTypeRequestValidator.validate(advtypesRequest);
		// if (errors.isEmpty() == true) {
		// AdmFollower admFollower = getValueOfAdmFollower(admFollowerRequest);
		int result = adminService.removeFollowerStatus(admFollowerRequest.getFollowerStatusId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "priority item", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addPriorityItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> priorityItem(@RequestBody AdmPriorityRequest admPriorityRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admPriorityRequestValidator.validate(admPriorityRequest);
		if (errors.isEmpty() == true) {
			AdmPriority admPriority = getValueOfAdmPriority(admPriorityRequest);
			int result = adminService.priorityItem(admPriority);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private AdmPriority getValueOfAdmPriority(AdmPriorityRequest admPriorityRequest) {
		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItemId(admPriorityRequest.getPriorityItemId());
		admPriority.setPriorityItem(admPriorityRequest.getPriorityItem());
		return admPriority;
	}

	@ApiOperation(value = "priority item", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyPriorityItem", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyPriorityItem(@RequestBody AdmPriorityRequest admPriorityRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = admPriorityRequestValidator.validate(admPriorityRequest);
		if (errors.isEmpty() == true) {
			AdmPriority admPriority = getValueOfModifyAdmPriority(admPriorityRequest);
			int result = adminService.modifyPriorityItem(admPriority);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private AdmPriority getValueOfModifyAdmPriority(AdmPriorityRequest admPriorityRequest) {
		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItemId(admPriorityRequest.getPriorityItemId());
		admPriority.setPriorityItem(admPriorityRequest.getPriorityItem());
		return admPriority;
	}

	@ApiOperation(value = "remove priorityItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removePriorityItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removePriorityItem(@RequestBody AdmPriorityRequest admPriorityRequest,
			HttpServletRequest request) {
		// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
		// HashMap<String, String>>();
		// errors = admAdvisorTypeRequestValidator.validate(advtypesRequest);
		// if (errors.isEmpty() == true) {
		// AdmFollower admFollower = getValueOfAdmFollower(admFollowerRequest);
		int result = adminService.removePriorityItem(admPriorityRequest.getPriorityItemId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "riskPortfolio", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addRiskPortfolio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> riskPortfolio(@RequestBody AdmRiskPortfolioRequest admRiskPortfolioRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = riskPortfolioRequestValitator.validate(admRiskPortfolioRequest);
		if (errors.isEmpty() == true) {
			AdmRiskPortfolio admRiskPortfolio = getValueOfRiskPortfolio(admRiskPortfolioRequest);
			int result = adminService.riskPortfolio(admRiskPortfolio);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private AdmRiskPortfolio getValueOfRiskPortfolio(AdmRiskPortfolioRequest admRiskPortfolioRequest) {
		// TODO Auto-generated method stub
		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setRiskPortfolioId(admRiskPortfolioRequest.getRiskPortfolioId());
		admRiskPortfolio.setPoints(admRiskPortfolioRequest.getPoints());
		admRiskPortfolio.setBehaviour(admRiskPortfolioRequest.getBehaviour());
		admRiskPortfolio.setEquity(admRiskPortfolioRequest.getEquity());
		admRiskPortfolio.setDebt(admRiskPortfolioRequest.getDebt());
		admRiskPortfolio.setCash(admRiskPortfolioRequest.getCash());
		return admRiskPortfolio;
	}

	@ApiOperation(value = "modifyRiskPortfolio", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyRiskPortfolio", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyRiskPortfolio(@RequestBody AdmRiskPortfolioRequest admRiskPortfolioRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = riskPortfolioRequestValitator.validate(admRiskPortfolioRequest);
		if (errors.isEmpty() == true) {
			AdmRiskPortfolio admRiskPortfolio = getValueOfModifyRiskPortfolio(admRiskPortfolioRequest);
			int result = adminService.modifyRiskPortfolio(admRiskPortfolio);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private AdmRiskPortfolio getValueOfModifyRiskPortfolio(AdmRiskPortfolioRequest admRiskPortfolioRequest) {
		// TODO Auto-generated method stub
		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setRiskPortfolioId(admRiskPortfolioRequest.getRiskPortfolioId());
		admRiskPortfolio.setPoints(admRiskPortfolioRequest.getPoints());
		admRiskPortfolio.setBehaviour(admRiskPortfolioRequest.getBehaviour());
		admRiskPortfolio.setEquity(admRiskPortfolioRequest.getEquity());
		admRiskPortfolio.setDebt(admRiskPortfolioRequest.getDebt());
		admRiskPortfolio.setCash(admRiskPortfolioRequest.getCash());
		return admRiskPortfolio;
	}

	@ApiOperation(value = "removeRiskPortfolio", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeRiskPortfolio", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeRiskPortfolio(@RequestBody AdmRiskPortfolioRequest admRiskPortfolioRequest,
			HttpServletRequest request) {
		// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
		// HashMap<String, String>>();
		// errors = admAdvisorTypeRequestValidator.validate(advtypesRequest);
		// if (errors.isEmpty() == true) {
		// AdmFollower admFollower = getValueOfAdmFollower(admFollowerRequest);
		int result = adminService.removeRiskPortfolio(admRiskPortfolioRequest.getRiskPortfolioId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "license", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addLicense", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> license(@RequestBody LicenseRequest licenseRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = licenseRequestValidator.validate(licenseRequest);
		if (errors.isEmpty() == true) {
			License license = getValueOfLicense(licenseRequest);
			int result = adminService.license(license);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private License getValueOfLicense(LicenseRequest licenseRequest) {
		// TODO Auto-generated method stub
		License license = new License();
		// license.setLicId(licenseRequest.getLicId());
		license.setLicense(licenseRequest.getLicense());
		license.setIssuedBy(licenseRequest.getIssuedBy());
		license.setProdId(licenseRequest.getProdId());
		return license;
	}

	@ApiOperation(value = "modifyLicense", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyLicense", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyLicense(@RequestBody LicenseRequest licenseRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = licenseRequestValidator.validate(licenseRequest);
		if (errors.isEmpty() == true) {
			License license = getValueOfModifyLicense(licenseRequest);
			int result = adminService.modifyLicense(license);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private License getValueOfModifyLicense(LicenseRequest licenseRequest) {
		// TODO Auto-generated method stub
		License license = new License();
		license.setLicId(licenseRequest.getLicId());
		license.setLicense(licenseRequest.getLicense());
		license.setIssuedBy(licenseRequest.getIssuedBy());
		license.setProdId(licenseRequest.getProdId());
		return license;
	}

	@ApiOperation(value = "removeLicense", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeLicense", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeLicense(@RequestBody LicenseRequest licenseRequest, HttpServletRequest request) {
		// HashMap<String, HashMap<String, String>> errors = new HashMap<String,
		// HashMap<String, String>>();
		// errors = admAdvisorTypeRequestValidator.validate(advtypesRequest);
		// if (errors.isEmpty() == true) {
		// AdmFollower admFollower = getValueOfAdmFollower(admFollowerRequest);
		int result = adminService.removeLicense(licenseRequest.getLicId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "add insuranceItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addInsuranceItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addInsuranceItem(@RequestBody InsuranceItemRequest insuranceItemRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = insuranceItemRequestValidator.validate(insuranceItemRequest);
		if (errors.isEmpty() == true) {
			InsuranceItem insuranceItem = getValueInsuranceItem(insuranceItemRequest);
			int result = adminService.addInsuranceItem(insuranceItem);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private InsuranceItem getValueInsuranceItem(InsuranceItemRequest insuranceItemRequest) {
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItem(insuranceItemRequest.getInsuranceItem());
		insuranceItem.setValue(insuranceItemRequest.getValue());
		return insuranceItem;
	}

	@ApiOperation(value = "modify insuranceItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyInsuranceItem", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyInsuranceItem(@RequestBody InsuranceItemRequest insuranceItemRequest,
			HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = insuranceItemRequestValidator.validate(insuranceItemRequest);
		if (errors.isEmpty() == true) {
			InsuranceItem insuranceItem = getModifyInsuranceItem(insuranceItemRequest);
			int result = adminService.modifyInsuranceItem(insuranceItem);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private InsuranceItem getModifyInsuranceItem(InsuranceItemRequest insuranceItemRequest) {
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItemId(insuranceItemRequest.getInsuranceItemId());
		insuranceItem.setInsuranceItem(insuranceItemRequest.getInsuranceItem());
		insuranceItem.setValue(insuranceItemRequest.getValue());
		return insuranceItem;
	}

	@ApiOperation(value = "remove insuranceItem", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeInsuranceItem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeInsuranceItem(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		int result = adminService.removeInsuranceItem(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {

			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "add userType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addUserType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addUserType(@RequestBody UserTypeRequest userTypeRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = userTypeRequestValidator.validate(userTypeRequest);
		if (errors.isEmpty() == true) {
			UserType userType = getValueUserType(userTypeRequest);
			int result = adminService.addUserType(userType);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private UserType getValueUserType(UserTypeRequest userTypeRequest) {
		UserType userType = new UserType();
		userType.setDesc(userTypeRequest.getDesc());
		return userType;
	}

	@ApiOperation(value = "modify userType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyUserType", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyUserType(@RequestBody UserTypeRequest userTypeRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = userTypeRequestValidator.validate(userTypeRequest);
		if (errors.isEmpty() == true) {
			UserType userType = getModifyUserType(userTypeRequest);
			int result = adminService.modifyUserType(userType);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private UserType getModifyUserType(UserTypeRequest userTypeRequest) {
		UserType userType = new UserType();
		userType.setId(userTypeRequest.getId());
		userType.setDesc(userTypeRequest.getDesc());
		return userType;
	}

	@ApiOperation(value = "remove userType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeUserType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeUserType(@RequestBody AdmIdRequest admIdRequest, HttpServletRequest request) {
		int result = adminService.removeUserType(admIdRequest.getId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {

			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "add Votetype", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addVotetype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addVotetype(@RequestBody VotetypeRequest votetypeRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = votetypeRequestValidator.validate(votetypeRequest);
		if (errors.isEmpty() == true) {
			Votetype votetype = getValueVotetype(votetypeRequest);
			int result = adminService.addVotetype(votetype);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Votetype getValueVotetype(VotetypeRequest votetypeRequest) {
		Votetype votetype = new Votetype();
		votetype.setDesc(votetypeRequest.getDesc());
		return votetype;
	}

	@ApiOperation(value = "modify Votetype", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyVotetype", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyVotetype(@RequestBody VotetypeRequest votetypeRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = votetypeRequestValidator.validate(votetypeRequest);
		if (errors.isEmpty() == true) {

			Votetype votetype = getValueModifyVotetype(votetypeRequest);
			int result = adminService.modifyVotetype(votetype);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Votetype getValueModifyVotetype(VotetypeRequest votetypeRequest) {
		Votetype votetype = new Votetype();
		votetype.setId(votetypeRequest.getId());

		votetype.setDesc(votetypeRequest.getDesc());
		return votetype;
	}

	@ApiOperation(value = "remove Votetype", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeVotetype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeVotetype(@RequestBody VotetypeRequest votetypeRequest, HttpServletRequest request) {// int
																														// result
																														// =
																														// adminService.removeWorkFlowStatus(workFlowStatusRequest.workflowId);

		// Acctype acctype = getValueOfAcctype(acctypeRequest);
		int result = adminService.removeVotetype(votetypeRequest.getId());

		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "add Brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addBrand", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addBrand(@RequestBody BrandRequest brandRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = brandRequestValidator.validate(brandRequest);
		if (errors.isEmpty() == true) {
			Brand brand = getValueBrand(brandRequest);
			int result = adminService.addBrand(brand);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Brand getValueBrand(BrandRequest brandRequest) {
		Brand brand = new Brand();
		brand.setBrand(brandRequest.getBrand());
		brand.setProdId(brandRequest.getProdId());

		return brand;
	}

	@ApiOperation(value = "modify Brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyBrand", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyBrand(@RequestBody BrandRequest brandRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = brandRequestValidator.validate(brandRequest);
		if (errors.isEmpty() == true) {

			Brand brand = getValueModifyBrand(brandRequest);
			int result = adminService.modifyBrand(brand);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Brand getValueModifyBrand(BrandRequest brandRequest) {
		Brand brand = new Brand();
		brand.setBrandId(brandRequest.getBrandId());
		brand.setBrand(brandRequest.getBrand());
		brand.setProdId(brandRequest.getProdId());
		return brand;
	}

	@ApiOperation(value = "remove Brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeBrand", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeBrand(@RequestBody BrandRequest brandRequest, HttpServletRequest request) {// int
																												// result
																												// =
																												// adminService.removeWorkFlowStatus(workFlowStatusRequest.workflowId);

		// Acctype acctype = getValueOfAcctype(acctypeRequest);
		int result = adminService.removeBrand(brandRequest.getBrandId());

		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "urgency", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addUrgency", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> urgency(@RequestBody UrgencyRequest urgencyRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = urgencyRequestValidator.validate(urgencyRequest);
		if (errors.isEmpty() == true) {
			Urgency urgency = getValueOfUrgency(urgencyRequest);
			int result = adminService.urgency(urgency);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Urgency getValueOfUrgency(UrgencyRequest urgencyRequest) {
		// TODO Auto-generated method stub
		Urgency urgency = new Urgency();
		urgency.setValue(urgencyRequest.getValue());
		return urgency;
	}

	@ApiOperation(value = "modifyUrgency", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyUrgency", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyUrgency(@RequestBody UrgencyRequest urgencyRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = urgencyRequestValidator.validate(urgencyRequest);
		if (errors.isEmpty() == true) {
			Urgency urgency = getValueOfModifyUrgency(urgencyRequest);
			int result = adminService.modifyUrgency(urgency);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Urgency getValueOfModifyUrgency(UrgencyRequest urgencyRequest) {
		// TODO Auto-generated method stub
		Urgency urgency = new Urgency();
		urgency.setUrgencyId(urgencyRequest.getUrgencyId());
		urgency.setValue(urgencyRequest.getValue());
		return urgency;
	}

	@ApiOperation(value = "removeUrgency", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeUrgency", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeLicense(@RequestBody UrgencyRequest urgencyRequest, HttpServletRequest request) {
		int result = adminService.removeUrgency(urgencyRequest.getUrgencyId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "account", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addAccount(@RequestBody AccountRequest accountRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = accountRequestValidator.validate(accountRequest);
		if (errors.isEmpty() == true) {
			Account account = getValueOfAccount(accountRequest);
			int result = adminService.account(account);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_added_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Account getValueOfAccount(AccountRequest accountRequest) {
		// TODO Auto-generated method stub
		Account account = new Account();
		account.setAccountEntry(accountRequest.getAccountEntry());
		account.setAccountTypeId(accountRequest.getAccountTypeId());
		return account;
	}

	@ApiOperation(value = "modifyAccount", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyAccount", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAccount(@RequestBody AccountRequest accountRequest, HttpServletRequest request) {
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		errors = accountRequestValidator.validate(accountRequest);
		if (errors.isEmpty() == true) {
			Account account = getValueOfModifyAccount(accountRequest);
			int result = adminService.modifyAccount(account);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
						appMessages.getAdmin_updated_successfully(), null, null);
				return ResponseEntity.ok().body(response);
			}
		} else if (errors.isEmpty() == false) {
			AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	private Account getValueOfModifyAccount(AccountRequest accountRequest) {
		// TODO Auto-generated method stub
		Account account = new Account();
		account.setAccountEntryId(accountRequest.getAccountEntryId());
		account.setAccountEntry(accountRequest.getAccountEntry());
		account.setAccountTypeId(accountRequest.getAccountTypeId());
		return account;
	}

	@ApiOperation(value = "removeAccount", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeAccount(@RequestBody AccountRequest accountRequest, HttpServletRequest request) {

		int result = adminService.removeAccount(accountRequest.getAccountEntryId());
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE,
					appMessages.getAdmin_deleted_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "addView", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addView", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addView(@RequestBody ViewRequest viewRequest, HttpServletRequest request) {
		View view = getViewDetails(viewRequest);
		Party party = adminService.fetchPartyByRoleBasedId(viewRequest.getViewerId());
		if (party == null) {
			AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getParty_not_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			int result = adminService.addView(view);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdmResponse response = messageResponse(AdminConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdmResponse response = messageResponse(AdminConstants.SUCCESS_CODE, appMessages.getSuccess());
				return ResponseEntity.ok().body(response);
			}
		}
	}

	private View getViewDetails(ViewRequest viewRequest) {
		View view = new View();
		if (viewRequest.getOwnerId() != null) {
			view.setOwnerId(viewRequest.getOwnerId());
		}
		if (viewRequest.getViewerId() != null) {
			view.setViewerId(viewRequest.getViewerId());
		}
		return view;
	}

	@ApiOperation(value = "fetch view count", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchCount(@RequestBody ViewIdRequest viewIdRequest, HttpServletRequest request) {
		int count = adminService.fetchCount(viewIdRequest.getOwnerId());
		AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getSuccess(), count, null);
		return ResponseEntity.ok().body(response);
	}

	@ApiOperation(value = "fetch view count list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllViewCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllViewCount(@RequestBody(required = false) ScreenIdRequest screenIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdmResponse response = responseWithData(AdminConstants.ERROR_CODE, appMessages.getAccess_denied(),
							null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdmResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		logger.info("Fetching advisor view list");

		List<View> viewList = adminService.fetchAllViewCount();
		if (viewList == null) {
			logger.info("No Record Found");
			AdmResponse response = messageResponse(AdminConstants.NO_RECORD_FOUND, appMessages.getNo_record_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdmResponse response = responseWithData(AdminConstants.SUCCESS_CODE, appMessages.getSuccess(), viewList,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

}