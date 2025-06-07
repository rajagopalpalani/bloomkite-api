package com.sowisetech.calc.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalcCommonTest {

	@Autowired
	CalcCommon common;
	@Autowired(required = true)
	CalcAppMessages appMessages;

	@Test
	public void nonEmptyCheckTest() {
		// Input value is empty,it will return error message
		String result = common.nonEmptyCheck("", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_empty() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void nonEmptyCheckTest_True() {

		// Input value is nonEmpty
		String result = common.nonEmptyCheck("aaa", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void isAlphaTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isAlpha("AAA4457", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_alpha() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isAlphaTest_True() {

		// Input value contains alpha character
		String result = common.isAlpha("ABC", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void isNumericValuesTest() {

		// Input value contains non numeric values,it return error message
		String result = common.isNumericValues("93424jtr", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_numeric() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isNumericValuesTest_True() {

		// Input value contains numeric values
		String result = common.isNumericValues("93424", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void tenureTypeCheckTest() {

		// tenureType field is must be a characters("YEAR,MONTH")
		// Input value contains other than a specified characters
		String result = common.tenureTypeCheck("year", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getTenuretype_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void tenureTypeCheckTest_True() {

		// tenureType field is must be a characters("YEAR,MONTH")
		// Input value contains specified character
		String result = common.tenureTypeCheck("YEAR", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void dateFormatCheckTest() {

		// dateFormat value must be in a format of dd-mm-yyyy
		// Input value is in wrong format,it will return error message
		String result = common.dateFormatCheck("1996-11-02", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getDate_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void dateFormatCheckTest_True() {

		// dateFormat value must be in a format of dd-mm-yyyy
		// Input value is in dateFormat
		String result = common.dateFormatCheck("02-11-1996", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void isAlphaNumericValuesTest() {

		// AlphaNumeric value contains numbers [0-9] and letters [a-z] both(uppercase
		// and lowercase)
		// Input value contains nonAlphaNumeric values, it will return error message
		String result = common.isAlphaNumericValues("AAA123@", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_alphanumeric() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isAlphaNumericValuesTest_True() {

		// AlphaNumeric value contains numbers [0-9] and letters [a-z] both(uppercase
		// and lowercase)
		// Input value contains AlphaNumeric values
		String result = common.isAlphaNumericValues("AAA123", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void isValidDoubleNumberTest() {

		// Value allow double and decimal values
		// Input value contains wrong values, it will return error message
		String result = common.isValidDoubleNumber("abc", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_numeric() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isValidDoubleNumberTest_True() {

		// Value allow double and decimal values
		// Input value contains double values
		String result = common.isValidDoubleNumber("12.00", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void checkStabilityTest() {

		// stability field is must be a
		// characters("stable|Stable|Fluctuating|fluctuating")
		// Input value contains other than a specified character,it return error message
		String result = common.checkStability("unstable", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getStability_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void checkStabilityTest_True() {

		// stability field is must be a
		// characters("stable|Stable|Fluctuating|fluctuating")
		// Input value contains specified character
		String result = common.checkStability("STABLE", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void checkPredictability() {
		// predictability field is must be a
		// characters(Predictable|predictable|Unpredictable|unpredictable)
		// Input value contains other than a specified character, it return error
		// message
		String result = common.checkPredictability("predict", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPredictabilty_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void checkPredictability_True() {
		// predictability field is must be a
		// characters(Predictable|predictable|Unpredictable|unpredictable)
		// Input value contains specified character
		String result = common.checkPredictability("PREDICTABLE", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void monthYearCheckTest() {

		// dateFormat value must be in a format of dd-mm-yyyy
		// Input value is in wrong format,it will return error message
		String result = common.monthYearCheck("10-07-1996", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getMonth_year_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void monthYearCheckTest_True() {

		// dateFormat value must be in a format of MMM-yyyy
		// Input value is in dateFormat
		String result = common.monthYearCheck("JUL-1996", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void futureDateCurrentDateCheck_True() {

		String result = common.futureDateOrCurrentDateCheck("12-02-2015", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}

	@Test
	public void futureDateCurrentDateCheck() {

		// dateFormat value must be in a format of dd-mm-yyyy
		// Input value is in wrong format,it will return error message
		String result = common.futureDateOrCurrentDateCheck("12-02-2050", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getFuture_date_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}
	@Test
	public void isValidInvestmentType_True() {

		String result = common.isValidInvestmentType("LUMSUM", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}
	@Test
	public void isValidInvestmentType_True_SIP() {

		String result = common.isValidInvestmentType("SIP", CalcConstants.SPACE_WTIH_COLON);
		assertTrue(result.isEmpty() == true);
	}
	@Test
	public void isValidInvestmentType_false() {
		String result = common.isValidInvestmentType("LUMSIP", CalcConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getInvestment_type_error() + CalcConstants.SPACE_WTIH_COLON, result);
	}
}
