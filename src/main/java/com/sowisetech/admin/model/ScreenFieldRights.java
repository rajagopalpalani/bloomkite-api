package com.sowisetech.admin.model;

import java.sql.Timestamp;
import java.util.List;

public class ScreenFieldRights {

	private int role_screen_rights_id;
	private int user_role_id;
	private int screen_id;
	private int add_rights;
	private int edit_rights;
	private int view_rights;
	private int delete_rights;
	private String created_by;
	private Timestamp created_date;
	private String updated_by;
	private Timestamp updated_date;

	private List<FieldRights> fieldRights;

	public int getRole_screen_rights_id() {
		return role_screen_rights_id;
	}

	public void setRole_screen_rights_id(int role_screen_rights_id) {
		this.role_screen_rights_id = role_screen_rights_id;
	}

	public int getUser_role_id() {
		return user_role_id;
	}

	public void setUser_role_id(int user_role_id) {
		this.user_role_id = user_role_id;
	}

	public int getScreen_id() {
		return screen_id;
	}

	public void setScreen_id(int screen_id) {
		this.screen_id = screen_id;
	}

	public int getAdd_rights() {
		return add_rights;
	}

	public void setAdd_rights(int add_rights) {
		this.add_rights = add_rights;
	}

	public int getEdit_rights() {
		return edit_rights;
	}

	public void setEdit_rights(int edit_rights) {
		this.edit_rights = edit_rights;
	}

	public int getView_rights() {
		return view_rights;
	}

	public void setView_rights(int view_rights) {
		this.view_rights = view_rights;
	}

	public int getDelete_rights() {
		return delete_rights;
	}

	public void setDelete_rights(int delete_rights) {
		this.delete_rights = delete_rights;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Timestamp getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Timestamp created_date) {
		this.created_date = created_date;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public Timestamp getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Timestamp updated_date) {
		this.updated_date = updated_date;
	}

	public List<FieldRights> getFieldRights() {
		return fieldRights;
	}

	public void setFieldRights(List<FieldRights> fieldRights) {
		this.fieldRights = fieldRights;
	}

}
