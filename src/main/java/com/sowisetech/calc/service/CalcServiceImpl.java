package com.sowisetech.calc.service;

import java.security.Security;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.advisor.dao.AdvisorDao;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.dao.CalcDao;
import com.sowisetech.calc.model.Account;
import com.sowisetech.calc.model.AccountType;
import com.sowisetech.calc.model.Advisor;
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
import com.sowisetech.calc.model.Fund;
import com.sowisetech.calc.model.FutureValue;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.Insurance;
import com.sowisetech.calc.model.InsuranceAmountItem;
import com.sowisetech.calc.model.InsuranceItem;
import com.sowisetech.calc.model.InsuranceStringItem;
import com.sowisetech.calc.model.InterestChange;
import com.sowisetech.calc.model.Networth;
import com.sowisetech.calc.model.NetworthSummary;
import com.sowisetech.calc.model.PartialPayment;
import com.sowisetech.calc.model.Party;
import com.sowisetech.calc.model.Plan;
import com.sowisetech.calc.model.Priority;
import com.sowisetech.calc.model.PriorityItem;
import com.sowisetech.calc.model.Queries;
import com.sowisetech.calc.model.RateFinder;
import com.sowisetech.calc.model.RiskPortfolio;
import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskQuestionaire;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.TenureFinder;
import com.sowisetech.calc.model.Urgency;
import com.sowisetech.calc.request.SharedRequest;
import com.sowisetech.calc.response.CalcResponse;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcTableFields;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.investor.model.Investor;

@Service("CalcService")
@Transactional(readOnly = true)
public class CalcServiceImpl implements CalcService {

	@Autowired
	private CalcDao calcDao;

	@Autowired
	private AdvisorDao advisorDao;
	@Autowired
	private CalcTableFields calcTableFields;
	@Autowired
	private AdvTableFields advTableFields;
	@Autowired
	AdminSignin adminSignin;

	// FutureCost
	@Transactional
	public double calculateGoalFutureCost(double goalAmt, double inflationRate, int tenure) {
		double futureCost = goalAmt * Math.pow((1 + inflationRate), tenure);
		return futureCost;
	}

	// Current Investment
	@Transactional
	public double calculateGoalCurrentInvestment(double currentAmt, double growthRate, int tenure) {
		double futureValue = currentAmt * Math.pow((1 + growthRate), tenure);
		return futureValue;
	}

	// Final Corpus
	@Transactional
	public double calculateGoalFinalCorpus(double futureCost, double futureValue) {
		double finalCorpus = futureCost - futureValue;
		return finalCorpus;
	}

	@Transactional
	public double calculateGoalMonthlyInvestment(double growthRate, double annualInvRate, double finalCorpus,
			int tenure, double returnRate) {
		double monthlyInvestment = 0;
		if (growthRate == annualInvRate) {
			monthlyInvestment = finalCorpus / (12 * tenure * Math.pow((1 + returnRate), (tenure * 12)));
		} else {
			monthlyInvestment = (finalCorpus * (returnRate - (annualInvRate / 12)))
					/ ((1 + returnRate) * ((Math.pow((1 + returnRate), (tenure * 12)))
							- (Math.pow((1 + (annualInvRate / 12)), (tenure * 12)))));
		}
		return monthlyInvestment;
	}

	@Transactional
	public double calculateGoalAnnualyInvestment(double growthRate, double annualInvRate, double finalCorpus,
			int tenure) {
		double annualInv = 0;
		if (growthRate == annualInvRate) {
			annualInv = finalCorpus / tenure * Math.pow((1 + growthRate), tenure);
		} else {
			annualInv = (finalCorpus * (growthRate - annualInvRate))
					/ ((1 + growthRate) * (Math.pow((1 + growthRate), tenure) - Math.pow((1 + annualInvRate), tenure)));
		}
		return annualInv;
	}

	@Transactional
	public int addGoalInfo(Goal goal) {
		String signedUserId = getSignedInUser();
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		goal.setCreated_by(signedUserId);
		goal.setUpdated_by(signedUserId);
		goal.setCreated(timestamp);
		goal.setUpdated(timestamp);
		return calcDao.addGoalInfo(goal);
	}

	@Transactional
	public int fetchCashFlowItemTypeIdByItemId(long cashFlowItemId) {
		return calcDao.fetchCashFlowItemTypeIdByItemId(cashFlowItemId);
	}

	@Transactional
	public int addAndModifyCashFlow(List<CashFlow> cashFlowList) {
		int result1 = 0;
		for (CashFlow cashFlow : cashFlowList) {
			int cashFlowItemTypeId = calcDao.fetchCashFlowItemTypeIdByItemId(cashFlow.getCashFlowItemId());
			cashFlow.setCashFlowItemTypeId(cashFlowItemTypeId);
			if (calcDao.checkCashFlowIsPresent(cashFlow.getReferenceId(), cashFlow.getCashFlowItemId()) == 0) {
				// if partyId and cashFlowItemId combination is not available,then add as new
				// record
				String signedUserId = getSignedInUser();
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				cashFlow.setCreated(timestamp);
				cashFlow.setUpdated(timestamp);
				cashFlow.setCreated_by(signedUserId);
				cashFlow.setUpdated_by(signedUserId);
				result1 = calcDao.addCashFlow(cashFlow);
			} else {
				// if partyId and cashFlowItemId combination is already present,then update it.
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				String signedUserId = getSignedInUser();
				cashFlow.setUpdated_by(signedUserId);
				cashFlow.setUpdated(timestamp);
				result1 = calcDao.updateCashFlow(cashFlow);
			}
			if (result1 == 0) {
				return result1;
			}
		}
		return result1;
	}

	@Transactional
	public int addCashFlow(CashFlow cashFlow) {
		String signedUserId = getSignedInUser();
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		cashFlow.setCreated(timestamp);
		cashFlow.setUpdated(timestamp);
		cashFlow.setCreated_by(signedUserId);
		cashFlow.setUpdated_by(signedUserId);
		return calcDao.addCashFlow(cashFlow);
	}

