package com.sowisetech.advisor.model;

import java.util.List;

public class StateCity {

	private long stateId;
	private String state;
	List<CityPincode> cities;

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<CityPincode> getCities() {
		return cities;
	}

	public void setCities(List<CityPincode> cities) {
		this.cities = cities;
	}

}
