package com.sowisetech.advisor.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvPersonalInfoRequestValidatorTest {

	@Autowired(required = true)
	AdvPersonalInfoRequestValidator advPersonalInfoRequestValidator;

	// DOB test

	@Test
	public void validateDobTest() {
		String dob = "12-07-2007";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateDob(dob);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	// @Test
	// public void validateDobTestFordobCheck() {
	// String dob = "Mar-1996 ";
	// HashMap<String, String> errors = new HashMap<String, String>();
	// errors = advPersonalInfoRequestValidator.validateDob(dob);
	// for (String error : errors.values()) {
	// assertTrue(StringUtils.isEmpty(error) == false);
	// assertEquals("DOB must be in a format (mm-dd-yyyy) : DOB", error);
	// }
	// }

	@Test
	public void validateDesignationTest() {
		String designation = "doctor";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateDesignation(designation);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateDesignationTestForAlphaNumeric() {
		String designation = "doctor56";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateDesignation(designation);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value should not allow some characters : DESIGNATION", error);
		}
	}

	// GenderTest

	@Test
	public void validateGenderTest() {
		String gender = "m";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateGender(gender);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateGenderTestForGenderCheck() {
		String gender = "c";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateGender(gender);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("value must be in single character(F,M,O,f,m,o) : GENDER", error);
			}
		}
	}

	// STATE
	@Test
	public void validateStateTest() {
		String state = "Tamilnadu";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateState(state);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateStateTestForAlphaNumeric() {
		String state = "Tamilnadu56";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateState(state);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains non-alpha characters : STATE", error);
			}
		}
	}

	// City Test

	@Test
	public void validateCityTest() {
		String city = "Chennai";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateCity(city);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateCityTestForAlphaNumeric() {
		String city = "Chennai54";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validateCity(city);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value should not allow some characters : CITY", error);
			}
		}
	}

	// Pincode Test

	@Test
	public void validatePincodeTest() {
		String pincode = "632514";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePincodeTestForisNumericValues() {
		String pincode = "45g6k9";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains non-numeric values : PINCODE", error);
			}
		}
	}

	@Test
	public void validatePincodeTestForPincodelengthCheck() {
		String pincode = "4569";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advPersonalInfoRequestValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains more than or less than SIX digits : PINCODE", error);
			}
		}
	}
}
