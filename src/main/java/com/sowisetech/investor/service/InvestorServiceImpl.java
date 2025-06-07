package com.sowisetech.investor.service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;
import com.sowisetech.investor.request.InvInterestRequest;
import com.sowisetech.investor.response.InvResponse;
import com.sowisetech.investor.util.InvTableFields;
import com.sowisetech.investor.util.InvestorConstants;
import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.advisor.model.PlanDetail;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.dao.CalcDao;
import com.sowisetech.calc.model.CashFlowSummary;
import com.sowisetech.calc.model.EmiCalculator;
import com.sowisetech.calc.model.EmiCapacity;
import com.sowisetech.calc.model.EmiChange;
import com.sowisetech.calc.model.FutureValue;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.Insurance;
import com.sowisetech.calc.model.InterestChange;
import com.sowisetech.calc.model.NetworthSummary;
import com.sowisetech.calc.model.PartialPayment;
import com.sowisetech.calc.model.Party;
import com.sowisetech.calc.model.Plan;
import com.sowisetech.calc.model.Priority;
import com.sowisetech.calc.model.RateFinder;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.TenureFinder;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.investor.dao.InvestorDao;

@Transactional(readOnly = true)
@Service("InvestorService")
public class InvestorServiceImpl implements InvestorService {
	@Autowired
	private InvestorDao investorDao;
	@Autowired
	private CalcDao calcDao;
	@Autowired
	InvTableFields invTableFields;
	@Autowired
	AdvTableFields advTableFields;
	@Autowired
	AdminSignin adminSignin;

	// @Transactional
	// public int addInvestor(Investor investor) {
	// String password = encrypt(investor.getPassword());
	// // String desc = "active";
	// long partyStatusId =
	// investorDao.fetchPartyStatusIdByDesc(invTableFields.getDesc());
	// investor.setPassword(password);
	// investor.setPartyStatusId(partyStatusId);
	// int result = investorDao.add(investor, invTableFields.getDelete_flag_N());
	//
	// // String name = "investor";
	// long roleId = investorDao.fetchRoleIdByName(invTableFields.getRoleName());
	// if (result != 0) {
	// int result1 = investorDao.addParty(investor, roleId,
	// invTableFields.getDelete_flag_N());
	// return result1;
	// }
	// return result;
	// }

	@Transactional
	public Investor fetchByInvestorId(String invId) {
		String deleteflag = invTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		long partyId = investorDao.fetchPartyIdByRoleBasedId(invId, deleteflag);
		Investor investor = investorDao.fetchInvestorByInvId(invId, deleteflag, encryptPass); // ?//
		if (investor == null || investor.getInvId() == null) {
			return null;
			// } else {
			// List<InvInterest> invInterest = investorDao.fetchInvInterestByInvId(invId,
			// deleteflag);
			// investor.setInvInterest(invInterest);
			// return investor;
		}
		List<Plan> planList = calcDao.fetchPlanByPartyId(partyId, encryptPass);
		List<String> referenceIdList = new ArrayList<>();
		List<PlanDetail> planDetailList = new ArrayList<>();
		if (planList != null && planList.size() > 0) {
			for (Plan plan : planList) {
				referenceIdList.add(plan.getReferenceId());
			}
			for (String referenceId : referenceIdList) {
				PlanDetail planDetail = new PlanDetail();
				planDetail.setReferenceId(referenceId);
				List<Goal> goalList = calcDao.fetchGoalByReferenceId(referenceId);
				if (goalList.size() > 0) {
					planDetail.setGoal(true);
				}
				RiskSummary riskSummary = calcDao.fetchRiskSummaryByRefId(referenceId);
				if (riskSummary != null) {
					planDetail.setRiskProfile(true);
				}
				CashFlowSummary cashFlowList = calcDao.fetchCashFlowSummaryByRefId(referenceId);
				if (cashFlowList != null) {
					planDetail.setFinance(true);
				} else {
					NetworthSummary networthSummary = calcDao.fetchNetworthSummaryByRefId(referenceId);
					if (networthSummary != null) {
						planDetail.setFinance(true);
					} else {
						Insurance insuranceList = calcDao.fetchInsuranceByRefId(referenceId);
						if (insuranceList != null) {
							planDetail.setFinance(true);
						} else {
							List<Priority> priorityList = calcDao.fetchPriorityByRefId(referenceId);
							if (priorityList.size() > 0) {
								planDetail.setFinance(true);
							}
						}
					}
				}
				FutureValue futureValueList = calcDao.fetchFutureValueByRefId(referenceId);
				if (futureValueList != null) {
					planDetail.setInvestment(true);
				} else {
					TargetValue targetValueList = calcDao.fetchTargetValueByRefId(referenceId);
					if (targetValueList != null) {
						planDetail.setInvestment(true);
					} else {
						RateFinder rateFinderList = calcDao.fetchRateFinderByRefId(referenceId);
						if (rateFinderList != null) {
							planDetail.setInvestment(true);
						} else {
							TenureFinder tenureFinderList = calcDao.fetchTenureFinderByRefId(referenceId);
							if (tenureFinderList != null) {
								planDetail.setInvestment(true);
							}
						}
					}
				}
				EmiCalculator emiCalculatorList = calcDao.fetchEmiCalculatorByRefId(referenceId);
				if (emiCalculatorList != null) {
					planDetail.setLoan(true);
				} else {
					EmiCapacity emiCapacityList = calcDao.fetchEmiCapacityByRefId(referenceId);
					if (emiCapacityList != null) {
						planDetail.setLoan(true);
					} else {
						List<PartialPayment> partialPaymentList = calcDao.fetchPartialPaymentByRefId(referenceId);
						if (partialPaymentList.size() > 0) {
							planDetail.setLoan(true);
						} else {
							List<InterestChange> interestChangeList = calcDao.fetchInterestChangeByRefId(referenceId);
							if (interestChangeList.size() > 0) {
								planDetail.setLoan(true);
							} else {
								List<EmiChange> emiChangeList = calcDao.fetchEmiChangeByRefId(referenceId);
								if (emiChangeList.size() > 0) {
									planDetail.setLoan(true);
								}
							}
						}
					}
				}
				planDetailList.add(planDetail);
			}
		}
		investor.setPlanDetailList(planDetailList);
		investor.setPartyId(partyId);
		return investor;
	}

