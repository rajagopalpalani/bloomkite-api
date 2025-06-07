package com.sowisetech.calc.model;

import java.sql.Timestamp;
import java.util.List;

public class Plan {

	long planId;
	long partyId;
	long parentPartyId;
	String referenceId;
	String name;
	int age;
	String selectedPlan;
	String spouse;
	String father;
	String mother;
	String inLaws;
	String child1;
	String child2;
	String child3;
	String grandParent;
	String sibilings;
	String others;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getParentPartyId() {
		return parentPartyId;
	}

	public void setParentPartyId(long parentPartyId) {
		this.parentPartyId = parentPartyId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(String selectedPlan) {
		this.selectedPlan = selectedPlan;
	}

	public String getSpouse() {
		return spouse;
	}

	public void setSpouse(String spouse) {
		this.spouse = spouse;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getInLaws() {
		return inLaws;
	}

	public void setInLaws(String inLaws) {
		this.inLaws = inLaws;
	}

	public String getChild1() {
		return child1;
	}

	public void setChild1(String child1) {
		this.child1 = child1;
	}

	public String getChild2() {
		return child2;
	}

	public void setChild2(String child2) {
		this.child2 = child2;
	}

	public String getChild3() {
		return child3;
	}

	public void setChild3(String child3) {
		this.child3 = child3;
	}

	public String getGrandParent() {
		return grandParent;
	}

	public void setGrandParent(String grandParent) {
		this.grandParent = grandParent;
	}

	public String getSibilings() {
		return sibilings;
	}

	public void setSibilings(String sibilings) {
		this.sibilings = sibilings;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
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

}
