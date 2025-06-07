package com.sowisetech.advisor.dao;

import java.util.List;

import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

public interface AuthDao {

	int addUser_role(long partyId, long roleId, String signedUserId, int isPrimaryRole);

	List<RoleScreenRights> fetchScreenRightsByRoleId(long user_role_id);

	List<RoleFieldRights> fetchFieldRights(List<Integer> roleScreenId);

	int fetchUserRoleIdByPartyId(long partyId, int roleId);

	List<User_role> fetchUserRoleByUserId(long partyId);

	List<RoleAuth> fetchRoleList();

	int fetchRoleIdByName(String name);

	String fetchRoleByRoleId(long roleId);

	String fetchScreenCodeByScreenId(int screenId);

	List<Integer> fetchScreenIdsByStartWithScreenCode(String screenCode);

	int changeToCorporateRoleId(int roleIdCorp, long partyId, String signedUserId);
}