	@Transactional
	public List<Investor> fetchInvestorList(int pageNum, int records) {
		String deleteflag = invTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Investor> investor = investorDao.fetchInvestor(pageable, deleteflag, encryptPass); // ? //
		for (Investor inv : investor) {
			long partyId = investorDao.fetchPartyIdByRoleBasedId(inv.getInvId(), deleteflag);
			inv.setPartyId(partyId);
		}
		return investor;
	}

	@Transactional
	public int modifyInvestor(String invId, Investor investor) {
		String encryptPass = advTableFields.getEncryption_password();
		Investor investor1 = investorDao.fetchInvestorByInvId(invId, invTableFields.getDelete_flag_N(), encryptPass);
		if (investor.getFullName() != null) {
			investor1.setFullName(investor.getFullName());
		}
		if (investor.getDisplayName() != null) {
			investor1.setDisplayName(investor.getDisplayName());
		}
		if (investor.getDob() != null) {
			investor1.setDob(investor.getDob());
		}
		if (investor.getGender() != null) {
			investor1.setGender(investor.getGender());
		}
		if (investor.getPincode() != null) {
			investor1.setPincode(investor.getPincode());
		}
		if (investor.getPartyStatusId() != 0) {
			investor1.setPartyStatusId(investor.getPartyStatusId());
		}
		if (investor.getImagePath() != null) {
			investor1.setImagePath(investor.getImagePath());
		}
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String signedUserId = getSignedInUser();
		investor1.setUpdated(timestamp);
		investor1.setUpdated_by(signedUserId);
		int result = investorDao.update(invId, investor1, encryptPass);
		// int result1 = investorDao.updatePersonalInfoInParty(investor1.getEmailId(),
		// investor1.getPhoneNumber(),
		// invId, encryptPass,investor1);
		// int result1 = investorDao.updatePersonalInfoInParty(investor1, invId,
		// encryptPass);
		return result;
	}

	@Transactional
	public int removeInvestor(String invId) {
		// String name = "investor";
		String deleteflag = invTableFields.getDelete_flag_Y();
		// long roleId = authDao.fetchRoleIdByName(invTableFields.getRoleNameInv());
		String signedUserId = getSignedInUser();
		return investorDao.deleteInvestor(invId, deleteflag, signedUserId);
	}

	// Investor Interest
	@Transactional
	public int addInvestorInterest(String invId, InvInterest invinterest) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

