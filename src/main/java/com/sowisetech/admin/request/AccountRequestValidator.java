package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AccountRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AccountRequest accountRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (accountRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (accountRequest != null && accountRequest.getAccountEntry() != null) {
				error = validateAccountEntry(accountRequest.getAccountEntry());
				if (error.isEmpty() == false) {
					allErrors.put("ACCOUNTENTRY", error);
				}
			}

		}
		return allErrors;

	}

	private HashMap<String, String> validateAccountEntry(String account) {
		String inputParamName = AdminConstants.ACCOUNTENTRY;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(account, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(account, inputParamName));
		}
		if (common.isAlpha(account, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(account, inputParamName));
		}
		return errors;
	}
	


}