	@Transactional
	public List<CashFlow> fetchCashFlowByRefIdAndTypeId(String referenceId, int typeId) {
		return calcDao.fetchCashFlowByRefIdAndTypeId(referenceId, typeId);
	}

	@Transactional
	public List<Integer> fetchRecurringExpenditureItemType() {
		int typeId1, typeId2, typeId3, typeId4;
		typeId1 = calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getMandatory_household_expense());
		typeId2 = calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getLife_style_expenses());
		typeId3 = calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getRecurring_loan_repayments());
		typeId4 = calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getRecurring_investments());
		List<Integer> cashFlowItemTypeId = new ArrayList<Integer>();
		cashFlowItemTypeId.add(typeId1);
		cashFlowItemTypeId.add(typeId2);
		cashFlowItemTypeId.add(typeId3);
		cashFlowItemTypeId.add(typeId4);
		return cashFlowItemTypeId;
	}

	// @Transactional
	// public List<CashFlow> fetchNonRecurringExpenditureByRefId(String referenceId)
	// {
	// int cashFlowItemTypeId = calcDao
	// .fetchCashFlowItemTypeIdByItemType(calcTableFields.getNon_recurring_expenditures());
	// return calcDao.fetchCashFlowByRefIdAndTypeId(referenceId,
	// cashFlowItemTypeId);
	// }

	@Transactional
	public List<CashFlow> fetchRecurringIncomeByRefId(String referenceId) {
		int cashFlowItemTypeId = calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getRecurring_income());
		return calcDao.fetchCashFlowByRefIdAndTypeId(referenceId, cashFlowItemTypeId);
	}

	// @Transactional
	// public List<CashFlow> fetchNonRecurringIncomeByRefId(String referenceId) {
	// int cashFlowItemTypeId =
	// calcDao.fetchCashFlowItemTypeIdByItemType(calcTableFields.getNon_recurring_income());
	// return calcDao.fetchCashFlowByRefIdAndTypeId(referenceId,
	// cashFlowItemTypeId);
	// }

	@Transactional
	public int addCashFlowSummary(CashFlowSummary cashFlowSummary) {
		String signedUserId = getSignedInUser();
		cashFlowSummary.setCreated_by(signedUserId);
		cashFlowSummary.setUpdated_by(signedUserId);
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		cashFlowSummary.setCreated(timestamp);
		cashFlowSummary.setUpdated(timestamp);
		return calcDao.addCashFlowSummary(cashFlowSummary);
	}

	@Transactional
	public int addAndModifyNetworth(List<Networth> networthList) {
		int result = 0;
		for (Networth networth : networthList) {
			int accountTypeId = calcDao.fetchAccountTypeIdByEntryId(networth.getAccountEntryId());
			networth.setAccountTypeId(accountTypeId);
			if (calcDao.checkNetworthIsPresent(networth.getReferenceId(), networth.getAccountEntryId()) == 0) {
				// if partyId and accountEntryId combination is not available, then add as new
				// record
				String signedUserId = getSignedInUser();
				networth.setCreated_by(signedUserId);
				networth.setUpdated_by(signedUserId);
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				networth.setCreated(timestamp);
				networth.setUpdated(timestamp);
				result = calcDao.addNetworth(networth);
			} else {
				// if partyId and accountEntryId combination is already present, then update it
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				String signedUserId = getSignedInUser();
				networth.setUpdated(timestamp);
				networth.setUpdated_by(signedUserId);
				result = calcDao.updateNetworth(networth);
			}
			if (result == 0) {
				return result;
			}
		}

		return result;
	}

	@Transactional
	public int addNetworth(Networth networth) {
		String signedUserId = getSignedInUser();
		networth.setCreated_by(signedUserId);
		networth.setUpdated_by(signedUserId);
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		networth.setCreated(timestamp);
		networth.setUpdated(timestamp);
		return calcDao.addNetworth(networth);
	}

	@Transactional
	public int fetchAccountTypeIdByEntryId(int accountEntryId) {
		return calcDao.fetchAccountTypeIdByEntryId(accountEntryId);
	}

	@Transactional
	public List<Networth> fetchNetworthByAssets(String referenceId) {
		int typeId = calcDao.fetchAccountTypeIdByAccountType(calcTableFields.getAssets());
		return calcDao.fetchNetworthByAccountTypeIdAndRefId(typeId, referenceId);
	}

	@Transactional
	public List<Networth> fetchNetworthByLiabilities(String referenceId) {
		int typeId = calcDao.fetchAccountTypeIdByAccountType(calcTableFields.getLiabilities());
		return calcDao.fetchNetworthByAccountTypeIdAndRefId(typeId, referenceId);
	}

	@Transactional
	public int addNetworthSummary(NetworthSummary networthSummary) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		networthSummary.setCreated(timestamp);
		networthSummary.setUpdated(timestamp);
		networthSummary.setCreated_by(signedUserId);
		networthSummary.setUpdated_by(signedUserId);
		return calcDao.addNetworthSummary(networthSummary);

	}

	@Transactional
	public int addInsurance(Insurance insurance) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		insurance.setCreated(timestamp);
		insurance.setUpdated(timestamp);
		insurance.setCreated_by(signedUserId);
		insurance.setUpdated_by(signedUserId);
		return calcDao.addInsurance(insurance);
	}

	@Transactional
	public int addAndModifyPriorities(List<Priority> priorityList) {
		int result = 0;
		for (Priority priority : priorityList) {
			if (calcDao.checkPriorityByRefIdAndItemId(priority.getReferenceId(), priority.getPriorityItemId()) == 0) {
				// If priorityItemId and partyId combination is not present, then add as new
				// record
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				String signedUserId = getSignedInUser();
				priority.setCreated(timestamp);
				priority.setUpdated(timestamp);
				priority.setCreated_by(signedUserId);
				priority.setUpdated_by(signedUserId);
				result = calcDao.addPriorities(priority);

			} else {
				// If priorityItemId and partyId combination is present, then update it
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				String signedUserId = getSignedInUser();
				priority.setUpdated(timestamp);
				priority.setUpdated_by(signedUserId);
				if (priority.getUrgencyId() == 4) {
					result = calcDao.removePriority(priority);
				} else {
					result = calcDao.updatePriority(priority);
				}
			}
		}
		return result;
	}

	@Transactional
	public int addPriorities(Priority priority) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		priority.setCreated(timestamp);
		priority.setUpdated(timestamp);
		priority.setCreated_by(signedUserId);
		priority.setUpdated_by(signedUserId);
		return calcDao.addPriorities(priority);
	}

	@Transactional
	public List<Priority> fetchPriorityByRefId(String referenceId) {
		return calcDao.fetchPriorityByRefId(referenceId);
	}

	@Transactional
	public Priority fetchPriorityByRefIdAndItemId(String referenceId, int priorityItemId) {
		return calcDao.fetchPriorityByRefIdAndItemId(referenceId, priorityItemId);
	}

	@Transactional
	public int updatePriorityOrder(String referenceId, int priorityItemId, int order) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		return calcDao.updatePriorityOrder(referenceId, priorityItemId, order, signedUserId, timestamp);
		// return calcDao.updatePriorityOrder(referenceId, priorityItemId, order,
		// timestamp);

	}

	@Transactional
	public Party fetchParty(long partyId) {
		String delete_flag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchParty(partyId, delete_flag, encryptPass);
	}

	@Transactional
	public int addAndModifyRiskProfile(List<RiskProfile> riskProfilelist) {
		int result = 0;
		// System.out.println("size " +riskProfilelist.size());
		for (RiskProfile riskProfile : riskProfilelist) {
			int score = calcDao.fetchScoreByAnswerId(riskProfile);
			if (calcDao.checkRiskProfileIsPresent(riskProfile.getReferenceId(), riskProfile.getQuestionId()) == 0) {
				// System.out.println("add " +riskProfile.toString());
				// If partyId and questionId combination is not present, then add as new record
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				String signedUserId = getSignedInUser();
				riskProfile.setCreated(timestamp);
				riskProfile.setUpdated(timestamp);
				riskProfile.setCreated_by(signedUserId);
				riskProfile.setUpdated_by(signedUserId);
				riskProfile.setScore(score);
				result = calcDao.addRiskProfile(riskProfile);
			} else {
				// System.out.println("update ---" + riskProfile.toString());
				// If partyId and questionId combination is already present, then update it
				riskProfile.setScore(score);
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
				String signedUserId = getSignedInUser();
				riskProfile.setUpdated(timestamp);
				riskProfile.setUpdated_by(signedUserId);
				result = calcDao.updateRiskProfile(riskProfile);
			}
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public int addRiskProfile(RiskProfile riskProfile) {
		int score = calcDao.fetchScoreByAnswerId(riskProfile);
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		riskProfile.setCreated(timestamp);
		riskProfile.setUpdated(timestamp);
		riskProfile.setCreated_by(signedUserId);
		riskProfile.setUpdated_by(signedUserId);
		riskProfile.setScore(score);
		return calcDao.addRiskProfile(riskProfile);
	}

	@Transactional
	public List<RiskProfile> fetchRiskProfileByRefId(String referenceId) {
		return calcDao.fetchRiskProfileByRefId(referenceId);
	}

	@Transactional
	public RiskPortfolio fetchRiskPortfolioByPoints(String points) {
		return calcDao.fetchRiskPortfolioByPoints(points);
	}

	@Transactional
	public int addRiskSummary(RiskSummary riskSummary) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		riskSummary.setCreated(timestamp);
		riskSummary.setUpdated(timestamp);
		riskSummary.setCreated_by(signedUserId);
		riskSummary.setUpdated_by(signedUserId);
		return calcDao.addRiskSummary(riskSummary);
	}

	@Transactional
	public List<AccountType> fetchAccountTypeList() {
		return calcDao.fetchAccountTypeList();
	}

	@Transactional
	public List<Account> fetchAccountList() {
		return calcDao.fetchAccountList();
	}

	@Transactional
	public List<CashFlowItemType> fetchCashFlowItemTypeList() {
		return calcDao.fetchCashFlowItemTypeList();
	}

	@Transactional
	public List<CashFlowItem> fetchCashFlowItemList() {
		return calcDao.fetchCashFlowItemList();
	}

	@Transactional
	public List<PriorityItem> fetchPriorityItemList() {
		return calcDao.fetchPriorityItemList();
	}

	@Transactional
	public List<Urgency> fetchUrgencyList() {
		return calcDao.fetchUrgencyList();
	}

	@Transactional
	public List<RiskPortfolio> fetchRiskPortfolioList() {
		return calcDao.fetchRiskPortfolioList();
	}

	@Transactional
	public List<RiskQuestionaire> fetchRiskQuestionaireList() {
		return calcDao.fetchRiskQuestionaireList();
	}

	@Transactional
	public Party fetchPartyIdByRoleBasedId(String id) {
		String delete_flag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchPartyIdByRoleBasedId(id, delete_flag, encryptPass);
	}

	@Transactional
	public List<Goal> fetchGoalByReferenceId(String referenceId) {
		return calcDao.fetchGoalByReferenceId(referenceId);
	}

	@Transactional
	public List<CashFlow> fetchCashFlowByRefId(String referenceId) {
		return calcDao.fetchCashFlowByRefId(referenceId);
	}

	@Transactional
	public CashFlowSummary fetchCashFlowSummaryByRefId(String referenceId) {
		return calcDao.fetchCashFlowSummaryByRefId(referenceId);
	}

	@Transactional
	public List<Networth> fetchNetworthByRefId(String referenceId) {
		return calcDao.fetchNetworthByRefId(referenceId);
	}

	@Transactional
	public NetworthSummary fetchNetworthSummaryByRefId(String referenceId) {
		return calcDao.fetchNetworthSummaryByRefId(referenceId);
	}

	@Transactional
	public Insurance fetchInsuranceByRefId(String referenceId) {
		return calcDao.fetchInsuranceByRefId(referenceId);
	}

	@Transactional
	public RiskSummary fetchRiskSummaryByRefId(String referenceId) {
		return calcDao.fetchRiskSummaryByRefId(referenceId);
	}

	@Transactional
	public int addEmiCalculator(EmiCalculator emiCalculator) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		emiCalculator.setCreated(timestamp);
		emiCalculator.setUpdated(timestamp);
		emiCalculator.setCreated_by(signedUserId);
		emiCalculator.setUpdated_by(signedUserId);
		return calcDao.addEmiCalculator(emiCalculator);
	}

	@Transactional
	public int addEmiCapacity(EmiCapacity emiCapacity) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		emiCapacity.setCreated(timestamp);
		emiCapacity.setUpdated(timestamp);
		emiCapacity.setCreated_by(signedUserId);
		emiCapacity.setUpdated_by(signedUserId);
		return calcDao.addEmiCapacity(emiCapacity);
	}

	@Transactional
	public int addPartialPayment(PartialPayment partialPayment) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		partialPayment.setCreated(timestamp);
		partialPayment.setUpdated(timestamp);
		partialPayment.setCreated_by(signedUserId);
		partialPayment.setUpdated_by(signedUserId);
		return calcDao.addPartialPayment(partialPayment);
	}

	@Transactional
	public int addInterestChange(InterestChange interestChange) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		interestChange.setCreated(timestamp);
		interestChange.setUpdated(timestamp);
		interestChange.setCreated_by(signedUserId);
		interestChange.setUpdated_by(signedUserId);
		return calcDao.addInterestChange(interestChange);
	}

	@Transactional
	public int addEmiChange(EmiChange emiChange) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		emiChange.setCreated(timestamp);
		emiChange.setUpdated(timestamp);
		emiChange.setCreated_by(signedUserId);
		emiChange.setUpdated_by(signedUserId);
		return calcDao.addEmiChange(emiChange);
	}

	@Transactional
	public int addEmiInterestChange(EmiInterestChange emiInterestChange) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		emiInterestChange.setCreated(timestamp);
		emiInterestChange.setUpdated(timestamp);
		emiInterestChange.setCreated_by(signedUserId);
		emiInterestChange.setUpdated_by(signedUserId);
		return calcDao.addEmiInterestChange(emiInterestChange);
	}

	@Transactional
	public EmiCalculator fetchEmiCalculatorByRefId(String referenceId) {
		return calcDao.fetchEmiCalculatorByRefId(referenceId);
	}

	@Transactional
	public EmiCapacity fetchEmiCapacityByRefId(String referenceId) {
		return calcDao.fetchEmiCapacityByRefId(referenceId);
	}

	@Transactional
	public List<PartialPayment> fetchPartialPaymentByRefId(String referenceId) {
		return calcDao.fetchPartialPaymentByRefId(referenceId);
	}

	@Transactional
	public List<InterestChange> fetchInterestChangeByRefId(String referenceId) {
		return calcDao.fetchInterestChangeByRefId(referenceId);
	}

	@Transactional
	public List<EmiChange> fetchEmiChangeByRefId(String referenceId) {
		return calcDao.fetchEmiChangeByRefId(referenceId);
	}

	@Transactional
	public List<EmiInterestChange> fetchEmiInterestChangeByRefId(String referenceId) {
		return calcDao.fetchEmiInterestChangeByRefId(referenceId);
	}

	@Transactional
	public RiskProfile fetchRiskProfileByRefIdAndAnswerId(String referenceId, long answerId) {
		return calcDao.fetchRiskProfileByRefIdAndAnswerId(referenceId, answerId);
	}

	@Transactional
	public Networth fetchNetworthByRefIdAndEntryId(String referenceId, long accountEntryId) {
		return calcDao.fetchNetworthByRefIdAndEntryId(referenceId, accountEntryId);
	}

	@Transactional
	public AccountType fetchAccountTypeByTypeId(long accountTypeId) {
		return calcDao.fetchAccountTypeByTypeId(accountTypeId);
	}

	@Transactional
	public CashFlow fetchCashFlowByRefIdAndItemId(String referenceId, long cashFlowItemId) {
		return calcDao.fetchCashFlowByRefIdAndItemId(referenceId, cashFlowItemId);
	}

	@Transactional
	public CashFlowItemType fetchCashFlowItemTypeByTypeId(long cashFlowItemTypeId) {
		return calcDao.fetchCashFlowItemTypeByTypeId(cashFlowItemTypeId);
	}

	@Transactional
	public InsuranceItem fetchInsuranceItemByRefId(String referenceId) {
		Insurance insurance = fetchInsuranceByRefId(referenceId);
		InsuranceItem insuranceItem = new InsuranceItem();
		InsuranceAmountItem annualIncome = new InsuranceAmountItem();
		InsuranceStringItem stability = new InsuranceStringItem();
		InsuranceStringItem predictability = new InsuranceStringItem();
		InsuranceAmountItem requiredInsurance = new InsuranceAmountItem();
		InsuranceAmountItem existingInsurance = new InsuranceAmountItem();
		InsuranceAmountItem additionalInsurance = new InsuranceAmountItem();
		insuranceItem.setReferenceId(referenceId);
		if (insurance != null) {
			insuranceItem.setInsuranceId(insurance.getInsuranceId());
			annualIncome.setValue(insurance.getAnnualIncome());
			stability.setValue(insurance.getStability());
			predictability.setValue(insurance.getPredictability());
			requiredInsurance.setValue(insurance.getRequiredInsurance());
			existingInsurance.setValue(insurance.getExistingInsurance());
			additionalInsurance.setValue(insurance.getAdditionalInsurance());
		}
		String annualIncomeLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getAnnualIncome());
		annualIncome.setLabel(annualIncomeLabel);
		insuranceItem.setAnnualIncome(annualIncome);
		String stabilityLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getStability());
		stability.setLabel(stabilityLabel);
		insuranceItem.setStability(stability);
		String predictabilityLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getPredictability());
		predictability.setLabel(predictabilityLabel);
		insuranceItem.setPredictability(predictability);
		String requiredInsuranceLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getRequiredInsurance());
		requiredInsurance.setLabel(requiredInsuranceLabel);
		insuranceItem.setRequiredInsurance(requiredInsurance);
		String existingInsuranceLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getExistingInsurance());
		existingInsurance.setLabel(existingInsuranceLabel);
		insuranceItem.setExistingInsurance(existingInsurance);
		String additionalInsuranceLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getAdditionalInsurance());
		additionalInsurance.setLabel(additionalInsuranceLabel);
		insuranceItem.setAdditionalInsurance(additionalInsurance);
		return insuranceItem;
	}

	@Transactional
	public InsuranceItem fetchInsuranceItemWithoutLogin(Insurance insurance) {
		InsuranceItem insuranceItem = new InsuranceItem();
		InsuranceAmountItem annualIncome = new InsuranceAmountItem();
		InsuranceStringItem stability = new InsuranceStringItem();
		InsuranceStringItem predictability = new InsuranceStringItem();
		InsuranceAmountItem requiredInsurance = new InsuranceAmountItem();
		InsuranceAmountItem existingInsurance = new InsuranceAmountItem();
		InsuranceAmountItem additionalInsurance = new InsuranceAmountItem();
		if (insurance != null) {
			insuranceItem.setInsuranceId(insurance.getInsuranceId());
			annualIncome.setValue(insurance.getAnnualIncome());
			stability.setValue(insurance.getStability());
			predictability.setValue(insurance.getPredictability());
			requiredInsurance.setValue(insurance.getRequiredInsurance());
			existingInsurance.setValue(insurance.getExistingInsurance());
			additionalInsurance.setValue(insurance.getAdditionalInsurance());
		}
		String annualIncomeLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getAnnualIncome());
		annualIncome.setLabel(annualIncomeLabel);
		insuranceItem.setAnnualIncome(annualIncome);
		String stabilityLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getStability());
		stability.setLabel(stabilityLabel);
		insuranceItem.setStability(stability);
		String predictabilityLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getPredictability());
		predictability.setLabel(predictabilityLabel);
		insuranceItem.setPredictability(predictability);
		String requiredInsuranceLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getRequiredInsurance());
		requiredInsurance.setLabel(requiredInsuranceLabel);
		insuranceItem.setRequiredInsurance(requiredInsurance);
		String existingInsuranceLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getExistingInsurance());
		existingInsurance.setLabel(existingInsuranceLabel);
		insuranceItem.setExistingInsurance(existingInsurance);
		String additionalInsuranceLabel = calcDao.fetchValueByInsuranceItem(calcTableFields.getAdditionalInsurance());
		additionalInsurance.setLabel(additionalInsuranceLabel);
		insuranceItem.setAdditionalInsurance(additionalInsurance);
		return insuranceItem;
	}

	@Transactional
	public int addPlanInfo(Plan plan) {
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		plan.setCreated_by(signedUserId);
		plan.setUpdated_by(signedUserId);
		plan.setCreated(timestamp);
		plan.setUpdated(timestamp);
		return calcDao.addPlanInfo(plan, encryptPass);
	}

	private String getSignedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
			return adminSignin.getAdmin_name();
		} else {
			Party party = calcDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
			return party.getRoleBasedId();
		}
	}

	@Transactional
	public String fetchRoleBasedIdByPartyId(long partyId) {
		return calcDao.fetchRoleBasedIdByPartyId(partyId);
	}

	@Transactional
	public Plan fetchPlanByReferenceId(String id) {
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchPlanByReferenceId(id, encryptPass);
	}

	@Transactional
	public int updateCashFlow(CashFlow cashFlow) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		cashFlow.setUpdated_by(signedUserId);
		cashFlow.setUpdated(timestamp);
		return calcDao.updateCashFlow(cashFlow);
	}

	@Transactional
	public int removeCashFlowSummary(String referenceId) {
		return calcDao.removeCashFlowSummary(referenceId);
	}

	@Transactional
	public int updateNetworth(Networth networth) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		networth.setUpdated(timestamp);
		networth.setUpdated_by(signedUserId);
		return calcDao.updateNetworth(networth);
	}

	@Transactional
	public int removeNetworthSummary(String referenceId) {
		return calcDao.removeNetworthSummary(referenceId);
	}

	@Transactional
	public int updatePriority(Priority priority) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		priority.setUpdated(timestamp);
		priority.setUpdated_by(signedUserId);
		return calcDao.updatePriority(priority);
	}

	@Transactional
	public int updateInsurance(Insurance insurance) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		insurance.setUpdated(timestamp);
		insurance.setUpdated_by(signedUserId);
		return calcDao.updateInsurance(insurance);
	}

	@Transactional
	public int updateRiskProfile(RiskProfile riskProfile) {
		int score = calcDao.fetchScoreByAnswerId(riskProfile);
		riskProfile.setScore(score);
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		riskProfile.setUpdated(timestamp);
		riskProfile.setUpdated_by(signedUserId);
		return calcDao.updateRiskProfile(riskProfile);
	}

	@Transactional
	public int removeRiskSummary(String referenceId) {
		return calcDao.removeRiskSummary(referenceId);
	}

	@Transactional
	public int updateEmiCalculator(EmiCalculator emiCalculator) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		emiCalculator.setUpdated(timestamp);
		emiCalculator.setUpdated_by(signedUserId);
		return calcDao.updateEmiCalculator(emiCalculator);
	}

	@Transactional
	public int updateEmiCapacity(EmiCapacity emiCapacity) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		emiCapacity.setUpdated(timestamp);
		emiCapacity.setUpdated_by(signedUserId);
		return calcDao.updateEmiCapacity(emiCapacity);
	}

	@Transactional
	public RiskProfile fetchRiskProfileByRefIdAndQuestionId(String referenceId, String questionId) {
		return calcDao.fetchRiskProfileByRefIdAndQuestionId(referenceId, questionId);
	}

	@Transactional
	public List<String> fetchQuestionIdFromRiskQuestionaire() {
		return calcDao.fetchQuestionIdFromRiskQuestionaire();
	}

	@Transactional
	public List<RiskQuestionaire> fetchRiskQuestionaireByQuestionId(String questionId) {
		return calcDao.fetchRiskQuestionaireByQuestionId(questionId);
	}

	@Transactional
	public String fetchQuestionByQuestionId(String questionId) {
		return calcDao.fetchQuestionByQuestionId(questionId);
	}

	@Transactional
	public Goal fetchGoalByRefIdAndGoalName(String referenceId, String goalName) {
		return calcDao.fetchGoalByRefIdAndGoalName(referenceId, goalName);
	}

	@Transactional
	public int updateGoalInfo(Goal goal) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		goal.setUpdated(timestamp);
		goal.setUpdated_by(signedUserId);
		return calcDao.updateGoalInfo(goal);
	}

	@Transactional
	public int removePartialPaymentByRefId(String referenceId) {
		return calcDao.removePartialPaymentByRefId(referenceId);
	}

	@Transactional
	public int removeEmiChangeByRefId(String referenceId) {
		return calcDao.removeEmiChangeByRefId(referenceId);
	}

	@Transactional
	public int removeInterestChangeByRefId(String referenceId) {
		return calcDao.removeInterestChangeByRefId(referenceId);
	}

	@Transactional
	public int removeEmiInterestChangeByRefId(String referenceId) {
		return calcDao.removeEmiInterestChangeByRefId(referenceId);
	}

	@Transactional
	public int updateCashFlowSummary(CashFlowSummary cashFlowSummary) {
		String signedUserId = getSignedInUser();
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		cashFlowSummary.setUpdated_by(signedUserId);
		cashFlowSummary.setUpdated(timestamp);
		return calcDao.updateCashFlowSummary(cashFlowSummary);
	}

	@Transactional
	public int updateNetworthSummary(NetworthSummary networthSummary) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		networthSummary.setUpdated(timestamp);
		networthSummary.setUpdated_by(signedUserId);
		return calcDao.updateNetworthSummary(networthSummary);
	}

	@Transactional
	public int updateRiskSummary(RiskSummary riskSummary) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		riskSummary.setUpdated(timestamp);
		riskSummary.setUpdated_by(signedUserId);
		return calcDao.updateRiskSummary(riskSummary);
	}

	@Transactional
	public int removeInsuranceByRefId(String referenceId) {
		return calcDao.removeInsuranceByRefId(referenceId);
	}

	@Transactional
	public PriorityItem fetchPriorityItemByItemId(int itemId) {
		return calcDao.fetchPriorityItemByItemId(itemId);
	}

	@Transactional
	public int removePriorityByRefIdAndItemId(String referenceId, int priorityItemId) {
		return calcDao.removePriorityByRefIdAndItemId(referenceId, priorityItemId);
	}

	@Transactional
	public String generatePlanReferenceId() {
		String id = calcDao.fetchPlanReferenceId();
		if (id != null) {
			String newId = referenceIdIncrement(id);
			int result = calcDao.addPlanReferenceId(newId);
			if (result == 0) {
				return null;
			} else {
				return newId;
			}
		} else {
			String newId = "P0000000000";
			calcDao.addPlanReferenceId(newId);
			return newId;
		}
	}

	private String referenceIdIncrement(String id) {
		char initial = id.charAt(0);
		String subNumber = id.substring(1, 11);
		long number = Long.parseLong(subNumber);
		String num = String.format("%010d", number + 1);
		String newId = initial + num;
		return newId;
	}

	@Transactional
	public List<Plan> fetchPlanByPartyId(long partyId) {
		String encryptPass = advTableFields.getEncryption_password();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return calcDao.fetchPlanByPartyId(partyId, encryptPass);
	}

	@Transactional
	public int modifyPlanInfo(Plan plan, String referenceId) {
		String encryptPass = advTableFields.getEncryption_password();
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		plan.setUpdated_by(signedUserId);
		plan.setUpdated(timestamp);
		return calcDao.modifyPlanInfo(plan, referenceId, encryptPass);
	}

	@Transactional
	public int removePlanInfo(String referenceId) {
		calcDao.removeCashFlow(referenceId);
		calcDao.removeCashFlowSummary(referenceId);
		calcDao.removeNetworth(referenceId);
		calcDao.removeNetworthSummary(referenceId);
		calcDao.removePriority(referenceId);
		calcDao.removeInsuranceByRefId(referenceId);
		calcDao.removeEmiCalculator(referenceId);
		calcDao.removeEmiCapacity(referenceId);
		calcDao.removePartialPaymentByRefId(referenceId);
		calcDao.removeEmiChangeByRefId(referenceId);
		calcDao.removeInterestChangeByRefId(referenceId);
		calcDao.removeEmiInterestChangeByRefId(referenceId);
		calcDao.removeGoal(referenceId);
		calcDao.removeRiskProfile(referenceId);
		calcDao.removeRiskSummary(referenceId);
		calcDao.removeFutureValue(referenceId);
		calcDao.removeTargetValue(referenceId);
		calcDao.removeRateFinder(referenceId);
		calcDao.removeTenureFinder(referenceId);

		return calcDao.removePlanInfo(referenceId);
	}

	// @Transactional
	// public List<Plan> fetchPlanBySuperParentId(long superParentId) {
	// String encryptPass = advTableFields.getEncryption_password();
	// return calcDao.fetchPlanBySuperParentId(superParentId, encryptPass);
	//
	// }

	@Transactional
	public String fetchEmailIdByPartyId(long partyId) {
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchEmailIdByPartyId(partyId, encryptPass);
	}

	@Transactional
	public int checkPlanIsPresentByReferenceId(String referenceId) {
		return calcDao.checkPlanIsPresentByReferenceId(referenceId);
	}

	@Transactional
	public int checkGoalIsPresentByRefIdAndGoalName(String referenceId, String goalName) {
		return calcDao.checkGoalIsPresentByRefIdAndGoalName(referenceId, goalName);
	}

	@Transactional
	public int checkCashFlowIsPresent(String referenceId, int cashFlowItemId) {
		return calcDao.checkCashFlowIsPresent(referenceId, cashFlowItemId);
	}

	@Transactional
	public int checkCashFlowSummaryIsPresent(String referenceId) {
		return calcDao.checkCashFlowSummaryIsPresent(referenceId);
	}

	@Transactional
	public int checkNetworthIsPresent(String referenceId, int accountEntryId) {
		return calcDao.checkNetworthIsPresent(referenceId, accountEntryId);
	}

	@Transactional
	public int checkNetworthSummaryIsPresent(String referenceId) {
		return calcDao.checkNetworthSummaryIsPresent(referenceId);
	}

	@Transactional
	public int checkPriorityByRefIdAndItemId(String referenceId, int priorityItemId) {
		return calcDao.checkPriorityByRefIdAndItemId(referenceId, priorityItemId);
	}

	@Transactional
	public int checkInsuranceByRefId(String referenceId) {
		return calcDao.checkInsuranceByRefId(referenceId);
	}

	@Transactional
	public int checkRiskProfileIsPresent(String referenceId, String questionId) {
		return calcDao.checkRiskProfileIsPresent(referenceId, questionId);

	}

	@Transactional
	public int checkRiskSummaryIsPresent(String referenceId) {
		return calcDao.checkRiskSummaryIsPresent(referenceId);
	}

	@Transactional
	public int checkEmiCalculatorIsPresent(String referenceId) {
		return calcDao.checkEmiCalculatorIsPresent(referenceId);
	}

	@Transactional
	public int checkemiCapacityIsPresent(String referenceId) {
		return calcDao.checkemiCapacityIsPresent(referenceId);
	}

	@Transactional
	public int checkpartialIsPresent(String referenceId) {
		return calcDao.checkpartialIsPresent(referenceId);
	}

	@Transactional
	public int checkEmiChangeIsPresent(String referenceId) {
		return calcDao.checkEmiChangeIsPresent(referenceId);
	}

	@Transactional
	public int checkInterestChangeIsPresent(String referenceId) {
		return calcDao.checkInterestChangeIsPresent(referenceId);
	}

	@Transactional
	public int checkPartyIsPresent(long partyId) {
		return calcDao.checkPartyIsPresent(partyId);
	}

	@Transactional
	public int checkForumSubCategoryIsPresent(long forumSubCategoryId) {
		return calcDao.checkForumSubCategoryIsPresent(forumSubCategoryId);
	}

	@Transactional
	public int createQuery(List<CalcQuery> queryList, boolean checked) {
		int result = 0;
		String encryptPass = advTableFields.getEncryption_password();
		String deleteflag = calcTableFields.getDelete_flag_N();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		for (CalcQuery query : queryList) {
			String signedUserId = getSignedInUser();
			query.setDelete_flag(deleteflag);
			query.setCreated_by(signedUserId);
			query.setUpdated_by(signedUserId);

			Plan plan = calcDao.fetchPlanByReferenceId(query.getReferenceId(), encryptPass);

			Party party = calcDao.fetchParty(query.getPartyId(), query.getDelete_flag(), encryptPass);
			Party postedParty = calcDao.fetchParty(query.getPostedToPartyId(), query.getDelete_flag(), encryptPass);

			Advisor advisor = calcDao.fetchAdvisorByAdvId(postedParty.getRoleBasedId(), deleteflag);
			if (party.getRoleBasedId().startsWith("INV")) {
				// Investor investor = calcDao.fetchInvestorByInvId(party.getRoleBasedId(),
				// deleteflag, encryptPass);
				Investor investor = calcDao.fetchInvestorForCalcQuery(party.getRoleBasedId(), deleteflag, encryptPass);
				query.setDisplayName(investor.getDisplayName());
				query.setName(investor.getFullName());
			} else if (party.getRoleBasedId().startsWith("ADV")) {
				Advisor adv = calcDao.fetchAdvisorByAdvId(party.getRoleBasedId(), deleteflag);
				query.setDisplayName(adv.getDisplayName());
				query.setName(adv.getName());
			}
			query.setPlanName(plan.getName());
			query.setAge(plan.getAge());
			if (checked) {
				query.setPhoneNumber(party.getPhoneNumber());
				query.setEmailId(party.getEmailId());
			}
			query.setReceiverName(advisor.getDisplayName());
			CalcQuery calcQuery = calcDao.fetchCalcQuery(query.getPartyId(), query.getReferenceId(), deleteflag,
					query.getPostedToPartyId(), encryptPass);
			if (calcQuery != null) {
				// modify sharedPlans
				List<String> sharedPlans = new ArrayList<String>();
				List<String> queryPlans = new ArrayList<String>();

				String[] planSplit = calcQuery.getPlans().split(","); // table plans//
				for (String p : planSplit) {
					sharedPlans.add(p);
				}

				String[] qPlans = query.getPlans().split(","); // input plans//
				for (String p : qPlans) {
					queryPlans.add(p);
				}

				String plans = calcQuery.getPlans();
				for (String queryPlan : queryPlans) {
					if (!sharedPlans.contains(queryPlan)) {
						plans = plans + "," + queryPlan;
					}
				}
				result = calcDao.modifyCalcQuery(calcQuery.getCalcQueryId(), plans, query, timestamp, signedUserId);
			} else {
				// add sharedPlans
				result = calcDao.createQuery(query);
			}
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public String decrypt(String encodedPassword) {
		Security.addProvider(new BouncyCastleProvider());
		StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
		String key = advisorDao.fetchEncryptionSecretKey();
		cryptor.setPassword(key);
		cryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		String decryptedText = cryptor.decrypt(encodedPassword);
		return decryptedText;
	}

	@Transactional
	public int createAnswer(CalcAnswer answer) {
		String signedUserId = getSignedInUser();
		answer.setDelete_flag(calcTableFields.getDelete_flag_N());
		answer.setCreated_by(signedUserId);
		answer.setUpdated_by(signedUserId);
		return calcDao.createAnswer(answer);
	}

	@Transactional
	public int checkSharedAdvisor(String referenceId, long partyId) {
		String delete_flag = calcTableFields.getDelete_flag_N();
		return calcDao.checkSharedAdvisor(referenceId, partyId, delete_flag);
	}

	@Transactional
	public int fetchScoreByAnswerId(RiskProfile riskprofile) {
		return calcDao.fetchScoreByAnswerId(riskprofile);
	}

	@Transactional
	public int checkFutureValueIsPresent(String referenceId) {
		return calcDao.checkFutureValueIsPresent(referenceId);
	}

	@Transactional
	public int addFutureValue(FutureValue futureValue) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		futureValue.setCreated(timestamp);
		futureValue.setUpdated(timestamp);
		futureValue.setCreated_by(signedUserId);
		futureValue.setUpdated_by(signedUserId);
		return calcDao.addFutureValue(futureValue);
	}

	@Transactional
	public int updateFutureValue(FutureValue futureValue) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		futureValue.setUpdated(timestamp);
		futureValue.setUpdated_by(signedUserId);
		return calcDao.updateFutureValue(futureValue);
	}

	@Transactional
	public int checkTargetValueIsPresent(String referenceId) {
		return calcDao.checkTargetValueIsPresent(referenceId);
	}

	@Transactional
	public int addTargetValue(TargetValue targetValue) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		targetValue.setCreated(timestamp);
		targetValue.setUpdated(timestamp);
		targetValue.setCreated_by(signedUserId);
		targetValue.setUpdated_by(signedUserId);
		return calcDao.addTargetValue(targetValue);
	}

	@Transactional
	public int updateTargetValue(TargetValue targetValue) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		targetValue.setUpdated(timestamp);
		targetValue.setUpdated_by(signedUserId);
		return calcDao.updateTargetValue(targetValue);
	}

	@Transactional
	public int checkRateFinderIsPresent(String referenceId) {
		return calcDao.checkRateFinderIsPresent(referenceId);
	}

	@Transactional
	public int addRateFinder(RateFinder rateFinder) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		rateFinder.setCreated(timestamp);
		rateFinder.setUpdated(timestamp);
		rateFinder.setCreated_by(signedUserId);
		rateFinder.setUpdated_by(signedUserId);
		return calcDao.addRateFinder(rateFinder);
	}

	@Transactional
	public int updateRateFinder(RateFinder rateFinder) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		rateFinder.setUpdated(timestamp);
		rateFinder.setUpdated_by(signedUserId);
		return calcDao.updateRateFinder(rateFinder);
	}

	@Transactional
	public int checkTenureFinderIsPresent(String referenceId) {
		return calcDao.checkTenureFinderIsPresent(referenceId);
	}

	@Transactional
	public int addTenureFinder(TenureFinder tenureFinder) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		tenureFinder.setCreated(timestamp);
		tenureFinder.setUpdated(timestamp);
		tenureFinder.setCreated_by(signedUserId);
		tenureFinder.setUpdated_by(signedUserId);
		return calcDao.addTenureFinder(tenureFinder);
	}

	@Transactional
	public int updateTenureFinder(TenureFinder tenureFinder) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = getSignedInUser();
		tenureFinder.setUpdated(timestamp);
		tenureFinder.setUpdated_by(signedUserId);
		return calcDao.updateTenureFinder(tenureFinder);
	}

	@Transactional
	public int fetchAccountTypeIdByAccountType(String type) {
		return calcDao.fetchAccountTypeIdByAccountType(type);
	}

	@Transactional
	public FutureValue fetchFutureValueByRefId(String referenceId) {
		return calcDao.fetchFutureValueByRefId(referenceId);
	}

	@Transactional
	public TargetValue fetchTargetValueByRefId(String referenceId) {
		return calcDao.fetchTargetValueByRefId(referenceId);
	}

	@Transactional
	public RateFinder fetchRateFinderByRefId(String referenceId) {
		return calcDao.fetchRateFinderByRefId(referenceId);

	}

	@Transactional
	public TenureFinder fetchTenureFinderByRefId(String referenceId) {
		return calcDao.fetchTenureFinderByRefId(referenceId);
	}

	@Transactional
	public Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass) {
		return calcDao.fetchPartyForSignIn(username, delete_flag, encryptPass);
	}

	@Transactional
	public List<CalcQuery> fetchSharedPlanByPostedPartyId(long partyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchSharedPlanByPostedPartyId(partyId, deleteflag, encryptPass);
	}

	@Transactional
	public List<CalcQuery> fetchSharedPlanByPartyId(long partyId, String referenceId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchSharedPlanByPartyId(partyId, referenceId, deleteflag, encryptPass);
	}

	@Transactional
	public int checkCalcQueryIsPresent(long queryId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return calcDao.checkCalcQueryIsPresent(queryId, deleteflag);
	}

	@Transactional
	public int createCommentQueries(Queries queries) {
		String delete_flag = advTableFields.getDelete_flag_N();
		String signedUserId = getSignedInUser();
		queries.setCreated_by(signedUserId);
		queries.setUpdated_by(signedUserId);
		queries.setDelete_flag(delete_flag);
		int result = calcDao.createCommentQueries(queries);
		if (result != 0) {
			int result1 = calcDao.modifyCalcQueryAfterComment(queries.getCalcQueryId(), signedUserId);
		}
		return result;
	}

	@Transactional
	public int checkQueriesIsPresent(long id) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return calcDao.checkQueriesIsPresent(id, deleteflag);
	}

	@Transactional
	public List<Queries> fetchQueries(long senderId, long receiverId, String plans) {
		String delete_flag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchQueries(senderId, receiverId, plans, delete_flag, encryptPass);
	}

	@Transactional
	public List<CalcQuery> fetchSharedPlanByRefId(String refId) {
		String delete_flag = calcTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return calcDao.fetchSharedPlanByRefId(refId, delete_flag, encryptPass);
	}

}
