package com.sowisetech.advisor.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
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
import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.CommentVote;
import com.sowisetech.advisor.model.CommentVoteAddress;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.FollowerStatus;
import com.sowisetech.advisor.model.Followers;
import com.sowisetech.advisor.model.ForumCategory;
import com.sowisetech.advisor.model.ForumStatus;
import com.sowisetech.advisor.model.ForumSubCategory;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.KeyPeople;
import com.sowisetech.advisor.model.License;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.model.PartyStatus;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.Service;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.investor.model.Investor;

public interface AdvisorDao {

	int advSignup(Advisor advisor, String encryptPass, int promoCount);

	int update(String advId, Advisor advisor, String encryptPass);

	int addAdvProductInfo(String advId, AdvProduct advProduct, String deleteflag);

	Award fetchAward(long awardId, String deleteflag);

	Experience fetchExperience(long expId, String deleteflag);

	Education fetchEducation(long eduId, String deleteflag);

	int removeAdvAward(long awardId, String advId, String deleteflag, String signedUserId);

	int removeAdvEducation(long eduId, String advId, String deleteflag, String signedUserId);

	int removeAdvExperience(long expId, String advId, String deleteflag, String signedUserId);

	Advisor fetchAdvisorByEmailId(String emailId, String deleteflag, String encryptPass);

	int addAdvPersonalInfo(String advId, Advisor adv, String encryptPass);

	// boolean checkForPasswordMatch(String advId, String currentPassword, String
	// deleteflag);

	int changeAdvPassword(String roleBasedId, String newPassword);

	AdvProduct fetchAdvProduct(long advProdId, String deleteflag);

	int modifyAdvisorProduct(AdvProduct advProduct, String advId);

	String fetchEncryptionSecretKey();

	Certificate fetchCertificate(long certificateId, String deleteflag);

	int removeAdvCertificate(long certificateId, String advId, String deleteflag, String signedUserId);

	List<Category> fetchCategoryList();

	List<CategoryType> fetchCategoryTypeList();

	List<ForumCategory> fetchForumCategoryList();

	List<RiskQuestionaire> fetchAllRiskQuestionaire();

	List<Product> fetchProductList();

	List<ForumSubCategory> fetchForumSubCategoryList();

	List<ForumStatus> fetchForumStatusList();

	List<PartyStatus> fetchPartyStatusList();

	List<Service> fetchServiceList();

	List<Brand> fetchBrandList();

	List<License> fetchLicenseList();

	List<Remuneration> fetchRemunerationList();

	int addAdvBrandInfo(String advId, AdvBrandInfo advBrandInfo, String deleteflag);

	List<AdvBrandInfo> fetchAdvBrandInfoByAdvIdAndProdId(String advId, long prodId, String deleteflag);

	List<Long> fetchPriorityByBrandIdAndAdvId(String advId, long prodId, long brandId, String deleteflag);

	AdvBrandRank fetchAdvBrandRank(String advId, long prodId, int rank, String deleteflag);

	int addAdvBrandAndRank(long brand, int rank, String advId, long prodId, String deleteflag, String signedUserId);

	int updateBrandAndRank(long brand, int rank, String advId, long prodId, String deleteflag, String signedUserId);

	List<AdvProduct> fetchAdvProductByAdvId(String advId, String deleteflag);

	int removeAdvProduct(long advProdId, String advId, String deleteflag, String signedUserId);

	int removeAdvBrandInfo(long prodId, String advId, String deleteflag, String signedUserId);

	int removeFromBrandRank(String advId, long prodId);

	AdvProduct fetchAdvProductByAdvIdAndAdvProdId(String advId, long advProdId, String deleteflag);

	int removeAdvBrandInfoByAdvId(String advId);

	int removeAdvBrandRankByAdvId(String advId);

	List<Product> fetchAllServiceAndBrand();

	List<Award> fetchAwardByadvId(String advId, String deleteflag);

	List<Certificate> fetchCertificateByadvId(String advId, String deleteflag);

	List<Experience> fetchExperienceByadvId(String advId, String deleteflag);

	List<Education> fetchEducationByadvId(String advId, String deleteflag);

	int modifyAdvisorAward(long awardId, Award award, String advId);

