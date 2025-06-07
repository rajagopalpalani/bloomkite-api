package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class LicenseRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(LicenseRequest licenseRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (licenseRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (licenseRequest != null && licenseRequest.getLicense() != null) {
				error = validateLicense(licenseRequest.getLicense());
				if (error.isEmpty() == false) {
					allErrors.put("LICENSE", error);
				}
			}
			if (licenseRequest != null && licenseRequest.getIssuedBy() != null) {
				error = validateIssuedBy(licenseRequest.getIssuedBy());
				if (error.isEmpty() == false) {
					allErrors.put("ISSUEDBY", error);
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

	private HashMap<String, String> validateLicense(String license) {
		String inputParamName = AdminConstants.LICENSE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(license, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(license, inputParamName));
		}
		if (common.isAlpha(license, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(license, inputParamName));
		}
		return errors;
	}
	
	private HashMap<String, String> validateIssuedBy(String issuedBy) {
		String inputParamName = AdminConstants.ISSUEDBY;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(issuedBy, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(issuedBy, inputParamName));
		}
		if (common.isAlpha(issuedBy, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(issuedBy, inputParamName));
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
