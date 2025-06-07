package com.sowisetech.calc.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InsuranceRequestValidatorTest {

	@Autowired
	InsuranceRequestValidator insuranceRequestValidator;

	@Test
	public void validateExistingInsuranceTest() {
		String value = "50000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validateExistingInsurance(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateExistingInsuranceTest_Error() {
		String value = "500aaa00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validateExistingInsurance(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : EXISTINGINSURANCE");
		}
	}

	@Test
	public void validatePredictabilityTest() {
		String value = "PREDICTABLE";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validatePredictability(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validatePredictabilityTest_Error() {
		String value = "abc";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validatePredictability(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value should be PREDICTABLE or UNPREDICTABLE : PREDICTABILITY");
		}
	}

	@Test
	public void validateStabilityTest() {
		String value = "FLUCTUATING";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validateStability(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateStabilityTest_Error() {
		String value = "abc";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validateStability(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value should be STABLE or FLUCTUATING : STABILITY");
		}
	}

	@Test
	public void validateAnnualIncomeTest() {
		String value = "50000.00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validateAnnualIncome(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateAnnualIncomeTest_Error() {
		String value = "400abc00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = insuranceRequestValidator.validateAnnualIncome(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : ANNUALINCOME");
		}
	}

}
