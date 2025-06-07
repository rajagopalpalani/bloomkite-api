package com.sowisetech.forum.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sowisetech.AdvisorApplication;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.request.AdvIdRequest;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumSubCategory;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.request.ArticleCommentRequest;
import com.sowisetech.forum.request.ArticleFavoriteRequest;
import com.sowisetech.forum.request.ArticlePostRequest;
import com.sowisetech.forum.request.ArticleVoteRequest;
import com.sowisetech.forum.request.ChangeStatusRequest;
import com.sowisetech.forum.request.ForumAnswerRequest;
import com.sowisetech.forum.request.ForumPostRequest;
import com.sowisetech.forum.request.ForumPostVoteRequest;
import com.sowisetech.forum.request.ForumThreadRequest;
import com.sowisetech.forum.request.ForumIdRequest;
import com.sowisetech.forum.request.ModerateRequest;
import com.sowisetech.forum.request.QueryRequest;
import com.sowisetech.forum.service.ForumService;
import com.sowisetech.forum.util.ForumAppMessages;
import com.sowisetech.forum.util.ForumTableFields;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AdvisorApplication.class)
public class ForumControllerTest {

	@Autowired(required = true)
	@Spy
	ForumAppMessages appMessages;

	@Autowired(required = true)
	@Spy
	ForumTableFields forumTableFields;

	@InjectMocks
	private ForumController forumController;

