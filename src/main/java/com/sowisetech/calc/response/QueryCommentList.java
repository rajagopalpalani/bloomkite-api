package com.sowisetech.calc.response;

import java.util.List;

import com.sowisetech.calc.model.CalcAnswer;

public class QueryCommentList {

	long totalRecords;
	List<CalcAnswer> queryCommentList;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<CalcAnswer> getQueryCommentList() {
		return queryCommentList;
	}

	public void setQueryCommentList(List<CalcAnswer> queryCommentList) {
		this.queryCommentList = queryCommentList;
	}

}
