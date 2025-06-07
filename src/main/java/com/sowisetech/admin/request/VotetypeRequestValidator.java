package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class VotetypeRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate( VotetypeRequest votetypeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (votetypeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (votetypeRequest != null && votetypeRequest.getDesc() != null) {
				error = validateStatus(votetypeRequest.getDesc());
				if (error.isEmpty() == false) {
					allErrors.put("VOTETYPE", error);
				}
			}

		}
		return allErrors;

}

	private HashMap<String, String> validateStatus(String desc) {
		String inputParamName = AdminConstants.VOTETYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(desc, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(desc, inputParamName));
		}
		if (common.isAlpha(desc, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(desc, inputParamName));
		}
		return errors;
	}
	
	}
