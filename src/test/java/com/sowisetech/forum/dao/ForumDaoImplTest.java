package com.sowisetech.forum.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.sowisetech.admin.util.AdmTableFields;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumPostVote;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumSubCategory;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ForumDaoImplTest {

	ForumDaoImpl forumDaoImpl;
	EmbeddedDatabase db;
	@Autowired(required = true)
	@Spy
	AdvTableFields advTableFields;

	@Before
	public void setUp() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("db_sql/forumschema.sql").addScript("db_sql/forumdata.sql").build();
		forumDaoImpl = new ForumDaoImpl();
		forumDaoImpl.setDataSource(db);
		forumDaoImpl.postConstruct();
	}

	// @Test // ENCODE DECODE
	// public void test_createArticlePost_Success() {
	// ArticlePost articlePost = new ArticlePost();
	// articlePost.setContent("ccc");
	// articlePost.setPartyId(2);
	// articlePost.setForumCategoryId(1);
	// articlePost.setForumSubCategoryId(1);
	// String encryptPass = advTableFields.getEncryption_password();
	// int result = forumDaoImpl.createArticlePost(articlePost, encryptPass);
	// Assert.assertEquals(1, result);
	// }
	//
	// @Test // ENCODE DECODE
	// public void test_createArticlePost_Failure() {
	// ArticlePost articlePost = new ArticlePost();
	// articlePost.setContent("ccc");
	// articlePost.setPartyId(2);
	// // articlePost.setForumCategoryId(1);
	// // articlePost.setForumSubCategoryId(1);
	// String encryptPass = advTableFields.getEncryption_password();
	// int result = forumDaoImpl.createArticlePost(articlePost, encryptPass);
	// Assert.assertEquals(0, result);
	// }

	@Test
	public void test_fetchForumStatusIdByDesc_Success() {
		int id = forumDaoImpl.fetchForumStatusIdByDesc("Inprogress");
		Assert.assertEquals(1, id);
	}

	@Test
	public void test_fetchForumStatusIdByDesc_Failure() {
		int id = forumDaoImpl.fetchForumStatusIdByDesc("inprogress_error");
		Assert.assertEquals(0, id);
	}

	@Test
	public void test_fetchArticlePost_Success() {
		int articlePost = forumDaoImpl.fetchArticlePost(1, "N");
		Assert.assertEquals(1, articlePost);
	}

	@Test
	public void test_fetchArticlePost_Failure() {
		int articlePost = forumDaoImpl.fetchArticlePost(10, "N");
		Assert.assertEquals(0, articlePost);
	}

	@Test
	public void test_fetchForumStatus_Success() {
		String desc = forumDaoImpl.fetchForumStatus(10);
		Assert.assertEquals(null, desc);
	}

	@Test
	public void test_moderateArticlePost_Success() {
		int result = forumDaoImpl.moderateArticlePost(1, 2, "ADM0000000002", "reason", "ADM0000000002");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_moderateArticlePost_Failure() {
		int result = forumDaoImpl.moderateArticlePost(15, 2, "ADM0000000002", "reason", "ADM0000000002");
		Assert.assertEquals(0, result);
	}

	// @Test // ENCODE DECODE
	// public void test_createArticlePostComment_Success() {
	// ArticleComment articleComment = new ArticleComment();
	// articleComment.setArticleId(2);
	// articleComment.setContent("ccc");
	// articleComment.setForumStatusId(1);
	// String encryptPass = advTableFields.getEncryption_password();
	// int result = forumDaoImpl.createArticlePostComment(articleComment,
	// encryptPass);
	// Assert.assertEquals(1, result);
	// }
	//
	// @Test // ENCODE DECODE
	// public void test_createArticlePostComment_Error() {
	// ArticleComment articleComment = new ArticleComment();
	// articleComment.setArticleId(2);
	// articleComment.setContent("ccc");
	// articleComment.setForumStatusId(10);
	// String encryptPass = advTableFields.getEncryption_password();
	// int commentId = forumDaoImpl.createArticlePostComment(articleComment,
	// encryptPass);
	// Assert.assertEquals(0, commentId);
	// }

	@Test
	public void test_fetchArticleComment_Success() {
		int articleComment = forumDaoImpl.fetchArticleComment(1, "N");
		Assert.assertEquals(1, articleComment);
	}

	@Test
	public void test_fetchArticleComment_Error() {
		int articleComment = forumDaoImpl.fetchArticleComment(15, "N");
		Assert.assertEquals(0, articleComment);
	}

	@Test
	public void test_moderateArticleComment_Success() {
		int result = forumDaoImpl.moderateArticleComment(1, 2, "ADM0000000002", "ADM0000000002");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_moderateArticleComment_Error() {
		int result = forumDaoImpl.moderateArticleComment(10, 2, "ADM0000000002", "ADM0000000002");
		Assert.assertEquals(0, result);
	}

	//
	// @Test
	// public void test_createArticleVote() {
	// forumDaoImpl.createArticleVote(1, 1);
	// }

	@Test
	public void test_fetchForumSubCategory_Success() {
		ForumSubCategory forumSubCategory = forumDaoImpl.fetchForumSubCategory(1);
		ForumSubCategory forumSubCategory2 = forumDaoImpl.fetchForumSubCategory(2);
		Assert.assertEquals("equity fund", forumSubCategory.getName());
		Assert.assertEquals("index fund", forumSubCategory2.getName());
	}

	@Test
	public void test_fetchForumSubCategory_Error() {
		ForumSubCategory forumSubCategory = forumDaoImpl.fetchForumSubCategory(10);
		ForumSubCategory forumSubCategory2 = forumDaoImpl.fetchForumSubCategory(20);
		Assert.assertEquals(null, forumSubCategory);
		Assert.assertEquals(null, forumSubCategory2);
	}

	// @Test //Encode decode
	// public void test_fetchParty_Success() {
	// String encryptPass ="Sowise@Ever21";
	// Party party = forumDaoImpl.fetchParty(1,encryptPass);
	// Assert.assertEquals(1, party.getPartyStatusId());
	// Assert.assertEquals("ADV0000000001", party.getRoleBasedId());
	// }

	// @Test //Encode decode
	// public void test_fetchParty_Error() {
	// String encryptPass ="Sowise@Ever21";
	// Party party = forumDaoImpl.fetchParty(10,encryptPass);
	// Assert.assertEquals(null, party);
	// }

	// @Test //Error - Foreign Key
	// public void test_removeArticle_Success() {
	// int result = forumDaoImpl.removeArticle(1, "N", "ADM0000000002");
	// Assert.assertEquals(1, result);
	// }

	@Test
	public void test_removeArticle_Error() {
		int result = forumDaoImpl.removeArticle(10, "N", "ADM0000000002");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_removeArticleComment_Success() {
		int result = forumDaoImpl.removeArticleComment(1, "N", "ADM0000000002");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removeArticleComment_Error() {
		int result = forumDaoImpl.removeArticleComment(10, "N", "ADM0000000002");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_saveArticleVote_Success() {
		int result = forumDaoImpl.saveArticleVote(1, 1, 1, "ADV0000000002");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_saveArticleVote_Error() {
		int result = forumDaoImpl.saveArticleVote(1, 10, 1, "ADV0000000002");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchUpVoteId_Success() {
		int vote = forumDaoImpl.fetchUpVoteId("up");
		Assert.assertEquals(1, vote);
	}

	@Test
	public void test_fetchUpVoteId_Error() {
		int vote = forumDaoImpl.fetchUpVoteId("Error");
		Assert.assertEquals(0, vote);
	}

	@Test
	public void test_fetchDownVoteId_Success() {
		int vote = forumDaoImpl.fetchDownVoteId("down");
		Assert.assertEquals(2, vote);
	}

	@Test
	public void test_fetchDownVoteId_Error() {
		int vote = forumDaoImpl.fetchDownVoteId("Error");
		Assert.assertEquals(0, vote);
	}

	@Test
	public void test_fetchArticleVoteByPostId_Success() {
		ArticleVote articleVote = forumDaoImpl.fetchArticleVoteByArticleId(1);
		Assert.assertEquals(1, articleVote.getVoteId());
		Assert.assertEquals(0, articleVote.getDown_count());
	}

	@Test
	public void test_fetchArticleVoteByPostId_Error() {
		ArticleVote articleVote = forumDaoImpl.fetchArticleVoteByArticleId(10);
		Assert.assertEquals(null, articleVote);
	}

	@Test
	public void test_firstArticleVote_Success() {
		ArticleVote articleVote = new ArticleVote();
		articleVote.setDown_count(0);
		articleVote.setUp_count(1);
		articleVote.setArticleId(1);
		int vote = forumDaoImpl.firstArticleVote(articleVote);
		Assert.assertEquals(1, vote);
	}

	@Test
	public void test_firstArticleVote_Error() {
		ArticleVote articleVote = new ArticleVote();
		articleVote.setDown_count(0);
		articleVote.setUp_count(1);
		articleVote.setArticleId(15);
		int vote = forumDaoImpl.firstArticleVote(articleVote);
		Assert.assertEquals(0, vote);
	}

	@Test
	public void test_fetchUpCountByArticleId_Success() {
		int articleVote = forumDaoImpl.fetchUpCountByArticleId(1);
		Assert.assertEquals(1, articleVote);
	}

	@Test
	public void test_fetchUpCountByArticleId_Error() {
		int articleVote = forumDaoImpl.fetchUpCountByArticleId(10);
		Assert.assertEquals(0, articleVote);
	}

	@Test
	public void test_updateArticleUpVote_Success() {
		int result = forumDaoImpl.updateArticleUpVote(1, 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateArticleUpVote_Error() {
		int result = forumDaoImpl.updateArticleUpVote(1, 10);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchDownCountByArticleId_Success() {
		int count = forumDaoImpl.fetchDownCountByArticleId(2);
		Assert.assertEquals(1, count);
	}

	@Test
	public void test_fetchDownCountByArticleId_Error() {
		int count = forumDaoImpl.fetchDownCountByArticleId(10);
		Assert.assertEquals(0, count);
	}

	@Test
	public void test_updateArticleDownVote_Success() {
		int result = forumDaoImpl.updateArticleDownVote(1, 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateArticleDownVote_Error() {
		int result = forumDaoImpl.updateArticleDownVote(1, 10);
		Assert.assertEquals(0, result);
	}

	// @Test // ENCODE DECODE
	// public void test_fetchRecentArticlePostList_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchRecentArticlePostList(1, 2, "N",
	// encryptPass);
	// Assert.assertEquals(2, list.size());
	// Assert.assertEquals(2, list.get(0).getPartyId());
	// Assert.assertEquals(1, list.get(1).getPartyId());
	// }
	//
	// @Test
	// public void test_fetchRecentArticlePostList_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchRecentArticlePostList(1, 1, "NO",
	// encryptPass);
	// Assert.assertEquals(0, list.size());
	// }
	//
	// @Test
	// public void test_fetchAllForumCategory_Success() {
	// List<ForumCategory> list = forumDaoImpl.fetchAllForumCategory();
	// Assert.assertEquals(2, list.size());
	// Assert.assertEquals("Mutualfund", list.get(0).getName());
	// Assert.assertEquals("Stock", list.get(1).getName());
	// }
	//
	// @Test
	// public void test_fetchRecentApprovedArticle_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// ArticlePost articlePost = forumDaoImpl.fetchRecentApprovedArticle(2,
	// "N",encryptPass);
	// Assert.assertEquals(2, articlePost.getPartyId());
	// Assert.assertEquals("article two", articlePost.getContent());
	//
	// }
	//
	// @Test
	// public void test_fetchRecentApprovedArticle_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// ArticlePost articlePost = forumDaoImpl.fetchRecentApprovedArticle(2,
	// "N0",encryptPass);
	// Assert.assertEquals(null, articlePost);
	// }
	//
	// @Test
	// public void test_fetchAllArticleInApprovedOrder_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchAllArticleInApprovedOrder(2, 1, 1,
	// "N",encryptPass);
	// Assert.assertEquals(1, list.size());
	// Assert.assertEquals(2, list.get(0).getPartyId());
	// Assert.assertEquals("article two", list.get(0).getContent());
	// }
	//
	// @Test
	// public void test_fetchAllArticleInApprovedOrder_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchAllArticleInApprovedOrder(2, 1, 1,
	// "NO",encryptPass);
	// Assert.assertEquals(0, list.size());
	// }
	//
	// @Test
	// public void test_fetchArticleCommentByArticleId_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticleComment> articleCommentList =
	// forumDaoImpl.fetchArticleCommentByArticleId(1, 1, 1, "N",encryptPass);
	// Assert.assertEquals(1, articleCommentList.size());
	// }
	//
	// @Test
	// public void test_fetchArticleCommentByArticleId_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticleComment> articleCommentList =
	// forumDaoImpl.fetchArticleCommentByArticleId(10, 1, 1, "N",encryptPass);
	// Assert.assertEquals(0, articleCommentList.size());
	// }
	//
	// @Test
	// public void test_fetchArticlePostList_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchArticlePostList(1, 2,
	// "N",encryptPass);
	// Assert.assertEquals(2, list.size());
	// Assert.assertEquals(1, list.get(0).getPartyId());
	// Assert.assertEquals("article", list.get(0).getContent());
	// }
	//
	// @Test
	// public void test_fetchArticlePostList_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchArticlePostList(1, 2,
	// "NO",encryptPass);
	// Assert.assertEquals(0, list.size());
	// }
	//
	// @Test
	// public void test_fetchArticlePostByarticleId_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// ArticlePost article = forumDaoImpl.fetchArticlePostByarticleId(1,
	// "N",encryptPass);
	// Assert.assertEquals("article", article.getContent());
	// }
	//
	// @Test
	// public void test_fetchArticlePostByarticleId_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// ArticlePost article = forumDaoImpl.fetchArticlePostByarticleId(10,
	// "N",encryptPass);
	// Assert.assertEquals(null, article);
	// }
	//
	// @Test
	// public void test_fetchArticleListByPartyId_Success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchArticleListByPartyId(1, 1, 1,
	// "N",encryptPass);
	// Assert.assertEquals(1, list.size());
	// Assert.assertEquals(1, list.get(0).getPartyId());
	// Assert.assertEquals("article", list.get(0).getContent());
	// }
	//
	// @Test
	// public void test_fetchArticleListByPartyId_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	// List<ArticlePost> list = forumDaoImpl.fetchArticleListByPartyId(10, 1, 1,
	// "N",encryptPass);
	// Assert.assertEquals(0, list.size());
	// }

	@Test
	public void test_changeArticleStatus_Success() {
		int result = forumDaoImpl.changeArticleStatus(1, 1, "ADM0000000002");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_changeArticleStatus_Error() {
		int result = forumDaoImpl.changeArticleStatus(10, 1, "ADM0000000002");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchArticleStatusIdByDesc_Success() {
		int id = forumDaoImpl.fetchArticleStatusIdByDesc("Active");
		Assert.assertEquals(1, id);
	}

	@Test
	public void test_fetchArticleStatusIdByDesc_Failure() {
		int id = forumDaoImpl.fetchArticleStatusIdByDesc("active_error");
		Assert.assertEquals(0, id);
	}
	// Thread and Post

	@Test
	public void test_createForumThread() {
		ForumThread forumThread = new ForumThread();
		forumThread.setSubject("aaa");
		forumThread.setPartyId(1);
		int threadId = forumDaoImpl.createForumThread(forumThread);
		Assert.assertEquals(1, threadId);
	}

	@Test
	public void test_fetchThread_Success() {
		ForumThread forumThread = forumDaoImpl.fetchThread(1, "N");
		Assert.assertEquals("Question", forumThread.getSubject());
	}

	@Test
	public void test_fetchThread_Error() {
		ForumThread forumThread = forumDaoImpl.fetchThread(10, "N");
		Assert.assertEquals(null, forumThread);
	}

	@Test
	public void test_createForumPost() {
		ForumPost forumPost = new ForumPost();
		forumPost.setContent("aaa");
		forumPost.setPostId(1);
		int postId = forumDaoImpl.createForumPost(forumPost);
		Assert.assertEquals(1, postId);
	}

	@Test
	public void test_fetchPost_Success() {
		ForumPost forumPost = forumDaoImpl.fetchPost(1, "N");
		Assert.assertEquals("Answer", forumPost.getContent());
	}

	@Test
	public void test_fetchPost_Error() {
		ForumPost forumPost = forumDaoImpl.fetchPost(10, "N");
		Assert.assertEquals(null, forumPost);
	}

	@Test
	public void test_moderatePost() {
		int result = forumDaoImpl.moderatePost(1, 2, "ADM0000000001", "ADM0000000001");
		ForumPost forumPost = forumDaoImpl.fetchPost(1, "N");
		Assert.assertEquals(2, forumPost.getForumStatusId());
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_moderatePost_Error() {
		int result = forumDaoImpl.moderatePost(100, 2, "ADM00000001", "ADM0000000001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_moderateThread() {
		int result = forumDaoImpl.moderateThread(1, 2, "ADM0000000001", "ADM0000000001");
		ForumThread forumThread = forumDaoImpl.fetchThread(1, "N");
		Assert.assertEquals("ADM0000000001", forumThread.getAdminId());
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_moderateThread_Error() {
		int result = forumDaoImpl.moderateThread(100, 2, "ADM00000001", "ADM0000000001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchForumPostVoteByPostId_Success() {
		ForumPostVote forumPostVote = forumDaoImpl.fetchForumPostVoteByPostId(1);
		Assert.assertEquals(1, forumPostVote.getVoteId());
		Assert.assertEquals(0, forumPostVote.getDown_count());
	}

	@Test
	public void test_fetchForumPostVoteByPostId_Error() {
		ForumPostVote forumPostVote = forumDaoImpl.fetchForumPostVoteByPostId(100);
		Assert.assertEquals(null, forumPostVote);
	}

	@Test
	public void test_firstForumPostVote_Success() {
		ForumPostVote forumPost = new ForumPostVote();
		forumPost.setDown_count(0);
		forumPost.setUp_count(1);
		forumPost.setPostId(1);
		int vote = forumDaoImpl.firstForumPostVote(forumPost);
		Assert.assertEquals(1, vote);
	}

	@Test
	public void test_fetchUpCountByPostId_Success() {
		int articleVote = forumDaoImpl.fetchUpCountByPostId(1);
		Assert.assertEquals(1, articleVote);
	}

	@Test
	public void test_fetchUpCountByPostId_Error() {
		int articleVote = forumDaoImpl.fetchUpCountByPostId(100);
		Assert.assertEquals(0, articleVote);
	}

	@Test
	public void test_updateForumPostUpVote_Success() {
		int result = forumDaoImpl.updateForumPostUpVote(1, 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateForumPostUpVote_Error() {
		int result = forumDaoImpl.updateForumPostUpVote(1, 100);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_fetchDownCountByPostId_Success() {
		int count = forumDaoImpl.fetchDownCountByPostId(1);
		Assert.assertEquals(1, count);
	}

	@Test
	public void test_fetchDownCountByPostId_Error() {
		int count = forumDaoImpl.fetchDownCountByPostId(100);
		Assert.assertEquals(0, count);
	}

	@Test
	public void test_updateForumPostDownVote_Success() {
		int result = forumDaoImpl.updateForumPostDownVote(1, 1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_updateForumPostDownVote_Error() {
		int result = forumDaoImpl.updateForumPostDownVote(1, 100);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_savePostVote_Success() {
		int result = forumDaoImpl.savePostVote(1, 1, 1, "ADV0000000001");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removePost_Success() {
		int result = forumDaoImpl.removePost(1, "N", "ADV0000000001");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_removePost_Error() {
		int result = forumDaoImpl.removePost(10, "N", "ADV0000000001");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_createQuery() {
		ForumQuery forumQuery = new ForumQuery();
		forumQuery.setQuery("query");
		forumQuery.setPartyId(1);
		int result = forumDaoImpl.createQuery(forumQuery);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_fetchForumQueryByPostedToPartyId_Success() {
		ForumQuery query = forumDaoImpl.fetchForumQueryByPostedToPartyId(1, 1, "N");
		Assert.assertEquals("query", query.getQuery());
	}

	@Test
	public void test_fetchForumQueryByPostedToPartyId_Error() {
		ForumQuery query = forumDaoImpl.fetchForumQueryByPostedToPartyId(10, 10, "N");
		Assert.assertEquals(null, query);
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_Success() {
		Pageable pageable = PageRequest.of(0, 10);
		List<ForumQuery> query = forumDaoImpl.fetchQueryListByPostedToPartyId(1, pageable, "N");
		Assert.assertEquals(1, query.size());
	}

	@Test
	public void test_fetchQueryListByPostedToPartyId_Error() {
		Pageable pageable = PageRequest.of(0, 10);
		List<ForumQuery> query = forumDaoImpl.fetchQueryListByPostedToPartyId(1, pageable, "NO");
		Assert.assertEquals(0, query.size());
	}

	@Test
	public void test_fetchAnswerListByQueryId_Success() {
		Pageable pageable = PageRequest.of(0, 10);
		List<ForumAnswer> answer = forumDaoImpl.fetchAnswerListByQueryId(1, pageable, "N");
		Assert.assertEquals(1, answer.size());
	}

	@Test
	public void test_fetchAnswerListByQueryId_Error() {
		Pageable pageable = PageRequest.of(0, 10);
		List<ForumAnswer> answer = forumDaoImpl.fetchAnswerListByQueryId(1, pageable, "NO");
		Assert.assertEquals(0, answer.size());
	}

	@Test
	public void test_addArticleToFavorite_Success() {
		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(2);
		articleFavorite.setPartyId(1);
		int result = forumDaoImpl.addArticleToFavorite(articleFavorite);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_Success() {
		ArticleFavorite articleFavorite = new ArticleFavorite();
		articleFavorite.setArticleId(2);
		articleFavorite.setPartyId(1);
		articleFavorite.setDelete_flag("N");
		Pageable pageable = PageRequest.of(0, 10);
		forumDaoImpl.addArticleToFavorite(articleFavorite);
		List<Long> articleFavoriteList = forumDaoImpl.fetchFavoriteArticlesByPartyId(1, "N");
		Assert.assertEquals(1, articleFavoriteList.size());
	}

	@Test
	public void test_fetchFavoriteArticlesByPartyId_Error() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Long> articleFavoriteList = forumDaoImpl.fetchFavoriteArticlesByPartyId(1, "NO");
		Assert.assertEquals(0, articleFavoriteList.size());
	}

	@Test
	public void test_fetchArticleStatus_Success() {
		String desc = forumDaoImpl.fetchArticleStatus(1);
		Assert.assertEquals("Active", desc);
	}

	@Test
	public void test_fetchArticleStatus_Error() {
		String desc = forumDaoImpl.fetchArticleStatus(10);
		Assert.assertEquals(null, desc);
	}

	@Test
	public void testcheckPartyIsPresent() {
		int result = forumDaoImpl.checkPartyIsPresent(1);
		Assert.assertEquals(1, result);

	}

	@Test
	public void testcheckPartyIsPresent_Error() {
		int result = forumDaoImpl.checkPartyIsPresent(10);
		Assert.assertEquals(0, result);
	}

	@Test
	public void testcheckForumSubCategoryIsPresent() {
		int result = forumDaoImpl.checkForumSubCategoryIsPresent(1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void testcheckForumSubCategoryIsPresent_Error() {
		int result = forumDaoImpl.checkForumSubCategoryIsPresent(3);
		Assert.assertEquals(0, result);

	}

	@Test
	public void testcheckThreadIsPresent() {
		int result = forumDaoImpl.checkThreadIsPresent(1, "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void testcheckThreadIsPresent_Error() {
		int result = forumDaoImpl.checkThreadIsPresent(1, "Y");
		Assert.assertEquals(0, result);
	}

	@Test
	public void testcheckPostIsPresent() {
		int result = forumDaoImpl.checkPostIsPresent(1, "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void testcheckPostIsPresent_Error() {
		int result = forumDaoImpl.checkPostIsPresent(1, "Y");
		Assert.assertEquals(0, result);
	}

	@Test
	public void testcheckForumQueryByPostedToPartyId() {
		int result = forumDaoImpl.checkForumQueryByPostedToPartyId(1L, 1, "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void testcheckForumQueryByPostedToPartyId_Error() {
		int result = forumDaoImpl.checkForumQueryByPostedToPartyId(1L, 1, "Y");
		Assert.assertEquals(0, result);
	}

	@Test
	public void testfetchTotalArticleCommentByParentId_success() {
		int result = forumDaoImpl.fetchTotalArticleCommentByParentId(1, 1, "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void testfetchTotalArticleCommentByParentId_Error() {
		int result = forumDaoImpl.fetchTotalArticleCommentByParentId(1, 1, "Y");
		Assert.assertEquals(0, result);
	}

	@Test
	public void testfetchArticlePostByTitle_Success() {
		String result = forumDaoImpl.fetchArticlePostByTitle("fixed income", "N");
		Assert.assertEquals("fixed income", result);
	}

	@Test
	public void testfetchArticlePostByTitle_Error() {
		String result = forumDaoImpl.fetchArticlePostByTitle("fixed income", "Y");
		Assert.assertEquals(null, result);
	}

	@Test
	public void testfetchCountForUrl_Success() {
		int result = forumDaoImpl.fetchCountForUrl("fixed income");
		Assert.assertEquals(1, result);
	}

	@Test
	public void testfetchCountForUrl_Error() {
		int result = forumDaoImpl.fetchCountForUrl("new");
		Assert.assertEquals(0, result);
	}
	
	@Test
	public void testfetchTotalAllArticleInApprovedOrder() {
		int result = forumDaoImpl.fetchTotalAllArticleInApprovedOrder(2,"N",1);
		Assert.assertEquals(1, result);
	}
	
	
	// @Test // ENCODE DECODE
	// public void testfetchArticleCommentByParentId_success() {
	// String encryptPass = advTableFields.getEncryption_password();
	// int result = forumDaoImpl.fetchArticleCommentByParentId(1, 1,
	// 1,"N",encryptPass);
	// Assert.assertEquals(1, result);
	// }
	//
	// @Test
	// public void testfetchArticleCommentByParentId_Error() {
	// String encryptPass = advTableFields.getEncryption_password();
	//
	// int result = forumDaoImpl.fetchArticleCommentByParentId(1, 1,
	// 1,"Y",encryptPass);
	// Assert.assertEquals(0, result);
	// }

}
