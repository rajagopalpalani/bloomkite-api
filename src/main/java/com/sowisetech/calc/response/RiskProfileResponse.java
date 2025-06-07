package com.sowisetech.calc.response;

import java.util.List;

import com.sowisetech.calc.model.RiskProfile;
import com.sowisetech.calc.model.RiskSummary;

public class RiskProfileResponse {

	List<RiskProfile> riskProfileList;
	RiskSummary riskSummary;

	public List<RiskProfile> getRiskProfileList() {
		return riskProfileList;
	}

	public void setRiskProfileList(List<RiskProfile> riskProfileList) {
		this.riskProfileList = riskProfileList;
	}

	public RiskSummary getRiskSummary() {
		return riskSummary;
	}

	public void setRiskSummary(RiskSummary riskSummary) {
		this.riskSummary = riskSummary;
	}

}
