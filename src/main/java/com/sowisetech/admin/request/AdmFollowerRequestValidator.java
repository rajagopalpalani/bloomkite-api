package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmFollowerRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdmFollowerRequest admFollowerRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (admFollowerRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (admFollowerRequest != null && admFollowerRequest.getStatus() != null) {
				error = validateStatus(admFollowerRequest.getStatus());
				if (error.isEmpty() == false) {
					allErrors.put("STATUS", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateStatus(String status) {
		String inputParamName = AdminConstants.STATUS;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(status, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(status, inputParamName));
		}
		if (common.isAlpha(status, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(status, inputParamName));
		}
		return errors;
	}


}
