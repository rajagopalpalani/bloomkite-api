package com.sowisetech.advisor.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;

@Component
public class PasswordValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(PasswordChangeRequest passwordChangeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (passwordChangeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (passwordChangeRequest != null && passwordChangeRequest.getNewPassword() != null) {
				error = validatePassword(passwordChangeRequest.getNewPassword());
				if (error.isEmpty() == false) {
					allErrors.put("PASSWORD", error);
				}
			}

		}

		// for (String error : allErrors) {
		// if (StringUtils.isEmpty(error) == false) {
		// validErrors.add(error);
		// }
		//
		// }
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
