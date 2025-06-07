package com.sowisetech.advisor.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sowisetech.admin.model.RoleAuth;

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
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.StateCity;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.investor.model.Investor;

@Service
public interface AdvisorService {

	List<Advisor> fetchAdvisorList(int pageNum, int records);

	Advisor fetchByAdvisorId(String advId);

	int removeAdvisor(String advId, int deactivate);

	int advSignup(Advisor adv, long roleId);

	int modifyAdvisor(String advId, Advisor adv);

	int addAdvProfessionalInfo(String advId, Advisor adv);

	int addAdvProductInfo(String advId, AdvProduct advProduct);

	Award fetchAward(long awardId);

	Experience fetchExperience(long expId);

	Education fetchEducation(long eduId);

	int removeAdvEducation(long eduId, String advId);

	int removeAdvExperience(long expId, String advId);

	Advisor fetchAdvisorByEmailId(String emailId);

	int removeAdvAward(long awardId, String advId);

	String generateId();

	int addAdvPersonalInfo(String advId, Advisor adv);

	boolean checkForPasswordMatch(String roleBasedId, String currentPassword);

	int changeAdvPassword(String roleBasedId, String newPassword);

	AdvProduct fetchAdvProduct(long advProdId);

	int modifyAdvisorProduct(AdvProduct advProduct, String advId);

	String encrypt(String rawPassword);

	String decrypt(String encodedPassword);

	Certificate fetchCertificate(long certificateId);

	int removeAdvCertificate(long certificateId, String advId);

	List<Category> fetchCategoryList();

	List<CategoryType> fetchCategoryTypeList();

	List<ForumCategory> fetchForumCategoryList();

	List<RiskQuestionaire> fetchRiskQuestionaireList();

	List<Product> fetchProductList();

	List<RoleAuth> fetchRoleList();

	List<ForumSubCategory> fetchForumSubCategoryList();

	List<ForumStatus> fetchForumStatusList();

	List<PartyStatus> fetchPartyStatusList();

	List<com.sowisetech.advisor.model.Service> fetchServiceList();

	List<Brand> fetchBrandList();

	List<License> fetchLicenseList();

	List<Remuneration> fetchRemunerationList();

	int addAdvBrandInfo(String advId, List<AdvBrandInfo> advBrandInfoList);

	List<AdvBrandInfo> fetchAdvBrandInfoByAdvIdAndProdId(String advId, long prodId);

	List<Long> fetchPriorityByBrandIdAndAdvId(String advId, long prodId, long brandId);

	AdvBrandRank fetchAdvBrandRank(String advId, long prodId, int rank);

	int addAdvBrandAndRank(long brand, int rank, String advId, long prodId);

	int updateBrandAndRank(long brand, int rank, String advId, long prodId);

	List<AdvProduct> fetchAdvProductByAdvId(String advId);

	int removeAdvProduct(long advProdId, String advId);

	int removeAdvBrandInfo(long prodId, String advId);

	int removeFromBrandRank(String advId, long prodId);

	AdvProduct fetchAdvProductByAdvIdAndAdvProdId(String advId, long advProdId);

	int removeAdvBrandInfoByAdvId(String advId);

	int removeAdvBrandRankByAdvId(String advId);

	List<Product> fetchAllServiceAndBrand();

	List<Award> fetchAwardByadvId(String advId);

	List<Certificate> fetchCertificateByadvId(String advId);

	List<Experience> fetchExperienceByadvId(String advId);

	List<Education> fetchEducationByadvId(String advId);

	int modifyAdvisorAward(long awardId, Award award, String advId);

	int modifyAdvisorCertificate(long certificateId, Certificate certificate, String advId);

	int modifyAdvisorExperience(long expId, Experience experience, String advId);

	int modifyAdvisorEducation(long eduId, Education education, String advId);

	int addAdvAwardInfo(String advId, Award award);

	int addAdvCertificateInfo(String advId, Certificate certificate);

	int addAdvExperienceInfo(String advId, Experience experience);

	int addAdvEducationInfo(String advId, Education education);

	Award fetchAdvAwardByAdvIdAndAwardId(long awardId, String advId);

	Certificate fetchAdvCertificateByAdvIdAndCertificateId(long certificateId, String advId);

