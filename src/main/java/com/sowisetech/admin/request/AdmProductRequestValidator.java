package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmProductRequestValidator {
	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;
	
	public HashMap<String, HashMap<String, String>> validateProduct(ProductRequest productRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		
		if (productRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (productRequest != null && productRequest.getProduct() != null) {
				error = validateProduct(productRequest.getProduct());
				if (error.isEmpty() == false) {
					allErrors.put("PRODUCT", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validateProduct(String product) {
		String inputParamName = AdminConstants.PRODUCT;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(product, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(product, inputParamName));
		}
		if (common.isAlpha(product, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(product, inputParamName));
		}
		return errors;
	}

}
