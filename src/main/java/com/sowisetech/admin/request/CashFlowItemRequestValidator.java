package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class CashFlowItemRequestValidator {

	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(CashFlowItemRequest cashFlowItemRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());

		if (cashFlowItemRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (cashFlowItemRequest != null && cashFlowItemRequest.getCashFlowItem() != null) {
				error = validate(cashFlowItemRequest.getCashFlowItem());
				if (error.isEmpty() == false) {
					allErrors.put("CASHFLOWITEM", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validate(String cashFlowItem) {
		String inputParamName = AdminConstants.CASHFLOWITEM;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(cashFlowItem, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(cashFlowItem, inputParamName));
		}
		if (common.isAlpha(cashFlowItem, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(cashFlowItem, inputParamName));
		}
		return errors;
	}
}
