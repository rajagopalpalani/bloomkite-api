package com.sowisetech.advisor.service;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.advisor.dao.AdvisorDao;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.AdvisorType;
import com.sowisetech.advisor.model.ArticleStatus;
import com.sowisetech.advisor.model.Award;
import com.sowisetech.advisor.model.Brand;
import com.sowisetech.advisor.model.Category;
import com.sowisetech.advisor.model.CategoryType;
import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.FollowerStatus;
import com.sowisetech.advisor.model.Followers;
import com.sowisetech.advisor.model.FollowersList;
import com.sowisetech.advisor.model.ForumCategory;
import com.sowisetech.advisor.model.ForumStatus;
import com.sowisetech.advisor.model.ForumSubCategory;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.KeyPeople;
import com.sowisetech.advisor.model.License;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.model.PartyStatus;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.PublicProfile;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.Service;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.StateCity;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.sowisetech.advisor.model.AdvBrandInfo;
import com.sowisetech.advisor.model.AdvBrandRank;
import com.sowisetech.advisor.model.AdvProduct;
import com.sowisetech.advisor.request.AdvProductRequest;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.dao.CalcDao;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.investor.model.Investor;

public class AdvisorServiceImplTest {

	@InjectMocks
	private AdvisorServiceImpl advisorServiceImpl;

	private MockMvc mockMvc;
	@Mock
	private AdvisorDao advisorDao;
	@Mock
	private CalcDao calcDao;

