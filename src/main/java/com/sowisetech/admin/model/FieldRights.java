package com.sowisetech.admin.model;

import java.sql.Timestamp;

public class FieldRights {

	private int role_field_rights_id;
	private int role_screen_rights_id;
	private int field_id;
	private int add_rights;
	private int edit_rights;
	private int view_rights;
	private String created_by;
	private Timestamp created_date;
	private String updated_by;
	private Timestamp updated_date;

	public int getRole_field_rights_id() {
		return role_field_rights_id;
	}

	public void setRole_field_rights_id(int role_field_rights_id) {
		this.role_field_rights_id = role_field_rights_id;
	}

	public int getRole_screen_rights_id() {
		return role_screen_rights_id;
	}

	public void setRole_screen_rights_id(int role_screen_rights_id) {
		this.role_screen_rights_id = role_screen_rights_id;
	}

	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
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

}
