package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class EmiCalculatorRequestValidator {

	@Autowired
	CalcAppMessages appMessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(EmiCalculatorRequest emiCalculatorRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getValue_empty());
		if (emiCalculatorRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (emiCalculatorRequest != null && emiCalculatorRequest.getLoanAmount() != null) {
				error = validateLoanAmount(emiCalculatorRequest.getLoanAmount());
				if (error.isEmpty() == false) {
					allErrors.put("LOAN_AMOUNT", error);
				}
			}
			if (emiCalculatorRequest != null && emiCalculatorRequest.getTenure() != null) {
				error = validateTenure(emiCalculatorRequest.getTenure());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE", error);
				}
			}
			if (emiCalculatorRequest != null && emiCalculatorRequest.getTenureType() != null) {
				error = validateTenureType(emiCalculatorRequest.getTenureType());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE_TYPE", error);
				}
			}
			if (emiCalculatorRequest != null && emiCalculatorRequest.getInterestRate() != null) {
				error = validateInterestRate(emiCalculatorRequest.getInterestRate());
				if (error.isEmpty() == false) {
					allErrors.put("INTEREST_RATE", error);
				}
			}
			if (emiCalculatorRequest != null && emiCalculatorRequest.getDate() != null) {
				error = validateDate(emiCalculatorRequest.getDate());
				if (error.isEmpty() == false) {
					allErrors.put("DATE", error);
				}
			}
		}
		return allErrors;
	}

	protected HashMap<String, String> validateTenureType(String tenureType) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.TENURETYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(tenureType, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.tenureTypeCheck(tenureType, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("TENURETYPE_ERROR", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateDate(String date) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.DATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(date, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.monthYearCheck(date, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("DATE_FORMAT", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateInterestRate(String interestRate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.INTERESTRATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(interestRate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(interestRate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateTenure(String tenure) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.TENURE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(tenure, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isNumericValues(tenure, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateLoanAmount(String loanAmount) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.LOANAMOUNT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(loanAmount, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(loanAmount, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

}