	@Mock
	private AuthDao authDao;

	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;
	@Autowired(required = true)
	@Spy
	AdminSignin adminSignin;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(advisorServiceImpl).build();
	}

	@Test
	public void test_signup_Success() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("AAA");
		adv1.setEmailId("aaa@aaa.com");
		adv1.setPhoneNumber("9874563210");
		adv1.setPassword("!@AS12as");
		adv1.setUserName("advisor");
		String advType = advTableFields.getAdvType();
		String desc = advTableFields.getPartystatus_desc();
		String name = advTableFields.getRoleName();
		String workFlowDefault = advTableFields.getWorkFlow_Default();
		long roleId = 1;
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		int promoCount = advTableFields.getPromoCount();

		int isPrimaryRole = advTableFields.getIs_primary_role_true();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
		when(advisorDao.fetchPartyStatusIdByDesc(desc)).thenReturn(1L);
		when(advisorDao.fetchWorkFlowStatusIdByDesc(workFlowDefault)).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.advSignup(adv1, encryptPass, promoCount)).thenReturn(1);
		when(authDao.fetchRoleIdByName(name)).thenReturn(1);
		when(advisorDao.addParty(adv1, encryptPass)).thenReturn(1);
		when(advisorDao.addSigninVerification(adv1.getEmailId(), encryptPass)).thenReturn(1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		when(authDao.addUser_role(1, 1, "ADV000000000A", isPrimaryRole)).thenReturn(1);
		int result = advisorServiceImpl.advSignup(adv1, roleId);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).advSignup(adv1, encryptPass, promoCount);
	}

	@Test
	public void test_signup_Negative() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("AAA");
		adv1.setEmailId("aaa@aaa.com");
		adv1.setPhoneNumber("9874563210");
		adv1.setPassword("!@AS12as");
		long roleId = 2;
		String deleteflag = advTableFields.getDelete_flag_N();
		String advType = advTableFields.getAdvType();
		String name = advTableFields.getRoleName();
		String workFlowDefault = advTableFields.getWorkFlow_Default();
		String desc = advTableFields.getPartystatus_desc();
		String encryptPass = advTableFields.getEncryption_password();
		int promoCount = advTableFields.getPromoCount();

		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
		// when(advisorDao.fetchTypeIdByAdvtype(advType)).thenReturn(1);
		when(advisorDao.fetchPartyStatusIdByDesc(desc)).thenReturn(1L);
		when(advisorDao.fetchWorkFlowStatusIdByDesc(workFlowDefault)).thenReturn(1);
		when(advisorDao.advSignup(adv1, encryptPass, promoCount)).thenReturn(0);
		when(authDao.fetchRoleIdByName(name)).thenReturn(1);

		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		int isPrimaryRole = advTableFields.getIs_primary_role_true();
		when(authDao.addUser_role(1, 1, "ADV000000000A", isPrimaryRole)).thenReturn(1);
		// when(advisorDao.addParty(adv1, 1L)).thenReturn(1);
		int result = advisorServiceImpl.advSignup(adv1, roleId);

		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).advSignup(adv1, encryptPass, promoCount);
	}

	@Test
	public void test_fetchByAdvisorId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("Dobby");
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setBrandId(1);
		advBrandRankList.add(advBrandRank);
		adv1.setAdvBrandRank(advBrandRankList);
		// Brand brand = new Brand();
		// brand.setBrand("Aditya Birla Sun Life");
		// brand.setBrandId(1);
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		when(advisorDao.fetchAdvisorPromoCount("ADV000000000A", deleteflag)).thenReturn(1);
		when(calcDao.fetchPlanByPartyId(1, encryptPass)).thenReturn(null);
		Advisor adv = advisorServiceImpl.fetchByAdvisorId("ADV000000000A");
		Assert.assertEquals("Dobby", adv.getName());
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchBrandByBrandId(1);
		verify(advisorDao, times(1)).fetchAdvisorPromoCount("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchByAdvisorId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setName("Dobby");
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setBrandId(1);
		advBrandRankList.add(advBrandRank);
		adv1.setAdvBrandRank(advBrandRankList);
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		when(advisorDao.fetchAdvisorPromoCount("ADV000000000A", deleteflag)).thenReturn(1);
		Advisor adv = advisorServiceImpl.fetchByAdvisorId("ADV000000000A");
		Assert.assertEquals(null, adv);
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).fetchAdvisorPromoCount("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_fetchAdvisorList() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// Advisor adv1 = new Advisor();
	// adv1.setAdvId("ADV000000000A");
	// adv1.setName("AAA");
	// Advisor adv2 = new Advisor();
	// adv2.setAdvId("ADV000000000B");
	// adv2.setName("BBB");
	// advisors.add(adv1);
	// advisors.add(adv2);
	// List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
	// AdvBrandRank advBrandRank = new AdvBrandRank();
	// advBrandRank.setAdvBrandRankId(1);
	// advBrandRank.setBrandId(1);
	// advBrandRankList.add(advBrandRank);
	// adv1.setAdvBrandRank(advBrandRankList);
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchAdvisorList(1, 1, deleteflag,
	// encryptPass)).thenReturn(advisors);
	// when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(1L);
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag)).thenReturn(2L);
	//
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchAdvisorList(1, 1);
	// Assert.assertEquals(2, returnAdvisors.size());
	// verify(advisorDao, times(1)).fetchAdvisorList(1, 1, deleteflag, encryptPass);
	// verify(advisorDao, times(1)).fetchBrandByBrandId(1);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test
	// public void test_fetchAdvisorList_Error() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchAdvisorList(1, 1, deleteflag,
	// encryptPass)).thenReturn(advisors);
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchAdvisorList(1, 1);
	// Assert.assertEquals(null, returnAdvisors);
	// verify(advisorDao, times(1)).fetchAdvisorList(1, 1, deleteflag, encryptPass);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_removeAdvisor() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("Shimpa");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.removeAdvisor("ADV000000000A", deleteflag, "ADV000000000A", 9)).thenReturn(1);
		when(advisorDao.removePublicAdvisorChild("ADV000000000A")).thenReturn(1);
		when(advisorDao.removePublicAdvisor("ADV000000000A")).thenReturn(1);
		advisorServiceImpl.removeAdvisor("ADV000000000A", 9);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).removeAdvisor("ADV000000000A", deleteflag, "ADV000000000A", 9);
		verify(advisorDao, times(1)).removePublicAdvisorChild("ADV000000000A");
		verify(advisorDao, times(1)).removePublicAdvisor("ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_modifyAdvisor() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// Advisor adv1 = new Advisor();
	// adv1.setAdvId("ADV000000000A");
	// adv1.setName("Shakshi");
	// Advisor adv = new Advisor();
	// adv.setAdvId("ADV000000000A");
	// adv.setName("Adv");
	// adv.setCity("city");
	// adv.setPhoneNumber("9958412630");
	// adv.setPanNumber("bloom1234k");
	// adv.setEmailId("adv@gmail.com");
	// adv.setImagePath("img");
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// List<ArticlePost> articleList = new ArrayList<ArticlePost>();
	// ArticlePost articlePost = new ArticlePost();
	// articlePost.setArticleId(1);
	// articlePost.setImagePath("img");
	// articleList.add(articlePost);
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	//
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	// when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag,
	// encryptPass)).thenReturn(adv);
	// when(advisorDao.update("ADV000000000A", adv, encryptPass)).thenReturn(1);
	// when(advisorDao.updatePersonalInfoInParty(adv.getEmailId(),
	// adv.getPhoneNumber(), adv.getPanNumber(),
	// adv.getUserName(), "ADV000000000A", encryptPass,
	// "ADV000000000A")).thenReturn(1);
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(1L);
	// when(advisorDao.fetchArticlePostList(1, deleteflag,
	// encryptPass)).thenReturn(articleList);
	// when(advisorDao.updateArticlePostInfoByPartyId("img", 1L,
	// deleteflag)).thenReturn(1);
	//
	// int result = advisorServiceImpl.modifyAdvisor("ADV000000000A", adv1);
	// Assert.assertEquals(1, result);
	//
	// verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV000000000A", deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).update("ADV000000000A", adv, encryptPass);
	// verify(advisorDao, times(1)).updatePersonalInfoInParty(adv.getEmailId(),
	// adv.getPhoneNumber(),
	// adv.getPanNumber(), adv.getUserName(), "ADV000000000A", encryptPass,
	// "ADV000000000A");
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag);
	// verify(advisorDao, times(1)).fetchArticlePostList(1, deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).updateArticlePostInfoByPartyId("img", 1L,
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_addAdvProfessionalInfo() throws Exception {
		List<Award> awards = new ArrayList<Award>();
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setAwardId(1);
		awards.add(award);
		List<Certificate> certificates = new ArrayList<Certificate>();
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setCertificateId(1);
		certificates.add(certificate);
		List<Education> educations = new ArrayList<Education>();
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setEduId(1);
		educations.add(education);
		List<Experience> experiences = new ArrayList<Experience>();
		Experience experience = new Experience();
		experience.setLocation("chennai");
		experiences.add(experience);
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setAwards(awards);
		adv1.setCertificates(certificates);
		adv1.setEducations(educations);
		adv1.setExperiences(experiences);
		Advisor adv = new Advisor();
		adv1.setAdvId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv);
		when(advisorDao.addAdvAwardInfo("ADV000000000A", award, deleteflag)).thenReturn(1);
		when(advisorDao.addAdvCertificateInfo("ADV000000000A", certificate, deleteflag)).thenReturn(1);
		when(advisorDao.addAdvEducationInfo("ADV000000000A", education, deleteflag)).thenReturn(1);
		when(advisorDao.addAdvExperienceInfo("ADV000000000A", experience, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvProfessionalInfo("ADV000000000A", adv1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvAwardInfo("ADV000000000A", award, deleteflag);
		verify(advisorDao, times(1)).addAdvCertificateInfo("ADV000000000A", certificate, deleteflag);
		verify(advisorDao, times(1)).addAdvEducationInfo("ADV000000000A", education, deleteflag);
		verify(advisorDao, times(1)).addAdvExperienceInfo("ADV000000000A", experience, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvProfessionalInfo_Certificate() throws Exception {
		List<Certificate> certificates = new ArrayList<Certificate>();
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setCertificateId(1);
		certificates.add(certificate);
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setCertificates(certificates);
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.addAdvCertificateInfo("ADV000000000A", certificate, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvCertificateInfo("ADV000000000A", certificate);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addAdvProfessionalInfo_Experience() throws Exception {
		List<Experience> experiences = new ArrayList<Experience>();
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setExpId(1);
		experiences.add(experience);
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setExperiences(experiences);
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.addAdvExperienceInfo("ADV000000000A", experience, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvExperienceInfo("ADV000000000A", experience);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addAdvProfessionalInfo_Education() throws Exception {
		List<Education> educations = new ArrayList<Education>();
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setEduId(1);
		educations.add(education);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setEducations(educations);

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.addAdvEducationInfo("ADV000000000A", education, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvEducationInfo("ADV000000000A", education);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addAdvProductInfo_Success() throws Exception {
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvProductInfo("ADV000000000A", advProduct, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvProductInfo("ADV000000000A", advProduct);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).addAdvProductInfo("ADV000000000A", advProduct, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvProductInfo_Error() throws Exception {
		AdvProduct advProduct = new AdvProduct();
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvProductInfo("ADV000000000A", advProduct, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addAdvProductInfo("ADV000000000A", advProduct);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).addAdvProductInfo("ADV000000000A", advProduct, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAward_Success() throws Exception {
		Award award = new Award();
		award.setIssuedBy("IRDA");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchAward(1, deleteflag)).thenReturn(award);
		Award awd = advisorServiceImpl.fetchAward(1);
		Assert.assertEquals("IRDA", awd.getIssuedBy());

		verify(advisorDao, times(1)).fetchAward(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAward_Error() throws Exception {
		Award award = new Award();
		award.setIssuedBy("IRDA");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchAward(1, deleteflag)).thenReturn(null);
		Award awd = advisorServiceImpl.fetchAward(1);
		Assert.assertEquals(null, awd);
		verify(advisorDao, times(1)).fetchAward(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchCertificate_Success() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setIssuedBy("IRDA");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchCertificate(1, deleteflag)).thenReturn(certificate);
		Certificate cert = advisorServiceImpl.fetchCertificate(1);
		Assert.assertEquals("IRDA", cert.getIssuedBy());
		verify(advisorDao, times(1)).fetchCertificate(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchCertificate_Error() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setIssuedBy("IRDA");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchCertificate(1, deleteflag)).thenReturn(null);
		Certificate cert = advisorServiceImpl.fetchCertificate(1);
		Assert.assertEquals(null, cert);
		verify(advisorDao, times(1)).fetchCertificate(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchExperience_Success() throws Exception {
		Experience experience = new Experience();
		experience.setLocation("chennai");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchExperience(1, deleteflag)).thenReturn(experience);
		Experience exp = advisorServiceImpl.fetchExperience(1);
		Assert.assertEquals("chennai", exp.getLocation());
		verify(advisorDao, times(1)).fetchExperience(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchExperience_Error() throws Exception {
		Experience experience = new Experience();
		experience.setLocation("chennai");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchExperience(1, deleteflag)).thenReturn(null);
		Experience exp = advisorServiceImpl.fetchExperience(1);
		Assert.assertEquals(null, exp);
		verify(advisorDao, times(1)).fetchExperience(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchEducation_Success() throws Exception {
		Education education = new Education();
		education.setInstitution("IIT");
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.fetchEducation(1, deleteflag)).thenReturn(education);
		Education edu = advisorServiceImpl.fetchEducation(1);
		Assert.assertEquals("IIT", edu.getInstitution());
		verify(advisorDao, times(1)).fetchEducation(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchEducation_Error() throws Exception {
		Education education = new Education();
		education.setInstitution("IIT");
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchEducation(1, deleteflag)).thenReturn(null);
		Education edu = advisorServiceImpl.fetchEducation(1);
		Assert.assertEquals(null, edu);
		verify(advisorDao, times(1)).fetchEducation(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAward_Success() throws Exception {
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setTitle("Oskar");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvAward(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvAward(1, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvAward(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAward_Error() throws Exception {
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setTitle("Oskar");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvAward(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvAward(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvAward(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeCertificate_Success() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setTitle("Oskar");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvCertificate(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvCertificate(1, "ADV000000000A");
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvCertificate(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeCertificate_Error() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setTitle("Oskar");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvCertificate(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvCertificate(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvCertificate(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeEducation_Success() throws Exception {
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setDegree("BE");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvEducation(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvEducation(1, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvEducation(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeEducation_Error() throws Exception {
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setDegree("BE");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvEducation(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvEducation(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvEducation(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeExperience_Success() throws Exception {
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setDesignation("Lawyer");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvExperience(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvExperience(1, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvExperience(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeExperience_Error() throws Exception {
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setDesignation("Lawyer");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvExperience(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvExperience(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvExperience(1, "ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvisorByEmailId_Success() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setEmailId("aaa@fo.com");
		adv1.setName("Dobby");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchAdvisorByEmailId("aaa@fo.com", deleteflag, encryptPass)).thenReturn(adv1);
		Advisor adv = advisorServiceImpl.fetchAdvisorByEmailId("aaa@fo.com");
		Assert.assertEquals("aaa@fo.com", adv.getEmailId());
		verify(advisorDao, times(1)).fetchAdvisorByEmailId("aaa@fo.com", deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvisorByEmailId_Error() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setEmailId("aaa@fo.com");
		adv1.setName("Dobby");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchAdvisorByEmailId("aaa@fo.com", deleteflag, encryptPass)).thenReturn(null);
		Advisor adv = advisorServiceImpl.fetchAdvisorByEmailId("aaa@fo.com");
		Assert.assertEquals(null, adv);
		verify(advisorDao, times(1)).fetchAdvisorByEmailId("aaa@fo.com", deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_generateId() throws Exception {
	// when(advisorDao.generateId()).thenReturn("ADV000000000A");
	// String id = advisorServiceImpl.generateId();
	// Assert.assertEquals("ADV000000000A", id);
	// verify(advisorDao, times(1)).generateId();
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_addAdvPersonalInfo_Success() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setPincode("654123");
		adv1.setGender("f");
		String encryptPass = advTableFields.getEncryption_password();
		String deleteflag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");
		long partyId = 1;
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvPersonalInfo("ADV000000000A", adv1, encryptPass)).thenReturn(1);
		when(advisorDao.updateUsernameInParty("ADV000000000A", adv1, "ADV000000000A", encryptPass)).thenReturn(1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",deleteflag)).thenReturn(partyId);
		int result = advisorServiceImpl.addAdvPersonalInfo("ADV000000000A", adv1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvPersonalInfo("ADV000000000A", adv1, encryptPass);
		verify(advisorDao, times(1)).updateUsernameInParty("ADV000000000A", adv1, "ADV000000000A", encryptPass);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvPersonalInfo_Error() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setPincode("654123");
		adv1.setGender("f");
		String encryptPass = advTableFields.getEncryption_password();
		String deleteflag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");
		long partyId = 1;
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvPersonalInfo("ADV000000000A", adv1, encryptPass)).thenReturn(0);
		when(advisorDao.updateUsernameInParty("ADV000000000A", adv1, "ADV000000000A", encryptPass)).thenReturn(1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",deleteflag)).thenReturn(partyId);
		int result = advisorServiceImpl.addAdvPersonalInfo("ADV000000000A", adv1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvPersonalInfo("ADV000000000A", adv1, encryptPass);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_checkForPasswordMatch() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// Advisor adv = new Advisor();
	// adv.setPassword("!@AS12as");
	// Party party = new Party();
	// party.setPassword("!@AS12as");
	// when(advisorDao.fetchPartyByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(party);
	// when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
	// boolean status = advisorServiceImpl.checkForPasswordMatch("ADV000000000A",
	// "!@AS12as");
	// Assert.assertEquals(true, status);
	// }
	//
	// @Test //Encruyption
	// public void test_changeAdvPassword() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_Y();
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	//
	// when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	// when(advisorDao.changeAdvPassword("ADV000000000A",
	// encryptPass)).thenReturn(1);
	// when(advisorDao.changePartyPassword("ADV000000000A",
	// encryptPass)).thenReturn(1);
	//
	// int result = advisorServiceImpl.changeAdvPassword("ADV000000000A",
	// "AS!@as12");
	// Assert.assertEquals(1, result);
	// verify(advisorDao, times(1)).fetchEncryptionSecretKey();
	// verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).changeAdvPassword("ADV000000000A", encryptPass);
	// verify(advisorDao, times(1)).changePartyPassword("ADV000000000A",
	// encryptPass);
	// }

	@Test
	public void test_fetchProduct_Success() throws Exception {
		AdvProduct product = new AdvProduct();
		product.setServiceId("1");
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvProduct(1, deleteflag)).thenReturn(product);
		AdvProduct prod = advisorServiceImpl.fetchAdvProduct(1);
		Assert.assertEquals("1", prod.getServiceId());
		verify(advisorDao, times(1)).fetchAdvProduct(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchProduct_Error() throws Exception {
		AdvProduct product = new AdvProduct();
		product.setServiceId("1");
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvProduct(1, deleteflag)).thenReturn(null);
		AdvProduct prod = advisorServiceImpl.fetchAdvProduct(1);
		Assert.assertEquals(null, prod);
		verify(advisorDao, times(1)).fetchAdvProduct(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorProduct_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		AdvProduct product = new AdvProduct();
		product.setAdvId("ADV000000000A");
		product.setAdvProdId(1);
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchAdvProduct(1, deleteflag)).thenReturn(product);
		when(advisorDao.modifyAdvisorProduct(product, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.modifyAdvisorProduct(product, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvProduct(1, deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorProduct(product, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorProduct_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		AdvProduct product = new AdvProduct();
		product.setAdvId("ADV000000000A");
		product.setAdvProdId(1);
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchAdvProduct(1, deleteflag)).thenReturn(product);
		when(advisorDao.modifyAdvisorProduct(product, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.modifyAdvisorProduct(product, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvProduct(1, deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorProduct(product, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_encrypt_decrypt() throws Exception {
		when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
		String encryptedText = advisorServiceImpl.encrypt("!@AS12as");
		String decryptedText = advisorServiceImpl.decrypt(encryptedText);
		Assert.assertEquals("!@AS12as", decryptedText);
		verify(advisorDao, times(2)).fetchEncryptionSecretKey();
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test // Encryption Error
	// public void test_encrypt() throws Exception {
	// when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
	//// when(advisorDao.encrypt("!@AS12as")).thenReturn("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=");
	// String encryptedText = advisorServiceImpl.encrypt("!@AS12as");
	// Assert.assertEquals("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=",
	// encryptedText);
	//
	//// verify(advisorDao, times(1)).encrypt("!@AS12as");
	// verify(advisorDao, times(1)).fetchEncryptionSecretKey();
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test // Encryption Error
	// public void test_decrypt() throws Exception {
	// when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
	//// when(advisorDao.decrypt("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=")).thenReturn("!@AS12as");
	// String encryptedText =
	// advisorServiceImpl.decrypt("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=");
	// Assert.assertEquals("!@AS12as", encryptedText);
	//// verify(advisorDao,
	// times(1)).decrypt("i2v0dQxostge0Yh7BQK1GK8LpGV37/MdWiT0jZCBFhg=");
	// verify(advisorDao, times(1)).fetchEncryptionSecretKey();
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_fetchCategoryList_Success() throws Exception {
		List<Category> category = new ArrayList<Category>();
		Category category1 = new Category();
		category1.setCategoryTypeId(1);
		category1.setDesc("LIC");
		Category category2 = new Category();
		category2.setCategoryTypeId(1);
		category2.setDesc("general");
		category.add(category1);
		category.add(category2);
		when(advisorDao.fetchCategoryList()).thenReturn(category);
		List<Category> categoryList = advisorServiceImpl.fetchCategoryList();
		Assert.assertEquals(2, categoryList.size());
		verify(advisorDao, times(1)).fetchCategoryList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchCategoryList_Error() throws Exception {
		List<Category> category = new ArrayList<Category>();
		Category category1 = new Category();
		category1.setCategoryTypeId(1);
		category1.setDesc("LIC");
		Category category2 = new Category();
		category2.setCategoryTypeId(1);
		category2.setDesc("general");
		category.add(category1);
		category.add(category2);
		when(advisorDao.fetchCategoryList()).thenReturn(null);
		List<Category> categoryList = advisorServiceImpl.fetchCategoryList();
		Assert.assertEquals(null, categoryList);
		verify(advisorDao, times(1)).fetchCategoryList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchCategoryTypeList_Success() throws Exception {
		List<CategoryType> categorytype = new ArrayList<CategoryType>();
		CategoryType categorytype1 = new CategoryType();
		categorytype1.setCategoryTypeId(1);
		categorytype1.setDesc("investment");
		CategoryType categorytype2 = new CategoryType();
		categorytype2.setCategoryTypeId(1);
		categorytype2.setDesc("accounting");
		categorytype.add(categorytype1);
		categorytype.add(categorytype2);
		when(advisorDao.fetchCategoryTypeList()).thenReturn(categorytype);
		List<CategoryType> categoryType = advisorServiceImpl.fetchCategoryTypeList();
		Assert.assertEquals(2, categoryType.size());
		verify(advisorDao, times(1)).fetchCategoryTypeList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchCategoryTypeList_Error() throws Exception {
		List<CategoryType> categorytype = new ArrayList<CategoryType>();
		CategoryType categorytype1 = new CategoryType();
		categorytype1.setCategoryTypeId(1);
		categorytype1.setDesc("investment");
		CategoryType categorytype2 = new CategoryType();
		categorytype2.setCategoryTypeId(1);
		categorytype2.setDesc("accounting");
		categorytype.add(categorytype1);
		categorytype.add(categorytype2);
		when(advisorDao.fetchCategoryTypeList()).thenReturn(null);
		List<CategoryType> categoryType = advisorServiceImpl.fetchCategoryTypeList();
		Assert.assertEquals(null, categoryType);
		verify(advisorDao, times(1)).fetchCategoryTypeList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchForumCategoryList_Success() throws Exception {
		List<ForumCategory> forumCategorys = new ArrayList<ForumCategory>();
		ForumCategory forumCategory1 = new ForumCategory();
		forumCategory1.setForumCategoryId(1);
		forumCategory1.setName("mutual funds");
		ForumCategory forumCategory2 = new ForumCategory();
		forumCategory2.setForumCategoryId(2);
		forumCategory2.setName("stock");
		forumCategorys.add(forumCategory1);
		forumCategorys.add(forumCategory2);
		when(advisorDao.fetchForumCategoryList()).thenReturn(forumCategorys);
		List<ForumCategory> forumCategory = advisorServiceImpl.fetchForumCategoryList();
		Assert.assertEquals(2, forumCategory.size());
		verify(advisorDao, times(1)).fetchForumCategoryList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchForumCategoryList_Error() throws Exception {
		List<ForumCategory> forumCategorys = new ArrayList<ForumCategory>();
		ForumCategory forumCategory1 = new ForumCategory();
		forumCategory1.setForumCategoryId(1);
		forumCategory1.setName("mutual funds");
		ForumCategory forumCategory2 = new ForumCategory();
		forumCategory2.setForumCategoryId(2);
		forumCategory2.setName("stock");
		forumCategorys.add(forumCategory1);
		forumCategorys.add(forumCategory2);
		when(advisorDao.fetchForumCategoryList()).thenReturn(null);
		List<ForumCategory> forumCategory = advisorServiceImpl.fetchForumCategoryList();
		Assert.assertEquals(null, forumCategory);
		verify(advisorDao, times(1)).fetchForumCategoryList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRiskQuestionaireList_Success() throws Exception {
		List<RiskQuestionaire> riskQuestionaires = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setQuestionId("1");
		riskQuestionaire.setQuestion("RiskQuestionaire");
		riskQuestionaires.add(riskQuestionaire);
		when(advisorDao.fetchAllRiskQuestionaire()).thenReturn(riskQuestionaires);
		List<RiskQuestionaire> risk = advisorServiceImpl.fetchRiskQuestionaireList();
		Assert.assertEquals(1, risk.size());
		verify(advisorDao, times(1)).fetchAllRiskQuestionaire();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRiskQuestionaireList_Error() throws Exception {
		List<RiskQuestionaire> riskQuestionaires = new ArrayList<RiskQuestionaire>();
		RiskQuestionaire riskQuestionaire = new RiskQuestionaire();
		riskQuestionaire.setQuestionId("1");
		riskQuestionaire.setQuestion("RiskQuestionaire");
		riskQuestionaires.add(riskQuestionaire);
		when(advisorDao.fetchAllRiskQuestionaire()).thenReturn(null);
		List<RiskQuestionaire> risk = advisorServiceImpl.fetchRiskQuestionaireList();
		Assert.assertEquals(null, risk);
		verify(advisorDao, times(1)).fetchAllRiskQuestionaire();
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_fetchProductList_Success() throws Exception { //Error
	// List<Product> productList = new ArrayList<Product>();
	// List<Service> serviceList = new ArrayList<Service>();
	// Service service = new Service();
	// service.setProdId(1);
	// service.setService("Goal Planning");
	// service.setServiceId(1);
	// Product product = new Product();
	// product.setProdId(1);
	// product.setProduct("Product");
	// product.setServices(serviceList);
	// productList.add(product);
	// List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
	// ServicePlan servicePlan = new ServicePlan();
	// servicePlan.setServiceId(1);
	// servicePlan.setServicePlan("Travel Plan");
	// servicePlan.setServicePlanId(2);
	// servicePlanList.add(servicePlan);
	// service.setServicePlans(servicePlanList);
	//
	// when(advisorDao.fetchProductList()).thenReturn(productList);
	// when(advisorDao.fetchServicePlanByServiceId(1)).thenReturn(servicePlanList);
	// List<Product> products = advisorServiceImpl.fetchProductList();
	// Assert.assertEquals(1, products.size());
	// verify(advisorDao, times(1)).fetchProductList();
	// verify(advisorDao, times(1)).fetchServicePlanByServiceId(1L);
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchProductList_Error() throws Exception { //Error
	// List<Product> productList = new ArrayList<Product>();
	// Product product = new Product();
	// product.setProdId(1);
	// product.setProduct("Product");
	// productList.add(product);
	// List<ServicePlan> servicePlanList = new ArrayList<ServicePlan>();
	// ServicePlan servicePlan = new ServicePlan();
	// servicePlan.setServiceId(1);
	// servicePlan.setServicePlan("Travel Plan");
	// servicePlan.setServicePlanId(2);
	// servicePlanList.add(servicePlan);
	// when(advisorDao.fetchProductList()).thenReturn(null);
	// when(advisorDao.fetchServicePlanByServiceId(1)).thenReturn(servicePlanList);
	// List<Product> products = advisorServiceImpl.fetchProductList();
	// Assert.assertEquals(null, products);
	// verify(advisorDao, times(1)).fetchProductList();
	// verify(advisorDao, times(1)).fetchServicePlanByServiceId(1L);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_fetchRoleList_Success() throws Exception {
		List<RoleAuth> roleList = new ArrayList<RoleAuth>();
		RoleAuth role = new RoleAuth();
		role.setId(1);
		role.setName("advisor");
		roleList.add(role);
		RoleAuth role2 = new RoleAuth();
		role2.setId(2);
		role2.setName("investor");
		roleList.add(role2);
		RoleAuth role3 = new RoleAuth();
		role3.setId(3);
		role3.setName("admin");
		roleList.add(role3);
		when(authDao.fetchRoleList()).thenReturn(roleList);
		List<RoleAuth> roles = advisorServiceImpl.fetchRoleList();
		Assert.assertEquals(3, roles.size());
		verify(authDao, times(1)).fetchRoleList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRoleList_Error() throws Exception {
		List<RoleAuth> roleList = new ArrayList<RoleAuth>();
		RoleAuth role = new RoleAuth();
		role.setId(1);
		role.setName("advisor");
		roleList.add(role);
		RoleAuth role2 = new RoleAuth();
		role2.setId(2);
		role2.setName("investor");
		roleList.add(role2);
		RoleAuth role3 = new RoleAuth();
		role3.setId(3);
		role3.setName("admin");
		roleList.add(role3);
		when(authDao.fetchRoleList()).thenReturn(null);
		List<RoleAuth> roles = advisorServiceImpl.fetchRoleList();
		Assert.assertEquals(null, roles);
		verify(authDao, times(1)).fetchRoleList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchForumSubCategoryList_Success() throws Exception {
		List<ForumSubCategory> forumSubCategoryList = new ArrayList<ForumSubCategory>();
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumCategoryId(1);
		forumSubCategory.setName("Forum Sub Category");
		forumSubCategoryList.add(forumSubCategory);
		when(advisorDao.fetchForumSubCategoryList()).thenReturn(forumSubCategoryList);
		List<ForumSubCategory> forumsub = advisorServiceImpl.fetchForumSubCategoryList();
		Assert.assertEquals(1, forumsub.size());
		verify(advisorDao, times(1)).fetchForumSubCategoryList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchForumSubCategoryList_Error() throws Exception {
		List<ForumSubCategory> forumSubCategoryList = new ArrayList<ForumSubCategory>();
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumCategoryId(1);
		forumSubCategory.setName("Forum Sub Category");
		forumSubCategoryList.add(forumSubCategory);
		when(advisorDao.fetchForumSubCategoryList()).thenReturn(null);
		List<ForumSubCategory> forumsub = advisorServiceImpl.fetchForumSubCategoryList();
		Assert.assertEquals(null, forumsub);
		verify(advisorDao, times(1)).fetchForumSubCategoryList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchForumStatusList_Success() throws Exception {
		List<ForumStatus> forumStatusList = new ArrayList<ForumStatus>();
		ForumStatus forumStatus = new ForumStatus();
		forumStatus.setId(1);
		forumStatus.setDesc("Forum Status");
		forumStatusList.add(forumStatus);
		when(advisorDao.fetchForumStatusList()).thenReturn(forumStatusList);
		List<ForumStatus> forum = advisorServiceImpl.fetchForumStatusList();
		Assert.assertEquals(1, forum.size());
		verify(advisorDao, times(1)).fetchForumStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchForumStatusList_Error() throws Exception {
		List<ForumStatus> forumStatusList = new ArrayList<ForumStatus>();
		ForumStatus forumStatus = new ForumStatus();
		forumStatus.setId(1);
		forumStatus.setDesc("Forum Status");
		forumStatusList.add(forumStatus);
		when(advisorDao.fetchForumStatusList()).thenReturn(null);
		List<ForumStatus> forum = advisorServiceImpl.fetchForumStatusList();
		Assert.assertEquals(null, forum);
		verify(advisorDao, times(1)).fetchForumStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyStatusList_Success() throws Exception {
		List<PartyStatus> partyStatusList = new ArrayList<PartyStatus>();
		PartyStatus partyStatus = new PartyStatus();
		partyStatus.setId(1);
		partyStatus.setDesc("Party Status");
		partyStatusList.add(partyStatus);
		when(advisorDao.fetchPartyStatusList()).thenReturn(partyStatusList);
		List<PartyStatus> party = advisorServiceImpl.fetchPartyStatusList();
		Assert.assertEquals(1, party.size());
		verify(advisorDao, times(1)).fetchPartyStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyStatusList_Error() throws Exception {
		List<PartyStatus> partyStatusList = new ArrayList<PartyStatus>();
		PartyStatus partyStatus = new PartyStatus();
		partyStatus.setId(1);
		partyStatus.setDesc("Party Status");
		partyStatusList.add(partyStatus);
		when(advisorDao.fetchPartyStatusList()).thenReturn(null);
		List<PartyStatus> party = advisorServiceImpl.fetchPartyStatusList();
		Assert.assertEquals(null, party);
		verify(advisorDao, times(1)).fetchPartyStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchServiceList_Success() throws Exception {
		List<Service> services = new ArrayList<Service>();
		Service service1 = new Service();
		service1.setServiceId(1);
		service1.setProdId(2);
		Service service2 = new Service();
		service2.setProdId(1);
		service2.setServiceId(2);
		services.add(service1);
		services.add(service2);
		when(advisorDao.fetchServiceList()).thenReturn(services);
		List<Service> returnServices = advisorServiceImpl.fetchServiceList();
		Assert.assertEquals(2, returnServices.size());
		verify(advisorDao, times(1)).fetchServiceList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchServiceList_Error() throws Exception {
		List<Service> services = new ArrayList<Service>();
		Service service1 = new Service();
		service1.setServiceId(1);
		service1.setProdId(2);
		Service service2 = new Service();
		service2.setProdId(1);
		service2.setServiceId(2);
		services.add(service1);
		services.add(service2);
		when(advisorDao.fetchServiceList()).thenReturn(null);
		List<Service> returnServices = advisorServiceImpl.fetchServiceList();
		Assert.assertEquals(null, returnServices);
		verify(advisorDao, times(1)).fetchServiceList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchBrandList_Success() throws Exception {
		List<Brand> brands = new ArrayList<Brand>();
		Brand brand1 = new Brand();
		brand1.setBrandId(1);
		brand1.setProdId(2);
		Brand brand2 = new Brand();
		brand2.setProdId(1);
		brand2.setBrandId(2);
		brands.add(brand1);
		brands.add(brand2);
		when(advisorDao.fetchBrandList()).thenReturn(brands);
		List<Brand> returnBrands = advisorServiceImpl.fetchBrandList();
		Assert.assertEquals(2, returnBrands.size());
		verify(advisorDao, times(1)).fetchBrandList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchBrandList_Error() throws Exception {
		List<Brand> brands = new ArrayList<Brand>();
		Brand brand1 = new Brand();
		brand1.setBrandId(1);
		brand1.setProdId(2);
		Brand brand2 = new Brand();
		brand2.setProdId(1);
		brand2.setBrandId(2);
		brands.add(brand1);
		brands.add(brand2);
		when(advisorDao.fetchBrandList()).thenReturn(null);
		List<Brand> returnBrands = advisorServiceImpl.fetchBrandList();
		Assert.assertEquals(null, returnBrands);
		verify(advisorDao, times(1)).fetchBrandList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchLicenseList_Success() throws Exception {
		List<License> licenses = new ArrayList<License>();
		License lic1 = new License();
		lic1.setLicId(1);
		lic1.setProdId(2);
		License lic2 = new License();
		lic2.setProdId(1);
		lic2.setLicId(2);
		licenses.add(lic1);
		licenses.add(lic2);
		when(advisorDao.fetchLicenseList()).thenReturn(licenses);
		List<License> returnLicenses = advisorServiceImpl.fetchLicenseList();
		Assert.assertEquals(2, returnLicenses.size());
		verify(advisorDao, times(1)).fetchLicenseList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchLicenseList_Error() throws Exception {
		List<License> licenses = new ArrayList<License>();
		License lic1 = new License();
		lic1.setLicId(1);
		lic1.setProdId(2);
		License lic2 = new License();
		lic2.setProdId(1);
		lic2.setLicId(2);
		licenses.add(lic1);
		licenses.add(lic2);
		when(advisorDao.fetchLicenseList()).thenReturn(null);
		List<License> returnLicenses = advisorServiceImpl.fetchLicenseList();
		Assert.assertEquals(null, returnLicenses);
		verify(advisorDao, times(1)).fetchLicenseList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRemunerationList_Success() throws Exception {
		List<Remuneration> remunerations = new ArrayList<Remuneration>();
		Remuneration rem1 = new Remuneration();
		rem1.setRemId(1);
		rem1.setRemuneration("Fee");
		Remuneration rem2 = new Remuneration();
		rem2.setRemId(2);
		rem2.setRemuneration("Comission");
		remunerations.add(rem1);
		remunerations.add(rem2);
		when(advisorDao.fetchRemunerationList()).thenReturn(remunerations);
		List<Remuneration> returnRemunerations = advisorServiceImpl.fetchRemunerationList();
		Assert.assertEquals(2, returnRemunerations.size());
		verify(advisorDao, times(1)).fetchRemunerationList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRemunerationList_Error() throws Exception {
		List<Remuneration> remunerations = new ArrayList<Remuneration>();
		Remuneration rem1 = new Remuneration();
		rem1.setRemId(1);
		rem1.setRemuneration("Fee");
		Remuneration rem2 = new Remuneration();
		rem2.setRemId(2);
		rem2.setRemuneration("Comission");
		remunerations.add(rem1);
		remunerations.add(rem2);
		when(advisorDao.fetchRemunerationList()).thenReturn(null);
		List<Remuneration> returnRemunerations = advisorServiceImpl.fetchRemunerationList();
		Assert.assertEquals(null, returnRemunerations);
		verify(advisorDao, times(1)).fetchRemunerationList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvBrandInfo_Success() throws Exception {
		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		AdvBrandInfo advBrandInfo = new AdvBrandInfo();
		advBrandInfo.setAdvId("ADV000000000A");
		advBrandInfo.setBrandId(1);
		advBrandInfo.setProdId(2);
		advBrandInfoList.add(advBrandInfo);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvBrandInfo("ADV000000000A", advBrandInfo, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvBrandInfo("ADV000000000A", advBrandInfoList);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvBrandInfo("ADV000000000A", advBrandInfo, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvBrandInfo_Error() throws Exception {
		List<AdvBrandInfo> advBrandInfoList = new ArrayList<>();
		AdvBrandInfo advBrandInfo = new AdvBrandInfo();
		advBrandInfo.setAdvId("ADV000000000A");
		advBrandInfo.setBrandId(1);
		advBrandInfo.setProdId(2);
		advBrandInfoList.add(advBrandInfo);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvBrandInfo("ADV000000000A", advBrandInfo, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addAdvBrandInfo("ADV000000000A", advBrandInfoList);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvBrandInfo("ADV000000000A", advBrandInfo, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandInfoByAdvIdAndProdId_Success() throws Exception {
		List<AdvBrandInfo> advBrandInfos = new ArrayList<AdvBrandInfo>();
		AdvBrandInfo brandInfo1 = new AdvBrandInfo();
		brandInfo1.setAdvId("ADV000000000A");
		brandInfo1.setBrandId(1);
		AdvBrandInfo brandInfo2 = new AdvBrandInfo();
		brandInfo2.setAdvId("ADV000000000A");
		brandInfo2.setBrandId(2);
		advBrandInfos.add(brandInfo1);
		advBrandInfos.add(brandInfo2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A", 1, deleteflag)).thenReturn(advBrandInfos);
		List<AdvBrandInfo> returnAdvBrandInfos = advisorServiceImpl.fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A",
				1);
		Assert.assertEquals(2, returnAdvBrandInfos.size());
		verify(advisorDao, times(1)).fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A", 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandInfoByAdvIdAndProdId_Error() throws Exception {
		List<AdvBrandInfo> advBrandInfos = new ArrayList<AdvBrandInfo>();
		AdvBrandInfo brandInfo1 = new AdvBrandInfo();
		brandInfo1.setAdvId("ADV000000000A");
		brandInfo1.setBrandId(1);
		AdvBrandInfo brandInfo2 = new AdvBrandInfo();
		brandInfo2.setAdvId("ADV000000000A");
		brandInfo2.setBrandId(2);
		advBrandInfos.add(brandInfo1);
		advBrandInfos.add(brandInfo2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A", 1, deleteflag)).thenReturn(null);
		List<AdvBrandInfo> returnAdvBrandInfos = advisorServiceImpl.fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A",
				1);
		Assert.assertEquals(null, returnAdvBrandInfos);
		verify(advisorDao, times(1)).fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A", 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPriorityByBrandIdAndAdvId_Success() throws Exception {
		List<Long> lists = new ArrayList<Long>();
		lists.add(1L);
		lists.add(2L);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1, deleteflag)).thenReturn(lists);
		List<Long> returnLists = advisorServiceImpl.fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1);
		Assert.assertEquals(2, returnLists.size());
		verify(advisorDao, times(1)).fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPriorityByBrandIdAndAdvId_Error() throws Exception {
		List<Long> lists = new ArrayList<Long>();
		lists.add(1L);
		lists.add(2L);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1, deleteflag)).thenReturn(null);
		List<Long> returnLists = advisorServiceImpl.fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1);
		Assert.assertEquals(null, returnLists);
		verify(advisorDao, times(1)).fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandRank_Success() throws Exception {
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setBrandId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandRank("ADV000000000A", 1, 1, deleteflag)).thenReturn(advBrandRank);
		AdvBrandRank brandRank = advisorServiceImpl.fetchAdvBrandRank("ADV000000000A", 1, 1);
		Assert.assertEquals(1, brandRank.getBrandId());
		verify(advisorDao, times(1)).fetchAdvBrandRank("ADV000000000A", 1, 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandRank_Error() throws Exception {
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setBrandId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandRank("ADV000000000A", 1, 1, deleteflag)).thenReturn(null);
		AdvBrandRank brandRank = advisorServiceImpl.fetchAdvBrandRank("ADV000000000A", 1, 1);
		Assert.assertEquals(null, brandRank);
		verify(advisorDao, times(1)).fetchAdvBrandRank("ADV000000000A", 1, 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvBrandAndRank_Success() throws Exception {
		AdvBrandRank advBrandAndRank = new AdvBrandRank();
		advBrandAndRank.setAdvId("ADV000000000A");
		advBrandAndRank.setBrandId(1);
		advBrandAndRank.setProdId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.addAdvBrandAndRank(1, 2, "ADV000000000A", 1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvBrandAndRank_Error() throws Exception {
		AdvBrandRank advBrandAndRank = new AdvBrandRank();
		advBrandAndRank.setAdvId("ADV000000000A");
		advBrandAndRank.setBrandId(1);
		advBrandAndRank.setProdId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.addAdvBrandAndRank(1, 2, "ADV000000000A", 1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).addAdvBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_updateBrandAndRank_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.updateBrandAndRank(1, 2, "ADV000000000A", 1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_updateBrandAndRank_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.updateBrandAndRank(1, 2, "ADV000000000A", 1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvProductByAdvId_Success() throws Exception {
		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct advProduct1 = new AdvProduct();
		advProduct1.setAdvId("ADV000000000A");
		advProduct1.setAdvProdId(1);
		AdvProduct advProduct2 = new AdvProduct();
		advProduct2.setAdvId("ADV000000000A");
		advProduct2.setAdvProdId(2);
		advProducts.add(advProduct1);
		advProducts.add(advProduct2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvProductByAdvId("ADV000000000A", deleteflag)).thenReturn(advProducts);
		List<AdvProduct> returnAdvProducts = advisorServiceImpl.fetchAdvProductByAdvId("ADV000000000A");
		Assert.assertEquals(2, returnAdvProducts.size());
		verify(advisorDao, times(1)).fetchAdvProductByAdvId("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvProductByAdvId_Error() throws Exception {
		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
		AdvProduct advProduct1 = new AdvProduct();
		advProduct1.setAdvId("ADV000000000A");
		advProduct1.setAdvProdId(1);
		AdvProduct advProduct2 = new AdvProduct();
		advProduct2.setAdvId("ADV000000000A");
		advProduct2.setAdvProdId(2);
		advProducts.add(advProduct1);
		advProducts.add(advProduct2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvProductByAdvId("ADV000000000A", deleteflag)).thenReturn(null);
		List<AdvProduct> returnAdvProducts = advisorServiceImpl.fetchAdvProductByAdvId("ADV000000000A");
		Assert.assertEquals(null, returnAdvProducts);
		verify(advisorDao, times(1)).fetchAdvProductByAdvId("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAdvProduct_Success() throws Exception {
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setLicId(1);
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvProduct(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvProduct(1, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvProduct(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_removeAdvProduct_Error() throws Exception {
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setLicId(1);
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvProduct(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvProduct(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvProduct(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_removeAdvBrandInfo_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvBrandInfo(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvBrandInfo(1, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvBrandInfo(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_removeAdvBrandInfo_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvBrandInfo(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvBrandInfo(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvBrandInfo(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_removeFromBrandRank_Success() throws Exception {
		when(advisorDao.removeFromBrandRank("ADV000000000A", 1)).thenReturn(1);
		int result = advisorServiceImpl.removeFromBrandRank("ADV000000000A", 1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).removeFromBrandRank("ADV000000000A", 1);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeFromBrandRank_Error() throws Exception {
		when(advisorDao.removeFromBrandRank("ADV000000000A", 1)).thenReturn(0);
		int result = advisorServiceImpl.removeFromBrandRank("ADV000000000A", 1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).removeFromBrandRank("ADV000000000A", 1);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvProductByAdvIdAndAdvProdId_Success() throws Exception {
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setProdId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1, deleteflag)).thenReturn(advProduct);
		AdvProduct product = advisorServiceImpl.fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1);
		Assert.assertEquals(1, product.getProdId());
		verify(advisorDao, times(1)).fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvProductByAdvIdAndAdvProdId_Error() throws Exception {
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProduct.setProdId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1, deleteflag)).thenReturn(null);
		AdvProduct product = advisorServiceImpl.fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1);
		Assert.assertEquals(null, product);
		verify(advisorDao, times(1)).fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAdvBrandInfoByAdvId_Success() throws Exception {
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvBrandInfoByAdvId("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvBrandInfoByAdvId("ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAdvBrandInfoByAdvId_Error() throws Exception {
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvBrandInfoByAdvId("ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvBrandInfoByAdvId("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvBrandInfoByAdvId("ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAdvBrandRankByAdvId_Success() throws Exception {
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAdvBrandRankByAdvId("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvBrandRankByAdvId("ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAdvBrandRankByAdvId_Error() throws Exception {
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvBrandRankByAdvId("ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAdvBrandRankByAdvId("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAdvBrandRankByAdvId("ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_fetchAllServiceAndBrand_Success() throws Exception { //Error
	// List<Product> products = new ArrayList<Product>();
	// Product product1 = new Product();
	// product1.setProdId(1);
	// product1.setProduct("Fixed Income");
	// Product product2 = new Product();
	// product2.setProdId(2);
	// product2.setProduct("Mutual Funds");
	// products.add(product1);
	// products.add(product2);
	// when(advisorDao.fetchAllServiceAndBrand()).thenReturn(products);
	// List<Product> returnProducts = advisorServiceImpl.fetchAllServiceAndBrand();
	// Assert.assertEquals(2, returnProducts.size());
	// verify(advisorDao, times(1)).fetchAllServiceAndBrand();
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchAllServiceAndBrand_Error() throws Exception { //Error
	// List<Product> products = new ArrayList<Product>();
	// Product product1 = new Product();
	// product1.setProdId(1);
	// product1.setProduct("Fixed Income");
	// Product product2 = new Product();
	// product2.setProdId(2);
	// product2.setProduct("Mutual Funds");
	// products.add(product1);
	// products.add(product2);
	// when(advisorDao.fetchAllServiceAndBrand()).thenReturn(null);
	// List<Product> returnProducts = advisorServiceImpl.fetchAllServiceAndBrand();
	// Assert.assertEquals(null, returnProducts);
	// verify(advisorDao, times(1)).fetchAllServiceAndBrand();
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test //comment in service//
	// public void test_fetchAwardByadvId_Success() throws Exception {
	// List<Award> awards = new ArrayList<Award>();
	// Award awd1 = new Award();
	// awd1.setAdvId("ADV000000000A");
	// awd1.setAwardId(1);
	// Award awd2 = new Award();
	// awd2.setAdvId("ADV000000000B");
	// awd2.setAwardId(2);
	// awards.add(awd1);
	// awards.add(awd2);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// when(advisorDao.fetchAwardByadvId("ADV000000000A",
	// deleteflag)).thenReturn(awards);
	// List<Award> returnAwards =
	// advisorServiceImpl.fetchAwardByadvId("ADV000000000A");
	// Assert.assertEquals(2, returnAwards.size());
	// verify(advisorDao, times(1)).fetchAwardByadvId("ADV000000000A", deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test
	// public void test_fetchAwardByadvId_Error() throws Exception {
	// List<Award> awards = new ArrayList<Award>();
	// Award awd1 = new Award();
	// awd1.setAdvId("ADV000000000A");
	// awd1.setAwardId(1);
	// Award awd2 = new Award();
	// awd2.setAdvId("ADV000000000B");
	// awd2.setAwardId(2);
	// awards.add(awd1);
	// awards.add(awd2);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// when(advisorDao.fetchAwardByadvId("ADV000000000A",
	// deleteflag)).thenReturn(null);
	// List<Award> returnAwards =
	// advisorServiceImpl.fetchAwardByadvId("ADV000000000A");
	// Assert.assertEquals(null, returnAwards);
	// verify(advisorDao, times(1)).fetchAwardByadvId("ADV000000000A", deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	@Test
	public void test_fetchCertificateByadvId_Success() throws Exception {
		List<Certificate> certificates = new ArrayList<Certificate>();
		Certificate cert1 = new Certificate();
		cert1.setAdvId("ADV000000000A");
		cert1.setCertificateId(1);
		Certificate cert2 = new Certificate();
		cert2.setAdvId("ADV000000000B");
		cert2.setCertificateId(2);
		certificates.add(cert1);
		certificates.add(cert2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchCertificateByadvId("ADV000000000A", deleteflag)).thenReturn(certificates);
		List<Certificate> returnCertificates = advisorServiceImpl.fetchCertificateByadvId("ADV000000000A");
		Assert.assertEquals(2, returnCertificates.size());
		verify(advisorDao, times(1)).fetchCertificateByadvId("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchCertificateByadvId_Error() throws Exception {
		List<Certificate> certificates = new ArrayList<Certificate>();
		Certificate cert1 = new Certificate();
		cert1.setAdvId("ADV000000000A");
		cert1.setCertificateId(1);
		Certificate cert2 = new Certificate();
		cert2.setAdvId("ADV000000000B");
		cert2.setCertificateId(2);
		certificates.add(cert1);
		certificates.add(cert2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchCertificateByadvId("ADV000000000A", deleteflag)).thenReturn(null);
		List<Certificate> returnCertificates = advisorServiceImpl.fetchCertificateByadvId("ADV000000000A");
		Assert.assertEquals(null, returnCertificates);
		verify(advisorDao, times(1)).fetchCertificateByadvId("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test //comment in service//
	// public void test_fetchExperienceByadvId_Success() throws Exception {
	// List<Experience> experiences = new ArrayList<Experience>();
	// Experience exp1 = new Experience();
	// exp1.setAdvId("ADV000000000A");
	// exp1.setExpId(1);
	// Experience exp2 = new Experience();
	// exp2.setAdvId("ADV000000000B");
	// exp2.setExpId(2);
	// experiences.add(exp1);
	// experiences.add(exp2);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// when(advisorDao.fetchExperienceByadvId("ADV000000000A",
	// deleteflag)).thenReturn(experiences);
	// List<Experience> returnExperiences =
	// advisorServiceImpl.fetchExperienceByadvId("ADV000000000A");
	// Assert.assertEquals(2, returnExperiences.size());
	// verify(advisorDao, times(1)).fetchExperienceByadvId("ADV000000000A",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test
	// public void test_fetchExperienceByadvId_Error() throws Exception {
	// List<Experience> experiences = new ArrayList<Experience>();
	// Experience exp1 = new Experience();
	// exp1.setAdvId("ADV000000000A");
	// exp1.setExpId(1);
	// Experience exp2 = new Experience();
	// exp2.setAdvId("ADV000000000B");
	// exp2.setExpId(2);
	// experiences.add(exp1);
	// experiences.add(exp2);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// when(advisorDao.fetchExperienceByadvId("ADV000000000A",
	// deleteflag)).thenReturn(null);
	// List<Experience> returnExperiences =
	// advisorServiceImpl.fetchExperienceByadvId("ADV000000000A");
	// Assert.assertEquals(null, returnExperiences);
	// verify(advisorDao, times(1)).fetchExperienceByadvId("ADV000000000A",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test //comment in service//
	// public void test_fetchEducationByadvId_Success() throws Exception {
	// List<Education> educations = new ArrayList<Education>();
	// Education edu1 = new Education();
	// edu1.setAdvId("ADV000000000A");
	// edu1.setEduId(1);
	// Education edu2 = new Education();
	// edu2.setAdvId("ADV000000000B");
	// edu2.setEduId(2);
	// educations.add(edu1);
	// educations.add(edu2);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// when(advisorDao.fetchEducationByadvId("ADV000000000A",
	// deleteflag)).thenReturn(educations);
	// List<Education> returnEducations =
	// advisorServiceImpl.fetchEducationByadvId("ADV000000000A");
	// Assert.assertEquals(2, returnEducations.size());
	// verify(advisorDao, times(1)).fetchEducationByadvId("ADV000000000A",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test
	// public void test_fetchEducationByadvId_Error() throws Exception {
	// List<Education> educations = new ArrayList<Education>();
	// Education edu1 = new Education();
	// edu1.setAdvId("ADV000000000A");
	// edu1.setEduId(1);
	// Education edu2 = new Education();
	// edu2.setAdvId("ADV000000000B");
	// edu2.setEduId(2);
	// educations.add(edu1);
	// educations.add(edu2);
	// String deleteflag = advTableFields.getDelete_flag_N();
	// when(advisorDao.fetchEducationByadvId("ADV000000000A",
	// deleteflag)).thenReturn(null);
	// List<Education> returnEducations =
	// advisorServiceImpl.fetchEducationByadvId("ADV000000000A");
	// Assert.assertEquals(null, returnEducations);
	// verify(advisorDao, times(1)).fetchEducationByadvId("ADV000000000A",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_modifyAdvisorAward_Success() throws Exception {
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setAwardId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag)).thenReturn(award);
		when(advisorDao.modifyAdvisorAward(1, award, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.modifyAdvisorAward(1, award, "ADV000000000A");
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorAward(1, award, "ADV000000000A");
	}

	@Test
	public void test_modifyAdvisorAward_Error() throws Exception {
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setAwardId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag)).thenReturn(award);
		when(advisorDao.modifyAdvisorAward(1, award, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.modifyAdvisorAward(1, award, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorAward(1, award, "ADV000000000A");
	}

	@Test
	public void test_modifyAdvisorCertificate_Success() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setCertificateId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag))
				.thenReturn(certificate);
		when(advisorDao.modifyAdvisorCertificate(1, certificate, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.modifyAdvisorCertificate(1, certificate, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorCertificate(1, certificate, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorCertificate_Error() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setCertificateId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag))
				.thenReturn(certificate);
		when(advisorDao.modifyAdvisorCertificate(1, certificate, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.modifyAdvisorCertificate(1, certificate, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorCertificate(1, certificate, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorExperience_Success() throws Exception {
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setExpId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag)).thenReturn(experience);
		when(advisorDao.modifyAdvisorExperience(1, experience, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.modifyAdvisorExperience(1, experience, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorExperience(1, experience, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorExperience_Error() throws Exception {
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setExpId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag)).thenReturn(experience);
		when(advisorDao.modifyAdvisorExperience(1, experience, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.modifyAdvisorExperience(1, experience, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorExperience(1, experience, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorEducation_Success() throws Exception {
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setEduId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag)).thenReturn(education);
		when(advisorDao.modifyAdvisorEducation(1, education, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.modifyAdvisorEducation(1, education, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorEducation(1, education, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAdvisorEducation_Error() throws Exception {
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setEduId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag)).thenReturn(education);
		when(advisorDao.modifyAdvisorEducation(1, education, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.modifyAdvisorEducation(1, education, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorEducation(1, education, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvAwardInfo_Success() throws Exception {
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setAwardId(1);
		award.setTitle("IFRD");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvAwardInfo("ADV000000000A", award, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvAwardInfo("ADV000000000A", award);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvAwardInfo("ADV000000000A", award, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvAwardInfo_Error() throws Exception {
		Award award = new Award();
		award.setAdvId("ADV000000000A");
		award.setAwardId(1);
		award.setTitle("IFRD");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvAwardInfo("ADV000000000A", award, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addAdvAwardInfo("ADV000000000A", award);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvAwardInfo("ADV000000000A", award, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvCertificateInfo_Success() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setCertificateId(1);
		certificate.setTitle("IFRD");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvCertificateInfo("ADV000000000A", certificate, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvCertificateInfo("ADV000000000A", certificate);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).addAdvCertificateInfo("ADV000000000A", certificate, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvCertificateInfo_Error() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setAdvId("ADV000000000A");
		certificate.setCertificateId(1);
		certificate.setTitle("IFRD");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvCertificateInfo("ADV000000000A", certificate, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addAdvCertificateInfo("ADV000000000A", certificate);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).addAdvCertificateInfo("ADV000000000A", certificate, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvExperienceInfo_Success() throws Exception {
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setExpId(1);
		experience.setLocation("Chennai");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvExperienceInfo("ADV000000000A", experience, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvExperienceInfo("ADV000000000A", experience);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).addAdvExperienceInfo("ADV000000000A", experience, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvExperienceInfo_Error() throws Exception {
		Experience experience = new Experience();
		experience.setAdvId("ADV000000000A");
		experience.setExpId(1);
		experience.setLocation("Chennai");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvExperienceInfo("ADV000000000A", experience, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addAdvExperienceInfo("ADV000000000A", experience);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvExperienceInfo("ADV000000000A", experience, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvEducationInfo_Success() throws Exception {
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setEduId(1);
		education.setFromYear("OCT-2018");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvEducationInfo("ADV000000000A", education, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvEducationInfo("ADV000000000A", education);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).addAdvEducationInfo("ADV000000000A", education, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addAdvEducationInfo_Error() throws Exception {
		Education education = new Education();
		education.setAdvId("ADV000000000A");
		education.setEduId(1);
		education.setFromYear("OCT-2018");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addAdvEducationInfo("ADV000000000A", education, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addAdvEducationInfo("ADV000000000A", education);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).addAdvEducationInfo("ADV000000000A", education, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvAwardByAdvIdAndAwardId_Success() throws Exception {
		Award award = new Award();
		award.setAwardId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag)).thenReturn(award);
		Award awd = advisorServiceImpl.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A");
		Assert.assertEquals(1, awd.getAwardId());
		verify(advisorDao, times(1)).fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvAwardByAdvIdAndAwardId_Error() throws Exception {
		Award award = new Award();
		award.setAwardId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag)).thenReturn(null);
		Award awd = advisorServiceImpl.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A");
		Assert.assertEquals(null, awd);
		verify(advisorDao, times(1)).fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvCertificateByAdvIdAndCertificateId_Success() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setCertificateId(2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag))
				.thenReturn(certificate);
		Certificate cert = advisorServiceImpl.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A");
		Assert.assertEquals(2, cert.getCertificateId());
		verify(advisorDao, times(1)).fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvCertificateByAdvIdAndCertificateId_Error() throws Exception {
		Certificate certificate = new Certificate();
		certificate.setCertificateId(2);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag)).thenReturn(null);
		Certificate cert = advisorServiceImpl.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A");
		Assert.assertEquals(null, cert);
		verify(advisorDao, times(1)).fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvEducationByAdvIdAndEduId_Success() throws Exception {
		Education education = new Education();
		education.setEduId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag)).thenReturn(education);
		Education edu = advisorServiceImpl.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A");
		Assert.assertEquals(1, edu.getEduId());
		verify(advisorDao, times(1)).fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvEducationByAdvIdAndEduId_Error() throws Exception {
		Education education = new Education();
		education.setEduId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag)).thenReturn(null);
		Education edu = advisorServiceImpl.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A");
		Assert.assertEquals(null, edu);
		verify(advisorDao, times(1)).fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvExperienceByAdvIdAndExpId_Success() throws Exception {
		Experience experience = new Experience();
		experience.setExpId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag)).thenReturn(experience);
		Experience exp = advisorServiceImpl.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A");
		Assert.assertEquals(1, exp.getExpId());
		verify(advisorDao, times(1)).fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvExperienceByAdvIdAndExpId_Error() throws Exception {
		Experience experience = new Experience();
		experience.setExpId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag)).thenReturn(null);
		Experience exp = advisorServiceImpl.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A");
		Assert.assertEquals(null, exp);
		verify(advisorDao, times(1)).fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAwardByAdvId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAwardByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeAwardByAdvId("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAwardByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeAwardByAdvId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAwardByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeAwardByAdvId("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeAwardByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeCertificateByAdvId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeCertificateByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeCertificateByAdvId("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeCertificateByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeCertificateByAdvId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeCertificateByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeCertificateByAdvId("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeCertificateByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeExperienceByAdvId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeExperienceByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeExperienceByAdvId("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeExperienceByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeExperienceByAdvId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeExperienceByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeExperienceByAdvId("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);

		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeExperienceByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeEducationByAdvId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeEducationByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeEducationByAdvId("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeEducationByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeEducationByAdvId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeEducationByAdvId("ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeEducationByAdvId("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removeEducationByAdvId("ADV000000000A", deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_fetchAllStateCityPincode_Success() throws Exception {
	// List<State> stateList = new ArrayList<State>();
	// State state1 = new State();
	// state1.setStateId(1);
	// state1.setState("TamilNadu");
	// State state2 = new State();
	// state2.setStateId(2);
	// state2.setState("Kerala");
	// stateList.add(state1);
	// stateList.add(state2);
	// when(advisorDao.fetchAllStateCityPincode()).thenReturn(stateList);
	// List<StateCity> states = advisorServiceImpl.fetchAllStateCityPincode();
	// Assert.assertEquals(2, states.size());
	// verify(advisorDao, times(1)).fetchAllStateCityPincode();
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test
	// public void test_fetchAllStateCityPincode_Error() throws Exception {
	// List<State> stateList = new ArrayList<State>();
	// State state1 = new State();
	// state1.setStateId(1);
	// state1.setState("TamilNadu");
	// State state2 = new State();
	// state2.setStateId(2);
	// state2.setState("Kerala");
	// stateList.add(state1);
	// stateList.add(state2);
	// when(advisorDao.fetchAllStateCityPincode()).thenReturn(null);
	// List<StateCity> states = advisorServiceImpl.fetchAllStateCityPincode();
	// Assert.assertEquals(null, states);
	// verify(advisorDao, times(1)).fetchAllStateCityPincode();
	// verifyNoMoreInteractions(advisorDao);
	//
	// }

	@Test
	public void test_fetchAdvBrandRankByAdvId_Success() throws Exception {
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank brandRank1 = new AdvBrandRank();
		brandRank1.setAdvId("ADV000000000A");
		brandRank1.setBrandId(1);
		AdvBrandRank brandRank2 = new AdvBrandRank();
		brandRank2.setAdvId("ADV000000000A");
		brandRank2.setBrandId(2);
		advBrandRankList.add(brandRank1);
		advBrandRankList.add(brandRank2);

		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandRankByAdvId("ADV000000000A", deleteflag)).thenReturn(advBrandRankList);
		when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
		List<AdvBrandRank> advBrandRank = advisorServiceImpl.fetchAdvBrandRankByAdvId("ADV000000000A");
		Assert.assertEquals(2, advBrandRank.size());
		verify(advisorDao, times(1)).fetchAdvBrandRankByAdvId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).fetchBrandByBrandId(1);
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandRankByAdvId_Error() throws Exception {
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		// AdvBrandRank brandRank1 = new AdvBrandRank();
		// brandRank1.setAdvId("ADV000000000A");
		// brandRank1.setBrandId(1);
		// AdvBrandRank brandRank2 = new AdvBrandRank();
		// brandRank2.setAdvId("ADV000000000A");
		// brandRank2.setBrandId(2);
		// advBrandRankList.add(brandRank1);
		// advBrandRankList.add(brandRank2);

		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandRankByAdvId("ADV000000000A", deleteflag)).thenReturn(advBrandRankList);
		// when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
		List<AdvBrandRank> advBrandRank = advisorServiceImpl.fetchAdvBrandRankByAdvId("ADV000000000A");
		Assert.assertEquals(0, advBrandRank.size());
		verify(advisorDao, times(1)).fetchAdvBrandRankByAdvId("ADV000000000A", deleteflag);
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyIdByRoleBasedId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		long result = advisorServiceImpl.fetchPartyIdByRoleBasedId("ADV000000000A");
		Assert.assertEquals(1L, result);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyIdByRoleBasedId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(0L);
		long result = advisorServiceImpl.fetchPartyIdByRoleBasedId("ADV000000000A");
		Assert.assertEquals(0L, result);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandInfoByAdvId_Success() throws Exception {
		List<AdvBrandInfo> advBrandInfoList = new ArrayList<AdvBrandInfo>();
		AdvBrandInfo advBrandInfo1 = new AdvBrandInfo();
		advBrandInfo1.setAdvId("ADV000000000A");
		advBrandInfo1.setBrandId(1);
		AdvBrandInfo advBrandInfo2 = new AdvBrandInfo();
		advBrandInfo2.setAdvId("ADV000000000A");
		advBrandInfo2.setBrandId(2);
		advBrandInfoList.add(advBrandInfo1);
		advBrandInfoList.add(advBrandInfo2);

		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandInfoByAdvId("ADV000000000A", deleteflag)).thenReturn(advBrandInfoList);
		List<AdvBrandInfo> advBrandInfo = advisorServiceImpl.fetchAdvBrandInfoByAdvId("ADV000000000A");
		Assert.assertEquals(2, advBrandInfo.size());
		verify(advisorDao, times(1)).fetchAdvBrandInfoByAdvId("ADV000000000A", deleteflag);
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvBrandInfoByAdvId_Error() throws Exception {
		List<AdvBrandInfo> advBrandInfoList = new ArrayList<AdvBrandInfo>();
		// AdvBrandInfo advBrandInfo1 = new AdvBrandInfo();
		// advBrandInfo1.setAdvId("ADV000000000A");
		// advBrandInfo1.setBrandId(1);
		// AdvBrandInfo advBrandInfo2 = new AdvBrandInfo();
		// advBrandInfo2.setAdvId("ADV000000000A");
		// advBrandInfo2.setBrandId(2);
		// advBrandInfoList.add(advBrandInfo1);
		// advBrandInfoList.add(advBrandInfo2);

		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvBrandInfoByAdvId("ADV000000000A", deleteflag)).thenReturn(advBrandInfoList);
		List<AdvBrandInfo> advBrandInfo = advisorServiceImpl.fetchAdvBrandInfoByAdvId("ADV000000000A");
		Assert.assertEquals(0, advBrandInfo.size());
		verify(advisorDao, times(1)).fetchAdvBrandInfoByAdvId("ADV000000000A", deleteflag);
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchArticleStatusList_Success() throws Exception {
		List<ArticleStatus> articleStatusList = new ArrayList<ArticleStatus>();
		ArticleStatus articleStatus1 = new ArticleStatus();
		articleStatus1.setId(1);
		articleStatus1.setDesc("active");
		ArticleStatus articleStatus2 = new ArticleStatus();
		articleStatus2.setId(2);
		articleStatus2.setDesc("inactive");
		articleStatusList.add(articleStatus1);
		articleStatusList.add(articleStatus2);

		when(advisorDao.fetchArticleStatusList()).thenReturn(articleStatusList);
		List<ArticleStatus> articleStatus = advisorServiceImpl.fetchArticleStatusList();
		Assert.assertEquals(2, articleStatus.size());
		verify(advisorDao, times(1)).fetchArticleStatusList();
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchArticleStatusList_Error() throws Exception {
		List<ArticleStatus> articleStatusList = new ArrayList<ArticleStatus>();
		// ArticleStatus articleStatus1 = new ArticleStatus();
		// articleStatus1.setId(1);
		// articleStatus1.setDesc("active");
		// ArticleStatus articleStatus2 = new ArticleStatus();
		// articleStatus2.setId(2);
		// articleStatus2.setDesc("inactive");
		// articleStatusList.add(articleStatus1);
		// articleStatusList.add(articleStatus2);

		when(advisorDao.fetchArticleStatusList()).thenReturn(articleStatusList);
		List<ArticleStatus> articleStatus = advisorServiceImpl.fetchArticleStatusList();
		Assert.assertEquals(0, articleStatus.size());
		verify(advisorDao, times(1)).fetchArticleStatusList();
		// verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkEmailAvailability_Success() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setEmailId("aaa@fo.com");
		adv1.setName("Dobby");
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.checkEmailAvailability("aaa@fo.com", encryptPass)).thenReturn(adv1);
		Advisor adv = advisorServiceImpl.checkEmailAvailability("aaa@fo.com");
		Assert.assertEquals("aaa@fo.com", adv.getEmailId());
		verify(advisorDao, times(1)).checkEmailAvailability("aaa@fo.com", encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkEmailAvailability_Error() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setEmailId("aaa@fo.com");
		adv1.setName("Dobby");
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.checkEmailAvailability("aaa@fo.com", encryptPass)).thenReturn(null);
		Advisor adv = advisorServiceImpl.checkEmailAvailability("aaa@fo.com");
		Assert.assertEquals(null, adv);
		verify(advisorDao, times(1)).checkEmailAvailability("aaa@fo.com", encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	//
	@Test
	public void test_addInvestor_Success() throws Exception {
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String signedUserId = party.getRoleBasedId();
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setPassword("!@AS12as");
		inv.setEmailId("abc@gmail.com");
		inv.setCreated_by(signedUserId);
		inv.setUpdated_by(signedUserId);
		String encryptPass = advTableFields.getEncryption_password();
		String deleteflag = advTableFields.getDelete_flag_N();

		String desc = advTableFields.getPartystatus_desc();
		String delete_flag = advTableFields.getDelete_flag_N();
		long roleId = 2;

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
		when(advisorDao.fetchPartyStatusIdByDesc(desc)).thenReturn(1L);
		when(advisorDao.addInv(inv, delete_flag, encryptPass)).thenReturn(1);
		when(advisorDao.addPartyInv(inv, delete_flag, encryptPass)).thenReturn(1);
		when(advisorDao.addSigninVerification(inv.getEmailId(), encryptPass)).thenReturn(1);
		when(advisorDao.fetchPartyIdByRoleBasedId("INV000000000A", deleteflag)).thenReturn(1L);
		int isPrimaryRole = advTableFields.getIs_primary_role_true();
		when(authDao.addUser_role(1, 1, "INV000000000A", isPrimaryRole)).thenReturn(1);

		int result = advisorServiceImpl.addInvestor(inv, roleId);
		Assert.assertEquals(1, result);

	}

	@Test
	public void test_addInvestor_Error() throws Exception {
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String signedUserId = party.getRoleBasedId();
		Investor inv = new Investor();
		inv.setInvId("INV000000000A");
		inv.setPassword("!@AS12as");
		inv.setEmailId("abc@gmail.com");
		inv.setCreated_by(signedUserId);
		inv.setUpdated_by(signedUserId);
		String encryptPass = advTableFields.getEncryption_password();

		String desc = advTableFields.getPartystatus_desc();
		String delete_flag = advTableFields.getDelete_flag_N();
		long roleId = 2;
		when(advisorDao.fetchEncryptionSecretKey()).thenReturn("key");
		when(advisorDao.fetchPartyStatusIdByDesc(desc)).thenReturn(1L);
		when(advisorDao.addInv(inv, delete_flag, encryptPass)).thenReturn(0);
		when(advisorDao.fetchPartyIdByRoleBasedId("INV000000000A", delete_flag)).thenReturn(1L);
		int isPrimaryRole = advTableFields.getIs_primary_role_true();
		when(authDao.addUser_role(1, 1, "INV000000000A", isPrimaryRole)).thenReturn(1);
		int result = advisorServiceImpl.addInvestor(inv, roleId);
		Assert.assertEquals(0, result);

		verify(advisorDao, times(1)).addInv(inv, delete_flag, encryptPass);
	}

	@Test
	public void test_fetchPartyByEmailId_Success() throws Exception {
		Party party = new Party();
		party.setEmailId("aaa@gmail.com");
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyByEmailId("aaa@gmail.com", encryptPass)).thenReturn(party);
		Party result = advisorServiceImpl.fetchPartyByEmailId("aaa@gmail.com");
		Assert.assertEquals("aaa@gmail.com", result.getEmailId());

		verify(advisorDao, times(1)).fetchPartyByEmailId("aaa@gmail.com", encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyByEmailId_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyByEmailId("aaa@gmail.com", encryptPass)).thenReturn(null);
		Party result = advisorServiceImpl.fetchPartyByEmailId("aaa@gmail.com");
		Assert.assertEquals(null, result);

		verify(advisorDao, times(1)).fetchPartyByEmailId("aaa@gmail.com", encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvRoleIdByName_Success() throws Exception {
		String name = advTableFields.getRoleName();
		when(authDao.fetchRoleIdByName(name)).thenReturn(1);
		long result = advisorServiceImpl.fetchAdvRoleIdByName();
		Assert.assertEquals(result, 1L);
		verify(authDao, times(1)).fetchRoleIdByName(name);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvRoleIdByName_Errror() throws Exception {
		String name = advTableFields.getRoleName();
		when(authDao.fetchRoleIdByName(name)).thenReturn(0);
		long result = advisorServiceImpl.fetchAdvRoleIdByName();
		Assert.assertEquals(result, 0);
		verify(authDao, times(1)).fetchRoleIdByName(name);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchInvRoleIdByName_Success() throws Exception {
		String name = advTableFields.getRoleName_inv();
		when(authDao.fetchRoleIdByName(name)).thenReturn(2);
		long result = advisorServiceImpl.fetchInvRoleIdByName();
		Assert.assertEquals(result, 2L);
		verify(authDao, times(1)).fetchRoleIdByName(name);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchInvRoleIdByName_Error() throws Exception {
		String name = advTableFields.getRoleName_inv();
		when(authDao.fetchRoleIdByName(name)).thenReturn(0);
		long result = advisorServiceImpl.fetchInvRoleIdByName();
		Assert.assertEquals(result, 0);
		verify(authDao, times(1)).fetchRoleIdByName(name);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchNonAdvRoleIdByName_Success() throws Exception {
		String name = advTableFields.getRoleName_nonIndividual();
		when(authDao.fetchRoleIdByName(name)).thenReturn(3);
		long result = advisorServiceImpl.fetchNonAdvRoleIdByName();
		Assert.assertEquals(result, 3L);
		verify(authDao, times(1)).fetchRoleIdByName(name);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchNonAdvRoleIdByName_Error() throws Exception {
		String name = advTableFields.getRoleName_nonIndividual();
		when(authDao.fetchRoleIdByName(name)).thenReturn(0);
		long result = advisorServiceImpl.fetchNonAdvRoleIdByName();
		Assert.assertEquals(result, 0);
		verify(authDao, times(1)).fetchRoleIdByName(name);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchPartyByRoleBasedId_Success() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();

		Party party = new Party();
		party.setRoleBasedId("INV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchPartyByRoleBasedId("INV000000000A", delete_flag, encryptPass)).thenReturn(party);
		Party result = advisorServiceImpl.fetchPartyByRoleBasedId("INV000000000A");
		Assert.assertEquals("INV000000000A", result.getRoleBasedId());

		verify(advisorDao, times(1)).fetchPartyByRoleBasedId("INV000000000A", delete_flag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchPartyByRoleBasedId_Error() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyByRoleBasedId("123", delete_flag, encryptPass)).thenReturn(null);
		Party result = advisorServiceImpl.fetchPartyByRoleBasedId("123");
		Assert.assertEquals(null, result);

		verify(advisorDao, times(1)).fetchPartyByRoleBasedId("123", delete_flag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	// @Test //Encryption
	// public void test_changeInvPassword_Success() throws Exception {
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// String encryptPass = advTableFields.getEncryption_password();
	// String deleteFlag = advTableFields.getDelete_flag_N();
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	//
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteFlag,
	// encryptPass)).thenReturn(party);
	//
	// when(advisorDao.fetchEncryptionSecretKey()).thenReturn("Key");
	// when(advisorDao.changeInvPassword("INV000000000A",
	// "vbHkrwTPK1SfvKzIZAywyFcYfuo078xYgnO5wIVIiIU="))
	// .thenReturn(1);
	// when(advisorDao.changePartyPassword("INV000000000A",
	// "vbHkrwTPK1SfvKzIZAywyFcYfuo078xYgnO5wIVIiIU="))
	// .thenReturn(1);
	//
	// int result = advisorServiceImpl.changeInvPassword("INV000000000A",
	// "!@AS12as");
	// Assert.assertEquals(1, result);
	//
	// }

	@Test
	public void test_changeInvPassword_Error() throws Exception {
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String deleteFlag = advTableFields.getDelete_flag_N();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteFlag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchEncryptionSecretKey()).thenReturn("Key");
		when(advisorDao.changeInvPassword("INV000000000A", "vbHkrwTPK1SfvKzIZAywyFcYfuo078xYgnO5wIVIiIU="))
				.thenReturn(0);
		int result = advisorServiceImpl.changeInvPassword("INV000000000A", "!@AS12as");
		Assert.assertEquals(0, result);

	}

	@Test
	public void test_addKeyPeople_Success() throws Exception {
		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setFullName("jeyanthi");
		keyPeople.setDesignation("emp");
		keyPeople.setImage("bbb.jpg");
		keyPeople.setParentPartyId(2);
		String encryptPass = advTableFields.getEncryption_password();
		String deleteFlag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteFlag, encryptPass)).thenReturn(party);
		when(advisorDao.addKeyPeople(keyPeople, deleteFlag, encryptPass)).thenReturn(1);
		int result = advisorServiceImpl.addKeyPeople(keyPeople);
		Assert.assertEquals(1, result);

	}

	@Test
	public void test_addKeyPeople_Error() throws Exception {
		KeyPeople key = new KeyPeople();
		key.setFullName("david");
		String encryptPass = advTableFields.getEncryption_password();
		String deleteFlag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteFlag, encryptPass)).thenReturn(party);
		when(advisorDao.addKeyPeople(key, "N", encryptPass)).thenReturn(0);
		int result = advisorServiceImpl.addKeyPeople(key);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchKeyPeopleByParentId_Success() throws Exception {
		List<KeyPeople> key = new ArrayList<KeyPeople>();
		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setParentPartyId(1);
		keyPeople.setFullName("david");
		keyPeople.setDesignation("aaa");
		keyPeople.setImage("aaa.jpg");
		key.add(keyPeople);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchKeyPeopleByParentId(1, deleteflag, encryptPass)).thenReturn(key);
		List<KeyPeople> result = advisorServiceImpl.fetchKeyPeopleByParentId(1);
		Assert.assertEquals(1, result.size());

	}

	@Test
	public void test_fetchKeyPeopleByParentId_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchKeyPeopleByParentId(1, "Y", encryptPass)).thenReturn(null);
		List<KeyPeople> result = advisorServiceImpl.fetchKeyPeopleByParentId(1);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void test_fetchAdvisorByUsername_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("Dobby");
		adv1.setUserName("user");
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setBrandId(1);
		advBrandRankList.add(advBrandRank);
		adv1.setAdvBrandRank(advBrandRankList);
		// Brand brand = new Brand();
		// brand.setBrand("Aditya Birla Sun Life");
		// brand.setBrandId(1);
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchAdvisorByUserName("user", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		Advisor adv = advisorServiceImpl.fetchAdvisorByUserName("user");
		Assert.assertEquals("Dobby", adv.getName());
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).fetchAdvisorByUserName("user", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchBrandByBrandId(1);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_teamMemberDeactivate_Success() throws Exception {
		String delete_flag_Y = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		String deleteflag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.teamMemberDeactive("ADV0000000000", delete_flag_Y, "ADV000000000A", 9)).thenReturn(1);
		int result = advisorServiceImpl.teamMemberDeactivate("ADV0000000000", 9);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_teamMemberDeactivate_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		String deleteflag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.teamMemberDeactive("null", "N", "ADV000000000A", 9)).thenReturn(1);
		int result = advisorServiceImpl.teamMemberDeactivate("null", 9);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_addWorkFlowStatusByAdvId_Approved() throws Exception {
		String status_approved = advTableFields.getWorkflow_status_approved();
		String delete_flag = advTableFields.getDelete_flag_N();
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000000");
		adv.setDisplayName("Display");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000B");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchWorkFlowStatusIdByDesc(status_approved)).thenReturn(4);
		when(advisorDao.addWorkFlowStatusApprovedByAdvId("ADV0000000000", 4, "ADV000000000B", null)).thenReturn(1);
		when(advisorDao.fetchAdvisorByAdvId("ADV0000000000", delete_flag, encryptPass)).thenReturn(adv);
		int result = advisorServiceImpl.addWorkFlowStatusByAdvId("ADV0000000000", 4, null);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addWorkFlowStatusByAdvId_Revoke() throws Exception {
		String status_revoke = advTableFields.getWorkflow_status_revoked();
		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", delete_flag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchWorkFlowStatusIdByDesc(status_revoke)).thenReturn(6);
		when(advisorDao.addWorkFlowStatusApprovedByAdvId("ADV0000000000", 6, "ADV000000000A", "reason for revoke"))
				.thenReturn(1);
		int result = advisorServiceImpl.addWorkFlowStatusByAdvId("ADV0000000000", 6, "reason for revoke");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addWorkFlowStatusByAdvId_HidePublic() throws Exception {
		String hide_public = advTableFields.getWorkflow_status_hide_public();
		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", delete_flag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchWorkFlowStatusIdByDesc(hide_public)).thenReturn(5);
		when(advisorDao.addWorkFlowStatusApprovedByAdvId("ADV0000000000", 5, "ADV000000000A", null)).thenReturn(1);
		int result = advisorServiceImpl.addWorkFlowStatusByAdvId("ADV0000000000", 5, null);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_addWorkFlowStatusByAdvId_Default() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV000000000A");

		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", delete_flag, encryptPass)).thenReturn(party);
		when(advisorDao.addWorkFlowStatusByAdvId("ADV0000000000", 5, "ADV000000000A", "reasons")).thenReturn(1);
		int result = advisorServiceImpl.addWorkFlowStatusByAdvId("ADV0000000000", 5, "reasons");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_fetchPromotionByAdvId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		List<Promotion> promotionList = new ArrayList<Promotion>();
		Promotion promotion1 = new Promotion();
		promotion1.setAdvId("ADV000000000A");
		promotion1.setPromotionId(1);
		promotion1.setTitle("promo");
		Promotion promotion2 = new Promotion();
		promotion2.setAdvId("ADV000000000B");
		promotion2.setPromotionId(2);
		promotion2.setTitle("second promo");
		promotionList.add(promotion1);
		promotionList.add(promotion2);

		when(advisorDao.fetchPromotionByAdvId("ADV000000000A", deleteflag)).thenReturn(promotionList);
		List<Promotion> promo = advisorServiceImpl.fetchPromotionByAdvId("ADV000000000A");
		Assert.assertEquals(2, promo.size());
		verify(advisorDao, times(1)).fetchPromotionByAdvId("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removePromotion_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removePromotion(1, deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removePromotion(1, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removePromotion(1, deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removePromotion_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removePromotion(1, deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removePromotion(1, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).removePromotion(1, deleteflag, "ADV000000000A");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addPromotion_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("promo");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addPromotion("ADV000000000A", promotion, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addPromotion("ADV000000000A", promotion);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addPromotion("ADV000000000A", promotion, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addPromotion_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("promo");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.addPromotion("ADV000000000A", promotion, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.addPromotion("ADV000000000A", promotion);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addPromotion("ADV000000000A", promotion, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyPromotion_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("promo");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag)).thenReturn(promotion);
		when(advisorDao.modifyPromotion(1, promotion, "ADV000000000A", deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.modifyPromotion(1, promotion, "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyPromotion(1, promotion, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyPromotion_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("promo");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag)).thenReturn(promotion);
		when(advisorDao.modifyPromotion(1, promotion, "ADV000000000A", deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.modifyPromotion(1, promotion, "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyPromotion(1, promotion, "ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test
	// public void test_fetchApprovedAdv() throws Exception { //Page Error//
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// Advisor adv1 = new Advisor();
	// adv1.setAdvId("ADV000000000A");
	// adv1.setName("AAA");
	// Advisor adv2 = new Advisor();
	// adv2.setAdvId("ADV000000000B");
	// adv2.setName("BBB");
	// advisors.add(adv1);
	// advisors.add(adv2);
	// List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
	// AdvBrandRank advBrandRank = new AdvBrandRank();
	// advBrandRank.setAdvBrandRankId(1);
	// advBrandRank.setBrandId(1);
	// advBrandRankList.add(advBrandRank);
	// adv1.setAdvBrandRank(advBrandRankList);
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchAdvisorList(1, 1, deleteflag,
	// encryptPass)).thenReturn(advisors);
	// when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(1L);
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag)).thenReturn(2L);
	//
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchAdvisorList(1, 1);
	// Assert.assertEquals(2, returnAdvisors.size());
	// verify(advisorDao, times(1)).fetchAdvisorList(1, 1, deleteflag, encryptPass);
	// verify(advisorDao, times(1)).fetchBrandByBrandId(1);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchApprovedAdv_Error() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// String encryptPass = advTableFields.getEncryption_password();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	//
	// when(advisorDao.fetchAdvisorList(1, 1, deleteflag,
	// encryptPass)).thenReturn(advisors);
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchAdvisorList(1, 1);
	// Assert.assertEquals(null, returnAdvisors);
	// verify(advisorDao, times(1)).fetchAdvisorList(1, 1, deleteflag, encryptPass);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_fetchKeyPeopleByKeyPeopleId_Success() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();

		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setKeyPeopleId(1);
		keyPeople.setFullName("XYZ");
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchKeyPeopleByKeyPeopleId(1, delete_flag, encryptPass)).thenReturn(keyPeople);
		KeyPeople result = advisorServiceImpl.fetchKeyPeopleByKeyPeopleId(1);
		Assert.assertEquals("XYZ", result.getFullName());

		verify(advisorDao, times(1)).fetchKeyPeopleByKeyPeopleId(1, delete_flag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchKeyPeopleByKeyPeopleId_Error() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchKeyPeopleByKeyPeopleId(1, delete_flag, encryptPass)).thenReturn(null);
		KeyPeople result = advisorServiceImpl.fetchKeyPeopleByKeyPeopleId(1);
		Assert.assertEquals(null, result);

		verify(advisorDao, times(1)).fetchKeyPeopleByKeyPeopleId(1, delete_flag, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyKeyPeople_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setKeyPeopleId(1);
		keyPeople.setFullName("XYZ");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchKeyPeopleByKeyPeopleId(1, deleteflag, encryptPass)).thenReturn(keyPeople);
		when(advisorDao.modifyKeyPeople(1, keyPeople, encryptPass)).thenReturn(1);
		int result = advisorServiceImpl.modifyKeyPeople(1, keyPeople);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchKeyPeopleByKeyPeopleId(1, deleteflag, encryptPass);
		verify(advisorDao, times(1)).modifyKeyPeople(1, keyPeople, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyKeyPeople_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setKeyPeopleId(1);
		keyPeople.setFullName("XYZ");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchKeyPeopleByKeyPeopleId(1, deleteflag, encryptPass)).thenReturn(keyPeople);
		when(advisorDao.modifyKeyPeople(1, keyPeople, encryptPass)).thenReturn(0);
		int result = advisorServiceImpl.modifyKeyPeople(1, keyPeople);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchKeyPeopleByKeyPeopleId(1, deleteflag, encryptPass);
		verify(advisorDao, times(1)).modifyKeyPeople(1, keyPeople, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_removeKeyPeople_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setKeyPeopleId(1);
		keyPeople.setFullName("XYZ");

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeKeyPeople(1, deleteflag, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.removeKeyPeople(1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).removeKeyPeople(1, deleteflag, "ADV000000000A");
	}

	@Test
	public void test_removeKeyPeople_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		KeyPeople keyPeople = new KeyPeople();
		keyPeople.setKeyPeopleId(1);
		keyPeople.setFullName("XYZ");

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeKeyPeople(1, deleteflag, "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.removeKeyPeople(1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).removeKeyPeople(1, deleteflag, "ADV000000000A");
	}

	@Test
	public void test_fetchFollowersByUserId_Success() throws Exception {
		List<Followers> followers = new ArrayList<Followers>();
		Followers follower = new Followers();
		follower.setUserId("ADV000000000A");
		followers.add(follower);

		String status = advTableFields.getFollower_Status_Active();
		long statusId = 1;

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.fetchFollowersByUserId("ADV0000000001", statusId)).thenReturn(followers);
		List<Followers> result = advisorServiceImpl.fetchFollowersByUserId("ADV0000000001");
		Assert.assertEquals(1, result.size());

		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchFollowersByUserId("ADV0000000001", statusId);
	}

	@Test
	public void test_fetchFollowersByUserId_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = 2;

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.fetchFollowersByUserId("ADV0000000001", statusId)).thenReturn(null);
		List<Followers> result = advisorServiceImpl.fetchFollowersByUserId("ADV0000000001");
		Assert.assertEquals(null, result);

		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchFollowersByUserId("ADV0000000001", statusId);
	}

	// @Test //If condition//
	// public void test_addFollowers_ADVSuccess() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_Y();
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// String encryptPass = advTableFields.getEncryption_password();
	// String signedUserId = "ADV000000000A";
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	//
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	//
	// Followers followers = new Followers();
	// followers.setUserId("ADV0000000001");
	// followers.setAdvId("ADV0000000002");
	// followers.setStatus(1);
	// followers.setUserType(1);
	// followers.setByWhom("ADV0000000001");
	// followers.setCreated_by(signedUserId);
	// followers.setUpdated_by(signedUserId);
	//
	// String status = advTableFields.getFollower_Status_Active();
	// String desc = advTableFields.getUser_type_advisor();// advisor//
	//
	// when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
	// when(advisorDao.fetchUserTypeIdByDesc(desc)).thenReturn(1L);
	// when(advisorDao.addFollowers(followers)).thenReturn(1);
	//
	// int result = advisorServiceImpl.addFollowers("ADV0000000002",
	// "ADV0000000001");
	// Assert.assertEquals(1, result);
	//
	// }
	//
	// @Test //If condition//
	// public void test_addFollowers_INVSuccess() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_Y();
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// String encryptPass = advTableFields.getEncryption_password();
	// String signedUserId = "ADV000000000A";
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	//
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	//
	// Followers followers = new Followers();
	// followers.setUserId("INV0000000001");
	// followers.setAdvId("ADV0000000002");
	// followers.setStatus(1);
	// followers.setUserType(2);
	// followers.setByWhom("INV0000000001");
	// followers.setCreated_by(signedUserId);
	// followers.setUpdated_by(signedUserId);
	// String status = advTableFields.getFollower_Status_Active();
	// String desc = advTableFields.getUser_type_investor();// advisor//
	//
	// when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
	// when(advisorDao.fetchUserTypeIdByDesc(desc)).thenReturn(2L);
	// when(advisorDao.addFollowers(followers)).thenReturn(1);
	//
	// int result = advisorServiceImpl.addFollowers("ADV0000000002",
	// "INV0000000001");
	// Assert.assertEquals(1, result);
	// }

	@Test
	public void test_blockFollowers_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("promo");

		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = 2;
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.updateFollowers(1, 2, "ADV0000000001", "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.blockFollowers(1, "ADV0000000001");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateFollowers(1, 2, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_blockFollowers_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		String status = advTableFields.getFollower_Status_Active();
		long statusId = 1;
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.updateFollowers(1, 1, "ADV0000000001", "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.blockFollowers(1, "ADV0000000001");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateFollowers(1, 1, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_fetchFollowersByUserIdWithAdvId_Success() throws Exception {
		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setStatus(1);

		when(advisorDao.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002")).thenReturn(followers);
		Followers result = advisorServiceImpl.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(1, result.getStatus());
		verify(advisorDao, times(1)).fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");

	}

	@Test
	public void test_fetchFollowersByUserIdWithAdvId_Error() throws Exception {
		when(advisorDao.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002")).thenReturn(null);
		Followers result = advisorServiceImpl.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
	}

	@Test
	public void test_fetchBlockedFollowersByUserIdWithAdvId_Success() throws Exception {
		String desc = advTableFields.getFollower_Status_Blocked();
		long statusId = 3;
		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setStatus(3);

		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(statusId);
		when(advisorDao.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId))
				.thenReturn(followers);
		Followers result = advisorServiceImpl.fetchBlockedFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(3, result.getStatus());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	@Test
	public void test_fetchBlockedFollowersByUserIdWithAdvId_Error() throws Exception {
		String desc = advTableFields.getFollower_Status_Blocked();
		long statusId = 3;

		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(statusId);
		when(advisorDao.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId)).thenReturn(null);
		Followers result = advisorServiceImpl.fetchBlockedFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	@Test
	public void test_fetchFollowersCount_Success() throws Exception {
		List<Integer> lists = new ArrayList<Integer>();
		Advisor advisor = new Advisor();
		advisor.setAdvId("ADV000000000A");
		advisor.setAdvType(1);
		advisor.setName("advisor");
		lists.add(1);
		String desc = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(1L);
		when(advisorDao.fetchFollowersCount("ADV000000000A", 1)).thenReturn(lists);
		List<Integer> returnLists = advisorServiceImpl.fetchFollowersCount("ADV000000000A");
		Assert.assertEquals(1, returnLists.size());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchFollowersCount("ADV000000000A", 1);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchFollowersCount_Error() throws Exception {
		String desc = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(1L);
		when(advisorDao.fetchFollowersCount("ADV000000000A", 1)).thenReturn(null);
		List<Integer> returnLists = advisorServiceImpl.fetchFollowersCount("ADV000000000A");
		Assert.assertEquals(null, returnLists);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchFollowersCount("ADV000000000A", 1);
		verifyNoMoreInteractions(advisorDao);
	}

	// @Test // Error
	// public void test_fetchFollowersByAdvId_Success() throws Exception {
	// List<Followers> followers = new ArrayList<Followers>();
	// Followers advisor = new Followers();
	// advisor.setAdvId("ADV000000000A");
	// advisor.setUserId("ADV000000000B");
	// advisor.setFollowersId(1);
	// advisor.setStatus(1);
	// followers.add(advisor);
	// Advisor adv = new Advisor();
	//
	// String deleteflag = advTableFields.getDelete_flag_N();
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// when(advisorDao.fetchFollowers("ADV000000000A")).thenReturn(followers);
	// when(advisorDao.fetchPublicAdvisorByAdvId("ADV000000000B", deleteflag,
	// encryptPass)).thenReturn(adv);
	//
	// List<Followers> returnLists =
	// advisorServiceImpl.fetchFollowersByAdvId("ADV000000000A");
	// Assert.assertEquals(1, returnLists.size());
	// verify(advisorDao, times(1)).fetchFollowers("ADV000000000A");
	// verify(advisorDao, times(1)).fetchPublicAdvisorByAdvId("ADV000000000B",
	// deleteflag, encryptPass);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test //
	public void test_fetchFollowersByAdvId_Error() throws Exception {
		List<Followers> followers = new ArrayList<Followers>();

		when(advisorDao.fetchFollowers("ADV000000000AB")).thenReturn(followers);

		List<Followers> returnLists = advisorServiceImpl.fetchFollowersByAdvId("ADV000000000AB");
		Assert.assertEquals(0, returnLists.size());
		verify(advisorDao, times(1)).fetchFollowers("ADV000000000AB");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_addOtpForPartyId_Success() throws Exception {
		when(advisorDao.addOtpForPhoneNumber("9999999999", "947432")).thenReturn(1);
		int result = advisorServiceImpl.addOtpForPhoneNumber("9999999999", "947432");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).addOtpForPhoneNumber("9999999999", "947432");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_validateOtp_Success() throws Exception {
		when(advisorDao.fetchOtpByPhoneNumber("9999999999")).thenReturn("947432");
		boolean result = advisorServiceImpl.validateOtp("9999999999", "947432");
		Assert.assertEquals(true, result);
		verify(advisorDao, times(1)).fetchOtpByPhoneNumber("9999999999");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_validateOtp_Error() throws Exception {
		when(advisorDao.fetchOtpByPhoneNumber("9999999999")).thenReturn("9474");
		boolean result = advisorServiceImpl.validateOtp("9999999999", "947432");
		Assert.assertEquals(false, result);
		verify(advisorDao, times(1)).fetchOtpByPhoneNumber("9999999999");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchWorkFlowStatusList_Success() throws Exception {
		List<WorkFlowStatus> workFlowStatusList = new ArrayList<WorkFlowStatus>();
		WorkFlowStatus workFlowStatus = new WorkFlowStatus();
		workFlowStatus.setWorkFlowId(1);
		workFlowStatus.setStatus("active");
		workFlowStatusList.add(workFlowStatus);

		when(advisorDao.fetchWorkFlowStatusList()).thenReturn(workFlowStatusList);
		List<WorkFlowStatus> result = advisorServiceImpl.fetchWorkFlowStatusList();
		Assert.assertEquals(1, result.size());
		verify(advisorDao, times(1)).fetchWorkFlowStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchWorkFlowStatusList_Error() throws Exception {
		List<WorkFlowStatus> workFlowStatusList = new ArrayList<WorkFlowStatus>();

		when(advisorDao.fetchWorkFlowStatusList()).thenReturn(workFlowStatusList);
		List<WorkFlowStatus> result = advisorServiceImpl.fetchWorkFlowStatusList();
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchWorkFlowStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchFollowerStatusList_Success() throws Exception {
		List<FollowerStatus> followerStatusList = new ArrayList<FollowerStatus>();
		FollowerStatus followerStatus = new FollowerStatus();
		followerStatus.setFollowerStatusId(1);
		followerStatus.setStatus("active");
		followerStatusList.add(followerStatus);

		when(advisorDao.fetchFollowerStatusList()).thenReturn(followerStatusList);
		List<FollowerStatus> result = advisorServiceImpl.fetchFollowerStatusList();
		Assert.assertEquals(1, result.size());
		verify(advisorDao, times(1)).fetchFollowerStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchFollowerStatusList_Error() throws Exception {
		List<FollowerStatus> followerStatusList = new ArrayList<FollowerStatus>();

		when(advisorDao.fetchFollowerStatusList()).thenReturn(followerStatusList);
		List<FollowerStatus> result = advisorServiceImpl.fetchFollowerStatusList();
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchFollowerStatusList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchWorkFlowStatusIdByDesc_Success() throws Exception {
		when(advisorDao.fetchWorkFlowStatusIdByDesc("drafted")).thenReturn(1);
		int result = advisorServiceImpl.fetchWorkFlowStatusIdByDesc("drafted");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchWorkFlowStatusIdByDesc("drafted");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchWorkFlowStatusIdByDesc_Error() throws Exception {
		when(advisorDao.fetchWorkFlowStatusIdByDesc("aaa")).thenReturn(0);
		int result = advisorServiceImpl.fetchWorkFlowStatusIdByDesc("aaa");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchWorkFlowStatusIdByDesc("aaa");
		verifyNoMoreInteractions(advisorDao);
	}
	//
	// @Test
	// public void test_fetchExploreAdvisorList_Success() throws Exception { //page
	// Error//
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// Advisor adv1 = new Advisor();
	// adv1.setAdvId("ADV000000000A");
	// adv1.setName("AAA");
	// adv1.setState("tamilnadu");
	// adv1.setCity("chennai");
	// adv1.setDisplayName("adv1");
	// adv1.setPincode("627002");
	// Advisor adv2 = new Advisor();
	// adv2.setAdvId("ADV000000000B");
	// adv2.setName("BBB");
	// adv2.setState("tamilnadu");
	// adv2.setCity("chennai");
	// adv2.setDisplayName("adv2");
	// adv2.setPincode("627002");
	// advisors.add(adv1);
	// advisors.add(adv2);
	// List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
	// AdvBrandRank advBrandRank = new AdvBrandRank();
	// advBrandRank.setAdvBrandRankId(1);
	// advBrandRank.setBrandId(1);
	// advBrandRankList.add(advBrandRank);
	// adv1.setAdvBrandRank(advBrandRankList);
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchExploreAdvisorList(1, 1, "tamilnadu", "chennai",
	// "627002", "adv1", deleteflag,
	// encryptPass)).thenReturn(advisors);
	// when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(1L);
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag)).thenReturn(2L);
	//
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchExploreAdvisorList(1,
	// 1, "tamilnadu", "chennai",
	// "627002", "adv1");
	// Assert.assertEquals(2, returnAdvisors.size());
	// verify(advisorDao, times(1)).fetchExploreAdvisorList(1, 1, "tamilnadu",
	// "chennai", "627002", "adv1", deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).fetchBrandByBrandId(1);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }
	//
	// @Test
	// public void test_fetchExploreAdvisorList_Error() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchExploreAdvisorList(1, 1, "tamilnadu", "chennai",
	// "627002", "627002", deleteflag,
	// encryptPass)).thenReturn(advisors);
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchExploreAdvisorList(1,
	// 1, "tamilnadu", "chennai",
	// "627002", "adv1");
	// Assert.assertEquals(null, returnAdvisors);
	// verify(advisorDao, times(1)).fetchExploreAdvisorList(1, 1, "tamilnadu",
	// "chennai", "627002", "adv1", deleteflag,
	// encryptPass);
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchExploreProductList_Success() throws Exception {
	// List<Product> productList = new ArrayList<Product>();
	// Product product1 = new Product();
	// product1.setProdId(1);
	// product1.setProduct("mutual fund");
	// Product product2 = new Product();
	// product2.setProdId(2);
	// product2.setProduct("financial planning");
	// productList.add(product1);
	// productList.add(product2);
	// when(advisorDao.fetchExploreProductList(1, 1, "mutual fund",
	// "service")).thenReturn(productList);
	// List<Product> products = advisorServiceImpl.fetchExploreProductList(1, 1,
	// "mutual fund", "service");
	// Assert.assertEquals(2, products.size());
	// verify(advisorDao, times(1)).fetchExploreProductList(1, 1, "mutual fund",
	// "service");
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchExploreProductList_Error() throws Exception { //page
	// Error//
	// List<Product> productList = new ArrayList<Product>();
	// Product product = new Product();
	// product.setProdId(1);
	// product.setProduct("Product");
	// productList.add(product);
	// when(advisorDao.fetchExploreProductList("mutal fund",
	// "service")).thenReturn(null);
	// List<Product> products = advisorServiceImpl.fetchExploreProductList("mutual
	// fund", "service");
	// Assert.assertEquals(0, products.size());
	// verify(advisorDao, times(1)).fetchExploreProductList("mutual fund",
	// "service");
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_exploreAdvisorByProduct_Success() throws Exception { //Error
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// Advisor adv1 = new Advisor();
	// adv1.setAdvId("ADV000000000A");
	// adv1.setName("AAA");
	// adv1.setState("tamilnadu");
	// adv1.setCity("chennai");
	// adv1.setDisplayName("adv1");
	// adv1.setPincode("627002");
	// Advisor adv2 = new Advisor();
	// adv2.setAdvId("ADV000000000B");
	// adv2.setName("BBB");
	// adv2.setState("tamilnadu");
	// adv2.setCity("chennai");
	// adv2.setDisplayName("adv2");
	// adv2.setPincode("627002");
	// advisors.add(adv1);
	// advisors.add(adv2);
	// List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
	// AdvBrandRank advBrandRank = new AdvBrandRank();
	// advBrandRank.setAdvBrandRankId(1);
	// advBrandRank.setBrandId(1);
	// advBrandRankList.add(advBrandRank);
	// adv1.setAdvBrandRank(advBrandRankList);
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchExploreAdvisorByProduct(1, 5, "tamilnadu", "chennai",
	// "627002", "adv1", "1", "1",
	// deleteflag, encryptPass)).thenReturn(advisors);
	// when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(1L);
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag)).thenReturn(2L);
	//
	// List<Advisor> returnAdvisors =
	// advisorServiceImpl.fetchExploreAdvisorByProduct(1, 5, "tamilnadu", "chennai",
	// "627002", "adv1", "1", "1");
	// Assert.assertEquals(2, returnAdvisors.size());
	// verify(advisorDao, times(1)).fetchExploreAdvisorByProduct(1, 5, "tamilnadu",
	// "chennai", "627002", "adv1", "1",
	// "1", deleteflag, encryptPass);
	// verify(advisorDao, times(1)).fetchBrandByBrandId(1);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchExploreAdvisorByProduct_Error() throws Exception {
	// //error//
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchExploreAdvisorByProduct(1, 1, "tamilnadu", "chennai",
	// "627002", "adv1", "1", "1",
	// deleteflag, encryptPass)).thenReturn(advisors);
	// List<Advisor> returnAdvisors =
	// advisorServiceImpl.fetchExploreAdvisorByProduct(1, 3, "tamilnadu", "chennai",
	// "627002", "adv1", "1", "1");
	// Assert.assertEquals(null, returnAdvisors);
	// verify(advisorDao, times(1)).fetchExploreAdvisorByProduct(1, 3, "tamilnadu",
	// "chennai", "627002", "adv1", "1",
	// "1", deleteflag, encryptPass);
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchSearchAdvisorList_Success() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// Advisor adv1 = new Advisor();
	// adv1.setAdvId("ADV000000000A");
	// adv1.setName("AAA");
	// adv1.setState("tamilnadu");
	// adv1.setCity("chennai");
	// adv1.setDisplayName("adv1");
	// adv1.setPincode("627002");
	// adv1.setPanNumber("MYPAN1234A");
	// adv1.setPhoneNumber("9874563210");
	// adv1.setUserName("Advisor1");
	// adv1.setEmailId("adcd@gmail.com");
	// Advisor adv2 = new Advisor();
	// adv2.setAdvId("ADV000000000B");
	// adv2.setName("BBB");
	// adv2.setState("tamilnadu");
	// adv2.setCity("chennai");
	// adv2.setDisplayName("adv2");
	// adv2.setPincode("627002");
	// adv2.setPanNumber("MYPAN1234B");
	// adv2.setPhoneNumber("7894561230");
	// adv2.setUserName("Advisor2");
	// adv2.setEmailId("adc@gmail.com");
	// advisors.add(adv1);
	// advisors.add(adv2);
	// List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
	// AdvBrandRank advBrandRank = new AdvBrandRank();
	// advBrandRank.setAdvBrandRankId(1);
	// advBrandRank.setBrandId(1);
	// advBrandRankList.add(advBrandRank);
	// adv1.setAdvBrandRank(advBrandRankList);
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchSearchAdvisorList(1, 5, "MYPAN1234B", "adc@gmail.com",
	// "7894561230", "Advisor2",
	// deleteflag, encryptPass)).thenReturn(advisors);
	// when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag)).thenReturn(1L);
	// when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag)).thenReturn(2L);
	//
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchSearchAdvisorList(1,
	// 5, "MYPAN1234B", "adc@gmail.com",
	// "7894561230", "Advisor2");
	// Assert.assertEquals(2, returnAdvisors.size());
	// verify(advisorDao, times(1)).fetchSearchAdvisorList(1, 5, "MYPAN1234B",
	// "adc@gmail.com", "7894561230",
	// "Advisor2", deleteflag, encryptPass);
	//
	// verify(advisorDao, times(1)).fetchBrandByBrandId(1);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A",
	// deleteflag);
	// verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000B",
	// deleteflag);
	// verifyNoMoreInteractions(advisorDao);
	// }

	// @Test
	// public void test_fetchSearchAdvisorList_Error() throws Exception { //Error//
	// String deleteflag = advTableFields.getDelete_flag_N();
	// List<Advisor> advisors = new ArrayList<Advisor>();
	// String encryptPass = advTableFields.getEncryption_password();
	// when(advisorDao.fetchSearchAdvisorList(1, 5, "MYPAN1234B", "adc@gmail.com",
	// "7894561230", "Advisor2",
	// deleteflag, encryptPass)).thenReturn(advisors);
	//
	// List<Advisor> returnAdvisors = advisorServiceImpl.fetchSearchAdvisorList(1,
	// 5, "MYPAN1234B", "adc@gmail.com",
	// "7894561230", "Advisor2");
	// Assert.assertEquals(null, returnAdvisors);
	// verify(advisorDao, times(1)).fetchSearchAdvisorList(1, 5, "MYPAN1234B",
	// "adc@gmail.com", "7894561230",
	// "Advisor2", deleteflag, encryptPass);
	// verifyNoMoreInteractions(advisorDao);
	// }

	@Test
	public void test_fetchUserTypeList_Success() throws Exception {
		List<UserType> userTypeList = new ArrayList<UserType>();
		UserType userType = new UserType();
		userType.setId(1);
		userType.setDesc("advisor");
		userTypeList.add(userType);

		when(advisorDao.fetchUserTypeList()).thenReturn(userTypeList);
		List<UserType> result = advisorServiceImpl.fetchUserTypeList();
		Assert.assertEquals(1, result.size());
		verify(advisorDao, times(1)).fetchUserTypeList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchUserTypeList_Error() throws Exception {
		List<UserType> userTypeList = new ArrayList<UserType>();

		when(advisorDao.fetchUserTypeList()).thenReturn(userTypeList);
		List<UserType> result = advisorServiceImpl.fetchUserTypeList();
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchUserTypeList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvisorTypeList_Success() throws Exception {
		List<AdvisorType> advisorTypeList = new ArrayList<AdvisorType>();
		AdvisorType advisorType = new AdvisorType();
		advisorType.setId(1);
		advisorType.setAdvType("individual");
		advisorTypeList.add(advisorType);

		when(advisorDao.fetchAdvisorTypeList()).thenReturn(advisorTypeList);
		List<AdvisorType> result = advisorServiceImpl.fetchAdvisorTypeList();
		Assert.assertEquals(1, result.size());
		verify(advisorDao, times(1)).fetchAdvisorTypeList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvisorTypeList_Error() throws Exception {
		List<AdvisorType> advisorTypeList = new ArrayList<AdvisorType>();

		when(advisorDao.fetchAdvisorTypeList()).thenReturn(advisorTypeList);
		List<AdvisorType> result = advisorServiceImpl.fetchAdvisorTypeList();
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchAdvisorTypeList();
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_updateFollowers_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String status = advTableFields.getFollower_Status_Refollow();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("promo");
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(4L);
		when(advisorDao.updateFollowers(1, 4, "ADV0000000001", "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.updateFollowers(1, "ADV0000000001");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateFollowers(1, 4, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_updateFollowers_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String status = advTableFields.getFollower_Status_Refollow();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(0L);
		when(advisorDao.updateFollowers(1, 0, "ADV0000000001", "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.updateFollowers(1, "ADV0000000001");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateFollowers(1, 0, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_approveFollowers_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String status = advTableFields.getFollower_Status_Refollow();
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.updateFollowers(1, 1, "ADV0000000001", "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.approveFollowers(1, "ADV0000000001");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateFollowers(1, 1, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_approveFollowers_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String status = advTableFields.getFollower_Status_Refollow();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(0L);
		when(advisorDao.updateFollowers(1, 0, "ADV0000000001", "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.approveFollowers(1, "ADV0000000001");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateFollowers(1, 0, "ADV0000000001", "ADV000000000A");
	}

	// @Test // error
	// public void test_fetchChatUserListByUserId_Success() throws Exception {
	// List<ChatUser> chatUsers = new ArrayList<ChatUser>();
	// ChatUser chat = new ChatUser();
	// chat.setAdvId("ADV000000000B");
	// chat.setUserId("ADV000000000A");
	// chatUsers.add(chat);
	//
	// when(advisorDao.fetchChatUserListByUserId("ADV000000000A")).thenReturn(chatUsers);
	// List<ChatUser> result =
	// advisorServiceImpl.fetchChatUserListByUserId("ADV000000000A");
	// Assert.assertEquals(1, result.size());
	// verify(advisorDao, times(1)).fetchChatUserListByUserId("ADV000000000A");
	// }

	@Test
	public void test_fetchChatUserListByUserId_Error() throws Exception {
		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		when(advisorDao.fetchChatUserListByUserId("ADV000000000A")).thenReturn(chatUsers);
		List<ChatUser> result = advisorServiceImpl.fetchChatUserListByUserId("ADV000000000A");
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchChatUserListByUserId("ADV000000000A");
	}

	@Test
	public void test_fetchBlockedChatUsersByUserIdWithAdvId_Success() throws Exception {
		String desc = advTableFields.getFollower_Status_Blocked();
		long statusId = 3;
		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId("ADV0000000001");
		chatUser.setUserId("ADV0000000002");
		chatUser.setStatus(3);

		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(statusId);
		when(advisorDao.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId))
				.thenReturn(chatUser);
		ChatUser result = advisorServiceImpl.fetchBlockedChatUsersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(3, result.getStatus());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	@Test
	public void test_fetchBlockedChatUsersByUserIdWithAdvId_Error() throws Exception {
		String desc = advTableFields.getFollower_Status_Blocked();
		long statusId = 3;
		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(statusId);
		when(advisorDao.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId)).thenReturn(null);
		ChatUser result = advisorServiceImpl.fetchBlockedChatUsersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	// @Test //If condition//
	// public void test_addChatUser_Adv_Success() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_Y();
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	//
	// ChatUser chatUser = new ChatUser();
	// chatUser.setUserId("ADV0000000001");
	// chatUser.setAdvId("ADV0000000002");
	// chatUser.setStatus(1);
	// chatUser.setUserType(1);
	// chatUser.setByWhom("ADV0000000001");
	// chatUser.setCreatedBy("ADV000000000A");
	// chatUser.setUpdatedBy("ADV000000000A");
	// String status = advTableFields.getFollower_Status_Inactive();
	// String desc = advTableFields.getUser_type_advisor();// advisor//
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	// when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
	// when(advisorDao.fetchUserTypeIdByDesc(desc)).thenReturn(1L);
	// when(advisorDao.addChatUser(chatUser)).thenReturn(1);
	//
	// int result = advisorServiceImpl.addChatUser("ADV0000000002",
	// "ADV0000000001");
	// Assert.assertEquals(1, result);
	// verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
	// verify(advisorDao, times(1)).fetchUserTypeIdByDesc(desc);
	// verify(advisorDao, times(1)).addChatUser(chatUser);
	// verifyNoMoreInteractions(advisorDao);
	//
	// }

	// @Test //If condition//
	// public void test_addChatUser_Inv_Success() throws Exception {
	// String deleteflag = advTableFields.getDelete_flag_Y();
	// Party party = new Party();
	// party.setRoleBasedId("ADV000000000A");
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// Authentication authentication = Mockito.mock(Authentication.class);
	// SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	// Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
	// SecurityContextHolder.setContext(securityContext);
	// when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass",
	// new ArrayList<>()));
	// ChatUser chatUser = new ChatUser();
	// chatUser.setUserId("INV0000000001");
	// chatUser.setAdvId("ADV0000000002");
	// chatUser.setStatus(1);
	// chatUser.setUserType(2);
	// chatUser.setByWhom("ADV0000000001");
	// chatUser.setCreatedBy("ADV000000000A");
	// chatUser.setUpdatedBy("ADV000000000A");
	// String status = advTableFields.getFollower_Status_Inactive();
	// String desc = advTableFields.getUser_type_investor();// advisor//
	// when(advisorDao.fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass)).thenReturn(party);
	// when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
	// when(advisorDao.fetchUserTypeIdByDesc(desc)).thenReturn(2L);
	// when(advisorDao.addChatUser(chatUser)).thenReturn(1);
	//
	// int result = advisorServiceImpl.addChatUser("ADV0000000002",
	// "INV0000000001");
	// Assert.assertEquals(1, result);
	// verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag,
	// encryptPass);
	// verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
	// verify(advisorDao, times(1)).fetchUserTypeIdByDesc(desc);
	// verify(advisorDao, times(1)).addChatUser(chatUser);
	// }

	@Test
	public void test_updateChatUser_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Refollow();
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(4L);
		when(advisorDao.updateChatUser(1, 4, "ADV0000000002", "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.updateChatUser(1, "ADV0000000002");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateChatUser(1, 4, "ADV0000000002", "ADV000000000A");
	}

	@Test
	public void test_updateChatUser_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Refollow();
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(0L);
		when(advisorDao.updateChatUser(1, 0, "ADV0000000002", "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.updateChatUser(1, "ADV0000000002");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).updateChatUser(1, 0, "ADV0000000002", "ADV000000000A");
	}

	@Test
	public void test_fetchChatUserByUserIdWithAdvId_Success() throws Exception {
		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId("ADV0000000001");
		chatUser.setUserId("ADV0000000002");
		chatUser.setStatus(1);
		String status = advTableFields.getFollower_Status_Active();

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", 1)).thenReturn(chatUser);
		ChatUser result = advisorServiceImpl.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(1, result.getStatus());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", 1);

	}

	@Test
	public void test_fetchChatUserByUserIdWithAdvId_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Active();

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", 1)).thenReturn(null);
		ChatUser result = advisorServiceImpl.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", 1);

	}

	@Test
	public void test_blockChat_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Blocked();
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.modifyChat(1, 1, "ADV0000000001", "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.blockChat(1, "ADV0000000001");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).modifyChat(1, 1, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_blockChat_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Blocked();
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(0L);
		when(advisorDao.modifyChat(1, 0, "ADV0000000001", "ADV000000000A")).thenReturn(0);

		int result = advisorServiceImpl.blockChat(1, "ADV0000000001");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).modifyChat(1, 0, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_fetchReFollowersByUserId_Success() throws Exception {
		List<Followers> followersList = new ArrayList<Followers>();
		Followers followers = new Followers();
		followers.setStatus(4);
		followers.setUserId("ADV0000000001");
		followersList.add(followers);
		String status_Refollow = advTableFields.getFollower_Status_Refollow();
		when(advisorDao.fetchFollowerStatusIdByDesc(status_Refollow)).thenReturn(4L);
		when(advisorDao.fetchFollowersByUserId("ADV0000000001", 4)).thenReturn(followersList);
		List<Followers> result = advisorServiceImpl.fetchReFollowersByUserId("ADV0000000001");
		Assert.assertEquals(1, result.size());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status_Refollow);
		verify(advisorDao, times(1)).fetchFollowersByUserId("ADV0000000001", 4);
	}

	@Test
	public void test_fetchReFollowersByUserId_Error() throws Exception {
		List<Followers> followersList = new ArrayList<Followers>();
		String status_Refollow = advTableFields.getFollower_Status_Refollow();

		when(advisorDao.fetchFollowerStatusIdByDesc(status_Refollow)).thenReturn(4L);
		when(advisorDao.fetchFollowersByUserId("ADV0000000001", 4)).thenReturn(followersList);
		List<Followers> result = advisorServiceImpl.fetchReFollowersByUserId("ADV0000000001");
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status_Refollow);
		verify(advisorDao, times(1)).fetchFollowersByUserId("ADV0000000001", 4);
	}

	@Test
	public void test_fetchProductNameByProdId_Success() throws Exception {
		when(advisorDao.fetchProductNameByProdId(1)).thenReturn("product");
		String result = advisorServiceImpl.fetchProductNameByProdId(1);
		Assert.assertEquals("product", result);
		verify(advisorDao, times(1)).fetchProductNameByProdId(1);
	}

	@Test
	public void test_fetchServiceNameByProdIdAndServiceId_Success() throws Exception {
		when(advisorDao.fetchServiceNameByProdIdAndServiceId(1, 1)).thenReturn("service");
		String result = advisorServiceImpl.fetchServiceNameByProdIdAndServiceId(1, 1);
		Assert.assertEquals("service", result);
		verify(advisorDao, times(1)).fetchServiceNameByProdIdAndServiceId(1, 1);
	}

	@Test
	public void test_fetchBrandNameByProdIdAndBrandId_Success() throws Exception {
		when(advisorDao.fetchBrandNameByProdIdAndBrandId(1, 1)).thenReturn("brand");
		String result = advisorServiceImpl.fetchBrandNameByProdIdAndBrandId(1, 1);
		Assert.assertEquals("brand", result);
		verify(advisorDao, times(1)).fetchBrandNameByProdIdAndBrandId(1, 1);
	}

	@Test
	public void test_fetchServicePlan_Success() throws Exception {
		ServicePlan plan = new ServicePlan();
		plan.setServicePlanLink("link");
		when(advisorDao.fetchServicePlan(1, 1, 1, "plan")).thenReturn(plan);
		ServicePlan result = advisorServiceImpl.fetchServicePlan(1, 1, 1, "plan");
		Assert.assertEquals("link", result.getServicePlanLink());
		verify(advisorDao, times(1)).fetchServicePlan(1, 1, 1, "plan");
	}

	@Test
	public void test_addServicePlan_Success() throws Exception {

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(advisorDao.addServicePlan(1, 1, 1, "plan", "url", "ADV0000000000")).thenReturn(1);
		int result = advisorServiceImpl.addServicePlan(1, 1, 1, "plan", "url");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(advisorDao, times(1)).addServicePlan(1, 1, 1, "plan", "url", "ADV0000000000");
	}

	@Test
	public void test_updateServicePlan_Success() throws Exception {

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000000");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("adv", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("adv", delete_flag, encryptPass)).thenReturn(party);

		when(advisorDao.updateServicePlan(1, 1, 1, "plan", "url", "ADV0000000000")).thenReturn(1);
		int result = advisorServiceImpl.updateServicePlan(1, 1, 1, "plan", "url");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("adv", delete_flag, encryptPass);
		verify(advisorDao, times(1)).updateServicePlan(1, 1, 1, "plan", "url", "ADV0000000000");
	}

	@Test
	public void test_searchStateCityPincodeByCity_Success() throws Exception {
		List<CityList> cityList = new ArrayList<CityList>();
		CityList city = new CityList();
		city.setCity("tirunelveli");
		city.setStateId("1");
		city.setState("tamilnadu");
		List<String> pincodes = new ArrayList<String>();
		pincodes.add("627001");
		pincodes.add("627004");
		pincodes.add("627006");
		city.setPincodes(pincodes);
		cityList.add(city);
		when(advisorDao.searchStateCityPincodeByCity("tirunelveli")).thenReturn(cityList);
		List<CityList> result = advisorServiceImpl.searchStateCityPincodeByCity("tirunelveli");
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("1", result.get(0).getStateId());
		Assert.assertEquals("tamilnadu", result.get(0).getState());
		verify(advisorDao, times(1)).searchStateCityPincodeByCity("tirunelveli");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_searchStateCityPincodeByCity_Error() throws Exception {
		List<CityList> cityList = new ArrayList<CityList>();
		when(advisorDao.searchStateCityPincodeByCity("")).thenReturn(cityList);
		List<CityList> result = advisorServiceImpl.searchStateCityPincodeByCity("");
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).searchStateCityPincodeByCity("");
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkAdvisorIsPresent_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkAdvisorIsPresent("ADV000000000A", deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.checkAdvisorIsPresent("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).checkAdvisorIsPresent("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkAdvisorIsPresent_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkAdvisorIsPresent("ADV000000000B", deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.checkAdvisorIsPresent("ADV000000000B");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).checkAdvisorIsPresent("ADV000000000B", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkAdvProductIsPresent_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkAdvProductIsPresent("ADV000000000A", 1, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.checkAdvProductIsPresent("ADV000000000A", 1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).checkAdvProductIsPresent("ADV000000000A", 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkAdvProductIsPresent_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkAdvProductIsPresent("ADV000000000B", 1, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.checkAdvProductIsPresent("ADV000000000B", 1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).checkAdvProductIsPresent("ADV000000000B", 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkAdvBrandRankIsPresent_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkAdvBrandRankIsPresent("ADV000000000A", 1, 1, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.checkAdvBrandRankIsPresent("ADV000000000A", 1, 1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).checkAdvBrandRankIsPresent("ADV000000000A", 1, 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkAdvBrandRankIsPresent_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkAdvBrandRankIsPresent("ADV000000000B", 1, 1, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.checkAdvBrandRankIsPresent("ADV000000000B", 1, 1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).checkAdvBrandRankIsPresent("ADV000000000B", 1, 1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkKeyPeopleIsPresent_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkKeyPeopleIsPresent(1, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.checkKeyPeopleIsPresent(1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).checkKeyPeopleIsPresent(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkKeyPeopleIsPresent_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();

		when(advisorDao.checkKeyPeopleIsPresent(1, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.checkKeyPeopleIsPresent(1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).checkKeyPeopleIsPresent(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkPartyIsPresent_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();

		when(advisorDao.checkPartyIsPresent(1, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.checkPartyIsPresent(1);
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).checkPartyIsPresent(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkPartyIsPresent_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();

		when(advisorDao.checkPartyIsPresent(1, deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.checkPartyIsPresent(1);
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).checkPartyIsPresent(1, deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkFollowersIsPresent_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.checkFollowersIsPresent("ADV000000000A", "ADV000000000A", 1L)).thenReturn(1);
		int result = advisorServiceImpl.checkFollowersIsPresent("ADV000000000A", "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkFollowersIsPresent("ADV000000000A", "ADV000000000A", 1L);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkFollowersIsPresent_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.checkFollowersIsPresent("ADV000000000B", "ADV000000000A", 1L)).thenReturn(0);
		int result = advisorServiceImpl.checkFollowersIsPresent("ADV000000000B", "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkFollowersIsPresent("ADV000000000B", "ADV000000000A", 1L);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkReFollowersIsPresent_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Refollow();
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(3L);
		when(advisorDao.checkFollowersIsPresent("ADV000000000A", "ADV000000000A", 3L)).thenReturn(1);
		int result = advisorServiceImpl.checkReFollowersIsPresent("ADV000000000A", "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkFollowersIsPresent("ADV000000000A", "ADV000000000A", 3L);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_checkReFollowersIsPresent_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Refollow();
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(3L);
		when(advisorDao.checkFollowersIsPresent("ADV000000000B", "ADV000000000A", 3L)).thenReturn(0);
		int result = advisorServiceImpl.checkReFollowersIsPresent("ADV000000000B", "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkFollowersIsPresent("ADV000000000B", "ADV000000000A", 3L);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_checkChatUserIsPresent_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = 1;
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.checkChatUserIsPresent("ADV000000000B", "ADV000000000A", 1L)).thenReturn(1);
		int result = advisorServiceImpl.checkChatUserIsPresent("ADV000000000B", "ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkChatUserIsPresent("ADV000000000B", "ADV000000000A", 1L);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_checkChatUserIsPresent_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.checkChatUserIsPresent("ADV000000000B", "ADV000000000A", 1L)).thenReturn(0);
		int result = advisorServiceImpl.checkChatUserIsPresent("ADV000000000B", "ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkChatUserIsPresent("ADV000000000B", "ADV000000000A", 1L);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_modifyAward_Remove_Success() throws Exception {
		List<Award> awards = new ArrayList<>();
		Award award1 = new Award();
		award1.setAdvId("ADV000000000A");
		award1.setAwardId(1);
		awards.add(award1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchAwardByadvId("ADV000000000A", deleteflag)).thenReturn(awards);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeAdvAward(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		List<Award> awardList = new ArrayList<>();
		int result = advisorServiceImpl.modifyAward("ADV000000000A", awardList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).fetchAwardByadvId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).removeAdvAward(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyAward_Modify_Success() throws Exception {
		List<Award> awards = new ArrayList<>();
		Award award1 = new Award();
		award1.setAdvId("ADV000000000A");
		award1.setAwardId(1);
		award1.setTitle("award one");
		awards.add(award1);

		Award award2 = new Award();
		award2.setAdvId("ADV000000000A");
		award2.setAwardId(1);
		award2.setTitle("award two");

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag)).thenReturn(award2);
		when(advisorDao.modifyAdvisorAward(1, award2, "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.modifyAward("ADV000000000A", awards);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorAward(1, award2, "ADV000000000A");
	}

	@Test
	public void test_modifyAward_Add_Success() throws Exception {
		List<Award> awards = new ArrayList<>();
		Award award1 = new Award();
		award1.setAdvId("ADV000000000A");
		award1.setTitle("award one");
		awards.add(award1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvAwardInfo("ADV000000000A", award1, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.modifyAward("ADV000000000A", awards);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvAwardInfo("ADV000000000A", award1, deleteflag);
	}

	@Test
	public void test_modifyAward_Success() throws Exception {
		List<Award> awards = new ArrayList<>();
		Award award1 = new Award();
		award1.setAdvId("ADV000000000A");
		award1.setAwardId(1);
		award1.setTitle("award one");

		Award award2 = new Award();
		award2.setAdvId("ADV000000000A");
		award2.setTitle("award two");

		awards.add(award1);
		awards.add(award2);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		List<Award> awardListInDb = new ArrayList<>();
		Award award3 = new Award();
		award3.setAdvId("ADV000000000A");
		award3.setAwardId(1);
		award3.setTitle("award 1");

		Award award4 = new Award();
		award4.setAdvId("ADV000000000A");
		award3.setAwardId(4);
		award4.setTitle("award two");
		awardListInDb.add(award3);
		awardListInDb.add(award4);

		when(advisorDao.fetchAwardByadvId("ADV000000000A", deleteflag)).thenReturn(awards);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeAdvAward(4, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", deleteflag)).thenReturn(award3);
		when(advisorDao.modifyAdvisorAward(1, award3, "ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvAwardInfo("ADV000000000A", award2, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.modifyAward("ADV000000000A", awards);
		Assert.assertEquals(1, result);
	}

	@Test

	public void test_fetchAdvisorByUserNameWithOutToken_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Advisor advisor = new Advisor();
		advisor.setUserName("advisor");
		advisor.setEmailId("advisor@gmail.com");
		advisor.setAdvId("ADV000000000A");

		List<AdvBrandRank> advbrand = new ArrayList<AdvBrandRank>();
		AdvBrandRank advbrandrank = new AdvBrandRank();
		advbrandrank.setAdvBrandRankId(1);
		advbrandrank.setBrandId(1L);

		advbrand.add(advbrandrank);

		advisor.setAdvBrandRank(advbrand);

		when(advisorDao.fetchAdvisorByUserNameWithOutToken("advisor", deleteflag, encryptPass)).thenReturn(advisor);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		when(advisorDao.fetchBrandByBrandId(advbrandrank.getBrandId())).thenReturn("brand");
		Advisor result = advisorServiceImpl.fetchAdvisorByUserNameWithOutToken("advisor");
		Assert.assertEquals("advisor", result.getUserName());
		verify(advisorDao, times(1)).fetchAdvisorByUserNameWithOutToken("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).fetchBrandByBrandId(advbrandrank.getBrandId());

		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvisorByUserNameWithOutToken_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Advisor advisor = new Advisor();
		advisor.setUserName("advisor");
		List<AdvBrandRank> advbrand = new ArrayList<AdvBrandRank>();
		AdvBrandRank advbrandrank = new AdvBrandRank();
		advbrandrank.setAdvBrandRankId(1);
		advbrandrank.setBrandId(1L);
		advbrand.add(advbrandrank);
		advisor.setAdvBrandRank(advbrand);

		when(advisorDao.fetchAdvisorByUserNameWithOutToken("advisor", deleteflag, encryptPass)).thenReturn(advisor);
		Advisor result = advisorServiceImpl.fetchAdvisorByUserNameWithOutToken("advisor");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchAdvisorByUserNameWithOutToken("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyByPhoneNumberAndDeleteFlag_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPhoneNumber("9600334321");

		when(advisorDao.fetchPartyByPhoneNumberAndDeleteFlag("9600334321", deleteflag, encryptPass)).thenReturn(party);
		Party result = advisorServiceImpl.fetchPartyByPhoneNumberAndDeleteFlag("9600334321");
		Assert.assertEquals("9600334321", result.getPhoneNumber());
		verify(advisorDao, times(1)).fetchPartyByPhoneNumberAndDeleteFlag("9600334321", deleteflag, encryptPass);

		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyByPhoneNumberAndDeleteFlag_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchPartyByPhoneNumberAndDeleteFlag("9600334321", deleteflag, encryptPass)).thenReturn(null);
		Party result = advisorServiceImpl.fetchPartyByPhoneNumberAndDeleteFlag("9600334321");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchPartyByPhoneNumberAndDeleteFlag("9600334321", deleteflag, encryptPass);

		verifyNoMoreInteractions(advisorDao);
	}

	public void test_modifyEducation_Add_Success() throws Exception {
		List<Education> educationList = new ArrayList<>();
		Education education1 = new Education();
		education1.setInstitution("eduOne");
		education1.setAdvId("ADV000000000A");
		educationList.add(education1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvEducationInfo("ADV000000000A", education1, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.modifyEducation("ADV000000000A", educationList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvEducationInfo("ADV000000000A", education1, deleteflag);
	}

	@Test
	public void test_modifyEducation_Modify_Success() throws Exception {
		List<Education> educationList = new ArrayList<>();
		Education education1 = new Education();
		education1.setInstitution("eduOne");
		education1.setEduId(1);
		education1.setAdvId("ADV000000000A");
		educationList.add(education1);

		Education education2 = new Education();
		education2.setInstitution("edu two");
		education2.setEduId(1);
		education2.setAdvId("ADV000000000A");

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag)).thenReturn(education2);
		when(advisorDao.modifyAdvisorEducation(1, education2, "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.modifyEducation("ADV000000000A", educationList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorEducation(1, education2, "ADV000000000A");
	}

	@Test
	public void test_modifyEducation_Remove_Success() throws Exception {
		List<Education> educationList = new ArrayList<>();
		Education education1 = new Education();
		education1.setEduId(1);
		education1.setAdvId("ADV000000000A");
		educationList.add(education1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchEducationByadvId("ADV000000000A", deleteflag)).thenReturn(educationList);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvEducation(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);

		List<Education> education = new ArrayList<>();
		int result = advisorServiceImpl.modifyEducation("ADV000000000A", education);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).fetchEducationByadvId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).removeAdvEducation(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyEducation_Success() throws Exception {
		List<Education> educationList = new ArrayList<>();
		Education education1 = new Education();
		education1.setInstitution("eduOne");
		education1.setEduId(1);
		education1.setAdvId("ADV000000000A");

		Education education2 = new Education();
		education2.setInstitution("edu two");
		education2.setAdvId("ADV000000000A");

		educationList.add(education1);
		educationList.add(education2);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		List<Education> educationListInDb = new ArrayList<>();
		Education education3 = new Education();
		education3.setInstitution("eduOne");
		education3.setEduId(1);
		education3.setAdvId("ADV000000000A");

		Education education4 = new Education();
		education4.setAdvId("ADV000000000A");
		education3.setEduId(3);
		education4.setInstitution("edu two");

		educationListInDb.add(education3);
		educationListInDb.add(education4);

		when(advisorDao.fetchEducationByadvId("ADV000000000A", deleteflag)).thenReturn(educationList);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeAdvEducation(4, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", deleteflag)).thenReturn(education3);
		when(advisorDao.modifyAdvisorEducation(1, education3, "ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvEducationInfo("ADV000000000A", education2, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.modifyEducation("ADV000000000A", educationList);
		Assert.assertEquals(1, result);

	}

	@Test
	public void test_modifyExperience_Add_Success() throws Exception {
		List<Experience> expList = new ArrayList<>();
		Experience experience1 = new Experience();
		experience1.setCompany("ffbl");
		experience1.setAdvId("ADV000000000A");
		expList.add(experience1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvExperienceInfo("ADV000000000A", experience1, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.modifyExperience("ADV000000000A", expList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvExperienceInfo("ADV000000000A", experience1, deleteflag);
	}

	@Test
	public void test_modifyExperience_Modify_Success() throws Exception {
		List<Experience> expList = new ArrayList<>();
		Experience experience1 = new Experience();
		experience1.setCompany("ffbl");
		experience1.setExpId(1);
		experience1.setAdvId("ADV000000000A");
		expList.add(experience1);

		Experience experience2 = new Experience();
		experience2.setCompany("ashok");
		experience2.setExpId(1);
		experience2.setAdvId("ADV000000000A");

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag)).thenReturn(experience2);
		when(advisorDao.modifyAdvisorExperience(1, experience2, "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.modifyExperience("ADV000000000A", expList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorExperience(1, experience2, "ADV000000000A");
	}

	@Test
	public void test_modifyExperience_Remove_Success() throws Exception {
		List<Experience> expList = new ArrayList<>();
		Experience experience1 = new Experience();
		experience1.setExpId(1);
		experience1.setAdvId("ADV000000000A");
		expList.add(experience1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchExperienceByadvId("ADV000000000A", deleteflag)).thenReturn(expList);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvExperience(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);

		List<Experience> experience = new ArrayList<>();
		int result = advisorServiceImpl.modifyExperience("ADV000000000A", experience);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).fetchExperienceByadvId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).removeAdvExperience(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyExperience_Success() throws Exception {
		List<Experience> expList = new ArrayList<>();
		Experience experience1 = new Experience();
		experience1.setCompany("ffbl");
		experience1.setExpId(1);
		experience1.setAdvId("ADV000000000A");
		expList.add(experience1);

		Experience experience2 = new Experience();
		experience2.setCompany("ashok");
		experience1.setExpId(1);
		experience2.setAdvId("ADV000000000A");

		expList.add(experience1);
		expList.add(experience2);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		List<Experience> expListInDb = new ArrayList<>();
		Experience experience3 = new Experience();
		experience3.setCompany("ffbl");
		experience3.setExpId(1);
		experience3.setAdvId("ADV000000000A");

		Experience experience4 = new Experience();
		experience4.setCompany("ashok");
		experience3.setExpId(3);
		experience4.setAdvId("ADV000000000A");

		expListInDb.add(experience3);
		expListInDb.add(experience4);

		when(advisorDao.fetchExperienceByadvId("ADV000000000A", deleteflag)).thenReturn(expList);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", deleteflag)).thenReturn(experience3);
		when(advisorDao.modifyAdvisorExperience(1, experience3, "ADV000000000A")).thenReturn(1);
		when(advisorDao.removeAdvExperience(4, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvExperienceInfo("ADV000000000A", experience2, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.modifyExperience("ADV000000000A", expList);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_approveChat_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId("ADV0000000001");
		chatUser.setStatus(4);

		String status = advTableFields.getFollower_Status_Active();
		long statusId = 2;
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.modifyChat(1, 2, "ADV0000000001", "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.approveChat(1, "ADV0000000001");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).modifyChat(1, 2, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_approveChat_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		String status = advTableFields.getFollower_Status_Active();
		long statusId = 2;
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.modifyChat(1, 2, "ADV0000000001", "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.approveChat(1, "ADV0000000001");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).modifyChat(1, 2, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_checkInActiveChatUserIsPresent_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = 2;

		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		ChatUser chat = new ChatUser();
		chat.setAdvId("ADV000000000B");
		chat.setUserId("ADV000000000A");
		chat.setStatus(statusId);
		chatUsers.add(chat);

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.checkChatUserIsPresent("ADV000000000A", "ADV000000000B", 2)).thenReturn(1);
		int result = advisorServiceImpl.checkInActiveChatUserIsPresent("ADV000000000A", "ADV000000000B");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkChatUserIsPresent("ADV000000000A", "ADV000000000B", 2);
	}

	@Test
	public void test_checkInActiveChatUserIsPresent_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Inactive();
		long statusId = 1;

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.checkChatUserIsPresent("ADV000000000A", "ADV000000000B", 1)).thenReturn(0);
		int result = advisorServiceImpl.checkInActiveChatUserIsPresent("ADV000000000A", "ADV000000000B");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkChatUserIsPresent("ADV000000000A", "ADV000000000B", 1);
	}

	@Test
	public void test_checkBlockedChatUserIsPresent_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Blocked();
		long statusId = 3;

		List<ChatUser> chatUsers = new ArrayList<ChatUser>();
		ChatUser chat = new ChatUser();
		chat.setAdvId("ADV000000000B");
		chat.setUserId("ADV000000000A");
		chat.setStatus(statusId);
		chatUsers.add(chat);

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.checkChatUserIsPresent("ADV000000000A", 3)).thenReturn(1);
		int result = advisorServiceImpl.checkBlockedChatUserIsPresent("ADV000000000A");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkChatUserIsPresent("ADV000000000A", 3);
	}

	@Test
	public void test_checkBlockedChatUserIsPresent_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Blocked();
		long statusId = 3;

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.checkChatUserIsPresent("ADV000000000A", 3)).thenReturn(0);
		int result = advisorServiceImpl.checkBlockedChatUserIsPresent("ADV000000000A");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).checkChatUserIsPresent("ADV000000000A", 3);
	}

	@Test
	public void test_modifyPromotion_Add_Success() throws Exception {
		List<Promotion> promotionList = new ArrayList<>();
		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setTitle("award one");
		promotionList.add(promotion);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addPromotion("ADV000000000A", promotion, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAndModifyPromotion("ADV000000000A", promotionList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addPromotion("ADV000000000A", promotion, deleteflag);
	}

	@Test
	public void test_modifyPromotion_Modify_Success() throws Exception {
		List<Promotion> promotionList = new ArrayList<>();
		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("award one");
		promotionList.add(promotion);

		Promotion promotion1 = new Promotion();
		promotion1.setAdvId("ADV000000000A");
		promotion1.setPromotionId(1);
		promotion1.setTitle("award two");

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag)).thenReturn(promotion1);
		when(advisorDao.modifyPromotion(1, promotion1, "ADV000000000A", deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAndModifyPromotion("ADV000000000A", promotionList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyPromotion(1, promotion1, "ADV000000000A", deleteflag);
	}

	@Test
	public void test_modifyPromotion_Remove_Success() throws Exception {
		List<Promotion> promotionList = new ArrayList<>();
		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotionList.add(promotion);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPromotionByAdvId("ADV000000000A", deleteflag)).thenReturn(promotionList);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removePromotion(1, deleteflag, "ADV000000000A")).thenReturn(1);
		List<Promotion> promoList = new ArrayList<>();
		int result = advisorServiceImpl.addAndModifyPromotion("ADV000000000A", promoList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).fetchPromotionByAdvId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).removePromotion(1, deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyPromorion_Success() throws Exception {
		List<Promotion> promotionList = new ArrayList<>();
		Promotion promotion = new Promotion();
		promotion.setAdvId("ADV000000000A");
		promotion.setPromotionId(1);
		promotion.setTitle("award one");

		Promotion promotion1 = new Promotion();
		promotion1.setAdvId("ADV000000000A");
		promotion1.setTitle("award two");

		promotionList.add(promotion);
		promotionList.add(promotion1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		List<Promotion> promotionListInDb = new ArrayList<>();
		Promotion promotion2 = new Promotion();
		promotion2.setAdvId("ADV000000000A");
		promotion2.setPromotionId(1);
		promotion2.setTitle("award 1");

		Promotion promotion3 = new Promotion();
		promotion3.setAdvId("ADV000000000A");
		promotion2.setPromotionId(4);
		promotion3.setTitle("award two");
		promotionListInDb.add(promotion2);
		promotionListInDb.add(promotion3);

		when(advisorDao.fetchPromotionByAdvId("ADV000000000A", deleteflag)).thenReturn(promotionList);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removePromotion(1, deleteflag, "ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", deleteflag)).thenReturn(promotion2);
		when(advisorDao.modifyPromotion(1, promotion2, "ADV000000000A", deleteflag)).thenReturn(1);
		when(advisorDao.addPromotion("ADV000000000A", promotion1, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.addAndModifyPromotion("ADV000000000A", promotionList);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_advProductInfoList_Add_Success() throws Exception {
		List<AdvProduct> advProductList = new ArrayList<>();
		AdvProduct advProduct = new AdvProduct();
		advProduct.setAdvId("ADV000000000A");
		advProductList.add(advProduct);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvProductInfo("ADV000000000A", advProduct, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.addAdvProductInfoList("ADV000000000A", advProductList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvProductInfo("ADV000000000A", advProduct, deleteflag);
	}

	@Test
	public void test_modifyBrandRankIntoTable_Add_Success() throws Exception {
		HashMap<Long, Integer> sortedBrandAndRank = new HashMap<>();
		sortedBrandAndRank.put((long) 1, 2);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.checkAdvBrandRankIsPresent("ADV000000000A", 1, 2, deleteflag)).thenReturn(0);
		when(advisorDao.addAdvBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.addBrandRankIntoTable(sortedBrandAndRank, "ADV000000000A", 1);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyBrandRankIntoTable_Update_Success() throws Exception {
		HashMap<Long, Integer> sortedBrandAndRank = new HashMap<>();
		sortedBrandAndRank.put((long) 1, 2);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.checkAdvBrandRankIsPresent("ADV000000000A", 1, 2, deleteflag)).thenReturn(1);
		when(advisorDao.updateBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.addBrandRankIntoTable(sortedBrandAndRank, "ADV000000000A", 1);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateBrandAndRank(1, 2, "ADV000000000A", 1, deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyProductInfo_Add_Success() throws Exception {
		List<AdvProduct> advProductList = new ArrayList<>();
		AdvProduct advProduct1 = new AdvProduct();
		advProduct1.setProdId(8);
		advProduct1.setAdvId("ADV000000000A");
		advProductList.add(advProduct1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvProductInfo("ADV000000000A", advProduct1, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.addAndModifyProductInfo("ADV000000000A", advProductList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvProductInfo("ADV000000000A", advProduct1, deleteflag);
	}

	@Test
	public void test_modifyProductInfo_Modify_Success() throws Exception {
		List<AdvProduct> advProductList = new ArrayList<>();
		AdvProduct advProduct1 = new AdvProduct();
		advProduct1.setProdId(8);
		advProduct1.setAdvProdId(1);
		advProduct1.setAdvId("ADV000000000A");
		advProductList.add(advProduct1);

		AdvProduct advProduct2 = new AdvProduct();
		advProduct1.setProdId(8);
		advProduct1.setAdvProdId(1);
		advProduct1.setAdvId("ADV000000000A");

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvProduct(1, deleteflag)).thenReturn(advProduct2);
		when(advisorDao.modifyAdvisorProduct(advProduct2, "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.addAndModifyProductInfo("ADV000000000A", advProductList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvProduct(1, deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorProduct(advProduct2, "ADV000000000A");
	}

	@Test
	public void test_modifyProductInfo_Success() throws Exception {
		List<AdvProduct> advProductList = new ArrayList<>();
		AdvProduct advProduct1 = new AdvProduct();
		advProduct1.setProdId(8);
		advProduct1.setAdvProdId(1);
		advProduct1.setAdvId("ADV000000000A");
		advProductList.add(advProduct1);

		AdvProduct advProduct2 = new AdvProduct();
		advProduct1.setProdId(8);
		advProduct1.setAdvProdId(1);
		advProduct1.setAdvId("ADV000000000A");

		advProductList.add(advProduct1);
		advProductList.add(advProduct2);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		List<AdvProduct> advProductListInDb = new ArrayList<>();
		AdvProduct advProduct3 = new AdvProduct();
		advProduct3.setProdId(8);
		advProduct3.setAdvProdId(1);
		advProduct3.setAdvId("ADV000000000A");

		AdvProduct advProduct4 = new AdvProduct();
		advProduct4.setProdId(8);
		advProduct3.setAdvProdId(3);
		advProduct4.setAdvId("ADV000000000A");

		advProductListInDb.add(advProduct3);
		advProductListInDb.add(advProduct4);

		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvProduct(1, deleteflag)).thenReturn(advProduct3);
		when(advisorDao.modifyAdvisorProduct(advProduct3, "ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvProductInfo("ADV000000000A", advProduct2, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.addAndModifyProductInfo("ADV000000000A", advProductList);
		Assert.assertEquals(1, result);

	}

	@Test
	public void test_ChatUserFetchActiveChatUser_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = 3;
		ChatUser chatUser = new ChatUser();
		chatUser.setAdvId("ADV0000000001");
		chatUser.setUserId("ADV0000000002");
		chatUser.setStatus(3);

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId))
				.thenReturn(chatUser);
		ChatUser result = advisorServiceImpl.fetchActiveChatUser("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(3, result.getStatus());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	@Test
	public void test_ChatUserFetchActiveChatUser_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		long statusId = 1;

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(statusId);
		when(advisorDao.fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId)).thenReturn(null);
		ChatUser result = advisorServiceImpl.fetchActiveChatUser("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchChatUserByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);
	}

	@Test
	public void test_ChatUserFetchChatUser_Success() throws Exception {
		ChatUser chat = new ChatUser();
		chat.setAdvId("ADV0000000001");
		chat.setUserId("ADV0000000002");
		chat.setStatus(1);

		when(advisorDao.fetchChatUser("ADV0000000001", "ADV0000000002")).thenReturn(chat);
		ChatUser result = advisorServiceImpl.fetchChatUser("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(1, result.getStatus());
		verify(advisorDao, times(1)).fetchChatUser("ADV0000000001", "ADV0000000002");

	}

	@Test
	public void test_ChatUserFetchChatUser_Error() throws Exception {
		when(advisorDao.fetchChatUser("ADV0000000001", "ADV0000000002")).thenReturn(null);
		ChatUser result = advisorServiceImpl.fetchChatUser("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchChatUser("ADV0000000001", "ADV0000000002");
	}

	// @Test // error
	// public void test_fetchFollowersListByUserId_Success() throws Exception {
	// List<Followers> followers = new ArrayList<Followers>();
	// Followers f = new Followers();
	// f.setUserId("ADV0000000001");
	// followers.add(f);
	//
	// String deleteflag = advTableFields.getDelete_flag_N();
	// String encryptPass = advTableFields.getEncryption_password();
	// Advisor advisor = new Advisor();
	// advisor.setAdvId("ADV0000000000");
	//
	// when(advisorDao.fetchFollowersListByUserId("ADV0000000001")).thenReturn(followers);
	// when(advisorDao.fetchPublicAdvisorByAdvId("ADV0000000000", deleteflag,
	// encryptPass)).thenReturn(advisor);
	// List<Followers> result =
	// advisorServiceImpl.fetchFollowersListByUserId("ADV0000000001");
	// Assert.assertEquals(1, result.size());
	// verify(advisorDao, times(1)).fetchFollowersListByUserId("ADV0000000001");
	// verify(advisorDao, times(1)).fetchPublicAdvisorByAdvId("ADV0000000000",
	// deleteflag, encryptPass);
	//
	// }

	@Test
	public void test_fetchFollowersListByUserId_Error() throws Exception {
		List<Followers> followers = new ArrayList<Followers>();
		when(advisorDao.fetchFollowersListByUserId("ADV0000000001")).thenReturn(followers);
		List<Followers> result = advisorServiceImpl.fetchFollowersListByUserId("ADV0000000001");
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchFollowersListByUserId("ADV0000000001");
	}

	@Test
	public void test_fetchActiveFollowersStatus_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowersStatus(status)).thenReturn(1L);
		long result = advisorServiceImpl.fetchActiveFollowersStatus();
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowersStatus(status);
	}

	@Test
	public void test_fetchReFollowersStatus_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Refollow();
		when(advisorDao.fetchFollowersStatus(status)).thenReturn(4L);
		long result = advisorServiceImpl.fetchReFollowersStatus();
		Assert.assertEquals(4, result);
		verify(advisorDao, times(1)).fetchFollowersStatus(status);
	}

	@Test
	public void test_fetchBlockedFollowersStatus_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Blocked();
		when(advisorDao.fetchFollowersStatus(status)).thenReturn(3L);
		long result = advisorServiceImpl.fetchBlockedFollowersStatus();
		Assert.assertEquals(3, result);
		verify(advisorDao, times(1)).fetchFollowersStatus(status);
	}

	@Test
	public void test_fetchInActiveFollowersStatus_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Active();
		when(advisorDao.fetchFollowersStatus(status)).thenReturn(2L);
		long result = advisorServiceImpl.fetchInActiveFollowersStatus();
		Assert.assertEquals(2, result);
		verify(advisorDao, times(1)).fetchFollowersStatus(status);
	}

	@Test
	public void test_fetchChatUserCount_Success() throws Exception {
		String status = advTableFields.getFollower_Status_Inactive();
		List<Integer> idList = new ArrayList<Integer>();
		int idone = 1;
		idList.add(idone);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.fetchChatUserCount("ADV0000000000", 1L)).thenReturn(idList);
		List<Integer> result = advisorServiceImpl.fetchChatUserCount("ADV0000000000");
		Assert.assertEquals(1, result.size());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchChatUserCount("ADV0000000000", 1L);
	}

	@Test
	public void test_fetchChatUserCount_Error() throws Exception {
		String status = advTableFields.getFollower_Status_Inactive();
		List<Integer> idList = new ArrayList<Integer>();
		// int idone = 1;
		// idList.add(idone);
		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(1L);
		when(advisorDao.fetchChatUserCount("ADV0000000000", 1L)).thenReturn(idList);
		List<Integer> result = advisorServiceImpl.fetchChatUserCount("ADV0000000000");
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchChatUserCount("ADV0000000000", 1L);
	}

	@Test
	public void test_UnFollowersByUserId_Success() throws Exception {
		List<Followers> followersList = new ArrayList<Followers>();
		Followers followers = new Followers();
		followers.setStatus(4);
		followers.setUserId("ADV0000000001");
		followersList.add(followers);
		String deleteflag = advTableFields.getDelete_flag_N();
		String status = advTableFields.getFollower_Status_Refollow();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(4L);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateFollowers(4, 4, "ADV0000000002", "ADV000000000A")).thenReturn(1);
		long result = advisorServiceImpl.unFollowByUserId(4, "ADV0000000002");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateFollowers(4, 4, "ADV0000000002", "ADV000000000A");
	}

	@Test
	public void test_UnFollowersByUserId_Error() throws Exception {
		List<Followers> followersList = new ArrayList<Followers>();
		Followers followers = new Followers();
		followers.setStatus(4);
		followers.setUserId("ADV0000000001");
		followersList.add(followers);
		String deleteflag = advTableFields.getDelete_flag_N();
		String status = advTableFields.getFollower_Status_Refollow();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(4L);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateFollowers(4, 4, "ADV0000000002", "ADV000000000A")).thenReturn(0);
		long result = advisorServiceImpl.unFollowByUserId(4, "ADV0000000002");
		Assert.assertEquals(result, 0);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateFollowers(4, 4, "ADV0000000002", "ADV000000000A");
	}

	@Test
	public void test_fetchUnFollowersByUserIdWithAdvId_Success() throws Exception {
		String desc = advTableFields.getFollower_status_unfollow();
		long statusId = 3;
		Followers followers = new Followers();
		followers.setAdvId("ADV0000000001");
		followers.setUserId("ADV0000000002");
		followers.setStatus(3);

		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(statusId);
		when(advisorDao.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId))
				.thenReturn(followers);
		Followers result = advisorServiceImpl.fetchUnFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(3, result.getStatus());
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	@Test
	public void test_fetchUnFollowersByUserIdWithAdvId_Error() throws Exception {
		String desc = advTableFields.getFollower_status_unfollow();
		long statusId = 3;

		when(advisorDao.fetchFollowerStatusIdByDesc(desc)).thenReturn(statusId);
		when(advisorDao.fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId)).thenReturn(null);
		Followers result = advisorServiceImpl.fetchUnFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(desc);
		verify(advisorDao, times(1)).fetchFollowersByUserIdWithAdvId("ADV0000000001", "ADV0000000002", statusId);

	}

	@Test
	public void test_fetchUnFollowersStatus_Success() throws Exception {
		String statusId = advTableFields.getFollower_status_unfollow();

		when(advisorDao.fetchFollowersStatus(statusId)).thenReturn(1L);
		long result = advisorServiceImpl.fetchUnFollowersStatus();
		Assert.assertEquals(result, 1L);

		verify(advisorDao, times(1)).fetchFollowersStatus(statusId);
	}

	@Test
	public void test_fetchUnFollowersStatus_Error() throws Exception {
		String statusId = advTableFields.getFollower_status_unfollow();

		when(advisorDao.fetchFollowersStatus(statusId)).thenReturn(0L);
		long result = advisorServiceImpl.fetchUnFollowersStatus();
		Assert.assertEquals(result, 0);

		verify(advisorDao, times(1)).fetchFollowersStatus(statusId);
	}

	@Test
	public void test_updateUnFollowers_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String status = advTableFields.getFollower_Status_Refollow();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(4L);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.updateFollowers(1, 4, "ADV0000000001", "ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.updateUnFollowers(1, "ADV0000000001");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateFollowers(1, 4, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_updateUnFollowers_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_Y();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");
		String encryptPass = advTableFields.getEncryption_password();
		String status = advTableFields.getFollower_Status_Refollow();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchFollowerStatusIdByDesc(status)).thenReturn(0L);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.updateFollowers(1, 0, "ADV0000000001", "ADV000000000A")).thenReturn(0);
		int result = advisorServiceImpl.updateUnFollowers(1, "ADV0000000001");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchFollowerStatusIdByDesc(status);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateFollowers(1, 0, "ADV0000000001", "ADV000000000A");
	}

	@Test
	public void test_modifyCertificate_Add_Success() throws Exception {
		List<Certificate> certificate = new ArrayList<>();
		Certificate certificate1 = new Certificate();
		certificate1.setAdvId("ADV000000000A");
		certificate1.setTitle("certificate one");
		certificate.add(certificate1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.addAdvCertificateInfo("ADV000000000A", certificate1, deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.modifyCertificate("ADV000000000A", certificate);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).addAdvCertificateInfo("ADV000000000A", certificate1, deleteflag);
	}

	@Test
	public void test_modifyCertificate_Modify_Success() throws Exception {
		List<Certificate> certificate = new ArrayList<>();
		Certificate certificate1 = new Certificate();
		certificate1.setAdvId("ADV000000000A");
		certificate1.setCertificateId(1);
		certificate1.setTitle("certificate one");
		certificate.add(certificate1);

		Certificate certificate2 = new Certificate();
		certificate2.setAdvId("ADV000000000A");
		certificate2.setCertificateId(1);
		certificate2.setTitle("certificate one");

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag))
				.thenReturn(certificate2);
		when(advisorDao.modifyAdvisorCertificate(1, certificate2, "ADV000000000A")).thenReturn(1);

		int result = advisorServiceImpl.modifyCertificate("ADV000000000A", certificate);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).modifyAdvisorCertificate(1, certificate2, "ADV000000000A");
	}

	@Test
	public void test_modifyCertificate_Remove_Success() throws Exception {
		List<Certificate> certificate = new ArrayList<>();
		Certificate certificate1 = new Certificate();
		certificate1.setAdvId("ADV000000000A");
		certificate1.setCertificateId(1);
		certificate.add(certificate1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		when(advisorDao.fetchCertificateByadvId("ADV000000000A", deleteflag)).thenReturn(certificate);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeAdvCertificate(1, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		List<Certificate> certificateList = new ArrayList<>();
		int result = advisorServiceImpl.modifyCertificate("ADV000000000A", certificateList);
		Assert.assertEquals(1, result);

		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(advisorDao, times(1)).updateAdvisorTimeStamp("ADV000000000A");
		verify(advisorDao, times(1)).fetchCertificateByadvId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).removeAdvCertificate(1, "ADV000000000A", deleteflag, "ADV000000000A");
	}

	@Test
	public void test_modifyCertificate_Success() throws Exception {
		List<Certificate> certificate = new ArrayList<>();
		Certificate certificate1 = new Certificate();
		certificate1.setAdvId("ADV000000000A");
		certificate1.setCertificateId(1);
		certificate1.setTitle("certificate");

		Certificate certificate2 = new Certificate();
		certificate2.setAdvId("ADV000000000A");
		certificate2.setTitle("college certificate");

		certificate.add(certificate1);
		certificate.add(certificate2);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));

		List<Certificate> certificateListInDb = new ArrayList<>();
		Certificate certificate3 = new Certificate();

		certificate3.setAdvId("ADV000000000A");
		certificate3.setCertificateId(1);
		certificate3.setTitle("certificate1");

		Certificate certificate4 = new Certificate();
		certificate4.setAdvId("ADV000000000A");
		certificate3.setCertificateId(4);
		certificate4.setTitle("certificate two");
		certificateListInDb.add(certificate3);
		certificateListInDb.add(certificate4);

		when(advisorDao.fetchCertificateByadvId("ADV000000000A", deleteflag)).thenReturn(certificate);
		when(advisorDao.updateAdvisorTimeStamp("ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(advisorDao.removeAdvCertificate(4, "ADV000000000A", deleteflag, "ADV000000000A")).thenReturn(1);
		when(advisorDao.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", deleteflag))
				.thenReturn(certificate3);
		when(advisorDao.modifyAdvisorCertificate(1, certificate3, "ADV000000000A")).thenReturn(1);
		when(advisorDao.addAdvCertificateInfo("ADV000000000A", certificate2, deleteflag)).thenReturn(1);

		int result = advisorServiceImpl.modifyCertificate("ADV000000000A", certificate);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_fetchPartyByPartyId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);

		when(advisorDao.fetchPartyByPartyId(1, deleteflag, encryptPass)).thenReturn(party);
		Party result = advisorServiceImpl.fetchPartyByPartyId(1);
		Assert.assertEquals(1, result.getPartyId());
		verify(advisorDao, times(1)).fetchPartyByPartyId(1, deleteflag, encryptPass);

		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyByPartyId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchPartyByPartyId(1, deleteflag, encryptPass)).thenReturn(null);
		Party result = advisorServiceImpl.fetchPartyByPartyId(1);
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchPartyByPartyId(1, deleteflag, encryptPass);

		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRoleByRoleId_Success() throws Exception {

		when(authDao.fetchRoleByRoleId(1L)).thenReturn("product");
		String result = advisorServiceImpl.fetchRoleByRoleId(1L);
		Assert.assertEquals("product", result);

		verify(authDao, times(1)).fetchRoleByRoleId(1L);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchRoleByRoleId_Error() throws Exception {

		when(authDao.fetchRoleByRoleId(1L)).thenReturn(null);
		String result = advisorServiceImpl.fetchRoleByRoleId(1L);
		Assert.assertEquals(null, result);

		verify(authDao, times(1)).fetchRoleByRoleId(1L);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchTeamByParentPartyId_Success() throws Exception {
		List<Advisor> adv = new ArrayList<Advisor>();
		Advisor advisor = new Advisor();
		advisor.setParentPartyId(1);
		advisor.setAdvId("ADV000000000A");
		adv.add(advisor);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchTeamByParentPartyId(1, deleteflag, encryptPass)).thenReturn(adv);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		List<Advisor> result = advisorServiceImpl.fetchTeamByParentPartyId(1);
		Assert.assertEquals(1, result.size());

		verify(advisorDao, times(1)).fetchTeamByParentPartyId(1, deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);

	}

	@Test
	public void test_fetchTeamByParentPartyId_Error() throws Exception {
		List<Advisor> adv = new ArrayList<Advisor>();

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		when(advisorDao.fetchTeamByParentPartyId(1, deleteflag, encryptPass)).thenReturn(adv);
		List<Advisor> result = advisorServiceImpl.fetchTeamByParentPartyId(1L);
		Assert.assertEquals(0, result.size());

		verify(advisorDao, times(1)).fetchTeamByParentPartyId(1, deleteflag, encryptPass);
	}

	@Test
	public void test_fetchInvestorByInvId_Success() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_N();
		Investor investor = new Investor();
		investor.setInvId("INV000000000A");
		investor.setEmailId("bala@gmail.com");
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchInvestorByInvId("INV000000000A", delete_flag, encryptPass)).thenReturn(investor);
		Investor result = advisorServiceImpl.fetchInvestorByInvId("INV000000000A");
		Assert.assertEquals("INV000000000A", result.getInvId());
		verify(advisorDao, times(1)).fetchInvestorByInvId("INV000000000A", delete_flag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchInvestorByInvId_Error() throws Exception {
		String delete_flag = advTableFields.getDelete_flag_Y();
		Investor investor = new Investor();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchInvestorByInvId("INV000000000A", delete_flag, encryptPass)).thenReturn(investor);
		Investor result = advisorServiceImpl.fetchInvestorByInvId("INV000000000A");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchInvestorByInvId("INV000000000A", delete_flag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_updateAdvisorAccountAsVerified_Success() throws Exception {
		int accountVerified = advTableFields.getAccount_verified();
		when(advisorDao.updateAdvisorAccountAsVerified("ADV0000000000", accountVerified)).thenReturn(1);
		int result = advisorServiceImpl.updateAdvisorAccountAsVerified("ADV0000000000");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).updateAdvisorAccountAsVerified("ADV0000000000", accountVerified);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_updateAdvisorAccountAsVerified_Error() throws Exception {
		int accountVerified = advTableFields.getAccount_verified();
		when(advisorDao.updateAdvisorAccountAsVerified("ADV0000000000", accountVerified)).thenReturn(0);
		int result = advisorServiceImpl.updateAdvisorAccountAsVerified("ADV0000000000");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).updateAdvisorAccountAsVerified("ADV0000000000", accountVerified);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_updateInvestorAccountAsVerified_Success() throws Exception {
		int accountVerified = advTableFields.getAccount_verified();
		when(advisorDao.updateInvestorAccountAsVerified("INV0000000000", accountVerified)).thenReturn(1);
		int result = advisorServiceImpl.updateInvestorAccountAsVerified("INV0000000000");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).updateInvestorAccountAsVerified("INV0000000000", accountVerified);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_updateInvestorAccountAsVerified_Error() throws Exception {
		int accountVerified = advTableFields.getAccount_verified();
		when(advisorDao.updateInvestorAccountAsVerified("INV0000000000", accountVerified)).thenReturn(0);
		int result = advisorServiceImpl.updateInvestorAccountAsVerified("INV0000000000");
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).updateInvestorAccountAsVerified("INV0000000000", accountVerified);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchByPublicAdvisorId_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		adv1.setName("Dobby");
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setBrandId(1);
		advBrandRankList.add(advBrandRank);
		adv1.setAdvBrandRank(advBrandRankList);

		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPublicAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.fetchBrandByBrandId(1)).thenReturn("Aditya Birla Sun Life");
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		Advisor adv = advisorServiceImpl.fetchByPublicAdvisorID("ADV000000000A");
		Assert.assertEquals("Dobby", adv.getName());
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		verify(advisorDao, times(1)).fetchPublicAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchBrandByBrandId(1);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchByPublicAdvisorId_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		Advisor adv1 = new Advisor();
		adv1.setName("Dobby");
		List<AdvBrandRank> advBrandRankList = new ArrayList<AdvBrandRank>();
		AdvBrandRank advBrandRank = new AdvBrandRank();
		advBrandRank.setAdvBrandRankId(1);
		advBrandRank.setBrandId(1);
		advBrandRankList.add(advBrandRank);
		adv1.setAdvBrandRank(advBrandRankList);
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPublicAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		when(advisorDao.fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag)).thenReturn(1L);
		Advisor adv = advisorServiceImpl.fetchByPublicAdvisorID("ADV000000000A");
		Assert.assertEquals(null, adv);
		verify(advisorDao, times(1)).fetchPublicAdvisorByAdvId("ADV000000000A", deleteflag, encryptPass);
		verify(advisorDao, times(1)).fetchPartyIdByRoleBasedId("ADV000000000A", deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchTypeIdByCorporateAdvtype_Success() throws Exception {
		String corporate = advTableFields.getAdvType_Corporate();
		when(advisorDao.fetchTypeIdByAdvtype(corporate)).thenReturn(1);
		int result = advisorServiceImpl.fetchTypeIdByCorporateAdvtype();
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchTypeIdByAdvtype(corporate);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchTypeIdByCorporateAdvtype_Error() throws Exception {
		String corporate = advTableFields.getAdvType_Corporate();
		when(advisorDao.fetchTypeIdByAdvtype(corporate)).thenReturn(0);
		int result = advisorServiceImpl.fetchTypeIdByCorporateAdvtype();
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchTypeIdByAdvtype(corporate);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchTypeIdByIndividualAdvtype_Success() throws Exception {
		String corporate = advTableFields.getAdvType();
		when(advisorDao.fetchTypeIdByAdvtype(corporate)).thenReturn(1);
		int result = advisorServiceImpl.fetchTypeIdByCorporateAdvtype();
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchTypeIdByAdvtype(corporate);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchTypeIdByIndividualAdvtype_Error() throws Exception {
		String corporate = advTableFields.getAdvType();
		when(advisorDao.fetchTypeIdByAdvtype(corporate)).thenReturn(0);
		int result = advisorServiceImpl.fetchTypeIdByCorporateAdvtype();
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchTypeIdByAdvtype(corporate);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchTotalApprovedAdv_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAllTotalPublicAdvisor(deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.fetchTotalApprovedAdv();
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchAllTotalPublicAdvisor(deleteflag);
	}

	@Test
	public void test_fetchTotalExploreAdvisorList_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setRoleBasedId("ADV000000000A");

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(advisorDao.fetchTotalExploreAdvisorList("tamilnadu", "ambai", "627413", "hari", deleteflag,
				"ADV000000000A")).thenReturn(1);
		int result = advisorServiceImpl.fetchTotalExploreAdvisorList("tamilnadu", "ambai", "627413", "hari");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchTotalExploreAdvisorList("tamilnadu", "ambai", "627413", "hari", deleteflag,
				"ADV000000000A");
	}

	@Test
	public void test_fetchTotalSearchAdvisorList_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchTotalSearchAdvisorList("tamil0098k", "ambai@gmail.com", "9090627413", "hari", deleteflag,
				"advisor2", encryptPass, "1")).thenReturn(1);
		int result = advisorServiceImpl.fetchTotalSearchAdvisorList("tamil0098k", "ambai@gmail.com", "9090627413",
				"hari", "advisor2", "1");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchTotalSearchAdvisorList("tamil0098k", "ambai@gmail.com", "9090627413", "hari",
				deleteflag, "advisor2", encryptPass, "1");
	}

	@Test
	public void test_fetchTotalExploreAdvisorByProduct_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		List<String> stateCityPincodeList = new ArrayList<String>();
		stateCityPincodeList.add("627001");
		stateCityPincodeList.add("627002");
		when(advisorDao.fetchTotalExploreAdvisorByProduct(stateCityPincodeList, "advisor", "1", "1", "1", deleteflag))
				.thenReturn(1);
		int result = advisorServiceImpl.fetchTotalExploreAdvisorByProduct(stateCityPincodeList, "advisor", "1", "1",
				"1");
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchTotalExploreAdvisorByProduct(stateCityPincodeList, "advisor", "1", "1", "1",
				deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPartyByUserName_Success() throws Exception {
		Party party = new Party();
		party.setUserName("advisor");
		party.setPanNumber("ADVIS1234R");
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyByUserName("advisor", encryptPass)).thenReturn(party);
		Party result = advisorServiceImpl.fetchPartyByUserName("advisor");
		Assert.assertEquals("ADVIS1234R", result.getPanNumber());
		verify(advisorDao, times(1)).fetchPartyByUserName("advisor", encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchPartyByUserName_Error() throws Exception {
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyByUserName("advisor", encryptPass)).thenReturn(null);
		Party result = advisorServiceImpl.fetchPartyByUserName("advisor");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchPartyByUserName("advisor", encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchPartyForSignIn_Success() throws Exception {
		Party party = new Party();
		party.setUserName("advisor");
		party.setPanNumber("ADVIS1234R");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		Party result = advisorServiceImpl.fetchPartyForSignIn("advisor");
		Assert.assertEquals("ADVIS1234R", result.getPanNumber());
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchPartyForSignIn_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(null);
		Party result = advisorServiceImpl.fetchPartyForSignIn("advisor");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchGeneratedOtp_Success() throws Exception {
		GeneratedOtp generatedOtp = new GeneratedOtp();
		generatedOtp.setOtp("12345");
		generatedOtp.setPartyId(1);
		when(advisorDao.fetchGeneratedOtp("9874563210", "12345")).thenReturn(generatedOtp);
		GeneratedOtp result = advisorServiceImpl.fetchGeneratedOtp("9874563210", "12345");
		Assert.assertEquals(1, result.getPartyId());
		verify(advisorDao, times(1)).fetchGeneratedOtp("9874563210", "12345");
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchGeneratedOtp_Error() throws Exception {
		when(advisorDao.fetchGeneratedOtp("9874563210", "12345")).thenReturn(null);
		GeneratedOtp result = advisorServiceImpl.fetchGeneratedOtp("9874563210", "12345");
		Assert.assertEquals(null, result);
		verify(advisorDao, times(1)).fetchGeneratedOtp("9874563210", "12345");
		verifyNoMoreInteractions(advisorDao);

	}

	@Test
	public void test_fetchAdvisorTotalList_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvisorTotalList(deleteflag)).thenReturn(1);
		int result = advisorServiceImpl.fetchAdvisorTotalList();
		Assert.assertEquals(1, result);
		verify(advisorDao, times(1)).fetchAdvisorTotalList(deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchAdvisorTotalList_Error() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		when(advisorDao.fetchAdvisorTotalList(deleteflag)).thenReturn(0);
		int result = advisorServiceImpl.fetchAdvisorTotalList();
		Assert.assertEquals(0, result);
		verify(advisorDao, times(1)).fetchAdvisorTotalList(deleteflag);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPincodeByState_Success() throws Exception {
		List<String> pincodes = new ArrayList<String>();
		// long stateId = 1;
		pincodes.add("627001");
		pincodes.add("627002");
		// chatUser.setUserId("ADV0000000002");
		// chatUser.setStatus(3);

		when(advisorDao.fetchStateIdByState("tamilnadu")).thenReturn(1L);
		when(advisorDao.fetchPincodeByState(1L)).thenReturn((pincodes));

		List<String> returnpincodes = advisorServiceImpl.fetchPincodeByState("tamilnadu");
		Assert.assertEquals(2, returnpincodes.size());
		verify(advisorDao, times(1)).fetchStateIdByState("tamilnadu");
		verify(advisorDao, times(1)).fetchPincodeByState(1L);

	}

	@Test
	public void test_fetchExploreAdvisorListByProduct_Success() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Advisor> advisorList = new ArrayList<Advisor>();
		// Advisor advisor = new Advisor();
		// advisor.setDisplayName("advisor");
		// advisor.setAdvId("ADV000000000A");
		// advisorList.add(advisor);
		List<String> pincodes = new ArrayList<String>();
		pincodes.add("627001");
		pincodes.add("627002");
		int pageNum = 0;
		int records = 10;
		Pageable pageable = PageRequest.of(pageNum, records);

		when(advisorDao.fetchExploreAdvisorListByProduct(pageable, pincodes, "advisor", "1", "1", "1", deleteflag,
				encryptPass)).thenReturn(advisorList);
		List<Advisor> result = advisorServiceImpl.fetchExploreAdvisorListByProduct(0, 10, pincodes, "advisor", "1", "1",
				"1");
		Assert.assertEquals(0, result.size());
		verify(advisorDao, times(1)).fetchExploreAdvisorListByProduct(pageable, pincodes, "advisor", "1", "1", "1",
				deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

	@Test
	public void test_fetchPincodeListByPincode_Success() throws Exception {

		List<String> pincodes = new ArrayList<>();
		pincodes.add("627001");
		List<String> cityPincodes = new ArrayList<>();
		cityPincodes.add("627002");
		cityPincodes.add("627003");
		pincodes.addAll(cityPincodes);
		List<String> districtPincodes = new ArrayList<>();
		districtPincodes.add("627004");
		districtPincodes.add("627005");
		pincodes.addAll(districtPincodes);
		List<String> statePincode = new ArrayList<>();
		statePincode.add("627006");
		statePincode.add("627007");
		pincodes.addAll(statePincode);

		// List<String> district = new ArrayList<String>();
		// district.add("chennai");
		// List<String> cityList = new ArrayList<String>();
		// cityList.add("Tirunelveli");
		// List<String> statePincode = new ArrayList<String>();
		// statePincode.add("627413");
		//

		// pincodelist.add("Tirunelveli");
		// pincodelist.add("Tamilnadu");
		when(advisorDao.fetchCityByPincode("627001")).thenReturn("Palayamkottai");
		when(advisorDao.fetchPincodesByCity("Palayamkottai")).thenReturn(cityPincodes);
		when(advisorDao.fetchDistrictByPincode("627001")).thenReturn("Tirunelveli");
		when(advisorDao.fetchPincodesByDistrict("Tirunelveli")).thenReturn(districtPincodes);
		when(advisorDao.fetchStateIdByPincode("627001")).thenReturn(1L);
		when(advisorDao.fetchPincodeByStateId(1L)).thenReturn(statePincode);

		List<String> result = advisorServiceImpl.fetchPincodeListByPincode("627001");
		Assert.assertEquals(7, result.size());

		// verify(advisorDao, times(1)).fetchCityByPincode("627001");
		// verify(advisorDao, times(1)).fetchPincodeByCity("Palayamkottai");
		// verify(advisorDao, times(1)).fetchDistrictByPincode("627001");
		// verify(advisorDao, times(1)).fetchPincodesByDistrict("Tirunelveli");
		// verify(advisorDao, times(1)).fetchStateIdByPincode("627001");
		// verify(advisorDao, times(1)).fetchPincodeByStateId(1);
		// verifyNoMoreInteractions(advisorDao);
	}

	//
	// @Test
	// public void test_fetchPincodeByState_Success() throws Exception {
	//
	//// long stateId = 1;
	// State pincodes = new State();
	// pincodes.setStateId(1);
	////// chatUser.setUserId("ADV0000000002");
	////// chatUser.setStatus(3);
	//
	// when(advisorDao.fetchStateIdByState("tamilnadu")).thenReturn(1L);
	//
	// when(advisorDao.fetchPincodeByState(1L))
	// .thenReturn((List<String>) pincodes);
	// List<String> result = advisorServiceImpl.fetchPincodeByState("tamilnadu");
	// Assert.assertEquals(1, result);
	// verify(advisorDao, times(1)).fetchStateIdByState("tamilnadu");
	// verify(advisorDao, times(1)).fetchPincodeByState(1L);
	//// List<CityList> stateList = new ArrayList<CityList>();
	//// StateList state = new StateList();
	////// city.setCity("tirunelveli");
	//// state.setStateId("1");
	//// state.setState("tamilnadu");
	//// List<String> pincodes = new ArrayList<String>();
	//// pincodes.add("627001");
	//// pincodes.add("627004");
	//// pincodes.add("627006");
	//// state.setPincodes(pincodes);
	//// stateList.add(state);
	//// when(advisorDao.fetchStateIdByState("tamilnadu")).thenReturn(stateList);
	//// when(advisorDao.fetchPincodeByState(1L)).thenReturn(pincodes);
	//// List<CityList> result =
	// advisorServiceImpl.searchStateCityPincodeByCity("tirunelveli");
	//// Assert.assertEquals(1, result.size());
	//// Assert.assertEquals("1", result.get(0).getStateId());
	//// Assert.assertEquals("tamilnadu", result.get(0).getState());
	//// verify(advisorDao, times(1)).searchStateCityPincodeByCity("tirunelveli");
	//// verifyNoMoreInteractions(advisorDao);
	//
	// }fetchAdvisorTypeList

	@Test
	public void test_fetchAdvisorGstByAdvId_Success() throws Exception {
		Advisor adv1 = new Advisor();
		adv1.setAdvId("ADV000000000A");
		String deleteflag = advTableFields.getDelete_flag_Y();
		String encryptPass = advTableFields.getEncryption_password();
		when(advisorDao.fetchAdvisorGstByAdvId("ADV000000000A", deleteflag, encryptPass)).thenReturn(adv1);
		Advisor adv = advisorServiceImpl.fetchAdvisorGstByAdvId("ADV000000000A");
		Assert.assertEquals("ADV000000000A", adv.getAdvId());
		verify(advisorDao, times(1)).fetchAdvisorGstByAdvId("ADV000000000A", deleteflag, encryptPass);
		verifyNoMoreInteractions(advisorDao);
	}

}
