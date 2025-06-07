package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class ModifyAdminRequestValidator {
	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(ModifyAdminRequest modifyAdminRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());

		if (modifyAdminRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (modifyAdminRequest != null && modifyAdminRequest.getName() != null) {
				error = validateName(modifyAdminRequest.getName());
				if (error.isEmpty() == false) {
					allErrors.put("NAME", error);
				}
			}
			/*
			 * Add the validation for each advisor attributes or column.
			 * 
			 * allErrors.addAll(validateRateOfInterest(advisorRequest.getDisplayName()));
			 * allErrors.addAll(validateTenure(advisorRequest.getDob()));
			 * allErrors.addAll(validateLoanDate(advisorRequest.getEmailId())));
			 */
		}

		// for (String error : allErrors) {
		// if (StringUtils.isEmpty(error) == false) {
		// validErrors.add(error);
		// }
		//
		// }
		// return validErrors;
		return allErrors;

	}

	// protected HashMap<String, String> validatePassword(String password) {
	// String inputParamPassword = AdminConstants.SPACE_WTIH_COLON +
	// AdminConstants.PASSWORD;
	// HashMap<String, String> errors = new HashMap<String, String>();
	// if (common.nonEmptyCheck(password, inputParamPassword).isEmpty() == false) {
	// errors.put("EMPTY", common.nonEmptyCheck(password, inputParamPassword));
	// }
	// if (common.validPasswordCheck(password, inputParamPassword).isEmpty() ==
	// false) {
	// errors.put("FORMAT_ERROR", common.validPasswordCheck(password,
	// inputParamPassword));
	// }
	// return errors;
	// }

	// VALIDATION OF NAME
	protected HashMap<String, String> validateName(String name) {
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

	// VALIDATION OF EMAILID
	// protected HashMap<String, String> validateEmailId(String emailId) {
	// String inputParamMailid = AdminConstants.SPACE_WTIH_COLON +
	// AdminConstants.EMAILID;
	// HashMap<String, String> errors = new HashMap<String, String>();
	// if (common.nonEmptyCheck(emailId, inputParamMailid).isEmpty() == false) {
	// errors.put("EMPTY", common.nonEmptyCheck(emailId, inputParamMailid));
	// }
	// if (common.isValidEmailAddress(emailId, inputParamMailid).isEmpty() == false)
	// {
	// errors.put("FORMAT_ERROR", common.isValidEmailAddress(emailId,
	// inputParamMailid));
	// }
	// return errors;
	// }

}
