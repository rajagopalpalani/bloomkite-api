package com.sowisetech.calc.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
import com.sowisetech.calc.model.Fund;
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
import com.sowisetech.calc.model.RateFinder;
import com.sowisetech.calc.model.RiskPortfolio;
import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskQuestionaire;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.TenureFinder;
import com.sowisetech.calc.model.Urgency;
import com.sowisetech.calc.request.SharedRequest;
import com.sowisetech.forum.model.ArticleComment;

@Service
public interface CalcService {

	double calculateGoalFutureCost(double goalAmt, double inflationRate, int tenure);

	double calculateGoalCurrentInvestment(double currentAmt, double growthRate, int tenure);

	double calculateGoalFinalCorpus(double futureCost, double futureValue);

	double calculateGoalMonthlyInvestment(double growthRate, double annualInvRate, double finalCorpus, int tenure,
			double returnRate);

	double calculateGoalAnnualyInvestment(double growthRate, double annualInvRate, double finalCorpus, int tenure);

	int addGoalInfo(Goal goal);

	int fetchCashFlowItemTypeIdByItemId(long cashFlowItemId);

	int addAndModifyCashFlow(List<CashFlow> cashFlowList);

	List<CashFlow> fetchCashFlowByRefIdAndTypeId(String referenceId, int typeId);

	List<Integer> fetchRecurringExpenditureItemType();

	// List<CashFlow> fetchNonRecurringExpenditureByRefId(String referenceId);

	List<CashFlow> fetchRecurringIncomeByRefId(String referenceId);

	// List<CashFlow> fetchNonRecurringIncomeByRefId(String referenceId);

	int addCashFlowSummary(CashFlowSummary cashFlowSummary);

	int addAndModifyNetworth(List<Networth> networthList);

	int fetchAccountTypeIdByEntryId(int accountEntryId);

	List<Networth> fetchNetworthByAssets(String referenceId);

	List<Networth> fetchNetworthByLiabilities(String referenceId);

	int addNetworthSummary(NetworthSummary networthSummary);

	int addInsurance(Insurance insurance);

	int addAndModifyPriorities(List<Priority> priorityList);

	List<Priority> fetchPriorityByRefId(String referenceId);

	Priority fetchPriorityByRefIdAndItemId(String referenceId, int priorityItemId);

	int updatePriorityOrder(String referenceId, int priorityItemId, int order);

	Party fetchParty(long partyId);

	int addAndModifyRiskProfile(List<RiskProfile> riskProfileList);

	List<RiskProfile> fetchRiskProfileByRefId(String referenceId);

	RiskPortfolio fetchRiskPortfolioByPoints(String points);

	int addRiskSummary(RiskSummary riskSummary);

	List<AccountType> fetchAccountTypeList();

	List<Account> fetchAccountList();

	List<CashFlowItemType> fetchCashFlowItemTypeList();

	List<CashFlowItem> fetchCashFlowItemList();

	List<PriorityItem> fetchPriorityItemList();

	List<Urgency> fetchUrgencyList();

	List<RiskPortfolio> fetchRiskPortfolioList();

	List<RiskQuestionaire> fetchRiskQuestionaireList();

	Party fetchPartyIdByRoleBasedId(String id);

	List<Goal> fetchGoalByReferenceId(String referenceId);

	List<CashFlow> fetchCashFlowByRefId(String referenceId);

	CashFlowSummary fetchCashFlowSummaryByRefId(String referenceId);

	List<Networth> fetchNetworthByRefId(String referenceId);

	Insurance fetchInsuranceByRefId(String referenceId);

	NetworthSummary fetchNetworthSummaryByRefId(String referenceId);

	RiskSummary fetchRiskSummaryByRefId(String referenceId);

	int addEmiCalculator(EmiCalculator emiCalculator);

	int addEmiCapacity(EmiCapacity emiCapacity);

	int addPartialPayment(PartialPayment partialPayment);

	int addInterestChange(InterestChange interestChange);

	int addEmiChange(EmiChange emiChange);

	int addEmiInterestChange(EmiInterestChange emiInterestChange);

	EmiCalculator fetchEmiCalculatorByRefId(String referenceId);

	EmiCapacity fetchEmiCapacityByRefId(String referenceId);

	List<PartialPayment> fetchPartialPaymentByRefId(String referenceId);

	List<InterestChange> fetchInterestChangeByRefId(String referenceId);

	List<EmiChange> fetchEmiChangeByRefId(String referenceId);

	List<EmiInterestChange> fetchEmiInterestChangeByRefId(String referenceId);

	RiskProfile fetchRiskProfileByRefIdAndAnswerId(String referenceId, long answerId);

	Networth fetchNetworthByRefIdAndEntryId(String referenceId, long accountEntryId);

	AccountType fetchAccountTypeByTypeId(long accountTypeId);

	CashFlow fetchCashFlowByRefIdAndItemId(String referenceId, long cashFlowItemId);

	CashFlowItemType fetchCashFlowItemTypeByTypeId(long cashFlowItemTypeId);

	InsuranceItem fetchInsuranceItemByRefId(String referenceId);

