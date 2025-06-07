package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class RiskSummary {

	long riskSummaryId;
	String behaviour;
	int eqty_alloc;
	int debt_alloc;
	int cash_alloc;
	String referenceId;
	private String created_by;
	private String updated_by;
	private Timestamp created;
	private Timestamp updated;

	public long getRiskSummaryId() {
		return riskSummaryId;
	}

	public void setRiskSummaryId(long riskSummaryId) {
		this.riskSummaryId = riskSummaryId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(String behaviour) {
		this.behaviour = behaviour;
	}

	public int getEqty_alloc() {
		return eqty_alloc;
	}

	public void setEqty_alloc(int eqty_alloc) {
		this.eqty_alloc = eqty_alloc;
	}

	public int getDebt_alloc() {
		return debt_alloc;
	}

	public void setDebt_alloc(int debt_alloc) {
		this.debt_alloc = debt_alloc;
	}

	public int getCash_alloc() {
		return cash_alloc;
	}

	public void setCash_alloc(int cash_alloc) {
		this.cash_alloc = cash_alloc;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

}
