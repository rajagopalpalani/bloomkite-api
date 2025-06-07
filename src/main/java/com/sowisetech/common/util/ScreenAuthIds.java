package com.sowisetech.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:screenrights.properties")
public class ScreenAuthIds {
	
	@Value("${screen_id}")
	public String screen_id;

	public String getScreen_id() {
		return screen_id;
	}

	public void setScreen_id(String screen_id) {
		this.screen_id = screen_id;
	}
}
