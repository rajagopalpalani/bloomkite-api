package com.sowisetech.membership.response;

import java.util.List;

import com.sowisetech.membership.model.MembershipPlan;

public class MembershipPlanTotalList {

	long totalRecords;
	List<MembershipPlan> memberShipPlans;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<MembershipPlan> getMemberShipPlans() {
		return memberShipPlans;
	}

	public void setMemberShipPlans(List<MembershipPlan> memberShipPlans) {
		this.memberShipPlans = memberShipPlans;
	}

}
