package com.sowisetech.admin.dao;

import java.util.List;

import com.sowisetech.admin.model.AdmFollower;
import com.sowisetech.admin.model.AdmPriority;
import com.sowisetech.admin.model.AdmRiskPortfolio;
import com.sowisetech.admin.model.Account;
import com.sowisetech.admin.model.Acctype;
import com.sowisetech.admin.model.Admin;
import com.sowisetech.admin.model.ArticleStatus;
import com.sowisetech.admin.model.Brand;
import com.sowisetech.admin.model.CashFlowItem;
import com.sowisetech.admin.model.CashFlowItemType;
import com.sowisetech.admin.model.City;
import com.sowisetech.admin.model.InsuranceItem;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.License;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Remuneration;
import com.sowisetech.admin.model.Service;
import com.sowisetech.admin.model.State;
import com.sowisetech.admin.model.Urgency;
import com.sowisetech.admin.model.Votetype;
import com.sowisetech.admin.model.UserType;
import com.sowisetech.admin.model.View;
import com.sowisetech.admin.model.Workflowstatus;

public interface AdminDao {

	List<Admin> fetchAdminList(String delete_flag, String encryptPass);

	Admin fetchAdminByEmailId(String emailId, String encryptPass);

	Admin fetchByAdminId(String adminId, String delete_flag, String encryptPass);

	int addAdmin(Admin admin, String encryptPass);

	int addPartyForAdmin(Admin admin, String encryptPass);

	String fetchEncryptionSecretKey();

	int modifyAdmin(String adminId, Admin admin, String encryptPass);

	int removeAdmin(String adminId, String delete_flag_y, String signedUserId);

	int removeParty(String adminId, String delete_flag_y, String signedUserId);

	String fetchAdminSmartId();

	int addAdmSmartId(String newId);

	long fetchPartyIdByRoleBasedId(String roleBasedId, String delete_flag_N);

	Party fetchPartyByEmailId(String emailId, String encryptPass);

	long fetchPartyStatusIdByDesc(String partystatus_desc);

	Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass);

	int advisorTypes(Advtypes advtypes);

	int removeAdvisorTypes(int id);

	int modifyAdvisorTypes(Advtypes advtypes);

	int followerStatus(AdmFollower admFollower);

	int modifyFollowerStatus(AdmFollower admFollower);

	int removeFollowerStatus(int followerStatusId);

	int priorityItem(AdmPriority admPriority);

	int modifyPriorityItem(AdmPriority admPriority);

	int removePriorityItem(int priorityItemId);

	int saveAddAcctype(Acctype acctype);

	int RemoveAcctype(int AccTypeId);

	int modifyAcctype(Acctype acctype);

	int addProducts(Product product);

	int removeProducts(int prodId);

	int modifyProduct(Product product);

	int riskPortfolio(AdmRiskPortfolio admRiskPortfolio);

	int modifyRiskPortfolio(AdmRiskPortfolio admRiskPortfolio);

	int removeRiskPortfolio(int riskPortfolioId);

	int license(License license);

	int modifyLicense(License license);

	int removeLicense(int licId);

	int addArticleStatus(ArticleStatus articleStatus);

	int modifyArticleStatus(ArticleStatus articleStatus);

	int removeArticleStatus(int id);

	int addCashFlowItemType(CashFlowItemType cashFlowItemType);

	int modifyCashFlowItemType(CashFlowItemType cashFlowItemType);

	int removeCashFlowItemType(int cashFlowItemTypeId);

	int addCashFlowItem(CashFlowItem cashFlowItem);

	int modifyCashFlowItem(CashFlowItem cashFlowItem);

	int removeCashFlowItem(int cashFlowItemId);

	int addCity(City city);

	int modifyCity(City city);

	int removeCity(int cityId);

	int addState(State state);

	int removeState(int prodId);

	int modifyState(State state);

	int addRemuneration(Remuneration remuneration);

	int removeRemuneration(int remId);

	int modifyRemuneration(Remuneration remuneration);

	// int addArticleStatus(ArticleStatus articleStatus);

	int checkPartyIsPresent(String emailId, String encryptPass);

	int checkAdminIsPresent(String adminId);

	int modifyUrgency(Urgency urgency);

	int removeUrgency(int urgencyId);

	int account(Account account);

	int urgency(Urgency urgency);

	int modifyAccount(Account account);

	int removeAccount(int accountEntryId);

	int addWorkflowstatus(Workflowstatus workflowstatus);

	int modifyWorkflowstatus(Workflowstatus workflowstatus);

	int removeWorkFlowStatus(int workflowId);

	int addService(Service service);

	int modifyService(Service service);

	int removeService(int serviceId);

	int addVotetype(Votetype votetype);

	int modifyVotetype(Votetype votetype);

	int removeVotetype(int id);

	int addBrand(Brand brand);

	int modifyBrand(Brand brand);

	int removeBrand(int brandId);

	int addInsuranceItem(InsuranceItem insuranceItem);

	int modifyInsuranceItem(InsuranceItem insuranceItem);

	int removeInsuranceItem(int insuranceItemId);

	int addUserType(UserType userType);

	int modifyUserType(UserType userType);

	int removeUserType(int id);

	View fetchView(String ownerId, String viewerId);

	int addView(View view, String delete_flag);

	int updateView(View view, long viewId);

	Party fetchPartyByRoleBasedId(String viewerId, String delete_flag_N);

	int updateLastView(int viewId);

	List<View> fetchViewByOwnerId(String ownerId, String delete_flag_N);

	List<View> fetchAllViewCount(String delete_flag_N);

}
