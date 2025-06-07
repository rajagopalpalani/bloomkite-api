package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class PresentValueAnnuityDueReqValidator {
	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(
			PresentValueAnnuityDueRequest presentValueAnnuityDueRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (presentValueAnnuityDueRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (presentValueAnnuityDueRequest != null && presentValueAnnuityDueRequest.getPeriodicAmount() != null) {
				error = validatePeriodicAmount(presentValueAnnuityDueRequest.getPeriodicAmount());
				if (error.isEmpty() == false) {
					allErrors.put("PERIODICAMOUNT", error);
				}
			}
			if (presentValueAnnuityDueRequest != null && presentValueAnnuityDueRequest.getAnnualGrowthRate() != null) {
				error = validateAnnualGrowthRate(presentValueAnnuityDueRequest.getAnnualGrowthRate());
				if (error.isEmpty() == false) {
					allErrors.put("ANNUALGROWTH", error);
				}
			}
		}
		if (presentValueAnnuityDueRequest != null && presentValueAnnuityDueRequest.getDuration() != null) {
			error = validateDuration(presentValueAnnuityDueRequest.getDuration());
			if (error.isEmpty() == false) {
				allErrors.put("DURATION", error);
			}
		}
		return allErrors;

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

	protected HashMap<String, String> validateAnnualGrowthRate(String annualGrowthRate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.ANNUALGROWTH;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(annualGrowthRate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(annualGrowthRate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validatePeriodicAmount(String periodicAmount) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.PERIODICAMOUNT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error = common.nonEmptyCheck(periodicAmount, inputParamName);
		if (error.isEmpty() == false) {
			errors.put("EMPTY", error);
		}
		String error2 = common.isValidDoubleNumber(periodicAmount, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}
}
