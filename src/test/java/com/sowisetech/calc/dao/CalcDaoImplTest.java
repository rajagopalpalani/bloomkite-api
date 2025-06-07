package com.sowisetech.calc.dao;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.model.Account;
import com.sowisetech.calc.model.AccountType;
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
import com.sowisetech.calc.model.InterestChange;
import com.sowisetech.calc.model.Networth;
import com.sowisetech.calc.model.NetworthSummary;
import com.sowisetech.calc.model.PartialPayment;
import com.sowisetech.calc.model.Party;
import com.sowisetech.calc.model.Plan;
import com.sowisetech.calc.model.Priority;
import com.sowisetech.calc.model.PriorityItem;
import com.sowisetech.calc.model.RiskPortfolio;
import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskQuestionaire;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.Urgency;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CalcDaoImplTest {

	CalcDaoImpl calcDaoImpl;
	EmbeddedDatabase db;

	@Mock
	private CalcDao calcDao;

	@Autowired
	AdvTableFields advTableFields;

	@Before
	public void setup() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("db_sql/calcschema.sql").addScript("db_sql/calcdata.sql").build();
		calcDaoImpl = new CalcDaoImpl();
		calcDaoImpl.setDataSource(db);
		calcDaoImpl.postConstruct();
	}

	@Test
	public void test_addGoalInfo_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Goal goal = new Goal();
		goal.setReferenceId("P00000");
		goal.setGoalName("Education");
		goal.setTenure(8);
		goal.setFutureValue(27568.15);
		goal.setCreated(timestamp);
		goal.setCreated_by("ADV0000000001");
		goal.setUpdated(timestamp);
		goal.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addGoalInfo(goal);
		Assert.assertEquals(1, result);
		List<Goal> goal1 = calcDaoImpl.fetchGoalByReferenceId("P00000");
		Assert.assertEquals("Education", goal1.get(0).getGoalName());
		Assert.assertEquals(8, goal1.get(0).getTenure());
	}

	@Test
	public void test_fetchCashFlowItemTypeIdByItemId_Success() {
		int cashFlow = calcDaoImpl.fetchCashFlowItemTypeIdByItemId(1);
		Assert.assertEquals(1, cashFlow);
	}

	@Test
	public void test_fetchCashFlowItemTypeIdByItemId_Error() {
		int cashFlow = calcDaoImpl.fetchCashFlowItemTypeIdByItemId(100);
		Assert.assertEquals(0, cashFlow);
	}

	@Test
	public void test_addCashFlow_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setActualAmt(125000);
		cashFlow.setBudgetAmt(14000);
		cashFlow.setCashFlowItemId(1);
		cashFlow.setCashFlowItemTypeId(2);
		cashFlow.setCreated(timestamp);
		cashFlow.setCreated_by("ADV0000000001");
		cashFlow.setUpdated(timestamp);
		cashFlow.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addCashFlow(cashFlow);
		Assert.assertEquals(1, result);
		List<CashFlow> cashFlow1 = calcDaoImpl.fetchCashFlowByRefId("P00000");
		Assert.assertEquals(6, cashFlow1.size());
		Assert.assertEquals(14000, cashFlow1.get(5).getBudgetAmt(), 0.00);
	}

	@Test
	public void test_addCashFlow_Error() {
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setActualAmt(125000);
		cashFlow.setBudgetAmt(14000);
		cashFlow.setCashFlowItemId(100);
		cashFlow.setCashFlowItemTypeId(200);
		int result = calcDaoImpl.addCashFlow(cashFlow);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchCashFlowByRefIdAndTypeId_Success() {
		List<CashFlow> cashFlowList = calcDaoImpl.fetchCashFlowByRefIdAndTypeId("P00000", 1);
		Assert.assertEquals(1, cashFlowList.size());
		Assert.assertEquals(1, cashFlowList.get(0).getCashFlowItemId());
	}

	@Test
	public void test_fetchCashFlowByRefIdAndTypeId_Error() {
		List<CashFlow> cashFlowList = calcDaoImpl.fetchCashFlowByRefIdAndTypeId("P00000", 100);
		Assert.assertEquals(0, cashFlowList.size());
	}

	@Test
	public void test_fetchCashFlowItemTypeIdByItemType_Success() {
		int result = calcDaoImpl.fetchCashFlowItemTypeIdByItemType("Mandatory Household Expense");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_fetchCashFlowItemTypeIdByItemType_Error() {
		int result = calcDaoImpl.fetchCashFlowItemTypeIdByItemType("Mandatory Household");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_addCashFlowSummary_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00001");
		cashFlowSummary.setMonthlyExpense(140000);
		cashFlowSummary.setCreated(timestamp);
		cashFlowSummary.setCreated_by("ADV0000000001");
		cashFlowSummary.setUpdated(timestamp);
		cashFlowSummary.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addCashFlowSummary(cashFlowSummary);
		Assert.assertEquals(1, result);
		CashFlowSummary flowSummary = calcDaoImpl.fetchCashFlowSummaryByRefId("P00001");
		Assert.assertEquals(140000, flowSummary.getMonthlyExpense(), 0.00);
	}

	@Test
	public void test_addNetworth_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setValue(150000);
		networth.setAccountTypeId(1);
		networth.setAccountEntryId(1);
		networth.setCreated(timestamp);
		networth.setCreated_by("ADV0000000001");
		int result = calcDaoImpl.addNetworth(networth);
		Assert.assertEquals(1, result);
		List<Networth> networth1 = calcDaoImpl.fetchNetworthByRefId("P00000");
		Assert.assertEquals(3, networth1.size());
		Assert.assertEquals(150000, networth1.get(1).getValue(), 0.00);
	}

	@Test
	public void test_addNetworth_Error() {
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setValue(150000);
		networth.setAccountTypeId(100);
		networth.setAccountEntryId(100);
		int result = calcDaoImpl.addNetworth(networth);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchAccountTypeIdByEntryId_Success() {
		int accountTypeId = calcDaoImpl.fetchAccountTypeIdByEntryId(1);
		Assert.assertEquals(1, accountTypeId);
	}

	@Test
	public void test_fetchAccountTypeIdByEntryId_Error() {
		int accountTypeId = calcDaoImpl.fetchAccountTypeIdByEntryId(100);
		Assert.assertEquals(0, accountTypeId);
	}

	@Test
	public void test_fetchAccountTypeIdByAccountType_Success() {
		int typeId = calcDaoImpl.fetchAccountTypeIdByAccountType("Assets");
		Assert.assertEquals(1, typeId);
	}

	@Test
	public void test_fetchAccountTypeIdByAccountType_Error() {
		int typeId = calcDaoImpl.fetchAccountTypeIdByAccountType("Asset");
		Assert.assertEquals(0, typeId);
	}

	@Test
	public void test_fetchNetworthByAccountTypeIdAndRefId_Success() {
		List<Networth> networth = calcDaoImpl.fetchNetworthByAccountTypeIdAndRefId(1, "P00000");
		Assert.assertEquals(1, networth.size());
	}

	@Test
	public void test_fetchNetworthByAccountTypeIdAndRefId_Error() {
		List<Networth> networth = calcDaoImpl.fetchNetworthByAccountTypeIdAndRefId(1, "P00120");
		Assert.assertEquals(0, networth.size());
	}

	@Test
	public void test_addNetworthSummary() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00001");
		networthSummary.setNetworth(1500000);
		networthSummary.setFuture_assetValue(11000);
		networthSummary.setCreated(timestamp);
		networthSummary.setCreated_by("ADV0000000001");
		networthSummary.setUpdated(timestamp);
		networthSummary.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addNetworthSummary(networthSummary);
		Assert.assertEquals(1, result);
		NetworthSummary networthSummary1 = calcDaoImpl.fetchNetworthSummaryByRefId("P00001");
		Assert.assertEquals(11000, networthSummary1.getFuture_assetValue(), 0.0);
		Assert.assertEquals(1500000, networthSummary1.getNetworth(), 0.00);
	}

	@Test
	public void test_addInsurance() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Insurance insurance = new Insurance();
		insurance.setReferenceId("P00001");
		insurance.setAnnualIncome(1900000);
		insurance.setStability("Stable");
		insurance.setCreated(timestamp);
		insurance.setCreated_by("ADV0000000001");
		insurance.setUpdated(timestamp);
		insurance.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addInsurance(insurance);
		Assert.assertEquals(1, result);
		Insurance insurance1 = calcDaoImpl.fetchInsuranceByRefId("P00001");
		Assert.assertEquals("Stable", insurance1.getStability());
		Assert.assertEquals(1900000, insurance1.getAnnualIncome(), 0.00);
	}

	@Test
	public void test_addPriorities_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(3);
		priority.setCreated(timestamp);
		priority.setCreated_by("ADV0000000001");
		priority.setUpdated(timestamp);
		priority.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addPriorities(priority);
		Assert.assertEquals(1, result);
		List<Priority> priority1 = calcDaoImpl.fetchPriorityByRefId("P00000");
		Assert.assertEquals(2, priority1.size());
		Assert.assertEquals(3, priority1.get(0).getUrgencyId());
	}

	@Test
	public void test_addPriorities_Error() {
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(300);
		int result = calcDaoImpl.addPriorities(priority);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchPriorityByRefId_Success() {
		List<Priority> priorityList = calcDaoImpl.fetchPriorityByRefId("P00000");
		Assert.assertEquals(1, priorityList.size());
		// Assert.assertEquals(1520, priorityList.get(0).getValue(), 0.00);
	}

	@Test
	public void test_fetchPriorityByRefId_Error() {
		List<Priority> priorityList = calcDaoImpl.fetchPriorityByRefId("P01257");
		Assert.assertEquals(0, priorityList.size());
	}

	@Test
	public void test_fetchPriorityByRefIdAndItemId_Success() {
		Priority priority = calcDaoImpl.fetchPriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(3, priority.getUrgencyId());
		// Assert.assertEquals(5, priority.getTimeLine());
	}

	@Test
	public void test_fetchPriorityByRefIdAndItemId_Error() {
		Priority priority = calcDaoImpl.fetchPriorityByRefIdAndItemId("P01121", 100);
		Assert.assertEquals(null, priority);
	}

	@Test
	public void test_updatePriorityOrder_Success() {
		calcDaoImpl.updatePriorityOrder("P00000", 1, 2, "ADV0000000001", null);
		List<Priority> priority = calcDaoImpl.fetchPriorityByRefId("P00000");
		Assert.assertEquals(1, priority.size());
		Assert.assertEquals(1, priority.get(0).getPriorityOrder());
	}

	@Test
	public void test_updatePriorityOrder_Error() {
		calcDaoImpl.updatePriorityOrder("P00000", 1, 2, "ADV0000000001", null);
		List<Priority> priority = calcDaoImpl.fetchPriorityByRefId("P12000");
		Assert.assertEquals(0, priority.size());
	}

	// @Test //Encode decode
	// public void test_fetchParty_Success() {
	// String encryptPass ="Sowise@Ever21";
	// Party party = calcDaoImpl.fetchParty(1, "N",encryptPass);
	// Assert.assertEquals("ADV000000000A", party.getRoleBasedId());
	// Assert.assertEquals(2, party.getRoleId());
	// }

	// @Test //Encode decode
	// public void test_fetchParty_Error() {
	// String encryptPass ="Sowise@Ever21";
	// Party party = calcDaoImpl.fetchParty(10, "N",encryptPass);
	// Assert.assertEquals(null, party);
	// }

	@Test
	public void test_fetchScoreByAnswerId_Success() {
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setAnswerId(1);
		int result = calcDaoImpl.fetchScoreByAnswerId(riskProfile);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_fetchScoreByAnswerId_Error() {
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setAnswerId(100);
		int result = calcDaoImpl.fetchScoreByAnswerId(riskProfile);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_addRiskProfile_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(1);
		riskProfile.setQuestionId("2");
		riskProfile.setScore(5);
		riskProfile.setUpdated(timestamp);
		riskProfile.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addRiskProfile(riskProfile);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addRiskProfile_Error() {
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(10);
		riskProfile.setQuestionId("2");
		riskProfile.setScore(5);
		int result = calcDaoImpl.addRiskProfile(riskProfile);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchRiskProfileByRefId_Success() {
		List<RiskProfile> riskProfileList = calcDaoImpl.fetchRiskProfileByRefId("P00000");
		Assert.assertEquals(1, riskProfileList.size());
		Assert.assertEquals(1, riskProfileList.get(0).getAnswerId());
	}

	@Test
	public void test_fetchRiskProfileByRefId_Error() {
		List<RiskProfile> riskProfileList = calcDaoImpl.fetchRiskProfileByRefId("P12300");
		Assert.assertEquals(0, riskProfileList.size());
	}

	@Test
	public void test_fetchRiskPortfolioByPoints_Success() {
		RiskPortfolio riskPortfolio = calcDaoImpl.fetchRiskPortfolioByPoints("30 or less");
		Assert.assertEquals(10, riskPortfolio.getCash());
		Assert.assertEquals("High Growth Investor", riskPortfolio.getBehaviour());
	}

	@Test
	public void test_fetchRiskPortfolioByPoints_Error() {
		RiskPortfolio riskPortfolio = calcDaoImpl.fetchRiskPortfolioByPoints("30");
		Assert.assertEquals(null, riskPortfolio);
	}

	@Test
	public void test_addRiskSummary_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00001");
		riskSummary.setBehaviour("Balanced Investor");
		riskSummary.setDebt_alloc(30);
		riskSummary.setCreated(timestamp);
		riskSummary.setCreated_by("ADV0000000001");
		riskSummary.setUpdated(timestamp);
		riskSummary.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addRiskSummary(riskSummary);
		Assert.assertEquals(1, result);
		RiskSummary risk = calcDaoImpl.fetchRiskSummaryByRefId("P00001");
		Assert.assertEquals("Balanced Investor", risk.getBehaviour());
		Assert.assertEquals(30, risk.getDebt_alloc());
	}

	@Test
	public void test_fetchGoalByRefId_Success() {
		List<Goal> goal = calcDaoImpl.fetchGoalByReferenceId("P00000");
		Assert.assertEquals(1, goal.size());
		Assert.assertEquals(8, goal.get(0).getTenure());
	}

	@Test
	public void test_fetchGoalByRefId_Error() {
		List<Goal> goal = calcDaoImpl.fetchGoalByReferenceId("P00051");
		Assert.assertEquals(0, goal.size());
	}

	@Test
	public void test_fetchCashFlowByRefId_Success() {
		List<CashFlow> cashFlow = calcDaoImpl.fetchCashFlowByRefId("P00000");
		Assert.assertEquals(5, cashFlow.size());
		// Assert.assertEquals(90000, cashFlow.get(6).getBudgetAmt(), 0.00);
	}

	@Test
	public void test_fetchCashFlowByRefId_Error() {
		List<CashFlow> cashFlow = calcDaoImpl.fetchCashFlowByRefId("P00025");
		Assert.assertEquals(0, cashFlow.size());
	}

	@Test
	public void test_fetchNetworthByRefId_Success() {
		List<Networth> networth = calcDaoImpl.fetchNetworthByRefId("P00000");
		Assert.assertEquals(2, networth.size());
		Assert.assertEquals(180000, networth.get(1).getFutureValue(), 0.00);
	}

	@Test
	public void test_fetchNetworthByRefId_Error() {
		List<Networth> networth = calcDaoImpl.fetchNetworthByRefId("P00015");
		Assert.assertEquals(0, networth.size());
	}

	@Test
	public void test_fetchCashFlowSummaryByRefId_Success() {
		CashFlowSummary cashFlowSummary = calcDaoImpl.fetchCashFlowSummaryByRefId("P00000");
		Assert.assertEquals(140000, cashFlowSummary.getMonthlyExpense(), 0.00);
	}

	@Test
	public void test_fetchCashFlowSummaryByRefId_Error() {
		CashFlowSummary cashFlowSummary = calcDaoImpl.fetchCashFlowSummaryByRefId("P04500");
		Assert.assertEquals(null, cashFlowSummary);
	}

	@Test
	public void test_fetchNetworthSummaryByRefId_Success() {
		NetworthSummary networthSummary = calcDaoImpl.fetchNetworthSummaryByRefId("P00000");
		Assert.assertEquals(17000, networthSummary.getCurrent_liability(), 0.0);
		Assert.assertEquals(12000, networthSummary.getCurrent_assetValue(), 0.00);
	}

	@Test
	public void test_fetchNetworthSummaryByRefId_Error() {
		NetworthSummary networthSummary = calcDaoImpl.fetchNetworthSummaryByRefId("P00044");
		Assert.assertEquals(null, networthSummary);
	}

	@Test
	public void test_fetchRiskSummaryByRefId_Success() {
		RiskSummary riskSummary = calcDaoImpl.fetchRiskSummaryByRefId("P00000");
		Assert.assertEquals("High Growth Investor", riskSummary.getBehaviour());
		Assert.assertEquals(10, riskSummary.getCash_alloc());
	}

	@Test
	public void test_fetchRiskSummaryByRefId_Error() {
		RiskSummary riskSummary = calcDaoImpl.fetchRiskSummaryByRefId("P00044");
		Assert.assertEquals(null, riskSummary);
	}

	@Test
	public void test_fetchInsuranceByRefId_Succcess() {
		Insurance insurance = calcDaoImpl.fetchInsuranceByRefId("P00000");
		Assert.assertEquals("Stable", insurance.getStability());
	}

	@Test
	public void test_fetchInsuranceByRefId_Error() {
		Insurance insurance = calcDaoImpl.fetchInsuranceByRefId("P00044");
		Assert.assertEquals(null, insurance);
	}

	@Test
	public void test_fetchAccountTypeList() {
		List<AccountType> accountType = calcDaoImpl.fetchAccountTypeList();
		Assert.assertEquals(2, accountType.size());
		Assert.assertEquals("Liabilities", accountType.get(1).getAccountType());
	}

	@Test
	public void test_fetchAccountList() {
		List<Account> account = calcDaoImpl.fetchAccountList();
		Assert.assertEquals(2, account.size());
		Assert.assertEquals("Savings Account Balance as on date", account.get(0).getAccountEntry());
	}

	// @Test //Encode decode
	// public void test_fetchPartyIdByRoleBasedId_Success() {
	// String encryptPass ="Sowise@Ever21";
	// Party party = calcDaoImpl.fetchPartyIdByRoleBasedId("ADV000000000A",
	// "N",encryptPass);
	// Assert.assertEquals("ADV000000000A", party.getRoleBasedId());
	// Assert.assertEquals(1, party.getPartyStatusId());
	// }

	// @Test //Encode decode
	// public void test_fetchPartyIdByRoleBasedId_Error() {
	// String encryptPass ="Sowise@Ever21";
	// Party party = calcDaoImpl.fetchPartyIdByRoleBasedId("ADV000000000Z",
	// "N",encryptPass);
	// Assert.assertEquals(null, party);
	// }

	@Test
	public void test_fetchCashFlowItemTypeList() {
		List<CashFlowItemType> cashFlowItemType = calcDaoImpl.fetchCashFlowItemTypeList();
		Assert.assertEquals(5, cashFlowItemType.size());
	}

	@Test
	public void test_fetchCashFlowItemList() {
		List<CashFlowItem> cashFlowItem = calcDaoImpl.fetchCashFlowItemList();
		Assert.assertEquals(5, cashFlowItem.size());
		// Assert.assertEquals("School Fees", cashFlowItem.get(1).getCashFlowItem());
	}

	@Test
	public void test_fetchPriorityItemList() {
		List<PriorityItem> priorityItem = calcDaoImpl.fetchPriorityItemList();
		Assert.assertEquals(6, priorityItem.size());
		Assert.assertEquals("Investing in Stocks", priorityItem.get(0).getPriorityItem());
	}

	@Test
	public void test_fetchUrgencyList() {
		List<Urgency> urgency = calcDaoImpl.fetchUrgencyList();
		Assert.assertEquals(3, urgency.size());
		Assert.assertEquals("High", urgency.get(0).getValue());
	}

	@Test
	public void test_fetchRiskPortfolioList() {
		List<RiskPortfolio> riskPortfolio = calcDaoImpl.fetchRiskPortfolioList();
		Assert.assertEquals(5, riskPortfolio.size());
		Assert.assertEquals("30 or less", riskPortfolio.get(0).getPoints());
	}

	@Test
	public void test_fetchRiskQuestionaireList() {
		List<RiskQuestionaire> riskQuestionaire = calcDaoImpl.fetchRiskQuestionaireList();
		Assert.assertEquals(2, riskQuestionaire.size());
		Assert.assertEquals("I enjoy taking very high risk as the same rewards high return",
				riskQuestionaire.get(0).getAnswer());
	}

	@Test
	public void test_addEmiCalculator() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00001");
		emiCalculator.setLoanAmount(500000);
		emiCalculator.setTenure(15);
		emiCalculator.setCreated(timestamp);
		emiCalculator.setCreated_by("ADV0000000001");
		emiCalculator.setUpdated(timestamp);
		emiCalculator.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addEmiCalculator(emiCalculator);
		Assert.assertEquals(1, result);
		EmiCalculator emiCalculator1 = calcDaoImpl.fetchEmiCalculatorByRefId("P00001");
		Assert.assertEquals(15, emiCalculator1.getTenure());
		Assert.assertEquals(500000, emiCalculator1.getLoanAmount(), 0.0);
	}

	@Test
	public void test_addEmiCapacity() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setReferenceId("P00001");
		emiCapacity.setStability("HIGH");
		emiCapacity.setCurrentAge(22);
		emiCapacity.setCreated(timestamp);
		emiCapacity.setCreated_by("ADV0000000001");
		emiCapacity.setUpdated(timestamp);
		emiCapacity.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addEmiCapacity(emiCapacity);
		Assert.assertEquals(1, result);
		EmiCapacity emiCapacity1 = calcDaoImpl.fetchEmiCapacityByRefId("P00001");
		Assert.assertEquals(22, emiCapacity1.getCurrentAge(), 0.0);
		Assert.assertEquals("HIGH", emiCapacity1.getStability());
	}

	@Test
	public void test_addPartialPayment() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		PartialPayment partialPayment = new PartialPayment();
		partialPayment.setReferenceId("P00000");
		partialPayment.setLoanAmount(8000000);
		partialPayment.setInterestRate(10);
		partialPayment.setCreated(timestamp);
		partialPayment.setCreated_by("ADV0000000001");
		partialPayment.setUpdated(timestamp);
		partialPayment.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addPartialPayment(partialPayment);
		Assert.assertEquals(1, result);
		List<PartialPayment> partialPayment1 = calcDaoImpl.fetchPartialPaymentByRefId("P00000");
		Assert.assertEquals(10, partialPayment1.get(0).getInterestRate(), 0.0);
		Assert.assertEquals(8000000, partialPayment1.get(0).getLoanAmount(), 0.0);
	}

	@Test
	public void test_addInterestChange() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		InterestChange interestChange = new InterestChange();
		interestChange.setReferenceId("P00001");
		interestChange.setLoanAmount(5000000);
		interestChange.setInterestRate(15);
		interestChange.setInterestChangedDate("10-02-2021");
		interestChange.setCreated(timestamp);
		interestChange.setCreated_by("ADV0000000001");
		interestChange.setUpdated(timestamp);
		interestChange.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addInterestChange(interestChange);
		Assert.assertEquals(1, result);
		List<InterestChange> interestChange1 = calcDaoImpl.fetchInterestChangeByRefId("P00001");
		Assert.assertEquals(15, interestChange1.get(0).getInterestRate(), 0.0);
		Assert.assertEquals("10-02-2021", interestChange1.get(0).getInterestChangedDate());
	}

	@Test
	public void test_addEmiChange() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EmiChange emiChange = new EmiChange();
		emiChange.setReferenceId("P00001");
		emiChange.setLoanAmount(5000000);
		emiChange.setInterestRate(20);
		emiChange.setLoanDate("01-01-2015");
		emiChange.setTenure(10);
		emiChange.setIncreasedEmi(9000);
		emiChange.setEmiChangedDate("02-02-2020");
		emiChange.setCreated(timestamp);
		emiChange.setCreated_by("ADV0000000001");
		emiChange.setUpdated(timestamp);
		emiChange.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addEmiChange(emiChange);
		Assert.assertEquals(1, result);
		List<EmiChange> interestChange1 = calcDaoImpl.fetchEmiChangeByRefId("P00001");
		Assert.assertEquals(20.0, interestChange1.get(0).getInterestRate(), 0.0);
		Assert.assertEquals(9000, interestChange1.get(0).getIncreasedEmi(), 0.0);
	}

	@Test
	public void test_addEmiInterestChange() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EmiInterestChange emiInterestChange = new EmiInterestChange();
		emiInterestChange.setReferenceId("P00001");
		emiInterestChange.setLoanAmount(5000000);
		emiInterestChange.setInterestRate(15);
		emiInterestChange.setCreated(timestamp);
		emiInterestChange.setCreated_by("ADV0000000001");
		emiInterestChange.setUpdated(timestamp);
		emiInterestChange.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.addEmiInterestChange(emiInterestChange);
		Assert.assertEquals(1, result);
		List<EmiInterestChange> emiInterestChange1 = calcDaoImpl.fetchEmiInterestChangeByRefId("P00001");
		Assert.assertEquals(15, emiInterestChange1.get(0).getInterestRate(), 0.0);
		Assert.assertEquals(5000000, emiInterestChange1.get(0).getLoanAmount(), 0.0);
	}

	@Test
	public void test_fetchPriorityItemByItemId_Success() {
		PriorityItem priorityItem = calcDaoImpl.fetchPriorityItemByItemId(1);
		Assert.assertEquals("Investing in Stocks", priorityItem.getPriorityItem());
	}

	@Test
	public void test_fetchPriorityItemByItemId_Error() {
		PriorityItem priorityItem = calcDaoImpl.fetchPriorityItemByItemId(100);
		Assert.assertEquals(null, priorityItem);
	}

	@Test
	public void test_fetchRiskProfileByRefIdAndAnswerId_Success() {
		RiskProfile riskProfile = calcDaoImpl.fetchRiskProfileByRefIdAndAnswerId("P00000", 1);
		Assert.assertEquals(5, riskProfile.getScore());
		Assert.assertEquals(1, riskProfile.getAnswerId());
	}

	@Test
	public void test_fetchRiskProfileByRefIdAndAnswerId_Error() {
		RiskProfile riskProfile = calcDaoImpl.fetchRiskProfileByRefIdAndAnswerId("P00000", 100);
		Assert.assertEquals(null, riskProfile);
	}

	@Test
	public void test_fetchNetworthByRefIdAndEntryId_Success() {
		Networth networth = calcDaoImpl.fetchNetworthByRefIdAndEntryId("P00000", 1);
		Assert.assertEquals(150000, networth.getValue(), 0.0);
		Assert.assertEquals(180000, networth.getFutureValue(), 0.0);
	}

	@Test
	public void test_fetchNetworthByRefIdAndEntryId_Error() {
		Networth networth = calcDaoImpl.fetchNetworthByRefIdAndEntryId("P45000", 100);
		Assert.assertEquals(null, networth);
	}

	@Test
	public void test_fetchAccountTypeByTypeId_Success() {
		AccountType accountType = calcDaoImpl.fetchAccountTypeByTypeId(1);
		Assert.assertEquals("Assets", accountType.getAccountType());
	}

	@Test
	public void test_fetchAccountTypeByTypeId_Error() {
		AccountType accountType = calcDaoImpl.fetchAccountTypeByTypeId(100);
		Assert.assertEquals(null, accountType);
	}

	@Test
	public void test_fetchCashFlowByRefIdAndItemId_Success() {
		CashFlow cashFlow = calcDaoImpl.fetchCashFlowByRefIdAndItemId("P00000", 2);
		Assert.assertEquals("10-07-2000", cashFlow.getDate());
		Assert.assertEquals(90000, cashFlow.getBudgetAmt(), 0.0);
	}

	@Test
	public void test_fetchCashFlowByRefIdAndItemId_Error() {
		CashFlow cashFlow = calcDaoImpl.fetchCashFlowByRefIdAndItemId("P00000", 20);
		Assert.assertEquals(null, cashFlow);
	}

	@Test
	public void test_fetchCashFlowItemTypeByTypeId_Success() {
		CashFlowItemType cashFlowItemType = calcDaoImpl.fetchCashFlowItemTypeByTypeId(2);
		Assert.assertEquals("Life Style Expenses", cashFlowItemType.getCashFlowItemType());
	}

	@Test
	public void test_fetchCashFlowItemTypeByTypeId_Error() {
		CashFlowItemType cashFlowItemType = calcDaoImpl.fetchCashFlowItemTypeByTypeId(200);
		Assert.assertEquals(null, cashFlowItemType);
	}

	@Test
	public void test_fetchEmiCalculatorByRefId_Success() {
		EmiCalculator emiCalculator = calcDaoImpl.fetchEmiCalculatorByRefId("P00000");
		Assert.assertEquals(5000000, emiCalculator.getLoanAmount(), 0.0);
		Assert.assertEquals(15, emiCalculator.getTenure());
	}

	@Test
	public void test_fetchEmiCalculatorByRefId_Error() {
		EmiCalculator emiCalculator = calcDaoImpl.fetchEmiCalculatorByRefId("P00044");
		Assert.assertEquals(null, emiCalculator);
	}

	@Test
	public void test_fetchEmiCapacityByRefId_Success() {
		EmiCapacity emiCapacity = calcDaoImpl.fetchEmiCapacityByRefId("P00000");
		Assert.assertEquals("HIGH", emiCapacity.getStability());
		Assert.assertEquals(140000, emiCapacity.getNetFamilyIncome(), 0.0);
	}

	@Test
	public void test_fetchEmiCapacityByRefId_Error() {
		EmiCapacity emiCapacity = calcDaoImpl.fetchEmiCapacityByRefId("P00044");
		Assert.assertEquals(null, emiCapacity);
	}

	@Test
	public void test_fetchPartialPaymentByRefId_Success() {
		List<PartialPayment> partialPayment = calcDaoImpl.fetchPartialPaymentByRefId("P00000");
		Assert.assertEquals(300000, partialPayment.get(0).getPartPayAmount(), 0.0);
		Assert.assertEquals(15, partialPayment.get(0).getTenure());
	}

	@Test
	public void test_fetchPartialPaymentByRefId_Error() {
		List<PartialPayment> partialPayment = calcDaoImpl.fetchPartialPaymentByRefId("P00044");
		Assert.assertEquals(0, partialPayment.size());
	}

	@Test
	public void test_fetchInterestChangeByRefId_Success() {
		List<InterestChange> interestChange = calcDaoImpl.fetchInterestChangeByRefId("P00000");
		Assert.assertEquals("10-02-2018", interestChange.get(0).getLoanDate());
		Assert.assertEquals(9, interestChange.get(0).getChangedRate(), 0.0);
	}

	@Test
	public void test_fetchInterestChangeByRefId_Error() {
		List<InterestChange> interestChange = calcDaoImpl.fetchInterestChangeByRefId("P00044");
		Assert.assertEquals(0, interestChange.size());
	}

	@Test
	public void test_fetchEmiChangeByRefId_Success() {
		List<EmiChange> emiChange = calcDaoImpl.fetchEmiChangeByRefId("P00000");
		Assert.assertEquals(10000, emiChange.get(0).getIncreasedEmi(), 0.0);
		Assert.assertEquals("01-01-2015", emiChange.get(0).getLoanDate());
	}

	@Test
	public void test_fetchEmiChangeByRefId_Error() {
		List<EmiChange> emiChange = calcDaoImpl.fetchEmiChangeByRefId("P00044");
		Assert.assertEquals(0, emiChange.size());
	}

	@Test
	public void test_fetchEmiInterestChangeByRefId_Success() {
		List<EmiInterestChange> emiInterestChange = calcDaoImpl.fetchEmiInterestChangeByRefId("P00000");
		Assert.assertEquals(5000, emiInterestChange.get(0).getIncreasedEmi(), 0.0);
		Assert.assertEquals(500000, emiInterestChange.get(0).getLoanAmount(), 0.0);
	}

	@Test
	public void test_fetchEmiInterestChangeByRefId_Error() {
		List<EmiInterestChange> emiInterestChange = calcDaoImpl.fetchEmiInterestChangeByRefId("P00044");
		Assert.assertEquals(0, emiInterestChange.size());
	}

	@Test
	public void test_fetchValueByInsuranceItem_Success() {
		String result = calcDaoImpl.fetchValueByInsuranceItem("annualIncome");
		Assert.assertEquals("Approximate Annual Income", result);
	}

	@Test
	public void test_fetchValueByInsuranceItem_Error() {
		String result = calcDaoImpl.fetchValueByInsuranceItem("annual");
		Assert.assertEquals(null, result);
	}

	// @Test //Encode decode
	// public void test_addPlanInfo() {
	// String encryptPass ="Sowise@Ever21";
	// Plan plan = new Plan();
	// plan.setPartyId(1);
	// plan.setChild1("yes");
	// plan.setGrandParent("yes");
	// plan.setName("name");
	// plan.setAge(24);
	// plan.setParentPartyId(1);
	// plan.setReferenceId("ADV000000000B");
	// int result = calcDaoImpl.addPlanInfo(plan,encryptPass);
	// Assert.assertEquals(1, result);
	// }

	@Test
	public void test_fetchRoleBasedIdByPartyId_Success() {
		String result = calcDaoImpl.fetchRoleBasedIdByPartyId(1);
		Assert.assertEquals("ADV000000000A", result);
	}

	@Test
	public void test_fetchRoleBasedIdByPartyId_Error() {
		String result = calcDaoImpl.fetchRoleBasedIdByPartyId(10);
		Assert.assertEquals(null, result);
	}

	// @Test //Encode decode
	// public void test_fetchPlanByReferenceId_Success() {
	// String encryptPass ="Sowise@Ever21";
	// Plan result = calcDaoImpl.fetchPlanByReferenceId("P00000",encryptPass);
	// Assert.assertEquals("Adv", result.getName());
	// }

	// @Test //Encode decode
	// public void test_fetchPlanByReferenceId_Error() {
	// String encryptPass ="Sowise@Ever21";
	// Plan result = calcDaoImpl.fetchPlanByReferenceId("P00044",encryptPass);
	// Assert.assertEquals(null, result);
	// }

	@Test
	public void test_updateCashFlow_Success() {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setActualAmt(1000);
		cashFlow.setBudgetAmt(5000);
		cashFlow.setCashFlowId(1);
		cashFlow.setDate("02-05-2000");
		cashFlow.setCashFlowItemId(1);
		cashFlow.setCashFlowItemTypeId(1);
		cashFlow.setUpdated(timestamp);
		cashFlow.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updateCashFlow(cashFlow);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateCashFlow_Error() {
		CashFlow cashFlow = new CashFlow();
		cashFlow.setReferenceId("P00000");
		cashFlow.setActualAmt(1000);
		cashFlow.setBudgetAmt(5000);
		cashFlow.setCashFlowId(1);
		cashFlow.setDate("02-05-2000");
		cashFlow.setCashFlowItemId(10);
		cashFlow.setCashFlowItemTypeId(10);
		int result = calcDaoImpl.updateCashFlow(cashFlow);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeCashFlowSummary_Success() {
		int result = calcDaoImpl.removeCashFlowSummary("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeCashFlowSummary_Error() {
		int result = calcDaoImpl.removeCashFlowSummary("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateNetworth_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setValue(150000);
		networth.setAccountTypeId(1);
		networth.setAccountEntryId(1);
		networth.setUpdated(timestamp);
		networth.setUpdated_by("ADV0000000001");
		networth.setFutureValue(200000);
		int result = calcDaoImpl.updateNetworth(networth);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateNetworth_Error() {
		Networth networth = new Networth();
		networth.setReferenceId("P00000");
		networth.setValue(150000);
		networth.setAccountTypeId(10);
		networth.setAccountEntryId(10);
		int result = calcDaoImpl.updateNetworth(networth);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeNetworthSummary_Success() {
		int result = calcDaoImpl.removeNetworthSummary("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeNetworthSummary_Error() {
		int result = calcDaoImpl.removeNetworthSummary("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updatePriority_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(1);
		priority.setPriorityOrder(1);
		priority.setPriorityId(1);
		priority.setUpdated(timestamp);
		priority.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updatePriority(priority);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updatePriority_Error() {
		Priority priority = new Priority();
		priority.setReferenceId("P00000");
		priority.setUrgencyId(1);
		priority.setPriorityItemId(10);
		priority.setPriorityOrder(1);
		priority.setPriorityId(1);
		// priority.setValue(100);
		// priority.setTimeLine(1);
		int result = calcDaoImpl.updatePriority(priority);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateInsurance_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Insurance insurance = new Insurance();
		insurance.setReferenceId("P00000");
		insurance.setAnnualIncome(1900000);
		insurance.setStability("Stable");
		insurance.setUpdated(timestamp);
		insurance.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updateInsurance(insurance);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateInsurance_Error() {
		Insurance insurance = new Insurance();
		insurance.setReferenceId("P00044");
		insurance.setAnnualIncome(1900000);
		insurance.setStability("Stable");
		int result = calcDaoImpl.updateInsurance(insurance);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateRiskProfile_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00000");
		riskProfile.setAnswerId(1);
		riskProfile.setQuestionId("1");
		riskProfile.setScore(5);
		riskProfile.setUpdated(timestamp);
		riskProfile.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updateRiskProfile(riskProfile);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateRiskProfile_Error() {
		RiskProfile riskProfile = new RiskProfile();
		riskProfile.setReferenceId("P00044");
		riskProfile.setAnswerId(1);
		riskProfile.setQuestionId("1");
		riskProfile.setScore(5);
		int result = calcDaoImpl.updateRiskProfile(riskProfile);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeRiskSummary_Success() {
		int result = calcDaoImpl.removeRiskSummary("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeRiskSummary_Error() {
		int result = calcDaoImpl.removeRiskSummary("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateEmiCalculator_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00000");
		emiCalculator.setLoanAmount(500000);
		emiCalculator.setTenure(16);
		emiCalculator.setUpdated(timestamp);
		emiCalculator.setUpdated_by("ADV0000000000");
		int result = calcDaoImpl.updateEmiCalculator(emiCalculator);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateEmiCalculator_Error() {
		EmiCalculator emiCalculator = new EmiCalculator();
		emiCalculator.setReferenceId("P00044");
		emiCalculator.setLoanAmount(500000);
		emiCalculator.setTenure(15);
		int result = calcDaoImpl.updateEmiCalculator(emiCalculator);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateEmiCapacity_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setReferenceId("P00000");
		emiCapacity.setStability("HIGH");
		emiCapacity.setCurrentAge(22);
		emiCapacity.setUpdated(timestamp);
		emiCapacity.setUpdated_by("ADV0000000000");
		int result = calcDaoImpl.updateEmiCapacity(emiCapacity);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateEmiCapacity_Error() {
		EmiCapacity emiCapacity = new EmiCapacity();
		emiCapacity.setReferenceId("P00044");
		emiCapacity.setStability("HIGH");
		emiCapacity.setCurrentAge(22);
		int result = calcDaoImpl.updateEmiCapacity(emiCapacity);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchRiskProfileByRefIdAndQuestionId_Success() {
		RiskProfile riskProfile = calcDaoImpl.fetchRiskProfileByRefIdAndQuestionId("P00000", "1");
		Assert.assertEquals(5, riskProfile.getScore());
		Assert.assertEquals(1, riskProfile.getAnswerId());
	}

	@Test
	public void test_fetchRiskProfileByRefIdAndQuestionId_Error() {
		RiskProfile riskProfile = calcDaoImpl.fetchRiskProfileByRefIdAndQuestionId("P00044", "1");
		Assert.assertEquals(null, riskProfile);
	}

	@Test
	public void test_fetchQuestionIdFromRiskQuestionaire() {
		List<String> questionIds = calcDaoImpl.fetchQuestionIdFromRiskQuestionaire();
		Assert.assertEquals(2, questionIds.size());
	}

	@Test
	public void test_fetchRiskQuestionaireByQuestionId_Success() {
		List<RiskQuestionaire> riskQuestionaire = calcDaoImpl.fetchRiskQuestionaireByQuestionId("1");
		Assert.assertEquals(1, riskQuestionaire.size());
		Assert.assertEquals(1, riskQuestionaire.get(0).getAnswerId());
	}

	@Test
	public void test_fetchRiskQuestionaireByQuestionId_Error() {
		List<RiskQuestionaire> riskQuestionaire = calcDaoImpl.fetchRiskQuestionaireByQuestionId("10");
		Assert.assertEquals(0, riskQuestionaire.size());
	}

	@Test
	public void test_fetchQuestionByQuestionId_Success() {
		String question = calcDaoImpl.fetchQuestionByQuestionId("5");
		Assert.assertEquals("What is the most aggressive investment you have made?", question);
	}

	@Test
	public void test_fetchQuestionByQuestionId_Error() {
		String question = calcDaoImpl.fetchQuestionByQuestionId("50");
		Assert.assertEquals(null, question);
	}

	@Test
	public void test_fetchGoalByRefIdAndGoalName_Success() {
		Goal result = calcDaoImpl.fetchGoalByRefIdAndGoalName("P00000", "Education");
		Assert.assertEquals(3000000.00, result.getGoalAmount(), 0.00);
	}

	@Test
	public void test_fetchGoalByRefIdAndGoalName_Error() {
		Goal result = calcDaoImpl.fetchGoalByRefIdAndGoalName("P00044", "Education");
		Assert.assertEquals(null, result);
	}

	@Test // test //
	public void test_updateGoalInfo_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Goal goal = new Goal();
		goal.setGoalId(1);
		goal.setReferenceId("P00000");
		goal.setGoalName("Education");
		goal.setGoalAmount(400000);
		goal.setUpdated(timestamp);
		goal.setUpdated_by("ADV0000000000");
		int result = calcDaoImpl.updateGoalInfo(goal);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateGoalInfo_Error() {
		Goal goal = new Goal();
		goal.setGoalId(1);
		goal.setReferenceId("P00044");
		goal.setGoalName("Education");
		goal.setGoalAmount(500000);
		int result = calcDaoImpl.updateGoalInfo(goal);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removePartialPaymentByRefId_Success() {
		int result = calcDaoImpl.removePartialPaymentByRefId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removePartialPaymentByRefId_Error() {
		int result = calcDaoImpl.removePartialPaymentByRefId("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeEmiChangeByRefId_Success() {
		int result = calcDaoImpl.removeEmiChangeByRefId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeEmiChangeByRefId_Error() {
		int result = calcDaoImpl.removeEmiChangeByRefId("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeInterestChangeByRefId_Success() {
		int result = calcDaoImpl.removeInterestChangeByRefId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeInterestChangeByRefId_Error() {
		int result = calcDaoImpl.removeInterestChangeByRefId("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeEmiInterestChangeByRefId_Success() {
		int result = calcDaoImpl.removeEmiInterestChangeByRefId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeEmiInterestChangeByRefId_Error() {
		int result = calcDaoImpl.removeEmiInterestChangeByRefId("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateCashFlowSummary_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00000");
		cashFlowSummary.setMonthlyExpense(140000);
		cashFlowSummary.setUpdated(timestamp);
		cashFlowSummary.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updateCashFlowSummary(cashFlowSummary);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateCashFlowSummary() {
		CashFlowSummary cashFlowSummary = new CashFlowSummary();
		cashFlowSummary.setReferenceId("P00001");
		cashFlowSummary.setMonthlyExpense(140000);
		int result = calcDaoImpl.updateCashFlowSummary(cashFlowSummary);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateNetworthSummary_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00000");
		networthSummary.setCurrent_assetValue(500000);
		networthSummary.setUpdated(timestamp);
		networthSummary.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updateNetworthSummary(networthSummary);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateNetworthSummary_Error() {
		NetworthSummary networthSummary = new NetworthSummary();
		networthSummary.setReferenceId("P00010");
		networthSummary.setCurrent_assetValue(500000);
		int result = calcDaoImpl.updateNetworthSummary(networthSummary);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_updateRiskSummary_Success() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00000");
		riskSummary.setBehaviour("Growth");
		riskSummary.setUpdated(timestamp);
		riskSummary.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.updateRiskSummary(riskSummary);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateRiskSummary_Error() {
		RiskSummary riskSummary = new RiskSummary();
		riskSummary.setReferenceId("P00010");
		riskSummary.setBehaviour("Growth");
		int result = calcDaoImpl.updateRiskSummary(riskSummary);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeInsuranceByRefId_Success() {
		int result = calcDaoImpl.removeInsuranceByRefId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeInsuranceByRefId_Error() {
		int result = calcDaoImpl.removeInsuranceByRefId("P00044");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removePriorityByRefIdAndItemId_Success() {
		int result = calcDaoImpl.removePriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removePriorityByRefIdAndItemId_Error() {
		int result = calcDaoImpl.removePriorityByRefIdAndItemId("P00044", 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchPlanReferenceId_Success() {
		String ref = calcDaoImpl.fetchPlanReferenceId();
		Assert.assertEquals("P00000", ref);
	}

	@Test
	public void test_addPlanReferenceId() {
		int result = calcDaoImpl.addPlanReferenceId("P00000");
		Assert.assertEquals(1, result);
	}

	// @Test //Encode decode
	// public void test_fetchPlanByPartyId_Success() {
	// String encryptPass ="Sowise@Ever21";
	// List<Plan> plan = calcDaoImpl.fetchPlanByPartyId(1, 1, 3,encryptPass);
	// Assert.assertEquals(1, plan.size());
	// Assert.assertEquals(25, plan.get(0).getAge());
	// }

	// @Test //Encode decode
	// public void test_fetchPlanByPartyId_Error() {
	// String encryptPass ="Sowise@Ever21";
	// List<Plan> plan = calcDaoImpl.fetchPlanByPartyId(2, 1, 1,encryptPass);
	// Assert.assertEquals(0, plan.size());
	// }

	// @Test //Encode decode
	// public void test_modifyPlanInfo_Success() {
	// String encryptPass ="Sowise@Ever21";
	// Plan plan = new Plan();
	// plan.setReferenceId("P00000");
	// plan.setAge(24);
	// plan.setPartyId(1);
	// plan.setParentPartyId(1);
	// int result = calcDaoImpl.modifyPlanInfo(plan, "P00000",encryptPass);
	// Assert.assertEquals(1, result);
	// }

	@Test
	public void test_modifyPlanInfo_Error() {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = "Sowise@Ever21";
		Plan plan = new Plan();
		plan.setReferenceId("P00000");
		plan.setAge(24);
		plan.setPartyId(1);
		plan.setParentPartyId(1);
		plan.setUpdated(timestamp);
		plan.setUpdated_by("ADV0000000001");
		int result = calcDaoImpl.modifyPlanInfo(plan, "P00044", encryptPass);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removePlanInfo_Success() {
		int result = calcDaoImpl.removePlanInfo("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removePlanInfo_Error() {
		int result = calcDaoImpl.removePlanInfo("P00044");
		Assert.assertEquals(0, result);
	}

	// @Test //Encode decode
	// public void test_fetchPlanBySuperParentId_Success() {
	// String encryptPass ="Sowise@Ever21";
	// List<Plan> plan = calcDaoImpl.fetchPlanBySuperParentId(1,encryptPass);
	// Assert.assertEquals(1, plan.size());
	// Assert.assertEquals(25, plan.get(0).getAge());
	// }

	// @Test //Encode decode
	// public void test_fetchPlanBySuperParentId_Error() {
	// String encryptPass ="Sowise@Ever21";
	// List<Plan> plan = calcDaoImpl.fetchPlanBySuperParentId(2,encryptPass);
	// Assert.assertEquals(0, plan.size());
	// }

	// @Test //Encode decode
	// public void test_fetchEmailIdByPartyId_Success() {
	// String encryptPass ="Sowise@Ever21";
	// String ref = calcDaoImpl.fetchEmailIdByPartyId(1,encryptPass);
	// Assert.assertEquals("abc@gmail.com", ref);
	// }

	// @Test //Encode decode
	// public void test_fetchEmailIdByPartyId_Error() {
	// String encryptPass ="Sowise@Ever21";
	// String ref = calcDaoImpl.fetchEmailIdByPartyId(2,encryptPass);
	// Assert.assertEquals(null, ref);
	// }

	@Test
	public void test_removeCashFlow_Success() {
		calcDaoImpl.removeCashFlow("P00000");
		List<CashFlow> cashFlow = calcDaoImpl.fetchCashFlowByRefIdAndTypeId("P00000", 1);
		Assert.assertEquals(0, cashFlow.size());

	}

	@Test
	public void test_removeNetworth_Success() {
		calcDaoImpl.removeNetworth("P00000");
		Networth networth = calcDaoImpl.fetchNetworthByRefIdAndEntryId("P00000", 1);
		Assert.assertEquals(null, networth);
	}

	@Test
	public void test_removePriority_Success() {
		calcDaoImpl.removePriority("P00000");
		Priority priority = calcDaoImpl.fetchPriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(null, priority);
	}

	@Test
	public void test_removeEmiCalculator_Success() {
		calcDaoImpl.removeEmiCalculator("P00000");
		EmiCalculator emiCalculator = calcDaoImpl.fetchEmiCalculatorByRefId("P00000");
		Assert.assertEquals(null, emiCalculator);
	}

	@Test
	public void test_removeEmiCapacity_Success() {
		calcDaoImpl.removeEmiCapacity("P00000");
		EmiCapacity emiCapacity = calcDaoImpl.fetchEmiCapacityByRefId("P00000");
		Assert.assertEquals(null, emiCapacity);
	}
	@Test
	public void test_removeFutureValue_Success() {
		calcDaoImpl.removeFutureValue("P00000");
		FutureValue futureValue = calcDaoImpl.fetchFutureValueByRefId("P00000");
		Assert.assertEquals(null, futureValue);
	}
	@Test
	public void test_removeTargetValue_Success() {
		calcDaoImpl.removeTargetValue("P00000");
		TargetValue targetValue = calcDaoImpl.fetchTargetValueByRefId("P00000");
		Assert.assertEquals(null, targetValue);
	}

	@Test
	public void test_removeGoal_Success() {
		calcDaoImpl.removeGoal("P00000");
		List<Goal> goal = calcDaoImpl.fetchGoalByReferenceId("P00000");
		Assert.assertEquals(0, goal.size());
	}

	@Test
	public void test_removeRiskProfile_Success() {
		calcDaoImpl.removeRiskProfile("P00000");
		List<RiskProfile> riskProfile = calcDaoImpl.fetchRiskProfileByRefId("P00000");
		Assert.assertEquals(0, riskProfile.size());
	}

	@Test
	public void test_checkPlanIsPresentByReferenceId() {
		int result = calcDaoImpl.checkPlanIsPresentByReferenceId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkPlanIsPresentByReferenceId_Error() {
		int result = calcDaoImpl.checkPlanIsPresentByReferenceId("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkGoalIsPresentByRefIdAndGoalName() {
		int result = calcDaoImpl.checkGoalIsPresentByRefIdAndGoalName("P00000", "Education");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkGoalIsPresentByRefIdAndGoalName_Error() {
		int result = calcDaoImpl.checkGoalIsPresentByRefIdAndGoalName("P00000", "Travel");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkCashFlowIsPresent() {
		int result = calcDaoImpl.checkCashFlowIsPresent("P00000", 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkCashFlowIsPresent_Error() {
		int result = calcDaoImpl.checkCashFlowIsPresent("P00001", 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkCashFlowSummaryIsPresent() {
		int result = calcDaoImpl.checkCashFlowSummaryIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkCashFlowSummaryIsPresent_Error() {
		int result = calcDaoImpl.checkCashFlowSummaryIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkNetworthIsPresent() {
		int result = calcDaoImpl.checkNetworthIsPresent("P00000", 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkNetworthIsPresent_Error() {
		int result = calcDaoImpl.checkNetworthIsPresent("P00001", 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkNetworthSummaryIsPresent() {
		int result = calcDaoImpl.checkNetworthSummaryIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkNetworthSummaryIsPresent_Error() {
		int result = calcDaoImpl.checkNetworthSummaryIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkPriorityByRefIdAndItemId() {
		int result = calcDaoImpl.checkPriorityByRefIdAndItemId("P00000", 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkPriorityByRefIdAndItemId_Error() {
		int result = calcDaoImpl.checkPriorityByRefIdAndItemId("P00001", 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkInsuranceByRefId() {
		int result = calcDaoImpl.checkInsuranceByRefId("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkInsuranceByRefId_Error() {
		int result = calcDaoImpl.checkInsuranceByRefId("P000001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkRiskProfileIsPresent() {
		int result = calcDaoImpl.checkRiskProfileIsPresent("P00000", "1");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkRiskProfileIsPresent_Error() {
		int result = calcDaoImpl.checkRiskProfileIsPresent("P00001", "1");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkRiskSummaryIsPresent() {
		int result = calcDaoImpl.checkRiskSummaryIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkRiskSummaryIsPresent_Error() {
		int result = calcDaoImpl.checkRiskSummaryIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkEmiCalculatorIsPresent() {
		int result = calcDaoImpl.checkEmiCalculatorIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkEmiCalculatorIsPresent_Error() {
		int result = calcDaoImpl.checkEmiCalculatorIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkemiCapacityIsPresent() {
		int result = calcDaoImpl.checkemiCapacityIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkemiCapacityIsPresent_Error() {
		int result = calcDaoImpl.checkemiCapacityIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkpartialIsPresent() {
		int result = calcDaoImpl.checkpartialIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkpartialIsPresent_Error() {
		int result = calcDaoImpl.checkpartialIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkEmiChangeIsPresent() {
		int result = calcDaoImpl.checkEmiChangeIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkEmiChangeIsPresent_Error() {
		int result = calcDaoImpl.checkEmiChangeIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkInterestChangeIsPresent() {
		int result = calcDaoImpl.checkInterestChangeIsPresent("P00000");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkInterestChangeIsPresent_Error() {
		int result = calcDaoImpl.checkInterestChangeIsPresent("P00001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_checkSharedAdvisor_Success() {
		int result = calcDaoImpl.checkSharedAdvisor("P0000", 2, "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkSharedAdvisor_Error() {
		int result = calcDaoImpl.checkSharedAdvisor("P0000", 5, "N");
		Assert.assertEquals(0, result);
	}

}
