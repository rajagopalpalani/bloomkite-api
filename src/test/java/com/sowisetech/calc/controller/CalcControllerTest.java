package com.sowisetech.calc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsMapWithSize;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.AdvisorApplication;
import com.sowisetech.admin.request.ScreenFieldRightsRequest;
import com.sowisetech.advisor.util.AdvTableFields;
//import com.sowisetech.calc.CalcApplication;
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
import com.sowisetech.calc.model.EmiInterestChange;
import com.sowisetech.calc.model.FinancialPlanning;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.GoalPlanning;
import com.sowisetech.calc.model.Insurance;
import com.sowisetech.calc.model.InsuranceItem;
import com.sowisetech.calc.model.InsuranceStringItem;
import com.sowisetech.calc.model.InterestChange;
import com.sowisetech.calc.model.LoanPlanning;
import com.sowisetech.calc.model.Networth;
import com.sowisetech.calc.model.NetworthSummary;
import com.sowisetech.calc.model.PartialPayment;
import com.sowisetech.calc.model.Party;
import com.sowisetech.calc.model.Plan;
import com.sowisetech.calc.model.Priority;
import com.sowisetech.calc.model.PriorityItem;
import com.sowisetech.calc.model.Queries;
import com.sowisetech.calc.model.RiskPortfolio;
import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskQuestionaire;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.Urgency;
import com.sowisetech.calc.request.CalcAnswerRequest;
import com.sowisetech.calc.request.CalcIdRequest;
import com.sowisetech.calc.request.CalcQueryRequest;
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
import com.sowisetech.calc.request.FutureValueAnnuityDueRequestValidator;
import com.sowisetech.calc.request.FutureValueAnnuityRequestValidator;
import com.sowisetech.calc.request.FutureValueRequest;
import com.sowisetech.calc.request.FutureValueRequestValidator;
import com.sowisetech.calc.request.GoalRequest;
import com.sowisetech.calc.request.GoalRequestValidator;
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
import com.sowisetech.calc.request.PresentValueAnnuityDueReqValidator;
import com.sowisetech.calc.request.PresentValueAnnuityRequestValidator;
import com.sowisetech.calc.request.PriorityReq;
import com.sowisetech.calc.request.PriorityRequest;
import com.sowisetech.calc.request.RateFinderAnnuityRequestValidator;
import com.sowisetech.calc.request.RateFinderRequest;
import com.sowisetech.calc.request.RateFinderRequestValidator;
import com.sowisetech.calc.request.RiskProfileReq;
import com.sowisetech.calc.request.RiskProfileRequest;
import com.sowisetech.calc.request.SharedRequest;
import com.sowisetech.calc.request.TargetValueRequest;
import com.sowisetech.calc.request.TargetValueRequestValidator;
import com.sowisetech.calc.request.TenureFinderAnnuityRequestValidator;
import com.sowisetech.calc.request.TenureFinderRequest;
import com.sowisetech.calc.request.TenureFinderRequestValidator;
import com.sowisetech.calc.response.RateFinderResponse;
import com.sowisetech.calc.response.TenureFinderResponse;
import com.sowisetech.calc.response.TotalValueResponse;
import com.sowisetech.calc.service.CalcService;
import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcTableFields;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.common.util.SendMail;
import com.sowisetech.forum.model.ForumSubCategory;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvisorApplication.class)
public class CalcControllerTest {

	@InjectMocks
	private CalcController calcController;

	private MockMvc mockMvc;
	@Mock
	private CalcService calcService;
	@Mock
	GoalRequestValidator goalRequestValidator;
	@Mock
	CashFlowRequestValidator cashFlowRequestValidator;
	@Mock
	NetworthRequestValidator networthRequestValidator;
	// @Mock
	// PriorityRequestValidator priorityRequestValidator;
	@Mock
	InsuranceRequestValidator insuranceRequestValidator;
	@Mock
	FutureValueRequestValidator futureValueRequestValidator;
	@Mock
	FutureValueAnnuityRequestValidator futureValueAnnuityRequestValidator;
	@Mock
	FutureValueAnnuityDueRequestValidator futureValueAnnuityDueRequestValidator;
	@Mock
	TargetValueRequestValidator targetValueRequestValidator;
	@Mock
	PresentValueAnnuityRequestValidator presentValueAnnuityRequestValidator;
	@Mock
	PresentValueAnnuityDueReqValidator presentValueAnnuityDueReqValidator;
	@Mock
	RateFinderRequestValidator rateFinderRequestValidator;
	@Mock
	RateFinderAnnuityRequestValidator rateFinderAnnuityRequestValidator;
	@Mock
	TenureFinderRequestValidator tenureFinderRequestValidator;
	@Mock
	TenureFinderAnnuityRequestValidator tenureFinderAnnuityRequestValidator;
	@Mock
	EmiCalculatorRequestValidator emiCalculatorRequestValidator;
	@Mock
	EmiCapacityRequestValidator emiCapacityRequestValidator;
	@Mock
	PartialPaymentRequestValidator partialPaymentRequestValidator;
	@Mock
	EmiChangeRequestValidator emiChangeRequestValidator;
	@Mock
	InterestChangeRequestValidator interestChangeRequestValidator;
	@Mock
	EmiInterestChangeRequestValidator emiInterestChangeRequestValidator;

	@Mock
	PlanRequestValidator planRequestValidator;

