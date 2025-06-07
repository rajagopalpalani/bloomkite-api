package com.sowisetech.advisor.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sowisetech.common.model.RoleScreenRights;

public class JwtResponse implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String roleBasedId;
	private final long partyId;
	private final long roleId;
	private final Map<Integer, RoleScreenRights> role_screenMap;

	public JwtResponse(String jwttoken, String roleBasedId, long partyId, long roleId,
			Map<Integer, RoleScreenRights> role_screenMap) {
		this.jwttoken = jwttoken;
		this.roleBasedId = roleBasedId;
		this.partyId = partyId;
		this.roleId = roleId;
		this.role_screenMap = role_screenMap;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public String getRoleBasedId() {
		return roleBasedId;
	}

	public long getPartyId() {
		return partyId;
	}

	public long getRoleId() {
		return roleId;
	}

	public Map<Integer, RoleScreenRights> getRole_screenMap() {
		return role_screenMap;
	}
}