	Education fetchAdvEducationByAdvIdAndEduId(long eduId, String advId);

	Experience fetchAdvExperienceByAdvIdAndExpId(long expId, String advId);

	int removeAwardByAdvId(String advId);

	int removeCertificateByAdvId(String advId);

	int removeExperienceByAdvId(String advId);

	int removeEducationByAdvId(String advId);

	List<StateCity> fetchAllStateCityPincode();

	List<AdvBrandRank> fetchAdvBrandRankByAdvId(String advId);

	long fetchPartyIdByRoleBasedId(String roleBasedId);

	List<AdvBrandInfo> fetchAdvBrandInfoByAdvId(String advId);

	List<ArticleStatus> fetchArticleStatusList();

	Advisor checkEmailAvailability(String emailId);

	int addInvestor(Investor inv, long roleId);

	Party fetchPartyByEmailId(String emailId);

	String generateIdInv();

	long fetchAdvRoleIdByName();

	long fetchInvRoleIdByName();

	long fetchNonAdvRoleIdByName();

	Party fetchPartyByRoleBasedId(String roleBasedId);

	int changeInvPassword(String roleBasedId, String newPassword);

	int fetchTypeIdByCorporateAdvtype();

	int fetchTypeIdByIndividualAdvtype();

	List<Advisor> fetchTeamByParentPartyId(long parentPartyId);

	int addKeyPeople(KeyPeople keyPeople);

	List<KeyPeople> fetchKeyPeopleByParentId(long parentPartyId);

	// long fetchTeamMemberByName();

	int addPromotion(String advId, Promotion promo);

	List<Promotion> fetchPromotionByAdvId(String advId);

	int modifyPromotion(long promotionId, Promotion promo, String advId);

	int removePromotion(long promotionId, String advId);

	// Advisor fetchAdvisorByParentPartyId(long parentPartyId);

	Party fetchPartyByPartyId(long parentPartyId);

	String fetchRoleByRoleId(long roleId);

	Investor fetchInvestorByInvId(String roleBasedId);

	int updateAdvisorAccountAsVerified(String advId);

	int updateInvestorAccountAsVerified(String invId);

	Party fetchPartyByPhoneNumber(String phoneNumber);

	Party fetchPartyByPAN(String panNumber);

	Party fetchPartyByUserName(String userName);

	Party fetchPartyForSignIn(String username);

	int teamMemberDeactivate(String advId, int deactivate);

	Advisor fetchAdvisorByUserName(String userName);

	List<Advisor> fetchApprovedAdv(int pageNum, int records);

	int addWorkFlowStatusByAdvId(String advId, int status, String Reason);

	KeyPeople fetchKeyPeopleByKeyPeopleId(long keyPeopleId);

	int modifyKeyPeople(long keyPeopleId, KeyPeople key);

	int removeKeyPeople(long id);

	List<Followers> fetchFollowersByUserId(String userId);

	int addFollowers(String advId, String userId);

	// Followers fetchFollowersByFollowersId(long followersId);

	int blockFollowers(long followersId, String blockedBy);

	List<Integer> fetchFollowersCount(String advId);

	List<Followers> fetchFollowersByAdvId(String advId);

	// List<Advisor> fetchActiveFollowersListByUserId(String userId, long statusId);

	boolean validateOtp(String phoneNumber, String otp);

	int addOtpForPhoneNumber(String phoneNumber, String otp);

	Followers fetchFollowersByUserIdWithAdvId(String advId, String userId);

	Followers fetchBlockedFollowersByUserIdWithAdvId(String advId, String userId);

	GeneratedOtp fetchGeneratedOtp(String phoneNumber, String otp);

	List<FollowerStatus> fetchFollowerStatusList();

	List<WorkFlowStatus> fetchWorkFlowStatusList();

	int fetchWorkFlowStatusIdByDesc(String workFlow_Default);

	List<Advisor> fetchExploreAdvisorList(int pageNum, int records, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName);

	List<ServicePlan> fetchExploreProductList(String productName, String serviceName, String brandName,
			String servicePlanName);

	List<Advisor> fetchExploreAdvisorByProduct(int pageNum, int records, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String product, String serviceId, String brandId);

	int fetchRoleIdByName(String roleName_admin);

