package com.sowisetech.admin.response;

import com.sowisetech.advisor.response.AdvResponseData;
import com.sowisetech.advisor.response.AdvResponseMessage;

public class AdmResponse {

	AdmResponseMessage responseMessage;
	AdmResponseData responseData;

	public AdmResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(AdmResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public AdmResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(AdmResponseData responseData) {
		this.responseData = responseData;
	}

}
