package com.sowisetech.calc.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;

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
import com.sowisetech.investor.model.Investor;

public interface CalcDao {

	int addGoalInfo(Goal goal);

	int fetchCashFlowItemTypeIdByItemId(long cashFlowItemId);

	int addCashFlow(CashFlow cashFlow);

	List<CashFlow> fetchCashFlowByRefIdAndTypeId(String referenceId, int typeId);

	int fetchCashFlowItemTypeIdByItemType(String type);

	int addCashFlowSummary(CashFlowSummary cashFlowSummary);

	int addNetworth(Networth networth);

	int fetchAccountTypeIdByEntryId(int accountEntryId);

	int fetchAccountTypeIdByAccountType(String type);

	List<Networth> fetchNetworthByAccountTypeIdAndRefId(int accountTypeId, String referenceId);

	int addNetworthSummary(NetworthSummary networthSummary);

	int addInsurance(Insurance insurance);

	int addPriorities(Priority priority);

	List<Priority> fetchPriorityByRefId(String referenceId);

	Priority fetchPriorityByRefIdAndItemId(String referenceId, int itemId);

	// int updatePriorityOrder(String referenceId, int priorityItemId, int order,
	// String signedUserId,
	// Timestamp timestamp);
	int updatePriorityOrder(String referenceId, int priorityItemId, int order, String signedUserId,
			Timestamp timestamp);

	Party fetchParty(long partyId, String delete_flag, String encryptPass);

	int addRiskProfile(RiskProfile riskProfile);

	List<RiskProfile> fetchRiskProfileByRefId(String referenceId);

	RiskPortfolio fetchRiskPortfolioByPoints(String points);

	int addRiskSummary(RiskSummary riskSummary);

	List<Goal> fetchGoalByReferenceId(String referenceId);

	List<CashFlow> fetchCashFlowByRefId(String referenceId);

	CashFlowSummary fetchCashFlowSummaryByRefId(String referenceId);

	List<Networth> fetchNetworthByRefId(String referenceId);

	NetworthSummary fetchNetworthSummaryByRefId(String referenceId);

	RiskSummary fetchRiskSummaryByRefId(String referenceId);

	Insurance fetchInsuranceByRefId(String referenceId);

	List<AccountType> fetchAccountTypeList();

	List<Account> fetchAccountList();

	Party fetchPartyIdByRoleBasedId(String id, String delete_flag, String encryptPass);

	List<CashFlowItemType> fetchCashFlowItemTypeList();

	List<CashFlowItem> fetchCashFlowItemList();

	List<PriorityItem> fetchPriorityItemList();

	List<Urgency> fetchUrgencyList();

	List<RiskPortfolio> fetchRiskPortfolioList();

	List<RiskQuestionaire> fetchRiskQuestionaireList();

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

	PriorityItem fetchPriorityItemByItemId(long priorityItemId);

	RiskProfile fetchRiskProfileByRefIdAndAnswerId(String referenceId, long answerId);

	Networth fetchNetworthByRefIdAndEntryId(String referenceId, long accountEntryId);

	AccountType fetchAccountTypeByTypeId(long accountTypeId);

	CashFlow fetchCashFlowByRefIdAndItemId(String referenceId, long cashFlowItemId);

	CashFlowItemType fetchCashFlowItemTypeByTypeId(long cashFlowItemTypeId);

	List<EmiInterestChange> fetchEmiInterestChangeByRefId(String referenceId);

	String fetchValueByInsuranceItem(String insuranceItem);

	int addPlanInfo(Plan plan, String encryptPass);

	String fetchRoleBasedIdByPartyId(long partyId);

	Plan fetchPlanByReferenceId(String id, String encryptPass);

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

	int fetchScoreByAnswerId(RiskProfile riskProfile);

	int removePartialPaymentByRefId(String referenceId);

	int removeEmiChangeByRefId(String referenceId);

	int removeInterestChangeByRefId(String referenceId);

	int removeEmiInterestChangeByRefId(String referenceId);

	int updateCashFlowSummary(CashFlowSummary cashFlowSummary);

	int updateNetworthSummary(NetworthSummary networthSummary);

	int updateRiskSummary(RiskSummary riskSummary);

	int removeInsuranceByRefId(String referenceId);

	int removePriorityByRefIdAndItemId(String referenceId, int priorityItemId);

	String fetchPlanReferenceId();

	int addPlanReferenceId(String newId);

	List<Plan> fetchPlanByPartyId(long partyId, String encryptPass);

	int modifyPlanInfo(Plan plan, String referenceId, String encryptPass);

	int removePlanInfo(String referenceId);

	// List<Plan> fetchPlanBySuperParentId(long superParentId, String encryptPass);

	String fetchEmailIdByPartyId(long partyId, String encryptPass);

	void removeCashFlow(String referenceId);

	void removeNetworth(String referenceId);

	void removePriority(String referenceId);

	void removeEmiCapacity(String referenceId);

	void removeEmiCalculator(String referenceId);

	void removeGoal(String referenceId);

	void removeRiskProfile(String referenceId);

	Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass);

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

	int checkForumSubCategoryIsPresent(long forumSubCategoryId);

	int checkPartyIsPresent(long partyId);

	int createQuery(CalcQuery query);

	int createAnswer(CalcAnswer answer);

	int checkSharedAdvisor(String referenceId, long partyId, String delete_flag);

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

	FutureValue fetchFutureValueByRefId(String referenceId);

	TargetValue fetchTargetValueByRefId(String referenceId);

	RateFinder fetchRateFinderByRefId(String referenceId);

	TenureFinder fetchTenureFinderByRefId(String referenceId);

	void removeFutureValue(String referenceId);

	void removeTargetValue(String referenceId);

	void removeRateFinder(String referenceId);

	void removeTenureFinder(String referenceId);

	List<CalcQuery> fetchSharedPlanByPostedPartyId(long partyId, String deleteflag, String encryptPass);

	List<CalcQuery> fetchSharedPlanByPartyId(long partyId, String referenceId, String deleteflag, String encryptPass);

	int checkCalcQueryIsPresent(long queryId, String deleteflag);

	int createCommentQueries(Queries queries);

	int checkQueriesIsPresent(long queryId, String deleteflag);

	List<Queries> fetchQueries(long senderId, long receiverId, String plans, String delete_flag, String encryptPass);

	Advisor fetchAdvisorByAdvId(String roleBasedId, String deleteflag);

	CalcQuery fetchCalcQuery(long partyId, String referenceId, String delete_flag, long postedToPartyId,
			String encryptPass);

	int modifyCalcQuery(long calcQueryId, String plans, CalcQuery query, Timestamp timestamp, String signedUserId);

	int modifyCalcQueryAfterComment(long calcQueryId, String signedUserId);

	Investor fetchInvestorByInvId(String roleBasedId, String deleteflag, String encryptPass);

	List<CalcQuery> fetchSharedPlanByRefId(String refId, String delete_flag, String encryptPass);

	int removePriority(Priority priority);

	Investor fetchInvestorForCalcQuery(String roleBasedId, String deleteflag, String encryptPass);

}
