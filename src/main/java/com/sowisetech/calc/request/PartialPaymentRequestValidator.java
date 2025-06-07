package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class PartialPaymentRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(PartialPaymentRequest partialPaymentRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (partialPaymentRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (partialPaymentRequest != null && partialPaymentRequest.getLoanAmount() != null) {
				error = validateLoanAmount(partialPaymentRequest.getLoanAmount());
				if (error.isEmpty() == false) {
					allErrors.put("LOAN_AMOUNT", error);
				}
			}
			if (partialPaymentRequest != null && partialPaymentRequest.getInterestRate() != null) {
				error = validateInterestRate(partialPaymentRequest.getInterestRate());
				if (error.isEmpty() == false) {
					allErrors.put("INTEREST_RATE", error);
				}
			}
			if (partialPaymentRequest != null && partialPaymentRequest.getTenure() != null) {
				error = validateTenure(partialPaymentRequest.getTenure());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE", error);
				}
			}
			if (partialPaymentRequest != null && partialPaymentRequest.getTenureType() != null) {
				error = validateTenureType(partialPaymentRequest.getTenureType());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE_TYPE", error);
				}
			}
			if (partialPaymentRequest != null && partialPaymentRequest.getLoanDate() != null) {
				error = validateLoanDate(partialPaymentRequest.getLoanDate());
				if (error.isEmpty() == false) {
					allErrors.put("LOAN_DATE", error);
				}
			}
			if (partialPaymentRequest.getPartialPaymentReq() != null
					&& partialPaymentRequest.getPartialPaymentReq().size() != 0) {
				for (PartialPaymentReq partialPaymentReq : partialPaymentRequest.getPartialPaymentReq()) {
					if (partialPaymentReq != null && partialPaymentReq.getPartPayDate() != null) {
						error = validatePartPayDate(partialPaymentReq.getPartPayDate());
						if (error.isEmpty() == false) {
							allErrors.put("PART_PAY_DATE", error);
						}
					}
					if (partialPaymentReq != null && partialPaymentReq.getPartPayAmount() != null) {
						error = validatePartPayAmount(partialPaymentReq.getPartPayAmount());
						if (error.isEmpty() == false) {
							allErrors.put("PART_PAY_AMOUNT", error);
						}
					}
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

	protected HashMap<String, String> validatePartPayAmount(String partPayAmount) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.PARTPAYAMOUNT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(partPayAmount, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(partPayAmount, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateLoanDate(String loanDate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.LOANDATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(loanDate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.monthYearCheck(loanDate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("DATE_FORMAT", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validatePartPayDate(String partPayDate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.PARTPAYDATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(partPayDate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.monthYearCheck(partPayDate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("DATE_FORMAT", error2);
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

	protected HashMap<String, String> validateLoanAmount(String loanAmt) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.LOANAMOUNT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(loanAmt, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(loanAmt, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}
}
