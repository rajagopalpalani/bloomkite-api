package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmCashFlowItemTypeRequestValidator {

	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;
	
	public HashMap<String, HashMap<String, String>> validate(AdmCashFlowItemTypeRequest admCashFlowItemTypeRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		
		if (admCashFlowItemTypeRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (admCashFlowItemTypeRequest != null && admCashFlowItemTypeRequest.getCashFlowItemType() != null) {
				error = validate(admCashFlowItemTypeRequest.getCashFlowItemType());
				if (error.isEmpty() == false) {
					allErrors.put("CASHFLOWITEMTYPE", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validate(String cashFlowItemType) {
		String inputParamName = AdminConstants.CASHFLOWITEMTYPE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(cashFlowItemType, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(cashFlowItemType, inputParamName));
		}
		if (common.isAlpha(cashFlowItemType, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(cashFlowItemType, inputParamName));
		}
		return errors;
	}
}
