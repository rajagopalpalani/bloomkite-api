package com.sowisetech.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:mailmessages.properties")
@Component
public class MailMessages {

	@Value("${tech_reason}")
	public String tech_reason;

	@Value("${valid_Reason}")
	public String valid_Reason;

	public String getTech_reason() {
		return tech_reason;
	}

	public void setTech_reason(String tech_reason) {
		this.tech_reason = tech_reason;
	}

	public String getValid_Reason() {
		return valid_Reason;
	}

	public void setValid_Reason(String valid_Reason) {
		this.valid_Reason = valid_Reason;
	}

}
