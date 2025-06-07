package com.sowisetech.advisor.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;

@Component
public class ModifyAdvReqValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(ModifyAdvRequest modifyAdvRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (modifyAdvRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (modifyAdvRequest != null && modifyAdvRequest.getName() != null) {
			// error = validateName(modifyAdvRequest.getName());
			// if (error.isEmpty() == false) {
			// allErrors.put("NAME", error);
			// }
			// }
			if (modifyAdvRequest != null && modifyAdvRequest.getDesignation() != null) {
				error = validateDesignation(modifyAdvRequest.getDesignation());
				if (error.isEmpty() == false) {
					allErrors.put("DESIGNATION", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getEmailId() != null) {
				error = validateEmailid(modifyAdvRequest.getEmailId());
				if (error.isEmpty() == false) {
					allErrors.put("EMAILID", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getPhoneNumber() != null) {
				error = validatePhoneNumber(modifyAdvRequest.getPhoneNumber());
				if (error.isEmpty() == false) {
					allErrors.put("PHONENUMBER", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getDob() != null) {
				error = validateDob(modifyAdvRequest.getDob());
				if (error.isEmpty() == false) {
					allErrors.put("DOB", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getGender() != null) {
				error = validateGender(modifyAdvRequest.getGender());
				if (error.isEmpty() == false) {
					allErrors.put("GENDER", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getPanNumber() != null) {
				error = validatePan(modifyAdvRequest.getPanNumber());
				if (error.isEmpty() == false) {
					allErrors.put("PAN", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getState() != null) {
				error = validateState(modifyAdvRequest.getState());
				if (error.isEmpty() == false) {
					allErrors.put("STATE", error);
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getCity() != null) {
				error = validateCity(modifyAdvRequest.getCity());
				if (error.isEmpty() == false) {
					allErrors.put("CITY", validateCity(modifyAdvRequest.getCity()));
				}
			}
			if (modifyAdvRequest != null && modifyAdvRequest.getPincode() != null) {
				error = validatePincode(modifyAdvRequest.getPincode());
				if (error.isEmpty() == false) {
					allErrors.put("PINCODE", validatePincode(modifyAdvRequest.getPincode()));
				}
			}

			/*
			 * Add the validation for each advisor attributes or column.
			 * 
			 * allErrors.addAll(validateRateOfInterest(modifyAdvRequest.getDisplayName()));
			 * allErrors.addAll(validateTenure(modifyAdvRequest.getDob()));
			 * allErrors.addAll(validateLoanDate(modifyAdvRequest.getEmailId())));
			 */
		}

		// for (String error : allErrors) {
		// if (StringUtils.isEmpty(error) == false) {
		// validErrors.add(error);
		// }
		//
		// }
		return allErrors;

	}

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

	// VALIDATION OF NAME
	protected HashMap<String, String> validateName(String name) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.NAME;
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
	protected HashMap<String, String> validateEmailid(String mailid) {
		String inputParamMailid = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.EMAILID;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(mailid, inputParamMailid);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidEmailAddress(mailid, inputParamMailid);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;

	}

	// VALIDATION OF MOBILENUMBER

	protected HashMap<String, String> validatePhoneNumber(String phoneNumber) {
		String inputParamPhoneNumber = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PHONENUMBER;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(phoneNumber, inputParamPhoneNumber);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isNumericValues(phoneNumber, inputParamPhoneNumber);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		String error3 = common.phoneNumberlengthCheck(phoneNumber, inputParamPhoneNumber);
		if (error3.isEmpty() == false) {
			errors.put("LENGTH_ERROR", error3);
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

	// PAN number validation
	protected HashMap<String, String> validatePan(String pan) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PAN;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error = common.validPan(pan, inputParamName);
		if (error.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error);
		}
		return errors;
	}

	// VALIDATION OF DISPLAYNAME
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

	// VALIDATION OF DISPLAYNAME
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
