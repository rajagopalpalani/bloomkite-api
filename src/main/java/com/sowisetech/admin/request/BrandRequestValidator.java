package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;
@Component
public class BrandRequestValidator {
	@Autowired
	AdmAppMessages appMessages;

	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate( BrandRequest brandRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		if (brandRequest == null) {
			allErrors.put("NULL", error);
		} else {
			// if (advPersonalInfoRequest != null &&
			// advPersonalInfoRequest.getDisplayName() != null) {
			// allErrors.addAll(validateDisplayName(advPersonalInfoRequest.getDisplayName()));
			// }
			if (brandRequest != null && brandRequest.getBrand() != null) {
				error = validateStatus(brandRequest.getBrand());
				if (error.isEmpty() == false) {
					allErrors.put("BRAND", error);
				}
			}

		}
		return allErrors;

}

	private HashMap<String, String> validateStatus(String brand) {
		String inputParamName = AdminConstants.BRAND;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(brand, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(brand, inputParamName));
		}
		if (common.isAlpha(brand, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(brand, inputParamName));
		}
		return errors;
	}
	

}