	int modifyAdvisorCertificate(long certificateId, Certificate certificate, String advId);

	int modifyAdvisorExperience(long expId, Experience experience, String advId);

	int modifyAdvisorEducation(long eduId, Education education, String advId);

	int addAdvAwardInfo(String advId, Award award, String deleteflag);

	int addAdvCertificateInfo(String advId, Certificate certificate, String deleteflag);

	int addAdvExperienceInfo(String advId, Experience experience, String deleteflag);

	int addAdvEducationInfo(String advId, Education education, String deleteflag);

	Award fetchAdvAwardByAdvIdAndAwardId(long awardId, String advId, String deleteflag);

	Certificate fetchAdvCertificateByAdvIdAndCertificateId(long certificateId, String advId, String deleteflag);

	Education fetchAdvEducationByAdvIdAndEduId(long eduId, String advId, String deleteflag);

	Experience fetchAdvExperienceByAdvIdAndExpId(long expId, String advId, String deleteflag);

	int removeAwardByAdvId(String advId, String deleteflag, String signedUserId);

	int removeCertificateByAdvId(String advId, String deleteflag, String signedUserId);

	int removeExperienceByAdvId(String advId, String deleteflag, String signedUserId);

	int removeEducationByAdvId(String advId, String deleteflag, String signedUserId);

	List<State> fetchAllStateCityPincode();
	// List<StateCity> fetchAllStateCityPincode();

	List<AdvBrandRank> fetchAdvBrandRankByAdvId(String advId, String deleteflag);

	long fetchPartyIdByRoleBasedId(String roleBasedId, String delete_flag);

	int fetchTypeIdByAdvtype(String advType);

	// long fetchRoleIdByName(String name);

	int addParty(Advisor advisor, String encryptPass);

	int updateAdvisorTimeStamp(String advId);

	Advisor fetchAdvisorByAdvId(String advId, String deleteflag, String encryptPass);

	List<AdvBrandInfo> fetchAdvBrandInfoByAdvId(String advId, String deleteflag);

	String fetchBrandByBrandId(long brandId);

	List<Advisor> fetchAdvisorList(Pageable pageable, String deleteflag, String encryptPass);

	String fetchAdvisorSmartId();

	int addAdvSmartId(String newId);

	int removeAdvisor(String advId, String deleteflag, String signedUserId, int deactivate);

	List<ArticleStatus> fetchArticleStatusList();

	Advisor checkEmailAvailability(String emailId, String encryptPass);

	long fetchPartyStatusIdByDesc(String desc);

	int addPartyInv(Investor investor, String deleteflag, String encryptPass);

	int addInv(Investor investor, String deleteflag, String encryptPass);

	Party fetchPartyByEmailId(String emailId, String encryptPass);

	String fetchInvestorSmartId();

	int addInvSmartId(String newId);

	Party fetchPartyByRoleBasedId(String roleBasedId, String delete_flag, String encryptPass);

	int changeInvPassword(String roleBasedId, String password);

	int changePartyPassword(String roleBasedId, String password);

	List<Advisor> fetchTeamByParentPartyId(long parentPartyId, String deleteflag, String encryptPass);

	int addKeyPeople(KeyPeople keyPeople, String deleteflag, String encryptPass);

	List<KeyPeople> fetchKeyPeopleByParentId(long parentPartyId, String deleteflag, String encryptPass);

	// long fetchTeamMemberByName(String name);

	int addPromotion(String advId, Promotion promo, String deleteflag);

	List<Promotion> fetchPromotionByAdvId(String advId, String deleteflag);

	Promotion fetchPromotionByAdvIdAndPromotionId(long promotionId, String advId, String deleteflag);

	int modifyPromotion(long promotionId, Promotion promotion, String advId, String deleteflag);

	int removePromotion(long promotionId, String delete_flag_Y, String signedUserId);

	// Advisor fetchAdvisorByParentPartyId(long parentPartyId);

	Party fetchPartyByPartyId(long parentPartyId, String deleteflag, String encryptPass);

	int addSigninVerification(String emailId, String encryptPass);

	Investor fetchInvestorByInvId(String roleBasedId, String deleteflag, String encryptPass);

