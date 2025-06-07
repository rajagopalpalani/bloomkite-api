package com.sowisetech.calc.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;
import com.sowisetech.calc.util.CalcCommon;

@Component
public class GoalRequestValidator {

	@Autowired
	CalcAppMessages appmessages;

	@Autowired
	CalcCommon common;

	public HashMap<String, HashMap<String, String>> validate(GoalRequest goalRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appmessages.getValue_empty());
		if (goalRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (goalRequest != null && goalRequest.getGoalName() != null) {
				error = validateGoalName(goalRequest.getGoalName());
				if (error.isEmpty() == false) {
					allErrors.put("GOALNAME", error);
				}
			}
			if (goalRequest != null && goalRequest.getTenure() != null) {
				error = validateTenure(goalRequest.getTenure());
				if (error.isEmpty() == false) {
					allErrors.put("TENURE", error);
				}
			}
			if (goalRequest != null && goalRequest.getTenureType() != null) {
				error = validateTenureType(goalRequest.getTenureType());
				if (error.isEmpty() == false) {
					allErrors.put("TENURETYPE", error);
				}
			}
			if (goalRequest != null && goalRequest.getGoalAmount() != null) {
				error = validateGoalAmount(goalRequest.getGoalAmount());
				if (error.isEmpty() == false) {
					allErrors.put("GOALAMOUNT", error);
				}
			}
			if (goalRequest != null && goalRequest.getInflationRate() != null) {
				error = validateInflationRate(goalRequest.getInflationRate());
				if (error.isEmpty() == false) {
					allErrors.put("INFLATIONRATE", error);
				}
			}
			if (goalRequest != null && goalRequest.getCurrentAmount() != null) {
				error = validateCurrentAmount(goalRequest.getCurrentAmount());
				if (error.isEmpty() == false) {
					allErrors.put("CURRENTAMOUNT", error);
				}
			}
			if (goalRequest != null && goalRequest.getGrowthRate() != null) {
				error = validateGrowthRate(goalRequest.getGrowthRate());
				if (error.isEmpty() == false) {
					allErrors.put("GROWTHRATE", error);
				}
			}
			if (goalRequest != null && goalRequest.getAnnualInvestmentRate() != null) {
				error = validateAnnualInvestmentRate(goalRequest.getAnnualInvestmentRate());
				if (error.isEmpty() == false) {
					allErrors.put("ANNUALINVESTMENTRATE", error);
				}
			}
		}

		return allErrors;

	}

	protected HashMap<String, String> validateAnnualInvestmentRate(String annualInvestmentRate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.ANNUALINVESTMENTRATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(annualInvestmentRate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(annualInvestmentRate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateGrowthRate(String growthRate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.GROWTHRATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(growthRate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(growthRate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateCurrentAmount(String currentAmount) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.CURRENTAMOUNT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(currentAmount, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(currentAmount, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateInflationRate(String inflationRate) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.INFLATIONRATE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(inflationRate, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(inflationRate, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
	}

	protected HashMap<String, String> validateGoalAmount(String goalAmount) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.GOALAMOUNT;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(goalAmount, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isValidDoubleNumber(goalAmount, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_NUMERIC", error2);
		}
		return errors;
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

	protected HashMap<String, String> validateGoalName(String goalName) {
		String inputParamName = CalcConstants.SPACE_WTIH_COLON + CalcConstants.GOALNAME;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(goalName, inputParamName);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.isAlphaNumericValues(goalName, inputParamName);
		if (error2.isEmpty() == false) {
			errors.put("NON_ALPHA", error2);
		}
		return errors;
	}

}
