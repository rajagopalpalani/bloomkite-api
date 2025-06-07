package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class UrgencyRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(UrgencyRequest urgencyRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (urgencyRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (urgencyRequest != null && urgencyRequest.getValue() != null) {
				error = validateValue(urgencyRequest.getValue());
				if (error.isEmpty() == false) {
					allErrors.put("VALUE", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateValue(String value) {
		String inputParamName = AdminConstants.VALUE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(value, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(value, inputParamName));
		}
		if (common.isAlpha(value, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(value, inputParamName));
		}
		return errors;
	}
	

}
