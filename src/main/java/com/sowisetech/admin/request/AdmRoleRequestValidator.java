package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;
import com.sowisetech.advisor.request.IValidator;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;
import com.sowisetech.advisor.util.AdvisorConstants;

@Component
public class AdmRoleRequestValidator implements IValidator {

	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdmRoleRequest admRoleRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (admRoleRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (admRoleRequest != null && admRoleRequest.getName() != null) {
				error = validateName(admRoleRequest.getName());
				if (error.isEmpty() == false) {
					allErrors.put("NAME", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateName(String name) {
		String inputParamName = AdminConstants.SPACE_WTIH_COLON + AdminConstants.NAME;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(name, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(name, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		return errors;
	}

}
