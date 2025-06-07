package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RiskProfileRequest {

	private int screenId;
	private String referenceId;
	private List<RiskProfileReq> riskProfileReq;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public List<RiskProfileReq> getRiskProfileReq() {
		return riskProfileReq;
	}

	public void setRiskProfileReq(List<RiskProfileReq> riskProfileReq) {
		this.riskProfileReq = riskProfileReq;
	}

}
