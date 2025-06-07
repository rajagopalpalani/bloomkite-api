package com.sowisetech.calc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.controller.AdvisorController;


@Component
public class CalcCommon {

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

	@Autowired
	CalcAppMessages appMessages;

	private static final Logger logger = LoggerFactory.getLogger(CalcCommon.class);
	
	public String nonEmptyCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isEmpty(inputParam)) {
			errorMsg = appMessages.getValue_empty() + inputParamName;
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

	public String isNumericValues(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isNumeric(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_numeric() + inputParamName;
		}
		return errorMsg;
	}

	public String tenureTypeCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		String genderRegex = "^(?:YEAR|MONTH)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getTenuretype_error() + inputParamName;
		}
		return errorMsg;
	}

	public String dateFormatCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		// Valid till must be a valid date in a format of dd-mm-yyyy
		String regex = "^([0]?[1-9]|[1|2][0-9]|[3][0|1])[-]([0]?[1-9]|[1][0-2])[-]([0-9]{4})$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getDate_error() + inputParamName;
		}
		return errorMsg;
	}

	public String isAlphaNumericValues(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isAlphanumericSpace(inputParam) == false) {
			errorMsg = appMessages.getValue_is_not_alphanumeric() + inputParamName;
		}
		return errorMsg;
	}

	public String isValidDoubleNumber(String inputParam, String inputParamName) {
		String errorMsg = "";

		// allow double and decimal value
		String regex = "^([0-9]+\\.[0-9]+)|([0-9]+)$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getValue_is_not_numeric() + inputParamName;
		}
		return errorMsg;
	}

	public String checkStability(String inputParam, String inputParamName) {
		String errorMsg = "";
		String genderRegex = "^(?:STABLE|FLUCTUATING)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getStability_error() + inputParamName;
		}
		return errorMsg;
	}

	public String checkPredictability(String inputParam, String inputParamName) {
		String errorMsg = "";
		String genderRegex = "^(?:PREDICTABLE|UNPREDICTABLE)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getPredictabilty_error() + inputParamName;
		}
		return errorMsg;
	}

	public String checkCapacityStability(String inputParam, String inputParamName) {
		String errorMsg = "";
		String genderRegex = "^(?:HIGH|MEDIUM)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getCapacity_stability_error() + inputParamName;
		}
		return errorMsg;
	}

	public String checkYesOrNo(String inputParam, String inputParamName) {
		String errorMsg = "";
		String genderRegex = "^(?:YES|NO)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getCapacity_backup_error() + inputParamName;
		}
		return errorMsg;
	}

	public String checkAmountType(String inputParam, String inputParamName) {

		String errorMsg = "";
		String genderRegex = "^(?:La|Cr)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getAmounttype_error() + inputParamName;

		}
		return errorMsg;
	}

	public String monthYearCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		// from date must be in a format Mon-yyyy eg: Jan-1996
		String regex = "^[A-Z]{3}-[0-9]{4}|[a-z]{3}-[0-9]{4}|[A-Z]{1}[a-z]{2}-[0-9]{4}$";
		if (inputParam.matches(regex) == false) {
			errorMsg = appMessages.getMonth_year_error() + inputParamName;
		}
		return errorMsg;
	}

	public String futureDateOrCurrentDateCheck(String inputParam, String inputParamName) {
		String errorMsg = "";
		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String currentDate = formatter.format(date);
		String myFormatString = "dd-MM-yyyy";
		SimpleDateFormat df = new SimpleDateFormat(myFormatString);
		Date givenDate;
		try {
			Date current = df.parse(currentDate);
			givenDate = df.parse(inputParam);
			if (givenDate.after(current) || (givenDate.equals(current))) {
				errorMsg = appMessages.getFuture_date_error() + inputParamName;
				return errorMsg;
			} else {
				return errorMsg;
			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
			return errorMsg;
		}
	}

	public String isValidInvestmentType(String inputParam, String inputParamName) {
		String errorMsg = "";
		String genderRegex = "^(?:LUMSUM|SIP)$";
		if (inputParam.matches(genderRegex) == false) {
			errorMsg = appMessages.getInvestment_type_error() + inputParamName;

		}
		return errorMsg;
	}

	public String validateIfZeroValue(String inputParam, String inputParamName) {
		String errorMsg = "";
		if (StringUtils.isNumeric(inputParam) == true) {
			double value = Double.parseDouble(inputParam);
			if (value < 1) {
				errorMsg = appMessages.getZero_validation_error_single_field() + inputParamName;
			}
		}
		return errorMsg;
	}

}
