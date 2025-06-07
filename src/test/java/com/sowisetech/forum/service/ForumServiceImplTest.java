package com.sowisetech.forum.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.dao.AdvisorDao;
import com.sowisetech.advisor.dao.AuthDao;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.forum.dao.ForumDao;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumPostVote;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumStatus;
import com.sowisetech.forum.model.ForumSubCategory;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.util.ForumAppMessages;
import com.sowisetech.forum.util.ForumTableFields;

public class ForumServiceImplTest {

	@InjectMocks
	private ForumServiceImpl forumServiceImpl;
	private MockMvc mockMvc;
	@Mock
	private ForumDao forumDao;
	@Mock
	private AuthDao authDao;
	@Mock
	private AdvisorDao advisorDao;

	@Autowired(required = true)
	@Spy
	ForumTableFields forumTableFields;
	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;
	@Autowired(required = true)
	@Spy
	AdminSignin adminSignin;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(forumServiceImpl).build();
	}

	// Create Article Post Test
	@Test
	public void test_createArticlePost() throws Exception {

		String desc = forumTableFields.getForumstatus_inprogress();
		String delete_flag = forumTableFields.getDelete_flag_N();
		String articleDesc = forumTableFields.getArticlestatus_active();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setPartyId(1);
		articlePost.setForumStatusId(1);
		articlePost.setDelete_flag(delete_flag);
		articlePost.setArticleStatusId(1);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1L);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		Advisor adv = new Advisor();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass)).thenReturn(adv);

		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(1);
		when(forumDao.fetchArticleStatusIdByDesc(articleDesc)).thenReturn(1);
		when(forumDao.fetchCountForUrl("url")).thenReturn(1);

		when(forumDao.createArticlePost(articlePost, encryptPass)).thenReturn(1);
		int result = forumServiceImpl.createArticlePost(articlePost, "url");
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchArticleStatusIdByDesc(articleDesc);
		verify(forumDao, times(1)).fetchCountForUrl("url");
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass);
		verify(forumDao, times(1)).createArticlePost(articlePost, encryptPass);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Create Article Post Test For Negative
	@Test
	public void test_createArticlePostNegative() throws Exception {
		String desc = forumTableFields.getForumstatus_inprogress();
		String articleDesc = forumTableFields.getArticlestatus_active();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setPartyId(1);
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(1);
		when(forumDao.fetchArticleStatusIdByDesc(articleDesc)).thenReturn(1);
		when(forumDao.fetchCountForUrl("url")).thenReturn(1);
		when(forumDao.createArticlePost(articlePost, encryptPass)).thenReturn(0);
		Advisor adv = new Advisor();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass)).thenReturn(adv);
		int result = forumServiceImpl.createArticlePost(articlePost, "url");
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass);
		verify(forumDao, times(1)).fetchArticleStatusIdByDesc(articleDesc);
		verify(forumDao, times(1)).fetchCountForUrl("url");
		verify(forumDao, times(1)).createArticlePost(articlePost, encryptPass);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch Article Post Test
	@Test
	public void test_fetchArticlePost() throws Exception {

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(3);
		articlePost.setContent("aaa");
		String delete_flag = forumTableFields.getDelete_flag_N();

		when(forumDao.fetchArticlePost(3, delete_flag)).thenReturn(1);
		int result = forumServiceImpl.fetchArticlePost(3);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchArticlePost(3, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch Article Post Test for Negative
	@Test
	public void test_fetchArticlePostNegative() throws Exception {

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(3);
		articlePost.setContent("aaa");
		articlePost.setDelete_flag("N");
		String delete_flag = forumTableFields.getDelete_flag_N();

		when(forumDao.fetchArticlePost(3, delete_flag)).thenReturn(0);
		int result = forumServiceImpl.fetchArticlePost(3);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchArticlePost(3, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	// // To check ForumStatus is rejected Test
	// @Test
	// public void test_checkForumStatusIsRejected() throws Exception {
	// String status = forumTableFields.getForum_status_rejected();
	// when(forumDao.fetchForumStatus(Mockito.anyLong())).thenReturn(status);
	// boolean result = forumServiceImpl.checkForumStatusIsRejected(3);
	// // Assert.assertTrue("rejected".equals(status));
	// Assert.assertEquals(true, result);
	// verify(forumDao, times(1)).fetchForumStatus(3);
	// verifyNoMoreInteractions(forumDao);
	// }

	// To check ForumStatus is rejected Test for Negative
	@Test
	public void test_checkForumStatusIsRejectedNegative() throws Exception {
		String status = forumTableFields.getForum_status_rejected();
		when(forumDao.fetchForumStatus(1)).thenReturn("inprogress");
		boolean result = forumServiceImpl.checkForumStatusIsRejected(1);
		Assert.assertFalse("inprogress".equals(status));
		Assert.assertEquals(false, result);
		verify(forumDao, times(1)).fetchForumStatus(1);
		verifyNoMoreInteractions(forumDao);
	}

	// Modify ArticlePost Test
	@Test
	public void test_moderateArticlePost() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.moderateArticlePost(1, 3, "ADM0000000001", "Irrelavnt content", "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.moderateArticlePost(1, 3, "ADM0000000001", "Irrelavnt content");

		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).moderateArticlePost(1, 3, "ADM0000000001", "Irrelavnt content", "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	// Modify ArticlePost Test for Negative
	@Test
	public void test_moderateArticlePostNegative() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.moderateArticlePost(1, 2, "ADM0000000001", "Irrelavnt content", "ADV0000000001")).thenReturn(0);
		int result = forumServiceImpl.moderateArticlePost(1, 2, "ADM0000000001", "Irrelavnt content");
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).moderateArticlePost(1, 2, "ADM0000000001", "Irrelavnt content", "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	// Create ArticlePostComment Test
	@Test
	public void test_createArticlePostComment() throws Exception {

		ArticleComment articleComment = new ArticleComment();
		articleComment.setContent("aaa");
		articleComment.setPartyId(1);
		articleComment.setForumStatusId(1);
		String desc = forumTableFields.getForumstatus_inprogress();
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(1);
		when(forumDao.createArticlePostComment(articleComment, encryptPass)).thenReturn(1);
		Advisor adv = new Advisor();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass)).thenReturn(adv);
		int result = forumServiceImpl.createArticlePostComment(articleComment);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass);
		verify(forumDao, times(1)).createArticlePostComment(articleComment, encryptPass);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Create ArticlePostComment Test for Negative
	@Test
	public void test_createArticlePostCommentNegative() throws Exception {

		ArticleComment articleComment = new ArticleComment();
		articleComment.setPartyId(1);
		String desc = forumTableFields.getForumstatus_inprogress();

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(1);
		when(forumDao.createArticlePostComment(articleComment, encryptPass)).thenReturn(0);
		Advisor adv = new Advisor();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(party);
		when(advisorDao.fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass)).thenReturn(adv);
		int result = forumServiceImpl.createArticlePostComment(articleComment);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verify(advisorDao, times(1)).fetchAdvisorByAdvId("ADV0000000001", deleteflag, encryptPass);
		verify(forumDao, times(1)).createArticlePostComment(articleComment, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch ArticlePostComment Test
	@Test
	public void test_fetchArticleComment() throws Exception {

		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(2);
		articleComment.setContent("aaa");
		String delete_flag = forumTableFields.getDelete_flag_N();

		when(forumDao.fetchArticleComment(2, delete_flag)).thenReturn(1);
		int result = forumServiceImpl.fetchArticleComment(2);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchArticleComment(2, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch ArticlePostComment Test for Negative
	@Test
	public void test_fetchArticleCommentNegative() throws Exception {

		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(2);
		articleComment.setContent("aaa");
		String delete_flag = forumTableFields.getDelete_flag_N();

		when(forumDao.fetchArticleComment(2, delete_flag)).thenReturn(0);
		int result = forumServiceImpl.fetchArticleComment(2);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchArticleComment(2, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	// Modify ArticlePostComment Test
	@Test
	public void test_moderateArticleComment() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.moderateArticleComment(1, 2, "ADM0000000001", "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.moderateArticleComment(1, 2, "ADM0000000001");
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).moderateArticleComment(1, 2, "ADM0000000001", "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	// Modify ArticlePostComment Test for Negative
	@Test
	public void test_moderateArticleCommentNegative() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.moderateArticleComment(1, 2, "ADM0000000001", "ADV0000000001")).thenReturn(0);
		int result = forumServiceImpl.moderateArticleComment(1, 2, "ADM0000000001");
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).moderateArticleComment(1, 2, "ADM0000000001", "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	// Article Vote Test for upVote
	@Test
	public void test_createArticleVoteForUp() throws Exception {
		ArticleVote articleVote = new ArticleVote();
		articleVote.setArticleId(1);
		articleVote.setUp_count(2);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = 1;
		int downVote = 0;
		int upCount = 2;
		int upCountNew = 3;

		when(forumDao.fetchUpVoteId(up)).thenReturn(upVote);
		when(forumDao.fetchDownVoteId(down)).thenReturn(downVote);
		when(forumDao.fetchArticleVoteByArticleId(1)).thenReturn(articleVote);
		when(forumDao.fetchUpCountByArticleId(1)).thenReturn(upCount);
		when(forumDao.updateArticleUpVote(upCountNew, 1)).thenReturn(1);
		int result = forumServiceImpl.createArticleVote(1, 1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchUpVoteId(up);
		verify(forumDao, times(1)).fetchDownVoteId(down);
		verify(forumDao, times(1)).fetchArticleVoteByArticleId(1);
		verify(forumDao, times(1)).fetchUpCountByArticleId(1);
		verify(forumDao, times(1)).updateArticleUpVote(upCountNew, 1);
		verifyNoMoreInteractions(forumDao);
	}

	// Article Vote Test for downVote
	@Test
	public void test_createArticleVoteForDown() throws Exception {
		ArticleVote articleVote = new ArticleVote();
		articleVote.setArticleId(1);
		articleVote.setDown_count(2);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = 0;
		int downVote = 1;
		int downCount = 2;
		int downCountNew = 3;

		when(forumDao.fetchUpVoteId(up)).thenReturn(upVote);
		when(forumDao.fetchDownVoteId(down)).thenReturn(downVote);
		when(forumDao.fetchArticleVoteByArticleId(1)).thenReturn(articleVote);
		when(forumDao.fetchDownCountByArticleId(1)).thenReturn(downCount);
		when(forumDao.updateArticleDownVote(downCountNew, 1)).thenReturn(1);
		int result = forumServiceImpl.createArticleVote(1, 1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchUpVoteId(up);
		verify(forumDao, times(1)).fetchDownVoteId(down);
		verify(forumDao, times(1)).fetchArticleVoteByArticleId(1);
		verify(forumDao, times(1)).fetchDownCountByArticleId(1);
		verify(forumDao, times(1)).updateArticleDownVote(downCountNew, 1);
		verifyNoMoreInteractions(forumDao);
	}

	// // Article Vote Test for Negative
	// @Test
	// public void test_createArticleVoteForNegative() throws Exception {
	// ArticleVote articleVote = new ArticleVote();
	// // articleVote.setArticleId(1);
	// // articleVote.setDown_count(2);
	// String up = forumTableFields.getUp();
	// String down = forumTableFields.getDown();
	// int upVote = 0;
	// int downVote = 0;
	// // int downCount = 2;
	// // int downCountNew = 3;
	// when(forumDao.fetchArticleVoteByArticleId(1)).thenReturn(null);
	// when(forumDao.fetchUpVoteId(up)).thenReturn(upVote);
	// when(forumDao.fetchDownVoteId(down)).thenReturn(downVote);
	// when(forumDao.firstArticleVote(articleVote)).thenReturn(0);
	// int result = forumServiceImpl.createArticleVote(1, 1);
	// Assert.assertEquals(0, result);
	// verify(forumDao, times(1)).fetchArticleVoteByArticleId(1);
	// verify(forumDao, times(1)).fetchUpVoteId(up);
	// verify(forumDao, times(1)).fetchDownVoteId(down);
	// verify(forumDao, times(1)).firstArticleVote(articleVote);
	// verifyNoMoreInteractions(forumDao);
	// }

	// fetch ForumSubCategory Test
	@Test
	public void test_fetchForumSubCategory() throws Exception {

		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumCategoryId(1);
		forumSubCategory.setName("aaa");

		when(forumDao.fetchForumSubCategory(1)).thenReturn(forumSubCategory);
		ForumSubCategory result = forumServiceImpl.fetchForumSubCategory(1);

		Assert.assertEquals(forumSubCategory, result);
		Assert.assertEquals("aaa", result.getName());
		verify(forumDao, times(1)).fetchForumSubCategory(1);
		verifyNoMoreInteractions(forumDao);

	}

	// fetch ForumSubCategory Test for Negative
	@Test
	public void test_fetchForumSubCategoryNegative() throws Exception {

		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumCategoryId(1);

		when(forumDao.fetchForumSubCategory(1)).thenReturn(null);
		ForumSubCategory result = forumServiceImpl.fetchForumSubCategory(1);
		Assert.assertEquals(null, result);
		verify(forumDao, times(1)).fetchForumSubCategory(1);
		verifyNoMoreInteractions(forumDao);
	}

	// fetch party Test
	@Test
	public void test_fetchParty() throws Exception {

		Party party = new Party();
		party.setParentPartyId("ADV0000000001");
		String encryptPass = advTableFields.getEncryption_password();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(party);
		Party result = forumServiceImpl.fetchParty(1);
		Assert.assertEquals("ADV0000000001", result.getParentPartyId());
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verifyNoMoreInteractions(forumDao);

	}

	// fetch ForumSubCategory Test for Negative
	@Test
	public void test_fetchPartyNegative() throws Exception {

		Party party = new Party();
		party.setParentPartyId("ADV0000000001");
		String encryptPass = advTableFields.getEncryption_password();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(null);
		Party result = forumServiceImpl.fetchParty(1);
		Assert.assertEquals(null, result);
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verifyNoMoreInteractions(forumDao);

	}

	// remove Article Test
	@Test
	public void test_removeArticle() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.removeArticle(1, delete_flag, "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.removeArticle(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).removeArticle(1, delete_flag, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	// remove Article Test for Negative
	@Test
	public void test_removeArticleNegative() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.removeArticle(1, delete_flag, "ADV0000000001")).thenReturn(0);
		int result = forumServiceImpl.removeArticle(1);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).removeArticle(1, delete_flag, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	// remove Article Comment Test
	@Test
	public void test_removeArticleComment() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.removeArticleComment(1, delete_flag, "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.removeArticleComment(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).removeArticleComment(1, delete_flag, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	// remove Article Comment Test for Negative
	@Test
	public void test_removeArticleCommentNegative() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.removeArticleComment(1, delete_flag, "ADV0000000001")).thenReturn(0);
		int result = forumServiceImpl.removeArticleComment(1);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).removeArticleComment(1, delete_flag, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	// Save Article Vote Test
	@Test
	public void test_saveArticleVote() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.saveArticleVote(1, 1, 3, "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.saveArticleVote(1, 1, 3);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).saveArticleVote(1, 1, 3, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	// Save Article Vote Test for Negative
	@Test
	public void test_saveArticleVoteNegative() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.saveArticleVote(1, 1, 3, "ADV0000000001")).thenReturn(0);
		int result = forumServiceImpl.saveArticleVote(1, 1, 3);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).saveArticleVote(1, 1, 3, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	// To check party is Advisor Test
	@Test
	public void test_checkPartyIsAdvisor() throws Exception {
		long partyId = 1;
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(1);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(authDao.fetchUserRoleByUserId(partyId)).thenReturn(user_role);
		when(authDao.fetchRoleIdByName(forumTableFields.getRole_advisor())).thenReturn(1);
		boolean ans = forumServiceImpl.checkPartyIsAdvisor(partyId);
		Assert.assertEquals(true, ans);
		verify(authDao, times(1)).fetchUserRoleByUserId(1);
		verify(authDao, times(2)).fetchRoleIdByName(forumTableFields.getRole_advisor());
		verify(authDao, times(2)).fetchRoleIdByName(forumTableFields.getNonindividual_role());
		verifyNoMoreInteractions(forumDao);
	}

	// To check party is Advisor Test for Negative
	@Test
	public void test_checkPartyIsAdvisorNegative() throws Exception {
		long partyId = 2;
		List<User_role> user_role = new ArrayList<>();
		User_role userRole = new User_role();
		userRole.setUser_id(1);
		userRole.setRole_id(2);
		userRole.setIsPrimaryRole(1);
		user_role.add(userRole);
		when(authDao.fetchUserRoleByUserId(partyId)).thenReturn(user_role);
		when(authDao.fetchRoleIdByName(forumTableFields.getRole_advisor())).thenReturn(1);
		boolean ans = forumServiceImpl.checkPartyIsAdvisor(partyId);
		Assert.assertEquals(false, ans);
		verify(authDao, times(1)).fetchUserRoleByUserId(2);
		verify(authDao, times(2)).fetchRoleIdByName(forumTableFields.getNonindividual_role());
		verify(authDao, times(2)).fetchRoleIdByName(forumTableFields.getRole_advisor());
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch Recent Article Test
	@Test
	public void test_fetchRecentArticlePostList() throws Exception {
		List<ArticlePost> articlePosts = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		articlePost.setForumStatusId(2);
		articlePosts.add(articlePost);
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String desc = forumTableFields.getForumstatus_approved();
		;
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchRecentArticlePostList(delete_flag, encryptPass)).thenReturn(articlePosts);
		List<ArticlePost> articlePostList = forumServiceImpl.fetchRecentArticlePostList();
		Assert.assertEquals(1, articlePostList.size());
		Assert.assertEquals("article", articlePostList.get(0).getContent());
	}

	// Fetch Recent Article Test for Negative
	@Test
	public void test_fetchRecentArticlePostListNegative() throws Exception {

		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		String desc = forumTableFields.getForumstatus_approved();
		Pageable pageable = PageRequest.of(0, 10);
		List<ArticlePost> articlePostList = new ArrayList<>();
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(null);
		when(forumDao.fetchRecentArticlePostList(delete_flag, encryptPass)).thenReturn(articlePostList);
		List<ArticlePost> articlePostList1 = forumServiceImpl.fetchRecentArticlePostList();
		Assert.assertEquals(articlePostList, articlePostList1);
	}

	// Fetch ForumCategory Test
	@Test
	public void test_fetchAllForumCategory() throws Exception {
		List<ForumCategory> categoryList = new ArrayList<ForumCategory>();
		ForumCategory category = new ForumCategory();
		category.setForumCategoryId(1);
		category.setName("category");
		ForumSubCategory subcategory = new ForumSubCategory();
		subcategory.setForumCategoryId(1);
		subcategory.setName("subcategory");
		subcategory.setForumSubCategoryId(1);
		List<ForumSubCategory> subCategoryList = new ArrayList<ForumSubCategory>();
		subCategoryList.add(subcategory);
		category.setForumSubCategory(subCategoryList);
		categoryList.add(category);
		when(forumDao.fetchAllForumCategory()).thenReturn(categoryList);
		List<ForumCategory> forumCategory = forumServiceImpl.fetchAllForumCategory();
		Assert.assertEquals(1, forumCategory.size());
		Assert.assertEquals("category", forumCategory.get(0).getName());
		Assert.assertEquals(1, forumCategory.get(0).getForumSubCategory().size());
	}

	// Fetch ForumCategory Test for Negative
	@Test
	public void test_fetchAllForumCategoryNegative() throws Exception {

		when(forumDao.fetchAllForumCategory()).thenReturn(null);
		List<ForumCategory> forumCategory = forumServiceImpl.fetchAllForumCategory();
		Assert.assertEquals(null, forumCategory);
	}

	// Fetch Recent Approved Article Test
	@Test
	public void test_fetchRecentApprovedArticle() throws Exception {
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		String desc = forumTableFields.getForumstatus_approved();
		String delete_flag = forumTableFields.getDelete_flag_N();

		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(2);
		String encryptPass = advTableFields.getEncryption_password();
		when(forumDao.fetchRecentApprovedArticle(2, delete_flag, encryptPass)).thenReturn(articlePost);
		ArticlePost article = forumServiceImpl.fetchRecentApprovedArticle();
		Assert.assertEquals("article", article.getContent());
		Assert.assertEquals("ADM000000000A", article.getAdminId());

	}

	// Fetch Recent Approved Article Test for Negative
	@Test
	public void test_fetchRecentApprovedArticleNegative() throws Exception {

		String desc = forumTableFields.getForumstatus_approved();
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(2);
		String encryptPass = advTableFields.getEncryption_password();
		when(forumDao.fetchRecentApprovedArticle(2, delete_flag, encryptPass)).thenReturn(null);
		ArticlePost article = forumServiceImpl.fetchRecentApprovedArticle();
		Assert.assertEquals(null, article);

	}
	// TODO:
	// Fetch ArticleList In ApprovedOrder Test
	// @Test
	// public void test_fetchAllArticleInApprovedOrder() throws Exception {
	// List<ArticlePost> articlePosts = new ArrayList<ArticlePost>();
	// ArticlePost articlePost = new ArticlePost();
	// articlePost.setArticleId(1);
	// articlePost.setContent("article");
	// articlePost.setAdminId("ADM000000000A");
	// articlePost.setForumCategoryId(1);
	// articlePost.setForumSubCategoryId(1);
	// articlePosts.add(articlePost);
	// String desc = forumTableFields.getForumstatus_approved();
	// String delete_flag = forumTableFields.getDelete_flag_N();
	// when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(2);
	// String encryptPass = advTableFields.getEncryption_password();
	// Pageable pageable = PageRequest.of(0, 10);
	// when(forumDao.fetchAllArticleInApprovedOrder(1, pageable, delete_flag,
	// encryptPass)).thenReturn(articlePosts);
	// List<ArticlePost> articlePostList =
	// forumServiceImpl.fetchAllArticleInApprovedOrder(0, 10);
	// Assert.assertEquals(1, articlePostList.size());
	// Assert.assertEquals("article", articlePostList.get(0).getContent());
	// }

	// Fetch ArticleList In ApprovedOrder Test for Negative
	@Test
	public void test_fetchAllArticleInApprovedOrderNegative() throws Exception {

		String desc = forumTableFields.getForumstatus_approved();
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(2);
		String articleStatus = forumTableFields.getArticlestatus_active();
		when(forumDao.fetchArticleStatusIdByDesc(articleStatus)).thenReturn(1);
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchAllArticleInApprovedOrder(1, 1, delete_flag, encryptPass)).thenReturn(null);
		List<ArticlePost> articlePostList = forumServiceImpl.fetchAllArticleInApprovedOrder();
		Assert.assertEquals(0, articlePostList.size());
	}

	// Fetch ArticleComment by articleId Test
	@Test
	public void test_fetchArticleCommentByArticleId() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_N();
		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setArticleId(1);
		articleComment1.setCommentId(1);
		articleComment1.setContent("This article is good.");
		articleCommentList.add(articleComment1);
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchArticleCommentByArticleId(1, delete_flag, encryptPass)).thenReturn(articleCommentList);
		when(forumDao.fetchArticleCommentByParentId(1, 1, delete_flag, encryptPass)).thenReturn(articleCommentList);
		List<ArticleComment> articleComment = forumServiceImpl.fetchArticleCommentByArticleId(1);
		Assert.assertEquals(1, articleComment.size());
		verify(forumDao, times(1)).fetchArticleCommentByArticleId(1, delete_flag, encryptPass);
		verify(forumDao, times(1)).fetchArticleCommentByParentId(1, 1, delete_flag, encryptPass);

	}

	// Fetch ArticleComment by articleId Test for Negative
	@Test
	public void test_fetchArticleCommentByArticleIdNegative() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchArticleCommentByArticleId(1, delete_flag, encryptPass)).thenReturn(null);
		when(forumDao.fetchArticleCommentByParentId(1L, 1L, delete_flag, encryptPass)).thenReturn(null);
		List<ArticleComment> articleComment = forumServiceImpl.fetchArticleCommentByArticleId(1);
		Assert.assertEquals(null, articleComment);
		verify(forumDao, times(1)).fetchArticleCommentByArticleId(1, delete_flag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch ArticleList Test
	@Test
	public void test_fetchArticlePostList() {
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("Content one");
		articlePost.setPartyId(1);
		articlePostList.add(articlePost);
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(null);
		when(forumDao.fetchArticlePostList(pageable, delete_flag, encryptPass)).thenReturn(articlePostList);
		List<ArticlePost> result = forumServiceImpl.fetchArticlePostList(0, 10);
		Assert.assertEquals(1, result.size());
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verify(forumDao, times(1)).fetchArticlePostList(pageable, delete_flag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch ArticleList Test for Negative
	@Test
	public void test_fetchArticlePostListNegative() {
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("Content one");
		articlePostList.add(articlePost);
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchArticlePostList(pageable, delete_flag, encryptPass)).thenReturn(null);
		List<ArticlePost> result = forumServiceImpl.fetchArticlePostList(0, 10);
		Assert.assertEquals(null, result);
		verify(forumDao, times(1)).fetchArticlePostList(pageable, delete_flag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch Article by articleId Test
	@Test
	public void test_fetchArticlePostByarticleId() {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		ArticlePost article = new ArticlePost();
		article.setArticleId(2);
		article.setContent("Post");
		String encryptPass = advTableFields.getEncryption_password();
		when(forumDao.fetchArticlePostByarticleId(2, delete_flag_N, encryptPass)).thenReturn(article);
		ArticlePost result = forumServiceImpl.fetchArticlePostByarticleId(2);
		Assert.assertEquals("Post", result.getContent());
		verify(forumDao, times(1)).fetchArticlePostByarticleId(2, delete_flag_N, encryptPass);
	}

	// Fetch Article by articleId Test for Negative
	@Test
	public void test_fetchArticlePostByarticleIdNegative() {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		ArticlePost article = new ArticlePost();
		article.setArticleId(2);
		article.setContent("Post");
		String encryptPass = advTableFields.getEncryption_password();
		when(forumDao.fetchArticlePostByarticleId(2, delete_flag_N, encryptPass)).thenReturn(null);
		ArticlePost result = forumServiceImpl.fetchArticlePostByarticleId(2);
		Assert.assertEquals(null, result);
		verify(forumDao, times(1)).fetchArticlePostByarticleId(2, delete_flag_N, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	// Fetch ArticleList by partyId Test
	@Test
	public void test_fetchArticleListByPartyId() {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("Content one");
		articlePost.setPartyId(3);
		articlePost.setArticleStatusId(1);
		articlePost.setForumStatusId(1);
		articlePostList.add(articlePost);

		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchArticleListByPartyId(3, delete_flag_N, encryptPass)).thenReturn(articlePostList);
		when(forumDao.fetchForumStatus(1)).thenReturn("inprogress");
		when(forumDao.fetchArticleStatus(1)).thenReturn("active");
		List<ArticlePost> result = forumServiceImpl.fetchArticleListByPartyId(3);
		Assert.assertEquals(1, result.size());
		verify(forumDao, times(1)).fetchArticleListByPartyId(3, delete_flag_N, encryptPass);
		verify(forumDao, times(1)).fetchForumStatus(1);
		verify(forumDao, times(1)).fetchArticleStatus(1);
	}

	// Fetch ArticleList by partyId Test for Negative
	@Test
	public void test_fetchArticleListByPartyIdNegative() {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("Content one");
		articlePost.setPartyId(3);
		articlePostList.add(articlePost);
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchArticleListByPartyId(3, delete_flag_N, encryptPass)).thenReturn(null);
		List<ArticlePost> result = forumServiceImpl.fetchArticleListByPartyId(3);
		Assert.assertEquals(null, result);
		verify(forumDao, times(1)).fetchArticleListByPartyId(3, delete_flag_N, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_changeArticleStatus() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.changeArticleStatus(1, 1, "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.changeArticleStatus(1, 1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).changeArticleStatus(1, 1, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_changeArticleStatus_Negative() throws Exception {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.changeArticleStatus(1, 1, "ADV0000000001")).thenReturn(0);
		int result = forumServiceImpl.changeArticleStatus(1, 1);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).changeArticleStatus(1, 1, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	// Thread and Post
	@Test
	public void test_createThread() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		ForumThread forumThread = new ForumThread();
		forumThread.setThreadId(1);
		forumThread.setPartyId(1);
		forumThread.setSubject("aaa");
		String desc = forumTableFields.getForumstatus_inprogress();
		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(1);
		when(forumDao.createForumThread(forumThread)).thenReturn(1);
		int result = forumServiceImpl.createThread(forumThread);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).createForumThread(forumThread);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_createPost() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		ForumPost forumPost = new ForumPost();
		forumPost.setContent("bbb");
		forumPost.setPartyId(1);
		String desc = forumTableFields.getForumstatus_inprogress();
		when(forumDao.createForumPost(forumPost)).thenReturn(1);
		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(1);
		int result = forumServiceImpl.createPost(forumPost);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).createForumPost(forumPost);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchThread() throws Exception {

		ForumThread forumThread = new ForumThread();
		forumThread.setSubject("aaa");
		forumThread.setThreadId(1);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchThread(1, delete_flag)).thenReturn(forumThread);
		ForumThread result = forumServiceImpl.fetchThread(1);
		Assert.assertEquals("aaa", result.getSubject());
		verify(forumDao, times(1)).fetchThread(1, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchPost() throws Exception {

		ForumPost forumPost = new ForumPost();
		forumPost.setContent("bbb");
		forumPost.setPostId(2);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchPost(2, delete_flag)).thenReturn(forumPost);
		ForumPost result = forumServiceImpl.fetchPost(2);
		Assert.assertEquals("bbb", result.getContent());
		verify(forumDao, times(1)).fetchPost(2, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_moderatePost() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.moderatePost(2, 1, "ADM0000000001", "ADM0000000001")).thenReturn(1);
		int result = forumServiceImpl.moderatePost(2, 1, "ADM0000000001");
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).moderatePost(2, 1, "ADM0000000001", "ADM0000000001");
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_moderateThread() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.moderateThread(2, 2, "ADM0000000002", "ADM0000000001")).thenReturn(1);
		int result = forumServiceImpl.moderateThread(2, 2, "ADM0000000002");
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).moderateThread(2, 2, "ADM0000000002", "ADM0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_createForumPostVoteForUp() throws Exception {
		ForumPostVote forumPostVote = new ForumPostVote();
		forumPostVote.setPostId(1);
		forumPostVote.setUp_count(2);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = 1;
		int downVote = 0;
		int upCount = 2;
		int upCountNew = 3;

		when(forumDao.fetchUpVoteId(up)).thenReturn(upVote);
		when(forumDao.fetchDownVoteId(down)).thenReturn(downVote);
		when(forumDao.fetchForumPostVoteByPostId(1)).thenReturn(forumPostVote);
		when(forumDao.fetchUpCountByPostId(1)).thenReturn(upCount);
		when(forumDao.updateForumPostUpVote(upCountNew, 1)).thenReturn(1);
		int result = forumServiceImpl.createForumPostVote(1, 1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchUpVoteId(up);
		verify(forumDao, times(1)).fetchDownVoteId(down);
		verify(forumDao, times(1)).fetchForumPostVoteByPostId(1);
		verify(forumDao, times(1)).fetchUpCountByPostId(1);
		verify(forumDao, times(1)).updateForumPostUpVote(upCountNew, 1);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_createForumPostVoteForDown() throws Exception {
		ForumPostVote forumPostVote = new ForumPostVote();
		forumPostVote.setPostId(1);
		forumPostVote.setUp_count(2);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = 0;
		int downVote = 1;
		int downCount = 2;
		int downCountNew = 3;

		when(forumDao.fetchUpVoteId(up)).thenReturn(upVote);
		when(forumDao.fetchDownVoteId(down)).thenReturn(downVote);
		when(forumDao.fetchForumPostVoteByPostId(1)).thenReturn(forumPostVote);
		when(forumDao.fetchDownCountByPostId(1)).thenReturn(downCount);
		when(forumDao.updateForumPostDownVote(downCountNew, 1)).thenReturn(1);
		int result = forumServiceImpl.createForumPostVote(1, 1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchUpVoteId(up);
		verify(forumDao, times(1)).fetchDownVoteId(down);
		verify(forumDao, times(1)).fetchForumPostVoteByPostId(1);
		verify(forumDao, times(1)).fetchDownCountByPostId(1);
		verify(forumDao, times(1)).updateForumPostDownVote(downCountNew, 1);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_savePostVote() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.savePostVote(1, 1, 3, "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.savePostVote(1, 1, 3);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).savePostVote(1, 1, 3, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_removePost() throws Exception {
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADV0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.removePost(1, delete_flag, "ADV0000000001")).thenReturn(1);
		int result = forumServiceImpl.removePost(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).removePost(1, delete_flag, "ADV0000000001");
		verifyNoMoreInteractions(forumDao);

	}

	@Test
	public void test_createQuery() throws Exception {
		List<ForumQuery> forumQueryList = new ArrayList<>();
		ForumQuery question = new ForumQuery();
		question.setQuery("query");
		question.setQueryId(1);
		question.setPartyId(1);
		forumQueryList.add(question);
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.createQuery(question)).thenReturn(1);
		int result = forumServiceImpl.createQuery(forumQueryList);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).createQuery(question);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchForumQueryByPostedToPartyId() throws Exception {
		ForumQuery question = new ForumQuery();
		question.setQuery("query");
		question.setQueryId(1);
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchForumQueryByPostedToPartyId(1, 1, delete_flag)).thenReturn(question);
		ForumQuery result = forumServiceImpl.fetchForumQueryByPostedToPartyId(1, 1);
		Assert.assertEquals(1, result.getQueryId());
		Assert.assertEquals("query", result.getQuery());
		verify(forumDao, times(1)).fetchForumQueryByPostedToPartyId(1, 1, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_createAnswer() throws Exception {
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setAnswerId(1);
		forumAnswer.setPartyId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setRoleBasedId("ADM0000000001");
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);
		when(forumDao.createForumAnswer(forumAnswer)).thenReturn(1);
		int result = forumServiceImpl.createForumAnswer(forumAnswer);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verify(forumDao, times(1)).createForumAnswer(forumAnswer);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId() throws Exception {
		List<ForumQuery> list = new ArrayList<>();
		ForumQuery question = new ForumQuery();
		question.setQuery("query");
		question.setQueryId(1);
		list.add(question);
		String delete_flag = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchQueryListByPostedToPartyId(1, pageable, delete_flag)).thenReturn(list);
		List<ForumQuery> result = forumServiceImpl.fetchQueryListByPostedToPartyId(1, 0, 10);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("query", result.get(0).getQuery());
		verify(forumDao, times(1)).fetchQueryListByPostedToPartyId(1, pageable, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchAnswerListByQueryId() throws Exception {
		List<ForumAnswer> list = new ArrayList<>();
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setAnswerId(1);
		list.add(forumAnswer);
		String delete_flag = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchAnswerListByQueryId(1, pageable, delete_flag)).thenReturn(list);
		List<ForumAnswer> result = forumServiceImpl.fetchAnswerListByQueryId(1, 0, 10);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("answer", result.get(0).getAnswer());
		verify(forumDao, times(1)).fetchAnswerListByQueryId(1, pageable, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_addArticleToFavorite() throws Exception {

		String delete_flag = forumTableFields.getDelete_flag_N();

		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(1);
		articleFavorite.setPartyId(1);
		articleFavorite.setDelete_flag(delete_flag);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1L);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.addArticleToFavorite(articleFavorite)).thenReturn(1);
		int result = forumServiceImpl.addArticleToFavorite(articleFavorite);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).addArticleToFavorite(articleFavorite);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_addArticleToFavorite_Error() throws Exception {

		String delete_flag = forumTableFields.getDelete_flag_N();

		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(1);
		articleFavorite.setPartyId(1);
		articleFavorite.setDelete_flag(delete_flag);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1L);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.addArticleToFavorite(articleFavorite)).thenReturn(0);
		int result = forumServiceImpl.addArticleToFavorite(articleFavorite);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).addArticleToFavorite(articleFavorite);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_Success() throws Exception {
		List<Long> list = new ArrayList<>();
		list.add(1L);
		List<ArticlePost> articlePostList = new ArrayList<>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setPartyId(1);
		articlePost.setArticleStatus("Active");
		articlePostList.add(articlePost);
		String encryptPass = advTableFields.getEncryption_password();

		when(forumDao.fetchParty(1, encryptPass)).thenReturn(null);
		String delete_flag = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchFavoriteArticlesByPartyId(1, delete_flag)).thenReturn(list);
		when(forumDao.fetchArticleListByArticleIdList(list, delete_flag, encryptPass)).thenReturn(articlePostList);
		List<ArticlePost> result = forumServiceImpl.fetchFavoriteArticlesByPartyId(1);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.get(0).getPartyId());
		verify(forumDao, times(1)).fetchFavoriteArticlesByPartyId(1, delete_flag);
		verify(forumDao, times(1)).fetchArticleListByArticleIdList(list, delete_flag, encryptPass);
		verify(forumDao, times(1)).fetchParty(1, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_Error() throws Exception {
		List<Long> list = new ArrayList<>();
		list.add(1L);
		List<ArticlePost> articlePostList = new ArrayList<>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setArticleStatus("Active");
		articlePostList.add(articlePost);

		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(0, 10);
		when(forumDao.fetchFavoriteArticlesByPartyId(1, delete_flag)).thenReturn(list);
		when(forumDao.fetchArticleListByArticleIdList(list, delete_flag, encryptPass)).thenReturn(null);
		List<ArticlePost> result = forumServiceImpl.fetchFavoriteArticlesByPartyId(1);
		Assert.assertEquals(null, result);
		// Assert.assertEquals(1, result.get(0).getPartyId());
		verify(forumDao, times(1)).fetchFavoriteArticlesByPartyId(1, delete_flag);
		verify(forumDao, times(1)).fetchArticleListByArticleIdList(list, delete_flag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_removeArticleFromFavorite() throws Exception {

		String delete_flag = forumTableFields.getDelete_flag_Y();

		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(1);
		articleFavorite.setPartyId(1);
		articleFavorite.setDelete_flag(delete_flag);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1L);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.removeArticleFromFavorite(articleFavorite)).thenReturn(1);
		int result = forumServiceImpl.removeArticleFromFavorite(articleFavorite);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).removeArticleFromFavorite(articleFavorite);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_removeArticleFromFavorite_Error() throws Exception {

		String delete_flag = forumTableFields.getDelete_flag_Y();

		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(1);
		articleFavorite.setPartyId(1);
		articleFavorite.setDelete_flag(delete_flag);

		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Party party = new Party();
		party.setPartyId(1L);
		party.setRoleBasedId("ADV0000000001");
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(authentication.getPrincipal()).thenReturn(new User("advisor", "pass", new ArrayList<>()));
		when(forumDao.fetchPartyForSignIn("advisor", deleteflag, encryptPass)).thenReturn(party);

		when(forumDao.removeArticleFromFavorite(articleFavorite)).thenReturn(0);
		int result = forumServiceImpl.removeArticleFromFavorite(articleFavorite);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).removeArticleFromFavorite(articleFavorite);
		verify(forumDao, times(1)).fetchPartyForSignIn("advisor", deleteflag, encryptPass);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkPartyIsPresent() throws Exception {
		when(forumDao.checkPartyIsPresent(1)).thenReturn(1);
		int result = forumServiceImpl.checkPartyIsPresent(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).checkPartyIsPresent(1);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkPartyIsPresent_Error() throws Exception {
		when(forumDao.checkPartyIsPresent(100)).thenReturn(0);
		int result = forumServiceImpl.checkPartyIsPresent(100);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).checkPartyIsPresent(100);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkForumSubCategoryIsPresent() throws Exception {
		when(forumDao.checkForumSubCategoryIsPresent(1)).thenReturn(1);
		int result = forumServiceImpl.checkForumSubCategoryIsPresent(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).checkForumSubCategoryIsPresent(1);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkForumSubCategoryIsPresent_Error() throws Exception {
		when(forumDao.checkForumSubCategoryIsPresent(100)).thenReturn(0);
		int result = forumServiceImpl.checkForumSubCategoryIsPresent(100);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).checkForumSubCategoryIsPresent(100);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkThreadIsPresent() throws Exception {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		when(forumDao.checkThreadIsPresent(1, delete_flag_N)).thenReturn(1);
		int result = forumServiceImpl.checkThreadIsPresent(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).checkThreadIsPresent(1, delete_flag_N);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkThreadIsPresent_Error() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		when(forumDao.checkThreadIsPresent(1, delete_flag)).thenReturn(0);
		int result = forumServiceImpl.checkThreadIsPresent(1);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).checkThreadIsPresent(1, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkPostIsPresent() throws Exception {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		when(forumDao.checkPostIsPresent(1, delete_flag_N)).thenReturn(1);
		int result = forumServiceImpl.checkPostIsPresent(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).checkPostIsPresent(1, delete_flag_N);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkPostIsPresent_Error() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		when(forumDao.checkPostIsPresent(1, delete_flag)).thenReturn(0);
		int result = forumServiceImpl.checkPostIsPresent(1);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).checkPostIsPresent(1, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkForumQueryByPostedToPartyId() throws Exception {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		when(forumDao.checkForumQueryByPostedToPartyId(1L, 1L, delete_flag_N)).thenReturn(1);
		int result = forumServiceImpl.checkForumQueryByPostedToPartyId(1L, 1L);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).checkForumQueryByPostedToPartyId(1L, 1L, delete_flag_N);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_checkForumQueryByPostedToPartyId_Error() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		when(forumDao.checkForumQueryByPostedToPartyId(1L, 0, delete_flag)).thenReturn(0);
		int result = forumServiceImpl.checkForumQueryByPostedToPartyId(1L, 0);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).checkForumQueryByPostedToPartyId(1L, 0, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchTotalArticleCommentByParentId_Success() throws Exception {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchTotalArticleCommentByParentId(1L, 1L, delete_flag_N)).thenReturn(1);
		int result = forumServiceImpl.fetchTotalArticleCommentByParentId(1L, 1L);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchTotalArticleCommentByParentId(1L, 1L, delete_flag_N);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchTotalArticleCommentByParentId_Error() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		when(forumDao.fetchTotalArticleCommentByParentId(1L, 0, delete_flag)).thenReturn(0);
		int result = forumServiceImpl.fetchTotalArticleCommentByParentId(1L, 0);
		Assert.assertEquals(0, result);
		verify(forumDao, times(1)).fetchTotalArticleCommentByParentId(1L, 0, delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchArticleCommentByParentId_Success() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(0, 10);
		String encryptPass = advTableFields.getEncryption_password();

		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setArticleId(1);
		articleComment1.setCommentId(1);
		articleComment1.setContent("This article is good.");
		ArticleComment articleComment2 = new ArticleComment();
		articleComment2.setArticleId(1);
		articleComment2.setCommentId(2);
		articleCommentList.add(articleComment1);
		articleCommentList.add(articleComment2);

		when(forumDao.fetchArticleCommentByParentId(1L, 1L, delete_flag, encryptPass)).thenReturn(articleCommentList);

		List<ArticleComment> articleComment = forumServiceImpl.fetchArticleCommentByParentId(1L, 1L);
		Assert.assertEquals(2, articleComment.size());
		verify(forumDao, times(1)).fetchArticleCommentByParentId(1L, 1L, delete_flag, encryptPass);
	}

	@Test
	public void test_fetchArticleCommentByParentId_Error() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(0, 10);
		String encryptPass = advTableFields.getEncryption_password();

		when(forumDao.fetchArticleCommentByParentId(1L, 1L, delete_flag, encryptPass)).thenReturn(null);
		List<ArticleComment> articleComment = forumServiceImpl.fetchArticleCommentByParentId(1L, 1L);
		when(forumDao.fetchParty(1, encryptPass)).thenReturn(null);
		Assert.assertEquals(null, articleComment);
		verify(forumDao, times(1)).fetchArticleCommentByParentId(1L, 1L, delete_flag, encryptPass);
		verifyNoMoreInteractions(forumDao);

		// String delete_flag = forumTableFields.getDelete_flag_N();
		// String encryptPass = advTableFields.getEncryption_password();
		// Pageable pageable = PageRequest.of(0, 10);
		// when(forumDao.fetchArticleCommentByArticleId(1, pageable, delete_flag,
		// encryptPass)).thenReturn(null);
		// List<ArticleComment> articleComment =
		// forumServiceImpl.fetchArticleCommentByArticleId(1, 0, 10);
		// Assert.assertEquals(null, articleComment);
		// verify(forumDao, times(1)).fetchArticleCommentByArticleId(1, pageable,
		// delete_flag, encryptPass);
		// verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchArticlePostByTitle_Success() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_N();
		when(forumDao.fetchArticlePostByTitle("url", delete_flag)).thenReturn("url");
		String url = forumServiceImpl.fetchArticlePostByTitle("url");
		Assert.assertEquals("url", url);
		verify(forumDao, times(1)).fetchArticlePostByTitle("url", delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchArticlePostByTitle_Error() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		when(forumDao.fetchArticlePostByTitle("url", delete_flag)).thenReturn(null);
		String url = forumServiceImpl.fetchArticlePostByTitle("url");
		Assert.assertEquals(null, url);
		verify(forumDao, times(1)).fetchArticlePostByTitle("url", delete_flag);
		verifyNoMoreInteractions(forumDao);
	}

	@Test
	public void test_fetchTotalAllArticleInApprovedOrder_Success() throws Exception {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();

		when(forumDao.fetchForumStatusIdByDesc(desc)).thenReturn(2);
		when(forumDao.fetchTotalAllArticleInApprovedOrder(2, delete_flag, 1)).thenReturn(1);

		int result = forumServiceImpl.fetchTotalAllArticleInApprovedOrder(1);
		Assert.assertEquals(1, result);
		verify(forumDao, times(1)).fetchForumStatusIdByDesc(desc);
		verify(forumDao, times(1)).fetchTotalAllArticleInApprovedOrder(2, delete_flag, 1);
		verifyNoMoreInteractions(forumDao);
	}

}
