package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmRemunerationRequestValidator {

	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(RemunerationRequest remunerationRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdd_accountType());
		if (remunerationRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (remunerationRequest != null && remunerationRequest.getRemuneration() != null) {
				error = validateRemuneration(remunerationRequest.getRemuneration());
				if (error.isEmpty() == false) {
					allErrors.put("REMUNERATION", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateRemuneration(String remuneration) {
		String inputParamName =  AdminConstants.REMUNERATION;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(remuneration, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(remuneration, inputParamName));
		}
		if (common.isAlpha(remuneration, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(remuneration, inputParamName));
		}
		return errors;
	}
	

}
