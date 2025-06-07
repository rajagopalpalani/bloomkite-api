package com.sowisetech.investor.request;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvestorRequestValidatorTest {

	@Autowired(required = true)
	InvestorRequestValidator investorRequestValidator;

	// Name Test

	@Test
	public void validateNameTest() {
		String name = "Charles";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateFullName(name);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}

	}

	@Test
	public void validateNameTestForAlphaNumeric() {
		String name = "Charles123 ";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateFullName(name);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains non-alpha characters : FULLNAME", error);
			}
		}

	}

	// Displayname test

	// @Test
	// public void validateDisplayNameTest() {
	// String displayName = "Dolby";
	// HashMap<String, String> errors = new HashMap<String, String>();
	// errors = investorRequestValidator.validateDisplayName(displayName);
	// for (String error : errors.values()) {
	// assertTrue(StringUtils.isEmpty(error) == true);
	//
	// }
	// }

	// @Test
	// public void validateDisplayNameTestForAlphaNumeric() {
	// String name = "Dolby123 ";
	// HashMap<String, String> errors = new HashMap<String, String>();
	// errors = investorRequestValidator.validateDisplayName(name);
	// for (String error : errors.values()) {
	// if (StringUtils.isEmpty(error) == false) {
	// assertTrue(StringUtils.isEmpty(error) == false);
	// assertEquals("Value contains non-alpha characters : DISPLAYNAME", error);
	// }
	// }
	//
	// }

	// DOB test
	// date format in mm-dd-yyyy//
	@Test
	public void validateDobTest() {
		String dob = "05-21-1995";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateDob(dob);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateDobTestFordobCheck() {
		String dob = "31-07-1996 ";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateDob(dob);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("DOB must be in a format (mm-dd-yyyy) : DOB", error);
			}
		}

	}

	// GenderTest

	@Test
	public void validateGenderTest() {
		String gender = "m";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateGender(gender);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateGenderTestForGenderCheck() {
		String gender = "c";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateGender(gender);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("value must be in single character(F,M,O,f,m,o) : GENDER", error);
			}
		}

	}

	// EmailId test

	@Test
	public void validateEmailidTest() {
		String emailId = "aaa@bbb.com";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateEmailId(emailId);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateEmailidTestForIsValidEmailAddress() {
		String emailId = "aaa!@_123@..com";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validateEmailId(emailId);
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
		String emailId = "9965487512";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validatePhoneNumber(emailId);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePhoneNumberTestForisNumericValues() {
		String phoneNumber = "aabb965475";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validatePhoneNumber(phoneNumber);
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
		errors = investorRequestValidator.validatePhoneNumber(phoneNumber);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains more than or less than TEN digits : PHONENUMBER", error);
			}
		}
	}

	// Password Test

	@Test
	public void validatePasswordTest() {
		String password = "Ah17@bcj";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validatePassword(password);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePasswordTestForvalidPasswordCheck() {
		String password = "aaabbb";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validatePassword(password);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals(
						"Value must contain MIN 8 and MAX 12 characters,One Special Character,One Number and One Capital letter : PASSWORD",
						error);
			}
		}
	}

	// Pincode Test

	@Test
	public void validatePincodeTest() {
		String pincode = "632514";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePincodeTestForisNumericValues() {
		String pincode = "45g6k9";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = investorRequestValidator.validatePincode(pincode);
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
		errors = investorRequestValidator.validatePincode(pincode);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains more than or less than SIX digits : PINCODE", error);
			}
		}
	}

}
