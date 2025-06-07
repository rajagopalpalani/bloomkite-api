package com.sowisetech.advisor.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;

@Component
public class AdvisorRequestValidator implements IValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;
	@Autowired
	AdvisorService advisorService;

	/**
	 * Basic validation on AdvisorRequest input values.
	 * 
	 * @param advisorRequest
	 * @param roleIdUser
	 * @param roleId
	 * @return - Empty list or list of errors in string format.
	 */
	public HashMap<String, HashMap<String, String>> validate(AdvisorRequest advisorRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (advisorRequest == null) {
			allErrors.put("NULL", error);
		} else {
			long roleIdNonAdv = advisorService.fetchNonAdvRoleIdByName();
			if (roleIdNonAdv != advisorRequest.getRoleId()) {
				if (advisorRequest != null && advisorRequest.getName() != null) {
					error = validateName(advisorRequest.getName());
					if (error.isEmpty() == false) {
						allErrors.put("NAME", error);
					}
				}
			}
			if (advisorRequest != null && advisorRequest.getEmailId() != null) {
				error = validateEmailid(advisorRequest.getEmailId());
				if (error.isEmpty() == false) {
					allErrors.put("EMAILID", error);
				}
			}
			if (advisorRequest != null && advisorRequest.getPanNumber() != null) {
				error = validatePan(advisorRequest.getPanNumber());
				if (error.isEmpty() == false) {
					allErrors.put("PAN", error);
				}
			}
			if (advisorRequest != null && advisorRequest.getPhoneNumber() != null) {
				error = validatePhoneNumber(advisorRequest.getPhoneNumber());
				if (error.isEmpty() == false) {
					allErrors.put("PHONENUMBER", error);
				}
			}
			if (advisorRequest != null && advisorRequest.getPassword() != null) {
				error = validatePassword(advisorRequest.getPassword());
				if (error.isEmpty() == false) {
					allErrors.put("PASSWORD", error);
				}
			}

			// ****investor***//

			if (advisorRequest != null && advisorRequest.getFullName() != null) {
				error = validateFullName(advisorRequest.getFullName());// validate name//
				if (error.isEmpty() == false) {
					allErrors.put("FULLNAME", error);
				}
			}

			if (advisorRequest != null && advisorRequest.getDob() != null) {
				error = validateDob(advisorRequest.getDob()); // validate dob//
				if (error.isEmpty() == false) {
					allErrors.put("DOB", error);
				}
			}
			if (advisorRequest != null && advisorRequest.getGender() != null) {
				error = validateGender(advisorRequest.getGender()); // validate gender//
				if (error.isEmpty() == false) {
					allErrors.put("GENDER", error);
				}
			}
			if (advisorRequest != null && advisorRequest.getPincode() != null) {
				error = validatePincode(advisorRequest.getPincode()); // validate pincode//
				if (error.isEmpty() == false) {
					allErrors.put("PINCODE", error);
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

	// PAN number validation
	protected HashMap<String, String> validatePan(String pan) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PAN;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.validPan(pan, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error1);
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

	// VALIDATION OF PASSWORD

	protected HashMap<String, String> validatePassword(String password) {
		String inputParamPasswrd = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PASSWRD;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(password, inputParamPasswrd);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.validPasswordCheck(password, inputParamPasswrd);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;
	}

	// ****investor****//
	// VALIDATION OF NAME
	protected HashMap<String, String> validateFullName(String name) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.FULLNAME;
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
