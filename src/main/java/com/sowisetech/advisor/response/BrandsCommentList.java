package com.sowisetech.advisor.response;

import java.util.List;

import com.sowisetech.advisor.model.BrandsComment;


public class BrandsCommentList {
	
	long totalRecords;
	List<BrandsComment> brandsCommentList;
	
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<BrandsComment> getBrandsCommentList() {
		return brandsCommentList;
	}
	public void setBrandsCommentList(List<BrandsComment> brandsCommentList) {
		this.brandsCommentList = brandsCommentList;
	}
	
}
