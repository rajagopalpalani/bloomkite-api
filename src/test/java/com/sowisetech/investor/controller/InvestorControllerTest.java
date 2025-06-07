package com.sowisetech.investor.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.investor.util.InvAppMessages;
import com.sowisetech.AdvisorApplication;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;
import com.sowisetech.investor.request.InvIdRequest;
import com.sowisetech.investor.request.InvInterestReq;
import com.sowisetech.investor.request.InvInterestRequest;
import com.sowisetech.investor.request.InvInterestRequestValidator;
import com.sowisetech.investor.request.InvestorRequest;
import com.sowisetech.investor.request.InvestorRequestValidator;
import com.sowisetech.investor.service.InvestorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvisorApplication.class)
public class InvestorControllerTest {

	@InjectMocks
	private InvestorController investorController;

	private MockMvc mockMvc;
	@Mock
	private InvestorService investorService;
	@Mock
	private InvestorRequestValidator investorRequestValidator;
	@Mock
	private InvInterestRequest invInterestRequest;
	@Mock
	private InvInterestRequestValidator invInterestRequestValidator;
	@Autowired(required = true)
	@Spy
	ScreenRightsConstants screenRightsConstants;
	@Mock
	ScreenRightsCommon screenRightsCommon;
	@Mock
	CommonService commonService;
	@Autowired(required = true)
	@Spy
	InvAppMessages appMessages;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(investorController).build();
	}

	@Test
	public void testEcv() throws Exception {
		this.mockMvc.perform(get("/investor-ecv")).andExpect(status().isOk());
	}

	// fetch investor list Test //
	@Test
	public void test_fetchAllInvestor_Success() throws Exception {
		List<Investor> investors = new ArrayList<Investor>();
		Investor inv1 = new Investor();
		inv1.setInvId("INV000000000A");
		inv1.setFullName("Investor1");
		Investor inv2 = new Investor();
		inv2.setInvId("INV000000000B");
		inv2.setFullName("Investor2");
		investors.add(inv1);
		investors.add(inv2);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(investorService.fetchTotalInvestorList()).thenReturn(2);
		when(investorService.fetchInvestorList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(investors);

		mockMvc.perform(post("/fetchAllInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseData.data.investors", hasSize(2)))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(status().isOk());

		verify(investorService, times(1)).fetchTotalInvestorList();
		verify(investorService, times(1)).fetchInvestorList(Mockito.anyInt(), Mockito.anyInt());
		verifyNoMoreInteractions(investorService);
	}

	@Test
	public void test_fetchAllInvestor_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAllInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test // null
	public void test_fetchAllInvestor_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Investor> investors = new ArrayList<Investor>();
		Investor inv1 = new Investor();
		inv1.setInvId("INV000000000A");
		inv1.setFullName("Investor1");
		investors.add(inv1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		when(investorService.fetchTotalInvestorList()).thenReturn(1);
		when(investorService.fetchInvestorList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(investors);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/fetchAllInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.investors", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.investors.[0].invId", is("INV000000000A")))
				.andExpect(jsonPath("$.responseData.data.investors.[0].fullName", is("Investor1")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchAllInvestor_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchAllInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// // fetch investor list Null Test //
	// @Test
	// public void test_fetchAllForNull() throws Exception {
	// when(investorService.fetchInvestorList(Mockito.anyInt(),
	// Mockito.anyInt())).thenReturn(null);
	//
	// mockMvc.perform(get("/fetchAllInvestor")).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.no_record_found)))
	// .andExpect(status().isInternalServerError());
	//
	// verify(investorService, times(1)).fetchInvestorList(Mockito.anyInt(),
	// Mockito.anyInt());
	// verifyNoMoreInteractions(investorService);
	// }

	// fetch investor Test//
	@Test
	public void test_fetchId() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setInvId("INV000000000A");
		invIdReq.setScreenId(1);
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setFullName("Investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(investorService.fetchByInvestorId(Mockito.anyString())).thenReturn(inv);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseData.data.invId", is("INV000000000A")))
				.andExpect(jsonPath("$.responseData.data.fullName", is("Investor")))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(status().isOk());

		verify(investorService, times(1)).fetchByInvestorId("INV000000000A");
		verifyNoMoreInteractions(investorService);

	}

	@Test
	public void test_fetchId_Mandatory() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_invId())))
				.andExpect(status().isOk());
	}

	@Test
	public void test_fetchInvestor_ScreenRights_AccessDenied() throws Exception {
		InvIdRequest screenIdRequest = new InvIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchInvestor_ScreenRights_Success() throws Exception {
		InvIdRequest invIdRequest = new InvIdRequest();
		invIdRequest.setScreenId(1);
		invIdRequest.setInvId("INV000000000A");
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setFullName("Investor");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdRequest);

		when(investorService.fetchByInvestorId(Mockito.anyString())).thenReturn(inv);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/fetchInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.invId", is("INV000000000A")))
				.andExpect(jsonPath("$.responseData.data.fullName", is("Investor")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchInvestor_ScreenRights_UnAuthorized() throws Exception {
		InvIdRequest invIdRequest = new InvIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdRequest);
		mockMvc.perform(post("/fetchInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// // fetch investor Null Test//
	// @Test
	// public void test_fetchIdForNull() throws Exception {
	// InvIdRequest invIdReq = new InvIdRequest();
	// invIdReq.setInvId("INV000000000A");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(invIdReq);
	//
	// when(investorService.fetchByInvestorId(Mockito.anyString())).thenReturn(null);
	// mockMvc.perform(MockMvcRequestBuilders.post("/fetchInvestor").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.no_record_found)))
	// .andExpect(status().isInternalServerError());
	//
	// verify(investorService, times(1)).fetchByInvestorId("INV000000000A");
	// verifyNoMoreInteractions(investorService);
	//
	// }

	// modify investor test//
	@Test
	public void test_modifyInvestor() throws Exception {

		InvestorRequest modInvReq = new InvestorRequest();
		modInvReq.setInvId("INV000000000A");
		modInvReq.setFullName("Investor");
		modInvReq.setDisplayName("investor!");
		modInvReq.setScreenId(1);
		int inv = 1;
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorRequestValidator.validate(modInvReq)).thenReturn(null);
		when(investorService.modifyInvestor(Mockito.anyString(), Mockito.any(Investor.class))).thenReturn(result);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_updated_successfully())))
				.andExpect(status().isOk());
		verify(investorService, times(1)).CheckInvestorIsPresent("INV000000000A");
		verify(investorService, times(1)).modifyInvestor(Mockito.anyString(), Mockito.any(Investor.class));
		verifyNoMoreInteractions(investorService);
	}

	@Test
	public void test_modifyInvestor_Mandatory() throws Exception {
		InvestorRequest modInvReq = new InvestorRequest();
		modInvReq.setFullName("Investor");
		modInvReq.setDisplayName("investor!");
		modInvReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_invId())))
				.andExpect(status().isOk());
	}

	@Test
	public void test_modifyInvestor_ScreenRights_AccessDenied() throws Exception {
		InvestorRequest screenIdRequest = new InvestorRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/modifyInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyInvestor_ScreenRights_Success() throws Exception {
		InvestorRequest modInvReq = new InvestorRequest();
		modInvReq.setInvId("INV000000000A");
		modInvReq.setFullName("Investor");
		modInvReq.setDisplayName("investor!");
		modInvReq.setScreenId(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		int inv = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);

		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorRequestValidator.validate(modInvReq)).thenReturn(null);
		when(investorService.modifyInvestor(Mockito.anyString(), Mockito.any(Investor.class))).thenReturn(1);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(put("/modifyInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_modifyInvestor_ScreenRights_UnAuthorized() throws Exception {
		InvestorRequest modInvReq = new InvestorRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);

		mockMvc.perform(put("/modifyInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// modify investor NoRecordFound test//
	@Test
	public void test_modifyInvestorNoRecordFound() throws Exception {

		InvestorRequest modInvReq = new InvestorRequest();
		modInvReq.setInvId("INV000000000A");
		modInvReq.setFullName("Investor");
		modInvReq.setDisplayName("investor!");
		modInvReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError());

		verify(investorService, times(1)).CheckInvestorIsPresent("INV000000000A");
		verifyNoMoreInteractions(investorService);
	}

	// modify investor Error test//
	@Test
	public void test_modifyInvestorError() throws Exception {

		InvestorRequest modInvReq = new InvestorRequest();
		modInvReq.setInvId("INV000000000A");
		modInvReq.setFullName("Investor");
		modInvReq.setDisplayName("investor!");
		modInvReq.setScreenId(1);

		int inv = 1;
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorRequestValidator.validate(modInvReq)).thenReturn(null);
		when(investorService.modifyInvestor(Mockito.anyString(), Mockito.any(Investor.class))).thenReturn(result);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError());

		verify(investorService, times(1)).CheckInvestorIsPresent(Mockito.anyString());
		verify(investorService, times(1)).modifyInvestor(Mockito.anyString(), Mockito.any(Investor.class));
		verifyNoMoreInteractions(investorService);
	}

	// modify investor ValidationError test//
	@Test
	public void test_modifyInvestorValidationError() throws Exception {

		InvestorRequest modInvReq = new InvestorRequest();
		modInvReq.setInvId("INV000000000A");
		modInvReq.setFullName("123");
		modInvReq.setDisplayName("31112");
		modInvReq.setScreenId(1);
		int inv = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(modInvReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
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

		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorRequestValidator.validate(Mockito.any(InvestorRequest.class))).thenReturn(allErrors);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyInvestor").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.responseData.data", is(allErrors)))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError())))
				.andExpect(status().isInternalServerError());
	}

	// remove investor Test//
	@Test
	public void test_removeInvestor() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setInvId("INV000000000B");
		invIdReq.setScreenId(1);
		int inv = 1;
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorService.removeInvestor(Mockito.anyString())).thenReturn(result);

		mockMvc.perform(post("/removeInvestor", invIdReq.getInvId()).content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.investor_deleted_successfully)))
				.andExpect(status().isOk());

		verify(investorService, times(1)).CheckInvestorIsPresent("INV000000000B");
		verify(investorService, times(1)).removeInvestor("INV000000000B");
		verifyNoMoreInteractions(investorService);
	}

	@Test
	public void test_removeInvestor_Mandatory() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeInvestor", invIdReq.getInvId()).content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_invId())))
				.andExpect(status().isOk());

	}

	@Test
	public void test_removeInvestor_ScreenRights_AccessDenied() throws Exception {
		InvIdRequest screenIdRequest = new InvIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeInvestor_ScreenRights_Success() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setInvId("INV000000000B");
		invIdReq.setScreenId(1);
		int inv = 1;
		int result = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);

		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorService.removeInvestor(Mockito.anyString())).thenReturn(result);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeInvestor", invIdReq.getInvId()).content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andExpect(status().isOk());
	}

	@Test
	public void test_removeInvestor_ScreenRights_UnAuthorized() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		mockMvc.perform(post("/removeInvestor").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// remove investor Null Test//
	@Test
	public void test_removeInvestorForNull() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setInvId("INV000000000B");
		invIdReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(
				post("/removeInvestor", "INV000000000B").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError());

		verify(investorService, times(1)).CheckInvestorIsPresent("INV000000000B");
		verifyNoMoreInteractions(investorService);
	}

	// remove investor Error Test//
	@Test
	public void test_removeInvestorForError() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setInvId("INV000000000B");
		invIdReq.setScreenId(1);
		int inv = 1;
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(investorService.CheckInvestorIsPresent(Mockito.anyString())).thenReturn(inv);
		when(investorService.removeInvestor(Mockito.anyString())).thenReturn(result);

		mockMvc.perform(
				post("/removeInvestor", "INV000000000B").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andExpect(status().isInternalServerError());

		verify(investorService, times(1)).CheckInvestorIsPresent("INV000000000B");
		verify(investorService, times(1)).removeInvestor("INV000000000B");
		verifyNoMoreInteractions(investorService);
	}

	// modify InvInterest Test //
	@Test
	public void test_modifyInvInterest_ModifySuccess() throws Exception {
		InvInterestReq invInterestReq = new InvInterestReq();
		invInterestReq.setInvId("INV000000000A");

		List<InvInterestRequest> invInterestRequestList = new ArrayList<InvInterestRequest>();

		InvInterestRequest request = new InvInterestRequest();
		request.setProdId(2);
		request.setScale("1");
		request.setInterestId(2);

		invInterestRequestList.add(request);
		invInterestReq.setInvInterestReq(invInterestRequestList);
		invInterestReq.setScreenId(1);
		int invInt = 1;
		int categ = 1;

		int result = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invInterestReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(invInterestRequestValidator.validate(invInterestRequestList)).thenReturn(null);
		when(investorService.addAndModifyInvestorInterest(Mockito.anyString(), Mockito.anyList())).thenReturn(result);

		mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_updated_successfully())))
				.andExpect(status().isOk());

		verify(investorService, times(1)).addAndModifyInvestorInterest(Mockito.anyString(), Mockito.anyList());
		verifyNoMoreInteractions(investorService);
	}

	@Test
	public void test_modifyInvInterest_Mandatory() throws Exception {
		InvInterestReq invInterestReq = new InvInterestReq();

		List<InvInterestRequest> invInterestRequestList = new ArrayList<InvInterestRequest>();

		InvInterestRequest request = new InvInterestRequest();
		request.setProdId(2);
		request.setScale("1");
		request.setInterestId(2);

		invInterestRequestList.add(request);
		invInterestReq.setInvInterestReq(invInterestRequestList);
		invInterestReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invInterestReq);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_invId())))
				.andExpect(status().isOk());
	}

	@Test
	public void test_modifyInvInterest_ScreenRights_AccessDenied() throws Exception {
		InvInterestReq screenIdRequest = new InvInterestReq();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/modifyInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyInvInterest_ScreenRights_Success() throws Exception {
		InvInterestReq invInterestReq = new InvInterestReq();
		invInterestReq.setInvId("INV000000000A");
		invInterestReq.setScreenId(1);
		List<InvInterestRequest> invInterestRequestList = new ArrayList<InvInterestRequest>();

		InvInterestRequest request = new InvInterestRequest();
		request.setProdId(2);
		request.setScale("1");
		request.setInterestId(2);

		invInterestRequestList.add(request);
		invInterestReq.setInvInterestReq(invInterestRequestList);

		int invInt = 1;
		int categ = 1;

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invInterestReq);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(investorService.CheckCategoryIsPresent(2)).thenReturn(categ);
		when(invInterestRequestValidator.validate(invInterestRequestList)).thenReturn(null);
		when(investorService.addAndModifyInvestorInterest(Mockito.anyString(), Mockito.anyList())).thenReturn(result);
		mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_updated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andExpect(status().isOk());
	}

	@Test
	public void test_modifyInvInterest_ScreenRights_UnAuthorized() throws Exception {
		InvInterestReq invInterestReq = new InvInterestReq();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invInterestReq);
		mockMvc.perform(post("/modifyInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// modify InvInterest Test //
	@Test
	public void test_modifyInvInterest_AddSuccess() throws Exception {
		InvInterestReq invInterestReq = new InvInterestReq();
		invInterestReq.setInvId("INV000000000A");
		invInterestReq.setScreenId(1);
		List<InvInterestRequest> invInterestRequestList = new ArrayList<InvInterestRequest>();

		InvInterestRequest request = new InvInterestRequest();
		request.setProdId(2);
		request.setScale("1");
		// request.setInterestId(2);

		invInterestRequestList.add(request);
		invInterestReq.setInvInterestReq(invInterestRequestList);

		InvInterest invInt = new InvInterest();
		invInt.setInterestId(2);
		invInt.setInvId("INV000000000A");

		int categ = 1;

		int result = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invInterestReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(invInterestRequestValidator.validate(invInterestRequestList)).thenReturn(null);
		when(investorService.CheckCategoryIsPresent(2)).thenReturn(categ);
		when(investorService.addAndModifyInvestorInterest(Mockito.anyString(), Mockito.anyList())).thenReturn(result);

		mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_updated_successfully())))
				.andExpect(status().isOk());
	}

	// // modify InvInterest NoRecordFound Test //
	// @Test
	// public void test_modifyInvInterestNoRecordFound() throws Exception {
	// InvInterestReq invInterestReq = new InvInterestReq();
	// invInterestReq.setInvId("INV000000000A");
	// invInterestReq.setScreenId(1);
	// List<InvInterestRequest> invInterestRequestList = new
	// ArrayList<InvInterestRequest>();
	//
	// InvInterestRequest request = new InvInterestRequest();
	// request.setCategoryId(2);
	// request.setScale("1");
	// request.setInterestId(2);
	//
	// invInterestRequestList.add(request);
	// invInterestReq.setInvInterestReq(invInterestRequestList);
	//
	// int categ = 1;
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(invInterestReq);
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
	// when(invInterestRequestValidator.validate(invInterestRequestList)).thenReturn(null);
	// when(investorService.CheckCategoryIsPresent(2)).thenReturn(categ);
	// when(investorService.CheckInvInterestIsPresent(2)).thenReturn(0);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getNo_record_found())))
	// .andExpect(status().isInternalServerError());
	//
	// verify(investorService, times(1)).CheckCategoryIsPresent(2);
	// verify(investorService, times(1)).CheckInvInterestIsPresent(2);
	// verifyNoMoreInteractions(investorService);
	// }

	// modify InvInterest Error Test //
	@Test
	public void test_modifyInvInterestError() throws Exception {
		InvInterestReq invInterestReq = new InvInterestReq();
		invInterestReq.setInvId("INV000000000A");
		invInterestReq.setScreenId(1);
		List<InvInterestRequest> invInterestRequestList = new ArrayList<InvInterestRequest>();

		InvInterestRequest request = new InvInterestRequest();
		request.setProdId(2);
		request.setScale("1");
		request.setInterestId(2);

		invInterestRequestList.add(request);
		invInterestReq.setInvInterestReq(invInterestRequestList);

		int invInt = 1;
		int categ = 1;

		int result = 0;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invInterestReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(invInterestRequestValidator.validate(invInterestRequestList)).thenReturn(null);
		when(investorService.addAndModifyInvestorInterest(Mockito.anyString(), Mockito.anyList())).thenReturn(result);

		mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andExpect(status().isInternalServerError());

		verify(investorService, times(1)).addAndModifyInvestorInterest(Mockito.anyString(), Mockito.anyList());
		verifyNoMoreInteractions(investorService);
	}

	// // modify InvInterest ValidationError Test //
	// @Test
	// public void test_modifyInvInterestValidationError() throws Exception {
	// InvInterestReq invInterestReq = new InvInterestReq();
	// invInterestReq.setInvId("INV000000000A");
	//
	// List<InvInterestRequest> invInterestRequestList = new
	// ArrayList<InvInterestRequest>();
	//
	// InvInterestRequest request = new InvInterestRequest();
	// request.setCategoryId(2);
	// request.setScale("1");
	// request.setInterestId(2);
	//
	// invInterestRequestList.add(request);
	// invInterestReq.setInvInterestReq(invInterestRequestList);
	//
	// InvInterest invInt = new InvInterest();
	// invInt.setInterestId(2);
	// invInt.setInvId("INV000000000A");
	//
	// Category categ = new Category();
	// categ.setCategoryId(2);
	//
	// HashMap<String, HashMap<String, String>> allErrors = new HashMap<String,
	// HashMap<String, String>>();
	// HashMap<String, String> error = new HashMap<String, String>();
	// allErrors.put("NULL", error);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(invInterestRequest);
	//
	// when(invInterestRequestValidator.validate(invInterestRequestList)).thenReturn(allErrors);
	// // when(investorService.fetchCategoryById(2)).thenReturn(categ);
	// // when(investorService.fetchByInvInterestById(2)).thenReturn(invInt);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/modifyInvInterest").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.responseData.data",
	// is(allErrors)))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.error)))
	// .andExpect(status().isInternalServerError());
	// }

	// remove InvInterest Test//
	@Test
	public void test_removeInvInterest() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setId(1);
		invIdReq.setScreenId(1);
		int invInterest = 1;
		int result = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(investorService.CheckInvInterestIsPresent(Mockito.anyLong())).thenReturn(invInterest);
		when(investorService.removeInvestorInterest(Mockito.anyLong())).thenReturn(result);

		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_deleted_successfully())))
				.andExpect(status().isOk());

		verify(investorService, times(1)).CheckInvInterestIsPresent(1);
		verify(investorService, times(1)).removeInvestorInterest(1);
		verifyNoMoreInteractions(investorService);
	}

	@Test
	public void test_removeInvInterest_Mandatory() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_intId())))
				.andExpect(status().isOk());
	}

	@Test
	public void test_removeInvInterest_ScreenRights_AccessDenied() throws Exception {
		InvIdRequest screenIdRequest = new InvIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeInvInterest_ScreenRights_Success() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setId(1);
		invIdReq.setScreenId(1);
		int invInterest = 1;
		int result = 1;
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);

		when(investorService.CheckInvInterestIsPresent(Mockito.anyLong())).thenReturn(invInterest);
		when(investorService.removeInvestorInterest(Mockito.anyLong())).thenReturn(result);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getInvestor_deleted_successfully())))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));

	}

	@Test
	public void test_removeInvInterest_ScreenRights_UnAuthorized() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// remove InvInterest Null Test//
	@Test
	public void test_removeInvInterestForNull() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setId(1);
		invIdReq.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(investorService.CheckInvInterestIsPresent(1)).thenReturn(0);
		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andExpect(status().isInternalServerError());
		verify(investorService, times(1)).CheckInvInterestIsPresent(1);
		verifyNoMoreInteractions(investorService);
	}

	// remove InvInterest Error Test//
	@Test
	public void test_removeInvInterestForError() throws Exception {
		InvIdRequest invIdReq = new InvIdRequest();
		invIdReq.setId(1);
		invIdReq.setScreenId(1);
		int invInterest = 1;
		int result = 0;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invIdReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(investorService.CheckInvInterestIsPresent(Mockito.anyLong())).thenReturn(invInterest);
		when(investorService.removeInvestorInterest(Mockito.anyLong())).thenReturn(result);

		mockMvc.perform(post("/removeInvInterest").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andExpect(status().isInternalServerError());
		verify(investorService, times(1)).CheckInvInterestIsPresent(1);
		verify(investorService, times(1)).removeInvestorInterest(1);
		verifyNoMoreInteractions(investorService);
	}

	public String createSuccessResponse(String text) {
		String response = "";
		try {
			response = new ObjectMapper().writeValueAsString(text);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}

}
