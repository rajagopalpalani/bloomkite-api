package com.sowisetech.admin.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sowisetech.admin.dao.AdminAuthDao;
import com.sowisetech.admin.dao.AdminDao;
import com.sowisetech.admin.model.Account;
import com.sowisetech.admin.model.Acctype;
import com.sowisetech.admin.model.AdmFollower;
import com.sowisetech.admin.model.AdmPriority;
import com.sowisetech.admin.model.AdmRiskPortfolio;
import com.sowisetech.admin.model.Admin;
import com.sowisetech.admin.model.ArticleStatus;
import com.sowisetech.admin.model.Brand;
import com.sowisetech.admin.model.CashFlowItem;
import com.sowisetech.admin.model.CashFlowItemType;
import com.sowisetech.admin.model.City;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.InsuranceItem;
import com.sowisetech.admin.model.License;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Remuneration;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.ScreenFieldRights;
import com.sowisetech.admin.model.Service;
import com.sowisetech.admin.model.State;
import com.sowisetech.admin.model.Urgency;
import com.sowisetech.admin.model.UserType;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.model.Votetype;
import com.sowisetech.admin.model.Workflowstatus;
import com.sowisetech.admin.request.InsuranceItemRequest;
import com.sowisetech.admin.util.AdmTableFields;
import com.sowisetech.admin.model.Product;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.common.util.AdminSignin;

public class AdminServiceImplTest {

	@InjectMocks
	private AdminServiceImpl adminServiceImpl;

	private MockMvc mockMvc;
	@Autowired(required = true)
	@Spy
	AdmTableFields adminTableFields;

	@Mock
	private AdminAuthDao adminAuthDao;
	@Mock
	private AdminDao adminDao;
	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;

