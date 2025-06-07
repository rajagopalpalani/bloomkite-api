package com.sowisetech.investor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:invtablefields.properties")
public class InvTableFields {

	@Value("${desc}")
	public String desc;

	@Value("${roleNameInv}")
	public String roleNameInv;

	@Value("${delete_flag_N}")
	public String delete_flag_N;

	@Value("${delete_flag_Y}")
	public String delete_flag_Y;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRoleNameInv() {
		return roleNameInv;
	}

	public void setRoleNameInv(String roleNameInv) {
		this.roleNameInv = roleNameInv;
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

}
