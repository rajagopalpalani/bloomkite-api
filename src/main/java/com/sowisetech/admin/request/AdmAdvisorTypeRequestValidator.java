package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmAdvisorTypeRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdvtypesRequest advtypesRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (advtypesRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (advtypesRequest != null && advtypesRequest.getAdvType() != null) {
				error = validateAdvType(advtypesRequest.getAdvType());
				if (error.isEmpty() == false) {
					allErrors.put("ADVTYPE", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateAdvType(String advType) {
		String inputParamName = AdminConstants.ADVTYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(advType, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(advType, inputParamName));
		}
		if (common.isAlpha(advType, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(advType, inputParamName));
		}
		return errors;
	}


}