	int updateAdvisorAccountAsVerified(String advId, int accountVerified);

	int updateInvestorAccountAsVerified(String invId, int accountVerified);

	Party fetchPartyByPhoneNumber(String phoneNumber, String encryptPass);

	Party fetchPartyByPAN(String panNumber, String encryptPass);

	Party fetchPartyByUserName(String userName, String encryptPass);

	Party fetchPartyForSignIn(String username, String deleteflag, String encryptPass);

	int teamMemberDeactive(String id, String delete_flag_Y, String signedUserId, int deactivate);

	Advisor fetchAdvisorByUserName(String userName, String deleteflag, String encryptPass);

	int addWorkFlowStatusApprovedByAdvId(String advId, int status_approved, String signedUserId, String reason);

	int addWorkFlowStatusRevokedByAdvId(String advId, int status_revoke, String signedUserId, String reason);

	int addWorkFlowStatusByAdvId(String advId, int status, String signedUserId, String reason);

	int fetchWorkFlowStatusIdByDesc(String workFlow_Default);

	int addPublicAdvisor(Advisor adv, String encryptPass);

	int addAdvPublicAwardInfo(String advId, Award award, String deleteflag);

	int addAdvPublicCertificateInfo(String advId, Certificate certificate, String deleteflag);

	int addAdvPublicEducationInfo(String advId, Education education, String deleteflag);

	int addAdvPublicExperienceInfo(String advId, Experience experience, String deleteflag);

	int addPublicAdvProductInfo(String advId, AdvProduct advProduct, String deleteflag);

	int addPublicAdvBrandInfo(String advId, AdvBrandInfo advBrandInfo, String deleteflag);

	int addPublicAdvBrandAndRank(long brandId, long ranking, String advId, long prodId, String deleteflag,
			String signedUserId);

	int addPublicPromotion(String advId, Promotion promotion, String deleteflag);

	int removePublicAdvisorChild(String advId);

	int addPublicAdvPersonalInfo(String advId, Advisor adv, String encryptPass);

	int removePublicAdvisor(String advId);

	List<Advisor> fetchAllPublicAdvisor(Pageable pageable, String deleteflag, String encryptPass);

	int removePublicAdvisorDeleteflag(String advId, String deleteflag, String signedUserId);

	KeyPeople fetchKeyPeopleByKeyPeopleId(long keyPeopleId, String deleteflag, String encryptPass);

	int modifyKeyPeople(long keyPeopleId, KeyPeople keyPeople, String encryptPass);

	int removeKeyPeople(long id, String deleteflag, String signedUserId);

	List<Followers> fetchFollowersByUserId(String userId, long statusId);

	int addFollowers(Followers followers);

	Followers fetchFollowersByFollowersId(long followersId, long statusId);

	List<Integer> fetchFollowersCount(String advId, long statusId);

	List<String> fetchFollowersId(String advId, long statusId, long advUserType);

	// List<Advisor> fetchAdvisorsByAdvIds(List<String> advIds, String deleteflag,
	// String encryptPass);

	// List<Investor> fetchInvestorsByInvIds(List<String> invIds, String deleteflag,
	// String encryptPass);

	long fetchFollowerStatusIdByDesc(String status);

	long fetchUserTypeIdByDesc(String user_type_advisor);

	List<String> fetchActiveFollowersListByUserId(String userId, long statusId);

	int addOtpForPhoneNumber(String phoneNumber, String otp);

	String fetchOtpByPhoneNumber(String phoneNumber);

	Followers fetchFollowersByUserIdWithAdvId(String advId, String userId);

	GeneratedOtp fetchGeneratedOtp(String phoneNumber, String otp);

	List<FollowerStatus> fetchFollowerStatusList();

	List<WorkFlowStatus> fetchWorkFlowStatusList();

	List<Advisor> fetchExploreAdvisorList(Pageable pageable, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String deleteflag, String encryptPass, String signedUserId);

	List<Product> fetchExploreProductList(String productName, String serviceName, String brandName);

	List<ServicePlan> fetchServicePlanByServiceId(long serviceId);

	List<Advisor> fetchExploreAdvisorByProduct(Pageable pageable, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String productId, String serviceId, String brandId,
			String deleteflag, String encryptPass);

