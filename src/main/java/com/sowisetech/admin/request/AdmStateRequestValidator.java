package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;
@Component

public class AdmStateRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(StateRequest stateRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdd_accountType());
		if (stateRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (stateRequest != null && stateRequest.getState() != null) {
				error = validateState(stateRequest.getState());
				if (error.isEmpty() == false) {
					allErrors.put("STATE", error);
				}
			}

		}
		return allErrors;

}

	private HashMap<String, String> validateState(String state) {
		String inputParamName =  AdminConstants.STATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(state, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(state, inputParamName));
		}
		if (common.isAlpha(state, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(state, inputParamName));
		}
		return errors;
	
	}
		
	}
