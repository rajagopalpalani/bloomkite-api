package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class AdmArticleStatusRequestValidator {
	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;
	
	public HashMap<String, HashMap<String, String>> validate(ArticleStatusRequest articleStatusRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());
		
		if (articleStatusRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (articleStatusRequest != null && articleStatusRequest.getDesc() != null) {
				error = validate(articleStatusRequest.getDesc());
				if (error.isEmpty() == false) {
					allErrors.put("DESC", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validate(String desc) {
		String inputParamName = AdminConstants.DESC;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(desc, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(desc, inputParamName));
		}
		if (common.isAlpha(desc, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(desc, inputParamName));
		}
		return errors;
	}
}
