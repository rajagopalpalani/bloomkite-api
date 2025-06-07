package com.sowisetech.admin.request;

public class AdmRiskPortfolioRequest {
	public int riskPortfolioId;
	public String points;
	public String behaviour;
	public int equity;
	public int debt;
	public int cash;
	public int getRiskPortfolioId() {
		return riskPortfolioId;
	}
	public void setRiskPortfolioId(int riskPortfolioId) {
		this.riskPortfolioId = riskPortfolioId;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getBehaviour() {
		return behaviour;
	}
	public void setBehaviour(String behaviour) {
		this.behaviour = behaviour;
	}
	public int getEquity() {
		return equity;
	}
	public void setEquity(int equity) {
		this.equity = equity;
	}
	public int getDebt() {
		return debt;
	}
	public void setDebt(int debt) {
		this.debt = debt;
	}
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}

}
