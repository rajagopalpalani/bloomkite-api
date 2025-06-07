package com.sowisetech.advisor.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.sowisetech.advisor.util.AdvisorConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvCommonTest {

	@Autowired(required = true)
	AdvCommon common;

	@Autowired(required = true)
	AdvAppMessages appMessages;

	@Test
	public void percentNumberCheckTest() {

		// Input value is more than hundred, it wil return error message
		String result = common.percentNumberCheck("104", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_invalid_percent() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void positiveNumberCheckTest() {

		// Input value is negative number,it return error message
		String result = common.positiveNumberCheck("-50", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_not_positive() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void nonEmptyCheckTest() {

		// Input value is empty,it return error message
		String result = common.nonEmptyCheck("", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_null_or_empty() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isAlphaTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isAlpha("AAA4457", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_alpha() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void designationCheck() {

		// Input value contains some special characters,it return error message
		String result = common.designationCheck("Software Developer - Business Strategy12",
				AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getCity_error() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isAlphaNumericSpaceTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isAlphaNumericSpace("@#_12!", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_alphaNumeric() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isAlphaNumericTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isAlphaNumeric("@#_!", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_alphaNumeric() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isValidEmailAddressTest() {

		// Input value contains non alpha characters,it return error message
		String result = common.isValidEmailAddress("sss@gmail", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_emailid() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void isNumericValuesTest() {

		// Input value contains non numeric values,it return error message
		String result = common.isNumericValues("93424jtr", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_numeric() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void phoneNumberlengthCheckTest() {

		// Phone Number must have 10 digits
		// Input value contains less than 10 digits, it return error message
		String result = common.phoneNumberlengthCheck("93764414", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPhonenumber_length_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void validPasswordCheckTest() {

		// Password must have One special character,one number and one capital
		// letter.length:min:8,max:12
		// Input value not in a specified format,It return error message
		String result = common.validPasswordCheck("acjhgd", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPassword_format_error() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void pincodelengthCheckTest() {

		// Pincode must have 6 digits
		// Input value contains more than 6 digits, it return error message
		String result = common.pincodelengthCheck("64144584", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPincode_length_error() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void dobCheckTest() {

		// DOB must be in a format yyyy-mm-dd
		// Input Values is in wrong format
		String result = common.dobCheck("1990-02", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getDob_error() + AdvisorConstants.SPACE_WTIH_COLON, result);
	}

	@Test
	public void genderCheckTest() {

		// Gender field is must be a single character("F,M,O,f,m,o")
		// Input value contains other than a specified characters
		String result = common.genderCheck("l", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getGender_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void validPanTest() {
		// PAN number must contains 10 digits,First 5 digits are letters,next 4 digits
		// are numbers and last digit is letter.
		String result = common.validPan("AAA123DDDL", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getPan_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void allowMultipleTextTest() {
		// It allow multiple Alphabet values which is seperated by comma
		String result = common.allowMultipleText("Life insure658,H54ealth insurance",
				AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getText_format_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void isValidExpertiseLevelTest() {
		// value must be 1(low) or 2(medium) or 3(high)
		String result = common.isValidExpertiseLevel("4", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getExpertise_level_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void isValidRemunerationTest() {
		// value must be fee or commision
		String result = common.isValidRemuneration("fee11", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getRemuneration_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void isValidDateTest() {
		// value must be a valid date in a format of dd-mm-yyyy
		String result = common.isValidDate("3245-12-2019", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValidtill_date_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void allowMultipleLegalNoTest() {
		// value must be a valid legal number without any Special character
		String result = common.allowMultipleLegalNo("AAA@3!3245", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getLegalno_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void aboutmeTest() {
		// value should not exceed 300 characters
		String result = common.validateAboutme(
				"My name is Mariah Alissa Valadez. I was born and raised in Corpus Christi, Texas, and attend Foy H. Moody High school as a sophomore. I am not employed, since I am only fifteen years old, though I am a year-round volunteer at Driscoll Children�s Hospital. I volunteer on Thursdays from 5-8pm as the cashier in the Gift Shop. I am attending Del Mar College because I would like to become a Pediatrician in the future, and I realize this will take at least ten years in college, so I�dlike to get as many college credits as I can now. There are many things that I do like, as I am not a picky person. I love the outdoors, when it isn�ttoo humid, to read, and to travel. There are a few things that I don�tlike such as bugs, especially spiders, hot beverages, and hunting, as I am a huge animal lover.",
				AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getAboutme_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void isYearTest() {
		// value must be in a format yyyy eg:1996
		String result = common.isYear("19l96", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getYear_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void isFromToTest() {
		// value must be in a format mm-yyyy to mm-yyyy eg:02-1996 to 05-2000
		String result = common.FromToCheck("02-1996", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getFrom_to_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void isImageTest() {
		// value must be a image path with the extension like .jpg , .png , .gif , .bmp
		String result = common.isImage("image.doc", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getValue_is_not_image() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void degreeCheckTest() {
		// value must be valid degree like B.Com or Bachelor of Commerce
		String result = common.degreeCheck("B.com654", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getDegree_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void fromDateCheckTest() {
		// from date must be in a format Mon-yyy eg: Jan-1996
		String result = common.fromDateCheck("Mar 2012-2012", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getFrom_date_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

	@Test
	public void toDateCheckTest() {
		// To date must be in a format Mon-yyy eg: Jan-1996 or it may be (Present)
		String result = common.toDateCheck("1996-jan", AdvisorConstants.SPACE_WTIH_COLON);
		assertEquals(appMessages.getTo_date_error() + AdvisorConstants.SPACE_WTIH_COLON, result);

	}

}