	int updatePersonalInfoInParty(String emailId, String phoneNumber, String panNumber, String userName, String advId,
			String encryptPass, String signedUserId);

	List<Advisor> fetchSearchAdvisorList(Pageable pageable, String panNumber, String emailId, String phoneNumber,
			String userName, String deleteflag, String workFlowStatusId, String encryptPass, String advType);

	List<UserType> fetchUserTypeList();

	List<AdvisorType> fetchAdvisorTypeList();

	int updateFollowers(long followersId, long statusId, String blockedBy, String signedUserId);

	List<ChatUser> fetchChatUserListByUserId(String userId);

	int addChatUser(ChatUser chatUser);

	ChatUser fetchChatUserByUserIdWithAdvId(String advId, String userId, long statusId);

	int modifyChat(long chatUserId, long statusId, String blockedBy, String signedUserId);

	Followers fetchFollowersByUserIdWithAdvId(String advId, String userId, long statusId);

	int updateChatUser(long chatUserId, long statusId, String userId, String signedUserId);

	int fetchAdvisorTotalList(String deleteflag);

	int fetchAllTotalPublicAdvisor(String deleteflag);

	int fetchTotalExploreAdvisorList(String sortByState, String sortByCity, String sortByPincode,
			String sortByDisplayName, String deleteflag, String signedUserId);

	int fetchTotalExploreAdvisorByProduct(List<String> stateCityPincodeList, String sortByDisplayName, String productId,
			String serviceId, String brandId, String deleteflag);

	int fetchTotalSearchAdvisorList(String panNumber, String emailId, String phoneNumber, String userName,
			String deleteflag, String encryptPass, String workFlowStatusId, String advType);

	List<Product> searchProductList(String productId, String serviceId, String servicePlanId);

	ServicePlan fetchServicePlanByServicePlanId(String servicePlanId);

	long fetchProductByProductName(String productName);

	long fetchServiceByServiceName(String serviceName, long productId);

	long fetchBrandByBrandName(String brandName, long productId);

	long fetchServicePlanIdByName(String servicePlanName, long productId, long serviceId, long brandId);

	List<ServicePlan> fetchServicePlanByProdIdAndServiceIdAndBrandIdAndServicePlanId(long productId, long serviceId,
			long brandId, long servicePlanId);

	String fetchProductNameByProdId(int prodId);

	String fetchServiceNameByProdIdAndServiceId(int prodId, int serviceId);

	String fetchBrandNameByProdIdAndBrandId(int prodId, int brandId);

	ServicePlan fetchServicePlan(int prodId, int serviceId, int brandId, String planName);

	int addServicePlan(int prodId, int serviceId, int brandId, String planName, String url, String signedUserId);

	int updateServicePlan(int prodId, int serviceId, int brandId, String planName, String url, String signedUserId);

	List<String> fetchListAdvIdByProdIdServiceIdBrandId(String productId, String serviceId, String brandId,
			String deleteflag);

	String fetchCityByPincode(String sortByPincode);

	List<String> fetchPincodesByCity(String city);

	String fetchDistrictByPincode(String sortByPincode);

	List<String> fetchPincodesByDistrict(String district);

	long fetchStateIdByState(String sortByState);

	List<String> fetchPincodeByState(long stateId);

	List<String> fetchPincodeByCity(String sortByCity);

	String fetchDistrictByCity(String sortByCity);

	long fetchStateIdByPincode(String district);

	List<String> fetchPincodeByStateId(long stateId);

	List<Advisor> fetchExploreAdvisorListByProduct(Pageable pageable, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId, String brandId, String deleteflag,
			String encryptPass);

	List<Advisor> fetchExploreAdvisorListByProductWithoutBrand(Pageable pageable, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId, String deleteflag, String encryptPass);

	int fetchTotalExploreAdvisorByProductWithoutBrandId(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId, String serviceId, String deleteflag);

	List<CityList> searchStateCityPincodeByCity(String cityName);

	int saveChatMessage(ChatMessage chatMessage);

	int checkAdvisorIsPresent(String advId, String deleteflag);

	int checkAdvProductIsPresent(String advId, long advProdId, String deleteflag);

