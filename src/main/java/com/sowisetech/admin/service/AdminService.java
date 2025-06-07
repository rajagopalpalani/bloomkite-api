package com.sowisetech.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.sowisetech.admin.model.Acctype;
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
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.ScreenFieldRights;
import com.sowisetech.admin.model.State;
import com.sowisetech.admin.model.Urgency;
import com.sowisetech.admin.model.UserType;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.model.View;
import com.sowisetech.admin.model.Votetype;
import com.sowisetech.admin.model.Workflowstatus;
import com.sowisetech.admin.request.ServiceRequest;
import com.sowisetech.admin.request.VotetypeRequest;

@Service
public interface AdminService {

	User_role fetchUserRoleByUserRoleId(int user_role_id);

	int addScreenRightsFieldRights(List<ScreenFieldRights> screenFieldRights);

	int modifyScreenRightsFieldRights(List<ScreenFieldRights> screenFieldRights, int userRoleId);

	int addRole(RoleAuth role);

	RoleAuth fetchRoleByRoleId(int id);

	int modifyRole(int id, RoleAuth role);

	int removeRole(int roleId);

	int addUserRole(User_role userRole);

	int modifyUserRole(int id, User_role role);

	int removeUserRole(int roleId);

	User_role fetchUserRoleByUserIdAndRoleId(long user_id, int role_id);

	List<Admin> fetchAdminList();

	Admin fetchAdminByEmailId(String emailId);

	String generateId();

	int addAdmin(Admin admin);

	Admin fetchByAdminId(String adminId);

	int modifyAdmin(String adminId, Admin admin1);

	int removeAdmin(String adminId);

	Party fetchPartyByEmailId(String emailId);

	int addProducts(Product Product);

	int removeProducts(int prodId);

	int modifyProduct(Product product);

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

	int saveAddAcctype(Acctype acctype);

	int RemoveAcctype(int AccTypeId);

	int modifyAcctype(Acctype acctype);

	int advisorTypes(Advtypes advtypes);

	int removeAdvisorTypes(int id);

	int modifyAdvisorTypes(Advtypes advtypes);

	int followerStatus(AdmFollower admFollower);

	int modifyFollowerStatus(AdmFollower admFollower);

	int removeFollowerStatus(int followerStatusId);

	int priorityItem(AdmPriority admPriority);

	int modifyPriorityItem(AdmPriority admPriority);

	int checkPartyIsPresent(String emailId);

	int checkAdminIsPresent(String adminId);

	int checkUserRoleIsPresent(int user_role_id);

	int checkRoleIsPresent(int id);

	int checkUserRoleByUserIdAndRoleId(long user_id, int role_id);

	int addState(State state);

	int removeState(int stateId);

	int modifyState(State state);

	int addRemuneration(Remuneration remuneration);

	int removeRemuneration(int remId);

	int modifyRemuneration(Remuneration remuneration);

	int removePriorityItem(int priorityItemId);

	int addWorkflowstatus(Workflowstatus workflowstatus);

	int modifyWorkflowstatus(Workflowstatus workflowstatus);

	int removeWorkFlowStatus(int workflowId);

	int addService(com.sowisetech.admin.model.Service service);

	int modifyService(com.sowisetech.admin.model.Service service);

	int removeService(int serviceId);

	int riskPortfolio(AdmRiskPortfolio admRiskPortfolio);

	int modifyRiskPortfolio(AdmRiskPortfolio admRiskPortfolio);

	int removeRiskPortfolio(int riskPortfolioId);

	int license(License license);

	int modifyLicense(License license);

	int removeLicense(int licId);

	int urgency(Urgency urgency);

	int modifyUrgency(Urgency urgency);

	int removeUrgency(int urgencyId);

	int account(Account account);

	int modifyAccount(Account account);

	int removeAccount(int accountEntryId);

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

	int addView(View view);

	Party fetchPartyByRoleBasedId(String viewerId);

	int fetchCount(String ownerId);

	List<View> fetchAllViewCount();

	// User_role fetchUserRoleByUserRoleIdAndRoleId(int user_role_id, int role_id);

	// User_role fetchUserRoleByUserRoleIdAndRoleId(int user_role_id, int role_id);

}
