package com.sowisetech.investor.service;

import static org.mockito.Mockito.doNothing;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.model.Goal;
import com.sowisetech.calc.model.Party;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.investor.dao.InvestorDao;
import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;
import com.sowisetech.investor.util.InvTableFields;

public class InvestorServiceImplTest {

	@InjectMocks
	private InvestorServiceImpl investorServiceImpl;

	@Mock
	private InvTableFields invTableFields;
	@Autowired(required = true)
	@Spy
	private AdvTableFields advTableFields;

	private MockMvc mockMvc;
	@Mock
	private InvestorDao investorDao;
	@Mock
	private AuthDao authDao;

	@Mock
	SecurityContextHolder mockSecurityContext;
	@Autowired(required = true)
	@Spy
	AdminSignin adminSignin;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(investorServiceImpl).build();
	}

	// fetch investor Test// //need to change
//	@Test
//	public void test_fetchByInvestorId() throws Exception {
//		Investor inv = new Investor();
//		inv.setInvId("INV000000000A");
//		inv.setFullName("Dobby");
//		String deleteflag = invTableFields.getDelete_flag_N();
//		String encryptPass = advTableFields.getEncryption_password();
//		when(investorDao.fetchPartyIdByRoleBasedId("INV000000000A", deleteflag)).thenReturn(1L);
//		when(investorDao.fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(), encryptPass))
//				.thenReturn(inv);
//		Investor investor = investorServiceImpl.fetchByInvestorId("INV000000000A");
//
//		Assert.assertEquals("Dobby", investor.getFullName());
//		verify(investorDao, times(1)).fetchPartyIdByRoleBasedId("INV000000000A", deleteflag);
//		verify(investorDao, times(1)).fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(),
//				encryptPass);
//		verifyNoMoreInteractions(investorDao);
//	}

	// fetch investor Negative Test//
	@Test
	public void test_fetchByInvestorIdNull() throws Exception {
		Investor inv = new Investor();
		String deleteflag = invTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(investorDao.fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(), encryptPass))
				.thenReturn(inv);
		when(investorDao.fetchPartyIdByRoleBasedId("INV000000000A", deleteflag)).thenReturn(1L);

		Investor result = investorServiceImpl.fetchByInvestorId("INV000000000A");

		Assert.assertEquals(null, result);

		verify(investorDao, times(1)).fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(),
				encryptPass);
		verify(investorDao, times(1)).fetchPartyIdByRoleBasedId("INV000000000A", deleteflag);
		verifyNoMoreInteractions(investorDao);
	}

	// fetch Investor List Test//
	@Test
	public void test_fetchAll() throws Exception {
		List<Investor> investors = new ArrayList<Investor>();
		Investor inv1 = new Investor();
		inv1.setInvId("INV000000000A");
		inv1.setFullName("AAA");
		Investor inv2 = new Investor();
		inv2.setInvId("INV000000000B");
		inv2.setFullName("BBB");
		investors.add(inv1);
		investors.add(inv2);
		String deleteflag = invTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(investorDao.fetchInvestor(pageable, invTableFields.getDelete_flag_N(), encryptPass)).thenReturn(investors);
		when(investorDao.fetchPartyIdByRoleBasedId("INV000000000A", deleteflag)).thenReturn(1L);
		when(investorDao.fetchPartyIdByRoleBasedId("INV000000000B", deleteflag)).thenReturn(2L);
		List<Investor> result = investorServiceImpl.fetchInvestorList(0, 10);
		Assert.assertEquals(2, result.size());
		verify(investorDao, times(1)).fetchInvestor(pageable, invTableFields.getDelete_flag_N(), encryptPass);
		verify(investorDao, times(1)).fetchPartyIdByRoleBasedId("INV000000000A", deleteflag);
		verify(investorDao, times(1)).fetchPartyIdByRoleBasedId("INV000000000B", deleteflag);

		verifyNoMoreInteractions(investorDao);
	}

	// fetch Investor List Negative Test//
	@Test
	public void test_fetchAllNull() throws Exception {
		List<Investor> investors = new ArrayList<Investor>();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(investorDao.fetchInvestor(pageable, invTableFields.getDelete_flag_N(), encryptPass)).thenReturn(investors);
		List<Investor> result = investorServiceImpl.fetchInvestorList(0, 10);
		Assert.assertEquals(0, result.size());
		verify(investorDao, times(1)).fetchInvestor(pageable, invTableFields.getDelete_flag_N(), encryptPass);
		verifyNoMoreInteractions(investorDao);
	}

	// modify investor Test//
	@Test
	public void test_modifyInvestor() throws Exception {
		String deleteflag = invTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setFullName("Shimba");
		inv.setUpdated(timestamp);
		inv.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(investorDao.fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(), encryptPass))
				.thenReturn(inv);
		when(investorDao.fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass)).thenReturn(party);
		when(investorDao.update("INV000000000A", inv, encryptPass)).thenReturn(1);
		// when(investorDao.updatePersonalInfoInParty(inv, "INV000000000A",
		// encryptPass)).thenReturn(1);

		int result = investorServiceImpl.modifyInvestor("INV000000000A", inv);
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(),
				encryptPass);
		verify(investorDao, times(1)).fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass);
		verify(investorDao, times(1)).update("INV000000000A", inv, encryptPass);
		// verify(investorDao, times(1)).updatePersonalInfoInParty(inv, "INV000000000A",
		// encryptPass);
		verifyNoMoreInteractions(investorDao);
	}

	// modify investor Negative Test//
	@Test
	public void test_modifyInvestorError() throws Exception {
		String deleteflag = invTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setFullName("Shimba");
		inv.setUpdated(timestamp);
		inv.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(investorDao.fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(), encryptPass))
				.thenReturn(inv);
		when(investorDao.fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass)).thenReturn(party);
		when(investorDao.update("INV000000000A", inv, encryptPass)).thenReturn(0);

		int result = investorServiceImpl.modifyInvestor("INV000000000A", inv);
		Assert.assertEquals(0, result);

		verify(investorDao, times(1)).fetchInvestorByInvId("INV000000000A", invTableFields.getDelete_flag_N(),
				encryptPass);
		verify(investorDao, times(1)).fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass);
		verify(investorDao, times(1)).update("INV000000000A", inv, encryptPass);
		// verifyNoMoreInteractions(investorDao);
	}

	// remove investor Test//
	@Test
	public void test_removeInvestor() throws Exception {

		String deleteflag = invTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setFullName("Shimba");
		inv.setUpdated(timestamp);
		inv.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass)).thenReturn(party);
		when(investorDao.deleteInvestor("INV000000000A", invTableFields.delete_flag_N, "INV000000000A")).thenReturn(1);
		int result = investorServiceImpl.removeInvestor("INV000000000A");
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass);
		verify(investorDao, times(1)).deleteInvestor("INV000000000A", invTableFields.delete_flag_N, "INV000000000A");
		verifyNoMoreInteractions(investorDao);
	}

	// remove investor Negative Test//
	@Test
	public void test_removeInvestorError() throws Exception {
		String deleteflag = invTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setFullName("Shimba");
		inv.setUpdated(timestamp);
		inv.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("admin@gmail.com", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass)).thenReturn(party);
		when(investorDao.deleteInvestor("INV000000000A", invTableFields.delete_flag_N, "INV000000000A")).thenReturn(0);
		int result = investorServiceImpl.removeInvestor("INV000000000A");
		Assert.assertEquals(0, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("admin@gmail.com", deleteflag, encryptPass);
		verify(investorDao, times(1)).deleteInvestor("INV000000000A", invTableFields.delete_flag_N, "INV000000000A");
		verifyNoMoreInteractions(investorDao);
	}

	// add investor interest Test //
	@Test
	public void test_addInvestorInterest() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		InvInterest invInterest1 = new InvInterest();
		invInterest1.setProdId(1);
		invInterest1.setScale("1");
		invInterest1.setCreated(timestamp);
		invInterest1.setUpdated(timestamp);
		invInterest1.setCreated_by(signedUserId);
		invInterest1.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.addInvestorInterest("INV000000000A", invInterest1, invTableFields.delete_flag_N))
				.thenReturn(1);
		int result = investorServiceImpl.addInvestorInterest("INV000000000A", invInterest1);
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).addInvestorInterest("INV000000000A", invInterest1, invTableFields.delete_flag_N);
		verifyNoMoreInteractions(investorDao);
	}

	// add investor interest Negative Test //
	@Test
	public void test_addInvestorInterestError() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");

		String signedUserId = party.getRoleBasedId();

		InvInterest invInterest1 = new InvInterest();
		invInterest1.setProdId(1);
		invInterest1.setScale("1");
		invInterest1.setCreated(timestamp);
		invInterest1.setUpdated(timestamp);
		invInterest1.setCreated_by(signedUserId);
		invInterest1.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.addInvestorInterest("INV000000000A", invInterest1, delete_flag)).thenReturn(0);
		int result = investorServiceImpl.addInvestorInterest("INV000000000A", invInterest1);
		Assert.assertEquals(0, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).addInvestorInterest("INV000000000A", invInterest1, delete_flag);
		verifyNoMoreInteractions(investorDao);

	}

	// modify investor interest Test//
	@Test
	public void test_modifyInvestorInterest() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		InvInterest invInterest = new InvInterest();
		invInterest.setInterestId(2);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.fetchInvInterest(2, delete_flag)).thenReturn(invInterest);
		when(investorDao.updateInvestorInterest(2, invInterest)).thenReturn(1);

		int result = investorServiceImpl.modifyInvestorInterest(2, invInterest);
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).fetchInvInterest(2, delete_flag);
		verify(investorDao, times(1)).updateInvestorInterest(2, invInterest);
		verifyNoMoreInteractions(investorDao);
	}

	// modify investor interest Negative Test//
	@Test
	public void test_modifyInvestorInterestError() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		InvInterest invInterest = new InvInterest();
		invInterest.setInterestId(2);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.fetchInvInterest(2, invTableFields.delete_flag_N)).thenReturn(invInterest);
		when(investorDao.updateInvestorInterest(2, invInterest)).thenReturn(0);

		int result = investorServiceImpl.modifyInvestorInterest(2, invInterest);
		Assert.assertEquals(0, result);
		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).fetchInvInterest(2, invTableFields.delete_flag_N);
		verify(investorDao, times(1)).updateInvestorInterest(2, invInterest);
		verifyNoMoreInteractions(investorDao);
	}

	// remove InvInterest Test//
	@Test
	public void test_removeInvestorInterest() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		InvInterest invInterest = new InvInterest();
		invInterest.setInterestId(2);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.deleteInvestorInterest(1, invTableFields.delete_flag_N, "INV000000000A")).thenReturn(1);

		int result = investorServiceImpl.removeInvestorInterest(1);
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).deleteInvestorInterest(1, invTableFields.delete_flag_N, "INV000000000A");
		verifyNoMoreInteractions(investorDao);
	}

	// remove InvInterest Negative Test//
	@Test
	public void test_removeInvestorInterestError() throws Exception {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		InvInterest invInterest = new InvInterest();
		invInterest.setInterestId(2);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by(signedUserId);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.deleteInvestorInterest(1, invTableFields.delete_flag_N, "INV000000000A")).thenReturn(0);

		int result = investorServiceImpl.removeInvestorInterest(1);
		Assert.assertEquals(0, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).deleteInvestorInterest(1, invTableFields.delete_flag_N, "INV000000000A");
		verifyNoMoreInteractions(investorDao);
	}

	// Fetch Category Test //
	@Test
	public void test_fetchCategoryById() throws Exception {
		Category categ = new Category();
		categ.setCategoryId(2);
		categ.setDesc("category");

		when(investorDao.fetchCategoryById(2)).thenReturn(categ);
		Category cat = investorServiceImpl.fetchCategoryById(2);
		Assert.assertEquals("category", cat.getDesc());

		verify(investorDao, times(1)).fetchCategoryById(2);
		verifyNoMoreInteractions(investorDao);
	}

	// Fetch Category Negative Test //
	@Test
	public void test_fetchCategoryByIdNull() throws Exception {
		when(investorDao.fetchCategoryById(2)).thenReturn(null);
		Category result = investorServiceImpl.fetchCategoryById(2);
		Assert.assertEquals(null, result);

		verify(investorDao, times(1)).fetchCategoryById(2);
		verifyNoMoreInteractions(investorDao);
	}

	// Find Duplicate Test//
	@Test
	public void test_findDuplicate() throws Exception {
		when(investorDao.findDuplicate("INV000000000A", 2)).thenReturn(true);
		boolean result = investorServiceImpl.findDuplicate("INV000000000A", 2);
		Assert.assertEquals(true, result);
		verify(investorDao, times(1)).findDuplicate("INV000000000A", 2);
		verifyNoMoreInteractions(investorDao);
	}

	// Find Duplicate Negative Test//
	@Test
	public void test_findDuplicateError() throws Exception {
		when(investorDao.findDuplicate("INV000000000A", 2)).thenReturn(false);
		boolean result = investorServiceImpl.findDuplicate("INV000000000A", 2);
		Assert.assertEquals(false, result);
		verify(investorDao, times(1)).findDuplicate("INV000000000A", 2);
		verifyNoMoreInteractions(investorDao);
	}

	// Fetch InvInterest Test//
	@Test
	public void test_fetchByInvInterestById() throws Exception {
		InvInterest invInterest = new InvInterest();
		invInterest.setInvId("INV000000000A");
		invInterest.setInterestId(1);
		invInterest.setProdId(1);

		when(investorDao.fetchInvInterest(1, invTableFields.delete_flag_N)).thenReturn(invInterest);

		InvInterest invInterest1 = investorServiceImpl.fetchByInvInterestById(1);
		Assert.assertEquals(1, invInterest1.getProdId());

		verify(investorDao, times(1)).fetchInvInterest(1, invTableFields.delete_flag_N);
		verifyNoMoreInteractions(investorDao);
	}

	// Fetch InvInterest Negative Test//
	@Test
	public void test_fetchByInvInterestByIdNull() throws Exception {
		when(investorDao.fetchInvInterest(1, invTableFields.delete_flag_N)).thenReturn(null);

		InvInterest result = investorServiceImpl.fetchByInvInterestById(1);
		Assert.assertEquals(null, result);

		verify(investorDao, times(1)).fetchInvInterest(1, invTableFields.delete_flag_N);
		verifyNoMoreInteractions(investorDao);
	}

	// Encrypt Test//
	@Test
	public void test_encrypt() throws Exception {
		when(investorDao.encrypt("!@AS12as")).thenReturn("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=");
		String encryptedText = investorServiceImpl.encrypt("!@AS12as");
		Assert.assertEquals("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=", encryptedText);
		verify(investorDao, times(1)).encrypt("!@AS12as");
		verifyNoMoreInteractions(investorDao);
	}

	// Decrypt Test//
	@Test
	public void test_decrypt() throws Exception {
		when(investorDao.decrypt("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=")).thenReturn("!@AS12as");
		String encryptedText = investorServiceImpl.decrypt("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=");
		Assert.assertEquals("!@AS12as", encryptedText);
		verify(investorDao, times(1)).decrypt("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=");
		verifyNoMoreInteractions(investorDao);
	}

	@Test
	public void test_addAndModifyInvestorInterest_modify() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		List<InvInterest> invInterestList = new ArrayList<>();
		InvInterest invInterest = new InvInterest();
		invInterest.setInterestId(2);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by(signedUserId);
		invInterestList.add(invInterest);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.fetchInvInterest(2, delete_flag)).thenReturn(invInterest);
		when(investorDao.updateInvestorInterest(2, invInterest)).thenReturn(1);

		int result = investorServiceImpl.addAndModifyInvestorInterest("INV000000000A", invInterestList);
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).fetchInvInterest(2, delete_flag);
		verify(investorDao, times(1)).updateInvestorInterest(2, invInterest);
		verifyNoMoreInteractions(investorDao);
	}

	@Test
	public void test_CheckInvestorIsPresent() throws Exception {
		String deleteflag = invTableFields.getDelete_flag_N();
		when(investorDao.checkInvestorIsPresent("INV0000000000", deleteflag)).thenReturn(1);
		int result = investorServiceImpl.CheckInvestorIsPresent("INV0000000000");
		Assert.assertEquals(1, result);
		verify(investorDao, times(1)).checkInvestorIsPresent("INV0000000000", deleteflag);
		verifyNoMoreInteractions(investorDao);
	}

	// public void test_CheckInvestorIsPresent() throws Exception {
	// String deleteflag = invTableFields.getDelete_flag_N();
	// when(investorDao.checkInvestorIsPresent("INV0000000000",
	// deleteflag)).thenReturn(1);
	// int result = investorServiceImpl.CheckInvestorIsPresent("INV0000000000");
	// Assert.assertEquals(1, result);
	// verify(investorDao, times(1)).checkInvestorIsPresent("INV0000000000",
	// deleteflag);
	// }

	@Test
	public void test_addAndModifyInvestorInterest_add() throws Exception {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("INV000000000A");
		String signedUserId = party.getRoleBasedId();

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = invTableFields.getDelete_flag_N();
		List<InvInterest> invInterestList = new ArrayList<>();
		InvInterest invInterest = new InvInterest();
		invInterest.setInterestId(0);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by(signedUserId);
		invInterestList.add(invInterest);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));

		when(investorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);
		when(investorDao.fetchInvInterest(2, delete_flag)).thenReturn(null);
		when(investorDao.addInvestorInterest("INV000000000A", invInterest, invTableFields.delete_flag_N)).thenReturn(1);

		int result = investorServiceImpl.addAndModifyInvestorInterest("INV000000000A", invInterestList);
		Assert.assertEquals(1, result);

		verify(investorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(investorDao, times(1)).addInvestorInterest("INV000000000A", invInterest, invTableFields.delete_flag_N);
		verifyNoMoreInteractions(investorDao);
	}

	// public void test_CheckInvestorIsPresent_Error() throws Exception {
	// String deleteflag = invTableFields.getDelete_flag_Y();
	// when(investorDao.checkInvestorIsPresent("INV0000000000",
	// deleteflag)).thenReturn(0);
	// int result = investorServiceImpl.CheckInvestorIsPresent("INV0000000000");
	// Assert.assertEquals(0, result);
	// verify(investorDao, times(1)).checkInvestorIsPresent("INV0000000000",
	// deleteflag);
	// verifyNoMoreInteractions(investorDao);
	// }

	@Test
	public void test_CheckInvInterestIsPresent() throws Exception {
		String deleteflag = invTableFields.getDelete_flag_N();
		when(investorDao.CheckInvInterestIsPresent(1, deleteflag)).thenReturn(1);
		int result = investorServiceImpl.CheckInvInterestIsPresent(1);
		Assert.assertEquals(1, result);
		verify(investorDao, times(1)).CheckInvInterestIsPresent(1, deleteflag);
		verifyNoMoreInteractions(investorDao);
	}

	@Test
	public void test_CheckInvInterestIsPresent_Error() throws Exception {
		String deleteflag = invTableFields.getDelete_flag_Y();
		when(investorDao.CheckInvInterestIsPresent(1, deleteflag)).thenReturn(0);
		int result = investorServiceImpl.CheckInvInterestIsPresent(1);
		Assert.assertEquals(0, result);
		verify(investorDao, times(1)).CheckInvInterestIsPresent(1, deleteflag);
		verifyNoMoreInteractions(investorDao);
	}

	@Test
	public void test_CheckCategoryIsPresent() throws Exception {
		when(investorDao.CheckCategoryIsPresent(1)).thenReturn(1);
		int result = investorServiceImpl.CheckCategoryIsPresent(1);
		Assert.assertEquals(1, result);
		verify(investorDao, times(1)).CheckCategoryIsPresent(1);
		verifyNoMoreInteractions(investorDao);
	}

	@Test
	public void test_CheckCategoryIsPresent_Error() throws Exception {
		when(investorDao.CheckCategoryIsPresent(100)).thenReturn(0);
		int result = investorServiceImpl.CheckCategoryIsPresent(100);
		Assert.assertEquals(0, result);
		verify(investorDao, times(1)).CheckCategoryIsPresent(100);
		verifyNoMoreInteractions(investorDao);
	}

}
