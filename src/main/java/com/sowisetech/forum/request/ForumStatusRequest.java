package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ForumStatusRequest {

	// @Column(name = "[ID]")
	// private int forumStatusId;
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
