package com.sowisetech.advisor.service;

import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.advisor.controller.AdvisorController;
import com.sowisetech.advisor.dao.AdvisorDao;
import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.advisor.model.AdvBrandInfo;
import com.sowisetech.advisor.model.AdvBrandRank;
import com.sowisetech.advisor.model.AdvProduct;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.AdvisorType;
import com.sowisetech.advisor.model.ArticleStatus;
import com.sowisetech.advisor.model.Award;
import com.sowisetech.advisor.model.Brand;
import com.sowisetech.advisor.model.BrandsComment;
import com.sowisetech.advisor.model.Category;
import com.sowisetech.advisor.model.CategoryType;
import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.model.ChatMessage;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.model.City;
import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.CityPincode;
import com.sowisetech.advisor.model.CommentVote;
import com.sowisetech.advisor.model.CommentVoteAddress;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.FollowerStatus;
import com.sowisetech.advisor.model.Followers;
import com.sowisetech.advisor.model.FollowersList;
import com.sowisetech.advisor.model.ForumCategory;
import com.sowisetech.advisor.model.ForumStatus;
import com.sowisetech.advisor.model.ForumSubCategory;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.KeyPeople;
import com.sowisetech.advisor.model.License;
import com.sowisetech.advisor.model.LookUp;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.model.PartyStatus;
import com.sowisetech.advisor.model.PlanDetail;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.StateCity;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.sowisetech.advisor.request.AdvProductRequest;
import com.sowisetech.advisor.request.AwardReq;
import com.sowisetech.advisor.request.CertificateReq;
import com.sowisetech.advisor.request.EducationReq;
import com.sowisetech.advisor.request.ExperienceReq;
import com.sowisetech.advisor.request.PromotionReq;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.dao.CalcDao;
import com.sowisetech.calc.model.CashFlow;
import com.sowisetech.calc.model.CashFlowSummary;
import com.sowisetech.calc.model.EmiCalculator;
import com.sowisetech.calc.model.EmiCapacity;
import com.sowisetech.calc.model.EmiChange;
import com.sowisetech.calc.model.FutureValue;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.Insurance;
import com.sowisetech.calc.model.InterestChange;
import com.sowisetech.calc.model.Networth;
import com.sowisetech.calc.model.NetworthSummary;
import com.sowisetech.calc.model.PartialPayment;
import com.sowisetech.calc.model.Plan;
import com.sowisetech.calc.model.Priority;
import com.sowisetech.calc.model.RateFinder;
import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskSummary;
import com.sowisetech.calc.model.TargetValue;
import com.sowisetech.calc.model.TenureFinder;
import com.sowisetech.common.dao.CommonDao;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.forum.dao.ForumDao;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.forum.util.ForumTableFields;
import com.sowisetech.investor.model.Investor;

@Transactional(readOnly = true)
@Service("AdvisorService")
public class AdvisorServiceImpl implements AdvisorService {

	@Autowired
	private AdvisorDao advisorDao;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private CalcDao calcDao;
	@Autowired
	private ForumDao forumDao;
	@Autowired
	AdvTableFields advTableFields;
	@Autowired
	private AuthDao authDao;
	@Autowired
	private ForumTableFields forumTableFields;
	@Autowired
	AdminSignin adminSignin;
	private static final Logger logger = LoggerFactory.getLogger(AdvisorServiceImpl.class);

	@Transactional
	public int advSignup(Advisor advisor, long roleId) {
		String password = encrypt(advisor.getPassword());
		String encryptPass = advTableFields.getEncryption_password();
		int promoCount = advTableFields.getPromoCount();
		// long signedUserId = getSignedInUser();
		advisor.setPassword(password);
		long partyStatusId = advisorDao.fetchPartyStatusIdByDesc(advTableFields.getPartystatus_desc());
		advisor.setPartyStatusId(partyStatusId);
		advisor.setDelete_flag(advTableFields.getDelete_flag_N());
		int workFlowStatusId = advisorDao.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkFlow_Default());
		advisor.setWorkFlowStatus(workFlowStatusId);
		advisor.setCreated_by(advisor.getAdvId());
		advisor.setUpdated_by(advisor.getAdvId());

