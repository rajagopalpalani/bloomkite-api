package com.sowisetech.advisor.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvCommon;

@Component
public class AdvProductInfoRequestValidator implements IValidator {

	@Autowired
	AdvAppMessages appMessages;

	@Autowired
	AdvCommon common;

	public HashMap<String, HashMap<String, String>> validate(AdvProductInfoRequest advProductInfoRequest) {
		HashMap<String, HashMap<String, String>> allErrors = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> error = new HashMap<String, String>();
		error.put("EMPTY", appMessages.getAdvisor_detail_empty());
		if (advProductInfoRequest == null) {
			allErrors.put("NULL", error);
		} else {
			if (advProductInfoRequest != null && advProductInfoRequest.getAdvProducts() != null) {
				error = validateProducts(advProductInfoRequest.getAdvProducts());
				if (error.isEmpty() == false) {
					allErrors.put("PRODUCTS", validateProducts(advProductInfoRequest.getAdvProducts()));
				}
			}

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

	public HashMap<String, String> validateProducts(List<AdvProductRequest> products) {
		String inputParamName = AdvisorConstants.SPACE_WTIH_COLON + AdvisorConstants.PRODUCT;
		HashMap<String, String> errors = new HashMap<String, String>();
		for (AdvProductRequest product : products) {
			// if (product.getProduct() != null) {
			// if (Common.allowMultipleText(product.getProduct(), inputParamName).isEmpty()
			// == false) {
			// errors.put("PRODUCT", Common.allowMultipleText(product.getProduct(),
			// inputParamName));
			// }
			// }
			// for (String service : product.getServices()) {
			// if (service != null) {
			// if (Common.allowMultipleText(service, inputParamName).isEmpty() == false) {
			// errors.put("SERVICE", Common.allowMultipleText(service, inputParamName));
			// }
			// }
			// }
			// if (product.getBrand() != null) {
			// if (Common.allowMultipleText(product.getBrand(), inputParamName).isEmpty() ==
			// false) {
			// errors.put("BRAND", Common.allowMultipleText(product.getBrand(),
			// inputParamName));
			// }
			// }
			// if (product.getRemuneration() != null) {
			// if (Common.isValidRemuneration(product.getRemuneration(),
			// inputParamName).isEmpty() == false) {
			// errors.put("RENUMERATION",
			// Common.isValidRemuneration(product.getRemuneration(), inputParamName));
			// }
			// }
			// if (product.getCertificate() != null) {
			// if (Common.allowMultipleText(product.getCertificate(),
			// inputParamName).isEmpty() == false) {
			// errors.put("CERTIFICATE", Common.allowMultipleText(product.getCertificate(),
			// inputParamName));
			// }
			// }
			// if (product.getLicNumber() != null) {
			// if (Common.allowMultipleLegalNo(product.getLicNumber(),
			// inputParamName).isEmpty() == false) {
			// errors.put("LIC_NUMBER",
			// Common.allowMultipleLegalNo(product.getLicNumber(), inputParamName));
			// }
			// }
			// if (product.getIssuedBy() != null) {
			// if (Common.allowMultipleText(product.getIssuedBy(), inputParamName).isEmpty()
			// == false) {
			// errors.put("ISSUED_BY", Common.allowMultipleText(product.getIssuedBy(),
			// inputParamName));
			// }
			// }
			if (product.getValidity() != null) {
				String error1 = common.isValidDate(product.getValidity(), inputParamName);
				if (error1.isEmpty() == false) {
					errors.put("VALIDITY", error1);
				}
			}
			// if (product.getImagePath() != null) {
			// if(Common.isImage(product.getImagePath(), inputParamName).isEmpty()==false) {
			// errors.put("IMAGE_PATH",Common.isImage(product.getImagePath(),
			// inputParamName));}
			// }
		}
		return errors;
	}

}
