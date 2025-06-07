package com.sowisetech.membership.response;

public class MembershipResponse {

	MembershipResponseMessage responseMessage;
	MembershipResponseData responseData;

	public MembershipResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(MembershipResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public MembershipResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(MembershipResponseData responseData) {
		this.responseData = responseData;
	}

}
