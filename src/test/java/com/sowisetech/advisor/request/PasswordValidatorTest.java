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
public class PasswordValidatorTest {

	@Autowired(required = true)
	PasswordValidator passwordValidator;

	@Test
	public void validatePasswordTest() {
		String password = "Ah17@bcj";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = passwordValidator.validatePassword(password);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validatePasswordTestForvalidPasswordCheck() {
		String password = "aaabbb";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = passwordValidator.validatePassword(password);
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
