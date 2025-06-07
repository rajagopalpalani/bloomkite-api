package com.sowisetech.common.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CommonDaoImplTest {

	CommonDaoImpl commonDaoImpl;

	EmbeddedDatabase db;

	@Before
	public void setup() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("db_sql/commonschema.sql").addScript("db_sql/commondata.sql").build();
		commonDaoImpl = new CommonDaoImpl();
		commonDaoImpl.setDataSource(db);
		commonDaoImpl.postConstruct();
	}

	// @Test //Encode decode
	// public void test_addMailMessage_Success() {
	// String to = "aaa@gmail.com";
	// String subject = "testMail";
	// String originalContent = "Welcome";
	// String fromUser = "info@gmail.com";
	// int noOfAttempt = 0;
	// String encryptPass ="Sowise@Ever21";
	// long result = commonDaoImpl.addMailMessage(to, subject, originalContent,
	// fromUser, noOfAttempt,encryptPass);
	// Assert.assertEquals(1, result);
	// }

	@Test
	public void test_updateMailMessage_ifFailed() {
		long IfFailed = 0;
		long messageId = 1;
		int noOfAttempt = 1;
		String reason = "issue";

		int result = commonDaoImpl.updateMailMessage_ifFailed(messageId, IfFailed, noOfAttempt, reason);
		Assert.assertEquals(1, result);

	}
	// @Test // Encode decode
	// public void test_addActivationLink_Success() {
	// int result = commonDaoImpl.addActivationLink("info@gmail.com",
	// "www.bloomkite", "Sowise@Ever21");
	// Assert.assertEquals(1, result);
	// }

	// @Test // Encode decode
	// public void test_fetchLatestKeyByEmailIdAndSub() {
	// String encryptPass = "Sowise@Ever21";
	//
	// String result = commonDaoImpl.fetchLatestKeyByEmailIdAndSub("info@gmail.com",
	// "Activation Email", encryptPass);
	// Assert.assertEquals("c3RhcnQsdmlub3RodmFnYWkxMjlAZ21haWwuY29tLG1pZGRsZSx2aW5vdGgsOTg5ODU0MTIzNCwyMDIwLTEwLTIyIDE3OjU4OjE2Ljc1NSxlbmQ=",
	// result);
	//
	// }

}
