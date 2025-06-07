package com.sowisetech.calc.response;

import java.util.List;

import com.sowisetech.calc.model.Networth;
import com.sowisetech.calc.model.NetworthSummary;

public class NetworthResponse {

	List<Networth> networthList;
	NetworthSummary networthSummary;

	public List<Networth> getNetworthList() {
		return networthList;
	}

	public void setNetworthList(List<Networth> networthList) {
		this.networthList = networthList;
	}

	public NetworthSummary getNetworthSummary() {
		return networthSummary;
	}

	public void setNetworthSummary(NetworthSummary networthSummary) {
		this.networthSummary = networthSummary;
	}

}
