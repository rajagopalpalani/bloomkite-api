package com.sowisetech.investor.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sowisetech.investor.util.InvestorConstants;
import com.sowisetech.investor.util.InvAppMessages;
import com.sowisetech.investor.util.InvCommon;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvCommonTest {

	@Autowired(required = true)
	InvCommon common;

	@Autowired(required = true)
	InvAppMessages appMessages;

	@Test
	public void percentNumberCheckTest() {

		// Input value is more than hundred, it wil return error message
		String result = common.percentNumberCheck("104", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_invalid_percent() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void positiveNumberCheckTest() {

		// Input value is negative number,it return error message
		String result = common.positiveNumberCheck("-50", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_not_positive() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void nonEmptyCheckTest() {

		// Input value is empty,it return error message
		String result = common.nonEmptyCheck("", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_null_or_empty() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isAlphaTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isAlpha("AAA4457", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_alpha() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isValidEmailAddressTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isValidEmailAddress("sss@gmail", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_emailid() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isNumericValuesTest() {

		// Input value contains non numeric values,it return error message
		String result = common.isNumericValues("93424jtr", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_numeric() + InvestorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void phoneNumberlengthCheckTest() {

		// Phone Number must have 10 digits
		// Input value contains less than 10 digits, it return error message
		String result = common.phoneNumberlengthCheck("93764414", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPhonenumber_length_error() + InvestorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void validPasswordCheckTest() {

		// Password must have One special character,one number and one capital
		// letter.length:min:8,max:12
		// Input value not in a specified format,It return error message
		String result = common.validPasswordCheck("acjhgd", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPassword_format_error() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void pincodelengthCheckTest() {

		// Pincode must have 6 digits
		// Input value contains more than 6 digits, it return error message
		String result = common.pincodelengthCheck("64144584", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPincode_length_error() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void dobCheckTest() {

		// DOB must be in a format yyyy-mm-dd
		// Input Values is in wrong format
		String result = common.dobCheck("1990-02", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getDob_error() + InvestorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void genderCheckTest() {

		// Gender field is must be a single character("F,M,O,f,m,o")
		// Input value contains other than a specified characters
		String result = common.genderCheck("l", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getGender_error() + InvestorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void scaleCheckTest() {

		// Gender field is must be a single digit("1,2,3")
		String result = common.scaleCheck("8", InvestorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getScale_error() + InvestorConstants.SPACE_WTIH_COLON, result);

	}

}
