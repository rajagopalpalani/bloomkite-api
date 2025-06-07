package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class UserTypeRequestValidator {
	
	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;
	
	public HashMap<String, HashMap<String, String>> validate(UserTypeRequest userTypeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		
		if (userTypeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (userTypeRequest != null && userTypeRequest.getDesc() != null) {
				error = validate(userTypeRequest.getDesc());
				if (error.isEmpty() == false) {
					allErrors.put("DESC", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validate(String userType) {
		String inputParamName = AdminConstants.DESC;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(userType, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(userType, inputParamName));
		}
		if (common.isAlpha(userType, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(userType, inputParamName));
		}
		return errors;
	}

}
