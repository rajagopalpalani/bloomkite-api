package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class RateFinderRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(RateFinderRequest rateFinderRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (rateFinderRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (rateFinderRequest != null && rateFinderRequest.getInvType() != null) {
				error = validateInvType(rateFinderRequest.getInvType());
				if (error.isEmpty() == false) {
					allErrors.put("INVESTMENT_TYPE", error);
				}
			}
			if (rateFinderRequest != null && rateFinderRequest.getPresentValue() != null) {
				error = validatePresentValue(rateFinderRequest.getPresentValue());
				if (error.isEmpty() == false) {
					allErrors.put("PRESENTVALUE", error);
				}
			}
			if (rateFinderRequest != null && rateFinderRequest.getDuration() != null) {
				error = validateDuration(rateFinderRequest.getDuration());
				if (error.isEmpty() == false) {
					allErrors.put("DURATION", error);
				}
			}
			if (rateFinderRequest != null && rateFinderRequest.getDurationType() != null) {
				error = validateDurationType(rateFinderRequest.getDurationType());
				if (error.isEmpty() == false) {
					allErrors.put("DURATION_TYPE", error);
				}
			}
			if (rateFinderRequest != null && rateFinderRequest.getFutureValue() != null) {
				error = validateFutureValue(rateFinderRequest.getFutureValue());
				if (error.isEmpty() == false) {
					allErrors.put("FUTUREVALUE", error);
				}
			}
		}

		return allErrors;
	}

	protected HashMap<String, String> validateDurationType(String durationType) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.TENURETYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(durationType, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.tenureTypeCheck(durationType, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("TENURETYPE_ERROR", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateInvType(String invType) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.INV_TYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(invType, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidInvestmentType(invType, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NOT_VALID", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateFutureValue(String futureValue) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.FUTUREVALUE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(futureValue, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(futureValue, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateDuration(String duration) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.DURATION;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(duration, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isNumericValues(duration, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validatePresentValue(String presentValue) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.PRESENTVALUE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(presentValue, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(presentValue, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

}
