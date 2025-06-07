package com.sowisetech.investor.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.investor.util.InvAppMessages;
import com.sowisetech.investor.util.InvCommon;
import com.sowisetech.investor.util.InvestorConstants;

@Component
public class InvestorRequestValidator {

	/**
	 * Basic validation on HomeLoanRequest input values.
	 * 
	 * @param homeLoanRequest
	 * @return - Empty list or list of errors in string format.
	 */
	@Autowired(required = true)
	InvAppMessages appMessages;

	@Autowired(required = true)
	InvCommon common;

	public HashMap<String, HashMap<String, String>> validate(InvestorRequest investorRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getInvestor_detail_empty());
		if (investorRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (investorRequest != null && investorRequest.getFullName() != null) {
				error = validateFullName(investorRequest.getFullName());// validate name//
				if (error.isEmpty() == false) {
					allErrors.put("FULLNAME", error);
				}
			}
			// if (investorRequest != null && investorRequest.getDisplayName() != null) {
			// error=validateDisplayName(investorRequest.getDisplayName());
			// if(error.isEmpty()==false) {
			// allErrors.put("DISPLAYNAME",error);}
			// }
			if (investorRequest != null && investorRequest.getDob() != null) {
				error = validateDob(investorRequest.getDob()); // validate dob//
				if (error.isEmpty() == false) {
					allErrors.put("DOB", error);
				}
			}
			if (investorRequest != null && investorRequest.getGender() != null) {
				error = validateGender(investorRequest.getGender()); // validate gender//
				if (error.isEmpty() == false) {
					allErrors.put("GENDER", error);
				}
			}
//			if (investorRequest != null && investorRequest.getEmailId() != null) {
//				error = validateEmailId(investorRequest.getEmailId()); // validate emailId//
//				if (error.isEmpty() == false) {
//					allErrors.put("EMAILID", error);
//				}
//			}
//			if (investorRequest != null && investorRequest.getPhoneNumber() != null) {
//				error = validatePhoneNumber(investorRequest.getPhoneNumber());// validate phone number//
//				if (error.isEmpty() == false) {
//					allErrors.put("PHONENUMBER", error);
//				}
//			}
//			if (investorRequest != null && investorRequest.getPassword() != null) {
//				error = validatePassword(investorRequest.getPassword()); // validate password//
//				if (error.isEmpty() == false) {
//					allErrors.put("PASSWORD", error);
//				}
//			}
			if (investorRequest != null && investorRequest.getPincode() != null) {
				error = validatePincode(investorRequest.getPincode()); // validate pincode//
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
	//
	// //PAN number validation
	// protected List<String> validatePan(String pan) {
	// String inputParamName = InvestorConstants.SPACE_WTIH_COLON +
	// InvestorConstants.PAN;
	// List<String> errors = new ArrayList<>();
	// errors.add(Common.validPan(pan, inputParamName));
	// return errors;
	// }

	// VALIDATION OF NAME
	protected HashMap<String, String> validateFullName(String name) {
		String inputParamName = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.FULLNAME;
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

	// // VALIDATION OF DISPLAYNAME
	// protected HashMap<String, String> validateDisplayName(String displayname) {
	// String inputParamDisplayName = InvestorConstants.SPACE_WTIH_COLON +
	// InvestorConstants.DISPLAYNAME;
	// HashMap<String,String> errors = new HashMap<String,String>();
	// if(Common.nonEmptyCheck(displayname, inputParamDisplayName).isEmpty()==false)
	// {
	// errors.put("EMPTY",Common.nonEmptyCheck(displayname,
	// inputParamDisplayName));}
	// if(Common.isAlpha(displayname, inputParamDisplayName).isEmpty()==false) {
	// errors.put("NON_ALPHA",Common.isAlpha(displayname, inputParamDisplayName));}
	// return errors;
	// }

	// VALIDATION OF DOB
	protected HashMap<String, String> validateDob(String dob) {
		String inputParamDob = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.DOB;
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
		String inputParamGender = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.GENDER;
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

	// VALIDATION OF EMAILID
	protected HashMap<String, String> validateEmailId(String mailid) {
		String inputParamMailid = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.EMAILID;
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
		String inputParamPhoneNumber = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.PHONENUMBER;
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
		String inputParamPasswrd = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.PASSWRD;
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

	// VALIDATION OF PINCODE
	protected HashMap<String, String> validatePincode(String pincode) {
		String inputParamPincode = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.PINCODE;
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
