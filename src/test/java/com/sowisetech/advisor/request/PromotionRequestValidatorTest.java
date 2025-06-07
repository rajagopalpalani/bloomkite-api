package com.sowisetech.advisor.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PromotionRequestValidatorTest {

	@Autowired(required = true)
	PromotionRequestValidator promotionRequestValidator;

	@Test
	public void validatePromotionsTest() {
		List<PromotionReq> promotionReq = new ArrayList<PromotionReq>();
		PromotionReq promotion = new PromotionReq();
		promotion.setTitle("Upper Crest Award");
		promotionReq.add(promotion);

		HashMap<String, String> errors = new HashMap<String, String>();
		errors = promotionRequestValidator.validatePromotions(promotionReq);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == true);
		}
	}

	@Test
	public void validatePromotionsTestForCheckTitle() {
		List<PromotionReq> promotionReq = new ArrayList<PromotionReq>();
		PromotionReq promotion = new PromotionReq();
		promotion.setTitle("Upper Crest Award567@#");
		promotionReq.add(promotion);

		HashMap<String, String> errors = new HashMap<String, String>();
		errors = promotionRequestValidator.validatePromotions(promotionReq);
		for (String error : errors.values()) {
			assertTrue(StringUtils.isEmpty(error) == false);
			assertEquals("Value contains non-alphaNumeric characters : PROMOTION", error);

		}
	}
}
