package com.sowisetech.advisor.model;

import java.sql.Timestamp;

public class GeneratedOtp {

	private int id;
	private long partyId;
	private String otp;
	private Timestamp created;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

}
