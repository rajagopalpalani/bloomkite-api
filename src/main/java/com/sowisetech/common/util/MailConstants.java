package com.sowisetech.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:mail.properties")
public class MailConstants {

	public static final String SIGN = "SIG";
	public static final String PASSWRD_CHANGE = "PASS_CHANGE";
	public static final String PRODUCT_ADD = "PROD_ADD";
	public static final String PRODUCT_MODIFY = "PROD_MODIFY";
	public static final String MODERATE_ARTICLE_POST = "MOD_ARTICLE_POST";
	public static final String ADD_PLAN = "PLAN_ADD";
	public static final String INV_SIGN = "INV_SIGN";
	public static final String BLOGGER_SIGN = "BLOGGER_SIGN";
	public static final String PLAN_SHARE = "PLAN_SHARE";
	public static final String TEST = "TEST";

	public static final String ADVISOR = "Advisor";
	public static final String INVESTOR = "Investor";
	public static final String BLOGGER = "Blogger";
	public static final String CONFIRMATION = "CONFIRMATION";
	public static final String FORGET_PASSWRD = "FORGET_PASSWORD";
	public static final String ON = "ON";
	public static final String OFF = "OFF";

	@Value("${subject}")
	private String subject;
	@Value("${smtp_host}")
	private String smtp_host;
	@Value("${smtp_port}")
	private String smtp_port;
	@Value("${smtp_port}")
	private int smtp_port_num;
	@Value("${smtp_auth}")
	private String smtp_auth;
	@Value("${smtp_startttls}")
	private String smtp_startttls;
	@Value("${smtp_from}")
	private String smtp_from;
	@Value("${smtp_protocol}")
	private String smtp_protocol;
	@Value("${name}")
	private String name;
	@Value("${from}")
	private String from;
	@Value("${SIG_SUBJECT}")
	private String SIG_SUBJECT;
	@Value("${PASS_CHANGE_SUBJECT}")
	private String PASS_CHANGE_SUBJECT;
	@Value("${MOD_ARTICLE_POST}")
	private String MOD_ARTICLE_POST;
	@Value("${PLAN_ADD}")
	private String PLAN_ADD;
	@Value("${url}")
	private String url;
	@Value("${reset_password_url}")
	private String reset_password_url;
	@Value("${startkey}")
	private String startkey;
	@Value("${midkey}")
	private String midkey;
	@Value("${endkey}")
	private String endkey;
	@Value("${link_validity}")
	private long link_validity;
	@Value("${mail_username}")
	private String mail_username;
	@Value("${mail_password}")
	private String mail_password;
	@Value("${product_mail_flag}")
	private String product_mail_flag;

	public int getSmtp_port_num() {
		return smtp_port_num;
	}

	public void setSmtp_port_num(int smtp_port_num) {
		this.smtp_port_num = smtp_port_num;
	}

	public String getMail_username() {
		return mail_username;
	}

	public void setMail_username(String mail_username) {
		this.mail_username = mail_username;
	}

	public String getMail_password() {
		return mail_password;
	}

	public void setMail_password(String mail_password) {
		this.mail_password = mail_password;
	}

	private String fromUser;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSmtp_host() {
		return smtp_host;
	}

	public void setSmtp_host(String smtp_host) {
		this.smtp_host = smtp_host;
	}

	public String getSmtp_port() {
		return smtp_port;
	}

	public void setSmtp_port(String smtp_port) {
		this.smtp_port = smtp_port;
	}

	public String getSmtp_auth() {
		return smtp_auth;
	}

	public void setSmtp_auth(String smtp_auth) {
		this.smtp_auth = smtp_auth;
	}

	public String getSmtp_startttls() {
		return smtp_startttls;
	}

	public void setSmtp_startttls(String smtp_startttls) {
		this.smtp_startttls = smtp_startttls;
	}

	public String getSmtp_from() {
		return smtp_from;
	}

	public void setSmtp_from(String smtp_from) {
		this.smtp_from = smtp_from;
	}

	public String getSmtp_protocol() {
		return smtp_protocol;
	}

	public void setSmtp_protocol(String smtp_protocol) {
		this.smtp_protocol = smtp_protocol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getConfirmation() {
		return CONFIRMATION;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	// public String getSIG() {
	// return SIG;
	// }
	//
	// public void setSIG(String sIG) {
	// SIG = sIG;
	// }

	public String getSIG_SUBJECT() {
		return SIG_SUBJECT;
	}

	public void setSIG_SUBJECT(String sIG_SUBJECT) {
		SIG_SUBJECT = sIG_SUBJECT;
	}

	public String getPASS_CHANGE_SUBJECT() {
		return PASS_CHANGE_SUBJECT;
	}

	public void setPASS_CHANGE_SUBJECT(String pASS_CHANGE_SUBJECT) {
		PASS_CHANGE_SUBJECT = pASS_CHANGE_SUBJECT;
	}

	public String getMOD_ARTICLE_POST() {
		return MOD_ARTICLE_POST;
	}

	public void setMOD_ARTICLE_POST(String mOD_ARTICLE_POST) {
		MOD_ARTICLE_POST = mOD_ARTICLE_POST;
	}

	public String getPLAN_ADD() {
		return PLAN_ADD;
	}

	public void setPLAN_ADD(String pLAN_ADD) {
		PLAN_ADD = pLAN_ADD;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public static String getSign() {
		return SIGN;
	}

	public static String getPasswrdChange() {
		return PASSWRD_CHANGE;
	}

	public static String getProductAdd() {
		return PRODUCT_ADD;
	}

	public static String getProductModify() {
		return PRODUCT_MODIFY;
	}

	public static String getModerateArticlePost() {
		return MODERATE_ARTICLE_POST;
	}

	public static String getAddPlan() {
		return ADD_PLAN;
	}

	public static String getAdvisor() {
		return ADVISOR;
	}

	public static String getInvestor() {
		return INVESTOR;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStartkey() {
		return startkey;
	}

	public void setStartkey(String startkey) {
		this.startkey = startkey;
	}

	public String getMidkey() {
		return midkey;
	}

	public void setMidkey(String midkey) {
		this.midkey = midkey;
	}

	public String getEndkey() {
		return endkey;
	}

	public void setEndkey(String endkey) {
		this.endkey = endkey;
	}

	public String getReset_password_url() {
		return reset_password_url;
	}

	public void setReset_password_url(String reset_password_url) {
		this.reset_password_url = reset_password_url;
	}

	public long getLink_validity() {
		return link_validity;
	}

	public void setLink_validity(long link_validity) {
		this.link_validity = link_validity;
	}

	public String getProduct_mail_flag() {
		return product_mail_flag;
	}

	public void setProduct_mail_flag(String product_mail_flag) {
		this.product_mail_flag = product_mail_flag;
	}

	public String getPlanShare() {
		return PLAN_SHARE;
	}

	public static String getBloggerSign() {
		return BLOGGER_SIGN;
	}

	public static String getBlogger() {
		return BLOGGER;
	}
	
}
