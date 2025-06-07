package com.sowisetech.calc.dao;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.investor.dao.InvestorExtractor;
import com.sowisetech.investor.model.Investor;

@Repository
@Transactional
public class CalcDaoImpl implements CalcDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(CalcDaoImpl.class);

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void postConstruct() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public int addGoalInfo(Goal goal) {
		try {
			String sql = "INSERT INTO `goal` (`referenceId`,`goalName`, `tenure`,`tenureType`, `goalAmount`, `inflationRate`,`currentAmount`,`growthRate`,`rateOfReturn`,`annualInvestmentRate`,`futureCost`,`futureValue`,`finalCorpus`,`monthlyInv`,`annualInv`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,?,?,?,?, ?, ?, ?, ?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, goal.getReferenceId(), goal.getGoalName(), goal.getTenure(),
					goal.getTenureType(), goal.getGoalAmount(), goal.getInflationRate(), goal.getCurrentAmount(),
					goal.getGrowthRate(), goal.getRateOfReturn(), goal.getAnnualInvestmentRate(), goal.getFutureCost(),
					goal.getFutureValue(), goal.getFinalCorpus(), goal.getMonthlyInv(), goal.getAnnualInv(),
					goal.getCreated(), goal.getCreated_by(), goal.getUpdated(), goal.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchCashFlowItemTypeIdByItemId(long cashFlowItemId) {
		try {
			String sql = "SELECT `cashFlowItemTypeId` FROM `cashflowitem` WHERE `cashFlowItemId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, cashFlowItemId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addCashFlow(CashFlow cashFlow) {
		try {
			String sql = "INSERT INTO `cashflow` (`referenceId`,`cashFlowItemId`, `budgetAmt`,`actualAmt`, `date`, `cashFlowItemTypeId`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?, ?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, cashFlow.getReferenceId(), cashFlow.getCashFlowItemId(),
					cashFlow.getBudgetAmt(), cashFlow.getActualAmt(), cashFlow.getDate(),
					cashFlow.getCashFlowItemTypeId(), cashFlow.getCreated(), cashFlow.getCreated_by(),
					cashFlow.getUpdated(), cashFlow.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<CashFlow> fetchCashFlowByRefIdAndTypeId(String referenceId, int typeId) {
		try {
			String sql = "SELECT `cashFlowId`,`referenceId`,`cashFlowItemId`,`budgetAmt`,`actualAmt`,`date`,`cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by` FROM `cashflow` WHERE `referenceId`=? AND `cashFlowItemTypeId`=?";
			RowMapper<CashFlow> rowMapper = new BeanPropertyRowMapper<CashFlow>(CashFlow.class);
			List<CashFlow> cashFlow = jdbcTemplate.query(sql, rowMapper, referenceId, typeId);
			return cashFlow;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchCashFlowItemTypeIdByItemType(String type) {
		try {
			String sqlCashFlowTypeId1 = "SELECT `cashFlowItemTypeId` FROM `cashflowitemtype` WHERE `cashFlowItemType`= ?";
			int typeId = jdbcTemplate.queryForObject(sqlCashFlowTypeId1, Integer.class, type);
			return typeId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addCashFlowSummary(CashFlowSummary cashFlowSummary) {
		try {
			String sql = "INSERT INTO `cashflowsummary` (`referenceId`,`monthlyExpense`,`yearlyExpense`,`monthlyIncome`,`yearlyIncome`,`monthlyNetCashFlow`,`yearlyNetCashFlow`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, cashFlowSummary.getReferenceId(), cashFlowSummary.getMonthlyExpense(),
					cashFlowSummary.getYearlyExpense(), cashFlowSummary.getMonthlyIncome(),
					cashFlowSummary.getYearlyIncome(), cashFlowSummary.getMonthlyNetCashFlow(),
					cashFlowSummary.getYearlyNetCashFlow(), cashFlowSummary.getCreated(),
					cashFlowSummary.getCreated_by(), cashFlowSummary.getUpdated(), cashFlowSummary.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addNetworth(Networth networth) {
		try {
			String sql = "INSERT INTO `networth` (`accountEntryId`,`value`,`futureValue`,`referenceId`,`accountTypeId`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, networth.getAccountEntryId(), networth.getValue(),
					networth.getFutureValue(), networth.getReferenceId(), networth.getAccountTypeId(),
					networth.getCreated(), networth.getCreated_by(), networth.getUpdated(), networth.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchAccountTypeIdByEntryId(int accountEntryId) {
		try {
			String sql = "SELECT `accountTypeId` FROM `account` WHERE `accountEntryId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, accountEntryId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchAccountTypeIdByAccountType(String type) {
		try {
			String sql = "SELECT `accountTypeId` FROM `accounttype` WHERE `accountType`= ?";
			int accountTypeId = jdbcTemplate.queryForObject(sql, Integer.class, type);
			return accountTypeId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Networth> fetchNetworthByAccountTypeIdAndRefId(int accountTypeId, String referenceId) {
		try {
			String sqlNetworth = "SELECT `networthId`,`accountEntryId`,`value`,`futureValue`,`referenceId`,`accountTypeId`,`created`,`updated`,`created_by`,`updated_by` FROM `networth` WHERE `referenceId` = ? AND `accountTypeId`=?";
			RowMapper<Networth> rowMapper = new BeanPropertyRowMapper<Networth>(Networth.class);
			List<Networth> networth = jdbcTemplate.query(sqlNetworth, rowMapper, referenceId, accountTypeId);
			return networth;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addNetworthSummary(NetworthSummary networthSummary) {
		try {
			String sql = "INSERT INTO `networthsummary` (`referenceId`,`current_assetValue`,`current_liability`,`networth`,`future_assetValue`,`future_liability`,`future_networth`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, networthSummary.getReferenceId(),
					networthSummary.getCurrent_assetValue(), networthSummary.getCurrent_liability(),
					networthSummary.getNetworth(), networthSummary.getFuture_assetValue(),
					networthSummary.getFuture_liability(), networthSummary.getFuture_networth(),
					networthSummary.getCreated(), networthSummary.getCreated_by(), networthSummary.getUpdated(),
					networthSummary.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addInsurance(Insurance insurance) {
		try {
			String sql = "INSERT INTO `insurance` (`referenceId`,`annualIncome`,`stability`,`predictability`,`requiredInsurance`,`existingInsurance`,`additionalInsurance`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, insurance.getReferenceId(), insurance.getAnnualIncome(),
					insurance.getStability(), insurance.getPredictability(), insurance.getRequiredInsurance(),
					insurance.getExistingInsurance(), insurance.getAdditionalInsurance(), insurance.getCreated(),
					insurance.getCreated_by(), insurance.getUpdated(), insurance.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addPriorities(Priority priority) {
		try {
			String sql = "INSERT INTO `priority` (`referenceId`,`priorityItemId`,`urgencyId`,`priorityOrder`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, priority.getReferenceId(), priority.getPriorityItemId(),
					priority.getUrgencyId(), priority.getPriorityOrder(), priority.getCreated(),
					priority.getCreated_by(), priority.getUpdated(), priority.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Priority> fetchPriorityByRefId(String referenceId) {
		List<Priority> priorityList = new ArrayList<Priority>();
		try {
			String sql = "SELECT `priorityId`,`referenceId`,`priorityItemId`,`urgencyId`,`priorityOrder`,`created`,`updated`,`created_by`,`updated_by` FROM `priority` WHERE `referenceId`= ?";
			RowMapper<Priority> rowMapper = new BeanPropertyRowMapper<Priority>(Priority.class);
			priorityList = jdbcTemplate.query(sql, rowMapper, referenceId);
			for (Priority priority : priorityList) {
				String sql1 = "SELECT `priorityItem` FROM `priorityitem` WHERE `priorityItemId`= ?";
				priority.setPriorityItem(
						(jdbcTemplate.queryForObject(sql1, String.class, priority.getPriorityItemId())));
				String sql2 = "SELECT `value` FROM `urgency` WHERE `urgencyId`= ?";
				priority.setUrgency((jdbcTemplate.queryForObject(sql2, String.class, priority.getUrgencyId())));
			}
			return priorityList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Priority fetchPriorityByRefIdAndItemId(String referenceId, int itemId) {
		try {
			String sql = "SELECT `priorityId`,`referenceId`,`priorityItemId`,`urgencyId`,`priorityOrder`,`created`,`updated`,`created_by`,`updated_by` FROM `priority` WHERE `referenceId`= ? AND `priorityItemId`=?";
			RowMapper<Priority> rowMapper = new BeanPropertyRowMapper<Priority>(Priority.class);
			Priority priority = jdbcTemplate.queryForObject(sql, rowMapper, referenceId, itemId);
			String sql1 = "SELECT `priorityItem` FROM `priorityitem` WHERE `priorityItemId`= ?";
			priority.setPriorityItem((jdbcTemplate.queryForObject(sql1, String.class, priority.getPriorityItemId())));
			String sql2 = "SELECT `value` FROM `urgency` WHERE `urgencyId`= ?";
			priority.setUrgency((jdbcTemplate.queryForObject(sql2, String.class, priority.getUrgencyId())));
			return priority;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updatePriorityOrder(String referenceId, int priorityItemId, int order, String signedUserId,
			Timestamp timestamp) {
		try {
			String sql1 = "UPDATE `priority` SET `priorityOrder`=? , `updated`=?, `updated_by` = ? WHERE `referenceId`=? AND `priorityItemId`=?";
			int result = jdbcTemplate.update(sql1, order, timestamp, signedUserId, referenceId, priorityItemId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Party fetchParty(long partyId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `partyId`= ? AND `delete_flag`=?";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			Party party = jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, partyId, delete_flag);
			return party;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchScoreByAnswerId(RiskProfile riskProfile) {
		// System.out.println("fetch score");
		try {
			String sqlScore = "SELECT `score` FROM `riskquestionaire` WHERE `answerId`= ?";
			int score = jdbcTemplate.queryForObject(sqlScore, Integer.class, riskProfile.getAnswerId());
			return score;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addRiskProfile(RiskProfile riskProfile) {
		// System.out.println("add dao");
		try {
			String sql = "INSERT INTO `riskprofile` (`referenceId`,`questionId`,`answerId`,`score`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, riskProfile.getReferenceId(), riskProfile.getQuestionId(),
					riskProfile.getAnswerId(), riskProfile.getScore(), riskProfile.getCreated(),
					riskProfile.getCreated_by(), riskProfile.getUpdated(), riskProfile.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<RiskProfile> fetchRiskProfileByRefId(String referenceId) {
		List<RiskProfile> riskProfileList = new ArrayList<RiskProfile>();
		try {
			String sql = "SELECT `riskProfileId`,`referenceId`,`questionId`,`answerId`,`score`,`created`,`updated`,`created_by`,`updated_by` FROM `riskprofile` WHERE `referenceId`= ?";
			RowMapper<RiskProfile> rowMapper = new BeanPropertyRowMapper<RiskProfile>(RiskProfile.class);
			riskProfileList = jdbcTemplate.query(sql, rowMapper, referenceId);
			return riskProfileList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public RiskPortfolio fetchRiskPortfolioByPoints(String points) {
		try {
			String sql = "SELECT `riskPortfolioId`,`points`,`behaviour`,`equity`,`debt`,`cash` FROM `riskportfolio` WHERE `points`= ?";
			RowMapper<RiskPortfolio> rowMapper = new BeanPropertyRowMapper<RiskPortfolio>(RiskPortfolio.class);
			RiskPortfolio riskPortfolio = jdbcTemplate.queryForObject(sql, rowMapper, points);
			return riskPortfolio;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addRiskSummary(RiskSummary riskSummary) {
		try {
			String sql = "INSERT INTO `risksummary` (`referenceId`,`behaviour`,`eqty_alloc`,`debt_alloc`,`cash_alloc`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?, ?, ?, ?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, riskSummary.getReferenceId(), riskSummary.getBehaviour(),
					riskSummary.getEqty_alloc(), riskSummary.getDebt_alloc(), riskSummary.getCash_alloc(),
					riskSummary.getCreated(), riskSummary.getCreated_by(), riskSummary.getUpdated(),
					riskSummary.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Goal> fetchGoalByReferenceId(String referenceId) {
		try {
			String sql = "SELECT `goalId`,`referenceId`,`goalName`, `tenure`,`tenureType`, `goalAmount`, `inflationRate`,`currentAmount`,`growthRate`,`rateOfReturn`,`annualInvestmentRate`,`futureCost`,`futureValue`,`finalCorpus`,`monthlyInv`,`annualInv`,`created`,`updated`,`created_by`,`updated_by` FROM `goal` WHERE `referenceId`= ?";
			RowMapper<Goal> rowMapper = new BeanPropertyRowMapper<Goal>(Goal.class);
			return jdbcTemplate.query(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<CashFlow> fetchCashFlowByRefId(String referenceId) {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		try {
			String sql = "SELECT `cashFlowId`,`referenceId`,`cashFlowItemId`,`budgetAmt`,`actualAmt`,`date`,`cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by` FROM `cashflow` WHERE `referenceId`= ?";
			RowMapper<CashFlow> rowMapper = new BeanPropertyRowMapper<CashFlow>(CashFlow.class);
			cashFlowList = jdbcTemplate.query(sql, rowMapper, referenceId);
			for (CashFlow cashFlow : cashFlowList) {
				String sql1 = "SELECT `cashFlowItem` FROM `cashflowitem` WHERE `cashFlowItemId`= ?";
				cashFlow.setCashFlowItem(jdbcTemplate.queryForObject(sql1, String.class, cashFlow.getCashFlowItemId()));
				String sql2 = "SELECT `cashFlowItemType` FROM `cashflowitemtype` WHERE `cashFlowItemTypeId`= ?";
				cashFlow.setCashFlowItemType(
						jdbcTemplate.queryForObject(sql2, String.class, cashFlow.getCashFlowItemTypeId()));
			}
			return cashFlowList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Networth> fetchNetworthByRefId(String referenceId) {
		List<Networth> networthList = new ArrayList<Networth>();
		try {
			String sql = "SELECT `networthId`,`accountEntryId`,`value`,`futureValue`,`referenceId`,`accountTypeId`,`created`,`updated`,`created_by`,`updated_by` FROM `networth` WHERE `referenceId`= ?";
			RowMapper<Networth> rowMapper = new BeanPropertyRowMapper<Networth>(Networth.class);
			networthList = jdbcTemplate.query(sql, rowMapper, referenceId);
			for (Networth networth : networthList) {
				String sql1 = "SELECT `accountEntry` FROM `account` WHERE `accountEntryId`= ?";
				networth.setAccountEntry(jdbcTemplate.queryForObject(sql1, String.class, networth.getAccountEntryId()));
				String sql2 = "SELECT `accountType` FROM `accounttype` WHERE `accountTypeId`= ?";
				networth.setAccountType(jdbcTemplate.queryForObject(sql2, String.class, networth.getAccountTypeId()));
			}
			return networthList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public CashFlowSummary fetchCashFlowSummaryByRefId(String referenceId) {
		try {
			String sql = "SELECT `cashFlowSummaryId`,`referenceId`,`monthlyExpense`,`yearlyExpense`,`monthlyIncome`,`yearlyIncome`,`monthlyNetCashFlow`,`yearlyNetCashFlow`,`created`,`updated`,`created_by`,`updated_by` FROM `cashflowsummary` WHERE `referenceId`= ?";
			RowMapper<CashFlowSummary> rowMapper = new BeanPropertyRowMapper<CashFlowSummary>(CashFlowSummary.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public NetworthSummary fetchNetworthSummaryByRefId(String referenceId) {
		try {
			String sql = "SELECT `networthSummaryId`,`referenceId`,`current_assetValue`,`current_liability`,`networth`,`future_assetValue`,`future_liability`,`future_networth`,`created`,`updated`,`created_by`,`updated_by` FROM `networthsummary` WHERE `referenceId`= ?";
			RowMapper<NetworthSummary> rowMapper = new BeanPropertyRowMapper<NetworthSummary>(NetworthSummary.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public RiskSummary fetchRiskSummaryByRefId(String referenceId) {
		try {
			String sql = "SELECT `riskSummaryId`,`referenceId`,`behaviour`,`eqty_alloc`,`debt_alloc`,`cash_alloc`,`created`,`updated`,`created_by`,`updated_by` FROM `risksummary` WHERE `referenceId`= ?";
			RowMapper<RiskSummary> rowMapper = new BeanPropertyRowMapper<RiskSummary>(RiskSummary.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Insurance fetchInsuranceByRefId(String referenceId) {
		try {
			String sql = "SELECT `insuranceId`,`referenceId`,`annualIncome`,`stability`,`predictability`,`requiredInsurance`,`existingInsurance`,`additionalInsurance`,`created`,`updated`,`created_by`,`updated_by` FROM `insurance` WHERE `referenceId`= ?";
			RowMapper<Insurance> rowMapper = new BeanPropertyRowMapper<Insurance>(Insurance.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<AccountType> fetchAccountTypeList() {
		try {
			String sql = "SELECT `accountTypeId`,`accountType` FROM `accounttype`";
			RowMapper<AccountType> rowMapper = new BeanPropertyRowMapper<AccountType>(AccountType.class);
			List<AccountType> accountType = jdbcTemplate.query(sql, rowMapper);
			return accountType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Account> fetchAccountList() {
		try {
			String sql = "SELECT `accountEntryId`,`accountEntry`,`accountTypeId` FROM `account`";
			RowMapper<Account> rowMapper = new BeanPropertyRowMapper<Account>(Account.class);
			List<Account> account = jdbcTemplate.query(sql, rowMapper);
			return account;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Party fetchPartyIdByRoleBasedId(String id, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `roleBasedId` = ? AND `delete_flag` = ?";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass, id,
					delete_flag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<CashFlowItemType> fetchCashFlowItemTypeList() {
		try {
			String sql = "SELECT `cashFlowItemTypeId`,`cashFlowItemType` FROM `cashflowitemtype`";
			RowMapper<CashFlowItemType> rowMapper = new BeanPropertyRowMapper<CashFlowItemType>(CashFlowItemType.class);
			List<CashFlowItemType> cashFlowItemType = jdbcTemplate.query(sql, rowMapper);
			return cashFlowItemType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<CashFlowItem> fetchCashFlowItemList() {
		try {
			String sql = "SELECT `cashFlowItemId`,`cashFlowItem`,`cashFlowItemTypeId` FROM `cashflowitem`";
			RowMapper<CashFlowItem> rowMapper = new BeanPropertyRowMapper<CashFlowItem>(CashFlowItem.class);
			List<CashFlowItem> cashFlowItem = jdbcTemplate.query(sql, rowMapper);
			return cashFlowItem;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<PriorityItem> fetchPriorityItemList() {
		try {
			String sql = "SELECT `priorityItemId`,`priorityItem` FROM `priorityitem`";
			RowMapper<PriorityItem> rowMapper = new BeanPropertyRowMapper<PriorityItem>(PriorityItem.class);
			List<PriorityItem> priorityItem = jdbcTemplate.query(sql, rowMapper);
			return priorityItem;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Urgency> fetchUrgencyList() {
		try {
			String sql = "SELECT `urgencyId`,`value` FROM `urgency`";
			RowMapper<Urgency> rowMapper = new BeanPropertyRowMapper<Urgency>(Urgency.class);
			List<Urgency> urgency = jdbcTemplate.query(sql, rowMapper);
			return urgency;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<RiskPortfolio> fetchRiskPortfolioList() {
		try {
			String sql = "SELECT `riskPortfolioId`,`points`,`behaviour`,`equity`,`debt`,`cash` FROM `riskportfolio`";
			RowMapper<RiskPortfolio> rowMapper = new BeanPropertyRowMapper<RiskPortfolio>(RiskPortfolio.class);
			List<RiskPortfolio> riskPortfolio = jdbcTemplate.query(sql, rowMapper);
			return riskPortfolio;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<RiskQuestionaire> fetchRiskQuestionaireList() {
		try {
			String sql = "SELECT `answerId`,`answer`,`question`,`questionId`,`score` FROM `riskquestionaire`";
			RowMapper<RiskQuestionaire> rowMapper = new BeanPropertyRowMapper<RiskQuestionaire>(RiskQuestionaire.class);
			List<RiskQuestionaire> riskQuestionaire = jdbcTemplate.query(sql, rowMapper);
			return riskQuestionaire;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addEmiCalculator(EmiCalculator emiCalculator) {
		try {
			String sql = "INSERT INTO `emicalculator` (`referenceId`,`loanAmount`,`tenure`,`tenureType`,`interestRate`,`date`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, emiCalculator.getReferenceId(), emiCalculator.getLoanAmount(),
					emiCalculator.getTenure(), emiCalculator.getTenureType(), emiCalculator.getInterestRate(),
					emiCalculator.getDate(), emiCalculator.getCreated(), emiCalculator.getCreated_by(),
					emiCalculator.getUpdated(), emiCalculator.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addEmiCapacity(EmiCapacity emiCapacity) {
		try {
			String sql = "INSERT INTO `emicapacity` (`referenceId`,`currentAge`,`retirementAge`,`stability`,`backUp`,`netFamilyIncome`,`existingEmi`,`houseHoldExpense`,`additionalIncome`,`interestRate`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, emiCapacity.getReferenceId(), emiCapacity.getCurrentAge(),
					emiCapacity.getRetirementAge(), emiCapacity.getStability(), emiCapacity.getBackUp(),
					emiCapacity.getNetFamilyIncome(), emiCapacity.getExistingEmi(), emiCapacity.getHouseHoldExpense(),
					emiCapacity.getAdditionalIncome(), emiCapacity.getInterestRate(), emiCapacity.getCreated(),
					emiCapacity.getCreated_by(), emiCapacity.getUpdated(), emiCapacity.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addPartialPayment(PartialPayment partialPayment) {
		try {
			String sql = "INSERT INTO `partialpayment` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`partPayDate`,`partPayAmount`,`created`,`created_by`,`updated`,`updated_by`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, partialPayment.getReferenceId(), partialPayment.getLoanAmount(),
					partialPayment.getInterestRate(), partialPayment.getTenure(), partialPayment.getTenureType(),
					partialPayment.getLoanDate(), partialPayment.getPartPayDate(), partialPayment.getPartPayAmount(),
					partialPayment.getCreated(), partialPayment.getCreated_by(), partialPayment.getUpdated(),
					partialPayment.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addInterestChange(InterestChange interestChange) {
		try {
			String sql = "INSERT INTO `interestchange` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`changedRate`,`interestChangedDate`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, interestChange.getReferenceId(), interestChange.getLoanAmount(),
					interestChange.getInterestRate(), interestChange.getTenure(), interestChange.getTenureType(),
					interestChange.getLoanDate(), interestChange.getChangedRate(),
					interestChange.getInterestChangedDate(), interestChange.getCreated(),
					interestChange.getCreated_by(), interestChange.getUpdated(), interestChange.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addEmiChange(EmiChange emiChange) {
		try {
			String sql = "INSERT INTO `emichange` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`increasedEmi`,`emiChangedDate`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, emiChange.getReferenceId(), emiChange.getLoanAmount(),
					emiChange.getInterestRate(), emiChange.getTenure(), emiChange.getTenureType(),
					emiChange.getLoanDate(), emiChange.getIncreasedEmi(), emiChange.getEmiChangedDate(),
					emiChange.getCreated(), emiChange.getCreated_by(), emiChange.getUpdated(),
					emiChange.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addEmiInterestChange(EmiInterestChange emiInterestChange) {
		try {
			String sql = "INSERT INTO `emiinterestchange` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`increasedEmi`,`changedRate`,`changedDate`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, emiInterestChange.getReferenceId(), emiInterestChange.getLoanAmount(),
					emiInterestChange.getInterestRate(), emiInterestChange.getTenure(),
					emiInterestChange.getTenureType(), emiInterestChange.getLoanDate(),
					emiInterestChange.getIncreasedEmi(), emiInterestChange.getChangedRate(),
					emiInterestChange.getChangedDate(), emiInterestChange.getCreated(),
					emiInterestChange.getCreated_by(), emiInterestChange.getUpdated(),
					emiInterestChange.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public PriorityItem fetchPriorityItemByItemId(long priorityItemId) {
		try {
			String sql = "SELECT `priorityItemId`,`priorityItem` FROM `priorityitem` WHERE `priorityItemId`=?";
			RowMapper<PriorityItem> rowMapper = new BeanPropertyRowMapper<PriorityItem>(PriorityItem.class);
			PriorityItem priorityItem = jdbcTemplate.queryForObject(sql, rowMapper, priorityItemId);
			return priorityItem;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public RiskProfile fetchRiskProfileByRefIdAndAnswerId(String referenceId, long answerId) {
		try {
			String sql = "SELECT `riskProfileId`,`referenceId`,`questionId`,`answerId`,`score`,`created`,`updated`,`created_by`,`updated_by` FROM `riskprofile` WHERE `referenceId`=? AND `answerId`=?";
			RowMapper<RiskProfile> rowMapper = new BeanPropertyRowMapper<RiskProfile>(RiskProfile.class);
			RiskProfile riskProfile = jdbcTemplate.queryForObject(sql, rowMapper, referenceId, answerId);
			return riskProfile;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Networth fetchNetworthByRefIdAndEntryId(String referenceId, long accountEntryId) {
		try {
			String sql = "SELECT `networthId`,`accountEntryId`,`value`,`futureValue`,`referenceId`,`accountTypeId`,`created`,`updated`,`created_by`,`updated_by` FROM `networth` WHERE `referenceId`=? AND `accountEntryId`=?";
			RowMapper<Networth> rowMapper = new BeanPropertyRowMapper<Networth>(Networth.class);
			Networth networth = jdbcTemplate.queryForObject(sql, rowMapper, referenceId, accountEntryId);
			String sql1 = "SELECT `accountEntry` FROM `account` WHERE `accountEntryId`= ?";
			networth.setAccountEntry(jdbcTemplate.queryForObject(sql1, String.class, networth.getAccountEntryId()));
			String sql2 = "SELECT `accountType` FROM `accounttype` WHERE `accountTypeId`= ?";
			networth.setAccountType(jdbcTemplate.queryForObject(sql2, String.class, networth.getAccountTypeId()));
			return networth;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public AccountType fetchAccountTypeByTypeId(long accountTypeId) {
		try {
			String sql = "SELECT `accountTypeId`,`accountType` FROM `accounttype` WHERE `accountTypeId`=?";
			RowMapper<AccountType> rowMapper = new BeanPropertyRowMapper<AccountType>(AccountType.class);
			AccountType accountType = jdbcTemplate.queryForObject(sql, rowMapper, accountTypeId);
			return accountType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public CashFlow fetchCashFlowByRefIdAndItemId(String referenceId, long cashFlowItemId) {
		try {
			String sql = "SELECT `cashFlowId`,`referenceId`,`cashFlowItemId`,`budgetAmt`,`actualAmt`,`date`,`cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by` FROM `cashflow` WHERE `referenceId`=? AND `cashFlowItemId`=?";
			RowMapper<CashFlow> rowMapper = new BeanPropertyRowMapper<CashFlow>(CashFlow.class);
			CashFlow cashFlow = jdbcTemplate.queryForObject(sql, rowMapper, referenceId, cashFlowItemId);
			String sql1 = "SELECT `cashFlowItem` FROM `cashflowitem` WHERE `cashFlowItemId`= ?";
			cashFlow.setCashFlowItem(jdbcTemplate.queryForObject(sql1, String.class, cashFlow.getCashFlowItemId()));
			String sql2 = "SELECT `cashFlowItemType` FROM `cashflowitemtype` WHERE `cashFlowItemTypeId`= ?";
			cashFlow.setCashFlowItemType(
					jdbcTemplate.queryForObject(sql2, String.class, cashFlow.getCashFlowItemTypeId()));
			return cashFlow;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public CashFlowItemType fetchCashFlowItemTypeByTypeId(long cashFlowItemTypeId) {
		try {
			String sql = "SELECT `cashFlowItemTypeId`,`cashFlowItemType` FROM `cashflowitemtype` WHERE `cashFlowItemTypeId`=?";
			RowMapper<CashFlowItemType> rowMapper = new BeanPropertyRowMapper<CashFlowItemType>(CashFlowItemType.class);
			CashFlowItemType cashFlowItemType = jdbcTemplate.queryForObject(sql, rowMapper, cashFlowItemTypeId);
			return cashFlowItemType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public EmiCalculator fetchEmiCalculatorByRefId(String referenceId) {
		try {
			String sql = "SELECT `emiCalculatorId`,`referenceId`,`loanAmount`,`tenure`,`tenureType`,`interestRate`,`date`,`created`,`updated`,`created_by`,`updated_by` FROM `emicalculator` WHERE `referenceId`= ?";
			RowMapper<EmiCalculator> rowMapper = new BeanPropertyRowMapper<EmiCalculator>(EmiCalculator.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public EmiCapacity fetchEmiCapacityByRefId(String referenceId) {
		try {
			String sql = "SELECT `emiCapacityId`,`referenceId`,`currentAge`,`retirementAge`,`stability`,`backUp`,`netFamilyIncome`,`existingEmi`,`houseHoldExpense`,`additionalIncome`,`interestRate`,`created`,`updated`,`created_by`,`updated_by` FROM `emicapacity` WHERE `referenceId`= ?";
			RowMapper<EmiCapacity> rowMapper = new BeanPropertyRowMapper<EmiCapacity>(EmiCapacity.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<PartialPayment> fetchPartialPaymentByRefId(String referenceId) {
		try {
			String sql = "SELECT `partialPaymentId`,`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`partPayDate`,`partPayAmount`,`created`,`updated`,`created_by`,`updated_by` FROM `partialpayment` WHERE `referenceId`= ?";
			RowMapper<PartialPayment> rowMapper = new BeanPropertyRowMapper<PartialPayment>(PartialPayment.class);
			return jdbcTemplate.query(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<InterestChange> fetchInterestChangeByRefId(String referenceId) {
		try {
			String sql = "SELECT `interestChangeId`,`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`changedRate`,`interestChangedDate`,`created`,`updated`,`created_by`,`updated_by` FROM `interestchange` WHERE `referenceId`= ?";
			RowMapper<InterestChange> rowMapper = new BeanPropertyRowMapper<InterestChange>(InterestChange.class);
			return jdbcTemplate.query(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<EmiChange> fetchEmiChangeByRefId(String referenceId) {
		try {
			String sql = "SELECT `emiChangeId`,`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`increasedEmi`,`emiChangedDate`,`created`,`updated`,`created_by`,`updated_by` FROM `emichange` WHERE `referenceId`= ?";
			RowMapper<EmiChange> rowMapper = new BeanPropertyRowMapper<EmiChange>(EmiChange.class);
			return jdbcTemplate.query(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<EmiInterestChange> fetchEmiInterestChangeByRefId(String referenceId) {
		try {
			String sql = "SELECT `emiIntChangeId`,`referenceId`,`loanAmount`,`interestRate`,`tenure`,`tenureType`,`loanDate`,`increasedEmi`,`changedRate`,`changedDate`,`created`,`updated`,`created_by`,`updated_by` FROM `emiinterestchange` WHERE `referenceId`= ?";
			RowMapper<EmiInterestChange> rowMapper = new BeanPropertyRowMapper<EmiInterestChange>(
					EmiInterestChange.class);
			return jdbcTemplate.query(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchValueByInsuranceItem(String insuranceItem) {
		try {
			String sql1 = "SELECT `value` FROM `insuranceitem` WHERE `insuranceItem`= ?";
			String label = jdbcTemplate.queryForObject(sql1, String.class, insuranceItem);
			return label;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addPlanInfo(Plan plan, String encryptPass) {
		try {
			String sql = "INSERT INTO `plan` (`partyId`,`parentPartyId`,`referenceId`,`name`,`age`,`selectedPlan`,`spouse`,`father`,`mother`,`child1`,`child2`,`child3`,`inLaws`,`grandParent`,`sibilings`,`others`,`created`,`created_by`,`updated`,`updated_by`) VALUES (?,?,?,ENCODE(?,?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, plan.getPartyId(), plan.getParentPartyId(), plan.getReferenceId(),
					plan.getName(), encryptPass, plan.getAge(), plan.getSelectedPlan(), plan.getSpouse(),
					plan.getFather(), plan.getMother(), plan.getChild1(), plan.getChild2(), plan.getChild3(),
					plan.getInLaws(), plan.getGrandParent(), plan.getSibilings(), plan.getOthers(), plan.getCreated(),
					plan.getCreated_by(), plan.getUpdated(), plan.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchRoleBasedIdByPartyId(long partyId) {
		try {
			String sql1 = "SELECT `roleBasedId` FROM `party` WHERE `partyId`= ?";
			String refId = jdbcTemplate.queryForObject(sql1, String.class, partyId);
			return refId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Plan fetchPlanByReferenceId(String id, String encryptPass) {
		try {
			String sql = "SELECT `planId`,`partyId`,`parentPartyId`,`referenceId`,DECODE(`name`,?) name,`age`,`selectedPlan`,`spouse`,`father`,`mother`,`child1`,`child2`,`child3`,`inLaws`,`grandParent`,`sibilings`,`others`,`created`,`updated`,`created_by`,`updated_by` FROM `plan` WHERE `referenceId`= ?";
			RowMapper<Plan> rowMapper = new BeanPropertyRowMapper<Plan>(Plan.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateCashFlow(CashFlow cashFlow) {
		try {
			String sql = "UPDATE `cashflow` SET `budgetAmt`=? ,`actualAmt`=?,`date`=?, `cashFlowItemTypeId`=? ,`updated`=? ,`updated_by`=? WHERE `referenceId`=? AND `cashFlowItemId`=?";
			int result = jdbcTemplate.update(sql, cashFlow.getBudgetAmt(), cashFlow.getActualAmt(), cashFlow.getDate(),
					cashFlow.getCashFlowItemTypeId(), cashFlow.getUpdated(), cashFlow.getUpdated_by(),
					cashFlow.getReferenceId(), cashFlow.getCashFlowItemId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeCashFlowSummary(String referenceId) {
		try {
			String sql = "DELETE FROM `cashflowsummary` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateNetworth(Networth networth) {
		try {
			String sql = "UPDATE `networth` SET `value`=? ,`futureValue`=?,`accountTypeId`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? AND `accountEntryId`=?";
			int result = jdbcTemplate.update(sql, networth.getValue(), networth.getFutureValue(),
					networth.getAccountTypeId(), networth.getUpdated(), networth.getUpdated_by(),
					networth.getReferenceId(), networth.getAccountEntryId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeNetworthSummary(String referenceId) {
		try {
			String sql = "DELETE FROM `networthsummary` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updatePriority(Priority priority) {
		try {
			String sql = "UPDATE `priority` SET  `urgencyId`=?, `priorityOrder`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? AND `priorityItemId`=?";
			int result = jdbcTemplate.update(sql, priority.getUrgencyId(), priority.getPriorityOrder(),
					priority.getUpdated(), priority.getUpdated_by(), priority.getReferenceId(),
					priority.getPriorityItemId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateInsurance(Insurance insurance) {
		try {
			String sql = "UPDATE `insurance` SET `annualIncome`=? ,`stability`=?,`predictability`=?, `requiredInsurance`=? ,`existingInsurance`=?,`additionalInsurance`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, insurance.getAnnualIncome(), insurance.getStability(),
					insurance.getPredictability(), insurance.getRequiredInsurance(), insurance.getExistingInsurance(),
					insurance.getAdditionalInsurance(), insurance.getUpdated(), insurance.getUpdated_by(),
					insurance.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateRiskProfile(RiskProfile riskProfile) {
		try {
			String sql = "UPDATE `riskprofile` SET `questionId`=? ,`answerId`=? , `score`=?, `updated`=?,`updated_by`=? WHERE `referenceId`=? AND `questionId`=?";
			int result = jdbcTemplate.update(sql, riskProfile.getQuestionId(), riskProfile.getAnswerId(),
					riskProfile.getScore(), riskProfile.getUpdated(), riskProfile.getUpdated_by(),
					riskProfile.getReferenceId(), riskProfile.getQuestionId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeRiskSummary(String referenceId) {
		try {
			String sql = "DELETE FROM `risksummary` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateEmiCalculator(EmiCalculator emiCalculator) {
		try {
			String sql = "UPDATE `emicalculator` SET `loanAmount`=? ,`tenure`=?, `tenureType`=?,`interestRate`=?,`date`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? ";
			int result = jdbcTemplate.update(sql, emiCalculator.getLoanAmount(), emiCalculator.getTenure(),
					emiCalculator.getTenureType(), emiCalculator.getInterestRate(), emiCalculator.getDate(),
					emiCalculator.getUpdated(), emiCalculator.getUpdated_by(), emiCalculator.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateEmiCapacity(EmiCapacity emiCapacity) {
		try {
			String sql = "UPDATE `emicapacity` SET `currentAge`=? ,`retirementAge`=?, `stability`=?,`backUp`=?, `netFamilyIncome`=?, `existingEmi`=?, `houseHoldExpense`=?, `additionalIncome`=?, `interestRate`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? ";
			int result = jdbcTemplate.update(sql, emiCapacity.getCurrentAge(), emiCapacity.getRetirementAge(),
					emiCapacity.getStability(), emiCapacity.getBackUp(), emiCapacity.getNetFamilyIncome(),
					emiCapacity.getExistingEmi(), emiCapacity.getHouseHoldExpense(), emiCapacity.getAdditionalIncome(),
					emiCapacity.getInterestRate(), emiCapacity.getUpdated(), emiCapacity.getUpdated_by(),
					emiCapacity.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public RiskProfile fetchRiskProfileByRefIdAndQuestionId(String referenceId, String questionId) {
		try {
			String sql = "SELECT `riskProfileId`,`referenceId`,`questionId`,`answerId`,`score`,`created`,`updated`,`created_by`,`updated_by` FROM `riskprofile` WHERE `referenceId`=? AND `questionId`=?";
			RowMapper<RiskProfile> rowMapper = new BeanPropertyRowMapper<RiskProfile>(RiskProfile.class);
			RiskProfile riskProfile = jdbcTemplate.queryForObject(sql, rowMapper, referenceId, questionId);
			return riskProfile;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchQuestionIdFromRiskQuestionaire() {
		try {
			String sql = "SELECT DISTINCT `questionId` FROM `riskquestionaire`";
			List<String> questionId = jdbcTemplate.queryForList(sql, String.class);
			return questionId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<RiskQuestionaire> fetchRiskQuestionaireByQuestionId(String questionId) {
		try {
			String sql = "SELECT `answerId`,`answer`,`question`,`questionId`,`score` FROM `riskquestionaire` WHERE `questionId`= ?";
			RowMapper<RiskQuestionaire> rowMapper = new BeanPropertyRowMapper<RiskQuestionaire>(RiskQuestionaire.class);
			return jdbcTemplate.query(sql, rowMapper, questionId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public String fetchQuestionByQuestionId(String questionId) {
		try {
			String sql = "SELECT DISTINCT `question` FROM `riskquestionaire` WHERE `questionId`=?";
			String question = jdbcTemplate.queryForObject(sql, String.class, questionId);
			return question;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Goal fetchGoalByRefIdAndGoalName(String referenceId, String goalName) {
		try {
			String sql = "SELECT `goalId`,`referenceId`,`goalName`, `tenure`,`tenureType`, `goalAmount`, `inflationRate`,`currentAmount`,`growthRate`,`rateOfReturn`,`annualInvestmentRate`,`futureCost`,`futureValue`,`finalCorpus`,`monthlyInv`,`annualInv`,`created`,`updated`,`created_by`,`updated_by` FROM `goal` WHERE `referenceId`=? AND `goalName`=?";
			RowMapper<Goal> rowMapper = new BeanPropertyRowMapper<Goal>(Goal.class);
			Goal goal = jdbcTemplate.queryForObject(sql, rowMapper, referenceId, goalName);
			return goal;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateGoalInfo(Goal goal) {
		try {
			String sql = "UPDATE `goal` SET `tenure`=? ,`tenureType`=?, `goalAmount`=?,`inflationRate`=?, `currentAmount`=?, `growthRate`=?, `rateOfReturn`=?, `annualInvestmentRate`=?, `futureCost`=?,`futureValue`=?,`finalCorpus`=?,`monthlyInv`=?,`annualInv`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? AND `goalName`=?";
			int result = jdbcTemplate.update(sql, goal.getTenure(), goal.getTenureType(), goal.getGoalAmount(),
					goal.getInflationRate(), goal.getCurrentAmount(), goal.getGrowthRate(), goal.getRateOfReturn(),
					goal.getAnnualInvestmentRate(), goal.getFutureCost(), goal.getFutureValue(), goal.getFinalCorpus(),
					goal.getMonthlyInv(), goal.getAnnualInv(), goal.getUpdated(), goal.getUpdated_by(),
					goal.getReferenceId(), goal.getGoalName());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePartialPaymentByRefId(String referenceId) {
		try {
			String sql = "DELETE FROM `partialpayment` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeEmiChangeByRefId(String referenceId) {
		try {
			String sql = "DELETE FROM `emichange` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeInterestChangeByRefId(String referenceId) {
		try {
			String sql = "DELETE FROM `interestchange` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeEmiInterestChangeByRefId(String referenceId) {
		try {
			String sql = "DELETE FROM `emiinterestchange` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateCashFlowSummary(CashFlowSummary cashFlowSummary) {
		try {
			String sql1 = "UPDATE `cashflowsummary` SET `monthlyExpense`=?,`yearlyExpense`=?,`monthlyIncome`=?,`yearlyIncome`=?,`monthlyNetCashFlow`=?,`yearlyNetCashFlow`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql1, cashFlowSummary.getMonthlyExpense(),
					cashFlowSummary.getYearlyExpense(), cashFlowSummary.getMonthlyIncome(),
					cashFlowSummary.getYearlyIncome(), cashFlowSummary.getMonthlyNetCashFlow(),
					cashFlowSummary.getYearlyNetCashFlow(), cashFlowSummary.getUpdated(),
					cashFlowSummary.getUpdated_by(), cashFlowSummary.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateNetworthSummary(NetworthSummary networthSummary) {
		try {
			String sql = "UPDATE `networthsummary` SET `current_assetValue`=?,`current_liability`=?,`networth`=?,`future_assetValue`=?,`future_liability`=?,`future_networth`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, networthSummary.getCurrent_assetValue(),
					networthSummary.getCurrent_liability(), networthSummary.getNetworth(),
					networthSummary.getFuture_assetValue(), networthSummary.getFuture_liability(),
					networthSummary.getFuture_networth(), networthSummary.getUpdated(), networthSummary.getUpdated_by(),
					networthSummary.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateRiskSummary(RiskSummary riskSummary) {
		try {
			String sql = "UPDATE `risksummary` SET `behaviour`=?,`eqty_alloc`=?,`debt_alloc`=?,`cash_alloc`=?,`updated`=?,`updated_by`=?  WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, riskSummary.getBehaviour(), riskSummary.getEqty_alloc(),
					riskSummary.getDebt_alloc(), riskSummary.getCash_alloc(), riskSummary.getUpdated(),
					riskSummary.getUpdated_by(), riskSummary.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeInsuranceByRefId(String referenceId) {
		try {
			String sql = "DELETE FROM `insurance` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePriorityByRefIdAndItemId(String referenceId, int priorityItemId) {
		try {
			String sql = "DELETE FROM `priority` WHERE `referenceId`=? AND `priorityItemId`=?";
			int result = jdbcTemplate.update(sql, referenceId, priorityItemId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchPlanReferenceId() {
		try {
			String sql1 = "SELECT `referenceId` FROM `planreferenceid` ORDER BY `s_no` DESC LIMIT 1";
			return jdbcTemplate.queryForObject(sql1, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addPlanReferenceId(String newId) {
		try {
			String sqlInsert = "INSERT INTO `planreferenceid` (`referenceId`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Plan> fetchPlanByPartyId(long partyId, String encryptPass) {
		try {
			String sql = "SELECT `planId`,`partyId`,`parentPartyId`,`referenceId`,DECODE(`name`,?) name,`age`,`selectedPlan`,`spouse`,`father`,`mother`,`child1`,`child2`,`child3`,`inLaws`,`grandParent`,`sibilings`,`others`,`created`,`updated`,`created_by`,`updated_by` FROM `plan` WHERE `partyId`= ? OR `parentPartyId`=?";
			RowMapper<Plan> rowMapper = new BeanPropertyRowMapper<Plan>(Plan.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, partyId, partyId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyPlanInfo(Plan plan, String referenceId, String encryptPass) {
		try {
			String sql = "UPDATE `plan` SET `name` = ENCODE(?,?),`age` = ?,`selectedPlan` = ?,`spouse` = ?,`father` = ?,`mother` = ?,`child1` = ?,`child2` = ?,`child3` = ?,`inLaws` = ?,`grandParent` = ?,`sibilings` = ?,`others` = ?,`updated`=?,`updated_by`=? WHERE `referenceId` = ?";
			int result = jdbcTemplate.update(sql, plan.getName(), encryptPass, plan.getAge(), plan.getSelectedPlan(),
					plan.getSpouse(), plan.getFather(), plan.getMother(), plan.getChild1(), plan.getChild2(),
					plan.getChild3(), plan.getInLaws(), plan.getGrandParent(), plan.getSibilings(), plan.getOthers(),
					plan.getUpdated(), plan.getUpdated_by(), referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePlanInfo(String referenceId) {
		try {
			String sql = "DELETE FROM `plan` WHERE `referenceId`=?";
			int result = jdbcTemplate.update(sql, referenceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}
	//
	// @Override
	// public List<Plan> fetchPlanBySuperParentId(long superParentId, String
	// encryptPass) {
	// try {
	// String sql = "SELECT
	// `planId`,`partyId`,`parentPartyId`,`superParentId`,`referenceId`,DECODE(`name`,?),`age`,`selectedPlan`,`spouse`,`father`,`mother`,`child1`,`child2`,`child3`,`inLaws`,`grandParent`,`sibilings`,`others`
	// FROM `plan` WHERE `superParentId`=?";
	// RowMapper<Plan> rowMapper = new BeanPropertyRowMapper<Plan>(Plan.class);
	// return jdbcTemplate.query(sql, rowMapper, encryptPass, superParentId);
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public String fetchEmailIdByPartyId(long partyId, String encryptPass) {
		try {
			String sql = "SELECT DECODE(`emailId`,?) emailId FROM `party` WHERE `partyId` = ?";
			String result = jdbcTemplate.queryForObject(sql, String.class, encryptPass, partyId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public void removeCashFlow(String referenceId) {
		try {
			String sql = "DELETE FROM `cashflow` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeNetworth(String referenceId) {
		try {
			String sql = "DELETE FROM `networth` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removePriority(String referenceId) {
		try {
			String sql = "DELETE FROM `priority` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeEmiCapacity(String referenceId) {
		try {
			String sql = "DELETE FROM `emicapacity` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeEmiCalculator(String referenceId) {
		try {
			String sql = "DELETE FROM `emicalculator` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeGoal(String referenceId) {
		try {
			String sql = "DELETE FROM `goal` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeRiskProfile(String referenceId) {
		try {
			String sql = "DELETE FROM `riskprofile` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `delete_flag`=? AND (DECODE(`emailId`,?) = ? OR DECODE(`panNumber`,?)=? OR DECODE(`phoneNumber`,?)=? OR DECODE(`userName`,?)=?)";
			RowMapper<Party> partyMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					delete_flag, encryptPass, username, encryptPass, username, encryptPass, username, encryptPass,
					username);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int checkPlanIsPresentByReferenceId(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `plan` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkGoalIsPresentByRefIdAndGoalName(String referenceId, String goalName) {
		try {
			String sql = "SELECT count(*) FROM `goal` WHERE `referenceId`=? AND `goalName`=?";
			int goal = jdbcTemplate.queryForObject(sql, Integer.class, referenceId, goalName);
			return goal;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkCashFlowIsPresent(String referenceId, int cashFlowItemId) {
		try {
			String sql = "SELECT count(*) FROM `cashflow` WHERE `referenceId`= ? AND `cashFlowItemId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId, cashFlowItemId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkCashFlowSummaryIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `cashflowsummary` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkNetworthIsPresent(String referenceId, int accountEntryId) {
		try {
			String sql = "SELECT count(*) FROM `networth` WHERE `referenceId`= ? AND `accountEntryId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId, accountEntryId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkNetworthSummaryIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `networthsummary` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkPriorityByRefIdAndItemId(String referenceId, int priorityItemId) {
		try {
			String sql = "SELECT count(*) FROM `priority` WHERE `referenceId`=? AND `priorityItemId` = ?";
			int priorityItem = jdbcTemplate.queryForObject(sql, Integer.class, referenceId, priorityItemId);
			return priorityItem;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkInsuranceByRefId(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `insurance` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkRiskProfileIsPresent(String referenceId, String questionId) {
		try {
			String sql = "SELECT count(*) FROM `riskprofile` WHERE `referenceId`=? AND `questionId`=?";
			int riskProfile = jdbcTemplate.queryForObject(sql, Integer.class, referenceId, questionId);
			return riskProfile;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkRiskSummaryIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `risksummary` WHERE `referenceId`= ?";
			int riskSummary = jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
			return riskSummary;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkEmiCalculatorIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `emicalculator` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkemiCapacityIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `emicapacity` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkpartialIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*)  FROM `partialpayment` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkEmiChangeIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `emichange` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkInterestChangeIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `interestchange` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkForumSubCategoryIsPresent(long forumSubCategoryId) {
		try {
			String sql = "SELECT count(*) FROM `forumsubcategory` WHERE `forumSubCategoryId` = ?";
			int forumSubCategory = jdbcTemplate.queryForObject(sql, Integer.class, forumSubCategoryId);
			return forumSubCategory;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkPartyIsPresent(long partyId) {
		try {
			String sql = "SELECT count(*) FROM `party` WHERE `partyId` = ?";
			int party = jdbcTemplate.queryForObject(sql, Integer.class, partyId);
			return party;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int createQuery(CalcQuery thread) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `calcquery` (`name`,`displayName`,`planName`,`age`,`phoneNumber`,`emailId`,`referenceId`,`partyId`,`postedToPartyId`,`receiverName`,`plans`,`url`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?, ?, ?, ?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, thread.getName(), thread.getDisplayName(), thread.getPlanName(),
					thread.getAge(), thread.getPhoneNumber(), thread.getEmailId(), thread.getReferenceId(),
					thread.getPartyId(), thread.getPostedToPartyId(), thread.getReceiverName(), thread.getPlans(),
					thread.getUrl(), timestamp, timestamp, thread.getDelete_flag(), thread.getCreated_by(),
					thread.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int createAnswer(CalcAnswer answer) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `calcanswer` (`queryId`,`answer`,`partyId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, answer.getQueryId(), answer.getAnswer(), answer.getPartyId(),
					timestamp, timestamp, answer.getDelete_flag(), answer.getCreated_by(), answer.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkSharedAdvisor(String referenceId, long partyId, String delete_flag) {
		try {
			String sql = "SELECT count(*) FROM `calcquery` WHERE `referenceId` = ? AND `postedToPartyId` = ? AND `delete_flag` = ?";
			int forumPost = jdbcTemplate.queryForObject(sql, Integer.class, referenceId, partyId, delete_flag);
			return forumPost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkFutureValueIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `futurevalue` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addFutureValue(FutureValue futureValue) {
		try {
			String sql = "INSERT INTO `futurevalue` (`referenceId`,`invType`,`invAmount`,`duration`,`durationType`,`annualGrowth`,`totalPayment`,`created`,`updated`,`created_by`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, futureValue.getReferenceId(), futureValue.getInvType(),
					futureValue.getInvAmount(), futureValue.getDuration(), futureValue.getDurationType(),
					futureValue.getAnnualGrowth(), futureValue.getTotalPayment(), futureValue.getCreated(),
					futureValue.getUpdated(), futureValue.getCreated_by(), futureValue.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateFutureValue(FutureValue futureValue) {
		try {
			String sql = "UPDATE `futurevalue` SET `invType`=? ,`invAmount`=?, `duration`=?,`durationType`=?,`annualGrowth`=?,`totalPayment`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? ";
			int result = jdbcTemplate.update(sql, futureValue.getInvType(), futureValue.getInvAmount(),
					futureValue.getDuration(), futureValue.getDurationType(), futureValue.getAnnualGrowth(),
					futureValue.getTotalPayment(), futureValue.getUpdated(), futureValue.getUpdated_by(),
					futureValue.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkTargetValueIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `targetvalue` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addTargetValue(TargetValue targetValue) {
		try {
			String sql = "INSERT INTO `targetvalue` (`referenceId`,`invType`,`futureValue`,`rateOfInterest`,`duration`,`durationType`,`totalPayment`,`created`,`updated`,`created_by`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, targetValue.getReferenceId(), targetValue.getInvType(),
					targetValue.getFutureValue(), targetValue.getRateOfInterest(), targetValue.getDuration(),
					targetValue.getDurationType(), targetValue.getTotalPayment(), targetValue.getCreated(),
					targetValue.getUpdated(), targetValue.getCreated_by(), targetValue.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateTargetValue(TargetValue targetValue) {
		try {
			String sql = "UPDATE `targetvalue` SET `invType`=? ,`futureValue`=?, `rateOfInterest`=?, `duration`=?,`durationType`=?,`totalPayment`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? ";
			int result = jdbcTemplate.update(sql, targetValue.getInvType(), targetValue.getFutureValue(),
					targetValue.getRateOfInterest(), targetValue.getDuration(), targetValue.getDurationType(),
					targetValue.getTotalPayment(), targetValue.getUpdated(), targetValue.getUpdated_by(),
					targetValue.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkRateFinderIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `ratefinder` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addRateFinder(RateFinder rateFinder) {
		try {
			String sql = "INSERT INTO `ratefinder` (`referenceId`,`invType`,`presentValue`,`futureValue`,`duration`,`durationType`,`rateOfInterest`,`created`,`updated`,`created_by`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, rateFinder.getReferenceId(), rateFinder.getInvType(),
					rateFinder.getPresentValue(), rateFinder.getFutureValue(), rateFinder.getDuration(),
					rateFinder.getDurationType(), rateFinder.getRateOfInterest(), rateFinder.getCreated(),
					rateFinder.getUpdated(), rateFinder.getCreated_by(), rateFinder.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateRateFinder(RateFinder rateFinder) {
		try {
			String sql = "UPDATE `ratefinder` SET `invType`=? ,`presentValue`=?,`futureValue`=?, `duration`=?,`durationType`=?,`rateOfInterest`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? ";
			int result = jdbcTemplate.update(sql, rateFinder.getInvType(), rateFinder.getPresentValue(),
					rateFinder.getFutureValue(), rateFinder.getDuration(), rateFinder.getDurationType(),
					rateFinder.getRateOfInterest(), rateFinder.getUpdated(), rateFinder.getUpdated_by(),
					rateFinder.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkTenureFinderIsPresent(String referenceId) {
		try {
			String sql = "SELECT count(*) FROM `tenurefinder` WHERE `referenceId`= ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addTenureFinder(TenureFinder tenureFinder) {
		try {
			String sql = "INSERT INTO `tenurefinder` (`referenceId`,`invType`,`presentValue`,`futureValue`,`rateOfInterest`,`tenure`,`created`,`updated`,`created_by`,`updated_by`) VALUES (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, tenureFinder.getReferenceId(), tenureFinder.getInvType(),
					tenureFinder.getPresentValue(), tenureFinder.getFutureValue(), tenureFinder.getRateOfInterest(),
					tenureFinder.getTenure(), tenureFinder.getCreated(), tenureFinder.getUpdated(),
					tenureFinder.getCreated_by(), tenureFinder.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateTenureFinder(TenureFinder tenureFinder) {
		try {
			String sql = "UPDATE `tenurefinder` SET `invType`=? ,`presentValue`=?,`futureValue`=?,`rateOfInterest`=?,`tenure`=?,`updated`=?,`updated_by`=? WHERE `referenceId`=? ";
			int result = jdbcTemplate.update(sql, tenureFinder.getInvType(), tenureFinder.getPresentValue(),
					tenureFinder.getFutureValue(), tenureFinder.getRateOfInterest(), tenureFinder.getTenure(),
					tenureFinder.getUpdated(), tenureFinder.getUpdated_by(), tenureFinder.getReferenceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public FutureValue fetchFutureValueByRefId(String referenceId) {
		try {
			String sql = "SELECT `futureValueId`,`referenceId`,`invType`,`invAmount`,`duration`,`durationType`,`annualGrowth`,`totalPayment`,`created`,`updated`,`created_by`,`updated_by` FROM `futurevalue` WHERE `referenceId`= ?";
			RowMapper<FutureValue> rowMapper = new BeanPropertyRowMapper<FutureValue>(FutureValue.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public TargetValue fetchTargetValueByRefId(String referenceId) {
		try {
			String sql = "SELECT `targetValueId`,`referenceId`,`invType`,`futureValue`,`rateOfInterest`,`duration`,`durationType`,`totalPayment`,`created`,`updated`,`created_by`,`updated_by` FROM `targetvalue` WHERE `referenceId`= ?";
			RowMapper<TargetValue> rowMapper = new BeanPropertyRowMapper<TargetValue>(TargetValue.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public RateFinder fetchRateFinderByRefId(String referenceId) {
		try {
			String sql = "SELECT `rateFinderId`,`referenceId`,`invType`,`presentValue`,`futureValue`,`duration`,`durationType`,`rateOfInterest`,`created`,`updated`,`created_by`,`updated_by` FROM `ratefinder` WHERE `referenceId`= ?";
			RowMapper<RateFinder> rowMapper = new BeanPropertyRowMapper<RateFinder>(RateFinder.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public TenureFinder fetchTenureFinderByRefId(String referenceId) {
		try {
			String sql = "SELECT `tenureFinderId`,`referenceId`,`invType`,`presentValue`,`futureValue`,`rateOfInterest`,`tenure`,`created`,`updated`,`created_by`,`updated_by` FROM `tenurefinder` WHERE `referenceId`= ?";
			RowMapper<TenureFinder> rowMapper = new BeanPropertyRowMapper<TenureFinder>(TenureFinder.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, referenceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public void removeFutureValue(String referenceId) {
		try {
			String sql = "DELETE FROM `futurevalue` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeTargetValue(String referenceId) {
		try {
			String sql = "DELETE FROM `targetvalue` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeRateFinder(String referenceId) {
		try {
			String sql = "DELETE FROM `ratefinder` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void removeTenureFinder(String referenceId) {
		try {
			String sql = "DELETE FROM `tenurefinder` WHERE `referenceId`=?";
			jdbcTemplate.update(sql, referenceId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public List<CalcQuery> fetchSharedPlanByPostedPartyId(long partyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `calcQueryId`,DECODE(`name`,?) name,`displayName`,`planName`,`age`,`phoneNumber`,`emailId`,`referenceId`,`partyId`,`postedToPartyId`,`receiverName`,`plans`,`url`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag` FROM `calcquery` WHERE `postedToPartyId`= ? AND `delete_flag`=?";
			RowMapper<CalcQuery> rowMapper = new BeanPropertyRowMapper<CalcQuery>(CalcQuery.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, partyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<CalcQuery> fetchSharedPlanByPartyId(long partyId, String referenceId, String deleteflag,
			String encryptPass) {
		try {
			String sql = "SELECT `calcQueryId`,DECODE(`name`,?) name,`displayName`,`planName`,`age`,`phoneNumber`,`emailId`,`referenceId`,`partyId`,`postedToPartyId`,`receiverName`,`plans`,`url`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag` FROM `calcquery` WHERE `partyId`= ? AND `referenceId`=? AND `delete_flag`=? ORDER BY `updated` DESC";
			RowMapper<CalcQuery> rowMapper = new BeanPropertyRowMapper<CalcQuery>(CalcQuery.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, partyId, referenceId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int checkCalcQueryIsPresent(long queryId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `calcquery` WHERE `calcQueryId` = ? AND `delete_flag` = ?";
			int calcQuery = jdbcTemplate.queryForObject(sql, Integer.class, queryId, deleteflag);
			return calcQuery;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkQueriesIsPresent(long id, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `queries` WHERE `queryId` = ? AND `delete_flag` = ?";
			int calcQuery = jdbcTemplate.queryForObject(sql, Integer.class, id, deleteflag);
			return calcQuery;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int createCommentQueries(Queries queries) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `queries` (`calcQueryId`,`query`,`senderId`,`referenceId`,`receiverId`,`plans`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag`) values (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, queries.getCalcQueryId(), queries.getQuery(), queries.getSenderId(),
					queries.getReferenceId(), queries.getReceiverId(), queries.getPlans(), timestamp, timestamp,
					queries.getCreated_by(), queries.getUpdated_by(), queries.getDelete_flag());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Queries> fetchQueries(long senderId, long receiverId, String plans, String delete_flag,
			String encryptPass) {
		try {
			String sql = "SELECT * FROM `queries` WHERE  `delete_flag`=? AND (`senderId`=? AND `receiverId`=? AND `plans`=?) OR (`senderId`=? AND `receiverId`=? AND `plans`=?)";
			RowMapper<Queries> rowMapper = new BeanPropertyRowMapper<Queries>(Queries.class);
			List<Queries> queries = jdbcTemplate.query(sql, rowMapper, delete_flag, senderId, receiverId, plans,
					receiverId, senderId, plans);
			return queries;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Advisor fetchAdvisorByAdvId(String roleBasedId, String deleteflag) {
		try {
			String sql = "SELECT * FROM `advisor` WHERE `advId`=? AND `delete_flag`=?";
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, roleBasedId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Investor fetchInvestorByInvId(String invId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT inv.`invId` invId,DECODE(inv.`fullName`,?) fullName,inv.`displayName` displayName,DECODE(inv.`dob`,?) dob,DECODE(inv.`emailId`,?) emailId,inv.`gender` gender,inv.`password` password,DECODE(inv.`userName`,?) userName,DECODE(inv.`phoneNumber`,?) phoneNumber,inv.`pincode` pincode,inv.`imagePath` imagePath,inv.`partyStatusId` partyStatusId,inv.`created` created,inv.`updated` updated, inv.`created_by` created_by,inv.`updated_by` updated_by, inv.`isVerified` isVerified,inv.`verifiedBy` verifiedBy,inv.`verified` verified,inv.`isMobileVerified` isMobileVerified,inv.`delete_flag` delete_flag,invInt.interestId COL_A,invInt.prodId COL_B,invInt.invId COL_INVID,invInt.scale COL_D,invInt.created COL_E,invInt.updated COL_F,  invInt.created_by COL_G,invInt.updated_by COL_H  ,invInt.delete_flag COL_DELETEFLAG, 'invInt' VALUE FROM investor inv LEFT JOIN invinterest invInt ON (inv.invId = invInt.invId)) AS investor WHERE invId=? AND delete_flag=?";
			return jdbcTemplate.query(sql, new InvestorExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, invId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public CalcQuery fetchCalcQuery(long partyId, String referenceId, String delete_flag, long postedToPartyId,
			String encryptPass) {
		try {
			String sql = "SELECT `calcQueryId`,DECODE(`name`,?) name,`displayName`,`planName`,`age`,`phoneNumber`,`emailId`,`referenceId`,`partyId`,`postedToPartyId`,`receiverName`,`plans`,`url`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag` FROM `calcquery` WHERE `partyId`=? AND `referenceId`=? AND `postedTopartyId`=? AND `delete_flag`=?";
			RowMapper<CalcQuery> rowMapper = new BeanPropertyRowMapper<CalcQuery>(CalcQuery.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, partyId, referenceId, postedToPartyId,
					delete_flag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyCalcQuery(long calcQueryId, String plans, CalcQuery query, Timestamp timestamp,
			String signedUserId) {
		try {
			String sql = "UPDATE `calcquery` SET `plans` = ?,`phoneNumber` =?,`emailId`=?,`updated`=?,`updated_by`=? WHERE `calcQueryId` = ?";
			int result = jdbcTemplate.update(sql, plans, query.getPhoneNumber(), query.getEmailId(), timestamp,
					signedUserId, calcQueryId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyCalcQueryAfterComment(long calcQueryId, String signedUserId) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			String sql = "UPDATE `calcquery` SET `updated`=?,`updated_by`=? WHERE `calcQueryId` = ?";
			int result = jdbcTemplate.update(sql, timestamp, signedUserId, calcQueryId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<CalcQuery> fetchSharedPlanByRefId(String refId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `calcQueryId`,DECODE(`name`,?) name,`displayName`,`planName`,`age`,`phoneNumber`,`emailId`,`referenceId`,`partyId`,`postedToPartyId`,`receiverName`,`plans`,`url`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag` FROM `calcquery` WHERE `referenceId`=? AND `delete_flag`=?";
			RowMapper<CalcQuery> rowMapper = new BeanPropertyRowMapper<CalcQuery>(CalcQuery.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, refId, delete_flag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int removePriority(Priority priority) {
		try {
			String sql = "DELETE FROM `priority` WHERE `referenceId`=? AND `priorityItemId`=?";
			return jdbcTemplate.update(sql, priority.getReferenceId(), priority.getPriorityItemId());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Investor fetchInvestorForCalcQuery(String roleBasedId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * from `investor` where `invId`=? AND `delete_flag`=?";
			RowMapper<Investor> rowMapper = new BeanPropertyRowMapper<Investor>(Investor.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, roleBasedId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
