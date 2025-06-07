package com.sowisetech.advisor.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.forum.request.BloggerRequest;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;
import com.sowisetech.advisor.util.AdvisorConstants;

@Component
public class BloggerRequestValidator {

	@Autowired(required = true)
	AdvAppMessages appMessages;

	@Autowired(required = true)
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(BloggerRequest bloggerRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getBlogger_detail_empty());
		if (bloggerRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (bloggerRequest != null && bloggerRequest.getFullName() != null) {
				error = validateFullName(bloggerRequest.getFullName());// validate name//
				if (error.isEmpty() == false) {
					allErrors.put("FULLNAME", error);
				}
			}
			if (bloggerRequest != null && bloggerRequest.getDob() != null) {
				error = validateDob(bloggerRequest.getDob()); // validate dob//
				if (error.isEmpty() == false) {
					allErrors.put("DOB", error);
				}
			}
			if (bloggerRequest != null && bloggerRequest.getGender() != null) {
				error = validateGender(bloggerRequest.getGender()); // validate gender//
				if (error.isEmpty() == false) {
					allErrors.put("GENDER", error);
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

//	 VALIDATION OF DISPLAYNAME
//	 protected HashMap<String, String> validateDisplayName(String displayname) {
//		  String inputParamDisplayName = AdvisorConstants.SPACE_WTIH_COLON +
//				  AdvisorConstants.DISPLAYNAME;
//		  HashMap<String,String> errors = new HashMap<String,String>();
//		  if(Common.nonEmptyCheck(displayname, inputParamDisplayName).isEmpty()==false)
//		  {
//			  errors.put("EMPTY",Common.nonEmptyCheck(displayname,
//					  inputParamDisplayName));}
//		  if(Common.isAlpha(displayname, inputParamDisplayName).isEmpty()==false) {
//			  errors.put("NON_ALPHA",Common.isAlpha(displayname, inputParamDisplayName));}
//		  return errors;
//	 	}

//	 VALIDATION OF DOB
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

	// VALIDATION OF EMAILID
	protected HashMap<String, String> validateEmailId(String mailid) {
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

}

