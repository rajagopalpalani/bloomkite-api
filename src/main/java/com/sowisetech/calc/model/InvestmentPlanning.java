package com.sowisetech.calc.model;

public class InvestmentPlanning {

	FutureValue futureValue;
	TargetValue targetValue;
	RateFinder rateFinder;
	TenureFinder tenureFinder;

	public FutureValue getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(FutureValue futureValue) {
		this.futureValue = futureValue;
	}

	public TargetValue getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(TargetValue targetValue) {
		this.targetValue = targetValue;
	}

	public RateFinder getRateFinder() {
		return rateFinder;
	}

	public void setRateFinder(RateFinder rateFinder) {
		this.rateFinder = rateFinder;
	}

	public TenureFinder getTenureFinder() {
		return tenureFinder;
	}

	public void setTenureFinder(TenureFinder tenureFinder) {
		this.tenureFinder = tenureFinder;
	}

}
