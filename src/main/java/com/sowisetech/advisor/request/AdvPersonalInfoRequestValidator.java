package com.sowisetech.advisor.request;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;

@Component
public class AdvPersonalInfoRequestValidator implements IValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdvPersonalInfoRequest advPersonalInfoRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (advPersonalInfoRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (advPersonalInfoRequest != null && advPersonalInfoRequest.getDesignation() != null) {
				error = validateDesignation(advPersonalInfoRequest.getDesignation());
				if (error.isEmpty() == false) {
					allErrors.put("DESIGNATION", error);
				}
			}
			if (advPersonalInfoRequest != null && advPersonalInfoRequest.getDob() != null) {
				error = validateDob(advPersonalInfoRequest.getDob());
				if (error.isEmpty() == false) {
					allErrors.put("DOB", error);
				}
			}
			if (advPersonalInfoRequest != null && advPersonalInfoRequest.getGender() != null) {
				error = validateGender(advPersonalInfoRequest.getGender());
				if (error.isEmpty() == false) {
					allErrors.put("GENDER", error);
				}
			}

			if (advPersonalInfoRequest != null && advPersonalInfoRequest.getState() != null) {
				error = validateState(advPersonalInfoRequest.getState());
				if (error.isEmpty() == false) {
					allErrors.put("STATE", error);
				}
			}
			if (advPersonalInfoRequest != null && advPersonalInfoRequest.getCity() != null) {
				error = validateCity(advPersonalInfoRequest.getCity());
				if (error.isEmpty() == false) {
					allErrors.put("CITY", validateCity(advPersonalInfoRequest.getCity()));
				}
			}
			if (advPersonalInfoRequest != null && advPersonalInfoRequest.getPincode() != null) {
				error = validatePincode(advPersonalInfoRequest.getPincode());
				if (error.isEmpty() == false) {
					allErrors.put("PINCODE", validatePincode(advPersonalInfoRequest.getPincode()));
				}
			}
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

	// // VALIDATION OF DISPLAYNAME
	// protected List<String> validateDisplayName(String displayname) {
	// String inputParamDisplayName = AdvisorConstants.SPACE_WTIH_COLON +
	// AdvisorConstants.DISPLAYNAME;
	// List<String> errors = new ArrayList<>();
	// errors.add(Common.nonEmptyCheck(displayname, inputParamDisplayName));
	// errors.add(Common.isAlpha(displayname, inputParamDisplayName));
	// return errors;
	// }

	// VALIDATION OF DESIGNATION
	HashMap<String, String> validateDesignation(String designation) {
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

	// VALIDATION OF DOB
	protected HashMap<String, String> validateDob(String dob) {
		String inputParamDob = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.DOB;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(dob, inputParamDob);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.dobCheck(dob, inputParamDob);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;
	}

	// VALIDATION OF GENDER
	protected HashMap<String, String> validateGender(String gender) {
		String inputParamGender = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.GENDER;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(gender, inputParamGender);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.genderCheck(gender, inputParamGender);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;
	}

	// VALIDATION OF STATE
	protected HashMap<String, String> validateState(String state) {
		String inputParamState = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.STATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(state, inputParamState);
		if (error1.isEmpty() == false) {
			errors.put("Empty", error1);
		}
		String error2 = common.isAlpha(state, inputParamState);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		return errors;
	}

	// VALIDATION OF CITY
	protected HashMap<String, String> validateCity(String city) {
		String inputParamCity = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.CITY;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(city, inputParamCity);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.validateCity(city, inputParamCity);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		return errors;
	}
	// VALIDATION OF PINCODE

	protected HashMap<String, String> validatePincode(String pincode) {
		String inputParamPincode = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PINCODE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(pincode, inputParamPincode);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isNumericValues(pincode, inputParamPincode);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		String error3 = common.pincodelengthCheck(pincode, inputParamPincode);
		if (error3.isEmpty() == false) {
			errors.put("LENGTH_ERROR", error3);
		}
		return errors;

	}
}
