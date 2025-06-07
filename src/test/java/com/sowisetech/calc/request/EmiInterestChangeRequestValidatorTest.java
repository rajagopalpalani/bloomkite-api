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
public class EmiInterestChangeRequestValidatorTest {

	@Autowired
	EmiInterestChangeRequestValidator emiInterestChangeRequestValidator;

//	@Test
//	public void validateLoanDateTest_Success() {
//		String input = "07-07-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiInterestChangeRequestValidator.validateLoanDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//		}
//	}

//	@Test
//	public void validateLoanDateTest_Error() {
//		String input = "07-07-2000ad";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiInterestChangeRequestValidator.validateLoanDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : LOANDATE", error);
//		}
//	}

//	@Test
//	public void validateChangedDateTest_Success() {
//		String input = "07-07-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiInterestChangeRequestValidator.validateChangedDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//		}
//	}

//	@Test
//	public void validateChangedDateTest_Error() {
//		String input = "07-07-200nb0";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiInterestChangeRequestValidator.validateChangedDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : CHANGEDDATE", error);
//		}
//	}

	@Test
	public void validateIncreasedEmiTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateIncreasedEmi(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateIncreasedEmiTest_Error() {
		String input = "800000ab";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateIncreasedEmi(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INCREASEDEMI", error);
		}
	}

	@Test
	public void validateChangedRateTest_Success() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateChangedRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateChangedRateTest_Error() {
		String input = "8.5ad";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateChangedRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : CHANGEDRATE", error);
		}
	}

	@Test
	public void validateLoanAmountTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateLoanAmountTest_Error() {
		String input = "800000ad";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : LOANAMOUNT", error);
		}
	}

	@Test
	public void validateTenureTest_Success() {
		String input = "8";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateTenureTest_Error() {
		String input = "8st";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : TENURE", error);

		}
	}

	@Test
	public void validateInterestRateTest_Success() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInterestRateTest_Error() {
		String input = "8.5qw";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiInterestChangeRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);

		}
	}

}
