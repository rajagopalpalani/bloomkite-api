package com.sowisetech.common.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sowisetech.advisor.service.AdvisorServiceImpl;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.common.dao.CommonDao;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

public class CommonServiceImplTest {

	@InjectMocks
	private CommonServiceImpl commonServiceImpl;

	private MockMvc mockMvc;
	@Mock
	CommonDao commonDao;
	@Mock
	AuthDao authDao;

	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(commonServiceImpl).build();
	}

	@Test
	public void test_addMailMessage_Success() throws Exception {
		String to = "aaa@gmail.com";
		String subject = "testMail";
		String originalContent = "Welcome";
		String fromUser = "info@gmail.com";
		int noOfAttempt = 0;

		when(commonDao.addMailMessage(to, subject, originalContent, fromUser, noOfAttempt,
				advTableFields.getEncryption_password())).thenReturn(1L);
		long result = commonServiceImpl.addMailMessage(to, subject, originalContent, fromUser, noOfAttempt);
		Assert.assertEquals(1, result);
		verify(commonDao, times(1)).addMailMessage(to, subject, originalContent, fromUser, noOfAttempt,
				advTableFields.getEncryption_password());
		verifyNoMoreInteractions(commonDao);
	}

	@Test
	public void test_updateMailMessage_ifFailed() throws Exception {
		long IfFailed = advTableFields.getIfFailed();
		long messageId = 1;
		int noOfAttempt = 1;
		String reason = "";

		when(commonDao.updateMailMessage_ifFailed(messageId, IfFailed, noOfAttempt, reason)).thenReturn(1);
		int result = commonServiceImpl.updateMailMessage_ifFailed(messageId, noOfAttempt, reason);
		Assert.assertEquals(1, result);
		verify(commonDao, times(1)).updateMailMessage_ifFailed(messageId, IfFailed, noOfAttempt, reason);
		verifyNoMoreInteractions(commonDao);
	}

	@Test
	public void test_fetchScreenRightsByUserRoleId() throws Exception {
		long user_role_id = 1;
		List<RoleScreenRights> roleScreenRights = new ArrayList<RoleScreenRights>();
		RoleScreenRights screenRights = new RoleScreenRights();
		screenRights.setRole_screen_rights_id(1);
		roleScreenRights.add(screenRights);

		when(authDao.fetchScreenRightsByRoleId(user_role_id)).thenReturn(roleScreenRights);
		List<RoleScreenRights> result = commonServiceImpl.fetchScreenRightsByRoleId(user_role_id);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.get(0).getRole_screen_rights_id());
		verify(authDao, times(1)).fetchScreenRightsByRoleId(user_role_id);
		verifyNoMoreInteractions(authDao);
	}

	@Test
	public void test_fetchFieldRights() throws Exception {
		int roleScreenId = 1;
		List<RoleFieldRights> roleFieldRights = new ArrayList<RoleFieldRights>();
		RoleFieldRights fieldRights = new RoleFieldRights();
		fieldRights.setRole_field_rights_id(1);
		roleFieldRights.add(fieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		when(authDao.fetchFieldRights(screenIds)).thenReturn(roleFieldRights);
		List<RoleFieldRights> result = commonServiceImpl.fetchFieldRights(screenIds);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.get(0).getRole_field_rights_id());
		verify(authDao, times(1)).fetchFieldRights(screenIds);
		verifyNoMoreInteractions(authDao);
	}

	@Test
	public void test_fetchUserRoleIdByPartyId() throws Exception {
		long partyId = 1;
		int roleId = 1;
		when(authDao.fetchUserRoleIdByPartyId(partyId, roleId)).thenReturn(1);
		int result = commonServiceImpl.fetchUserRoleIdByPartyId(partyId, roleId);
		Assert.assertEquals(1, result);
		verify(authDao, times(1)).fetchUserRoleIdByPartyId(partyId, roleId);
		verifyNoMoreInteractions(authDao);
	}

	@Test
	public void test_fetchUserRoleIdByPartyIdError() throws Exception {
		long partyId = 0;
		int roleId = 14;
		when(authDao.fetchUserRoleIdByPartyId(partyId, roleId)).thenReturn(0);
		int result = commonServiceImpl.fetchUserRoleIdByPartyId(partyId, roleId);
		Assert.assertEquals(0, result);
		verify(authDao, times(1)).fetchUserRoleIdByPartyId(partyId, roleId);
		verifyNoMoreInteractions(authDao);
	}

	@Test
	public void test_addActivationLink() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(commonDao.addActivationLink("info@gmail.com", "www.bloom.com", "verifyKey", "sub", encryptPass))
				.thenReturn(1);
		int result = commonServiceImpl.addActivationLink("info@gmail.com", "www.bloom.com", "verifyKey", "sub");
		Assert.assertEquals(1, result);
		verify(commonDao, times(1)).addActivationLink("info@gmail.com", "www.bloom.com", "verifyKey", "sub",
				encryptPass);
		verifyNoMoreInteractions(commonDao);
	}

	@Test
	public void test_addActivationLink_Error() throws Exception {
		when(commonDao.addActivationLink("info@gmail.com", "www.bloom.com", "verifyKey", "sub", "encryptPass"))
				.thenReturn(0);
		int result = commonServiceImpl.addActivationLink("info@gmail.com", "www.bloom.com", "verifyKey", "sub");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchLatestKeyByEmailIdAndSub() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		String key = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXlhbnRoaUBzb3dpc2V0ZWNo";
		when(commonDao.fetchLatestKeyByEmailIdAndSub("info@gmail.com", "sub", encryptPass)).thenReturn(key);
		String result = commonServiceImpl.fetchLatestKeyByEmailIdAndSub("info@gmail.com", "sub");
		Assert.assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZXlhbnRoaUBzb3dpc2V0ZWNo", result);
		verify(commonDao, times(1)).fetchLatestKeyByEmailIdAndSub("info@gmail.com", "sub", encryptPass);
		verifyNoMoreInteractions(commonDao);
	}

	@Test
	public void test_fetchLatestKeyByEmailIdAndSub_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(commonDao.fetchLatestKeyByEmailIdAndSub("aaa", "sub", encryptPass)).thenReturn(null);
		String result = commonServiceImpl.fetchLatestKeyByEmailIdAndSub("aaa", "sub");
		Assert.assertEquals(null, result);
		verify(commonDao, times(1)).fetchLatestKeyByEmailIdAndSub("aaa", "sub", encryptPass);
		verifyNoMoreInteractions(commonDao);
	}

	@Test
	public void test_fetchUserRoleByUserId() throws Exception {
		long partyId = 1;
		List<User_role> userRole = new ArrayList<User_role>();
		User_role role1 = new User_role();
		role1.setRole_id(1);
		userRole.add(role1);

		when(authDao.fetchUserRoleByUserId(partyId)).thenReturn(userRole);
		List<User_role> result = commonServiceImpl.fetchUserRoleByUserId(partyId);
		Assert.assertEquals(1, result.size());
		verify(authDao, times(1)).fetchUserRoleByUserId(partyId);
		verifyNoMoreInteractions(authDao);
	}

	@Test
	public void test_fetchUserRoleByUserId_Error() throws Exception {
		long partyId = 0;
		List<User_role> userRole = new ArrayList<User_role>();

		when(authDao.fetchUserRoleByUserId(partyId)).thenReturn(userRole);
		List<User_role> result = commonServiceImpl.fetchUserRoleByUserId(partyId);
		Assert.assertEquals(0, result.size());
		verify(authDao, times(1)).fetchUserRoleByUserId(partyId);
		verifyNoMoreInteractions(authDao);
	}

	@Test
	public void test_fetchScreenCodeByScreenId() throws Exception {
		int screenId = 1;
		when(authDao.fetchScreenCodeByScreenId(screenId)).thenReturn("S1");
		String result = commonServiceImpl.fetchScreenCodeByScreenId(screenId);
		Assert.assertEquals("S1", result);
		verify(authDao, times(1)).fetchScreenCodeByScreenId(screenId);
		verifyNoMoreInteractions(authDao);

	}

	@Test
	public void test_fetchScreenCodeByScreenId_Error() throws Exception {
		int screenId = 0;
		when(authDao.fetchScreenCodeByScreenId(screenId)).thenReturn("null");
		String result = commonServiceImpl.fetchScreenCodeByScreenId(screenId);
		Assert.assertEquals("null", result);
		verify(authDao, times(1)).fetchScreenCodeByScreenId(screenId);
		verifyNoMoreInteractions(authDao);

	}

	@Test
	public void test_fetchScreenIdsByStartWithScreenCode() throws Exception {
		List<Integer> screenIds = new ArrayList<Integer>();
		int screenId1 = 1;
		int screenId2 = 2;
		screenIds.add(screenId1);
		screenIds.add(screenId2);

		String screenCode = "S1";
		when(authDao.fetchScreenIdsByStartWithScreenCode(screenCode)).thenReturn(screenIds);
		List<Integer> result = commonServiceImpl.fetchScreenIdsByStartWithScreenCode(screenCode);
		Assert.assertEquals(2, result.size());
		verify(authDao, times(1)).fetchScreenIdsByStartWithScreenCode(screenCode);
		verifyNoMoreInteractions(authDao);

	}

	@Test
	public void test_fetchScreenIdsByStartWithScreenCode_Error() throws Exception {
		List<Integer> screenIds = new ArrayList<Integer>();

		String screenCode = "S1";
		when(authDao.fetchScreenIdsByStartWithScreenCode(screenCode)).thenReturn(screenIds);
		List<Integer> result = commonServiceImpl.fetchScreenIdsByStartWithScreenCode(screenCode);
		Assert.assertEquals(0, result.size());
		verify(authDao, times(1)).fetchScreenIdsByStartWithScreenCode(screenCode);
		verifyNoMoreInteractions(authDao);

	}

}
