package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class RiskPortfolioRequestValitator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdmRiskPortfolioRequest admRiskPortfolioRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (admRiskPortfolioRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
//			if (admRiskPortfolioRequest != null && admRiskPortfolioRequest.getPoints() != null) {
//				error = validatePoints(admRiskPortfolioRequest.getPoints());
//				if (error.isEmpty() == false) {
//					allErrors.put("POINTS", error);
//				}
//			}
			if (admRiskPortfolioRequest != null && admRiskPortfolioRequest.getBehaviour() != null) {
				error = validateBehaviour(admRiskPortfolioRequest.getBehaviour());
				if (error.isEmpty() == false) {
					allErrors.put("BEHAVIOUR", error);
				}
			}
//			if (admRiskPortfolioRequest != null && admRiskPortfolioRequest.getEquity() != 0) {
//				error = validateEquity(admRiskPortfolioRequest.getEquity());
//				if (error.isEmpty() == false) {
//					allErrors.put("EQUITY", error);
//				}
//			}
//			if (admRiskPortfolioRequest != null && admRiskPortfolioRequest.getDebt() != 0) {
//				error = validateDebt(admRiskPortfolioRequest.getDebt());
//				if (error.isEmpty() == false) {
//					allErrors.put("DEBT", error);
//				}
//			}
//			if (admRiskPortfolioRequest != null && admRiskPortfolioRequest.getCash() != 0) {
//				error = validateCash(admRiskPortfolioRequest.getCash());
//				if (error.isEmpty() == false) {
//					allErrors.put("CASH", error);
//				}
//			}
			

		}
		return allErrors;

	}

//	private HashMap<String, String> validatePoints(String points) {
//		String inputParamName = AdminConstants.POINTS;
//		HashMap<String, String> errors = new HashMap<String, String>();
//		if (common.nonEmptyCheck(points, inputParamName).isEmpty() == false) {
//			errors.put("EMPTY", common.nonEmptyCheck(points, inputParamName));
//		}
//		if (common.isAlpha(points, inputParamName).isEmpty() == false) {
//			errors.put("NON_ALPHA", common.isAlpha(points, inputParamName));
//		}
//		return errors;
//	}
	
	private HashMap<String, String> validateBehaviour(String behaviour) {
		String inputParamName = AdminConstants.BEHAVIOUR;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(behaviour, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(behaviour, inputParamName));
		}
		if (common.isAlpha(behaviour, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(behaviour, inputParamName));
		}
		return errors;
	}
//	private HashMap<String, String> validateEquity(int equity) {
//		String inputParamName = AdminConstants.EQUITY;
//		HashMap<String, String> errors = new HashMap<String, String>();
//		if (common.nonEmptyCheck(equity, inputParamName).isEmpty() == false) {
//			errors.put("EMPTY", common.nonEmptyCheck(equity, inputParamName));
//		}
//		if (common.isAlpha(equity, inputParamName).isEmpty() == false) {
//			errors.put("NON_ALPHA", common.isAlpha(equity, inputParamName));
//		}
//		return errors;
//	}
//	private HashMap<String, String> validateDebt(int debt) {
//		String inputParamName = AdminConstants.DEBT;
//		HashMap<String, String> errors = new HashMap<String, String>();
//		if (common.nonEmptyCheck(debt, inputParamName).isEmpty() == false) {
//			errors.put("EMPTY", common.nonEmptyCheck(debt, inputParamName));
//		}
//		if (common.isAlpha(debt, inputParamName).isEmpty() == false) {
//			errors.put("NON_ALPHA", common.isAlpha(debt, inputParamName));
//		}
//		return errors;
//	}
//	private HashMap<String, String> validateCash(int cash) {
//		String inputParamName = AdminConstants.CASH;
//		HashMap<String, String> errors = new HashMap<String, String>();
//		if (common.nonEmptyCheck(cash, inputParamName).isEmpty() == false) {
//			errors.put("EMPTY", common.nonEmptyCheck(cash, inputParamName));
//		}
//		if (common.isAlpha(cash, inputParamName).isEmpty() == false) {
//			errors.put("NON_ALPHA", common.isAlpha(cash, inputParamName));
//		}
//		return errors;
//	}

}
