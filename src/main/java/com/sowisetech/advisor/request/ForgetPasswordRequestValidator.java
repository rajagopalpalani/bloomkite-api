package com.sowisetech.advisor.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;
import com.sowisetech.advisor.util.AdvisorConstants;

@Component
public class ForgetPasswordRequestValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(ForgetPasswordRequest forgetPasswordRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (forgetPasswordRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (forgetPasswordRequest != null && forgetPasswordRequest.getEmailId() != null) {
				error = validateEmailId(forgetPasswordRequest.getEmailId());
				if (error.isEmpty() == false) {
					allErrors.put("EmailId", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateEmailId(String emailId) {

		String inputParamMailid = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.EMAILID;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(emailId, inputParamMailid);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidEmailAddress(emailId, inputParamMailid);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;
	}
}
