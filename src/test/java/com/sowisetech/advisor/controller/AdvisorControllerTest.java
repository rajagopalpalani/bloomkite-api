package com.sowisetech.advisor.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.collection.IsMapWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sowisetech.AdvisorApplication;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.model.AdvBrandInfo;
import com.sowisetech.advisor.model.AdvBrandRank;
import com.sowisetech.advisor.model.AdvProduct;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.AdvisorType;
import com.sowisetech.advisor.model.ArticleStatus;
import com.sowisetech.advisor.model.Award;
import com.sowisetech.advisor.model.Brand;
import com.sowisetech.advisor.model.Category;
import com.sowisetech.advisor.model.CategoryType;
import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.Dashboard;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.FollowerStatus;
import com.sowisetech.advisor.model.Followers;
import com.sowisetech.advisor.model.FollowersList;
import com.sowisetech.advisor.model.ForumCategory;
import com.sowisetech.advisor.model.ForumStatus;
import com.sowisetech.advisor.model.ForumSubCategory;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.KeyPeople;
import com.sowisetech.advisor.model.License;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.model.PartyStatus;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.Service;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.advisor.request.AdvBrandInfoReq;
import com.sowisetech.advisor.request.AdvBrandInfoRequest;
import com.sowisetech.advisor.request.AdvIdRequest;
import com.sowisetech.advisor.request.AdvPersonalInfoRequest;
import com.sowisetech.advisor.request.AdvPersonalInfoRequestValidator;
import com.sowisetech.advisor.request.AdvProductInfoRequest;
import com.sowisetech.advisor.request.AdvProductInfoRequestValidator;
import com.sowisetech.advisor.request.AdvProductRequest;
import com.sowisetech.advisor.request.AdvProfessionalInfoRequest;
import com.sowisetech.advisor.request.AdvProfessionalInfoRequestValidator;
import com.sowisetech.advisor.request.AdvisorRequest;
import com.sowisetech.advisor.request.AdvisorRequestValidator;
import com.sowisetech.advisor.request.AwardReq;
import com.sowisetech.advisor.request.CertificateReq;
import com.sowisetech.advisor.request.ChatRequest;
import com.sowisetech.advisor.request.EducationReq;
import com.sowisetech.advisor.request.ExperienceReq;
import com.sowisetech.advisor.request.FollowerRequest;
import com.sowisetech.advisor.request.ForgetPasswordRequest;
import com.sowisetech.advisor.request.ForgetPasswordRequestValidator;
import com.sowisetech.advisor.request.IdRequest;
import com.sowisetech.advisor.request.KeyPeopleRequest;
import com.sowisetech.advisor.request.KeyPeopleRequestValidator;
import com.sowisetech.advisor.request.ModifyAdvReqValidator;
import com.sowisetech.advisor.request.ModifyAdvRequest;
import com.sowisetech.advisor.request.OtpRequest;
import com.sowisetech.advisor.request.PasswordChangeRequest;
import com.sowisetech.advisor.request.PasswordValidator;
import com.sowisetech.advisor.request.PromotionReq;
import com.sowisetech.advisor.request.PromotionRequest;
import com.sowisetech.advisor.request.PromotionRequestValidator;
import com.sowisetech.advisor.request.ResendMailRequest;
import com.sowisetech.advisor.request.ResetPasswordRequest;
import com.sowisetech.advisor.request.ResetPasswordRequestValidator;
import com.sowisetech.advisor.request.StatusRequest;
import com.sowisetech.advisor.request.UniqueFieldRequest;
import com.sowisetech.advisor.response.PartyIdResponse;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.service.AmazonClient;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.MailConstants;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.common.util.SendMail;
import com.sowisetech.common.util.SendSms;
import com.sowisetech.common.util.SmsConstants;
import com.sowisetech.investor.model.Investor;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvisorApplication.class)
public class AdvisorControllerTest {

	@InjectMocks
	private AdvisorController advisorController;

