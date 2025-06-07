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

import com.sowisetech.calc.util.CalcAppMessages;
import com.sowisetech.calc.util.CalcConstants;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmiCapacityRequestValidatorTest {

	@Autowired
	EmiCapacityRequestValidator emiCapacityRequestValidator;

	@Test
	public void validateInterestRateTest_Success() {
		String input = "10.05";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInterestRateTest_Error() {
		String input = "10.05ad";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateInterestRate(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : INTERESTRATE", error);
		}
	}

	@Test
	public void validateAdditionalIncomeTest_Success() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateAdditionalIncome(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateAdditionalIncomeTest_Error() {
		String input = "10000asd0";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateAdditionalIncome(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : ADDITIONALINCOME", error);
		}
	}

	@Test
	public void validateHouseHoldExpenseTest_Success() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateHouseHoldExpense(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateHouseHoldExpenseTest_Error() {
		String input = "100000ad";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateHouseHoldExpense(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : HOUSEHOLDEXPENSE", error);
		}
	}

	@Test
	public void validateExistingEmiTest_Success() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateExistingEmi(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateExistingEmiTest_Error() {
		String input = "1000dg00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateExistingEmi(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : EXISTINGEMI", error);
		}
	}

	@Test
	public void validateNetFamilyIncomeTest_Success() {
		String input = "100000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateNetFamilyIncome(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateNetFamilyIncomeTest_Error() {
		String input = "10fg0000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateNetFamilyIncome(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : NETFAMILYINCOME", error);
		}
	}

	@Test
	public void validateBackUpTest_Success() {
		String input = "YES";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateBackUp(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateBackUpTest_Error() {
		String input = "YES_No";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateBackUp(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value should be YES or NO : BACKUP", error);
		}
	}

	@Test
	public void validateStabilityTest_Success() {
		String input = "HIGH";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateStability(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateStabilityTest_Error() {
		String input = "HIGH_LOW";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateStability(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value should be HIGH or MEDIUM : STABILITY", error);
		}
	}

	@Test
	public void validateRetirementAgeTest_Success() {
		String input = "58";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateRetirementAge(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateRetirementAgeTest_Error() {
		String input = "58age";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateRetirementAge(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : RETIREMENTAGE", error);
		}
	}

	@Test
	public void validateCurrentAgeTest_Success() {
		String input = "23";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateCurrentAge(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateCurrentAgeTest_Error() {
		String input = "23age";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = emiCapacityRequestValidator.validateCurrentAge(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : CURRENTAGE", error);
		}
	}

}
