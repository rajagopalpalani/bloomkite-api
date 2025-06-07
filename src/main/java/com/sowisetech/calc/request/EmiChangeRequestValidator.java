package com.sowisetech.calc.request;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class EmiChangeRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(EmiChangeRequest emiChangeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (emiChangeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (emiChangeRequest.getEmiChangeReq() != null && emiChangeRequest.getEmiChangeReq().size() != 0) {
				for (EmiChangeReq emiChangeReq : emiChangeRequest.getEmiChangeReq()) {
					if (emiChangeReq != null && emiChangeReq.getIncreasedEmi() != null) {
						error = validateIncreasedEmi(emiChangeReq.getIncreasedEmi());
						if (error.isEmpty() == false) {
							allErrors.put("INCREASED_EMI", error);
						}
					}
					if (emiChangeReq != null && emiChangeReq.getEmiChangedDate() != null) {
						error = validateEmiChangedDate(emiChangeReq.getEmiChangedDate());
						if (error.isEmpty() == false) {
							allErrors.put("EMI_CHANGED_DATE", error);
						}
					}

				}
			}

			if (emiChangeRequest != null && emiChangeRequest.getLoanAmount() != null) {
				error = validateLoanAmount(emiChangeRequest.getLoanAmount());
				if (error.isEmpty() == false) {
					allErrors.put("LOANAMOUNT", error);
				}
			}
			if (emiChangeRequest != null && emiChangeRequest.getInterestRate() != null) {
				error = validateInterestRate(emiChangeRequest.getInterestRate());
				if (error.isEmpty() == false) {
					allErrors.put("INTEREST_RATE", error);
				}
			}
			if (emiChangeRequest != null && emiChangeRequest.getTenure() != null) {
				error = validateTenure(emiChangeRequest.getTenure());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE", error);
				}
			}
			if (emiChangeRequest != null && emiChangeRequest.getTenureType() != null) {
				error = validateTenureType(emiChangeRequest.getTenureType());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE_TYPE", error);
				}
			}
			if (emiChangeRequest != null && emiChangeRequest.getLoanDate() != null) {
				error = validateLoanDate(emiChangeRequest.getLoanDate());
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

	protected HashMap<String, String> validateEmiChangedDate(String emiChangedDate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.EMICHANGEDDATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(emiChangedDate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.monthYearCheck(emiChangedDate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("DATE_FORMAT", error2);
		}
		return errors;
	}

}