	List<Advisor> fetchSearchAdvisorList(int pageNum, int records, String panNumber, String emailId, String phoneNumber,
			String userName, String workFlowStatusId, String advType);

	List<UserType> fetchUserTypeList();

	List<AdvisorType> fetchAdvisorTypeList();

	int updateFollowers(long followersId, String userId);

	int approveFollowers(long followersId, String advId);

	List<ChatUser> fetchChatUserListByUserId(String userId);

	ChatUser fetchBlockedChatUsersByUserIdWithAdvId(String advId, String userId);

	int addChatUser(String advId, String userId);

	int updateChatUser(long chatUserId, String userId);

	ChatUser fetchChatUserByUserIdWithAdvId(String advId, String userId);

	int blockChat(long chatUserId, String blockedBy);

	List<Followers> fetchReFollowersByUserId(String userId);

	int fetchAdvisorTotalList();

	int fetchTotalApprovedAdv();

	int fetchTotalExploreAdvisorList(String sortByState, String sortByCity, String sortByPincode,
			String sortByDisplayName);

	int fetchTotalExploreAdvisorByProduct(List<String> stateCityPincodeList, String sortByDisplayName, String productId,
			String serviceId, String brandId);

	int fetchTotalSearchAdvisorList(String panNumber, String emailId, String phoneNumber, String userName,
			String workFlowStatusId, String advType);

	String fetchProductNameByProdId(int prodId);

	String fetchServiceNameByProdIdAndServiceId(int prodId, int serviceId);

	String fetchBrandNameByProdIdAndBrandId(int prodId, int brandId);

	ServicePlan fetchServicePlan(int prodId, int serviceId, int brandId, String planName);

	int addServicePlan(int prodId, int serviceId, int brandId, String planName, String url);

	int updateServicePlan(int prodId, int serviceId, int brandId, String planName, String url);

	List<String> fetchPincodeListByPincode(String sortByPincode);

	List<String> fetchPincodeByState(String sortByState);

	List<String> fetchPincodeByStateAndCity(String sortByState, String sortByCity);

	List<Advisor> fetchExploreAdvisorListByProduct(int pageNum, int records, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId, String brandId);

	List<Advisor> fetchExploreAdvisorListByProductWithoutBrand(int pageNum, int records,
			List<String> stateCityPincodeList, String sortByDisplayName, String productId, String serviceId);

	List<ServicePlan> fetchExploreProductListById(String productId, String serviceId, String brandId,
			String servicePlanName);

	int fetchTotalExploreAdvisorByProductWithoutBrandId(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId, String serviceId);

	List<CityList> searchStateCityPincodeByCity(String city);

	int saveChatMessage(List<ChatMessage> chatMessageList);

	int checkAdvisorIsPresent(String advId);

	int checkAdvProductIsPresent(String advId, long advProdId);

	int checkAdvBrandRankIsPresent(String advId, long prodId, int rank);

	int checkKeyPeopleIsPresent(long keyPeopleId);

	int checkPartyIsPresent(long partyId);

	List<Advisor> fetchExploreAdvisorDESCListOrder(int pageNum, int records, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName);

	int modifyAward(String advId, List<Award> awards);

	int modifyCertificate(String advId, List<Certificate> certificateList);

	int modifyExperience(String advId, List<Experience> experienceList);

	int modifyEducation(String advId, List<Education> educationList);

	int addAndModifyPromotion(String advId, List<Promotion> promotionList);

	int addAdvProductInfoList(String advId, List<AdvProduct> advProductList);

	int addBrandRankIntoTable(HashMap<Long, Integer> sortedBrandAndRank, String advId, long prodId);

	int addAndModifyProductInfo(String advId, List<AdvProduct> advProductList);

	Advisor fetchAdvisorByUserNameWithOutToken(String userName);

	Party fetchPartyByPhoneNumberAndDeleteFlag(String phoneNumber);

	ChatUser fetchActiveChatUser(String advId, String userId);

	ChatUser fetchChatUser(String advId, String userId);

	int approveChat(long chatUserId, String advId);

	List<ChatUser> fetchChatUserListByAdvId(String advId);

	int checkBlockedChatUserIsPresent(String userId);

	Advisor fetchByPublicAdvisorID(String advId);

