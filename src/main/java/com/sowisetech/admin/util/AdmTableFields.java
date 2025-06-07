package com.sowisetech.admin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:admtablefields.properties")
@Component
public class AdmTableFields {

	@Value("${delete_flag}")
	public String delete_flag;

	@Value("${delete_flag_y}")
	public String delete_flag_y;

	@Value("${role_name}")
	public String role_name;

	@Value("${encryption_password}")
	public String encryption_password;

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public String getDelete_flag_y() {
		return delete_flag_y;
	}

	public void setDelete_flag_y(String delete_flag_y) {
		this.delete_flag_y = delete_flag_y;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getEncryption_password() {
		return encryption_password;
	}

	public void setEncryption_password(String encryption_password) {
		this.encryption_password = encryption_password;
	}

}
