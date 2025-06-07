package com.sowisetech.admin.request;

import org.springframework.stereotype.Component;

@Component
public class AcctypeRequest {

	public int accTypeId;
	public String accType;

	public int getAccTypeId() {
		return accTypeId;
	}

	public void setAccTypeId(int accTypeId) {
		this.accTypeId = accTypeId;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

}
