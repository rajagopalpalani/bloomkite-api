package com.sowisetech.advisor.response;

import java.util.List;

import com.sowisetech.common.model.RoleFieldRights;

public class AdvResponseData {

	Object data;
	List<RoleFieldRights> roleFieldRights;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<RoleFieldRights> getRoleFieldRights() {
		return roleFieldRights;
	}

	public void setRoleFieldRights(List<RoleFieldRights> roleFieldRights) {
		this.roleFieldRights = roleFieldRights;
	}

}