	int addPlanInfo(Plan plan);

	String fetchRoleBasedIdByPartyId(long partyId);

	Plan fetchPlanByReferenceId(String id);

	int updateCashFlow(CashFlow cashFlow);

	int removeCashFlowSummary(String referenceId);

	int updateNetworth(Networth networth);

	int removeNetworthSummary(String referenceId);

	int updatePriority(Priority priority);

	int updateInsurance(Insurance insurance);

	int updateRiskProfile(RiskProfile riskProfile);

	int removeRiskSummary(String referenceId);

	int updateEmiCalculator(EmiCalculator emiCalculator);

	int updateEmiCapacity(EmiCapacity emiCapacity);

	RiskProfile fetchRiskProfileByRefIdAndQuestionId(String referenceId, String questionId);

	List<String> fetchQuestionIdFromRiskQuestionaire();

	List<RiskQuestionaire> fetchRiskQuestionaireByQuestionId(String questionId);

	String fetchQuestionByQuestionId(String questionId);

	Goal fetchGoalByRefIdAndGoalName(String referenceId, String goalName);

	int updateGoalInfo(Goal goal);

	int removePartialPaymentByRefId(String referenceId);

	int removeEmiChangeByRefId(String referenceId);

	int removeInterestChangeByRefId(String referenceId);

	int removeEmiInterestChangeByRefId(String referenceId);

	int updateCashFlowSummary(CashFlowSummary cashFlowSummary);

	int updateNetworthSummary(NetworthSummary networthSummary);

	int updateRiskSummary(RiskSummary riskSummary);

	int removeInsuranceByRefId(String referenceId);

	PriorityItem fetchPriorityItemByItemId(int itemId);

	int removePriorityByRefIdAndItemId(String referenceId, int priorityItemId);

	String generatePlanReferenceId();

	List<Plan> fetchPlanByPartyId(long partyId);

	int modifyPlanInfo(Plan plan, String referenceId);

	int removePlanInfo(String referenceId);

	// List<Plan> fetchPlanBySuperParentId(long superParentId);

	String fetchEmailIdByPartyId(long partyId);

	int checkPlanIsPresentByReferenceId(String referenceId);

	int checkGoalIsPresentByRefIdAndGoalName(String referenceId, String goalName);

	int checkCashFlowIsPresent(String referenceId, int cashFlowItemId);

	int checkCashFlowSummaryIsPresent(String referenceId);

	int checkNetworthIsPresent(String referenceId, int accountEntryId);

	int checkNetworthSummaryIsPresent(String referenceId);

	int checkPriorityByRefIdAndItemId(String referenceId, int priorityItemId);

	int checkInsuranceByRefId(String referenceId);

	int checkRiskProfileIsPresent(String referenceId, String questionId);

	int checkRiskSummaryIsPresent(String referenceId);

	int checkEmiCalculatorIsPresent(String referenceId);

	int checkemiCapacityIsPresent(String referenceId);

	int checkpartialIsPresent(String referenceId);

	int checkEmiChangeIsPresent(String referenceId);

	int checkInterestChangeIsPresent(String referenceId);

	int checkPartyIsPresent(long partyId);

	int checkForumSubCategoryIsPresent(long forumSubCategoryId);

	int createQuery(List<CalcQuery> queryList, boolean checked);

	int createAnswer(CalcAnswer answer);

	int checkSharedAdvisor(String referenceId, long partyId);

	int fetchScoreByAnswerId(RiskProfile riskprofile);

	int checkFutureValueIsPresent(String referenceId);

	int addFutureValue(FutureValue futureValue);

	int updateFutureValue(FutureValue futureValue);

	int checkTargetValueIsPresent(String referenceId);

	int addTargetValue(TargetValue targetValue);

	int updateTargetValue(TargetValue targetValue);

	int checkRateFinderIsPresent(String referenceId);

	int addRateFinder(RateFinder rateFinder);

	int updateRateFinder(RateFinder rateFinder);

	int checkTenureFinderIsPresent(String referenceId);

	int addTenureFinder(TenureFinder tenureFinder);

	int updateTenureFinder(TenureFinder tenureFinder);

	int fetchAccountTypeIdByAccountType(String assets);

	InsuranceItem fetchInsuranceItemWithoutLogin(Insurance insurance);

	FutureValue fetchFutureValueByRefId(String referenceId);

	TargetValue fetchTargetValueByRefId(String referenceId);

	RateFinder fetchRateFinderByRefId(String referenceId);

	TenureFinder fetchTenureFinderByRefId(String referenceId);

	Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass);

	List<CalcQuery> fetchSharedPlanByPostedPartyId(long partyId);

	List<CalcQuery> fetchSharedPlanByPartyId(long partyId,String referenceId);

	int checkCalcQueryIsPresent(long queryId);

	int createCommentQueries(Queries queries);

	int checkQueriesIsPresent(long queryId);

	List<Queries> fetchQueries(long senderId,long receiverId, String plans);

	List<CalcQuery> fetchSharedPlanByRefId(String refId);	

}
