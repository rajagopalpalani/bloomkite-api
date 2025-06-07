package com.sowisetech.admin.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ScreenRightsRequest {

	private int screenId;
	private int screen_id;
	private int add_rights;
	private int edit_rights;
	private int view_rights;
	private int delete_rights;
	private List<FieldRightsRequest> fieldRightsRequests;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
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

	public List<FieldRightsRequest> getFieldRightsRequests() {
		return fieldRightsRequests;
	}

	public void setFieldRightsRequests(List<FieldRightsRequest> fieldRightsRequests) {
		this.fieldRightsRequests = fieldRightsRequests;
	}

}
