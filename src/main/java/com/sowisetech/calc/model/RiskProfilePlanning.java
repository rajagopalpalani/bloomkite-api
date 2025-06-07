package com.sowisetech.calc.model;

import java.util.List;

public class RiskProfilePlanning {

	List<RiskProfile> riskProfile;
	RiskSummary riskSummary;

	public List<RiskProfile> getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(List<RiskProfile> riskProfile) {
		this.riskProfile = riskProfile;
	}

	public RiskSummary getRiskSummary() {
		return riskSummary;
	}

	public void setRiskSummary(RiskSummary riskSummary) {
		this.riskSummary = riskSummary;
	}

}
