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
public class EmiChangeRequestValidatorTest {

	@Autowired
	EmiChangeRequestValidator emiChangeRequestValidator;

//	@Test
//	public void validateLoanDateTest_Success() {
//		String input = "07-07-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiChangeRequestValidator.validateLoanDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//		}
//	}
//
//	@Test
//	public void validateLoanDateTest_Error() {
//		String input = "07-0707-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiChangeRequestValidator.validateLoanDate(input);
//		assertEquals("Date should not be Current Date or Future Date : LOANDATE", errors.get("FUTURE_DATE_ERROR"));
//		assertEquals("Date mustbe in a format of DD-MM-YYYY : LOANDATE", errors.get("DATE_FORMAT"));
//	}

//	@Test
//	public void validateEmiChangedDateTest_Success() {
//		String input = "07-07-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiChangeRequestValidator.validateEmiChangedDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == true);
//		}
//	}

//	@Test
//	public void validateEmiChangedDateTest_Error() {
//		String input = "07-0707-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = emiChangeRequestValidator.validateEmiChangedDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : EMICHANGEDDATE", error);
//		}
//	}

	@Test
	public void validateIncreasedEmiTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateIncreasedEmi(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateIncreasedEmiTest_Error() {
		String input = "800000asdf";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateIncreasedEmi(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INCREASEDEMI", error);
		}
	}

	@Test
	public void validateLoanAmountTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateLoanAmountTest_Error() {
		String input = "800000as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : LOANAMOUNT", error);
		}
	}

	@Test
	public void validateTenureTest_Success() {
		String input = "8";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateTenureTest_Error() {
		String input = "8";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);
		}
	}

	@Test
	public void validateInterestRateTest_Success() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInterestRateTest_Error() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiChangeRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);
		}
	}
	
	}