	int checkAdvBrandRankIsPresent(String advId, long prodId, int rank, String deleteflag);

	int checkKeyPeopleIsPresent(long keyPeopleId, String deleteflag);

	int checkPartyIsPresent(long partyId, String deleteflag);

	int checkChatUserIsPresent(String userId, long statusId);

	List<Advisor> fetchExploreAdvisorDESCListOrder(Pageable pageable, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String deleteflag, String encryptPass, String signedUserId);

	Advisor fetchAdvisorByUserNameWithOutToken(String userName, String deleteflag, String encryptPass);

	Party fetchPartyByPhoneNumberAndDeleteFlag(String phoneNumber, String deleteflag, String encryptPass);

	int deleteAdvisorChild(String advId);

	int deleteAdvisor(String advId);

	List<ChatUser> fetchChatUserListByAdvId(String advId);

	ChatUser fetchChatUser(String advId, String userId);

	Advisor fetchPublicAdvisorByAdvId(String advId, String deleteflag, String encryptPass);

	List<Followers> fetchFollowersListByUserId(String userId);

	int checkFollowersIsPresent(String userId, String advId, long statusId);

	int checkChatUserIsPresent(String userId, String advId, long statusId);

	List<Followers> fetchFollowers(String advId);

	long fetchFollowersStatus(String statusId);

	List<Integer> fetchChatUserCount(String advId, long statusId);

	int fetchFollowersCountByUserId(String userId, long statusId);

	int fetchSharedPlanCountPartyId(long partyId, String deleteflag);

	int fetchPlannedUserCountPartyId(long partyId);

	List<Advisor> fetchExploreAdvisorListByProdId(Pageable pageable, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String deleteflag, String encryptPass);

	int fetchTotalExploreAdvisorListByProdId(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId, String deleteflag);

	List<Advisor> fetchPublishTeamByParentPartyId(long parentPartyId, String deleteflag, String encryptPass);

	int updateAdvisorMobileAsVerified(String roleBasedId, int accountVerified);

	int updateInvestorMobileAsVerified(String roleBasedId, int accountVerified);

	int updateUsernameInParty(String advId, Advisor adv, String signedUserId, String encryptPass);

	int fetchTotalSearchAdvisorListWithEmptyValues(String pan_lc, String email_lc, String phone_lc, String userName_lc,
			String deleteflag, String workFlowStatusId, String encryptPass);

	List<Advisor> fetchSearchAdvisorListWithEmptyValues(Pageable pageable, String pan_lc, String email_lc,
			String phone_lc, String userName_lc, String deleteflag, String workFlowStatusId, String encryptPass);

	Advisor fetchAdvisorGstByAdvId(String advId, String deleteflag, String encryptPass);

	int fetchBrands(long brandId);

	Party fetchParty(long partyId, String encryptPass);

	int createBrandsComment(BrandsComment comment, String encryptPass);

	int removeBrandsComment(long commentId, String delete_flag, String signedUserId);

	List<BrandsComment> fetchBrandsCommentByParentId(String paramId, long parentId, String delete_flag,
			String encryptPass);

	int fetchTotalBrandsCommentByParentId(long brandId, long parentCommentId, String delete_flag);

	int modifyComment(BrandsComment brandsComment, String deleteflag);

	List<AdvBrandInfo> fetchExploreAdvisorList(long brandId, String deleteflag);

	String fetchBloggerSmartId();

	int addBloggerSmartId(String newId);

	int addBlogger(Blogger blog, String delete_flag_N, String encryptPass);

	int addPartyBlogger(Blogger blog, String delete_flag_N, String encryptPass);

	Blogger fetchBloggerByBloggerId(String roleBasedId, String deleteflag, String encryptPass);

	int fetchBloggerTotalList(String deleteflag);

	List<Blogger> fetchBlogger(Pageable pageable, String deleteflag, String encryptPass);

	int checkBloggerIsPresent(String bloggerId, String deleteflag);

	int update(String bloggerId, Blogger blogger, String encryptPass);

	int updatePersonalInfoInParty(String emailId, String phoneNumber, String userName, String bloggerId,
			String encryptPass, String signedUserId);

	int removeBlogger(String bloggerId, String deleteflag, String signedUserId);

