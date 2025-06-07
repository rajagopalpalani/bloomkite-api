package com.sowisetech.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sowisetech.admin.model.User_role;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

@Service
public interface CommonService {

	long addMailMessage(String to, String subject, String originalContent, String fromUser, int noOfAttempt);

	int updateMailMessage_ifFailed(long messageId, int noOfAttempt, String reason);


	List<RoleScreenRights> fetchScreenRightsByRoleId(long user_role_id);

	List<RoleFieldRights> fetchFieldRights(List<Integer> roleScreenId);

	int fetchUserRoleIdByPartyId(long partyId, int roleId);

	int addActivationLink(String string, String url, String verifyKey, String subject);

	String fetchLatestKeyByEmailIdAndSub(String emailId, String mailSub);

	List<User_role> fetchUserRoleByUserId(long partyId);

	String fetchScreenCodeByScreenId(int screenId);

	List<Integer> fetchScreenIdsByStartWithScreenCode(String screenCode);
}