	@Autowired(required = true)
	@Spy
	CalcAppMessages appMessages;
	@Autowired(required = true)
	@Spy
	CalcTableFields calcTableFields;
	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;
	@Autowired(required = true)
	@Spy
	AdminSignin adminSignin;
	@Autowired(required = true)
	@Spy
	ScreenRightsConstants screenRightsConstants;
	@Mock
	ScreenRightsCommon screenRightsCommon;
	@Mock
	SendMail sendMail;
	@Mock
	CommonService commonService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(calcController).build();
	}

	@Test
	public void testEcv() throws Exception {
		this.mockMvc.perform(get("/calc-ecv")).andExpect(status().isOk());
	}

	@Test
	public void test_addPlan_AddSuccess() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");
		PlanRequest planRequest = new PlanRequest();
		planRequest.setPartyId(1);
		planRequest.setName("Advisor");
		planRequest.setAge("25");
		planRequest.setSelectedPlan(selectedPlan);
		planRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(planRequest);

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

		when(calcService.fetchParty(Mockito.anyLong())).thenReturn(party);
		when(planRequestValidator.validate(planRequest)).thenReturn(null);
		when(calcService.generatePlanReferenceId()).thenReturn("P00000");
		when(calcService.fetchRoleBasedIdByPartyId(Mockito.anyLong())).thenReturn("ADV000000000A");
		when(calcService.fetchEmailIdByPartyId(Mockito.anyLong())).thenReturn("aaa.gmail.com");
		when(calcService.addPlanInfo(Mockito.any(Plan.class))).thenReturn(1);

		mockMvc.perform(post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getPlan_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addPlan_ValidatorError() throws Exception {
		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");
		PlanRequest planRequest = new PlanRequest();
		planRequest.setPartyId(1);
		planRequest.setName("Advisor");
		planRequest.setAge("25");
		planRequest.setSelectedPlan(selectedPlan);
		planRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(planRequest);

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

		when(calcService.fetchParty(Mockito.anyLong())).thenReturn(party);
		when(planRequestValidator.validate(Mockito.any(PlanRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError()))).andReturn();
	}

	// @Test
	// public void test_addPlan_ParentReferenceId() throws Exception {
	//
	// List<String> selectedPlan = new ArrayList<>();
	// selectedPlan.add("Investment Planning");
	// PlanRequest planRequest = new PlanRequest();
	// planRequest.setPartyId(1);
	// planRequest.setName("Advisor");
	// planRequest.setAge("25");
	// planRequest.setSelectedPlan(selectedPlan);
	// planRequest.setParentPartyId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(planRequest);
	//
	// when(calcService.fetchParty(Mockito.anyLong())).thenReturn(party);
	// when(planRequestValidator.validate(planRequest)).thenReturn(null);
	// when(calcService.generatePlanReferenceId()).thenReturn("P00000");
	// when(calcService.addPlanInfo(Mockito.any(Plan.class))).thenReturn(1);
	//
	// mockMvc.perform(post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPlan_added_successfully())))
	// .andReturn();
	// }

	// @Test
	// public void test_addPlan_AddError() throws Exception {
	//
	// List<String> selectedPlan = new ArrayList<>();
	// selectedPlan.add("Investment Planning");
	// PlanRequest planRequest = new PlanRequest();
	// planRequest.setPartyId(1);
	// planRequest.setName("Advisor");
	// planRequest.setAge("25");
	// planRequest.setSelectedPlan(selectedPlan);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(planRequest);
	//
	// when(calcService.fetchParty(Mockito.anyLong())).thenReturn(party);
	// when(planRequestValidator.validate(planRequest)).thenReturn(null);
	// when(calcService.fetchRoleBasedIdByPartyId(Mockito.anyLong())).thenReturn("ADV000000000A");
	// when(calcService.addPlanInfo(Mockito.any(Plan.class))).thenReturn(0);
	//
	// mockMvc.perform(post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_addPlan_ScreenRights_AccessDenied() throws Exception {
		PlanRequest screenIdRequest = new PlanRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_addPlan_ScreenRights_Success() throws Exception { //Error
	//
	// List<String> selectedPlan = new ArrayList<>();
	// selectedPlan.add("Investment Planning");
	// PlanRequest planRequest = new PlanRequest();
	// planRequest.setPartyId(1);
	// planRequest.setName("Advisor");
	// planRequest.setAge("25");
	// planRequest.setSelectedPlan(selectedPlan);
	// planRequest.setScreenId(1);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(planRequest);
	//
	// when(calcService.fetchParty(Mockito.anyLong())).thenReturn(party);
	// when(planRequestValidator.validate(planRequest)).thenReturn(null);
	// when(calcService.generatePlanReferenceId()).thenReturn("P00000");
	// when(calcService.fetchRoleBasedIdByPartyId(Mockito.anyLong())).thenReturn("ADV000000000A");
	// when(calcService.fetchEmailIdByPartyId(Mockito.anyLong())).thenReturn("aaa.gmail.com");
	// when(calcService.addPlanInfo(Mockito.any(Plan.class))).thenReturn(1);
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
	// mockMvc.perform(post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPlan_added_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_addPlan_ScreenRights_UnAuthorized() throws Exception {
		PlanRequest planRequest = new PlanRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(planRequest);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyPlan_Success() throws Exception {
		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		ModeratePlanRequest moderatePlanRequest = new ModeratePlanRequest();
		moderatePlanRequest.setReferenceId("P00005");
		moderatePlanRequest.setName("advisor");
		moderatePlanRequest.setSelectedPlan(selectedPlan);
		moderatePlanRequest.setScreenId(1);
		Plan plan = new Plan();
		plan.setReferenceId("P00005");
		plan.setName("adv");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderatePlanRequest);

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

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.modifyPlanInfo(Mockito.any(Plan.class), Mockito.anyString())).thenReturn(1);

		mockMvc.perform(put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPlan_moderate_successfully())))
				.andReturn();
	}

	@Test
	public void test_modifyPlan_Mandatory() throws Exception {
		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		ModeratePlanRequest moderatePlanRequest = new ModeratePlanRequest();
		moderatePlanRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderatePlanRequest);

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

		mockMvc.perform(put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_refId())))
				.andReturn();
	}

	@Test
	public void test_modifyPlan_Negative() throws Exception {
		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		ModeratePlanRequest moderatePlanRequest = new ModeratePlanRequest();
		moderatePlanRequest.setReferenceId("P00005");
		moderatePlanRequest.setName("advisor");
		moderatePlanRequest.setSelectedPlan(selectedPlan);
		moderatePlanRequest.setScreenId(1);
		Plan plan = new Plan();
		plan.setReferenceId("P00005");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderatePlanRequest);
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

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.modifyPlanInfo(Mockito.any(Plan.class), Mockito.anyString())).thenReturn(0);

		mockMvc.perform(put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_modifyPlan_NoRecordFound() throws Exception {
		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		ModeratePlanRequest moderatePlanRequest = new ModeratePlanRequest();
		moderatePlanRequest.setReferenceId("P00005");
		moderatePlanRequest.setSelectedPlan(selectedPlan);
		moderatePlanRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderatePlanRequest);
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
		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);

		mockMvc.perform(put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_modifyPlan_ScreenRights_AccessDenied() throws Exception {
		ModeratePlanRequest screenIdRequest = new ModeratePlanRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_modifyPlan_ScreenRights_Success() throws Exception { //Error
	// List<String> selectedPlan = new ArrayList<>();
	// selectedPlan.add("Investment Planning");
	//
	// ModeratePlanRequest moderatePlanRequest = new ModeratePlanRequest();
	// moderatePlanRequest.setReferenceId("P00005");
	// moderatePlanRequest.setName("advisor");
	// moderatePlanRequest.setSelectedPlan(selectedPlan);
	// moderatePlanRequest.setScreenId(1);
	// Plan plan = new Plan();
	// plan.setReferenceId("P00005");
	// plan.setName("adv");
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(moderatePlanRequest);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.modifyPlanInfo(Mockito.any(Plan.class),
	// Mockito.anyString())).thenReturn(1);
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
	// mockMvc.perform(put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPlan_moderate_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_modifyPlan_ScreenRights_UnAuthorized() throws Exception {
		ModeratePlanRequest moderatePlanRequest = new ModeratePlanRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderatePlanRequest);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/modifyPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removePlan_Success() throws Exception {
		CalcIdRequest calcIdRequest = new CalcIdRequest();
		calcIdRequest.setId("P00005");
		calcIdRequest.setScreenId(1);

		Plan plan = new Plan();
		plan.setReferenceId("P00005");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(calcIdRequest);

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

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.removePlanInfo(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/removePlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPlan_delete_successfully())));
	}

	@Test
	public void test_removePlan_Mandatory() throws Exception {
		CalcIdRequest calcIdRequest = new CalcIdRequest();
		calcIdRequest.setScreenId(1);

		Plan plan = new Plan();
		plan.setReferenceId("P00005");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(calcIdRequest);

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

		mockMvc.perform(post("/removePlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_refId())));
	}

	@Test
	public void test_removePlan_ScreenRights_AccessDenied() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removePlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_removePlan_ScreenRights_Success() throws Exception {
	// CalcIdRequest calcIdRequest = new CalcIdRequest();
	// calcIdRequest.setId("P00005");
	// calcIdRequest.setScreenId(1);
	// Plan plan = new Plan();
	// plan.setReferenceId("P00005");
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(calcIdRequest);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.removePlanInfo(Mockito.anyString())).thenReturn(1);
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
	// mockMvc.perform(post("/removePlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPlan_delete_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	// }

	@Test
	public void test_removePlan_ScreenRights_UnAuthorized() throws Exception {
		CalcIdRequest calcIdRequest = new CalcIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(calcIdRequest);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removePlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchPlan_Success() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("ADV000000000A");
		idReq.setScreenId(1);

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

		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);

		mockMvc.perform(post("/fetchPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.name", is("Advisor"))).andReturn();
	}

	@Test
	public void test_fetchPlan_Mandatory() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setScreenId(1);

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
		mockMvc.perform(post("/fetchPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_refId())));
		// .andExpect(jsonPath("$.responseData.data.name", is("Advisor"))).andReturn();
	}

	@Test
	public void test_fetchPlan_ScreenRights_AccessDenied() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchPlan_ScreenRights_Success() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("ADV000000000A");
		idReq.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
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

		mockMvc.perform(post("/fetchPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.name", is("Advisor")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchPlan_ScreenRights_UnAuthorized() throws Exception {
		ScreenFieldRightsRequest screenFieldRightsRequest = new ScreenFieldRightsRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenFieldRightsRequest);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/fetchPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchPlanByPartyId_Success() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		List<Plan> plans = new ArrayList<Plan>();
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		plans.add(plan);

		PartyIdRequest idReq = new PartyIdRequest();
		idReq.setPartyId(1);
		idReq.setScreenId(1);
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

		when(calcService.fetchPlanByPartyId(Mockito.anyLong())).thenReturn(plans);

		mockMvc.perform(post("/fetchPlanByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchPlanByPartyId_Mandatory() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		List<Plan> plans = new ArrayList<Plan>();
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		plans.add(plan);

		PartyIdRequest idReq = new PartyIdRequest();
		idReq.setScreenId(1);
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

		mockMvc.perform(post("/fetchPlanByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_refId())));
		// .andExpect(jsonPath("$.responseData.data", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchPlanByPartyId_ScreenRights_AccessDenied() throws Exception {
		PartyIdRequest screenIdRequest = new PartyIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchPlanByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchPlanByPartyId_ScreenRights_Success() throws Exception {

		List<String> selectedPlan = new ArrayList<>();
		selectedPlan.add("Investment Planning");

		List<Plan> plans = new ArrayList<Plan>();
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		plans.add(plan);

		PartyIdRequest idReq = new PartyIdRequest();
		idReq.setPartyId(1);
		idReq.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		when(calcService.fetchPlanByPartyId(Mockito.anyLong())).thenReturn(plans);
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

		mockMvc.perform(post("/fetchPlanByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchPlanByPartyId_ScreenRights_UnAuthorized() throws Exception {
		PartyIdRequest idReq = new PartyIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchPlanByPartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_fetchPlan_NotFound() throws Exception {
	// CalcIdRequest idReq = new CalcIdRequest();
	// idReq.setId("ADV000000000A");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(idReq);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
	//
	// mockMvc.perform(post("/fetchPlan").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	// GOAL
	// @Test
	// public void test_calculateGoal_AddSuccess() throws Exception { //Error
	//
	// GoalRequest goalRequest = new GoalRequest();
	// goalRequest.setReferenceId("P00000");
	// goalRequest.setGoalName("Education");
	// goalRequest.setTenure("1");
	// goalRequest.setTenureType("Mo");
	// goalRequest.setGoalAmount("100000");
	// goalRequest.setCurrentAmount("10000");
	// goalRequest.setInflationRate("2");
	// goalRequest.setGrowthRate("3");
	// goalRequest.setAnnualInvestmentRate("5");
	// goalRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// double futureCost = 10000;
	// double futureValue = 1000;
	// double finalCorpus = 2000;
	// double monthlyInv = 700;
	// double annualInv = 12000;
	// Goal goal = new Goal();
	// goal.setGoalName("Education");
	// goal.setFutureCost(futureCost);
	// goal.setFutureValue(futureValue);
	// goal.setFinalCorpus(finalCorpus);
	// goal.setMonthlyInv(monthlyInv);
	// goal.setAnnualInv(annualInv);
	// List<Goal> goalList = new ArrayList<Goal>();
	// goalList.add(goal);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(goalRequest);
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// String token = "token";
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(0);
	// when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
	// when(calcService.calculateGoalFutureCost(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureCost);
	// when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureValue);
	// when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(),
	// Mockito.anyDouble())).thenReturn(finalCorpus);
	// when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
	// when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt())).thenReturn(annualInv);
	// when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
	// when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//
	//
	// mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getGoal_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.[0].goalName",
	// is("Education"))).andReturn();
	// }

	@Test
	public void test_calculateGoal_ZeroError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("0");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("0");
		goalRequest.setCurrentAmount("0");
		goalRequest.setInflationRate("2");
		goalRequest.setGrowthRate("3");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
		when(calcService.calculateGoalFutureCost(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureCost);
		when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureValue);
		when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(finalCorpus);
		when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
		when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt())).thenReturn(annualInv);
		when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//

		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
		// .andExpect(jsonPath("$.responseData.data", hasSize(1)))
		// .andExpect(jsonPath("$.responseData.data.[0].goalName",
		// is("Education"))).andReturn();
	}

	// @Test
	// public void test_calculateGoal_ScreenRights_AccessDenied() throws Exception {
	// GoalRequest screenIdRequest = new GoalRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	@Test
	public void test_calculateGoal_TenureZeroError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("0");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("475780");
		goalRequest.setCurrentAmount("12130");
		goalRequest.setInflationRate("1");
		goalRequest.setGrowthRate("3");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
		when(calcService.calculateGoalFutureCost(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureCost);
		when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureValue);
		when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(finalCorpus);
		when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
		when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt())).thenReturn(annualInv);
		when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//

		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "TENURE")));
	}

	@Test
	public void test_calculateGoal_AmtZeroError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("10");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("0");
		goalRequest.setCurrentAmount("12130");
		goalRequest.setInflationRate("1");
		goalRequest.setGrowthRate("3");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
		when(calcService.calculateGoalFutureCost(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureCost);
		when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureValue);
		when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(finalCorpus);
		when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
		when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt())).thenReturn(annualInv);
		when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//

		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "GOAL AMOUNT")));
	}

	@Test
	public void test_calculateGoal_RateZeroError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("10");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("13880");
		goalRequest.setCurrentAmount("12130");
		goalRequest.setInflationRate("0");
		goalRequest.setGrowthRate("0");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
		when(calcService.calculateGoalFutureCost(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureCost);
		when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureValue);
		when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(finalCorpus);
		when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
		when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt())).thenReturn(annualInv);
		when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//

		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "GROWTH RATE")));
	}

	@Test
	public void test_calculateGoal_TwoFieldZeroError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("0");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("10000");
		goalRequest.setCurrentAmount("1548");
		goalRequest.setInflationRate("0");
		goalRequest.setGrowthRate("0");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
		when(calcService.calculateGoalFutureCost(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureCost);
		when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt()))
				.thenReturn(futureValue);
		when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(finalCorpus);
		when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
		when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
				Mockito.anyInt())).thenReturn(annualInv);
		when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//

		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
		// .andExpect(jsonPath("$.responseData.data", hasSize(1)))
		// .andExpect(jsonPath("$.responseData.data.[0].goalName",
		// is("Education"))).andReturn();
	}

	// @Test
	// public void test_calculateGoal_ScreenRights_Success() throws Exception {
	//
	// GoalRequest goalRequest = new GoalRequest();
	// goalRequest.setReferenceId("P00000");
	// goalRequest.setGoalName("Education");
	// goalRequest.setTenure("1");
	// goalRequest.setTenureType("Mo");
	// goalRequest.setGoalAmount("100000");
	// goalRequest.setCurrentAmount("10000");
	// goalRequest.setInflationRate("2");
	// goalRequest.setGrowthRate("3");
	// goalRequest.setAnnualInvestmentRate("5");
	// goalRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// double futureCost = 10000;
	// double futureValue = 1000;
	// double finalCorpus = 2000;
	// double monthlyInv = 700;
	// double annualInv = 12000;
	// Goal goal = new Goal();
	// goal.setGoalName("Education");
	// goal.setFutureCost(futureCost);
	// goal.setFutureValue(futureValue);
	// goal.setFinalCorpus(finalCorpus);
	// goal.setMonthlyInv(monthlyInv);
	// goal.setAnnualInv(annualInv);
	// List<Goal> goalList = new ArrayList<Goal>();
	// goalList.add(goal);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(goalRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
	// when(calcService.calculateGoalFutureCost(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureCost);
	// when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureValue);
	// when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(),
	// Mockito.anyDouble())).thenReturn(finalCorpus);
	// when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
	// when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt())).thenReturn(annualInv);
	// when(calcService.fetchGoalByRefIdAndGoalName(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(null);
	// when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
	// when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//
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
	// mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getGoal_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.[0].goalName", is("Education")))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test
	// public void test_calculateGoal_ScreenRights_UnAuthorized() throws Exception {
	// GoalRequest goalRequest = new GoalRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(goalRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/calculateGoal").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_calculateGoal_ValidatorError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("1");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("100000");
		goalRequest.setCurrentAmount("10000");
		goalRequest.setInflationRate("2");
		goalRequest.setGrowthRate("3");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(goalRequestValidator.validate(Mockito.any(GoalRequest.class))).thenReturn(allErrors);
		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculateGoal_FieldsEmptyError() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("1");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("100000");
		goalRequest.setCurrentAmount("10000");
		goalRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
		mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getFields_cannot_be_empty())))
				.andReturn();
	}

	// @Test
	// public void test_calculateGoal_AddError() throws Exception { //Error
	//
	// GoalRequest goalRequest = new GoalRequest();
	// goalRequest.setReferenceId("P00000");
	// goalRequest.setGoalName("Education");
	// goalRequest.setTenure("1");
	// goalRequest.setTenureType("Mo");
	// goalRequest.setGoalAmount("100000");
	// goalRequest.setCurrentAmount("10000");
	// goalRequest.setInflationRate("2");
	// goalRequest.setGrowthRate("3");
	// goalRequest.setAnnualInvestmentRate("5");
	// goalRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// double futureCost = 10000;
	// double futureValue = 1000;
	// double finalCorpus = 2000;
	// double monthlyInv = 700;
	// double annualInv = 12000;
	// Goal goal = new Goal();
	// goal.setGoalName("Education");
	// goal.setFutureCost(futureCost);
	// goal.setFutureValue(futureValue);
	// goal.setFinalCorpus(finalCorpus);
	// goal.setMonthlyInv(monthlyInv);
	// goal.setAnnualInv(annualInv);
	// List<Goal> goalList = new ArrayList<Goal>();
	// goalList.add(goal);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(goalRequest);
	//
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(0);
	// when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
	// when(calcService.calculateGoalFutureCost(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureCost);
	// when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureValue);
	// when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(),
	// Mockito.anyDouble())).thenReturn(finalCorpus);
	// when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
	// when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt())).thenReturn(annualInv);
	// when(calcService.addGoalInfo(Mockito.any(Goal.class))).thenReturn(0);
	// when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//
	//
	// mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_calculateGoal_NotFound() throws Exception {

		GoalRequest goalRequest = new GoalRequest();
		goalRequest.setReferenceId("P00000");
		goalRequest.setGoalName("Education");
		goalRequest.setTenure("1");
		goalRequest.setTenureType("Mo");
		goalRequest.setGoalAmount("100000");
		goalRequest.setCurrentAmount("10000");
		goalRequest.setInflationRate("2");
		goalRequest.setGrowthRate("3");
		goalRequest.setAnnualInvestmentRate("5");
		goalRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		double futureCost = 10000;
		double futureValue = 1000;
		double finalCorpus = 2000;
		double monthlyInv = 700;
		double annualInv = 12000;
		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setFutureCost(futureCost);
		goal.setFutureValue(futureValue);
		goal.setFinalCorpus(finalCorpus);
		goal.setMonthlyInv(monthlyInv);
		goal.setAnnualInv(annualInv);
		List<Goal> goalList = new ArrayList<Goal>();
		goalList.add(goal);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(goalRequest);
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

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(post("/calculateGoal").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	// @Test
	// public void test_calculateGoal_UpdateSuccess() throws Exception { //Error
	// GoalRequest goalRequest = new GoalRequest();
	// goalRequest.setReferenceId("P00000");
	// goalRequest.setGoalName("Education");
	// goalRequest.setTenure("1");
	// goalRequest.setTenureType("Mo");
	// goalRequest.setGoalAmount("100000");
	// goalRequest.setCurrentAmount("10000");
	// goalRequest.setInflationRate("2");
	// goalRequest.setGrowthRate("3");
	// goalRequest.setAnnualInvestmentRate("5");
	// goalRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// double futureCost = 10000;
	// double futureValue = 1000;
	// double finalCorpus = 2000;
	// double monthlyInv = 700;
	// double annualInv = 12000;
	// Goal goal = new Goal();
	// goal.setGoalName("Education");
	// goal.setFutureCost(futureCost);
	// goal.setFutureValue(futureValue);
	// goal.setFinalCorpus(finalCorpus);
	// goal.setMonthlyInv(monthlyInv);
	// goal.setAnnualInv(annualInv);
	// List<Goal> goalList = new ArrayList<Goal>();
	// goalList.add(goal);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(goalRequest);
	//
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(1);
	// when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
	// when(calcService.calculateGoalFutureCost(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureCost);
	// when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureValue);
	// when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(),
	// Mockito.anyDouble())).thenReturn(finalCorpus);
	// when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
	// when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt())).thenReturn(annualInv);
	// when(calcService.updateGoalInfo(Mockito.any(Goal.class))).thenReturn(1);
	// when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//
	//
	// mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getGoal_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.[0].goalName",
	// is("Education"))).andReturn();
	// }

	// @Test
	// public void test_calculateGoal_UpdateError() throws Exception { //Error
	// GoalRequest goalRequest = new GoalRequest();
	// goalRequest.setReferenceId("P00000");
	// goalRequest.setGoalName("Education");
	// goalRequest.setTenure("1");
	// goalRequest.setTenureType("Mo");
	// goalRequest.setGoalAmount("100000");
	// goalRequest.setCurrentAmount("10000");
	// goalRequest.setInflationRate("2");
	// goalRequest.setGrowthRate("3");
	// goalRequest.setAnnualInvestmentRate("5");
	// goalRequest.setScreenId(1);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// double futureCost = 10000;
	// double futureValue = 1000;
	// double finalCorpus = 2000;
	// double monthlyInv = 700;
	// double annualInv = 12000;
	// Goal goal = new Goal();
	// goal.setGoalName("Education");
	// goal.setFutureCost(futureCost);
	// goal.setFutureValue(futureValue);
	// goal.setFinalCorpus(finalCorpus);
	// goal.setMonthlyInv(monthlyInv);
	// goal.setAnnualInv(annualInv);
	// List<Goal> goalList = new ArrayList<Goal>();
	// goalList.add(goal);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(goalRequest);
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
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
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(calcService.checkGoalIsPresentByRefIdAndGoalName(Mockito.anyString(),
	// Mockito.anyString())).thenReturn(0);
	// when(goalRequestValidator.validate(goalRequest)).thenReturn(null);
	// when(calcService.calculateGoalFutureCost(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureCost);
	// when(calcService.calculateGoalCurrentInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyInt()))
	// .thenReturn(futureValue);
	// when(calcService.calculateGoalFinalCorpus(Mockito.anyDouble(),
	// Mockito.anyDouble())).thenReturn(finalCorpus);
	// when(calcService.calculateGoalMonthlyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt(), Mockito.anyDouble())).thenReturn(monthlyInv);
	// when(calcService.calculateGoalAnnualyInvestment(Mockito.anyDouble(),
	// Mockito.anyDouble(), Mockito.anyDouble(),
	// Mockito.anyInt())).thenReturn(annualInv);
	//
	// when(calcService.updateGoalInfo(Mockito.any(Goal.class))).thenReturn(0);
	//
	// mockMvc.perform(post("/calculateGoal").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	// CASHFLOW
	@Test
	public void test_calculateCashFlow_AddSuccess() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);

		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		int cashFlowSummary = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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

		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		// when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		when(calcService.checkCashFlowSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(1);
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		CashFlowSummary cashFlowSummaryRes = new CashFlowSummary();
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchCashFlowSummaryByRefId(Mockito.anyString())).thenReturn(cashFlowSummaryRes);

		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCashflow_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.cashFlowList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.cashFlowSummary.monthlyIncome", is(0.0))).andReturn();
	}

	@Test
	public void test_calculateCashFlow_ScreenRights_AccessDenied() throws Exception {
		CashFlowRequest screenIdRequest = new CashFlowRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_calculateCashFlow_ScreenRights_Success() throws Exception {
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// CashFlowRequest cashFlowRequest = new CashFlowRequest();
	// cashFlowRequest.setReferenceId("P00000");
	// cashFlowRequest.setDate("12-2-1999");
	//
	// List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
	// CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
	// cashFlowItemReq.setCashFlowItemId(1);
	// cashFlowItemReq.setActualAmt("10000");
	// cashFlowItemReq.setBudgetAmt("1000");
	// cashFlowItemReqList.add(cashFlowItemReq);
	// cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);
	// cashFlowRequest.setScreenId(1);
	//
	// CashFlow cashFlow = new CashFlow();
	// cashFlow.setCashFlowItemTypeId(1);
	// cashFlow.setReferenceId("P00000");
	// cashFlow.setDate("12-2-1999");
	// cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
	// cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
	// cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());
	//
	// List<Integer> recurringExpItemType = new ArrayList<Integer>();
	// recurringExpItemType.add(1);
	// recurringExpItemType.add(2);
	// List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
	// cashFlowRecurringExp.add(cashFlow);
	// List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
	// cashFlowNonRecurringExp.add(cashFlow);
	// List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
	// cashFlowRecurringIncome.add(cashFlow);
	// List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
	// cashFlowNonRecurringIncome.add(cashFlow);
	//
	// List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
	// CashFlowItem cashFlowItem = new CashFlowItem();
	// cashFlowItem.setCashFlowItemId(1);
	// cashFlowItem.setCashFlowItemTypeId(1);
	// cashFlowItemList.add(cashFlowItem);
	// CashFlowItemType cashFlowItemType = new CashFlowItemType();
	// cashFlowItemType.setCashFlowItemTypeId(1);
	//
	// CashFlowSummary cashFlowSummary = new CashFlowSummary();
	// cashFlowSummary.setReferenceId("P00000");
	// cashFlowSummary.setCashFlowSummaryId(1);
	// cashFlowSummary.setMonthlyIncome(20000);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(cashFlowRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
	// when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
	// when(calcService.addCashFlow(Mockito.any(CashFlow.class))).thenReturn(1);
	// when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
	// when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(),
	// Mockito.anyInt()))
	// .thenReturn(cashFlowRecurringExp);
	// //
	// when(calcService.fetchNonRecurringExpenditureByRefId(Mockito.anyString())).thenReturn(cashFlowNonRecurringExp);
	// when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
	// //
	// when(calcService.fetchNonRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowNonRecurringIncome);
	// when(calcService.updateCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(1);//
	// when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
	// when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
	// when(calcService.fetchCashFlowByRefIdAndItemId("P00000",
	// 1)).thenReturn(null);
	// when(calcService.fetchCashFlowSummaryByRefId("P00000")).thenReturn(cashFlowSummary);
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
	// mockMvc.perform(post("/calculateCashFlow").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getCashflow_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.cashFlowList", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.cashFlowSummary.monthlyIncome",
	// is(20000.0)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateCashFlow_ScreenRights_UnAuthorized() throws Exception {
		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculateCashFlow").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_calculateCashFlow_ValidatorError() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);
		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(cashFlowRequestValidator.validate(Mockito.any(CashFlowRequest.class))).thenReturn(allErrors);
		mockMvc.perform(post("/calculateCashFlow").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1)));
	}

	@Test
	public void test_calculateCashFlow_AddError() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");

		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);
		cashFlowRequest.setScreenId(1);
		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setCashFlowSummaryId(1);
		cashFlowSummary.setMonthlyIncome(20000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkCashFlowSummaryIsPresent("P00000")).thenReturn(1);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(0);
		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculateCashFlow_NotFound() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);
		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setCashFlowSummaryId(1);
		cashFlowSummary.setMonthlyIncome(20000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);
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

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(0);
		when(calcService.checkCashFlowSummaryIsPresent("P00000")).thenReturn(1);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		// when(calcService.fetchNonRecurringExpenditureByRefId(Mockito.anyString())).thenReturn(cashFlowNonRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		// when(calcService.fetchNonRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowNonRecurringIncome);
		when(calcService.updateCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(1);//
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);

		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())));
	}

	// CASHFLOW
	@Test
	public void test_calculateCashFlow_UpdateSuccess() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);
		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		int cashFlowPresent = 1;
		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		int cashFlowSummary = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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
		int plan = 1;

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		when(calcService.checkCashFlowSummaryIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.updateCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(1);
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		CashFlowSummary cashFlowSummaryRes = new CashFlowSummary();
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchCashFlowSummaryByRefId(Mockito.anyString())).thenReturn(cashFlowSummaryRes);

		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCashflow_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.cashFlowList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.cashFlowSummary.monthlyIncome", is(0.0))).andReturn();
	}

	@Test
	public void test_calculateCashFlow_UpdateError() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);
		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		int cash = 1;
		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setCashFlowSummaryId(1);
		cashFlowSummary.setMonthlyIncome(20000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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
		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
		when(calcService.checkCashFlowIsPresent("P00000", 1)).thenReturn(cash);
		when(calcService.updateCashFlow(Mockito.any(CashFlow.class))).thenReturn(0);
		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculateCashFlow_UpdateSummarySuccess() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);

		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		when(calcService.checkCashFlowSummaryIsPresent(Mockito.anyString())).thenReturn(1);

		when(calcService.updateCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(1);
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		CashFlowSummary cashFlowSummaryRes = new CashFlowSummary();
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchCashFlowSummaryByRefId(Mockito.anyString())).thenReturn(cashFlowSummaryRes);

		mockMvc.perform(post("/calculateCashFlow").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCashflow_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.cashFlowList", hasSize(1))).andReturn();
	}

	@Test
	public void test_calculateCashFlow_UpdateSummaryError() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);
		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setCashFlowSummaryId(1);
		cashFlowSummary.setMonthlyIncome(20000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkCashFlowSummaryIsPresent("P00000")).thenReturn(1);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		when(calcService.updateCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(0);//
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);

		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured_adding_summary())))
				.andReturn();
	}

	@Test
	public void test_calculateCashFlow_AddSummarySuccess() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);

		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setCashFlowSummaryId(1);
		cashFlowSummary.setMonthlyIncome(20000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		when(calcService.checkCashFlowSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(1);
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		CashFlowSummary cashFlowSummaryRes = new CashFlowSummary();
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchCashFlowSummaryByRefId(Mockito.anyString())).thenReturn(cashFlowSummaryRes);

		mockMvc.perform(post("/calculateCashFlow").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCashflow_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.cashFlowList", hasSize(1))).andReturn();
	}

	@Test
	public void test_calculateCashFlow_AddSummaryError() throws Exception {

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		CashFlowRequest cashFlowRequest = new CashFlowRequest();
		cashFlowRequest.setReferenceId("P00000");
		cashFlowRequest.setDate("12-2-1999");
		cashFlowRequest.setScreenId(1);
		List<CashFlowItemReq> cashFlowItemReqList = new ArrayList<CashFlowItemReq>();
		CashFlowItemReq cashFlowItemReq = new CashFlowItemReq();
		cashFlowItemReq.setCashFlowItemId(1);
		cashFlowItemReq.setActualAmt("10000");
		cashFlowItemReq.setBudgetAmt("1000");
		cashFlowItemReqList.add(cashFlowItemReq);
		cashFlowRequest.setCashFlowItemReq(cashFlowItemReqList);

		CashFlow cashFlow = new CashFlow();
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setReferenceId("P00000");
		cashFlow.setDate("12-2-1999");
		cashFlow.setActualAmt(Double.parseDouble(cashFlowItemReq.getActualAmt()));
		cashFlow.setBudgetAmt(Double.parseDouble(cashFlowItemReq.getBudgetAmt()));
		cashFlow.setCashFlowItemId(cashFlowItemReq.getCashFlowItemId());

		List<Integer> recurringExpItemType = new ArrayList<Integer>();
		recurringExpItemType.add(1);
		recurringExpItemType.add(2);
		List<CashFlow> cashFlowRecurringExp = new ArrayList<CashFlow>();
		cashFlowRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringExp = new ArrayList<CashFlow>();
		cashFlowNonRecurringExp.add(cashFlow);
		List<CashFlow> cashFlowRecurringIncome = new ArrayList<CashFlow>();
		cashFlowRecurringIncome.add(cashFlow);
		List<CashFlow> cashFlowNonRecurringIncome = new ArrayList<CashFlow>();
		cashFlowNonRecurringIncome.add(cashFlow);

		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setCashFlowSummaryId(1);
		cashFlowSummary.setMonthlyIncome(20000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(cashFlowRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkCashFlowSummaryIsPresent("P00000")).thenReturn(0);
		when(cashFlowRequestValidator.validate(cashFlowRequest)).thenReturn(null);
		when(calcService.fetchCashFlowItemTypeIdByItemId(Mockito.anyLong())).thenReturn(1);
		when(calcService.addAndModifyCashFlow(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRecurringExpenditureItemType()).thenReturn(recurringExpItemType);
		when(calcService.fetchCashFlowByRefIdAndTypeId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(cashFlowRecurringExp);
		when(calcService.fetchRecurringIncomeByRefId(Mockito.anyString())).thenReturn(cashFlowRecurringIncome);
		when(calcService.addCashFlowSummary(Mockito.any(CashFlowSummary.class))).thenReturn(0);
		when(calcService.fetchCashFlowItemTypeByTypeId(Mockito.anyLong())).thenReturn(cashFlowItemType);
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(null);

		mockMvc.perform(post("/calculateCashFlow").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured_adding_summary())))
				.andReturn();
	}

	// NETWORTH
	@Test
	public void test_calculateNetworth_AddSuccess() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.updateNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(1);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId("P00000", 1)).thenReturn(null);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		when(calcService.checkNetworthSummaryIsPresent("P00000")).thenReturn(1);

		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getNetworth_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.networthList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.networth", is(300.0)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.future_networth", is(1000.0))).andReturn();
	}

	@Test
	public void test_calculateNetworth_ScreenRights_AccessDenied() throws Exception {
		NetworthRequest screenIdRequest = new NetworthRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_calculateNetworth_ScreenRights_Success() throws Exception {
	// List<Account> accountList = new ArrayList<Account>();
	// Account account = new Account();
	// account.setAccountEntryId(1);
	// account.setAccountTypeId(1);
	// accountList.add(account);
	// AccountType accountType = new AccountType();
	// accountType.setAccountTypeId(1);
	// accountType.setAccountType("Assests");
	//
	// NetworthRequest networthRequest = new NetworthRequest();
	// networthRequest.setReferenceId("P00000");
	// networthRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
	// NetworthReq networthReq = new NetworthReq();
	// networthReq.setAccountEntryId(1);
	// networthReq.setValue("120000");
	// networthReq.setFutureValue("148000");
	// networthReqList.add(networthReq);
	// networthRequest.setNetworthReq(networthReqList);
	//
	// Networth networth = new Networth();
	// networth.setAccountEntryId(1);
	// networth.setAccountTypeId(1);
	// networth.setReferenceId("P00000");
	// networth.setValue(120000);
	// networth.setFutureValue(148000);
	//
	// List<Networth> networthAssetsList = new ArrayList<Networth>();
	// networthAssetsList.add(networth);
	// List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
	// networthLiabilitiesList.add(networth);
	//
	// Integer accountTypeId = 1;
	// NetworthSummary networthSummary = new NetworthSummary();
	// networthSummary.setReferenceId("P00000");
	// networthSummary.setFuture_networth(1000);
	// networthSummary.setNetworth(300);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(networthRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
	// when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
	// when(calcService.addNetworth(Mockito.any(Networth.class))).thenReturn(1);
	// when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
	// when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
	// when(calcService.updateNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(1);
	// when(calcService.fetchAccountList()).thenReturn(accountList);
	// when(calcService.fetchNetworthByRefIdAndEntryId("P00000",
	// 1)).thenReturn(null);
	// when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
	// when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
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
	// mockMvc.perform(post("/calculateNetworth").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNetworth_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.networthList", hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.networthSummary.networth",
	// is(300.0)))
	// .andExpect(jsonPath("$.responseData.data.networthSummary.future_networth",
	// is(1000.0)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateNetworth_ScreenRights_UnAuthorized() throws Exception {
		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculateNetworth").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_calculateNetworth_ValidatorError() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(networthRequestValidator.validate(Mockito.any(NetworthRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/calculateNetworth").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculateNetworth_AddError() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(0);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// @Test // condition removed
	public void test_calculateNetworth_UpdateSuccess() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		int networthPresent = 1;
		NetworthSummary networthSummary = new NetworthSummary();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);
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

		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.checkNetworthSummaryIsPresent("P00000")).thenReturn(1);
		when(calcService.updateNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(1);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		mockMvc.perform(post("/calculateNetworth").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getNetworth_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.networthList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.networth", is(300.0)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.future_networth", is(1000.0))).andReturn();
	}

	@Test
	public void test_calculateNetworth_UpdateError() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		int networthPresent = 1;
		NetworthSummary networthSummary = new NetworthSummary();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);
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

		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(0);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.checkNetworthSummaryIsPresent("P00000")).thenReturn(1);
		when(calcService.updateNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(0);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculateNetworth_AddSummarySuccess() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);
		networthRequest.setScreenId(1);
		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.checkNetworthSummaryIsPresent("P00000")).thenReturn(0);
		when(calcService.addNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(1);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId("P00000", 1)).thenReturn(null);
		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);

		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getNetworth_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.networthList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.networthList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.networth", is(300.0)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.future_networth", is(1000.0))).andReturn();
	}

	@Test
	public void test_calculateNetworth_AddSummaryError() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.checkNetworthSummaryIsPresent("P00000")).thenReturn(0);
		when(calcService.addNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(0);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured_adding_summary())))
				.andReturn();
	}

	@Test
	public void test_calculateNetworth_UpdateSummarySuccess() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		when(calcService.checkNetworthSummaryIsPresent("P00000")).thenReturn(1);
		when(calcService.updateNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(1);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId("P00000", 1)).thenReturn(null);
		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getNetworth_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.networthList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.networth", is(300.0)))
				.andExpect(jsonPath("$.responseData.data.networthSummary.future_networth", is(1000.0))).andReturn();
	}

	@Test
	public void test_calculateNetworth_UpdateSummaryError() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		Integer accountTypeId = 1;
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		when(networthRequestValidator.validate(networthRequest)).thenReturn(null);
		when(calcService.fetchAccountTypeIdByEntryId(Mockito.anyInt())).thenReturn(accountTypeId);
		when(calcService.addAndModifyNetworth(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchNetworthByAssets(Mockito.anyString())).thenReturn(networthAssetsList);
		when(calcService.fetchNetworthByLiabilities(Mockito.anyString())).thenReturn(networthLiabilitiesList);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		when(calcService.updateNetworthSummary(Mockito.any(NetworthSummary.class))).thenReturn(0);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId("P00000", 1)).thenReturn(null);
		when(calcService.fetchAccountTypeByTypeId(Mockito.anyLong())).thenReturn(accountType);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured_adding_summary())))
				.andReturn();
	}

	@Test
	public void test_calculateNetworth_NotFound() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");

		NetworthRequest networthRequest = new NetworthRequest();
		networthRequest.setReferenceId("P00000");
		networthRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<NetworthReq> networthReqList = new ArrayList<NetworthReq>();
		NetworthReq networthReq = new NetworthReq();
		networthReq.setAccountEntryId(1);
		networthReq.setValue("120000");
		networthReq.setFutureValue("148000");
		networthReqList.add(networthReq);
		networthRequest.setNetworthReq(networthReqList);

		Networth networth = new Networth();
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(148000);

		List<Networth> networthAssetsList = new ArrayList<Networth>();
		networthAssetsList.add(networth);
		List<Networth> networthLiabilitiesList = new ArrayList<Networth>();
		networthLiabilitiesList.add(networth);

		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(1000);
		networthSummary.setNetworth(300);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(networthRequest);

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

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(0);
		when(calcService.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		mockMvc.perform(post("/calculateNetworth").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	// PRIORITIES
	@Test
	public void test_calculatePriorities_AddSuccess() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);
		priorityRequest.setScreenId(1);
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPriorityByRefIdAndItemId(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);

		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPriority_added_successfully())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].priorityItemId", is(1))).andReturn();
	}

	@Test
	public void test_calculatePriorities_ScreenRights_AccessDenied() throws Exception {
		PriorityRequest screenIdRequest = new PriorityRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_calculatePriorities_ScreenRights_UnAuthorized() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculatePriorities").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_calculatePriorities_ScreenRights_Success() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");
		priorityRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);

		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPriorityByRefIdAndItemId(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);
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

		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPriority_added_successfully())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].priorityItemId", is(1))).andReturn();
	}

	@Test
	public void test_calculatePriorities_AddError() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");
		priorityRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);

		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPriorityByRefIdAndItemId(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(0);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		when(calcService.fetchPriorityByRefIdAndItemId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);
		when(calcService.fetchPriorityByRefIdAndItemId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test // condition removed
	public void test_calculatePriorities_UpdateSuccess() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);
		priorityRequest.setScreenId(1);
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		when(calcService.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(priority);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPriority_added_successfully())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].priorityItemId", is(1))).andReturn();
	}

	@Test
	public void test_calculatePriorities_UpdateError() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);

		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);
		priorityRequest.setScreenId(1);
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		// when(priorityRequestValidator.validate(priorityRequest)).thenReturn(null);
		// when(calcService.updatePriority(Mockito.any(Priority.class))).thenReturn(0);

		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(0);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		when(calcService.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		// when(calcService.fetchPriorityByRefIdAndItemId(Mockito.anyString(),
		// Mockito.anyInt())).thenReturn(priority);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculatePriorities_NotFound() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		// priorityReq.setTimeline("2");
		priorityReq.setUrgencyId(1);
		// priorityReq.setValue("120000");
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);
		priorityRequest.setScreenId(1);
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		// priority.setTimeLine(Integer.parseInt(priorityReq.getTimeline()));
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);
		// priority.setValue(Double.parseDouble(priorityReq.getValue()));

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);
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

		when(calcService.fetchParty(Mockito.anyLong())).thenReturn(null);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_calculatePriorities_UpdateOrderSuccess() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");
		priorityRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);

		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		// when(calcService.checkPriorityByRefIdAndItemId(Mockito.anyString(),
		// Mockito.anyInt())).thenReturn(1);
		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
		when(calcService.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPriority_added_successfully())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].priorityItemId", is(1))).andReturn();
	}

	@Test
	public void test_calculatePriorities_UpdateOrderError() throws Exception {
		PriorityRequest priorityRequest = new PriorityRequest();
		priorityRequest.setReferenceId("P00000");
		priorityRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		List<PriorityReq> priorityReqList = new ArrayList<PriorityReq>();
		PriorityReq priorityReq = new PriorityReq();
		priorityReq.setPriorityItemId(1);
		priorityReq.setUrgencyId(1);
		priorityReqList.add(priorityReq);
		priorityRequest.setPriorityReq(priorityReqList);

		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);

		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(priorityRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPriorityByRefIdAndItemId(Mockito.anyString(), Mockito.anyInt())).thenReturn(1);
		when(calcService.addAndModifyPriorities(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchPriorityByRefId(Mockito.anyString())).thenReturn(priorityList);
		when(calcService.fetchPriorityItemByItemId(Mockito.anyInt())).thenReturn(priorityItem);
		when(calcService.updatePriorityOrder(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(0);
		when(calcService.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(null);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		mockMvc.perform(post("/calculatePriorities").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getError_occured_update_priority_order())))
				.andReturn();
	}

	@Test
	public void test_calculateInsurance_AddSuccess() throws Exception {
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P00000");
		insuranceRequest.setStability("STABLE");
		insuranceRequest.setPredictability("PREDICTABLE");
		insuranceRequest.setExistingInsurance("10000");
		insuranceRequest.setAnnualIncome("20000");
		insuranceRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		Insurance insurance = new Insurance();
		insurance.setAnnualIncome(800000);
		insurance.setAdditionalInsurance(12000);
		insurance.setRequiredInsurance(2000);
		insurance.setReferenceId("P00000");
		InsuranceStringItem stability = new InsuranceStringItem();
		stability.setValue("STABLE");
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		insuranceItem.setStability(stability);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(insuranceRequestValidator.validate(insuranceRequest)).thenReturn(null);
		when(calcService.checkInsuranceByRefId(Mockito.anyString())).thenReturn(0);
		when(calcService.addInsurance(Mockito.any(Insurance.class))).thenReturn(1);
		when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);

		mockMvc.perform(post("/calculateInsurance").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInsurance_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.referenceId", is("P00000")))
				.andExpect(jsonPath("$.responseData.data.insuranceId", is(1))).andReturn();
	}

	@Test
	public void test_calculateInsurance_ScreenRights_AccessDenied() throws Exception {
		InsuranceRequest screenIdRequest = new InsuranceRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateInsurance").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_calculateInsurance_ScreenRights_Success() throws Exception {
	// //Error
	// InsuranceRequest insuranceRequest = new InsuranceRequest();
	// insuranceRequest.setReferenceId("P00000");
	// insuranceRequest.setStability("STABLE");
	// insuranceRequest.setPredictability("PREDICTABLE");
	// insuranceRequest.setExistingInsurance("10000");
	// insuranceRequest.setAnnualIncome("20000");
	// insuranceRequest.setScreenId(1);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setPartyStatusId(1);
	//
	// Insurance insurance = new Insurance();
	// insurance.setAnnualIncome(800000);
	// insurance.setAdditionalInsurance(12000);
	// insurance.setRequiredInsurance(2000);
	// insurance.setReferenceId("P00000");
	// InsuranceStringItem stability = new InsuranceStringItem();
	// stability.setValue("STABLE");
	// InsuranceItem insuranceItem = new InsuranceItem();
	// insuranceItem.setReferenceId("P00000");
	// insuranceItem.setInsuranceId(1);
	// insuranceItem.setStability(stability);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(insuranceRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(insuranceRequestValidator.validate(insuranceRequest)).thenReturn(null);
	// when(calcService.fetchInsuranceByRefId(Mockito.anyString())).thenReturn(null);
	// when(calcService.addInsurance(Mockito.any(Insurance.class))).thenReturn(1);
	// when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);
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
	// mockMvc.perform(post("/calculateInsurance").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getInsurance_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.referenceId", is("P00000")))
	// .andExpect(jsonPath("$.responseData.data.insuranceId", is(1)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateInsurance_ScreenRights_UnAuthorized() throws Exception {
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculateInsurance").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_calculateInsurance_ValidatorError() throws Exception {

		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P00000");
		insuranceRequest.setStability("STABLE");
		insuranceRequest.setPredictability("PREDICTABLE");
		insuranceRequest.setExistingInsurance("10000");
		insuranceRequest.setAnnualIncome("20000");
		insuranceRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(insuranceRequestValidator.validate(Mockito.any(InsuranceRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/calculateInsurance").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculateInsurance_NotFound() throws Exception {
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P00000");
		insuranceRequest.setStability("STABLE");
		insuranceRequest.setPredictability("PREDICTABLE");
		insuranceRequest.setExistingInsurance("10000");
		insuranceRequest.setAnnualIncome("20000");
		insuranceRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		Insurance insurance = new Insurance();
		insurance.setAnnualIncome(800000);
		insurance.setAdditionalInsurance(12000);
		insurance.setRequiredInsurance(2000);
		insurance.setReferenceId("P00000");
		InsuranceStringItem stability = new InsuranceStringItem();
		stability.setValue("STABLE");
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		insuranceItem.setStability(stability);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);

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

		when(calcService.fetchParty(Mockito.anyLong())).thenReturn(null);
		mockMvc.perform(post("/calculateInsurance").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_calculateInsurance_AddError() throws Exception {
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P00000");
		insuranceRequest.setStability("STABLE");
		insuranceRequest.setPredictability("PREDICTABLE");
		insuranceRequest.setExistingInsurance("10000");
		insuranceRequest.setAnnualIncome("20000");
		insuranceRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		Insurance insurance = new Insurance();
		insurance.setAnnualIncome(800000);
		insurance.setAdditionalInsurance(12000);
		insurance.setRequiredInsurance(2000);
		insurance.setReferenceId("P00000");
		InsuranceStringItem stability = new InsuranceStringItem();
		stability.setValue("STABLE");
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		insuranceItem.setStability(stability);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(insuranceRequestValidator.validate(insuranceRequest)).thenReturn(null);
		when(calcService.checkInsuranceByRefId(Mockito.anyString())).thenReturn(0);
		when(calcService.addInsurance(Mockito.any(Insurance.class))).thenReturn(0);
		when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);

		mockMvc.perform(post("/calculateInsurance").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculateInsurance_UpdateSuccess() throws Exception {
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P00000");
		insuranceRequest.setStability("STABLE");
		insuranceRequest.setPredictability("PREDICTABLE");
		insuranceRequest.setExistingInsurance("10000");
		insuranceRequest.setAnnualIncome("20000");
		insuranceRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		Insurance insurance = new Insurance();
		insurance.setAnnualIncome(800000);
		insurance.setAdditionalInsurance(12000);
		insurance.setRequiredInsurance(2000);
		insurance.setReferenceId("P00000");
		InsuranceStringItem stability = new InsuranceStringItem();
		stability.setValue("STABLE");
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		insuranceItem.setStability(stability);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(insuranceRequestValidator.validate(insuranceRequest)).thenReturn(null);
		when(calcService.checkInsuranceByRefId(Mockito.anyString())).thenReturn(1);
		when(calcService.updateInsurance(Mockito.any(Insurance.class))).thenReturn(1);
		when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);

		mockMvc.perform(post("/calculateInsurance").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInsurance_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.referenceId", is("P00000")))
				.andExpect(jsonPath("$.responseData.data.insuranceId", is(1))).andReturn();
	}

	@Test
	public void test_calculateInsurance_UpdateError() throws Exception {
		InsuranceRequest insuranceRequest = new InsuranceRequest();
		insuranceRequest.setReferenceId("P00000");
		insuranceRequest.setStability("STABLE");
		insuranceRequest.setPredictability("PREDICTABLE");
		insuranceRequest.setExistingInsurance("10000");
		insuranceRequest.setAnnualIncome("20000");
		insuranceRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);

		Insurance insurance = new Insurance();
		insurance.setAnnualIncome(800000);
		insurance.setAdditionalInsurance(12000);
		insurance.setRequiredInsurance(2000);
		insurance.setReferenceId("P00000");
		InsuranceStringItem stability = new InsuranceStringItem();
		stability.setValue("STABLE");
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		insuranceItem.setStability(stability);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(insuranceRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(insuranceRequestValidator.validate(insuranceRequest)).thenReturn(null);
		when(calcService.checkInsuranceByRefId(Mockito.anyString())).thenReturn(1);
		when(calcService.updateInsurance(Mockito.any(Insurance.class))).thenReturn(0);
		when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);

		mockMvc.perform(post("/calculateInsurance").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	// RISKPROFILE
	@Test
	public void test_calculateRiskProfile_AddSuccess() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P0000000000");

		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);
		riskProfileRequest.setScreenId(1);
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P0000000000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P0000000000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);

		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.riskProfileList", IsCollectionWithSize.hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.riskSummary.referenceId", is("P0000000000")))
				.andExpect(jsonPath("$.responseData.data.riskSummary.behaviour", is("High Growth Investor")))
				.andReturn();
	}

	// @Test//need to change relate to update code
	// public void test_calculateRiskProfile_ScreenRights_AccessDenied() throws
	// Exception {
	// RiskProfileRequest screenIdRequest = new RiskProfileRequest();
	// screenIdRequest.setScreenId(1);
	// screenIdRequest.setReferenceId("P0000000000");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	// @Test //null pointer
	// public void test_calculateRiskProfile_ScreenRights_Success() throws Exception
	// {
	// RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
	// riskProfileRequest.setReferenceId("P0000000000");
	// riskProfileRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
	// RiskProfileReq riskProfileReq = new RiskProfileReq();
	// riskProfileReq.setQuestionId("1");
	// riskProfileReq.setAnswerId(1);
	// riskProfileReqList.add(riskProfileReq);
	// riskProfileRequest.setRiskProfileReq(riskProfileReqList);
	//
	// RiskProfile riskProfile = new RiskProfile();
	// riskProfile.setReferenceId("P0000000000");
	// riskProfile.setQuestionId("1");
	// riskProfile.setAnswerId(1);
	// riskProfile.setScore(1);
	//
	// List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
	// riskProfileList.add(riskProfile);
	//
	// RiskPortfolio riskPortFolio = new RiskPortfolio();
	// riskPortFolio.setRiskPortfolioId(1);
	// riskPortFolio.setBehaviour("High Growth Investor");
	//
	// RiskSummary riskSummary = new RiskSummary();
	// riskSummary.setRiskSummaryId(1);
	// riskSummary.setReferenceId("P00000");
	// riskSummary.setBehaviour("High Growth Investor");
	//
	// List<RiskQuestionaire> riskQuestionaireList = new
	// ArrayList<RiskQuestionaire>();
	// RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
	// riskQuestionaire.setAnswerId(1);
	// riskQuestionaireList.add(riskQuestionaire);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(riskProfileRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// // when(calcService.fetchRiskProfileByRefIdAndQuestionId("P00000",
	// // "1")).thenReturn(null);
	// when(calcService.addAndModifyRiskProfile(riskProfileList)).thenReturn(1);
	// //
	// when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null);
	// when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
	// when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
	// when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
	// when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
	// when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
	// when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);
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
	// mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getRiskprofile_added_successfully())))
	// .andExpect(jsonPath("$.responseData.data.riskProfileList",
	// IsCollectionWithSize.hasSize(1)))
	// .andExpect(jsonPath("$.responseData.data.riskSummary.referenceId",
	// is("P0000000000")))
	// .andExpect(jsonPath("$.responseData.data.riskSummary.behaviour", is("High
	// Growth Investor")))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test//need to change relate to update code
	// public void test_calculateRiskProfile_ScreenRights_UnAuthorized() throws
	// Exception {
	// RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
	// riskProfileRequest.setReferenceId("P0000000000");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(riskProfileRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/calculateRiskProfile").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_calculateRiskProfile_AddError() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P0000000000");
		riskProfileRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);

		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P0000000000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P0000000000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		plan.setReferenceId("P0000000000");

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(0);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);

		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andReturn();
	}

	// @Test //condition removed
	public void test_calculateRiskProfile_UpdateSuccess() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P00000");

		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);
		riskProfileRequest.setScreenId(1);
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		// when(calcService.checkRiskProfileIsPresent("P00000", "1")).thenReturn(1);
		// when(calcService.updateRiskProfile(Mockito.any(RiskProfile.class))).thenReturn(1);
		// when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		// when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		// when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		// when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null).thenReturn(riskSummary);
		// when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);

		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.riskProfileList", IsCollectionWithSize.hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.riskSummary.referenceId", is("P00000")))
				.andExpect(jsonPath("$.responseData.data.riskSummary.behaviour", is("High Growth Investor")))
				.andReturn();
	}

	// @Test
	// public void test_calculateRiskProfile_UpdateError() throws Exception {
	// RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
	// riskProfileRequest.setReferenceId("P00000");
	// riskProfileRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
	// RiskProfileReq riskProfileReq = new RiskProfileReq();
	// riskProfileReq.setQuestionId("1");
	// riskProfileReq.setAnswerId(1);
	// riskProfileReqList.add(riskProfileReq);
	// riskProfileRequest.setRiskProfileReq(riskProfileReqList);
	//
	// RiskProfile riskProfile = new RiskProfile();
	// riskProfile.setReferenceId("P00000");
	// riskProfile.setQuestionId("1");
	// riskProfile.setAnswerId(1);
	// riskProfile.setScore(1);
	//
	// List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
	// riskProfileList.add(riskProfile);
	//
	// RiskPortfolio riskPortFolio = new RiskPortfolio();
	// riskPortFolio.setRiskPortfolioId(1);
	// riskPortFolio.setBehaviour("High Growth Investor");
	//
	// RiskSummary riskSummary = new RiskSummary();
	// riskSummary.setRiskSummaryId(1);
	// riskSummary.setReferenceId("P00000");
	// riskSummary.setBehaviour("High Growth Investor");
	//
	// List<RiskQuestionaire> riskQuestionaireList = new
	// ArrayList<RiskQuestionaire>();
	// RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
	// riskQuestionaire.setAnswerId(1);
	// riskQuestionaireList.add(riskQuestionaire);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(riskProfileRequest);
	//
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(calcService.checkRiskProfileIsPresent("P00000", "1")).thenReturn(1);
	// when(calcService.updateRiskProfile(Mockito.any(RiskProfile.class))).thenReturn(0);
	// when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
	// when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
	// when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
	// when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null).thenReturn(riskSummary);
	// when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
	// mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_calculateRiskProfile_AddSummarySuccess() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P00000");
		riskProfileRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);

		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);

		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.riskProfileList", IsCollectionWithSize.hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.riskSummary.referenceId", is("P00000")))
				.andExpect(jsonPath("$.responseData.data.riskSummary.behaviour", is("High Growth Investor")))
				.andReturn();
	}

	@Test
	public void test_calculateRiskProfile_AddSummaryError() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P00000");
		riskProfileRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);

		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkRiskProfileIsPresent("P00000", "1")).thenReturn(0);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null).thenReturn(riskSummary);
		when(calcService.addRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(0);

		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_calculateRiskProfile_UpdateSummarySuccess() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P00000");
		riskProfileRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);

		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkRiskProfileIsPresent("P00000", "1")).thenReturn(0);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);
		when(calcService.checkRiskSummaryIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.updateRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(1);
		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andExpect(jsonPath("$.responseData.data.riskProfileList", IsCollectionWithSize.hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.riskSummary.referenceId", is("P00000")))
				.andExpect(jsonPath("$.responseData.data.riskSummary.behaviour", is("High Growth Investor")))
				.andReturn();
	}

	@Test
	public void test_calculateRiskProfile_UpdateSummaryError() throws Exception {
		RiskProfileRequest riskProfileRequest = new RiskProfileRequest();
		riskProfileRequest.setReferenceId("P0000000000");
		riskProfileRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<RiskProfileReq> riskProfileReqList = new ArrayList<RiskProfileReq>();
		RiskProfileReq riskProfileReq = new RiskProfileReq();
		riskProfileReq.setQuestionId("1");
		riskProfileReq.setAnswerId(1);
		riskProfileReqList.add(riskProfileReq);
		riskProfileRequest.setRiskProfileReq(riskProfileReqList);

		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P0000000000");
		riskProfile.setQuestionId("1");
		riskProfile.setAnswerId(1);
		riskProfile.setScore(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		riskProfileList.add(riskProfile);

		RiskPortfolio riskPortFolio = new RiskPortfolio();
		riskPortFolio.setRiskPortfolioId(1);
		riskPortFolio.setBehaviour("High Growth Investor");

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setRiskSummaryId(1);
		riskSummary.setReferenceId("P0000000000");
		riskSummary.setBehaviour("High Growth Investor");

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(riskProfileRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkRiskProfileIsPresent("P0000000000", "1")).thenReturn(0);
		when(calcService.fetchRiskProfileByRefIdAndQuestionId("P0000000000", "1")).thenReturn(null);
		when(calcService.addAndModifyRiskProfile(Mockito.anyList())).thenReturn(1);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(null);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskPortfolioByPoints(Mockito.anyString())).thenReturn(riskPortFolio);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);
		when(calcService.updateRiskSummary(Mockito.any(RiskSummary.class))).thenReturn(0);
		mockMvc.perform(post("/calculateRiskProfile").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRiskprofile_added_successfully())))
				.andReturn();
	}

	// FutureValue
	@Test
	public void test_calculateFutureValue_LUMSUM() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("3");
		futureValueRequest.setDuration("5");
		futureValueRequest.setInvAmount("2000");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);

		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getValue_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.totalPayment", is(2318.55))).andReturn();
	}

	@Test
	public void test_calculateFutureValue_LUMSUM_ZeroError() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("0");
		futureValueRequest.setDuration("0");
		futureValueRequest.setInvAmount("0");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);

		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculateFutureValue_LUMSUM_DurationZeroError() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("1");
		futureValueRequest.setDuration("0");
		futureValueRequest.setInvAmount("156500");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);

		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "DURATION")));
	}

	@Test
	public void test_calculateFutureValue_LUMSUM_GrowthZeroError() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("0");
		futureValueRequest.setDuration("10");
		futureValueRequest.setInvAmount("156500");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);

		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "ANNUAL GROWTH RATE")));
	}

	@Test
	public void test_calculateFutureValue_LUMSUM_InvAmtError() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("1");
		futureValueRequest.setDuration("10");
		futureValueRequest.setInvAmount("0");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);

		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INVESTMENT AMOUNT")));
	}

	@Test
	public void test_calculateFutureValue_LUMSUM_TwoFieldZeroError() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("0");
		futureValueRequest.setDuration("0");
		futureValueRequest.setInvAmount("2000");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);

		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	// @Test
	// public void test_FutureValue_ScreenRights_AccessDenied() throws Exception {
	// FutureValueRequest screenIdRequest = new FutureValueRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	// @Test
	// public void test_calculateFutureValue_LUMSUM_ScreenRights_Success() throws
	// Exception {
	// FutureValueRequest futureValueRequest = new FutureValueRequest();
	// futureValueRequest.setInvType("LUMSUM");
	// futureValueRequest.setAnnualGrowth("3");
	// futureValueRequest.setDuration("5");
	// futureValueRequest.setInvAmount("2000");
	// futureValueRequest.setDurationType("YEAR");
	// futureValueRequest.setScreenId(1);
	//
	// TotalValueResponse response = new TotalValueResponse();
	// response.setTotalPayment(2318.55);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(futureValueRequest);
	//
	// when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);
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
	// mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getValue_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.totalPayment", is(2318.55)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test
	// public void test_calculateFutureValue_ScreenRights_UnAuthorized() throws
	// Exception {
	// FutureValueRequest futureValueRequest = new FutureValueRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(futureValueRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/IP-FutureValue").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_calculateFutureValue_ValidatorError() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("LUMSUM");
		futureValueRequest.setAnnualGrowth("3");
		futureValueRequest.setDuration("5");
		futureValueRequest.setInvAmount("2000");
		futureValueRequest.setScreenId(1);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(Mockito.any(FutureValueRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculateFutureValue_SIP() throws Exception {
		FutureValueRequest futureValueRequest = new FutureValueRequest();
		futureValueRequest.setInvType("SIP");
		futureValueRequest.setAnnualGrowth("3");
		futureValueRequest.setDuration("5");
		futureValueRequest.setInvAmount("2000");
		// futureValueRequest.setYearlyIncrease("5");
		futureValueRequest.setDurationType("YEAR");
		futureValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(2318.55);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(futureValueRequest);

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

		when(futureValueRequestValidator.validate(futureValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-FutureValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getValue_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.totalPayment", is(2318.55))).andReturn();
	}

	// // @Test
	// // public void test_calculateFutureValueAnnuity() throws Exception {
	// // FutureValueAnnuityRequest futureValueAnnuityRequest = new
	// // FutureValueAnnuityRequest();
	// // futureValueAnnuityRequest.setAnnualGrowth("3");
	// // futureValueAnnuityRequest.setDuration("5");
	// // futureValueAnnuityRequest.setInvAmount("2000");
	// // futureValueAnnuityRequest.setYearlyIncrease("5");
	// //
	// // TotalValueResponse response = new TotalValueResponse();
	// // response.setTotalPayment(11700.75);
	// // ObjectMapper mapper = new ObjectMapper();
	// // String jsonStringResult = mapper.writeValueAsString(response);
	// // String jsonString = mapper.writeValueAsString(futureValueAnnuityRequest);

	// //
	// //
	// when(futureValueAnnuityRequestValidator.validate(futureValueAnnuityRequest)).thenReturn(null);
	// //
	// // MvcResult result = mockMvc
	// // .perform(post("/calculateFutureValueAnnuity").content(jsonString)
	// // .contentType(MediaType.APPLICATION_JSON))
	// //
	// .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	// // Assert.assertEquals(result.getResponse().getContentAsString(),
	// // jsonStringResult);
	// //
	// // }
	//
	// // @Test
	// // public void test_calculateFutureValueAnnuityDue() throws Exception {
	// // FutureValueAnnuityDueRequest futureValueAnnuityDueRequest = new
	// // FutureValueAnnuityDueRequest();
	// // futureValueAnnuityDueRequest.setAnnualGrowth("3");
	// // futureValueAnnuityDueRequest.setDuration("5");
	// // futureValueAnnuityDueRequest.setInvAmount("2000");
	// // futureValueAnnuityDueRequest.setYearlyIncrease("5");
	// //
	// // TotalValueResponse response = new TotalValueResponse();
	// // response.setTotalPayment(11700.75);
	// // ObjectMapper mapper = new ObjectMapper();
	// // String jsonStringResult = mapper.writeValueAsString(response);
	// // String jsonString =
	// mapper.writeValueAsString(futureValueAnnuityDueRequest);
	// //
	// //
	//
	// when(futureValueAnnuityDueRequestValidator.validate(futureValueAnnuityDueRequest)).thenReturn(null);
	// //
	// // MvcResult result = mockMvc
	// // .perform(post("/calculateFutureValueAnnuity").content(jsonString)
	// // .contentType(MediaType.APPLICATION_JSON))
	// //
	//
	// .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	// // Assert.assertEquals(result.getResponse().getContentAsString(),
	// // jsonStringResult);
	// //
	// // }
	//
	// Present Value
	@Test
	public void test_calculatePresentValue_LUMSUM() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("500000");
		presentValueRequest.setDuration("10");
		presentValueRequest.setRateOfInterest("5");
		presentValueRequest.setDurationType("YEAR");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getValue_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.totalPayment", is(306956.63))).andReturn();
	}

	@Test
	public void test_calculatePresentValue_LUMSUM_ZeroError() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("0");
		presentValueRequest.setDuration("0");
		presentValueRequest.setRateOfInterest("0");
		presentValueRequest.setDurationType("YEAR");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculatePresentValue_LUMSUM_FutureValueZeroError() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("0");
		presentValueRequest.setDuration("10");
		presentValueRequest.setRateOfInterest("1");
		presentValueRequest.setDurationType("YEAR");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "FUTURE VALUE")));
	}

	@Test
	public void test_calculatePresentValue_LUMSUM_DurationZeroError() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("1689870");
		presentValueRequest.setDuration("0");
		presentValueRequest.setRateOfInterest("1");
		presentValueRequest.setDurationType("YEAR");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "DURATION")));
	}

	@Test
	public void test_calculatePresentValue_LUMSUM_RateZeroError() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("1689870");
		presentValueRequest.setDuration("10");
		presentValueRequest.setRateOfInterest("0");
		presentValueRequest.setDurationType("YEAR");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE")));
	}

	@Test
	public void test_calculatePresentValue_LUMSUM_TwoFieldZeroError() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("50000");
		presentValueRequest.setDuration("0");
		presentValueRequest.setRateOfInterest("0");
		presentValueRequest.setDurationType("YEAR");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	// @Test
	// public void test_TargetValue_ScreenRights_AccessDenied() throws Exception {
	// TargetValueRequest screenIdRequest = new TargetValueRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	// @Test
	// public void test_calculatePresentValue_LUMSUM_ScreenRights_Success() throws
	// Exception {
	// TargetValueRequest presentValueRequest = new TargetValueRequest();
	// presentValueRequest.setInvType("LUMSUM");
	// presentValueRequest.setFutureValue("500000");
	// presentValueRequest.setDuration("10");
	// presentValueRequest.setRateOfInterest("5");
	// presentValueRequest.setDurationType("YEAR");
	// presentValueRequest.setScreenId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// TotalValueResponse response = new TotalValueResponse();
	// response.setTotalPayment(306956.63);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(presentValueRequest);
	//
	// when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);
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
	// mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.value_calculated_successfully)))
	// .andExpect(jsonPath("$.responseData.data.totalPayment", is(306956.63)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test
	// public void test_calculatePresentValue_ScreenRights_UnAuthorized() throws
	// Exception {
	// TargetValueRequest presentValueRequest = new TargetValueRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(presentValueRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/IP-TargetValue").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_calculatePresentValue_ValidatorError() throws Exception {
		TargetValueRequest presentValueRequest = new TargetValueRequest();
		presentValueRequest.setInvType("LUMSUM");
		presentValueRequest.setFutureValue("500000");
		presentValueRequest.setDuration("10");
		presentValueRequest.setRateOfInterest("5");
		presentValueRequest.setScreenId(1);
		TotalValueResponse response = new TotalValueResponse();
		response.setTotalPayment(306956.63);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(presentValueRequest);

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
		when(targetValueRequestValidator.validate(Mockito.any(TargetValueRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	// @Test
	// public void test_calculatePresentValue_SIP() throws Exception {
	// TargetValueRequest presentValueRequest = new TargetValueRequest();
	// presentValueRequest.setInvType("SIP");
	// presentValueRequest.setFutureValue("500");
	// presentValueRequest.setDuration("1");
	// presentValueRequest.setRateOfInterest("12");
	// presentValueRequest.setDurationType("YEAR");
	// TotalValueResponse response = new TotalValueResponse();
	// response.setTotalPayment(5627.54);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(presentValueRequest);
	//
	// when(targetValueRequestValidator.validate(presentValueRequest)).thenReturn(null);
	//
	// mockMvc.perform(post("/IP-TargetValue").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getValue_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.totalPayment",
	// is(5627.54))).andReturn();
	// }

	// // Present Value Annuity
	// @Test
	// public void test_calculatePresentValueAnnuity() throws Exception {
	// PresentValueAnnuityRequest presentValueAnnuityRequest = new
	// PresentValueAnnuityRequest();
	// presentValueAnnuityRequest.setPeriodicAmount("500");
	// presentValueAnnuityRequest.setDuration("1");
	// presentValueAnnuityRequest.setAnnualGrowthRate("12");
	//
	// TotalValueResponse response = new TotalValueResponse();
	// response.setTotalPayment(5627.54);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonStringResult = mapper.writeValueAsString(response);
	// String jsonString = mapper.writeValueAsString(presentValueAnnuityRequest);
	//
	//
	// when(presentValueAnnuityRequestValidator.validate(presentValueAnnuityRequest)).thenReturn(null);
	//
	// MvcResult result = mockMvc
	// .perform(post("/calculatePresentValueAnnuity").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON))
	//
	// .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	// Assert.assertEquals(result.getResponse().getContentAsString(),
	// jsonStringResult);
	//
	// }
	//
	// // Present Value Annuity
	// @Test
	// public void test_calculatePresentValueAnnuityDue() throws Exception {
	// PresentValueAnnuityDueRequest presentValueAnnuityDueRequest = new
	// PresentValueAnnuityDueRequest();
	// presentValueAnnuityDueRequest.setPeriodicAmount("1000");
	// presentValueAnnuityDueRequest.setDuration("1");
	// presentValueAnnuityDueRequest.setAnnualGrowthRate("13.2");
	//
	// TotalValueResponse response = new TotalValueResponse();
	// response.setTotalPayment(11307.32);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonStringResult = mapper.writeValueAsString(response);
	// String jsonString =
	// mapper.writeValueAsString(presentValueAnnuityDueRequest);
	// //
	// //
	// when(presentValueAnnuityDueReqValidator.validate(presentValueAnnuityDueRequest)).thenReturn(null);
	// //
	// // MvcResult result = mockMvc
	// // .perform(post("/calculatePresentValueAnnuityDue").content(jsonString)
	// // .contentType(MediaType.APPLICATION_JSON))
	// //
	// .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	// // Assert.assertEquals(result.getResponse().getContentAsString(),
	// jsonStringResult);
	//
	// }

	// Rate Finder
	@Test
	public void test_findRate_LUMSUM() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("10");
		rateFinderRequest.setFutureValue("500000");
		rateFinderRequest.setPresentValue("123592");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);

		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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
		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.rate_calculated_successfully)))
				.andExpect(jsonPath("$.responseData.data.rateOfInterest", is(15.0))).andReturn();
	}

	@Test
	public void test_findRate_LUMSUM_ZeroError() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("0");
		rateFinderRequest.setFutureValue("0");
		rateFinderRequest.setPresentValue("0");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);

		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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
		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_findRate_LUMSUM_DurationZeroError() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("0");
		rateFinderRequest.setFutureValue("157850");
		rateFinderRequest.setPresentValue("16879");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);

		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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
		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "DURATION")));
	}

	@Test
	public void test_findRate_LUMSUM_FutureValueZeroError() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("10");
		rateFinderRequest.setFutureValue("0");
		rateFinderRequest.setPresentValue("16879");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);

		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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
		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "FUTURE VALUE")));
	}

	@Test
	public void test_findRate_LUMSUM_PresentValueZeroError() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("10");
		rateFinderRequest.setFutureValue("166840");
		rateFinderRequest.setPresentValue("0");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);

		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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
		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "PRESENT VALUE")));
	}

	@Test
	public void test_findRate_LUMSUM_TwoFieldZeroError() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("0");
		rateFinderRequest.setFutureValue("0");
		rateFinderRequest.setPresentValue("15215");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);

		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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
		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	// @Test
	// public void test_RateFinder_ScreenRights_AccessDenied() throws Exception {
	// RateFinderRequest screenIdRequest = new RateFinderRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	// @Test
	// public void test_findRate_LUMSUM_ScreenRights_Success() throws Exception {
	// RateFinderRequest rateFinderRequest = new RateFinderRequest();
	// rateFinderRequest.setInvType("LUMSUM");
	// rateFinderRequest.setDuration("10");
	// rateFinderRequest.setFutureValue("500000");
	// rateFinderRequest.setPresentValue("123592");
	// rateFinderRequest.setDurationType("YEAR");
	// rateFinderRequest.setScreenId(1);
	//
	// RateFinderResponse response = new RateFinderResponse();
	// response.setRateOfInterest(15.0);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(rateFinderRequest);
	//
	// when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);
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
	// mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.rate_calculated_successfully)))
	// .andExpect(jsonPath("$.responseData.data.rateOfInterest", is(15.0)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test
	// public void test_findRate_ScreenRights_UnAuthorized() throws Exception {
	// RateFinderRequest rateFinderRequest = new RateFinderRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(rateFinderRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/IP-RateFinder").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_findRate_ValidatorError() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("LUMSUM");
		rateFinderRequest.setDuration("10");
		rateFinderRequest.setFutureValue("500000");
		rateFinderRequest.setPresentValue("123592");
		rateFinderRequest.setScreenId(1);
		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);

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

		when(rateFinderRequestValidator.validate(Mockito.any(RateFinderRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_findRate_SIP() throws Exception {
		RateFinderRequest rateFinderRequest = new RateFinderRequest();
		rateFinderRequest.setInvType("SIP");
		rateFinderRequest.setDuration("10");
		rateFinderRequest.setFutureValue("500000");
		rateFinderRequest.setPresentValue("123592");
		rateFinderRequest.setDurationType("YEAR");
		rateFinderRequest.setScreenId(1);
		RateFinderResponse response = new RateFinderResponse();
		response.setRateOfInterest(15.0);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(rateFinderRequest);
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

		when(rateFinderRequestValidator.validate(rateFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-RateFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.rate_calculated_successfully)))
				.andExpect(jsonPath("$.responseData.data.rateOfInterest", is(15.0))).andReturn();
	}

	// @Test
	// public void test_rateFinderAnnuity() throws Exception {
	// RateFinderAnnuityRequest rateFinderAnnuityRequest = new
	// RateFinderAnnuityRequest();
	// rateFinderAnnuityRequest.setDuration("10");
	// rateFinderAnnuityRequest.setFutureValue("500000");
	// rateFinderAnnuityRequest.setPresentValue("123592");
	//
	// RateFinderResponse response = new RateFinderResponse();
	// response.setRateOfInterest(15.0);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonStringResult = mapper.writeValueAsString(response);
	// String jsonString = mapper.writeValueAsString(rateFinderAnnuityRequest);
	//
	// //
	// when(rateFinderAnnuityRequestValidator.validate(rateFinderAnnuityRequest)).thenReturn(null);
	// //
	// // MvcResult result = mockMvc
	// //
	// .perform(post("/calculateRateAnnuity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// //
	// .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	// // Assert.assertEquals(result.getResponse().getContentAsString(),
	// jsonStringResult);
	//
	// }

	// Tenure Finder
	@Test
	public void test_findTenure_LUMSUM() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("15");
		tenureFinderRequest.setFutureValue("500000");
		tenureFinderRequest.setPresentValue("123592");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.tenure_calculated_successfully)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(10.0))).andReturn();

	}

	@Test
	public void test_findTenure_LUMSUM_ZeroError() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("0");
		tenureFinderRequest.setFutureValue("0");
		tenureFinderRequest.setPresentValue("0");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_findTenure_LUMSUM_RateZeroError() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("0");
		tenureFinderRequest.setFutureValue("16780");
		tenureFinderRequest.setPresentValue("16878");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE")));
	}

	@Test
	public void test_findTenure_LUMSUM_FutureValueZeroError() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("1");
		tenureFinderRequest.setFutureValue("0");
		tenureFinderRequest.setPresentValue("16878");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "FUTURE VALUE")));
	}

	@Test
	public void test_findTenure_LUMSUM_PresentValueZeroError() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("1");
		tenureFinderRequest.setFutureValue("16784");
		tenureFinderRequest.setPresentValue("0");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "PRESENT VALUE")));
	}

	@Test
	public void test_findTenure_LUMSUM_TwoFieldZeroError() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("0");
		tenureFinderRequest.setFutureValue("0");
		tenureFinderRequest.setPresentValue("123592");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	// @Test
	// public void test_TenureFinder_ScreenRights_AccessDenied() throws Exception {
	// TenureFinderRequest screenIdRequest = new TenureFinderRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	// @Test
	// public void test_findTenure_LUMSUM_ScreenRights_Success() throws Exception {
	// TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
	// tenureFinderRequest.setInvType("LUMSUM");
	// tenureFinderRequest.setRateOfInterest("15");
	// tenureFinderRequest.setFutureValue("500000");
	// tenureFinderRequest.setPresentValue("123592");
	// tenureFinderRequest.setScreenId(1);
	//
	// TenureFinderResponse response = new TenureFinderResponse();
	// response.setTenure(10);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(tenureFinderRequest);
	//
	// when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);
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
	// mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.tenure_calculated_successfully)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(10.0)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	//
	// }

	// @Test
	// public void test_findTenure_ScreenRights_UnAuthorized() throws Exception {
	// TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(tenureFinderRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/IP-TenureFinder").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_findTenure_ValidatorError() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("LUMSUM");
		tenureFinderRequest.setRateOfInterest("15");
		tenureFinderRequest.setFutureValue("500000");
		tenureFinderRequest.setPresentValue("123592");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);
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
		when(tenureFinderRequestValidator.validate(Mockito.any(TenureFinderRequest.class))).thenReturn(allErrors);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();

	}

	@Test
	public void test_findTenure_SIP() throws Exception {
		TenureFinderRequest tenureFinderRequest = new TenureFinderRequest();
		tenureFinderRequest.setInvType("SIP");
		tenureFinderRequest.setRateOfInterest("15");
		tenureFinderRequest.setFutureValue("500000");
		tenureFinderRequest.setPresentValue("123592");
		tenureFinderRequest.setScreenId(1);
		TenureFinderResponse response = new TenureFinderResponse();
		response.setTenure(10);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(tenureFinderRequest);

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

		when(tenureFinderRequestValidator.validate(tenureFinderRequest)).thenReturn(null);

		mockMvc.perform(post("/IP-TenureFinder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getTenure_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.tenure", is(10.0))).andReturn();

	}

	// @Test
	// public void test_tenureFinderAnnuity() throws Exception {
	// TenureFinderAnnuityRequest tenureFinderAnnuityRequest = new
	// TenureFinderAnnuityRequest();
	// tenureFinderAnnuityRequest.setRateOfInterest("15");
	// tenureFinderAnnuityRequest.setFutureValue("500000");
	// tenureFinderAnnuityRequest.setPresentValue("123592");
	//
	// TenureFinderResponse response = new TenureFinderResponse();
	// response.setTenure(10);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonStringResult = mapper.writeValueAsString(response);
	// String jsonString = mapper.writeValueAsString(tenureFinderAnnuityRequest);
	//
	//
	// when(tenureFinderAnnuityRequestValidator.validate(tenureFinderAnnuityRequest)).thenReturn(null);
	//
	// MvcResult result = mockMvc
	//
	// .perform(post("/calculateTenureAnnuity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	//
	// .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
	// Assert.assertEquals(result.getResponse().getContentAsString(),
	// jsonStringResult);
	//
	// }

	// Loan Planning
	@Test
	public void test_emiCalculator_AddSuccess() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("5000000");
		emiCalculatorRequest.setInterestRate("10");
		emiCalculatorRequest.setTenure("15");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

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
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getEmi_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(1.25)))
				.andExpect(jsonPath("$.responseData.data.emi", is(355985.75)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(339786.28)))
				.andExpect(jsonPath("$.responseData.data.total", is(5339786.28))).andReturn();

	}

	@Test
	public void test_emiCalculator_ZeroError() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("0");
		emiCalculatorRequest.setInterestRate("0");
		emiCalculatorRequest.setTenure("0");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

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
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_emiCalculator_LoanAmtZeroError() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("0");
		emiCalculatorRequest.setInterestRate("10");
		emiCalculatorRequest.setTenure("10");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

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
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT")));
	}

	@Test
	public void test_emiCalculator_RateZeroError() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("67884");
		emiCalculatorRequest.setInterestRate("0");
		emiCalculatorRequest.setTenure("10");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

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
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE")));
	}

	@Test
	public void test_emiCalculator_TenureZeroError() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("67884");
		emiCalculatorRequest.setInterestRate("10");
		emiCalculatorRequest.setTenure("0");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

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
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "TENURE")));
	}

	@Test
	public void test_emiCalculator_TwoFieldZeroError() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("123354");
		emiCalculatorRequest.setInterestRate("0");
		emiCalculatorRequest.setTenure("0");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

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
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	// @Test
	// public void test_calculateEmi_ScreenRights_AccessDenied() throws Exception {
	// EmiCalculatorRequest screenIdRequest = new EmiCalculatorRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }

	// @Test
	// public void test_emiCalculator_ScreenRights_Success() throws Exception {
	// //Error
	// EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
	// emiCalculatorRequest.setReferenceId("P00000");
	// emiCalculatorRequest.setLoanAmount("5000000");
	// emiCalculatorRequest.setInterestRate("10");
	// emiCalculatorRequest.setTenure("15");
	// emiCalculatorRequest.setTenureType("MONTH");
	// emiCalculatorRequest.setDate("Aug-2020");
	// emiCalculatorRequest.setScreenId(1);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiCalculatorRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
	// when(calcService.fetchEmiCalculatorByRefId(Mockito.anyString())).thenReturn(null);
	// when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
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
	// mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getEmi_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(1.0)))
	// .andExpect(jsonPath("$.responseData.data.emi", is(439579.44)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(274953.23)))
	// .andExpect(jsonPath("$.responseData.data.total", is(5274953.23)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	//
	// }

	// @Test
	// public void test_calculateEmi_ScreenRights_UnAuthorized() throws Exception {
	// EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiCalculatorRequest);
	// mockMvc.perform(MockMvcRequestBuilders.post("/calculateEmi").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }

	@Test
	public void test_emiCalculator_UpdateSuccess() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("5000000");
		emiCalculatorRequest.setInterestRate("10");
		emiCalculatorRequest.setTenure("15");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00000");
		emiCalculator.setLoanAmount(5000000);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);

		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);

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

		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
		when(calcService.fetchEmiCalculatorByRefId(Mockito.anyString())).thenReturn(emiCalculator);
		when(calcService.updateEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getEmi_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(1.25)))
				.andExpect(jsonPath("$.responseData.data.emi", is(355985.75)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(339786.28)))
				.andExpect(jsonPath("$.responseData.data.total", is(5339786.28))).andReturn();

	}

	@Test
	public void test_emiCalculator_ValidatorError() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("5000000");
		emiCalculatorRequest.setInterestRate("10");
		emiCalculatorRequest.setTenure("15");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00000");
		emiCalculator.setLoanAmount(5000000);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		when(emiCalculatorRequestValidator.validate(Mockito.any(EmiCalculatorRequest.class))).thenReturn(allErrors);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	// @Test
	// public void test_emiCalculator_AddError() throws Exception { //Error
	// EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
	// emiCalculatorRequest.setReferenceId("P00000");
	// emiCalculatorRequest.setLoanAmount("5000000");
	// emiCalculatorRequest.setInterestRate("10");
	// emiCalculatorRequest.setTenure("15");
	// emiCalculatorRequest.setTenureType("MONTH");
	// emiCalculatorRequest.setDate("Aug-2020");
	// emiCalculatorRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiCalculatorRequest);
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	//
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
	// when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(0);
	// when(calcService.addEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(0);
	//
	// mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())));
	// }

	// @Test
	// public void test_emiCalculator_UpdateError() throws Exception { //Error
	// EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
	// emiCalculatorRequest.setReferenceId("P00000");
	// emiCalculatorRequest.setLoanAmount("5000000");
	// emiCalculatorRequest.setInterestRate("10");
	// emiCalculatorRequest.setTenure("15");
	// emiCalculatorRequest.setTenureType("MONTH");
	// emiCalculatorRequest.setDate("Aug-2020");
	// emiCalculatorRequest.setScreenId(1);
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// EmiCalculator emiCalculator = new EmiCalculator();
	// emiCalculator.setReferenceId("P00000");
	// emiCalculator.setLoanAmount(5000000);
	// when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiCalculatorRequest);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	// String token = "eyJhbGciOiJIUzUxMiJ9";
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	//// when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);
	// when(calcService.checkEmiCalculatorIsPresent(Mockito.anyString())).thenReturn(1);
	// when(calcService.updateEmiCalculator(Mockito.any(EmiCalculator.class))).thenReturn(0);
	// mockMvc.perform(post("/calculateEmi").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_emiCalculator_NotFound() throws Exception {
		EmiCalculatorRequest emiCalculatorRequest = new EmiCalculatorRequest();
		emiCalculatorRequest.setReferenceId("P00000");
		emiCalculatorRequest.setLoanAmount("5000000");
		emiCalculatorRequest.setInterestRate("10");
		emiCalculatorRequest.setTenure("15");
		emiCalculatorRequest.setTenureType("MONTH");
		emiCalculatorRequest.setDate("Aug-2020");
		emiCalculatorRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00000");
		emiCalculator.setLoanAmount(5000000);
		when(emiCalculatorRequestValidator.validate(emiCalculatorRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCalculatorRequest);

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

		when(calcService.fetchParty(Mockito.anyLong())).thenReturn(null);

		mockMvc.perform(post("/calculateEmi").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_emiCapacity_AddSuccess() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P00000");
		emiCapacityRequest.setCurrentAge("38");
		emiCapacityRequest.setRetirementAge("58");
		emiCapacityRequest.setInterestRate("10");
		emiCapacityRequest.setStability("HIGH");
		emiCapacityRequest.setBackUp("YES");
		emiCapacityRequest.setNetFamilyIncome("140000");
		emiCapacityRequest.setExistingEmi("7000");
		emiCapacityRequest.setHouseHoldExpense("80000");
		emiCapacityRequest.setAdditionalIncome("19000");
		emiCapacityRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		when(emiCapacityRequestValidator.validate(emiCapacityRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiCapacity(Mockito.any(EmiCapacity.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiCapacity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getEmi_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.termOfLoan", is(20.00)))
				.andExpect(jsonPath("$.responseData.data.surplusMoney", is(53000.0)))
				.andExpect(jsonPath("$.responseData.data.surplus", is(53000.0)))
				.andExpect(jsonPath("$.responseData.data.emiCapacity", is(72000.00)))
				.andExpect(jsonPath("$.responseData.data.emiPayable", is(72000.00)))
				.andExpect(jsonPath("$.responseData.data.advisableLoanAmount", is(7460972.55))).andReturn();
	}

	@Test
	public void test_calculateEmiCapacity__AccessDenied() throws Exception {
		EmiCapacityRequest screenIdRequest = new EmiCapacityRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateEmiCapacity").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_emiCapacity_AccessSuccess() throws Exception { //Error
	// EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
	// emiCapacityRequest.setReferenceId("P00000");
	// emiCapacityRequest.setCurrentAge("38");
	// emiCapacityRequest.setRetirementAge("58");
	// emiCapacityRequest.setInterestRate("10");
	// emiCapacityRequest.setStability("HIGH");
	// emiCapacityRequest.setBackUp("YES");
	// emiCapacityRequest.setNetFamilyIncome("140000");
	// emiCapacityRequest.setExistingEmi("7000");
	// emiCapacityRequest.setHouseHoldExpense("80000");
	// emiCapacityRequest.setAdditionalIncome("19000");
	// emiCapacityRequest.setScreenId(1);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	//
	// when(emiCapacityRequestValidator.validate(emiCapacityRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiCapacityRequest);
	//
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addEmiCapacity(Mockito.any(EmiCapacity.class))).thenReturn(1);
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
	// mockMvc.perform(post("/calculateEmiCapacity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getEmi_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.termOfLoan", is(20.00)))
	// .andExpect(jsonPath("$.responseData.data.surplusMoney", is(53000.0)))
	// .andExpect(jsonPath("$.responseData.data.surplus", is(53000.0)))
	// .andExpect(jsonPath("$.responseData.data.emiCapacity", is(72000.00)))
	// .andExpect(jsonPath("$.responseData.data.emiPayable", is(72000.00)))
	// .andExpect(jsonPath("$.responseData.data.advisableLoanAmount",
	// is(7460972.55)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateEmiCapacity_ScreenRights_UnAuthorized() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculateEmiCapacity").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_emiCapacity_ValidatorError() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P00000");
		emiCapacityRequest.setCurrentAge("38");
		emiCapacityRequest.setRetirementAge("58");
		emiCapacityRequest.setInterestRate("10");
		emiCapacityRequest.setStability("HIGH");
		emiCapacityRequest.setBackUp("YES");
		emiCapacityRequest.setNetFamilyIncome("140000");
		emiCapacityRequest.setExistingEmi("7000");
		emiCapacityRequest.setHouseHoldExpense("80000");
		emiCapacityRequest.setAdditionalIncome("19000");
		emiCapacityRequest.setScreenId(1);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		Party party = new Party();
		party.setPartyId(1);
		when(emiCapacityRequestValidator.validate(Mockito.any(EmiCapacityRequest.class))).thenReturn(allErrors);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/calculateEmiCapacity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_emiCapacity_AddError() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P00000");
		emiCapacityRequest.setCurrentAge("38");
		emiCapacityRequest.setRetirementAge("58");
		emiCapacityRequest.setInterestRate("10");
		emiCapacityRequest.setStability("HIGH");
		emiCapacityRequest.setBackUp("YES");
		emiCapacityRequest.setNetFamilyIncome("140000");
		emiCapacityRequest.setExistingEmi("7000");
		emiCapacityRequest.setHouseHoldExpense("80000");
		emiCapacityRequest.setAdditionalIncome("19000");
		emiCapacityRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		when(emiCapacityRequestValidator.validate(emiCapacityRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiCapacity(Mockito.any(EmiCapacity.class))).thenReturn(0);
		mockMvc.perform(post("/calculateEmiCapacity").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_emiCapacity_UpdateSuccess() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P00000");
		emiCapacityRequest.setCurrentAge("38");
		emiCapacityRequest.setRetirementAge("58");
		emiCapacityRequest.setInterestRate("10");
		emiCapacityRequest.setStability("HIGH");
		emiCapacityRequest.setBackUp("YES");
		emiCapacityRequest.setNetFamilyIncome("140000");
		emiCapacityRequest.setExistingEmi("7000");
		emiCapacityRequest.setHouseHoldExpense("80000");
		emiCapacityRequest.setAdditionalIncome("19000");
		emiCapacityRequest.setScreenId(1);
		EmiCapacity emiCapacity = new EmiCapacity();

		Party party = new Party();
		party.setPartyId(1);

		when(emiCapacityRequestValidator.validate(emiCapacityRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkemiCapacityIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.updateEmiCapacity(Mockito.any(EmiCapacity.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiCapacity").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getEmi_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.termOfLoan", is(20.00)))
				.andExpect(jsonPath("$.responseData.data.surplusMoney", is(53000.0)))
				.andExpect(jsonPath("$.responseData.data.surplus", is(53000.0)))
				.andExpect(jsonPath("$.responseData.data.emiCapacity", is(72000.00))).andReturn();
	}

	@Test
	public void test_emiCapacity_UpdateError() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P00000");
		emiCapacityRequest.setCurrentAge("38");
		emiCapacityRequest.setRetirementAge("58");
		emiCapacityRequest.setInterestRate("10");
		emiCapacityRequest.setStability("HIGH");
		emiCapacityRequest.setBackUp("YES");
		emiCapacityRequest.setNetFamilyIncome("140000");
		emiCapacityRequest.setExistingEmi("7000");
		emiCapacityRequest.setHouseHoldExpense("80000");
		emiCapacityRequest.setAdditionalIncome("19000");
		emiCapacityRequest.setScreenId(1);

		EmiCapacity emiCapacity = new EmiCapacity();

		Party party = new Party();
		party.setPartyId(1);

		when(emiCapacityRequestValidator.validate(emiCapacityRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkemiCapacityIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.updateEmiCapacity(Mockito.any(EmiCapacity.class))).thenReturn(0);
		mockMvc.perform(post("/calculateEmiCapacity").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_emiCapacity_NotFound() throws Exception {
		EmiCapacityRequest emiCapacityRequest = new EmiCapacityRequest();
		emiCapacityRequest.setReferenceId("P00000");
		emiCapacityRequest.setCurrentAge("38");
		emiCapacityRequest.setRetirementAge("58");
		emiCapacityRequest.setInterestRate("10");
		emiCapacityRequest.setStability("HIGH");
		emiCapacityRequest.setBackUp("YES");
		emiCapacityRequest.setNetFamilyIncome("140000");
		emiCapacityRequest.setExistingEmi("7000");
		emiCapacityRequest.setHouseHoldExpense("80000");
		emiCapacityRequest.setAdditionalIncome("19000");
		emiCapacityRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		when(emiCapacityRequestValidator.validate(emiCapacityRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiCapacityRequest);

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

		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateEmiCapacity").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_calculatePartialPayment_Success() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("8000000");
		partialPaymentRequest.setTenure("15");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("10");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPartpayment_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(8000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(15.0)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(6219077.5)))
				.andExpect(jsonPath("$.responseData.data.amortisation", IsCollectionWithSize.hasSize(154))).andReturn();
	}

	@Test
	public void test_calculatePartialPayment_ZeroError() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("0");
		partialPaymentRequest.setTenure("0");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("0");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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

		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculatePartialPayment_LoanAmtZeroError() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("0");
		partialPaymentRequest.setTenure("10");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("10");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT")));
	}

	// @Test
	// public void test_calculatePartialPayment_TenureZeroError() throws Exception {
	// PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
	// partialPaymentRequest.setReferenceId("P00000");
	// partialPaymentRequest.setLoanAmount("15487");
	// partialPaymentRequest.setTenure("0");
	// partialPaymentRequest.setTenureType("YEAR");
	// partialPaymentRequest.setInterestRate("10");
	// partialPaymentRequest.setLoanDate("JAN-2015");
	// partialPaymentRequest.setScreenId(1);
	// PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
	// partialPaymentReq1.setPartPayDate("MAR-2019");
	// partialPaymentReq1.setPartPayAmount("300000");
	//
	// List<PartialPaymentReq> partialPaymentReqList = new
	// ArrayList<PartialPaymentReq>();
	// PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
	// partialPaymentReq2.setPartPayDate("JAN-2021");
	// partialPaymentReq2.setPartPayAmount("500000");
	//
	// PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
	// partialPaymentReq3.setPartPayDate("JAN-2023");
	// partialPaymentReq3.setPartPayAmount("200000");
	// partialPaymentReqList.add(partialPaymentReq1);
	// partialPaymentReqList.add(partialPaymentReq2);
	// partialPaymentReqList.add(partialPaymentReq3);
	//
	// partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
	// mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getZero_validation_error_single_field() + " : " + "TENURE")));
	// }

	@Test
	public void test_calculatePartialPayment_RateZeroError() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("15487");
		partialPaymentRequest.setTenure("10");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("0");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE")));
	}

	@Test
	public void test_calculatePartialPayment_TwoFieldZeroError() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("12334");
		partialPaymentRequest.setTenure("0");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("0");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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

		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculatePartialPayment_ScreenRights_AccessDenied() throws Exception {
		PartialPaymentRequest screenIdRequest = new PartialPaymentRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculatePartialPayment").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_calculatePartialPayment_ScreenRights_Success() throws
	// Exception { //Error
	// PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
	// partialPaymentRequest.setReferenceId("P00000");
	// partialPaymentRequest.setLoanAmount("8000000");
	// partialPaymentRequest.setTenure("15");
	// partialPaymentRequest.setTenureType("YEAR");
	// partialPaymentRequest.setInterestRate("10");
	// partialPaymentRequest.setLoanDate("JAN-2015");
	// partialPaymentRequest.setScreenId(1);
	//
	// PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
	// partialPaymentReq1.setPartPayDate("MAR-2019");
	// partialPaymentReq1.setPartPayAmount("300000");
	//
	// List<PartialPaymentReq> partialPaymentReqList = new
	// ArrayList<PartialPaymentReq>();
	// PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
	// partialPaymentReq2.setPartPayDate("JAN-2021");
	// partialPaymentReq2.setPartPayAmount("500000");
	//
	// PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
	// partialPaymentReq3.setPartPayDate("JAN-2023");
	// partialPaymentReq3.setPartPayAmount("200000");
	// partialPaymentReqList.add(partialPaymentReq1);
	// partialPaymentReqList.add(partialPaymentReq2);
	// partialPaymentReqList.add(partialPaymentReq3);
	//
	// partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(partialPaymentRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
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
	// mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getPartpayment_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(8000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(15.0)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(6219077.5)))
	// .andExpect(jsonPath("$.responseData.data.amortisation",
	// IsCollectionWithSize.hasSize(154)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculatePartialPayment_ScreenRights_UnAuthorized() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculatePartialPayment").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_calculatePartialPayment_ValidatorError() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("8000000");
		partialPaymentRequest.setTenure("15");
		partialPaymentRequest.setInterestRate("10");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);

		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		when(partialPaymentRequestValidator.validate(Mockito.any(PartialPaymentRequest.class))).thenReturn(allErrors);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculatePartialPayment_FieldsEmpty() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("26-03-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("01-01-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("01-01-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getFields_cannot_be_empty())))
				.andReturn();
	}

	// @Test
	// public void test_calculatePartialPayment_Error() throws Exception {
	// PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
	// partialPaymentRequest.setReferenceId("P00000");
	// partialPaymentRequest.setLoanAmount("8000000");
	// partialPaymentRequest.setTenure("15");
	// partialPaymentRequest.setTenureType("YEAR");
	// partialPaymentRequest.setInterestRate("10");
	// partialPaymentRequest.setLoanDate("01-01-2015");
	//
	// PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
	// partialPaymentReq1.setPartPayDate("26-03-2019");
	// partialPaymentReq1.setPartPayAmount("300000");
	//
	// List<PartialPaymentReq> partialPaymentReqList = new
	// ArrayList<PartialPaymentReq>();
	// PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
	// partialPaymentReq2.setPartPayDate("01-01-2021");
	// partialPaymentReq2.setPartPayAmount("500000");
	//
	// PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
	// partialPaymentReq3.setPartPayDate("01-01-2023");
	// partialPaymentReq3.setPartPayAmount("200000");
	// partialPaymentReqList.add(partialPaymentReq1);
	// partialPaymentReqList.add(partialPaymentReq2);
	// partialPaymentReqList.add(partialPaymentReq3);
	//
	// partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(partialPaymentRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(0);
	// mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_calculatePartialPayment_NotFound() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("8000000");
		partialPaymentRequest.setTenure("15");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("10");
		partialPaymentRequest.setLoanDate("01-01-2015");

		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("26-03-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("01-01-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("01-01-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);
		partialPaymentRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);

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

		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_calculatePartialPayment_Remove() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("8000000");
		partialPaymentRequest.setTenure("15");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("10");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(partialPaymentRequest.getTenure()) * 12;

		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		List<PartialPayment> partPayList = new ArrayList<>();
		PartialPayment partPay = new PartialPayment();
		partPayList.add(partPay);
		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkpartialIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.removePartialPaymentByRefId(Mockito.anyString())).thenReturn(1);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPartpayment_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(8000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(15.0)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(6219077.5)))
				.andExpect(jsonPath("$.responseData.data.amortisation", IsCollectionWithSize.hasSize(154))).andReturn();
	}

	@Test
	public void test_calculatePartialPayment_RemoveError() throws Exception {
		PartialPaymentRequest partialPaymentRequest = new PartialPaymentRequest();
		partialPaymentRequest.setReferenceId("P00000");
		partialPaymentRequest.setLoanAmount("8000000");
		partialPaymentRequest.setTenure("15");
		partialPaymentRequest.setTenureType("YEAR");
		partialPaymentRequest.setInterestRate("10");
		partialPaymentRequest.setLoanDate("JAN-2015");
		partialPaymentRequest.setScreenId(1);
		PartialPaymentReq partialPaymentReq1 = new PartialPaymentReq();
		partialPaymentReq1.setPartPayDate("MAR-2019");
		partialPaymentReq1.setPartPayAmount("300000");

		List<PartialPaymentReq> partialPaymentReqList = new ArrayList<PartialPaymentReq>();
		PartialPaymentReq partialPaymentReq2 = new PartialPaymentReq();
		partialPaymentReq2.setPartPayDate("JAN-2021");
		partialPaymentReq2.setPartPayAmount("500000");

		PartialPaymentReq partialPaymentReq3 = new PartialPaymentReq();
		partialPaymentReq3.setPartPayDate("JAN-2023");
		partialPaymentReq3.setPartPayAmount("200000");
		partialPaymentReqList.add(partialPaymentReq1);
		partialPaymentReqList.add(partialPaymentReq2);
		partialPaymentReqList.add(partialPaymentReq3);

		partialPaymentRequest.setPartialPaymentReq(partialPaymentReqList);

		List<PartialPayment> partPayList = new ArrayList<>();
		PartialPayment partPay = new PartialPayment();
		partPayList.add(partPay);
		Party party = new Party();
		party.setPartyId(1);

		when(partialPaymentRequestValidator.validate(partialPaymentRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partialPaymentRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkpartialIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.removePartialPaymentByRefId(Mockito.anyString())).thenReturn(0);
		when(calcService.addPartialPayment(Mockito.any(PartialPayment.class))).thenReturn(1);
		mockMvc.perform(post("/calculatePartialPayment").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_calculateEmiChange_Success() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("5000000");
		emiChangeRequest.setTenure("20");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("10.50");
		emiChangeRequest.setLoanDate("JAN-2015");

		int noOfInstall = Integer.parseInt(emiChangeRequest.getTenure()) * 12;

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);
		when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getEmi_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(4699373.58)))
				.andExpect(jsonPath("$.responseData.data.amortisation", IsCollectionWithSize.hasSize(158))).andReturn();
	}

	@Test
	public void test_calculateEmiChange_ZeroError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("0");
		emiChangeRequest.setTenure("0");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("0");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculateEmiChange_LoanAmtZeroError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("0");
		emiChangeRequest.setTenure("10");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("2");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT")));
	}

	@Test
	public void test_calculateEmiChange_TenureZeroError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("165445");
		emiChangeRequest.setTenure("0");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("2");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "TENURE")));
	}

	@Test
	public void test_calculateEmiChange_RateZeroError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("165445");
		emiChangeRequest.setTenure("10");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("0");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE")));
	}

	@Test
	public void test_calculateEmiChange_TwoFieldZeroError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("0");
		emiChangeRequest.setTenure("0");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("122345");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(1);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculateEmiChange_ScreenRights_AccessDenied() throws Exception {
		EmiChangeRequest screenIdRequest = new EmiChangeRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateEmiChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_calculateEmiChange_ScreenRights_Success() throws Exception {
	// //Error
	// EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
	// emiChangeRequest.setReferenceId("P00000");
	// emiChangeRequest.setLoanAmount("5000000");
	// emiChangeRequest.setTenure("20");
	// emiChangeRequest.setTenureType("YEAR");
	// emiChangeRequest.setInterestRate("10.50");
	// emiChangeRequest.setLoanDate("JAN-2015");
	// emiChangeRequest.setScreenId(1);
	// int noOfInstall = Integer.parseInt(emiChangeRequest.getTenure()) * 12;
	//
	// List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();
	//
	// EmiChangeReq emiChangeReq1 = new EmiChangeReq();
	// emiChangeReq1.setEmiChangedDate("JAN-2019");
	// emiChangeReq1.setIncreasedEmi("10000");
	//
	// EmiChangeReq emiChangeReq2 = new EmiChangeReq();
	// emiChangeReq2.setEmiChangedDate("FEB-2021");
	// emiChangeReq2.setIncreasedEmi("9000");
	//
	// emiChangeReqList.add(emiChangeReq1);
	// emiChangeReqList.add(emiChangeReq2);
	//
	// emiChangeRequest.setEmiChangeReq(emiChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// List<EmiChange> emiChangeList = new ArrayList<EmiChange>();
	//
	// when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// when(calcService.fetchEmiChangeByRefId(Mockito.anyString())).thenReturn(emiChangeList);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
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
	// mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getEmi_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(4699373.58)))
	// .andExpect(jsonPath("$.responseData.data.amortisation",
	// IsCollectionWithSize.hasSize(158)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateEmiChange_ScreenRights_UnAuthorized() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculateEmiChange").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_calculateEmiChange_Success() throws Exception {
	// EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
	// emiChangeRequest.setReferenceId("P00000");
	// emiChangeRequest.setLoanAmount("5000000");
	// emiChangeRequest.setTenure("20");
	// emiChangeRequest.setTenureType("YEAR");
	// emiChangeRequest.setInterestRate("10.50");
	// emiChangeRequest.setLoanDate("01-01-2015");
	// emiChangeRequest.setScreenId(1);
	// int noOfInstall = Integer.parseInt(emiChangeRequest.getTenure()) * 12;
	//
	// List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();
	//
	// EmiChangeReq emiChangeReq1 = new EmiChangeReq();
	// emiChangeReq1.setEmiChangedDate("01-01-2019");
	// emiChangeReq1.setIncreasedEmi("10000");
	//
	// EmiChangeReq emiChangeReq2 = new EmiChangeReq();
	// emiChangeReq2.setEmiChangedDate("01-02-2021");
	// emiChangeReq2.setIncreasedEmi("9000");
	//
	// emiChangeReqList.add(emiChangeReq1);
	// emiChangeReqList.add(emiChangeReq2);
	//
	// emiChangeRequest.setEmiChangeReq(emiChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// HashMap<String, HashMap<String, String>> allErrors = new HashMap<String,
	// HashMap<String, String>>();
	// HashMap<String, String> error = new HashMap<String, String>();
	// allErrors.put("NULL", error);
	//
	// when(emiChangeRequestValidator.validate(Mockito.any(EmiChangeRequest.class))).thenReturn(allErrors);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiChangeRequest);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
	// when(calcService.checkEmiChangeIsPresent(Mockito.anyString())).thenReturn(0);
	// when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
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
	// mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getEmi_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(4699373.58)))
	// .andExpect(jsonPath("$.responseData.data.amortisation",
	// IsCollectionWithSize.hasSize(158)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateEmiChange_ValidatorError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("5000000");
		emiChangeRequest.setTenure("20");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("10.50");
		emiChangeRequest.setLoanDate("01-01-2015");
		emiChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(emiChangeRequest.getTenure()) * 12;

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("01-01-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("01-02-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		when(emiChangeRequestValidator.validate(Mockito.any(EmiChangeRequest.class))).thenReturn(allErrors);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);

		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculateEmiChange_FieldsEmpty() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setTenureType("YEAR");
		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("01-01-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("01-02-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getFields_cannot_be_empty())))
				.andReturn();
	}

	@Test
	public void test_calculateEmiChange_Error() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("5000000");
		emiChangeRequest.setTenure("20");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("10.50");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		int plan = 1;
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(plan);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(0);
		mockMvc.perform(post("/calculateEmiChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculateEmiChange_RemoveSuccess() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("5000000");
		emiChangeRequest.setTenure("20");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("10.50");
		emiChangeRequest.setLoanDate("JAN-2015");
		emiChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(emiChangeRequest.getTenure()) * 12;

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<>();
		EmiChange emiChange = new EmiChange();
		emiChangeList.add(emiChange);
		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkEmiChangeIsPresent("P00000")).thenReturn(1);
		when(calcService.removeEmiChangeByRefId(Mockito.anyString())).thenReturn(1);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getEmi_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(4699373.58)))
				.andExpect(jsonPath("$.responseData.data.amortisation", IsCollectionWithSize.hasSize(158))).andReturn();
	}

	@Test
	public void test_calculateEmiChange_RemoveError() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("5000000");
		emiChangeRequest.setTenure("20");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("10.50");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<>();
		EmiChange emiChange = new EmiChange();
		emiChangeList.add(emiChange);
		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkEmiChangeIsPresent("P00000")).thenReturn(1);
		when(calcService.removeEmiChangeByRefId(Mockito.anyString())).thenReturn(0);
		when(calcService.addEmiChange(Mockito.any(EmiChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateEmiChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_calculateEmiChange_NotFound() throws Exception {
		EmiChangeRequest emiChangeRequest = new EmiChangeRequest();
		emiChangeRequest.setReferenceId("P00000");
		emiChangeRequest.setLoanAmount("5000000");
		emiChangeRequest.setTenure("20");
		emiChangeRequest.setTenureType("YEAR");
		emiChangeRequest.setInterestRate("10.50");
		emiChangeRequest.setLoanDate("JAN-2015");

		List<EmiChangeReq> emiChangeReqList = new ArrayList<EmiChangeReq>();

		EmiChangeReq emiChangeReq1 = new EmiChangeReq();
		emiChangeReq1.setEmiChangedDate("JAN-2019");
		emiChangeReq1.setIncreasedEmi("10000");

		EmiChangeReq emiChangeReq2 = new EmiChangeReq();
		emiChangeReq2.setEmiChangedDate("FEB-2021");
		emiChangeReq2.setIncreasedEmi("9000");

		emiChangeReqList.add(emiChangeReq1);
		emiChangeReqList.add(emiChangeReq2);

		emiChangeRequest.setEmiChangeReq(emiChangeReqList);
		emiChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		List<EmiChange> emiChangeList = new ArrayList<>();
		EmiChange emiChange = new EmiChange();
		emiChangeList.add(emiChange);
		when(emiChangeRequestValidator.validate(emiChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(emiChangeRequest);
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
		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateEmiChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_calculateInterestChange_Success() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenure("20");
		interestChangeRequest.setInterestRate("10.50");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInt_change_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
				.andExpect(jsonPath("$.responseData.data.emi", is(49918.99)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(4907978.7)))
				.andExpect(jsonPath("$.responseData.data.total", is(9907978.7)))
				.andExpect(jsonPath("$.responseData.data.amortisation", IsCollectionWithSize.hasSize(199))).andReturn();
	}

	@Test
	public void test_calculateInterestChange_ZeroError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("0");
		interestChangeRequest.setTenure("0");
		interestChangeRequest.setInterestRate("0");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculateInterestChange_LoanAmtZeroError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("0");
		interestChangeRequest.setTenure("10");
		interestChangeRequest.setInterestRate("2");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "LOAN AMOUNT")));
	}

	@Test
	public void test_calculateInterestChange_TenureZeroError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("45123");
		interestChangeRequest.setTenure("0");
		interestChangeRequest.setInterestRate("2");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "TENURE")));
	}

	@Test
	public void test_calculateInterestChange_RateZeroError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("45123");
		interestChangeRequest.setTenure("10");
		interestChangeRequest.setInterestRate("0");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getZero_validation_error_single_field() + " : " + "INTEREST RATE")));
	}

	@Test
	public void test_calculateInterestChange_TwoFieldZeroError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("0");
		interestChangeRequest.setTenure("10");
		interestChangeRequest.setInterestRate("0");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000))).andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getZero_validation_error())));
	}

	@Test
	public void test_calculateInterestChange_ValidatorError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenure("20");
		interestChangeRequest.setInterestRate("10.50");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		Party party = new Party();
		party.setPartyId(1);
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		allErrors.put("NULL", error);

		when(interestChangeRequestValidator.validate(Mockito.any(InterestChangeRequest.class))).thenReturn(allErrors);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(jsonPath("$.responseData.data", IsMapWithSize.aMapWithSize(1))).andReturn();
	}

	@Test
	public void test_calculateInterestChange_FieldsEmpty() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenureType("YEAR");
		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);
		interestChangeRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);

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
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getFields_cannot_be_empty())))
				.andReturn();
	}

	@Test
	public void test_calculateInterestChange_ScreenRights_AccessDenied() throws Exception {
		InterestChangeRequest screenIdRequest = new InterestChangeRequest();
		screenIdRequest.setReferenceId("P0000000000");
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/calculateInterestChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	// @Test
	// public void test_calculateInterestChange_ScreenRights_Success() throws
	// Exception { //Error
	// InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
	// interestChangeRequest.setReferenceId("P00000");
	// interestChangeRequest.setLoanAmount("5000000");
	// interestChangeRequest.setTenure("20");
	// interestChangeRequest.setInterestRate("10.50");
	// interestChangeRequest.setTenureType("YEAR");
	// interestChangeRequest.setLoanDate("FEB-2018");
	// interestChangeRequest.setScreenId(1);
	//
	// int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;
	//
	// List<InterestChangeReq> interestChangeReqList = new
	// ArrayList<InterestChangeReq>();
	//
	// InterestChangeReq interestChangeReq1 = new InterestChangeReq();
	// interestChangeReq1.setInterestChangedDate("FEB-2022");
	// interestChangeReq1.setChangedRate("9");
	//
	// InterestChangeReq interestChangeReq2 = new InterestChangeReq();
	// interestChangeReq2.setInterestChangedDate("APR-2027");
	// interestChangeReq2.setChangedRate("7");
	//
	// interestChangeReqList.add(interestChangeReq1);
	// interestChangeReqList.add(interestChangeReq2);
	//
	// interestChangeRequest.setInterestChangeReq(interestChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(interestChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
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
	// mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getInt_change_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
	// .andExpect(jsonPath("$.responseData.data.emi", is(49918.99)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(4907978.7)))
	// .andExpect(jsonPath("$.responseData.data.total", is(9907978.7)))
	// .andExpect(jsonPath("$.responseData.data.amortisation",
	// IsCollectionWithSize.hasSize(199)))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_calculateInterestChange_ScreenRights_UnAuthorized() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/calculateInterestChange").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_calculateInterestChange_Error() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenure("20");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setInterestRate("10.50");
		interestChangeRequest.setLoanDate("FEB-2018");

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);
		interestChangeRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(0);
		mockMvc.perform(post("/calculateInterestChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_calculateInterestChange_RemoveSuccess() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenure("20");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setInterestRate("10.50");
		interestChangeRequest.setLoanDate("FEB-2018");
		interestChangeRequest.setScreenId(1);
		int noOfInstall = Integer.parseInt(interestChangeRequest.getTenure()) * 12;

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);

		InterestChange intChange = new InterestChange();
		List<InterestChange> intChangeList = new ArrayList<>();
		intChangeList.add(intChange);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);

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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkInterestChangeIsPresent("P00000")).thenReturn(1);
		when(calcService.removeInterestChangeByRefId("P00000")).thenReturn(1);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInt_change_calculated_successfully())))
				.andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
				.andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
				.andExpect(jsonPath("$.responseData.data.emi", is(49918.99)))
				.andExpect(jsonPath("$.responseData.data.interestPayable", is(4907978.7)))
				.andExpect(jsonPath("$.responseData.data.total", is(9907978.7)))
				.andExpect(jsonPath("$.responseData.data.amortisation", IsCollectionWithSize.hasSize(199))).andReturn();
	}

	@Test
	public void test_calculateInterestChange_RemoveError() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenure("20");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setInterestRate("10.50");
		interestChangeRequest.setLoanDate("FEB-2018");

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);
		interestChangeRequest.setScreenId(1);
		InterestChange intChange = new InterestChange();
		List<InterestChange> intChangeList = new ArrayList<>();
		intChangeList.add(intChange);

		Party party = new Party();
		party.setPartyId(1);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);
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

		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setName("Advisor");
		plan.setAge(25);
		when(calcService.checkPlanIsPresentByReferenceId(Mockito.anyString())).thenReturn(1);
		when(calcService.checkInterestChangeIsPresent("P00000")).thenReturn(1);
		when(calcService.removeInterestChangeByRefId("P00000")).thenReturn(0);
		mockMvc.perform(post("/calculateInterestChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_calculateInterestChange_NotFound() throws Exception {
		InterestChangeRequest interestChangeRequest = new InterestChangeRequest();
		interestChangeRequest.setReferenceId("P00000");
		interestChangeRequest.setLoanAmount("5000000");
		interestChangeRequest.setTenure("20");
		interestChangeRequest.setTenureType("YEAR");
		interestChangeRequest.setInterestRate("10.50");
		interestChangeRequest.setLoanDate("FEB-2018");

		List<InterestChangeReq> interestChangeReqList = new ArrayList<InterestChangeReq>();

		InterestChangeReq interestChangeReq1 = new InterestChangeReq();
		interestChangeReq1.setInterestChangedDate("FEB-2022");
		interestChangeReq1.setChangedRate("9");

		InterestChangeReq interestChangeReq2 = new InterestChangeReq();
		interestChangeReq2.setInterestChangedDate("APR-2027");
		interestChangeReq2.setChangedRate("7");

		interestChangeReqList.add(interestChangeReq1);
		interestChangeReqList.add(interestChangeReq2);

		interestChangeRequest.setInterestChangeReq(interestChangeReqList);
		interestChangeRequest.setScreenId(1);
		InterestChange intChange = new InterestChange();
		List<InterestChange> intChangeList = new ArrayList<>();
		intChangeList.add(intChange);

		when(interestChangeRequestValidator.validate(interestChangeRequest)).thenReturn(null);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(interestChangeRequest);

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

		when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
		when(calcService.addInterestChange(Mockito.any(InterestChange.class))).thenReturn(1);
		mockMvc.perform(post("/calculateInterestChange").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	// @Test
	// public void test_calculateEmiInterestChange_Success() throws Exception {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	// emiInterestChangeRequest.setTenure("20");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setInterestRate("10.50");
	// emiInterestChangeRequest.setLoanDate("JAN-2015");
	//
	// int noOfInstall = Integer.parseInt(emiInterestChangeRequest.getTenure()) *
	// 12;
	//
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	//
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("JAN-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	//
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("FEB-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(emiInterestChangeRequestValidator.validate(emiInterestChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addEmiInterestChange(Mockito.any(EmiInterestChange.class))).thenReturn(1);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getValue_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
	// .andExpect(jsonPath("$.responseData.data.emi", is(49918.99)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(3708462.97)))
	// .andExpect(jsonPath("$.responseData.data.total", is(8708462.97)))
	// .andExpect(jsonPath("$.responseData.data.amortisation",
	// IsCollectionWithSize.hasSize(noOfInstall)))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_ValidatorError() throws Exception
	// {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	// emiInterestChangeRequest.setTenure("20");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setInterestRate("10.50");
	// emiInterestChangeRequest.setLoanDate("JAN-2015");
	//
	// int noOfInstall = Integer.parseInt(emiInterestChangeRequest.getTenure()) *
	// 12;
	//
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	//
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("JAN-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	//
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("FEB-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// HashMap<String, HashMap<String, String>> allErrors = new HashMap<String,
	// HashMap<String, String>>();
	// HashMap<String, String> error = new HashMap<String, String>();
	// allErrors.put("NULL", error);
	//
	// when(emiInterestChangeRequestValidator.validate(Mockito.any(EmiInterestChangeRequest.class)))
	// .thenReturn(allErrors);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError())))
	// .andExpect(jsonPath("$.responseData.data",
	// IsMapWithSize.aMapWithSize(1))).andReturn();
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_ScreenRights_AccessDenied()
	// throws Exception {
	// EmiInterestChangeRequest screenIdRequest = new EmiInterestChangeRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_FieldsEmpty() throws Exception {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	//
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	//
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("JAN-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	//
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("FEB-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(emiInterestChangeRequestValidator.validate(emiInterestChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addEmiInterestChange(Mockito.any(EmiInterestChange.class))).thenReturn(1);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getFields_cannot_be_empty())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_Error() throws Exception {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	// emiInterestChangeRequest.setTenure("20");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setInterestRate("10.50");
	// emiInterestChangeRequest.setLoanDate("JAN-2015");
	//
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	//
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("JAN-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	//
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("FEB-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(emiInterestChangeRequestValidator.validate(emiInterestChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.addEmiInterestChange(Mockito.any(EmiInterestChange.class))).thenReturn(0);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_RemoveSuccess() throws Exception
	// {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	// emiInterestChangeRequest.setTenure("20");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setInterestRate("10.50");
	// emiInterestChangeRequest.setLoanDate("JAN-2015");
	//
	// int noOfInstall = Integer.parseInt(emiInterestChangeRequest.getTenure()) *
	// 12;
	//
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	//
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("JAN-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	//
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("FEB-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	//
	// EmiInterestChange emiIntChange = new EmiInterestChange();
	// List<EmiInterestChange> emiIntChangeList = new ArrayList<>();
	// emiIntChangeList.add(emiIntChange);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(emiInterestChangeRequestValidator.validate(emiInterestChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	//
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.fetchEmiInterestChangeByRefId(Mockito.anyString())).thenReturn(emiIntChangeList);
	// when(calcService.removeEmiInterestChangeByRefId(Mockito.anyString())).thenReturn(1);
	// when(calcService.addEmiInterestChange(Mockito.any(EmiInterestChange.class))).thenReturn(1);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getValue_calculated_successfully())))
	// .andExpect(jsonPath("$.responseData.data.loanAmount", is(5000000.00)))
	// .andExpect(jsonPath("$.responseData.data.tenure", is(20.0)))
	// .andExpect(jsonPath("$.responseData.data.emi", is(49918.99)))
	// .andExpect(jsonPath("$.responseData.data.interestPayable", is(3708462.97)))
	// .andExpect(jsonPath("$.responseData.data.total", is(8708462.97)))
	// .andExpect(jsonPath("$.responseData.data.amortisation",
	// IsCollectionWithSize.hasSize(noOfInstall)))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_RemoveError() throws Exception {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	// emiInterestChangeRequest.setTenure("20");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setInterestRate("10.50");
	// emiInterestChangeRequest.setLoanDate("JAN-2015");
	//
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	//
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("JAN-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	//
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("FEB-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	//
	// EmiInterestChange emiIntChange = new EmiInterestChange();
	// List<EmiInterestChange> emiIntChangeList = new ArrayList<>();
	// emiIntChangeList.add(emiIntChange);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	//
	// when(emiInterestChangeRequestValidator.validate(emiInterestChangeRequest)).thenReturn(null);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setName("Advisor");
	// plan.setAge(25);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(plan);
	// when(calcService.fetchEmiInterestChangeByRefId(Mockito.anyString())).thenReturn(emiIntChangeList);
	// when(calcService.removeEmiInterestChangeByRefId(Mockito.anyString())).thenReturn(0);
	// when(calcService.addEmiInterestChange(Mockito.any(EmiInterestChange.class))).thenReturn(1);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured_remove())))
	// .andReturn();
	// }
	//
	// @Test
	// public void test_calculateEmiInterestChange_NotFound() throws Exception {
	// EmiInterestChangeRequest emiInterestChangeRequest = new
	// EmiInterestChangeRequest();
	// emiInterestChangeRequest.setReferenceId("P00000");
	// emiInterestChangeRequest.setLoanAmount("5000000");
	// emiInterestChangeRequest.setTenure("20");
	// emiInterestChangeRequest.setTenureType("YEAR");
	// emiInterestChangeRequest.setInterestRate("10.50");
	// emiInterestChangeRequest.setLoanDate("01-01-2015");
	// List<EmiInterestChangeReq> emiInterestChangeReqList = new
	// ArrayList<EmiInterestChangeReq>();
	// EmiInterestChangeReq emiInterestChangeReq1 = new EmiInterestChangeReq();
	// emiInterestChangeReq1.setChangedDate("01-01-2019");
	// emiInterestChangeReq1.setIncreasedEmi("10000");
	// emiInterestChangeReq1.setChangedRate("9");
	// EmiInterestChangeReq emiInterestChangeReq2 = new EmiInterestChangeReq();
	// emiInterestChangeReq2.setChangedDate("01-02-2021");
	// emiInterestChangeReq2.setIncreasedEmi("9000");
	// emiInterestChangeReq2.setChangedRate("7");
	//
	// emiInterestChangeReqList.add(emiInterestChangeReq1);
	// emiInterestChangeReqList.add(emiInterestChangeReq2);
	//
	// emiInterestChangeRequest.setEmiInterestChangeReq(emiInterestChangeReqList);
	// when(emiInterestChangeRequestValidator.validate(emiInterestChangeRequest)).thenReturn(null);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(emiInterestChangeRequest);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/calculateEmiInterestChange").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }

	@Test
	public void test_fetchFinancialPlanning() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		List<Goal> goalList = new ArrayList<Goal>();
		Goal goal = new Goal();
		goal.setReferenceId("P00000");
		goal.setGoalName("Travel");
		goalList.add(goal);
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setCashFlowItemId(1);
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setBudgetAmt(500);
		cashFlowList.add(cashFlow);
		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setMonthlyNetCashFlow(10000);
		List<Networth> networthList = new ArrayList<Networth>();
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setAccountType("Assests");
		networthList.add(networth);

		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setNetworth(10000);
		List<Priority> priorityList = new ArrayList<Priority>();
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setPriorityItemId(1);
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(1);
		riskProfileList.add(riskProfile);
		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("Balanced Investor");
		Plan plan = new Plan();
		plan.setPlanId(1);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);

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
		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);//
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(cashFlow);
		when(calcService.fetchCashFlowSummaryByRefId("P00000")).thenReturn(cashFlowSummary);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId("P00000", 1)).thenReturn(networth);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(priority);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);

		when(calcService.fetchRiskQuestionaireList()).thenReturn(riskQuestionaireList);
		when(calcService.fetchRiskProfileByRefIdAndAnswerId("P00000", 1)).thenReturn(riskProfile);
		when(calcService.fetchRiskSummaryByRefId("P00000")).thenReturn(riskSummary);

		FinancialPlanning financialPlanning = new FinancialPlanning();
		financialPlanning.setCashFlow(cashFlowList);
		financialPlanning.setCashFlowSummary(cashFlowSummary);
		financialPlanning.setNetworth(networthList);
		financialPlanning.setNetworthSummary(networthSummary);
		financialPlanning.setPriority(priorityList);
		financialPlanning.setInsurance(insuranceItem);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-financialPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(
						jsonPath("$.responseData.data.cashFlow", IsCollectionWithSize.hasSize(cashFlowItemList.size())))
				.andExpect(jsonPath("$.responseData.data.networth", IsCollectionWithSize.hasSize(networthList.size())))
				.andExpect(jsonPath("$.responseData.data.networth.[0].accountType", is("Assests"))).andReturn();
	}

	@Test
	public void test_fetch_financialPlanning_ScreenRights_AccessDenied() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetch-financialPlanning").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchFinancialPlanning_ScreenRights_Success() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		List<Goal> goalList = new ArrayList<Goal>();
		Goal goal = new Goal();
		goal.setReferenceId("P00000");
		goal.setGoalName("Travel");
		goalList.add(goal);
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setCashFlowItemId(1);
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setBudgetAmt(500);
		cashFlowList.add(cashFlow);
		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem);
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setMonthlyNetCashFlow(10000);
		List<Networth> networthList = new ArrayList<Networth>();
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setAccountEntryId(1);
		networth.setAccountTypeId(1);
		networth.setAccountType("Assests");
		networthList.add(networth);

		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountTypeId(1);
		accountList.add(account);
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("Assests");
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setNetworth(10000);
		List<Priority> priorityList = new ArrayList<Priority>();
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setPriorityItemId(1);
		priorityList.add(priority);
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItemList.add(priorityItem);
		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setReferenceId("P00000");
		insuranceItem.setInsuranceId(1);
		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(1);
		riskProfileList.add(riskProfile);
		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaireList.add(riskQuestionaire);
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("Balanced Investor");
		Plan plan = new Plan();
		plan.setPlanId(1);
		plan.setPartyId(1);
		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);//
		when(calcService.fetchGoalByReferenceId("P00000")).thenReturn(goalList);//
		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);//
		when(calcService.fetchCashFlowByRefIdAndItemId("P00000", 1)).thenReturn(cashFlow);
		when(calcService.fetchCashFlowSummaryByRefId("P00000")).thenReturn(cashFlowSummary);
		when(calcService.fetchAccountList()).thenReturn(accountList);
		when(calcService.fetchNetworthByRefIdAndEntryId("P00000", 1)).thenReturn(networth);
		when(calcService.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(priority);
		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);
		when(calcService.fetchInsuranceItemByRefId("P00000")).thenReturn(insuranceItem);

		when(calcService.fetchRiskQuestionaireList()).thenReturn(riskQuestionaireList);
		when(calcService.fetchRiskProfileByRefIdAndAnswerId("P00000", 1)).thenReturn(riskProfile);
		when(calcService.fetchRiskSummaryByRefId("P00000")).thenReturn(riskSummary);

		FinancialPlanning financialPlanning = new FinancialPlanning();
		financialPlanning.setCashFlow(cashFlowList);
		financialPlanning.setCashFlowSummary(cashFlowSummary);
		financialPlanning.setNetworth(networthList);
		financialPlanning.setNetworthSummary(networthSummary);
		financialPlanning.setPriority(priorityList);
		financialPlanning.setInsurance(insuranceItem);
		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
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

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		long loggedPartyId = plan.getPartyId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcService.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcService.checkSharedAdvisor("P00000", loggedPartyId)).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-financialPlanning").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(
						jsonPath("$.responseData.data.cashFlow", IsCollectionWithSize.hasSize(cashFlowItemList.size())))
				.andExpect(jsonPath("$.responseData.data.networth", IsCollectionWithSize.hasSize(networthList.size())))
				.andExpect(jsonPath("$.responseData.data.networth.[0].accountType", is("Assests")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchFinancialPlanning_ScreenRights_UnAuthorized() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-financialPlanning").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_fetchFinancialPlanning_NotFound() throws Exception {
	// CalcIdRequest idReq = new CalcIdRequest();
	// idReq.setId("P00008");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(idReq);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(MockMvcRequestBuilders.post("/fetch-financialPlanning").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	@Test
	public void test_fetchLoanPlanning() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setLoanAmount(10000.0);
		emiCalculator.setTenure(10);
		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setCurrentAge(23);
		PartialPayment partPayment1 = new PartialPayment();
		PartialPayment partPayment2 = new PartialPayment();
		List<PartialPayment> partPaymentList = new ArrayList<PartialPayment>();
		partPaymentList.add(partPayment1);
		partPaymentList.add(partPayment2);
		EmiChange emiChange1 = new EmiChange();
		EmiChange emiChange2 = new EmiChange();
		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();
		emiChangeList.add(emiChange1);
		emiChangeList.add(emiChange2);
		InterestChange interestChange1 = new InterestChange();
		InterestChange interestChange2 = new InterestChange();
		List<InterestChange> interestChangeList = new ArrayList<InterestChange>();
		interestChangeList.add(interestChange1);
		interestChangeList.add(interestChange2);
		EmiInterestChange emiInterestChange1 = new EmiInterestChange();
		EmiInterestChange emiInterestChange2 = new EmiInterestChange();
		List<EmiInterestChange> emiInterestChangeList = new ArrayList<EmiInterestChange>();
		emiInterestChangeList.add(emiInterestChange1);
		emiInterestChangeList.add(emiInterestChange2);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
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

		Plan plan = new Plan();
		plan.setPlanId(1);
		plan.setPartyId(1);
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		long loggedPartyId = plan.getPartyId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcService.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);
		when(calcService.checkSharedAdvisor("P00000", loggedPartyId)).thenReturn(1);
		when(calcService.fetchEmiCalculatorByRefId("P00000")).thenReturn(emiCalculator);
		when(calcService.fetchEmiCapacityByRefId("P00000")).thenReturn(emiCapacity);
		when(calcService.fetchPartialPaymentByRefId("P00000")).thenReturn(partPaymentList);
		when(calcService.fetchEmiChangeByRefId("P00000")).thenReturn(emiChangeList);
		when(calcService.fetchInterestChangeByRefId("P00000")).thenReturn(interestChangeList);
		when(calcService.fetchEmiInterestChangeByRefId("P00000")).thenReturn(emiInterestChangeList);

		LoanPlanning loanPlanning = new LoanPlanning();
		loanPlanning.setEmiCalculator(emiCalculator);
		loanPlanning.setEmiCapacity(emiCapacity);
		loanPlanning.setPartialPayment(partPaymentList);
		loanPlanning.setEmiChange(emiChangeList);
		loanPlanning.setInterestChange(interestChangeList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-loanPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.emiCalculator.loanAmount", is(10000.0)))
				.andExpect(jsonPath("$.responseData.data.emiCalculator.tenure", is(10)))
				.andExpect(jsonPath("$.responseData.data.partialPayment",
						IsCollectionWithSize.hasSize(partPaymentList.size())))
				.andReturn();

	}

	@Test
	public void test_fetch_loanPlanning_ScreenRights_AccessDenied() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetch-loanPlanning").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchLoanPlanning_ScreenRights_Success() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setLoanAmount(10000.0);
		emiCalculator.setTenure(10);
		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setCurrentAge(23);
		PartialPayment partPayment1 = new PartialPayment();
		PartialPayment partPayment2 = new PartialPayment();
		List<PartialPayment> partPaymentList = new ArrayList<PartialPayment>();
		partPaymentList.add(partPayment1);
		partPaymentList.add(partPayment2);
		EmiChange emiChange1 = new EmiChange();
		EmiChange emiChange2 = new EmiChange();
		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();
		emiChangeList.add(emiChange1);
		emiChangeList.add(emiChange2);
		InterestChange interestChange1 = new InterestChange();
		InterestChange interestChange2 = new InterestChange();
		List<InterestChange> interestChangeList = new ArrayList<InterestChange>();
		interestChangeList.add(interestChange1);
		interestChangeList.add(interestChange2);
		EmiInterestChange emiInterestChange1 = new EmiInterestChange();
		EmiInterestChange emiInterestChange2 = new EmiInterestChange();
		List<EmiInterestChange> emiInterestChangeList = new ArrayList<EmiInterestChange>();
		emiInterestChangeList.add(emiInterestChange1);
		emiInterestChangeList.add(emiInterestChange2);
		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		Plan plan = new Plan();
		plan.setPlanId(1);
		plan.setPartyId(1);
		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);
		when(calcService.fetchEmiCalculatorByRefId("P00000")).thenReturn(emiCalculator);
		when(calcService.fetchEmiCapacityByRefId("P00000")).thenReturn(emiCapacity);
		when(calcService.fetchPartialPaymentByRefId("P00000")).thenReturn(partPaymentList);
		when(calcService.fetchEmiChangeByRefId("P00000")).thenReturn(emiChangeList);
		when(calcService.fetchInterestChangeByRefId("P00000")).thenReturn(interestChangeList);
		when(calcService.fetchEmiInterestChangeByRefId("P00000")).thenReturn(emiInterestChangeList);

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

		LoanPlanning loanPlanning = new LoanPlanning();
		loanPlanning.setEmiCalculator(emiCalculator);
		loanPlanning.setEmiCapacity(emiCapacity);
		loanPlanning.setPartialPayment(partPaymentList);
		loanPlanning.setEmiChange(emiChangeList);
		loanPlanning.setInterestChange(interestChangeList);

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		long loggedPartyId = plan.getPartyId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcService.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcService.checkSharedAdvisor("P00000", loggedPartyId)).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-loanPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.emiCalculator.loanAmount", is(10000.0)))
				.andExpect(jsonPath("$.responseData.data.emiCalculator.tenure", is(10)))
				.andExpect(jsonPath("$.responseData.data.partialPayment",
						IsCollectionWithSize.hasSize(partPaymentList.size())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();

	}

	@Test
	public void test_fetchLoanPlanning_ScreenRights_UnAuthorized() throws Exception {
		CalcIdRequest idReq = new CalcIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-loanPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchGoalPlanning() throws Exception {
		Party party = new Party();
		party.setPartyId(1);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
		Plan plan = new Plan();
		plan.setPlanId(1);
		plan.setPartyId(1);

		List<Goal> goalList = new ArrayList<Goal>();
		Goal goal = new Goal();
		goal.setGoalId(1);
		goal.setGoalName("Travel");

		goalList.add(goal);

		GoalPlanning goalPlanning = new GoalPlanning();
		goalPlanning.setGoal(goalList);

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

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		long loggedPartyId = plan.getPartyId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcService.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);
		when(calcService.checkSharedAdvisor("P00000", loggedPartyId)).thenReturn(1);
		when(calcService.fetchGoalByReferenceId(Mockito.anyString())).thenReturn(goalList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-goalPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.goal", IsCollectionWithSize.hasSize(goalList.size())))
				.andReturn();

	}

	@Test
	public void test_fetch_GoalPlanning_ScreenRights_AccessDenied() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetch-goalPlanning").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchGoalPlanning_ScreenRights_Success() throws Exception {
		Party party = new Party();
		party.setPartyId(1);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
		Plan plan = new Plan();
		plan.setPlanId(1);
		plan.setPartyId(1);

		List<Goal> goalList = new ArrayList<Goal>();
		Goal goal = new Goal();
		goal.setGoalId(1);
		goal.setGoalName("Travel");

		goalList.add(goal);

		GoalPlanning goalPlanning = new GoalPlanning();
		goalPlanning.setGoal(goalList);

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

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		long loggedPartyId = plan.getPartyId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcService.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);
		when(calcService.checkSharedAdvisor("P00000", loggedPartyId)).thenReturn(1);
		when(calcService.fetchGoalByReferenceId(Mockito.anyString())).thenReturn(goalList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-goalPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.goal", IsCollectionWithSize.hasSize(goalList.size())))
				.andReturn();

	}

	@Test
	public void test_fetchGoalPlanning_ScreenRights_UnAuthorized() throws Exception {
		CalcIdRequest idReq = new CalcIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-goalPlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchRiskProfilePlanningById_ScreenRights_Success() throws Exception {
		Party party = new Party();
		party.setPartyId(1);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
		Plan plan = new Plan();
		plan.setPlanId(1);
		plan.setPartyId(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfileList.add(riskProfile);

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00000");
		riskSummary.setRiskSummaryId(1);

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setQuestionId("1");
		riskQuestionaireList.add(riskQuestionaire);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		long loggedPartyId = plan.getPartyId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcService.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);
		when(calcService.checkSharedAdvisor("P00000", loggedPartyId)).thenReturn(1);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);
		when(calcService.fetchRiskQuestionaireList()).thenReturn(riskQuestionaireList);
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

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-riskProfilePlanning").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.riskProfile",
						IsCollectionWithSize.hasSize(riskProfileList.size())))
				.andExpect(jsonPath("$.responseData.data.riskSummary.riskSummaryId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();

	}

	@Test
	public void test_fetchRiskProfilePlanningById_AccessDenied() throws Exception {
		CalcIdRequest screenIdRequest = new CalcIdRequest();
		screenIdRequest.setScreenId(1);
		screenIdRequest.setId("P00000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetch-riskProfilePlanning").header("Authorization", "token").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchRiskProfilePlanningById() throws Exception {
		Party party = new Party();
		party.setPartyId(1);

		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P00000");
		idReq.setScreenId(1);
		Plan plan = new Plan();
		plan.setPlanId(1);

		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfileList.add(riskProfile);

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00000");
		riskSummary.setRiskSummaryId(1);

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setQuestionId("1");
		riskQuestionaireList.add(riskQuestionaire);

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

		when(calcService.fetchPlanByReferenceId("P00000")).thenReturn(plan);
		when(calcService.fetchRiskProfileByRefId(Mockito.anyString())).thenReturn(riskProfileList);
		when(calcService.fetchRiskSummaryByRefId(Mockito.anyString())).thenReturn(riskSummary);
		when(calcService.fetchRiskQuestionaireList()).thenReturn(riskQuestionaireList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-riskProfilePlanning").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.riskProfile",
						IsCollectionWithSize.hasSize(riskProfileList.size())))
				.andExpect(jsonPath("$.responseData.data.riskSummary.riskSummaryId", is(1))).andReturn();

	}

	@Test
	public void test_fetchRiskProfilePlanningById_ScreenRights_UnAuthorized() throws Exception {
		CalcIdRequest idReq = new CalcIdRequest();
		idReq.setId("P0000000000");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idReq);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetch-riskProfilePlanning").header("Authorization", "token")
				.content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_fetchLoanPlanning_NotFound() throws Exception {
	// CalcIdRequest idReq = new CalcIdRequest();
	// idReq.setId("ADV000000000A");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(idReq);
	// when(calcService.fetchPlanByReferenceId(Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(MockMvcRequestBuilders.post("/fetch-loanPlanning").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	// // Lookup table fetch services
	//
	@Test
	public void test_fetchAccountTypeList_Success() throws Exception {
		List<AccountType> accountTypeList = new ArrayList<AccountType>();
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(1);
		accountType.setAccountType("assets");
		accountTypeList.add(accountType);

		when(calcService.fetchAccountTypeList()).thenReturn(accountTypeList);

		mockMvc.perform(get("/fetch-all-accountType")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].accountTypeId", is(1))).andReturn();

	}

	//
	// @Test
	// public void test_fetchAccountTypeList_Error() throws Exception {
	// when(calcService.fetchAccountTypeList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-accountType")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	//
	// }
	//
	@Test
	public void test_fetchAccount_Success() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account = new Account();
		account.setAccountEntryId(1);
		account.setAccountEntry("Equity Shares");
		account.setAccountTypeId(1);
		accountList.add(account);
		when(calcService.fetchAccountList()).thenReturn(accountList);

		mockMvc.perform(get("/fetch-all-account")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].accountEntryId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].accountEntry", is("Equity Shares")))
				.andExpect(jsonPath("$.responseData.data.[0].accountTypeId", is(1))).andReturn();

	}

	//
	// @Test
	// public void test_fetchAccount_Error() throws Exception {
	//
	// when(calcService.fetchAccountList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-account")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	//
	// }
	//
	@Test
	public void test_fetchCashFlowItemTypeList_Success() throws Exception {
		List<CashFlowItemType> cashFlowItemTypeList = new ArrayList<CashFlowItemType>();
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		cashFlowItemType.setCashFlowItemType("Life Style");
		cashFlowItemTypeList.add(cashFlowItemType);

		when(calcService.fetchCashFlowItemTypeList()).thenReturn(cashFlowItemTypeList);

		mockMvc.perform(get("/fetch-all-cashFlowItemType")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].cashFlowItemTypeId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].cashFlowItemType", is("Life Style"))).andReturn();

	}

	// @Test
	// public void test_fetchCashFlowItemTypeList_Error() throws Exception {
	//
	// when(calcService.fetchCashFlowItemTypeList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-cashFlowItemType")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	//
	// }
	//
	@Test
	public void test_fetchCashFlowItemList_Success() throws Exception {
		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemTypeId(1);
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItem("School Fees");
		cashFlowItemList.add(cashFlowItem);

		when(calcService.fetchCashFlowItemList()).thenReturn(cashFlowItemList);

		mockMvc.perform(get("/fetch-all-cashFlowItem")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].cashFlowItemTypeId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].cashFlowItemId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].cashFlowItem", is("School Fees"))).andReturn();
	}

	//
	// @Test
	// public void test_fetchCashFlowItemList_Error() throws Exception {
	// when(calcService.fetchCashFlowItemList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-cashFlowItem")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	@Test
	public void test_fetchPriorityItemList_Success() throws Exception {
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItemId(1);
		priorityItem.setPriorityItem("vacation");
		priorityItemList.add(priorityItem);

		when(calcService.fetchPriorityItemList()).thenReturn(priorityItemList);

		mockMvc.perform(get("/fetch-all-priorityItem")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].priorityItemId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].priorityItem", is("vacation"))).andReturn();
	}

	// @Test
	// public void test_fetchPriorityItemList_Error() throws Exception {
	// when(calcService.fetchPriorityItemList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-priorityItem")).andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	@Test
	public void test_fetchUrgencyList_Success() throws Exception {
		List<Urgency> urgencyList = new ArrayList<Urgency>();
		Urgency urgency = new Urgency();
		urgency.setUrgencyId(1);
		urgency.setValue("High");
		urgencyList.add(urgency);

		when(calcService.fetchUrgencyList()).thenReturn(urgencyList);

		mockMvc.perform(get("/fetch-all-urgency")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].urgencyId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].value", is("High"))).andReturn();

	}

	// @Test
	// public void test_fetchUrgencyList_Error() throws Exception {
	// when(calcService.fetchUrgencyList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-urgency")).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	//
	// }
	//
	@Test
	public void test_fetchRiskPortfolioList_Success() throws Exception {
		List<RiskPortfolio> riskPortfolioList = new ArrayList<RiskPortfolio>();
		RiskPortfolio riskPortfolio = new RiskPortfolio();
		riskPortfolio.setRiskPortfolioId(1);
		riskPortfolio.setBehaviour("Growth Investor");
		riskPortfolio.setCash(10);
		riskPortfolio.setPoints("30 or less");
		riskPortfolioList.add(riskPortfolio);

		when(calcService.fetchRiskPortfolioList()).thenReturn(riskPortfolioList);

		mockMvc.perform(get("/fetch-all-riskPortfolio")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].riskPortfolioId", is(1)))
				.andExpect(jsonPath("$.responseData.data.[0].points", is("30 or less"))).andReturn();
	}

	// @Test
	// public void test_fetchRiskPortfolioList_Error() throws Exception {
	// when(calcService.fetchRiskPortfolioList()).thenReturn(null);
	//
	// mockMvc.perform(get("/fetch-all-riskPortfolio")).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andReturn();
	// }
	//
	@Test
	public void test_fetchRiskQuestionaireList() throws Exception {
		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswerId(1);
		riskQuestionaire.setAnswer("answer");
		riskQuestionaire.setQuestionId("1");
		riskQuestionaire.setQuestion("question");
		riskQuestionaire.setScore(1);
		riskQuestionaireList.add(riskQuestionaire);

		String questionId1 = "1";
		String questionId2 = "2";
		List<String> questionIds = new ArrayList<>();
		questionIds.add(questionId1);
		questionIds.add(questionId2);

		when(calcService.fetchQuestionIdFromRiskQuestionaire()).thenReturn(questionIds);
		when(calcService.fetchRiskQuestionaireList()).thenReturn(riskQuestionaireList);
		when(calcService.fetchRiskQuestionaireByQuestionId(Mockito.anyString())).thenReturn(riskQuestionaireList);
		when(calcService.fetchQuestionByQuestionId(Mockito.anyString())).thenReturn("Question one")
				.thenReturn("Question two");

		mockMvc.perform(get("/fetch-all-riskQuestionaire")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.[0].question", is("Question one")))
				.andExpect(jsonPath("$.responseData.data.[1].question", is("Question two"))).andReturn();
	}

	@Test
	public void test_checkSharedAdvisor_Success() throws Exception {
		SharedRequest sharedRequest = new SharedRequest();
		sharedRequest.setScreenId(1);
		sharedRequest.setReferenceId("P0000000000");
		sharedRequest.setPartyId(2);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(sharedRequest);

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

		when(calcService.checkSharedAdvisor(Mockito.anyString(), Mockito.anyLong())).thenReturn(1);

		mockMvc.perform(post("/checkSharedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getShared_plan_advisor())))
				// .andExpect(jsonPath("$.responseData.data", hasSize(1)))
				// .andExpect(jsonPath("$.responseData.data", is(1)))
				.andReturn();
	}

	@Test
	public void test_checkSharedAdvisor_Mandatory() throws Exception {
		SharedRequest sharedRequest = new SharedRequest();
		sharedRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(sharedRequest);

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

		mockMvc.perform(post("/checkSharedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_RefParty())))
				.andReturn();
	}

	@Test
	public void test_checkSharedAdvisor_Error() throws Exception {
		SharedRequest sharedRequest = new SharedRequest();
		sharedRequest.setScreenId(1);
		sharedRequest.setReferenceId("P0000000000");
		sharedRequest.setPartyId(2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(sharedRequest);

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

		when(calcService.checkSharedAdvisor(Mockito.anyString(), Mockito.anyLong())).thenReturn(0);

		mockMvc.perform(post("/checkSharedAdvisor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getNot_shared_plan_advisor())))
				.andReturn();
	}

	// @Test //need to change
	// public void test_createCalcQuery_Success() throws Exception {
	// CalcQueryRequest queryRequest = new CalcQueryRequest();
	// queryRequest.setQuery("query");
	// // queryRequest.setForumCategoryId(1);
	// // queryRequest.setForumSubCategoryId(1);
	// queryRequest.setPartyId(1);
	// List<Long> postedTo = new ArrayList<>();
	// postedTo.add(1L);
	// queryRequest.setPostedToPartyId(postedTo);
	// queryRequest.setScreenId(1);
	// queryRequest.setReferenceId("P0000000000");
	// queryRequest.setUrl("url");
	//
	// int party = 1;
	// int forumSubCategory = 1;
	// CalcQuery calcQuery = new CalcQuery();
	// calcQuery.setForumSubCategoryId(1);
	// calcQuery.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(queryRequest);
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
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// //
	// when(calcService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
	// when(calcService.createQuery(Mockito.anyList())).thenReturn(1);
	// mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getForumthread_added_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_createCalcQuery_Mandatory() throws Exception {
		CalcQueryRequest queryRequest = new CalcQueryRequest();
		queryRequest.setScreenId(1);

		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);

		int party = 1;
		int forumSubCategory = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);
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
		mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_calcQuery())));

	}

	@Test
	public void test_createCalcQuery_ScreenRights_AccessDenied() throws Exception {
		CalcQueryRequest screenIdRequest = new CalcQueryRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createCalcQuery_ScreenRights_UnAuthorized() throws Exception {
		CalcQueryRequest screenIdRequest = new CalcQueryRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_createCalcQuery_ErrorOccurred() throws Exception {
	// CalcQueryRequest queryRequest = new CalcQueryRequest();
	// queryRequest.setQuery("query");
	// // queryRequest.setForumCategoryId(1);
	// // queryRequest.setForumSubCategoryId(1);
	// queryRequest.setPartyId(1);
	// List<Long> postedTo = new ArrayList<>();
	// postedTo.add(1L);
	// queryRequest.setPostedToPartyId(postedTo);
	// queryRequest.setScreenId(1);
	// queryRequest.setUrl("url");
	// queryRequest.setReferenceId("P000000000");
	// int party = 1;
	// int calcSubCategory = 1;
	// CalcQuery calcQuery = new CalcQuery();
	//// calcQuery.setForumSubCategoryId(1);
	// calcQuery.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(queryRequest);
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
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// //
	// when(calcService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(calcSubCategory);
	// when(calcService.createQuery(Mockito.anyList())).thenReturn(0);
	// mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	// @Test //need to change relate to update code
	// public void test_createCalcQuery_ScreenRights_Success() throws Exception {
	// CalcQueryRequest queryRequest = new CalcQueryRequest();
	// queryRequest.setQuery("query");
	// // queryRequest.setForumCategoryId(1);
	// // queryRequest.setForumSubCategoryId(1);
	// queryRequest.setPartyId(1);
	// queryRequest.setScreenId(1);
	// List<Long> postedTo = new ArrayList<>();
	// postedTo.add(2L);
	// queryRequest.setPostedToPartyId(postedTo);
	// queryRequest.setReferenceId("P0000000000");
	// queryRequest.setUrl("url");
	// int party = 1;
	// int calcSubCategory = 1;
	// CalcQuery calcQuery = new CalcQuery();
	// calcQuery.setForumSubCategoryId(1);
	// calcQuery.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(queryRequest);
	//
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// //
	// when(calcService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(calcSubCategory);
	// when(calcService.createQuery(Mockito.anyList())).thenReturn(1);
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
	// mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getForumthread_added_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test //need to change//
	// public void test_createCalcQuery_Empty() throws Exception {
	// CalcQueryRequest queryRequest = new CalcQueryRequest();
	// queryRequest.setScreenId(1);
	// List<Long> postedTo = new ArrayList<>();
	// postedTo.add(1L);
	// queryRequest.setPostedToPartyId(postedTo);
	//
	// int party = 1;
	// int forumSubCategory = 1;
	// CalcQuery forumQuery = new CalcQuery();
	// forumQuery.setCalcQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(queryRequest);
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
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// //
	// when(calcService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
	// when(calcService.createQuery(Mockito.anyList())).thenReturn(1);
	// mockMvc.perform(post("/createCalcQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getFields_empty())))
	// .andReturn();
	// }

	// @Test
	// public void test_createCalcAnswer_Success() throws Exception {
	// CalcAnswerRequest answerRequest = new CalcAnswerRequest();
	// answerRequest.setQueryId(1);
	// answerRequest.setPartyId(1);
	// answerRequest.setAnswer("answer");
	// answerRequest.setPartyId(1);
	// answerRequest.setScreenId(1);
	//
	// int party = 1;
	//
	// int forumQuery = 1;
	// CalcAnswer calcAnswer = new CalcAnswer();
	// calcAnswer.setAnswer("answer");
	// calcAnswer.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(answerRequest);
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
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// when(calcService.checkQueriesIsPresent(Mockito.anyLong())).thenReturn(forumQuery);
	// when(calcService.createAnswer(Mockito.any(CalcAnswer.class))).thenReturn(1);
	// mockMvc.perform(post("/createCalcAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getThread_added_successfully())))
	// .andReturn();
	// }
	// @Test
	// public void test_createCalcAnswer_ScreenRights_Success() throws Exception {
	// CalcAnswerRequest answerRequest = new CalcAnswerRequest();
	// answerRequest.setQueryId(1);
	// answerRequest.setPartyId(1);
	// answerRequest.setAnswer("answer");
	// answerRequest.setPartyId(1);
	// answerRequest.setScreenId(1);
	//
	// int party = 1;
	//
	// int forumQuery = 1;
	// CalcAnswer calcAnswer = new CalcAnswer();
	// calcAnswer.setAnswer("answer");
	// calcAnswer.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(answerRequest);
	//
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// when(calcService.checkQueriesIsPresent(Mockito.anyLong())).thenReturn(forumQuery);
	// when(calcService.createAnswer(Mockito.any(CalcAnswer.class))).thenReturn(1);
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
	// mockMvc.perform(post("/createCalcAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getThread_added_successfully())))
	// .andReturn();
	// }
	// @Test
	// public void test_createCalcAnswer_ScreenRights_AccessDenied() throws
	// Exception {
	// CalcAnswerRequest screenIdRequest = new CalcAnswerRequest();
	// screenIdRequest.setScreenId(1);
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(post("/createCalcAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getAccess_denied())));
	// }
	// @Test
	// public void test_createCalcAnswer_ScreenRights_UnAuthorized() throws
	// Exception {
	// CalcAnswerRequest screenIdRequest = new CalcAnswerRequest();
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(screenIdRequest);
	// mockMvc.perform(post("/createCalcAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getUnauthorized())));
	// }
	// @Test
	// public void test_createCalcAnswer_NotFound() throws Exception {
	// CalcAnswerRequest answerRequest = new CalcAnswerRequest();
	// answerRequest.setQueryId(1);
	// answerRequest.setPartyId(1);
	// answerRequest.setAnswer("answer");
	// answerRequest.setPartyId(1);
	// answerRequest.setScreenId(1);
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// // party.setParentPartyId("ADV0000000001");
	//
	// CalcQuery calcQuery = new CalcQuery();
	// // calcQuery.setForumSubCategoryId(1);
	// calcQuery.setCalcQueryId(1);
	// CalcAnswer calcAnswer = new CalcAnswer();
	// calcAnswer.setAnswer("answer");
	// calcAnswer.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(answerRequest);
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
	// when(calcService.fetchParty(Mockito.anyLong())).thenReturn(null);
	// // when(calcService.fetchForumQueryByPostedToPartyId(Mockito.anyLong(),
	// // Mockito.anyLong()))
	// // .thenReturn(calcQuery);
	// //
	// when(calcService.createAnswer(Mockito.any(CalcAnswer.class))).thenReturn(1);
	// mockMvc.perform(post("/createCalcAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getParty_not_found())))
	// .andReturn();
	// }
	// @Test
	// public void test_createCalcAnswer_ErrorOccurred() throws Exception {
	// CalcAnswerRequest answerRequest = new CalcAnswerRequest();
	// answerRequest.setQueryId(1);
	// answerRequest.setPartyId(1);
	// answerRequest.setAnswer("answer");
	// answerRequest.setPartyId(1);
	// answerRequest.setScreenId(1);
	//
	// int party = 1;
	//
	// int forumQuery = 1;
	// CalcAnswer calcAnswer = new CalcAnswer();
	// calcAnswer.setAnswer("answer");
	// calcAnswer.setQueryId(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(answerRequest);
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
	// when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
	// when(calcService.checkQueriesIsPresent(Mockito.anyLong())).thenReturn(forumQuery);
	// when(calcService.createAnswer(Mockito.any(CalcAnswer.class))).thenReturn(0);
	// mockMvc.perform(post("/createCalcAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isInternalServerError())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occured())))
	// .andReturn();
	// }

	@Test
	public void test_createCommentQueries_Success() throws Exception {
		CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
		commentQueryRequest.setCalcQueryId(1);
		commentQueryRequest.setQuery("ans");
		commentQueryRequest.setSenderId(123);

		commentQueryRequest.setScreenId(1);

		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(commentQueryRequest);
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

		when(calcService.checkCalcQueryIsPresent(Mockito.anyLong())).thenReturn(1);
		when(calcService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(calcService.createCommentQueries(Mockito.any(Queries.class))).thenReturn(1);
		mockMvc.perform(post("/commentQueries").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getThread_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_fetchSharedPlanByPostedPartyId_Success() throws Exception {
		PartyIdRequest partyIdRequest = new PartyIdRequest();
		partyIdRequest.setPartyId(2);
		partyIdRequest.setScreenId(1);

		List<CalcQuery> planList = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partyIdRequest);
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

		when(calcService.fetchSharedPlanByPostedPartyId(Mockito.anyLong())).thenReturn(planList);
		mockMvc.perform(
				post("/fetchSharedPlanByPostedPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchSharedPlanByPostedPartyId_Mandatory() throws Exception {
		PartyIdRequest partyIdRequest = new PartyIdRequest();
		partyIdRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partyIdRequest);
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
				post("/fetchSharedPlanByPostedPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_Party())))
				.andReturn();
	}

	@Test
	public void test_fetchSharedPlanByPostedPartyId_ScreenRights_AccessDenied() throws Exception {
		PartyIdRequest partyIdRequest = new PartyIdRequest();
		partyIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partyIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchSharedPlanByPostedPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchSharedPlanByPostedPartyId_ScreenRights_UnAuthorized() throws Exception {
		PartyIdRequest partyIdRequest = new PartyIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(partyIdRequest);
		mockMvc.perform(
				post("/fetchSharedPlanByPostedPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createCommentQueries_Mandatory() throws Exception {

		CommentQueryRequest commentQueryRequest = new CommentQueryRequest();
		commentQueryRequest.setScreenId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(commentQueryRequest);
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
		mockMvc.perform(post("/commentQueries").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_commentQuery())))
				.andReturn();
	}

	@Test
	public void test_createCommentQueries_ScreenRights_AccessDenied() throws Exception {
		CommentQueryRequest screenIdRequest = new CommentQueryRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/commentQueries").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createCommentQueries_ScreenRights_UnAuthorized() throws Exception {
		CommentQueryRequest screenIdRequest = new CommentQueryRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/commentQueries").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}
}
