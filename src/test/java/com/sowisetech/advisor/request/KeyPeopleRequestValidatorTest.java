package com.sowisetech.advisor.request;

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
public class KeyPeopleRequestValidatorTest {
	@Autowired(required = true)
	KeyPeopleRequestValidator keyPeopleRequestValidator;

	// FullName Test
	@Test
	public void validateFullNameTest() {
		String fullName = "Charles";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = keyPeopleRequestValidator.validateFullName(fullName);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateFullNameisAlphaTest() {
		String fullName = "123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = keyPeopleRequestValidator.validateFullName(fullName);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);

		}
	}

	@Test
	public void validateDesignationTest() {
		String designation = "Charles";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = keyPeopleRequestValidator.validateDesignation(designation);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

	@Test
	public void validateDesignationisAlphaTest() {
		String designation = "Charles";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = keyPeopleRequestValidator.validateDesignation(designation);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);

		}
	}

//	@Test
//	public void validateImageTest() {
//		String image = "aaa.jpg";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = keyPeopleRequestValidator.validateImage(image);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//
//		}
//	}
//
//	@Test
//	public void validateImageisImageTest() {
//		String image = "aaa123";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = keyPeopleRequestValidator.validateImage(image);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//
//		}
//	}
}