	@Mock
	SecurityContextHolder mockSecurityContext;
	@Autowired(required = true)
	@Spy
	AdminSignin adminSignin;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminServiceImpl).build();
	}

	@Test
	public void test_addScreenRightsFieldRights_Success() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();

		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addScreenRights(screenFieldRights)).thenReturn(1);
		when(adminAuthDao.addFieldRights(fieldRights)).thenReturn(1);
		int result = adminServiceImpl.addScreenRightsFieldRights(screenFieldRightsList);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addScreenRightsFieldRights_Screen_Error() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();

		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addScreenRights(screenFieldRights)).thenReturn(0);
		int result = adminServiceImpl.addScreenRightsFieldRights(screenFieldRightsList);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_addScreenRightsFieldRights_Error() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();

		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addScreenRights(screenFieldRights)).thenReturn(1);
		when(adminAuthDao.addFieldRights(fieldRights)).thenReturn(0);
		int result = adminServiceImpl.addScreenRightsFieldRights(screenFieldRightsList);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_modifyScreenRightsFieldRights_Success() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();
		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.deleteFieldRightsByRoleScreenRightsId(1)).thenReturn(1);
		when(adminAuthDao.deleteScreenRightsByUserRoleId(1)).thenReturn(1);
		when(adminAuthDao.addScreenRights(screenFieldRights)).thenReturn(1);
		when(adminAuthDao.addFieldRights(fieldRights)).thenReturn(1);
		int result = adminServiceImpl.addScreenRightsFieldRights(screenFieldRightsList);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_modifyScreenRightsFieldRights_Error() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();
		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.deleteFieldRightsByRoleScreenRightsId(1)).thenReturn(0);
		int result = adminServiceImpl.modifyScreenRightsFieldRights(screenFieldRightsList, 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_modifyScreenRightsFieldRights_DeleteError() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();
		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.deleteFieldRightsByRoleScreenRightsId(1)).thenReturn(1);
		when(adminAuthDao.deleteScreenRightsByUserRoleId(1)).thenReturn(0);
		int result = adminServiceImpl.modifyScreenRightsFieldRights(screenFieldRightsList, 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_modifyScreenRightsFieldRights_AddScreenError() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();
		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.deleteFieldRightsByRoleScreenRightsId(1)).thenReturn(1);
		when(adminAuthDao.deleteScreenRightsByUserRoleId(1)).thenReturn(1);
		when(adminAuthDao.addScreenRights(screenFieldRights)).thenReturn(0);
		int result = adminServiceImpl.modifyScreenRightsFieldRights(screenFieldRightsList, 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_modifyScreenRightsFieldRights_AddFieldError() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		String signedUserId = party.getRoleBasedId();
		List<ScreenFieldRights> screenFieldRightsList = new ArrayList<ScreenFieldRights>();
		ScreenFieldRights screenFieldRights = new ScreenFieldRights();
		screenFieldRights.setUser_role_id(1);
		screenFieldRights.setAdd_rights(1);
		screenFieldRights.setEdit_rights(1);
		screenFieldRights.setView_rights(1);
		screenFieldRights.setDelete_rights(1);
		screenFieldRights.setCreated_date(timestamp);
		screenFieldRights.setCreated_by(signedUserId);
		screenFieldRights.setUpdated_date(timestamp);
		screenFieldRights.setUpdated_by(signedUserId);

		FieldRights fieldRights = new FieldRights();
		fieldRights.setRole_screen_rights_id(1);
		fieldRights.setField_id(1);
		fieldRights.setAdd_rights(1);
		fieldRights.setEdit_rights(1);
		fieldRights.setView_rights(1);
		fieldRights.setCreated_by(signedUserId);
		fieldRights.setUpdated_by(signedUserId);
		fieldRights.setCreated_date(timestamp);
		fieldRights.setUpdated_date(timestamp);
		List<FieldRights> fieldRightsList = new ArrayList<>();
		fieldRightsList.add(fieldRights);
		screenFieldRights.setFieldRights(fieldRightsList);
		screenFieldRightsList.add(screenFieldRights);

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.deleteFieldRightsByRoleScreenRightsId(1)).thenReturn(1);
		when(adminAuthDao.deleteScreenRightsByUserRoleId(1)).thenReturn(1);
		when(adminAuthDao.addScreenRights(screenFieldRights)).thenReturn(1);
		when(adminAuthDao.addFieldRights(fieldRights)).thenReturn(0);
		int result = adminServiceImpl.modifyScreenRightsFieldRights(screenFieldRightsList, 1);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_addRole_Success() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addRole(roleAuth)).thenReturn(1);
		int result = adminServiceImpl.addRole(roleAuth);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).addRole(roleAuth);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_addRole_Error() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addRole(roleAuth)).thenReturn(0);
		int result = adminServiceImpl.addRole(roleAuth);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).addRole(roleAuth);
		verifyNoMoreInteractions(adminAuthDao);
	}

	// @Test
	// public void test_fetchRoleByRoleId_Success() throws Exception {
	// RoleAuth roleAuth = new RoleAuth();
	// roleAuth.setName("admin");
	// when(adminAuthDao.fetchRoleByRoleId(1)).thenReturn(roleAuth);
	// RoleAuth role = adminAuthDao.fetchRoleByRoleId(1);
	// Assert.assertEquals(1, role.getCreated_by());
	// verify(adminAuthDao, times(1)).fetchRoleByRoleId(1);
	// verifyNoMoreInteractions(adminAuthDao);
	// }

	@Test
	public void test_fetchRoleByRoleId_Error() throws Exception {
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");
		when(adminAuthDao.fetchRoleByRoleId(1)).thenReturn(null);
		RoleAuth role = adminAuthDao.fetchRoleByRoleId(1);
		Assert.assertEquals(null, role);
		verify(adminAuthDao, times(1)).fetchRoleByRoleId(1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_modifyRole_Success() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		String delete_flag = adminTableFields.getDelete_flag();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminAuthDao.fetchRoleByRoleId(1)).thenReturn(roleAuth);
		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.modifyRole(1, roleAuth)).thenReturn(1);
		int result = adminServiceImpl.modifyRole(1, roleAuth);
		Assert.assertEquals(1, result);

		verify(adminAuthDao, times(1)).fetchRoleByRoleId(1);
		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).modifyRole(1, roleAuth);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_modifyRole_Error() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		String delete_flag = adminTableFields.getDelete_flag();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminAuthDao.fetchRoleByRoleId(1)).thenReturn(roleAuth);
		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.modifyRole(1, roleAuth)).thenReturn(0);
		int result = adminServiceImpl.modifyRole(1, roleAuth);
		Assert.assertEquals(0, result);
		verify(adminAuthDao, times(1)).fetchRoleByRoleId(1);
		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).modifyRole(1, roleAuth);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_removeRole_Success() throws Exception {
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");
		when(adminAuthDao.removeRole(1)).thenReturn(1);
		int result = adminServiceImpl.removeRole(1);
		Assert.assertEquals(1, result);
		verify(adminAuthDao, times(1)).removeRole(1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_removeRole_Error() throws Exception {
		RoleAuth roleAuth = new RoleAuth();
		roleAuth.setName("admin");
		when(adminAuthDao.removeRole(1)).thenReturn(0);
		int result = adminServiceImpl.removeRole(1);
		Assert.assertEquals(0, result);
		verify(adminAuthDao, times(1)).removeRole(1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_addUserRole_Success() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addUserRole(userRole)).thenReturn(1);
		int result = adminServiceImpl.addUserRole(userRole);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).addUserRole(userRole);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_addUserRole_Error() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.addUserRole(userRole)).thenReturn(0);
		int result = adminServiceImpl.addUserRole(userRole);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).addUserRole(userRole);
		verifyNoMoreInteractions(adminAuthDao);
	}

	// @Test
	// public void test_fetchUserRoleByUserRoleId_Success() throws Exception {
	// User_role userRole = new User_role();
	// userRole.setUser_id(1);
	// userRole.setRole_id(1);
	// when(adminAuthDao.fetchUserRoleByUserRoleId(1)).thenReturn(userRole);
	// User_role role = adminAuthDao.fetchUserRoleByUserRoleId(1);
	// Assert.assertEquals(1, role.getCreated_by());
	// verify(adminAuthDao, times(1)).fetchUserRoleByUserRoleId(1);
	// verifyNoMoreInteractions(adminAuthDao);
	// }

	@Test
	public void test_fetchUserRoleByUserRoleId_Error() throws Exception {
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		when(adminAuthDao.fetchUserRoleByUserRoleId(1)).thenReturn(null);
		User_role role = adminAuthDao.fetchUserRoleByUserRoleId(1);
		Assert.assertEquals(null, role);
		verify(adminAuthDao, times(1)).fetchUserRoleByUserRoleId(1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_modifyUserRole_Success() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		String delete_flag = adminTableFields.getDelete_flag();

		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminAuthDao.fetchUserRoleByUserRoleId(1)).thenReturn(userRole);
		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.modifyUserRole(1, userRole)).thenReturn(1);
		int result = adminServiceImpl.modifyUserRole(1, userRole);
		Assert.assertEquals(1, result);
		verify(adminAuthDao, times(1)).fetchUserRoleByUserRoleId(1);
		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).modifyUserRole(1, userRole);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_modifyUserRole_Error() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		String delete_flag = adminTableFields.getDelete_flag();

		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminAuthDao.fetchUserRoleByUserRoleId(1)).thenReturn(userRole);
		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminAuthDao.modifyUserRole(1, userRole)).thenReturn(0);
		int result = adminServiceImpl.modifyUserRole(1, userRole);
		Assert.assertEquals(0, result);

		verify(adminAuthDao, times(1)).fetchUserRoleByUserRoleId(1);
		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminAuthDao, times(1)).modifyUserRole(1, userRole);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_removeUserRole_Success() throws Exception {
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		when(adminAuthDao.removeUserRole(1)).thenReturn(1);
		int result = adminServiceImpl.removeUserRole(1);
		Assert.assertEquals(1, result);
		verify(adminAuthDao, times(1)).removeUserRole(1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_removeUserRole_Error() throws Exception {
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		when(adminAuthDao.removeUserRole(1)).thenReturn(0);
		int result = adminServiceImpl.removeUserRole(1);
		Assert.assertEquals(0, result);
		verify(adminAuthDao, times(1)).removeUserRole(1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_fetchUserRoleByUserIdAndRoleId_Success() throws Exception {
		User_role user_role = new User_role();
		user_role.setUser_role_id(1);
		user_role.setUser_id(1);
		user_role.setRole_id(1);
		when(adminAuthDao.fetchUserRoleByUserIdAndRoleId(1, 1)).thenReturn(user_role);
		User_role result = adminServiceImpl.fetchUserRoleByUserIdAndRoleId(1, 1);
		Assert.assertEquals(1, result.getRole_id());
		verify(adminAuthDao, times(1)).fetchUserRoleByUserIdAndRoleId(1, 1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_fetchUserRoleByUserIdAndRoleId_Error() throws Exception {
		User_role user_role = new User_role();
		when(adminAuthDao.fetchUserRoleByUserIdAndRoleId(1, 1)).thenReturn(user_role);
		User_role result = adminServiceImpl.fetchUserRoleByUserIdAndRoleId(1, 1);
		Assert.assertEquals(0, result.getRole_id());
		verify(adminAuthDao, times(1)).fetchUserRoleByUserIdAndRoleId(1, 1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_fetchAdminList_Success() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		List<Admin> adm = new ArrayList<Admin>();
		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		admin.setEmailId("admin@gmail.com");
		adm.add(admin);
		when(adminDao.fetchAdminList(delete_flag, encryptPass)).thenReturn(adm);
		List<Admin> result = adminServiceImpl.fetchAdminList();
		Assert.assertEquals(1, result.size());
		verify(adminDao, times(1)).fetchAdminList(delete_flag, encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchAdminList_Error() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		List<Admin> adm = new ArrayList<Admin>();

		when(adminDao.fetchAdminList(delete_flag, encryptPass)).thenReturn(adm);
		List<Admin> result = adminServiceImpl.fetchAdminList();
		Assert.assertEquals(0, result.size());
		verify(adminDao, times(1)).fetchAdminList(delete_flag, encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchAdminByEmailId_Success() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		admin.setEmailId("admin@gmail.com");
		when(adminDao.fetchAdminByEmailId("admin@gmail.com", encryptPass)).thenReturn(admin);
		Admin result = adminServiceImpl.fetchAdminByEmailId("admin@gmail.com");
		Assert.assertEquals("ADM000000000A", result.getAdminId());
		verify(adminDao, times(1)).fetchAdminByEmailId("admin@gmail.com", encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchAdminByEmailId_Error() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		when(adminDao.fetchAdminByEmailId("admin@gmail.com", encryptPass)).thenReturn(null);
		Admin result = adminServiceImpl.fetchAdminByEmailId("admin@gmail.com");
		Assert.assertEquals(null, result);
		verify(adminDao, times(1)).fetchAdminByEmailId("admin@gmail.com", encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchByAdminId_Success() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		admin.setName("admin");
		when(adminDao.fetchByAdminId("ADM000000000A", delete_flag, encryptPass)).thenReturn(admin);
		Admin result = adminServiceImpl.fetchByAdminId("ADM000000000A");
		Assert.assertEquals("admin", result.getName());
		verify(adminDao, times(1)).fetchByAdminId("ADM000000000A", delete_flag, encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchByAdminId_Error() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		admin.setName("admin");
		when(adminDao.fetchByAdminId("ADM000000000B", delete_flag, encryptPass)).thenReturn(null);
		Admin result = adminServiceImpl.fetchByAdminId("ADM000000000B");
		Assert.assertEquals(null, result);
		verify(adminDao, times(1)).fetchByAdminId("ADM000000000B", delete_flag, encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addAdmin_Success() throws Exception {

		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		String role = adminTableFields.getRole_name();
		String partyStatus = advTableFields.getPartystatus_desc();
		int isPrimaryRole = advTableFields.getIs_primary_role_true();

		long partyId = 1;
		long roleId = 3;
		long partyStatusId = 1;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String signedUserId = "ADM000000000A";

		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		admin.setEmailId("admin@gmail.com");
		admin.setPartyStatusId(partyStatusId);
		admin.setCreated(timestamp);
		admin.setUpdated(timestamp);
		admin.setCreated_by(signedUserId);
		admin.setUpdated_by(signedUserId);
		//
		// Authentication authentication = Mockito.mock(Authentication.class);
		// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		// SecurityContextHolder.setContext(securityContext);
		// when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com",
		// "pass", new ArrayList<>()));

		when(adminAuthDao.fetchRoleIdByName(role)).thenReturn(roleId);
		when(adminDao.fetchPartyStatusIdByDesc(partyStatus)).thenReturn(partyStatusId);
		when(adminDao.fetchEncryptionSecretKey()).thenReturn("key");
		// when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag,
		// encryptPass)).thenReturn(party);
		when(adminDao.addAdmin(admin, encryptPass)).thenReturn(1);
		when(adminDao.addPartyForAdmin(admin, encryptPass)).thenReturn(1);
		when(adminDao.fetchPartyIdByRoleBasedId("ADM000000000A", delete_flag)).thenReturn(partyId);
		when(adminAuthDao.addUser_role(partyId, roleId, "ADM000000000A", isPrimaryRole)).thenReturn(1);

		int result1 = adminServiceImpl.addAdmin(admin);
		Assert.assertEquals(1, result1);

		verify(adminAuthDao, times(1)).fetchRoleIdByName(role);
		verify(adminDao, times(1)).fetchPartyStatusIdByDesc(partyStatus);
		verify(adminDao, times(1)).fetchEncryptionSecretKey();
		// verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com",
		// delete_flag, encryptPass);
		verify(adminDao, times(1)).addAdmin(admin, encryptPass);
		verify(adminDao, times(1)).addPartyForAdmin(admin, encryptPass);
		verify(adminDao, times(1)).fetchPartyIdByRoleBasedId("ADM000000000A", delete_flag);
		verify(adminAuthDao, times(1)).addUser_role(partyId, roleId, "ADM000000000A", isPrimaryRole);
		verifyNoMoreInteractions(adminDao);

	}

	@Test
	public void test_addAdmin_Error() throws Exception {
		String encryptPass = adminTableFields.getEncryption_password();
		String role = adminTableFields.getRole_name();
		String partyStatus = advTableFields.getPartystatus_desc();
		String delete_flag = adminTableFields.getDelete_flag();

		long roleId = 3;
		long partyStatusId = 1;

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		admin.setEmailId("admin@gmail.com");
		admin.setPartyStatusId(partyStatusId);

		// Authentication authentication = Mockito.mock(Authentication.class);
		// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		// SecurityContextHolder.setContext(securityContext);
		// when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com",
		// "pass", new ArrayList<>()));

		when(adminAuthDao.fetchRoleIdByName(role)).thenReturn(roleId);
		when(adminDao.fetchPartyStatusIdByDesc(partyStatus)).thenReturn(partyStatusId);
		when(adminDao.fetchEncryptionSecretKey()).thenReturn("key");
		// when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag,
		// encryptPass)).thenReturn(party);
		when(adminDao.addAdmin(admin, encryptPass)).thenReturn(0);

		int result1 = adminServiceImpl.addAdmin(admin);
		Assert.assertEquals(0, result1);

		verify(adminAuthDao, times(1)).fetchRoleIdByName(role);
		verify(adminDao, times(1)).fetchPartyStatusIdByDesc(partyStatus);
		verify(adminDao, times(1)).fetchEncryptionSecretKey();
		// verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com",
		// delete_flag, encryptPass);
		verify(adminDao, times(1)).addAdmin(admin, encryptPass);
	}

	@Test
	public void test_modifyAdmin_Success() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Admin admin = new Admin();
		admin.setAdminId("ADM000000000A");
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchByAdminId("ADM000000000A", delete_flag, encryptPass)).thenReturn(admin);
		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminDao.modifyAdmin("ADM000000000A", admin, encryptPass)).thenReturn(1);

		int result = adminServiceImpl.modifyAdmin("ADM000000000A", admin);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).fetchByAdminId("ADM000000000A", delete_flag, encryptPass);
		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminDao, times(1)).modifyAdmin("ADM000000000A", admin, encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyAdmin_Error() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Admin admin = new Admin();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchByAdminId("ADM000000000A", delete_flag, encryptPass)).thenReturn(admin);
		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);
		when(adminDao.modifyAdmin("ADM000000000A", admin, encryptPass)).thenReturn(0);

		int result = adminServiceImpl.modifyAdmin("ADM000000000A", admin);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).fetchByAdminId("ADM000000000A", delete_flag, encryptPass);
		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminDao, times(1)).modifyAdmin("ADM000000000A", admin, encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeAdmin_Success() throws Exception {
		String delete_flag_y = adminTableFields.getDelete_flag_y();
		String encryptPass = adminTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag_y, encryptPass)).thenReturn(party);
		when(adminDao.removeAdmin("ADM000000000A", delete_flag_y, "ADM000000000A")).thenReturn(1);
		when(adminDao.removeParty("ADM000000000A", delete_flag_y, "ADM000000000A")).thenReturn(1);

		int result = adminServiceImpl.removeAdmin("ADM000000000A");
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag_y, encryptPass);
		verify(adminDao, times(1)).removeAdmin("ADM000000000A", delete_flag_y, "ADM000000000A");
		verify(adminDao, times(1)).removeParty("ADM000000000A", delete_flag_y, "ADM000000000A");
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeAdmin_Error() throws Exception {
		String delete_flag = adminTableFields.getDelete_flag();
		String encryptPass = adminTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(adminDao.fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass)).thenReturn(party);

		when(adminDao.removeAdmin("ADM000000000A", delete_flag, "ADM000000000A")).thenReturn(0);

		int result = adminServiceImpl.removeAdmin("ADM000000000A");
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).fetchPartyForSignIn("admin@gmail.com", delete_flag, encryptPass);
		verify(adminDao, times(1)).removeAdmin("ADM000000000A", delete_flag, "ADM000000000A");
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchPartyByEmailId_Success() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM000000000A");
		party.setEmailId("admin@gmail.com");
		when(adminDao.fetchPartyByEmailId("admin@gmail.com", encryptPass)).thenReturn(party);
		Party result = adminServiceImpl.fetchPartyByEmailId("admin@gmail.com");
		Assert.assertEquals(1, result.getPartyId());
		verify(adminDao, times(1)).fetchPartyByEmailId("admin@gmail.com", encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_fetchPartyByEmailId_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(adminDao.fetchPartyByEmailId("admin@gmail.com", encryptPass)).thenReturn(null);
		Party result = adminServiceImpl.fetchPartyByEmailId("admin@gmail.com");
		Assert.assertEquals(null, result);
		verify(adminDao, times(1)).fetchPartyByEmailId("admin@gmail.com", encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addProducts_Success() throws Exception {

		Product product = new Product();
		product.setProdId(1);
		product.setProduct("product");

		when(adminDao.addProducts(product)).thenReturn(1);
		int result = adminServiceImpl.addProducts(product);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addProducts(product);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addProducts_Error() throws Exception {

		Product product = new Product();
		product.setProdId(1);
		product.setProduct("product");

		when(adminDao.addProducts(product)).thenReturn(0);
		int result = adminServiceImpl.addProducts(product);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addProducts(product);
	}

	@Test
	public void test_addRemuneration_Success() throws Exception {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemuneration("Remuneration");
		when(adminDao.addRemuneration(remuneration)).thenReturn(1);
		int result = adminServiceImpl.addRemuneration(remuneration);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).addRemuneration(remuneration);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addRemuneration_Error() throws Exception {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemuneration("Remuneration");
		when(adminDao.addRemuneration(remuneration)).thenReturn(0);
		int result = adminServiceImpl.addRemuneration(remuneration);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).addRemuneration(remuneration);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeProducts_Success() throws Exception {

		Product product = new Product();
		product.setProdId(1);
		// product.setProduct("product");

		when(adminDao.removeProducts(1)).thenReturn(1);
		int result = adminServiceImpl.removeProducts(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeProducts(1);
	}

	@Test
	public void test_modifyRemuneration_Success() throws Exception {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemuneration("Remuneration");
		remuneration.setRemId(1);
		when(adminDao.modifyRemuneration(remuneration)).thenReturn(1);
		int result = adminServiceImpl.modifyRemuneration(remuneration);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyRemuneration(remuneration);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyRemuneration_Error() throws Exception {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemuneration("Remuneration");
		remuneration.setRemId(1);
		when(adminDao.modifyRemuneration(remuneration)).thenReturn(0);
		int result = adminServiceImpl.modifyRemuneration(remuneration);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyRemuneration(remuneration);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeRemuneration_Success() throws Exception {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemId(1);
		when(adminDao.removeRemuneration(1)).thenReturn(1);
		int result = adminServiceImpl.removeRemuneration(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).removeRemuneration(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeProducts_Error() throws Exception {

		Product product = new Product();
		product.setProdId(1);

		when(adminDao.removeProducts(1)).thenReturn(0);
		int result = adminServiceImpl.removeProducts(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeProducts(1);
	}

	@Test
	public void test_removeRemuneration_Error() throws Exception {
		Remuneration remuneration = new Remuneration();
		remuneration.setRemId(1);
		when(adminDao.removeRemuneration(1)).thenReturn(0);
		int result = adminServiceImpl.removeRemuneration(1);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).removeRemuneration(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addState_Success() throws Exception {
		State state = new State();
		state.setState("State");
		when(adminDao.addState(state)).thenReturn(1);
		int result = adminServiceImpl.addState(state);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).addState(state);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyProducts_Success() throws Exception {

		Product product = new Product();
		product.setProdId(1);
		product.setProduct("product");

		when(adminDao.modifyProduct(product)).thenReturn(1);
		int result = adminServiceImpl.modifyProduct(product);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyProduct(product);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyProducts_Error() throws Exception {

		Product product = new Product();
		product.setProdId(1);
		product.setProduct("product");

		when(adminDao.modifyProduct(product)).thenReturn(0);
		int result = adminServiceImpl.modifyProduct(product);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyProduct(product);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addArticleStatus_Success() throws Exception {

		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(1);
		articleStatus.setDesc("articleStatus");

		when(adminDao.addArticleStatus(articleStatus)).thenReturn(1);
		int result = adminServiceImpl.addArticleStatus(articleStatus);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addArticleStatus(articleStatus);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addArticleStatus_Error() throws Exception {

		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(1);
		articleStatus.setDesc("articleStatus");

		when(adminDao.addArticleStatus(articleStatus)).thenReturn(0);
		int result = adminServiceImpl.addArticleStatus(articleStatus);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addArticleStatus(articleStatus);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyArticleStatus_Success() throws Exception {

		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(1);
		articleStatus.setDesc("articleStatus");

		when(adminDao.modifyArticleStatus(articleStatus)).thenReturn(1);
		int result = adminServiceImpl.modifyArticleStatus(articleStatus);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyArticleStatus(articleStatus);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyArticleStatus_Error() throws Exception {

		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(1);
		articleStatus.setDesc("articleStatus");

		when(adminDao.modifyArticleStatus(articleStatus)).thenReturn(0);
		int result = adminServiceImpl.modifyArticleStatus(articleStatus);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyArticleStatus(articleStatus);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeArticleStatus_Success() throws Exception {

		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(1);

		when(adminDao.removeArticleStatus(1)).thenReturn(1);
		int result = adminServiceImpl.removeArticleStatus(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeArticleStatus(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeArticleStatus_Error() throws Exception {

		ArticleStatus articleStatus = new ArticleStatus();
		articleStatus.setId(1);

		when(adminDao.removeArticleStatus(1)).thenReturn(0);
		int result = adminServiceImpl.removeArticleStatus(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeArticleStatus(1);
	}

	@Test
	public void test_addState_Error() throws Exception {
		State state = new State();
		state.setState("State");
		when(adminDao.addState(state)).thenReturn(0);
		int result = adminServiceImpl.addState(state);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).addState(state);
	}

	@Test
	public void test_checkPartyIsPresent_Success() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(adminDao.checkPartyIsPresent("admin@gmail.com", encryptPass)).thenReturn(1);
		int result = adminServiceImpl.checkPartyIsPresent("admin@gmail.com");
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).checkPartyIsPresent("admin@gmail.com", encryptPass);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_checkPartyIsPresent_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(adminDao.checkPartyIsPresent("admin@gmail.com", encryptPass)).thenReturn(0);
		int result = adminServiceImpl.checkPartyIsPresent("admin@gmail.com");
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).checkPartyIsPresent("admin@gmail.com", encryptPass);
		verifyNoMoreInteractions(adminDao);

	}

	@Test
	public void test_checkAdminIsPresent_Success() throws Exception {
		when(adminDao.checkAdminIsPresent("ADM0000000000")).thenReturn(1);
		int result = adminServiceImpl.checkAdminIsPresent("ADM0000000000");
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).checkAdminIsPresent("ADM0000000000");
		verifyNoMoreInteractions(adminDao);

	}

	@Test
	public void test_checkAdminIsPresent_Error() throws Exception {
		when(adminDao.checkAdminIsPresent("ADM000000000")).thenReturn(0);
		int result = adminServiceImpl.checkAdminIsPresent("ADM000000000");
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).checkAdminIsPresent("ADM000000000");
		verifyNoMoreInteractions(adminDao);

	}

	@Test
	public void test_checkUserRoleIsPresent_Success() throws Exception {
		when(adminAuthDao.checkUserRoleIsPresent(1)).thenReturn(1);
		int result = adminServiceImpl.checkUserRoleIsPresent(1);
		Assert.assertEquals(1, result);
		verify(adminAuthDao, times(1)).checkUserRoleIsPresent(1);
		verifyNoMoreInteractions(adminAuthDao);

	}

	@Test
	public void test_checkUserRoleIsPresent_Error() throws Exception {
		when(adminAuthDao.checkUserRoleIsPresent(10000)).thenReturn(0);
		int result = adminServiceImpl.checkUserRoleIsPresent(10000);
		Assert.assertEquals(0, result);
		verify(adminAuthDao, times(1)).checkUserRoleIsPresent(10000);
		verifyNoMoreInteractions(adminAuthDao);

	}

	@Test
	public void test_checkRoleIsPresent_Success() throws Exception {
		when(adminAuthDao.checkRoleIsPresent(1)).thenReturn(1);
		int result = adminServiceImpl.checkRoleIsPresent(1);
		Assert.assertEquals(1, result);
		verify(adminAuthDao, times(1)).checkRoleIsPresent(1);
		verifyNoMoreInteractions(adminAuthDao);

	}

	@Test
	public void test_checkRoleIsPresent_Error() throws Exception {
		when(adminAuthDao.checkRoleIsPresent(10000)).thenReturn(0);
		int result = adminServiceImpl.checkRoleIsPresent(10000);
		Assert.assertEquals(0, result);
		verify(adminAuthDao, times(1)).checkRoleIsPresent(10000);
		verifyNoMoreInteractions(adminAuthDao);

	}

	@Test
	public void test_checkUserRoleByUserIdAndRoleId_Success() throws Exception {
		when(adminAuthDao.checkUserRoleByUserIdAndRoleId(1L, 1)).thenReturn(1);
		int result = adminServiceImpl.checkUserRoleByUserIdAndRoleId(1L, 1);
		Assert.assertEquals(1, result);
		verify(adminAuthDao, times(1)).checkUserRoleByUserIdAndRoleId(1L, 1);
		verifyNoMoreInteractions(adminAuthDao);

	}

	@Test
	public void test_checkUserRoleByUserIdAndRoleId_Error() throws Exception {
		when(adminAuthDao.checkUserRoleByUserIdAndRoleId(1000L, 1)).thenReturn(0);
		int result = adminServiceImpl.checkUserRoleByUserIdAndRoleId(1000L, 1);
		Assert.assertEquals(0, result);
		verify(adminAuthDao, times(1)).checkUserRoleByUserIdAndRoleId(1000L, 1);
		verifyNoMoreInteractions(adminAuthDao);
	}

	@Test
	public void test_addAdvisorTypes_Success() throws Exception {
		Advtypes advtypes = new Advtypes();
		advtypes.setAdvtype("Advtypes");

		when(adminDao.advisorTypes(advtypes)).thenReturn(1);
		int result = adminServiceImpl.advisorTypes(advtypes);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).advisorTypes(advtypes);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addAdvisorTypes_Error() throws Exception {

		Advtypes advtypes = new Advtypes();
		advtypes.setAdvtype("Advtypes");

		when(adminDao.advisorTypes(advtypes)).thenReturn(0);
		int result = adminServiceImpl.advisorTypes(advtypes);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).advisorTypes(advtypes);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyAdvisorTypes_Success() throws Exception {

		Advtypes advtypes = new Advtypes();
		advtypes.setId(1);
		advtypes.setAdvtype("Advtypes");

		when(adminDao.modifyAdvisorTypes(advtypes)).thenReturn(1);
		int result = adminServiceImpl.modifyAdvisorTypes(advtypes);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyAdvisorTypes(advtypes);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyAdvisorTypes_Error() throws Exception {

		Advtypes advtypes = new Advtypes();
		advtypes.setId(1);
		advtypes.setAdvtype("Advtypes");

		when(adminDao.modifyAdvisorTypes(advtypes)).thenReturn(0);
		int result = adminServiceImpl.modifyAdvisorTypes(advtypes);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyAdvisorTypes(advtypes);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeAdvisorTypes_Success() throws Exception {

		Advtypes advtypes = new Advtypes();
		advtypes.setId(1);

		when(adminDao.removeAdvisorTypes(1)).thenReturn(1);
		int result = adminServiceImpl.removeAdvisorTypes(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeAdvisorTypes(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeAdvisorTypes_Error() throws Exception {

		Advtypes advtypes = new Advtypes();
		advtypes.setId(1);

		when(adminDao.removeAdvisorTypes(1)).thenReturn(0);
		int result = adminServiceImpl.removeAdvisorTypes(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeAdvisorTypes(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addCashFlowItemType_Success() throws Exception {

		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		cashFlowItemType.setCashFlowItemType("articleStatus");

		when(adminDao.addCashFlowItemType(cashFlowItemType)).thenReturn(1);
		int result = adminServiceImpl.addCashFlowItemType(cashFlowItemType);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addCashFlowItemType(cashFlowItemType);

	}

	@Test
	public void test_modifyState_Success() throws Exception {
		State state = new State();
		state.setState("State");
		state.setStateId(1);
		when(adminDao.modifyState(state)).thenReturn(1);
		int result = adminServiceImpl.modifyState(state);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyState(state);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addCashFlowItemType_Error() throws Exception {

		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		cashFlowItemType.setCashFlowItemType("articleStatus");

		when(adminDao.addCashFlowItemType(cashFlowItemType)).thenReturn(0);
		int result = adminServiceImpl.addCashFlowItemType(cashFlowItemType);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addCashFlowItemType(cashFlowItemType);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyCashFlowItemType_Success() throws Exception {

		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		cashFlowItemType.setCashFlowItemType("articleStatus");

		when(adminDao.modifyCashFlowItemType(cashFlowItemType)).thenReturn(1);
		int result = adminServiceImpl.modifyCashFlowItemType(cashFlowItemType);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyCashFlowItemType(cashFlowItemType);
	}

	@Test
	public void test_modifyState_Error() throws Exception {
		State state = new State();
		state.setState("State");
		state.setStateId(1);
		when(adminDao.modifyState(state)).thenReturn(0);
		int result = adminServiceImpl.modifyState(state);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyState(state);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeState_Success() throws Exception {
		State state = new State();
		state.setStateId(1);
		when(adminDao.removeState(1)).thenReturn(1);
		int result = adminServiceImpl.removeState(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).removeState(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyCashFlowItemType_Error() throws Exception {

		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);
		cashFlowItemType.setCashFlowItemType("articleStatus");

		when(adminDao.modifyCashFlowItemType(cashFlowItemType)).thenReturn(0);
		int result = adminServiceImpl.modifyCashFlowItemType(cashFlowItemType);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyCashFlowItemType(cashFlowItemType);
	}

	@Test
	public void test_removeState_Error() throws Exception {
		State state = new State();
		state.setStateId(1);
		when(adminDao.removeState(1)).thenReturn(0);
		int result = adminServiceImpl.removeState(1);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).removeState(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeCashFlowItemType_Success() throws Exception {

		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		when(adminDao.removeCashFlowItemType(1)).thenReturn(1);
		int result = adminServiceImpl.removeCashFlowItemType(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeCashFlowItemType(1);
	}

	@Test
	public void test_saveAddAcctype_Success() throws Exception {
		Acctype acctype = new Acctype();
		acctype.setAccType("Acctype");
		when(adminDao.saveAddAcctype(acctype)).thenReturn(1);
		int result = adminServiceImpl.saveAddAcctype(acctype);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).saveAddAcctype(acctype);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeCashFlowItemType_Error() throws Exception {

		CashFlowItemType cashFlowItemType = new CashFlowItemType();
		cashFlowItemType.setCashFlowItemTypeId(1);

		when(adminDao.removeCashFlowItemType(1)).thenReturn(0);
		int result = adminServiceImpl.removeCashFlowItemType(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeCashFlowItemType(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addCashFlowItem_Success() throws Exception {

		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItem("articleStatus");
		cashFlowItem.setCashFlowItemTypeId(3);

		when(adminDao.addCashFlowItem(cashFlowItem)).thenReturn(1);
		int result = adminServiceImpl.addCashFlowItem(cashFlowItem);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addCashFlowItem(cashFlowItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addCashFlowItem_Error() throws Exception {

		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItem("articleStatus");
		cashFlowItem.setCashFlowItemTypeId(3);

		when(adminDao.addCashFlowItem(cashFlowItem)).thenReturn(0);
		int result = adminServiceImpl.addCashFlowItem(cashFlowItem);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addCashFlowItem(cashFlowItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyCashFlowItem_Success() throws Exception {

		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItem("articleStatus");
		cashFlowItem.setCashFlowItemTypeId(3);

		when(adminDao.modifyCashFlowItem(cashFlowItem)).thenReturn(1);
		int result = adminServiceImpl.modifyCashFlowItem(cashFlowItem);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyCashFlowItem(cashFlowItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyCashFlowItem_Error() throws Exception {

		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);
		cashFlowItem.setCashFlowItem("articleStatus");
		cashFlowItem.setCashFlowItemTypeId(3);

		when(adminDao.modifyCashFlowItem(cashFlowItem)).thenReturn(0);
		int result = adminServiceImpl.modifyCashFlowItem(cashFlowItem);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyCashFlowItem(cashFlowItem);
	}

	@Test
	public void test_saveAddAcctype_Error() throws Exception {
		Acctype acctype = new Acctype();
		acctype.setAccType("Acctype");
		when(adminDao.saveAddAcctype(acctype)).thenReturn(0);
		int result = adminServiceImpl.saveAddAcctype(acctype);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).saveAddAcctype(acctype);
	}

	@Test
	public void test_addFollowerStatus_Success() throws Exception {

		AdmFollower admFollower = new AdmFollower();
		admFollower.setStatus("AdmFollower");

		when(adminDao.followerStatus(admFollower)).thenReturn(1);
		int result = adminServiceImpl.followerStatus(admFollower);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).followerStatus(admFollower);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addFollowerStatus_Error() throws Exception {

		AdmFollower admFollower = new AdmFollower();
		admFollower.setStatus("AdmFollower");

		when(adminDao.followerStatus(admFollower)).thenReturn(0);
		int result = adminServiceImpl.followerStatus(admFollower);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).followerStatus(admFollower);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyFollowerStatus_Success() throws Exception {

		AdmFollower admFollower = new AdmFollower();
		admFollower.setFollowerStatusId(1);
		admFollower.setStatus("AdmFollower");

		when(adminDao.modifyFollowerStatus(admFollower)).thenReturn(1);
		int result = adminServiceImpl.modifyFollowerStatus(admFollower);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyFollowerStatus(admFollower);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyFollowerStatus_Error() throws Exception {

		AdmFollower admFollower = new AdmFollower();
		admFollower.setFollowerStatusId(1);
		admFollower.setStatus("AdmFollower");

		when(adminDao.modifyFollowerStatus(admFollower)).thenReturn(0);
		int result = adminServiceImpl.modifyFollowerStatus(admFollower);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyFollowerStatus(admFollower);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeFollowerStatus_Success() throws Exception {

		AdmFollower admFollower = new AdmFollower();
		admFollower.setFollowerStatusId(1);

		when(adminDao.removeFollowerStatus(1)).thenReturn(1);
		int result = adminServiceImpl.removeFollowerStatus(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeFollowerStatus(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeFollowerStatus_Error() throws Exception {

		AdmFollower admFollower = new AdmFollower();
		admFollower.setFollowerStatusId(1);

		when(adminDao.removeFollowerStatus(1)).thenReturn(0);
		int result = adminServiceImpl.removeFollowerStatus(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeFollowerStatus(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeCashFlowItem_Success() throws Exception {

		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);

		when(adminDao.removeCashFlowItem(1)).thenReturn(1);
		int result = adminServiceImpl.removeCashFlowItem(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeCashFlowItem(1);
	}

	@Test
	public void test_modifyAcctype_Success() throws Exception {
		Acctype acctype = new Acctype();
		acctype.setAccType("Acctype");
		acctype.setAccTypeId(1);
		when(adminDao.modifyAcctype(acctype)).thenReturn(1);
		int result = adminServiceImpl.modifyAcctype(acctype);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyAcctype(acctype);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeCashFlowItem_Error() throws Exception {

		CashFlowItem cashFlowItem = new CashFlowItem();
		cashFlowItem.setCashFlowItemId(1);

		when(adminDao.removeCashFlowItem(1)).thenReturn(0);
		int result = adminServiceImpl.removeCashFlowItem(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeCashFlowItem(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addCity_Success() throws Exception {

		City city = new City();
		city.setStateId(4);
		city.setCity("madurai");
		city.setPincode("685254");
		city.setDistrict("madurai");

		when(adminDao.addCity(city)).thenReturn(1);
		int result = adminServiceImpl.addCity(city);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addCity(city);
	}

	public void test_modifyAcctype_Error() throws Exception {
		Acctype acctype = new Acctype();
		acctype.setAccType("Acctype");
		acctype.setAccTypeId(1);
		when(adminDao.modifyAcctype(acctype)).thenReturn(0);
		int result = adminServiceImpl.modifyAcctype(acctype);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyAcctype(acctype);
	}

	@Test
	public void test_addPriorityItem_Success() throws Exception {

		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItem("AdmPriority");

		when(adminDao.priorityItem(admPriority)).thenReturn(1);
		int result = adminServiceImpl.priorityItem(admPriority);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).priorityItem(admPriority);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addPriorityItem_Error() throws Exception {

		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItem("AdmPriority");

		when(adminDao.priorityItem(admPriority)).thenReturn(0);
		int result = adminServiceImpl.priorityItem(admPriority);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).priorityItem(admPriority);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addCity_Error() throws Exception {

		City city = new City();
		city.setStateId(4);
		city.setCity("madurai");
		city.setPincode("685254");
		city.setDistrict("madurai");

		when(adminDao.addCity(city)).thenReturn(0);
		int result = adminServiceImpl.addCity(city);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addCity(city);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyCity_Success() throws Exception {

		City city = new City();
		city.setCityId(1);
		city.setStateId(4);
		city.setCity("madurai");
		city.setPincode("685254");
		city.setDistrict("madurai");

		when(adminDao.modifyCity(city)).thenReturn(1);
		int result = adminServiceImpl.modifyCity(city);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyCity(city);
	}

	@Test
	public void test_RemoveAcctype_Success() throws Exception {
		Acctype acctype = new Acctype();
		acctype.setAccTypeId(1);
		when(adminDao.RemoveAcctype(1)).thenReturn(1);
		int result = adminServiceImpl.RemoveAcctype(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).RemoveAcctype(1);
	}

	@Test
	public void test_modifyPriorityItem_Success() throws Exception {

		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItemId(1);
		admPriority.setPriorityItem("AdmPriority");

		when(adminDao.modifyPriorityItem(admPriority)).thenReturn(1);
		int result = adminServiceImpl.modifyPriorityItem(admPriority);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyPriorityItem(admPriority);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyPriorityItem_Error() throws Exception {

		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItemId(1);
		admPriority.setPriorityItem("AdmPriority");

		when(adminDao.modifyPriorityItem(admPriority)).thenReturn(0);
		int result = adminServiceImpl.modifyPriorityItem(admPriority);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyPriorityItem(admPriority);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyCity_Error() throws Exception {

		City city = new City();
		city.setCityId(1);
		city.setStateId(4);
		city.setCity("madurai");
		city.setPincode("685254");
		city.setDistrict("madurai");

		when(adminDao.modifyCity(city)).thenReturn(0);
		int result = adminServiceImpl.modifyCity(city);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyCity(city);
	}

	@Test
	public void test_RemoveAcctype_Error() throws Exception {
		Acctype acctype = new Acctype();
		acctype.setAccTypeId(1);
		when(adminDao.RemoveAcctype(1)).thenReturn(0);
		int result = adminServiceImpl.RemoveAcctype(1);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).RemoveAcctype(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeCity_Success() throws Exception {

		City city = new City();
		city.setCityId(1);

		when(adminDao.removeCity(1)).thenReturn(1);
		int result = adminServiceImpl.removeCity(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeCity(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeCity_Error() throws Exception {

		City city = new City();
		city.setCityId(1);

		when(adminDao.removeCity(1)).thenReturn(0);
		int result = adminServiceImpl.removeCity(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeCity(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removePriorityItem_Success() throws Exception {

		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItemId(1);

		when(adminDao.removePriorityItem(1)).thenReturn(1);
		int result = adminServiceImpl.removePriorityItem(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removePriorityItem(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removePriorityItem_Error() throws Exception {

		AdmPriority admPriority = new AdmPriority();
		admPriority.setPriorityItemId(1);

		when(adminDao.removePriorityItem(1)).thenReturn(0);
		int result = adminServiceImpl.removePriorityItem(1);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removePriorityItem(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addWorkflowstatus_Success() throws Exception {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setStatus("Workflowstatus");
		when(adminDao.addWorkflowstatus(workflowstatus)).thenReturn(1);
		int result = adminServiceImpl.addWorkflowstatus(workflowstatus);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).addWorkflowstatus(workflowstatus);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addWorkflowstatus_Error() throws Exception {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setStatus("Workflowstatus");
		when(adminDao.addWorkflowstatus(workflowstatus)).thenReturn(0);
		int result = adminServiceImpl.addWorkflowstatus(workflowstatus);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).addWorkflowstatus(workflowstatus);
	}

	@Test
	public void test_modifyWorkflowstatus_Success() throws Exception {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setStatus("Workflowstatus");
		workflowstatus.setWorkflowId(1);
		when(adminDao.modifyWorkflowstatus(workflowstatus)).thenReturn(1);
		int result = adminServiceImpl.modifyWorkflowstatus(workflowstatus);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyWorkflowstatus(workflowstatus);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyWorkflowstatus_Error() throws Exception {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setStatus("Workflowstatus");
		workflowstatus.setWorkflowId(1);
		when(adminDao.modifyWorkflowstatus(workflowstatus)).thenReturn(0);
		int result = adminServiceImpl.modifyWorkflowstatus(workflowstatus);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyWorkflowstatus(workflowstatus);
	}

	@Test
	public void test_removeWorkFlowStatus_Success() throws Exception {
		Workflowstatus workflowstatus = new Workflowstatus();
		workflowstatus.setWorkflowId(1);
		when(adminDao.removeWorkFlowStatus(1)).thenReturn(1);
		int result = adminServiceImpl.removeWorkFlowStatus(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).removeWorkFlowStatus(1);
	}

	@Test
	public void test_addService_Success() throws Exception {
		Service service = new Service();
		service.setService("Service");
		when(adminDao.addService(service)).thenReturn(1);
		int result = adminServiceImpl.addService(service);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).addService(service);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addService_Error() throws Exception {
		Service service = new Service();
		service.setService("Service");
		when(adminDao.addService(service)).thenReturn(0);
		int result = adminServiceImpl.addService(service);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).addService(service);
	}

	@Test
	public void test_modifyService_Success() throws Exception {
		Service service = new Service();
		service.setService("Service");
		service.setServiceId(1);
		when(adminDao.modifyService(service)).thenReturn(1);
		int result = adminServiceImpl.modifyService(service);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyService(service);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addRiskPortfolio_Success() throws Exception {

		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setPoints("points");
		admRiskPortfolio.setBehaviour("behaviour");
		admRiskPortfolio.setEquity(10);
		admRiskPortfolio.setDebt(12);
		admRiskPortfolio.setCash(13);

		when(adminDao.riskPortfolio(admRiskPortfolio)).thenReturn(1);
		int result = adminServiceImpl.riskPortfolio(admRiskPortfolio);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).riskPortfolio(admRiskPortfolio);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addRiskPortfolio_Error() throws Exception {

		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setPoints("points");
		admRiskPortfolio.setBehaviour("behaviour");
		admRiskPortfolio.setEquity(10);
		admRiskPortfolio.setDebt(12);
		admRiskPortfolio.setCash(13);

		when(adminDao.riskPortfolio(admRiskPortfolio)).thenReturn(0);
		int result = adminServiceImpl.riskPortfolio(admRiskPortfolio);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).riskPortfolio(admRiskPortfolio);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyRiskPortfolio_Success() throws Exception {

		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setRiskPortfolioId(1);
		admRiskPortfolio.setPoints("points");
		admRiskPortfolio.setBehaviour("behaviour");
		admRiskPortfolio.setEquity(10);
		admRiskPortfolio.setDebt(12);
		admRiskPortfolio.setCash(13);

		when(adminDao.modifyRiskPortfolio(admRiskPortfolio)).thenReturn(1);
		int result = adminServiceImpl.modifyRiskPortfolio(admRiskPortfolio);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyRiskPortfolio(admRiskPortfolio);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_ModifyRiskPortfolio_Error() throws Exception {

		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setRiskPortfolioId(1);
		admRiskPortfolio.setPoints("points");
		admRiskPortfolio.setBehaviour("behaviour");
		admRiskPortfolio.setEquity(10);
		admRiskPortfolio.setDebt(12);
		admRiskPortfolio.setCash(13);

		when(adminDao.modifyRiskPortfolio(admRiskPortfolio)).thenReturn(0);
		int result = adminServiceImpl.modifyRiskPortfolio(admRiskPortfolio);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyRiskPortfolio(admRiskPortfolio);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeRiskPortfolio_Success() throws Exception {

		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setRiskPortfolioId(1);

		when(adminDao.removeRiskPortfolio(1)).thenReturn(1);
		int result = adminServiceImpl.removeRiskPortfolio(1);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeRiskPortfolio(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeRiskPortfolio_Error() throws Exception {

		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
		admRiskPortfolio.setRiskPortfolioId(1);

		when(adminDao.removeRiskPortfolio(1)).thenReturn(0);
		int result = adminServiceImpl.removeRiskPortfolio(1);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).removeRiskPortfolio(1);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyService_Error() throws Exception {
		Service service = new Service();
		service.setService("Service");
		service.setServiceId(1);
		when(adminDao.modifyService(service)).thenReturn(0);
		int result = adminServiceImpl.modifyService(service);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyService(service);
		verifyNoMoreInteractions(adminDao);

	}

	@Test
	public void test_removeService_Success() throws Exception {
		Service service = new Service();
		service.setServiceId(1);
		when(adminDao.removeService(1)).thenReturn(1);
		int result = adminServiceImpl.removeService(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).removeService(1);
		verifyNoMoreInteractions(adminDao);

	}

	@Test
	public void test_addLicense_Success() throws Exception {

		License license = new License();
		license.setLicense("bike");
		license.setIssuedBy("govt");
		license.setProdId(10);
		when(adminDao.license(license)).thenReturn(1);
		int result = adminServiceImpl.license(license);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).license(license);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addLicense_Error() throws Exception {

		License license = new License();
		license.setLicense("bike");
		license.setIssuedBy("govt");
		license.setProdId(10);

		when(adminDao.license(license)).thenReturn(0);
		int result = adminServiceImpl.license(license);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).license(license);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyLicense_Success() throws Exception {

		License license = new License();
		license.setLicId(2);
		license.setLicense("bike");
		license.setIssuedBy("govt");
		license.setProdId(10);

		when(adminDao.modifyLicense(license)).thenReturn(1);
		int result = adminServiceImpl.modifyLicense(license);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyLicense(license);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyLicense_Error() throws Exception {

		License license = new License();
		license.setLicId(2);
		license.setLicense("bike");
		license.setIssuedBy("govt");
		license.setProdId(10);

		when(adminDao.modifyLicense(license)).thenReturn(0);
		int result = adminServiceImpl.modifyLicense(license);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyLicense(license);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeLicense_Success() throws Exception {

		License license = new License();
		license.setLicId(2);

		when(adminDao.removeLicense(2)).thenReturn(1);
		int result = adminServiceImpl.removeLicense(2);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeLicense(2);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeLicense_Error() throws Exception {

		License license = new License();
		license.setLicId(2);

		when(adminDao.removeLicense(2)).thenReturn(0);
		int result = adminServiceImpl.removeLicense(2);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeLicense(2);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addUrgency_Success() throws Exception {
		
		Urgency urgency = new Urgency();
		urgency.setValue("bike");
	
		
		when(adminDao.urgency(urgency)).thenReturn(1);
		int result = adminServiceImpl.urgency(urgency);
		Assert.assertEquals(1, result);
		
		verify(adminDao, times(1)).urgency(urgency);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addUrgency_Error() throws Exception {
		
		Urgency urgency = new Urgency();
		urgency.setValue("bike");
		
		when(adminDao.urgency(urgency)).thenReturn(0);
		int result = adminServiceImpl.urgency(urgency);
		Assert.assertEquals(0, result);
		
		verify(adminDao, times(1)).urgency(urgency);
		verifyNoMoreInteractions(adminDao);
	}
	@Test
	public void test_modifyUrgency_Success() throws Exception {
		
		Urgency urgency = new Urgency();
		urgency.setValue("bike");
		urgency.setUrgencyId(2);

		
		when(adminDao.modifyUrgency(urgency)).thenReturn(1);
		int result = adminServiceImpl.modifyUrgency(urgency);
		Assert.assertEquals(1, result);
		
		verify(adminDao, times(1)).modifyUrgency(urgency);
		verifyNoMoreInteractions(adminDao);
	}
	
	@Test
	public void test_addVotetype_Success() throws Exception {
		Votetype votetype = new Votetype();
		votetype.setDesc("Votetype");
		when(adminDao.addVotetype(votetype)).thenReturn(1);
		int result = adminServiceImpl.addVotetype(votetype);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).addVotetype(votetype);
		verifyNoMoreInteractions(adminDao);
	}
	
	@Test
	public void test_addVotetype_Error() throws Exception {
		Votetype votetype = new Votetype();
		votetype.setDesc("Votetype");
		when(adminDao.addVotetype(votetype)).thenReturn(0);
		int result = adminServiceImpl.addVotetype(votetype);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).addVotetype(votetype);
		verifyNoMoreInteractions(adminDao);
	}
	@Test
	public void test_modifyVotetype_Success() throws Exception {
		Votetype votetype = new Votetype();
		votetype.setDesc("Votetype");
		votetype.setId(1);
		when(adminDao.modifyVotetype(votetype)).thenReturn(1);
		int result = adminServiceImpl.modifyVotetype(votetype);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyVotetype(votetype);
		verifyNoMoreInteractions(adminDao);
	}
	@Test
	public void test_modifyVotetype_Error() throws Exception {
		Votetype votetype = new Votetype();
		votetype.setDesc("Votetype");
		votetype.setId(1);
		when(adminDao.modifyVotetype(votetype)).thenReturn(0);
		int result = adminServiceImpl.modifyVotetype(votetype);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyVotetype(votetype);
		verifyNoMoreInteractions(adminDao);

	}
	@Test
	public void test_removeVotetype_Success() throws Exception {
		Votetype votetype = new Votetype();
		votetype.setId(1);
		when(adminDao.removeVotetype(1)).thenReturn(1);
		int result = adminServiceImpl.removeVotetype(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).removeVotetype(1);
		verifyNoMoreInteractions(adminDao);

	}
	

	@Test
	public void test_addBrand_Success() throws Exception {
		Brand brand = new Brand();
		brand.setBrand("Brand");
		when(adminDao.addBrand(brand)).thenReturn(1);
		int result = adminServiceImpl.addBrand(brand);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).addBrand(brand);
		verifyNoMoreInteractions(adminDao);
	}
	
	@Test
	public void test_addBrand_Error() throws Exception {
		Brand brand = new Brand();
		brand.setBrand("Brand");
		when(adminDao.addBrand(brand)).thenReturn(0);
		int result = adminServiceImpl.addBrand(brand);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).addBrand(brand);
	}
	@Test
	public void test_modifyBrand_Success() throws Exception {
		Brand brand = new Brand();
		brand.setBrand("Brand");
		brand.setBrandId(1);
		when(adminDao.modifyBrand(brand)).thenReturn(1);
		int result = adminServiceImpl.modifyBrand(brand);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).modifyBrand(brand);
		verifyNoMoreInteractions(adminDao);
	}
	@Test
	public void test_modifyBrand_Error() throws Exception {
		Brand brand = new Brand();
		brand.setBrand("Brand");
		brand.setBrandId(1);
		when(adminDao.modifyBrand(brand)).thenReturn(0);
		int result = adminServiceImpl.modifyBrand(brand);
		Assert.assertEquals(0, result);
		verify(adminDao, times(1)).modifyBrand(brand);
		verifyNoMoreInteractions(adminDao);

	}
	@Test
	public void test_removeBrand_Success() throws Exception {
		Brand brand = new Brand();
		brand.setBrandId(1);
		when(adminDao.removeBrand(1)).thenReturn(1);
		int result = adminServiceImpl.removeBrand(1);
		Assert.assertEquals(1, result);
		verify(adminDao, times(1)).removeBrand(1);
		verifyNoMoreInteractions(adminDao);
	}


	@Test
	public void test_addInsurance_Success() throws Exception {

		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItem("home loan");
		insuranceItem.setValue("annual rate");

		when(adminDao.addInsuranceItem(insuranceItem)).thenReturn(1);
		int result = adminServiceImpl.addInsuranceItem(insuranceItem);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addInsuranceItem(insuranceItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyUrgency_Error() throws Exception {
		
		Urgency urgency = new Urgency();
		urgency.setValue("bike");
		urgency.setUrgencyId(2);

		
		when(adminDao.modifyUrgency(urgency)).thenReturn(0);
		int result = adminServiceImpl.modifyUrgency(urgency);
		Assert.assertEquals(0, result);
		
		verify(adminDao, times(1)).modifyUrgency(urgency);
		verifyNoMoreInteractions(adminDao);
	}
	@Test
	public void test_removeUrgency_Success() throws Exception {
		
		Urgency urgency = new Urgency();
		urgency.setUrgencyId(2);
		
		when(adminDao.removeUrgency(2)).thenReturn(1);
		int result = adminServiceImpl.removeUrgency(2);
		Assert.assertEquals(1, result);
		
		verify(adminDao, times(1)).removeUrgency(2);
		verifyNoMoreInteractions(adminDao);
		}
	public void test_addInsurance_Error() throws Exception {

		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItem("home loan");
		insuranceItem.setValue("annual rate");

		when(adminDao.addInsuranceItem(insuranceItem)).thenReturn(0);
		int result = adminServiceImpl.addInsuranceItem(insuranceItem);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addInsuranceItem(insuranceItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyInsurance_Success() throws Exception {

		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItemId(1);
		insuranceItem.setInsuranceItem("home loan");
		insuranceItem.setValue("annual rate");

		when(adminDao.modifyInsuranceItem(insuranceItem)).thenReturn(1);
		int result = adminServiceImpl.modifyInsuranceItem(insuranceItem);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyInsuranceItem(insuranceItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeUrgency_Error() throws Exception {
		
		Urgency urgency = new Urgency();
		urgency.setUrgencyId(4);
		
		when(adminDao.removeUrgency(4)).thenReturn(0);
		int result = adminServiceImpl.removeUrgency(2);
		Assert.assertEquals(0, result);
		
		verify(adminDao, times(1)).removeUrgency(2);
		verifyNoMoreInteractions(adminDao);
	}
	@Test
	public void test_addAccount_Success() throws Exception {
		
		Account account	= new Account();
		account.setAccountEntry("bike");
		account.setAccountTypeId(1);
	
		
		when(adminDao.account(account)).thenReturn(1);
		int result = adminServiceImpl.account(account);
		Assert.assertEquals(1, result);
		
		verify(adminDao, times(1)).account(account);
		verifyNoMoreInteractions(adminDao);
		}
	public void test_modifyInsurance_Error() throws Exception {

		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItemId(1);
		insuranceItem.setInsuranceItem("house loan");
		insuranceItem.setValue("annual rate");

		when(adminDao.modifyInsuranceItem(insuranceItem)).thenReturn(0);
		int result = adminServiceImpl.modifyInsuranceItem(insuranceItem);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyInsuranceItem(insuranceItem);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeInsurance_Success() throws Exception {

		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItemId(2);

		when(adminDao.removeInsuranceItem(2)).thenReturn(1);
		int result = adminServiceImpl.removeInsuranceItem(2);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeInsuranceItem(2);
		verifyNoMoreInteractions(adminDao);
	}

	@Test

	public void test_addAccount_Error() throws Exception {
		
		Account account	= new Account();
		account.setAccountEntry("bike");
		account.setAccountTypeId(1);
		
		when(adminDao.account(account)).thenReturn(0);
		int result = adminServiceImpl.account(account);
		Assert.assertEquals(0, result);
		
		verify(adminDao, times(1)).account(account);
		verifyNoMoreInteractions(adminDao);
	}
	
	@Test
	public void test_modifyAccount_Success() throws Exception {
		
		Account account	= new Account();
		account.setAccountEntryId(1);
		account.setAccountEntry("bike");
		account.setAccountTypeId(1);

		
		when(adminDao.modifyAccount(account)).thenReturn(1);
		int result = adminServiceImpl.modifyAccount(account);
		Assert.assertEquals(1, result);
		
		verify(adminDao, times(1)).modifyAccount(account);
		verifyNoMoreInteractions(adminDao);
		}
	public void test_removeInsurance_Error() throws Exception {

		InsuranceItem insuranceItem = new InsuranceItem();
		insuranceItem.setInsuranceItemId(2);

		when(adminDao.removeInsuranceItem(2)).thenReturn(0);
		int result = adminServiceImpl.removeInsuranceItem(2);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeInsuranceItem(2);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_addUserType_Success() throws Exception {

		UserType userType = new UserType();
		userType.setDesc("home loan");

		when(adminDao.addUserType(userType)).thenReturn(1);
		int result = adminServiceImpl.addUserType(userType);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).addUserType(userType);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyAccount_Error() throws Exception {
		
		Account account	= new Account();
		account.setAccountEntryId(2);
		account.setAccountEntry("bike");
		account.setAccountTypeId(1);

		
		when(adminDao.modifyAccount(account)).thenReturn(0);
		int result = adminServiceImpl.modifyAccount(account);
		Assert.assertEquals(0, result);
		
		verify(adminDao, times(1)).modifyAccount(account);
		verifyNoMoreInteractions(adminDao);
	}
	
	@Test
	public void test_removeAccount_Success() throws Exception {
		
		Account account	= new Account();
		account.setAccountEntryId(2);
		
		when(adminDao.removeAccount(2)).thenReturn(1);
		int result = adminServiceImpl.removeAccount(2);
		Assert.assertEquals(1, result);
		
		verify(adminDao, times(1)).removeAccount(2);
		verifyNoMoreInteractions(adminDao);
		}
	public void test_addUserType_Error() throws Exception {

		UserType userType = new UserType();
		userType.setDesc("home loan");

		when(adminDao.addUserType(userType)).thenReturn(0);
		int result = adminServiceImpl.addUserType(userType);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).addUserType(userType);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyUserType_Success() throws Exception {

		UserType userType = new UserType();
		userType.setId(1);
		userType.setDesc("home loan");

		when(adminDao.modifyUserType(userType)).thenReturn(1);
		int result = adminServiceImpl.modifyUserType(userType);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).modifyUserType(userType);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_modifyUserType_Error() throws Exception {

		UserType userType = new UserType();
		userType.setId(1);
		userType.setDesc("home loan");

		when(adminDao.modifyUserType(userType)).thenReturn(0);
		int result = adminServiceImpl.modifyUserType(userType);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).modifyUserType(userType);
		verifyNoMoreInteractions(adminDao);
	}

	@Test
	public void test_removeUserType_Success() throws Exception {

		UserType userType = new UserType();
		userType.setId(2);

		when(adminDao.removeUserType(2)).thenReturn(1);
		int result = adminServiceImpl.removeUserType(2);
		Assert.assertEquals(1, result);

		verify(adminDao, times(1)).removeUserType(2);
		verifyNoMoreInteractions(adminDao);
	}

	@Test

	public void test_removeAccount_Error() throws Exception {
		
		Account account	= new Account();
		account.setAccountEntryId(2);
		
		when(adminDao.removeAccount(4)).thenReturn(0);
		int result = adminServiceImpl.removeAccount(2);
		Assert.assertEquals(0, result);
		
		verify(adminDao, times(1)).removeAccount(2);
		verifyNoMoreInteractions(adminDao);
	}
	
	public void test_removeUserType_Error() throws Exception {

		UserType userType = new UserType();
		userType.setId(2);

		when(adminDao.removeUserType(2)).thenReturn(0);
		int result = adminServiceImpl.removeUserType(2);
		Assert.assertEquals(0, result);

		verify(adminDao, times(1)).removeUserType(2);
		verifyNoMoreInteractions(adminDao);
	}
}
