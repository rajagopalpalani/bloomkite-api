
package com.sowisetech.admin.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.AdvisorApplication;
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
import com.sowisetech.admin.model.InsuranceItem;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.License;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Remuneration;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.Service;
import com.sowisetech.admin.model.State;
import com.sowisetech.admin.model.Urgency;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.request.AccountRequest;
import com.sowisetech.admin.request.AccountRequestValidator;
import com.sowisetech.admin.model.UserType;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.model.Votetype;
import com.sowisetech.admin.model.Workflowstatus;
import com.sowisetech.admin.request.AcctypeRequest;
import com.sowisetech.admin.request.AdmAccTypeRequestValidator;
import com.sowisetech.admin.request.AdmAdvisorTypeRequestValidator;
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
import com.sowisetech.admin.request.WorkFlowStatusRequest;
import com.sowisetech.admin.request.WorkFlowStatusRequestValidator;
import com.sowisetech.admin.service.AdminService;
import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvisorApplication.class)
public class AdminControllerTest {

	@InjectMocks
	private AdminController adminController;

	private MockMvc mockMvc;
	@Mock
	private AdminService adminService;
	@Autowired(required = true)
	@Spy
	AdmAppMessages appMessages;
	@Mock
	private AdmRoleRequestValidator admRoleRequestValidator;
	@Mock
	private AdmAdvisorTypeRequestValidator admAdvisorTypeRequestValidator;
	@Mock
	private AdmPriorityRequestValidator admPriorityRequestValidator;
	@Mock
	private AdmFollowerRequestValidator admFollowerRequestValidator;
	@Mock
	private AdmAccTypeRequestValidator admAccTypeRequestValidator;
	@Mock
	private AdmRemunerationRequestValidator admRemunerationRequestValidator;
	@Mock
	private WorkFlowStatusRequestValidator workFlowStatusRequestValidator;
	@Mock
	private VotetypeRequestValidator votetypeRequestValidator;
	@Mock
	private BrandRequestValidator brandRequestValidator;
	@Mock
	private ServiceRequestValidator serviceRequestValidator;
	@Mock
	private AdmStateRequestValidator admStateRequestValidator;
	@Mock
	private AdminRequestValidator adminRequestValidator;
	@Mock
	private LicenseRequestValidator licenseRequestValidator;
	@Mock
	private UrgencyRequestValidator urgencyRequestValidator;
	@Mock
	private RiskPortfolioRequestValitator riskPortfolioRequestValitator;
	@Mock
	private AccountRequestValidator accountRequestValidator;
	@Mock
	private InsuranceItemRequestValidator insuranceItemRequestValidator;

