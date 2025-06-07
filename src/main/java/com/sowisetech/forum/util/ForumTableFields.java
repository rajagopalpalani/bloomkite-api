package com.sowisetech.forum.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:forumtablefields.properties")
public class ForumTableFields {

	@Value("${forumstatus_inprogress}")
	public String forumstatus_inprogress;

	@Value("${forumstatus_approved}")
	public String forumstatus_approved;

	@Value("${delete_flag_N}")
	public String delete_flag_N;

	@Value("${delete_flag_Y}")
	public String delete_flag_Y;

	@Value("${up}")
	public String up;

	@Value("${down}")
	public String down;

	@Value("${role_advisor}")
	public String role_advisor;

	@Value("${forum_status_rejected}")
	public String forum_status_rejected;

	@Value("${articlestatus_active}")
	public String articlestatus_active;

	@Value("${nonindividual_role}")
	public String nonindividual_role;

	public String getForumstatus_inprogress() {
		return forumstatus_inprogress;
	}

	public void setForumstatus_inprogress(String forumstatus_inprogress) {
		this.forumstatus_inprogress = forumstatus_inprogress;
	}

	public String getForumstatus_approved() {
		return forumstatus_approved;
	}

	public void setForumstatus_approved(String forumstatus_approved) {
		this.forumstatus_approved = forumstatus_approved;
	}

	public String getDelete_flag_N() {
		return delete_flag_N;
	}

	public void setDelete_flag_N(String delete_flag_N) {
		this.delete_flag_N = delete_flag_N;
	}

	public String getDelete_flag_Y() {
		return delete_flag_Y;
	}

	public void setDelete_flag_Y(String delete_flag_Y) {
		this.delete_flag_Y = delete_flag_Y;
	}

	public String getUp() {
		return up;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getRole_advisor() {
		return role_advisor;
	}

	public void setRole_advisor(String role_advisor) {
		this.role_advisor = role_advisor;
	}

	public String getForum_status_rejected() {
		return forum_status_rejected;
	}

	public void setForum_status_rejected(String forum_status_rejected) {
		this.forum_status_rejected = forum_status_rejected;
	}

	public String getArticlestatus_active() {
		return articlestatus_active;
	}

	public void setArticlestatus_active(String articlestatus_active) {
		this.articlestatus_active = articlestatus_active;
	}

	public String getNonindividual_role() {
		return nonindividual_role;
	}

	public void setNonindividual_role(String nonindividual_role) {
		this.nonindividual_role = nonindividual_role;
	}

}
