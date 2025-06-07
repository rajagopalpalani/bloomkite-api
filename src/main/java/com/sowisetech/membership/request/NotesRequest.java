package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class NotesRequest {

	String notes_key_1;
	String notes_key_2;

	public String getNotes_key_1() {
		return notes_key_1;
	}

	public void setNotes_key_1(String notes_key_1) {
		this.notes_key_1 = notes_key_1;
	}

	public String getNotes_key_2() {
		return notes_key_2;
	}

	public void setNotes_key_2(String notes_key_2) {
		this.notes_key_2 = notes_key_2;
	}

}
