package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class InsuranceRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(InsuranceRequest insuranceRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (insuranceRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (insuranceRequest != null && insuranceRequest.getAnnualIncome() != null) {
				error = validateAnnualIncome(insuranceRequest.getAnnualIncome());
				if (error.isEmpty() == false) {
					allErrors.put("ANNUALINCOME", error);
				}
			}
			if (insuranceRequest != null && insuranceRequest.getStability() != null) {
				error = validateStability(insuranceRequest.getStability());
				if (error.isEmpty() == false) {
					allErrors.put("STABILITY", error);
				}
			}
			if (insuranceRequest != null && insuranceRequest.getPredictability() != null) {
				error = validatePredictability(insuranceRequest.getPredictability());
				if (error.isEmpty() == false) {
					allErrors.put("PREDICTABILITY", error);
				}
			}
			if (insuranceRequest != null && insuranceRequest.getExistingInsurance() != null) {
				error = validateExistingInsurance(insuranceRequest.getExistingInsurance());
				if (error.isEmpty() == false) {
					allErrors.put("EXISTINGINSURANCE", error);
				}
			}

		}

		return allErrors;

	}

	protected HashMap<String, String> validateExistingInsurance(String existingInsurance) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.EXISTINGINSURANCE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(existingInsurance, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(existingInsurance, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validatePredictability(String predictability) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.PREDICTABILITY;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(predictability, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.checkPredictability(predictability, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("PREDICTABILITY_ERROR", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateStability(String stability) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.STABILITY;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(stability, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.checkStability(stability, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("STABILITY_ERROR", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateAnnualIncome(String annualIncome) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.ANNUALINCOME;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(annualIncome, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(annualIncome, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}
}
