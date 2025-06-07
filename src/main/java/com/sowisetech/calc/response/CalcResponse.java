package com.sowisetech.calc.response;

public class CalcResponse {

	CalcResponseMessage responseMessage;
	CalcResponseData responseData;

	public CalcResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(CalcResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public CalcResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(CalcResponseData responseData) {
		this.responseData = responseData;
	}

}