	List<Followers> fetchFollowersListByUserId(String userId);

	int checkFollowersIsPresent(String userId, String advId);

	int checkReFollowersIsPresent(String userId, String advId);

	int checkChatUserIsPresent(String userId, String advId);

	int checkInActiveChatUserIsPresent(String userId, String advId);

	long fetchActiveFollowersStatus();

	long fetchReFollowersStatus();

	long fetchBlockedFollowersStatus();

	long fetchInActiveFollowersStatus();

	List<Integer> fetchChatUserCount(String advId);

	int unFollowByUserId(long followersId, String userId);

	Followers fetchUnFollowersByUserIdWithAdvId(String advId, String userId);

	long fetchUnFollowersStatus();

	int updateUnFollowers(long followersId, String userId);

	int fetchFollowersCountByUserId(String advId);

	int fetchSharedPlanCountPartyId(long partyId);

	int fetchPlannedUserCountPartyId(long partyId);

	List<Advisor> fetchExploreAdvisorListByProdId(int pageNum, int records, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId);

	int fetchTotalExploreAdvisorListByProdId(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId);

	List<Advisor> fetchPublishTeamByParentPartyId(long parentPartyId);

	int updateMobileAsVerified(Party party);

	int fetchTotalSearchAdvisorListWithEmptyValues(String pan_lc, String email_lc, String phone_lc, String userName_lc,
			String workFlowStatusId);

	List<Advisor> fetchSearchAdvisorListWithEmptyValues(int pageNum, int records, String pan_lc, String email_lc,
			String phone_lc, String userName_lc, String workFlowStatusId);

	Advisor fetchAdvisorGstByAdvId(String advId);

	// List<AdvBrandInfo> fetchAdvBrandInfoByProdIdAndServiceId(long prodId,
	// long serviceId, String advId);

	// List<Award> fetchAwardByadvId(long advid);
	//
	// List<Education> fetchEducationByadvId(long advid);
	//
	// List<Experience> fetchExperienceByadvId(long advid);

	LookUp fetchAllLookUp();

	int fetchBrands(long brandId);

	int createBrandsComment(BrandsComment comment);

	int removeBrandsComment(long commentId);

	// int fetchTotalBrandCommentByParentId(long brandId, long parentCommentId);

	// List<BrandsComment> fetchBrandCommentByParentId(long brandId, long
	// parentCommentId);

	int modifyComment(BrandsComment brandsComment);

	List<Advisor> fetchExploreAdvisorByBrand(String brand);

	long fetchBloggerIdByName();

	String generateIdBlogger();

	Blogger fetchBloggerByBloggerId(String roleBasedId);

	int fetchBloggerTotalList();

	List<Blogger> fetchBloggerList(int pageNum, int records);

	Blogger fetchByBloggerId(String bloggerId);

	int CheckBloggerIsPresent(String bloggerId);

	int modifyBlogger(String bloggerId, Blogger blog);

	int removeBlogger(String bloggerId);

	int fetchTotalExploreAdvisorByBrand(String brand);

	List<Advisor> fetchExploreAdvisorByBrandAndCity(String brand, String city);

	int changeToCorporate(String advId, long roleId);

	int fetchBrandsTotalComment(String paramId);

	List<BrandsComment> fetchBrandsComment(String paramId);

	List<Advisor> fetchExploreAdvisorListByProduct(String sortByCity, String productId, String serviceId,
			String brandId);

	int fetchTotalExploreAdvisorByProduct(String sortByCity, String productId, String serviceId, String brandId);

	int fetchTotalExploreAdvisorListByProdId(String productId);

	int addBlogger(Blogger blog, long roleId, long blgAdminId);

	List<Advisor> fetchExploreAdvisorByProdDetails(int pageNum, int records, String product, String service,
			String brand);

	List<Advisor> fetchExploreAdvisorByProdDetailsWithoutBrand(int pageNum, int records, String product,
			String service);

	List<Advisor> fetchExploreAdvisorByCityDetails(int pageNum, int records, String state, String city, String pincode);

	List<Advisor> fetchExploreAdvisorByCityDetailsWithoutPin(int pageNum, int records, String state, String city);

	List<Advisor> fetchExploreAdvisorByProdAndState(int pageNum, int records, String product, String state);

