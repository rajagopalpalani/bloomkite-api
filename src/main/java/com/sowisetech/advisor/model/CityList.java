package com.sowisetech.advisor.model;

import java.util.List;

public class CityList {

//	private Long cityId;
	private String city;
	private String stateId;
	private String state;
	List<String> pincodes;

//	public Long getCityId() {
//		return cityId;
//	}
//
//	public void setCityId(Long cityId) {
//		this.cityId = cityId;
//	}

	public String getState() {
		return state;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getPincodes() {
		return pincodes;
	}

	public void setPincodes(List<String> pincodes) {
		this.pincodes = pincodes;
	}

}