	@Mock
	private AdmProductRequestValidator admProductRequestValidator;
	@Mock
	private AdmArticleStatusRequestValidator admArticleStatusRequestValidator;
	@Mock
	private AdmCashFlowItemTypeRequestValidator admCashFlowItemTypeRequestValidator;
	@Mock
	private CashFlowItemRequestValidator cashFlowItemRequestValidator;
	@Mock
	private ModifyAdminRequestValidator modifyAdminRequestValidator;
	@Mock
	private CityRequestValidator cityRequestValidator;
	@Mock
	private UserTypeRequestValidator userTypeRequestValidator;
	@Autowired(required = true)
	@Spy
	ScreenRightsConstants screenRightsConstants;
	@Mock
	ScreenRightsCommon screenRightsCommon;
	@Mock
	CommonService commonService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
	}

	@Test
	public void testEcv() throws Exception {
		this.mockMvc.perform(get("/admin-ecv")).andExpect(status().isOk());
	}

	// Fetch Admin Test//
	@Test
	public void test_fetchAll() throws Exception {
		List<Admin> admins = new ArrayList<Admin>();
		Admin adm1 = new Admin();
		adm1.setAdminId(("ADM000000000A"));
		adm1.setName("AAA");
		Admin adm2 = new Admin();
		adm2.setAdminId("ADM000000000B");
		adm2.setName("BBB");
		admins.add(adm1);
		admins.add(adm2);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		when(adminService.fetchAdminList()).thenReturn(admins);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAllAdmin").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)));
	}

	@Test
	public void test_fetchAll_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAllAdmin").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// Fetch Admin NoRecordFound Test//
	@Test
	public void test_fetchAllNoRecordFound() throws Exception {
		when(adminService.fetchAdminList()).thenReturn(null);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAllAdmin").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError());
	}

	// Signup Test//
	@Test
	public void test_signup() throws Exception {
		AdminRequest adminReq = new AdminRequest();
		adminReq.setName("name");
		adminReq.setEmailId("aaa@aaa.com");
		adminReq.setPassword("!@AS12as");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);
		int result = 1;

		when(adminRequestValidator.validate(adminReq)).thenReturn(null);
		when(adminService.fetchAdminByEmailId(adminReq.getEmailId())).thenReturn(null);
		when(adminService.generateId()).thenReturn("ADV000000000A");
		when(adminService.addAdmin(Mockito.any(Admin.class))).thenReturn(result);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/adminSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andExpect(status().isOk());
	}

	// Signup IsEmpty Test//
	@Test
	public void test_signupAdminEmpty() throws Exception {
		AdminRequest adminReq = new AdminRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);

		when(adminRequestValidator.validate(adminReq)).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/adminSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdmin_is_empty())))
				.andExpect(status().isInternalServerError());

	}

	// Signup Admin Already PresentTest//
	@Test
	public void test_signupAlreadyPresent() throws Exception {
		AdminRequest adminReq = new AdminRequest();
		adminReq.setName("name");
		adminReq.setEmailId("aaa@aaa.com");
		adminReq.setPassword("!@AS12as");

		int admin = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);

		when(adminRequestValidator.validate(adminReq)).thenReturn(null);
		when(adminService.checkPartyIsPresent(adminReq.getEmailId())).thenReturn(admin);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/adminSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdmin_already_present())))
				.andExpect(status().isInternalServerError());
	}

	// Signup ErrorOccured Test//
	@Test
	public void test_signupErrorOccured() throws Exception {
		AdminRequest adminReq = new AdminRequest();
		adminReq.setName("name");
		adminReq.setEmailId("aaa@aaa.com");
		adminReq.setPassword("!@AS12as");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);
		int result = 0;

		when(adminRequestValidator.validate(adminReq)).thenReturn(null);
		when(adminService.fetchAdminByEmailId(adminReq.getEmailId())).thenReturn(null);
		when(adminService.generateId()).thenReturn("ADV000000000A");
		when(adminService.addAdmin(Mockito.any(Admin.class))).thenReturn(result);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/adminSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError());
	}

	// Signup ValidationError Test//
	@Test
	public void test_signupValidationError() throws Exception {
		AdminRequest adminReq = new AdminRequest();
		adminReq.setName("123");
		adminReq.setEmailId("1234");
		adminReq.setPassword("1234");

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);

		when(adminRequestValidator.validate(Mockito.any(AdminRequest.class))).thenReturn(allErrors);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/adminSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseData.data", is(allErrors)))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(status().isInternalServerError());

	}

	// modify Admin Test//
	@Test
	public void test_modifyAdmin() throws Exception {
		ModifyAdminRequest adminReq = new ModifyAdminRequest();
		adminReq.setScreenId(1);
		adminReq.setAdminId("ADM000000000A");
		adminReq.setName("name");
		int admin = 1;
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(adminService.checkAdminIsPresent(Mockito.anyString())).thenReturn(admin);
		when(modifyAdminRequestValidator.validate(adminReq)).thenReturn(null);
		when(adminService.modifyAdmin(Mockito.anyString(), Mockito.any(Admin.class))).thenReturn(result);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andExpect(status().isOk());
	}

	@Test
	public void test_modifyAdmin_Unauthorised() throws Exception {
		ModifyAdminRequest adminReq = new ModifyAdminRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAllAdmin").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// modify Admin ErrorOccured Test//
	@Test
	public void test_modifyAdminErrorOccured() throws Exception {
		ModifyAdminRequest adminReq = new ModifyAdminRequest();
		adminReq.setAdminId("ADM000000000A");
		adminReq.setName("name");
		adminReq.setScreenId(1);
		int admin = 1;
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(adminService.checkAdminIsPresent(Mockito.anyString())).thenReturn(admin);
		when(modifyAdminRequestValidator.validate(adminReq)).thenReturn(null);
		when(adminService.modifyAdmin(Mockito.anyString(), Mockito.any(Admin.class))).thenReturn(result);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError());
	}

	// modify Admin NoRecordFound Test//
	@Test
	public void test_modifyAdminNoRecordFound() throws Exception {
		ModifyAdminRequest adminReq = new ModifyAdminRequest();
		adminReq.setAdminId("ADM000000000A");
		adminReq.setName("name");
		adminReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(adminService.fetchByAdminId(Mockito.anyString())).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError());
	}

	// modify Admin ValidationError Test//
	@Test
	public void test_modifyAdminValidationError() throws Exception {
		ModifyAdminRequest adminReq = new ModifyAdminRequest();
		adminReq.setAdminId("ADM000000000A");
		adminReq.setName("123");
		adminReq.setScreenId(1);
		int admin = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(adminReq);

		when(adminService.checkAdminIsPresent(Mockito.anyString())).thenReturn(admin);
		when(modifyAdminRequestValidator.validate(Mockito.any(ModifyAdminRequest.class))).thenReturn(allErrors);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", is(allErrors))).andExpect(status().isInternalServerError());

	}

	// remove Admin Test//
	@Test
	public void test_removeAdmin() throws Exception {
		AdminIdRequest admIdReq = new AdminIdRequest();
		admIdReq.setAdminId("ADM000000000B");
		admIdReq.setScreenId(1);
		int adm = 1;
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(adminService.checkAdminIsPresent(Mockito.anyString())).thenReturn(adm);
		when(adminService.removeAdmin(Mockito.anyString())).thenReturn(result);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andExpect(status().isOk());
	}

	@Test
	public void test_removeAdmin_Unauthorised() throws Exception {
		AdminIdRequest admIdReq = new AdminIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAllAdmin").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// remove Admin NoRecordFound Test//
	@Test
	public void test_removeAdminNoRecordFound() throws Exception {
		AdminIdRequest admIdReq = new AdminIdRequest();
		admIdReq.setAdminId("ADM000000000B");
		admIdReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(adminService.fetchByAdminId(Mockito.anyString())).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError());

	}

	// remove Admin ErrorOccured Test//
	@Test
	public void test_removeAdminErrorOccured() throws Exception {
		AdminIdRequest admIdReq = new AdminIdRequest();
		admIdReq.setAdminId("ADM000000000B");
		admIdReq.setScreenId(1);
		int adm = 1;
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(adminService.checkAdminIsPresent(Mockito.anyString())).thenReturn(adm);
		when(adminService.removeAdmin(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeAdmin").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void test_addScreenRightsFieldRights_Success() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		screenFieldRightsRequest.setScreenId(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		int user_role = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.checkUserRoleIsPresent(1)).thenReturn(user_role);
		when(adminService.addScreenRightsFieldRights(Mockito.anyList())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	// @Test -- this condition not used
	// public void test_addScreenRightsFieldRights_ScreenRights_AccessDenied()
	// throws Exception {
	// ScreenFieldRightsRequest screenFieldRightsRequest = new
	// ScreenFieldRightsRequest();
	// screenFieldRightsRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(MockMvcRequestBuilders.post("/addScreenRightsFieldRights").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	@Test
	public void test_addScreenRightsFieldRights_ScreenRights_Success() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		screenFieldRightsRequest.setScreenId(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		int user_role = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		when(adminService.checkUserRoleIsPresent(1)).thenReturn(user_role);
		when(adminService.addScreenRightsFieldRights(Mockito.anyList())).thenReturn(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/addScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addScreenRightsFieldRights_NotFound() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		screenFieldRightsRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchUserRoleByUserRoleId(1)).thenReturn(null);
		when(adminService.addScreenRightsFieldRights(Mockito.anyList())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_addScreenRightsFieldRights_ErrorOccured() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		screenFieldRightsRequest.setScreenId(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		int user_role = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.checkUserRoleIsPresent(1)).thenReturn(user_role);
		when(adminService.addScreenRightsFieldRights(Mockito.anyList())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyScreenRightsFieldRights_Success() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		screenFieldRightsRequest.setScreenId(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		int user_role = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(adminService.checkUserRoleIsPresent(1)).thenReturn(user_role);
		when(adminService.modifyScreenRightsFieldRights(Mockito.anyList(), Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyScreenRightsFieldRights_ScreenRights_AccessDenied() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAdvisorByUserName_ScreenRights_UnAuthorized() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyScreenRightsFieldRights_ScreenRights_Success() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		screenFieldRightsRequest.setScreenId(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		int user_role = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		when(adminService.checkUserRoleIsPresent(1)).thenReturn(user_role);
		when(adminService.modifyScreenRightsFieldRights(Mockito.anyList(), Mockito.anyInt())).thenReturn(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_modifyScreenRightsFieldRights_NotFound() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		screenFieldRightsRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchUserRoleByUserRoleId(1)).thenReturn(null);
		when(adminService.modifyScreenRightsFieldRights(Mockito.anyList(), Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_modifyScreenRightsFieldRights_ErrorOccured() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		screenFieldRightsRequest.setUser_role_id(1);
		screenFieldRightsRequest.setScreenId(1);
		List<ScreenRightsRequest> screenRightsRequestList = new ArrayList<>();
		ScreenRightsRequest screenRightsRequest = new ScreenRightsRequest();
		screenRightsRequest.setScreen_id(1);
		screenRightsRequest.setAdd_rights(1);
		screenRightsRequest.setEdit_rights(1);
		screenRightsRequest.setDelete_rights(1);
		screenRightsRequest.setView_rights(1);
		List<FieldRightsRequest> fieldRightsRequestList = new ArrayList<>();
		FieldRightsRequest fieldRightsRequest = new FieldRightsRequest();
		fieldRightsRequest.setField_id(1);
		fieldRightsRequest.setAdd_rights(1);
		fieldRightsRequest.setEdit_rights(1);
		fieldRightsRequest.setView_rights(1);
		fieldRightsRequestList.add(fieldRightsRequest);
		screenRightsRequest.setFieldRightsRequests(fieldRightsRequestList);
		screenRightsRequestList.add(screenRightsRequest);
		screenFieldRightsRequest.setScreenRightsRequest(screenRightsRequestList);
		int user_role = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(adminService.checkUserRoleIsPresent(1)).thenReturn(user_role);
		when(adminService.modifyScreenRightsFieldRights(Mockito.anyList(), Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyScreenRightsFieldRights").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addRoleAuth_Success() throws Exception {

		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.addRole(Mockito.any(RoleAuth.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	// @Test -- this condition not used
	// public void test_addRoleAuth_ScreenRights_AccessDenied() throws Exception {
	// AdmRoleRequest admRoleRequest = new AdmRoleRequest();
	// admRoleRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(admRoleRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/addRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	@Test
	public void test_addRoleAuth_ScreenRights_Success() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);

		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.addRole(Mockito.any(RoleAuth.class))).thenReturn(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	// @Test -- this condition not used
	// public void test_addRoleAuth_ScreenRights_UnAuthorized() throws Exception {
	// AdmRoleRequest admRoleRequest = new AdmRoleRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(admRoleRequest);
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/addRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_addRoleAuth_ErrorOccured() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.addRole(Mockito.any(RoleAuth.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addRoleAuth_ValidationError() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(admRoleRequestValidator.validate(Mockito.any(AdmRoleRequest.class))).thenReturn(allErrors);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/addRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_modifyRoleAuth_Success() throws Exception {

		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);

		int roleAuth = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.modifyRole(Mockito.anyInt(), Mockito.any(RoleAuth.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyRoleAuth_ScreenRights_AccessDenied() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyRoleAuth_ScreenRights_Success() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		int roleAuth = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.modifyRole(Mockito.anyInt(), Mockito.any(RoleAuth.class))).thenReturn(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_modifyRoleAuth_ScreenRights_UnAuthorized() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyRoleAuth_NotFound() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);

		when(adminService.fetchRoleByRoleId(Mockito.anyInt())).thenReturn(null);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_modifyRoleAuth_ErrorOccured() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		int roleAuth = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.modifyRole(Mockito.anyInt(), Mockito.any(RoleAuth.class))).thenReturn(0);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyRoleAuth_ValidationError() throws Exception {
		AdmRoleRequest admRoleRequest = new AdmRoleRequest();
		admRoleRequest.setName("investor");
		admRoleRequest.setScreenId(1);
		int roleAuth = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);
		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(admRoleRequestValidator.validate(Mockito.any(AdmRoleRequest.class))).thenReturn(allErrors);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_removeRoleAuth_Success() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		int roleAuth = 1;
		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(adminService.removeRole(Mockito.anyInt())).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeRoleAuth_ScreenRights_AccessDenied() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeRoleAuth_ScreenRights_Success() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		int roleAuth = 1;
		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(adminService.removeRole(Mockito.anyInt())).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeRoleAuth_ScreenRights_UnAuthorized() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removeRoleAuth_NotFound() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchRoleByRoleId(Mockito.anyInt())).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_removeRoleAuth_ErrorOccured() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		int roleAuth = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.checkRoleIsPresent(Mockito.anyInt())).thenReturn(roleAuth);
		when(adminService.removeRole(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_addUserRole_Success() throws Exception {

		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);

		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);

		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchUserRoleByUserIdAndRoleId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		when(adminService.addUserRole(Mockito.any(User_role.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUserRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addUserRole_ScreenRights_AccessDenied() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUserRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addUserRole_ScreenRights_Success() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);

		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		when(adminService.fetchUserRoleByUserIdAndRoleId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		when(adminService.addUserRole(Mockito.any(User_role.class))).thenReturn(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUserRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_addUserRole_ScreenRights_UnAuthorized() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUserRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addUserRole_AlreadyPresent_UserId() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);
		int userRole = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(adminService.checkUserRoleByUserIdAndRoleId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(userRole);
		when(adminService.addUserRole(Mockito.any(User_role.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUserRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAlready_present_for_userId())))
				.andReturn();
	}

	@Test
	public void test_addUserRole_ErrorOccured() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);
		User_role userRole = new User_role();
		userRole.setUser_id(2);
		userRole.setRole_id(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchUserRoleByUserIdAndRoleId(Mockito.anyLong(), Mockito.anyInt())).thenReturn(null);
		when(adminService.addUserRole(Mockito.any(User_role.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUserRole").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyUserRole_Success() throws Exception {

		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);
		int userRole = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(adminService.checkUserRoleIsPresent(Mockito.anyInt())).thenReturn(userRole);
		when(adminService.modifyUserRole(Mockito.anyInt(), Mockito.any(User_role.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyUserRole_ScreenRights_AccessDenied() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyUserRole_ScreenRights_Success() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);

		User_role userRole = new User_role();
		userRole.setUser_id(2);
		userRole.setRole_id(2);
		int userRolePresent = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		when(adminService.checkUserRoleIsPresent(Mockito.anyInt())).thenReturn(userRolePresent);
		when(adminService.modifyUserRole(Mockito.anyInt(), Mockito.any(User_role.class))).thenReturn(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_modifyUserRole_ScreenRights_UnAuthorized() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyUserRole_NotFound() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);
		User_role userRole = new User_role();
		userRole.setUser_id(2);
		userRole.setRole_id(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchUserRoleByUserRoleId(Mockito.anyInt())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_modifyUserRole_ErrorOccured() throws Exception {
		AdmUserRoleRequest admUserRoleRequest = new AdmUserRoleRequest();
		admUserRoleRequest.setUser_id(1);
		admUserRoleRequest.setRole_id(1);
		admUserRoleRequest.setScreenId(1);
		int userRole = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admUserRoleRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.checkUserRoleIsPresent(Mockito.anyInt())).thenReturn(userRole);
		when(adminService.modifyUserRole(Mockito.anyInt(), Mockito.any(User_role.class))).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeUserRole_Success() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		User_role userRole = new User_role();
		userRole.setUser_id(2);
		userRole.setRole_id(2);
		int userRolePresent = 1;

		when(adminService.checkUserRoleIsPresent(Mockito.anyInt())).thenReturn(userRolePresent);
		when(adminService.removeUserRole(Mockito.anyInt())).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeUserRole_ScreenRights_AccessDenied() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeUserRole_ScreenRights_Success() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);

		int userRole = 1;

		when(adminService.checkUserRoleIsPresent(Mockito.anyInt())).thenReturn(userRole);
		when(adminService.removeUserRole(Mockito.anyInt())).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeUserRole_ScreenRights_UnAuthorized() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removeUserRole_NotFound() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		User_role userRole = new User_role();
		userRole.setUser_id(2);
		userRole.setRole_id(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(adminService.fetchRoleByRoleId(Mockito.anyInt())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_removeUserRole_ErrorOccured() throws Exception {
		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(1);
		admIdRequest.setScreenId(1);
		int userRole = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(adminService.checkUserRoleIsPresent(Mockito.anyInt())).thenReturn(userRole);
		when(adminService.removeUserRole(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserRole").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_addAdvisorTypes_Success() throws Exception {

		AdvtypesRequest advtypesRequest = new AdvtypesRequest();
		advtypesRequest.setAdvType("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advtypesRequest);

		when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.advisorTypes(Mockito.any(Advtypes.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvisorTypes").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addAdvisorTypes_ErrorOccured() throws Exception {

		AdvtypesRequest advtypesRequest = new AdvtypesRequest();
		advtypesRequest.setAdvType("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advtypesRequest);

		when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.advisorTypes(Mockito.any(Advtypes.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvisorTypes").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyAdvisorTypes_Success() throws Exception {

		AdvtypesRequest advtypesRequest = new AdvtypesRequest();
		advtypesRequest.setId(2);
		advtypesRequest.setAdvType("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advtypesRequest);

		when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.modifyAdvisorTypes(Mockito.any(Advtypes.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvisorTypes").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyAdvisorTypes_ErrorOccured() throws Exception {

		AdvtypesRequest advtypesRequest = new AdvtypesRequest();
		advtypesRequest.setAdvType("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advtypesRequest);

		when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.advisorTypes(Mockito.any(Advtypes.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvisorTypes").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeAdvisorTypes_Success() throws Exception {

		AdvtypesRequest advtypesRequest = new AdvtypesRequest();
		advtypesRequest.setId(2);
		// advtypesRequest.setAdvType("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advtypesRequest);

		// when(admRoleRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.removeAdvisorTypes(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeAdvisorTypes").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeAdvisorTypes_ErrorOccured() throws Exception {

		AdvtypesRequest advtypesRequest = new AdvtypesRequest();
		advtypesRequest.setAdvType("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advtypesRequest);

		when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.advisorTypes(Mockito.any(Advtypes.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeAdvisorTypes").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addFollowerStatus_Success() throws Exception {

		AdmFollowerRequest admFollowerRequest = new AdmFollowerRequest();
		admFollowerRequest.setStatus("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admFollowerRequest);

		when(admFollowerRequestValidator.validate(admFollowerRequest)).thenReturn(null);
		when(adminService.followerStatus(Mockito.any(AdmFollower.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addFollowerStatus_ErrorOccured() throws Exception {

		AdmFollowerRequest admFollowerRequest = new AdmFollowerRequest();
		admFollowerRequest.setStatus("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admFollowerRequest);

		when(admFollowerRequestValidator.validate(admFollowerRequest)).thenReturn(null);
		when(adminService.followerStatus(Mockito.any(AdmFollower.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyFollowerStatus_Success() throws Exception {

		AdmFollowerRequest admFollowerRequest = new AdmFollowerRequest();
		admFollowerRequest.setFollowerStatusId(2);
		admFollowerRequest.setStatus("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admFollowerRequest);

		when(admFollowerRequestValidator.validate(admFollowerRequest)).thenReturn(null);
		when(adminService.modifyFollowerStatus(Mockito.any(AdmFollower.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyFollowerStatus_ErrorOccured() throws Exception {

		AdmFollowerRequest admFollowerRequest = new AdmFollowerRequest();
		admFollowerRequest.setFollowerStatusId(2);
		admFollowerRequest.setStatus("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admFollowerRequest);

		when(admFollowerRequestValidator.validate(admFollowerRequest)).thenReturn(null);
		when(adminService.modifyFollowerStatus(Mockito.any(AdmFollower.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeFollowerStatus_Success() throws Exception {

		AdmFollowerRequest admFollowerRequest = new AdmFollowerRequest();
		// admFollowerRequest.setStatus("investor");
		admFollowerRequest.setFollowerStatusId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admFollowerRequest);

		// when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.removeFollowerStatus(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeFollowerStatus_ErrorOccured() throws Exception {

		AdmFollowerRequest admFollowerRequest = new AdmFollowerRequest();
		// admFollowerRequest.setStatus("investor");
		admFollowerRequest.setFollowerStatusId(2);

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(admFollowerRequest);
		// when(admAdvisorTypeRequestValidator.validate(admFollowerRequest)).thenReturn(null);
		when(adminService.removeFollowerStatus(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addPriorityItem_Success() throws Exception {

		AdmPriorityRequest admPriorityRequest = new AdmPriorityRequest();
		admPriorityRequest.setPriorityItem("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admPriorityRequest);

		when(admPriorityRequestValidator.validate(admPriorityRequest)).thenReturn(null);
		when(adminService.priorityItem(Mockito.any(AdmPriority.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPriorityItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addPriorityItem_ErrorOccured() throws Exception {

		AdmPriorityRequest admPriorityRequest = new AdmPriorityRequest();
		admPriorityRequest.setPriorityItem("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admPriorityRequest);

		//
		when(admPriorityRequestValidator.validate(admPriorityRequest)).thenReturn(null);
		when(adminService.priorityItem(Mockito.any(AdmPriority.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPriorityItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyPriorityItem_Success() throws Exception {

		AdmPriorityRequest admPriorityRequest = new AdmPriorityRequest();
		admPriorityRequest.setPriorityItemId(2);
		admPriorityRequest.setPriorityItem("investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admPriorityRequest);

		//
		when(admPriorityRequestValidator.validate(admPriorityRequest)).thenReturn(null);
		when(adminService.modifyPriorityItem(Mockito.any(AdmPriority.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyPriorityItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyPriorityItem_ErrorOccured() throws Exception {

		AdmPriorityRequest admPriorityRequest = new AdmPriorityRequest();
		admPriorityRequest.setPriorityItem("investor");
		admPriorityRequest.setPriorityItemId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admPriorityRequest);

		when(admPriorityRequestValidator.validate(admPriorityRequest)).thenReturn(null);
		when(adminService.modifyPriorityItem(Mockito.any(AdmPriority.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyPriorityItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removePriorityItem_Success() throws Exception {

		AdmPriorityRequest admPriorityRequest = new AdmPriorityRequest();
		// admPriorityRequest.setPriorityItem("investor");
		admPriorityRequest.setPriorityItemId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admPriorityRequest);
		// when(admAdvisorTypeRequestValidator.validate(advtypesRequest)).thenReturn(null);
		when(adminService.removePriorityItem(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removePriorityItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removePriorityItem_ErrorOccured() throws Exception {

		AdmPriorityRequest admPriorityRequest = new AdmPriorityRequest();
		// admPriorityRequest.setPriorityItem("investor");
		admPriorityRequest.setPriorityItemId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admPriorityRequest);
		// when(admAdvisorTypeRequestValidator.validate(admFollowerRequest)).thenReturn(null);
		when(adminService.removePriorityItem(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeFollowerStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addAccType_Success() throws Exception {

		AcctypeRequest acctypeRequest = new AcctypeRequest();
		acctypeRequest.setAccType("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(acctypeRequest);

		when(admAccTypeRequestValidator.validate(acctypeRequest)).thenReturn(null);
		when(adminService.saveAddAcctype(Mockito.any(Acctype.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addAcctype").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdd_accountType())))
				.andReturn();
	}

	@Test
	public void test_addAccType_ErrorOccured() throws Exception {

		AcctypeRequest acctypeRequest = new AcctypeRequest();
		acctypeRequest.setAccType("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(acctypeRequest);

		when(admAccTypeRequestValidator.validate(acctypeRequest)).thenReturn(null);
		when(adminService.saveAddAcctype(Mockito.any(Acctype.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addAcctype").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyAccType_Success() throws Exception {

		AcctypeRequest acctypeRequest = new AcctypeRequest();
		acctypeRequest.setAccTypeId(2);
		acctypeRequest.setAccType("Investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(acctypeRequest);

		when(admAccTypeRequestValidator.validate(acctypeRequest)).thenReturn(null);
		when(adminService.modifyAcctype(Mockito.any(Acctype.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAcctype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getModify_accountType())))
				.andReturn();
	}

	@Test
	public void test_modifyAccType_ErrorOccured() throws Exception {

		AcctypeRequest acctypeRequest = new AcctypeRequest();
		acctypeRequest.setAccTypeId(2);
		acctypeRequest.setAccType("Investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(acctypeRequest);

		when(admAccTypeRequestValidator.validate(acctypeRequest)).thenReturn(null);
		when(adminService.modifyAcctype(Mockito.any(Acctype.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAcctype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeAccType_Success() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.RemoveAcctype(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/RemoveAcctype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getRemove_accountType())))
				.andReturn();
	}

	@Test
	public void test_removeAccType_ErrorOccured() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		// when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.RemoveAcctype(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/RemoveAcctype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addProduct_Success() throws Exception {

		ProductRequest productRequest = new ProductRequest();
		productRequest.setProduct("Goal planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(productRequest);

		when(admProductRequestValidator.validateProduct(productRequest)).thenReturn(null);
		when(adminService.addProducts(Mockito.any(Product.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addProducts").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdmin_product())))
				.andReturn();
	}

	@Test
	public void test_addProduct_ErrorOccured() throws Exception {

		ProductRequest productRequest = new ProductRequest();
		productRequest.setProduct("Goal planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(productRequest);

		when(admProductRequestValidator.validateProduct(productRequest)).thenReturn(null);
		when(adminService.addProducts(Mockito.any(Product.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addProducts").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyProduct_Success() throws Exception {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setProdId(3);
		productRequest.setProduct("Life Insurance");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(productRequest);

		when(admProductRequestValidator.validateProduct(productRequest)).thenReturn(null);
		when(adminService.modifyProduct(Mockito.any(Product.class))).thenReturn(3);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyProducts").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getModify_product())))
				.andReturn();
	}

	@Test
	public void test_modifyProduct_ErrorOccured() throws Exception {
		ProductRequest productRequest = new ProductRequest();
		productRequest.setProdId(3);
		productRequest.setProduct("Life Insurance");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(productRequest);

		when(admProductRequestValidator.validateProduct(productRequest)).thenReturn(null);
		when(adminService.modifyProduct(Mockito.any(Product.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyProducts").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeProducts_Success() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeProducts(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeProducts").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getRemove_product())))
				.andReturn();
	}

	@Test
	public void test_addRiskPortpolio_Success() throws Exception {

		AdmRiskPortfolioRequest admRiskPortfolioRequest = new AdmRiskPortfolioRequest();
		admRiskPortfolioRequest.setPoints("investor");
		admRiskPortfolioRequest.setBehaviour("corporate");
		admRiskPortfolioRequest.setEquity(10);
		admRiskPortfolioRequest.setDebt(20);
		admRiskPortfolioRequest.setCash(30);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRiskPortfolioRequest);

		when(riskPortfolioRequestValitator.validate(admRiskPortfolioRequest)).thenReturn(null);
		when(adminService.riskPortfolio(Mockito.any(AdmRiskPortfolio.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addRiskPortfolio").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addRiskPortfolio_ErrorOccured() throws Exception {

		AdmRiskPortfolioRequest admRiskPortfolioRequest = new AdmRiskPortfolioRequest();
		admRiskPortfolioRequest.setPoints("investor");
		admRiskPortfolioRequest.setBehaviour("corporate");
		admRiskPortfolioRequest.setEquity(10);
		admRiskPortfolioRequest.setDebt(20);
		admRiskPortfolioRequest.setCash(30);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRiskPortfolioRequest);

		when(riskPortfolioRequestValitator.validate(admRiskPortfolioRequest)).thenReturn(null);
		when(adminService.riskPortfolio(Mockito.any(AdmRiskPortfolio.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addRiskPortfolio").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyRiskPortfolio_Success() throws Exception {

		AdmRiskPortfolioRequest admRiskPortfolioRequest = new AdmRiskPortfolioRequest();
		admRiskPortfolioRequest.setRiskPortfolioId(1);
		admRiskPortfolioRequest.setPoints("mutual");
		admRiskPortfolioRequest.setBehaviour("business");
		admRiskPortfolioRequest.setEquity(78);
		admRiskPortfolioRequest.setDebt(58);
		admRiskPortfolioRequest.setCash(33);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRiskPortfolioRequest);

		when(riskPortfolioRequestValitator.validate(admRiskPortfolioRequest)).thenReturn(null);
		when(adminService.modifyRiskPortfolio(Mockito.any(AdmRiskPortfolio.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyRiskPortfolio").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyRiskPortfolio_ErrorOccured() throws Exception {

		AdmRiskPortfolioRequest admRiskPortfolioRequest = new AdmRiskPortfolioRequest();
		admRiskPortfolioRequest.setRiskPortfolioId(1);
		admRiskPortfolioRequest.setPoints("investor");
		admRiskPortfolioRequest.setBehaviour("corporate");
		admRiskPortfolioRequest.setEquity(10);
		admRiskPortfolioRequest.setDebt(20);
		admRiskPortfolioRequest.setCash(30);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRiskPortfolioRequest);

		when(riskPortfolioRequestValitator.validate(admRiskPortfolioRequest)).thenReturn(null);
		when(adminService.modifyRiskPortfolio(Mockito.any(AdmRiskPortfolio.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyRiskPortfolio").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeRiskPortfolio_Success() throws Exception {


		AdmRiskPortfolioRequest admRiskPortfolioRequest = new AdmRiskPortfolioRequest();
		admRiskPortfolioRequest.setRiskPortfolioId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRiskPortfolioRequest);

		when(adminService.removeRiskPortfolio(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeRiskPortfolio").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeRiskPortfolio_ErrorOccured() throws Exception {

		AdmRiskPortfolioRequest admRiskPortfolioRequest = new AdmRiskPortfolioRequest();
		admRiskPortfolioRequest.setRiskPortfolioId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admRiskPortfolioRequest);

		when(adminService.removeRiskPortfolio(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeRiskPortfolio").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addLicense_Success() throws Exception {

		LicenseRequest licenseRequest = new LicenseRequest();
		licenseRequest.setLicense("investor");
		licenseRequest.setIssuedBy("corporate");
		licenseRequest.setProdId(10);		
		licenseRequest.setProdId(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(licenseRequest);

		when(licenseRequestValidator.validate(licenseRequest)).thenReturn(null);
		when(adminService.license(Mockito.any(License.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addLicense").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addLicense_ErrorOccured() throws Exception {
		LicenseRequest licenseRequest = new LicenseRequest();
		licenseRequest.setLicense("investor");
		licenseRequest.setIssuedBy("corporate");
		licenseRequest.setProdId(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(licenseRequest);

		when(licenseRequestValidator.validate(licenseRequest)).thenReturn(null);
		when(adminService.license(Mockito.any(License.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addLicense").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyLicense_Success() throws Exception {

		LicenseRequest licenseRequest = new LicenseRequest();
		licenseRequest.setLicId(1);
		licenseRequest.setLicense("investor");
		licenseRequest.setIssuedBy("corporate");
		licenseRequest.setProdId(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(licenseRequest);

		when(licenseRequestValidator.validate(licenseRequest)).thenReturn(null);
		when(adminService.modifyLicense(Mockito.any(License.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyLicense").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyLicense_ErrorOccured() throws Exception {

		LicenseRequest licenseRequest = new LicenseRequest();
		licenseRequest.setLicId(1);
		licenseRequest.setLicense("investor");
		licenseRequest.setIssuedBy("corporate");
		licenseRequest.setProdId(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(licenseRequest);

		when(licenseRequestValidator.validate(licenseRequest)).thenReturn(null);
		when(adminService.modifyLicense(Mockito.any(License.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyLicense").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeLicense_Success() throws Exception {
		LicenseRequest licenseRequest = new LicenseRequest();
		licenseRequest.setLicId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(licenseRequest);

		when(adminService.removeLicense(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeLicense").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeLicense_ErrorOccured() throws Exception {

		LicenseRequest licenseRequest = new LicenseRequest();
		licenseRequest.setLicId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(licenseRequest);

		when(adminService.removeLicense(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeLicense").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}
	@Test
	public void test_addUrgency_Success() throws Exception {

		UrgencyRequest urgencyRequest = new UrgencyRequest();
		urgencyRequest.setValue("low");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(urgencyRequest);
				
		when(urgencyRequestValidator.validate(urgencyRequest)).thenReturn(null);
		when(adminService.urgency(Mockito.any(Urgency.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUrgency").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_addUrgency_ErrorOccured() throws Exception {

		UrgencyRequest urgencyRequest = new UrgencyRequest();
		urgencyRequest.setValue("low");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(urgencyRequest);
		
		when(urgencyRequestValidator.validate(urgencyRequest)).thenReturn(null);
		when(adminService.urgency(Mockito.any(Urgency.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addUrgency").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	@Test
	public void test_modifyUrgency_Success() throws Exception {

		UrgencyRequest urgencyRequest = new UrgencyRequest();
		urgencyRequest.setValue("low");
		urgencyRequest.setUrgencyId(2);
		

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(urgencyRequest);
				
		when(urgencyRequestValidator.validate(urgencyRequest)).thenReturn(null);
		when(adminService.modifyUrgency(Mockito.any(Urgency.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyUrgency").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_modifyUrgency_ErrorOccured() throws Exception {

		UrgencyRequest urgencyRequest = new UrgencyRequest();
		urgencyRequest.setValue("low");
		urgencyRequest.setUrgencyId(2);
		

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(urgencyRequest);
		
		when(urgencyRequestValidator.validate(urgencyRequest)).thenReturn(null);
		when(adminService.modifyUrgency(Mockito.any(Urgency.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyUrgency").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	@Test
	public void test_removeUrgency_Success() throws Exception {

		UrgencyRequest urgencyRequest = new UrgencyRequest();
		urgencyRequest.setUrgencyId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(urgencyRequest);
		when(adminService.removeUrgency(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeUrgency").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_removeUrgency_ErrorOccured() throws Exception {
		
		UrgencyRequest urgencyRequest = new UrgencyRequest();
		urgencyRequest.setUrgencyId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(urgencyRequest);
		when(adminService.removeUrgency(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeUrgency").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addAccount_Success() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountEntry("low");
		accountRequest.setAccountTypeId(7);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(accountRequest);
				
		when(accountRequestValidator.validate(accountRequest)).thenReturn(null);
		when(adminService.account(Mockito.any(Account.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addAccount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_addAccount_ErrorOccured() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountEntry("low");
		accountRequest.setAccountTypeId(7);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(accountRequest);
		
		when(accountRequestValidator.validate(accountRequest)).thenReturn(null);
		when(adminService.account(Mockito.any(Account.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addAccount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	
	@Test
	public void test_modifyAccount_Success() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountEntryId(1);
		accountRequest.setAccountEntry("low");
		accountRequest.setAccountTypeId(7);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(accountRequest);
		
		when(accountRequestValidator.validate(accountRequest)).thenReturn(null);
		when(adminService.modifyAccount(Mockito.any(Account.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyAccount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_modifyAccount_ErrorOccured() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountEntryId(1);
		accountRequest.setAccountEntry("low");
		accountRequest.setAccountTypeId(7);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(accountRequest);
		
		when(accountRequestValidator.validate(accountRequest)).thenReturn(null);
		when(adminService.modifyAccount(Mockito.any(Account.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyAccount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	
	@Test
	public void test_removeAccount_Success() throws Exception {

		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountEntryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(accountRequest);
		when(adminService.removeAccount(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeAccount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_removeAccount_ErrorOccured() throws Exception {
		
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountEntryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(accountRequest);
		when(adminService.removeAccount(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeAccount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	
	@Test
	public void test_addWorkflowstatus_Success() throws Exception {

		WorkFlowStatusRequest workFlowStatusRequest = new WorkFlowStatusRequest();
		workFlowStatusRequest.setStatus("Financial planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(workFlowStatusRequest);

		when(workFlowStatusRequestValidator.validate(workFlowStatusRequest)).thenReturn(null);
		when(adminService.addWorkflowstatus(Mockito.any(Workflowstatus.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addWorkflowstatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addWorkflowstatus_ErrorOccured() throws Exception {

		WorkFlowStatusRequest workFlowStatusRequest = new WorkFlowStatusRequest();
		workFlowStatusRequest.setStatus("Financial planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(workFlowStatusRequest);

		when(workFlowStatusRequestValidator.validate(workFlowStatusRequest)).thenReturn(null);
		when(adminService.addWorkflowstatus(Mockito.any(Workflowstatus.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addWorkflowstatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyWorkflowstatus_Success() throws Exception {

		WorkFlowStatusRequest workFlowStatusRequest = new WorkFlowStatusRequest();
		workFlowStatusRequest.setWorkflowId(2);
		workFlowStatusRequest.setStatus("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(workFlowStatusRequest);

		when(workFlowStatusRequestValidator.validate(workFlowStatusRequest)).thenReturn(null);
		when(adminService.modifyWorkflowstatus(Mockito.any(Workflowstatus.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyWorkflowstatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyWorkflowstatus_ErrorOccured() throws Exception {

		WorkFlowStatusRequest workFlowStatusRequest = new WorkFlowStatusRequest();
		workFlowStatusRequest.setWorkflowId(2);
		workFlowStatusRequest.setStatus("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(workFlowStatusRequest);

		when(workFlowStatusRequestValidator.validate(workFlowStatusRequest)).thenReturn(null);
		when(adminService.modifyWorkflowstatus(Mockito.any(Workflowstatus.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyWorkflowstatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeWorkFlowStatus_Success() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeWorkFlowStatus(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeWorkFlowStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeWorkFlowStatus_ErrorOccured() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeWorkFlowStatus(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeWorkFlowStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addService_Success() throws Exception {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setService("Financial planning");
		serviceRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(serviceRequest);

		when(serviceRequestValidator.validate(serviceRequest)).thenReturn(null);
		when(adminService.addService(Mockito.any(Service.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addService").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addRemuneration_Success() throws Exception {

		RemunerationRequest remunerationRequest = new RemunerationRequest();
		remunerationRequest.setRemuneration("Financial planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(remunerationRequest);

		when(admRemunerationRequestValidator.validate(remunerationRequest)).thenReturn(null);
		when(adminService.addRemuneration(Mockito.any(Remuneration.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addService_ErrorOccured() throws Exception {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setService("Financial planning");
		serviceRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(serviceRequest);

		when(serviceRequestValidator.validate(serviceRequest)).thenReturn(null);
		when(adminService.addService(Mockito.any(Service.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addService").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyService_Success() throws Exception {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId(2);
		serviceRequest.setService("Risk profile");
		serviceRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(serviceRequest);

		when(adminService.modifyService(Mockito.any(Service.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyService").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyService_ErrorOccured() throws Exception {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId(2);
		serviceRequest.setService("Risk profile");
		serviceRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(serviceRequest);

		when(adminService.modifyService(Mockito.any(Service.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyService").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeService_Success() throws Exception {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(serviceRequest);

		when(adminService.removeService(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeService").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeService_ErrorOccured() throws Exception {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(serviceRequest);

		when(adminService.removeService(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeService").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addRemuneration_ErrorOccured() throws Exception {

		RemunerationRequest remunerationRequest = new RemunerationRequest();
		remunerationRequest.setRemuneration("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(remunerationRequest);

		when(admRemunerationRequestValidator.validate(remunerationRequest)).thenReturn(null);
		when(adminService.addRemuneration(Mockito.any(Remuneration.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyRemuneration_Success() throws Exception {

		RemunerationRequest remunerationRequest = new RemunerationRequest();
		remunerationRequest.setRemId(2);
		remunerationRequest.setRemuneration("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(remunerationRequest);

		when(admRemunerationRequestValidator.validate(remunerationRequest)).thenReturn(null);
		when(adminService.modifyRemuneration(Mockito.any(Remuneration.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyRemuneration_ErrorOccured() throws Exception {

		RemunerationRequest remunerationRequest = new RemunerationRequest();
		remunerationRequest.setRemId(2);
		remunerationRequest.setRemuneration("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(remunerationRequest);

		when(admRemunerationRequestValidator.validate(remunerationRequest)).thenReturn(null);
		when(adminService.modifyRemuneration(Mockito.any(Remuneration.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeRemuneration_Success() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeRemuneration(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeRemuneration_ErrorOccured() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeRemuneration(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addState_Success() throws Exception {

		StateRequest stateRequest = new StateRequest();
		stateRequest.setState("Financial planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(stateRequest);

		when(admStateRequestValidator.validate(stateRequest)).thenReturn(null);
		when(adminService.addState(Mockito.any(State.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addState").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addState_ErrorOccured() throws Exception {

		StateRequest stateRequest = new StateRequest();
		stateRequest.setState("Financial planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(stateRequest);

		// when(admRemunerationRequestValidator.validate(remunerationRequest)).thenReturn(null);
		when(adminService.addState(Mockito.any(State.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addState").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyState_Success() throws Exception {

		StateRequest stateRequest = new StateRequest();
		stateRequest.setStateId(2);
		stateRequest.setState("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(stateRequest);

		when(admStateRequestValidator.validate(stateRequest)).thenReturn(null);
		when(adminService.modifyState(Mockito.any(State.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyState").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyState_ErrorOccured() throws Exception {

		StateRequest stateRequest = new StateRequest();
		stateRequest.setStateId(2);
		stateRequest.setState("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(stateRequest);

		// when(admRemunerationRequestValidator.validate(remunerationRequest)).thenReturn(null);
		when(adminService.modifyState(Mockito.any(State.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyState").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeState_Success() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeState(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeState").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeState_ErrorOccured() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		// when(admRoleRequestValidator.validate(admRoleRequest)).thenReturn(null);
		when(adminService.removeState(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeState").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeProducts_ErrorOccured() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeProducts(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeProducts").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addArticleStatus_Success() throws Exception {

		ArticleStatusRequest articleStatusRequest = new ArticleStatusRequest();
		articleStatusRequest.setDesc("connected");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleStatusRequest);

		when(admArticleStatusRequestValidator.validate(articleStatusRequest)).thenReturn(null);
		when(adminService.addArticleStatus(Mockito.any(ArticleStatus.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addArticleStatus_ErrorOccured() throws Exception {

		ArticleStatusRequest articleStatusRequest = new ArticleStatusRequest();
		articleStatusRequest.setDesc("connected");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleStatusRequest);

		when(admArticleStatusRequestValidator.validate(articleStatusRequest)).thenReturn(null);
		when(adminService.addArticleStatus(Mockito.any(ArticleStatus.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyArticleStatus_Success() throws Exception {
		ArticleStatusRequest articleStatusRequest = new ArticleStatusRequest();
		articleStatusRequest.setId(3);
		articleStatusRequest.setDesc("Updated");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleStatusRequest);

		when(admArticleStatusRequestValidator.validate(articleStatusRequest)).thenReturn(null);
		when(adminService.modifyArticleStatus(Mockito.any(ArticleStatus.class))).thenReturn(3);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyArticleStatus_ErrorOccured() throws Exception {
		ArticleStatusRequest articleStatusRequest = new ArticleStatusRequest();
		articleStatusRequest.setId(3);
		articleStatusRequest.setDesc("Updated");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleStatusRequest);

		when(admArticleStatusRequestValidator.validate(articleStatusRequest)).thenReturn(null);
		when(adminService.modifyArticleStatus(Mockito.any(ArticleStatus.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeArticleStatus_Success() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeArticleStatus(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeArticleStatus_ErrorOccured() throws Exception {

		AdmIdRequest admIdRequest = new AdmIdRequest();
		admIdRequest.setId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admIdRequest);

		when(adminService.removeArticleStatus(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addCashFlowItemType_Success() throws Exception {

		AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest = new AdmCashFlowItemTypeRequest();
		admCashFlowItemTypeRequest.setCashFlowItemType("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admCashFlowItemTypeRequest);

		when(admCashFlowItemTypeRequestValidator.validate(admCashFlowItemTypeRequest)).thenReturn(null);
		when(adminService.addCashFlowItemType(Mockito.any(CashFlowItemType.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addCashFlowItemType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addCashFlowItemType_ErrorOccured() throws Exception {

		AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest = new AdmCashFlowItemTypeRequest();
		admCashFlowItemTypeRequest.setCashFlowItemType("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admCashFlowItemTypeRequest);

		when(admCashFlowItemTypeRequestValidator.validate(admCashFlowItemTypeRequest)).thenReturn(null);
		when(adminService.addCashFlowItemType(Mockito.any(CashFlowItemType.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addCashFlowItemType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyCashFlowItemType_Success() throws Exception {
		AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest = new AdmCashFlowItemTypeRequest();
		admCashFlowItemTypeRequest.setCashFlowItemTypeId(3);
		admCashFlowItemTypeRequest.setCashFlowItemType("Updated");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admCashFlowItemTypeRequest);

		when(admCashFlowItemTypeRequestValidator.validate(admCashFlowItemTypeRequest)).thenReturn(null);
		when(adminService.modifyCashFlowItemType(Mockito.any(CashFlowItemType.class))).thenReturn(3);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyCashFlowItemType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyCashFlowItemType_ErrorOccured() throws Exception {
		AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest = new AdmCashFlowItemTypeRequest();
		admCashFlowItemTypeRequest.setCashFlowItemTypeId(3);
		admCashFlowItemTypeRequest.setCashFlowItemType("Updated");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admCashFlowItemTypeRequest);

		when(admCashFlowItemTypeRequestValidator.validate(admCashFlowItemTypeRequest)).thenReturn(null);
		when(adminService.modifyCashFlowItemType(Mockito.any(CashFlowItemType.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyCashFlowItemType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeCashFlowItemType_Success() throws Exception {

		AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest = new AdmCashFlowItemTypeRequest();
		admCashFlowItemTypeRequest.setCashFlowItemTypeId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admCashFlowItemTypeRequest);

		when(adminService.removeCashFlowItemType(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeCashFlowItemType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeCashFlowItemType_ErrorOccured() throws Exception {

		AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest = new AdmCashFlowItemTypeRequest();
		admCashFlowItemTypeRequest.setCashFlowItemTypeId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(admCashFlowItemTypeRequest);

		when(adminService.removeCashFlowItemType(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeCashFlowItemType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addCashFlowItem_Success() throws Exception {

		CashFlowItemRequest cashFlowItemRequest = new CashFlowItemRequest();
		cashFlowItemRequest.setCashFlowItem("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowItemRequest);

		when(cashFlowItemRequestValidator.validate(cashFlowItemRequest)).thenReturn(null);
		when(adminService.addCashFlowItem(Mockito.any(CashFlowItem.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addCashFlowItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addCashFlowItem_ErrorOccured() throws Exception {

		CashFlowItemRequest cashFlowItemRequest = new CashFlowItemRequest();
		cashFlowItemRequest.setCashFlowItem("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowItemRequest);

		when(cashFlowItemRequestValidator.validate(cashFlowItemRequest)).thenReturn(null);
		when(adminService.addCashFlowItem(Mockito.any(CashFlowItem.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addCashFlowItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyCashFlowItem_Success() throws Exception {
		CashFlowItemRequest cashFlowItemRequest = new CashFlowItemRequest();
		cashFlowItemRequest.setCashFlowItemId(30);
		cashFlowItemRequest.setCashFlowItem("Updated");
		cashFlowItemRequest.setCashFlowItemTypeId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowItemRequest);

		when(cashFlowItemRequestValidator.validate(cashFlowItemRequest)).thenReturn(null);
		when(adminService.modifyCashFlowItem(Mockito.any(CashFlowItem.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyCashFlowItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyCashFlowItem_ErrorOccured() throws Exception {
		CashFlowItemRequest cashFlowItemRequest = new CashFlowItemRequest();
		cashFlowItemRequest.setCashFlowItemId(30);
		cashFlowItemRequest.setCashFlowItem("Updated");
		cashFlowItemRequest.setCashFlowItemTypeId(3);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowItemRequest);

		when(cashFlowItemRequestValidator.validate(cashFlowItemRequest)).thenReturn(null);
		when(adminService.modifyCashFlowItem(Mockito.any(CashFlowItem.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyCashFlowItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeCashFlowItem_Success() throws Exception {

		CashFlowItemRequest cashFlowItemRequest = new CashFlowItemRequest();
		cashFlowItemRequest.setCashFlowItemId(30);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowItemRequest);

		when(adminService.removeCashFlowItem(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeCashFlowItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeCashFlowItem_ErrorOccured() throws Exception {

		CashFlowItemRequest cashFlowItemRequest = new CashFlowItemRequest();
		cashFlowItemRequest.setCashFlowItemId(30);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowItemRequest);

		when(adminService.removeCashFlowItem(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeCashFlowItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addCity_Success() throws Exception {

		CityRequest cityRequest = new CityRequest();
		cityRequest.setStateId(4);
		cityRequest.setCity("madurai");
		cityRequest.setPincode("627001");
		cityRequest.setDistrict("madurai");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cityRequest);

		when(cityRequestValidator.validate(cityRequest)).thenReturn(null);
		when(adminService.addCity(Mockito.any(City.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addCity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addCity_ErrorOccured() throws Exception {

		CityRequest cityRequest = new CityRequest();
		cityRequest.setStateId(4);
		cityRequest.setCity("madurai");
		cityRequest.setPincode("627001");
		cityRequest.setDistrict("madurai");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cityRequest);

		when(cityRequestValidator.validate(cityRequest)).thenReturn(null);
		when(adminService.addCity(Mockito.any(City.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addCity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyCity_Success() throws Exception {
		CityRequest cityRequest = new CityRequest();
		cityRequest.setCityId(1);
		cityRequest.setStateId(4);
		cityRequest.setCity("madurai");
		cityRequest.setPincode("627001");
		cityRequest.setDistrict("madurai");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cityRequest);

		when(cityRequestValidator.validate(cityRequest)).thenReturn(null);
		when(adminService.modifyCity(Mockito.any(City.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyCity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyCity_ErrorOccured() throws Exception {
		CityRequest cityRequest = new CityRequest();
		cityRequest.setCityId(1);
		cityRequest.setStateId(4);
		cityRequest.setCity("madurai");
		cityRequest.setPincode("627001");
		cityRequest.setDistrict("madurai");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cityRequest);

		when(cityRequestValidator.validate(cityRequest)).thenReturn(null);
		when(adminService.modifyCity(Mockito.any(City.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyCity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeCity_Success() throws Exception {

		CityRequest cityRequest = new CityRequest();
		cityRequest.setCityId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cityRequest);

		when(adminService.removeCity(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeCity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeCity_ErrorOccured() throws Exception {

		CityRequest cityRequest = new CityRequest();
		cityRequest.setCityId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cityRequest);

		when(adminService.removeCity(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removeCity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}
			
	@Test
	public void test_addVotetype_Success() throws Exception {

		VotetypeRequest votetypeRequest = new VotetypeRequest();
		votetypeRequest.setDesc("Financial planning");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(votetypeRequest);

		when(votetypeRequestValidator.validate(votetypeRequest)).thenReturn(null);
		when(adminService.addVotetype(Mockito.any(Votetype.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addVotetype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription",
				is(appMessages.getAdmin_added_successfully())))
		.andReturn();
}
	
	@Test
	public void test_addInsuranceItem_Success() throws Exception {

		InsuranceItemRequest insuranceItemRequest = new InsuranceItemRequest();
		insuranceItemRequest.setInsuranceItem("house loan");
		insuranceItemRequest.setValue("annual rate");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceItemRequest);

		when(insuranceItemRequestValidator.validate(insuranceItemRequest)).thenReturn(null);
		when(adminService.addInsuranceItem(Mockito.any(InsuranceItem.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addInsuranceItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_addVotetype_ErrorOccured() throws Exception {

		VotetypeRequest votetypeRequest = new VotetypeRequest();
		votetypeRequest.setDesc("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(votetypeRequest);

		when(votetypeRequestValidator.validate(votetypeRequest)).thenReturn(null);
		when(adminService.addVotetype(Mockito.any(Votetype.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addVotetype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(500))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
		.andReturn();
}
	
	@Test
	public void test_addInsuranceItem_ErrorOccured() throws Exception {

		InsuranceItemRequest insuranceItemRequest = new InsuranceItemRequest();
		insuranceItemRequest.setInsuranceItem("house loan");
		insuranceItemRequest.setValue("annual rate");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceItemRequest);

		when(insuranceItemRequestValidator.validate(insuranceItemRequest)).thenReturn(null);
		when(adminService.addInsuranceItem(Mockito.any(InsuranceItem.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addInsuranceItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyVotetype_Success() throws Exception {

		VotetypeRequest votetypeRequest = new VotetypeRequest();
		votetypeRequest.setId(2);
		votetypeRequest.setDesc("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(votetypeRequest);

		when(votetypeRequestValidator.validate(votetypeRequest)).thenReturn(null);
		when(adminService.modifyVotetype(Mockito.any(Votetype.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyVotetype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription",
				is(appMessages.getAdmin_updated_successfully())))
		.andReturn();
}
	@Test						
	public void test_modifyInsuranceItem_Success() throws Exception {

		InsuranceItemRequest insuranceItemRequest = new InsuranceItemRequest();
		insuranceItemRequest.setInsuranceItemId(1);
		insuranceItemRequest.setInsuranceItem("house loan");
		insuranceItemRequest.setValue("annual rate");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceItemRequest);

		when(insuranceItemRequestValidator.validate(insuranceItemRequest)).thenReturn(null);
		when(adminService.modifyInsuranceItem(Mockito.any(InsuranceItem.class))).thenReturn(3);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInsuranceItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyVotetype_ErrorOccured() throws Exception {

		VotetypeRequest votetypeRequest = new VotetypeRequest();
		votetypeRequest.setId(2);
		votetypeRequest.setDesc("Risk profile");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(votetypeRequest);

		when(votetypeRequestValidator.validate(votetypeRequest)).thenReturn(null);
		when(adminService.modifyVotetype(Mockito.any(Votetype.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyRemuneration").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyInsuranceItem_Error() throws Exception {

		InsuranceItemRequest insuranceItemRequest = new InsuranceItemRequest();
		insuranceItemRequest.setInsuranceItemId(1);
		insuranceItemRequest.setInsuranceItem("house loan");
		insuranceItemRequest.setValue("annual rate");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceItemRequest);

	when(insuranceItemRequestValidator.validate(insuranceItemRequest)).thenReturn(null);
		when(adminService.modifyInsuranceItem(Mockito.any(InsuranceItem.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInsuranceItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeVotetype_Success() throws Exception {

		VotetypeRequest votetypeRequest = new VotetypeRequest();
		votetypeRequest.setId(2);


		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(votetypeRequest);

		when(adminService.removeVotetype(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeVotetype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription",
				is(appMessages.getAdmin_deleted_successfully())))
		.andReturn();
}
				
				
	public void test_removeInsuranceItem_Success() throws Exception {

		InsuranceItemRequest insuranceItemRequest = new InsuranceItemRequest();
		insuranceItemRequest.setInsuranceItemId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceItemRequest);

		when(adminService.removeInsuranceItem(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeInsuranceItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeVotetype_ErrorOccured() throws Exception {

		VotetypeRequest votetypeRequest = new VotetypeRequest();
		votetypeRequest.setId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(votetypeRequest);

		when(adminService.removeVotetype(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeVotetype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(500))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
		.andReturn();
}
				
				
	public void test_removeInsuranceItem_ErrorOccured() throws Exception {

		InsuranceItemRequest insuranceItemRequest = new InsuranceItemRequest();
		insuranceItemRequest.setInsuranceItemId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceItemRequest);

		when(adminService.removeInsuranceItem(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeInsuranceItem").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}
	
	@Test
	public void test_addBrand_Success() throws Exception {

		BrandRequest brandRequest = new BrandRequest();
	
		brandRequest.setBrand("Financial planning");
		brandRequest.setProdId(5);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(brandRequest);

		when(brandRequestValidator.validate(brandRequest)).thenReturn(null);
		when(adminService.addBrand(Mockito.any(Brand.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addBrand").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription",
				is(appMessages.getAdmin_added_successfully())))
		.andReturn();
}
				
				
	public void test_addUserType_Success() throws Exception {

		UserTypeRequest userTypeRequest = new UserTypeRequest();
		userTypeRequest.setDesc("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(userTypeRequest);

		when(userTypeRequestValidator.validate(userTypeRequest)).thenReturn(null);
		when(adminService.addUserType(Mockito.any(UserType.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addUserType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_added_successfully())))
				.andReturn();
	}
	
	@Test
	public void test_addBrand_ErrorOccured() throws Exception {

		BrandRequest brandRequest = new BrandRequest();
		
		brandRequest.setBrand("advisor");
		brandRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(brandRequest);

		when(brandRequestValidator.validate(brandRequest)).thenReturn(null);
		when(adminService.addBrand(Mockito.any(Brand.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addVotetype").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyBrand_Success() throws Exception {

		BrandRequest brandRequest = new BrandRequest();
		brandRequest.setBrandId(2);
		brandRequest.setBrand("Risk profile");
		brandRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(brandRequest);

		when(brandRequestValidator.validate(brandRequest)).thenReturn(null);
		when(adminService.modifyBrand(Mockito.any(Brand.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyBrand").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription",
				is(appMessages.getAdmin_updated_successfully())))
		.andReturn();
}
				
				
	public void test_addUserType_Error() throws Exception {

		UserTypeRequest userTypeRequest = new UserTypeRequest();
		userTypeRequest.setDesc("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(userTypeRequest);

		when(userTypeRequestValidator.validate(userTypeRequest)).thenReturn(null);
		when(adminService.addUserType(Mockito.any(UserType.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addUserType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	
	@Test
	public void test_modifyUserType_Success() throws Exception {

		UserTypeRequest userTypeRequest = new UserTypeRequest();
		userTypeRequest.setId(1);
		userTypeRequest.setDesc("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(userTypeRequest);

		when(userTypeRequestValidator.validate(userTypeRequest)).thenReturn(null);
		when(adminService.modifyUserType(Mockito.any(UserType.class))).thenReturn(3);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyBrand_ErrorOccured() throws Exception {

		BrandRequest brandRequest = new BrandRequest();
		brandRequest.setBrandId(2);
		brandRequest.setBrand("Risk profile");
		brandRequest.setProdId(5);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(brandRequest);

		when(brandRequestValidator.validate(brandRequest)).thenReturn(null);
		when(adminService.modifyBrand(Mockito.any(Brand.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyBrand").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeBrand_Success() throws Exception {

		BrandRequest brandRequest = new BrandRequest();
		brandRequest.setBrandId(2);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(brandRequest);

		when(adminService.removeBrand(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeBrand").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
		.andExpect(jsonPath("$.responseMessage.responseDescription",
				is(appMessages.getAdmin_deleted_successfully())))
		.andReturn();
}

				
				
	@Test
	public void test_modifyUserType_Error() throws Exception {

		UserTypeRequest userTypeRequest = new UserTypeRequest();
		userTypeRequest.setId(1);
		userTypeRequest.setDesc("house loan");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(userTypeRequest);

		when(userTypeRequestValidator.validate(userTypeRequest)).thenReturn(null);
		when(adminService.modifyUserType(Mockito.any(UserType.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyUserType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}
	
	@Test
	public void test_removeUserType_Success() throws Exception {

		UserTypeRequest userTypeRequest = new UserTypeRequest();
		userTypeRequest.setId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(userTypeRequest);

		when(adminService.removeUserType(Mockito.anyInt())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdmin_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeBrand_ErrorOccured() throws Exception {

		BrandRequest brandRequest = new BrandRequest();
		brandRequest.setBrandId(2);
		

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(brandRequest);

		when(adminService.removeBrand(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeBrand").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	
	
	@Test
	public void test_removeUserType_Error() throws Exception {

		UserTypeRequest userTypeRequest = new UserTypeRequest();
		userTypeRequest.setId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(userTypeRequest);

		when(adminService.removeUserType(Mockito.anyInt())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeUserType").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured())))
				.andReturn();
	}

}



