package com.sowisetech.membership.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.model.Party;
import com.sowisetech.membership.dao.MembershipDao;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubscriptionPayment;
import com.sowisetech.membership.request.PaymentEntityRequest;
import com.sowisetech.membership.request.SubscriptionChargedRequest;
import com.sowisetech.membership.model.SinglePayment;

public class MembershipServiceImplTest {

	@InjectMocks
	private MembershipServiceImpl membershipServiceImpl;

	private MockMvc mockMvc;

	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;

	@Mock
	private MembershipDao membershipDao;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(membershipServiceImpl).build();
	}

	@Test
	public void addMembershipPlan_Success() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();
		MembershipPlan membershipPlan = new MembershipPlan();
		membershipPlan.setMembershipPlanId(1);
		membershipPlan.setDelete_flag(delete_flag);

		when(membershipDao.addMembershipPlan(membershipPlan)).thenReturn(1);
		int result = membershipServiceImpl.addMembershipPlan(membershipPlan);
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).addMembershipPlan(membershipPlan);
	}

	@Test
	public void fetchInvoiceIdFromSubPayment_Success() throws Exception {
		List<String> stringList = new ArrayList<String>();
		stringList.add("invoice_1");
		stringList.add("invoice_2");
		stringList.add("invoice_3");
		when(membershipDao.fetchInvoiceIdFromSubPayment("sub_1")).thenReturn(stringList);
		List<String> result = membershipServiceImpl.fetchInvoiceIdFromSubPayment("sub_1");
		Assert.assertEquals(3, result.size());
		verify(membershipDao, times(1)).fetchInvoiceIdFromSubPayment("sub_1");
	}

	@Test
	public void fetchAllMembershipPlan_Success() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();
		List<MembershipPlan> membershipPlanList = new ArrayList<MembershipPlan>();
		MembershipPlan membershipPlan = new MembershipPlan();
		membershipPlan.setMembershipPlanId(1);
		MembershipPlan membershipPlan2 = new MembershipPlan();
		membershipPlan2.setMembershipPlanId(2);
		membershipPlanList.add(membershipPlan);
		membershipPlanList.add(membershipPlan2);
		when(membershipDao.fetchAllMembershipPlan(delete_flag)).thenReturn(membershipPlanList);
		List<MembershipPlan> result = membershipServiceImpl.fetchAllMembershipPlan();
		Assert.assertEquals(2, result.size());
		verify(membershipDao, times(1)).fetchAllMembershipPlan(delete_flag);
	}

	@Test
	public void fetchMemPlanTotalList_Success() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();
		when(membershipDao.fetchMemPlanTotalList(delete_flag)).thenReturn(1);
		int result = membershipServiceImpl.fetchMemPlanTotalList();
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).fetchMemPlanTotalList(delete_flag);
	}

	@Test
	public void fetchMemSubscriptionTotalList_Success() throws Exception {
		when(membershipDao.fetchMemSubscriptionTotalList()).thenReturn(1);
		int result = membershipServiceImpl.fetchMemSubscriptionTotalList();
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).fetchMemSubscriptionTotalList();
	}

	@Test
	public void addSinglePaymentOrder_Success() throws Exception {
		SinglePayment singlePayment = new SinglePayment();
		singlePayment.setOrder_id("order1");
		when(membershipDao.addSinglePaymentOrder(singlePayment)).thenReturn(1);
		int result = membershipServiceImpl.addSinglePaymentOrder(singlePayment);
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).addSinglePaymentOrder(singlePayment);
	}

	@Test
	public void updatePaymentDetails_Success() throws Exception {
		SubscriptionPayment subscriptionPayment = new SubscriptionPayment();
		SinglePayment singlePayment = new SinglePayment();

		when(membershipDao.updatePaymentDetails(subscriptionPayment, 1, singlePayment)).thenReturn(1);
		int result = membershipServiceImpl.updatePaymentDetails(subscriptionPayment, 1, singlePayment);
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).updatePaymentDetails(subscriptionPayment, 1, singlePayment);
	}

	@Test
	public void addOrder_Success() throws Exception {
		OrderDetail invoice = new OrderDetail();
		invoice.setEmailId("adv@gmail.com");
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("Shimpa");

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setRoleBasedId("ADV0000000000");
		party.setPartyId(1);

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		invoice.setCreated(timestamp);
		invoice.setCreated_by("ADV0000000000");
		invoice.setUpdated(timestamp);
		invoice.setUpdated_by("ADV0000000000");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(membershipDao.fetchPartyForSignIn("advisor", delete_flag, encryptPass)).thenReturn(party);

		when(membershipDao.addOrder(invoice, encryptPass)).thenReturn(1);
		membershipServiceImpl.addOrder(invoice);
		verify(membershipDao, times(1)).addOrder(invoice, encryptPass);
		verifyNoMoreInteractions(membershipDao);
	}

	@Test
	public void updateSubscription_Success() throws Exception {
		MemberSubscription membershipSub = new MemberSubscription();
		membershipSub.setSub_id(1);
		membershipSub.setAdvId("ADV0000000002");

		when(membershipDao.updateSubscription(membershipSub)).thenReturn(1);
		int result = membershipServiceImpl.updateSubscription("ADV0000000002", membershipSub);
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).updateSubscription(membershipSub);
	}

	@Test
	public void fetchOrderDetail_test() throws Exception {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderDetailId("0001");
		String encryptPass = advTableFields.getEncryption_password();

		when(membershipDao.fetchOrderDetail("0001", encryptPass)).thenReturn(orderDetail);
		OrderDetail result = membershipServiceImpl.fetchOrderDetail("0001");
		Assert.assertEquals("0001", result.getOrderDetailId());
		verify(membershipDao, times(1)).fetchOrderDetail("0001", encryptPass);
	}

	@Test
	public void addSubscription_Success() throws Exception {
		MemberSubscription membershipSub = new MemberSubscription();
		membershipSub.setSub_id(1);
		membershipSub.setAdvId("ADV000000000");

		when(membershipDao.addSubscription(membershipSub)).thenReturn(1);
		int result = membershipServiceImpl.addSubscription("ADV000000000", membershipSub);
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).addSubscription(membershipSub);
	}

	// @Test // if condition null error
	// public void test_updateSubscriptionWebHook() throws Exception {
	// MemberSubscription memberSubscription = new MemberSubscription();
	// memberSubscription.setAdvId("ADV0000000000");
	// PaymentEntityRequest entity = new PaymentEntityRequest();
	// entity.setAmount(1000);
	// SubscriptionChargedRequest subscriptionChargedRequest = new
	// SubscriptionChargedRequest();
	//
	// when(membershipDao.updateSubscription(memberSubscription)).thenReturn(1);
	// when(membershipDao.fetchPaymentIdFromSubPay("1")).thenReturn(1);
	// int result =
	// membershipServiceImpl.updateSubscriptionWebHook(subscriptionChargedRequest);
	// Assert.assertEquals(1, result);
	// verify(membershipDao, times(1)).updateSubscription(memberSubscription);
	// verify(membershipDao, times(1)).fetchPaymentIdFromSubPay("1");
	// verifyNoMoreInteractions(membershipDao);
	// }

	@Test
	public void updateOrderDetail_test() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setEmailId("adv@gmail.com");
		orderDetail.setCreated(timestamp);
		orderDetail.setUpdated(timestamp);

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		party.setUserName("advisor");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(membershipDao.fetchPartyForSignIn("advisor", delete_flag, encryptPass)).thenReturn(party);

		when(membershipDao.updateOrderDetail(orderDetail)).thenReturn(1);
		OrderDetail result = membershipServiceImpl.updateOrderDetail(orderDetail);
		Assert.assertEquals("adv@gmail.com", result.getEmailId());
		verify(membershipDao, times(1)).updateOrderDetail(orderDetail);
		verifyNoMoreInteractions(membershipDao);
	}

	@Test
	public void fetchAllMemberSubscription_Success() throws Exception {

		List<MemberSubscription> membersubscriptionList = new ArrayList<MemberSubscription>();
		MemberSubscription membersubscription = new MemberSubscription();
		membersubscription.setAdvId("ADV0000000000");
		membersubscription.setQuantity(20);
		membersubscriptionList.add(membersubscription);

		int pageNum = 1;
		int records = 20;
		Pageable pageable = PageRequest.of(pageNum, records);

		when(membershipDao.fetchAllMemberSubscription(pageable, pageNum, records)).thenReturn(membersubscriptionList);
		List<MemberSubscription> result = membershipServiceImpl.fetchAllMemberSubscription(1, 20);
		Assert.assertEquals(1, result.size());
		verify(membershipDao, times(1)).fetchAllMemberSubscription(pageable, pageNum, records);
	}

	// nullPointerException for if condition

	// @Test
	// public void fetchMemberSubByadvId_Success() throws Exception {
	// MemberSubscription memberSubscription = new MemberSubscription();
	// String advId = "ADV0000000001";
	// String statusCancelled = "cancel";
	// String statusCompleted = "completed";
	//
	// when(membershipDao.fetchMemberSubByAdvId("ADV0000000001", "cancel",
	// "completed"))
	// .thenReturn(memberSubscription);
	// MemberSubscription result =
	// membershipServiceImpl.fetchMemberSubByadvId("ADV0000000001");
	//
	// Assert.assertEquals("cancel", result);
	// verify(membershipDao, times(1)).fetchMemberSubByAdvId("ADV0000000001",
	// "cancel", "completed");
	//
	// }

	@Test
	public void fetchSinglePaymentByPrimaryKey_Success() throws Exception {
		SinglePayment singlePayment = new SinglePayment();
		singlePayment.setSinglePaymentId(1);
		when(membershipDao.fetchSinglePaymentByPrimaryKey(1)).thenReturn(singlePayment);
		SinglePayment result = membershipServiceImpl.fetchSinglePaymentByPrimaryKey(1);
		Assert.assertEquals(1, result.getSinglePaymentId());
		verify(membershipDao, times(1)).fetchSinglePaymentByPrimaryKey(1);
		verifyNoMoreInteractions(membershipDao);

	}

	@Test
	public void fetchMemberSinglePaySubByAdvId_Success() throws Exception {
		SinglePayment singlePaymentId = new SinglePayment();
		singlePaymentId.setRoleBasedId("INV0000000001");
		String AdvId = "INV0000000001";
		String SinglePaidStatus = "done";
		String Type = "advisor";
		when(membershipDao.fetchMemberSinglePaySubByAdvId("INV0000000001", "done", "advisor"))
				.thenReturn(singlePaymentId);
		SinglePayment result = membershipServiceImpl.fetchMemberSinglePaySubByAdvId("INV0000000001", "done", "advisor");
		Assert.assertEquals("INV0000000001", result.getRoleBasedId());
		verify(membershipDao, times(1)).fetchMemberSinglePaySubByAdvId("INV0000000001", "done", "advisor");
		verifyNoMoreInteractions(membershipDao);

	}

	@Test
	public void test_checkAdvisorIsPresent_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		when(membershipDao.checkAdvisorIsPresent("ADV000000000A", deleteflag)).thenReturn(1);
		int result = membershipServiceImpl.checkAdvisorIsPresent("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(membershipDao, times(1)).checkAdvisorIsPresent("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(membershipDao);
	}

	@Test
	public void test_generateSubOrderNumber_Success() throws Exception {
		when(membershipDao.fetchSubOrderNumber()).thenReturn("ORDP00000001");
		when(membershipDao.addSubOrderNumber("ORDP00000002")).thenReturn(1);
		String result = membershipServiceImpl.generateSubOrderNumber();
		Assert.assertEquals("ORDP00000002", result);
		verify(membershipDao, times(1)).fetchSubOrderNumber();
		verify(membershipDao, times(1)).addSubOrderNumber("ORDP00000002");
		verifyNoMoreInteractions(membershipDao);
	}

	@Test
	public void test_generateSubOrderNumber_Error() throws Exception {
		when(membershipDao.fetchSubOrderNumber()).thenReturn("ORDP00000001");
		when(membershipDao.addSubOrderNumber("ORDP00000002")).thenReturn(0);
		String result = membershipServiceImpl.generateSubOrderNumber();
		Assert.assertEquals(null, result);
		verify(membershipDao, times(1)).fetchSubOrderNumber();
		verify(membershipDao, times(1)).addSubOrderNumber("ORDP00000002");
		verifyNoMoreInteractions(membershipDao);
	}

	@Test
	public void test_generatePaymentOrderNumber_Success() throws Exception {
		when(membershipDao.fetchPaymentOrderNumber()).thenReturn("ORDP00000001");
		when(membershipDao.addPaymentOrderNumber("ORDP00000002")).thenReturn(1);
		String result = membershipServiceImpl.generatePaymentOrderNumber();
		Assert.assertEquals("ORDP00000002", result);
		verify(membershipDao, times(1)).fetchPaymentOrderNumber();
		verify(membershipDao, times(1)).addPaymentOrderNumber("ORDP00000002");
		verifyNoMoreInteractions(membershipDao);
	}

	@Test
	public void test_generatePaymentOrderNumber_Error() throws Exception {
		when(membershipDao.fetchPaymentOrderNumber()).thenReturn("ORDP00000001");
		when(membershipDao.addPaymentOrderNumber("ORDP00000002")).thenReturn(0);
		String result = membershipServiceImpl.generatePaymentOrderNumber();
		Assert.assertEquals(null, result);
		verify(membershipDao, times(1)).fetchPaymentOrderNumber();
		verify(membershipDao, times(1)).addPaymentOrderNumber("ORDP00000002");
		verifyNoMoreInteractions(membershipDao);
	}

}
