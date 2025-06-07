package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CalcQueryRequest {

	private int screenId;
	private List<Long> postedToPartyId;
	private long partyId;
	private String url;
	private String referenceId;
	private List<String> plans;
	private boolean checked;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public List<Long> getPostedToPartyId() {
		return postedToPartyId;
	}

	public void setPostedToPartyId(List<Long> postedToPartyId) {
		this.postedToPartyId = postedToPartyId;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public List<String> getPlans() {
		return plans;
	}

	public void setPlans(List<String> plans) {
		this.plans = plans;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	

	
}
