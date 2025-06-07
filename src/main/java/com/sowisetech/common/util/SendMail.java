package com.sowisetech.common.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sowisetech.common.service.CommonService;

@Component
@PropertySource("classpath:mail.properties")
public class SendMail {

	// @Autowired
	// private JavaMailSender javaMailSender;
	@Autowired
	private MailMessages mailMessages;
	@Autowired
	CommonService commonService;
	@Autowired
	private Environment env;
	@Autowired
	private MailConstants mailConstants;
	@Autowired
	private TemplateEngine templateEngine;

	private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

	private String replaceValue(List<String> toUsers, String content, String key, String... args) {
		String orginalContent = null;
		if (key.equals(MailConstants.SIGN)) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String verifyKey = verificationKey(toUsers.get(0), args[0], args[1], timestamp);
			String url = mailConstants.getUrl() + verifyKey;
			Context context = new Context();
			context.setVariable("name", args[0]);
			context.setVariable("link", url);
			if (args.length == 5  && args[4] != null) {
				context.setVariable("corpName", args[4]);
			}
			if (args.length == 4 && args[3] != null && MailConstants.INV_SIGN.equals(args[3])) {
				orginalContent = templateEngine.process("Welcome-MailTemplate-Inv", context);
			} else if (args.length == 5 && args[3] != null && "TEAM".equals(args[3])) {
				orginalContent = templateEngine.process("Welcome-MailTemplate-Team", context);
			} else {
				orginalContent = templateEngine.process("Welcome-MailTemplate", context);
			}
			logger.info("Save activation link in table");
			String subject = getMailText(key + "_SUBJECT");
			commonService.addActivationLink(toUsers.get(0), url, verifyKey, subject);
		}
		if (key.equals(MailConstants.FORGET_PASSWRD)) {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String verifyKey = forgetPasswordKey(args[0], args[1], args[2], timestamp);
			String url = mailConstants.getReset_password_url() + verifyKey;
			Context context = new Context();
			context.setVariable("name", args[0]);
			context.setVariable("link", url);
			orginalContent = templateEngine.process("Password-MailTemplate", context);
			logger.info("Save forgetpassword link in table");
			String subject = getMailText(key + "_SUBJECT");
			commonService.addActivationLink(toUsers.get(0), url, verifyKey, subject);
		}
		if (key.equals(MailConstants.PASSWRD_CHANGE)) {
			orginalContent = String.format(content, args[0], args[1]);
			// Context context = new Context();
			// context.setVariable("name", args[0]);
			// orginalContent = templateEngine.process("Password-MailTemplate", context);
		}
		if (key.equals(MailConstants.PRODUCT_ADD)) {
			Context context = new Context();
			context.setVariable("name", args[0]);
			orginalContent = templateEngine.process("Product-MailTemplate", context);
		}
		if (key.equals(MailConstants.CONFIRMATION)) {
			Context context = new Context();
			context.setVariable("name", args[0]);
			if (args.length == 2 && args[1] != null && MailConstants.INV_SIGN.equals(args[1])) {
				orginalContent = templateEngine.process("Confirmation-MailTemplate-Inv", context);
			} else {
				orginalContent = templateEngine.process("Confirmation-MailTemplate", context);
			}
		}
		if (key.equals(MailConstants.PRODUCT_MODIFY)) {
			Context context = new Context();
			context.setVariable("name", args[0]);
			orginalContent = templateEngine.process("Product-MailTemplate", context);
		}
		if (key.equals(MailConstants.MODERATE_ARTICLE_POST)) {
			orginalContent = String.format(content);
		}
		if (key.equals(MailConstants.ADD_PLAN)) {
			orginalContent = String.format(content, args[0], args[1]);
		}
		if (key.equals(MailConstants.TEST)) {
			Context context = new Context();
			orginalContent = templateEngine.process(args[0], context);
		}
		return orginalContent;
	}

	private String forgetPasswordKey(String name, String emailId, String phoneNumber, Timestamp timestamp) {
		String key = mailConstants.getStartkey() + "," + name + "," + mailConstants.getMidkey() + "," + emailId + ","
				+ phoneNumber + "," + timestamp + "," + mailConstants.getEndkey();
		String encodedKey = encodeKey(key);
		return encodedKey;
	}

	private String verificationKey(String emailId, String phoneNumber, String password, Timestamp timestamp) {
		String key = mailConstants.getStartkey() + "," + emailId + "," + mailConstants.getMidkey() + "," + phoneNumber
				+ "," + password + "," + timestamp + "," + mailConstants.getEndkey();
		String encodedKey = encodeKey(key);
		return encodedKey;
	}

	private String encodeKey(String key) {
		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(key.getBytes());
		return encodedString;
	}

	public void sendMailMessage(String key, List<String> toUsers, String fromUser, FileSystemResource file,
			String... args) {
		int noOfAttempt = 0;
		String toUser = convertListToString(toUsers);
		String content = getMailText(key);
		String subject = getMailText(key + "_SUBJECT");
		String originalContent = replaceValue(toUsers, content, key, args);
		List<Long> messageIds = new ArrayList<>();
		if (!key.equals("TEST")) {
			for (String to : toUsers) {
				logger.info("add into MailMessage");
				long mailMessageId = commonService.addMailMessage(to, subject, originalContent, fromUser, noOfAttempt);
				messageIds.add(mailMessageId);
			}
		}
		logger.info("validate method");
		boolean result = validate(key, toUsers, subject, originalContent, fromUser);
		if (result) {
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", mailConstants.getSmtp_host());
			properties.put("mail.smtp.port", mailConstants.getSmtp_port());
			properties.put("mail.smtp.auth", mailConstants.getSmtp_auth());
			properties.put("mail.smtp.starttls.enable", mailConstants.getSmtp_startttls());
			properties.put("mail.smtp.from", mailConstants.getSmtp_from());
			// properties.put("mail.smtp.ssl.protocols", mailConstants.getSmtp_protocol());
			Session session = Session.getDefaultInstance(properties);
			logger.info("Inside of MailSender");
			MimeMessage message = new MimeMessage(session);
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(new InternetAddress(mailConstants.getName() + "<" + mailConstants.getFrom() + ">"));
				helper.setTo(InternetAddress.parse(toUser));
				helper.setText(originalContent, true);
				helper.setSubject(subject);
				if (file != null) {
					helper.addAttachment(file.getFilename(), file);
				}
				Transport transport = session.getTransport("smtp");
				transport.connect(mailConstants.getSmtp_host(), mailConstants.getSmtp_port_num(),
						mailConstants.getMail_username(), mailConstants.getMail_password());
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
				// javaMailSender.send(message);
			} catch (Exception e) {
				//
				logger.error("Technical Error occurred while sending mail..");
				++noOfAttempt;
				String reason = mailMessages.getTech_reason();
				System.out.println(reason);
				if (!key.equals("TEST")) {
					for (long messageId : messageIds) {
						commonService.updateMailMessage_ifFailed(messageId, noOfAttempt, reason);
					}
				}
				logger.error(e.getMessage());
			}
		} else {
			//
			logger.error("Validation Error occurred while sending mail..");
			++noOfAttempt;
			String reason = mailMessages.getValid_Reason();
			if (!key.equals("TEST")) {
				for (long messageId : messageIds) {
					commonService.updateMailMessage_ifFailed(messageId, noOfAttempt, reason);
				}
			}
		}

	}

	public String getMailText(String key) {
		String text = env.getProperty(key);
		return text;
	}

	private boolean validate(String key, List<String> toUsers, String subject, String originalContent,
			String fromUser) {

		if (subject == null || subject.isEmpty()) {
			logger.error("subject is empty unable to process the mail content");
			return false;
		}
		if (originalContent == null || originalContent.isEmpty()) {
			logger.error("originalContent is empty unable to process the mail content");
			return false;
		}
		if (key == null || key.isEmpty()) {
			logger.error("key is empty unable to process the mail content");
			return false;
		}
		if (toUsers == null || toUsers.size() == 0) {
			logger.error("to address is empty unable to process the mail content");
			return false;
		}
		return true;
	}

	private String convertListToString(List<String> toUsers) {
		String toUser = String.join(",", toUsers);
		return toUser;
	}

}
