package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmAccTypeRequestValidator {
	
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AcctypeRequest acctypeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdd_accountType());
		if (acctypeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (acctypeRequest != null && acctypeRequest.getAccType() != null) {
				error = validateAccType(acctypeRequest.getAccType());
				if (error.isEmpty() == false) {
					allErrors.put("ACCTYPE", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateAccType(String accType) {
		String inputParamName = AdminConstants.SPACE_WTIH_COLON + AdminConstants.ACCTYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(accType, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(accType, inputParamName));
		}
		if (common.isAlpha(accType, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(accType, inputParamName));
		}
		return errors;
	}

}
