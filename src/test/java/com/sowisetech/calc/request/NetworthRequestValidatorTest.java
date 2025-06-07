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
public class NetworthRequestValidatorTest {

	@Autowired
	NetworthRequestValidator networthRequestValidator;

	@Test
	public void validateFutureValueTest() {
		String value = "50000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = networthRequestValidator.validateFutureValue(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateFutureValueTest_Error() {
		String value = "50ab00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = networthRequestValidator.validateFutureValue(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : FUTUREVALUE");
		}
	}

	@Test
	public void validateValueTest() {
		String value = "50000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = networthRequestValidator.validateValue(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateValueTest_Error() {
		String value = "50ab0";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = networthRequestValidator.validateValue(value);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : VALUE");
		}
	}
}