	List<Advisor> fetchExploreAdvisorByProdServBrandAndState(int pageNum, int records, String product, String service,
			String brand, String state);

	List<Advisor> fetchExploreAdvisorByProdStateAndCity(int pageNum, int records, String product, String state,
			String city);

	List<Advisor> fetchExploreAdvisorByProdServStateAndCity(int pageNum, int records, String product, String service,
			String state, String city);

	List<Advisor> fetchExploreAdvisorByProdServBrandStateAndCity(int pageNum, int records, String product,
			String service, String brand, String state, String city);

	List<Advisor> fetchExploreAdvisorByproduct(int pageNum, int records, String product);

	List<Advisor> fetchExploreAdvisorByState(int pageNum, int records, String state);

	List<Advisor> fetchExploreAdvisorByprodServAndStateDetails(int pageNum, int records, String product, String service,
			String state, String city, String pincode);

	List<Advisor> fetchExploreAdvisorByProdDetailsAndStateDetails(int pageNum, int records, String product,
			String service, String brand, String state, String city, String pincode);

	List<Advisor> fetchExploreAdvisorByProdServAndState(int pageNum, int records, String product, String service,
			String state);

	int fetchCommentsTotalList();

	List<BrandsComment> fetchCommentsList(int pageNum, int records);

	List<Advisor> fetchExploreAdvisorByProdStateCityAndPincode(int pageNum, int records, String product, String state,
			String city, String pincode);

	List<Advisor> fetchExploreAdvisorByDisplayName(int pageNum, int records, String displayName);

	List<Advisor> fetchExploreAdvisorByCityDetailsAndDisplayName(int pageNum, int records, String state, String city,
			String pincode, String displayName);

	List<Advisor> fetchAllExploreAdvisorList(int pageNum, int records);

	List<Advisor> fetchExploreAdvisorByStateCityAndDisplayName(int pageNum, int records, String state, String city,
			String displayName);

	List<Advisor> fetchExploreAdvisorByStateAndDisplayName(int pageNum, int records, String state, String displayName);

	int fetchAllExploreAdvisorTotalList();

	int fetchTotalExploreAdvisorByDisplayName(String displayName);

	int fetchTotalExploreAdvisorByState(String state);

	int fetchExploreAdvisorByproductTotal(String product);

	int fetchTotalExploreAdvisorByProdDetailsWithoutBrand(String product, String service);

	int fetchTotalExploreAdvisorByProdAndState(String product, String state);

	int fetchTotalExploreAdvisorByStateAndDisplayName(String state, String displayName);

	int fetchTotalExploreAdvisorByCityDetailsWithoutPin(String state, String city);

	int fetchTotalExploreAdvisorByStateCityAndDisplayName(String state, String city, String displayName);

	int fetchTotalExploreAdvisorByCityDetails(String state, String city, String pincode);

	int fetchTotalExploreAdvisorByProdDetails(String product, String service, String brand);

	int fetchTotalExploreAdvisorByProdServAndState(String product, String service, String state);

	int fetchTotalExploreAdvisorByProdStateAndCity(String product, String state, String city);

	int fetchTotalExploreAdvisorByProdStateCityAndPincode(String product, String state, String city, String pincode);

	int fetchTotalExploreAdvisorByProdServStateAndCity(String product, String service, String state, String city);

	int fetchTotalExploreAdvisorByProdServBrandAndState(String product, String service, String brand, String state);

	int fetchTotalExploreAdvisorByCityDetailsAndDisplayName(String state, String city, String pincode,
			String displayName);

	int fetchTotalExploreAdvisorByprodServAndStateDetails(String product, String service, String state, String city,
			String pincode);

	int fetchTotalExploreAdvisorByProdServBrandStateAndCity(String product, String service, String brand, String state,
			String city);

	int fetchTotalExploreAdvisorByProdDetailsAndStateDetails(String product, String service, String brand, String state,
			String city, String pincode);

	int fetchCommentPost(long commentId);

	int createCommentVote(long voteType, long commentId);

	int saveCommentVote(long voteType, long commentId, long partyId);

	int decreaseLikeCount(long commentId);

	int removeCommentVoteAddress(long commentId, long partyId);

	List<CommentVoteAddress> fetchCommentVoteAddress(long partyId);

}
