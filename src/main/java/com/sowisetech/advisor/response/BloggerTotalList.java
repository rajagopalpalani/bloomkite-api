package com.sowisetech.advisor.response;

import java.util.List;
import com.sowisetech.forum.model.Blogger;

public class BloggerTotalList {
	
	long totalRecords;
	List<Blogger> blogger;
	
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<Blogger> getBlogger() {
		return blogger;
	}
	public void setBlogger(List<Blogger> blogger) {
		this.blogger = blogger;
	}
	
	

}
