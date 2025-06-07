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
public class ModifyAdvReqValidatorTest {

	@Autowired(required = true)
	ModifyAdvReqValidator modifyAdvReqValidator;

	// Designation Test

	@Test
	public void validateDesignationTest() {
		String designation = "Engineer";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateDesignation(designation);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateDesignationTestForAlphaNumeric() {
		String designation = "Engineer5677";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateDesignation(designation);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value should not allow some characters : DESIGNATION", error);
			}
		}
	}

	// Name Test

	@Test
	public void validateNameTest() {
		String name = "Charles";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateName(name);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateNameTestForAlphaNumeric() {
		String name = "Charles123 ";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateName(name);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains non-alpha characters : NAME", error);
			}
		}
	}

	// EmailId test

	@Test
	public void validateEmailidTest() {
		String emailId = "aaa@bbb.com";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateEmailid(emailId);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateEmailidTestForIsValidEmailAddress() {
		String emailId = "aaa!@_123@..com";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateEmailid(emailId);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value is not in a mail id format : EMAILID", error);
			}
		}
	}

	// PhoneNumber Test

	@Test
	public void validatePhoneNumberTest() {
		String phoneNumber = "9965487512";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validatePhoneNumber(phoneNumber);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePhoneNumberTestForisNumericValues() {
		String phoneNumber = "aabb965475";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validatePhoneNumber(phoneNumber);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains non-numeric values : PHONENUMBER", error);
			}
		}
	}

	@Test
	public void validatePhoneNumberTestForphoneNumberlengthCheck() {
		String phoneNumber = "96547545";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validatePhoneNumber(phoneNumber);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains more than or less than TEN digits : PHONENUMBER", error);
			}
		}
	}

	// DOB test

	@Test
	public void validateDobTest() {
		String dob = "07-23-1996";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateDob(dob);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateDobTestFordobCheck() {
		String dob = "08-10-1996";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateDob(dob);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == true) {
				assertTrue(StringUtils.isEmpty(error) == true);
				assertEquals("DOB must be in a format (mm-dd-yyyy) : DOB", error);
			}
		}
	}

	// GenderTest

	@Test
	public void validateGenderTest() {
		String gender = "m";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateGender(gender);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateGenderTestForGenderCheck() {
		String gender = "AAAPL1234C";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateGender(gender);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("value must be in single character(F,M,O,f,m,o) : GENDER", error);
			}
		}
	}
	// PanTest

	@Test
	public void validatePanTest() {
		String pan = "AAAPL1234C";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validatePan(pan);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validatePanTestForlengthCheck() {
		String pan = "AAAPL1234C32";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validatePan(pan);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value must be valid PAN number eg: AABPL1234C : PAN", error);
			}
		}
	}

	// State Test

	@Test
	public void validateStateTest() {
		String state = "Tamilnadu";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateState(state);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateStateTestForAlphaNumeric() {
		String state = "Tamilnadu56";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateState(state);
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
		errors = modifyAdvReqValidator.validateCity(city);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateCityTestForAlphaNumeric() {
		String city = "Chennai54";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validateCity(city);
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
		errors = modifyAdvReqValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePincodeTestForisNumericValues() {
		String pincode = "45g6k9";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = modifyAdvReqValidator.validatePincode(pincode);
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
		errors = modifyAdvReqValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains more than or less than SIX digits : PINCODE", error);
			}
		}
	}

}
