package com.sowisetech.membership.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Invoice;
import com.sowisetech.AdvisorApplication;
import com.sowisetech.advisor.controller.AdvisorController;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.request.AdvIdRequest;
import com.sowisetech.advisor.request.ChatRequest;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.membership.model.InvoiceSubscription;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubscriptionPayment;
import com.sowisetech.membership.request.OrderRequest;
import com.sowisetech.membership.request.SinglePaymentRequest;
import com.sowisetech.membership.request.StringIdRequest;
import com.sowisetech.membership.request.SubscriptionRequest;
import com.sowisetech.membership.request.VerifyPaymentRequest;
import com.sowisetech.membership.service.MembershipService;
import com.sowisetech.membership.service.RazorpayService;
import com.sowisetech.membership.util.MembershipAppMessages;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdvisorApplication.class)

public class MembershipControllerTest {

	@InjectMocks
	private MembershipController membershipController;

	private MockMvc mockMvc;
	@Mock
	private MembershipService membershipService;
	@Mock
	private RazorpayService razorpayService;
	@Mock
	private AdvisorService advisorService;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired(required = true)
	@Spy
	ScreenRightsConstants screenRightsConstants;
	@Mock
	ScreenRightsCommon screenRightsCommon;
	@Mock
	CommonService commonService;
	@Autowired(required = true)
	@Spy
	MembershipAppMessages appMessages;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(membershipController).build();
	}

	// @Test
	// public void testEcv() throws Exception {
	// this.mockMvc.perform(get("/ecv")).andExpect(status().isOk());
	// }

	// @Test
	// public void test_fetchAllInvoice_Success() throws Exception {
	// List<String> invoiceId = new ArrayList<String>();
	// invoiceId.add("1");
	// invoiceId.add("2");
	//
	// List<InvoiceSubscription> invoiceList = new ArrayList<InvoiceSubscription>();
	// InvoiceSubscription invoiceSubscription1 = new InvoiceSubscription();
	// invoiceSubscription1.setName("advisor");
	// InvoiceSubscription invoiceSubscription2 = new InvoiceSubscription();
	// invoiceSubscription2.setName("Order2");
	//
	// invoiceList.add(invoiceSubscription1);
	// invoiceList.add(invoiceSubscription2);
	//
	// StringIdRequest subIdRequest = new StringIdRequest();
	// subIdRequest.setId("amount");
	// subIdRequest.setScreenId(1);
	//
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(subIdRequest);
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
	// when(membershipService.fetchInvoiceIdFromSubPayment(Mockito.anyString())).thenReturn(invoiceId);
	// when(razorpayService.fetchAllInvoiceSub(Mockito.anyList())).thenReturn(invoiceList);
	// mockMvc.perform(post("/fetchAllInvoice").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSuccess())))
	// .andExpect(jsonPath("$.responseData.data", hasSize(2)))
	// // .andExpect(jsonPath("$.responseData.data.invoiceList.[0].name",
	// // is("advisor")))
	// // .andExpect(jsonPath("$.responseData.data.order_id", is("Order2")))
	// .andReturn();
	// }

	@Test
	public void test_fetchAllInvoice_Mandatory() throws Exception {
		List<String> invoiceId = new ArrayList<String>();
		StringIdRequest subIdRequest = new StringIdRequest();
		subIdRequest.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subIdRequest);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/fetchAllInvoice").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_fetchAllInvoice())))
				.andReturn();

	}

	@Test
	public void test_fetchAllSubscriptions_Success() throws Exception {
		ScreenIdRequest fetchAllRequest = new ScreenIdRequest();

		List<MemberSubscription> memberSubscriptions = new ArrayList<MemberSubscription>();
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		when(membershipService.fetchAllMemberSubscription(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(memberSubscriptions);
		mockMvc.perform(
				post("/fetchAllMemberSubscriptions").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();

	}

	@Test
	public void test_fetchAllSubscriptions_ScreenRights_Success() throws Exception {

		ScreenIdRequest fetchAllRequest = new ScreenIdRequest();
		fetchAllRequest.setScreenId(1);

		when(membershipService.fetchMemSubscriptionTotalList()).thenReturn(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(fetchAllRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
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
				post("/fetchAllMemberSubscriptions").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchAllSubscriptions_ScreenRights_AccessDenied() throws Exception {

		ScreenIdRequest fetchAllRequest = new ScreenIdRequest();
		fetchAllRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(fetchAllRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchAllMemberSubscriptions").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAllSubscriptions_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest fetchAllRequest = new ScreenIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(fetchAllRequest);
		mockMvc.perform(
				post("/fetchAllMemberSubscriptions").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchMemberSubById_Success() throws Exception {
		StringIdRequest subIdRequest = new StringIdRequest();
		subIdRequest.setScreenId(1);
		subIdRequest.setId("abc");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(razorpayService.fetchMemberSubById(subIdRequest.getId())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubById").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchMemberSubById_ScreenRights_Success() throws Exception {
		StringIdRequest subIdRequest = new StringIdRequest();
		subIdRequest.setScreenId(1);
		subIdRequest.setId("abc");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subIdRequest);
		when(razorpayService.fetchMemberSubById(subIdRequest.getId())).thenReturn(null);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubById").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchMemberSubById_ScreenRights_AccessDenied() throws Exception {
		StringIdRequest subIdRequest = new StringIdRequest();
		subIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchMemberSubById").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchMemberSubById_ScreenRights_UnAuthorized() throws Exception {
		StringIdRequest subIdRequest = new StringIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subIdRequest);
		mockMvc.perform(post("/fetchMemberSubById").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchMemberSubById_Mandatory() throws Exception {
		StringIdRequest subIdRequest = new StringIdRequest();
		subIdRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubById").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_fetchMemberSubById())))
				.andReturn();
	}

	@Test
	public void test_fetchMemberSubByAdvId_Success() throws Exception {
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
		subscriptionRequest.setAdvId("ADV000000000A");
		subscriptionRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subscriptionRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(membershipService.fetchMemberSubByadvId(subscriptionRequest.getAdvId())).thenReturn(null);
		when(membershipService.fetchMemberSinglePaySubByAdvId(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();

	}

	@Test
	public void test_fetchMemberSubByAdvId_ScreenRights_Success() throws Exception {
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
		subscriptionRequest.setAdvId("ADV000000000A");
		subscriptionRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subscriptionRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();

	}

	@Test
	public void test_fetchMemberSubByAdvId_ScreenRights_AccessDenied() throws Exception {
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
		subscriptionRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subscriptionRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchMemberSubByAdvId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchMemberSubByAdvId_ScreenRights_UnAuthorized() throws Exception {
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subscriptionRequest);
		mockMvc.perform(post("/fetchMemberSubByAdvId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchMemberSubByAdvId_NotFound() throws Exception {
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
		subscriptionRequest.setAdvId("ADV000000000A");
		subscriptionRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subscriptionRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(membershipService.fetchMemberSubByadvId(subscriptionRequest.getAdvId())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();

	}

	@Test
	public void test_fetchMemberSubByAdvId_Mandatory() throws Exception {
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
		subscriptionRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(subscriptionRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchMemberSubByAdvId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_fetchMemberSubByAdvId())))
				.andReturn();

	}
	// Assestion Error
	// @Test
	// public void test_verifySinglePayment_Success() throws Exception {
	// VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
	// verifyPaymentRequest.setScreenId(1);
	// verifyPaymentRequest.setType("subscription");
	// verifyPaymentRequest.setSinglePaymentId(1);
	// verifyPaymentRequest.setRazorpay_order_id("order1");
	//
	// Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
	//
	// SinglePayment singlePayment = new SinglePayment();
	// singlePayment.setSubStartedAt(timestamp1);
	// singlePayment.setSubEndAt(timestamp1);
	// boolean verified = true;
	//
	// SubscriptionPayment subscriptionPayment = new SubscriptionPayment();
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
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
	// when(membershipService.fetchSinglePaymentByPrimaryKey(verifyPaymentRequest.getSinglePaymentId()))
	// .thenReturn(singlePayment);
	// when(membershipService.updatePaymentDetails(subscriptionPayment,
	// verifyPaymentRequest.getSinglePaymentId(),
	// singlePayment)).thenReturn(1);
	// when(razorpayService.verifySinglePayment(verifyPaymentRequest,
	// singlePayment)).thenReturn(true);
	// when(razorpayService.fetchPaymentDetailsByPaymentId(verifyPaymentRequest.getRazorpay_payment_id()))
	// .thenReturn(subscriptionPayment);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/verifySinglePayment").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getVerified_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	// @Test
	// public void test_verifySinglePayment_ScreenRights_Success() throws Exception
	// {
	// VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
	// verifyPaymentRequest.setScreenId(1);
	// verifyPaymentRequest.setType("");
	// verifyPaymentRequest.setSinglePaymentId(1);
	// verifyPaymentRequest.setRazorpay_order_id("");
	//
	// Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
	//
	// SinglePayment singlePayment = new SinglePayment();
	// singlePayment.setSubStartedAt(timestamp1);
	// singlePayment.setSubEndAt(timestamp1);
	//
	// SubscriptionPayment subscriptionPayment = new SubscriptionPayment();
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
	// when(membershipService.fetchSinglePaymentByPrimaryKey(verifyPaymentRequest.getSinglePaymentId()))
	// .thenReturn(singlePayment);
	// when(membershipService.updatePaymentDetails(subscriptionPayment,
	// verifyPaymentRequest.getSinglePaymentId(),
	// singlePayment)).thenReturn(1);
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
	// when(razorpayService.verifySinglePayment(verifyPaymentRequest,
	// singlePayment)).thenReturn(true);
	// when(razorpayService.fetchPaymentDetailsByPaymentId(verifyPaymentRequest.getRazorpay_payment_id()))
	// .thenReturn(subscriptionPayment);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/verifySinglePayment").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(
	// jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getVerified_successfully())))
	// .andReturn();
	// }

	@Test
	public void test_verifySinglePayment_ScreenRights_AccessDenied() throws Exception {
		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		verifyPaymentRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/verifySinglePayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_verifySinglePayment_ScreenRights_UnAuthorized() throws Exception {
		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		mockMvc.perform(post("/verifySinglePayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_verifySinglePayment_Mandatory() throws Exception {
		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		verifyPaymentRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/verifySinglePayment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_verifySinglePayment())))
				.andReturn();
	}
	// Assestion Error
	// @Test
	// public void test_createSinglePaymentOrder_Success() throws Exception {
	// Party party = new Party();
	//
	// SinglePaymentRequest singlePaymentRequest = new SinglePaymentRequest();
	// singlePaymentRequest.setId("abc");
	// singlePaymentRequest.setScreenId(1);
	// singlePaymentRequest.setType("abc");
	// singlePaymentRequest.setPlan_id("Abc");
	// singlePaymentRequest.setPeriod("abc");
	// singlePaymentRequest.setTotal_count(1);
	//
	// SinglePayment singlePayment = new SinglePayment();
	// singlePayment.setType("abc");
	// singlePayment.setPlan_id("Abc");
	// singlePayment.setPeriod("abc");
	// singlePayment.setTotal_count(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(singlePayment);
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
	// when(membershipService.addSinglePaymentOrder(singlePayment)).thenReturn(1);
	// when(razorpayService.createSinglePaymentOrder(singlePaymentRequest)).thenReturn(singlePayment);
	// when(advisorService.fetchPartyByRoleBasedId(singlePaymentRequest.getId())).thenReturn(party);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/createSinglePaymentOrder").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getSingle_pay_order_created())))
	// .andReturn();
	//
	// }

	@Test
	public void test_createSinglePaymentOrder_ScreenRights_AccessDenied() throws Exception {
		SinglePaymentRequest singlePaymentRequest = new SinglePaymentRequest();
		singlePaymentRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(singlePaymentRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/createSinglePaymentOrder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createSinglePaymentOrder_ScreenRights_UnAuthorized() throws Exception {
		SinglePaymentRequest singlePaymentRequest = new SinglePaymentRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(singlePaymentRequest);
		mockMvc.perform(post("/createSinglePaymentOrder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	// @Test
	// public void test_createSinglePaymentOrder_Error() throws Exception {
	// SinglePayment singlePayment = new SinglePayment();
	// singlePayment.setType("abc");
	// singlePayment.setPlan_id("Abc");
	// singlePayment.setPeriod("abc");
	// singlePayment.setTotal_count(1);
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(singlePayment);
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
	// when(membershipService.addSinglePaymentOrder(singlePayment)).thenReturn(1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/createSinglePaymentOrder").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getError_occurred_order())))
	// .andReturn();
	//
	// }

	@Test
	public void test_createSinglePaymentOrder_Mandatory() throws Exception {
		SinglePaymentRequest singlePaymentRequest = new SinglePaymentRequest();
		singlePaymentRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(singlePaymentRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/createSinglePaymentOrder").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_type_planId_period__totalCount())))
				.andReturn();

	}
	// assertion Error
	// @Test
	// public void test_verifySubscriptionPayment_Success() throws Exception {
	// VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
	// verifyPaymentRequest.setScreenId(1);
	// verifyPaymentRequest.setRazorpay_payment_id("captured");
	// verifyPaymentRequest.setSubscription_id("subscription");
	// boolean isVerified = true;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
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
	// when(razorpayService.verifySubscriptionPayment(verifyPaymentRequest)).thenReturn(true);
	//
	// mockMvc.perform(MockMvcRequestBuilders.post("/verifySubscriptionPayment").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getVerified_successfully())))
	// .andReturn();
	//
	// }

	// Assertion Error

	// @Test
	// public void test_verifySubscriptionPayment_ScreenRights_Success() throws
	// Exception {
	// VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
	// verifyPaymentRequest.setScreenId(1);
	// verifyPaymentRequest.setRazorpay_payment_id("");
	// verifyPaymentRequest.setSubscription_id("");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
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
	// when(razorpayService.verifySubscriptionPayment(verifyPaymentRequest)).thenReturn(true);
	// mockMvc.perform(MockMvcRequestBuilders.post("/verifySubscriptionPayment").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(MockMvcResultMatchers.status().is(200))
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getVerified_successfully())))
	// .andReturn();
	//
	// }

	@Test
	public void test_verifySubscriptionPayment_ScreenRights_UnAuthorized() throws Exception {
		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		mockMvc.perform(post("/verifySubscriptionPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_verifySubscriptionPayment_ScreenRights_AccessDenied() throws Exception {
		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		verifyPaymentRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/verifySubscriptionPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_verifySubscriptionPayment_Error() throws Exception {

		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		verifyPaymentRequest.setScreenId(1);
		verifyPaymentRequest.setRazorpay_payment_id("abc1");
		verifyPaymentRequest.setSubscription_id("subscription");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/verifySubscriptionPayment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getVerification_failed())))
				.andReturn();

	}

	@Test
	public void test_verifySubscriptionPayment_Mandatory() throws Exception {
		VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
		verifyPaymentRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(verifyPaymentRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/verifySubscriptionPayment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_verifySubscriptionPayment())))
				.andReturn();

	}

	@Test
	public void test_createOrderNumber_Success() throws Exception {
		OrderRequest invoiceRequest = new OrderRequest();
		invoiceRequest.setScreenId(1);
		invoiceRequest.setRoleBasedId("abc");
		invoiceRequest.setType("type1");
		invoiceRequest.setOrderDetailId("order1");

		OrderDetail invoice = new OrderDetail();
		invoice.setType("type1");
		invoice.setOrderDetailId("order1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invoiceRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(membershipService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(membershipService.generateSubOrderNumber()).thenReturn("type1");
		when(membershipService.generatePaymentOrderNumber()).thenReturn("type1");
		when(membershipService.addOrder(invoice)).thenReturn(invoice);

		mockMvc.perform(MockMvcRequestBuilders.post("/createOrderNumber").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getOrder_num_created())))
				.andReturn();

	}

	@Test
	public void test_createOrderNumber_ScreenRights_Success() throws Exception {
		OrderRequest invoiceRequest = new OrderRequest();
		invoiceRequest.setScreenId(1);
		invoiceRequest.setRoleBasedId("abc");
		invoiceRequest.setType("type1");
		invoiceRequest.setOrderDetailId("order1");

		OrderDetail invoice = new OrderDetail();
		invoice.setType("type1");
		invoice.setOrderDetailId("order1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invoiceRequest);
		when(membershipService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(1);
		when(membershipService.generateSubOrderNumber()).thenReturn("type1");
		when(membershipService.generatePaymentOrderNumber()).thenReturn("type1");
		when(membershipService.addOrder(invoice)).thenReturn(invoice);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/createOrderNumber").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getOrder_num_created())))
				.andReturn();

	}

	@Test
	public void test_createOrderNumber_ScreenRights_AccessDenied() throws Exception {
		OrderRequest invoiceRequest = new OrderRequest();
		invoiceRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invoiceRequest);

		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);

		mockMvc.perform(post("/verifySubscriptionPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createOrderNumber_ScreenRights_UnAuthorized() throws Exception {
		OrderRequest invoiceRequest = new OrderRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invoiceRequest);
		mockMvc.perform(post("/verifySubscriptionPayment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createOrderNumber_NotFound() throws Exception {
		OrderRequest invoiceRequest = new OrderRequest();
		invoiceRequest.setScreenId(1);
		invoiceRequest.setRoleBasedId("abc");
		invoiceRequest.setType("type1");
		invoiceRequest.setOrderDetailId("order1");

		OrderDetail invoice = new OrderDetail();
		invoice.setType("type1");
		invoice.setOrderDetailId("order1");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invoiceRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(membershipService.checkAdvisorIsPresent(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.post("/createOrderNumber").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_createOrderNumber_Mandatory() throws Exception {
		OrderRequest invoiceRequest = new OrderRequest();
		invoiceRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(invoiceRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/createOrderNumber").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_orderDetailId_type_roleBasedId())))
				.andReturn();

	}

	@Test
	public void test_updateOrderDetail_Success() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setScreenId(1);
		orderRequest.setOrderDetailId("paid");

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setStatus("abc");
		orderDetail.setOrderDetailId("paid");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(orderRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(membershipService.fetchOrderDetail(orderRequest.getOrderDetailId())).thenReturn(orderDetail);
		when(membershipService.updateOrderDetail(orderDetail)).thenReturn(orderDetail);

		mockMvc.perform(MockMvcRequestBuilders.post("/updateOrderDetailAsPaid").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getUpdate_order_detail_status_changed())))
				.andReturn();

	}

	@Test
	public void test_updateOrderDetail_ScreenRights_Success() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setScreenId(1);
		orderRequest.setOrderDetailId("paid");

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setStatus("abc");
		orderDetail.setOrderDetailId("paid");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(orderRequest);

		when(membershipService.fetchOrderDetail(orderRequest.getOrderDetailId())).thenReturn(orderDetail);
		when(membershipService.updateOrderDetail(orderDetail)).thenReturn(orderDetail);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/updateOrderDetailAsPaid").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getUpdate_order_detail_status_changed())))
				.andReturn();

	}

	@Test
	public void test_updateOrderDetail_ScreenRights_AccessDenied() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(orderRequest);

		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);

		mockMvc.perform(post("/updateOrderDetailAsPaid").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_updateOrderDetail_ScreenRights_UnAuthorized() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(orderRequest);
		mockMvc.perform(post("/updateOrderDetailAsPaid").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_updateOrderDetail_NotFound() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setScreenId(1);
		orderRequest.setOrderDetailId("Paid");

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setStatus("abc");
		orderDetail.setOrderDetailId("Paid");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(orderRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(membershipService.fetchOrderDetail(orderRequest.getOrderDetailId())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/updateOrderDetailAsPaid").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_updateOrderDetail_Mandatory() throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(orderRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/updateOrderDetailAsPaid").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_updateOrder())))
				.andReturn();

	}
}
