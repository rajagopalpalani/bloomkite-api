package com.sowisetech.calc.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TargetValueRequestValidatorTest {

	@Autowired
	TargetValueRequestValidator targetValueRequestValidator;

	@Test
	public void validateInvTypeTest() {
		String input = "LUMSUM";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateInvType(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInvTypeTest_Error() {
		String input = "LUMSUM123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateInvType(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value should be LUMSUM or SIP : INV_TYPE", error);
		}
	}

	@Test
	public void validateDurationTest() {
		String input = "10";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateDuration(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateDurationTest_Success() {
		String input = "10";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateDuration(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateFutureValueTest() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateFutureValue(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateFutureValueTest_Error() {
		String input = "100000as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateFutureValue(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : FUTUREVALUE", error);
		}
	}

	@Test
	public void validateRateOfInterestTest_Success() {
		String input = "10.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateRateOfInterest(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateRateOfInterestTest_Error() {
		String input = "10.5as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = targetValueRequestValidator.validateRateOfInterest(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : RATE_OF_INTEREST", error);
		}
	}
}
