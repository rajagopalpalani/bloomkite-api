package com.sowisetech.investor.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.investor.util.InvAppMessages;
import com.sowisetech.investor.util.InvCommon;
import com.sowisetech.investor.util.InvestorConstants;

@Component
public class InvInterestRequestValidator {

	/**
	 * Basic validation on HomeLoanRequest input values.
	 * 
	 * @param homeLoanRequest
	 * @return - Empty list or list of errors in string format.
	 */
	@Autowired
	InvAppMessages appMessages;

	@Autowired
	InvCommon common;

	public HashMap<String, HashMap<String, String>> validate(List<InvInterestRequest> invInterestRequestList) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		// List<String> validErrors = new ArrayList<>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getInvestor_detail_empty());
		if (invInterestRequestList == null) {
			allErrors.put("NULL", error);
		} else {
			for (InvInterestRequest invInterestRequest : invInterestRequestList) {

				if (invInterestRequest != null && invInterestRequest.getScale() != null) {
					error = validateScale(invInterestRequest.getScale()); // validate scale method call//
					if (error.isEmpty() == false) {
						allErrors.put("SCALE", error);
					}
				}
			}
			/*
			 * Add the validation for each advisor attributes or column.
			 * 
			 * allErrors.addAll(validateRateOfInterest(advisorRequest.getDisplayName()));
			 * allErrors.addAll(validateTenure(advisorRequest.getDob()));
			 * allErrors.addAll(validateLoanDate(advisorRequest.getEmailId())));
			 */
		}

		// for (String error : allErrors) {
		// if (StringUtils.isEmpty(error) == false) {
		// validErrors.add(error);
		// }
		//
		// }
		// return validErrors;
		return allErrors;

	}

	protected HashMap<String, String> validateScale(String scale) {
		String inputParamScale = InvestorConstants.SPACE_WTIH_COLON + InvestorConstants.SCALE;
		HashMap<String, String> errors = new HashMap<String, String>();
		String error1 = common.nonEmptyCheck(scale, inputParamScale);
		if (error1.isEmpty() == false) {
			errors.put("EMPTY", error1);
		}
		String error2 = common.scaleCheck(scale, inputParamScale);
		if (error2.isEmpty() == false) {
			errors.put("FORMAT_ERROR", error2);
		}
		return errors;
	}

}