	int fetchTotalExploreAdvisorByBrand(long brandId, String deleteflag);

	List<Advisor> fetchExploreAdvisorByBrandAndCity(String brand, String city, String deleteflag, String encryptPass);

	int changeToCorporate(String deleteflag, String advId, int advType_corp);

	// Party fetchPartyByRoleId(long roleId, String deleteflag);

	// List<AdvBrandInfo> fetchAdvBrandInfoByProdIdAndServiceId(long prodId,
	// long serviceId, String advId);

	// List<Award> fetchAwardByadvId(long advid);
	//
	// List<Education> fetchEducationByadvId(long advid);
	//
	// List<Experience> fetchExperienceByadvId(long advid);

	List<BrandsComment> fetchBrandsComment(String paramId, String delete_flag, String encryptPass);

	int fetchBrandsTotalComment(String paramId, String delete_flag);

	int fetchTotalExploreAdvisorByProduct(String sortByCity, String productId, String serviceId, String brandId,
			String deleteflag);

	List<Advisor> fetchExploreAdvisorListByProduct(String sortByCity, String productId, String serviceId,
			String brandId, String deleteflag, String encryptPass);

	List<Advisor> fetchExploreAdvisorListByProdId(String productId, String deleteflag, String encryptPass);

	int fetchTotalExploreAdvisorListByProdId(String productId, String deleteflag);

	int fetchAdvisorPromoCount(String advId, String deleteflag);

	int updateArticlePostInfoByPartyId(String imagePath, long partyId, String delete_flag);

	List<ArticlePost> fetchArticlePostList(long partyId, String deleteflag, String encryptPass);

	List<AdvBrandInfo> fetchAdvBrandInfoByProdServBrand(Pageable pageable, long productId, long serviceId, long brandId,
			String deleteflag);

	List<AdvProduct> fetchAdvBrandInfoByProdServ(Pageable pageable, long productId, long serviceId, String deleteflag);

	List<Advisor> fetchExploreAdvisorByCityList(Pageable pageable, String state, String city, String pincode,
			String deleteflag, String encryptPass, String signedUserId);

