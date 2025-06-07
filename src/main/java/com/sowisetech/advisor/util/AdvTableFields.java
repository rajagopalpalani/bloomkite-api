package com.sowisetech.advisor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:advtablefields.properties")
public class AdvTableFields {

	@Value("${advType}")
	public String advType;

	@Value("${advType_Corporate}")
	public String advType_Corporate;

	@Value("${roleName}")
	public String roleName;

	@Value("${roleName_inv}")
	public String roleName_inv;

	@Value("${roleName_nonIndividual}")
	public String roleName_nonIndividual;

	@Value("${partystatus_desc}")
	public String partystatus_desc;

	@Value("${delete_flag_N}")
	public String delete_flag_N;

	@Value("${delete_flag_Y}")
	public String delete_flag_Y;

	@Value("${ifFailed}")
	public long ifFailed;

	@Value("${account_verified}")
	public int account_verified;

	@Value("${account_not_verified}")
	public int account_not_verified;

	@Value("${workflow_status_drafted}")
	public String workflow_status_drafted;

	@Value("${workflow_status_created}")
	public String workflow_status_created;

	@Value("${workflow_status_pending}")
	public String workflow_status_pending;

	@Value("${workflow_status_approved}")
	public String workflow_status_approved;

	@Value("${workflow_status_hide_public}")
	public String workflow_status_hide_public;

	@Value("${workflow_status_revoked}")
	public String workflow_status_revoked;

	@Value("${workFlow_Default}")
	public String workFlow_Default;

	@Value("${encryption_password}")
	public String encryption_password;

	@Value("${follower_Status_Active}")
	public String follower_Status_Active;

	@Value("${follower_Status_Inactive}")
	public String follower_Status_Inactive;

	@Value("${user_type_advisor}")
	public String user_type_advisor;

	@Value("${user_type_investor}")
	public String user_type_investor;

	@Value("${is_primary_role_true}")
	public int is_primary_role_true;

	@Value("${common_application}")
	public String common_application;

	@Value("${admin_application}")
	public String admin_application;

	@Value("${roleName_admin}")
	public String roleName_admin;

	@Value("${follower_Status_Blocked}")
	public String follower_Status_Blocked;

	@Value("${follower_Status_Refollow}")
	public String follower_Status_Refollow;

	@Value("${workflow_status_deleted}")
	public String workflow_status_deleted;

	@Value("${workflow_status_created_with_approved}")
	public String workflow_status_created_with_approved;

	@Value("${workflow_status_deactivated}")
	public String workflow_status_deactivated;

	@Value("${follower_status_unfollow}")
	public String follower_status_unfollow;

	@Value("${roleName_blogger}")
	public String roleName_blogger;

	@Value("${promoCount}")
	public int promoCount;

	public long getIfFailed() {
		return ifFailed;
	}

	public void setIfFailed(long ifFailed) {
		this.ifFailed = ifFailed;
	}

	// @Value("${roleName_teamMember}")
	// public String roleName_teamMember;

	public String getAdvType() {
		return advType;
	}

