package com.sowisetech.admin.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.admin.request.AdminRequest;

@Component
public class AdmCommon {

	@Autowired
	AdmAppMessages appMessages;

	public AdminRequest getJsonRequest(String requestStr, AdminRequest jsonRequest)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(requestStr)) {
			throw new IllegalArgumentException(appMessages.getJson_request_error());
		}
		jsonRequest = new ObjectMapper().readValue(requestStr, jsonRequest.getClass());
		return jsonRequest;
	}

	public String nonEmptyCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isEmpty(inputParam)) {
			errorMsg = appMessages.getValue_null_or_empty() + inputParamName;
		}
		return errorMsg;
	}

	public String isAlpha(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isAlphaSpace(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_alpha() + inputParamName;
		}
		return errorMsg;
	}

	public String isValidEmailAddress(String inputParam, String inputParamMailid) {
		String errorMsg = "";

		String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

		if (inputParam.matches(EMAIL_REGEX) == false) {
			errorMsg = appMessages.getValue_is_not_emailid() + inputParamMailid;
		}
		return errorMsg;

	}
	
	public String pincodelengthCheck(String inputParam, String inputParamPincode) {
		String errorMsg = "";
		if (inputParam.length() > 6 || inputParam.length() < 6) {
			errorMsg = appMessages.getValue_is_not_pincode() + inputParamPincode;
		}
		return errorMsg;
	}
	
	public String isNumericValues(String inputParam, String inputParamNumbers) {
		String errorMsg = "";
		if (StringUtils.isNumeric(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_pincode() + inputParamNumbers;
		}
		return errorMsg;
	}

	public String validPasswordCheck(String inputParam, String inputParamPassword) {
		String errorMsg = "";
		// Password must have One special character,one number and one capital letter.
		// length:min=8,max=16
		String PASWRD_REGEX = "((?=.*\\d)(?=.*[A-Z])(?=.*[@!^&*(+_?)#$%]).{8,16})";

		if (inputParam.matches(PASWRD_REGEX) == false) {
			errorMsg = appMessages.getPassword_format_error() + inputParamPassword;
		}
		return errorMsg;

	}

}
