package com.sowisetech.investor.request;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvInterestRequestValidatorTest {

	@Autowired(required = true)
	InvInterestRequestValidator invInterestRequestValidator;

	@Test
	public void validateScaleTest() {
		String scale = "2";
		HashMap<String, String> errors = new HashMap<String, String>();
		errors = invInterestRequestValidator.validateScale(scale);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);

		}
	}

}
