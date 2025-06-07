package com.sowisetech.calc.service;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.dao.CalcDao;
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
import com.sowisetech.calc.model.FutureValue;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.Insurance;
import com.sowisetech.calc.model.InsuranceItem;
import com.sowisetech.calc.model.InterestChange;
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
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.Urgency;
import com.sowisetech.calc.util.CalcTableFields;
import com.sowisetech.common.util.AdminSignin;

public class CalcServiceImplTest {

	@InjectMocks
	private CalcServiceImpl calcServiceImpl;
	private MockMvc mockMvc;
	@Mock
	private CalcDao calcDao;
	@Mock
	SecurityContextHolder mockSecurityContext;

	@Autowired(required = true)
	@Spy
	CalcTableFields calcTableFields;
	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;
	@Autowired(required = true)
	@Spy
	AdminSignin adminSignin;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(calcServiceImpl).build();
	}

	@Test
	public void test_calculateGoalFutureCost() throws Exception {
		double ans = 4105707.15;
		double result = calcServiceImpl.calculateGoalFutureCost(3000000, 0.04, 8);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_calculateGoalCurrentInvestment() throws Exception {
		double ans = 2753120.58;
		double result = calcServiceImpl.calculateGoalCurrentInvestment(900000, 0.15, 8);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_calculateGoalFinalCorpus() throws Exception {
		double futureCost = 4105707.15;
		double futureValue = 2753120.58;
		double ans = 1352586.57;
		double result = calcServiceImpl.calculateGoalFinalCorpus(futureCost, futureValue);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_calculateGoalMonthlyInvestment() throws Exception {
		double ans = 2823.15;
		double result = calcServiceImpl.calculateGoalMonthlyInvestment(0.15, 0.25, 1352586.57, 8, 0.0125);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_calculateGoalMonthlyInvestment_GrowthAndAnnualRate() throws Exception {
		double ans = 4275.34;
		double result = calcServiceImpl.calculateGoalMonthlyInvestment(0.15, 0.15, 1352586.57, 8, 0.0125);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_calculateGoalAnnualyInvestment() throws Exception {
		double ans = 40537.17;
		double result = calcServiceImpl.calculateGoalAnnualyInvestment(0.15, 0.25, 1352586.57, 8);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_calculateGoalAnnualyInvestment_GrowthAndAnnualRate() throws Exception {
		double ans = 517199.16;
		double result = calcServiceImpl.calculateGoalAnnualyInvestment(0.15, 0.15, 1352586.57, 8);
		String str = String.format("%.2f", result);
		double roundedValue = Double.valueOf(str);
		Assert.assertEquals(ans, roundedValue, 0.00);
	}

	@Test
	public void test_addGoalInfo() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		Goal goal = new Goal();
		goal.setGoalName("Education");
		goal.setCurrentAmount(150000);
		goal.setCreated(timestamp);
		goal.setUpdated(timestamp);
		goal.setCreated_by(signedUserId);
		goal.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addGoalInfo(goal)).thenReturn(1);
		int result = calcServiceImpl.addGoalInfo(goal);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addGoalInfo(goal);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowItemTypeIdByItemId_Success() throws Exception {
		when(calcDao.fetchCashFlowItemTypeIdByItemId(1)).thenReturn(1);
		int cashFlow1 = calcServiceImpl.fetchCashFlowItemTypeIdByItemId(1);
		Assert.assertEquals(1, cashFlow1);
		verify(calcDao, times(1)).fetchCashFlowItemTypeIdByItemId(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addAndModifyCashFlow_add() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<CashFlow> cashFlowList = new ArrayList<>();
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setActualAmt(15000);
		cashFlow.setDate("12-10-2016");
		cashFlow.setCreated(timestamp);
		cashFlow.setUpdated(timestamp);
		cashFlow.setCreated_by(signedUserId);
		cashFlow.setUpdated_by(signedUserId);
		cashFlowList.add(cashFlow);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.fetchCashFlowItemTypeIdByItemId(cashFlow.getCashFlowItemId())).thenReturn(1);
		when(calcDao.checkCashFlowIsPresent(cashFlow.getReferenceId(), cashFlow.getCashFlowItemId())).thenReturn(0);
		when(calcDao.addCashFlow(cashFlow)).thenReturn(1);
		int result = calcServiceImpl.addAndModifyCashFlow(cashFlowList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).fetchCashFlowItemTypeIdByItemId(cashFlow.getCashFlowItemId());
		verify(calcDao, times(1)).checkCashFlowIsPresent(cashFlow.getReferenceId(), cashFlow.getCashFlowItemId());
		verify(calcDao, times(1)).addCashFlow(cashFlow);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addAndModifyCashFlow_modify() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<CashFlow> cashFlowList = new ArrayList<>();
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setActualAmt(15000);
		cashFlow.setDate("12-10-2016");
		cashFlow.setCreated(timestamp);
		cashFlow.setUpdated(timestamp);
		cashFlow.setCreated_by(signedUserId);
		cashFlow.setUpdated_by(signedUserId);
		cashFlowList.add(cashFlow);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.fetchCashFlowItemTypeIdByItemId(cashFlow.getCashFlowItemId())).thenReturn(1);
		when(calcDao.checkCashFlowIsPresent(cashFlow.getReferenceId(), cashFlow.getCashFlowItemId())).thenReturn(1);
		when(calcDao.updateCashFlow(cashFlow)).thenReturn(1);
		int result = calcServiceImpl.addAndModifyCashFlow(cashFlowList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).fetchCashFlowItemTypeIdByItemId(cashFlow.getCashFlowItemId());
		verify(calcDao, times(1)).checkCashFlowIsPresent(cashFlow.getReferenceId(), cashFlow.getCashFlowItemId());
		verify(calcDao, times(1)).updateCashFlow(cashFlow);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowByRefIdAndTypeId() throws Exception {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		CashFlow cashFlow1 = new CashFlow();
		cashFlow1.setActualAmt(15000);
		cashFlow1.setReferenceId("P00000");
		cashFlow1.setBudgetAmt(12000);
		cashFlow1.setCashFlowItemTypeId(2);
		CashFlow cashFlow2 = new CashFlow();
		cashFlow2.setActualAmt(14000);
		cashFlow2.setReferenceId("P00000");
		cashFlow2.setBudgetAmt(10000);
		cashFlow2.setCashFlowItemTypeId(2);
		cashFlowList.add(cashFlow1);
		cashFlowList.add(cashFlow2);
		when(calcDao.fetchCashFlowByRefIdAndTypeId("P00000", 2)).thenReturn(cashFlowList);
		List<CashFlow> categoryList = calcServiceImpl.fetchCashFlowByRefIdAndTypeId("P00000", 2);
		Assert.assertEquals(2, categoryList.size());
		Assert.assertEquals(2, categoryList.get(0).getCashFlowItemTypeId());

		verify(calcDao, times(1)).fetchCashFlowByRefIdAndTypeId("P00000", 2);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRecurringExpenditureItemType() throws Exception {
		when(calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getMandatory_household_expense())).thenReturn(1);
		List<Integer> returnLists = calcServiceImpl.fetchRecurringExpenditureItemType();
		Assert.assertEquals(4, returnLists.size());
		Assert.assertEquals(1, returnLists.get(0).intValue());
	}

	// @Test
	// public void test_fetchNonRecurringExpenditureByRefId() throws Exception {
	// List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
	// CashFlow cashFlow1 = new CashFlow();
	// cashFlow1.setActualAmt(15000);
	// cashFlow1.setReferenceId("P00000");
	// cashFlow1.setBudgetAmt(12000);
	// cashFlow1.setCashFlowItemTypeId(2);
	//
	// CashFlow cashFlow2 = new CashFlow();
	// cashFlow2.setActualAmt(14000);
	// cashFlow2.setReferenceId("P00000");
	// cashFlow2.setBudgetAmt(10000);
	// cashFlow2.setCashFlowItemTypeId(2);
	//
	// cashFlowList.add(cashFlow1);
	// cashFlowList.add(cashFlow2);
	// when(calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getNon_recurring_expenditures())).thenReturn(1);
	// when(calcDao.fetchCashFlowByRefIdAndTypeId("P00000",
	// 1)).thenReturn(cashFlowList);
	//
	// List<CashFlow> categoryList =
	// calcServiceImpl.fetchNonRecurringExpenditureByRefId("P00000");
	// Assert.assertEquals(2, categoryList.size());
	// Assert.assertEquals(2, categoryList.get(0).getCashFlowItemTypeId());
	// }

	@Test
	public void test_fetchRecurringIncomeByRefId() throws Exception {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		CashFlow cashFlow1 = new CashFlow();
		cashFlow1.setActualAmt(15000);
		cashFlow1.setReferenceId("P00000");
		cashFlow1.setBudgetAmt(12000);
		cashFlow1.setCashFlowItemTypeId(6);

		CashFlow cashFlow2 = new CashFlow();
		cashFlow2.setActualAmt(14000);
		cashFlow2.setReferenceId("P00000");
		cashFlow2.setBudgetAmt(10000);
		cashFlow2.setCashFlowItemTypeId(6);

		cashFlowList.add(cashFlow1);
		cashFlowList.add(cashFlow2);
		when(calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getRecurring_income())).thenReturn(1);
		when(calcDao.fetchCashFlowByRefIdAndTypeId("P00000", 1)).thenReturn(cashFlowList);
		List<CashFlow> categoryList = calcServiceImpl.fetchRecurringIncomeByRefId("P00000");
		Assert.assertEquals(2, categoryList.size());
		Assert.assertEquals(6, categoryList.get(0).getCashFlowItemTypeId());
	}

	// @Test
	// public void test_fetchNonRecurringIncomeByRefId() throws Exception {
	// List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
	// CashFlow cashFlow1 = new CashFlow();
	// cashFlow1.setActualAmt(15000);
	// cashFlow1.setReferenceId("P00000");
	// cashFlow1.setBudgetAmt(12000);
	// cashFlow1.setCashFlowItemTypeId(7);
	// CashFlow cashFlow2 = new CashFlow();
	// cashFlow2.setActualAmt(14000);
	// cashFlow2.setReferenceId("P00000");
	// cashFlow2.setBudgetAmt(10000);
	// cashFlow2.setCashFlowItemTypeId(7);
	//
	// cashFlowList.add(cashFlow1);
	// cashFlowList.add(cashFlow2);
	// when(calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getNon_recurring_income())).thenReturn(1);
	// when(calcDao.fetchCashFlowByRefIdAndTypeId("P00000",
	// 1)).thenReturn(cashFlowList);
	// List<CashFlow> categoryList =
	// calcServiceImpl.fetchNonRecurringIncomeByRefId("P00000");
	// Assert.assertEquals(2, categoryList.size());
	// Assert.assertEquals(7, categoryList.get(0).getCashFlowItemTypeId());
	// }

	@Test
	public void test_addCashFlowSummary() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setMonthlyExpense(12000);
		cashFlowSummary.setCreated(timestamp);
		cashFlowSummary.setUpdated(timestamp);
		cashFlowSummary.setCreated_by(signedUserId);
		cashFlowSummary.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addCashFlowSummary(cashFlowSummary)).thenReturn(1);
		calcServiceImpl.addCashFlowSummary(cashFlowSummary);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addCashFlowSummary(cashFlowSummary);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addAndModifyNetworth_add() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<Networth> networthList = new ArrayList<>();
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(17000);
		networth.setCreated(timestamp);
		networth.setUpdated(timestamp);
		networth.setCreated_by(signedUserId);
		networth.setUpdated_by(signedUserId);
		networthList.add(networth);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addNetworth(networth)).thenReturn(1);
		when(calcDao.fetchAccountTypeIdByEntryId(networth.getAccountEntryId())).thenReturn(1);
		when(calcDao.checkNetworthIsPresent(networth.getReferenceId(), networth.getAccountEntryId())).thenReturn(0);
		int result = calcServiceImpl.addAndModifyNetworth(networthList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).fetchAccountTypeIdByEntryId(networth.getAccountEntryId());
		verify(calcDao, times(1)).checkNetworthIsPresent(networth.getReferenceId(), networth.getAccountEntryId());

		verify(calcDao, times(1)).addNetworth(networth);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addAndModifyNetworth_modify() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<Networth> networthList = new ArrayList<>();
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setValue(120000);
		networth.setFutureValue(17000);
		networth.setCreated(timestamp);
		networth.setUpdated(timestamp);
		networth.setCreated_by(signedUserId);
		networth.setUpdated_by(signedUserId);
		networthList.add(networth);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateNetworth(networth)).thenReturn(1);
		when(calcDao.fetchAccountTypeIdByEntryId(networth.getAccountEntryId())).thenReturn(1);
		when(calcDao.checkNetworthIsPresent(networth.getReferenceId(), networth.getAccountEntryId())).thenReturn(1);
		int result = calcServiceImpl.addAndModifyNetworth(networthList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).fetchAccountTypeIdByEntryId(networth.getAccountEntryId());
		verify(calcDao, times(1)).checkNetworthIsPresent(networth.getReferenceId(), networth.getAccountEntryId());
		verify(calcDao, times(1)).updateNetworth(networth);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchAccountTypeIdByEntryId() throws Exception {
		when(calcDao.fetchAccountTypeIdByEntryId(1)).thenReturn(1);
		int networth1 = calcServiceImpl.fetchAccountTypeIdByEntryId(1);
		Assert.assertEquals(1, networth1);
		verify(calcDao, times(1)).fetchAccountTypeIdByEntryId(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchNetworthByAssets() throws Exception {
		List<Networth> networthList = new ArrayList<Networth>();
		Networth networth1 = new Networth();
		networth1.setAccountTypeId(1);
		networth1.setReferenceId("P00000");
		networth1.setValue(12000);
		Networth networth2 = new Networth();
		networth2.setAccountTypeId(2);
		networth2.setReferenceId("P00000");
		networth2.setValue(18000);
		networthList.add(networth1);
		networthList.add(networth2);
		when(calcDao.fetchAccountTypeIdByAccountType(calcTableFields.getAssets())).thenReturn(1);
		when(calcDao.fetchNetworthByAccountTypeIdAndRefId(1, "P00000")).thenReturn(networthList);
		List<Networth> networthList1 = calcServiceImpl.fetchNetworthByAssets("P00000");
		Assert.assertEquals(2, networthList1.size());
		Assert.assertEquals(12000, networthList1.get(0).getValue(), 0.00);
	}

	@Test
	public void test_fetchNetworthByLiabilities() throws Exception {
		List<Networth> networthList = new ArrayList<Networth>();
		Networth networth1 = new Networth();
		networth1.setAccountTypeId(1);
		networth1.setReferenceId("P00000");
		networth1.setValue(12000);
		Networth networth2 = new Networth();
		networth2.setAccountTypeId(2);
		networth2.setReferenceId("P00000");
		networth2.setValue(18000);
		networthList.add(networth1);
		networthList.add(networth2);
		when(calcDao.fetchAccountTypeIdByAccountType(calcTableFields.getLiabilities())).thenReturn(1);
		when(calcDao.fetchNetworthByAccountTypeIdAndRefId(1, "P00000")).thenReturn(networthList);
		List<Networth> networthList1 = calcServiceImpl.fetchNetworthByLiabilities("P00000");
		Assert.assertEquals(2, networthList1.size());
		Assert.assertEquals(12000, networthList1.get(0).getValue(), 0.00);
	}

	@Test
	public void test_addNetworthSummary() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setFuture_networth(12000);
		networthSummary.setFuture_assetValue(15000);
		networthSummary.setCreated(timestamp);
		networthSummary.setUpdated(timestamp);
		networthSummary.setCreated_by("ADV0000000001");
		networthSummary.setUpdated_by("ADV0000000001");
		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addNetworthSummary(networthSummary)).thenReturn(1);
		calcServiceImpl.addNetworthSummary(networthSummary);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addNetworthSummary(networthSummary);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addInsurance() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		Insurance insurance = new Insurance();
		insurance.setReferenceId("P00000");
		insurance.setPredictability("predictable");
		insurance.setAnnualIncome(15000);
		insurance.setCreated(timestamp);
		insurance.setUpdated(timestamp);
		insurance.setCreated_by(signedUserId);
		insurance.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addInsurance(insurance)).thenReturn(1);
		calcServiceImpl.addInsurance(insurance);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addInsurance(insurance);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addAndModifyPriorities_add() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<Priority> priorityList = new ArrayList<>();
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setCreated(timestamp);
		priority.setUpdated(timestamp);
		priority.setCreated_by(signedUserId);
		priority.setUpdated_by(signedUserId);
		priorityList.add(priority);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.checkPriorityByRefIdAndItemId(priority.getReferenceId(), priority.getPriorityItemId()))
				.thenReturn(0);
		when(calcDao.addPriorities(priority)).thenReturn(1);
		int result = calcServiceImpl.addAndModifyPriorities(priorityList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).checkPriorityByRefIdAndItemId(priority.getReferenceId(),
				priority.getPriorityItemId());

		verify(calcDao, times(1)).addPriorities(priority);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addAndModifyPriorities_modify() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<Priority> priorityList = new ArrayList<>();
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setCreated(timestamp);
		priority.setUpdated(timestamp);
		priority.setCreated_by(signedUserId);
		priority.setUpdated_by(signedUserId);
		priorityList.add(priority);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.checkPriorityByRefIdAndItemId(priority.getReferenceId(), priority.getPriorityItemId()))
				.thenReturn(1);
		when(calcDao.updatePriority(priority)).thenReturn(1);
		int result = calcServiceImpl.addAndModifyPriorities(priorityList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).checkPriorityByRefIdAndItemId(priority.getReferenceId(),
				priority.getPriorityItemId());
		verify(calcDao, times(1)).updatePriority(priority);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPriorityByRefId() throws Exception {
		List<Priority> priorityList = new ArrayList<Priority>();
		Priority priority1 = new Priority();
		priority1.setReferenceId("P00000");
		// priority1.setTimeLine(5);
		Priority priority2 = new Priority();
		priority2.setReferenceId("P00000");
		// priority2.setTimeLine(3);
		priorityList.add(priority1);
		priorityList.add(priority2);
		when(calcDao.fetchPriorityByRefId("P00000")).thenReturn(priorityList);
		List<Priority> priorityList1 = calcServiceImpl.fetchPriorityByRefId("P00000");
		Assert.assertEquals(2, priorityList1.size());
		// Assert.assertEquals(5, priorityList1.get(0).getTimeLine());

		verify(calcDao, times(1)).fetchPriorityByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPriorityByRefIdAndItemId() throws Exception {
		Priority priority = new Priority();
		priority.setUrgencyId(3);
		priority.setReferenceId("P00000");
		when(calcDao.fetchPriorityByRefIdAndItemId("P00000", 1)).thenReturn(priority);
		Priority priority1 = calcServiceImpl.fetchPriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(3, priority1.getUrgencyId());
		verify(calcDao, times(1)).fetchPriorityByRefIdAndItemId("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updatePriorityOrder() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updatePriorityOrder("P00000", 1, 1, signedUserId, timestamp)).thenReturn(1);
		calcServiceImpl.updatePriorityOrder("P00000", 1, 1);
	}

	@Test
	public void test_fetchParty() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setEmailId("aaa");
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = calcTableFields.getDelete_flag_N();
		when(calcDao.fetchParty(1, delete_flag, encryptPass)).thenReturn(party);
		Party party1 = calcServiceImpl.fetchParty(1);
		Assert.assertEquals("aaa", party1.getEmailId());
		verify(calcDao, times(1)).fetchParty(1, delete_flag, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	// @Test // test//
	// public void test_addAndModifyRiskProfile_add() throws Exception { //Error
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	// String encryptPass = advTableFields.getEncryption_password();
	// String delete_flag = advTableFields.getDelete_flag_N();
	//
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleBasedId("ADV0000000000");
	//
	// String signedUserId = party.getRoleBasedId();
	//
	// List<RiskProfile> riskProfileList = new ArrayList<>();
	// RiskProfile riskProfile = new RiskProfile();
	// riskProfile.setReferenceId("P00000");
	// riskProfile.setAnswerId(1);
	// riskProfile.setQuestionId("2");
	// riskProfile.setCreated(timestamp);
	// riskProfile.setUpdated(timestamp);
	// riskProfile.setCreated_by(signedUserId);
	// riskProfile.setUpdated_by(signedUserId);
	//
	// // Authentication authentication = Mockito.mock(Authentication.class);
	// // SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// //
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// // SecurityContextHolder.setContext(securityContext);
	// // when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new
	// // ArrayList<>()));
	//
	// when(calcDao.fetchScoreByAnswerId(riskProfile)).thenReturn(1);
	// when(calcDao.fetchPartyForSignIn("adv", delete_flag,
	// encryptPass)).thenReturn(party);
	// when(calcDao.addRiskProfile(riskProfile)).thenReturn(1);
	// when(calcDao.checkRiskProfileIsPresent("P00000", "2")).thenReturn(0);
	// int result = calcServiceImpl.addAndModifyRiskProfile(riskProfileList);
	// Assert.assertEquals(1, result);
	// verify(calcDao, times(1)).fetchScoreByAnswerId(riskProfile);
	// // verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag,
	// // encryptPass);
	// verify(calcDao,
	// times(1)).checkRiskProfileIsPresent(riskProfile.getReferenceId(),
	// riskProfile.getQuestionId());
	//
	// verify(calcDao, times(1)).addRiskProfile(riskProfile);
	// verifyNoMoreInteractions(calcDao);
	//
	// }

	@Test // test//
	public void test_addAndModifyRiskProfile_modify() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		List<RiskProfile> riskProfileList = new ArrayList<>();
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(1);
		riskProfile.setQuestionId("2");
		riskProfile.setCreated(timestamp);
		riskProfile.setUpdated(timestamp);
		riskProfile.setCreated_by(signedUserId);
		riskProfile.setUpdated_by(signedUserId);
		riskProfileList.add(riskProfile);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchScoreByAnswerId(riskProfile)).thenReturn(1);
		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateRiskProfile(riskProfile)).thenReturn(1);
		when(calcDao.checkRiskProfileIsPresent(riskProfile.getReferenceId(), riskProfile.getQuestionId()))
				.thenReturn(1);
		int result = calcServiceImpl.addAndModifyRiskProfile(riskProfileList);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchScoreByAnswerId(riskProfile);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).checkRiskProfileIsPresent(riskProfile.getReferenceId(), riskProfile.getQuestionId());
		verify(calcDao, times(1)).updateRiskProfile(riskProfile);
		verifyNoMoreInteractions(calcDao);

	}

	@Test
	public void test_fetchRiskProfileByRefId() throws Exception {
		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		RiskProfile risk1 = new RiskProfile();
		risk1.setReferenceId("P00000");
		risk1.setAnswerId(1);
		risk1.setRiskProfileId(2);
		RiskProfile risk2 = new RiskProfile();
		risk2.setReferenceId("P00000");
		risk2.setAnswerId(2);
		risk2.setQuestionId("1");
		riskProfileList.add(risk1);
		riskProfileList.add(risk2);
		when(calcDao.fetchRiskProfileByRefId("P00000")).thenReturn(riskProfileList);
		List<RiskProfile> RiskProfileList1 = calcServiceImpl.fetchRiskProfileByRefId("P00000");
		Assert.assertEquals(2, RiskProfileList1.size());
		Assert.assertEquals(1, RiskProfileList1.get(0).getAnswerId());

		verify(calcDao, times(1)).fetchRiskProfileByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskPortfolioByPoints() throws Exception {
		RiskPortfolio riskPortfolio = new RiskPortfolio();
		riskPortfolio.setCash(1);
		riskPortfolio.setDebt(2);
		when(calcDao.fetchRiskPortfolioByPoints("30 or less")).thenReturn(riskPortfolio);
		RiskPortfolio riskPortfolio1 = calcServiceImpl.fetchRiskPortfolioByPoints("30 or less");
		Assert.assertEquals(1, riskPortfolio1.getCash());
		verify(calcDao, times(1)).fetchRiskPortfolioByPoints("30 or less");
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addRiskSummary() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00000");
		riskSummary.setCash_alloc(2);
		riskSummary.setCreated(timestamp);
		riskSummary.setUpdated(timestamp);
		riskSummary.setCreated_by(signedUserId);
		riskSummary.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addRiskSummary(riskSummary)).thenReturn(1);
		int result = calcServiceImpl.addRiskSummary(riskSummary);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addRiskSummary(riskSummary);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchAccountTypeList() throws Exception {
		List<AccountType> accountTypeList = new ArrayList<AccountType>();
		AccountType type1 = new AccountType();
		type1.setAccountType("Assets");
		type1.setAccountTypeId(1);
		AccountType type2 = new AccountType();
		type2.setAccountType("Liabilities");
		type2.setAccountTypeId(2);
		accountTypeList.add(type1);
		accountTypeList.add(type2);
		when(calcDao.fetchAccountTypeList()).thenReturn(accountTypeList);
		List<AccountType> accountTypeList1 = calcServiceImpl.fetchAccountTypeList();
		Assert.assertEquals(2, accountTypeList1.size());
		Assert.assertEquals("Assets", accountTypeList1.get(0).getAccountType());

		verify(calcDao, times(1)).fetchAccountTypeList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchAccountList() throws Exception {
		List<Account> accountList = new ArrayList<Account>();
		Account account1 = new Account();
		account1.setAccountEntry("Savings Account Balance as on date");
		account1.setAccountTypeId(1);
		account1.setAccountEntryId(1);
		Account account2 = new Account();
		account2.setAccountEntry("Home Loan Out Standing");
		account2.setAccountTypeId(2);
		account2.setAccountEntryId(12);
		accountList.add(account1);
		accountList.add(account2);
		when(calcDao.fetchAccountList()).thenReturn(accountList);
		List<Account> accountList1 = calcServiceImpl.fetchAccountList();
		Assert.assertEquals(2, accountList1.size());
		Assert.assertEquals("Home Loan Out Standing", accountList1.get(1).getAccountEntry());

		verify(calcDao, times(1)).fetchAccountList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowItemTypeList() throws Exception {
		List<CashFlowItemType> cashFlowItemTypeList = new ArrayList<CashFlowItemType>();
		CashFlowItemType cashFlowItemType1 = new CashFlowItemType();
		cashFlowItemType1.setCashFlowItemType("Mandatory Household Expense");
		cashFlowItemType1.setCashFlowItemTypeId(1);
		CashFlowItemType cashFlowItemType2 = new CashFlowItemType();
		cashFlowItemType2.setCashFlowItemType("Non Recurring Expenditures");
		cashFlowItemType2.setCashFlowItemTypeId(5);
		cashFlowItemTypeList.add(cashFlowItemType1);
		cashFlowItemTypeList.add(cashFlowItemType2);
		when(calcDao.fetchCashFlowItemTypeList()).thenReturn(cashFlowItemTypeList);
		List<CashFlowItemType> cashFlowItemTypeList1 = calcServiceImpl.fetchCashFlowItemTypeList();
		Assert.assertEquals(2, cashFlowItemTypeList1.size());
		Assert.assertEquals("Non Recurring Expenditures", cashFlowItemTypeList1.get(1).getCashFlowItemType());

		verify(calcDao, times(1)).fetchCashFlowItemTypeList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowItemList() throws Exception {
		List<CashFlowItem> cashFlowItemList = new ArrayList<CashFlowItem>();
		CashFlowItem cashFlowItem1 = new CashFlowItem();
		cashFlowItem1.setCashFlowItem("House Rent");
		cashFlowItem1.setCashFlowItemId(1);
		cashFlowItem1.setCashFlowItemTypeId(1);
		CashFlowItem cashFlowItem2 = new CashFlowItem();
		cashFlowItem2.setCashFlowItem("Cooking Gas");
		cashFlowItem2.setCashFlowItemId(10);
		cashFlowItem2.setCashFlowItemTypeId(1);
		cashFlowItemList.add(cashFlowItem1);
		cashFlowItemList.add(cashFlowItem2);
		when(calcDao.fetchCashFlowItemList()).thenReturn(cashFlowItemList);
		List<CashFlowItem> cashFlowItemList1 = calcServiceImpl.fetchCashFlowItemList();
		Assert.assertEquals(2, cashFlowItemList1.size());
		Assert.assertEquals("House Rent", cashFlowItemList1.get(0).getCashFlowItem());
		Assert.assertEquals(1, cashFlowItemList1.get(0).getCashFlowItemTypeId());
		verify(calcDao, times(1)).fetchCashFlowItemList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPriorityItemList() throws Exception {
		List<PriorityItem> priorityItemList = new ArrayList<PriorityItem>();
		PriorityItem priorityItem1 = new PriorityItem();
		priorityItem1.setPriorityItem("Starting a Mutual Funds Sip");
		priorityItem1.setPriorityItemId(2);
		PriorityItem priorityItem2 = new PriorityItem();
		priorityItem2.setPriorityItem("Purchasing a car");
		priorityItem2.setPriorityItemId(7);
		priorityItemList.add(priorityItem1);
		priorityItemList.add(priorityItem2);
		when(calcDao.fetchPriorityItemList()).thenReturn(priorityItemList);
		List<PriorityItem> priorityItemList1 = calcServiceImpl.fetchPriorityItemList();
		Assert.assertEquals(2, priorityItemList1.size());
		Assert.assertEquals("Purchasing a car", priorityItemList1.get(1).getPriorityItem());

		verify(calcDao, times(1)).fetchPriorityItemList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchUrgencyList() throws Exception {
		List<Urgency> urgencyList = new ArrayList<Urgency>();
		Urgency urgency1 = new Urgency();
		urgency1.setUrgencyId(1);
		urgency1.setValue("Low");
		Urgency urgency2 = new Urgency();
		urgency2.setUrgencyId(2);
		urgency2.setValue("Medium");
		urgencyList.add(urgency1);
		urgencyList.add(urgency2);
		when(calcDao.fetchUrgencyList()).thenReturn(urgencyList);
		List<Urgency> urgencyList1 = calcServiceImpl.fetchUrgencyList();
		Assert.assertEquals(2, urgencyList1.size());
		Assert.assertEquals("Medium", urgencyList1.get(1).getValue());

		verify(calcDao, times(1)).fetchUrgencyList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskPortfolioList() throws Exception {
		List<RiskPortfolio> riskPortfolioList = new ArrayList<RiskPortfolio>();
		RiskPortfolio riskPortfolio1 = new RiskPortfolio();
		riskPortfolio1.setBehaviour("High Growth Investor");
		riskPortfolio1.setPoints("30 or less");
		RiskPortfolio riskPortfolio2 = new RiskPortfolio();
		riskPortfolio2.setBehaviour("Balanced Investor");
		riskPortfolio2.setPoints("41 - 51");
		riskPortfolioList.add(riskPortfolio1);
		riskPortfolioList.add(riskPortfolio2);
		when(calcDao.fetchRiskPortfolioList()).thenReturn(riskPortfolioList);
		List<RiskPortfolio> riskPortfolioList1 = calcServiceImpl.fetchRiskPortfolioList();
		Assert.assertEquals(2, riskPortfolioList1.size());
		Assert.assertEquals("41 - 51", riskPortfolioList1.get(1).getPoints());

		verify(calcDao, times(1)).fetchRiskPortfolioList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskQuestionaireList() throws Exception {
		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire1 = new RiskQuestionaire();
		riskQuestionaire1.setAnswer("I enjoy taking very high risk as the same rewards high return");
		riskQuestionaire1.setScore(1);
		RiskQuestionaire riskQuestionaire2 = new RiskQuestionaire();
		riskQuestionaire2.setAnswer("I have some experience to understand some aspects of investment schemes/markets");
		riskQuestionaire2.setScore(4);
		riskQuestionaireList.add(riskQuestionaire1);
		riskQuestionaireList.add(riskQuestionaire2);
		when(calcDao.fetchRiskQuestionaireList()).thenReturn(riskQuestionaireList);
		List<RiskQuestionaire> riskQuestionaireList1 = calcServiceImpl.fetchRiskQuestionaireList();
		Assert.assertEquals(2, riskQuestionaireList1.size());
		Assert.assertEquals(4, riskQuestionaireList1.get(1).getScore());

		verify(calcDao, times(1)).fetchRiskQuestionaireList();
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPartyIdByRoleBasedId() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setPartyStatusId(1);
		party.setEmailId("aaa");
		String deleteFlag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(calcDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteFlag, encryptPass)).thenReturn(party);
		Party party1 = calcServiceImpl.fetchPartyIdByRoleBasedId("ADV000000000A");
		Assert.assertEquals("aaa", party1.getEmailId());
		verify(calcDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteFlag, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchGoalByRefId() throws Exception {
		List<Goal> goalList = new ArrayList<Goal>();
		Goal goal1 = new Goal();
		goal1.setReferenceId("P00000");
		goal1.setGoalAmount(3000000);
		goal1.setInflationRate(4.0);
		goal1.setRateOfReturn(1.25);
		Goal goal2 = new Goal();
		goal2.setReferenceId("P00000");
		goal2.setGoalName("Education");
		goal2.setGrowthRate(15);
		goalList.add(goal1);
		goalList.add(goal2);
		when(calcDao.fetchGoalByReferenceId("P00000")).thenReturn(goalList);
		List<Goal> goalList1 = calcServiceImpl.fetchGoalByReferenceId("P00000");
		Assert.assertEquals(2, goalList1.size());
		Assert.assertEquals("Education", goalList1.get(1).getGoalName());
		Assert.assertEquals(4, goalList1.get(0).getInflationRate(), 0.0);

		verify(calcDao, times(1)).fetchGoalByReferenceId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowByRefId() throws Exception {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		CashFlow cashFlow1 = new CashFlow();
		cashFlow1.setReferenceId("P00000");
		cashFlow1.setBudgetAmt(1000);
		cashFlow1.setDate("JUL-2000");
		CashFlow cashFlow2 = new CashFlow();
		cashFlow2.setReferenceId("P00000");
		cashFlow2.setActualAmt(3000);
		cashFlow2.setBudgetAmt(2000);
		cashFlowList.add(cashFlow1);
		cashFlowList.add(cashFlow2);
		when(calcDao.fetchCashFlowByRefId("P00000")).thenReturn(cashFlowList);
		List<CashFlow> cashFlowList1 = calcServiceImpl.fetchCashFlowByRefId("P00000");
		Assert.assertEquals(2, cashFlowList1.size());
		Assert.assertEquals(1000, cashFlowList1.get(0).getBudgetAmt(), 0.0);
		Assert.assertEquals(3000, cashFlowList1.get(1).getActualAmt(), 0.0);

		verify(calcDao, times(1)).fetchCashFlowByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowSummaryByRefId() throws Exception {
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setMonthlyExpense(250000);
		when(calcDao.fetchCashFlowSummaryByRefId("P00000")).thenReturn(cashFlowSummary);
		CashFlowSummary cashFlowSummary1 = calcServiceImpl.fetchCashFlowSummaryByRefId("P00000");
		Assert.assertEquals(250000, cashFlowSummary1.getMonthlyExpense(), 0.0);
		verify(calcDao, times(1)).fetchCashFlowSummaryByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchNetworthByRefId() throws Exception {
		List<Networth> networthList = new ArrayList<Networth>();
		Networth cashFlow1 = new Networth();
		cashFlow1.setReferenceId("P00000");
		cashFlow1.setAccountEntryId(5);
		cashFlow1.setValue(5000);
		Networth cashFlow2 = new Networth();
		cashFlow2.setAccountEntryId(2);
		cashFlow2.setValue(60000);
		cashFlow2.setReferenceId("P00000");
		networthList.add(cashFlow1);
		networthList.add(cashFlow2);
		when(calcDao.fetchNetworthByRefId("P00000")).thenReturn(networthList);
		List<Networth> networthList1 = calcServiceImpl.fetchNetworthByRefId("P00000");
		Assert.assertEquals(2, networthList1.size());
		Assert.assertEquals(5, networthList1.get(0).getAccountEntryId());
		Assert.assertEquals(60000, networthList1.get(1).getValue(), 0.0);

		verify(calcDao, times(1)).fetchNetworthByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchNetworthSummaryByRefId() throws Exception {
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setCurrent_assetValue(100000);
		networthSummary.setCurrent_liability(500000);
		when(calcDao.fetchNetworthSummaryByRefId("P00000")).thenReturn(networthSummary);
		NetworthSummary networthSummary1 = calcServiceImpl.fetchNetworthSummaryByRefId("P00000");
		Assert.assertEquals(100000, networthSummary1.getCurrent_assetValue(), 0.0);
		verify(calcDao, times(1)).fetchNetworthSummaryByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchInsuranceByRefId() throws Exception {
		Insurance insurance1 = new Insurance();
		insurance1.setStability("Stable");
		insurance1.setAnnualIncome(1200000);
		insurance1.setReferenceId("P00000");
		when(calcDao.fetchInsuranceByRefId("P00000")).thenReturn(insurance1);
		Insurance insurance = calcServiceImpl.fetchInsuranceByRefId("P00000");
		Assert.assertEquals(1200000, insurance.getAnnualIncome(), 0.0);
		Assert.assertEquals("Stable", insurance.getStability());

		verify(calcDao, times(1)).fetchInsuranceByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskSummaryByRefId() throws Exception {
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setBehaviour("High Growth Investor");
		riskSummary.setReferenceId("P00000");
		when(calcDao.fetchRiskSummaryByRefId("P00000")).thenReturn(riskSummary);
		RiskSummary riskSummary1 = calcServiceImpl.fetchRiskSummaryByRefId("P00000");
		Assert.assertEquals("High Growth Investor", riskSummary1.getBehaviour());
		verify(calcDao, times(1)).fetchRiskSummaryByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addEmiCalculator() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00000");
		emiCalculator.setLoanAmount(500000);
		emiCalculator.setInterestRate(4);
		emiCalculator.setCreated(timestamp);
		emiCalculator.setUpdated(timestamp);
		emiCalculator.setCreated_by(signedUserId);
		emiCalculator.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addEmiCalculator(emiCalculator)).thenReturn(1);
		int result = calcServiceImpl.addEmiCalculator(emiCalculator);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addEmiCalculator(emiCalculator);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addEmiCapacity() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setReferenceId("P00000");
		emiCapacity.setCurrentAge(21);
		emiCapacity.setInterestRate(4);
		emiCapacity.setCreated(timestamp);
		emiCapacity.setUpdated(timestamp);
		emiCapacity.setCreated_by(signedUserId);
		emiCapacity.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addEmiCapacity(emiCapacity)).thenReturn(1);
		int result = calcServiceImpl.addEmiCapacity(emiCapacity);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addEmiCapacity(emiCapacity);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addPartialPayment() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		PartialPayment partialPayment = new PartialPayment();
		partialPayment.setReferenceId("P00000");
		partialPayment.setLoanAmount(800000);
		partialPayment.setInterestRate(4);
		partialPayment.setCreated(timestamp);
		partialPayment.setUpdated(timestamp);
		partialPayment.setCreated_by(signedUserId);
		partialPayment.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addPartialPayment(partialPayment)).thenReturn(1);
		int result = calcServiceImpl.addPartialPayment(partialPayment);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addPartialPayment(partialPayment);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addInterestChange() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");
		String signedUserId = party.getRoleBasedId();

		InterestChange interestChange = new InterestChange();
		interestChange.setReferenceId("P00000");
		interestChange.setLoanAmount(800000);
		interestChange.setInterestRate(4);
		interestChange.setLoanDate("10-05-2018");
		interestChange.setCreated(timestamp);
		interestChange.setUpdated(timestamp);
		interestChange.setCreated_by(signedUserId);
		interestChange.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addInterestChange(interestChange)).thenReturn(1);
		int result = calcServiceImpl.addInterestChange(interestChange);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addInterestChange(interestChange);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addEmiChange() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		EmiChange emiChange = new EmiChange();
		emiChange.setReferenceId("P00000");
		emiChange.setLoanAmount(800000);
		emiChange.setInterestRate(4);
		emiChange.setTenure(20);
		emiChange.setCreated(timestamp);
		emiChange.setUpdated(timestamp);
		emiChange.setCreated_by(signedUserId);
		emiChange.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addEmiChange(emiChange)).thenReturn(1);
		int result = calcServiceImpl.addEmiChange(emiChange);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addEmiChange(emiChange);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_addEmiInterestChange() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		EmiInterestChange emiIntChange = new EmiInterestChange();
		emiIntChange.setReferenceId("P00000");
		emiIntChange.setLoanAmount(800000);
		emiIntChange.setInterestRate(4);
		emiIntChange.setTenure(20);
		emiIntChange.setCreated(timestamp);
		emiIntChange.setUpdated(timestamp);
		emiIntChange.setCreated_by(signedUserId);
		emiIntChange.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addEmiInterestChange(emiIntChange)).thenReturn(1);
		int result = calcServiceImpl.addEmiInterestChange(emiIntChange);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addEmiInterestChange(emiIntChange);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchEmiCalculatorByRefId() throws Exception {
		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setLoanAmount(500000);
		emiCalculator.setReferenceId("P00000");
		when(calcDao.fetchEmiCalculatorByRefId("P00000")).thenReturn(emiCalculator);
		EmiCalculator emiCalculator1 = calcServiceImpl.fetchEmiCalculatorByRefId("P00000");
		Assert.assertEquals(500000, emiCalculator1.getLoanAmount(), 0.0);
		verify(calcDao, times(1)).fetchEmiCalculatorByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchEmiCapacityByRefId() throws Exception {
		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setRetirementAge(58);
		emiCapacity.setReferenceId("P00000");
		when(calcDao.fetchEmiCapacityByRefId("P00000")).thenReturn(emiCapacity);
		EmiCapacity emiCapacity1 = calcServiceImpl.fetchEmiCapacityByRefId("P00000");
		Assert.assertEquals(58, emiCapacity1.getRetirementAge());
		verify(calcDao, times(1)).fetchEmiCapacityByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPartialPaymentByRefId() throws Exception {
		List<PartialPayment> partialPaymentList = new ArrayList<PartialPayment>();
		PartialPayment partialPayment = new PartialPayment();
		partialPayment.setInterestRate(10);
		partialPayment.setReferenceId("P00000");
		partialPaymentList.add(partialPayment);
		when(calcDao.fetchPartialPaymentByRefId("P00000")).thenReturn(partialPaymentList);
		List<PartialPayment> partialPayment1 = calcServiceImpl.fetchPartialPaymentByRefId("P00000");
		Assert.assertEquals(10, partialPayment1.get(0).getInterestRate(), 0.0);
		verify(calcDao, times(1)).fetchPartialPaymentByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchInterestChangeByRefId() throws Exception {
		List<InterestChange> interestChangeList = new ArrayList<InterestChange>();
		InterestChange interestChange = new InterestChange();
		interestChange.setInterestRate(10);
		interestChange.setReferenceId("P00000");
		interestChangeList.add(interestChange);
		when(calcDao.fetchInterestChangeByRefId("P00000")).thenReturn(interestChangeList);
		List<InterestChange> interestChange1 = calcServiceImpl.fetchInterestChangeByRefId("P00000");
		Assert.assertEquals(10, interestChange1.get(0).getInterestRate(), 0.0);
		verify(calcDao, times(1)).fetchInterestChangeByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchEmiChangeByRefId() throws Exception {
		List<EmiChange> emiChangeList = new ArrayList<EmiChange>();
		EmiChange emiChange = new EmiChange();
		emiChange.setInterestRate(10);
		emiChange.setReferenceId("P00000");
		emiChangeList.add(emiChange);
		when(calcDao.fetchEmiChangeByRefId("P00000")).thenReturn(emiChangeList);
		List<EmiChange> emiChange1 = calcServiceImpl.fetchEmiChangeByRefId("P00000");
		Assert.assertEquals(10, emiChange1.get(0).getInterestRate(), 0.0);
		verify(calcDao, times(1)).fetchEmiChangeByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchEmiInterestChangeByRefId() throws Exception {
		List<EmiInterestChange> emiInterestChangeList = new ArrayList<EmiInterestChange>();
		EmiInterestChange emiInterestChange = new EmiInterestChange();
		emiInterestChange.setInterestRate(20);
		emiInterestChange.setReferenceId("P00000");
		emiInterestChangeList.add(emiInterestChange);
		when(calcDao.fetchEmiInterestChangeByRefId("P00000")).thenReturn(emiInterestChangeList);
		List<EmiInterestChange> emiInterestChange1 = calcServiceImpl.fetchEmiInterestChangeByRefId("P00000");
		Assert.assertEquals(20, emiInterestChange1.get(0).getInterestRate(), 0.0);
		verify(calcDao, times(1)).fetchEmiInterestChangeByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskProfileByRefIdAndAnswerId() throws Exception {
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setAnswerId(1);
		riskProfile.setReferenceId("P00000");
		when(calcDao.fetchRiskProfileByRefIdAndAnswerId("P00000", 1)).thenReturn(riskProfile);
		RiskProfile riskProfile1 = calcServiceImpl.fetchRiskProfileByRefIdAndAnswerId("P00000", 1);
		Assert.assertEquals(1, riskProfile1.getAnswerId());
		verify(calcDao, times(1)).fetchRiskProfileByRefIdAndAnswerId("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchNetworthByRefIdAndEntryId() throws Exception {
		Networth networth = new Networth();
		networth.setValue(50000);
		networth.setReferenceId("P00000");
		when(calcDao.fetchNetworthByRefIdAndEntryId("P00000", 2)).thenReturn(networth);
		Networth networth1 = calcServiceImpl.fetchNetworthByRefIdAndEntryId("P00000", 2);
		Assert.assertEquals(50000, networth1.getValue(), 0.0);
		verify(calcDao, times(1)).fetchNetworthByRefIdAndEntryId("P00000", 2);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchAccountTypeByTypeId() throws Exception {
		AccountType accountType = new AccountType();
		accountType.setAccountType("Assets");
		accountType.setAccountTypeId(1);
		when(calcDao.fetchAccountTypeByTypeId(1)).thenReturn(accountType);
		AccountType accountType1 = calcServiceImpl.fetchAccountTypeByTypeId(1);
		Assert.assertEquals("Assets", accountType1.getAccountType());
		verify(calcDao, times(1)).fetchAccountTypeByTypeId(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowByRefIdAndItemId() throws Exception {
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setBudgetAmt(5000);
		cashFlow.setActualAmt(7000);
		when(calcDao.fetchCashFlowByRefIdAndItemId("P00000", 3)).thenReturn(cashFlow);
		CashFlow cashFlow1 = calcServiceImpl.fetchCashFlowByRefIdAndItemId("P00000", 3);
		Assert.assertEquals(5000, cashFlow1.getBudgetAmt(), 0.0);
		verify(calcDao, times(1)).fetchCashFlowByRefIdAndItemId("P00000", 3);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchCashFlowItemTypeByTypeId() throws Exception {
		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemType("Mandatory Household Expense");
		cashFlowItemType.setCashFlowItemTypeId(1);
		when(calcDao.fetchCashFlowItemTypeByTypeId(3)).thenReturn(cashFlowItemType);
		CashFlowItemType cashFlowItemType1 = calcServiceImpl.fetchCashFlowItemTypeByTypeId(3);
		Assert.assertEquals("Mandatory Household Expense", cashFlowItemType1.getCashFlowItemType());
		verify(calcDao, times(1)).fetchCashFlowItemTypeByTypeId(3);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchInsuranceItemByRefId() throws Exception {
		Insurance insurance = new Insurance();
		insurance.setReferenceId("P00000");
		insurance.setInsuranceId(1);
		insurance.setAdditionalInsurance(5000000);
		when(calcDao.fetchInsuranceByRefId("P00000")).thenReturn(insurance);
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getAnnualIncome())).thenReturn("annualIncome");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getStability())).thenReturn("stability");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getPredictability())).thenReturn("predictability");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getRequiredInsurance())).thenReturn("requiredInsurance");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getExistingInsurance())).thenReturn("existingInsurance");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getAdditionalInsurance()))
				.thenReturn("additionalInsurance");
		InsuranceItem insuranceItem = calcServiceImpl.fetchInsuranceItemByRefId("P00000");
		Assert.assertEquals("P00000", insuranceItem.getReferenceId());
		Assert.assertEquals(1, insuranceItem.getInsuranceId());
		verify(calcDao, times(1)).fetchInsuranceByRefId("P00000");
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getAnnualIncome());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getStability());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getPredictability());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getRequiredInsurance());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getExistingInsurance());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getAdditionalInsurance());
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchInsuranceItemByRefId_NotFound() throws Exception {
		// Insurance insurance = new Insurance();
		// insurance.setPartyId(1);
		// insurance.setInsuranceId(1);
		// insurance.setAdditionalInsurance(5000000);
		// List<Insurance> insuranceList = new ArrayList<>();
		// insuranceList.add(insurance);
		when(calcDao.fetchInsuranceByRefId("P00000")).thenReturn(null);
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getAnnualIncome())).thenReturn("annualIncome");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getStability())).thenReturn("stability");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getPredictability())).thenReturn("predictability");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getRequiredInsurance())).thenReturn("requiredInsurance");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getExistingInsurance())).thenReturn("existingInsurance");
		when(calcDao.fetchValueByInsuranceItem(calcTableFields.getAdditionalInsurance()))
				.thenReturn("additionalInsurance");
		InsuranceItem insuranceItem = calcServiceImpl.fetchInsuranceItemByRefId("P00000");
		Assert.assertEquals("P00000", insuranceItem.getReferenceId());
		Assert.assertEquals(0, insuranceItem.getInsuranceId());
		verify(calcDao, times(1)).fetchInsuranceByRefId("P00000");
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getAnnualIncome());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getStability());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getPredictability());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getRequiredInsurance());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getExistingInsurance());
		verify(calcDao, times(6)).fetchValueByInsuranceItem(calcTableFields.getAdditionalInsurance());
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addPlanInfo() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		// party.setUserName("adv");

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		String signedUserId = party.getRoleBasedId();
		Plan plan = new Plan();
		plan.setPartyId(1);
		plan.setCreated(timestamp);
		plan.setUpdated(timestamp);
		plan.setCreated_by(signedUserId);
		plan.setUpdated_by(signedUserId);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addPlanInfo(plan, encryptPass)).thenReturn(1);
		int result = calcServiceImpl.addPlanInfo(plan);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addPlanInfo(plan, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRoleBasedIdByPartyId() throws Exception {
		when(calcDao.fetchRoleBasedIdByPartyId(1)).thenReturn("ADV000000000A");
		String result = calcServiceImpl.fetchRoleBasedIdByPartyId(1);
		Assert.assertEquals(result, "ADV000000000A");
		verify(calcDao, times(1)).fetchRoleBasedIdByPartyId(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPlanByReferenceId() throws Exception {
		Plan plan1 = new Plan();
		plan1.setPartyId(1);
		String encryptPass = advTableFields.getEncryption_password();
		when(calcDao.fetchPlanByReferenceId("ADV000000000A", encryptPass)).thenReturn(plan1);
		Plan plan = calcServiceImpl.fetchPlanByReferenceId("ADV000000000A");
		Assert.assertEquals(plan.getPartyId(), 1);
		verify(calcDao, times(1)).fetchPlanByReferenceId("ADV000000000A", encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateCashFlow() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setBudgetAmt(1000);
		// cashFlow.setCreated(timestamp);
		cashFlow.setUpdated(timestamp);
		// cashFlow.setCreated_by(signedUserId);
		cashFlow.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcDao.updateCashFlow(cashFlow)).thenReturn(1);
		int result = calcServiceImpl.updateCashFlow(cashFlow);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateCashFlow(cashFlow);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeCashFlowSummary() throws Exception {
		when(calcDao.removeCashFlowSummary("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeCashFlowSummary("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeCashFlowSummary("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateNetworth() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setUpdated(timestamp);
		networth.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateNetworth(networth)).thenReturn(1);
		int result = calcServiceImpl.updateNetworth(networth);
		Assert.assertEquals(result, 1);

		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateNetworth(networth);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeNetworthSummary() throws Exception {
		when(calcDao.removeNetworthSummary("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeNetworthSummary("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeNetworthSummary("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updatePriority() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUpdated(timestamp);
		priority.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updatePriority(priority)).thenReturn(1);
		int result = calcServiceImpl.updatePriority(priority);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).updatePriority(priority);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateInsurance() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		Insurance insurance = new Insurance();
		insurance.setReferenceId("P00000");
		// insurance.setCreated(timestamp);
		insurance.setUpdated(timestamp);
		// insurance.setCreated_by(signedUserId);
		insurance.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateInsurance(insurance)).thenReturn(1);
		int result = calcServiceImpl.updateInsurance(insurance);
		Assert.assertEquals(result, 1);

		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateInsurance(insurance);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_updateRiskProfile() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(1);
		riskProfile.setUpdated(timestamp);
		riskProfile.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchScoreByAnswerId(riskProfile)).thenReturn(5);
		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateRiskProfile(riskProfile)).thenReturn(1);
		int result = calcServiceImpl.updateRiskProfile(riskProfile);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchScoreByAnswerId(riskProfile);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateRiskProfile(riskProfile);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeRiskSummary() throws Exception {
		when(calcDao.removeRiskSummary("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeRiskSummary("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeRiskSummary("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_updateEmiCalculator() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00000");
		emiCalculator.setLoanAmount(500000);
		emiCalculator.setInterestRate(4);
		emiCalculator.setUpdated(timestamp);
		emiCalculator.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateEmiCalculator(emiCalculator)).thenReturn(1);
		int result = calcServiceImpl.updateEmiCalculator(emiCalculator);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateEmiCalculator(emiCalculator);
		verifyNoMoreInteractions(calcDao);

	}

	@Test // test//
	public void test_updateEmiCapacity() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setReferenceId("P00000");
		emiCapacity.setUpdated(timestamp);
		emiCapacity.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateEmiCapacity(emiCapacity)).thenReturn(1);
		int result = calcServiceImpl.updateEmiCapacity(emiCapacity);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateEmiCapacity(emiCapacity);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskProfileByRefIdAndQuestionId() throws Exception {
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(10);
		when(calcDao.fetchRiskProfileByRefIdAndQuestionId("P00000", "1")).thenReturn(riskProfile);
		RiskProfile result = calcServiceImpl.fetchRiskProfileByRefIdAndQuestionId("P00000", "1");
		Assert.assertEquals(result.getAnswerId(), 10);
		verify(calcDao, times(1)).fetchRiskProfileByRefIdAndQuestionId("P00000", "1");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchQuestionIdFromRiskQuestionaire() throws Exception {
		List<String> questionIds = new ArrayList<>();
		questionIds.add("1");
		when(calcDao.fetchQuestionIdFromRiskQuestionaire()).thenReturn(questionIds);
		List<String> result = calcServiceImpl.fetchQuestionIdFromRiskQuestionaire();
		verify(calcDao, times(1)).fetchQuestionIdFromRiskQuestionaire();
		Assert.assertEquals(result.get(0), "1");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchRiskQuestionaireByQuestionId() throws Exception {

		List<RiskQuestionaire> riskQuestionaireList = new ArrayList<>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setAnswer("answer");
		riskQuestionaireList.add(riskQuestionaire);
		when(calcDao.fetchRiskQuestionaireByQuestionId("1")).thenReturn(riskQuestionaireList);
		List<RiskQuestionaire> result = calcServiceImpl.fetchRiskQuestionaireByQuestionId("1");
		Assert.assertEquals(result.get(0).getAnswer(), "answer");
		verify(calcDao, times(1)).fetchRiskQuestionaireByQuestionId("1");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchQuestionByQuestionId() throws Exception {
		when(calcDao.fetchQuestionByQuestionId("1")).thenReturn("question");
		String result = calcServiceImpl.fetchQuestionByQuestionId("1");
		Assert.assertEquals(result, "question");
		verify(calcDao, times(1)).fetchQuestionByQuestionId("1");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchGoalByRefIdAndGoalName() throws Exception {
		Goal goal = new Goal();
		goal.setGoalId(1);
		goal.setGoalName("travel");
		when(calcDao.fetchGoalByRefIdAndGoalName("P00000", "travel")).thenReturn(goal);
		Goal result = calcServiceImpl.fetchGoalByRefIdAndGoalName("P00000", "travel");
		Assert.assertEquals(result.getGoalName(), "travel");
		verify(calcDao, times(1)).fetchGoalByRefIdAndGoalName("P00000", "travel");
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_updateGoalInfo() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		Goal goal = new Goal();
		goal.setGoalId(1);
		goal.setGoalName("travel");
		goal.setUpdated(timestamp);
		goal.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateGoalInfo(goal)).thenReturn(1);
		int result = calcServiceImpl.updateGoalInfo(goal);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateGoalInfo(goal);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removePartialPaymentByRefId() throws Exception {
		when(calcDao.removePartialPaymentByRefId("P00000")).thenReturn(1);
		int result = calcServiceImpl.removePartialPaymentByRefId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removePartialPaymentByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeEmiChangeByRefId() throws Exception {
		when(calcDao.removeEmiChangeByRefId("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeEmiChangeByRefId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeEmiChangeByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeInterestChangeByRefId() throws Exception {
		when(calcDao.removeInterestChangeByRefId("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeInterestChangeByRefId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeInterestChangeByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeEmiInterestChangeByRefId() throws Exception {
		when(calcDao.removeEmiInterestChangeByRefId("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeEmiInterestChangeByRefId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeEmiInterestChangeByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateCashFlowSummary() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setUpdated(timestamp);
		cashFlowSummary.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateCashFlowSummary(cashFlowSummary)).thenReturn(1);
		int result = calcServiceImpl.updateCashFlowSummary(cashFlowSummary);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateCashFlowSummary(cashFlowSummary);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateNetworthSummary() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setUpdated(timestamp);
		networthSummary.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateNetworthSummary(networthSummary)).thenReturn(1);
		int result = calcServiceImpl.updateNetworthSummary(networthSummary);
		Assert.assertEquals(result, 1);

		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateNetworthSummary(networthSummary);
		verifyNoMoreInteractions(calcDao);
	}

	@Test // test//
	public void test_updateRiskSummary() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");

		String signedUserId = party.getRoleBasedId();

		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setUpdated(timestamp);
		riskSummary.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.updateRiskSummary(riskSummary)).thenReturn(1);
		int result = calcServiceImpl.updateRiskSummary(riskSummary);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateRiskSummary(riskSummary);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removeInsuranceByRefId() throws Exception {
		when(calcDao.removeInsuranceByRefId("P00000")).thenReturn(1);
		int result = calcServiceImpl.removeInsuranceByRefId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removeInsuranceByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchPriorityItemByItemId() throws Exception {
		PriorityItem priorityItem = new PriorityItem();
		priorityItem.setPriorityItem("Investing in Stocks");
		priorityItem.setPriorityItemId(1);
		when(calcDao.fetchPriorityItemByItemId(1)).thenReturn(priorityItem);
		PriorityItem result = calcServiceImpl.fetchPriorityItemByItemId(1);
		Assert.assertEquals(result.getPriorityItem(), "Investing in Stocks");
		verify(calcDao, times(1)).fetchPriorityItemByItemId(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_removePriorityByRefIdAndItemId() throws Exception {
		when(calcDao.removePriorityByRefIdAndItemId("P00000", 1)).thenReturn(1);
		int result = calcServiceImpl.removePriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).removePriorityByRefIdAndItemId("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	// generatePlanReferenceId
	// referenceIdIncrement

	@Test
	public void test_fetchPlanByPartyId() throws Exception {
		List<Plan> planList = new ArrayList<Plan>();
		Plan plan = new Plan();
		plan.setAge(24);
		plan.setReferenceId("P00000");
		plan.setPartyId(1);
		plan.setPlanId(1);
		planList.add(plan);
		String encryptPass = advTableFields.getEncryption_password();
		when(calcDao.fetchPlanByPartyId(1, encryptPass)).thenReturn(planList);
		List<Plan> plan1 = calcServiceImpl.fetchPlanByPartyId(1);
		Assert.assertEquals(24, plan1.get(0).getAge());
		verify(calcDao, times(1)).fetchPlanByPartyId(1, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_modifyPlanInfo() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		Plan plan = new Plan();
		plan.setAge(24);
		plan.setReferenceId("P00000");
		plan.setPartyId(1);
		plan.setPlanId(1);
		plan.setUpdated(timestamp);
		plan.setUpdated_by(signedUserId);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.modifyPlanInfo(plan, "P00000", encryptPass)).thenReturn(1);
		int result = calcServiceImpl.modifyPlanInfo(plan, "P00000");
		Assert.assertEquals(result, 1);

		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).modifyPlanInfo(plan, "P00000", encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	// @Test
	// public void test_removePlanInfo() throws Exception {
	// calcDao.removeCashFlow("P0000000000");
	// calcDao.removeCashFlowSummary("P0000000000");
	// calcDao.removeNetworth("P0000000000");
	// calcDao.removeNetworthSummary("P0000000000");
	// calcDao.removePriority("P0000000000");
	// calcDao.removeInsuranceByRefId("P0000000000");
	// calcDao.removeEmiCalculator("P0000000000");
	// calcDao.removeEmiCapacity("P0000000000");
	// calcDao.removePartialPaymentByRefId("P0000000000");
	// calcDao.removeEmiChangeByRefId("P0000000000");
	// calcDao.removeInterestChangeByRefId("P0000000000");
	// calcDao.removeEmiInterestChangeByRefId("P0000000000");
	// calcDao.removeGoal("P0000000000");
	// calcDao.removeRiskProfile("P0000000000");
	// calcDao.removeRiskSummary("P0000000000");
	// calcDao.removeFutureValue("P0000000000");
	// calcDao.removeTargetValue("P0000000000");
	// calcDao.removeRateFinder("P0000000000");
	// calcDao.removeTenureFinder("P0000000000");
	// when(calcDao.removePlanInfo("P0000000000")).thenReturn(1);
	//
	// int result = calcServiceImpl.removePlanInfo("P0000000000");
	// Assert.assertEquals(result, 1);
	// verify(calcDao, times(1)).removePlanInfo("P0000000000");
	// verifyNoMoreInteractions(calcDao);
	// }

	// @Test
	// public void test_fetchPlanBySuperParentId() throws Exception {
	// List<Plan> planList = new ArrayList<Plan>();
	// Plan plan = new Plan();
	// plan.setAge(24);
	// plan.setReferenceId("P00000");
	// plan.setPartyId(1);
	// plan.setPlanId(1);
	// planList.add(plan);
	// String encryptPass = advTableFields.getEncryption_password();
	// when(calcDao.fetchPlanBySuperParentId(1, encryptPass)).thenReturn(planList);
	// List<Plan> plan1 = calcServiceImpl.fetchPlanBySuperParentId(1);
	// Assert.assertEquals(24, plan1.get(0).getAge());
	// verify(calcDao, times(1)).fetchPlanBySuperParentId(1, encryptPass);
	// verifyNoMoreInteractions(calcDao);
	// }

	@Test
	public void test_fetchEmailIdByPartyId() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(calcDao.fetchEmailIdByPartyId(1, encryptPass)).thenReturn("abc@gmail.com");
		String result = calcServiceImpl.fetchEmailIdByPartyId(1);
		Assert.assertEquals(result, "abc@gmail.com");
		verify(calcDao, times(1)).fetchEmailIdByPartyId(1, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkPlanIsPresentByReferenceId() throws Exception {
		when(calcDao.checkPlanIsPresentByReferenceId("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkPlanIsPresentByReferenceId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkPlanIsPresentByReferenceId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkGoalIsPresentByRefIdAndGoalName() throws Exception {
		when(calcDao.checkGoalIsPresentByRefIdAndGoalName("P00000", "Education")).thenReturn(1);
		int result = calcServiceImpl.checkGoalIsPresentByRefIdAndGoalName("P00000", "Education");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkGoalIsPresentByRefIdAndGoalName("P00000", "Education");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkCashFlowIsPresent() throws Exception {
		when(calcDao.checkCashFlowIsPresent("P00000", 1)).thenReturn(1);
		int result = calcServiceImpl.checkCashFlowIsPresent("P00000", 1);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkCashFlowIsPresent("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkCashFlowSummaryIsPresent() throws Exception {
		when(calcDao.checkCashFlowSummaryIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkCashFlowSummaryIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkCashFlowSummaryIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkNetworthIsPresent() throws Exception {
		when(calcDao.checkNetworthIsPresent("P00000", 1)).thenReturn(1);
		int result = calcServiceImpl.checkNetworthIsPresent("P00000", 1);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkNetworthIsPresent("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkNetworthSummaryIsPresent() throws Exception {
		when(calcDao.checkNetworthSummaryIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkNetworthSummaryIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkNetworthSummaryIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkPriorityByRefIdAndItemId() throws Exception {
		when(calcDao.checkPriorityByRefIdAndItemId("P00000", 1)).thenReturn(1);
		int result = calcServiceImpl.checkPriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkPriorityByRefIdAndItemId("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkInsuranceByRefId() throws Exception {
		when(calcDao.checkInsuranceByRefId("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkInsuranceByRefId("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkInsuranceByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkRiskProfileIsPresent() throws Exception {
		when(calcDao.checkRiskProfileIsPresent("P00000", "1")).thenReturn(1);
		int result = calcServiceImpl.checkRiskProfileIsPresent("P00000", "1");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkRiskProfileIsPresent("P00000", "1");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkRiskSummaryIsPresent() throws Exception {
		when(calcDao.checkRiskSummaryIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkRiskSummaryIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkRiskSummaryIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkEmiCalculatorIsPresent() throws Exception {
		when(calcDao.checkEmiCalculatorIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkEmiCalculatorIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkEmiCalculatorIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkemiCapacityIsPresent() throws Exception {
		when(calcDao.checkemiCapacityIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkemiCapacityIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkemiCapacityIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkpartialIsPresent() throws Exception {
		when(calcDao.checkpartialIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkpartialIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkpartialIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkEmiChangeIsPresent() throws Exception {
		when(calcDao.checkEmiChangeIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkEmiChangeIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkEmiChangeIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkInterestChangeIsPresent() throws Exception {
		when(calcDao.checkInterestChangeIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkInterestChangeIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkInterestChangeIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkPlanIsPresentByReferenceId_Error() throws Exception {
		when(calcDao.checkPlanIsPresentByReferenceId("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkPlanIsPresentByReferenceId("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkPlanIsPresentByReferenceId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkGoalIsPresentByRefIdAndGoalName_Error() throws Exception {
		when(calcDao.checkGoalIsPresentByRefIdAndGoalName("P00000", "Education")).thenReturn(0);
		int result = calcServiceImpl.checkGoalIsPresentByRefIdAndGoalName("P00000", "Education");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkGoalIsPresentByRefIdAndGoalName("P00000", "Education");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkCashFlowIsPresent_Error() throws Exception {
		when(calcDao.checkCashFlowIsPresent("P00000", 1)).thenReturn(0);
		int result = calcServiceImpl.checkCashFlowIsPresent("P00000", 1);
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkCashFlowIsPresent("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkCashFlowSummaryIsPresent_Error() throws Exception {
		when(calcDao.checkCashFlowSummaryIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkCashFlowSummaryIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkCashFlowSummaryIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkNetworthIsPresent_Error() throws Exception {
		when(calcDao.checkNetworthIsPresent("P00000", 1)).thenReturn(0);
		int result = calcServiceImpl.checkNetworthIsPresent("P00000", 1);
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkNetworthIsPresent("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkNetworthSummaryIsPresent_Error() throws Exception {
		when(calcDao.checkNetworthSummaryIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkNetworthSummaryIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkNetworthSummaryIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkPriorityByRefIdAndItemId_Error() throws Exception {
		when(calcDao.checkPriorityByRefIdAndItemId("P00000", 1)).thenReturn(0);
		int result = calcServiceImpl.checkPriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkPriorityByRefIdAndItemId("P00000", 1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkInsuranceByRefId_Error() throws Exception {
		when(calcDao.checkInsuranceByRefId("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkInsuranceByRefId("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkInsuranceByRefId("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkRiskProfileIsPresent_Error() throws Exception {
		when(calcDao.checkRiskProfileIsPresent("P00000", "1")).thenReturn(0);
		int result = calcServiceImpl.checkRiskProfileIsPresent("P00000", "1");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkRiskProfileIsPresent("P00000", "1");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkRiskSummaryIsPresent_Error() throws Exception {
		when(calcDao.checkRiskSummaryIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkRiskSummaryIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkRiskSummaryIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkEmiCalculatorIsPresent_Error() throws Exception {
		when(calcDao.checkEmiCalculatorIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkEmiCalculatorIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkEmiCalculatorIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkemiCapacityIsPresent_Error() throws Exception {
		when(calcDao.checkemiCapacityIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkemiCapacityIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkemiCapacityIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkpartialIsPresent_Error() throws Exception {
		when(calcDao.checkpartialIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkpartialIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkpartialIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkEmiChangeIsPresent_Error() throws Exception {
		when(calcDao.checkEmiChangeIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkEmiChangeIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkEmiChangeIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkInterestChangeIsPresent_Error() throws Exception {
		when(calcDao.checkInterestChangeIsPresent("P00000")).thenReturn(0);
		int result = calcServiceImpl.checkInterestChangeIsPresent("P00000");
		Assert.assertEquals(result, 0);
		verify(calcDao, times(1)).checkInterestChangeIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	private long getSignedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		if (userDetails.getUsername().equals("admin@sowisetech.com")) {
			return 0;
		} else {
			Party party = calcDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
			return party.getPartyId();
		}
	}

	// @Test //nullPointer exception
	// public void test_createQuery() throws Exception {
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setUserName("Adv1");
	// party.setRoleBasedId("ADM0000000001");
	// List<CalcQuery> calcQueryList = new ArrayList<>();
	// CalcQuery query = new CalcQuery();
	// query.setName(party.getUserName());
	// query.setQueryId(1);
	// query.setPartyId(1);
	// calcQueryList.add(query);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// String encryptPass = advTableFields.getEncryption_password();
	// Authentication authentication = Mockito.mock(Authentication.class);
	// // Mockito.whens() for your authorization object
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	// when(calcDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	// when(calcDao.fetchParty(1, deleteflag, encryptPass)).thenReturn(party);
	// when(calcDao.createQuery(query)).thenReturn(1);
	// int result = calcServiceImpl.createQuery(calcQueryList);
	// Assert.assertEquals(1, result);
	// verify(calcDao, times(1)).fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass);
	// verify(calcDao, times(1)).fetchParty(1, deleteflag, encryptPass);
	// verify(calcDao, times(1)).createQuery(query);
	// verifyNoMoreInteractions(calcDao);
	// }

	@Test
	public void test_createAnswer() throws Exception {
		CalcAnswer calcAnswer = new CalcAnswer();
		calcAnswer.setAnswer("answer");
		calcAnswer.setAnswerId(1);
		calcAnswer.setPartyId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(calcDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(calcDao.createAnswer(calcAnswer)).thenReturn(1);
		int result = calcServiceImpl.createAnswer(calcAnswer);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(calcDao, times(1)).createAnswer(calcAnswer);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addPriorities() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		List<Priority> priorityList = new ArrayList<>();
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setCreated(timestamp);
		priority.setUpdated(timestamp);
		priority.setCreated_by(signedUserId);
		priority.setUpdated_by(signedUserId);
		priorityList.add(priority);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcDao.addPriorities(priority)).thenReturn(1);
		int result = calcServiceImpl.addPriorities(priority);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);

		verify(calcDao, times(1)).addPriorities(priority);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkSharedAdvisor_Success() throws Exception {
		String delete_flag_N = calcTableFields.getDelete_flag_N();
		when(calcDao.checkSharedAdvisor("P0000000000", 1L, delete_flag_N)).thenReturn(1);
		int result = calcServiceImpl.checkSharedAdvisor("P0000000000", 1L);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).checkSharedAdvisor("P0000000000", 1L, delete_flag_N);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkSharedAdvisor_Error() throws Exception {
		String delete_flag = calcTableFields.getDelete_flag_N();
		when(calcDao.checkSharedAdvisor("P0000000000", 1L, delete_flag)).thenReturn(0);
		int result = calcServiceImpl.checkSharedAdvisor("P0000000000", 1L);
		Assert.assertEquals(0, result);
		verify(calcDao, times(1)).checkSharedAdvisor("P0000000000", 1L, delete_flag);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkPartyIsPresent_Success() throws Exception {
		when(calcDao.checkPartyIsPresent(1)).thenReturn(1);
		int result = calcServiceImpl.checkPartyIsPresent(1);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).checkPartyIsPresent(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkPartyIsPresent_Error() throws Exception {
		when(calcDao.checkPartyIsPresent(1)).thenReturn(0);
		int result = calcServiceImpl.checkPartyIsPresent(1);
		Assert.assertEquals(0, result);
		verify(calcDao, times(1)).checkPartyIsPresent(1);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkForumSubCategoryIsPresent_Success() throws Exception {
		when(calcDao.checkForumSubCategoryIsPresent(1L)).thenReturn(1);
		int result = calcServiceImpl.checkForumSubCategoryIsPresent(1L);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).checkForumSubCategoryIsPresent(1L);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkForumSubCategoryIsPresent_Error() throws Exception {
		when(calcDao.checkForumSubCategoryIsPresent(1L)).thenReturn(0);
		int result = calcServiceImpl.checkForumSubCategoryIsPresent(1L);
		Assert.assertEquals(0, result);
		verify(calcDao, times(1)).checkForumSubCategoryIsPresent(1L);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_generatePlanReferenceId_Success() throws Exception {
		when(calcDao.fetchPlanReferenceId()).thenReturn("P0000000001");
		when(calcDao.addPlanReferenceId("P0000000002")).thenReturn(1);
		String result = calcServiceImpl.generatePlanReferenceId();
		Assert.assertEquals("P0000000002", result);
		verify(calcDao, times(1)).fetchPlanReferenceId();
		verify(calcDao, times(1)).addPlanReferenceId("P0000000002");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_generatePlanReferenceId_Error() throws Exception {
		when(calcDao.fetchPlanReferenceId()).thenReturn("P0000000001");
		when(calcDao.addPlanReferenceId("P0000000002")).thenReturn(0);
		String result = calcServiceImpl.generatePlanReferenceId();
		Assert.assertEquals(null, result);
		verify(calcDao, times(1)).fetchPlanReferenceId();
		verify(calcDao, times(1)).addPlanReferenceId("P0000000002");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_generatePlanReferenceId_NullError() throws Exception {
		when(calcDao.fetchPlanReferenceId()).thenReturn(null);
		when(calcDao.addPlanReferenceId("P0000000000")).thenReturn(1);
		String result = calcServiceImpl.generatePlanReferenceId();
		Assert.assertEquals("P0000000000", result);
		verify(calcDao, times(1)).fetchPlanReferenceId();
		verify(calcDao, times(1)).addPlanReferenceId("P0000000000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkFutureValueIsPresent() throws Exception {
		when(calcDao.checkFutureValueIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkFutureValueIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkFutureValueIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addFutureValue() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		FutureValue futureValue = new FutureValue();
		futureValue.setReferenceId("P00000");
		futureValue.setCreated(timestamp);
		futureValue.setUpdated(timestamp);
		futureValue.setCreated_by(signedUserId);
		futureValue.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addFutureValue(futureValue)).thenReturn(1);
		int result = calcServiceImpl.addFutureValue(futureValue);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addFutureValue(futureValue);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateFutureValue() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		FutureValue futureValue = new FutureValue();
		futureValue.setReferenceId("P00000");
		// futureValue.setCreated(signedUserId);
		futureValue.setUpdated(timestamp);
		// futureValue.setCreated_by(signedUserId);
		futureValue.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcDao.updateFutureValue(futureValue)).thenReturn(1);
		int result = calcServiceImpl.updateFutureValue(futureValue);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateFutureValue(futureValue);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkTargetValueIsPresent() throws Exception {
		when(calcDao.checkTargetValueIsPresent("P00000")).thenReturn(1);
		int result = calcServiceImpl.checkTargetValueIsPresent("P00000");
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).checkTargetValueIsPresent("P00000");
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_addTargetValue() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		TargetValue targetValue = new TargetValue();
		targetValue.setReferenceId("P00000");
		targetValue.setCreated(timestamp);
		targetValue.setUpdated(timestamp);
		targetValue.setCreated_by(signedUserId);
		targetValue.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(calcDao.addTargetValue(targetValue)).thenReturn(1);
		int result = calcServiceImpl.addTargetValue(targetValue);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).addTargetValue(targetValue);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_updateTargetValue() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		String signedUserId = party.getRoleBasedId();
		TargetValue targetValue = new TargetValue();
		targetValue.setReferenceId("P00000");
		// targetValue.setCreated(signedUserId);
		targetValue.setUpdated(timestamp);
		// targetValue.setCreated_by(signedUserId);
		targetValue.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(calcDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(calcDao.updateTargetValue(targetValue)).thenReturn(1);
		int result = calcServiceImpl.updateTargetValue(targetValue);
		Assert.assertEquals(result, 1);
		verify(calcDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(calcDao, times(1)).updateTargetValue(targetValue);
		verifyNoMoreInteractions(calcDao);
	}

	// @Test //if condition//
	// public void test_createCommentQueries() throws Exception {
	// Queries queries = new Queries();
	// Party party = new Party();
	// party.setPartyId(1);
	// party.setRoleBasedId("ADM0000000001");
	// String deleteflag = advTableFields.getDelete_flag_N();
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// // Mockito.whens() for your authorization object
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	// when(calcDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	//
	// when(calcDao.createCommentQueries(queries)).thenReturn(1);
	// when(calcDao.modifyCalcQueryAfterComment(1L,"ADM0000000001")).thenReturn(1);
	//
	// int result = calcServiceImpl.createCommentQueries(queries);
	// Assert.assertEquals(1, result);
	// verify(calcDao, times(1)).fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass);
	// verify(calcDao, times(1)).createCommentQueries(queries);
	// verify(calcDao, times(1)).modifyCalcQueryAfterComment(1L, "ADM0000000001");
	// verifyNoMoreInteractions(calcDao);
	// }

	@Test
	public void test_fetchQueries() throws Exception {
		List<Queries> queriesList = new ArrayList<Queries>();
		Queries queries = new Queries();
		queries.setSenderId(1);
		queries.setReceiverId(1);
		queriesList.add(queries);
		// Queries queries2 = new Queries();
		// queries2.setSenderId(1);
		// queries2.setReceiverId(1);
		// queriesList.add(queries2);
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = calcTableFields.getDelete_flag_N();
		when(calcDao.fetchQueries(1, 1, "N", delete_flag, encryptPass)).thenReturn(queriesList);
		List<Queries> queries1 = calcServiceImpl.fetchQueries(1, 1, "N");
		Assert.assertEquals(1, queries1.size());
		verify(calcDao, times(1)).fetchQueries(1, 1, "N", delete_flag, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_checkQueriesIsPresent() throws Exception {
		String deleteFlag = calcTableFields.getDelete_flag_N();
		when(calcDao.checkQueriesIsPresent(1, deleteFlag)).thenReturn(1);
		int result = calcServiceImpl.checkQueriesIsPresent(1);
		Assert.assertEquals(1, result);
		verify(calcDao, times(1)).checkQueriesIsPresent(1, deleteFlag);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchSharedPlanByPartyId() throws Exception {
		String deleteFlag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		List<CalcQuery> calcQueryList = new ArrayList<CalcQuery>();
		CalcQuery calcQuery1 = new CalcQuery();
		calcQuery1.setReferenceId("P00000");
		calcQuery1.setPartyId(1);
		CalcQuery calcQuery2 = new CalcQuery();
		calcQuery2.setReferenceId("P00000");
		calcQuery2.setPartyId(1);
		calcQueryList.add(calcQuery1);
		calcQueryList.add(calcQuery2);
		when(calcDao.fetchSharedPlanByPartyId(1, "P00000", deleteFlag, encryptPass)).thenReturn(calcQueryList);
		List<CalcQuery> calcQueryList1 = calcServiceImpl.fetchSharedPlanByPartyId(1, "P00000");
		Assert.assertEquals(2, calcQueryList1.size());
		verify(calcDao, times(1)).fetchSharedPlanByPartyId(1, "P00000", deleteFlag, encryptPass);
		verifyNoMoreInteractions(calcDao);
	}

	@Test
	public void test_fetchSharedPlanByPostedPartyId() throws Exception {
		String deleteFlag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		List<CalcQuery> calcQueryList = new ArrayList<CalcQuery>();
		CalcQuery calcQuery1 = new CalcQuery();
		calcQuery1.setPartyId(1);
		calcQuery1.setDelete_flag(deleteFlag);

		CalcQuery calcQuery2 = new CalcQuery();
		calcQuery2.setPartyId(1);
		calcQueryList.add(calcQuery1);
		calcQueryList.add(calcQuery2);
		when(calcDao.fetchSharedPlanByPostedPartyId(1, encryptPass, deleteFlag)).thenReturn(calcQueryList);
		List<CalcQuery> calcQueryList1 = calcServiceImpl.fetchSharedPlanByPostedPartyId(1);
		Assert.assertEquals(2, calcQueryList1.size());
		verify(calcDao, times(1)).fetchSharedPlanByPostedPartyId(1, encryptPass, deleteFlag);
		verifyNoMoreInteractions(calcDao);
	}
}