		int result = advisorDao.advSignup(advisor, encryptPass, promoCount);
		int result1 = 0;
		if (result != 0) {
			result1 = advisorDao.addParty(advisor, encryptPass);
		}
		int result2 = 0;
		if (result1 != 0) {
			result2 = advisorDao.addSigninVerification(advisor.getEmailId(), encryptPass);
		}
		long partyId = advisorDao.fetchPartyIdByRoleBasedId(advisor.getAdvId(), advTableFields.getDelete_flag_N());
		if (result1 != 0) {
			int isPrimaryRole = advTableFields.getIs_primary_role_true();
			int result3 = authDao.addUser_role(partyId, roleId, advisor.getCreated_by(), isPrimaryRole);

		}
		return result2;
	}

	@Transactional
	public Advisor fetchByAdvisorId(String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		long partyId = advisorDao.fetchPartyIdByRoleBasedId(advId, deleteflag);
		Advisor advisor = advisorDao.fetchAdvisorByAdvId(advId, deleteflag, encryptPass);
		int promoCount = advisorDao.fetchAdvisorPromoCount(advId, deleteflag);
		advisor.setPromoCount(promoCount);
		// Advisor advisor = advisorDao.fetchAdvisor(advId, deleteflag, encryptPass);
		if (advisor.getAdvId() == null && advisor.getEmailId() == null) {
			return null;
		}
		advisor.setPartyId(partyId);
		List<AdvBrandRank> advBrandRankList = new ArrayList<>();
		if (advisor.getAdvBrandRank() != null && advisor.getAdvBrandRank().size() != 0) {
			for (AdvBrandRank advBrandRank1 : advisor.getAdvBrandRank()) {
				String brand;
				brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
				advBrandRank1.setBrand(brand);
				advBrandRankList.add(advBrandRank1);
			}
			advisor.setAdvBrandRank(advBrandRankList);
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
		advisor.setPlanDetailList(planDetailList);
		return advisor;
	}

	@Transactional
	public Advisor fetchByPublicAdvisorID(String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		long partyId = advisorDao.fetchPartyIdByRoleBasedId(advId, deleteflag);
		Advisor advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
		if (advisor == null || (advisor.getAdvId() == null && advisor.getEmailId() == null)) {
			return null;
		}
		advisor.setPartyId(partyId);
		List<AdvBrandRank> advBrandRankList = new ArrayList<>();
		if (advisor.getAdvBrandRank() != null && advisor.getAdvBrandRank().size() != 0) {
			for (AdvBrandRank advBrandRank1 : advisor.getAdvBrandRank()) {
				String brand;
				brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
				advBrandRank1.setBrand(brand);
				advBrandRankList.add(advBrandRank1);
			}
			advisor.setAdvBrandRank(advBrandRankList);
		}
		return advisor;
	}

	@Transactional
	public List<Advisor> fetchAdvisorList(int pageNum, int records) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchAdvisorList(pageable, deleteflag, encryptPass);
		if (advisors.size() == 0) {
			return null;
		}
		// To fetch lookup table brand value
		for (Advisor adv : advisors) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisors;
	}

	@Transactional
	public int removeAdvisor(String advId, int deactivate) {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		int result = advisorDao.removeAdvisor(advId, deleteflag, signedUserId, deactivate);
		// int result1 = advisorDao.removePublicAdvisorDeleteflag(advId, deleteflag,
		// signedUserId);
		int result3 = advisorDao.removePublicAdvisorChild(advId);
		int result4 = advisorDao.removePublicAdvisor(advId);
		return result;
	}

	@Transactional
	public int modifyAdvisor(String advId, Advisor adv) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		int result2 = 0;

		Advisor advisor = advisorDao.fetchAdvisorByAdvId(advId, deleteflag, encryptPass);
		if (adv.getName() != null) {
			advisor.setName(adv.getName());
		}
		if (adv.getEmailId() != null) {
			advisor.setEmailId(adv.getEmailId().toLowerCase());
		}
		if (adv.getDesignation() != null) {
			advisor.setDesignation(adv.getDesignation());
		}
		if (adv.getPhoneNumber() != null) {
			advisor.setPhoneNumber(adv.getPhoneNumber().toLowerCase());
		}
		if (adv.getUserName() != null) {
			advisor.setUserName(adv.getUserName().toLowerCase());
		}
		if (adv.getDisplayName() != null) {
			advisor.setDisplayName(adv.getDisplayName());
		}
		if (adv.getDob() != null) {
			advisor.setDob(adv.getDob());
		}
		if (adv.getGender() != null) {
			advisor.setGender(adv.getGender());
		}
		if (adv.getPanNumber() != null) {
			advisor.setPanNumber(adv.getPanNumber().toLowerCase());
		}
		if (adv.getAddress1() != null) {
			advisor.setAddress1(adv.getAddress1());
		}
		if (adv.getAddress2() != null) {
			advisor.setAddress2(adv.getAddress2());
		}
		if (adv.getCity() != null) {
			advisor.setCity(adv.getCity());
		}
		if (adv.getState() != null) {
			advisor.setState(adv.getState());
		}
		if (adv.getAboutme() != null) {
			advisor.setAboutme(adv.getAboutme());
		}
		if (adv.getPincode() != null) {
			advisor.setPincode(adv.getPincode());
		}
		if (adv.getPartyStatusId() != 0) {
			advisor.setPartyStatusId(adv.getPartyStatusId());
		}
		if (adv.getImagePath() != null) {
			advisor.setImagePath(adv.getImagePath());
		}
		if (adv.getGst() != null) {
			advisor.setGst(adv.getGst());
		}
		// advtype

		// advisor.setCreated_by(signedUserId);
		advisor.setUpdated_by(signedUserId);
		int result = advisorDao.update(advId, advisor, encryptPass);

		int result1 = advisorDao.updatePersonalInfoInParty(advisor.getEmailId(), advisor.getPhoneNumber(),
				advisor.getPanNumber(), advisor.getUserName(), advId, encryptPass, signedUserId);

		long partyId = advisorDao.fetchPartyIdByRoleBasedId(advId, deleteflag);
		if (adv.getDisplayName() != null || adv.getImagePath() != null) {
			List<BrandsComment> commentList = advisorDao.fetchBrandsCommentByPartyId(partyId, deleteflag, encryptPass);
			List<ArticlePost> articlePostList = advisorDao.fetchArticlePostList(partyId, deleteflag, encryptPass);
			List<ArticleComment> articleComment = advisorDao.fetchArticleCommentByParyId(partyId, deleteflag,
					encryptPass);
			if (commentList.size() != 0) {
				for (BrandsComment comment : commentList) {
					result2 = advisorDao.updateArticleCommentByPartyId(advisor.getDisplayName(), advisor.getImagePath(),
							comment.getPartyId(), signedUserId, deleteflag, encryptPass);
				}
			}
			if (articlePostList.size() != 0) {
				for (ArticlePost article : articlePostList) {
					int result3 = advisorDao.updateArticlePostNameByPartyId(advisor.getDisplayName(),
							advisor.getImagePath(), article.getPartyId(), signedUserId, deleteflag, encryptPass);
				}
			}
			if (articleComment.size() != 0) {
				for (ArticleComment article : articleComment) {
					int result3 = advisorDao.updateArticlePostCommentByPartyId(advisor.getDisplayName(),
							advisor.getImagePath(), article.getPartyId(), signedUserId, deleteflag, encryptPass);
				}
			}
		}
		if (adv.getDisplayName() != null) {
			int result4 = advisorDao.updateCalcQuery(partyId, advisor.getDisplayName(), signedUserId);
		}
		if (result != 0 && result1 != 0) {
			return result;
		} else {
			return 0;
		}
	}

	@Transactional
	public int addAdvProfessionalInfo(String advId, Advisor adv) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		advisorDao.updateAdvisorTimeStamp(advId);
		List<Award> awards = new ArrayList<Award>();
		List<Certificate> certificates = new ArrayList<Certificate>();
		List<Education> educations = new ArrayList<Education>();
		List<Experience> experiences = new ArrayList<Experience>();
		if (adv != null && adv.getAwards() != null) {
			awards = adv.getAwards();
		}
		if (adv != null && adv.getCertificates() != null) {
			certificates = adv.getCertificates();
		}
		if (adv != null && adv.getEducations() != null) {
			educations = adv.getEducations();
		}
		if (adv != null && adv.getExperiences() != null) {
			experiences = adv.getExperiences();
		}
		Advisor advisor = advisorDao.fetchAdvisorByAdvId(advId, deleteflag, encryptPass);
		advisor.setAwards(adv.getAwards());
		advisor.setCertificates(adv.getCertificates());
		advisor.setEducations(adv.getEducations());
		advisor.setExperiences(adv.getExperiences());
		for (Award award : awards) {
			award.setCreated_by(signedUserId);
			award.setUpdated_by(signedUserId);
			int result1 = advisorDao.addAdvAwardInfo(advId, award, deleteflag);
			if (result1 == 0) {
				return result1;
			}
		}
		for (Certificate certificate : certificates) {
			certificate.setCreated_by(signedUserId);
			certificate.setUpdated_by(signedUserId);
			int result2 = advisorDao.addAdvCertificateInfo(advId, certificate, deleteflag);
			if (result2 == 0) {
				return result2;
			}
		}
		for (Education education : educations) {
			education.setCreated_by(signedUserId);
			education.setUpdated_by(signedUserId);
			int result3 = advisorDao.addAdvEducationInfo(advId, education, deleteflag);
			if (result3 == 0) {
				return result3;
			}
		}
		for (Experience experience : experiences) {
			experience.setCreated_by(signedUserId);
			experience.setUpdated_by(signedUserId);
			int result4 = advisorDao.addAdvExperienceInfo(advId, experience, deleteflag);
			if (result4 == 0) {
				return result4;
			}
		}
		return 1;
	}

	@Transactional
	public int addAdvProductInfo(String advId, AdvProduct advProduct) {
		advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		advProduct.setCreated_by(signedUserId);
		advProduct.setUpdated_by(signedUserId);

		return advisorDao.addAdvProductInfo(advId, advProduct, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Award fetchAward(long awardId) {
		return advisorDao.fetchAward(awardId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Certificate fetchCertificate(long certificateId) {
		return advisorDao.fetchCertificate(certificateId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Experience fetchExperience(long expId) {
		return advisorDao.fetchExperience(expId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Education fetchEducation(long eduId) {
		return advisorDao.fetchEducation(eduId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int removeAdvAward(long awardId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removeAdvAward(awardId, advId, advTableFields.getDelete_flag_Y(), signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	@Transactional
	public int removeAdvCertificate(long certificateId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removeAdvCertificate(certificateId, advId, advTableFields.getDelete_flag_Y(),
				signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	@Transactional
	public int removeAdvEducation(long eduId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removeAdvEducation(eduId, advId, advTableFields.getDelete_flag_Y(), signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	@Transactional
	public int removeAdvExperience(long expId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removeAdvExperience(expId, advId, advTableFields.getDelete_flag_Y(), signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	@Transactional
	public Advisor fetchAdvisorByEmailId(String emailId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchAdvisorByEmailId(emailId, deleteflag, encryptPass);
	}

	@Transactional
	public String generateId() {
		String id = advisorDao.fetchAdvisorSmartId();
		if (id != null) {
			String newId = idIncrement(id);
			advisorDao.addAdvSmartId(newId);
			return newId;
		} else {
			String newId = "ADV0000000000";
			advisorDao.addAdvSmartId(newId);
			return newId;
		}
	}

	private String idIncrement(String id) {
		String middle = id.substring(3, 12);
		String suffix;
		String newId;
		if (Character.isDigit(id.charAt(12))) {
			if (id.charAt(12) != '9') {
				String number = id.substring(3, 13);
				long num = Long.parseLong(number);
				middle = String.format("%010d", num + 1);
				newId = "ADV" + middle;
			} else {
				newId = "ADV" + middle + "A";
			}
		} else {
			if (id.charAt(12) != 'Z') {
				char last = id.charAt(12);
				suffix = String.valueOf((char) (last + 1));
				newId = id.substring(0, 12) + suffix;
			} else {
				long num = Long.parseLong(middle);
				middle = String.format("%09d", num + 1);
				newId = "ADV" + middle + "0";
			}
		}
		return newId;
	}

	@Transactional
	public int addAdvPersonalInfo(String advId, Advisor adv) {
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		String deleteflag = advTableFields.getDelete_flag_N();
		// adv.setCreated_by(signedUserId);
		int result2 = 0;
		adv.setUpdated_by(signedUserId);
		int result = advisorDao.addAdvPersonalInfo(advId, adv, encryptPass);
		if (result != 0) {
			int result1 = advisorDao.updateUsernameInParty(advId, adv, signedUserId, encryptPass);
		}
		long partyId = advisorDao.fetchPartyIdByRoleBasedId(advId, deleteflag);
		if (adv.getDisplayName() != null || adv.getImagePath() != null) {
			List<BrandsComment> commentList = advisorDao.fetchBrandsCommentByPartyId(partyId, deleteflag, encryptPass);
			List<ArticlePost> articlePostList = advisorDao.fetchArticlePostList(partyId, deleteflag, encryptPass);
			List<ArticleComment> articleComment = advisorDao.fetchArticleCommentByParyId(partyId, deleteflag,
					encryptPass);
			if (commentList.size() != 0) {
				for (BrandsComment comment : commentList) {
					result2 = advisorDao.updateArticleCommentByPartyId(adv.getDisplayName(), adv.getImagePath(),
							comment.getPartyId(), signedUserId, deleteflag, encryptPass);
				}
			}
			if (articlePostList.size() != 0) {
				for (ArticlePost article : articlePostList) {
					int result3 = advisorDao.updateArticlePostNameByPartyId(adv.getDisplayName(), adv.getImagePath(),
							article.getPartyId(), signedUserId, deleteflag, encryptPass);
				}
			}
			if (articleComment.size() != 0) {
				for (ArticleComment article : articleComment) {
					int result3 = advisorDao.updateArticlePostCommentByPartyId(adv.getDisplayName(), adv.getImagePath(),
							article.getPartyId(), signedUserId, deleteflag, encryptPass);
				}
			}
		}
		return result;
	}

	@Transactional
	public boolean checkForPasswordMatch(String roleBasedId, String currentPassword) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = advisorDao.fetchPartyByRoleBasedId(roleBasedId, deleteflag, encryptPass);
		String password = decrypt(party.getPassword());
		if (password.equals(currentPassword)) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public int changeAdvPassword(String roleBasedId, String newPassword) {
		String password = encrypt(newPassword);
		int result = advisorDao.changeAdvPassword(roleBasedId, password);
		if (result != 0) {
			int result1 = advisorDao.changePartyPassword(roleBasedId, password);
			return result1;
		}
		return result;
	}

	@Transactional
	public AdvProduct fetchAdvProduct(long advProdId) {
		return advisorDao.fetchAdvProduct(advProdId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int modifyAdvisorProduct(AdvProduct advProduct, String advId) {
		String signedUserId = getSignedInUser();
		AdvProduct advProduct1 = advisorDao.fetchAdvProduct(advProduct.getAdvProdId(),
				advTableFields.getDelete_flag_N());
		if (advProduct.getProdId() != 0) {
			advProduct1.setProdId(advProduct.getProdId());
		}
		if (advProduct.getServiceId() != null) {
			advProduct1.setServiceId(advProduct.getServiceId());
		}
		if (advProduct.getRemId() != 0) {
			advProduct1.setRemId(advProduct.getRemId());
		}
		if (advProduct.getLicId() != 0) {
			advProduct1.setLicId(advProduct.getLicId());
		}
		if (advProduct.getLicNumber() != null) {
			advProduct1.setLicNumber(advProduct.getLicNumber());
		}
		if (advProduct.getValidity() != null) {
			advProduct1.setValidity(advProduct.getValidity());
		}
		if (advProduct.getLicImage() != null) {
			advProduct1.setLicImage(advProduct.getLicImage());
		}
		advProduct1.setUpdated_by(signedUserId);

		return advisorDao.modifyAdvisorProduct(advProduct1, advId);
	}

	@Transactional
	public String encrypt(String rawPassword) {
		Security.addProvider(new BouncyCastleProvider());
		StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
		String key = advisorDao.fetchEncryptionSecretKey();
		cryptor.setPassword(key);
		cryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		String encryptedText = cryptor.encrypt(rawPassword);
		return encryptedText;
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
	public List<Category> fetchCategoryList() {
		return advisorDao.fetchCategoryList();
	}

	@Transactional
	public List<CategoryType> fetchCategoryTypeList() {
		return advisorDao.fetchCategoryTypeList();
	}

	@Transactional
	public List<ForumCategory> fetchForumCategoryList() {
		return advisorDao.fetchForumCategoryList();
	}

	@Transactional
	public List<RiskQuestionaire> fetchRiskQuestionaireList() {
		return advisorDao.fetchAllRiskQuestionaire();
	}

	@Transactional
	public List<Product> fetchProductList() {
		// return advisorDao.fetchProductList();
		List<Product> productList = advisorDao.fetchProductList();
		for (Product prod : productList) {
			for (com.sowisetech.advisor.model.Service serv : prod.getServices()) {
				List<ServicePlan> servicePlans = advisorDao.fetchServicePlanByServiceId(serv.getServiceId());
				serv.setServicePlans(servicePlans);
			}
		}
		return productList;
	}

	@Transactional
	public List<RoleAuth> fetchRoleList() {
		return authDao.fetchRoleList();
	}

	@Transactional
	public List<ForumSubCategory> fetchForumSubCategoryList() {
		return advisorDao.fetchForumSubCategoryList();
	}

	@Transactional
	public List<ForumStatus> fetchForumStatusList() {
		return advisorDao.fetchForumStatusList();
	}

	@Transactional
	public List<PartyStatus> fetchPartyStatusList() {
		return advisorDao.fetchPartyStatusList();
	}

	@Transactional
	public List<com.sowisetech.advisor.model.Service> fetchServiceList() {
		return advisorDao.fetchServiceList();

		// List<com.sowisetech.advisor.model.Service> serviceList =
		// advisorDao.fetchServiceList();
		// for (com.sowisetech.advisor.model.Service service : serviceList) {
		// for (ServicePlan serv : service.getServicePlans()) {
		// List<ServicePlan> servicePlans =
		// advisorDao.fetchServicePlanByServiceId(serv.getServiceId());
		// serv.setServicePlans(servicePlans);
		// }
		// }
		// return productList;
	}

	@Transactional
	public List<Brand> fetchBrandList() {
		return advisorDao.fetchBrandList();
	}

	@Transactional
	public List<License> fetchLicenseList() {
		return advisorDao.fetchLicenseList();
	}

	@Transactional
	public List<Remuneration> fetchRemunerationList() {
		return advisorDao.fetchRemunerationList();
	}

	@Transactional
	public int addAdvBrandInfo(String advId, List<AdvBrandInfo> advBrandInfoList) {
		int result = 0;
		for (AdvBrandInfo advBrandInfo : advBrandInfoList) {
			String signedUserId = getSignedInUser();
			advBrandInfo.setCreated_by(signedUserId);
			advBrandInfo.setUpdated_by(signedUserId);
			result = advisorDao.addAdvBrandInfo(advId, advBrandInfo, advTableFields.getDelete_flag_N());
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public List<AdvBrandInfo> fetchAdvBrandInfoByAdvIdAndProdId(String advId, long prodId) {
		return advisorDao.fetchAdvBrandInfoByAdvIdAndProdId(advId, prodId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public List<Long> fetchPriorityByBrandIdAndAdvId(String advId, long prodId, long brandId) {
		return advisorDao.fetchPriorityByBrandIdAndAdvId(advId, prodId, brandId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public AdvBrandRank fetchAdvBrandRank(String advId, long prodId, int rank) {
		return advisorDao.fetchAdvBrandRank(advId, prodId, rank, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int addAdvBrandAndRank(long brand, int rank, String advId, long prodId) {
		String signedUserId = getSignedInUser();
		return advisorDao.addAdvBrandAndRank(brand, rank, advId, prodId, advTableFields.getDelete_flag_N(),
				signedUserId);
	}

	@Transactional
	public int updateBrandAndRank(long brand, int rank, String advId, long prodId) {
		String signedUserId = getSignedInUser();
		return advisorDao.updateBrandAndRank(brand, rank, advId, prodId, advTableFields.getDelete_flag_N(),
				signedUserId);
	}

	@Transactional
	public List<AdvProduct> fetchAdvProductByAdvId(String advId) {
		return advisorDao.fetchAdvProductByAdvId(advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int removeAdvProduct(long advProdId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removeAdvProduct(advProdId, advId, advTableFields.getDelete_flag_Y(), signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	@Transactional
	public int removeAdvBrandInfo(long prodId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removeAdvBrandInfo(prodId, advId, advTableFields.getDelete_flag_Y(), signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	//
	@Transactional
	public int removeFromBrandRank(String advId, long prodId) {
		return advisorDao.removeFromBrandRank(advId, prodId);
	}

	@Transactional
	public AdvProduct fetchAdvProductByAdvIdAndAdvProdId(String advId, long advProdId) {
		return advisorDao.fetchAdvProductByAdvIdAndAdvProdId(advId, advProdId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int removeAdvBrandInfoByAdvId(String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		int result2 = advisorDao.removeAdvBrandInfoByAdvId(advId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	@Transactional
	public int removeAdvBrandRankByAdvId(String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		int result2 = advisorDao.removeAdvBrandRankByAdvId(advId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}

	}

	//
	@Transactional
	public List<Product> fetchAllServiceAndBrand() {
		// return advisorDao.fetchAllServiceAndBrand();

		List<Product> productList = advisorDao.fetchAllServiceAndBrand();
		for (Product prod : productList) {
			for (com.sowisetech.advisor.model.Service serv : prod.getServices()) {
				List<ServicePlan> servicePlans = advisorDao.fetchServicePlanByServiceId(serv.getServiceId());
				serv.setServicePlans(servicePlans);
			}
		}
		return productList;
	}

	@Transactional
	public List<Award> fetchAwardByadvId(String advId) {
		return advisorDao.fetchAwardByadvId(advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public List<Certificate> fetchCertificateByadvId(String advId) {
		return advisorDao.fetchCertificateByadvId(advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public List<Experience> fetchExperienceByadvId(String advId) {
		return advisorDao.fetchExperienceByadvId(advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public List<Education> fetchEducationByadvId(String advId) {
		return advisorDao.fetchEducationByadvId(advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int modifyAdvisorAward(long awardId, Award award, String advId) {
		Award award1 = advisorDao.fetchAdvAwardByAdvIdAndAwardId(awardId, advId, advTableFields.getDelete_flag_N());
		String signedUserId = getSignedInUser();
		if (award.getImagePath() != null) {
			award1.setImagePath(award.getImagePath());
		}
		if (award.getIssuedBy() != null) {
			award1.setIssuedBy(award.getIssuedBy());
		}
		if (award.getTitle() != null) {
			award1.setTitle(award.getTitle());
		}
		if (award.getYear() != null) {
			award1.setYear(award.getYear());
		}
		award1.setUpdated_by(signedUserId);
		return advisorDao.modifyAdvisorAward(awardId, award1, advId);
	}

	@Transactional
	public int modifyAdvisorCertificate(long certificateId, Certificate certificate, String advId) {
		Certificate certificate1 = advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(certificateId, advId,
				advTableFields.getDelete_flag_N());
		String signedUserId = getSignedInUser();
		if (certificate.getImagePath() != null) {
			certificate1.setImagePath(certificate.getImagePath());
		}
		if (certificate.getIssuedBy() != null) {
			certificate1.setIssuedBy(certificate.getIssuedBy());
		}
		if (certificate.getTitle() != null) {
			certificate1.setTitle(certificate.getTitle());
		}
		if (certificate.getYear() != null) {
			certificate1.setYear(certificate.getYear());
		}
		certificate1.setUpdated_by(signedUserId);
		return advisorDao.modifyAdvisorCertificate(certificateId, certificate1, advId);
	}

	@Transactional
	public int modifyAdvisorExperience(long expId, Experience experience, String advId) {
		Experience experience1 = advisorDao.fetchAdvExperienceByAdvIdAndExpId(expId, advId,
				advTableFields.getDelete_flag_N());
		String signedUserId = getSignedInUser();
		if (experience.getCompany() != null) {
			experience1.setCompany(experience.getCompany());
		}
		if (experience.getDesignation() != null) {
			experience1.setDesignation(experience.getDesignation());
		}
		if (experience.getLocation() != null) {
			experience1.setLocation(experience.getLocation());
		}
		if (experience.getFromYear() != null) {
			experience1.setFromYear(experience.getFromYear());
		}
		if (experience.getToYear() != null) {
			experience1.setToYear(experience.getToYear());
		}
		experience1.setUpdated_by(signedUserId);
		return advisorDao.modifyAdvisorExperience(expId, experience1, advId);
	}

	@Transactional
	public int modifyAdvisorEducation(long eduId, Education education, String advId) {
		Education education1 = advisorDao.fetchAdvEducationByAdvIdAndEduId(eduId, advId,
				advTableFields.getDelete_flag_N());
		String signedUserId = getSignedInUser();
		if (education.getDegree() != null) {
			education1.setDegree(education.getDegree());
		}
		if (education.getInstitution() != null) {
			education1.setInstitution(education.getInstitution());
		}
		if (education.getField() != null) {
			education1.setField(education.getField());
		}
		if (education.getFromYear() != null) {
			education1.setFromYear(education.getFromYear());
		}
		if (education.getToYear() != null) {
			education1.setToYear(education.getToYear());
		}
		education1.setUpdated_by(signedUserId);
		return advisorDao.modifyAdvisorEducation(eduId, education1, advId);
	}

	@Transactional
	public int addAdvAwardInfo(String advId, Award award) {
		String signedUserId = getSignedInUser();
		award.setCreated_by(signedUserId);
		award.setUpdated_by(signedUserId);
		return advisorDao.addAdvAwardInfo(advId, award, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int addAdvCertificateInfo(String advId, Certificate certificate) {
		String signedUserId = getSignedInUser();
		certificate.setCreated_by(signedUserId);
		certificate.setUpdated_by(signedUserId);
		return advisorDao.addAdvCertificateInfo(advId, certificate, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int addAdvExperienceInfo(String advId, Experience experience) {
		String signedUserId = getSignedInUser();
		experience.setCreated_by(signedUserId);
		experience.setUpdated_by(signedUserId);
		return advisorDao.addAdvExperienceInfo(advId, experience, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int addAdvEducationInfo(String advId, Education education) {
		String signedUserId = getSignedInUser();
		education.setCreated_by(signedUserId);
		education.setUpdated_by(signedUserId);
		return advisorDao.addAdvEducationInfo(advId, education, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Award fetchAdvAwardByAdvIdAndAwardId(long awardId, String advId) {
		return advisorDao.fetchAdvAwardByAdvIdAndAwardId(awardId, advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Certificate fetchAdvCertificateByAdvIdAndCertificateId(long certificateId, String advId) {
		return advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(certificateId, advId,
				advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Education fetchAdvEducationByAdvIdAndEduId(long eduId, String advId) {
		return advisorDao.fetchAdvEducationByAdvIdAndEduId(eduId, advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public Experience fetchAdvExperienceByAdvIdAndExpId(long expId, String advId) {
		return advisorDao.fetchAdvExperienceByAdvIdAndExpId(expId, advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int removeAwardByAdvId(String advId) {
		advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		return advisorDao.removeAwardByAdvId(advId, advTableFields.getDelete_flag_Y(), signedUserId);
	}

	@Transactional
	public int removeCertificateByAdvId(String advId) {
		advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		return advisorDao.removeCertificateByAdvId(advId, advTableFields.getDelete_flag_Y(), signedUserId);
	}

	@Transactional
	public int removeExperienceByAdvId(String advId) {
		advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		return advisorDao.removeExperienceByAdvId(advId, advTableFields.getDelete_flag_Y(), signedUserId);
	}

	@Transactional
	public int removeEducationByAdvId(String advId) {
		advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		return advisorDao.removeEducationByAdvId(advId, advTableFields.getDelete_flag_Y(), signedUserId);
	}

	@Transactional
	public List<StateCity> fetchAllStateCityPincode() {
		// List<StateCity> stateCityList = advisorDao.fetchAllStateCityPincode();
		// return stateCityList;
		List<State> stateList = advisorDao.fetchAllStateCityPincode();
		List<StateCity> stateCityList = new ArrayList<>();
		for (State state : stateList) {
			Map<String, String> map = new LinkedHashMap<>();
			for (City city : state.getCities()) {
				map.put(city.getPincode(), city.getCity());
			}
			Multimap<String, String> multiMap = LinkedHashMultimap.create();
			for (Entry<String, String> entry : map.entrySet()) {
				multiMap.put(entry.getValue(), entry.getKey());
			}
			StateCity stateCity = new StateCity();
			stateCity.setStateId(state.getStateId());
			stateCity.setState(state.getState());
			List<CityPincode> citiesList = new ArrayList<>();
			for (Entry<String, Collection<String>> entry : multiMap.asMap().entrySet()) {
				CityPincode cities = new CityPincode();
				cities.setCity(entry.getKey());
				List<String> pincodeList = new ArrayList<String>();
				for (String pincode : entry.getValue()) {
					pincodeList.add(pincode);
				}
				cities.setPincodes(pincodeList);
				citiesList.add(cities);
			}
			stateCity.setCities(citiesList);
			stateCityList.add(stateCity);
		}
		return stateCityList;
	}

	@Transactional
	public List<AdvBrandRank> fetchAdvBrandRankByAdvId(String advId) {
		List<AdvBrandRank> advBrandRankList = advisorDao.fetchAdvBrandRankByAdvId(advId,
				advTableFields.getDelete_flag_N());
		// To fetch lookup table brand value
		if (advBrandRankList != null) {
			for (AdvBrandRank advBrandRank1 : advBrandRankList) {
				String brand;
				brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
				advBrandRank1.setBrand(brand);
			}
		}
		return advBrandRankList;
	}

	@Transactional
	public long fetchPartyIdByRoleBasedId(String roleBasedId) {
		String delete_flag = advTableFields.getDelete_flag_N();
		return advisorDao.fetchPartyIdByRoleBasedId(roleBasedId, delete_flag);
	}

	@Transactional
	public List<AdvBrandInfo> fetchAdvBrandInfoByAdvId(String advId) {
		return advisorDao.fetchAdvBrandInfoByAdvId(advId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public List<ArticleStatus> fetchArticleStatusList() {
		return advisorDao.fetchArticleStatusList();
	}

	@Transactional
	public Advisor checkEmailAvailability(String emailId) {
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.checkEmailAvailability(emailId, encryptPass);
	}

	@Transactional
	public int addInvestor(Investor investor, long roleId) {
		String password = encrypt(investor.getPassword());
		String encryptPass = advTableFields.getEncryption_password();
		// String desc = "active";
		long partyStatusId = advisorDao.fetchPartyStatusIdByDesc(advTableFields.getPartystatus_desc());
		investor.setPassword(password);
		investor.setPartyStatusId(partyStatusId);
		investor.setCreated_by(investor.getInvId());
		investor.setUpdated_by(investor.getInvId());
		int result = advisorDao.addInv(investor, advTableFields.getDelete_flag_N(), encryptPass);
		int result1 = 0;
		if (result != 0) {
			result1 = advisorDao.addPartyInv(investor, advTableFields.getDelete_flag_N(), encryptPass);
		}
		int result2 = 0;
		if (result1 != 0) {
			result2 = advisorDao.addSigninVerification(investor.getEmailId(), encryptPass);
		}
		long partyId = advisorDao.fetchPartyIdByRoleBasedId(investor.getInvId(), advTableFields.getDelete_flag_N());
		if (result1 != 0) {
			int isPrimaryRole = advTableFields.getIs_primary_role_true();
			// String signedUserId = getSignedInUser();

			int result3 = authDao.addUser_role(partyId, roleId, investor.getCreated_by(), isPrimaryRole);
		}
		return result2;
	}

	@Transactional
	public Party fetchPartyByEmailId(String emailId) {
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyByEmailId(emailId, encryptPass);
	}

	@Transactional
	public String generateIdInv() {
		String id = advisorDao.fetchInvestorSmartId();
		if (id != null) {
			String newId = idIncrementInv(id);
			int result = advisorDao.addInvSmartId(newId);
			if (result == 0) {
				return null;
			} else {
				return newId;
			}
		} else {
			String newId = "INV0000000000";
			advisorDao.addInvSmartId(newId);
			return newId;
		}
	}

	@Transactional
	public long fetchAdvRoleIdByName() {
		String name = advTableFields.getRoleName();
		return authDao.fetchRoleIdByName(name);
	}

	@Transactional
	public long fetchInvRoleIdByName() {
		String name = advTableFields.getRoleName_inv();
		return authDao.fetchRoleIdByName(name);
	}

	@Transactional
	public long fetchNonAdvRoleIdByName() {
		String name = advTableFields.getRoleName_nonIndividual();
		return authDao.fetchRoleIdByName(name);
	}

	// @Transactional
	// public long fetchTeamMemberByName() {
	// String name = advTableFields.getRoleName_teamMember();
	// return advisorDao.fetchTeamMemberByName(name);
	// }

	/* Increment investor id */
	private String idIncrementInv(String id) {
		String middle = id.substring(3, 12);
		String suffix;
		String newId;
		if (Character.isDigit(id.charAt(12))) {
			if (id.charAt(12) != '9') {
				String number = id.substring(3, 13);
				long num = Long.parseLong(number);
				middle = String.format("%010d", num + 1);
				newId = "INV" + middle;
			} else {
				newId = "INV" + middle + "A";
			}
		} else {
			if (id.charAt(12) != 'Z') {
				char last = id.charAt(12);
				suffix = String.valueOf((char) (last + 1));
				newId = id.substring(0, 12) + suffix;
			} else {
				long num = Long.parseLong(middle);
				middle = String.format("%09d", num + 1);
				newId = "INV" + middle + "0";
			}
		}
		return newId;
	}

	@Transactional
	public Party fetchPartyByRoleBasedId(String roleBasedId) {
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		return advisorDao.fetchPartyByRoleBasedId(roleBasedId, delete_flag, encryptPass);
	}

	@Transactional
	public int changeInvPassword(String roleBasedId, String newPassword) {
		String password = encrypt(newPassword);
		int result = advisorDao.changeInvPassword(roleBasedId, password);
		if (result != 0) {
			int result1 = advisorDao.changePartyPassword(roleBasedId, password);
			return result1;
		}
		return result;
	}

	@Transactional
	public int fetchTypeIdByCorporateAdvtype() {
		int typeId = advisorDao.fetchTypeIdByAdvtype(advTableFields.getAdvType_Corporate());
		return typeId;
	}

	@Transactional
	public int fetchTypeIdByIndividualAdvtype() {
		int typeId = advisorDao.fetchTypeIdByAdvtype(advTableFields.getAdvType());
		return typeId;
	}

	@Transactional
	public List<Advisor> fetchTeamByParentPartyId(long parentPartyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisors = advisorDao.fetchTeamByParentPartyId(parentPartyId, deleteflag, encryptPass);
		for (Advisor adv : advisors) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
		}
		return advisors;
	}

	@Transactional
	public int addKeyPeople(KeyPeople keyPeople) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		keyPeople.setCreated_by(signedUserId);
		keyPeople.setUpdated_by(signedUserId);
		return advisorDao.addKeyPeople(keyPeople, deleteflag, encryptPass);
	}

	@Transactional
	public List<KeyPeople> fetchKeyPeopleByParentId(long parentPartyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchKeyPeopleByParentId(parentPartyId, deleteflag, encryptPass);
	}

	@Transactional
	public int addPromotion(String advId, Promotion promo) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String signedUserId = getSignedInUser();
		promo.setCreated_by(signedUserId);
		promo.setUpdated_by(signedUserId);
		return advisorDao.addPromotion(advId, promo, deleteflag);
	}

	@Transactional
	public List<Promotion> fetchPromotionByAdvId(String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.fetchPromotionByAdvId(advId, deleteflag);
	}

	@Transactional
	public int modifyPromotion(long promotionId, Promotion promo, String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		Promotion promotion = advisorDao.fetchPromotionByAdvIdAndPromotionId(promotionId, advId, deleteflag);
		if (promo.getTitle() != null) {
			promotion.setTitle(promo.getTitle());
		}
		if (promo.getAboutVideo() != null) {
			promotion.setAboutVideo(promo.getAboutVideo());
		}
		if (promo.getVideo() != null) {
			promotion.setVideo(promo.getVideo());
		}
		if (promo.getImagePath() != null) {
			promotion.setImagePath(promo.getImagePath());
		}
		String signedUserId = getSignedInUser();
		promotion.setUpdated_by(signedUserId);
		return advisorDao.modifyPromotion(promotionId, promotion, advId, deleteflag);
	}

	@Transactional
	public int removePromotion(long promotionId, String advId) {
		int result1 = advisorDao.updateAdvisorTimeStamp(advId);
		String signedUserId = getSignedInUser();
		int result2 = advisorDao.removePromotion(promotionId, advTableFields.getDelete_flag_Y(), signedUserId);
		if (result1 != 0 && result2 != 0) {
			return result2;
		} else {
			return 0;
		}
	}

	// @Transactional
	// public Advisor fetchAdvisorByParentPartyId(long parentPartyId) {
	// return advisorDao.fetchAdvisorByParentPartyId(parentPartyId);
	// }

	@Transactional
	public Party fetchPartyByPartyId(long parentPartyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyByPartyId(parentPartyId, deleteflag, encryptPass);
	}

	@Transactional
	public String fetchRoleByRoleId(long roleId) {
		return authDao.fetchRoleByRoleId(roleId);
	}

	@Transactional
	public Investor fetchInvestorByInvId(String roleBasedId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Investor investor = advisorDao.fetchInvestorByInvId(roleBasedId, deleteflag, encryptPass);
		if (investor.getInvId() == null && investor.getEmailId() == null) {
			return null;
		}
		return investor;
	}

	@Transactional
	public int updateAdvisorAccountAsVerified(String advId) {
		int accountVerified = advTableFields.getAccount_verified();
		int result = advisorDao.updateAdvisorAccountAsVerified(advId, accountVerified);
		return result;
	}

	@Transactional
	public int updateInvestorAccountAsVerified(String invId) {
		int accountVerified = advTableFields.getAccount_verified();
		int result = advisorDao.updateInvestorAccountAsVerified(invId, accountVerified);
		return result;
	}

	@Transactional
	public Party fetchPartyByPhoneNumber(String phoneNumber) {
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyByPhoneNumber(phoneNumber, encryptPass);
	}

	@Transactional
	public Party fetchPartyByPAN(String panNumber) {
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyByPAN(panNumber, encryptPass);
	}

	@Transactional
	public Party fetchPartyByUserName(String userName) {
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyByUserName(userName, encryptPass);
	}

	@Transactional
	public Party fetchPartyForSignIn(String username) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyForSignIn(username, deleteflag, encryptPass);
	}

	@Transactional
	public int teamMemberDeactivate(String id, int deactivate) {
		String delete_flag_Y = advTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		return advisorDao.teamMemberDeactive(id, delete_flag_Y, signedUserId, deactivate);
	}

	@Transactional
	public Advisor fetchAdvisorByUserName(String userName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Advisor advisor = advisorDao.fetchAdvisorByUserName(userName, deleteflag, encryptPass);
		if (advisor.getAdvId() == null && advisor.getEmailId() == null) {
			return null;
		}
		if (advisor.getParentPartyId() != 0) {
			Party party = advisorDao.fetchPartyByPartyId(advisor.getParentPartyId(), deleteflag, encryptPass);
			if (party != null) {
				advisor.setCorporateUsername(party.getUserName());
			}
		}
		if (advisor != null) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(advisor.getAdvId(), deleteflag);
			advisor.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (advisor.getAdvBrandRank() != null && advisor.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : advisor.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				advisor.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisor;
	}

	@Transactional
	public int addWorkFlowStatusByAdvId(String advId, int status, String reason) {
		String status_approved = advTableFields.getWorkflow_status_approved();
		String status_revoke = advTableFields.getWorkflow_status_revoked();
		String hide_public = advTableFields.getWorkflow_status_hide_public();
		String status_delete = advTableFields.getWorkflow_status_deleted();
		String status_created = advTableFields.getWorkflow_status_created();
		String created_with_approved = advTableFields.getWorkflow_status_created_with_approved();

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();

		int status_approvedId = advisorDao.fetchWorkFlowStatusIdByDesc(status_approved);
		int status_revokeId = advisorDao.fetchWorkFlowStatusIdByDesc(status_revoke);
		int status_deleteId = advisorDao.fetchWorkFlowStatusIdByDesc(status_delete);
		int status_hideId = advisorDao.fetchWorkFlowStatusIdByDesc(hide_public);
		int status_createdId = advisorDao.fetchWorkFlowStatusIdByDesc(status_created);
		int status_createdWithApproved = advisorDao.fetchWorkFlowStatusIdByDesc(created_with_approved);

		Advisor advisor = advisorDao.fetchAdvisorByAdvId(advId, deleteflag, encryptPass);

		int result = 0;
		if (status == status_approvedId) {
			result = advisorDao.addWorkFlowStatusApprovedByAdvId(advId, status_approvedId, signedUserId, reason);
			if (result != 0) {
				int removeResult = advisorDao.removePublicAdvisorChild(advId);
				int remove = advisorDao.removePublicAdvisor(advId);
				Advisor adv = advisorDao.fetchAdvisorByAdvId(advId, deleteflag, encryptPass);
				if (adv != null) {
					adv.setCreated_by(signedUserId);
					adv.setUpdated_by(signedUserId);
					advisorDao.addPublicAdvisor(adv, encryptPass);
					advisorDao.addPublicAdvPersonalInfo(advId, adv, encryptPass);
					if (adv.getAwards() != null && adv.getAwards().size() != 0) {
						for (Award award : adv.getAwards()) {
							award.setCreated_by(signedUserId);
							award.setUpdated_by(signedUserId);
							int awardResult = advisorDao.addAdvPublicAwardInfo(advId, award, deleteflag);
							if (awardResult == 0) {
								return awardResult;
							}
						}
					}
					if (adv.getCertificates() != null && adv.getCertificates().size() != 0) {
						for (Certificate certificate : adv.getCertificates()) {
							certificate.setCreated_by(signedUserId);
							certificate.setUpdated_by(signedUserId);
							int certResult = advisorDao.addAdvPublicCertificateInfo(advId, certificate, deleteflag);
							if (certResult == 0) {
								return certResult;
							}
						}
					}
					if (adv.getEducations() != null && adv.getEducations().size() != 0) {
						for (Education education : adv.getEducations()) {
							education.setCreated_by(signedUserId);
							education.setUpdated_by(signedUserId);
							int eduResult = advisorDao.addAdvPublicEducationInfo(advId, education, deleteflag);
							if (eduResult == 0) {
								return eduResult;
							}
						}
					}
					if (adv.getExperiences() != null && adv.getExperiences().size() != 0) {
						for (Experience experience : adv.getExperiences()) {
							experience.setCreated_by(signedUserId);
							experience.setUpdated_by(signedUserId);
							int expResult = advisorDao.addAdvPublicExperienceInfo(advId, experience, deleteflag);
							if (expResult == 0) {
								return expResult;
							}
						}
					}
					if (adv.getAdvProducts() != null && adv.getAdvProducts().size() != 0) {
						for (AdvProduct advProduct : adv.getAdvProducts()) {
							advProduct.setCreated_by(signedUserId);
							advProduct.setUpdated_by(signedUserId);
							int prodResult = advisorDao.addPublicAdvProductInfo(advId, advProduct, deleteflag);
							if (prodResult == 0) {
								return prodResult;
							}
						}
					}
					if (adv.getAdvBrandInfo() != null && adv.getAdvBrandInfo().size() != 0) {
						for (AdvBrandInfo advBrandInfo : adv.getAdvBrandInfo()) {
							advBrandInfo.setCreated_by(signedUserId);
							advBrandInfo.setUpdated_by(signedUserId);
							int brandResult = advisorDao.addPublicAdvBrandInfo(advId, advBrandInfo, deleteflag);
							if (brandResult == 0) {
								return brandResult;
							}
						}
					}
					if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
						for (AdvBrandRank advBrandRank : adv.getAdvBrandRank()) {
							// advBrandRank.setCreated_by(signedUserId);
							// advBrandRank.setUpdated_by(signedUserId);
							int rankResult = advisorDao.addPublicAdvBrandAndRank(advBrandRank.getBrandId(),
									advBrandRank.getRanking(), advId, advBrandRank.getProdId(), deleteflag,
									signedUserId);
							if (rankResult == 0) {
								return rankResult;
							}
						}
					}
					if (adv.getPromotions() != null && adv.getPromotions().size() != 0) {
						for (Promotion promotion : adv.getPromotions()) {
							promotion.setCreated_by(signedUserId);
							promotion.setUpdated_by(signedUserId);
							int promoResult = advisorDao.addPublicPromotion(advId, promotion, deleteflag);
							if (promoResult == 0) {
								return promoResult;
							}
						}
					}
					return result;
				}
			}
		} else if (status == status_revokeId) {
			result = advisorDao.addWorkFlowStatusRevokedByAdvId(advId, status_revokeId, signedUserId, reason);
		} else if (status == status_hideId) {
			result = advisorDao.addWorkFlowStatusByAdvId(advId, status_hideId, signedUserId, reason);
			if (result != 0) {
				int removeResult = advisorDao.removePublicAdvisorChild(advId);
				int remove = advisorDao.removePublicAdvisor(advId);
				if (remove == 0) {
					return 0;
				}
			}
		} else if (status == status_deleteId) {
			int result3 = advisorDao.removePublicAdvisorChild(advId);
			int result4 = advisorDao.removePublicAdvisor(advId);
			int result1 = advisorDao.deleteAdvisorChild(advId);
			int result2 = advisorDao.deleteAdvisor(advId);
			return result2;
		} else if (status == status_createdId && advisor.getWorkFlowStatus() == status_approvedId) {
			result = advisorDao.addWorkFlowStatusApprovedByAdvId(advId, status_createdWithApproved, signedUserId,
					reason);
			return result;
		} else {
			result = advisorDao.addWorkFlowStatusByAdvId(advId, status, signedUserId, reason);
		}
		return result;
	}

	@Transactional
	public List<Advisor> fetchApprovedAdv(int pageNum, int records) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisorList = advisorDao.fetchAllPublicAdvisor(pageable, deleteflag, encryptPass);
		if (advisorList.size() == 0) {
			return null;
		}
		for (Advisor adv : advisorList) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisorList;
	}
	// @Transactional
	// public List<AdvBrandInfo> fetchAdvBrandInfoByProdIdAndServiceId(long
	// prodId,
	// long serviceId, String advId) {
	// return
	// advisorDao.fetchAdvBrandInfoByProdIdAndServiceId(prodId,serviceId,advId);
	// }

	@Transactional
	public KeyPeople fetchKeyPeopleByKeyPeopleId(long keyPeopleId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchKeyPeopleByKeyPeopleId(keyPeopleId, deleteflag, encryptPass);
	}

	@Transactional
	public int modifyKeyPeople(long keyPeopleId, KeyPeople key) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		KeyPeople keyPeople = advisorDao.fetchKeyPeopleByKeyPeopleId(keyPeopleId, deleteflag, encryptPass);
		if (key.getFullName() != null) {
			keyPeople.setFullName(key.getFullName());
		}
		if (key.getDesignation() != null) {
			keyPeople.setDesignation(key.getDesignation());
		}
		if (key.getImage() != null) {
			keyPeople.setImage(key.getImage());
		}
		if (key.getParentPartyId() != 0) {
			keyPeople.setParentPartyId(key.getParentPartyId());
		}
		String signedUserId = getSignedInUser();
		keyPeople.setUpdated_by(signedUserId);
		return advisorDao.modifyKeyPeople(keyPeopleId, keyPeople, encryptPass);
	}

	@Transactional
	public int removeKeyPeople(long id) {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		int result = advisorDao.removeKeyPeople(id, deleteflag, signedUserId);
		return result;
	}

	@Transactional
	public List<Followers> fetchFollowersByUserId(String userId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		return advisorDao.fetchFollowersByUserId(userId, statusId);
	}

	@Transactional
	public int addFollowers(String advId, String userId) {
		// active status//
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);

		String desc = advTableFields.getUser_type_advisor();// advisor//
		String desc1 = advTableFields.getUser_type_investor();// investor//
		String signedUserId = getSignedInUser();

		long userTypeId = 0;
		if (userId.startsWith("ADV")) {
			userTypeId = advisorDao.fetchUserTypeIdByDesc(desc);
		} else if (userId.startsWith("INV")) {
			userTypeId = advisorDao.fetchUserTypeIdByDesc(desc1);
		}
		Followers followers = new Followers();
		followers.setAdvId(advId);
		followers.setUserId(userId);
		followers.setStatus(statusId);
		followers.setByWhom(userId);
		followers.setCreated_by(signedUserId);
		followers.setUpdated_by(signedUserId);
		followers.setUserType(userTypeId);

		int result = advisorDao.addFollowers(followers);
		return result;
	}

	@Transactional
	public int blockFollowers(long followersId, String blockedBy) {
		String status = advTableFields.getFollower_Status_Blocked();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		return advisorDao.updateFollowers(followersId, statusId, blockedBy, signedUserId);
	}

	@Transactional
	public List<Integer> fetchFollowersCount(String advId) {
		String desc = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		List<Integer> result = advisorDao.fetchFollowersCount(advId, statusId);
		return result;
	}

	@Transactional
	public List<Followers> fetchFollowersByAdvId(String advId) {
		List<Followers> followersList = advisorDao.fetchFollowers(advId);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Advisor pub_advisor = new Advisor();
		Investor investor = new Investor();

		for (Followers followers : followersList) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(followers.getAdvId(), deleteflag);
			followers.setPartyId(partyId);
			String userId = followers.getUserId();
			if (userId.startsWith("ADV")) {
				pub_advisor = advisorDao.fetchPublicAdvisorByAdvId(userId, deleteflag, encryptPass);
				if (pub_advisor.getAdvId() != null || pub_advisor.getName() != null) {
					followers.setName(pub_advisor.getDisplayName());
					followers.setProfileImage(pub_advisor.getImagePath());
					followers.setUserName(pub_advisor.getUserName());
				} else {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(userId, deleteflag, encryptPass);
					if (advisor.getDisplayName() != null) {
						followers.setName(advisor.getDisplayName());
					} else {
						followers.setName(advisor.getName());
					}
					followers.setProfileImage(advisor.getImagePath());
					followers.setUserName(advisor.getUserName());
				}
			} else if (userId.startsWith("INV")) {
				investor = advisorDao.fetchInvestorByInvId(userId, deleteflag, encryptPass);
				followers.setName(investor.getFullName());
				followers.setProfileImage(investor.getImagePath());
				followers.setUserName(investor.getUserName());
			}
		}
		return followersList;
	}

	@Transactional
	public int addOtpForPhoneNumber(String phoneNumber, String otp) {
		int result = advisorDao.addOtpForPhoneNumber(phoneNumber, otp);
		return result;
	}

	@Transactional
	public boolean validateOtp(String phoneNumber, String otp) {
		String savedOtp = advisorDao.fetchOtpByPhoneNumber(phoneNumber);
		if (otp.equals(savedOtp)) {
			return true;
		}
		return false;
	}

	@Transactional
	public Followers fetchFollowersByUserIdWithAdvId(String advId, String userId) {
		Followers result = advisorDao.fetchFollowersByUserIdWithAdvId(advId, userId);
		return result;
	}

	@Transactional
	public Followers fetchBlockedFollowersByUserIdWithAdvId(String advId, String userId) {
		// blocked status//
		String desc = advTableFields.getFollower_Status_Blocked();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		Followers result = advisorDao.fetchFollowersByUserIdWithAdvId(advId, userId, statusId);
		return result;
	}

	@Transactional
	public GeneratedOtp fetchGeneratedOtp(String phoneNumber, String otp) {
		return advisorDao.fetchGeneratedOtp(phoneNumber, otp);
	}

	@Transactional
	public List<WorkFlowStatus> fetchWorkFlowStatusList() {
		return advisorDao.fetchWorkFlowStatusList();
	}

	@Transactional
	public List<FollowerStatus> fetchFollowerStatusList() {
		return advisorDao.fetchFollowerStatusList();
	}

	@Transactional
	public int fetchWorkFlowStatusIdByDesc(String workFlow_Default) {
		return advisorDao.fetchWorkFlowStatusIdByDesc(workFlow_Default);
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorList(int pageNum, int records, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorList(pageable, sortByState, sortByCity, sortByPincode,
				sortByDisplayName, deleteflag, encryptPass, signedUserId);
		if (advisors == null) {
			return null;
		}
		// To fetch lookup table brand value
		for (Advisor adv : advisors) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisors;
	}

	@Transactional
	public List<ServicePlan> fetchExploreProductList(String productName, String serviceName, String brandName,
			String servicePlanName) {
		List<Product> productList = advisorDao.fetchExploreProductList(productName, serviceName, brandName);
		long productId = advisorDao.fetchProductByProductName(productName);
		long serviceId = advisorDao.fetchServiceByServiceName(serviceName, productId);
		long brandId = advisorDao.fetchBrandByBrandName(brandName, productId);
		long servicePlanId = advisorDao.fetchServicePlanIdByName(servicePlanName, productId, serviceId, brandId);

		List<ServicePlan> servicePlans = advisorDao.fetchServicePlanByProdIdAndServiceIdAndBrandIdAndServicePlanId(
				productId, serviceId, brandId, servicePlanId);

		return servicePlans;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProduct(int pageNum, int records, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String productId, String serviceId, String brandId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByProduct(pageable, sortByState, sortByCity,
				sortByPincode, sortByDisplayName, productId, serviceId, brandId, deleteflag, encryptPass);
		if (advisors == null || advisors.size() == 0) {
			return null;
		}
		List<Advisor> advList = new ArrayList<>();
		for (Advisor advisor : advisors) {
			// for (AdvProduct advProd : advisor.getAdvProducts()) {
			// List<AdvProduct> advProdList = advisorDao.fetchAdvProductByProdId(productId,
			// deleteflag);
			if (advisor.getAdvProducts() != null && advisor.getAdvProducts().size() != 0
					&& advisor.getAdvBrandInfo() != null && advisor.getAdvBrandInfo().size() != 0) {
				advList.add(advisor);
			}
			// }
		}

		// To fetch lookup table brand value
		for (Advisor adv : advList) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
		}
		return advList;
	}

	@Transactional
	public int fetchRoleIdByName(String roleName_admin) {
		return authDao.fetchRoleIdByName(roleName_admin);
	}

	@Transactional
	public List<Advisor> fetchSearchAdvisorList(int pageNum, int records, String panNumber, String emailId,
			String phoneNumber, String userName, String workFlowStatusId, String advType) {
		String deleteflag = "";
		String encryptPass = advTableFields.getEncryption_password();
		int deactivate = advisorDao.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_deactivated());

		Pageable pageable = PageRequest.of(pageNum, records);
		if (Integer.parseInt(workFlowStatusId) == deactivate) {
			deleteflag = advTableFields.getDelete_flag_Y();
		} else {
			deleteflag = advTableFields.getDelete_flag_N();
		}
		List<Advisor> advisors = advisorDao.fetchSearchAdvisorList(pageable, panNumber, emailId, phoneNumber, userName,
				deleteflag, workFlowStatusId, encryptPass, advType);
		if (advisors.size() == 0) {
			return null;
		}
		// To fetch lookup table brand value
		for (Advisor adv : advisors) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
			List<KeyPeople> keypeopleList = advisorDao.fetchKeyPeopleByParentId(partyId, deleteflag, encryptPass);
			adv.setKeyPeopleList(keypeopleList);
			List<Advisor> teamMemberList = advisorDao.fetchTeamByParentPartyId(partyId, deleteflag, encryptPass);
			adv.setTeamMemberList(teamMemberList);
		}
		return advisors;
	}

	@Transactional
	public List<UserType> fetchUserTypeList() {
		return advisorDao.fetchUserTypeList();
	}

	@Transactional
	public List<AdvisorType> fetchAdvisorTypeList() {
		return advisorDao.fetchAdvisorTypeList();
	}

	@Transactional
	public int updateFollowers(long followersId, String userId) {
		String status = advTableFields.getFollower_Status_Refollow();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		int result = advisorDao.updateFollowers(followersId, statusId, userId, signedUserId);
		return result;
	}

	@Transactional
	public int approveFollowers(long followersId, String advId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		int result = advisorDao.updateFollowers(followersId, statusId, advId, signedUserId);
		return result;
	}

	@Transactional
	public List<ChatUser> fetchChatUserListByUserId(String userId) {
		List<ChatUser> chatUserList = advisorDao.fetchChatUserListByUserId(userId);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		for (ChatUser chatUser : chatUserList) {
			Advisor pub_advisor = advisorDao.fetchPublicAdvisorByAdvId(chatUser.getAdvId(), deleteflag, encryptPass);
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(chatUser.getAdvId(), deleteflag);
			chatUser.setPartyId(partyId);
			if (pub_advisor.getAdvId() != null || pub_advisor.getName() != null) {
				chatUser.setName(pub_advisor.getDisplayName());
				chatUser.setProfileImage(pub_advisor.getImagePath());
			} else {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(chatUser.getAdvId(), deleteflag, encryptPass);
				if (advisor.getDisplayName() != null) {
					chatUser.setName(advisor.getDisplayName());
				} else {
					chatUser.setName(advisor.getName());
				}
				chatUser.setProfileImage(advisor.getImagePath());
			}
		}
		return chatUserList;
	}

	@Transactional
	public ChatUser fetchBlockedChatUsersByUserIdWithAdvId(String advId, String userId) {
		// blocked status//
		String desc = advTableFields.getFollower_Status_Blocked();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		ChatUser result = advisorDao.fetchChatUserByUserIdWithAdvId(advId, userId, statusId);
		return result;
	}

	@Transactional
	public int addChatUser(String advId, String userId) {
		// active status//
		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);

		String desc = advTableFields.getUser_type_advisor();// advisor//
		String desc1 = advTableFields.getUser_type_investor();// investor//

		long userTypeId = 0;
		if (userId.startsWith("ADV")) {
			userTypeId = advisorDao.fetchUserTypeIdByDesc(desc);
		} else if (userId.startsWith("INV")) {
			userTypeId = advisorDao.fetchUserTypeIdByDesc(desc1);
		}

		String signedUserId = getSignedInUser();

		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId(advId);
		chatUser.setUserId(userId);
		chatUser.setStatus(statusId);
		chatUser.setUserType(userTypeId);
		chatUser.setByWhom(userId);
		chatUser.setCreatedBy(signedUserId);
		chatUser.setUpdatedBy(signedUserId);
		int result = advisorDao.addChatUser(chatUser);
		return result;
	}

	@Transactional
	public int updateChatUser(long chatUserId, String userId) {
		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		int result = advisorDao.updateChatUser(chatUserId, statusId, userId, signedUserId);
		return result;
	}

	@Transactional
	public ChatUser fetchChatUserByUserIdWithAdvId(String advId, String userId) {
		String desc = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		ChatUser result = advisorDao.fetchChatUserByUserIdWithAdvId(advId, userId, statusId);
		return result;
	}

	@Transactional
	public int blockChat(long chatUserId, String blockedBy) {
		String status = advTableFields.getFollower_Status_Blocked();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		int result = advisorDao.modifyChat(chatUserId, statusId, blockedBy, signedUserId);
		return result;
	}

	@Transactional
	public List<Followers> fetchReFollowersByUserId(String userId) {
		String status_Refollow = advTableFields.getFollower_Status_Refollow();
		long statusId_Refollow = advisorDao.fetchFollowerStatusIdByDesc(status_Refollow);
		return advisorDao.fetchFollowersByUserId(userId, statusId_Refollow);
	}

	private String getSignedInUser() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String encryptPass = advTableFields.getEncryption_password();
			String delete_flag = advTableFields.getDelete_flag_N();
			if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
				return adminSignin.getAdmin_name();
			} else {
				Party party = advisorDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
				return party.getRoleBasedId();
			}
		} catch (Exception e) {
			return "";
		}
	}

	@Transactional
	public int fetchAdvisorTotalList() {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchAdvisorTotalList(deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalApprovedAdv() {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisorList = advisorDao.fetchAllTotalPublicAdvisor(deleteflag);
		return advisorList;
	}

	@Transactional
	public int fetchTotalExploreAdvisorList(String sortByState, String sortByCity, String sortByPincode,
			String sortByDisplayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String signedUserId = getSignedInUser();
		int advisors = advisorDao.fetchTotalExploreAdvisorList(sortByState, sortByCity, sortByPincode,
				sortByDisplayName, deleteflag, signedUserId);
		return advisors;

	}
	// @Transactional
	// public List<Advisor> fetchTotalExploreAdvisorList(String sortByState, String
	// sortByCity, String sortByPincode,
	// String sortByDisplayName) {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// // Pageable paging = PageRequest.of(pageNum, records);
	// List<Advisor> advisors = advisorDao.fetchTotalExploreAdvisorList(sortByState,
	// sortByCity, sortByPincode,
	// sortByDisplayName, deleteflag, encryptPass);
	// if (advisors.size() == 0) {
	// return null;
	// }
	// // To fetch lookup table brand value
	// for (Advisor adv : advisors) {
	// long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(),
	// deleteflag);
	// adv.setPartyId(partyId);
	// List<AdvBrandRank> advBrandRankList = new ArrayList<>();
	// if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
	// for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
	// String brand;
	// brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
	// advBrandRank1.setBrand(brand);
	// advBrandRankList.add(advBrandRank1);
	// }
	// adv.setAdvBrandRank(advBrandRankList);
	// }
	// }
	// return advisors;
	// }

	@Transactional
	public int fetchTotalExploreAdvisorByProduct(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId, String serviceId, String brandId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		// Pageable paging = PageRequest.of(pageNum, records);
		int advisors = advisorDao.fetchTotalExploreAdvisorByProduct(stateCityPincodeList, sortByDisplayName, productId,
				serviceId, brandId, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalSearchAdvisorList(String panNumber, String emailId, String phoneNumber, String userName,
			String workFlowStatusId, String advType) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		int advisors = advisorDao.fetchTotalSearchAdvisorList(panNumber, emailId, phoneNumber, userName, deleteflag,
				workFlowStatusId, encryptPass, advType);
		return advisors;
	}

	@Transactional
	public String fetchProductNameByProdId(int prodId) {
		return advisorDao.fetchProductNameByProdId(prodId);
	}

	@Transactional
	public String fetchServiceNameByProdIdAndServiceId(int prodId, int serviceId) {
		return advisorDao.fetchServiceNameByProdIdAndServiceId(prodId, serviceId);
	}

	@Transactional
	public String fetchBrandNameByProdIdAndBrandId(int prodId, int brandId) {
		return advisorDao.fetchBrandNameByProdIdAndBrandId(prodId, brandId);
	}

	@Transactional
	public ServicePlan fetchServicePlan(int prodId, int serviceId, int brandId, String planName) {
		return advisorDao.fetchServicePlan(prodId, serviceId, brandId, planName);
	}

	@Transactional
	public int addServicePlan(int prodId, int serviceId, int brandId, String planName, String url) {
		String signedUserId = getSignedInUser();
		return advisorDao.addServicePlan(prodId, serviceId, brandId, planName, url, signedUserId);
	}

	@Transactional
	public int updateServicePlan(int prodId, int serviceId, int brandId, String planName, String url) {
		String signedUserId = getSignedInUser();
		return advisorDao.updateServicePlan(prodId, serviceId, brandId, planName, url, signedUserId);
	}

	@Transactional
	public List<String> fetchPincodeListByPincode(String sortByPincode) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<String> pincodes = new ArrayList<>();
		pincodes.add(sortByPincode);
		String city = advisorDao.fetchCityByPincode(sortByPincode);
		List<String> cityPincodes = advisorDao.fetchPincodesByCity(city);
		pincodes.addAll(cityPincodes);

		String district = advisorDao.fetchDistrictByPincode(sortByPincode);
		List<String> districtPincodes = advisorDao.fetchPincodesByDistrict(district);
		pincodes.addAll(districtPincodes);

		long stateId = advisorDao.fetchStateIdByPincode(sortByPincode);
		List<String> statePincode = advisorDao.fetchPincodeByStateId(stateId);
		pincodes.addAll(statePincode);

		// List<Advisor> advisors = advisorDao.fetchAdvisorListByPincode(pincodes,
		// deleteflag, encryptPass);
		return pincodes;
	}

	@Transactional
	public List<String> fetchPincodeByState(String sortByState) {
		long stateId = advisorDao.fetchStateIdByState(sortByState);
		List<String> pincodes = advisorDao.fetchPincodeByState(stateId);
		return pincodes;
	}

	@Transactional
	public List<String> fetchPincodeByStateAndCity(String sortByState, String sortByCity) {
		List<String> pincodes = new ArrayList<>();

		// System.out.println("sortByCity===================" +sortByCity);
		// System.out.println("sortByState==================" + sortByState);
		List<String> cityPincode = advisorDao.fetchPincodeByCity(sortByCity);
		pincodes.addAll(cityPincode);
		// System.out.println("cityPincode========" + cityPincode.toString());
		String district = advisorDao.fetchDistrictByCity(sortByCity);
		// System.out.println("district ==================="+ district);

		List<String> pincodeDistrict = advisorDao.fetchPincodesByDistrict(district);
		// System.out.println("pincodeDistrict========" + pincodeDistrict.toString());

		pincodes.addAll(pincodeDistrict);
		long stateId = advisorDao.fetchStateIdByState(sortByState);

		List<String> pincodeState = advisorDao.fetchPincodeByState(stateId);
		// System.out.println("pincodeState========" + pincodeState.toString());

		pincodes.addAll(pincodeState);
		return pincodes;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorListByProduct(int pageNum, int records, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId, String brandId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisorList = advisorDao.fetchExploreAdvisorListByProduct(pageable, stateCityPincodeList,
				sortByDisplayName, productId, serviceId, brandId, deleteflag, encryptPass);
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorListByProductWithoutBrand(int pageNum, int records,
			List<String> stateCityPincodeList, String sortByDisplayName, String productId, String serviceId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisorList = advisorDao.fetchExploreAdvisorListByProductWithoutBrand(pageable,
				stateCityPincodeList, sortByDisplayName, productId, serviceId, deleteflag, encryptPass);
		return advisorList;
	}

	@Transactional
	public List<ServicePlan> fetchExploreProductListById(String productId, String serviceId, String brandId,
			String servicePlanName) {
		// List<Product> productList = advisorDao.fetchExploreProductList(productName,
		// serviceName, brandName);
		// long productId = advisorDao.fetchProductByProductName(productName);
		// long serviceId = advisorDao.fetchServiceByServiceName(serviceName, productId)
		// long brandId = advisorDao.fetchBrandByBrandName(brandName, productId);
		long servicePlanId = advisorDao.fetchServicePlanIdByName(servicePlanName, Long.parseLong(productId),
				Long.parseLong(serviceId), Long.parseLong(brandId));

		List<ServicePlan> servicePlans = advisorDao.fetchServicePlanByProdIdAndServiceIdAndBrandIdAndServicePlanId(
				Long.parseLong(productId), Long.parseLong(serviceId), Long.parseLong(brandId), servicePlanId);

		return servicePlans;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProductWithoutBrandId(List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisorList = advisorDao.fetchTotalExploreAdvisorByProductWithoutBrandId(stateCityPincodeList,
				sortByDisplayName, productId, serviceId, deleteflag);
		return advisorList;
	}

	@Transactional
	public List<CityList> searchStateCityPincodeByCity(String cityName) {
		List<CityList> cityList = new ArrayList<CityList>();
		cityList = advisorDao.searchStateCityPincodeByCity(cityName);
		return cityList;
	}

	@Transactional
	public int saveChatMessage(List<ChatMessage> chatMessageList) {
		int result = 0;
		for (ChatMessage chatMessage : chatMessageList) {
			result = advisorDao.saveChatMessage(chatMessage);
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public int checkAdvisorIsPresent(String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.checkAdvisorIsPresent(advId, deleteflag);
	}

	@Transactional
	public int checkAdvProductIsPresent(String advId, long advProdId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.checkAdvProductIsPresent(advId, advProdId, deleteflag);
	}

	@Transactional
	public int checkAdvBrandRankIsPresent(String advId, long prodId, int rank) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.checkAdvBrandRankIsPresent(advId, prodId, rank, deleteflag);
	}

	@Transactional
	public int checkKeyPeopleIsPresent(long keyPeopleId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.checkKeyPeopleIsPresent(keyPeopleId, deleteflag);
	}

	@Transactional
	public int checkPartyIsPresent(long partyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.checkPartyIsPresent(partyId, deleteflag);
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorDESCListOrder(int pageNum, int records, String sortByState,
			String sortByCity, String sortByPincode, String sortByDisplayName) {

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorDESCListOrder(pageable, sortByState, sortByCity,
				sortByPincode, sortByDisplayName, deleteflag, encryptPass, signedUserId);
		if (advisors.size() == 0) {
			return null;
		}
		// To fetch lookup table brand value
		for (Advisor adv : advisors) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisors;
	}

	@Transactional
	public int modifyAward(String advId, List<Award> awards) {
		List<Award> awardList = advisorDao.fetchAwardByadvId(advId, advTableFields.getDelete_flag_N());
		List<Long> awardIdList = new ArrayList<>();
		for (Award award : awards) {
			awardIdList.add(award.getAwardId());
		}
		if (awardList != null && awardList.size() != 0) {
			for (Award awd : awardList) {
				if (awardIdList.contains(awd.getAwardId()) == false) {
					int result1 = advisorDao.updateAdvisorTimeStamp(advId);
					String signedUserId = getSignedInUser();
					int resultRemove = advisorDao.removeAdvAward(awd.getAwardId(), advId,
							advTableFields.getDelete_flag_Y(), signedUserId);
					if (resultRemove == 0 || result1 == 0) {
						return 0;
					}
				}
			}
		}
		for (Award awd : awards) {
			int result = 0;
			if (awd.getAwardId() != 0) {
				long awardId = awd.getAwardId();
				Award award1 = advisorDao.fetchAdvAwardByAdvIdAndAwardId(awardId, advId,
						advTableFields.getDelete_flag_N());
				String signedUserId = getSignedInUser();
				if (awd.getImagePath() != null) {
					award1.setImagePath(awd.getImagePath());
				}
				if (awd.getIssuedBy() != null) {
					award1.setIssuedBy(awd.getIssuedBy());
				}
				if (awd.getTitle() != null) {
					award1.setTitle(awd.getTitle());
				}
				if (awd.getYear() != null) {
					award1.setYear(awd.getYear());
				}
				award1.setUpdated_by(signedUserId);
				result = advisorDao.modifyAdvisorAward(awardId, award1, advId);
			} else {
				String signedUserId = getSignedInUser();
				awd.setCreated_by(signedUserId);
				awd.setUpdated_by(signedUserId);
				result = advisorDao.addAdvAwardInfo(advId, awd, advTableFields.getDelete_flag_N());
			}
			if (result == 0) {
				return result;
			}
		}
		return 1;
	}

	@Transactional
	public int modifyCertificate(String advId, List<Certificate> certificateList) {
		List<Certificate> certList = advisorDao.fetchCertificateByadvId(advId, advTableFields.getDelete_flag_N());
		List<Long> certificateIdList = new ArrayList<>();
		for (Certificate cert : certificateList) {
			certificateIdList.add(cert.getCertificateId());
		}
		if (certList != null && certList.size() != 0) {
			for (Certificate cert : certList) {
				if (certificateIdList.contains(cert.getCertificateId()) == false) {
					int result1 = advisorDao.updateAdvisorTimeStamp(advId);
					String signedUserId = getSignedInUser();
					int resultRemove = advisorDao.removeAdvCertificate(cert.getCertificateId(), advId,
							advTableFields.getDelete_flag_Y(), signedUserId);
					if (resultRemove == 0 || result1 == 0) {
						return 0;
					}
				}
			}
		}
		for (Certificate certificate : certificateList) {
			int result = 0;
			if (certificate.getCertificateId() != 0) {
				Certificate certificate1 = advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(
						certificate.getCertificateId(), advId, advTableFields.getDelete_flag_N());
				String signedUserId = getSignedInUser();
				if (certificate.getImagePath() != null) {
					certificate1.setImagePath(certificate.getImagePath());
				}
				if (certificate.getIssuedBy() != null) {
					certificate1.setIssuedBy(certificate.getIssuedBy());
				}
				if (certificate.getTitle() != null) {
					certificate1.setTitle(certificate.getTitle());
				}
				if (certificate.getYear() != null) {
					certificate1.setYear(certificate.getYear());
				}
				certificate1.setUpdated_by(signedUserId);
				result = advisorDao.modifyAdvisorCertificate(certificate.getCertificateId(), certificate1, advId);
			} else {
				String signedUserId = getSignedInUser();
				certificate.setCreated_by(signedUserId);
				certificate.setUpdated_by(signedUserId);
				result = advisorDao.addAdvCertificateInfo(advId, certificate, advTableFields.getDelete_flag_N());
			}
			if (result == 0) {
				return result;
			}
		}
		return 1;
	}

	@Transactional
	public int modifyExperience(String advId, List<Experience> experienceList) {
		List<Experience> expList = advisorDao.fetchExperienceByadvId(advId, advTableFields.getDelete_flag_N());
		List<Long> experienceIdList = new ArrayList<>();
		for (Experience exp : experienceList) {
			experienceIdList.add(exp.getExpId());
		}
		if (expList != null && expList.size() != 0) {
			for (Experience exp : expList) {
				if (experienceIdList.contains(exp.getExpId()) == false) {
					int result1 = advisorDao.updateAdvisorTimeStamp(advId);
					String signedUserId = getSignedInUser();
					int resultRemove = advisorDao.removeAdvExperience(exp.getExpId(), advId,
							advTableFields.getDelete_flag_Y(), signedUserId);
					if (resultRemove == 0 || result1 == 0) {
						return 0;
					}
				}
			}
		}
		for (Experience experience : experienceList) {
			int result = 0;
			if (experience.getExpId() != 0) {
				Experience experience1 = advisorDao.fetchAdvExperienceByAdvIdAndExpId(experience.getExpId(), advId,
						advTableFields.getDelete_flag_N());
				String signedUserId = getSignedInUser();
				if (experience.getCompany() != null) {
					experience1.setCompany(experience.getCompany());
				}
				if (experience.getDesignation() != null) {
					experience1.setDesignation(experience.getDesignation());
				}
				if (experience.getLocation() != null) {
					experience1.setLocation(experience.getLocation());
				}
				if (experience.getFromYear() != null) {
					experience1.setFromYear(experience.getFromYear());
				}
				if (experience.getToYear() != null) {
					experience1.setToYear(experience.getToYear());
				}
				experience1.setUpdated_by(signedUserId);
				result = advisorDao.modifyAdvisorExperience(experience.getExpId(), experience1, advId);
			} else {
				String signedUserId = getSignedInUser();
				experience.setCreated_by(signedUserId);
				experience.setUpdated_by(signedUserId);
				result = advisorDao.addAdvExperienceInfo(advId, experience, advTableFields.getDelete_flag_N());
			}
			if (result == 0) {
				return result;
			}
		}
		return 1;
	}

	@Transactional
	public int modifyEducation(String advId, List<Education> educationList) {
		List<Education> eduList = advisorDao.fetchEducationByadvId(advId, advTableFields.getDelete_flag_N());
		List<Long> educationIdList = new ArrayList<>();
		for (Education edu : educationList) {
			educationIdList.add(edu.getEduId());
		}
		if (eduList != null && eduList.size() != 0) {
			for (Education edu : eduList) {
				if (educationIdList.contains(edu.getEduId()) == false) {
					int result1 = advisorDao.updateAdvisorTimeStamp(advId);
					String signedUserId = getSignedInUser();
					int resultRemove = advisorDao.removeAdvEducation(edu.getEduId(), advId,
							advTableFields.getDelete_flag_Y(), signedUserId);
					if (resultRemove == 0 || result1 == 0) {
						return 0;
					}
				}
			}
		}
		for (Education edu : educationList) {
			int result = 0;
			if (edu.getEduId() != 0) {
				Education education1 = advisorDao.fetchAdvEducationByAdvIdAndEduId(edu.getEduId(), advId,
						advTableFields.getDelete_flag_N());
				String signedUserId = getSignedInUser();
				if (edu.getDegree() != null) {
					education1.setDegree(edu.getDegree());
				}
				if (edu.getInstitution() != null) {
					education1.setInstitution(edu.getInstitution());
				}
				if (edu.getField() != null) {
					education1.setField(edu.getField());
				}
				if (edu.getFromYear() != null) {
					education1.setFromYear(edu.getFromYear());
				}
				if (edu.getToYear() != null) {
					education1.setToYear(edu.getToYear());
				}
				education1.setUpdated_by(signedUserId);
				result = advisorDao.modifyAdvisorEducation(edu.getEduId(), education1, advId);
			} else {
				String signedUserId = getSignedInUser();
				edu.setCreated_by(signedUserId);
				edu.setUpdated_by(signedUserId);
				result = advisorDao.addAdvEducationInfo(advId, edu, advTableFields.getDelete_flag_N());
			}
			if (result == 0) {
				return result;
			}
		}
		return 1;
	}

	@Transactional
	public int addAndModifyPromotion(String advId, List<Promotion> promotionList) {
		String deleteflag = advTableFields.getDelete_flag_N();
		List<Promotion> promoList = advisorDao.fetchPromotionByAdvId(advId, deleteflag);
		List<Long> promotionIdList = new ArrayList<>();
		for (Promotion promo : promotionList) {
			promotionIdList.add(promo.getPromotionId());
		}
		if (promoList != null && promoList.size() != 0) {
			for (Promotion promo : promoList) {
				if (promotionIdList.contains(promo.getPromotionId()) == false) {
					int result1 = advisorDao.updateAdvisorTimeStamp(advId);
					String signedUserId = getSignedInUser();
					int resultRemove = advisorDao.removePromotion(promo.getPromotionId(),
							advTableFields.getDelete_flag_Y(), signedUserId);
					if (resultRemove == 0 || result1 == 0) {
						return 0;
					}
				}
			}
		}
		for (Promotion promo : promotionList) {
			int result = 0;
			if (promo.getPromotionId() != 0) {
				// getvalue
				long promotionId = promo.getPromotionId();
				Promotion promotion = advisorDao.fetchPromotionByAdvIdAndPromotionId(promotionId, advId, deleteflag);
				if (promo.getTitle() != null) {
					promotion.setTitle(promo.getTitle());
				}
				if (promo.getAboutVideo() != null) {
					promotion.setAboutVideo(promo.getAboutVideo());
				}
				if (promo.getVideo() != null) {
					promotion.setVideo(promo.getVideo());
				}
				if (promo.getImagePath() != null) {
					promotion.setImagePath(promo.getImagePath());
				}
				String signedUserId = getSignedInUser();
				promotion.setUpdated_by(signedUserId);
				result = advisorDao.modifyPromotion(promotionId, promotion, advId, deleteflag);
			} else {
				String signedUserId = getSignedInUser();
				promo.setCreated_by(signedUserId);
				promo.setUpdated_by(signedUserId);
				result = advisorDao.addPromotion(advId, promo, deleteflag);
			}
			if (result == 0) {
				return result;
			}
		}
		return 1;
	}

	@Transactional
	public int addAdvProductInfoList(String advId, List<AdvProduct> advProductList) {
		int result = 0;
		for (AdvProduct advProduct : advProductList) {
			advisorDao.updateAdvisorTimeStamp(advId);
			String signedUserId = getSignedInUser();
			advProduct.setCreated_by(signedUserId);
			advProduct.setUpdated_by(signedUserId);
			result = advisorDao.addAdvProductInfo(advId, advProduct, advTableFields.getDelete_flag_N());
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public int addBrandRankIntoTable(HashMap<Long, Integer> sortedBrandAndRank, String advId, long prodId) {
		for (Map.Entry<Long, Integer> entry : sortedBrandAndRank.entrySet()) {
			int result = 0;
			if (advisorDao.checkAdvBrandRankIsPresent(advId, prodId, entry.getValue(),
					advTableFields.getDelete_flag_N()) == 0) {
				// Add brandRank
				String signedUserId = getSignedInUser();
				result = advisorDao.addAdvBrandAndRank(entry.getKey(), entry.getValue(), advId, prodId,
						advTableFields.getDelete_flag_N(), signedUserId);
			} else {
				// Update brandRank
				String signedUserId = getSignedInUser();
				result = advisorDao.updateBrandAndRank(entry.getKey(), entry.getValue(), advId, prodId,
						advTableFields.getDelete_flag_N(), signedUserId);
			}
			if (result == 0) {
				return result;
			}
		}
		return 1;
	}

	@Transactional
	public int addAndModifyProductInfo(String advId, List<AdvProduct> advProductList) {
		int result = 0;
		for (AdvProduct advProduct : advProductList) {
			if (advProduct.getAdvProdId() != 0) {
				String signedUserId = getSignedInUser();
				AdvProduct advProduct1 = advisorDao.fetchAdvProduct(advProduct.getAdvProdId(),
						advTableFields.getDelete_flag_N());
				if (advProduct.getProdId() != 0) {
					advProduct1.setProdId(advProduct.getProdId());
				}
				if (advProduct.getServiceId() != null) {
					advProduct1.setServiceId(advProduct.getServiceId());
				}
				if (advProduct.getRemId() != 0) {
					advProduct1.setRemId(advProduct.getRemId());
				}
				if (advProduct.getLicId() != 0) {
					advProduct1.setLicId(advProduct.getLicId());
				}
				if (advProduct.getLicNumber() != null) {
					advProduct1.setLicNumber(advProduct.getLicNumber());
				}
				if (advProduct.getValidity() != null) {
					advProduct1.setValidity(advProduct.getValidity());
				}
				if (advProduct.getLicImage() != null) {
					advProduct1.setLicImage(advProduct.getLicImage());
				}
				advProduct1.setUpdated_by(signedUserId);
				result = advisorDao.modifyAdvisorProduct(advProduct1, advId);
			} else {
				advisorDao.updateAdvisorTimeStamp(advId);
				String signedUserId = getSignedInUser();
				advProduct.setCreated_by(signedUserId);
				advProduct.setUpdated_by(signedUserId);
				result = advisorDao.addAdvProductInfo(advId, advProduct, advTableFields.getDelete_flag_N());
			}
			if (result == 0) {
				return 0;
			}
		}
		return result;
	}

	@Transactional
	public Advisor fetchAdvisorByUserNameWithOutToken(String userName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Advisor advisor = advisorDao.fetchAdvisorByUserNameWithOutToken(userName, deleteflag, encryptPass);
		if (advisor.getAdvId() == null && advisor.getEmailId() == null) {
			return null;
		}
		if (advisor.getParentPartyId() != 0) {
			Party party = advisorDao.fetchPartyByPartyId(advisor.getParentPartyId(), deleteflag, encryptPass);
			if (party != null) {
				advisor.setCorporateUsername(party.getUserName());
			}
		}
		if (advisor != null) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(advisor.getAdvId(), deleteflag);
			advisor.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (advisor.getAdvBrandRank() != null && advisor.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : advisor.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				advisor.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisor;
	}

	@Transactional
	public Party fetchPartyByPhoneNumberAndDeleteFlag(String phoneNumber) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchPartyByPhoneNumberAndDeleteFlag(phoneNumber, deleteflag, encryptPass);
	}

	@Transactional
	public ChatUser fetchActiveChatUser(String advId, String userId) {
		String desc = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		ChatUser result = advisorDao.fetchChatUserByUserIdWithAdvId(advId, userId, statusId);
		return result;
	}

	@Transactional
	public ChatUser fetchChatUser(String advId, String userId) {
		ChatUser result = advisorDao.fetchChatUser(advId, userId);
		return result;
	}

	@Transactional
	public int approveChat(long chatUserId, String advId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();
		return advisorDao.modifyChat(chatUserId, statusId, advId, signedUserId);
	}

	@Transactional
	public List<ChatUser> fetchChatUserListByAdvId(String advId) {
		List<ChatUser> chatUserList = advisorDao.fetchChatUserListByAdvId(advId);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Advisor pub_advisor = new Advisor();
		Investor investor = new Investor();

		for (ChatUser chatUser : chatUserList) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(chatUser.getUserId(), deleteflag);
			chatUser.setPartyId(partyId);
			String userId = chatUser.getUserId();
			if (userId.startsWith("ADV")) {
				pub_advisor = advisorDao.fetchPublicAdvisorByAdvId(userId, deleteflag, encryptPass);
				if (pub_advisor.getAdvId() != null || pub_advisor.getName() != null) {
					chatUser.setName(pub_advisor.getDisplayName());
					chatUser.setProfileImage(pub_advisor.getImagePath());
				} else {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(userId, deleteflag, encryptPass);
					if (advisor.getDisplayName() != null) {
						chatUser.setName(advisor.getDisplayName());
					} else {
						chatUser.setName(advisor.getName());
					}
					chatUser.setProfileImage(advisor.getImagePath());
				}
			} else if (userId.startsWith("INV")) {
				investor = advisorDao.fetchInvestorByInvId(userId, deleteflag, encryptPass);
				chatUser.setName(investor.getFullName());
				chatUser.setProfileImage(investor.getImagePath());
			}
		}

		return chatUserList;
	}

	@Transactional
	public int checkBlockedChatUserIsPresent(String userId) {
		String status = advTableFields.getFollower_Status_Blocked();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		return advisorDao.checkChatUserIsPresent(userId, statusId);
	}

	@Transactional
	public List<Followers> fetchFollowersListByUserId(String userId) {
		List<Followers> followersList = advisorDao.fetchFollowersListByUserId(userId);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		for (Followers followers : followersList) {
			Advisor pub_advisor = advisorDao.fetchPublicAdvisorByAdvId(followers.getAdvId(), deleteflag, encryptPass);
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(followers.getAdvId(), deleteflag);
			followers.setPartyId(partyId);
			if (pub_advisor.getAdvId() != null || pub_advisor.getName() != null) {
				followers.setName(pub_advisor.getDisplayName());
				followers.setProfileImage(pub_advisor.getImagePath());
				followers.setUserName(pub_advisor.getUserName());
			} else {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(followers.getAdvId(), deleteflag, encryptPass);
				if (advisor.getDisplayName() != null) {
					followers.setName(advisor.getDisplayName());
				} else {
					followers.setName(advisor.getName());
				}
				followers.setProfileImage(advisor.getImagePath());
				followers.setUserName(advisor.getUserName());
			}
		}
		return followersList;

	}

	@Transactional
	public int checkFollowersIsPresent(String userId, String advId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		return advisorDao.checkFollowersIsPresent(userId, advId, statusId);
	}

	@Transactional
	public int checkReFollowersIsPresent(String userId, String advId) {
		String status_Refollow = advTableFields.getFollower_Status_Refollow();
		long statusId_Refollow = advisorDao.fetchFollowerStatusIdByDesc(status_Refollow);
		return advisorDao.checkFollowersIsPresent(userId, advId, statusId_Refollow);
	}

	@Transactional
	public int checkChatUserIsPresent(String userId, String advId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		return advisorDao.checkChatUserIsPresent(userId, advId, statusId);
	}

	@Transactional
	public int checkInActiveChatUserIsPresent(String userId, String advId) {
		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		return advisorDao.checkChatUserIsPresent(userId, advId, statusId);
	}

	@Transactional
	public long fetchActiveFollowersStatus() {
		String statusId = advTableFields.getFollower_Status_Active();
		return advisorDao.fetchFollowersStatus(statusId);
	}

	@Transactional
	public long fetchReFollowersStatus() {
		String statusId = advTableFields.getFollower_Status_Refollow();
		return advisorDao.fetchFollowersStatus(statusId);
	}

	@Transactional
	public long fetchBlockedFollowersStatus() {
		String statusId = advTableFields.getFollower_Status_Blocked();
		return advisorDao.fetchFollowersStatus(statusId);
	}

	@Transactional
	public long fetchInActiveFollowersStatus() {
		String statusId = advTableFields.getFollower_Status_Inactive();
		return advisorDao.fetchFollowersStatus(statusId);
	}

	@Transactional
	public List<Integer> fetchChatUserCount(String advId) {
		String desc = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		List<Integer> result = advisorDao.fetchChatUserCount(advId, statusId);
		return result;
	}

	@Transactional
	public int unFollowByUserId(long followersId, String userId) {
		String status = advTableFields.getFollower_status_unfollow();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		return advisorDao.updateFollowers(followersId, statusId, userId, signedUserId);
	}

	@Transactional
	public Followers fetchUnFollowersByUserIdWithAdvId(String advId, String userId) {
		// unfollow status//
		String desc = advTableFields.getFollower_status_unfollow();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(desc);
		Followers result = advisorDao.fetchFollowersByUserIdWithAdvId(advId, userId, statusId);
		return result;

	}

	@Transactional
	public long fetchUnFollowersStatus() {
		String statusId = advTableFields.getFollower_status_unfollow();
		return advisorDao.fetchFollowersStatus(statusId);
	}

	@Transactional
	public int updateUnFollowers(long followersId, String userId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		String signedUserId = getSignedInUser();

		int result = advisorDao.updateFollowers(followersId, statusId, userId, signedUserId);
		return result;
	}

	@Transactional
	public int fetchFollowersCountByUserId(String userId) {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = advisorDao.fetchFollowerStatusIdByDesc(status);
		return advisorDao.fetchFollowersCountByUserId(userId, statusId);
	}

	@Transactional
	public int fetchSharedPlanCountPartyId(long partyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.fetchSharedPlanCountPartyId(partyId,deleteflag);
	}

	@Transactional
	public int fetchPlannedUserCountPartyId(long partyId) {
		return advisorDao.fetchPlannedUserCountPartyId(partyId);
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorListByProdId(int pageNum, int records, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisorList = advisorDao.fetchExploreAdvisorListByProdId(pageable, stateCityPincodeList,
				sortByDisplayName, productId, deleteflag, encryptPass);
		return advisorList;
	}

	@Transactional
	public int fetchTotalExploreAdvisorListByProdId(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisorList = advisorDao.fetchTotalExploreAdvisorListByProdId(stateCityPincodeList, sortByDisplayName,
				productId, deleteflag);
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchPublishTeamByParentPartyId(long parentPartyId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisors = advisorDao.fetchPublishTeamByParentPartyId(parentPartyId, deleteflag, encryptPass);
		for (Advisor adv : advisors) {
			Party party = advisorDao.fetchPartyByPartyId(adv.getParentPartyId(), deleteflag, encryptPass);
			if (party != null) {
				adv.setCorporateUsername(party.getUserName());
			}
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
		}
		return advisors;
	}

	@Transactional
	public int updateMobileAsVerified(Party party) {
		int accountVerified = advTableFields.getAccount_verified();
		int result = 0;
		if (party.getRoleBasedId().startsWith("ADV")) {
			result = advisorDao.updateAdvisorMobileAsVerified(party.getRoleBasedId(), accountVerified);
		} else if (party.getRoleBasedId().startsWith("INV")) {
			result = advisorDao.updateInvestorMobileAsVerified(party.getRoleBasedId(), accountVerified);
		}
		return result;
	}

	@Transactional
	public int fetchTotalSearchAdvisorListWithEmptyValues(String pan_lc, String email_lc, String phone_lc,
			String userName_lc, String workFlowStatusId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		// Pageable paging = PageRequest.of(pageNum, records);
		int advisors = advisorDao.fetchTotalSearchAdvisorListWithEmptyValues(pan_lc, email_lc, phone_lc, userName_lc,
				deleteflag, workFlowStatusId, encryptPass);
		return advisors;
	}

	@Transactional
	public List<Advisor> fetchSearchAdvisorListWithEmptyValues(int pageNum, int records, String pan_lc, String email_lc,
			String phone_lc, String userName_lc, String workFlowStatusId) {
		String deleteflag = "";
		String encryptPass = advTableFields.getEncryption_password();
		int deactivate = advisorDao.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_deactivated());

		Pageable pageable = PageRequest.of(pageNum, records);
		if (Integer.parseInt(workFlowStatusId) == deactivate) {
			deleteflag = advTableFields.getDelete_flag_Y();
		} else {
			deleteflag = advTableFields.getDelete_flag_N();
		}
		List<Advisor> advisors = advisorDao.fetchSearchAdvisorListWithEmptyValues(pageable, pan_lc, email_lc, phone_lc,
				userName_lc, deleteflag, workFlowStatusId, encryptPass);
		if (advisors.size() == 0) {
			return null;
		}
		// To fetch lookup table brand value
		for (Advisor adv : advisors) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(), deleteflag);
			adv.setPartyId(partyId);
			List<AdvBrandRank> advBrandRankList = new ArrayList<>();
			if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
				for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
					String brand;
					brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
					advBrandRank1.setBrand(brand);
					advBrandRankList.add(advBrandRank1);
				}
				adv.setAdvBrandRank(advBrandRankList);
			}
		}
		return advisors;
	}

	@Transactional
	public Advisor fetchAdvisorGstByAdvId(String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchAdvisorGstByAdvId(advId, deleteflag, encryptPass);
	}

	// @Transactional
	// public List<Award> fetchAwardByadvId(long advid) {
	// return advisorDao.fetchAwardByadvId(advid);
	// }
	//
	// @Transactional
	// public List<Education> fetchEducationByadvId(long advid) {
	// return advisorDao.fetchEducationByadvId(advid);
	// }
	//
	// @Transactional
	// public List<Experience> fetchExperienceByadvId(long advid) {
	// return advisorDao.fetchExperienceByadvId(advid);
	//
	// }

	@Transactional
	public LookUp fetchAllLookUp() {
		List<Product> productList = advisorDao.fetchProductList();
		for (Product prod : productList) {
			for (com.sowisetech.advisor.model.Service serv : prod.getServices()) {
				List<ServicePlan> servicePlans = advisorDao.fetchServicePlanByServiceId(serv.getServiceId());
				serv.setServicePlans(servicePlans);
			}
		}
		List<com.sowisetech.advisor.model.Service> serviceList = advisorDao.fetchServiceList();
		List<PartyStatus> partyStatus = advisorDao.fetchPartyStatusList();
		List<Brand> brandList = advisorDao.fetchBrandList();
		List<License> licenseList = advisorDao.fetchLicenseList();
		List<Remuneration> remunerationList = advisorDao.fetchRemunerationList();
		List<State> stateList = advisorDao.fetchAllStateCityPincode();
		List<Category> category = advisorDao.fetchCategoryList();
		List<CategoryType> categoryType = advisorDao.fetchCategoryTypeList();
		List<RiskQuestionaire> riskQuestionaire = advisorDao.fetchAllRiskQuestionaire();
		List<ForumSubCategory> forumSubCategory = advisorDao.fetchForumSubCategoryList();
		List<ForumStatus> forumStatus = advisorDao.fetchForumStatusList();
		List<ArticleStatus> articleStatus = advisorDao.fetchArticleStatusList();
		List<FollowerStatus> followersStatus = advisorDao.fetchFollowerStatusList();
		List<WorkFlowStatus> workFlowStatus = advisorDao.fetchWorkFlowStatusList();
		List<AdvisorType> advisorTypeList = advisorDao.fetchAdvisorTypeList();
		List<UserType> userType = advisorDao.fetchUserTypeList();
		List<RoleAuth> roleList = authDao.fetchRoleList();
		LookUp lookUp = new LookUp();
		lookUp.setProductList(productList);
		lookUp.setPartyStatus(partyStatus);
		lookUp.setStateList(stateList);
		lookUp.setServiceList(serviceList);
		lookUp.setLicenseList(licenseList);
		lookUp.setRemunerationList(remunerationList);
		lookUp.setBrandList(brandList);
		lookUp.setCategory(category);
		lookUp.setCategoryType(categoryType);
		lookUp.setRiskQuestionaire(riskQuestionaire);
		lookUp.setForumSubCategory(forumSubCategory);
		lookUp.setForumStatus(forumStatus);
		lookUp.setArticleStatus(articleStatus);
		lookUp.setFolloersStatus(followersStatus);
		lookUp.setWorkFlowStatus(workFlowStatus);
		lookUp.setAdvisorTypeList(advisorTypeList);
		lookUp.setUserType(userType);
		lookUp.setRoleList(roleList);
		return lookUp;

	}

	@Transactional
	public int fetchBrands(long brandId) {
		return advisorDao.fetchBrands(brandId);
	}

	@Transactional
	public int createBrandsComment(BrandsComment comment) {
		String signedUserId = getSignedInUser();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = advisorDao.fetchParty(comment.getPartyId(), encryptPass);
		if (party.getRoleBasedId().startsWith("ADV")) {
			Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
			if (advisor.getDisplayName() != null) {
				comment.setName(advisor.getDisplayName());
			} else {
				comment.setName(advisor.getName());
			}
			comment.setDesignation(advisor.getDesignation());
			comment.setImagePath(advisor.getImagePath());
		} else if (party.getRoleBasedId().startsWith("INV")) {
			Investor investor = advisorDao.fetchInvestorByInvId(party.getRoleBasedId(), delete_flag, encryptPass);
			comment.setName(investor.getFullName());
			comment.setImagePath(investor.getImagePath());
		}
		comment.setCreated_by(signedUserId);
		comment.setUpdated_by(signedUserId);
		comment.setDelete_flag(delete_flag);

		return advisorDao.createBrandsComment(comment, encryptPass);

	}

	@Transactional
	public int removeBrandsComment(long commentId) {
		String delete_flag = advTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		return advisorDao.removeBrandsComment(commentId, delete_flag, signedUserId);
	}

	@Transactional
	public int fetchTotalBrandCommentByParentId(long brandId, long parentCommentId) {
		String delete_flag = advTableFields.getDelete_flag_N();
		return advisorDao.fetchTotalBrandsCommentByParentId(brandId, parentCommentId, delete_flag);
	}

	@Transactional
	public List<BrandsComment> fetchBrandCommentByParentId(String paramId, long parentCommentId) {
		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<BrandsComment> brandsCommentList = advisorDao.fetchBrandsCommentByParentId(paramId, parentCommentId,
				delete_flag, encryptPass);
		if (brandsCommentList != null) {
			for (BrandsComment brandsComment : brandsCommentList) {
				Party party = advisorDao.fetchParty(brandsComment.getPartyId(), encryptPass);
				if (party != null && party.getRoleBasedId().startsWith("ADV")) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
					brandsComment.setUserName(advisor.getUserName());
				}
			}
		}
		return brandsCommentList;
	}

	@Transactional
	public int modifyComment(BrandsComment brandsComment) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.modifyComment(brandsComment, deleteflag);
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByBrand(String brand) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		long brandId = Long.parseLong(brand);

		List<AdvBrandInfo> advList = advisorDao.fetchExploreAdvisorList(brandId, deleteflag);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		if (advList != null && advList.size() != 0) {
			for (AdvBrandInfo advBrandInfo : advList) {
				advisor = advisorDao.fetchAdvisorByAdvId(advBrandInfo.getAdvId(), deleteflag, encryptPass);
				advisorList.add(advisor);
			}
		}
		if (advisor == null) {
			return null;
		}
		return advisorList;
	}

	@Transactional
	public long fetchBloggerIdByName() {
		String name = advTableFields.getRoleName_blogger();
		return authDao.fetchRoleIdByName(name);
	}

	@Transactional
	public String generateIdBlogger() {
		String id = advisorDao.fetchBloggerSmartId();
		if (id != null) {
			String newId = idIncrementBlogger(id);
			int result = advisorDao.addBloggerSmartId(newId);
			if (result == 0) {
				return null;
			} else {
				return newId;
			}
		} else {
			String newId = "BLG0000000000";
			advisorDao.addBloggerSmartId(newId);
			return newId;
		}
	}

	private String idIncrementBlogger(String id) {
		String middle = id.substring(3, 12);
		String suffix;
		String newId;
		if (Character.isDigit(id.charAt(12))) {
			if (id.charAt(12) != '9') {
				String number = id.substring(3, 13);
				long num = Long.parseLong(number);
				middle = String.format("%010d", num + 1);
				newId = "BLG" + middle;
			} else {
				newId = "BLG" + middle + "A";
			}
		} else {
			if (id.charAt(12) != 'Z') {
				char last = id.charAt(12);
				suffix = String.valueOf((char) (last + 1));
				newId = id.substring(0, 12) + suffix;
			} else {
				long num = Long.parseLong(middle);
				middle = String.format("%09d", num + 1);
				newId = "BLG" + middle + "0";
			}
		}
		return newId;
	}

	@Transactional
	public int addBlogger(Blogger blog, long roleId) {
		String password = encrypt(blog.getPassword());
		String encryptPass = advTableFields.getEncryption_password();
		// String desc = "active";
		long partyStatusId = advisorDao.fetchPartyStatusIdByDesc(advTableFields.getPartystatus_desc());
		blog.setPassword(password);
		blog.setPartyStatusId(partyStatusId);
		blog.setCreated_by(blog.getBloggerId());
		blog.setUpdated_by(blog.getBloggerId());
		int result = advisorDao.addBlogger(blog, advTableFields.getDelete_flag_N(), encryptPass);
		int result1 = 0;
		if (result != 0) {
			result1 = advisorDao.addPartyBlogger(blog, advTableFields.getDelete_flag_N(), encryptPass);
		}
		int result2 = 0;
		if (result1 != 0) {
			result2 = advisorDao.addSigninVerification(blog.getEmailId(), encryptPass);
		}
		long partyId = advisorDao.fetchPartyIdByRoleBasedId(blog.getBloggerId(), advTableFields.getDelete_flag_N());
		if (result1 != 0) {
			int isPrimaryRole = advTableFields.getIs_primary_role_true();
			// String signedUserId = getSignedInUser();

			int result3 = authDao.addUser_role(partyId, roleId, blog.getCreated_by(), isPrimaryRole);
		}
		return result2;
	}

	@Transactional
	public Blogger fetchBloggerByBloggerId(String roleBasedId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Blogger blogger = advisorDao.fetchBloggerByBloggerId(roleBasedId, deleteflag, encryptPass);
		if (blogger.getBloggerId() == null && blogger.getEmailId() == null) {
			return null;
		}
		return blogger;
	}

	@Transactional
	public int fetchBloggerTotalList() {
		String deleteflag = advTableFields.getDelete_flag_N();
		int blogger = advisorDao.fetchBloggerTotalList(deleteflag);
		return blogger;
	}

	@Transactional
	public List<Blogger> fetchBloggerList(int pageNum, int records) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Blogger> blogger = advisorDao.fetchBlogger(pageable, deleteflag, encryptPass);
		for (Blogger blog : blogger) {
			long partyId = advisorDao.fetchPartyIdByRoleBasedId(blog.getBloggerId(), deleteflag);
			blog.setPartyId(partyId);
		}
		return blogger;
	}

	@Transactional
	public Blogger fetchByBloggerId(String bloggerId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		long partyId = advisorDao.fetchPartyIdByRoleBasedId(bloggerId, deleteflag);
		Blogger blogger = advisorDao.fetchBloggerByBloggerId(bloggerId, deleteflag, encryptPass);
		blogger.setPartyId(partyId);
		return blogger;
	}

	@Transactional
	public int CheckBloggerIsPresent(String bloggerId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return advisorDao.checkBloggerIsPresent(bloggerId, deleteflag);
	}

	@Transactional
	public int modifyBlogger(String bloggerId, Blogger blog) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		int result2 = 0;

		Blogger blogger = advisorDao.fetchBloggerByBloggerId(bloggerId, deleteflag, encryptPass);
		if (blog.getFullName() != null) {
			blogger.setFullName(blog.getFullName());
		}
		if (blog.getEmailId() != null) {
			blogger.setEmailId(blog.getEmailId().toLowerCase());
		}
		if (blog.getPhoneNumber() != null) {
			blogger.setPhoneNumber(blog.getPhoneNumber());
		}
		if (blog.getUserName() != null) {
			blogger.setUserName(blog.getUserName());
		}
		if (blog.getDisplayName() != null) {
			blogger.setDisplayName(blog.getDisplayName());
		}
		if (blog.getDob() != null) {
			blogger.setDob(blog.getDob());
		}
		if (blog.getGender() != null) {
			blogger.setGender(blog.getGender());
		}
		if (blog.getPartyStatusId() != 0) {
			blogger.setPartyStatusId(blog.getPartyStatusId());
		}
		if (blog.getImagePath() != null) {
			blogger.setImagePath(blog.getImagePath());
		}

		// advisor.setCreated_by(signedUserId);
		blogger.setUpdated_by(signedUserId);
		int result = advisorDao.update(bloggerId, blogger, encryptPass);

		int result1 = advisorDao.updatePersonalInfoInParty(blogger.getEmailId(), blogger.getPhoneNumber(),
				blogger.getUserName(), bloggerId, encryptPass, signedUserId);

		long partyId = advisorDao.fetchPartyIdByRoleBasedId(bloggerId, deleteflag);
		List<ArticlePost> articlePostList = advisorDao.fetchArticlePostList(partyId, deleteflag, encryptPass);

		if (articlePostList != null) {
			result2 = advisorDao.updateArticlePostInfoByPartyId(blogger.getImagePath(), partyId, deleteflag);
		}

		if (result != 0 && result1 != 0 && result2 != 0) {
			return result;
		} else {
			return 0;
		}
	}

	@Transactional
	public int removeBlogger(String bloggerId) {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		int result = advisorDao.removeBlogger(bloggerId, deleteflag, signedUserId);
		return result;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByBrand(String brand) {
		long brandId = Long.parseLong(brand);
		String deleteflag = advTableFields.getDelete_flag_N();

		return advisorDao.fetchTotalExploreAdvisorByBrand(brandId, deleteflag);
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByBrandAndCity(String brand, String city) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return advisorDao.fetchExploreAdvisorByBrandAndCity(brand, city, deleteflag, encryptPass);
	}

	@Transactional
	public int changeToCorporate(String advId, long roleId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String deleteflag_Y = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		String advType = advTableFields.getAdvType_Corporate();
		String nonIndividual = advTableFields.getRoleName_nonIndividual();

		String signedUserId = getSignedInUser();
		int resultEdu = 0;
		int resultExp = 0;
		int result = 0;
		int resultPublic = 0;
		int resultPublic1 = 0;

		List<Education> educationList = advisorDao.fetchEducationByadvId(advId, deleteflag);
		if (educationList.size() != 0) {
			for (Education education : educationList) {
				resultEdu = advisorDao.removeAdvEducation(education.getEduId(), advId, deleteflag_Y, signedUserId);
			}
		}
		List<Experience> experienceList = advisorDao.fetchExperienceByadvId(advId, deleteflag);
		if (experienceList.size() != 0) {
			for (Experience experience : experienceList) {
				resultExp = advisorDao.removeAdvExperience(experience.getExpId(), advId, deleteflag_Y, signedUserId);
			}
		}
		Advisor pub_advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
		if (pub_advisor != null) {

			resultPublic1 = advisorDao.removePublicAdvisorChild(advId);
			resultPublic = advisorDao.removePublicAdvisor(advId);

		}
		int roleIdCorp = authDao.fetchRoleIdByName(nonIndividual);
		Party party = advisorDao.fetchPartyByRoleBasedId(advId, deleteflag, encryptPass);
		if (roleId == 1) {
			result = authDao.changeToCorporateRoleId(roleIdCorp, party.getPartyId(), signedUserId);
		}
		int advType_corp = advisorDao.fetchTypeIdByAdvtype(advType);
		// Advisor advisor = advisorDao.fetchAdvisorByAdvId(advId, deleteflag,
		// encryptPass);
		result = advisorDao.changeToCorporate(deleteflag, advId, advType_corp);

		return result;
	}

	@Transactional
	public int fetchBrandsTotalComment(String paramId) {
		String delete_flag = advTableFields.getDelete_flag_N();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return advisorDao.fetchBrandsTotalComment(paramId, delete_flag);
	}

	@Transactional
	public List<BrandsComment> fetchBrandsComment(String paramId) {
		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<BrandsComment> brandCommentList = advisorDao.fetchBrandsComment(paramId, delete_flag, encryptPass);
		if (brandCommentList != null) {
			for (BrandsComment brandComment : brandCommentList) {
				Party party = advisorDao.fetchParty(brandComment.getPartyId(), encryptPass);
				if (party != null && party.getRoleBasedId().startsWith("ADV")) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
					brandComment.setUserName(advisor.getUserName());
				}
				CommentVote commentVote = advisorDao.fetchCommentsVote(brandComment.getCommentId());
				if (commentVote != null) {
					brandComment.setLikes(commentVote.getUpCount());
				}
			}
		}
		return brandCommentList;
	}

	@Transactional
	public int addBlogger(Blogger blog, long roleId, long blgAdminId) {
		String password = encrypt(blog.getPassword());
		String encryptPass = advTableFields.getEncryption_password();
		// String desc = "active";
		long partyStatusId = advisorDao.fetchPartyStatusIdByDesc(advTableFields.getPartystatus_desc());
		blog.setPassword(password);
		blog.setPartyStatusId(partyStatusId);
		blog.setCreated_by(blog.getBloggerId());
		blog.setUpdated_by(blog.getBloggerId());
		int result = advisorDao.addBlogger(blog, advTableFields.getDelete_flag_N(), encryptPass);
		int result1 = 0;
		if (result != 0) {
			result1 = advisorDao.addPartyBlogger(blog, advTableFields.getDelete_flag_N(), encryptPass);
		}
		int result2 = 0;
		if (result1 != 0) {
			result2 = advisorDao.addSigninVerification(blog.getEmailId(), encryptPass);
		}
		long partyId = advisorDao.fetchPartyIdByRoleBasedId(blog.getBloggerId(), advTableFields.getDelete_flag_N());
		if (result1 != 0) {
			int isPrimaryRole = advTableFields.getIs_primary_role_true();
			// String signedUserId = getSignedInUser();

			int result3 = authDao.addUser_role(partyId, roleId, blog.getCreated_by(), isPrimaryRole);
		}
		return result2;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorListByProduct(String sortByCity, String productId, String serviceId,
			String brandId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisorList = advisorDao.fetchExploreAdvisorListByProduct(sortByCity, productId, serviceId,
				brandId, deleteflag, encryptPass);
		return advisorList;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProduct(String sortByCity, String productId, String serviceId,
			String brandId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProduct(sortByCity, productId, serviceId, brandId,
				deleteflag);
		return advisors;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorListByProdId(String productId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisorList = advisorDao.fetchExploreAdvisorListByProdId(productId, deleteflag, encryptPass);
		return advisorList;
	}

	@Transactional
	public int fetchTotalExploreAdvisorListByProdId(String productId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisorList = advisorDao.fetchTotalExploreAdvisorListByProdId(productId, deleteflag);
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdDetails(int pageNum, int records, String product, String service,
			String brand) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, productId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<AdvBrandInfo> brandInfo = advisorDao.fetchAdvBrandInfoByProdServBrand(pageable, productId, serviceId,
				brandId, deleteflag);
		if (brandInfo.size() != 0) {
			for (AdvBrandInfo brands : brandInfo) {
				advisor = advisorDao.fetchPublicAdvisorByAdvId(brands.getAdvId(), deleteflag, encryptPass);
				advisorList.add(advisor);
			}
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByCityDetails(int pageNum, int records, String state, String city,
			String pincode) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByCityList(pageable, state, city, pincode, deleteflag,
				encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}

		// if (advisors == null) {
		// return null;
		// }
		// // To fetch lookup table brand value
		// for (Advisor adv : advisors) {
		// long partyId = advisorDao.fetchPartyIdByRoleBasedId(adv.getAdvId(),
		// deleteflag);
		// adv.setPartyId(partyId);
		// List<AdvBrandRank> advBrandRankList = new ArrayList<>();
		// if (adv.getAdvBrandRank() != null && adv.getAdvBrandRank().size() != 0) {
		// for (AdvBrandRank advBrandRank1 : adv.getAdvBrandRank()) {
		// String brand;
		// brand = advisorDao.fetchBrandByBrandId(advBrandRank1.getBrandId());
		// advBrandRank1.setBrand(brand);
		// advBrandRankList.add(advBrandRank1);
		// }
		// adv.setAdvBrandRank(advBrandRankList);
		// }
		// }
		return advisorList;
	}

	@Override
	public List<Advisor> fetchExploreAdvisorByCityDetailsWithoutPin(int pageNum, int records, String state,
			String city) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByCityListWithoutPin(pageable, state, city, deleteflag,
				encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdAndState(int pageNum, int records, String product, String state) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdAndState(pageable, prodId, state, deleteflag,
				encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;

	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdDetailsWithoutBrand(int pageNum, int records, String product,
			String service) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<AdvProduct> prodInfo = advisorDao.fetchAdvBrandInfoByProdServ(pageable, productId, serviceId, deleteflag);

		if (prodInfo.size() != 0) {
			for (AdvProduct prod : prodInfo) {
				advisor = advisorDao.fetchPublicAdvisorByAdvId(prod.getAdvId(), deleteflag, encryptPass);
				advisorList.add(advisor);
			}
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdServBrandAndState(int pageNum, int records, String product,
			String service, String brand, String state) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, prodId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, prodId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdServBrandAndState(pageable, prodId, serviceId,
				brandId, state, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdStateAndCity(int pageNum, int records, String product, String state,
			String city) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdStateAndCity(pageable, prodId, state, city,
				deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;

	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdServStateAndCity(int pageNum, int records, String product,
			String service, String state, String city) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, prodId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdServStateAndCity(pageable, prodId, state, serviceId,
				city, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdServBrandStateAndCity(int pageNum, int records, String product,
			String service, String brand, String state, String city) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, prodId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, prodId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdServStateAndCity(pageable, prodId, serviceId,
				brandId, state, city, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByproduct(int pageNum, int records, String product) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		long productId = advisorDao.fetchProductByProductName(product);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByproduct(pageable, productId, deleteflag, encryptPass);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByState(int pageNum, int records, String state) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByState(pageable, state, deleteflag, encryptPass,
				signedUserId);

		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByprodServAndStateDetails(int pageNum, int records, String product,
			String service, String state, String city, String pincode) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, prodId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByprodServAndStateDetails(pageable, prodId, serviceId,
				state, city, pincode, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdDetailsAndStateDetails(int pageNum, int records, String product,
			String service, String brand, String state, String city, String pincode) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, prodId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, prodId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdDetailsAndStateDetails(pageable, prodId, serviceId,
				brandId, state, city, pincode, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdServAndState(int pageNum, int records, String product, String service,
			String state) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, prodId);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdServAndState(pageable, prodId, serviceId, state,
				deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByProdStateCityAndPincode(int pageNum, int records, String product,
			String state, String city, String pincode) {

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		long prodId = advisorDao.fetchProductByProductName(product);
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchExploreAdvisorByProdStateCityAndPincode(pageable, prodId, state, city,
				pincode, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public int fetchCommentsTotalList() {
		String deleteflag = advTableFields.getDelete_flag_N();
		int blogger = advisorDao.fetchCommentsTotalList(deleteflag);
		return blogger;
	}

	@Transactional
	public List<BrandsComment> fetchCommentsList(int pageNum, int records) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<BrandsComment> brandsCommentList = advisorDao.fetchBrandsCommentsList(pageable, deleteflag, encryptPass);
		if (brandsCommentList != null) {
			for (BrandsComment brandComment : brandsCommentList) {
				Party party = advisorDao.fetchParty(brandComment.getPartyId(), encryptPass);
				if (party != null && party.getRoleBasedId().startsWith("ADV")) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), deleteflag, encryptPass);
					brandComment.setUserName(advisor.getUserName());
				}
				CommentVote commentVote = advisorDao.fetchCommentsVote(brandComment.getCommentId());
				if (commentVote != null) {
					brandComment.setLikes(commentVote.getUpCount());
				}
			}

		}
		return brandsCommentList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByDisplayName(int pageNum, int records, String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByDisplayName(pageable, displayName, deleteflag,
				encryptPass, signedUserId);

		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByCityDetailsAndDisplayName(int pageNum, int records, String state,
			String city, String pincode, String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByCityDetailsAndDisplayName(pageable, state, city,
				pincode, displayName, deleteflag, encryptPass, signedUserId);

		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchAllExploreAdvisorList(int pageNum, int records) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<String> advisors = advisorDao.fetchAllExploreAdvisorList(pageable, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (String advId : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(advId, deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByStateCityAndDisplayName(int pageNum, int records, String state,
			String city, String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByProdStateCityAndDisplayName(pageable, state, city,
				displayName, deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public List<Advisor> fetchExploreAdvisorByStateAndDisplayName(int pageNum, int records, String state,
			String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<Advisor> advisors = advisorDao.fetchExploreAdvisorByStateAndDisplayName(pageable, state, displayName,
				deleteflag, encryptPass, signedUserId);
		List<Advisor> advisorList = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		for (Advisor adv : advisors) {
			advisor = advisorDao.fetchPublicAdvisorByAdvId(adv.getAdvId(), deleteflag, encryptPass);
			advisorList.add(advisor);
		}
		return advisorList;
	}

	@Transactional
	public int fetchAllExploreAdvisorTotalList() {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchAllExploreAdvisorTotalList(deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByDisplayName(String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByDisplayName(displayName, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByState(String state) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByState(state, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchExploreAdvisorByproductTotal(String product) {
		String deleteflag = advTableFields.getDelete_flag_N();
		long productId = advisorDao.fetchProductByProductName(product);
		int advisors = advisorDao.fetchExploreAdvisorByproductTotal(productId, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdDetailsWithoutBrand(String product, String service) {
		String deleteflag = advTableFields.getDelete_flag_N();
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdDetailsWithoutBrand(productId, serviceId, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdAndState(String product, String state) {
		String deleteflag = advTableFields.getDelete_flag_N();
		long productId = advisorDao.fetchProductByProductName(product);
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdAndState(productId, state, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByStateAndDisplayName(String state, String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByStateAndDisplayName(state, displayName, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByCityDetailsWithoutPin(String state, String city) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByCityDetailsWithoutPin(state, city, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByStateCityAndDisplayName(String state, String city, String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByStateCityAndDisplayName(state, city, displayName,
				deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByCityDetails(String state, String city, String pincode) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByCityDetails(state, city, pincode, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdDetails(String product, String service, String brand) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdDetails(productId, serviceId, brandId, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdServAndState(String product, String service, String state) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdServAndState(productId, serviceId, state, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdStateAndCity(String product, String state, String city) {
		long productId = advisorDao.fetchProductByProductName(product);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdStateAndCity(productId, state, city, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdStateCityAndPincode(String product, String state, String city,
			String pincode) {
		long productId = advisorDao.fetchProductByProductName(product);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdStateCityAndPincode(productId, state, city, pincode,
				deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdServStateAndCity(String product, String service, String state,
			String city) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdServStateAndCity(productId, serviceId, state, city,
				deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdServBrandAndState(String product, String service, String brand,
			String state) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdServBrandAndState(productId, serviceId, brandId, state,
				deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByCityDetailsAndDisplayName(String state, String city, String pincode,
			String displayName) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByCityDetailsAndDisplayName(state, city, pincode, displayName,
				deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByprodServAndStateDetails(String product, String service, String state,
			String city, String pincode) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByprodServAndStateDetails(productId, serviceId, state, city,
				pincode, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdServBrandStateAndCity(String product, String service, String brand,
			String state, String city) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdServBrandStateAndCity(productId, serviceId, brandId,
				state, city, deleteflag);
		return advisors;
	}

	@Transactional
	public int fetchTotalExploreAdvisorByProdDetailsAndStateDetails(String product, String service, String brand,
			String state, String city, String pincode) {
		long productId = advisorDao.fetchProductByProductName(product);
		long serviceId = advisorDao.fetchServiceByServiceName(service, productId);
		long brandId = advisorDao.fetchBrandByBrandName(brand, productId);
		String deleteflag = advTableFields.getDelete_flag_N();
		int advisors = advisorDao.fetchTotalExploreAdvisorByProdDetailsAndStateDetails(productId, serviceId, brandId,
				state, city, pincode, deleteflag);
		return advisors;
	}

	@Transactional
	public int createCommentVote(long voteType, long commentId) {
		CommentVote commentVote = advisorDao.fetchCommentVoteByCommentId(commentId);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = forumDao.fetchUpVoteId(up);
		int downVote = forumDao.fetchDownVoteId(down);
		if (commentVote == null) {
			CommentVote commentVote1 = new CommentVote();
			if (voteType == upVote) {
				commentVote1.setCommentId(commentId);
				commentVote1.setUpCount(1);
				commentVote1.setDownCount(0);
			}
			if (voteType == downVote) {
				commentVote1.setCommentId(commentId);
				commentVote1.setUpCount(0);
				commentVote1.setDownCount(1);
			}
			int result = advisorDao.firstCommentVote(commentVote1);
			return result;
		} else {
			int result = 0;
			if (voteType == upVote) {
				int upCount = advisorDao.fetchUpCountByCommentId(commentId);
				upCount++;
				result = advisorDao.updateCommentUpVote(upCount, commentId);
			}
			if (voteType == downVote) {
				int downCount = advisorDao.fetchDownCountByCommentId(commentId);
				downCount++;
				result = advisorDao.updateCommentDownVote(downCount, commentId);
			}
			return result;
		}

	}

	@Transactional
	public int saveCommentVote(long voteType, long commentId, long partyId) {
		String signedUserId = getSignedInUser();
		return advisorDao.saveCommentVote(voteType, commentId, partyId, signedUserId);
	}

	@Transactional
	public int decreaseLikeCount(long commentId) {
		int upCount = advisorDao.fetchUpCountByCommentId(commentId);
		int count = upCount - 1;
		int result = advisorDao.updateCommentUpVote(count, commentId);
		return result;
	}

	@Transactional
	public int removeCommentVoteAddress(long commentId, long partyId) {
		int result = advisorDao.removeCommentVoteAddress(commentId, partyId);
		return result;
	}

	@Transactional
	public List<CommentVoteAddress> fetchCommentVoteAddress(long partyId) {
		return advisorDao.fetchCommentVoteAddress(partyId);
	}

	@Transactional
	public int fetchCommentPost(long commentId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return advisorDao.fetchCommentPost(commentId, delete_flag);
	}

}
