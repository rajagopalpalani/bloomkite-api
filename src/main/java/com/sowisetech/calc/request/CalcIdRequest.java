package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class CalcIdRequest {

	private int screenId;
	private String id;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
