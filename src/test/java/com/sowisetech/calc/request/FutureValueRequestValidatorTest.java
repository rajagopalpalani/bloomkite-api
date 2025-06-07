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
public class FutureValueRequestValidatorTest {

	@Autowired
	FutureValueRequestValidator futureValueRequestValidator;

	@Test
	public void validateInvTypeTest_Success() {
		String input = "LUMSUM";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateInvType(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInvTypeTest_Error() {
		String input = "LUMSUM123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateInvType(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value should be LUMSUM or SIP : INV_TYPE", error);
		}
	}

	@Test
	public void validateYearlyIncreaseTest_Success() {
		String input = "10.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateYearlyIncrease(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateYearlyIncreaseTest_Error() {
		String input = "10.5ha";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateYearlyIncrease(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : YEARLYINCREASE", error);

		}
	}

	@Test
	public void validateAnnualGrowthTest_success() {
		String input = "10.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateAnnualGrowth(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateAnnualGrowthTest_Error() {
		String input = "10.5ri";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateAnnualGrowth(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : ANNUALGROWTH", error);

		}
	}

	@Test
	public void validateDurationTest_Success() {
		String input = "10";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateDuration(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateDurationTest_Error() {
		String input = "10sh";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateDuration(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : DURATION", error);

		}
	}

	@Test
	public void validateInvAmountTest_Success() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateInvAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInvAmountTest_Error() {
		String input = "100000sa";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = futureValueRequestValidator.validateInvAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INVESTMENTAMOUNT", error);
		}
	}

}
