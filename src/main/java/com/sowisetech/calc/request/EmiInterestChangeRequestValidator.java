package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class EmiInterestChangeRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(EmiInterestChangeRequest emiInterestChangeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (emiInterestChangeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			for (EmiInterestChangeReq emiInterestChangeReq : emiInterestChangeRequest.getEmiInterestChangeReq()) {
				if (emiInterestChangeReq != null && emiInterestChangeReq.getChangedRate() != null) {
					error = validateChangedRate(emiInterestChangeReq.getChangedRate());
					if (error.isEmpty() == false) {
						allErrors.put("CHANGED_RATE", error);
					}
				}
				if (emiInterestChangeReq != null && emiInterestChangeReq.getChangedDate() != null) {
					error = validateChangedDate(emiInterestChangeReq.getChangedDate());
					if (error.isEmpty() == false) {
						allErrors.put("CHANGED_DATE", error);
					}
				}
				if (emiInterestChangeReq != null && emiInterestChangeReq.getIncreasedEmi() != null) {
					error = validateIncreasedEmi(emiInterestChangeReq.getIncreasedEmi());
					if (error.isEmpty() == false) {
						allErrors.put("INCREASED_EMI", error);
					}
				}
			}
			if (emiInterestChangeRequest != null && emiInterestChangeRequest.getLoanAmount() != null) {
				error = validateLoanAmount(emiInterestChangeRequest.getLoanAmount());
				if (error.isEmpty() == false) {
					allErrors.put("LOAN_AMOUNT", error);
				}
			}
			if (emiInterestChangeRequest != null && emiInterestChangeRequest.getInterestRate() != null) {
				error = validateInterestRate(emiInterestChangeRequest.getInterestRate());
				if (error.isEmpty() == false) {
					allErrors.put("INTEREST_RATE", error);
				}
			}
			if (emiInterestChangeRequest != null && emiInterestChangeRequest.getTenure() != null) {
				error = validateTenure(emiInterestChangeRequest.getTenure());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE", error);
				}
			}
			if (emiInterestChangeRequest != null && emiInterestChangeRequest.getTenureType() != null) {
				error = validateTenureType(emiInterestChangeRequest.getTenureType());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE_TYPE", error);
				}
			}
			if (emiInterestChangeRequest != null && emiInterestChangeRequest.getLoanDate() != null) {
				error = validateLoanDate(emiInterestChangeRequest.getLoanDate());
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

	protected HashMap<String, String> validateIncreasedEmi(String increasedEmi) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.INCREASEDEMI;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(increasedEmi, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(increasedEmi, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
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

	protected HashMap<String, String> validateChangedDate(String changedDate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.CHANGEDDATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(changedDate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.monthYearCheck(changedDate, inputParamName);
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
