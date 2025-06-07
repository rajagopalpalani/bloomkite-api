package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class PartyIdRequest {

	private int screenId;
	private long partyId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

}
