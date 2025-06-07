package com.sowisetech.common.model;

public class RoleScreenRights {

	private int role_screen_rights_id;
	private int user_role_id;
	private int screen_id;
	private int add_rights;
	private int edit_rights;
	private int view_rights;
	private int delete_rights;

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

}
