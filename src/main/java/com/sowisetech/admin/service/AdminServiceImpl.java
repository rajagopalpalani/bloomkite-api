package com.sowisetech.admin.service;

import java.security.Security;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.admin.dao.AdminAuthDao;
import com.sowisetech.admin.dao.AdminDao;
import com.sowisetech.admin.model.Account;
import com.sowisetech.admin.model.Acctype;
import com.sowisetech.admin.model.AdmFollower;
import com.sowisetech.admin.model.AdmPriority;
import com.sowisetech.admin.model.AdmRiskPortfolio;
import com.sowisetech.admin.model.Admin;
import com.sowisetech.admin.model.ArticleStatus;
import com.sowisetech.admin.model.Brand;
import com.sowisetech.admin.model.CashFlowItem;
import com.sowisetech.admin.model.CashFlowItemType;
import com.sowisetech.admin.model.City;
import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.InsuranceItem;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.FieldRights;
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
import com.sowisetech.admin.request.VotetypeRequest;
import com.sowisetech.admin.util.AdmTableFields;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.common.util.AdminSignin;

@Transactional(readOnly = true)
@Service("AdminService")
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminAuthDao adminAuthDao;
	@Autowired
	AdmTableFields adminTableFields;
	@Autowired
	AdvTableFields advTableFields;
	@Autowired
	AdminDao adminDao;
	@Autowired
	AdminSignin adminSignin;

	@Transactional
	public User_role fetchUserRoleByUserRoleId(int user_role_id) {
		return adminAuthDao.fetchUserRoleByUserRoleId(user_role_id);
	}

	@Transactional
	public int addScreenRightsFieldRights(List<ScreenFieldRights> screenFieldRightsList) {
		int result = 0;
		for (ScreenFieldRights screenFieldRights : screenFieldRightsList) {
			String signedUserId = getSignedInUser();
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			screenFieldRights.setCreated_by(signedUserId);
			screenFieldRights.setUpdated_by(signedUserId);
			screenFieldRights.setCreated_date(timestamp);
			screenFieldRights.setUpdated_date(timestamp);
			int role_screen_rights_id = adminAuthDao.addScreenRights(screenFieldRights);
			if (role_screen_rights_id == 0) {
				return result;
			}
			for (FieldRights fieldRights : screenFieldRights.getFieldRights()) {
				fieldRights.setRole_screen_rights_id(role_screen_rights_id);
				fieldRights.setCreated_by(signedUserId);
				fieldRights.setUpdated_by(signedUserId);
				fieldRights.setCreated_date(timestamp);
				fieldRights.setUpdated_date(timestamp);
				result = adminAuthDao.addFieldRights(fieldRights);
				if (result == 0) {
					return result;
				}
			}
		}
		return result;
	}

	@Transactional
	public int modifyScreenRightsFieldRights(List<ScreenFieldRights> screenFieldRightsList, int user_role_id) {
		List<ScreenFieldRights> screenFieldRights = adminAuthDao.fetchScreenRightsByUserRoleId(user_role_id);
		if (screenFieldRights != null && screenFieldRights.size() != 0) {
			for (ScreenFieldRights screenRight : screenFieldRights) {
				// System.out.println(screenRight.getRole_screen_rights_id());
				adminAuthDao.deleteFieldRightsByRoleScreenRightsId(screenRight.getRole_screen_rights_id());
			}
		}
		adminAuthDao.deleteScreenRightsByUserRoleId(user_role_id);
		int result = 0;
		for (ScreenFieldRights screenFieldRight : screenFieldRightsList) {
			String signedUserId = getSignedInUser();
			screenFieldRight.setUpdated_by(signedUserId);
			int role_screen_rights_id = adminAuthDao.addScreenRights(screenFieldRight);
			for (FieldRights fieldRights : screenFieldRight.getFieldRights()) {
				fieldRights.setRole_screen_rights_id(role_screen_rights_id);
				fieldRights.setUpdated_by(signedUserId);
				result = adminAuthDao.addFieldRights(fieldRights);
				if (result == 0) {
					return result;
				}
			}
		}
		return result;
	}

	@Transactional
	public int addRole(RoleAuth role) {
		String signedUserId = getSignedInUser();
		role.setCreated_by(signedUserId);
		role.setUpdated_by(signedUserId);
		return adminAuthDao.addRole(role);
	}

	@Transactional
	public RoleAuth fetchRoleByRoleId(int id) {
		return adminAuthDao.fetchRoleByRoleId(id);
	}

	@Transactional
	public int modifyRole(int id, RoleAuth role) {
		RoleAuth roleAuth = adminAuthDao.fetchRoleByRoleId(id);
		if (role.getName() != null) {
			roleAuth.setName(role.getName());
		}
		String signedUserId = getSignedInUser();
		role.setUpdated_by(signedUserId);
		if (role.getUpdated_by() != null) {
			roleAuth.setUpdated_by(role.getUpdated_by());
		}
		return adminAuthDao.modifyRole(id, roleAuth);
	}

	@Transactional
	public int removeRole(int roleId) {
		return adminAuthDao.removeRole(roleId);
	}

	@Transactional
	public int addUserRole(User_role userRole) {
		String signedUserId = getSignedInUser();
		userRole.setCreated_by(signedUserId);
		userRole.setUpdated_by(signedUserId);
		return adminAuthDao.addUserRole(userRole);
	}

	@Transactional
	public int modifyUserRole(int id, User_role role) {
		User_role userRole = adminAuthDao.fetchUserRoleByUserRoleId(id);
		if (role.getUser_id() != 0) {
			userRole.setUser_id(role.getUser_id());
		}
		if (role.getRole_id() != 0) {
			userRole.setRole_id(role.getRole_id());
		}
		String signedUserId = getSignedInUser();
		role.setUpdated_by(signedUserId);
		if (role.getUpdated_by() != null) {
			userRole.setUpdated_by(role.getUpdated_by());
		}
		return adminAuthDao.modifyUserRole(id, userRole);
	}

	@Transactional
	public int removeUserRole(int roleId) {
		return adminAuthDao.removeUserRole(roleId);
	}

	@Transactional
	public User_role fetchUserRoleByUserIdAndRoleId(long user_id, int role_id) {
		return adminAuthDao.fetchUserRoleByUserIdAndRoleId(user_id, role_id);
	}

	// Admin
	@Transactional
	public List<Admin> fetchAdminList() {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		return adminDao.fetchAdminList(delete_flag, encryptPass);
	}

	@Transactional
	public Admin fetchAdminByEmailId(String emailId) {
		String encryptPass = adminTableFields.getEncryption_password();
		return adminDao.fetchAdminByEmailId(emailId, encryptPass);
	}

	@Transactional
	public Admin fetchByAdminId(String adminId) {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		return adminDao.fetchByAdminId(adminId, delete_flag, encryptPass);
	}

	@Transactional
	public int addAdmin(Admin admin) {
		String encryptPass = adminTableFields.getEncryption_password();
		String role = adminTableFields.getRole_name();
		long roleId = adminAuthDao.fetchRoleIdByName(role);
		long partyStatusId = adminDao.fetchPartyStatusIdByDesc(advTableFields.getPartystatus_desc());
		String password = encrypt(admin.getPassword());
		admin.setPassword(password);
		admin.setPartyStatusId(partyStatusId);
		admin.setDelete_flag(adminTableFields.getDelete_flag());
		// String signedUserId = getSignedInUser();
		String adminId = admin.getAdminId();
		admin.setCreated_by(adminId);
		admin.setUpdated_by(adminId);
		int result = adminDao.addAdmin(admin, encryptPass);
		int result1 = 0;
		if (result != 0) {
			result1 = adminDao.addPartyForAdmin(admin, encryptPass);
		}
		long partyId = adminDao.fetchPartyIdByRoleBasedId(admin.getAdminId(), advTableFields.getDelete_flag_N());
		int result2 = 0;
		if (result1 != 0) {
			int isPrimaryRole = advTableFields.getIs_primary_role_true();
			result2 = adminAuthDao.addUser_role(partyId, roleId, adminId, isPrimaryRole);
			// System.out.println(result2);

		}
		return result2;
	}

	@Transactional
	public String encrypt(String password) {
		Security.addProvider(new BouncyCastleProvider());
		StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
		String key = adminDao.fetchEncryptionSecretKey();
		cryptor.setPassword(key);
		cryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		String encryptedText = cryptor.encrypt(password);
		return encryptedText;
	}

	@Transactional
	public int modifyAdmin(String adminId, Admin admin) {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Admin admin1 = adminDao.fetchByAdminId(adminId, delete_flag, encryptPass);
		if (admin != null && admin.getName() != null) {
			admin1.setName(admin.getName());
		}
		if (admin != null && admin.getEmailId() != null) {
			admin1.setEmailId(admin.getEmailId());
		}
		if (admin != null && admin.getPartyStatusId() != 0) {
			admin1.setPartyStatusId(admin.getPartyStatusId());
		}
		String signedUserId = getSignedInUser();
		admin1.setUpdated_by(signedUserId);
		return adminDao.modifyAdmin(adminId, admin1, encryptPass);
	}

	@Transactional
	public int removeAdmin(String adminId) {
		String signedUserId = getSignedInUser();
		String delete_flag_y = adminTableFields.getDelete_flag_y();
		int result = adminDao.removeAdmin(adminId, delete_flag_y, signedUserId);
		if (result != 0) {
			int result1 = adminDao.removeParty(adminId, delete_flag_y, signedUserId);
			return result1;
		}
		return result;
	}

	@Transactional
	public String generateId() {
		String id = adminDao.fetchAdminSmartId();
		if (id != null) {
			String newId = idIncrement(id);
			adminDao.addAdmSmartId(newId);
			return newId;
		} else {
			String newId = "ADM0000000000";
			adminDao.addAdmSmartId(newId);
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
				newId = "ADM" + middle;
			} else {
				newId = "ADM" + middle + "A";
			}
		} else {
			if (id.charAt(12) != 'Z') {
				char last = id.charAt(12);
				suffix = String.valueOf((char) (last + 1));
				newId = id.substring(0, 12) + suffix;
			} else {
				long num = Long.parseLong(middle);
				middle = String.format("%09d", num + 1);
				newId = "ADM" + middle + "0";
			}
		}
		return newId;
	}

	@Transactional
	public Party fetchPartyByEmailId(String emailId) {
		String encryptPass = advTableFields.getEncryption_password();
		return adminDao.fetchPartyByEmailId(emailId, encryptPass);
	}

	private String getSignedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
			return adminSignin.getAdmin_name();
		} else {
			Party party = adminDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
			return party.getRoleBasedId();
		}
	}
	// @Transactional
	// public User_role fetchUserRoleByUserRoleIdAndRoleId(int user_role_id, int
	// role_id) {
	// return adminDao.fetchUserRoleByUserRoleIdAndRoleId(user_role_id, role_id);
	// }

	@Transactional
	public int checkPartyIsPresent(String emailId) {
		String encryptPass = advTableFields.getEncryption_password();
		return adminDao.checkPartyIsPresent(emailId, encryptPass);
	}

	@Transactional
	public int checkAdminIsPresent(String adminId) {
		return adminDao.checkAdminIsPresent(adminId);
	}

	@Transactional
	public int checkUserRoleIsPresent(int user_role_id) {
		return adminAuthDao.checkUserRoleIsPresent(user_role_id);
	}

	@Transactional
	public int checkRoleIsPresent(int id) {
		return adminAuthDao.checkRoleIsPresent(id);
	}

	@Transactional
	public int checkUserRoleByUserIdAndRoleId(long user_id, int role_id) {
		return adminAuthDao.checkUserRoleByUserIdAndRoleId(user_id, role_id);
	}

	@Transactional
	public int advisorTypes(Advtypes advtypes) {
		int result = adminDao.advisorTypes(advtypes);
		return result;
	}

	@Transactional
	public int removeAdvisorTypes(int id) {
		int result = adminDao.removeAdvisorTypes(id);
		return result;
	}

	@Transactional
	public int modifyAdvisorTypes(Advtypes advtypes) {
		int result = adminDao.modifyAdvisorTypes(advtypes);
		return result;
	}

	@Transactional
	public int followerStatus(AdmFollower admFollower) {
		int result = adminDao.followerStatus(admFollower);
		return result;
	}

	@Transactional
	public int modifyFollowerStatus(AdmFollower admFollower) {
		int result = adminDao.modifyFollowerStatus(admFollower);
		return result;
	}

	@Transactional
	public int removeFollowerStatus(int followerStatusId) {
		int result = adminDao.removeFollowerStatus(followerStatusId);
		return result;
	}

	@Transactional
	public int priorityItem(AdmPriority admPriority) {
		int result = adminDao.priorityItem(admPriority);
		return result;
	}

	@Transactional
	public int modifyPriorityItem(AdmPriority admPriority) {
		int result = adminDao.modifyPriorityItem(admPriority);
		return result;
	}

	@Transactional
	public int removePriorityItem(int priorityItemId) {
		int result = adminDao.removePriorityItem(priorityItemId);
		return result;
	}

	@Transactional
	public int saveAddAcctype(Acctype acctype) {
		int result = adminDao.saveAddAcctype(acctype);
		return result;

	}

	@Transactional
	public int RemoveAcctype(int AccTypeId) {
		int result = adminDao.RemoveAcctype(AccTypeId);
		return result;
	}

	@Transactional
	public int addProducts(Product Product) {
		int result = adminDao.addProducts(Product);
		return result;
	}

	@Transactional
	public int removeProducts(int prodId) {
		int result = adminDao.removeProducts(prodId);
		return result;
	}

	@Transactional
	public int modifyAcctype(Acctype acctype) {
		int result = adminDao.modifyAcctype(acctype);
		return result;
	}

	@Transactional
	public int modifyProduct(Product product) {
		int result = adminDao.modifyProduct(product);
		return result;
	}

	@Transactional
	public int riskPortfolio(AdmRiskPortfolio admRiskPortfolio) {
		// TODO Auto-generated method stub
		int result = adminDao.riskPortfolio(admRiskPortfolio);
		return result;
	}

	@Transactional
	public int modifyRiskPortfolio(AdmRiskPortfolio admRiskPortfolio) {
		// TODO Auto-generated method stub
		int result = adminDao.modifyRiskPortfolio(admRiskPortfolio);
		return result;
	}

	@Transactional
	public int removeRiskPortfolio(int riskPortfolioId) {
		// TODO Auto-generated method stub
		int result = adminDao.removeRiskPortfolio(riskPortfolioId);
		return result;
	}

	@Transactional
	public int license(License license) {
		// TODO Auto-generated method stub
		int result = adminDao.license(license);
		return result;
	}

	@Transactional
	public int modifyLicense(License license) {
		// TODO Auto-generated method stub
		int result = adminDao.modifyLicense(license);
		return result;
	}

	@Transactional
	public int removeLicense(int licId) {
		// TODO Auto-generated method stub
		int result = adminDao.removeLicense(licId);
		return result;
	}

	@Transactional
	public int addState(State state) {
		int result = adminDao.addState(state);
		return result;

	}

	@Transactional
	public int removeState(int prodId) {
		int result = adminDao.removeState(prodId);
		return result;
	}

	@Transactional
	public int modifyState(State state) {
		int result = adminDao.modifyState(state);
		return result;
	}

	@Transactional
	public int addRemuneration(Remuneration remuneration) {
		int result = adminDao.addRemuneration(remuneration);
		return result;
	}

	@Transactional
	public int removeRemuneration(int remId) {
		int result = adminDao.removeRemuneration(remId);
		return result;
	}

	@Transactional
	public int modifyRemuneration(Remuneration remuneration) {
		int result = adminDao.modifyRemuneration(remuneration);
		return result;
	}

	@Transactional
	public int addWorkflowstatus(Workflowstatus workflowstatus) {
		int result = adminDao.addWorkflowstatus(workflowstatus);
		return result;
	}

	@Transactional
	public int addArticleStatus(ArticleStatus articleStatus) {
		int result = adminDao.addArticleStatus(articleStatus);
		return result;
	}

	@Transactional
	public int modifyArticleStatus(ArticleStatus articleStatus) {
		int result = adminDao.modifyArticleStatus(articleStatus);
		return result;
	}

	@Transactional
	public int removeArticleStatus(int id) {
		int result = adminDao.removeArticleStatus(id);
		return result;
	}

	@Transactional
	public int modifyWorkflowstatus(Workflowstatus workflowstatus) {
		int result = adminDao.modifyWorkflowstatus(workflowstatus);
		return result;
	}

	@Transactional
	public int addCashFlowItemType(CashFlowItemType cashFlowItemType) {
		int result = adminDao.addCashFlowItemType(cashFlowItemType);
		return result;
	}

	@Transactional
	public int removeWorkFlowStatus(int workflowId) {
		int result = adminDao.removeWorkFlowStatus(workflowId);
		return result;
	}

	@Transactional
	public int modifyCashFlowItemType(CashFlowItemType cashFlowItemType) {
		int result = adminDao.modifyCashFlowItemType(cashFlowItemType);
		return result;
	}

	@Transactional
	public int addService(com.sowisetech.admin.model.Service service) {
		int result = adminDao.addService(service);
		return result;
	}

	@Transactional
	public int removeCashFlowItemType(int cashFlowItemTypeId) {
		int result = adminDao.removeCashFlowItemType(cashFlowItemTypeId);
		return result;
	}

	@Transactional
	public int modifyService(com.sowisetech.admin.model.Service service) {
		int result = adminDao.modifyService(service);
		return result;
	}

	@Transactional
	public int addCashFlowItem(CashFlowItem cashFlowItem) {
		int result = adminDao.addCashFlowItem(cashFlowItem);
		return result;
	}

	@Transactional
	public int removeService(int serviceId) {
		int result = adminDao.removeService(serviceId);
		return result;
	}

	// @Transactional
	// public int addArticleStatus(ArticleStatus articleStatus) {
	// int result = adminDao.addArticleStatus(articleStatus);
	// return result;
	// }
	// @Transactional
	// public int addArticleStatus(ArticleStatus articleStatus) {
	// int result = adminDao.addArticleStatus(articleStatus);
	// return result;
	// }

	@Transactional
	public int modifyCashFlowItem(CashFlowItem cashFlowItem) {
		int result = adminDao.modifyCashFlowItem(cashFlowItem);
		return result;
	}

	@Transactional
	public int removeCashFlowItem(int cashFlowItemId) {
		int result = adminDao.removeCashFlowItem(cashFlowItemId);
		return result;
	}

	@Transactional
	public int addCity(City city) {
		int result = adminDao.addCity(city);
		return result;
	}

	@Transactional
	public int modifyCity(City city) {
		int result = adminDao.modifyCity(city);
		return result;
	}

	@Transactional
	public int removeCity(int cityId) {
		int result = adminDao.removeCity(cityId);
		return result;
	}

	@Transactional
	public int urgency(Urgency urgency) {
		// TODO Auto-generated method stub
		int result = adminDao.urgency(urgency);
		return result;
	}

	public int addVotetype(Votetype votetype) {
		int result = adminDao.addVotetype(votetype);
		return result;
	}

	@Transactional

	public int modifyUrgency(Urgency urgency) {
		// TODO Auto-generated method stub
		int result = adminDao.modifyUrgency(urgency);
		return result;
	}

	public int addInsuranceItem(InsuranceItem insuranceItem) {
		int result = adminDao.addInsuranceItem(insuranceItem);
		return result;
	}

	@Transactional
	public int modifyVotetype(Votetype votetype) {
		int result = adminDao.modifyVotetype(votetype);
		return result;
	}

	@Transactional
	public int modifyInsuranceItem(InsuranceItem insuranceItem) {
		int result = adminDao.modifyInsuranceItem(insuranceItem);
		return result;
	}

	@Transactional
	public int removeVotetype(int id) {
		int result = adminDao.removeVotetype(id);
		return result;
	}

	@Transactional
	public int removeInsuranceItem(int insuranceItemId) {
		int result = adminDao.removeInsuranceItem(insuranceItemId);
		return result;
	}

	@Transactional
	public int addBrand(Brand brand) {
		int result = adminDao.addBrand(brand);
		return result;
	}

	@Transactional
	public int removeUrgency(int urgencyId) {
		// TODO Auto-generated method stub
		int result = adminDao.removeUrgency(urgencyId);
		return result;
	}

	public int addUserType(UserType userType) {
		int result = adminDao.addUserType(userType);
		return result;
	}

	@Transactional

	public int account(Account account) {
		// TODO Auto-generated method stub
		int result = adminDao.account(account);
		return result;
	}

	public int modifyBrand(Brand brand) {
		int result = adminDao.modifyBrand(brand);
		return result;
	}

	@Transactional

	public int modifyAccount(Account account) {
		// TODO Auto-generated method stub
		int result = adminDao.modifyAccount(account);
		return result;
	}

	public int modifyUserType(UserType userType) {
		int result = adminDao.modifyUserType(userType);
		return result;
	}

	@Transactional
	public int removeAccount(int accountEntryId) {
		int result = adminDao.removeAccount(accountEntryId);
		return result;
	}

	@Transactional
	public int removeBrand(int brandId) {
		int result = adminDao.removeBrand(brandId);
		return result;
	}

	@Transactional
	public int removeUserType(int id) {
		int result = adminDao.removeUserType(id);
		return result;
	}

	@Transactional
	public int addView(View view) {
		View viewPresent = adminDao.fetchView(view.getOwnerId(), view.getViewerId());
		int count = 1;
		int result = 0;
		if (viewPresent == null) {
			view.setCount(count);
			result = adminDao.addView(view, adminTableFields.getDelete_flag());
		} else {
			int countInc = 0;
			int viewCount = viewPresent.getCount();
			Date d1 = viewPresent.getUpdated();
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			Date d2 = timestamp;

			try {
				// in milliseconds
				long diffs = d2.getTime() - d1.getTime();
				System.out.print(diffs + "different\n");

				long diffSeconds = diffs / 1000 % 60;
				long diffMinutes = diffs / (60 * 1000) % 60;
				long diffHours = diffs / (60 * 60 * 1000) % 24;

				if (diffHours > 12 || diffMinutes > 720 || diffSeconds > 43200) {
					countInc = 1;
					count = viewCount + countInc;
					view.setCount(count);
					result = adminDao.updateView(view, viewPresent.getViewId());
				} else {
					result = adminDao.updateLastView(viewPresent.getViewId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Transactional
	public Party fetchPartyByRoleBasedId(String viewerId) {
		return adminDao.fetchPartyByRoleBasedId(viewerId, advTableFields.getDelete_flag_N());
	}

	@Transactional
	public int fetchCount(String ownerId) {
		List<View> viewList = (List<View>) adminDao.fetchViewByOwnerId(ownerId, advTableFields.getDelete_flag_N());
		int count = 0;
		for (View view : viewList) {
			count = count + view.getCount();
		}
		return count;
	}

	@Transactional
	public List<View> fetchAllViewCount() {
		return adminDao.fetchAllViewCount(advTableFields.getDelete_flag_N());
	}

}
