package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class CancelSubRequest {

	int screenId;
	String advId;
	String sub_id;
	String cancel_at_cycle_end;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public String getCancel_at_cycle_end() {
		return cancel_at_cycle_end;
	}

	public void setCancel_at_cycle_end(String cancel_at_cycle_end) {
		this.cancel_at_cycle_end = cancel_at_cycle_end;
	}

}