	public void setAdvType(String advType) {
		this.advType = advType;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDelete_flag_N() {
		return delete_flag_N;
	}

	public void setDelete_flag_N(String delete_flag_N) {
		this.delete_flag_N = delete_flag_N;
	}

	public String getDelete_flag_Y() {
		return delete_flag_Y;
	}

	public void setDelete_flag_Y(String delete_flag_Y) {
		this.delete_flag_Y = delete_flag_Y;
	}

	public String getAdvType_Corporate() {
		return advType_Corporate;
	}

	public void setAdvType_Corporate(String advType_Corporate) {
		this.advType_Corporate = advType_Corporate;
	}

	public String getRoleName_inv() {
		return roleName_inv;
	}

	public void setRoleName_inv(String roleName_inv) {
		this.roleName_inv = roleName_inv;
	}

	public String getRoleName_nonIndividual() {
		return roleName_nonIndividual;
	}

	public void setRoleName_nonIndividual(String roleName_nonIndividual) {
		this.roleName_nonIndividual = roleName_nonIndividual;
	}

	public String getPartystatus_desc() {
		return partystatus_desc;
	}

	public void setPartystatus_desc(String partystatus_desc) {
		this.partystatus_desc = partystatus_desc;
	}

	public int getAccount_verified() {
		return account_verified;
	}

	public void setAccount_verified(int account_verified) {
		this.account_verified = account_verified;
	}

	public int getAccount_not_verified() {
		return account_not_verified;
	}

	public void setAccount_not_verified(int account_not_verified) {
		this.account_not_verified = account_not_verified;
	}

	public String getWorkflow_status_drafted() {
		return workflow_status_drafted;
	}

	public void setWorkflow_status_drafted(String workflow_status_drafted) {
		this.workflow_status_drafted = workflow_status_drafted;
	}

	public String getWorkflow_status_created() {
		return workflow_status_created;
	}

	public void setWorkflow_status_created(String workflow_status_created) {
		this.workflow_status_created = workflow_status_created;
	}

	public String getWorkflow_status_pending() {
		return workflow_status_pending;
	}

	public void setWorkflow_status_pending(String workflow_status_pending) {
		this.workflow_status_pending = workflow_status_pending;
	}

	public String getWorkflow_status_approved() {
		return workflow_status_approved;
	}

	public void setWorkflow_status_approved(String workflow_status_approved) {
		this.workflow_status_approved = workflow_status_approved;
	}

	public String getWorkflow_status_hide_public() {
		return workflow_status_hide_public;
	}

	public void setWorkflow_status_hide_public(String workflow_status_hide_public) {
		this.workflow_status_hide_public = workflow_status_hide_public;
	}

	public String getWorkflow_status_revoked() {
		return workflow_status_revoked;
	}

	public void setWorkflow_status_revoked(String workflow_status_revoked) {
		this.workflow_status_revoked = workflow_status_revoked;
	}

	public String getWorkFlow_Default() {
		return workFlow_Default;
	}

	public void setWorkFlow_Default(String workFlow_Default) {
		this.workFlow_Default = workFlow_Default;
	}

	public String getEncryption_password() {
		return encryption_password;
	}

	public void setEncryption_password(String encryption_password) {
		this.encryption_password = encryption_password;
	}

	public String getUser_type_advisor() {
		return user_type_advisor;
	}

	public void setUser_type_advisor(String user_type_advisor) {
		this.user_type_advisor = user_type_advisor;
	}

	public String getUser_type_investor() {
		return user_type_investor;
	}

	public void setUser_type_investor(String user_type_investor) {
		this.user_type_investor = user_type_investor;
	}

	public String getFollower_Status_Active() {
		return follower_Status_Active;
	}

	public void setFollower_Status_Active(String follower_Status_Active) {
		this.follower_Status_Active = follower_Status_Active;
	}

	public String getFollower_Status_Inactive() {
		return follower_Status_Inactive;
	}

	public void setFollower_Status_Inactive(String follower_Status_Inactive) {
		this.follower_Status_Inactive = follower_Status_Inactive;
	}

	public int getIs_primary_role_true() {
		return is_primary_role_true;
	}

	public void setIs_primary_role_true(int is_primary_role_true) {
		this.is_primary_role_true = is_primary_role_true;
	}

	public String getCommon_application() {
		return common_application;
	}

	public void setCommon_application(String common_application) {
		this.common_application = common_application;
	}

	public String getAdmin_application() {
		return admin_application;
	}

	public void setAdmin_application(String admin_application) {
		this.admin_application = admin_application;
	}

	public String getRoleName_admin() {
		return roleName_admin;
	}

	public void setRoleName_admin(String roleName_admin) {
		this.roleName_admin = roleName_admin;
	}

	public String getFollower_Status_Blocked() {
		return follower_Status_Blocked;
	}

	public void setFollower_Status_Blocked(String follower_Status_Blocked) {
		this.follower_Status_Blocked = follower_Status_Blocked;
	}

	public String getFollower_Status_Refollow() {
		return follower_Status_Refollow;
	}

	public void setFollower_Status_Refollow(String follower_Status_Refollow) {
		this.follower_Status_Refollow = follower_Status_Refollow;
	}

	public String getWorkflow_status_deleted() {
		return workflow_status_deleted;
	}

	public void setWorkflow_status_deleted(String workflow_status_deleted) {
		this.workflow_status_deleted = workflow_status_deleted;
	}

	public String getWorkflow_status_created_with_approved() {
		return workflow_status_created_with_approved;
	}

	public void setWorkflow_status_created_with_approved(String workflow_status_created_with_approved) {
		this.workflow_status_created_with_approved = workflow_status_created_with_approved;
	}

	public String getWorkflow_status_deactivated() {
		return workflow_status_deactivated;
	}

	public void setWorkflow_status_deactivated(String workflow_status_deactivated) {
		this.workflow_status_deactivated = workflow_status_deactivated;
	}

	public String getFollower_status_unfollow() {
		return follower_status_unfollow;
	}

	public void setFollower_status_unfollow(String follower_status_unfollow) {
		this.follower_status_unfollow = follower_status_unfollow;
	}

	public String getRoleName_blogger() {
		return roleName_blogger;
	}

	public void setRoleName_blogger(String roleName_blogger) {
		this.roleName_blogger = roleName_blogger;
	}

	public int getPromoCount() {
		return promoCount;
	}

	public void setPromoCount(int promoCount) {
		this.promoCount = promoCount;
	}

	// public String getRoleName_teamMember() {
	// return roleName_teamMember;
	// }
	//
	// public void setRoleName_teamMember(String roleName_teamMember) {
	// this.roleName_teamMember = roleName_teamMember;
	// }

}
