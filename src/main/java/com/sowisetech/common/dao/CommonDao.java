package com.sowisetech.common.dao;

public interface CommonDao {

	long addMailMessage(String to, String subject, String originalContent, String fromUser, int noOfAttempt, String encryptPass);

	int updateMailMessage_ifFailed(long messageId, long ifFailed, int noOfAttempt, String reason);

	int addActivationLink(String emailId, String url,String verifykey,String subject,String encryptPass);

	String fetchLatestKeyByEmailIdAndSub(String emailId, String mailSub, String encryptPass);

}
