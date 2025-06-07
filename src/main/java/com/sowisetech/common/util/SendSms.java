package com.sowisetech.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.sowisetech.common.service.CommonService;

@Component
public class SendSms {

	@Autowired
	private SmsConstants smsConstants;
	@Autowired
	CommonService commonService;
	// HttpURLConnection conn;
	private static final Logger logger = LoggerFactory.getLogger(SendSms.class);

	// private SendSms() {
	// try {
	// conn = (HttpURLConnection) new
	// URL("https://api.textlocal.in/send/?").openConnection();
	// logger.error("Connection created");
	// } catch (IOException e) {
	// logger.error("Connection creation issue");
	// e.printStackTrace();
	// }
	// }

	private String replaceValue(String key, String[] args) {
		String originalMessage = null;
		if (key.equals(SmsConstants.OTP)) {
			String content = smsConstants.getOtp_message();
			originalMessage = String.format(content, args[0]);
		}
		return originalMessage;
	}

	public String sendSms(List<String> phoneNumber, String key, String... args) {
		try {
			// Construct data
			String originalMessage = replaceValue(key, args);
			String number = String.join(",", phoneNumber);

			String sender = "&sender=" + smsConstants.getSender();
			String apiKey = "apikey=" + smsConstants.getApikey();
			String message = "&message=" + originalMessage;
			String numbers = "&numbers=" + number;

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS " + e);
			return "Error " + e;
		}
	}
}