		String signedUserId = getSignedInUser();
		invinterest.setCreated(timestamp);
		invinterest.setUpdated(timestamp);
		invinterest.setCreated_by(signedUserId);
		invinterest.setUpdated_by(signedUserId);
		int result = investorDao.addInvestorInterest(invId, invinterest, invTableFields.getDelete_flag_N());
		return result;
	}

	private String getSignedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
			return adminSignin.getAdmin_name();
		} else {
			Party party = investorDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
			return party.getRoleBasedId();
		}
	}

	@Transactional
	public int modifyInvestorInterest(long interestId, InvInterest invInterest) {
		InvInterest invInterests = investorDao.fetchInvInterest(interestId, invTableFields.getDelete_flag_N());
		if (invInterest.getProdId() != 0) {
			invInterests.setProdId(invInterest.getProdId());
		}
		if (invInterest.getScale() != null) {
			invInterests.setScale(invInterest.getScale());
		}
		if (invInterest.getInvId() != null) {
			invInterests.setInvId(invInterest.getInvId());
		}
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

		String signedUserId = getSignedInUser();
		invInterests.setUpdated(timestamp);
		invInterests.setUpdated_by(signedUserId);
		int result = investorDao.updateInvestorInterest(interestId, invInterests);
		return result;
	}

	@Transactional
	public int removeInvestorInterest(long interestId) {
		String signedUserId = getSignedInUser();
		int result = investorDao.deleteInvestorInterest(interestId, invTableFields.getDelete_flag_Y(), signedUserId);
		return result;
	}

	@Transactional
	public Category fetchCategoryById(long categoryId) {
		return investorDao.fetchCategoryById(categoryId);
	}

	@Transactional
	public boolean findDuplicate(String invId, long categoryId) {
		return investorDao.findDuplicate(invId, categoryId);
	}

	@Transactional
	public InvInterest fetchByInvInterestById(long interestId) {
		return investorDao.fetchInvInterest(interestId, invTableFields.getDelete_flag_N());
	}

	// @Transactional
	// public Investor fetchInvByEmailId(String emailId) {
	// return investorDao.fetchInvByEmailId(emailId);
	// }

	// @Transactional
	// public String generateId() {
	// String id = investorDao.fetchInvestorSmartId();
	// if (id != null) {
	// String newId = idIncrement(id);
	// int result = investorDao.addInvSmartId(newId);
	// if(result==0) {
	// return null;
	// }else {
	// return newId;
	// }
	// } else {
	// String newId = "INV0000000000";
	// investorDao.addInvSmartId(newId);
	// return newId;
	// }
	// }

	@Transactional
	public String encrypt(String rawPassword) {
		return investorDao.encrypt(rawPassword);
	}

	@Transactional
	public String decrypt(String encodedPassword) {
		return investorDao.decrypt(encodedPassword);
	}

	@Transactional
	public int fetchTotalInvestorList() {
		String deleteflag = invTableFields.getDelete_flag_N();
		int investor = investorDao.fetchTotalInvestorList(deleteflag);
		return investor;
	}

	@Transactional
	public int addAndModifyInvestorInterest(String invId, List<InvInterest> invInterestList) {
		int result = 0;
		for (InvInterest invInterest : invInterestList) {
			Long interestId = invInterest.getInterestId();
			if (interestId != 0) {
				InvInterest invInt = investorDao.fetchInvInterest(interestId, invTableFields.getDelete_flag_N());
				if (invInterest.getProdId() != 0) {
					invInt.setProdId(invInterest.getProdId());
				}
				if (invInterest.getScale() != null) {
					invInt.setScale(invInterest.getScale());
				}
				if (invInterest.getInvId() != null) {
					invInt.setInvId(invInterest.getInvId());
				}
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

				String signedUserId = getSignedInUser();
				invInt.setUpdated(timestamp);
				invInt.setUpdated_by(signedUserId);
				result = investorDao.updateInvestorInterest(interestId, invInt);
			} else {
				// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
				Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

				String signedUserId = getSignedInUser();
				invInterest.setCreated(timestamp);
				invInterest.setUpdated(timestamp);
				invInterest.setCreated_by(signedUserId);
				invInterest.setUpdated_by(signedUserId);
				result = investorDao.addInvestorInterest(invId, invInterest, invTableFields.getDelete_flag_N());
			}
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public int CheckInvestorIsPresent(String invId) {
		String deleteflag = invTableFields.getDelete_flag_N();
		return investorDao.checkInvestorIsPresent(invId, deleteflag);
	}

	@Transactional
	public int CheckCategoryIsPresent(long categoryId) {
		return investorDao.CheckCategoryIsPresent(categoryId);
	}

	@Transactional
	public int CheckInvInterestIsPresent(long interestId) {
		String deleteflag = invTableFields.getDelete_flag_N();
		return investorDao.CheckInvInterestIsPresent(interestId, deleteflag);
	}

	// /* Increment id */
	// private String idIncrement(String id) {
	// String middle = id.substring(3, 12);
	// String suffix;
	// String newId;
	// if (Character.isDigit(id.charAt(12))) {
	// if (id.charAt(12) != '9') {
	// String number = id.substring(3, 13);
	// long num = Long.parseLong(number);
	// middle = String.format("%010d", num + 1);
	// newId = "INV" + middle;
	// } else {
	// newId = "INV" + middle + "A";
	// }
	// } else {
	// if (id.charAt(12) != 'Z') {
	// char last = id.charAt(12);
	// suffix = String.valueOf((char) (last + 1));
	// newId = id.substring(0, 12) + suffix;
	// } else {
	// long num = Long.parseLong(middle);
	// middle = String.format("%09d", num + 1);
	// newId = "INV" + middle + "0";
	// }
	// }
	// return newId;
	// }

}
