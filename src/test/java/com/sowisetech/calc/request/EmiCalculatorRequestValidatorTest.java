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
public class EmiCalculatorRequestValidatorTest {

	@Autowired
	EmiCalculatorRequestValidator emiCalculatorRequestValidator;

	@Test
	public void validateInterestRateTest_Success() {
		String input = "10.05";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCalculatorRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInterestRateTest_Error() {
		String input = "10.as05";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCalculatorRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);
		}
	}

	@Test
	public void validateTenureTest_Success() {
		String input = "10";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCalculatorRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateTenureTest_Error() {
		String input = "10asd";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCalculatorRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : TENURE", error);
		}
	}

	@Test
	public void validateLoanAmountTest_Success() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCalculatorRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateLoanAmountTest_Error() {
		String input = "100000ad";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCalculatorRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : LOANAMOUNT", error);
		}
	}

}
