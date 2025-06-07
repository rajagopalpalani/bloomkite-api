package com.sowisetech.membership.response;

import java.util.List;

import com.sowisetech.membership.model.MemberSubscription;

public class MemberSubscriptionTotalList {

	long totalRecords;
	List<MemberSubscription> memberSubscription;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<MemberSubscription> getMemberSubscription() {
		return memberSubscription;
	}

	public void setMemberSubscription(List<MemberSubscription> memberSubscription) {
		this.memberSubscription = memberSubscription;
	}

}
