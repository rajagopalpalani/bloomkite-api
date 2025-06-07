package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PlanRequest {

	int screenId;
	long partyId;
	long parentPartyId;
	String name;
	String age;
	List<String> selectedPlan;
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

	public long getParentPartyId() {
		return parentPartyId;
	}

	public void setParentPartyId(long parentPartyId) {
		this.parentPartyId = parentPartyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public List<String> getSelectedPlan() {
		return selectedPlan;
	}

	public void setSelectedPlan(List<String> selectedPlan) {
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

}
