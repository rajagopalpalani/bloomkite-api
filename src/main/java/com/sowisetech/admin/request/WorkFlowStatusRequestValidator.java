package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class WorkFlowStatusRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate( WorkFlowStatusRequest workFlowStatusRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (workFlowStatusRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (workFlowStatusRequest != null && workFlowStatusRequest.getStatus() != null) {
				error = validateStatus(workFlowStatusRequest.getStatus());
				if (error.isEmpty() == false) {
					allErrors.put("WORKFLOWSTATUS", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateStatus(String status) {
		String inputParamName = AdminConstants.WORKFLOWSTATUS;
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
