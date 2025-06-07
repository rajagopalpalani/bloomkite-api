package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class InsuranceItemRequestValidator {
	
	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;
	
	public HashMap<String, HashMap<String, String>> validate(InsuranceItemRequest insuranceItemRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		
		if (insuranceItemRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (insuranceItemRequest != null && insuranceItemRequest.getInsuranceItem() != null) {
				error = validateInsuranceItem(insuranceItemRequest.getInsuranceItem());
				if (error.isEmpty() == false) {
					allErrors.put("INSURANCEITEM", error);
				}
			}
			if (insuranceItemRequest != null && insuranceItemRequest.getValue() != null) {
				error = validateValue(insuranceItemRequest.getValue());
				if (error.isEmpty() == false) {
					allErrors.put("VALUE", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validateInsuranceItem(String insuranceItem) {
		String inputParamName = AdminConstants.INSURANCEITEM;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(insuranceItem, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(insuranceItem, inputParamName));
		}
		if (common.isAlpha(insuranceItem, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(insuranceItem, inputParamName));
		}
		return errors;
	}
	
	private HashMap<String, String> validateValue(String insuranceItem) {
		String inputParamName = AdminConstants.VALUE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(insuranceItem, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(insuranceItem, inputParamName));
		}
		if (common.isAlpha(insuranceItem, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(insuranceItem, inputParamName));
		}
		return errors;
	}

}
