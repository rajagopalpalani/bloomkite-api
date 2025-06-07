package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmPriorityRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdmPriorityRequest admPriorityRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (admPriorityRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (admPriorityRequest != null && admPriorityRequest.getPriorityItem() != null) {
				error = validatePriorityItem(admPriorityRequest.getPriorityItem());
				if (error.isEmpty() == false) {
					allErrors.put("PRIORITYITEM", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validatePriorityItem(String priorityItem) {
		String inputParamName = AdminConstants.PRIORITYITEM;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(priorityItem, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(priorityItem, inputParamName));
		}
		if (common.isAlpha(priorityItem, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(priorityItem, inputParamName));
		}
		return errors;
	}



}