	List<Advisor> fetchExploreAdvisorByCityListWithoutPin(Pageable pageable, String state, String city,
			String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdAndState(Pageable pageable, long prodId, String state, String deleteflag,
			String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdServBrandAndState(Pageable pageable, long prodId, long serviceId,
			long brandId, String state, String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdStateAndCity(Pageable pageable, long prodId, String state, String city,
			String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdServStateAndCity(Pageable pageable, long prodId, String state, long serviceId,
			String city, String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdServStateAndCity(Pageable pageable, long prodId, long serviceId, long brandId,
			String state, String city, String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByproduct(Pageable pageable, long productId, String deleteflag, String encryptPass);

	List<Advisor> fetchExploreAdvisorByState(Pageable pageable, String state, String deleteflag, String encryptPass,
			String signedUserId);

	List<String> fetchExploreAdvisorByprodServAndStateDetails(Pageable pageable, long prodId, long serviceId,
			String state, String city, String pincode, String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdDetailsAndStateDetails(Pageable pageable, long prodId, long serviceId,
			long brandId, String state, String city, String pincode, String deleteflag, String encryptPass,
			String signedUserId);

	List<String> fetchExploreAdvisorByProdServAndState(Pageable pageable, long prodId, long serviceId, String state,
			String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchExploreAdvisorByProdStateCityAndPincode(Pageable pageable, long prodId, String state, String city,
			String pincode, String deleteflag, String encryptPass, String signedUserId);

	int updateArticleCommentByPartyId(String displayName, String imagePath, long partyId, String signedUserId, String deleteflag,
			String encryptPass);

	List<BrandsComment> fetchBrandsCommentByPartyId(long partyId, String deleteflag, String encryptPass);

	int fetchCommentsTotalList(String deleteflag);

	List<BrandsComment> fetchBrandsCommentsList(Pageable pageable, String deleteflag, String encryptPass);

	CommentVote fetchCommentsVote(long commentId);

	List<Advisor> fetchExploreAdvisorByDisplayName(Pageable pageable, String displayName, String deleteflag,
			String encryptPass, String signedUserId);

	List<Advisor> fetchExploreAdvisorByCityDetailsAndDisplayName(Pageable pageable, String state, String city,
			String pincode, String displayName, String deleteflag, String encryptPass, String signedUserId);

	List<String> fetchAllExploreAdvisorList(Pageable pageable, String deleteflag, String encryptPass,
			String signedUserId);

	List<Advisor> fetchExploreAdvisorByProdStateCityAndDisplayName(Pageable pageable, String state, String city,
			String displayName, String deleteflag, String encryptPass, String signedUserId);

	List<Advisor> fetchExploreAdvisorByStateAndDisplayName(Pageable pageable, String state, String displayName,
			String deleteflag, String encryptPass, String signedUserId);

	int fetchAllExploreAdvisorTotalList(String deleteflag);

	int fetchTotalExploreAdvisorByDisplayName(String displayName, String deleteflag);

	int fetchTotalExploreAdvisorByState(String state, String deleteflag);

	int fetchExploreAdvisorByproductTotal(long product, String deleteflag);

	int fetchTotalExploreAdvisorByProdDetailsWithoutBrand(long productId, long serviceId, String deleteflag);

	int fetchTotalExploreAdvisorByProdAndState(long productId, String state, String deleteflag);

	int fetchTotalExploreAdvisorByStateAndDisplayName(String state, String displayName, String deleteflag);

	int fetchTotalExploreAdvisorByCityDetailsWithoutPin(String state, String city, String deleteflag);

	int fetchTotalExploreAdvisorByStateCityAndDisplayName(String state, String city, String displayName,
			String deleteflag);

	int fetchTotalExploreAdvisorByCityDetails(String state, String city, String pincode, String deleteflag);

	int fetchTotalExploreAdvisorByProdDetails(long productId, long serviceId, long brandId, String deleteflag);

	int fetchTotalExploreAdvisorByProdServAndState(long productId, long serviceId, String state, String deleteflag);

	int fetchTotalExploreAdvisorByProdStateAndCity(long productId, String state, String city, String deleteflag);

	int fetchTotalExploreAdvisorByProdStateCityAndPincode(long productId, String state, String city, String pincode,
			String deleteflag);

	int fetchTotalExploreAdvisorByProdServStateAndCity(long productId, long serviceId, String state, String city,
			String deleteflag);

	int fetchTotalExploreAdvisorByProdServBrandAndState(long productId, long serviceId, long brandId, String state,
			String deleteflag);

	int fetchTotalExploreAdvisorByCityDetailsAndDisplayName(String state, String city, String pincode,
			String displayName, String deleteflag);

	int fetchTotalExploreAdvisorByprodServAndStateDetails(long productId, long serviceId, String state, String city,
			String pincode, String deleteflag);

	int fetchTotalExploreAdvisorByProdServBrandStateAndCity(long productId, long serviceId, long brandId, String state,
			String city, String deleteflag);

	int fetchTotalExploreAdvisorByProdDetailsAndStateDetails(long productId, long serviceId, long brandId, String state,
			String city, String pincode, String deleteflag);

	int fetchCommentPost(long commentId, String delete_flag);

	CommentVote fetchCommentVoteByCommentId(long commentId);

	int firstCommentVote(CommentVote commentVote1);

	int fetchUpCountByCommentId(long commentId);

	int updateCommentUpVote(int upCount, long commentId);

	int fetchDownCountByCommentId(long commentId);

	int updateCommentDownVote(int downCount, long commentId);

	int saveCommentVote(long voteType, long commentId, long partyId, String signedUserId);

	int removeCommentVoteAddress(long commentId, long partyId);

	List<CommentVoteAddress> fetchCommentVoteAddress(long partyId);

	int updateArticlePostNameByPartyId(String displayName,String imagePath, long partyId,  String signedUserId, String deleteflag,
			String encryptPass);

	List<ArticleComment> fetchArticleCommentByParyId(long partyId, String deleteflag, String encryptPass);

	int updateArticlePostCommentByPartyId(String displayName, String imagePath, long partyId, String signedUserId,
			String deleteflag, String encryptPass);

	int updateCalcQuery(long partyId, String displayName, String signedUserId);

}
