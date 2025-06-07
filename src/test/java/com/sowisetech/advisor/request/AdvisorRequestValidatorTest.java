package com.sowisetech.advisor.request;

import static org.junit.Assert.*;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvisorRequestValidatorTest {

	@Autowired(required = true)
	AdvisorRequestValidator advisorRequestValidator;

	// Name Test
	@Test
	public void validateNameTest() {
		String name = "Charles";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validateName(name);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateNameTestForAlphaNumeric() {
		String name = "Charles123 ";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validateName(name);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value contains non-alpha characters : NAME", error);
			}
		}
	}
	
	@Test
	public void validatePanTest() {
		String pan = "AAAPL1234C";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validatePan(pan);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	
	@Test
	public void validatePanTestForlengthCheck() {
		String pan = "AAAPL1234C32";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validatePan(pan);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals("Value must be valid PAN number eg: AABPL1234C : PAN", error);
			}
		}
	}

	// EmailId test

	@Test
	public void validateEmailidTest() {
		String emailId = "aaa@bbb.com";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validateEmailid(emailId);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateEmailidTestForIsValidEmailAddress() {
		String emailId = "aaa!@_123@..com";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validateEmailid(emailId);
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
		errors = advisorRequestValidator.validatePhoneNumber(emailId);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePhoneNumberTestForisNumericValues() {
		String phoneNumber = "aabb965475";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validatePhoneNumber(phoneNumber);
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
		errors = advisorRequestValidator.validatePhoneNumber(phoneNumber);
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
		errors = advisorRequestValidator.validatePassword(password);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePasswordTestForvalidPasswordCheck() {
		String password = "aaabbb";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advisorRequestValidator.validatePassword(password);
		for (String error : errors.values()) {
			if (StringUtils.isEmpty(error) == false) {
				assertTrue(StringUtils.isEmpty(error) == false);
				assertEquals(
						"Value must contain MIN 8 and MAX 12 characters,One Special Character,One Number and One Capital letter : PASSWORD",
						error);
			}
		}
	}

}
