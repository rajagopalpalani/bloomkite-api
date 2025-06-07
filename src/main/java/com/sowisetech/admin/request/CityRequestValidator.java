package com.sowisetech.admin.request;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.util.AdmAppMessages;
import com.sowisetech.admin.util.AdmCommon;
import com.sowisetech.admin.util.AdminConstants;

@Component
public class CityRequestValidator {

	@Autowired
	AdmAppMessages appMessages;
	@Autowired
	AdmCommon common;

	public HashMap<String, HashMap<String, String>> validate(CityRequest cityRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdmin_detail_empty());

		if (cityRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (cityRequest != null && cityRequest.getCity() != null) {
				error = validateCity(cityRequest.getCity());
				if (error.isEmpty() == false) {
					allErrors.put("CITY", error);
				}
			}
			if (cityRequest != null && cityRequest.getPincode() != null) {
				error = validatePincode(cityRequest.getPincode());
				if (error.isEmpty() == false) {
					allErrors.put("PINCODE", error);
				}
			}
			if (cityRequest != null && cityRequest.getDistrict() != null) {
				error = validateDistrict(cityRequest.getDistrict());
				if (error.isEmpty() == false) {
					allErrors.put("DISTRICT", error);
				}
			}
		}
		return allErrors;
	}

	private HashMap<String, String> validateDistrict(String district) {
		String inputParamName = AdminConstants.DISTRICT;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(district, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(district, inputParamName));
		}
		if (common.isAlpha(district, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(district, inputParamName));
		}
		return errors;
	}

	private HashMap<String, String> validatePincode(String pincode) {
		String inputParamName = AdminConstants.PINCODE;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(pincode, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(pincode, inputParamName));
		}
		if (common.pincodelengthCheck(pincode, inputParamName).isEmpty() == false) {
			errors.put("NON_PINCODE_FORMAT", common.pincodelengthCheck(pincode, inputParamName));
		}
		if (common.isNumericValues(pincode, inputParamName).isEmpty() == false) {
			errors.put("NON_PINCODE_FORMAT", common.isNumericValues(pincode, inputParamName));
		}
		return errors;
	}

	private HashMap<String, String> validateCity(String city) {
		String inputParamName = AdminConstants.CITY;
		HashMap<String, String> errors = new HashMap<String, String>();
		if (common.nonEmptyCheck(city, inputParamName).isEmpty() == false) {
			errors.put("EMPTY", common.nonEmptyCheck(city, inputParamName));
		}
		if (common.isAlpha(city, inputParamName).isEmpty() == false) {
			errors.put("NON_ALPHA", common.isAlpha(city, inputParamName));
		}
		return errors;
	}
}
