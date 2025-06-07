package com.sowisetech.advisor.request;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;
import com.sowisetech.advisor.util.AdvisorConstants;

@Component
public class PromotionRequestValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(PromotionRequest promotionRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (promotionRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (promotionRequest != null && promotionRequest.getPromotionReq() != null) {
				error = validatePromotions(promotionRequest.getPromotionReq());
				if (error.isEmpty() == false) {
					allErrors.put("PROMOTION", error);
				}
			}

		}
		return allErrors;
	}

	public HashMap<String, String> validatePromotions(List<PromotionReq> promotionReq) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PROMOTION;
		HashMap<String, String> errors = new HashMap<String, String>();
		for (PromotionReq promo : promotionReq) {
			String error = common.isAlphaNumericSpace(promo.getTitle(), inputParamName);
			if (error.isEmpty() == false) {
				errors.put("TITLE", error);
			}
		}
		return errors;
	}

}