	private MockMvc mockMvc;
	@Mock
	private ForumService forumService;
	@Autowired(required = true)
	@Spy
	ScreenRightsConstants screenRightsConstants;
	@Mock
	ScreenRightsCommon screenRightsCommon;
	@Mock
	CommonService commonService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(forumController).build();
	}

	@Test
	public void testEcv() throws Exception {
		this.mockMvc.perform(get("/forum-ecv")).andExpect(status().isOk());
	}

	// Article Test//
	@Test
	public void test_createArticlePost_Success() throws Exception {
		ArticlePostRequest articlePostRequest = new ArticlePostRequest();
		articlePostRequest.setContent("aaa");
		articlePostRequest.setPartyId(1);
		articlePostRequest.setScreenId(1);
		articlePostRequest.setTitle("url");
		int party = 1;
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.createArticlePost(Mockito.any(ArticlePost.class), Mockito.anyString())).thenReturn(1);
		when(forumService.checkPartyIsAdvisor(Mockito.anyLong())).thenReturn(true);
		Party party1 = new Party();
		party1.setRoleBasedId("ADV0000000000");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000000");
		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(party1);
		when(forumService.fetchByPublicAdvisorID(Mockito.anyString())).thenReturn(adv);
		when(forumService.fetchArticlePostByTitle(Mockito.anyString())).thenReturn("url");

		mockMvc.perform(
				MockMvcRequestBuilders.post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlepost_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createArticlePost_Mandatory() throws Exception {
		ArticlePostRequest articlePostRequest = new ArticlePostRequest();
		articlePostRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		Party party1 = new Party();
		party1.setRoleBasedId("ADV0000000000");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000000");

		mockMvc.perform(
				MockMvcRequestBuilders.post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_addArticle())))
				.andReturn();
	}

	@Test
	public void test_articlePost_ScreenRights_Success() throws Exception {
		ArticlePostRequest articlePostRequest = new ArticlePostRequest();
		articlePostRequest.setContent("aaa");
		articlePostRequest.setPartyId(1);
		articlePostRequest.setScreenId(1);
		articlePostRequest.setTitle("url");

		int party = 1;
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostRequest);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.createArticlePost(Mockito.any(ArticlePost.class), Mockito.anyString())).thenReturn(1);
		when(forumService.checkPartyIsAdvisor(Mockito.anyLong())).thenReturn(true);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		Party party1 = new Party();
		party1.setRoleBasedId("ADV0000000000");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000000");
		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(party1);
		when(forumService.fetchByPublicAdvisorID(Mockito.anyString())).thenReturn(adv);
		when(forumService.fetchArticlePostByTitle(Mockito.anyString())).thenReturn("url");
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlepost_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_articlePost_ScreenRights_AccessDenied() throws Exception {
		ArticlePostRequest screenIdRequest = new ArticlePostRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_articlePost_ScreenRights_UnAuthorized() throws Exception {
		ArticlePostRequest screenIdRequest = new ArticlePostRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createArticlePost_PartyNotFound() throws Exception {
		ArticlePostRequest articlePostRequest = new ArticlePostRequest();
		articlePostRequest.setContent("aaa");
		articlePostRequest.setPartyId(1);
		articlePostRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();
	}

	@Test
	public void test_createArticlePost_not_Advisor() throws Exception {
		ArticlePostRequest articlePostRequest = new ArticlePostRequest();
		articlePostRequest.setContent("aaa");
		articlePostRequest.setPartyId(1);
		articlePostRequest.setScreenId(1);
		int party = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		Party party1 = new Party();
		party1.setRoleBasedId("ADV0000000000");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000000");
		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(party1);
		when(forumService.fetchByPublicAdvisorID(Mockito.anyString())).thenReturn(null);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkPartyIsAdvisor(Mockito.anyLong())).thenReturn(false);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAdvisor_can_post())))
				.andReturn();
	}

	@Test
	public void test_createArticlePost_ErrorOccured() throws Exception {
		ArticlePostRequest articlePostRequest = new ArticlePostRequest();
		articlePostRequest.setContent("aaa");
		articlePostRequest.setPartyId(1);
		articlePostRequest.setScreenId(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		Party party = new Party();
		party.setRoleBasedId("ADV0000000000");
		Advisor adv = new Advisor();
		adv.setAdvId("ADV0000000000");
		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(party);
		when(forumService.fetchByPublicAdvisorID(Mockito.anyString())).thenReturn(adv);

		when(forumService.createArticlePost(Mockito.any(ArticlePost.class), Mockito.anyString())).thenReturn(0);
		when(forumService.checkPartyIsAdvisor(Mockito.anyLong())).thenReturn(true);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/articlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_deleteArticle_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticle(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticle").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages. getArticlePost_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeArticle_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticle(Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlePost_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeArticle_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		mockMvc.perform(post("/removeArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removeArticle_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_deleteArticle_ArticleNotFound() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticle").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_deleteArticle_ErrorOccured() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticle(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticle").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePost_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setArticleId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setReason("irrelevent");
		moderateRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setForumStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticlePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(1);
		when(forumService.checkForumStatusIsRejected(Mockito.anyLong())).thenReturn(false);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticle_moderated_successfully())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePost_ScreenRights_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setArticleId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setReason("irrelevent");
		moderateRequest.setScreenId(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setForumStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticlePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(1);
		when(forumService.checkForumStatusIsRejected(Mockito.anyLong())).thenReturn(false);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(put("/moderateArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticle_moderated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_moderateArticlePost_ScreenRights_AccessDenied() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/moderateArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_moderateArticlePost_ScreenRights_UnAuthorized() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(put("/moderateArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_moderateArticlePost_ArticleNotFound() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setArticleId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setReason("irrelevent");
		moderateRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setForumStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_moderateArticlePost_RejectedStatus() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setArticleId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setForumStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticlePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(1);
		when(forumService.checkForumStatusIsRejected(Mockito.anyLong())).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getRequired_reason())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePost_ErrorOccured() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setArticleId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setReason("irrelevent");
		moderateRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setForumStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticlePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(0);
		when(forumService.checkForumStatusIsRejected(Mockito.anyLong())).thenReturn(false);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_createArticleVote_Success() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setVoteType(1);
		articleVoteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.createArticleVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.saveArticleVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlevote_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_voteArticle_ScreenRights_Success() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setScreenId(1);
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setVoteType(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.createArticleVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.saveArticleVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlevote_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_voteArticle_ScreenRights_AccessDenied() throws Exception {
		ArticleVoteRequest screenIdRequest = new ArticleVoteRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_voteArticle_ScreenRights_UnAuthorized() throws Exception {
		ArticleVoteRequest screenIdRequest = new ArticleVoteRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createArticleVote_ArticleNotFound() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setVoteType(1);
		articleVoteRequest.setScreenId(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_createArticleVote_ErrorOccured() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setVoteType(1);
		articleVoteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.createArticleVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_createArticleVote_SaveErrorOccured() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setVoteType(1);
		articleVoteRequest.setScreenId(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.createArticleVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.saveArticleVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_createArticleVote_Mandatory() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/voteArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_voteArticle())))
				.andReturn();
	}

	@Test
	public void test_createArticlePostComment_Success() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setContent("aaa");
		articleCommentRequest.setPartyId(1);
		articleCommentRequest.setScreenId(1);
		articleCommentRequest.setArticleId(1);

		ArticleComment comment = new ArticleComment();
		comment.setContent("aaa");
		comment.setCommentId(1);

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.createArticlePostComment(Mockito.any(ArticleComment.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/articlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlecomment_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createArticlePostComment_Mandatory() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setContent("aaa");
		articleCommentRequest.setScreenId(1);

		ArticleComment comment = new ArticleComment();
		comment.setContent("aaa");
		comment.setCommentId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/articlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_articlePostComment())))
				.andReturn();
	}

	@Test
	public void test_articlePostComment_ScreenRights_Success() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setScreenId(1);
		articleCommentRequest.setContent("aaa");
		articleCommentRequest.setPartyId(1);
		articleCommentRequest.setArticleId(1);

		ArticleComment comment = new ArticleComment();
		comment.setContent("aaa");
		comment.setCommentId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.createArticlePostComment(Mockito.any(ArticleComment.class))).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/articlePostComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlecomment_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_articlePostComment_ScreenRights_AccessDenied() throws Exception {
		ArticleCommentRequest screenIdRequest = new ArticleCommentRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/articlePostComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_articlePostComment_ScreenRights_UnAuthorized() throws Exception {
		ArticleCommentRequest screenIdRequest = new ArticleCommentRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/articlePostComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createArticlePostComment_PartyNotFound() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setContent("aaa");
		articleCommentRequest.setPartyId(1);
		articleCommentRequest.setArticleId(1);
		articleCommentRequest.setScreenId(1);

		ArticleComment comment = new ArticleComment();
		comment.setContent("aaa");
		comment.setCommentId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/articlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();
	}

	@Test
	public void test_createArticlePostComment_ArticleNotFound() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setContent("aaa");
		articleCommentRequest.setPartyId(1);
		articleCommentRequest.setScreenId(1);
		articleCommentRequest.setArticleId(1);

		ArticleComment comment = new ArticleComment();
		comment.setContent("aaa");
		comment.setCommentId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/articlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_createArticlePostComment_ErrorOccured() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setContent("aaa");
		articleCommentRequest.setPartyId(1);
		articleCommentRequest.setScreenId(1);
		articleCommentRequest.setArticleId(1);

		ArticleComment comment = new ArticleComment();
		comment.setContent("aaa");
		comment.setCommentId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.createArticlePostComment(Mockito.any(ArticleComment.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/articlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePostComment_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setCommentId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);

		ArticleComment articleComment = new ArticleComment();
		articleComment.setCommentId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticleComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
				.thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlecomment_moderated_successfully())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePostComment_ScreenRights_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setScreenId(1);
		moderateRequest.setCommentId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");

		ArticleComment articleComment = new ArticleComment();
		articleComment.setCommentId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticleComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
				.thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(put("/moderateArticlePostComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlecomment_moderated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_moderateArticlePostComment_ScreenRights_AccessDenied() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/moderateArticlePostComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_moderateArticlePostComment_ScreenRights_UnAuthorized() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(put("/moderateArticlePostComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_moderateArticlePostComment_CommentNotFound() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setCommentId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);

		ArticleComment articleComment = new ArticleComment();
		articleComment.setCommentId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePostComment_ErrorOccured() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setCommentId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);
		ArticleComment articleComment = new ArticleComment();
		articleComment.setCommentId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderateArticleComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
				.thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_deleteArticleComment_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleComment(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_deleteArticleComment_CommentNotFound() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_removeArticleComment_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleComment(Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeArticleComment_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeArticleComment_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/removeArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_deleteArticleComment_ErrorOccured() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ArticleComment articleComment = new ArticleComment();
		articleComment.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleComment(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_fetchAllRecentArticle_Success() throws Exception {
		List<ArticlePost> articlePosts = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(2);
		articlePosts.add(articlePost);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(forumService.fetchTotalRecentArticlePostList()).thenReturn(1);
		when(forumService.fetchRecentArticlePostList()).thenReturn(articlePosts);

		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		mockMvc.perform(post("/fetchAllRecentArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].prodId", is(2))).andReturn();

	}

	@Test
	public void test_fetchAllRecentArticle_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<ArticlePost> articlePosts = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		articlePosts.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(forumService.fetchTotalRecentArticlePostList()).thenReturn(1);
		when(forumService.fetchRecentArticlePostList()).thenReturn(articlePosts);

		mockMvc.perform(post("/fetchAllRecentArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].prodId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchAllRecentArticle_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAllRecentArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAllRecentArticle_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchAllRecentArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchAllForumCategory_Success() throws Exception {
		List<ForumCategory> categoryList = new ArrayList<ForumCategory>();
		when(forumService.fetchAllForumCategory()).thenReturn(categoryList);
		ForumCategory category = new ForumCategory();
		category.setForumCategoryId(1);
		category.setName("category");
		ForumSubCategory subcategory = new ForumSubCategory();
		subcategory.setForumCategoryId(1);
		subcategory.setName("category");
		subcategory.setForumSubCategoryId(1);
		List<ForumSubCategory> subCategoryList = new ArrayList<ForumSubCategory>();
		subCategoryList.add(subcategory);
		category.setForumSubCategory(subCategoryList);
		categoryList.add(category);

		mockMvc.perform(get("/fetchAllForumCategory")).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.[0].forumCategoryId", is(1))).andReturn();
	}

	@Test
	public void test_fetchRecentApprovedArticle_Success() throws Exception {
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchRecentApprovedArticle()).thenReturn(articlePost);

		mockMvc.perform(post("/fetchRecentApprovedArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articleId", is(1)))
				.andExpect(jsonPath("$.responseData.data.adminId", is("ADM000000000A"))).andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void test_fetchRecentApprovedArticle_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		when(forumService.fetchRecentApprovedArticle()).thenReturn(articlePost);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/fetchRecentApprovedArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articleId", is(1)))
				.andExpect(jsonPath("$.responseData.data.adminId", is("ADM000000000A")))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchRecentApprovedArticle_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchRecentApprovedArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchRecentApprovedArticle_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchRecentApprovedArticle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchAllArticleInApprovedOrder_Success() throws Exception {
		List<ArticlePost> articlePosts = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		articlePosts.add(articlePost);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalAllArticleInApprovedOrder()).thenReturn(1);
		when(forumService.fetchAllArticleInApprovedOrder()).thenReturn(articlePosts);

		mockMvc.perform(
				post("/fetchAllArticleInApprovedOrder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].articleId", is(1))).andReturn();
	}

	@Test
	public void test_fetchAllArticleInApprovedOrder_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<ArticlePost> articlePosts = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("article");
		articlePost.setAdminId("ADM000000000A");
		articlePost.setProdId(1);
		articlePosts.add(articlePost);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalAllArticleInApprovedOrder()).thenReturn(1);
		when(forumService.fetchAllArticleInApprovedOrder()).thenReturn(articlePosts);

		mockMvc.perform(
				post("/fetchAllArticleInApprovedOrder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList", hasSize(1)))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].articleId", is(1))).andReturn();
	}

	@Test
	public void test_fetchAllArticleInApprovedOrder_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchAllArticleInApprovedOrder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAllArticleInApprovedOrder_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(
				post("/fetchAllArticleInApprovedOrder").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticleComment_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setArticleId(1);
		articleComment1.setPartyId(1);
		articleComment1.setCommentId(1);
		articleComment1.setForumStatusId(1);
		ArticleComment articleComment2 = new ArticleComment();
		articleComment2.setArticleId(1);
		articleComment2.setPartyId(1);
		articleComment2.setCommentId(2);
		articleComment2.setContent("This article is good.");

		articleCommentList.add(articleComment1);
		articleCommentList.add(articleComment2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleCommentByArticleId(Mockito.anyLong())).thenReturn(2);
		when(forumService.fetchArticleCommentByArticleId(Mockito.anyLong())).thenReturn(articleCommentList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articleCommentList", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.articleCommentList.[0].articleId", is(1))).andReturn();
	}

	@Test
	public void test_fetchArticleComment_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_articleComment())));
	}

	@Test
	public void test_fetchArticleComment_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setArticleId(1);
		articleComment1.setPartyId(1);
		articleComment1.setCommentId(1);
		articleComment1.setForumStatusId(1);
		ArticleComment articleComment2 = new ArticleComment();
		articleComment2.setArticleId(1);
		articleComment2.setPartyId(1);
		articleComment2.setCommentId(2);
		articleComment2.setContent("This article is good.");

		articleCommentList.add(articleComment1);
		articleCommentList.add(articleComment2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.fetchTotalArticleCommentByArticleId(Mockito.anyLong())).thenReturn(2);
		when(forumService.fetchArticleCommentByArticleId(Mockito.anyLong())).thenReturn(articleCommentList);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/fetchArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articleCommentList", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.articleCommentList.[0].articleId", is(1))).andReturn();
	}

	@Test
	public void test_fetchArticleComment_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchArticleComment_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticlePost_Success() throws Exception {
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();
		ArticlePost articlePost1 = new ArticlePost();
		articlePost1.setContent("content one");
		articlePost1.setPartyId(1);
		articlePostList.add(articlePost1);

		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticlePostList()).thenReturn(1);
		when(forumService.fetchArticlePostList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(articlePostList);
		mockMvc.perform(post("/fetchAllArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1))).andReturn();
	}

	@Test
	public void test_fetchAllArticlePost_ScreenRights_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();
		ArticlePost articlePost1 = new ArticlePost();
		articlePost1.setContent("content one");
		articlePost1.setPartyId(1);
		articlePostList.add(articlePost1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticlePostList()).thenReturn(1);
		when(forumService.fetchArticlePostList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(articlePostList);

		mockMvc.perform(post("/fetchAllArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchAllArticlePost_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAllArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAllArticlePost_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchAllArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticlePostByArticleId_Success() throws Exception {
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("content one");

		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePostByarticleId(Mockito.anyLong())).thenReturn(articlePost);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticlePostByArticleId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articleId", is(1))).andReturn();
	}

	@Test
	public void test_fetchArticlePostByArticleId_Mandatory() throws Exception {
		ArticlePost articlePost = new ArticlePost();

		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticlePostByArticleId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_articlePost_articleId())));
	}

	@Test
	public void test_fetchArticlePostByArticleId_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setContent("content one");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.fetchArticlePostByarticleId(Mockito.anyLong())).thenReturn(articlePost);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(
				post("/fetchArticlePostByArticleId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articleId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_fetchArticlePostByArticleId_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchArticlePostByArticleId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchArticlePostByArticleId_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(
				post("/fetchArticlePostByArticleId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticleListByForumStatusId_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("content one");
		articlePost.setPartyId(1);

		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleListByStatusId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchArticleListByStatusId(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(articlePostList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleListByForumStatusId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1))).andReturn();
	}

	@Test
	public void test_fetchArticleListByForumStatusId_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);

		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleListByForumStatusId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_forumStatusId())));
	}

	@Test
	public void test_fetchArticleListByForumStatusId_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("content one");
		articlePost.setPartyId(1);

		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleListByStatusId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchArticleListByStatusId(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(articlePostList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleListByForumStatusId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1))).andReturn();
	}

	@Test
	public void test_fetchArticleListByForumStatusId_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchArticleListByForumStatusId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchArticleListByForumStatusId_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(
				post("/fetchArticleListByForumStatusId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticleListByPartyId_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("content one");
		articlePost.setPartyId(1);

		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleListByPartyId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchArticleListByPartyId(Mockito.anyLong())).thenReturn(articlePostList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleListBypartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1))).andReturn();
	}

	@Test
	public void test_fetchArticleListByPartyId_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);

		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();

		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleListBypartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_fetchArticleList_By_PartyId())));
	}

	@Test
	public void test_fetchArticleListBypartyId_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchArticleListBypartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchArticleListBypartyId_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		List<ArticlePost> articlePostList = new ArrayList<ArticlePost>();

		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("content one");
		articlePost.setPartyId(1);

		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleListByPartyId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchArticleListByPartyId(Mockito.anyLong())).thenReturn(articlePostList);

		mockMvc.perform(post("/fetchArticleListBypartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].content", is("content one")))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchArticleListBypartyId_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchArticleListBypartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_changeArticleStatus_Success() throws Exception {
		ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
		changeStatusRequest.setArticleId(1);
		changeStatusRequest.setArticleStatusId(1);
		changeStatusRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(changeStatusRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.changeArticleStatus(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.put("/changeArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChanged_articlestatus_successfully())))
				.andReturn();

	}

	@Test
	public void test_changeArticleStatus_Mandatory() throws Exception {
		ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
		changeStatusRequest.setArticleStatusId(1);
		changeStatusRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(changeStatusRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/changeArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_articleId_articleStatusId())))
				.andReturn();

	}

	@Test
	public void test_changeArticleStatus_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/changeArticleStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_changeArticleStatus_ScreenRights_Success() throws Exception {
		ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
		changeStatusRequest.setArticleId(1);
		changeStatusRequest.setArticleStatusId(1);
		changeStatusRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(changeStatusRequest);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.changeArticleStatus(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(put("/changeArticleStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getChanged_articlestatus_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_changeArticleStatus_ScreenRights_UnAuthorized() throws Exception {
		ChangeStatusRequest screenIdRequest = new ChangeStatusRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		mockMvc.perform(put("/changeArticleStatus").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_changeArticleStatus_NotFound() throws Exception {
		ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
		changeStatusRequest.setArticleId(1);
		changeStatusRequest.setArticleStatusId(1);
		changeStatusRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(changeStatusRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(0);
		when(forumService.changeArticleStatus(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.put("/changeArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_changeArticleStatus_ErrorOccured() throws Exception {
		ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
		changeStatusRequest.setArticleId(1);
		changeStatusRequest.setArticleStatusId(1);
		changeStatusRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(changeStatusRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.changeArticleStatus(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.put("/changeArticleStatus").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();

	}

	@Test
	public void test_createForumThread_Success() throws Exception {
		ForumThreadRequest forumThreadRequest = new ForumThreadRequest();
		forumThreadRequest.setSubject("aaa");
		forumThreadRequest.setForumCategoryId(1);
		forumThreadRequest.setForumSubCategoryId(1);
		forumThreadRequest.setPartyId(1);
		forumThreadRequest.setScreenId(1);

		int party = 1;
		int forumSubCategory = 1;
		ForumThread forumThread = new ForumThread();
		forumThread.setForumStatusId(1);
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumThreadRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createThread(Mockito.any(ForumThread.class))).thenReturn(1);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumthread_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createForumThread_ScreenRights_Success() throws Exception {
		ForumThreadRequest forumThreadRequest = new ForumThreadRequest();
		forumThreadRequest.setSubject("aaa");
		forumThreadRequest.setForumCategoryId(1);
		forumThreadRequest.setForumSubCategoryId(1);
		forumThreadRequest.setPartyId(1);
		forumThreadRequest.setScreenId(1);

		int party = 1;
		int forumSubCategory = 1;
		ForumThread forumThread = new ForumThread();
		forumThread.setForumStatusId(1);
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumThreadRequest);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createThread(Mockito.any(ForumThread.class))).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumthread_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_createForumThread_ScreenRights_AccessDenied() throws Exception {
		ForumThreadRequest screenIdRequest = new ForumThreadRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createForumThread_ScreenRights_UnAuthorized() throws Exception {
		ForumThreadRequest screenIdRequest = new ForumThreadRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createForumThread_NotFound() throws Exception {
		ForumThreadRequest forumThreadRequest = new ForumThreadRequest();
		forumThreadRequest.setSubject("aaa");
		forumThreadRequest.setForumCategoryId(1);
		forumThreadRequest.setForumSubCategoryId(1);
		forumThreadRequest.setPartyId(1);
		forumThreadRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setParentPartyId("ADV0000000001");
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumSubCategoryId(1);
		ForumThread forumThread = new ForumThread();
		forumThread.setForumStatusId(1);
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumThreadRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(null);
		when(forumService.fetchForumSubCategory(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createThread(Mockito.any(ForumThread.class))).thenReturn(1);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();
	}

	@Test
	public void test_createAddThread_Mandatory() throws Exception {
		ForumThreadRequest forumThreadRequest = new ForumThreadRequest();
		forumThreadRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumThreadRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_add_thread())))
				.andReturn();
	}

	@Test
	public void test_createForumThread_ForumNotFound() throws Exception {
		ForumThreadRequest forumThreadRequest = new ForumThreadRequest();
		forumThreadRequest.setSubject("aaa");
		forumThreadRequest.setForumCategoryId(1);
		forumThreadRequest.setForumSubCategoryId(1);
		forumThreadRequest.setPartyId(1);
		forumThreadRequest.setScreenId(1);
		int party = 1;
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumSubCategoryId(1);
		ForumThread forumThread = new ForumThread();
		forumThread.setForumStatusId(1);
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumThreadRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(0);
		when(forumService.createThread(Mockito.any(ForumThread.class))).thenReturn(1);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumsubcategory_not_found())))
				.andReturn();
	}

	@Test
	public void test_createForumThread_ErrorOccured() throws Exception {
		ForumThreadRequest forumThreadRequest = new ForumThreadRequest();
		forumThreadRequest.setSubject("aaa");
		forumThreadRequest.setForumCategoryId(1);
		forumThreadRequest.setForumSubCategoryId(1);
		forumThreadRequest.setPartyId(1);
		forumThreadRequest.setScreenId(1);

		int party = 1;
		int forumSubCategory = 1;
		ForumThread forumThread = new ForumThread();
		forumThread.setForumStatusId(1);
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumThreadRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createThread(Mockito.any(ForumThread.class))).thenReturn(0);
		mockMvc.perform(post("/addThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_moderateForumThread_Success() throws Exception {

		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setThreadId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM0000000001");
		moderateRequest.setScreenId(1);
		int forumThread = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.moderateThread(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getThread_moderated_successfully())))
				.andReturn();
	}

	@Test
	public void test_moderateForumThread_ScreenRights_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setThreadId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM0000000001");
		moderateRequest.setScreenId(1);

		int forumThread = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.moderateThread(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(put("/moderateThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getThread_moderated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_moderateForumThread_ScreenRights_AccessDenied() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/moderateThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_moderateForumThread_ScreenRights_UnAuthorized() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(put("/moderateThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_moderateForumThread_NotFound() throws Exception {

		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setThreadId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM0000000001");
		moderateRequest.setScreenId(1);
		ForumThread forumThread = new ForumThread();
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchThread(Mockito.anyLong())).thenReturn(null);
		when(forumService.moderateThread(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_moderateForumThread_Empty() throws Exception {

		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/moderateThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_moderat_thread())))
				.andReturn();
	}

	@Test
	public void test_moderateForumThread_ErrorOccurred() throws Exception {

		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setThreadId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM0000000001");
		moderateRequest.setScreenId(1);
		int forumThread = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.moderateThread(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/moderateThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_deleteThread_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		int forumThread = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.removeThread(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_deleteThread_ScreenRights_AccessDenied() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_deleteThread_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		int forumThread = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.removeThread(Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeThread_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		mockMvc.perform(post("/removeThread").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_deleteThread_NotFound() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		ForumThread forumThread = new ForumThread();
		forumThread.setThreadId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchThread(Mockito.anyLong())).thenReturn(null);
		when(forumService.removeThread(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_deleteThread_ErrorOccurred() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		int forumThread = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.removeThread(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_createForumPost_Success() throws Exception {

		ForumPostRequest forumPostRequest = new ForumPostRequest();
		forumPostRequest.setThreadId(1);
		forumPostRequest.setContent("aaa");
		forumPostRequest.setPartyId(1);
		forumPostRequest.setScreenId(1);

		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		forumPost.setContent("aaa");
		forumPost.setPartyId(1);
		int forumThread = 1;
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.createPost(Mockito.any(ForumPost.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumpost_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createForumPost_ScreenRights_Success() throws Exception {
		ForumPostRequest forumPostRequest = new ForumPostRequest();
		forumPostRequest.setThreadId(1);
		forumPostRequest.setContent("aaa");
		forumPostRequest.setPartyId(1);
		forumPostRequest.setScreenId(1);
		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		forumPost.setContent("aaa");
		forumPost.setPartyId(1);
		int forumThread = 1;
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostRequest);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.createPost(Mockito.any(ForumPost.class))).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumpost_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_createForumPost_ScreenRights_AccessDenied() throws Exception {
		ForumPostRequest screenIdRequest = new ForumPostRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createForumPost_ScreenRights_UnAuthorized() throws Exception {
		ForumPostRequest screenIdRequest = new ForumPostRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createForumPost_NotFound() throws Exception {

		ForumPostRequest forumPostRequest = new ForumPostRequest();
		forumPostRequest.setThreadId(1);
		forumPostRequest.setContent("aaa");
		forumPostRequest.setPartyId(1);
		forumPostRequest.setScreenId(1);

		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		forumPost.setContent("aaa");
		forumPost.setPartyId(1);
		ForumThread forumThread = new ForumThread();
		forumThread.setThreadId(1);
		Party party = new Party();
		party.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(null);
		when(forumService.fetchThread(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.createPost(Mockito.any(ForumPost.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();
	}

	@Test
	public void test_createForumPost_ThreadNotFound() throws Exception {

		ForumPostRequest forumPostRequest = new ForumPostRequest();
		forumPostRequest.setThreadId(1);
		forumPostRequest.setContent("aaa");
		forumPostRequest.setPartyId(1);
		forumPostRequest.setScreenId(1);

		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		forumPost.setContent("aaa");
		forumPost.setPartyId(1);
		ForumThread forumThread = new ForumThread();
		forumThread.setThreadId(1);
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(0);
		when(forumService.createPost(Mockito.any(ForumPost.class))).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_createForumPost_ErrorOccurred() throws Exception {

		ForumPostRequest forumPostRequest = new ForumPostRequest();
		forumPostRequest.setThreadId(1);
		forumPostRequest.setContent("aaa");
		forumPostRequest.setPartyId(1);
		forumPostRequest.setScreenId(1);

		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		forumPost.setContent("aaa");
		forumPost.setPartyId(1);
		int forumThread = 1;
		int party = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkThreadIsPresent(Mockito.anyLong())).thenReturn(forumThread);
		when(forumService.createPost(Mockito.any(ForumPost.class))).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/addPost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_moderateForumPost_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setPostId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);

		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPost_moderated_successfully())))
				.andReturn();

	}

	@Test
	public void test_moderatePost_ScreenRights_Success() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setPostId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getPost_moderated_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_moderatePost_ScreenRights_AccessDenied() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_moderatePost_ScreenRights_UnAuthorized() throws Exception {
		ModerateRequest screenIdRequest = new ModerateRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_moderateForumPost_NotFound() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setPostId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);
		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchPost(Mockito.anyLong())).thenReturn(null);
		when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();

	}

	@Test
	public void test_moderateForumPost_Empty() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_moderat_post())))
				.andReturn();

	}

	@Test
	public void test_moderateForumPost_ErrorOccurred() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setPostId(1);
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();

	}

	@Test
	public void test_createPostVote_Success() throws Exception {
		ForumPostVoteRequest forumPostVoteRequest = new ForumPostVoteRequest();
		forumPostVoteRequest.setPostId(1);
		forumPostVoteRequest.setPartyId(1);
		forumPostVoteRequest.setVoteType(1);
		forumPostVoteRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.createForumPostVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.savePostVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/forumPostVote").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumvote_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createPostVote_ScreenRights_Success() throws Exception {

		ForumPostVoteRequest forumPostVoteRequest = new ForumPostVoteRequest();
		forumPostVoteRequest.setPostId(1);
		forumPostVoteRequest.setPartyId(1);
		forumPostVoteRequest.setVoteType(1);
		forumPostVoteRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostVoteRequest);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.createForumPostVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.savePostVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/forumPostVote").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumvote_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_createPostVote_ScreenRights_AccessDenied() throws Exception {
		ForumPostVoteRequest screenIdRequest = new ForumPostVoteRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/forumPostVote").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createPostVote_ScreenRights_UnAuthorized() throws Exception {
		ForumPostVoteRequest screenIdRequest = new ForumPostVoteRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/forumPostVote").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createPostVote_NotFound() throws Exception {
		ForumPostVoteRequest forumPostVoteRequest = new ForumPostVoteRequest();
		forumPostVoteRequest.setPostId(1);
		forumPostVoteRequest.setPartyId(1);
		forumPostVoteRequest.setVoteType(1);
		forumPostVoteRequest.setScreenId(1);
		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchPost(Mockito.anyLong())).thenReturn(null);
		when(forumService.createForumPostVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.savePostVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/forumPostVote").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_createPostVote_VoteTypeMandatory() throws Exception {
		ForumPostVoteRequest forumPostVoteRequest = new ForumPostVoteRequest();
		forumPostVoteRequest.setScreenId(1);

		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/forumPostVote").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_forumPostVote())))
				.andReturn();
	}

	@Test
	public void test_createPostVote_CreateVoteError() throws Exception {
		ForumPostVoteRequest forumPostVoteRequest = new ForumPostVoteRequest();
		forumPostVoteRequest.setPostId(1);
		forumPostVoteRequest.setPartyId(1);
		forumPostVoteRequest.setVoteType(1);
		forumPostVoteRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.createForumPostVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);
		when(forumService.savePostVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/forumPostVote").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_createPostVote_SaveVoteError() throws Exception {
		ForumPostVoteRequest forumPostVoteRequest = new ForumPostVoteRequest();
		forumPostVoteRequest.setPostId(1);
		forumPostVoteRequest.setPartyId(1);
		forumPostVoteRequest.setVoteType(1);
		forumPostVoteRequest.setScreenId(1);

		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumPostVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.createForumPostVote(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(forumService.savePostVote(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/forumPostVote").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_deletePost() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.removePost(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andReturn();
	}

	@Test
	public void test_deletePost_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		int forumPost = 1;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.removePost(Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_deletePost_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_deletePost_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);

		mockMvc.perform(post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_deletePost_NotFound() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		forumPost.setContent("aaa");

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchPost(Mockito.anyLong())).thenReturn(null);
		when(forumService.removePost(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getNo_record_found())))
				.andReturn();
	}

	@Test
	public void test_deletePost_ErrorOccurred() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPostIsPresent(Mockito.anyLong())).thenReturn(forumPost);
		when(forumService.removePost(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_createForumQuery_Success() throws Exception {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setQuery("query");
		queryRequest.setForumCategoryId(1);
		queryRequest.setForumSubCategoryId(1);
		queryRequest.setPartyId(1);
		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);
		queryRequest.setPostedToPartyId(postedTo);
		queryRequest.setScreenId(1);

		int party = 1;
		int forumSubCategory = 1;
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createQuery(Mockito.anyList())).thenReturn(1);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumthread_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createForumQuery_ScreenRights_Success() throws Exception {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setQuery("query");
		queryRequest.setForumCategoryId(1);
		queryRequest.setForumSubCategoryId(1);
		queryRequest.setPartyId(1);
		queryRequest.setScreenId(1);
		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);
		queryRequest.setPostedToPartyId(postedTo);

		int party = 1;
		int forumSubCategory = 1;
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createQuery(Mockito.anyList())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumthread_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_createForumQuery_ScreenRights_AccessDenied() throws Exception {
		QueryRequest screenIdRequest = new QueryRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createForumQuery_ScreenRights_UnAuthorized() throws Exception {
		QueryRequest screenIdRequest = new QueryRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createForumQuery_NotFound() throws Exception {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setQuery("query");
		queryRequest.setForumCategoryId(1);
		queryRequest.setForumSubCategoryId(1);
		queryRequest.setPartyId(1);
		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);
		queryRequest.setPostedToPartyId(postedTo);
		queryRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		party.setParentPartyId("ADV0000000001");
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumSubCategoryId(1);
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(null);
		when(forumService.fetchForumSubCategory(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createQuery(Mockito.anyList())).thenReturn(1);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();
	}

	@Test
	public void test_createForumQuery_Mandatory() throws Exception {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setScreenId(1);
		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);
		// queryRequest.setPostedToPartyId(postedTo);

		int party = 1;
		int forumSubCategory = 1;
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_createQuery())))
				.andReturn();
	}

	@Test
	public void test_createForumQuery_ForumNotFound() throws Exception {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setQuery("query");
		queryRequest.setForumCategoryId(1);
		queryRequest.setForumSubCategoryId(1);
		queryRequest.setPartyId(1);
		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);
		queryRequest.setPostedToPartyId(postedTo);
		queryRequest.setScreenId(1);

		int party = 1;
		ForumSubCategory forumSubCategory = new ForumSubCategory();
		forumSubCategory.setForumSubCategoryId(1);
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(0);
		when(forumService.createQuery(Mockito.anyList())).thenReturn(1);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumsubcategory_not_found())))
				.andReturn();
	}

	@Test
	public void test_createForumQuery_ErrorOccurred() throws Exception {
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setQuery("query");
		queryRequest.setForumCategoryId(1);
		queryRequest.setForumSubCategoryId(1);
		queryRequest.setPartyId(1);
		List<Long> postedTo = new ArrayList<>();
		postedTo.add(1L);
		queryRequest.setPostedToPartyId(postedTo);
		queryRequest.setScreenId(1);

		int party = 1;
		int forumSubCategory = 1;
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(queryRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumSubCategoryIsPresent(Mockito.anyLong())).thenReturn(forumSubCategory);
		when(forumService.createQuery(Mockito.anyList())).thenReturn(0);
		mockMvc.perform(post("/createQuery").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_createForumAnswer_Success() throws Exception {
		ForumAnswerRequest answerRequest = new ForumAnswerRequest();
		answerRequest.setQueryId(1);
		answerRequest.setPartyId(1);
		answerRequest.setAnswer("answer");
		answerRequest.setPartyId(1);
		answerRequest.setScreenId(1);

		int party = 1;

		int forumQuery = 1;
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(answerRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumQueryByPostedToPartyId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(forumQuery);
		when(forumService.createForumAnswer(Mockito.any(ForumAnswer.class))).thenReturn(1);
		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumpost_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_createForumAnswer_ScreenRights_Success() throws Exception {
		ForumAnswerRequest answerRequest = new ForumAnswerRequest();
		answerRequest.setScreenId(1);
		answerRequest.setQueryId(1);
		answerRequest.setPartyId(1);
		answerRequest.setAnswer("answer");
		answerRequest.setPartyId(1);

		int party = 1;

		int forumQuery = 1;
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(answerRequest);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumQueryByPostedToPartyId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(forumQuery);
		when(forumService.createForumAnswer(Mockito.any(ForumAnswer.class))).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getForumpost_added_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_createForumAnswer_ScreenRights_AccessDenied() throws Exception {
		ForumAnswerRequest screenIdRequest = new ForumAnswerRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_createForumAnswer_ScreenRights_UnAuthorized() throws Exception {
		ForumAnswerRequest screenIdRequest = new ForumAnswerRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_createForumAnswer_NotFound() throws Exception {
		ForumAnswerRequest answerRequest = new ForumAnswerRequest();
		answerRequest.setQueryId(1);
		answerRequest.setPartyId(1);
		answerRequest.setAnswer("answer");
		answerRequest.setPartyId(1);
		answerRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);
		party.setParentPartyId("ADV0000000001");

		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(answerRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchParty(Mockito.anyLong())).thenReturn(null);
		when(forumService.fetchForumQueryByPostedToPartyId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(forumQuery);
		when(forumService.createForumAnswer(Mockito.any(ForumAnswer.class))).thenReturn(1);
		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();
	}

	@Test
	public void test_createForumAnswer_Mandatory() throws Exception {
		ForumAnswerRequest answerRequest = new ForumAnswerRequest();
		answerRequest.setScreenId(1);

		Party party = new Party();
		party.setPartyId(1);
		party.setParentPartyId("ADV0000000001");

		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setForumSubCategoryId(1);
		forumQuery.setQueryId(1);
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(answerRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_createAnswer())))
				.andReturn();
	}

	@Test
	public void test_createForumAnswer_ErrorOccurred() throws Exception {
		ForumAnswerRequest answerRequest = new ForumAnswerRequest();
		answerRequest.setQueryId(1);
		answerRequest.setPartyId(1);
		answerRequest.setAnswer("answer");
		answerRequest.setPartyId(1);
		answerRequest.setScreenId(1);

		int party = 1;

		int forumQuery = 1;
		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setQueryId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(answerRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.checkForumQueryByPostedToPartyId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(forumQuery);
		when(forumService.createForumAnswer(Mockito.any(ForumAnswer.class))).thenReturn(0);
		mockMvc.perform(post("/createAnswer").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		List<ForumQuery> queryList = new ArrayList<ForumQuery>();

		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setQuery("query");
		forumQuery.setPartyId(1);

		queryList.add(forumQuery);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalQueryListByPostedToPartyId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchQueryListByPostedToPartyId(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(queryList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchQueryListByPostedToPartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.forumQueryList.[0].query", is("query")))
				.andExpect(jsonPath("$.responseData.data.forumQueryList.[0].partyId", is(1))).andReturn();
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		idRequest.setId(1);

		List<ForumQuery> queryList = new ArrayList<ForumQuery>();

		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setQuery("query");
		forumQuery.setPartyId(1);

		queryList.add(forumQuery);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalQueryListByPostedToPartyId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchQueryListByPostedToPartyId(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(queryList);

		mockMvc.perform(
				post("/fetchQueryListByPostedToPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.forumQueryList.[0].query", is("query")))
				.andExpect(jsonPath("$.responseData.data.forumQueryList.[0].partyId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchQueryListByPostedToPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(
				post("/fetchQueryListByPostedToPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_ScreenRights() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchQueryListByPostedToPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAnswerListByQueryId_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		List<ForumAnswer> answerList = new ArrayList<ForumAnswer>();

		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setPartyId(1);

		answerList.add(forumAnswer);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalAnswerListByQueryId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchAnswerListByQueryId(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(answerList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAnswerListByQueryId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.forumAnswerList.[0].answer", is("answer")))
				.andExpect(jsonPath("$.responseData.data.forumAnswerList.[0].partyId", is(1))).andReturn();
	}

	@Test
	public void test_fetchAnswerListByQueryId_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();

		idRequest.setScreenId(1);

		List<ForumAnswer> answerList = new ArrayList<ForumAnswer>();

		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setPartyId(1);

		answerList.add(forumAnswer);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchAnswerListByQueryId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_queryId())));

	}

	@Test
	public void test_fetchAnswerListByQueryId_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		idRequest.setId(1);

		List<ForumAnswer> answerList = new ArrayList<ForumAnswer>();

		ForumAnswer forumAnswer = new ForumAnswer();
		forumAnswer.setAnswer("answer");
		forumAnswer.setPartyId(1);

		answerList.add(forumAnswer);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalAnswerListByQueryId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchAnswerListByQueryId(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(answerList);

		mockMvc.perform(post("/fetchAnswerListByQueryId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.forumAnswerList.[0].answer", is("answer")))
				.andExpect(jsonPath("$.responseData.data.forumAnswerList.[0].partyId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchAnswerListByQueryId_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchAnswerListByQueryId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchAnswerListByQueryId_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchAnswerListByQueryId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_addArticleToFavorite_Success() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setArticleId(1);
		articleFavoriteRequest.setPartyId(1);
		articleFavoriteRequest.setScreenId(1);
		int party = 1;
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.fetchArticlePostByarticleId(Mockito.anyLong())).thenReturn(articlePost);

		when(forumService.addArticleToFavorite(Mockito.any(ArticleFavorite.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addArticleToFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFavorite_added_successfully())))
				.andReturn();
	}

	@Test
	public void test_addArticleToFavorite_OwnArticle() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setArticleId(1);
		articleFavoriteRequest.setPartyId(1);
		articleFavoriteRequest.setScreenId(1);
		int party = 1;
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);
		articlePost.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.fetchArticlePostByarticleId(Mockito.anyLong())).thenReturn(articlePost);

		when(forumService.addArticleToFavorite(Mockito.any(ArticleFavorite.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/addArticleToFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getCant_add_own_article_as_favorite())))
				.andReturn();
	}

	@Test
	public void test_addArticleToFavorite_Mandatory() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/addArticleToFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_addArticleFavorite())))
				.andReturn();
	}

	@Test
	public void test_addArticleToFavorite_ErrorOccurred() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setArticleId(1);
		articleFavoriteRequest.setPartyId(1);
		articleFavoriteRequest.setScreenId(1);
		int party = 1;
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(party);
		when(forumService.fetchArticlePostByarticleId(Mockito.anyLong())).thenReturn(articlePost);

		when(forumService.addArticleToFavorite(Mockito.any(ArticleFavorite.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/addArticleToFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeArticleLike_Success() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.decreaseLikeCount(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleVoteAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleLike").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlevote_removed_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeArticleLike_ErrorOccurred() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.decreaseLikeCount(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleVoteAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleLike").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_removeArticleLikeDecrease_ErrorOccurred() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.decreaseLikeCount(Mockito.anyLong())).thenReturn(0);
		when(forumService.removeArticleVoteAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(0);

		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleLike").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured_remove())))
				.andReturn();
	}

	@Test
	public void test_removeArticleLike_Mandatory() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleLike").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_removeArticleLike())))
				.andReturn();
	}

	@Test
	public void test_removeArticleLike_ScreenRights_Success() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);

		when(forumService.decreaseLikeCount(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleVoteAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleLike").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getArticlevote_removed_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeArticleLike_ScreenRights_AccessDenied() throws Exception {
		ArticleVoteRequest screenIdRequest = new ArticleVoteRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeArticleLike").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeArticleLike_ScreenRights_UnAuthorized() throws Exception {
		ArticleVoteRequest screenIdRequest = new ArticleVoteRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/removeArticleLike").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_removeArticleFromFavorite_Success() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setArticleId(1);
		articleFavoriteRequest.setPartyId(1);
		articleFavoriteRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.removeArticleFromFavorite(Mockito.any(ArticleFavorite.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleFromFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFavorite_removed_successfully())))
				.andReturn();
	}

	@Test
	public void test_removeArticleFromFavorite_EmptyFields() throws Exception {

		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleFromFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_removeArticleFavorite())))
				.andReturn();
	}

	@Test
	public void test_removeArticleFromFavorite_ErrorOccurred() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setArticleId(1);
		articleFavoriteRequest.setPartyId(1);
		articleFavoriteRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.removeArticleFromFavorite(Mockito.any(ArticleFavorite.class))).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleFromFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();
	}

	@Test
	public void test_removeArticleFromFavorite_ScreenRights_Success() throws Exception {
		ArticleFavoriteRequest articleFavoriteRequest = new ArticleFavoriteRequest();
		articleFavoriteRequest.setArticleId(1);
		articleFavoriteRequest.setPartyId(1);
		articleFavoriteRequest.setScreenId(1);
		Party party = new Party();
		party.setPartyId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setContent("aaa");
		articlePost.setArticleId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleFavoriteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.removeArticleFromFavorite(Mockito.any(ArticleFavorite.class))).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticleFromFavorite").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getFavorite_removed_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_removeArticleFromFavorite_ScreenRights_AccessDenied() throws Exception {
		ArticleFavoriteRequest screenIdRequest = new ArticleFavoriteRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/removeArticleFromFavorite").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_removeArticleFromFavorite_ScreenRights_UnAuthorized() throws Exception {
		ArticleFavoriteRequest screenIdRequest = new ArticleFavoriteRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/removeArticleFromFavorite").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		idRequest.setId(1);
		List<ArticleFavorite> articleFavoriteList = new ArrayList<>();
		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(1);
		articleFavorite.setPartyId(1);
		articleFavoriteList.add(articleFavorite);
		List<ArticlePost> articlePostList = new ArrayList<>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setArticleStatus("Active");
		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalFavoriteArticlesByPartyId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchFavoriteArticlesByPartyId(Mockito.anyLong())).thenReturn(articlePostList);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchFavoriteArticlesByPartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		List<ArticleFavorite> articleFavoriteList = new ArrayList<>();
		List<ArticlePost> articlePostList = new ArrayList<>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setArticleStatus("Active");
		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchFavoriteArticlesByPartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_FavoriteArticlesByPartyId())))
				.andReturn();
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_ScreenRights_Success() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		idRequest.setId(1);
		List<ArticleFavorite> articleFavoriteList = new ArrayList<>();
		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(1);
		articleFavorite.setPartyId(1);
		articleFavoriteList.add(articleFavorite);
		List<ArticlePost> articlePostList = new ArrayList<>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setArticleId(1);
		articlePost.setPartyId(1);
		articlePost.setArticleStatus("Active");
		articlePostList.add(articlePost);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalFavoriteArticlesByPartyId(Mockito.anyLong())).thenReturn(1);
		when(forumService.fetchFavoriteArticlesByPartyId(Mockito.anyLong())).thenReturn(articlePostList);

		mockMvc.perform(
				post("/fetchFavoriteArticlesByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].articleId", is(1)))
				.andExpect(jsonPath("$.responseData.data.articlePostList.[0].partyId", is(1)))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(
				post("/fetchFavoriteArticlesByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(
				post("/fetchFavoriteArticlesByPartyId").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticleUpCount_Success() throws Exception {
		ForumIdRequest forumIdRequest = new ForumIdRequest();
		forumIdRequest.setId(1);
		forumIdRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumIdRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleUpCount(Mockito.anyLong())).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleUpCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchArticleUpCount_Mandatory() throws Exception {
		ForumIdRequest forumIdRequest = new ForumIdRequest();
		forumIdRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumIdRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleUpCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_ArticleUpCount())))
				.andReturn();
	}

	@Test
	public void test_fetchArticleUpCount_ScreenRights_Success() throws Exception {
		ForumIdRequest forumIdRequest = new ForumIdRequest();
		forumIdRequest.setId(1);
		forumIdRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(forumIdRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleUpCount(Mockito.anyLong())).thenReturn(1);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleUpCount").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchArticleUpCount_ScreenRights_AccessDenied() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchArticleUpCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchArticleUpCount_ScreenRights_UnAuthorized() throws Exception {
		ForumIdRequest screenIdRequest = new ForumIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchArticleUpCount").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchReplyArticleComment_Success() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setArticleId(1);
		articleCommentRequest.setParentCommentId(1);
		articleCommentRequest.setScreenId(1);

		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setArticleId(1);
		articleComment1.setPartyId(1);
		articleComment1.setCommentId(1);
		articleComment1.setForumStatusId(1);
		ArticleComment articleComment2 = new ArticleComment();
		articleComment2.setArticleId(1);
		articleComment2.setPartyId(1);
		articleComment2.setCommentId(2);
		articleComment2.setContent("This article is good.");

		articleCommentList.add(articleComment1);
		articleCommentList.add(articleComment2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleCommentByParentId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(2);
		when(forumService.fetchArticleCommentByParentId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(articleCommentList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchReplyArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articleCommentList", hasSize(2)))
				.andExpect(jsonPath("$.responseData.data.articleCommentList.[0].articleId", is(1))).andReturn();
	}

	@Test
	public void test_fetchReplyArticleComment_Error() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setArticleId(1);
		articleCommentRequest.setParentCommentId(1);
		articleCommentRequest.setScreenId(1);

		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchTotalArticleCommentByParentId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(2);
		when(forumService.fetchArticleCommentByParentId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(articleCommentList);
		mockMvc.perform(MockMvcRequestBuilders.post("/fetchReplyArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.data.articleCommentList", hasSize(0))).andReturn();
		// .andExpect(jsonPath("$.responseData.data.articleCommentList.[0].articleId",
		// is(1))).andReturn();
	}

	@Test
	public void test_searchArticleByTitle_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<ArticlePost> articlePostLists = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setForumStatusId(12);
		articlePost.setTitle("article post");

		articlePostLists.add(articlePost);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		when(forumService.fetchTotalSearchArticleByTitle(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(forumService.searchArticleByTitle(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(articlePostLists);

		mockMvc.perform(MockMvcRequestBuilders.post("/searchArticleByTitle").content(jsonString)
				.param("title", "Username").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())));
	}

	@Test
	public void test_searchArticleByTitle_Mandatory() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		List<ArticlePost> articlePostLists = new ArrayList<ArticlePost>();

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.post("/searchArticleByTitle").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_searchArticle())));
	}

	@Test
	public void test_searchArticleByTitle_ScreenRight_Success() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		int pageNum = 1;
		screenIdRequest.setScreenId(1);
		List<ArticlePost> articlePostLists = new ArrayList<ArticlePost>();
		ArticlePost articlePost = new ArticlePost();
		articlePost.setForumStatusId(12);
		articlePost.setTitle("article post");

		articlePostLists.add(articlePost);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(forumService.fetchTotalSearchArticleByTitle(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
		when(forumService.searchArticleByTitle(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(articlePostLists);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.post("/searchArticleByTitle").content(jsonString)
				.param("title", "Username").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1)));
	}

	@Test
	public void test_searchArticleByTitle_ScreenRights_AccessDenied() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/searchArticleByTitle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_searchArticleByTitle_ScreenRights_UnAuthorized() throws Exception {
		ScreenIdRequest screenIdRequest = new ScreenIdRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/searchArticleByTitle").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchArticleVoteAddress_Success() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setScreenId(1);

		ArticleVoteAddress articleVoteAddress = new ArticleVoteAddress();
		articleVoteAddress.setArticleId(1);
		articleVoteAddress.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleVoteAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(articleVoteAddress);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleVoteAddress").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess()))).andReturn();
	}

	@Test
	public void test_fetchArticleVoteAddress_Mandatory() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleVoteAddress").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_ArticleVoteAddress())))
				.andReturn();
	}

	@Test
	public void test_fetchArticleVoteAddress_ScreenRights_Success() throws Exception {
		ArticleVoteRequest articleVoteRequest = new ArticleVoteRequest();
		articleVoteRequest.setArticleId(1);
		articleVoteRequest.setPartyId(1);
		articleVoteRequest.setScreenId(1);

		ArticleVoteAddress articleVoteAddress = new ArticleVoteAddress();
		articleVoteAddress.setArticleId(1);
		articleVoteAddress.setPartyId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleVoteRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticleVoteAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(articleVoteAddress);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchArticleVoteAddress").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_fetchArticleVoteAddress_ScreenRights_AccessDenied() throws Exception {
		ArticleVoteRequest screenIdRequest = new ArticleVoteRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(post("/fetchArticleVoteAddress").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_fetchArticleVoteAddress_ScreenRights_UnAuthorized() throws Exception {
		ArticleVoteRequest screenIdRequest = new ArticleVoteRequest();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(post("/fetchArticleVoteAddress").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_fetchReplyArticleComment_Mandatory() throws Exception {
		ArticleCommentRequest articleCommentRequest = new ArticleCommentRequest();
		articleCommentRequest.setScreenId(1);

		List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();
		ArticleComment articleComment1 = new ArticleComment();
		articleComment1.setCommentId(1);
		articleComment1.setForumStatusId(1);
		ArticleComment articleComment2 = new ArticleComment();
		articleComment2.setCommentId(2);
		articleComment2.setContent("This article is good.");

		articleCommentList.add(articleComment1);
		articleCommentList.add(articleComment2);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articleCommentRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchReplyArticleComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000))).andExpect(jsonPath(
						"$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_articleId())));

	}

	@Test
	public void test_removeArticleComment_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);

		ArticleComment articleComment = new ArticleComment();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		when(forumService.fetchArticleComment(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticleComment(Mockito.anyLong())).thenReturn(1);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);
		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(post("/removeArticleComment").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getRecord_deleted_successfully())))
				.andExpect(jsonPath("$.responseData.roleFieldRights", hasSize(1))).andReturn();
	}

	@Test
	public void test_deleteArticle_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.fetchArticlePost(Mockito.anyLong())).thenReturn(1);
		when(forumService.removeArticle(Mockito.anyLong())).thenReturn(1);
		mockMvc.perform(MockMvcRequestBuilders.post("/removeArticle").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_articleId())))
				.andReturn();
	}

	@Test
	public void test_deleteThread_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/removeThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_threadId())))
				.andReturn();
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setId(1);
		idRequest.setScreenId(1);
		List<ForumQuery> queryList = new ArrayList<ForumQuery>();

		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setQuery("query");

		queryList.add(forumQuery);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.post("/fetchQueryListByPostedToPartyId").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getSuccess())));
	}

	@Test
	public void test_deletePost_Mandatory() throws Exception {
		ForumIdRequest idRequest = new ForumIdRequest();
		idRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(idRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/removePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(
						jsonPath("$.responseMessage.responseDescription", is(appMessages.getMandatory_fields_postId())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePost_Mandatory() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setReason("irrelevent");
		moderateRequest.setScreenId(1);
		ArticlePost articlePost = new ArticlePost();
		articlePost.setForumStatusId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_moderate_article())))
				.andReturn();
	}

	@Test
	public void test_moderateArticlePostComment_Mandatory() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/moderateArticlePostComment").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_moderate_comment())))
				.andReturn();
	}

	@Test
	public void test_moderateForumThread_Mandatory() throws Exception {

		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM0000000001");
		moderateRequest.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/moderateThread").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_moderat_thread())))
				.andReturn();
	}

	@Test
	public void test_moderateForumPost_Mandatory() throws Exception {
		ModerateRequest moderateRequest = new ModerateRequest();
		moderateRequest.setForumStatusId(2);
		moderateRequest.setAdminId("ADM000000000A");
		moderateRequest.setScreenId(1);

		int forumPost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(moderateRequest);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/moderatePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_moderat_post())))
				.andReturn();

	}

	// @Test //error
	// public void test_modifyArticlePost_Success() throws Exception {
	// ArticlePostRequest articlePostReq = new ArticlePostRequest();
	// articlePostReq.setPartyId(1);
	// articlePostReq.setArticleId(2);
	// articlePostReq.setScreenId(1);
	// articlePostReq.setContent("aaa");
	// int articlePost = 1;
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(articlePostReq);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	//
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(articlePost);
	// when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	// mockMvc.perform(MockMvcRequestBuilders.put("/modifyArticlePost").content(jsonString)
	// .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	// .andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getRecord_modified_successfully())))
	// .andReturn();
	// }

	// @Test //error
	// public void test_modifyArticlePost_ScreenRights_Success() throws Exception {
	// ArticlePostRequest articlePostReq = new ArticlePostRequest();
	// articlePostReq.setPartyId(1);
	// articlePostReq.setArticleId(2);
	// articlePostReq.setScreenId(1);
	// articlePostReq.setContent("aaa");
	//
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(articlePostReq);
	//
	// when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(1);
	// when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(),
	// Mockito.anyString())).thenReturn(1);
	// RoleFieldRights roleFieldRights = new RoleFieldRights();
	// roleFieldRights.setRole_screen_rights_id(1);
	// roleFieldRights.setField_id(1);
	// roleFieldRights.setRole_field_rights_id(1);
	// List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
	// roleFieldRightsList.add(roleFieldRights);
	// List<Integer> screenIds = new ArrayList<>();
	// screenIds.add(1);
	// List<Integer> rolescreenIds = new ArrayList<>();
	// rolescreenIds.add(1);
	// when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
	// when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
	// when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
	// when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
	// when(screenRightsCommon.screenRights(Mockito.anyInt(),
	// Mockito.any(HttpServletRequest.class),
	// Mockito.anyString())).thenReturn(rolescreenIds);
	//
	// mockMvc.perform(put("/modifyArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
	// .andExpect(status().isOk()).andExpect(jsonPath("$.responseMessage.responseCode",
	// is(6000)))
	// .andExpect(jsonPath("$.responseMessage.responseDescription",
	// is(appMessages.getRecord_modified_successfully())))
	// .andExpect(jsonPath("$.responseData.roleFieldRights",
	// hasSize(1))).andReturn();
	// }

	@Test
	public void test_modifyArticlePost_ScreenRights_AccessDenied() throws Exception {
		ArticlePostRequest screenIdRequest = new ArticlePostRequest();
		screenIdRequest.setScreenId(1);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(null);
		mockMvc.perform(put("/modifyArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getAccess_denied())));
	}

	@Test
	public void test_modifyArticlePost_ScreenRights_UnAuthorized() throws Exception {
		ArticlePostRequest screenIdRequest = new ArticlePostRequest();

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(screenIdRequest);
		mockMvc.perform(put("/modifyArticlePost").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getUnauthorized())));
	}

	@Test
	public void test_modifyArticlePost_NotFound() throws Exception {
		ArticlePostRequest articlePostReq = new ArticlePostRequest();
		articlePostReq.setPartyId(1);
		articlePostReq.setArticleId(2);
		articlePostReq.setContent("aaa");
		articlePostReq.setScreenId(1);
		ForumPost forumPost = new ForumPost();
		forumPost.setPostId(1);
		int articlePost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostReq);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5004)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getParty_not_found())))
				.andReturn();

	}

	@Test
	public void test_modifyArticlePost_Empty() throws Exception {
		ArticlePostRequest articlePostReq = new ArticlePostRequest();
		articlePostReq.setScreenId(1);
		int articlePost = 1;

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostReq);

		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		mockMvc.perform(MockMvcRequestBuilders.put("/modifyArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(jsonPath("$.responseMessage.responseCode", is(6000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription",
						is(appMessages.getMandatory_fields_modify_article())))
				.andReturn();

	}

	@Test
	public void test_modifyArticlePost_ErrorOccurred() throws Exception {
		ArticlePostRequest articlePostReq = new ArticlePostRequest();
		articlePostReq.setPartyId(1);
		articlePostReq.setContent("aaa");
		articlePostReq.setArticleId(2);
		articlePostReq.setScreenId(1);

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(articlePostReq);
		RoleFieldRights roleFieldRights = new RoleFieldRights();
		roleFieldRights.setRole_screen_rights_id(1);
		roleFieldRights.setField_id(1);
		roleFieldRights.setRole_field_rights_id(1);
		List<RoleFieldRights> roleFieldRightsList = new ArrayList<>();
		roleFieldRightsList.add(roleFieldRights);

		List<Integer> screenIds = new ArrayList<>();
		screenIds.add(1);
		List<Integer> rolescreenIds = new ArrayList<>();
		rolescreenIds.add(1);
		when(commonService.fetchScreenCodeByScreenId(Mockito.anyInt())).thenReturn("S1");
		when(commonService.fetchScreenIdsByStartWithScreenCode(Mockito.anyString())).thenReturn(screenIds);
		when(commonService.fetchFieldRights(Mockito.anyList())).thenReturn(roleFieldRightsList);
		when(screenRightsCommon.isAuthNeedForScreenId(Mockito.anyInt())).thenReturn(true);
		when(screenRightsCommon.screenRights(Mockito.anyInt(), Mockito.any(HttpServletRequest.class),
				Mockito.anyString())).thenReturn(rolescreenIds);

		when(forumService.checkPartyIsPresent(Mockito.anyLong())).thenReturn(1);
		when(forumService.moderatePost(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(0);
		mockMvc.perform(MockMvcRequestBuilders.put("/modifyArticlePost").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.responseMessage.responseCode", is(5000)))
				.andExpect(jsonPath("$.responseMessage.responseDescription", is(appMessages.getError_occured())))
				.andReturn();

	}

}
