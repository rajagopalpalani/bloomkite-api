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
public class GoalRequestValidatorTest {

	@Autowired
	GoalRequestValidator goalRequestValidator;

	@Test
	public void validateAnnualInvestmentRateTest() {
		String rate = "5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateAnnualInvestmentRate(rate);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateAnnualInvestmentRateTest_Error() {
		String rate = "aa";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateAnnualInvestmentRate(rate);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : ANNUALINVESTMENTRATE");
		}
	}

	@Test
	public void validateGrowthRateTest() {
		String rate = "5";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGrowthRate(rate);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateGrowthRateTest_Error() {
		String rate = "aa";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGrowthRate(rate);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : GROWTHRATE");
		}
	}

	@Test
	public void validateCurrentAmountTest() {
		String amount = "50000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateCurrentAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateCurrentAmountTest_Error() {
		String amount = "5aa00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateCurrentAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : CURRENTAMOUNT");
		}
	}

	@Test
	public void validateInflationRateTest() {
		String rate = "2.00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateInflationRate(rate);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateInflationRateTest_Error() {
		String rate = "2ab.00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateInflationRate(rate);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : INFLATIONRATE");
		}
	}

	@Test
	public void validateGoalAmountTest() {
		String amount = "200000";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGoalAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateGoalAmountTest_Error() {
		String amount = "2ab00";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGoalAmount(amount);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : GOALAMOUNT");
		}
	}

	@Test
	public void validateTenureTypeTest() {
		String tenureType = "YEAR";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateTenureType(tenureType);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateTenureTypeTest_Error() {
		String tenureType = "Year";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateTenureType(tenureType);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value must be YEAR or MONTH : TENURETYPE");
		}
	}

	@Test
	public void validateTenureTest() {
		String tenureType = "4";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateTenure(tenureType);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateTenureTest_Error() {
		String tenureType = "4ab";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateTenure(tenureType);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-numeric values : TENURE");
		}
	}

	@Test
	public void validateGoalName() {
		String goalName = "Building House";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGoalName(goalName);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateGoalName_Error() {
		String goalName = "";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGoalName(goalName);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value is null or empty : GOALNAME");
		}
	}

	@Test
	public void validateGoalName_Error2() {
		String goalName = "Education1";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = goalRequestValidator.validateGoalName(goalName);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals(error, "Value contains non-alpha characters : GOALNAME");
		}
	}

}
