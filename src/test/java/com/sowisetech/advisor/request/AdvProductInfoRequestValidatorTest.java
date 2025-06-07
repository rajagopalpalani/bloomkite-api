package com.sowisetech.advisor.request;

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
public class AdvProductInfoRequestValidatorTest {

	@Autowired(required = true)
	AdvProductInfoRequestValidator advProductInfoRequestValidator;


	@Test
	public void validateProductTest() {

		List<AdvProductRequest> products = new ArrayList<AdvProductRequest>();
		AdvProductRequest product = new AdvProductRequest();
		product.setValidity("07-05-2019");
		products.add(product);

		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProductInfoRequestValidator.validateProducts(products);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validateProductTestForCheckProduct() {

		List<AdvProductRequest> products = new ArrayList<AdvProductRequest>();
		AdvProductRequest product = new AdvProductRequest();
		product.setValidity("20-05-2019");
		products.add(product);

		HashMap<String, String> errors = new HashMap<String, String>();
		errors = advProductInfoRequestValidator.validateProducts(products);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			 assertEquals("Value must be a valid date in a format of (mm-dd-yyyy) : PRODUCT", error);
		}
	}
	
}
