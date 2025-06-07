package com.sowisetech.advisor.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;
import com.sowisetech.advisor.util.AdvisorConstants;

@Component
public class ResetPasswordRequestValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(ResetPasswordRequest resetPasswordRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (resetPasswordRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (resetPasswordRequest != null && resetPasswordRequest.getNewPassword() != null) {
				error = validatePassword(resetPasswordRequest.getNewPassword());
				if (error.isEmpty() == false) {
					allErrors.put("PASSWORD", error);
				}
			}

		}
		return allErrors;

	}

	protected HashMap<String, String> validatePassword(String password) {
		String inputParamPasswrd = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PASSWRD;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(password, inputParamPasswrd);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.validPasswordCheck(password, inputParamPasswrd);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;
	}
}
