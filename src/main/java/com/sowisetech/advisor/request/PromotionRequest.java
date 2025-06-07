package com.sowisetech.advisor.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PromotionRequest {
	private int screenId;
	private String advId;
	List<PromotionReq> promotionReq;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public List<PromotionReq> getPromotionReq() {
		return promotionReq;
	}

	public void setPromotionReq(List<PromotionReq> promotionReq) {
		this.promotionReq = promotionReq;
	}

}
