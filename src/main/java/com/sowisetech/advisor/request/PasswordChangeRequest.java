package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class PasswordChangeRequest {

	private int screenId;
	private String currentPassword;
	private String newPassword;
	// private String advId;
	private String roleBasedId;
	// private String invId;
	// private long roleId;

	// public long getRoleId() {
	// return roleId;
	// }
	//
	// public void setRoleId(long roleId) {
	// this.roleId = roleId;
	// }

	// public String getAdvId() {
	// return advId;
	// }
	//
	// public void setAdvId(String advId) {
	// this.advId = advId;
	// }
	//
	// public String getInvId() {
	// return invId;
	// }
	//
	// public void setInvId(String invId) {
	// this.invId = invId;
	// }
	//

	public String getRoleBasedId() {
		return roleBasedId;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public void setRoleBasedId(String roleBasedId) {
		this.roleBasedId = roleBasedId;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
