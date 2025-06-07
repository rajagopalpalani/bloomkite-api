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
public class CashFlowRequestValidatorTest {

	 @Autowired
	 CashFlowRequestValidator cashFlowRequestValidator;
	
	@Test
	public void validateDateTest() {
		String date = "AUG-1996";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = cashFlowRequestValidator.validateDate(date);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateDateTest_Error() {
		String date = "1999-08-16";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = cashFlowRequestValidator.validateDate(date);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value must be MMM-YYYY eg: JAN-2019 : DATE");
		}
	}

	@Test
	public void validateActualAmountTest() {
		String amount = "240000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = cashFlowRequestValidator.validateActualAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateActualAmountTest_Error() {
		String amount = "24abc00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = cashFlowRequestValidator.validateActualAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : ACTUALAMOUNT");
		}
	}

	@Test
	public void validateBudgetAmountTest() {
		String amount = "250000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = cashFlowRequestValidator.validateBudgetAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateBudgetAmountTest_Error() {
		String amount = "25abc00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = cashFlowRequestValidator.validateBudgetAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : BUDGETAMOUNT");
		}
	}
}