	private MockMvc mockMvc;
	@Mock
	private AdvisorService advisorService;
	@Mock
	private AdvisorRequestValidator advisorRequestValidator;
	@Mock
	private ModifyAdvReqValidator modifyAdvReqValidator;
	@Mock
	private AdvPersonalInfoRequestValidator advPersonalInfoRequestValidator;
	@Mock
	private AdvProductInfoRequestValidator advProductInfoRequestValidator;
	@Mock
	private AdvProfessionalInfoRequestValidator advProfessionalInfoRequestValidator;
	@Mock
	private PasswordValidator passwordValidator;
	@Mock
	AmazonClient amazonClient;
	@Mock
	private KeyPeopleRequestValidator keyPeopleRequestValidator;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired(required = true)
	@Spy
	ScreenRightsConstants screenRightsConstants;
	@Mock
	ScreenRightsCommon screenRightsCommon;
	@Mock
	CommonService commonService;
	@Mock
	private PromotionRequestValidator promotionRequestValidator;
	@Mock
	private ForgetPasswordRequestValidator forgetPasswordRequestValidator;
	@Mock
	private ResetPasswordRequestValidator resetPasswordRequestValidator;
	@Autowired(required = true)
	@Spy
	AdvAppMessages appMessages;
	@Mock
	SendMail sendMail;
	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;
	@Autowired(required = true)
	@Spy
	MailConstants mailConstants;
	@Mock
	SendSms sendSms;
	@Autowired(required = true)
	@Spy
	SmsConstants smsConstants;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(advisorController).build();
	}

	@Test
	public void testEcv() throws Exception {
		this.mockMvc.perform(get("/ecv")).andExpect(status().isOk());
	}
	// =========================================== Get All Advisors
	// ==========================================

	@Test
	public void test_fetchAll_Success() throws Exception {
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv1 = new Advisor();
		adv1.setAdvId(("ADV000000000A"));
		adv1.setName("AAA");
		Advisor adv2 = new Advisor();
		adv2.setAdvId("ADV000000000B");
		adv2.setName("BBB");
		advisors.add(adv1);
		advisors.add(adv2);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		when(advisorService.fetchAdvisorTotalList()).thenReturn(2);
		when(advisorService.fetchAdvisorList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(advisors);

		mockMvc.perform(post("/fetch-all").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("AAA")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].advId", is("ADV000000000B")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].name", is("BBB")));
	}

	@Test
	public void test_fetchAll_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetch-all").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAll_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetch-all").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchAll_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv1 = new Advisor();
		adv1.setAdvId(("ADV000000000A"));
		adv1.setName("AAA");
		Advisor adv2 = new Advisor();
		adv2.setAdvId("ADV000000000B");
		adv2.setName("BBB");
		advisors.add(adv1);
		advisors.add(adv2);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(advisorService.fetchAdvisorTotalList()).thenReturn(2);
		when(advisorService.fetchAdvisorList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(advisors);
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
		mockMvc.perform(post("/fetch-all").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("AAA")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].advId", is("ADV000000000B")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].name", is("BBB")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	// =========================================== Get Advisor By ID
	// ==========================================

	// @Test
	// public void test_fetchAdvisorById_Success() throws Exception { //Error
	// AdvIdRequest advIdReq = new AdvIdRequest();
	// advIdReq.setAdvId("ADV000000000A");
	// advIdReq.setScreenId(1);
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("aaa");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdReq);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data.advId", is("ADV000000000A")))
	// .andExpect(jsonPath("$.responseData.data.name", is("aaa")));
	// }

	@Test
	public void test_fetchAdvisorById_Success() throws Exception { // Error
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("aaa");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		mockMvc.perform(
				MockMvcRequestBuilders.post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));
	}

	// @Test
	// public void test_fetchAdvisorByUserName_Success() throws Exception { //Error
	// AdvIdRequest advIdReq = new AdvIdRequest();
	// advIdReq.setUserName("advisor");
	// String userName_lc = advIdReq.getUserName().toLowerCase();
	//
	// advIdReq.setScreenId(1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setUserName("advisor");
	// adv.setName("aaa");
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdReq);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	//
	// when(advisorService.fetchAdvisorByUserName(Mockito.anyString())).thenReturn(adv);
	// mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvisorByUserName").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data.advId", is("ADV000000000A")))
	// .andExpect(jsonPath("$.responseData.data.userName", is("advisor")))
	// .andExpect(jsonPath("$.responseData.data.name", is("aaa")));
	// }

	@Test
	public void test_fetchAdvisorByUserName_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

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

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvisorByUserName").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_userName())));
	}

	@Test
	public void test_fetchAdvisorByUserName_ScreenRights() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();
		advIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAdvisorByUserName").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_fetchAdvisorByUserName_ScreenRights_Success() throws
	// Exception { //Error
	// AdvIdRequest advIdRequest = new AdvIdRequest();
	// advIdRequest.setScreenId(1);
	// advIdRequest.setUserName("advisor");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdRequest);
	// AdvIdRequest advIdReq = new AdvIdRequest();
	// advIdReq.setUserName("advisor");
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setUserName("advisor");
	// adv.setName("aaa");
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(advisorService.fetchAdvisorByUserName(Mockito.anyString())).thenReturn(adv);
	//
	// mockMvc.perform(post("/fetchAdvisorByUserName").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data.advId", is("ADV000000000A")))
	// .andExpect(jsonPath("$.responseData.data.userName", is("advisor")))
	// .andExpect(jsonPath("$.responseData.data.name", is("aaa")))
	// .andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	// }

	@Test
	public void test_fetchAdvisorByUserName_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
		mockMvc.perform(post("/fetchAdvisorByUserName").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetch_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();
		advIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_fetch_ScreenRights_Success() throws Exception { //Error
	// AdvIdRequest advIdRequest = new AdvIdRequest();
	// advIdRequest.setScreenId(1);
	// advIdRequest.setAdvId("ADV000000000A");
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setUserName("Adv Username");
	// adv.setName("aaa");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// mockMvc.perform(post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data.advId", is("ADV000000000A")))
	// .andExpect(jsonPath("$.responseData.data.userName", is("Adv Username")))
	// .andExpect(jsonPath("$.responseData.data.name", is("aaa")))
	// .andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	// }

	@Test
	public void test_fetch_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
		mockMvc.perform(post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchTeamByParentPartyId_Success() throws Exception {
		IdRequest idRequest = new IdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		advisors.add(adv);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
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
		when(advisorService.fetchTeamByParentPartyId(Mockito.anyLong())).thenReturn(advisors);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/fetchTeam").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].advId", is("ADV000000000A")));
	}

	@Test
	public void test_fetchTeamByParentPartyId_Mandatory() throws Exception {
		IdRequest idRequest = new IdRequest();
		idRequest.setScreenId(1);
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		advisors.add(adv);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
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
		mockMvc.perform(
				MockMvcRequestBuilders.post("/fetchTeam").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_parentPartyId())));
	}

	@Test
	public void test_fetchTeam_ScreenRights_Success() throws Exception {
		IdRequest idRequest = new IdRequest();
		idRequest.setScreenId(1);
		idRequest.setId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		advisors.add(adv);
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
		when(advisorService.fetchTeamByParentPartyId(Mockito.anyLong())).thenReturn(advisors);

		mockMvc.perform(post("/fetchTeam").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchTeam_ScreenRights_AccessDenied() throws Exception {
		IdRequest idRequest = new IdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchTeam").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchTeam_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();
		advIdRequest.setScreenId(0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
		mockMvc.perform(post("/fetchTeam").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_fetchAdvisorById_NotFound() throws Exception {
	// AdvIdRequest advIdReq = new AdvIdRequest();
	// advIdReq.setAdvId("ADV000000000A");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdReq);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	//
	// }

	// =========================================== Delete Advisor By ID
	// ==========================================

	@Test
	public void test_removeAdvisor_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000B");
		advIdReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000B");
		adv.setName("aaa");
		when(advisorService.checkAdvisorIsPresent(adv.getAdvId())).thenReturn(1);
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.removeAdvisor(adv.getAdvId(), 9)).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		mockMvc.perform(post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeAdvisor_Mandatory() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		mockMvc.perform(post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_remove_ScreenRights_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		advIdReq.setAdvId("ADV000000000B");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000B");
		adv.setName("aaa");
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
		when(advisorService.checkAdvisorIsPresent(adv.getAdvId())).thenReturn(1);
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.removeAdvisor(adv.getAdvId(), 9)).thenReturn(1);

		mockMvc.perform(post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_remove_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_remove_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
		mockMvc.perform(post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removeAdvisor_NotFound() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000B");
		advIdReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000B");
		adv.setName("aaa");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		when(advisorService.checkAdvisorIsPresent(adv.getAdvId())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_removeAdvisor_ErrorOccured() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000B");
		advIdReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000B");
		adv.setName("aaa");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		when(advisorService.checkAdvisorIsPresent(adv.getAdvId())).thenReturn(1);
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.removeAdvisor(adv.getAdvId(), 9)).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/remove").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	// ================== investor signup Test ==================== //
	@Test
	public void test_Invsignup_Success() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setUserName("Inv User");
		advReq.setDob("07-02-2000");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		long roleId = advReq.getRoleId();
		long partyId = 12;
		PartyIdResponse idResponse = new PartyIdResponse();
		idResponse.setPartyId(partyId);

		List<String> toUsers = new ArrayList<>();
		toUsers.add("inv@aaa.com");
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setEmailId("inv@aaa.com");
		inv.setPassword("!@AS12as");

		int result = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(roleId);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(advReq.getUserName())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(advReq.getPhoneNumber())).thenReturn(null);
		when(advisorService.generateIdInv()).thenReturn("INV000000000A");
		when(advisorService.addInvestor(Mockito.any(Investor.class), Mockito.anyLong())).thenReturn(result);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		when(advisorService.fetchPartyIdByRoleBasedId("INV000000000A")).thenReturn(partyId);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.partyId", is(12))).andReturn();
	}

	@Test
	public void test_Invsignup_FieldsCannotBeEmpty() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_signup())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_AlreadyPresent() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setUserName("adv123");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		Party party = new Party();
		party.setEmailId("inv@aaa.com");
		party.setRoleBasedId("INV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int verified = advTableFields.getAccount_verified();

		Investor inv = new Investor();
		inv.setIsVerified(verified);
		inv.setInvId("INV0000000001");

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(party);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdvisor_already_present())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_ValidationError() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("123");
		advReq.setDisplayName("123");
		advReq.setGender("Male");
		advReq.setDob("May-2000");
		advReq.setEmailId("invaaa.com");
		advReq.setPhoneNumber("9874563210as");
		advReq.setUserName("inv123");
		advReq.setPassword("AS12as");
		advReq.setPincode("123456as");
		advReq.setRoleId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(Mockito.any(AdvisorRequest.class))).thenReturn(allErrors);
		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", is(allErrors))).andReturn();

	}

	@Test
	public void test_Invsignup_AlreadyPresent_NotVerified() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setUserName("adv123");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		Party party = new Party();
		party.setEmailId("inv@aaa.com");
		party.setRoleBasedId("INV0000000001");

		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int not_Verified = advTableFields.getAccount_not_verified();

		Investor inv = new Investor();
		inv.setIsVerified(not_Verified);
		inv.setInvId("INV0000000001");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(party);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_not_verified())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_UserName_AlreadyPresent() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setUserName("adv123");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		Party party = new Party();
		party.setUserName("inv123");
		party.setRoleBasedId("INV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int verified = advTableFields.getAccount_verified();

		Investor inv = new Investor();
		inv.setIsVerified(verified);
		inv.setInvId("INV0000000001");

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getUser_already_present_username())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_UserName_AlreadyPresent_NotVerified() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setUserName("adv123");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		Party party = new Party();
		party.setUserName("inv123");
		party.setRoleBasedId("INV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int not_Verified = advTableFields.getAccount_not_verified();

		Investor inv = new Investor();
		inv.setIsVerified(not_Verified);
		inv.setInvId("INV0000000001");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getUser_already_present_username_not_verified())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_PhoneNumber_AlreadyPresent() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setUserName("adv123");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		Party party = new Party();
		party.setPhoneNumber("9994851310");
		party.setRoleBasedId("INV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int verified = advTableFields.getAccount_verified();

		Investor inv = new Investor();
		inv.setIsVerified(verified);
		inv.setInvId("INV0000000001");

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_phone())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_PhoneNumber_AlreadyPresent_NotVerified() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setUserName("adv123");
		advReq.setEmailId("inv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		Party party = new Party();
		party.setPhoneNumber("9994851310");
		party.setRoleBasedId("INV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int not_Verified = advTableFields.getAccount_not_verified();

		Investor inv = new Investor();
		inv.setIsVerified(not_Verified);
		inv.setInvId("INV0000000001");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_phone_not_verified())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_GenerateIdError() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setEmailId("inv@aaa.com");
		advReq.setUserName("inv123");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(advReq.getUserName())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(advReq.getPhoneNumber())).thenReturn(null);
		when(advisorService.generateIdInv()).thenReturn(null);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_Invsignup_ErrorOccured() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setFullName("investor");
		advReq.setDisplayName("inv");
		advReq.setGender("m");
		advReq.setDob("07-02-2000");
		advReq.setEmailId("inv@aaa.com");
		advReq.setUserName("inv123");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setPincode("123456");
		advReq.setRoleId(2);

		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchAdvisorByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.generateIdInv()).thenReturn("INV000000000A");
		when(advisorService.addInvestor(Mockito.any(Investor.class), Mockito.anyLong())).thenReturn(result);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// ====================== Advisor signup Test ==================== //
	@Test
	public void test_Advsignup_Success() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setPanNumber("abcde1234f");
		advReq.setEmailId("adv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@QW12as");
		advReq.setUserName("advisor");
		advReq.setRoleId(1);

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setEmailId("adv@aaa.com");
		adv.setPassword("!@QW12as");
		adv.setAdvType(1);
		adv.setUserName("advisor");
		adv.setPanNumber("abcde1234f");

		int result = 1;
		long roleId = advReq.getRoleId();
		long partyId = 11;
		int advType = 1;
		PartyIdResponse idResponse = new PartyIdResponse();
		idResponse.setPartyId(partyId);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(advReq.getUserName())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(advReq.getPanNumber())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(advReq.getPhoneNumber())).thenReturn(null);

		when(advisorService.fetchTypeIdByIndividualAdvtype()).thenReturn(advType);
		when(advisorService.generateId()).thenReturn("ADV000000000A");
		when(advisorService.fetchPartyByPartyId(advReq.getParentPartyId())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(party.getRoleBasedId())).thenReturn(adv);
		when(advisorService.advSignup(Mockito.any(Advisor.class), Mockito.anyLong())).thenReturn(result);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		when(advisorService.fetchPartyIdByRoleBasedId(Mockito.anyString())).thenReturn((partyId));
		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.partyId", is(11))).andReturn();
	}

	@Test
	public void test_Advsignup_FieldsCannotBeEmpty() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@AS12as");
		advReq.setRoleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_advSignup())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_AlreadyPresent() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("aaa@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setUserName("advisor");
		advReq.setPassword("!@AS12as");
		advReq.setRoleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		Party party = new Party();
		party.setEmailId("aaa@aaa.com");
		party.setRoleBasedId("ADV0000000001");

		int verified = advTableFields.getAccount_verified();

		Advisor adv = new Advisor();
		adv.setIsVerified(verified);
		adv.setAdvId("ADV0000000001");

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdvisor_already_present())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_AlreadyPresent_NotVerified() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("aaa@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setUserName("adv user");
		advReq.setPassword("!@AS12as");
		advReq.setRoleId(1);

		Party party = new Party();
		party.setEmailId("aaa@aaa.com");
		party.setRoleBasedId("ADV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int not_Verified = advTableFields.getAccount_not_verified();

		Advisor adv = new Advisor();
		adv.setIsVerified(not_Verified);
		adv.setAdvId("ADV0000000001");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_not_verified())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_PAN_AlreadyPresent() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("aaa@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setUserName("adv user");
		advReq.setPassword("!@AS12as");
		advReq.setRoleId(1);
		advReq.setPanNumber("abcde1234f");

		Party party = new Party();
		party.setPanNumber("abcde1234f");
		party.setRoleBasedId("ADV0000000001");
		int Verified = advTableFields.getAccount_verified();

		Advisor adv = new Advisor();
		adv.setIsVerified(Verified);
		adv.setAdvId("ADV0000000001");
		adv.setPanNumber("abcde1234f");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_pan())))
				.andReturn();
	}

	// @Test
	// public void test_Advsignup_PAN_AlreadyPresent_NotVerified() throws Exception
	// {
	// AdvisorRequest advReq = new AdvisorRequest();
	// advReq.setName("name");
	// advReq.setEmailId("aaa@aaa.com");
	// advReq.setPhoneNumber("9874563210");
	// advReq.setUserName("adv user");
	// advReq.setPassword("!@AS12as");
	// advReq.setRoleId(1);
	// advReq.setPanNumber("mypan1234e");
	//
	// Party party = new Party();
	// party.setPanNumber("abcde1234f");
	// party.setRoleBasedId("ADV0000000001");
	//
	// int not_Verified = advTableFields.getAccount_not_verified();
	//
	// Advisor adv = new Advisor();
	// adv.setIsVerified(not_Verified);
	// adv.setAdvId("ADV0000000001");
	// adv.setPanNumber("abcde1234f");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advReq);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
	// when(advisorRequestValidator.validate(advReq)).thenReturn(null);
	// when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
	// when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(null);
	// when(advisorService.fetchPartyByPAN(Mockito.anyString())).thenReturn(party);
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	//
	// mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_already_present_pan_not_verified())))
	// .andReturn();
	// }
	//
	@Test
	public void test_Advsignup_PhoneNumber_AlreadyPresent() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("aaa@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setUserName("adv user");
		advReq.setPassword("!@AS12as");
		advReq.setRoleId(1);
		advReq.setPanNumber("abcde1234f");

		Party party = new Party();
		party.setPhoneNumber("9874563210");
		party.setRoleBasedId("ADV0000000001");

		int Verified = advTableFields.getAccount_verified();

		Advisor adv = new Advisor();
		adv.setIsVerified(Verified);
		adv.setAdvId("ADV0000000001");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_phone())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_PhoneNumber_AlreadyPresent_NotVerified() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("aaa@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setUserName("adv user");
		advReq.setPassword("!@AS12as");
		advReq.setRoleId(1);
		advReq.setPanNumber("abcde1234f");

		Party party = new Party();
		party.setPhoneNumber("9874563210");
		party.setRoleBasedId("ADV0000000001");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		int not_Verified = advTableFields.getAccount_not_verified();

		Advisor adv = new Advisor();
		adv.setIsVerified(not_Verified);
		adv.setAdvId("ADV0000000001");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(Mockito.anyString())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(Mockito.anyString())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_already_present_phone_not_verified())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_GenerateIdError() throws Exception {
		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setUserName("adv user");
		advReq.setEmailId("adv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@QW12as");
		advReq.setUserName("Adv User");
		advReq.setPanNumber("abcde1234f");
		advReq.setRoleId(1);

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setEmailId("adv@aaa.com");
		adv.setPassword("!@QW12as");
		adv.setAdvType(1);
		adv.setPanNumber("abcde1234f");
		adv.setCorporateLable("lable");

		long roleId = advReq.getRoleId();
		long partyId = 11;
		int advType = 1;
		PartyIdResponse idResponse = new PartyIdResponse();
		idResponse.setPartyId(partyId);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(advReq.getUserName())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(advReq.getPanNumber())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(advReq.getPhoneNumber())).thenReturn(null);
		when(advisorService.fetchTypeIdByIndividualAdvtype()).thenReturn(advType);
		when(advisorService.generateId()).thenReturn(null);

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_ValidationError() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("123");
		advReq.setEmailId("aaaaaa.com");
		advReq.setUserName("adv user");
		advReq.setPhoneNumber("9874563210as");
		advReq.setPassword("AS12as");
		advReq.setRoleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(Mockito.any(AdvisorRequest.class))).thenReturn(allErrors);
		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", is(allErrors))).andReturn();

	}

	// Invalid RoleId //
	@Test
	public void test_RoleIdFailure() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("NonAdv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@QW12as");
		advReq.setRoleId(4);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getRoleId_not_found())))
				.andReturn();
	}

	@Test
	public void test_Advsignup_ErrorOccured() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		// advReq.setUserName("adv user");
		advReq.setEmailId("adv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@QW12as");
		advReq.setUserName("advisor");
		advReq.setRoleId(1);
		advReq.setPanNumber("abcde1234f");

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setEmailId("adv@aaa.com");
		adv.setPassword("!@QW12as");
		adv.setAdvType(1);
		adv.setUserName("advisor");
		adv.setPanNumber("abcde1234f");

		int result = 0;
		long roleId = advReq.getRoleId();
		long partyId = 11;
		int advType = 1;
		PartyIdResponse idResponse = new PartyIdResponse();
		idResponse.setPartyId(partyId);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(advReq.getUserName())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(advReq.getPanNumber())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(advReq.getPhoneNumber())).thenReturn(null);

		when(advisorService.fetchTypeIdByIndividualAdvtype()).thenReturn(advType);
		when(advisorService.generateId()).thenReturn("ADV000000000A");
		when(advisorService.fetchPartyByPartyId(advReq.getParentPartyId())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(party.getRoleBasedId())).thenReturn(adv);
		when(advisorService.advSignup(Mockito.any(Advisor.class), Mockito.anyLong())).thenReturn(result);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// ====================== NonIndividual Advisor signup Test =============//
	@Test
	public void test_NonAdvsignup_Success() throws Exception {

		AdvisorRequest advReq = new AdvisorRequest();
		advReq.setName("name");
		advReq.setEmailId("adv@aaa.com");
		advReq.setPhoneNumber("9874563210");
		advReq.setPassword("!@QW12as");
		advReq.setUserName("advisor");
		advReq.setRoleId(3);
		advReq.setPanNumber("abcde1234f");

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setEmailId("adv@aaa.com");
		adv.setPassword("!@QW12as");
		adv.setAdvType(2);
		adv.setCorporateLable("lable");
		adv.setUserName("advisor");
		adv.setPanNumber("abcde1234f");

		int result = 1;
		long roleId = advReq.getRoleId();
		long partyId = 11;
		int advType = 2;
		PartyIdResponse idResponse = new PartyIdResponse();
		idResponse.setPartyId(partyId);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advReq);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(roleId);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorRequestValidator.validate(advReq)).thenReturn(null);
		when(advisorService.fetchPartyByEmailId(advReq.getEmailId())).thenReturn(null);
		when(advisorService.fetchPartyByUserName(advReq.getUserName())).thenReturn(null);
		when(advisorService.fetchPartyByPAN(advReq.getPanNumber())).thenReturn(null);
		when(advisorService.fetchPartyByPhoneNumber(advReq.getPhoneNumber())).thenReturn(null);
		when(advisorService.fetchTypeIdByCorporateAdvtype()).thenReturn(advType);
		when(advisorService.generateId()).thenReturn("ADV000000000A");
		when(advisorService.fetchPartyByPartyId(advReq.getParentPartyId())).thenReturn(party);
		when(advisorService.fetchByAdvisorId(party.getRoleBasedId())).thenReturn(adv);
		when(advisorService.advSignup(Mockito.any(Advisor.class), Mockito.anyLong())).thenReturn(result);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		when(advisorService.fetchPartyIdByRoleBasedId(Mockito.anyString())).thenReturn((partyId));
		mockMvc.perform(post("/signup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.partyId", is(11))).andReturn();
	}

	@Test
	public void test_modifyAdvisor_Success() throws Exception {

		ModifyAdvRequest modAdvReq = new ModifyAdvRequest();
		modAdvReq.setAdvId("ADV000000000A");
		modAdvReq.setName("advisor");
		modAdvReq.setDisplayName("advisor!");
		modAdvReq.setScreenId(1);
		modAdvReq.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modAdvReq);
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

		when(modifyAdvReqValidator.validate(modAdvReq)).thenReturn(null);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyAdvisor(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyAdvisor_Mandatory() throws Exception {

		ModifyAdvRequest modAdvReq = new ModifyAdvRequest();
		modAdvReq.setName("advisor");
		modAdvReq.setDisplayName("advisor!");
		modAdvReq.setScreenId(1);
		modAdvReq.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modAdvReq);
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

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_modify_ScreenRights_Success() throws Exception {
		ModifyAdvRequest modAdvReq = new ModifyAdvRequest();
		modAdvReq.setScreenId(1);
		modAdvReq.setAdvId("ADV000000000A");
		modAdvReq.setName("advisor");
		modAdvReq.setUserName("adv");
		modAdvReq.setDisplayName("advisor!");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modAdvReq);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		when(modifyAdvReqValidator.validate(modAdvReq)).thenReturn(null);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyAdvisor(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(1);
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
		mockMvc.perform(put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_modify_ScreenRights_AccessDenied() throws Exception {
		ModifyAdvRequest modifyAdvRequest = new ModifyAdvRequest();
		modifyAdvRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modifyAdvRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(null);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modify_ScreenRights_UnAuthorized() throws Exception {
		ModifyAdvRequest modifyAdvRequest = new ModifyAdvRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modifyAdvRequest);
		mockMvc.perform(put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyAdvisor_NotFound() throws Exception {
		ModifyAdvRequest modAdvReq = new ModifyAdvRequest();
		modAdvReq.setAdvId("ADV000000000A");
		modAdvReq.setName("advisor");
		modAdvReq.setDisplayName("advisor!");
		modAdvReq.setScreenId(1);
		modAdvReq.setUserName("adv");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

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
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modAdvReq);

		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_modifyAdvisor_ErrorOccured() throws Exception {
		ModifyAdvRequest modAdvReq = new ModifyAdvRequest();
		modAdvReq.setAdvId("ADV000000000A");
		modAdvReq.setName("advisor");
		modAdvReq.setDisplayName("advisor!");
		modAdvReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		modAdvReq.setUserName("adv");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modAdvReq);
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

		when(modifyAdvReqValidator.validate(modAdvReq)).thenReturn(null);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyAdvisor(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyAdvisor_ValidationError() throws Exception {

		ModifyAdvRequest modAdvReq = new ModifyAdvRequest();
		modAdvReq.setAdvId("ADV000000000A");
		modAdvReq.setName("advisor");
		modAdvReq.setDisplayName("advisor!");
		modAdvReq.setScreenId(1);
		modAdvReq.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modAdvReq);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(modifyAdvReqValidator.validate(Mockito.any(ModifyAdvRequest.class))).thenReturn(allErrors);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modify").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_AddPersonalInfo_Success() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoReq = new AdvPersonalInfoRequest();
		advPersonalInfoReq.setAdvId("ADV000000000A");
		advPersonalInfoReq.setDisplayName("display_name");
		advPersonalInfoReq.setScreenId(1);
		advPersonalInfoReq.setUserName("adv");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoReq);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advPersonalInfoRequestValidator.validate(advPersonalInfoReq)).thenReturn(null);
		when(advisorService.addAdvPersonalInfo(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(1);

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
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvPersonalInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_info_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_AddPersonalInfo_Mandatory() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoReq = new AdvPersonalInfoRequest();
		advPersonalInfoReq.setDisplayName("display_name");
		advPersonalInfoReq.setScreenId(1);
		advPersonalInfoReq.setUserName("adv");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoReq);

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
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvPersonalInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_addPersonalInfo_ScreenRights_Success() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoRequest = new AdvPersonalInfoRequest();
		advPersonalInfoRequest.setScreenId(1);
		advPersonalInfoRequest.setAdvId("ADV000000000A");
		advPersonalInfoRequest.setDisplayName("display_name");
		advPersonalInfoRequest.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advPersonalInfoRequestValidator.validate(advPersonalInfoRequest)).thenReturn(null);
		when(advisorService.addAdvPersonalInfo(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(1);
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
		mockMvc.perform(post("/addAdvPersonalInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_info_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_addPersonalInfo_ScreenRights_AccessDenied() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoRequest = new AdvPersonalInfoRequest();
		advPersonalInfoRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(null);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/addAdvPersonalInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addPersonalInfo_ScreenRights_UnAuthorized() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoRequest = new AdvPersonalInfoRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoRequest);
		mockMvc.perform(post("/addAdvPersonalInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_AddPersonalInfo_NotFound() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoReq = new AdvPersonalInfoRequest();
		advPersonalInfoReq.setAdvId("ADV000000000A");
		advPersonalInfoReq.setDisplayName("display_name");
		advPersonalInfoReq.setScreenId(1);
		advPersonalInfoReq.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoReq);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
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
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvPersonalInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_AddPersonalInfo_ErrorOccured() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoReq = new AdvPersonalInfoRequest();
		advPersonalInfoReq.setAdvId("ADV000000000A");
		advPersonalInfoReq.setDisplayName("display_name");
		advPersonalInfoReq.setScreenId(1);
		advPersonalInfoReq.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoReq);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advPersonalInfoRequestValidator.validate(advPersonalInfoReq)).thenReturn(null);
		when(advisorService.addAdvPersonalInfo(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvPersonalInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_AddPersonalInfo_ValidationError() throws Exception {
		AdvPersonalInfoRequest advPersonalInfoReq = new AdvPersonalInfoRequest();
		advPersonalInfoReq.setAdvId("ADV000000000A");
		advPersonalInfoReq.setDisplayName("display_name");
		advPersonalInfoReq.setScreenId(1);
		advPersonalInfoReq.setUserName("adv");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advPersonalInfoReq);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advPersonalInfoRequestValidator.validate(Mockito.any(AdvPersonalInfoRequest.class))).thenReturn(allErrors);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvPersonalInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_AddProductInfo_Success() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setAdvId("ADV000000000A");
		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest = new AdvProductRequest();
		productRequest.setProdId(1);
		productRequest.setServiceId("1");
		productRequestList.add(productRequest);
		advProductInfoRequest.setAdvProducts(productRequestList);
		advProductInfoRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setServiceId("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
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

		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(advProductInfoRequest)).thenReturn(null);
		when(advisorService.addAdvProductInfoList(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString());
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_info_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_AddProductInfo_Mandatory() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest = new AdvProductRequest();
		productRequest.setProdId(1);
		productRequest.setServiceId("1");
		productRequestList.add(productRequest);
		advProductInfoRequest.setAdvProducts(productRequestList);
		advProductInfoRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setServiceId("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
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

		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_addAdvProdInfo_ScreenRights_Success() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setAdvId("ADV000000000A");
		advProductInfoRequest.setScreenId(1);
		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest = new AdvProductRequest();
		productRequest.setProdId(1);
		productRequest.setServiceId("1");
		productRequestList.add(productRequest);
		advProductInfoRequest.setAdvProducts(productRequestList);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setServiceId("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
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
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(advProductInfoRequest)).thenReturn(null);
		when(advisorService.addAdvProductInfoList(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString());
		mockMvc.perform(post("/addAdvProdInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_info_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_addAdvProdInfo_ScreenRights_AccessDenied() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(null);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/addAdvProdInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addAdvProdInfo_ScreenRights_UnAuthorized() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
		mockMvc.perform(post("/addAdvProdInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_AddProductInfo_NotFound() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setAdvId("ADV000000000A");
		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest = new AdvProductRequest();
		productRequest.setProdId(1);
		productRequest.setServiceId("1");
		productRequestList.add(productRequest);
		advProductInfoRequest.setAdvProducts(productRequestList);
		advProductInfoRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setServiceId("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
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
		when(advProductInfoRequestValidator.validate(advProductInfoRequest)).thenReturn(null);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_AddProductInfo_ErrorOccured() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setAdvId("ADV000000000A");
		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest = new AdvProductRequest();
		productRequest.setProdId(1);
		productRequest.setServiceId("1");
		productRequestList.add(productRequest);
		advProductInfoRequest.setAdvProducts(productRequestList);
		advProductInfoRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setServiceId("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
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

		when(advProductInfoRequestValidator.validate(advProductInfoRequest)).thenReturn(null);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advisorService.addAdvProductInfoList(Mockito.anyString(), Mockito.anyList())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_AddProductInfo_ValidationError() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setAdvId("ADV000000000A");
		advProductInfoRequest.setScreenId(1);

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest = new AdvProductRequest();
		productRequest.setProdId(1);
		productRequest.setServiceId("1");
		productRequestList.add(productRequest);
		advProductInfoRequest.setAdvProducts(productRequestList);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setServiceId("1");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);
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
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(Mockito.any(AdvProductInfoRequest.class))).thenReturn(allErrors);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_ModifyProductInfo_Success() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvId("ADV0000000001");
		advProductInfoReq.setAdvProducts(productRequestList);
		advProductInfoReq.setScreenId(1);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);
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

		when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(advProductInfoReq)).thenReturn(null);
		// remove
		when(advisorService.fetchAdvProductByAdvId("ADV0000000001")).thenReturn(advProducts);
		when(advisorService.removeAdvProduct(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfo(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeFromBrandRank(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		when(advisorService.checkAdvProductIsPresent(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		//
		// when(advisorService.modifyAdvisorProduct(Mockito.any(AdvProduct.class),
		// Mockito.anyString())).thenReturn(1);
		when(advisorService.addAndModifyProductInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString());
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_ModifyProductInfo_Mandatory() throws Exception {
		Advisor adv = new Advisor();

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvProducts(productRequestList);
		advProductInfoReq.setScreenId(1);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);
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

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_ModifyProductInfo_ValidationError() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvId("ADV0000000001");
		advProductInfoReq.setAdvProducts(productRequestList);
		advProductInfoReq.setScreenId(1);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);
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

		when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(Mockito.any(AdvProductInfoRequest.class))).thenReturn(allErrors);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_modifyAdvProdInfo_ScreenRights_Success() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvId("ADV0000000001");
		advProductInfoReq.setScreenId(1);
		advProductInfoReq.setAdvProducts(productRequestList);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);

		when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(advProductInfoReq)).thenReturn(null);
		when(advisorService.fetchAdvProductByAdvId("ADV0000000001")).thenReturn(advProducts);
		when(advisorService.removeAdvProduct(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfo(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeFromBrandRank(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		when(advisorService.checkAdvProductIsPresent(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		// when(advisorService.modifyAdvisorProduct(Mockito.any(AdvProduct.class),
		// Mockito.anyString())).thenReturn(1);
		when(advisorService.addAndModifyProductInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString());
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
		mockMvc.perform(put("/modifyAdvProdInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_modifyAdvProdInfo_ScreenRights_AccessDenied() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();
		advProductInfoRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(null);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(put("/modifyAdvProdInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyAdvProdInfo_ScreenRights_UnAuthorized() throws Exception {
		AdvProductInfoRequest advProductInfoRequest = new AdvProductInfoRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoRequest);
		mockMvc.perform(put("/modifyAdvProdInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_ModifyProductInfo_NotFound() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvId("ADV0000000001");
		advProductInfoReq.setAdvProducts(productRequestList);
		advProductInfoReq.setScreenId(1);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
				.thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);

		when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(null);
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

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	// @Test //condition moved to serviceImpl
	// public void test_ModifyProductInfo_RemoveError() throws Exception {
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV0000000001");
	//
	// List<AdvProductRequest> productRequestList = new
	// ArrayList<AdvProductRequest>();
	// AdvProductRequest productRequest1 = new AdvProductRequest();
	// productRequest1.setAdvProdId(1);
	// productRequest1.setProdId(1);
	// productRequest1.setServiceId("2");
	// productRequest1.setValidity("14-02-2012");
	//
	// AdvProductRequest productRequest2 = new AdvProductRequest();
	// productRequest2.setProdId(3);
	// productRequest2.setServiceId("2");
	// productRequest2.setValidity("14-02-2012");
	//
	// productRequestList.add(productRequest1);
	// productRequestList.add(productRequest2);
	//
	// AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
	// advProductInfoReq.setAdvId("ADV0000000001");
	// advProductInfoReq.setAdvProducts(productRequestList);
	// advProductInfoReq.setScreenId(1);
	//
	// List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
	// AdvProduct product1 = new AdvProduct();
	// product1.setAdvId("ADV0000000001");
	// product1.setAdvProdId(1);
	// product1.setProdId(1);
	// AdvProduct product2 = new AdvProduct();
	// product2.setAdvId("ADV0000000001");
	// product2.setAdvProdId(2);
	// product2.setProdId(2);
	// advProducts.add(product1);
	// advProducts.add(product2);
	//
	// when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(),
	// Mockito.anyInt()))
	// .thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProductInfoReq);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(adv);
	// when(advProductInfoRequestValidator.validate(advProductInfoReq)).thenReturn(null);
	// // remove
	// when(advisorService.fetchAdvProductByAdvId("ADV0000000001")).thenReturn(advProducts);
	// when(advisorService.removeAdvProduct(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(0);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyProductInfo_AdvProdNotFound() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvId("ADV0000000001");
		advProductInfoReq.setAdvProducts(productRequestList);
		advProductInfoReq.setScreenId(1);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		List<Long> advBrandInfoListPriority = new ArrayList<>();
		when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
				.thenReturn(null);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);

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

		when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(advProductInfoReq)).thenReturn(null);
		// remove
		when(advisorService.fetchAdvProductByAdvId("ADV0000000001")).thenReturn(advProducts);
		when(advisorService.removeAdvProduct(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfo(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeFromBrandRank(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		when(advisorService.fetchAdvBrandInfoByAdvIdAndProdId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(advBrandInfoList);
		when(advisorService.fetchPriorityByBrandIdAndAdvId(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(advBrandInfoListPriority);
		when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
				.thenReturn(null);
		when(advisorService.addAdvBrandAndRank(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyLong())).thenReturn(1);
		when(advisorService.fetchAdvProductByAdvIdAndAdvProdId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdvproduct_not_found())))
				.andReturn();
	}

	@Test
	public void test_ModifyProductInfo_ErrorOccured() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<AdvProductRequest> productRequestList = new ArrayList<AdvProductRequest>();
		AdvProductRequest productRequest1 = new AdvProductRequest();
		productRequest1.setAdvProdId(1);
		productRequest1.setProdId(1);
		productRequest1.setServiceId("2");
		productRequest1.setValidity("14-02-2012");

		AdvProductRequest productRequest2 = new AdvProductRequest();
		productRequest2.setProdId(3);
		productRequest2.setServiceId("2");
		productRequest2.setValidity("14-02-2012");

		productRequestList.add(productRequest1);
		productRequestList.add(productRequest2);

		AdvProductInfoRequest advProductInfoReq = new AdvProductInfoRequest();
		advProductInfoReq.setAdvId("ADV0000000001");
		advProductInfoReq.setAdvProducts(productRequestList);
		advProductInfoReq.setScreenId(1);

		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct product1 = new AdvProduct();
		product1.setAdvId("ADV0000000001");
		product1.setAdvProdId(1);
		product1.setProdId(1);
		AdvProduct product2 = new AdvProduct();
		product2.setAdvId("ADV0000000001");
		product2.setAdvProdId(2);
		product2.setProdId(2);
		advProducts.add(product1);
		advProducts.add(product2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProductInfoReq);
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

		when(advisorService.fetchByAdvisorId("ADV0000000001")).thenReturn(adv);
		when(advProductInfoRequestValidator.validate(advProductInfoReq)).thenReturn(null);
		// remove
		when(advisorService.fetchAdvProductByAdvId("ADV0000000001")).thenReturn(advProducts);
		when(advisorService.removeAdvProduct(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfo(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.removeFromBrandRank(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		when(advisorService.checkAdvProductIsPresent(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);
		when(advisorService.modifyAdvisorProduct(Mockito.any(AdvProduct.class), Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProdInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_forgetPasswordAdv_Success() throws Exception {
		ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
		forgetPasswordRequest.setEmailId("abc@gmail.com");

		// Advisor
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		party.setEmailId("abc@gmail.com");

		Advisor adv = new Advisor();
		adv.setEmailId("abc@gmail.com");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forgetPasswordRequest);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);

		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
		when(forgetPasswordRequestValidator.validate(forgetPasswordRequest)).thenReturn(null);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(MockMvcRequestBuilders.put("/forgetPassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getReset_password_mail_sent())))
				.andReturn();
	}

	@Test
	public void test_validatePassword_NotFound() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);

		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

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

		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/validatePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_validatePassword_Success() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

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

		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);

		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.put("/validatePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_validatePassword_Mandatory() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

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

		mockMvc.perform(MockMvcRequestBuilders.put("/validatePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_validatePassword())))
				.andReturn();
	}

	@Test
	public void test_validatePassword_ScreenRights_AccessDenied() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/validatePassword").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_validatePassword_ScreenRights_UnAuthorized() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

		mockMvc.perform(put("/validatePassword").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_validatePassword_ScreenRights_Success() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

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

		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);

		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.put("/validatePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_forgetPasswordNonAdv_Success() throws Exception {
		ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
		forgetPasswordRequest.setEmailId("abc@gmail.com");

		// nonIndividualAdv
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(3);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		party.setEmailId("abc@gmail.com");

		Advisor adv = new Advisor();
		adv.setEmailId("abc@gmail.com");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forgetPasswordRequest);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
		when(forgetPasswordRequestValidator.validate(forgetPasswordRequest)).thenReturn(null);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(MockMvcRequestBuilders.put("/forgetPassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getReset_password_mail_sent())))
				.andReturn();
	}

	@Test
	public void test_forgetPasswordInv_Success() throws Exception {
		ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
		forgetPasswordRequest.setEmailId("abc@gmail.com");

		// Investor
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		party.setEmailId("abc@gmail.com");

		Investor inv = new Investor();
		inv.setEmailId("abc@gmail.com");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forgetPasswordRequest);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
		when(forgetPasswordRequestValidator.validate(forgetPasswordRequest)).thenReturn(null);
		when(advisorService.fetchInvestorByInvId(Mockito.anyString())).thenReturn(inv);

		doNothing().when(sendMail).sendMailMessage(Mockito.anyString(), Mockito.anyList(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(MockMvcRequestBuilders.put("/forgetPassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getReset_password_mail_sent())))
				.andReturn();
	}

	@Test
	public void test_forgetPasswordNoRecordFound() throws Exception {
		ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
		forgetPasswordRequest.setEmailId("abc@gmail.com");

		Advisor adv = new Advisor();
		adv.setEmailId("abc@gmail.com");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forgetPasswordRequest);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/forgetPassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getNo_record_found_with_emailid())))
				.andReturn();
	}

	@Test
	public void test_forgetPasswordValidation() throws Exception {
		ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
		forgetPasswordRequest.setEmailId("abc@gmail.com");

		// nonIndividualAdv
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(3);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		party.setEmailId("abc@gmail.com");

		Advisor adv = new Advisor();
		adv.setEmailId("abc@gmail.com");

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forgetPasswordRequest);

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
		when(forgetPasswordRequestValidator.validate(Mockito.any(ForgetPasswordRequest.class))).thenReturn(allErrors);

		mockMvc.perform(MockMvcRequestBuilders.put("/forgetPassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_forgetPasswordMandatory() throws Exception {
		ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();

		// nonIndividualAdv
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(3);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		party.setEmailId("abc@gmail.com");

		Advisor adv = new Advisor();
		adv.setEmailId("abc@gmail.com");

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forgetPasswordRequest);

		mockMvc.perform(MockMvcRequestBuilders.put("/forgetPassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_emailId())))
				.andReturn();
	}

	@Test
	public void test_AddProfInfo_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<AwardReq> awards = new ArrayList<AwardReq>();
		List<EducationReq> educations = new ArrayList<EducationReq>();
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();

		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setExperiences(experiences);
		advProfessionalInfoRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.addAdvProfessionalInfo(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_AddProfInfo_Mandatory() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		List<AwardReq> awards = new ArrayList<AwardReq>();
		List<EducationReq> educations = new ArrayList<EducationReq>();
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();

		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setExperiences(experiences);
		advProfessionalInfoRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_addAdvProfInfo_ScreenRights_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setScreenId(1);
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<AwardReq> awards = new ArrayList<AwardReq>();
		List<EducationReq> educations = new ArrayList<EducationReq>();
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setExperiences(experiences);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.addAdvProfessionalInfo(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(1);
		mockMvc.perform(post("/addAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addAdvProfInfo_ScreenRights_AccessDenied() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addAdvProfInfo_ScreenRights_UnAuthorized() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
		mockMvc.perform(post("/addAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_AddProfInfo_NotFound() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		advProfessionalInfoRequest.setScreenId(1);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		List<EducationReq> educations = new ArrayList<EducationReq>();
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();

		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setExperiences(experiences);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_AddProfInfo_ErrorOccured() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		advProfessionalInfoRequest.setScreenId(1);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		List<EducationReq> educations = new ArrayList<EducationReq>();
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();

		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setExperiences(experiences);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.addAdvProfessionalInfo(Mockito.anyString(), Mockito.any(Advisor.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_AddProfInfo_ValidationError() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		advProfessionalInfoRequest.setScreenId(1);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		List<EducationReq> educations = new ArrayList<EducationReq>();
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();

		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setExperiences(experiences);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(Mockito.any(AdvProfessionalInfoRequest.class)))
				.thenReturn(allErrors);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_modifyAdvProfInfo_ScreenRights_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setScreenId(1);
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");
		AwardReq awardReq2 = new AwardReq();
		awardReq2.setTitle("award two");
		awards.add(awardReq1);
		awards.add(awardReq2);
		advProfessionalInfoRequest.setAwards(awards);

		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		// when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(award);
		// when(advisorService.removeAdvAward(Mockito.anyLong(),
		// Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyAward(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		when(advisorService.addAdvAwardInfo(Mockito.anyString(), Mockito.any(Award.class))).thenReturn(1);
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
		mockMvc.perform(put("/modifyAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())));
	}

	@Test
	public void test_modifyAdvProfInfo_Mandatory() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setScreenId(1);
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");
		AwardReq awardReq2 = new AwardReq();
		awardReq2.setTitle("award two");
		awards.add(awardReq1);
		awards.add(awardReq2);
		advProfessionalInfoRequest.setAwards(awards);

		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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
		mockMvc.perform(put("/modifyAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));
	}

	@Test
	public void test_modifyAdvProfInfo_ScreenRights_AccessDenied() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/modifyAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyAdvProfInfo_ScreenRights_UnAuthorized() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
		mockMvc.perform(put("/modifyAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_ModifyAdvProfInfo_Award_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");
		AwardReq awardReq2 = new AwardReq();
		awardReq2.setTitle("award two");
		awards.add(awardReq1);
		awards.add(awardReq2);
		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setScreenId(1);
		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(award);
		when(advisorService.removeAdvAward(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyAward(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		// when(advisorService.addAdvAwardInfo(Mockito.anyString(),
		// Mockito.any(Award.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Award_NotFound() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		advProfessionalInfoRequest.setScreenId(1);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");
		AwardReq awardReq2 = new AwardReq();
		awardReq2.setTitle("award two");
		awards.add(awardReq1);
		awards.add(awardReq2);
		advProfessionalInfoRequest.setAwards(awards);

		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Award_DeleteAllError() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");
		AwardReq awardReq2 = new AwardReq();
		awardReq2.setTitle("award two");
		awards.add(awardReq1);
		awards.add(awardReq2);
		advProfessionalInfoRequest.setAwards(awards);

		List<Award> award = new ArrayList<Award>();

		List<String> deleteAll = new ArrayList<String>();
		deleteAll.add("award");
		advProfessionalInfoRequest.setDeleteAll(deleteAll);
		advProfessionalInfoRequest.setScreenId(1);

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.removeAwardByAdvId(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Award_Remove() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<AwardReq> awards = new ArrayList<AwardReq>();
	//
	// advProfessionalInfoRequest.setAwards(awards);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Award> award = new ArrayList<Award>();
	//
	// Award award1 = new Award();
	// award1.setAwardId(1);
	// award1.setTitle("award");
	// Award award2 = new Award();
	// award1.setAwardId(2);
	// award1.setTitle("award two");
	// award.add(award1);
	// award.add(award2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(award);
	// when(advisorService.removeAdvAward(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Award_RemoveErrorOccured() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<AwardReq> awards = new ArrayList<AwardReq>();

		advProfessionalInfoRequest.setAwards(awards);
		advProfessionalInfoRequest.setScreenId(1);

		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(award);
		when(advisorService.removeAdvAward(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// @Test // conditn moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Award_Modify() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// advProfessionalInfoRequest.setScreenId(1);
	// List<AwardReq> awards = new ArrayList<AwardReq>();
	// AwardReq awardReq1 = new AwardReq();
	// awardReq1.setAwardId(1);
	// awardReq1.setTitle("award one");
	// awards.add(awardReq1);
	// advProfessionalInfoRequest.setAwards(awards);
	//
	// List<Award> award = new ArrayList<Award>();
	//
	// Award award1 = new Award();
	// award1.setAwardId(1);
	// award1.setTitle("award");
	//
	// award.add(award1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(award);
	// when(advisorService.modifyAdvisorAward(Mockito.anyLong(),
	// Mockito.any(Award.class), Mockito.anyString()))
	// .thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Award_ModifyErrorOccured() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		advProfessionalInfoRequest.setScreenId(1);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");

		awards.add(awardReq1);
		advProfessionalInfoRequest.setAwards(awards);

		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");

		award.add(award1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(award);
		when(advisorService.modifyAdvisorAward(Mockito.anyLong(), Mockito.any(Award.class), Mockito.anyString()))
				.thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Award_Add() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<AwardReq> awards = new ArrayList<AwardReq>();
	// // AwardReq awardReq1 = new AwardReq();
	//
	// AwardReq awardReq2 = new AwardReq();
	// awardReq2.setTitle("award two");
	// awards.add(awardReq2);
	// advProfessionalInfoRequest.setAwards(awards);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Award> award = new ArrayList<Award>();
	//
	// Award award1 = new Award();
	// award1.setAwardId(1);
	// award1.setTitle("award");
	//
	// award.add(award1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvAwardInfo(Mockito.anyString(),
	// Mockito.any(Award.class))).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Award_AddErrorOccured() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<AwardReq> awards = new ArrayList<AwardReq>();
	// // AwardReq awardReq1 = new AwardReq();
	//
	// AwardReq awardReq2 = new AwardReq();
	// awardReq2.setTitle("award two");
	// awards.add(awardReq2);
	// advProfessionalInfoRequest.setAwards(awards);
	// advProfessionalInfoRequest.setScreenId(1);
	// List<Award> award = new ArrayList<Award>();
	//
	// Award award1 = new Award();
	// award1.setAwardId(1);
	// award1.setTitle("award");
	//
	// award.add(award1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchAwardByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvAwardInfo(Mockito.anyString(),
	// Mockito.any(Award.class))).thenReturn(0);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Certificate_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificateReq1 = new CertificateReq();
		certificateReq1.setCertificateId(1);
		certificateReq1.setTitle("certificate one");
		CertificateReq certificateReq2 = new CertificateReq();
		certificateReq2.setTitle("certificate two");
		certificates.add(certificateReq1);
		certificates.add(certificateReq2);
		advProfessionalInfoRequest.setCertificates(certificates);
		advProfessionalInfoRequest.setScreenId(1);

		List<Certificate> certificate = new ArrayList<Certificate>();

		Certificate certificate1 = new Certificate();
		certificate1.setCertificateId(1);
		certificate1.setTitle("certificate");
		Certificate certificate2 = new Certificate();
		certificate2.setCertificateId(2);
		certificate2.setTitle("certificate two");
		certificate.add(certificate1);
		certificate.add(certificate2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(certificate);
		when(advisorService.removeAdvCertificate(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyCertificate(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		// when(advisorService.addAdvCertificateInfo(Mockito.anyString(),
		// Mockito.any(Certificate.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Certificate_NotFound() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificateReq1 = new CertificateReq();
		certificateReq1.setCertificateId(1);
		certificateReq1.setTitle("certificate one");
		CertificateReq certificateReq2 = new CertificateReq();
		certificateReq2.setTitle("certificate two");
		certificates.add(certificateReq1);
		certificates.add(certificateReq2);
		advProfessionalInfoRequest.setCertificates(certificates);
		advProfessionalInfoRequest.setScreenId(1);

		List<Certificate> certificate = new ArrayList<Certificate>();

		Certificate certificate1 = new Certificate();
		certificate1.setCertificateId(1);
		certificate1.setTitle("certificate");
		Certificate certificate2 = new Certificate();
		certificate2.setCertificateId(2);
		certificate1.setTitle("certificate two");
		certificate.add(certificate1);
		certificate.add(certificate2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Certificate_DeleteAllError() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificateReq1 = new CertificateReq();
		certificateReq1.setCertificateId(1);
		certificateReq1.setTitle("certificate one");
		CertificateReq certificateReq2 = new CertificateReq();
		certificateReq2.setTitle("certificate two");
		certificates.add(certificateReq1);
		certificates.add(certificateReq2);
		advProfessionalInfoRequest.setCertificates(certificates);

		List<Certificate> certificate = new ArrayList<Certificate>();

		List<String> deleteAll = new ArrayList<String>();
		deleteAll.add("Certificate");
		advProfessionalInfoRequest.setDeleteAll(deleteAll);
		advProfessionalInfoRequest.setScreenId(1);

		Certificate certificate1 = new Certificate();
		certificate1.setCertificateId(1);
		certificate1.setTitle("certificate");
		Certificate certificate2 = new Certificate();
		certificate2.setCertificateId(2);
		certificate2.setTitle("certificate two");
		certificate.add(certificate1);
		certificate.add(certificate2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.removeCertificateByAdvId(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Certificate_Remove() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<CertificateReq> certificates = new ArrayList<CertificateReq>();
	//
	// advProfessionalInfoRequest.setCertificates(certificates);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Certificate> certificate = new ArrayList<Certificate>();
	//
	// Certificate certificate1 = new Certificate();
	// certificate1.setCertificateId(1);
	// certificate1.setTitle("award");
	// Certificate certificate2 = new Certificate();
	// certificate2.setCertificateId(2);
	// certificate2.setTitle("award two");
	// certificate.add(certificate1);
	// certificate.add(certificate2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(certificate);
	// when(advisorService.removeAdvCertificate(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Certificate_RemoveErrorOccured() throws
	// Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<CertificateReq> certificates = new ArrayList<CertificateReq>();
	//
	// advProfessionalInfoRequest.setCertificates(certificates);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Certificate> certificate = new ArrayList<Certificate>();
	//
	// Certificate certificate1 = new Certificate();
	// certificate1.setCertificateId(1);
	// certificate1.setTitle("award");
	// Certificate certificate2 = new Certificate();
	// certificate2.setCertificateId(2);
	// certificate2.setTitle("award two");
	// certificate.add(certificate1);
	// certificate.add(certificate2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(certificate);
	// when(advisorService.removeAdvCertificate(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(0);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Certificate_Modify() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<CertificateReq> certificates = new ArrayList<CertificateReq>();
	// CertificateReq certificateReq1 = new CertificateReq();
	// certificateReq1.setCertificateId(1);
	// certificateReq1.setTitle("certificate one");
	//
	// certificates.add(certificateReq1);
	// advProfessionalInfoRequest.setCertificates(certificates);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Certificate> certificate = new ArrayList<Certificate>();
	//
	// Certificate certificate1 = new Certificate();
	// certificate1.setCertificateId(1);
	// certificate1.setTitle("award");
	//
	// certificate.add(certificate1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(certificate);
	// when(advisorService.modifyAdvisorCertificate(Mockito.anyLong(),
	// Mockito.any(Certificate.class),
	// Mockito.anyString())).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Certificate_ModifyErrorOccured() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<CertificateReq> certificates = new ArrayList<CertificateReq>();
		CertificateReq certificateReq1 = new CertificateReq();
		certificateReq1.setCertificateId(1);
		certificateReq1.setTitle("award one");

		certificates.add(certificateReq1);
		advProfessionalInfoRequest.setCertificates(certificates);
		advProfessionalInfoRequest.setScreenId(1);

		List<Certificate> certificate = new ArrayList<Certificate>();

		Certificate certificate1 = new Certificate();
		certificate1.setCertificateId(1);
		certificate1.setTitle("certificate");

		certificate.add(certificate1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(certificate);
		when(advisorService.modifyCertificate(Mockito.anyString(), Mockito.anyList())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// @Test
	// public void test_ModifyAdvProfInfo_Certificate_Add() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<CertificateReq> certificates = new ArrayList<CertificateReq>();
	//
	// CertificateReq certificateReq2 = new CertificateReq();
	// certificateReq2.setTitle("certificate two");
	// certificates.add(certificateReq2);
	// advProfessionalInfoRequest.setCertificates(certificates);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Certificate> certificate = new ArrayList<Certificate>();
	//
	// Certificate certificate1 = new Certificate();
	// certificate1.setCertificateId(1);
	// certificate1.setTitle("certificate");
	//
	// certificate.add(certificate1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvCertificateInfo(Mockito.anyString(),
	// Mockito.any(Certificate.class))).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Certificate_AddErrorOccured() throws
	// Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// advProfessionalInfoRequest.setScreenId(1);
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<CertificateReq> certificates = new ArrayList<CertificateReq>();
	//
	// CertificateReq certificateReq2 = new CertificateReq();
	// certificateReq2.setTitle("certificate two");
	// certificates.add(certificateReq2);
	// advProfessionalInfoRequest.setCertificates(certificates);
	//
	// List<Certificate> certificate = new ArrayList<Certificate>();
	//
	// Certificate certificate1 = new Certificate();
	// certificate1.setCertificateId(1);
	// certificate1.setTitle("certificate");
	//
	// certificate.add(certificate1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchCertificateByadvId("ADV000000000A")).thenReturn(null);
	// when(advisorService.addAdvCertificateInfo(Mockito.anyString(),
	// Mockito.any(Certificate.class))).thenReturn(0);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Experience_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experienceReq1 = new ExperienceReq();
		experienceReq1.setExpId(1);
		experienceReq1.setLocation("chennai");
		ExperienceReq experienceReq2 = new ExperienceReq();
		experienceReq2.setLocation("Karnataka");
		experiences.add(experienceReq1);
		experiences.add(experienceReq2);
		advProfessionalInfoRequest.setExperiences(experiences);
		advProfessionalInfoRequest.setScreenId(1);

		List<Experience> experience = new ArrayList<Experience>();

		Experience experience1 = new Experience();
		experience1.setExpId(1);
		experience1.setLocation("chennai");
		Experience experience2 = new Experience();
		experience2.setExpId(2);
		experience2.setLocation("kerala");
		experience.add(experience1);
		experience.add(experience2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(experience);
		when(advisorService.removeAdvExperience(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyExperience(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		// when(advisorService.addAdvExperienceInfo(Mockito.anyString(),
		// Mockito.any(Experience.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Experience_NotFound() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experienceReq1 = new ExperienceReq();
		experienceReq1.setExpId(1);
		experienceReq1.setLocation("chennai");
		ExperienceReq experienceReq2 = new ExperienceReq();
		experienceReq2.setLocation("kerala");
		experiences.add(experienceReq1);
		experiences.add(experienceReq2);
		advProfessionalInfoRequest.setExperiences(experiences);
		advProfessionalInfoRequest.setScreenId(1);

		List<Experience> experience = new ArrayList<Experience>();

		Experience experience1 = new Experience();
		experience1.setExpId(1);
		experience1.setLocation("chennai");
		Experience experience2 = new Experience();
		experience2.setExpId(2);
		experience2.setLocation("kerala");
		experience.add(experience1);
		experience.add(experience2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Experience_DeleteAllError() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experienceReq1 = new ExperienceReq();
		experienceReq1.setExpId(1);
		experienceReq1.setLocation("chennai");
		ExperienceReq experienceReq2 = new ExperienceReq();
		experienceReq2.setLocation("kerala");
		experiences.add(experienceReq1);
		experiences.add(experienceReq2);
		advProfessionalInfoRequest.setExperiences(experiences);

		List<Experience> experience = new ArrayList<Experience>();

		List<String> deleteAll = new ArrayList<String>();
		deleteAll.add("experience");
		advProfessionalInfoRequest.setDeleteAll(deleteAll);
		advProfessionalInfoRequest.setScreenId(1);

		Experience experience1 = new Experience();
		experience1.setExpId(1);
		experience1.setLocation("chennai");
		Experience experience2 = new Experience();
		experience2.setExpId(2);
		experience2.setLocation("kerala");
		experience.add(experience1);
		experience.add(experience2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.removeExperienceByAdvId(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Experience_Remove() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
	//
	// advProfessionalInfoRequest.setExperiences(experiences);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Experience> experience = new ArrayList<Experience>();
	//
	// Experience experience1 = new Experience();
	// experience1.setExpId(1);
	// experience1.setLocation("chennai");
	// Experience experience2 = new Experience();
	// experience2.setExpId(2);
	// experience2.setLocation("kerala");
	// experience.add(experience1);
	// experience.add(experience2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(experience);
	// when(advisorService.removeAdvExperience(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Experience_RemoveErrorOccured() throws
	// Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
	//
	// advProfessionalInfoRequest.setExperiences(experiences);
	// advProfessionalInfoRequest.setScreenId(1);
	// List<Experience> experience = new ArrayList<Experience>();
	//
	// Experience experience1 = new Experience();
	// experience1.setExpId(1);
	// experience1.setLocation("chennai");
	// Experience experience2 = new Experience();
	// experience2.setExpId(2);
	// experience2.setLocation("kerala");
	// experience.add(experience1);
	// experience.add(experience2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(experience);
	// when(advisorService.removeAdvExperience(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(0);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Experience_Modify() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
	// ExperienceReq experienceReq1 = new ExperienceReq();
	// experienceReq1.setExpId(1);
	// experienceReq1.setLocation("chennai");
	//
	// experiences.add(experienceReq1);
	// advProfessionalInfoRequest.setExperiences(experiences);
	//
	// List<Experience> experience = new ArrayList<Experience>();
	//
	// Experience experience1 = new Experience();
	// experience1.setExpId(1);
	// experience1.setLocation("chennai");
	//
	// experience.add(experience1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// //
	// when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(experience);
	// when(advisorService.modifyExperience(Mockito.anyString(),
	// Mockito.anyList())).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Experience_ModifyErrorOccured() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		advProfessionalInfoRequest.setScreenId(1);
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
		ExperienceReq experienceReq1 = new ExperienceReq();
		experienceReq1.setExpId(1);
		experienceReq1.setLocation("chennai");

		experiences.add(experienceReq1);
		advProfessionalInfoRequest.setExperiences(experiences);

		List<Experience> experience = new ArrayList<Experience>();

		Experience experience1 = new Experience();
		experience1.setExpId(1);
		experience1.setLocation("chennai");

		experience.add(experience1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		// when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(experience);
		when(advisorService.modifyExperience(Mockito.anyString(), Mockito.anyList())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// @Test //Conditon moved to seviceImpl
	// public void test_ModifyAdvProfInfo_Experience_Add() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
	//
	// ExperienceReq experienceReq2 = new ExperienceReq();
	// experienceReq2.setLocation("kerala");
	// experiences.add(experienceReq2);
	// advProfessionalInfoRequest.setExperiences(experiences);
	// advProfessionalInfoRequest.setScreenId(1);
	// List<Experience> experience = new ArrayList<Experience>();
	//
	// Experience experience1 = new Experience();
	// experience1.setExpId(1);
	// experience1.setLocation("chennai");
	//
	// experience.add(experience1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvExperienceInfo(Mockito.anyString(),
	// Mockito.any(Experience.class))).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Experience_AddErrorOccured() throws
	// Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<ExperienceReq> experiences = new ArrayList<ExperienceReq>();
	//
	// ExperienceReq experienceReq2 = new ExperienceReq();
	// experienceReq2.setLocation("kerala");
	// experiences.add(experienceReq2);
	// advProfessionalInfoRequest.setExperiences(experiences);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Experience> experience = new ArrayList<Experience>();
	//
	// Experience experience1 = new Experience();
	// experience1.setExpId(1);
	// experience1.setLocation("chennai");
	//
	// experience.add(experience1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchExperienceByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvExperienceInfo(Mockito.anyString(),
	// Mockito.any(Experience.class))).thenReturn(0);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Education_Success() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		advProfessionalInfoRequest.setScreenId(1);
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq educationReq1 = new EducationReq();
		educationReq1.setEduId(1);
		educationReq1.setDegree("BA");
		EducationReq educationReq2 = new EducationReq();
		educationReq2.setDegree("BE");
		educations.add(educationReq1);
		educations.add(educationReq2);
		advProfessionalInfoRequest.setEducations(educations);

		List<Education> education = new ArrayList<Education>();

		Education education1 = new Education();
		education1.setEduId(1);
		education1.setDegree("BA");
		Education education2 = new Education();
		education2.setEduId(2);
		education2.setDegree("BE");
		education.add(education1);
		education.add(education2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(education);
		when(advisorService.removeAdvEducation(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		when(advisorService.modifyEducation(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		// when(advisorService.addAdvEducationInfo(Mockito.anyString(),
		// Mockito.any(Education.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Education_NotFound() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		advProfessionalInfoRequest.setScreenId(1);

		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq educationReq1 = new EducationReq();
		educationReq1.setEduId(1);
		educationReq1.setDegree("BA");
		EducationReq educationReq2 = new EducationReq();
		educationReq2.setDegree("BE");
		educations.add(educationReq1);
		educations.add(educationReq2);
		advProfessionalInfoRequest.setEducations(educations);

		List<Education> education = new ArrayList<Education>();

		Education education1 = new Education();
		education1.setEduId(1);
		education1.setDegree("BA");
		Education education2 = new Education();
		education2.setEduId(2);
		education2.setDegree("BE");
		education.add(education1);
		education.add(education2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
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

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_ModifyAdvProfInfo_Education_DeleteAllError() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq educationReq1 = new EducationReq();
		educationReq1.setEduId(1);
		educationReq1.setDegree("BA");
		EducationReq educationReq2 = new EducationReq();
		educationReq2.setDegree("BE");
		educations.add(educationReq1);
		educations.add(educationReq2);
		advProfessionalInfoRequest.setEducations(educations);

		List<Education> education = new ArrayList<Education>();

		List<String> deleteAll = new ArrayList<String>();
		deleteAll.add("education");
		advProfessionalInfoRequest.setDeleteAll(deleteAll);
		advProfessionalInfoRequest.setScreenId(1);

		Education education1 = new Education();
		education1.setEduId(1);
		education1.setDegree("BA");
		Education education2 = new Education();
		education2.setEduId(2);
		education2.setDegree("BE");
		education.add(education1);
		education.add(education2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		when(advisorService.removeEducationByAdvId(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Education_Remove() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<EducationReq> educations = new ArrayList<EducationReq>();
	//
	// advProfessionalInfoRequest.setEducations(educations);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Education> education = new ArrayList<Education>();
	//
	// Education education1 = new Education();
	// education1.setEduId(1);
	// education1.setDegree("BA");
	// Education education2 = new Education();
	// education2.setEduId(2);
	// education2.setDegree("BE");
	// education.add(education1);
	// education.add(education2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(education);
	// when(advisorService.removeAdvEducation(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Education_RemoveErrorOccured() throws
	// Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<EducationReq> educations = new ArrayList<EducationReq>();
	//
	// advProfessionalInfoRequest.setEducations(educations);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Education> education = new ArrayList<Education>();
	//
	// Education education1 = new Education();
	// education1.setEduId(1);
	// education1.setDegree("BA");
	// Education education2 = new Education();
	// education2.setEduId(2);
	// education2.setDegree("BE");
	// education.add(education1);
	// education.add(education2);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(education);
	// when(advisorService.removeAdvEducation(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(0);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Education_Modify() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<EducationReq> educations = new ArrayList<EducationReq>();
	// EducationReq educationReq1 = new EducationReq();
	// educationReq1.setEduId(1);
	// educationReq1.setDegree("BA");
	//
	// educations.add(educationReq1);
	// advProfessionalInfoRequest.setEducations(educations);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Education> education = new ArrayList<Education>();
	//
	// Education education1 = new Education();
	// education1.setEduId(1);
	// education1.setDegree("BA");
	//
	// education.add(education1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(education);
	// when(advisorService.modifyAdvisorEducation(Mockito.anyLong(),
	// Mockito.any(Education.class),
	// Mockito.anyString())).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_Education_ModifyErrorOccured() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<EducationReq> educations = new ArrayList<EducationReq>();
		EducationReq educationReq1 = new EducationReq();
		educationReq1.setEduId(1);
		educationReq1.setDegree("BA");

		educations.add(educationReq1);
		advProfessionalInfoRequest.setEducations(educations);
		advProfessionalInfoRequest.setScreenId(1);
		List<Education> education = new ArrayList<Education>();

		Education education1 = new Education();
		education1.setEduId(1);
		education1.setDegree("BA");

		education.add(education1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
		// when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(education);
		when(advisorService.modifyEducation(Mockito.anyString(), Mockito.anyList())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_ModifyAdvProfInfo_Education_Add() throws Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// List<EducationReq> educations = new ArrayList<EducationReq>();
	//
	// EducationReq educationReq2 = new EducationReq();
	// educationReq2.setDegree("BE");
	// educations.add(educationReq2);
	// advProfessionalInfoRequest.setEducations(educations);
	// advProfessionalInfoRequest.setScreenId(1);
	//
	// List<Education> education = new ArrayList<Education>();
	//
	// Education education1 = new Education();
	// education1.setEduId(1);
	// education1.setDegree("BA");
	//
	// education.add(education1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvEducationInfo(Mockito.anyString(),
	// Mockito.any(Education.class))).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_updated_successfully())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_ModifyAdvProfInfo_Education_AddErrorOccured() throws
	// Exception {
	// AdvProfessionalInfoRequest advProfessionalInfoRequest = new
	// AdvProfessionalInfoRequest();
	// advProfessionalInfoRequest.setAdvId("ADV000000000A");
	// List<String> delete_all = new ArrayList<>();
	// advProfessionalInfoRequest.setDeleteAll(delete_all);
	// advProfessionalInfoRequest.setScreenId(1);
	// List<EducationReq> educations = new ArrayList<EducationReq>();
	//
	// EducationReq educationReq2 = new EducationReq();
	// educationReq2.setDegree("BA");
	// educations.add(educationReq2);
	// advProfessionalInfoRequest.setEducations(educations);
	//
	// List<Education> education = new ArrayList<Education>();
	//
	// Education education1 = new Education();
	// education1.setEduId(1);
	// education1.setDegree("BA");
	//
	// education.add(education1);
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(advProfessionalInfoRequestValidator.validate(advProfessionalInfoRequest)).thenReturn(null);
	// when(advisorService.fetchEducationByadvId("ADV000000000A")).thenReturn(null);
	//
	// when(advisorService.addAdvEducationInfo(Mockito.anyString(),
	// Mockito.any(Education.class))).thenReturn(0);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyAdvProfInfo").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_ModifyAdvProfInfo_ValidationError() throws Exception {
		AdvProfessionalInfoRequest advProfessionalInfoRequest = new AdvProfessionalInfoRequest();
		advProfessionalInfoRequest.setAdvId("ADV000000000A");
		advProfessionalInfoRequest.setScreenId(1);
		List<String> delete_all = new ArrayList<>();
		advProfessionalInfoRequest.setDeleteAll(delete_all);
		List<AwardReq> awards = new ArrayList<AwardReq>();
		AwardReq awardReq1 = new AwardReq();
		awardReq1.setAwardId(1);
		awardReq1.setTitle("award one");
		AwardReq awardReq2 = new AwardReq();
		awardReq2.setTitle("award two");
		awards.add(awardReq1);
		awards.add(awardReq2);
		advProfessionalInfoRequest.setAwards(awards);

		List<Award> award = new ArrayList<Award>();

		Award award1 = new Award();
		award1.setAwardId(1);
		award1.setTitle("award");
		Award award2 = new Award();
		award1.setAwardId(2);
		award1.setTitle("award two");
		award.add(award1);
		award.add(award2);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advProfessionalInfoRequest);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advProfessionalInfoRequestValidator.validate(Mockito.any(AdvProfessionalInfoRequest.class)))
				.thenReturn(allErrors);

		mockMvc.perform(put("/modifyAdvProfInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(500))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_changeAdvPassword_ScreenRights_Success() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setScreenId(1);
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
		Advisor adv = new Advisor();
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleIdAdv);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeAdvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
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
		mockMvc.perform(put("/changePassword").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPassword_changed_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_changeAdvPassword_Mandatory() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setScreenId(1);
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");

		Party party = new Party();
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
		Advisor adv = new Advisor();
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
		mockMvc.perform(put("/changePassword").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_role())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_changeAdvPassword_ScreenRights_AccessDenied() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/changePassword").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_changeAdvPassword_ScreenRights_UnAuthorized() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

		mockMvc.perform(put("/changePassword").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_changeAdvPassword_Success() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdAdv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

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
		Advisor adv = new Advisor();
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleIdAdv);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeAdvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPassword_changed_successfully())))
				.andReturn();
	}

	@Test
	public void test_changeNonAdvPassword_Success() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(3);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdNonAdv = 3;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
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
		Advisor adv = new Advisor();
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(roleIdNonAdv);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeAdvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPassword_changed_successfully())))
				.andReturn();
	}

	@Test
	public void test_changePassword_NotFound() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
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

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_changePassword_PasswordMatchError() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
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
		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getIncorrect_password())))
				.andReturn();
	}

	@Test
	public void test_changePassword_ValidationError() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
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

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(Mockito.any(PasswordChangeRequest.class))).thenReturn(allErrors);

		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", is(allErrors))).andReturn();
	}

	@Test
	public void test_changeAdvPassword_ErrorOccured() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		Advisor adv = new Advisor();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
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

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeAdvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);

		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// ================== Change Investor Password Test ========================//
	@Test
	public void test_changeInvPassword_Success() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("INV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("INV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		long roleIdInv = 2;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
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
		Advisor adv = new Advisor();
		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(roleIdInv);
		when(advisorService.fetchPartyByRoleBasedId("INV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeInvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPassword_changed_successfully())))
				.andReturn();
	}

	@Test
	public void test_changeInvPassword_ErrorOccured() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("INV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("INV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);
		Advisor adv = new Advisor();

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

		when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("INV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeInvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// Invalid RoleId Test//
	@Test
	public void test_Invalid_RoleId_Error() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setRoleBasedId("ADV000000000A");
		passwordChangeRequest.setCurrentPassword("AS!@as12");
		passwordChangeRequest.setNewPassword("!@AS12as");
		passwordChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(4);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(commonService.fetchUserRoleByUserId(Mockito.anyLong())).thenReturn(user_role);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(passwordChangeRequest);

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

		when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
		when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
		when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
		when(advisorService.fetchPartyByRoleBasedId("ADV000000000A")).thenReturn(party);
		when(advisorService.checkForPasswordMatch(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(passwordValidator.validate(passwordChangeRequest)).thenReturn(null);
		when(advisorService.changeInvPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/changePassword").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getRoleId_not_found())))
				.andReturn();
	}

	// @Test
	// public void test_resetPassword_Inv_Success() throws Exception {
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(2);
	// party.setEmailId("abc@gmail.com");
	// party.setPhoneNumber("9994857120");
	// party.setPassword("abc@123");
	// String emailId = "abc@gmail.com";
	// String phoneNumber = "9994857120";
	// String password = "abc@123";
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	//
	// String key = mailConstants.getStartkey() + "," + emailId + "," +
	// mailConstants.getMidkey() + "," + phoneNumber
	// + "," + password + "," + timestamp + "," + mailConstants.getEndkey();
	// String encodedKey = encodeKey(key);
	//
	// long roleId = 2;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	// when(resetPasswordRequestValidator.validate(resetPasswordRequest)).thenReturn(null);
	// when(advisorService.changeInvPassword(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").param("key",
	// encodedKey).content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPassword_changed_successfully())))
	// .andReturn();
	// }
	//
	// private String encodeKey(String key) {
	// Base64.Encoder encoder = Base64.getEncoder();
	// String encodedString = encoder.encodeToString(key.getBytes());
	// return encodedString;
	// }
	// @Test
	// public void test_resetPassword_Adv_Success() throws Exception {
	//
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(1);
	// party.setEmailId("abc@gmail.com");
	// party.setPhoneNumber("9994857120");
	// party.setPassword("abc@123");
	// String emailId = "abc@gmail.com";
	// String phoneNumber = "9994857120";
	// String password = "abc@123";
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	//
	// String key = mailConstants.getStartkey() + "," + emailId + "," +
	// mailConstants.getMidkey() + "," + phoneNumber
	// + "," + password + "," + timestamp + "," + mailConstants.getEndkey();
	// String encodedKey = encodeKey(key);
	//
	// long roleId = 1;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	// when(resetPasswordRequestValidator.validate(resetPasswordRequest)).thenReturn(null);
	// when(advisorService.changeAdvPassword(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").param("key",
	// encodedKey).content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPassword_changed_successfully())))
	// .andReturn();
	//
	// }
	//
	// @Test
	// public void test_resetPassword_NonAdv_Success() throws Exception {
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(3);
	// party.setEmailId("abc@gmail.com");
	// party.setPhoneNumber("9994857120");
	// party.setPassword("abc@123");
	// String emailId = "abc@gmail.com";
	// String phoneNumber = "9994857120";
	// String password = "abc@123";
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	//
	// String key = mailConstants.getStartkey() + "," + emailId + "," +
	// mailConstants.getMidkey() + "," + phoneNumber
	// + "," + password + "," + timestamp + "," + mailConstants.getEndkey();
	// String encodedKey = encodeKey(key);
	//
	// long roleId = 3;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	// when(resetPasswordRequestValidator.validate(resetPasswordRequest)).thenReturn(null);
	// when(advisorService.changeAdvPassword(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").param("key",
	// encodedKey).content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPassword_changed_successfully())))
	// .andReturn();
	//
	// }
	//
	// @Test
	// public void test_resetPassword_Inv_Error() throws Exception {
	//
	// long roleId = 2;
	//
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(2);
	// party.setEmailId("abc@gmail.com");
	// party.setPhoneNumber("9994857120");
	// party.setPassword("abc@123");
	// String emailId = "abc@gmail.com";
	// String phoneNumber = "9994857120";
	// String password = "abc@123";
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	//
	// String key = mailConstants.getStartkey() + "," + emailId + "," +
	// mailConstants.getMidkey() + "," + phoneNumber
	// + "," + password + "," + timestamp + "," + mailConstants.getEndkey();
	// String encodedKey = encodeKey(key);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	// when(resetPasswordRequestValidator.validate(resetPasswordRequest)).thenReturn(null);
	// when(advisorService.changeAdvPassword(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").param("key",
	// encodedKey).content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPassword_changed_successfully())))
	// .andReturn();
	//
	// }
	//
	// @Test
	// public void test_resetPassword_Adv_Error() throws Exception {
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(1);
	//
	// long roleId = 1;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	// when(resetPasswordRequestValidator.validate(resetPasswordRequest)).thenReturn(null);
	// when(advisorService.changeInvPassword(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(0);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").content(jsonString).param("key",
	// "c3RhcnQsbG9nZXN3YXJpQHNvd2lzZXRlY2guY29tLG1pZGRsZSxMb2dlc2gsOTg3NDU2MzIxMCwyMDIwLTEwLTA4IDE1OjU1OjM3Ljc5NixlbmQ=")
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_resetPassword_NoRecordFound() throws Exception {
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(2);
	//
	// long roleId = 2;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(null);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").content(jsonString).param("key",
	// "c3RhcnQsbG9nZXN3YXJpQHNvd2lzZXRlY2guY29tLG1pZGRsZSxMb2dlc2gsOTg3NDU2MzIxMCwyMDIwLTEwLTA4IDE1OjU1OjM3Ljc5NixlbmQ=")
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_resetPassword_Validator() throws Exception {
	// ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
	// resetPasswordRequest.setNewPassword("Sowise123");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(2);
	//
	// long roleId = 2;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resetPasswordRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(1L);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	// when(resetPasswordRequestValidator.validate(resetPasswordRequest)).thenReturn(null);
	//
	// mockMvc.perform(MockMvcRequestBuilders.put("/resetPassword").content(jsonString).param("key",
	// "c3RhcnQsbG9nZXN3YXJpQHNvd2lzZXRlY2guY29tLG1pZGRsZSxMb2dlc2gsOTg3NDU2MzIxMCwyMDIwLTEwLTA4IDE1OjU1OjM3Ljc5NixlbmQ=")
	// .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
	// .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError()))).andReturn();
	// }
	//
	// @Test
	// public void test_resendMail_Adv_Success() throws Exception {
	// String signup = AdvisorConstants.SIGNUP_RESEND;
	// String emailId = "info@gmail.com";
	// String phoneNumber = "9994857120";
	// String password = "Sowise@123";
	// String timestamp = "";
	// boolean isVerified = true;
	//
	// String key = mailConstants.getStartkey() + "," + emailId + "," +
	// mailConstants.getMidkey() + "," + phoneNumber
	// + "," + password + "," + timestamp + "," + mailConstants.getEndkey();
	// String encodedKey = encodeKey(key);
	//
	// ResendMailRequest resendMailRequest = new ResendMailRequest();
	// resendMailRequest.setKey(signup);
	// resendMailRequest.setToken(
	// "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXlhbnRoaUBzb3dpc2V0ZWNoLmNvbSIsImV4cCI6MTYwMjE0NzMzMCwiaWF0IjoxNjAyMTQzNzMwfQ.63qCMkE3Hf24HWscCcYxDM95vzMIYSIgnOwuGcBpVbKa9FKfPxFkBxDC7LEvzG2bC5sQwDKYieetO-MEzwy6Wg");
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleId(1);
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV0000000001");
	//
	// long roleId = 1;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(resendMailRequest);
	//
	// when(advisorService.fetchAdvRoleIdByName()).thenReturn(roleId);
	// when(advisorService.fetchNonAdvRoleIdByName()).thenReturn(3L);
	// when(advisorService.fetchInvRoleIdByName()).thenReturn(2L);
	// when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// doNothing().when(sendMail).sendMailMessage(Mockito.anyString(),
	// Mockito.anyList(), Mockito.anyString(),
	// Mockito.any(), Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString());
	//
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/resendMail").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getVerification_mail_sent())))
	// .andReturn();
	// }

	// @Test //data null error//
	// public void test_addPromotion_ScreenRights_Success() throws Exception {
	// PromotionRequest promotionRequest = new PromotionRequest();
	// promotionRequest.setAdvId("ADV000000000A");
	// List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
	// promotionRequest.setPromotionReq(promoReq);
	// promotionRequest.setScreenId(1);
	// List<Promotion> promotions = new ArrayList<Promotion>();
	// Promotion promotion1 = new Promotion();
	// promotion1.setAboutVideo("This is my video");
	// promotion1.setAdvId("ADV000000000A");
	// Promotion promotion2 = new Promotion();
	// promotion2.setAboutVideo("This is my another video");
	// promotion2.setAdvId("ADV000000000B");
	// promotions.add(promotion1);
	// promotions.add(promotion2);
	//
	// Promotion promo = new Promotion();
	// promo.setTitle("Promotion");
	// promo.setAboutVideo("Video");
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(promotionRequest);
	// when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
	// when(promotionRequestValidator.validate(promotionRequest)).thenReturn(null);
	// //
	// when(advisorService.fetchPromotionByAdvId(Mockito.anyString())).thenReturn(promotions);
	// // when(advisorService.removePromotion(Mockito.anyLong(),
	// // Mockito.anyString())).thenReturn(1);
	// // when(advisorService.addPromotion(Mockito.anyString(),
	// // Mockito.any())).thenReturn(0);
	// when(advisorService.addAndModifyPromotion(Mockito.anyString(),
	// Mockito.anyList())).thenReturn(1);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// mockMvc.perform(post("/addPromotion").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_added_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();

	// }

	@Test
	public void test_addPromotion_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addPromotion").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addPromotion_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/addPromotion").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addPromotion_Success() throws Exception {
		PromotionRequest promotionRequest = new PromotionRequest();
		promotionRequest.setAdvId("ADV000000000A");
		List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
		promotionRequest.setPromotionReq(promoReq);
		promotionRequest.setScreenId(1);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAboutVideo("This is my video");
		promotion1.setAdvId("ADV000000000A");
		Promotion promotion2 = new Promotion();
		promotion2.setAboutVideo("This is my another video");
		promotion2.setAdvId("ADV000000000B");
		promotions.add(promotion1);
		promotions.add(promotion2);

		Promotion promo = new Promotion();
		promo.setTitle("Promotion");
		promo.setAboutVideo("Video");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(promotionRequest);
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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(promotionRequestValidator.validate(promotionRequest)).thenReturn(null);
		//
		when(advisorService.fetchPromotionByAdvId(Mockito.anyString())).thenReturn(promotions);
		// when(advisorService.removePromotion(Mockito.anyLong(),
		// Mockito.anyString())).thenReturn(1);
		// when(advisorService.addPromotion(Mockito.anyString(),
		// Mockito.any())).thenReturn(0);
		when(advisorService.addAndModifyPromotion(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_addPromotion_Mandatory() throws Exception {
		PromotionRequest promotionRequest = new PromotionRequest();
		List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
		promotionRequest.setPromotionReq(promoReq);
		promotionRequest.setScreenId(1);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAboutVideo("This is my video");
		promotion1.setAdvId("ADV000000000A");
		Promotion promotion2 = new Promotion();
		promotion2.setAboutVideo("This is my another video");
		promotion2.setAdvId("ADV000000000B");
		promotions.add(promotion1);
		promotions.add(promotion2);

		Promotion promo = new Promotion();
		promo.setTitle("Promotion");
		promo.setAboutVideo("Video");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(promotionRequest);
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
		mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_addPromotion_NotFound() throws Exception {
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		PromotionRequest promotionRequest = new PromotionRequest();
		promotionRequest.setAdvId("ADV000000000A");
		List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
		promotionRequest.setPromotionReq(promoReq);
		promotionRequest.setScreenId(1);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAboutVideo("This is my video");
		promotion1.setAdvId("ADV000000000A");
		promotion1.setPromotionId(1);
		Promotion promotion2 = new Promotion();
		promotion2.setAboutVideo("This is my another video");
		promotion2.setAdvId("ADV000000000B");
		promotion2.setPromotionId(2);
		promotions.add(promotion1);
		promotions.add(promotion2);

		Promotion promo = new Promotion();
		promo.setTitle("Promotion");
		promo.setAboutVideo("Video");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(promotionRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	// @Test //Condition moved to serviceImpl
	// public void test_removePromotion_Success() throws Exception {
	// PromotionRequest promotionRequest = new PromotionRequest();
	// promotionRequest.setAdvId("ADV000000000A");
	// List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
	// promotionRequest.setPromotionReq(promoReq);
	//
	// promotionRequest.setScreenId(1);
	// List<Promotion> promotions = new ArrayList<Promotion>();
	// Promotion promotion1 = new Promotion();
	// promotion1.setAboutVideo("This is my video");
	// promotion1.setAdvId("ADV000000000A");
	// promotion1.setPromotionId(1);
	// Promotion promotion2 = new Promotion();
	// promotion2.setAboutVideo("This is my another video");
	// promotion2.setAdvId("ADV000000000B");
	// promotion2.setPromotionId(2);
	// promotions.add(promotion1);
	// promotions.add(promotion2);
	//
	// Promotion promo = new Promotion();
	// promo.setTitle("Promotion");
	// promo.setAboutVideo("Video");
	//
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(promotionRequest);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchByAdvisorId(Mockito.anyString())).thenReturn(adv);
	// when(promotionRequestValidator.validate(promotionRequest)).thenReturn(null);
	// when(advisorService.fetchPromotionByAdvId(Mockito.anyString())).thenReturn(promotions);
	// when(advisorService.removePromotion(Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAdvisor_added_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_modifyPromotion_Success() throws Exception {
		PromotionRequest promotionRequest = new PromotionRequest();
		promotionRequest.setAdvId("ADV000000000A");
		List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
		promotionRequest.setPromotionReq(promoReq);
		promotionRequest.setScreenId(1);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAboutVideo("This is my video");
		promotion1.setAdvId("ADV000000000A");
		promotion1.setPromotionId(1);
		Promotion promotion2 = new Promotion();
		promotion2.setAboutVideo("This is my another video");
		promotion2.setAdvId("ADV000000000B");
		promotion2.setPromotionId(2);
		promotions.add(promotion1);
		promotions.add(promotion2);

		Promotion promo = new Promotion();
		promo.setTitle("Promotion");
		promo.setAboutVideo("Video");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(promotionRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(promotionRequestValidator.validate(promotionRequest)).thenReturn(null);
		//
		when(advisorService.fetchPromotionByAdvId(Mockito.anyString())).thenReturn(promotions);
		// when(advisorService.removePromotion(Mockito.anyLong(),
		// Mockito.anyString())).thenReturn(1);
		when(advisorService.addAndModifyPromotion(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_addPromotion_Error() throws Exception {
		PromotionRequest promotionRequest = new PromotionRequest();
		promotionRequest.setAdvId("ADV000000000A");
		List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
		promotionRequest.setPromotionReq(promoReq);
		promotionRequest.setScreenId(1);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAboutVideo("This is my video");
		promotion1.setAdvId("ADV000000000A");
		Promotion promotion2 = new Promotion();
		promotion2.setAboutVideo("This is my another video");
		promotion2.setAdvId("ADV000000000B");
		promotions.add(promotion1);
		promotions.add(promotion2);

		Promotion promo = new Promotion();
		promo.setTitle("Promotion");
		promo.setAboutVideo("Video");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(promotionRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(promotionRequestValidator.validate(promotionRequest)).thenReturn(null);
		when(advisorService.fetchPromotionByAdvId(Mockito.anyString())).thenReturn(promotions);
		when(advisorService.removePromotion(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_addPromotion_ValidationError() throws Exception {
		PromotionRequest promotionRequest = new PromotionRequest();
		promotionRequest.setAdvId("ADV000000000A");
		List<PromotionReq> promoReq = new ArrayList<PromotionReq>();
		promotionRequest.setPromotionReq(promoReq);
		promotionRequest.setScreenId(1);

		List<Promotion> promotions = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAboutVideo("This is my video");
		promotion1.setAdvId("ADV000000000A");
		promotion1.setPromotionId(1);
		Promotion promotion2 = new Promotion();
		promotion2.setAboutVideo("This is my another video");
		promotion2.setAdvId("ADV000000000B");
		promotion2.setPromotionId(2);
		promotions.add(promotion1);
		promotions.add(promotion2);

		Promotion promo = new Promotion();
		promo.setTitle("Promotion");
		promo.setAboutVideo("Video");

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(promotionRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(promotionRequestValidator.validate(Mockito.any(PromotionRequest.class))).thenReturn(allErrors);
		mockMvc.perform(MockMvcRequestBuilders.post("/addPromotion").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_addAdvBrandInfo_ScreenRights_AccessDenied() throws Exception {
		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addAdvBrandInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addAdvBrandInfo_ScreenRights_UnAuthorized() throws Exception {
		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);
		mockMvc.perform(post("/addAdvBrandInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addAdvBrandInfo_ScreenRights_Success() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);

		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);

		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		List<Long> advBrandInfoListPriority = new ArrayList<>();

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.addAdvBrandInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		when(advisorService.fetchAdvBrandInfoByAdvIdAndProdId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(advBrandInfoList);
		when(advisorService.fetchPriorityByBrandIdAndAdvId(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(advBrandInfoListPriority);
		// when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(),
		// Mockito.anyInt()))
		// .thenReturn(null);
		// when(advisorService.addAdvBrandAndRank(Mockito.anyLong(), Mockito.anyInt(),
		// Mockito.anyString(),
		// Mockito.anyLong())).thenReturn(1);
		HashMap<Long, Integer> sortedBrandAndRank = new LinkedHashMap<>();
		when(advisorService.addBrandRankIntoTable(sortedBrandAndRank, "ADV000000000A", 1)).thenReturn(1);
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
		mockMvc.perform(post("/addAdvBrandInfo").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_AddAdvBrandInfo_Success() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);

		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);

		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		List<Long> advBrandInfoListPriority = new ArrayList<>();

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.addAdvBrandInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(1);
		when(advisorService.fetchAdvBrandInfoByAdvIdAndProdId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(advBrandInfoList);
		when(advisorService.fetchPriorityByBrandIdAndAdvId(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(advBrandInfoListPriority);
		// when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(),
		// Mockito.anyInt()))
		// .thenReturn(null);
		// when(advisorService.addAdvBrandAndRank(Mockito.anyLong(), Mockito.anyInt(),
		// Mockito.anyString(),
		// Mockito.anyLong())).thenReturn(1);
		HashMap<Long, Integer> sortedBrandAndRank = new LinkedHashMap<>();
		when(advisorService.addBrandRankIntoTable(sortedBrandAndRank, "ADV000000000A", 1)).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_AddAdvBrandInfo_Mandatory() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);

		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);

		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		List<Long> advBrandInfoListPriority = new ArrayList<>();

		Advisor adv = new Advisor();
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);
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

		HashMap<Long, Integer> sortedBrandAndRank = new LinkedHashMap<>();
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andReturn();
	}

	@Test
	public void test_AddAdvBrandInfo_NotFound() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);

		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_AddAdvBrandInfo_Brand() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getBrand_should_be_added())))
				.andReturn();
	}

	@Test
	public void test_AddAdvBrandRank_BrandRankAddSuccess() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);

		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setScreenId(1);
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);

		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		List<Long> advBrandInfoListPriority = new ArrayList<>();

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		AdvBrandRank brandRank = new AdvBrandRank();
		brandRank.setBrand("1");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.addAdvBrandInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(1);

		when(advisorService.fetchAdvBrandInfoByAdvIdAndProdId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(advBrandInfoList);
		when(advisorService.fetchPriorityByBrandIdAndAdvId(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(advBrandInfoListPriority);
		when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
				.thenReturn(null);
		when(advisorService.addAdvBrandAndRank(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyLong())).thenReturn(1);
		// when(advisorService.addAdvBrandAndRank(Mockito.anyLong(),Mockito.anyInt(),
		// Mockito.anyString(),
		// Mockito.anyLong())).thenReturn(1);
		HashMap<Long, Integer> sortedBrandAndRank = new LinkedHashMap<>();
		when(advisorService.addBrandRankIntoTable(sortedBrandAndRank, "ADV000000000A", 1)).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_AddAdvBrandRank_BrandRankUpdateSuccess() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);

		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);
		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		List<Long> advBrandInfoListPriority = new ArrayList<>();

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");

		AdvBrandRank brandRank = new AdvBrandRank();
		brandRank.setBrand("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.addAdvBrandInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(1);

		when(advisorService.fetchAdvBrandInfoByAdvIdAndProdId(Mockito.anyString(), Mockito.anyLong()))
				.thenReturn(advBrandInfoList);
		when(advisorService.fetchPriorityByBrandIdAndAdvId(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(advBrandInfoListPriority);
		// when(advisorService.fetchAdvBrandRank(Mockito.anyString(), Mockito.anyLong(),
		// Mockito.anyInt()))
		// .thenReturn(brandRank);
		// when(advisorService.addAdvBrandAndRank(Mockito.anyLong(),Mockito.anyInt(),
		// Mockito.anyString(),
		// Mockito.anyLong())).thenReturn(1);
		HashMap<Long, Integer> sortedBrandAndRank = new LinkedHashMap<>();
		when(advisorService.addBrandRankIntoTable(sortedBrandAndRank, "ADV000000000A", 1)).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_AddAdvBrandInfo_ErrorOccured() throws Exception {
		AdvBrandInfoReq advBrandInfoReq = new AdvBrandInfoReq();
		advBrandInfoReq.setProdId(1);
		advBrandInfoReq.setServiceId("1");
		advBrandInfoReq.setBrandId1(1);
		advBrandInfoReq.setBrandId2(2);
		advBrandInfoReq.setBrandId3(3);
		List<AdvBrandInfoReq> advBrandInfoReqList = new ArrayList<AdvBrandInfoReq>();
		advBrandInfoReqList.add(advBrandInfoReq);

		AdvBrandInfoRequest advBrandInfoRequest = new AdvBrandInfoRequest();
		advBrandInfoRequest.setAdvId("ADV000000000A");
		advBrandInfoRequest.setBrandInfoReqList(advBrandInfoReqList);
		advBrandInfoRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		AdvBrandRank brandRank = new AdvBrandRank();
		brandRank.setBrand("1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advBrandInfoRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);
		when(advisorService.addAdvBrandInfo(Mockito.anyString(), Mockito.anyList())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addAdvBrandInfo").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_fetchAdvBrandRankByAdvId_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setScreenId(1);

		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvId("ADV000000000A");
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setRanking(1);
		advBrandRankList.add(advBrandRank);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

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

		when(advisorService.fetchAdvBrandRankByAdvId(Mockito.anyString())).thenReturn(advBrandRankList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvBrandRank").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.[0].advBrandRankId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].ranking", is(1)));
	}

	@Test
	public void test_fetchAdvBrandRankByAdvId_Mandatory() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);

		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setRanking(1);
		advBrandRankList.add(advBrandRank);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

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

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvBrandRank").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));
	}

	@Test
	public void test_fetchAdvBrandRank_ScreenRights_Success() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();
		idRequest.setAdvId("ADV000000000A");
		idRequest.setScreenId(1);

		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvId("ADV000000000A");
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setRanking(1);
		advBrandRankList.add(advBrandRank);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
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
		when(advisorService.fetchAdvBrandRankByAdvId(Mockito.anyString())).thenReturn(advBrandRankList);

		mockMvc.perform(post("/fetchAdvBrandRank").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.[0].advBrandRankId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].ranking", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchAdvBrandRank_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAdvBrandRank").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAdvBrandRank_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/fetchAdvBrandRank").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_fetchAdvBrandRankByAdvId_NotFound() throws Exception {
	// AdvIdRequest advIdReq = new AdvIdRequest();
	// advIdReq.setAdvId("ADV000000000A");
	// List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
	// AdvBrandRank advBrandRank = new AdvBrandRank();
	// advBrandRank.setAdvId("ADV000000000A");
	// advBrandRank.setAdvBrandRankId(1);
	// advBrandRank.setRanking(1);
	// advBrandRankList.add(advBrandRank);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdReq);
	//
	// when(advisorService.fetchAdvBrandRankByAdvId(Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvBrandRank").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchProductList_Success() throws Exception {
		List<Product> productList = new ArrayList<Product>();
		Product product = new Product();
		product.setProdId(1);
		product.setProduct("Product");
		productList.add(product);
		when(advisorService.fetchProductList()).thenReturn(productList);
		mockMvc.perform(get("/fetch-all-product")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].prodId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].product", is("Product")));
	}

	// @Test
	// public void test_fetchProductList_NotFound() throws Exception {
	//
	// when(advisorService.fetchProductList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-product")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchRoleList_Success() throws Exception {
		List<RoleAuth> roleList = new ArrayList<RoleAuth>();
		RoleAuth role = new RoleAuth();
		role.setId(1);
		role.setName("Advisor");
		RoleAuth role2 = new RoleAuth();
		role2.setId(2);
		role2.setName("Investor");
		roleList.add(role);
		roleList.add(role2);
		when(advisorService.fetchRoleList()).thenReturn(roleList);
		mockMvc.perform(get("/fetch-all-role")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].id", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].name", is("Advisor")))
				.andExpect(jsonPath("$.responseData.data.[1].id", is(2)))
				.andExpect(jsonPath("$.responseData.data.[1].name", is("Investor")));
	}

	// @Test
	// public void test_fetchRoleList_NotFound() throws Exception {
	// when(advisorService.fetchRoleList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-role")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchPartyStatusList_Success() throws Exception {
		List<PartyStatus> partyStatusList = new ArrayList<PartyStatus>();
		PartyStatus partyStatus = new PartyStatus();
		partyStatus.setId(1);
		partyStatus.setDesc("Party Status");
		partyStatusList.add(partyStatus);
		when(advisorService.fetchPartyStatusList()).thenReturn(partyStatusList);
		mockMvc.perform(get("/fetch-all-partystatus")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].id", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].desc", is("Party Status")));
	}
	//
	// @Test
	// public void test_fetchPartyStatusList_NotFound() throws Exception {
	//
	// when(advisorService.fetchPartyStatusList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-partystatus")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchServiceList_Success() throws Exception {
		List<Service> serviceList = new ArrayList<Service>();
		Service service = new Service();
		service.setServiceId(1);
		service.setService("service");
		serviceList.add(service);
		when(advisorService.fetchServiceList()).thenReturn(serviceList);
		mockMvc.perform(get("/fetch-all-service")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].serviceId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].service", is("service")));
	}
	//
	// @Test
	// public void test_fetchServiceList_NotFound() throws Exception {
	//
	// when(advisorService.fetchServiceList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-service")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchBrandList_Success() throws Exception {
		List<Brand> brandList = new ArrayList<Brand>();
		Brand brand = new Brand();
		brand.setBrandId(2);
		brand.setBrand("LIC");
		brand.setProdId(3);
		brandList.add(brand);
		when(advisorService.fetchBrandList()).thenReturn(brandList);
		mockMvc.perform(get("/fetch-all-brand")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].brandId", is(2)))
				.andExpect(jsonPath("$.responseData.data.[0].brand", is("LIC")))
				.andExpect(jsonPath("$.responseData.data.[0].prodId", is(3)));
		verify(advisorService, times(1)).fetchBrandList();
	}

	// @Test
	// public void test_fetchBrandList_NotFound() throws Exception {
	//
	// when(advisorService.fetchBrandList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-brand")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchLicenseList_Success() throws Exception {
		List<License> licenseList = new ArrayList<License>();
		License license = new License();
		license.setLicId(1);
		license.setLicense("License");
		license.setProdId(1);
		licenseList.add(license);
		when(advisorService.fetchLicenseList()).thenReturn(licenseList);
		mockMvc.perform(get("/fetch-all-license")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].licId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].license", is("License")))
				.andExpect(jsonPath("$.responseData.data.[0].prodId", is(1)));
	}

	// @Test
	// public void test_fetchLicenseList_NotFound() throws Exception {
	//
	// when(advisorService.fetchLicenseList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-license")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchRemunerationList_Success() throws Exception {
		List<Remuneration> remunerationList = new ArrayList<Remuneration>();
		Remuneration rem1 = new Remuneration();
		rem1.setRemId(1);
		rem1.setRemuneration("Fee Based");
		Remuneration rem2 = new Remuneration();
		rem2.setRemId(2);
		rem2.setRemuneration("Commision Based");
		remunerationList.add(rem1);
		remunerationList.add(rem2);
		when(advisorService.fetchRemunerationList()).thenReturn(remunerationList);
		mockMvc.perform(get("/fetch-all-remuneration")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].remId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].remuneration", is("Fee Based")))
				.andExpect(jsonPath("$.responseData.data.[1].remId", is(2)))
				.andExpect(jsonPath("$.responseData.data.[1].remuneration", is("Commision Based")));
	}

	// @Test
	// public void test_fetchRemunerationList_NotFound() throws Exception {
	//
	// when(advisorService.fetchRemunerationList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-remuneration")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchAllServiceAndBrand_Success() throws Exception {
		List<Product> products = new ArrayList<Product>();
		Product prod1 = new Product();
		prod1.setProdId(1);
		prod1.setProduct("ABC");
		Product prod2 = new Product();
		prod2.setProdId(2);
		prod2.setProduct("DEF");
		products.add(prod1);
		products.add(prod2);
		when(advisorService.fetchAllServiceAndBrand()).thenReturn(products);
		mockMvc.perform(get("/fetch-all-productServBrand")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].prodId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].product", is("ABC")))
				.andExpect(jsonPath("$.responseData.data.[1].prodId", is(2)))
				.andExpect(jsonPath("$.responseData.data.[1].product", is("DEF")));
	}

	// @Test
	// public void test_fetchAllServiceAndBrand_NotFound() throws Exception {
	//
	// when(advisorService.fetchAllServiceAndBrand()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-productServBrand")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	// @Test
	// public void test_fetchAllStateCityPincode_Success() throws Exception {
	// List<State> stateList = new ArrayList<State>();
	// State state1 = new State();
	// state1.setStateId(1);
	// state1.setState("TamilNadu");
	// State state2 = new State();
	// state2.setStateId(2);
	// state2.setState("Kerala");
	// stateList.add(state1);
	// stateList.add(state2);
	// when(advisorService.fetchAllStateCityPincode()).thenReturn(stateList);
	// mockMvc.perform(get("/fetch-all-stateCityPincode")).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data", hasSize(2)))
	// .andExpect(jsonPath("$.responseData.data.[0].stateId", is(1)))
	// .andExpect(jsonPath("$.responseData.data.[0].state", is("TamilNadu")))
	// .andExpect(jsonPath("$.responseData.data.[1].stateId", is(2)))
	// .andExpect(jsonPath("$.responseData.data.[1].state", is("Kerala")));
	// }

	// @Test
	// public void test_fetchAllStateCityPincode_NotFound() throws Exception {
	//
	// when(advisorService.fetchAllStateCityPincode()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-stateCityPincode")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchCategoryList_Success() throws Exception {
		List<Category> category = new ArrayList<Category>();
		Category category1 = new Category();
		category1.setCategoryId(1);
		category1.setDesc("life insurance");
		Category category2 = new Category();
		category2.setCategoryId(2);
		category2.setDesc("general insurance");
		category.add(category1);
		category.add(category2);

		when(advisorService.fetchCategoryList()).thenReturn(category);
		mockMvc.perform(get("/fetch-all-Category")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].categoryId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].desc", is("life insurance")))
				.andExpect(jsonPath("$.responseData.data.[1].categoryId", is(2)))
				.andExpect(jsonPath("$.responseData.data.[1].desc", is("general insurance")));
	}

	// @Test
	// public void test_fetchCategoryList_NotFound() throws Exception {
	//
	// when(advisorService.fetchCategoryList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-Category")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchCategoryTypeList_Success() throws Exception {
		List<CategoryType> categoryTypeList = new ArrayList<CategoryType>();
		CategoryType category1 = new CategoryType();
		category1.setCategoryTypeId(1);
		category1.setDesc("investment");
		CategoryType category2 = new CategoryType();
		category2.setCategoryTypeId(2);
		category2.setDesc("accounting");
		categoryTypeList.add(category1);
		categoryTypeList.add(category2);

		when(advisorService.fetchCategoryTypeList()).thenReturn(categoryTypeList);
		mockMvc.perform(get("/fetch-all-CategoryType")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].categoryTypeId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].desc", is("investment")))
				.andExpect(jsonPath("$.responseData.data.[1].categoryTypeId", is(2)))
				.andExpect(jsonPath("$.responseData.data.[1].desc", is("accounting")));
	}

	// @Test
	// public void test_fetchCategoryTypeList_NotFound() throws Exception {
	//
	// when(advisorService.fetchCategoryTypeList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-CategoryType")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchForumCategoryList_Success() throws Exception {
		List<ForumCategory> forumCategoryList = new ArrayList<ForumCategory>();
		ForumCategory forumCategory1 = new ForumCategory();
		forumCategory1.setForumCategoryId(1);
		forumCategory1.setName("mutual funds");
		ForumCategory forumCategory2 = new ForumCategory();
		forumCategory2.setForumCategoryId(2);
		forumCategory2.setName("stock");
		forumCategoryList.add(forumCategory1);
		forumCategoryList.add(forumCategory2);
		when(advisorService.fetchForumCategoryList()).thenReturn(forumCategoryList);
		mockMvc.perform(get("/fetch-all-ForumCategory")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].forumCategoryId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].name", is("mutual funds")))
				.andExpect(jsonPath("$.responseData.data.[1].forumCategoryId", is(2)))
				.andExpect(jsonPath("$.responseData.data.[1].name", is("stock")));
	}

	// @Test
	// public void test_fetchForumCategoryList_NotFound() throws Exception {
	//
	// when(advisorService.fetchForumCategoryList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-ForumCategory")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchRiskQuestionaireList_Success() throws Exception {
		List<RiskQuestionaire> riskQuestionaires = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire risk = new RiskQuestionaire();
		risk.setQuestionId("1");
		risk.setQuestion("RiskQuestionaire");
		riskQuestionaires.add(risk);
		when(advisorService.fetchRiskQuestionaireList()).thenReturn(riskQuestionaires);
		mockMvc.perform(get("/fetch-all-RiskQuestionaire")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].questionId", is("1")))
				.andExpect(jsonPath("$.responseData.data.[0].question", is("RiskQuestionaire")));
	}

	// @Test
	// public void test_fetchRiskQuestionaireList_NotFound() throws Exception {
	//
	// when(advisorService.fetchRiskQuestionaireList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-RiskQuestionaire")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchForumSubCategoryList_Success() throws Exception {
		List<ForumSubCategory> forumSubCategoryList = new ArrayList<ForumSubCategory>();
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumCategoryId(1);
		forumSubCategory.setName("Forum SubCategory");
		forumSubCategoryList.add(forumSubCategory);
		when(advisorService.fetchForumSubCategoryList()).thenReturn(forumSubCategoryList);
		mockMvc.perform(get("/fetch-all-ForumSubCategory")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].forumCategoryId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].name", is("Forum SubCategory")));
	}
	//
	// @Test
	// public void test_fetchForumSubCategoryList_NotFound() throws Exception {
	//
	// when(advisorService.fetchForumSubCategoryList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-ForumSubCategory")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchForumStatusList_Success() throws Exception {
		List<ForumStatus> forumStatusList = new ArrayList<ForumStatus>();
		ForumStatus forumStatus = new ForumStatus();
		forumStatus.setId(2);
		forumStatus.setDesc("Forum Status");
		forumStatusList.add(forumStatus);
		when(advisorService.fetchForumStatusList()).thenReturn(forumStatusList);
		mockMvc.perform(get("/fetch-all-ForumStatus")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].id", is(2)))
				.andExpect(jsonPath("$.responseData.data.[0].desc", is("Forum Status")));
	}

	// @Test
	// public void test_fetchForumStatusList_NotFound() throws Exception {
	//
	// when(advisorService.fetchForumStatusList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-ForumStatus")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchArticleStatusList_Success() throws Exception {
		List<ArticleStatus> articleStatusList = new ArrayList<ArticleStatus>();
		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(2);
		articleStatus.setDesc("inactive");
		articleStatusList.add(articleStatus);
		when(advisorService.fetchArticleStatusList()).thenReturn(articleStatusList);
		mockMvc.perform(get("/fetch-all-articleStatus")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].id", is(2)))
				.andExpect(jsonPath("$.responseData.data.[0].desc", is("inactive")));
	}

	// @Test
	// public void test_fetchArticleStatusList_NotFound() throws Exception {
	//
	// when(advisorService.fetchArticleStatusList()).thenReturn(null);
	// mockMvc.perform(get("/fetch-all-articleStatus")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_uploadFile() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
		String url = "URL";
		when(amazonClient.uploadFile(file)).thenReturn(url);
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(multipart("/uploadFile").file(file)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())));
	}

	// public void test_uploadFile_Mandatory() throws Exception {
	// MockMvc mockMvc =
	// MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	// mockMvc.perform(multipart("/uploadFile").file(null)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getMandatory_fields_file())));
	// }

	@Test
	public void test_keyPeopleSignup_Success() throws Exception {
		KeyPeopleRequest req = new KeyPeopleRequest();
		req.setFullName("david");
		req.setDesignation("backendDeveloper");
		req.setImage("aaa.jpg");
		req.setParentPartyId(1);
		req.setScreenId(1);
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(req);
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

		when(keyPeopleRequestValidator.validate(req)).thenReturn(null);
		when(advisorService.addKeyPeople(Mockito.any(KeyPeople.class))).thenReturn(result);
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getKeyPeople_added_successfully())))
				.andReturn();

	}

	@Test
	public void test_keyPeopleSignup_ScreenRights_Success() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setScreenId(1);
		keyPeopleRequest.setFullName("david");
		keyPeopleRequest.setDesignation("backendDeveloper");
		keyPeopleRequest.setImage("aaa.jpg");
		keyPeopleRequest.setParentPartyId(1);
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
		when(keyPeopleRequestValidator.validate(keyPeopleRequest)).thenReturn(null);
		when(advisorService.addKeyPeople(Mockito.any(KeyPeople.class))).thenReturn(result);
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
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getKeyPeople_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_keyPeopleSignup_ScreenRights_AccessDenied() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_keyPeopleSignup_ScreenRights_UnAuthorized() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_keyPeopleSignup_Error() throws Exception {
		KeyPeopleRequest req = new KeyPeopleRequest();
		req.setFullName("david");
		req.setDesignation("backendDeveloper");
		req.setImage("aaa.jpg");
		req.setParentPartyId(1);
		req.setScreenId(1);
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(req);
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

		when(keyPeopleRequestValidator.validate(req)).thenReturn(null);
		when(advisorService.addKeyPeople(Mockito.any(KeyPeople.class))).thenReturn(result);
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_keyPeopleSignup_Empty() throws Exception {
		KeyPeopleRequest req = new KeyPeopleRequest();
		req.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(req);
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

		when(keyPeopleRequestValidator.validate(req)).thenReturn(null);
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_key())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_keyPeopleSignup_ValidationError() throws Exception {
		KeyPeopleRequest req = new KeyPeopleRequest();
		req.setFullName("123");
		req.setDesignation("123");
		req.setImage("aaa.jpg");
		req.setParentPartyId(1);
		req.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(req);

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

		when(keyPeopleRequestValidator.validate(Mockito.any(KeyPeopleRequest.class))).thenReturn(allErrors);
		mockMvc.perform(post("/keyPeopleSignup").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", is(allErrors))).andExpect(status().isInternalServerError())
				.andReturn();

	}

	@Test
	public void test_modifyKeyPeople_Success() throws Exception {

		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setKeyPeopleId(1);
		keyPeopleRequest.setFullName("Key People");
		keyPeopleRequest.setParentPartyId(1);
		keyPeopleRequest.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(keyPeopleRequestValidator.validate(keyPeopleRequest)).thenReturn(null);
		when(advisorService.modifyKeyPeople(Mockito.anyLong(), Mockito.any(KeyPeople.class))).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyKeyPeople_ScreenRights_Success() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setScreenId(1);
		keyPeopleRequest.setKeyPeopleId(1);
		keyPeopleRequest.setFullName("Key People");
		keyPeopleRequest.setParentPartyId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(keyPeopleRequestValidator.validate(keyPeopleRequest)).thenReturn(null);
		when(advisorService.modifyKeyPeople(Mockito.anyLong(), Mockito.any(KeyPeople.class))).thenReturn(1);
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
		mockMvc.perform(put("/modifyKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_modifyKeyPeople_ScreenRights_AccessDenied() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/modifyKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyKeyPeople_ScreenRights_UnAuthorized() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
		mockMvc.perform(put("/modifyKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyKeyPeople_NotFound() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setKeyPeopleId(1);
		keyPeopleRequest.setFullName("Key People");
		keyPeopleRequest.setParentPartyId(1);
		keyPeopleRequest.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
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
		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_modifyKeyPeople_ErrorOccured() throws Exception {
		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setKeyPeopleId(1);
		keyPeopleRequest.setFullName("Key People");
		keyPeopleRequest.setParentPartyId(1);
		keyPeopleRequest.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("advisor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(keyPeopleRequestValidator.validate(keyPeopleRequest)).thenReturn(null);

		when(advisorService.modifyKeyPeople(Mockito.anyLong(), Mockito.any(KeyPeople.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyKeyPeople_ValidationError() throws Exception {

		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setKeyPeopleId(1);
		keyPeopleRequest.setFullName("Key People");
		keyPeopleRequest.setParentPartyId(1);
		keyPeopleRequest.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(keyPeopleRequestValidator.validate(Mockito.any(KeyPeopleRequest.class))).thenReturn(allErrors);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	@Test
	public void test_removeKeyPeople_Success() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setId(1);
		idReq.setScreenId(1);

		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("XYZ");
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(advisorService.removeKeyPeople(Mockito.anyLong())).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(post("/removeKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeKeyPeople_ScreenRights_Success() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setId(1);
		idReq.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("XYZ");
		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(advisorService.removeKeyPeople(Mockito.anyLong())).thenReturn(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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
		mockMvc.perform(post("/removeKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getAdvisor_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeKeyPeople_ScreenRights_AccessDenied() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);

		mockMvc.perform(post("/removeKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeKeyPeople_ScreenRights_UnAuthorized() throws Exception {
		IdRequest idReq = new IdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(post("/removeKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removeKeyPeople_NotFound() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setScreenId(1);
		idReq.setId(1);
		idReq.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("XYZ");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_removeKeyPeople_ErrorOccured() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setScreenId(1);
		idReq.setId(1);

		KeyPeople key = new KeyPeople();
		key.setKeyPeopleId(1);
		key.setFullName("XYZ");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(advisorService.removeKeyPeople(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_fetchKeyPeopleByParentId_Success() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setId(1);
		idReq.setScreenId(1);
		List<KeyPeople> key = new ArrayList<KeyPeople>();
		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setParentPartyId(1);
		keyPeople.setFullName("david");
		keyPeople.setDesignation("aaa");
		keyPeople.setImage("aaa.jpg");
		key.add(keyPeople);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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

		when(advisorService.fetchKeyPeopleByParentId(Mockito.anyLong())).thenReturn(key);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchkeyPeopleByParentId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data[0].fullName", is("david")))
				.andExpect(jsonPath("$.responseData.data[0].designation", is("aaa")))
				.andExpect(jsonPath("$.responseData.data[0].image", is("aaa.jpg")));

	}

	@Test
	public void test_fetchkeyPeopleByParentId_ScreenRights_Success() throws Exception {
		IdRequest idRequest = new IdRequest();
		idRequest.setScreenId(1);
		idRequest.setId(1);

		List<KeyPeople> key = new ArrayList<KeyPeople>();
		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setParentPartyId(1);
		keyPeople.setFullName("david");
		keyPeople.setDesignation("aaa");
		keyPeople.setImage("aaa.jpg");
		key.add(keyPeople);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(advisorService.fetchKeyPeopleByParentId(Mockito.anyLong())).thenReturn(key);
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
		mockMvc.perform(post("/fetchkeyPeopleByParentId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data[0].fullName", is("david")))
				.andExpect(jsonPath("$.responseData.data[0].designation", is("aaa")))
				.andExpect(jsonPath("$.responseData.data[0].image", is("aaa.jpg")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchkeyPeopleByParentId_ScreenRights_AccessDenied() throws Exception {
		IdRequest idRequest = new IdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchkeyPeopleByParentId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchkeyPeopleByParentId_ScreenRights_UnAuthorized() throws Exception {
		IdRequest idReq = new IdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(post("/fetchkeyPeopleByParentId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_teamMemberDeactivate_Success() throws Exception {
		AdvIdRequest idReq = new AdvIdRequest();
		idReq.setAdvId("ADV000000000A");
		idReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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
		when(advisorService.checkAdvisorIsPresent("ADV000000000A")).thenReturn(1);
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.teamMemberDeactivate(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

		mockMvc.perform(post("/teamMemberDeactivate").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getTeam_Member_Deactivated())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_teamMemberDeactivate_Error() throws Exception {
		AdvIdRequest idReq = new AdvIdRequest();
		idReq.setAdvId("ADV000000000A");
		idReq.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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
		when(advisorService.checkAdvisorIsPresent("ADV000000000A")).thenReturn(1);
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.teamMemberDeactivate(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);

		mockMvc.perform(post("/teamMemberDeactivate").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_teamMemberDeactivate_ScreenRights_Success() throws Exception {
		AdvIdRequest idReq = new AdvIdRequest();
		idReq.setAdvId("ADV000000000A");
		idReq.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		when(advisorService.checkAdvisorIsPresent("ADV000000000A")).thenReturn(1);
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.teamMemberDeactivate(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
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
		mockMvc.perform(post("/teamMemberDeactivate").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getTeam_Member_Deactivated())))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void test_teamMemberDeactivate_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/teamMemberDeactivate").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_teamMemberDeactivate_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/teamMemberDeactivate").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_workFlowStatus_Success() throws Exception {

		StatusRequest statusRequest = new StatusRequest();
		statusRequest.setAdvId("ADV0000000001");
		statusRequest.setStatus(4);
		statusRequest.setScreenId(1);
		statusRequest.setReason("reason");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.addWorkFlowStatusByAdvId(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(result);
		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getWorkFlowStatus_added_asApproved_successfully())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_workFlowStatus_ScreenRights_Success() throws Exception {

		StatusRequest statusRequest = new StatusRequest();
		statusRequest.setScreenId(1);
		statusRequest.setAdvId("ADV0000000001");
		statusRequest.setStatus(4);
		statusRequest.setReason("reason");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.addWorkFlowStatusByAdvId(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(result);
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
		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getWorkFlowStatus_added_asApproved_successfully())))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	public void test_workFlowStatus_ScreenRights_AccessDenied() throws Exception {

		StatusRequest statusRequest = new StatusRequest();
		statusRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);

		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_workFlowStatus_ScreenRights_UnAuthorized() throws Exception {
		StatusRequest statusRequest = new StatusRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);
		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_workFlowStatus_Error() throws Exception {

		StatusRequest statusRequest = new StatusRequest();
		statusRequest.setAdvId("ADV0000000001");
		statusRequest.setStatus(4);
		statusRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 0;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.addWorkFlowStatusByAdvId(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(result);
		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_workFlowStatus_NoRecordFound() throws Exception {

		StatusRequest statusRequest = new StatusRequest();
		statusRequest.setStatus(4);
		statusRequest.setAdvId("ADV0000000000");
		statusRequest.setScreenId(1);
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
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_workFlowStatus_FieldsCannotEmpty() throws Exception {

		StatusRequest statusRequest = new StatusRequest();
		// statusRequest.setAdvId("ADV0000000001");
		statusRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

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

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(statusRequest);
		// when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/workFlowStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_status())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_fetchAllApprovedAdv_Success() throws Exception {
		List<Advisor> advisors = new ArrayList<Advisor>();

		Advisor adv1 = new Advisor();
		adv1.setAdvId(("ADV000000000A"));
		adv1.setWorkFlowStatus(4);
		Advisor adv2 = new Advisor();
		adv2.setAdvId("ADV000000000B");
		adv2.setWorkFlowStatus(4);
		advisors.add(adv1);
		advisors.add(adv2);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(advisorService.fetchApprovedAdv(Mockito.anyInt(), Mockito.anyInt())).thenReturn(advisors);
		mockMvc.perform(post("/fetchAllApprovedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].workFlowStatus", is(4)))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].advId", is("ADV000000000B")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].workFlowStatus", is(4)));

	}

	@Test
	public void test_fetchAllApprovedAdv_Error() throws Exception {
		List<Advisor> advisors = new ArrayList<Advisor>();

		when(advisorService.fetchApprovedAdv(Mockito.anyInt(), Mockito.anyInt())).thenReturn(advisors);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(advisorService.fetchApprovedAdv(Mockito.anyInt(), Mockito.anyInt())).thenReturn(advisors);
		mockMvc.perform(post("/fetchAllApprovedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(0)));

	}

	@Test
	public void test_fetchAllApprovedAdv_ScreenRights_Success() throws Exception {

		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<Advisor> advisors = new ArrayList<Advisor>();

		Advisor adv1 = new Advisor();
		adv1.setAdvId(("ADV000000000A"));
		adv1.setWorkFlowStatus(4);
		Advisor adv2 = new Advisor();
		adv2.setAdvId("ADV000000000B");
		adv2.setWorkFlowStatus(4);
		advisors.add(adv1);
		advisors.add(adv2);

		when(advisorService.fetchApprovedAdv(Mockito.anyInt(), Mockito.anyInt())).thenReturn(advisors);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
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

		mockMvc.perform(post("/fetchAllApprovedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].workFlowStatus", is(4)))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].advId", is("ADV000000000B")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].workFlowStatus", is(4)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchAllApprovedAdv_ScreenRights_AccessDenied() throws Exception {

		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAllApprovedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAllApprovedAdv_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchAllApprovedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addFollowers_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		List<Followers> followers = new ArrayList<Followers>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkReFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.fetchUnFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.addFollowers(Mockito.anyString(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFollowers_added_successfully())))
				.andReturn();

	}

	@Test
	public void test_addFollowers_ScreenRights_AccessDenied() throws Exception {
		FollowerRequest screenIdRequest = new FollowerRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addFollowers_ScreenRights_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		List<Followers> followers = new ArrayList<Followers>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkReFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.addFollowers(Mockito.anyString(), Mockito.anyString())).thenReturn(result);

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
		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFollowers_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_addFollowers_ScreenRights_UnAuthorized() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addFollowers_Error() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		List<Followers> followers = new ArrayList<Followers>();

		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 0;

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkReFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.fetchUnFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.addFollowers(Mockito.anyString(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addFollowers_CannotFollowInv() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("INV0000000001");
		followerRequest.setUserId("INV0000000002");
		followerRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCannot_follow_the_investor())))
				.andReturn();

	}

	@Test
	public void test_addFollowers_NoRecordFound() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addFollowers_SameFollowerError() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000001");
		followerRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getCannot_follow_the_user())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addFollowers_FollowerAlreadyPresent() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<Followers> followers = new ArrayList<Followers>();
		Followers follower = new Followers();
		follower.setUserId("ADV000000000A");
		followers.add(follower);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFollowers_already_present())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addFollowers_ReFollowerAlreadyPresent() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<Followers> followersList = new ArrayList<Followers>();
		List<Followers> followers = new ArrayList<Followers>();
		Followers follower = new Followers();
		follower.setUserId("INV0000000001");
		follower.setStatus(4);
		followers.add(follower);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkReFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFollowers_already_present())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addFollowers_updateFollowers_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<Followers> followers = new ArrayList<Followers>();
		List<Followers> followers1 = new ArrayList<Followers>();

		Followers follower = new Followers();
		follower.setStatus(3);
		follower.setFollowersId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkReFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(follower);
		when(advisorService.updateFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRefollowers_added_successfully())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_addFollowers_UnFollowers_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("INV0000000001");
		followerRequest.setScreenId(1);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		List<Followers> followers = new ArrayList<Followers>();
		List<Followers> followers1 = new ArrayList<Followers>();

		Followers follower = new Followers();
		follower.setStatus(5);
		follower.setFollowersId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkReFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.fetchUnFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(follower);
		when(advisorService.updateUnFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFollowers_added_successfully())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_blockFollowers_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);
		followerRequest.setBlockedBy("ADV0000000001");

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.blockFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getFollower_blocked())))
				.andReturn();

	}

	@Test
	public void test_blockFollowers_ScreenRights_Success() throws Exception {

		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setBlockedBy("ADV0000000001");

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.blockFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(result);
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
		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getFollower_blocked())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_blockFollowers_ScreenRights_AccessDenied() throws Exception {

		StatusRequest screenIdRequest = new StatusRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_blockFollowers_ScreenRights_UnAuthorized() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_blockFollowers_Error() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);
		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.blockFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();

	}

	@Test
	public void test_blockFollowers_NoRecordFound() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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
		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_approveFollowers_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setFollowersId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.approveFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getFollower_approved())))
				.andReturn();
	}

	@Test
	public void test_approveFollowers_Error() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setFollowersId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.approveFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_approveFollowers_NoRecordFound() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setFollowersId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_approveFollowers_ScreenRights_Success() throws Exception {

		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.approveFollowers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
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
		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getFollower_approved())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_approveFollowers_ScreenRights_AccessDenied() throws Exception {

		StatusRequest screenIdRequest = new StatusRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_approveFollowers_ScreenRights_UnAuthorized() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchAdvisorType_Success() throws Exception {
		List<AdvisorType> advisorTypeList = new ArrayList<AdvisorType>();
		AdvisorType advisorType = new AdvisorType();
		advisorType.setId(1);
		advisorType.setAdvType("individual");
		advisorTypeList.add(advisorType);
		when(advisorService.fetchAdvisorTypeList()).thenReturn(advisorTypeList);
		mockMvc.perform(get("/fetch-all-advisorType")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].id", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].advType", is("individual")));

	}

	@Test
	public void test_fetchUserType_Success() throws Exception {
		List<UserType> userTypeList = new ArrayList<UserType>();
		UserType userType = new UserType();
		userType.setId(1);
		userType.setDesc("advisor");
		userTypeList.add(userType);
		when(advisorService.fetchUserTypeList()).thenReturn(userTypeList);
		mockMvc.perform(get("/fetch-all-userType")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].id", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].desc", is("advisor")));

	}

	@Test
	public void test_addChat_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000002");

		List<ChatUser> chatUserList = new ArrayList<ChatUser>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkInActiveChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);

		// when(advisorService.checkReChatUserIsPresent(Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedChatUsersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.addChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_added_successfully())))
				.andReturn();

	}

	@Test
	public void test_addChat_ScreenRights_AccessDenied() throws Exception {
		ChatRequest screenIdRequest = new ChatRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_addChat_ScreenRights_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkInActiveChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedChatUsersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.addChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(result);

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
		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_addChat_ScreenRights_UnAuthorized() throws Exception {
		ChatRequest chatRequest = new ChatRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addChat_Error() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();

		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 0;

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkInActiveChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedChatUsersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		when(advisorService.addChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addChat_CannotChatInv() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("INV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCannot_follow_the_investor())))
				.andReturn();

	}

	@Test
	public void test_addChat_NoRecordFound() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addChat_SameFollowerError() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000001");
		chatRequest.setScreenId(1);

		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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
		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getCannot_follow_the_user())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addChat_ChatUserAlreadyPresent() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);
		chatUser.setUserId("INV0000000001");
		chatUsers.add(chatUser);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_already_present())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addChat_InActiveChatUserAlreadyPresent() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(2);
		chatUser.setUserId("INV0000000001");
		chatUsers.add(chatUser);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkInActiveChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_already_present())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_addChat_BlockedChatUserAlreadyPresent_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(3);
		chatUser.setUserId("INV0000000001");
		chatUsers.add(chatUser);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkInActiveChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedChatUsersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(chatUser);
		when(advisorService.updateChatUser(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_updated_successfully())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_addChat_BlockedChatUserAlreadyPresent_Error() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setScreenId(1);
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(3);
		chatUser.setUserId("INV0000000001");
		chatUsers.add(chatUser);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.checkChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.checkInActiveChatUserIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(advisorService.fetchBlockedChatUsersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(chatUser);
		when(advisorService.updateChatUser(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	public void test_blockChat_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000002");
		chatRequest.setScreenId(1);
		chatRequest.setBlockedBy("ADV0000000001");
		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchActiveChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(chatUser);
		when(advisorService.blockChat(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_blocked_successfully())))
				.andReturn();
	}

	@Test
	public void test_blockChat_Error() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000002");
		chatRequest.setBlockedBy("ADV0000000001");
		chatRequest.setScreenId(1);

		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchActiveChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(chatUser);
		when(advisorService.blockChat(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_blockChat_NoRecordFound() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000002");
		chatRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchActiveChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_blockChat_ScreenRights_AccessDenied() throws Exception {
		ChatRequest screenIdRequest = new ChatRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_blockChat_ScreenRights_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");
		chatRequest.setBlockedBy("ADV0000000001");

		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchActiveChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(chatUser);
		when(advisorService.blockChat(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_blocked_successfully())));
	}

	@Test
	public void test_blockChat_ScreenRights_UnAuthorized() throws Exception {
		ChatRequest chatRequest = new ChatRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchFollowersListByUserId_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setUserId("ADV000000000B");
		followerRequest.setAdvId("ADV000000000A");
		followerRequest.setScreenId(1);
		List<Followers> followers = new ArrayList<Followers>();
		Followers follower = new Followers();
		follower.setUserId("ADV000000000A");
		followers.add(follower);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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
		when(advisorService.checkFollowersIsPresent(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchFollowersListByUserId(Mockito.anyString())).thenReturn(followers);
		mockMvc.perform(post("/fetchFollowersListByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchFollowersListByUserId_ScreenRights_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);
		followerRequest.setUserId("ADV000000000B");
		followerRequest.setAdvId("ADV000000000A");

		List<Followers> followers = new ArrayList<Followers>();
		Followers follower = new Followers();
		follower.setUserId("ADV000000000A");
		followers.add(follower);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

		when(advisorService.fetchFollowersListByUserId(Mockito.anyString())).thenReturn(followers);
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
		mockMvc.perform(post("/fetchFollowersListByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	// @Test //200 error//
	// public void test_fetchFollowersListByUserId_ScreenRights_AccessDenied()
	// throws Exception {
	// FollowerRequest idRequest = new FollowerRequest();
	// idRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(idRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/fetchFollowersListByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	@Test
	public void test_fetchFollowersListByUserId_ScreenRights_UnAuthorized() throws Exception {
		FollowerRequest idRequest = new FollowerRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/fetchFollowersListByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test // is Equals condition
	// public void test_sendOtp_Success() throws Exception {
	// OtpRequest otpRequest = new OtpRequest();
	// otpRequest.setPhoneNumber("9999999999");
	// otpRequest.setOtp("857655");
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPhoneNumber("9999999999");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(otpRequest);
	// when(advisorService.fetchPartyByPhoneNumberAndDeleteFlag(Mockito.anyString())).thenReturn(party);
	// when(sendSms.sendSms(Mockito.anyList(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn("success");
	// when(advisorService.addOtpForPhoneNumber(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/sendOtp").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$", is("success")));
	// }

	@Test
	public void test_sendOtp_NotFound() throws Exception {
		OtpRequest otpRequest = new OtpRequest();
		otpRequest.setPhoneNumber("9999999999");
		otpRequest.setOtp("857655");
		Party party = new Party();
		party.setPartyId(1);
		party.setPhoneNumber("9999999999");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(otpRequest);
		when(advisorService.fetchPartyByPhoneNumberAndDeleteFlag(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/sendOtp").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_verifyOtp_Success() throws Exception {
		OtpRequest otpRequest = new OtpRequest();
		otpRequest.setPhoneNumber("9999999999");
		otpRequest.setOtp("857655");
		Party party = new Party();
		party.setPartyId(1);
		party.setPhoneNumber("9999999999");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		GeneratedOtp generatedOtp = new GeneratedOtp();
		generatedOtp.setPartyId(1);
		generatedOtp.setOtp("857655");
		generatedOtp.setCreated(timestamp);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(otpRequest);

		when(advisorService.fetchGeneratedOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(generatedOtp);
		when(advisorService.fetchPartyByPhoneNumberAndDeleteFlag(Mockito.anyString())).thenReturn(party);
		when(advisorService.validateOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/verifyOtp").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getOtp_verified())))
				.andReturn();
	}

	@Test
	public void test_verifyOtp_Vaidity_Error() throws Exception {
		OtpRequest otpRequest = new OtpRequest();
		otpRequest.setPhoneNumber("9999999999");
		otpRequest.setOtp("857655");
		Party party = new Party();
		party.setPartyId(1);
		party.setPhoneNumber("9999999999");
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		GeneratedOtp generatedOtp = new GeneratedOtp();
		generatedOtp.setPartyId(1);
		generatedOtp.setOtp("857655");
		generatedOtp.setCreated(new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(60)));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(otpRequest);

		when(advisorService.fetchGeneratedOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(generatedOtp);
		when(advisorService.fetchPartyByPhoneNumberAndDeleteFlag(Mockito.anyString())).thenReturn(party);
		when(advisorService.validateOtp(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/verifyOtp").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getOtp_expired())))
				.andReturn();
	}

	@Test
	public void test_verifyOtp_NotFound() throws Exception {
		OtpRequest otpRequest = new OtpRequest();
		otpRequest.setPhoneNumber("9999999999");
		otpRequest.setOtp("857655");
		Party party = new Party();
		party.setPartyId(1);
		party.setPhoneNumber("9999999999");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(otpRequest);

		when(advisorService.fetchPartyByPhoneNumberAndDeleteFlag(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/verifyOtp").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_fetchFollowersCount_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setScreenId(1);
		List<Integer> advList = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		advList.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchFollowersCount(Mockito.anyString())).thenReturn(advList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchFollowersCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(1)));

	}

	@Test
	public void test_fetchFollowersCount_ScreenRights_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		advIdReq.setAdvId("ADV000000000A");
		List<Integer> advList = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		advList.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchFollowersCount(Mockito.anyString())).thenReturn(advList);
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
		mockMvc.perform(post("/fetchFollowersCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchFollowersCount_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchFollowersCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchFollowersCount_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/fetchFollowersCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchFollowersCount_NotFound() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setScreenId(1);
		List<Integer> advList = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		advList.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchFollowersCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_fetchFollowersByAdvId_Success() throws Exception {
		FollowerRequest advIdReq = new FollowerRequest();
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setScreenId(1);
		List<Followers> followersList = new ArrayList<Followers>();
		Followers followers = new Followers();
		followers.setStatus(1);
		followers.setAdvId("ADV000000000A");
		followersList.add(followers);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

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

		when(advisorService.fetchFollowersByAdvId(Mockito.anyString())).thenReturn(followersList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchFollowersByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)));
		// .andExpect(jsonPath("$.responseData.data.investors", hasSize(1)));

	}

	@Test
	public void test_fetchFollowersByAdvId_ScreenRights_Success() throws Exception {
		FollowerRequest advIdReq = new FollowerRequest();
		advIdReq.setScreenId(1);
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setStatusId(1L);
		List<Followers> followersList = new ArrayList<Followers>();
		Followers followers = new Followers();
		followers.setStatus(1);
		followers.setAdvId("ADV000000000A");
		followersList.add(followers);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

		when(advisorService.fetchFollowersByAdvId(Mockito.anyString())).thenReturn(followersList);
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
		mockMvc.perform(post("/fetchFollowersByAdvId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				// .andExpect(jsonPath("$.responseData.data.investors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchFollowersByAdvId_ScreenRights_AccessDenied() throws Exception {
		FollowerRequest idRequest = new FollowerRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchFollowersByAdvId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchFollowersByAdvId_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/fetchFollowersByAdvId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchFollowerStatusList_Success() throws Exception {
		List<FollowerStatus> followerStatusList = new ArrayList<FollowerStatus>();
		FollowerStatus followerStatus = new FollowerStatus();
		followerStatus.setFollowerStatusId(1);
		followerStatus.setStatus("active");
		followerStatusList.add(followerStatus);

		when(advisorService.fetchFollowerStatusList()).thenReturn(followerStatusList);
		mockMvc.perform(get("/fetch-all-followerStatus")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].followerStatusId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].status", is("active")));

	}

	@Test
	public void test_fetchWorkFlowStatusList_Success() throws Exception {
		List<WorkFlowStatus> workFlowStatusList = new ArrayList<WorkFlowStatus>();
		WorkFlowStatus workFlowStatus = new WorkFlowStatus();
		workFlowStatus.setWorkFlowId(1);
		workFlowStatus.setStatus("active");
		workFlowStatusList.add(workFlowStatus);
		when(advisorService.fetchWorkFlowStatusList()).thenReturn(workFlowStatusList);
		mockMvc.perform(get("/fetch-all-workFlowStatus")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].workFlowId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].status", is("active")));

	}

	@Test
	public void test_exploreAdvisor_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		adv.setEmailId("adv@gmail.com");
		adv.setState("tamilnadu");
		adv.setCity("chennai");
		adv.setPincode("624145");
		adv.setDisplayName("adv");
		advisorList.add(adv);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
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

		when(advisorService.fetchExploreAdvisorDESCListOrder(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);
		when(advisorService.fetchExploreAdvisorList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);

		mockMvc.perform(MockMvcRequestBuilders.post("/exploreAdvisor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].emailId", is("adv@gmail.com")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("advisor")));
	}

	@Test
	public void test_exploreAdvisor_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/exploreAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_exploreAdvisor_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/exploreAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_exploreAdvisor_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv1 = new Advisor();
		adv1.setAdvId(("ADV000000000A"));
		adv1.setName("AAA");
		Advisor adv2 = new Advisor();
		adv2.setAdvId("ADV000000000B");
		adv2.setName("BBB");
		advisors.add(adv1);
		advisors.add(adv2);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		// when(advisorService.fetchAdvisorList(Mockito.anyInt(),
		// Mockito.anyInt())).thenReturn(advisors);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(advisorService.fetchExploreAdvisorDESCListOrder(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisors);
		when(advisorService.fetchExploreAdvisorList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisors);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/exploreAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("AAA")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].advId", is("ADV000000000B")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].name", is("BBB")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_exploreProduct_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);

		List<Product> productList = new ArrayList<Product>();
		Product product = new Product();
		product.setProdId(1);
		product.setProduct("Product");
		productList.add(product);

		List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
		ServicePlan servicePlan = new ServicePlan();
		servicePlan.setBrandId(1);
		servicePlan.setProdId(1);
		servicePlan.setServiceId(1);
		servicePlan.setServicePlan("Equity");
		servicePlan.setServicePlanId(1);
		servicePlan.setServicePlanLink("servicePlanLink");
		servicePlanList.add(servicePlan);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
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

		when(advisorService.fetchExploreProductList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(servicePlanList);

		mockMvc.perform(MockMvcRequestBuilders.post("/exploreProduct").content(jsonString).param("productName", "prod")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.servicePlanList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.servicePlanList.[0].prodId", is(1)))
				.andExpect(jsonPath("$.responseData.data.servicePlanList.[0].servicePlanLink", is("servicePlanLink")));
	}

	@Test
	public void test_exploreProduct_Mandatory() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);

		List<Product> productList = new ArrayList<Product>();
		Product product = new Product();
		productList.add(product);

		List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
		ServicePlan servicePlan = new ServicePlan();
		servicePlanList.add(servicePlan);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
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

		mockMvc.perform(MockMvcRequestBuilders.post("/exploreProduct").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_explore())));
	}

	@Test
	public void test_exploreProduct_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/exploreProduct").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_exploreProduct_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/exploreProduct").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_exploreProduct_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Product> productList = new ArrayList<Product>();
		Product product1 = new Product();
		product1.setProdId(1);
		product1.setProduct("Product");
		Product product2 = new Product();
		product2.setProdId(2);
		product2.setProduct("Second Product");
		productList.add(product1);
		productList.add(product2);

		List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
		ServicePlan servicePlan = new ServicePlan();
		servicePlan.setBrandId(1);
		servicePlan.setProdId(1);
		servicePlan.setServiceId(1);
		servicePlan.setServicePlan("Equity");
		servicePlan.setServicePlanId(1);
		servicePlan.setServicePlanLink("servicePlanLink");
		servicePlanList.add(servicePlan);
		ServicePlan servicePlan1 = new ServicePlan();
		servicePlan1.setBrandId(2);
		servicePlan1.setProdId(2);
		servicePlan1.setServiceId(2);
		servicePlan1.setServicePlan("Equity One");
		servicePlan1.setServicePlanId(2);
		servicePlan1.setServicePlanLink("servicePlanLink One");
		servicePlanList.add(servicePlan1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(advisorService.fetchExploreProductList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(servicePlanList);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/exploreProduct").content(jsonString).contentType(MediaType.APPLICATION_JSON)
				.param("productName", "prod")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.servicePlanList", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.servicePlanList.[0].servicePlan", is("Equity")))
				.andExpect(jsonPath("$.responseData.data.servicePlanList.[0].servicePlanLink", is("servicePlanLink")))
				.andExpect(jsonPath("$.responseData.data.servicePlanList.[1].servicePlan", is("Equity One")))
				.andExpect(jsonPath("$.responseData.data.servicePlanList.[1].servicePlanId", is(2)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_exploreAdvisorByProduct_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);

		List<Advisor> advisorList = new ArrayList<Advisor>();
		List<AdvProduct> productList = new ArrayList<AdvProduct>();
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setProdId(1);
		productList.add(advProduct);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		adv.setEmailId("adv@gmail.com");
		adv.setState("tamilnadu");
		adv.setCity("chennai");
		adv.setPincode("624145");
		adv.setDisplayName("adv");
		adv.setAdvProducts(productList);
		advisorList.add(adv);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
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

		List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
		ServicePlan servicePlan = new ServicePlan();
		servicePlan.setBrandId(1);
		servicePlan.setProdId(1);
		servicePlan.setServiceId(1);
		servicePlan.setServicePlan("Equity");
		servicePlan.setServicePlanId(1);
		servicePlan.setServicePlanLink("servicePlanLink");
		servicePlanList.add(servicePlan);
		List<String> pincodeList = new ArrayList<String>();
		pincodeList.add("627001");
		pincodeList.add("627002");
		List<String> stateCityPincodeList = new ArrayList<String>();
		stateCityPincodeList.add("627003");
		stateCityPincodeList.add("627004");

		when(advisorService.fetchPincodeByState(Mockito.anyString())).thenReturn(pincodeList);
		when(advisorService.fetchPincodeByStateAndCity(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(pincodeList);
		when(advisorService.fetchPincodeListByPincode(Mockito.anyString())).thenReturn(pincodeList);
		when(advisorService.fetchExploreProductListById(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(servicePlanList);
		when(advisorService.fetchExploreAdvisorListByProduct(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
						.thenReturn(advisorList);
		when(advisorService.fetchTotalExploreAdvisorByProduct(Mockito.anyList(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchExploreAdvisorListByProductWithoutBrand(Mockito.anyInt(), Mockito.anyInt(),
				Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
						.thenReturn(advisorList);
		when(advisorService.fetchTotalExploreAdvisorByProductWithoutBrandId(Mockito.anyList(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchExploreAdvisorDESCListOrder(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);
		when(advisorService.fetchTotalExploreAdvisorList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(1);

		// when(advisorService.fetchExploreAdvisorByProduct(Mockito.anyInt(),
		// Mockito.anyInt(), Mockito.anyString(),
		// Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
		// Mockito.anyString(), Mockito.anyString(),
		// Mockito.anyString())).thenReturn(advisorList);
		mockMvc.perform(MockMvcRequestBuilders.post("/exploreAdvisorByProduct").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].emailId", is("adv@gmail.com")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("advisor")));
	}

	@Test
	public void test_exploreAdvisorByProduct_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/exploreAdvisorByProduct").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_exploreAdvisorByProduct_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/exploreAdvisorByProduct").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_exploreAdvisorByProduct_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Advisor> advisorList = new ArrayList<Advisor>();
		List<AdvProduct> productList = new ArrayList<AdvProduct>();
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setAdvProdId(1);
		advProduct.setProdId(1);
		productList.add(advProduct);
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		adv.setEmailId("adv@gmail.com");
		adv.setState("tamilnadu");
		adv.setCity("chennai");
		adv.setPincode("624145");
		adv.setDisplayName("adv");
		adv.setAdvProducts(productList);
		advisorList.add(adv);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		// when(advisorService.fetchAdvisorList(Mockito.anyInt(),
		// Mockito.anyInt())).thenReturn(advisors);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
		ServicePlan servicePlan = new ServicePlan();
		servicePlan.setBrandId(1);
		servicePlan.setProdId(1);
		servicePlan.setServiceId(1);
		servicePlan.setServicePlan("Equity");
		servicePlan.setServicePlanId(1);
		servicePlan.setServicePlanLink("servicePlanLink");
		servicePlanList.add(servicePlan);
		List<String> pincodeList = new ArrayList<String>();
		pincodeList.add("627001");
		pincodeList.add("627002");
		List<String> stateCityPincodeList = new ArrayList<String>();
		stateCityPincodeList.add("627003");
		stateCityPincodeList.add("627004");

		when(advisorService.fetchPincodeByState(Mockito.anyString())).thenReturn(pincodeList);
		when(advisorService.fetchPincodeByStateAndCity(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(pincodeList);
		when(advisorService.fetchPincodeListByPincode(Mockito.anyString())).thenReturn(pincodeList);
		when(advisorService.fetchExploreProductListById(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(servicePlanList);
		when(advisorService.fetchExploreAdvisorListByProduct(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
						.thenReturn(advisorList);
		when(advisorService.fetchTotalExploreAdvisorByProduct(Mockito.anyList(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchExploreAdvisorListByProductWithoutBrand(Mockito.anyInt(), Mockito.anyInt(),
				Mockito.anyList(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
						.thenReturn(advisorList);
		when(advisorService.fetchTotalExploreAdvisorByProductWithoutBrandId(Mockito.anyList(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchExploreAdvisorDESCListOrder(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);
		when(advisorService.fetchTotalExploreAdvisorList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(1);

		// when(advisorService.fetchExploreAdvisorByProduct(Mockito.anyInt(),
		// Mockito.anyInt(), Mockito.anyString(),
		// Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
		// Mockito.anyString(), Mockito.anyString(),
		// Mockito.anyString())).thenReturn(advisorList)

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/exploreAdvisorByProduct").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].emailId", is("adv@gmail.com")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("advisor")));
	}

	@Test
	public void test_searchAdvisor_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setName("advisor");
		adv.setEmailId("adv@gmail.com");
		adv.setState("tamilnadu");
		adv.setCity("chennai");
		adv.setPincode("624145");
		adv.setDisplayName("adv");
		adv.setPanNumber("mypan1234a");
		adv.setPhoneNumber("7894561230");
		adv.setUserName("advisor");
		advisorList.add(adv);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
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

		when(advisorService.fetchTotalSearchAdvisorList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchSearchAdvisorList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(advisorList);

		mockMvc.perform(MockMvcRequestBuilders.post("/searchAdvisor").content(jsonString).param("userName", "Username")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].emailId", is("adv@gmail.com")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("advisor")));
	}

	@Test
	public void test_searchAdvisor_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/searchAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_searchAdvisor_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/searchAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_searchAdvisor_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Advisor> advisors = new ArrayList<Advisor>();
		Advisor adv1 = new Advisor();
		adv1.setAdvId(("ADV000000000A"));
		adv1.setName("AAA");
		Advisor adv2 = new Advisor();
		adv2.setAdvId("ADV000000000B");
		adv2.setName("BBB");
		advisors.add(adv1);
		advisors.add(adv2);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		// when(advisorService.fetchAdvisorList(Mockito.anyInt(),
		// Mockito.anyInt())).thenReturn(advisors);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(advisorService.fetchTotalSearchAdvisorList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(2);
		when(advisorService.fetchSearchAdvisorList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(advisors);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/searchAdvisor").param("userName", "Username").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].name", is("AAA")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].advId", is("ADV000000000B")))
				.andExpect(jsonPath("$.responseData.data.advisors.[1].name", is("BBB")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_exploreAdvisorWithOutToken_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV000000000A");
		adv.setDisplayName("advisor");
		adv.setEmailId("adv@gmail.com");
		adv.setState("tamilnadu");
		adv.setCity("chennai");
		adv.setPincode("624145");
		advisorList.add(adv);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		when(advisorService.fetchTotalExploreAdvisorList(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchExploreAdvisorDESCListOrder(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),

				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);
		when(advisorService.fetchExploreAdvisorList(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);

		mockMvc.perform(MockMvcRequestBuilders.get("/exploreAdvisorWithOutToken").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advisors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].city", is("chennai")))
				.andExpect(jsonPath("$.responseData.data.advisors.[0].displayName", is("advisor")));
	}

	// @Test
	// public void test_exploreAdvisorByProductWithOutToken_Success() throws
	// Exception { //Error
	// ScreenIdRequest screenIdRequest = new ScreenIdRequest();
	// screenIdRequest.setScreenId(1);
	//
	// List<Advisor> advisorList = new ArrayList<Advisor>();
	// List<AdvProduct> productList = new ArrayList<AdvProduct>();
	// AdvProduct advProduct = new AdvProduct();
	// advProduct.setAdvId("ADV000000000A");
	// advProduct.setAdvProdId(1);
	// advProduct.setProdId(1);
	// productList.add(advProduct);
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	// adv.setEmailId("adv@gmail.com");
	// adv.setState("tamilnadu");
	// adv.setCity("chennai");
	// adv.setPincode("624145");
	// adv.setDisplayName("adv");
	// adv.setAdvProducts(productList);
	// advisorList.add(adv);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	//
	// List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
	// ServicePlan servicePlan = new ServicePlan();
	// servicePlan.setBrandId(1);
	// servicePlan.setProdId(1);
	// servicePlan.setServiceId(1);
	// servicePlan.setServicePlan("Equity");
	// servicePlan.setServicePlanId(1);
	// servicePlan.setServicePlanLink("servicePlanLink");
	// servicePlanList.add(servicePlan);
	// List<String> pincodeList = new ArrayList<String>();
	// pincodeList.add("627001");
	// pincodeList.add("627002");
	// List<String> stateCityPincodeList = new ArrayList<String>();
	// stateCityPincodeList.add("627003");
	// stateCityPincodeList.add("627004");
	//
	// when(advisorService.fetchPincodeByState(Mockito.anyString())).thenReturn(pincodeList);
	// when(advisorService.fetchPincodeByStateAndCity(Mockito.anyString(),
	// Mockito.anyString()))
	// .thenReturn(pincodeList);
	// when(advisorService.fetchPincodeListByPincode(Mockito.anyString())).thenReturn(pincodeList);
	// when(advisorService.fetchExploreProductListById(Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn(servicePlanList);
	// when(advisorService.fetchExploreAdvisorListByProduct(Mockito.anyInt(),
	// Mockito.anyInt(), Mockito.anyList(),
	// Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString()))
	// .thenReturn(advisorList);
	// when(advisorService.fetchTotalExploreAdvisorByProduct(Mockito.anyList(),
	// Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn(advisorList);
	// when(advisorService.fetchExploreAdvisorListByProductWithoutBrand(Mockito.anyInt(),
	// Mockito.anyInt(),
	// Mockito.anyList(), Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString()))
	// .thenReturn(advisorList);
	// when(advisorService.fetchTotalExploreAdvisorByProductWithoutBrandId(Mockito.anyList(),
	// Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString())).thenReturn(advisorList);
	// when(advisorService.fetchExploreAdvisorWithOutTokenList(Mockito.anyInt(),
	// Mockito.anyInt(), Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn(advisorList);
	// when(advisorService.fetchTotalExploreAdvisorList(Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn(advisorList);
	//
	// // when(advisorService.fetchExploreAdvisorByProduct(Mockito.anyInt(),
	// // Mockito.anyInt(), Mockito.anyString(),
	// // Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
	// // Mockito.anyString(), Mockito.anyString(),
	// // Mockito.anyString())).thenReturn(advisorList);
	// mockMvc.perform(MockMvcRequestBuilders.post("/exploreAdvisorByProductWithOutToken").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data.exploreAdvisor", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.exploreAdvisor.[0].emailId",
	// is("adv@gmail.com")))
	// .andExpect(jsonPath("$.responseData.data.exploreAdvisor.[0].name",
	// is("advisor")));
	// }

	// @Test
	// public void test_exploreAdvisorByProductWithOutToken_Success() throws
	// Exception { // Error
	// ScreenIdRequest screenIdRequest = new ScreenIdRequest();
	// screenIdRequest.setScreenId(1);
	//
	// List<Advisor> advisorList = new ArrayList<Advisor>();
	// List<AdvProduct> productList = new ArrayList<AdvProduct>();
	// AdvProduct advProduct = new AdvProduct();
	// advProduct.setAdvId("ADV000000000A");
	// advProduct.setAdvProdId(1);
	// advProduct.setProdId(1);
	// productList.add(advProduct);
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("advisor");
	// adv.setEmailId("adv@gmail.com");
	// adv.setState("tamilnadu");
	// adv.setCity("chennai");
	// adv.setPincode("624145");
	// adv.setDisplayName("adv");
	// adv.setAdvProducts(productList);
	// advisorList.add(adv);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	//
	// when(advisorService.fetchExploreAdvisorByProduct(Mockito.anyInt(),
	// Mockito.anyInt(), Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn(advisorList);
	// mockMvc.perform(MockMvcRequestBuilders.get("/exploreAdvisorByProductWithOutToken").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data.exploreAdvisor", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.exploreAdvisor.[0].city",
	// is("chennai")))
	// .andExpect(jsonPath("$.responseData.data.exploreAdvisor.[0].name",
	// is("advisor")));
	// }

	@Test
	public void test_validateUniqueFields_EmailId_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setEmailId("aaa@gmail.com");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_N());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.EMAILID, appMessages.getUser_already_present_emailid());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_EmailId_Diasble_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setEmailId("aaa@gmail.com");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_Y());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByEmailId(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.EMAILID, appMessages.getUser_already_present_emailid_disabled());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_PAN_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setPanNumber("ABCDE1234F");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_N());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByPAN(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.PAN, appMessages.getUser_already_present_pan());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_PAN_Disable_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setPanNumber("ABCDE1234F");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_Y());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByPAN(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.PAN, appMessages.getUser_already_present_pan_disabled());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_Phonenumber_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setPhoneNumber("8547691235");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_N());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByPhoneNumber(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.PHONENUMBER, appMessages.getUser_already_present_phone());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_Phonenumber_Disable_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setPhoneNumber("8547691235");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_Y());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByPhoneNumber(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.PHONENUMBER, appMessages.getUser_already_present_phone_disabled());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_Username_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setUserName("USER123");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_N());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.USERNAME, appMessages.getUser_already_present_with_username());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	@Test
	public void test_validateUniqueFields_Username_Disable_Success() throws Exception {
		UniqueFieldRequest uniqueFieldRequest = new UniqueFieldRequest();
		uniqueFieldRequest.setScreenId(1);
		uniqueFieldRequest.setUserName("USER123");
		Party party = new Party();
		party.setDelete_flag(advTableFields.getDelete_flag_Y());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(uniqueFieldRequest);

		when(advisorService.fetchPartyByUserName(Mockito.anyString())).thenReturn(party);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(AdvisorConstants.USERNAME, appMessages.getUser_already_present_username_disabled());
		mockMvc.perform(MockMvcRequestBuilders.post("/validateUniqueFields").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(errors)));
	}

	// @Test // not working
	// public void test_uploadPdfFile() throws Exception {
	// MockMultipartFile file = new MockMultipartFile("file", "Hello,
	// World!".getBytes());
	// String url = "URL";
	// ServicePlan servicePlan = new ServicePlan();
	// servicePlan.setProdId(1);
	// servicePlan.setServiceId(1);
	// servicePlan.setBrandId(1);
	// servicePlan.setServicePlan("plan");
	// servicePlan.setServicePlanLink("link");
	// when(advisorService.fetchProductNameByProdId(Mockito.anyInt())).thenReturn("product");
	// when(advisorService.fetchServiceNameByProdIdAndServiceId(Mockito.anyInt(),
	// Mockito.anyInt()))
	// .thenReturn("service");
	// when(advisorService.fetchBrandNameByProdIdAndBrandId(Mockito.anyInt(),
	// Mockito.anyInt())).thenReturn("brand");
	// when(advisorService.fetchServicePlan(Mockito.anyInt(), Mockito.anyInt(),
	// Mockito.anyInt(), Mockito.anyString()))
	// .thenReturn(null);
	// when(amazonClient.uploadPdfFile(Mockito.any(),
	// Mockito.anyString())).thenReturn(url);
	// when(advisorService.addServicePlan(Mockito.anyInt(), Mockito.anyInt(),
	// Mockito.anyInt(), Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	// MockMvc mockMvc =
	// MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	// mockMvc.perform(multipart("/uploadPdfFile").file(file).queryParam("prodId",
	// "3").queryParam("serviceId", "11")
	// .queryParam("brandId", "1")).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())));
	// }

	@Test
	public void test_searchCity_Success() throws Exception {
		List<CityList> cityList = new ArrayList<CityList>();
		CityList city = new CityList();
		city.setCity("tirunelveli");
		city.setStateId("1");
		city.setState("tamilnadu");
		List<String> pincodes = new ArrayList<String>();
		pincodes.add("627001");
		pincodes.add("627004");
		pincodes.add("627006");
		city.setPincodes(pincodes);
		cityList.add(city);

		when(advisorService.searchStateCityPincodeByCity("tirunelveli")).thenReturn(cityList);

		mockMvc.perform(MockMvcRequestBuilders.get("/searchCity").queryParam("cityName", "tirunelveli")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].city", is("tirunelveli")))
				.andExpect(jsonPath("$.responseData.data.[0].stateId", is("1")))
				.andExpect(jsonPath("$.responseData.data.[0].state", is("tamilnadu")));

	}

	@Test
	public void test_fetchChatUserByAdvId_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV000000000A");
		chatRequest.setScreenId(1);

		List<ChatUser> chatUserList = new ArrayList<ChatUser>();
		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId("ADV000000000A");
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);
		chatUserList.add(chatUser);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchChatUserListByAdvId(Mockito.anyString())).thenReturn(chatUserList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].advId", is("ADV000000000A")))
				.andExpect(jsonPath("$.responseData.data.[0].chatUserId", is(1)));
	}

	@Test
	public void test_fetchChatUserByAdvId_ScreenRights_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000000");
		chatRequest.setScreenId(1);

		List<ChatUser> chatUser = new ArrayList<ChatUser>();
		ChatUser user = new ChatUser();
		user.setUserId("INV0000000000");
		user.setAdvId("ADV0000000000");
		user.setStatus(1);
		chatUser.add(user);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchChatUserListByAdvId("ADV0000000000")).thenReturn(chatUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)));
	}

	@Test
	public void test_fetchChatUserListByAdvId_UnAuthorized() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setUserId("ADV0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchAdvisorByUserNameWithOutToken_Success() throws Exception {

		AdvIdRequest advIdRequest = new AdvIdRequest();
		advIdRequest.setUserName("adv");
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV0000000000");
		advisor.setUserName("adv");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);

		when(advisorService.fetchAdvisorByUserNameWithOutToken("adv")).thenReturn(advisor);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvisorByUserNameWithOutToken").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.advId", is("ADV0000000000")))
				.andExpect(jsonPath("$.responseData.data.userName", is("adv")));

	}

	@Test
	public void test_fetchChatUserListByUserId_Success() throws Exception {

		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setUserId("INV0000000000");
		chatRequest.setScreenId(1);

		List<ChatUser> chatUser = new ArrayList<ChatUser>();
		ChatUser user = new ChatUser();
		user.setUserId("INV0000000000");
		user.setAdvId("ADV0000000000");
		user.setStatus(1);
		chatUser.add(user);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchChatUserListByUserId("INV0000000000")).thenReturn(chatUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserListByUserId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)));

	}

	@Test
	public void test_fetchChatUserListByUserId_ScreenRights_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setUserId("INV0000000000");
		chatRequest.setScreenId(1);

		List<ChatUser> chatUser = new ArrayList<ChatUser>();
		ChatUser user = new ChatUser();
		user.setUserId("INV0000000000");
		user.setAdvId("ADV0000000000");
		user.setStatus(1);
		chatUser.add(user);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchChatUserListByUserId("INV0000000000")).thenReturn(chatUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserListByUserId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)));
	}

	@Test
	public void test_fetchChatUserListByUserId_UnAuthorized() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setUserId("INV0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserListByUserId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test //200 error
	// public void test_fetchChatUserListByUserId_AccessDenied() throws Exception {
	// ChatRequest chatRequest = new ChatRequest();
	// chatRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(chatRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	//
	// mockMvc.perform(post("/fetchChatUserListByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	@Test
	public void test_approveChat_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000002");
		chatRequest.setScreenId(1);

		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(chatUser);
		when(advisorService.approveChat(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(put("/approveChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_approved_successfully())))
				.andReturn();
	}

	@Test
	public void test_approveChat_Error() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("ADV0000000002");
		chatRequest.setScreenId(1);

		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		when(advisorService.fetchChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(chatUser);
		when(advisorService.approveChat(Mockito.anyLong(), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(put("/approveChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_approveChat_ScreenRights_Success() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);
		chatRequest.setAdvId("ADV0000000001");
		chatRequest.setUserId("INV0000000001");

		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		when(advisorService.fetchChatUser(Mockito.anyString(), Mockito.anyString())).thenReturn(chatUser);
		when(advisorService.approveChat(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);

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

		mockMvc.perform(put("/approveChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChatuser_approved_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_approveChat_ScreenRights_UnAuthorized() throws Exception {
		ChatRequest chatRequest = new ChatRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		mockMvc.perform(put("/approveChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_approveChat_AccessDenied() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);

		mockMvc.perform(put("/approveChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchChatUserCount_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setScreenId(1);
		List<Integer> advList = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		advList.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchChatUserCount(Mockito.anyString())).thenReturn(advList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(1)));

	}

	@Test
	public void test_fetchChatUserCount_ScreenRights_Success() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		advIdReq.setAdvId("ADV000000000A");
		List<Integer> advList = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		advList.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(advisorService.fetchChatUserCount(Mockito.anyString())).thenReturn(advList);
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
		mockMvc.perform(post("/fetchChatUserCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchChatUserCount_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchChatUserCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchChatUserCount_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest idRequest = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/fetchChatUserCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchChatUserCount_NotFound() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setAdvId("ADV000000000A");
		advIdReq.setScreenId(1);
		List<Integer> advList = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		advList.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		when(advisorService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_UnFollowByUserId_Success() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.unFollowByUserId(Mockito.anyLong(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(put("/unFollowByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getFollower_unfollowed())))
				.andReturn();
	}

	@Test
	public void test_UnFollowByUserId_Error() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(followers);
		when(advisorService.unFollowByUserId(Mockito.anyLong(), Mockito.anyString())).thenReturn(result);

		mockMvc.perform(put("/unFollowByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_UnFollowByUserId_NotFound() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setAdvId("ADV0000000001");
		followerRequest.setUserId("ADV0000000002");
		followerRequest.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		when(advisorService.fetchFollowersByUserIdWithAdvId(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

		mockMvc.perform(put("/unFollowByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	// @Test
	// public void test_fetchKeyPeopleByParentId_Error() throws Exception {
	// IdRequest idReq = new IdRequest();
	// idReq.setId(1);
	// //
	// // List<KeyPeople> key = new ArrayList<KeyPeople>();
	// // KeyPeople keyPeople = new KeyPeople();
	// // keyPeople.setParentPartyId(1);
	// // keyPeople.setFullName("david");
	// // key.add(keyPeople);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(idReq);
	//
	// when(advisorService.fetchKeyPeopleByParentId(Mockito.anyLong())).thenReturn(null);
	// mockMvc.perform(
	// MockMvcRequestBuilders.post("/fetch").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())));
	// }

	// @Test
	// public void test_uploadFile_Error() throws Exception {
	// MockMultipartFile file = new MockMultipartFile("file", "Hello,
	// World!".getBytes());
	// when(amazonClient.uploadFile(file)).thenReturn(null);
	// MockMvc mockMvc =
	// MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	// mockMvc.perform(multipart("/uploadFile").file(file)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured_upload())));
	// }
	@Test
	public void test_fetchChatUserByAdvId_Mandatory() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);

		List<ChatUser> chatUserList = new ArrayList<ChatUser>();
		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId("ADV000000000A");
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);
		chatUserList.add(chatUser);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));
	}

	@Test
	public void test_fetchChatUserListByUserId_Mandatory() throws Exception {

		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);

		List<ChatUser> chatUser = new ArrayList<ChatUser>();
		ChatUser user = new ChatUser();
		user.setUserId("INV0000000000");
		user.setAdvId("ADV0000000000");
		user.setStatus(1);
		chatUser.add(user);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserListByUserId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_userId())));

	}

	@Test
	public void test_searchCity_Mandatory() throws Exception {
		List<CityList> cityList = new ArrayList<CityList>();
		List<String> pincodes = new ArrayList<String>();
		pincodes.add("627001");
		pincodes.add("627004");
		pincodes.add("627006");

		mockMvc.perform(MockMvcRequestBuilders.get("/searchCity").queryParam("cityName", "")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_city())));
	}

	@Test
	public void test_blockChat_Mandatory() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);
		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		mockMvc.perform(put("/blockChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_block())))
				.andReturn();
	}

	@Test
	public void test_addChat_Mandatory() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);

		List<ChatUser> chatUserList = new ArrayList<ChatUser>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);
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

		mockMvc.perform(post("/addChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_followers())))
				.andReturn();

	}

	@Test
	public void test_UnFollowByUserId_Mandatory() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		mockMvc.perform(put("/unFollowByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_followers())))
				.andReturn();
	}

	@Test
	public void test_approveFollowers_Mandatory() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setFollowersId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		mockMvc.perform(post("/approveFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_followers())))
				.andReturn();
	}

	@Test
	public void test_addFollowers_Mandatory() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);
		List<Followers> followers = new ArrayList<Followers>();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000001");
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		mockMvc.perform(post("/addFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_followers())))
				.andReturn();

	}

	@Test
	public void test_blockFollowers_Mandatory() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		followerRequest.setScreenId(1);

		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setFollowersId(1);
		int result = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);

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

		mockMvc.perform(put("/blockFollowers").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_block())))
				.andReturn();

	}

	@Test
	public void test_approveChat_Mandatory() throws Exception {
		ChatRequest chatRequest = new ChatRequest();
		chatRequest.setScreenId(1);

		ChatUser chatUser = new ChatUser();
		chatUser.setChatUserId(1);
		chatUser.setStatus(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(chatRequest);

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

		mockMvc.perform(put("/approveChat").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_followers())))
				.andReturn();
	}

	public void test_fetchChatUserCount_Mandatory() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchChatUserCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));
	}

	@Test
	public void test_fetchFollowersListByUserId_Mandatory() throws Exception {
		FollowerRequest followerRequest = new FollowerRequest();
		// followerRequest.setUserId("ADV000000000B");
		followerRequest.setAdvId("ADV000000000A");
		followerRequest.setScreenId(1);
		List<Followers> followers = new ArrayList<Followers>();
		Followers follower = new Followers();
		// follower.setUserId("ADV000000000A");
		followers.add(follower);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(followerRequest);
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

		mockMvc.perform(post("/fetchFollowersListByUserId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_userId())));
	}

	@Test
	public void test_fetchAdvisorByUserNameWithOutToken_Mandatory() throws Exception {

		AdvIdRequest advIdRequest = new AdvIdRequest();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV0000000000");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAdvisorByUserNameWithOutToken").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_userName())));

	}

	@Test
	public void test_modifyKeyPeople_Mandatory() throws Exception {

		KeyPeopleRequest keyPeopleRequest = new KeyPeopleRequest();
		keyPeopleRequest.setFullName("Key People");
		keyPeopleRequest.setParentPartyId(1);
		keyPeopleRequest.setScreenId(1);
		KeyPeople key = new KeyPeople();
		key.setFullName("advisor");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(keyPeopleRequest);
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

		when(advisorService.checkKeyPeopleIsPresent(Mockito.anyLong())).thenReturn(1);
		when(keyPeopleRequestValidator.validate(keyPeopleRequest)).thenReturn(null);
		when(advisorService.modifyKeyPeople(Mockito.anyLong(), Mockito.any(KeyPeople.class))).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyKeyPeople").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_keyPeopleId())))
				.andReturn();
	}

	@Test
	public void test_teamMemberDeactivate_Mandatory() throws Exception {
		AdvIdRequest idReq = new AdvIdRequest();
		idReq.setScreenId(1);
		Advisor adv = new Advisor();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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
		when(advisorService.fetchWorkFlowStatusIdByDesc("deactivated")).thenReturn(9);
		when(advisorService.teamMemberDeactivate(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);

		mockMvc.perform(post("/teamMemberDeactivate").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void test_verifyOtp_Mandatory() throws Exception {
		OtpRequest otpRequest = new OtpRequest();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(otpRequest);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/verifyOtp").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_otp())))
				.andReturn();
	}

	@Test
	public void test_removeKeyPeople_Mandatory() throws Exception {
		IdRequest idReq = new IdRequest();
		idReq.setScreenId(1);

		KeyPeople key = new KeyPeople();
		key.setFullName("XYZ");
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

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(post("/removeKeyPeople").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_keyPeopleId())))
				.andReturn();
	}
	// @Test //If condition//
	//
	// public void test_fetchDashBoardCount_Success() throws Exception {
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// AdvIdRequest advIdReq = new AdvIdRequest();
	//
	// advIdReq.setAdvId("ADV000000000A");
	// advIdReq.setScreenId(1);
	// List<Integer> counts = new ArrayList<Integer>();
	// counts.add(1);
	// Dashboard dashBoard = new Dashboard();
	// dashBoard.setFollowers(2);
	// dashBoard.setFollowing(2);
	// dashBoard.setPlannedUser(1);
	// dashBoard.setSharedPlan(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(advIdReq);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(advisorService.fetchPartyByRoleBasedId(Mockito.anyString())).thenReturn(party);
	// when(advisorService.fetchFollowersCount(Mockito.anyString())).thenReturn(counts);
	// when(advisorService.fetchSharedPlanCountPartyId(Mockito.anyLong())).thenReturn(1);
	// when(advisorService.fetchFollowersCountByUserId(Mockito.anyString())).thenReturn(1);
	// when(advisorService.fetchPlannedUserCountPartyId(Mockito.anyLong())).thenReturn(5);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/fetchDashboardCount").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())));
	// .andExpect(jsonPath("$.responseData.data", hasSize(1)));
	// .andExpect(jsonPath("$.responseData.data.followers", is(2)))
	// .andExpect(jsonPath("$.responseData.data.following", is(2)))
	// .andExpect(jsonPath("$.responseData.data.sharedPlan", is(1)))
	// .andExpect(jsonPath("$.responseData.data.plannedUser", is(1)));
	//
	// }

	@Test
	public void test_fetchDashBoardCount_ScreenRights_AccessDenied() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();
		advIdReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchDashboardCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchDashBoardCount_ScreenRights_UnAuthorized() throws Exception {
		AdvIdRequest advIdReq = new AdvIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
		mockMvc.perform(post("/fetchDashboardCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchDashBoardCount_Mandatory() throws Exception {
		Party party = new Party();
		AdvIdRequest advIdReq = new AdvIdRequest();

		advIdReq.setScreenId(1);
		List<Integer> counts = new ArrayList<Integer>();

		counts.add(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdReq);
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
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchDashboardCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));

	}

	// mandatory fields expected but was success

	// @Test
	// public void test_searchAdvisor_Mandatory() throws Exception {
	// ScreenIdRequest screenIdRequest = new ScreenIdRequest();
	// screenIdRequest.setScreenId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/searchAdvisor").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000))).andExpect(jsonPath(
	// "$.responseMessage.responseDescription",
	// is(appMessages.getMandatory_fields_advisor())));
	// }
	@Test
	public void test_fetchGst_Success() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();
		advIdRequest.setScreenId(1);
		advIdRequest.setAdvId("ADV000000000A");
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
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

		when(advisorService.fetchAdvisorGstByAdvId("ADV000000000A")).thenReturn(advisor);

		mockMvc.perform(post("/fetchGst").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())));
	}

	@Test
	public void test_fetchGst_Mandatory() throws Exception {
		AdvIdRequest advIdRequest = new AdvIdRequest();
		advIdRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(advIdRequest);
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

		mockMvc.perform(post("/fetchGst").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields())));
	}

	// @Test //response code error
	// public void test_exploreAdvisorByBrand_Success() throws Exception {
	// ScreenIdRequest screenIdRequest = new ScreenIdRequest();
	// screenIdRequest.setScreenId(1);
	//
	// List<Advisor> advisorList = new ArrayList<Advisor>();
	//
	// List<AdvBrandInfo> brandList = new ArrayList<AdvBrandInfo>();
	// AdvBrandInfo advBrandInfo = new AdvBrandInfo();
	// advBrandInfo.setAdvBrandId(1);
	// advBrandInfo.setProdId(1);
	// advBrandInfo.setServiceId("1");
	// brandList.add(advBrandInfo);
	//
	// Advisor adv = new Advisor();
	// adv.setEmailId("adv@gmail.com");
	// adv.setName("advisor");
	// adv.setState("tamilnadu");
	// adv.setCity("chennai");
	// adv.setPincode("624145");
	// adv.setAdvBrandInfo(brandList);
	// advisorList.add(adv);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	//
	// when(advisorService.fetchExploreAdvisorByProdDetailsAndStateDetails(Mockito.anyString(),
	// Mockito.anyString(),
	// Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
	// Mockito.anyString()))
	// .thenReturn(advisorList);
	//
	// mockMvc.perform(post("/exploreAdvisorByBrand").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.data.advisors",
	// hasSize(0)))
	// .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.data.advisors.[0].name",
	// is("advisor")))
	// .andExpect(MockMvcResultMatchers.jsonPath("$.responseData.data.advisors.[0].emailId",
	// is("adv@gmail.com")));
	//
	// }
	// @Test
	// public void test_exploreAdvisorByBrand_Error() throws Exception {
	// ScreenIdRequest screenIdRequest = new ScreenIdRequest();
	// screenIdRequest.setScreenId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	//
	// when(advisorService.fetchExploreAdvisorByProdDetails(Mockito.anyString(),
	// Mockito.anyString(),
	// Mockito.anyString())).thenReturn(null);
	// when(advisorService.fetchExploreAdvisorByCityDetails(Mockito.anyString(),
	// Mockito.anyString(),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(MockMvcRequestBuilders.post("/exploreAdvisorByBrand").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError())));
	// }

}
