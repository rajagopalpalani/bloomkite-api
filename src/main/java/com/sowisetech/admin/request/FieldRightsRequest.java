package com.sowisetech.admin.request;

import org.springframework.stereotype.Component;

@Component
public class FieldRightsRequest {

	private int screenId;
	private int field_id;
	private int add_rights;
	private int edit_rights;
	private int view_rights;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
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

}
