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
public class PartialPaymentRequestValidatorTest {
	
	@Autowired
	PartialPaymentRequestValidator partialPaymentRequestValidator;

	@Test
	public void validateLoanDateTest_Success() {
		String input = "JUL-2000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateLoanDate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
//	@Test
//	public void validateLoanDateTest_Error() {
//		String input = "07-07as-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = partialPaymentRequestValidator.validateLoanDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : LOANDATE", error);
//		}
//	}

	@Test
	public void validatePartPayDateTest_Success() {
		String input = "JUL-2000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validatePartPayDate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
//	@Test
//	public void validatePartPayDateTest_Error() {
//		String input = "07-07as-2000";
//		HashMap<String, String> errors = new HashMap<String, String>();
//		errors = partialPaymentRequestValidator.validatePartPayDate(input);
//		for (String error : errors.values()) {
//			assertTrue(StringUtils.isEmpty(error) == false);
//			assertEquals("Date mustbe in a format of DD-MM-YYYY : PARTPAYDATE", error);
//		}
//	}

	@Test
	public void validateInterestRateTest_Success() {
		String input = "8.5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateInterestRateTest_Error() {
		String input = "8.5as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);
		}
	}

	@Test
	public void validateLoanAmountTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateLoanAmountTest_Error() {
		String input = "800000as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateLoanAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : LOANAMOUNT", error);
		}
	}
	
	@Test
	public void validatePartPayAmountTest_Success() {
		String input = "800000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validatePartPayAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validatePartPayAmountTest_Error() {
		String input = "800000as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validatePartPayAmount(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : PARTPAYAMOUNT", error);
		}
	}
	@Test
	public void validateTenureTest_Success() {
		String input = "8";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}
	@Test
	public void validateTenureTest_Error() {
		String input = "8s";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = partialPaymentRequestValidator.validateTenure(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : TENURE", error);
		}
	}


}
