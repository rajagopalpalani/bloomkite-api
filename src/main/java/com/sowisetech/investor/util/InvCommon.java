package com.sowisetech.investor.util;

import java.io.IOException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.investor.request.InvestorRequest;

@Component
public class InvCommon {

	@Autowired
	InvAppMessages appMessages;

	/**
	 * To convert input string format http body request to
	 * <code>HomeLoanRequest</code>
	 * 
	 * @param requestStr
	 * @param jsonRequest
	 * @return HomeLoanRequest
	 * @throws JsonParseException
	 *             - if the input JSON structure does not match structure expected
	 *             for result type (or has other mismatch issues).
	 * @throws JsonMappingException
	 *             - if underlying input contains invalid content of type JsonParser
	 *             supports.
	 * @throws IOException
	 *             - - if an I/O error occurs.
	 */
	public InvestorRequest getJsonRequest(String requestStr, InvestorRequest jsonRequest)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(requestStr)) {
			throw new IllegalArgumentException(appMessages.getJson_request_error());
		}
		jsonRequest = new ObjectMapper().readValue(requestStr, jsonRequest.getClass());
		return jsonRequest;
	}

	/**
	 * @return - system current date in String ("yyyy-mm-dd") format.
	 */

	public String getSystemDateAsString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(InvestorConstants.DEFAULT_DATE_PATTERN);
		return dateFormat.format(new Date());
	}

	/**
	 * To check the input value is positive number and range greater than zero and
	 * less than or equal to hundred.
	 * 
	 * @param inputParam
	 *            - input value to be checked.
	 * @param inputParamName
	 *            - input field name.
	 * @return - Empty string or error in string format.
	 * 
	 */
	// Pan number check//
	public String validPan(String inputParam, String inputParamName) {

		String errorMsg = "";
		// In PAN number first 5 digits are letters, next 4 digits are numbers and last
		// digit is letter
		String panRegex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
		if (inputParam.matches(panRegex) == false) {
			errorMsg = appMessages.getPan_error() + inputParamName;
		}
		return errorMsg;

	}

	// percent check//
	public String percentNumberCheck(String inputParam, String inputParamName) {
		BigDecimal inputParamNum = null;
		String errorMsg = positiveNumberCheck(inputParam, inputParamName);
		if (StringUtils.isEmpty(errorMsg) == false) {
			return errorMsg;
		} else {
			inputParamNum = new BigDecimal(inputParam);
			BigDecimal hunderd = new BigDecimal("100");
			if ((inputParamNum.compareTo(BigDecimal.ZERO) < 1) || (inputParamNum.compareTo(hunderd)) == 1) {
				errorMsg = appMessages.getValue_invalid_percent() + inputParamName;
			}
		}
		return errorMsg;
	}

	/**
	 * To check input value is numeric and greater than zero.
	 * 
	 * @param inputParam
	 *            - input value to be checked.
	 * @param inputParamName
	 *            - input field name
	 * @return - Empty string or error in string format.
	 */
	// positive number check//
	public String positiveNumberCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		BigDecimal inputParamNum = null;
		if (StringUtils.isEmpty(inputParam)) {
			errorMsg = appMessages.getValue_null_or_empty() + inputParamName;
		} else if (NumberUtils.isCreatable(inputParam) == false) {
			errorMsg = appMessages.getValue_not_number() + inputParamName;
		} else {
			inputParamNum = new BigDecimal(inputParam);
			if (inputParamNum.compareTo(BigDecimal.ZERO) < 1) {
				errorMsg = appMessages.getValue_not_positive() + inputParamName;
			}
		}
		return errorMsg;
	}

	// Non empty check//
	public String nonEmptyCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isEmpty(inputParam)) {
			errorMsg = appMessages.getValue_null_or_empty() + inputParamName;
		}
		return errorMsg;
	}

	// Alpha check//
	public String isAlpha(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isAlphaSpace(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_alpha() + inputParamName;
		}
		return errorMsg;
	}

	// Email address check//
	public String isValidEmailAddress(String inputParam, String inputParamMailid) {
		String errorMsg = "";

		String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

		if (inputParam.matches(EMAIL_REGEX) == false) {
			errorMsg = appMessages.getValue_is_not_emailid() + inputParamMailid;
		}
		return errorMsg;

	}

	// Numeric value check//
	public String isNumericValues(String inputParam, String inputParamNumbers) {
		String errorMsg = "";
		if (StringUtils.isNumeric(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_numeric() + inputParamNumbers;
		}
		return errorMsg;
	}

	// phone number length check//
	public String phoneNumberlengthCheck(String inputParam, String inputParamPhoneNumber) {
		String errorMsg = "";
		if (inputParam.length() > 10 || inputParam.length() < 10) {
			errorMsg = appMessages.getPhonenumber_length_error() + inputParamPhoneNumber;
		}
		return errorMsg;
	}

	// password format check//
	public String validPasswordCheck(String inputParam, String inputParamPassword) {
		String errorMsg = "";
		// Password must have One special character,one number and one capital letter.
		// length:min=8,max=16
		String PASSWRD_REGEX = "((?=.*\\d)(?=.*[A-Z])(?=.*[@!^&*(+_?)#$%]).{8,16})";

		if (inputParam.matches(PASSWRD_REGEX) == false) {
			errorMsg = appMessages.getPassword_format_error() + inputParamPassword;
		}
		return errorMsg;

	}

	// pincode length check//
	public String pincodelengthCheck(String inputParam, String inputParamPincode) {
		String errorMsg = "";
		if (inputParam.length() > 6 || inputParam.length() < 6) {
			errorMsg = appMessages.getPincode_length_error() + inputParamPincode;
		}
		return errorMsg;
	}

	// DOB check//
	//date format in mm-dd-yyyy//
	public String dobCheck(String inputParam, String inputParamDob) {
		String errorMsg = "";
//		String dateRegex = "^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$";
		String dateRegex = "^[0-1][0-9]-[0-3][0-9]-[0-9]{4}$";
		if (inputParam.matches(dateRegex) == false) {
			errorMsg = appMessages.getDob_error() + inputParamDob;

		}

		return errorMsg;
	}

	// gender check//
	public String genderCheck(String inputParam, String inputParamGender) {

		String errorMsg = "";
		String genderRegex = "^(?:m|M|f|F|o|O)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getGender_error() + inputParamGender;

		}
		return errorMsg;

	}

	// scale check//
	public String scaleCheck(String inputParam, String inputParamScale) {
		String errorMsg = "";
		String scaleRegex = "^(?:1|2|3)$";
		if (inputParam.matches(scaleRegex) == false) {
			errorMsg = appMessages.getScale_error() + inputParamScale;

		}
		return errorMsg;
	}

}
