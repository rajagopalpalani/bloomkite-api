package com.sowisetech.admin.dao;

import java.util.List;

import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.ScreenFieldRights;
import com.sowisetech.admin.model.User_role;

public interface AdminAuthDao {

	User_role fetchUserRoleByUserRoleId(int user_role_id);

	int addScreenRights(ScreenFieldRights screenFieldRights);

	int addFieldRights(FieldRights fieldRights);

	List<ScreenFieldRights> fetchScreenRightsByUserRoleId(long user_role_id);

	int deleteFieldRightsByRoleScreenRightsId(int role_screen_rights_id);

	int deleteScreenRightsByUserRoleId(int user_role_id);

	int addRole(RoleAuth role);

	RoleAuth fetchRoleByRoleId(int id);

	int modifyRole(int id, RoleAuth roleAuth);

	int removeRole(int roleId);

	int addUserRole(User_role userRole);

	int modifyUserRole(int id, User_role userRole);

	int removeUserRole(int roleId);

	User_role fetchUserRoleByUserIdAndRoleId(long user_id, int role_id);

	long fetchRoleIdByName(String name);

	int addUser_role(long partyId, long roleId, String adminId, int isPrimaryRole);

	int checkUserRoleIsPresent(int user_role_id);

	int checkRoleIsPresent(int id);

	int checkUserRoleByUserIdAndRoleId(long user_id, int role_id);

	// User_role fetchUserRoleByUserRoleIdAndRoleId(int user_role_id, int role_id);
}
