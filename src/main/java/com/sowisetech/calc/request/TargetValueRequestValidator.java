package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class TargetValueRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(TargetValueRequest targetValueRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (targetValueRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (targetValueRequest != null && targetValueRequest.getInvType() != null) {
				error = validateInvType(targetValueRequest.getInvType());
				if (error.isEmpty() == false) {
					allErrors.put("INVESTMENT_TYPE", error);
				}
			}
			if (targetValueRequest != null && targetValueRequest.getFutureValue() != null) {
				error = validateFutureValue(targetValueRequest.getFutureValue());
				if (error.isEmpty() == false) {
					allErrors.put("FUTUREVALUE", error);
				}
			}
			if (targetValueRequest != null && targetValueRequest.getDuration() != null) {
				error = validateDuration(targetValueRequest.getDuration());
				if (error.isEmpty() == false) {
					allErrors.put("DURATION", error);
				}
			}
			if (targetValueRequest != null && targetValueRequest.getDurationType() != null) {
				error = validateDurationType(targetValueRequest.getDurationType());
				if (error.isEmpty() == false) {
					allErrors.put("DURATION_TYPE", error);
				}
			}
			if (targetValueRequest != null && targetValueRequest.getRateOfInterest() != null) {
				error = validateRateOfInterest(targetValueRequest.getRateOfInterest());
				if (error.isEmpty() == false) {
					allErrors.put("RATE_OF_INTEREST", error);
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

	protected HashMap<String, String> validateRateOfInterest(String rateOfInterest) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.RATE_OF_INTEREST;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(rateOfInterest, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(rateOfInterest, inputParamName);
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
}
