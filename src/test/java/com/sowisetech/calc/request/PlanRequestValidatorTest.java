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
public class PlanRequestValidatorTest {

	@Autowired
	PlanRequestValidator planRequestValidator;

	@Test
	public void validateSibilingsTest_Success() {
		String input = "YES";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateSibilings(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateSibilingsTest_Error() {
		String input = "YES123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateSibilings(input);
		assertEquals("Value contains non-alpha characters : SIBILINGS", errors.get("NON_ALPHA"));
		assertEquals("Value should be YES or NO : SIBILINGS", errors.get("YES_OR_NO"));

	}

	@Test
	public void validateGrandParentTest_Success() {
		String input = "YES";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateGrandParent(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateGrandParentTest_Error() {
		String input = "YES23";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateGrandParent(input);
		assertEquals("Value contains non-alpha characters : GRAND_PARENT", errors.get("NON_ALPHA"));
		assertEquals("Value should be YES or NO : GRAND_PARENT", errors.get("YES_OR_NO"));
	}

	@Test
	public void validateChildrenTest_Success() {
		String input = "YES";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateChildren(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateChildrenTest_Error() {
		String input = "1as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateChildren(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value should be YES or NO : CHILDREN", error);
		}
	}

	@Test
	public void validateParentsTest_Success() {
		String input = "YES123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateParents(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
		}
	}

	@Test
	public void validateParentsTest_Error() {
		String input = "YES123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateParents(input);
		assertEquals("Value contains non-alpha characters : PARENTS", errors.get("NON_ALPHA"));
		assertEquals("Value should be YES or NO : PARENTS", errors.get("YES_OR_NO"));
	}

	@Test
	public void validateSpouseTest_Success() {
		String input = "YES";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateSpouse(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateSpouseTest_Error() {
		String input = "YES123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateSpouse(input);
		assertEquals("Value contains non-alpha characters : SPOUSE", errors.get("NON_ALPHA"));
		assertEquals("Value should be YES or NO : SPOUSE", errors.get("YES_OR_NO"));

	}

	@Test
	public void validateSelectedPlanTest_Success() {
		String input = "YES";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateSelectedPlan(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateSelectedPlanTest_Error() {
		String input = "YES123";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateSelectedPlan(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-alpha characters : SELECTED_PLAN", error);
		}
	}

	@Test
	public void validateAgeTest_Success() {
		String input = "12";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateAge(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateAgeTest_Error() {
		String input = "12as";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateAge(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : AGE", error);
		}
	}

	@Test
	public void validateNameTest_Success() {
		String input = "Name";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateName(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateNameTest_Error() {
		String input = "Name";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = planRequestValidator.validateName(input);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-numeric values : TENURE", error);
		}
	}
}
