package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class InterestChangeRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(InterestChangeRequest interestChangeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (interestChangeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (interestChangeRequest.getInterestChangeReq() != null
					&& interestChangeRequest.getInterestChangeReq().size() != 0) {
				for (InterestChangeReq interestChangeReq : interestChangeRequest.getInterestChangeReq()) {
					if (interestChangeReq != null && interestChangeReq.getChangedRate() != null) {
						error = validateChangedRate(interestChangeReq.getChangedRate());
						if (error.isEmpty() == false) {
							allErrors.put("CHANGED_RATE", error);
						}
					}
					if (interestChangeReq != null && interestChangeReq.getInterestChangedDate() != null) {
						error = validateInterestChangedDate(interestChangeReq.getInterestChangedDate());
						if (error.isEmpty() == false) {
							allErrors.put("INTEREST_CHANGED_DATE", error);
						}
					}
				}
			}
			if (interestChangeRequest != null && interestChangeRequest.getLoanAmount() != null) {
				error = validateLoanAmount(interestChangeRequest.getLoanAmount());
				if (error.isEmpty() == false) {
					allErrors.put("LOAN_AMOUNT", error);
				}
			}
			if (interestChangeRequest != null && interestChangeRequest.getInterestRate() != null) {
				error = validateInterestRate(interestChangeRequest.getInterestRate());
				if (error.isEmpty() == false) {
					allErrors.put("INTEREST_RATE", error);
				}
			}
			if (interestChangeRequest != null && interestChangeRequest.getTenure() != null) {
				error = validateTenure(interestChangeRequest.getTenure());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE", error);
				}
			}
			if (interestChangeRequest != null && interestChangeRequest.getTenureType() != null) {
				error = validateTenureType(interestChangeRequest.getTenureType());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE_TYPE", error);
				}
			}
			if (interestChangeRequest != null && interestChangeRequest.getLoanDate() != null) {
				error = validateLoanDate(interestChangeRequest.getLoanDate());
				if (error.isEmpty() == false) {
					allErrors.put("LOAN_DATE", error);
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

	protected HashMap<String, String> validateChangedRate(String changedRate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.CHANGEDRATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(changedRate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(changedRate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateInterestChangedDate(String interestChangedDate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.INTERESTCHANGEDDATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(interestChangedDate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.monthYearCheck(interestChangedDate, inputParamName);
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
}
