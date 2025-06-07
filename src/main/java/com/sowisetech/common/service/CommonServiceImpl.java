package com.sowisetech.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.common.dao.CommonDao;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

@Transactional(readOnly = true)
@Service("CommonService")
public class CommonServiceImpl implements CommonService {

	@Autowired
	private CommonDao commonDao;
	@Autowired
	private AuthDao authDao;
	@Autowired
	AdvTableFields advTableFields;

	@Transactional
	public long addMailMessage(String to, String subject, String originalContent, String fromUser, int noOfAttempt) {
		String encryptPass = advTableFields.getEncryption_password();
		return commonDao.addMailMessage(to, subject, originalContent, fromUser, noOfAttempt, encryptPass);
	}

	@Transactional
	public int updateMailMessage_ifFailed(long messageId, int noOfAttempt, String reason) {
		long IfFailed = advTableFields.getIfFailed();
//		System.out.println(messageId + "           " + reason);
		return commonDao.updateMailMessage_ifFailed(messageId, IfFailed, noOfAttempt, reason);

	}

	@Transactional
	public List<RoleScreenRights> fetchScreenRightsByRoleId(long user_role_id) {
		return authDao.fetchScreenRightsByRoleId(user_role_id);
	}

	@Transactional
	public List<RoleFieldRights> fetchFieldRights(List<Integer> roleScreenId) {
		return authDao.fetchFieldRights(roleScreenId);
	}

	@Transactional
	public int fetchUserRoleIdByPartyId(long partyId, int roleId) {
		return authDao.fetchUserRoleIdByPartyId(partyId, roleId);
	}

	@Transactional
	public int addActivationLink(String emailId, String url, String verifykey, String subject) {
		String encryptPass = advTableFields.getEncryption_password();
		return commonDao.addActivationLink(emailId, url, verifykey, subject, encryptPass);
	}

	@Transactional
	public String fetchLatestKeyByEmailIdAndSub(String emailId, String mailSub) {
		String encryptPass = advTableFields.getEncryption_password();
		return commonDao.fetchLatestKeyByEmailIdAndSub(emailId, mailSub, encryptPass);
	}

	@Transactional
	public List<User_role> fetchUserRoleByUserId(long partyId) {
		return authDao.fetchUserRoleByUserId(partyId);
	}

	@Transactional
	public String fetchScreenCodeByScreenId(int screenId) {
		return authDao.fetchScreenCodeByScreenId(screenId);
	}

	@Transactional
	public List<Integer> fetchScreenIdsByStartWithScreenCode(String screenCode) {
		return authDao.fetchScreenIdsByStartWithScreenCode(screenCode);
	}

}
