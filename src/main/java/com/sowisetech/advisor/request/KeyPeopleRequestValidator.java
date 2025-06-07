package com.sowisetech.advisor.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;
import com.sowisetech.advisor.util.AdvisorConstants;

@Component
public class KeyPeopleRequestValidator implements IValidator {
	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	/**
	 * Basic validation on KeyPeopleRequest input values.
	 * 
	 * @param keyPeopleRequest
	 * @return - Empty list or list of errors in string format.
	 */
	public HashMap<String, HashMap<String, String>> validate(KeyPeopleRequest keyPeopleRequest) {

		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (keyPeopleRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (keyPeopleRequest != null && keyPeopleRequest.getFullName() != null) {
				error = validateFullName(keyPeopleRequest.getFullName());
				if (error.isEmpty() == false) {
					allErrors.put("NAME", error);
				}
			}
			if (keyPeopleRequest != null && keyPeopleRequest.getDesignation() != null) {
				error = validateDesignation(keyPeopleRequest.getDesignation());
				if (error.isEmpty() == false) {
					allErrors.put("DESIGNATION", error);
				}
			}
			// if (keyPeopleRequest != null && keyPeopleRequest.getImage() != null) {
			// error = validateImage(keyPeopleRequest.getImage());
			// if (error.isEmpty() == false) {
			// allErrors.put("IMAGE", error);
			// }
			// }
		}
		return allErrors;

	}

	// protected HashMap<String, String> validateImage(String image) {
	// String inputParamName = AdvisorConstants.SPACE_WTIH_COLON +
	// AdvisorConstants.FULLNAME;
	// HashMap<String, String> errors = new HashMap<String, String>();
	// if (common.nonEmptyCheck(image, inputParamName).isEmpty() == false) {
	// errors.put("EMPTY", common.nonEmptyCheck(image, inputParamName));
	// }
	// if (common.isImage(image, inputParamName).isEmpty() == false) {
	// errors.put("NON_ALPHA", common.isImage(image, inputParamName));
	// }
	// return errors;
	// }

	protected HashMap<String, String> validateDesignation(String designation) {
		String inputParamDesignation = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.DESIGNATION;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(designation, inputParamDesignation);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}

		String error2 = common.designationCheck(designation, inputParamDesignation);
		if (error2.isEmpty() == false) {
			errors.put("DESIGNATION_ERROR", common.designationCheck(designation, inputParamDesignation));
		}
		return errors;
	}

	protected HashMap<String, String> validateFullName(String fullName) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.FULLNAME;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(fullName, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlpha(fullName, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		return errors;
	}

}
