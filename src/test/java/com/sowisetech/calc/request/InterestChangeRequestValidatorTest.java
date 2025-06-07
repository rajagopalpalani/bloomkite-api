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
public class InterestChangeRequestValidatorTest {
	
	@Autowired
	InterestChangeRequestValidator interestChangeRequestValidator;

	
//	@Test
//	public void validateLoanDateTest_Success() {
//		String input = "07-07-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = interestChangeRequestValidator.validateLoanDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//		}
//	}
//	@Test
//	public void validateLoanDateTest_Error() {
//		String input = "07-07-2000as";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = interestChangeRequestValidator.validateLoanDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : LOANDATE", error);
//		}
//	}

//	@Test
//	public void validateChangedDateTest_Success() {
//		String input = "07-07-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = interestChangeRequestValidator.validateInterestChangedDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//		}
//	}
//	@Test
//	public void validateChangedDateTest_Error() {
//		String input = "07-07-2000sa";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = interestChangeRequestValidator.validateInterestChangedDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : INTERESTCHANGEDDATE", error);
//
//		}
//	}

	@Test
	public void validateChangedRateTest_Success() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateChangedRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateChangedRateTest_Error() {
		String input = "8.5ba";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateChangedRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : CHANGEDRATE", error);

		}
	}
	@Test
	public void validateLoanAmountTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateLoanAmountTest_Error() {
		String input = "800000ri";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : LOANAMOUNT", error);

		}
	}
	@Test
	public void validateTenureTest_Success() {
		String input = "8";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateTenureTest_Error() {
		String input = "8na";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : TENURE", error);

		}
	}
	@Test
	public void validateInterestRateTest_Success() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateInterestRateTest_Error() {
		String input = "8.5th";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = interestChangeRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);

		}
	}
}
