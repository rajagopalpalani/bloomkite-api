package com.sowisetech.advisor.util;

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
import com.sowisetech.advisor.request.AdvProfessionalInfoRequest;
import com.sowisetech.advisor.request.AdvisorRequest;

@Component
public class AdvCommon {

	@Autowired
	AdvAppMessages appMessages;

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
	public AdvisorRequest getJsonRequest(String requestStr, AdvisorRequest jsonRequest)
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
		SimpleDateFormat dateFormat = new SimpleDateFormat(AdvisorConstants.DEFAULT_DATE_PATTERN);
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
	 */
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

	public String nonEmptyCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isEmpty(inputParam)) {
			errorMsg = appMessages.getValue_null_or_empty() + inputParamName;
		}
		return errorMsg;
	}

	public String isAlpha(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isAlphaSpace(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_alpha() + inputParamName;
		}
		return errorMsg;
	}

	public String designationCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		String dateRegex = "^[A-Za-z-& ]*$";
		if (inputParam.matches(dateRegex) == false) {
			errorMsg = appMessages.getCity_error() + inputParamName;
		}
		return errorMsg;
	}

	public String isAlphaNumericSpace(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isAlphanumericSpace(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_alphaNumeric() + inputParamName;
		}
		return errorMsg;
	}

	public String isAlphaNumeric(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isAlphanumeric(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_alphaNumeric() + inputParamName;
		}
		return errorMsg;
	}

	public String isValidEmailAddress(String inputParam, String inputParamMailid) {
		String errorMsg = "";

		String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

		if (inputParam.matches(EMAIL_REGEX) == false) {
			errorMsg = appMessages.getValue_is_not_emailid() + inputParamMailid;
		}
		return errorMsg;

	}

	public String isNumericValues(String inputParam, String inputParamNumbers) {
		String errorMsg = "";
		if (StringUtils.isNumeric(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_numeric() + inputParamNumbers;
		}
		return errorMsg;
	}

	public String phoneNumberlengthCheck(String inputParam, String inputParamPhoneNumber) {
		String errorMsg = "";
		if (inputParam.length() > 10 || inputParam.length() < 10) {
			errorMsg = appMessages.getPhonenumber_length_error() + inputParamPhoneNumber;
		}
		return errorMsg;
	}

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

	public String pincodelengthCheck(String inputParam, String inputParamPincode) {
		String errorMsg = "";
		if (inputParam.length() > 6 || inputParam.length() < 6) {
			errorMsg = appMessages.getPincode_length_error() + inputParamPincode;
		}
		return errorMsg;
	}

	public String dobCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		String dateRegex = "^[0-1][0-9]-[0-3][0-9]-[0-9]{4}$";
		if (inputParam.matches(dateRegex) == false) {
			errorMsg = appMessages.getDob_error() + inputParamName;

		}

		return errorMsg;
	}

	public String genderCheck(String inputParam, String inputParamName) {

		String errorMsg = "";
		String genderRegex = "^(?:m|M|f|F|o|O)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getGender_error() + inputParamName;

		}
		return errorMsg;

	}

	// public static AdvAdditionalInfoRequest getJsonRequest(String requestStr,
	// AdvAdditionalInfoRequest jsonRequest)
	// throws JsonParseException, JsonMappingException, IOException {
	// if (StringUtils.isEmpty(requestStr)) {
	// throw new IllegalArgumentException(AppMessages.JSON_REQUEST_ERROR);
	// }
	// jsonRequest = new ObjectMapper().readValue(requestStr,
	// jsonRequest.getClass());
	// return jsonRequest;
	// }
	public String validPan(String inputParam, String inputParamName) {

		String errorMsg = "";
		// In PAN number first 5 digits are letters, next 4 digits are numbers and last
		// digit is letter
		String panRegex = "[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}";
		if (inputParam.matches(panRegex) == false) {
			errorMsg = appMessages.getPan_error() + inputParamName;
		}
		return errorMsg;

	}

	public String allowMultipleText(String inputParam, String inputParamName) {

		String errorMsg = "";
		// It allow multiple text values which is seperated by comma
		String regex = "^[A-Za-z ]+((,)[A-Za-z ]+)*[A-Za-z ]+$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getText_format_error() + inputParamName;
		}
		return errorMsg;

	}

	public String isValidExpertiseLevel(String inputParam, String inputParamName) {

		String errorMsg = "";
		// Expertise level must be any of these numbers 1(low) or 2(medium) or 3(high)
		String regex = "^(1|2|3)$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getExpertise_level_error() + inputParamName;
		}
		return errorMsg;
	}

	public String isValidRemuneration(String inputParam, String inputParamName) {

		String errorMsg = "";
		// remuneration must be any one of these values fee or commision
		String regex = "^(Fee|Commision|fee|commision)$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getRemuneration_error() + inputParamName;
		}
		return errorMsg;
	}

	public String isValidDate(String inputParam, String inputParamName) {

		String errorMsg = "";
		// Valid till must be a valid date in a format of mm-dd-yyyy
		String regex = "^([0]?[1-9]|[1][0-2])[-]([0]?[1-9]|[1|2][0-9]|[3][0|1])[-]([0-9]{4})$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getValidtill_date_error() + inputParamName;
		}
		return errorMsg;
	}

	public String allowMultipleLegalNo(String inputParam, String inputParamName) {
		String errorMsg = "";
		// legal number may contains alphabets and it allow us to add multiple values
		String regex = "^[A-Za-z0-9]+((,)[A-Za-z0-9]+)*[A-Za-z0-9]+$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getLegalno_error() + inputParamName;
		}
		return errorMsg;
	}

	public AdvProfessionalInfoRequest getJsonRequest(String requestStr, AdvProfessionalInfoRequest jsonRequest)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(requestStr)) {
			throw new IllegalArgumentException(appMessages.getJson_request_error());
		}
		jsonRequest = new ObjectMapper().readValue(requestStr, jsonRequest.getClass());
		return jsonRequest;
	}

	public String validateAboutme(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value should not exceed 300 characters
		String regex = "^[A-Za-z0-9.,/?<>!@#${}+%^&*()_';: / -]{0,300}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getAboutme_error() + inputParamName;
		}
		return errorMsg;

	}

	public String isYear(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value must be in a format yyyy eg:1996
		String regex = "^\\d{4}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getYear_error() + inputParamName;
		}
		return errorMsg;
	}

	public String monthYearCheck(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value must be in a format mm-yyyy eg:1996
		String regex = "^[0-1][0-9]-[0-9]{4}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getCertificate_year_error() + inputParamName;
		}
		return errorMsg;
	}

	public String FromToCheck(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value must be in a format mm-yyyy to mm-yyyy eg:02-1998 to 12-2000
		String regex = "^[0-1][0-9]-[0-9]{4} to [0-1][0-9]-[0-9]{4}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getFrom_to_error() + inputParamName;
		}
		return errorMsg;
	}

	public String isImage(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value must be a image path with the extension like .jpg , .png , .gif , .bmp
		String regex = "([^\\s|^\\s ]+(\\.(?i)(jpg|png|gif|bmp))$)";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getValue_is_not_image() + inputParamName;
		}
		return errorMsg;
	}

	public String degreeCheck(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value must be valid degree like B.Com or Bachelor of Commerce
		String regex = "^[A-Za-z. ]+$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getDegree_error() + inputParamName;
		}
		return errorMsg;
	}

	public String fromDateCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		// from date must be in a format Mon-yyy eg: Jan-1996
		// String regex = "^[A-Z]{1}[a-z]{2} [0-9]{4}-[A-Z]{1}[a-z]{2}
		// [0-9]{4}|[A-Z]{1}[a-z]{2} [0-9]{4}-present|[A-Z]{1}[a-z]{2}
		// [0-9]{4}-Present$";
		String regex = "^[A-Z]{3}-[0-9]{4}|[a-z]{3}-[0-9]{4}|[A-Z]{1}[a-z]{2}-[0-9]{4}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getFrom_date_error() + inputParamName;
		}
		return errorMsg;
	}

	public String toDateCheck(String inputParam, String inputParamName) {

		String errorMsg = "";
		// To date must be in a format Mon-yyy eg: Jan-1996
		String regex = "^[A-Z]{1}[a-z]{2}-[0-9]{4}|present|Present$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getTo_date_error() + inputParamName;
		}
		return errorMsg;
	}

	public String isVideo(String inputParam, String inputParamName) {
		String errorMsg = "";
		// value must be a video path with the extension like .mp4 , .wmv , .avi, .mov
		String regex = "([^\\s|^\\s ]+(\\.(?i)(mp4|avi|wmv|mov))$)";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getValue_is_not_video() + inputParamName;
		}
		return errorMsg;
	}

	public String validateCity(String inputParam, String inputParamName) {

		String errorMsg = "";
		// value should not exceed 300 characters
		String regex = "^[A-Za-z.,/?<>!@#${}+%^&*()_';: /-]{0,100}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getCity_error() + inputParamName;
		}
		return errorMsg;

	}

}
