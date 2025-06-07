package com.sowisetech.forum.dao;

import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.model.VoteType;
import com.sowisetech.forum.model.ForumSubCategory;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumPostVote;
import com.sowisetech.forum.model.ForumQuery;

public interface ForumDao {

	ForumSubCategory fetchForumSubCategory(long forumSubCategoryId);

	// Article

	int createArticlePost(ArticlePost articlePost, String encryptPass);

	int fetchArticlePost(long articleId, String delete_flag);

	int createArticlePostComment(ArticleComment comment, String encryptPass);

	int fetchArticleComment(long commentId, String delete_flag);

	int moderateArticleComment(long commentId, long forumStatusId, String adminId, String signedUserId);

	Party fetchParty(long partyId, String encryptPass);

	int removeArticle(long articleId, String delete_flag, String signedUserId);

	int removeArticleComment(long commentId, String delete_flag, String signedUserId);

	int saveArticleVote(long voteType, long articleId, long partyId, String signedUserId);

	int fetchForumStatusIdByDesc(String desc);

	int fetchUpVoteId(String up);

	int fetchDownVoteId(String down);

	ArticleVote fetchArticleVoteByArticleId(long articleId);

	int firstArticleVote(ArticleVote articleVote);

	int fetchUpCountByArticleId(long articleId);

	int updateArticleUpVote(int upCount, long articleId);

	int fetchDownCountByArticleId(long articleId);

	int updateArticleDownVote(int downCount, long articleId);

	// int fetchRoleIdByPartyId(long partyId);

	// int fetchRoleIdByRole(String role_advisor);

	List<ArticlePost> fetchRecentArticlePostList(String delete_flag, String encryptPass);

	List<ForumCategory> fetchAllForumCategory();

	ArticlePost fetchRecentApprovedArticle(int forumStatusId, String delete_flag, String encryptPass);

	List<ArticlePost> fetchAllArticleInApprovedOrder(int forumStatusId, int articleStatusId, String delete_flag,
			String encryptPass);

	String fetchForumStatus(long forumStatusId);

	int moderateArticlePost(long articleId, long forumStatusId, String adminId, String reason, String signedUserId);

	List<ArticleComment> fetchArticleCommentByArticleId(long articleId, String delete_flag, String encryptPass);

	List<ArticlePost> fetchArticlePostList(Pageable pageable, String delete_flag, String encryptPass);

	ArticlePost fetchArticlePostByarticleId(long articleId, String delete_flag_N, String encryptPass);

	List<ArticlePost> fetchArticleListByPartyId(long partyId, String delete_flag_N, String encryptPass);

	int changeArticleStatus(long articleId, long articleStatusId, String signedUserId);

	int fetchArticleStatusIdByDesc(String articleDesc);

	String fetchEmailIdByPartyId(long partyId, String delete_flag, String encryptPass);

	int createForumThread(ForumThread thread);

	ForumThread fetchThread(long threadId, String delete_flag);

	int moderateThread(long threadId, long forumStatusId, String adminId, String signedUserId);

	int removeThread(long threadId, String delete_flag, String signedUserId);

	int createForumPost(ForumPost fpost);

	ForumPost fetchPost(long postId, String delete_flag);

	int moderatePost(long postId, long forumStatusId, String adminId, String signedUserId);

	ForumPostVote fetchForumPostVoteByPostId(long postId);

	int firstForumPostVote(ForumPostVote forumPostUpVote);

	int fetchUpCountByPostId(long postId);

	int updateForumPostUpVote(int upCount, long postId);

	int fetchDownCountByPostId(long postId);

	int updateForumPostDownVote(int downCount, long postId);

	int savePostVote(long voteType, long postId, long partyId, String signedUserId);

	int removePost(long postId, String delete_flag, String signedUserId);

	int createQuery(ForumQuery question);

	ForumQuery fetchForumQueryByPostedToPartyId(long questionId, long partyId, String delete_flag);

	int createForumAnswer(ForumAnswer answer);

	List<ForumQuery> fetchQueryListByPostedToPartyId(long partyId, Pageable pageable, String delete_flag_N);

	List<ForumAnswer> fetchAnswerListByQueryId(long id, Pageable pageable, String delete_flag_N);

	List<VoteType> fetchVoteTypeList();

	Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass);

	int addArticleToFavorite(ArticleFavorite articleFavorite);

	List<Long> fetchFavoriteArticlesByPartyId(long partyId, String delete_flag_N);

	int fetchTotalRecentArticlePostList(String delete_flag);

	int fetchTotalAllArticleInApprovedOrder(int forumStatusId, String delete_flag);

	int fetchTotalArticlePostList(String delete_flag);

	int removeArticleFromFavorite(ArticleFavorite articleFavorite);

	int fetchTotalArticleCommentByArticleId(long id, String delete_flag);

	int fetchTotalArticleListByPartyId(long partyId, String delete_flag);

	int fetchTotalQueryListByPostedToPartyId(long partyId, String delete_flag_N);

	int fetchTotalAnswerListByQueryId(long id, String delete_flag_N);

	int fetchTotalFavoriteArticlesByPartyId(long partyId, String delete_flag_N);

	String fetchArticleStatus(long articleStatusId);

	List<ArticlePost> fetchArticleListByArticleIdList(List<Long> articleIdList, String delete_flag_N,
			String encryptPass);

	int checkPartyIsPresent(long partyId);

	int checkForumSubCategoryIsPresent(long forumSubCategoryId);

	int checkThreadIsPresent(long threadId, String delete_flag_N);

	int checkPostIsPresent(long postId, String delete_flag);

	int checkForumQueryByPostedToPartyId(long questionId, long partyId, String delete_flag);

	// List<ArticleComment> fetchArticleReplyCommentByParentId(long parentCommentId,
	// Pageable pageable, String delete_flag,
	// String encryptPass);

	int fetchTotalArticleCommentByParentId(long id, long parentId, String delete_flag);

	List<ArticleComment> fetchArticleCommentByParentId(long id, long parentId, String delete_flag, String encryptPass);

	int fetchArticleUpCount(long articleId);

	int modifyArticlePost( ArticlePost modifypost, String encryptPass);

	int fetchTotalArticleListByStatusId(long articleStatusId, String delete_flag);

	List<ArticlePost> fetchArticleListByStatusId(Pageable pageable, long articleStatusId, String delete_flag_N,
			String encryptPass);

	ArticleVoteAddress fetchArticleVoteAddress(long articleId, long partyId);

	int removeArticleVoteAddress(long articleId, long partyId);

	Advisor fetchPublicAdvisorByAdvId(String roleBasedId, String deleteflag, String encryptPass);

	List<ArticlePost> searchArticleByTitle(Pageable pageable, String title, String forumStatusId, String deleteflag,
			String encryptPass);

	int fetchTotalSearchArticleByTitle(String deleteflag, String encryptPass, String forumStatusId);

	List<ArticlePost> fetchAllArticleInApprovedOrderWithoutToken(int forumStatusId, int articleStatusId,
			String delete_flag, String encryptPass);

	List<ArticlePost> fetchAllArticleInApprovedOrderWTByProdId(int prodId, int forumStatusId, int articleStatusId,
			String delete_flag, String encryptPass);

	String fetchArticlePostByTitle(String title, String delete_flag);

	int fetchCountForUrl(String url);

	int fetchTotalAllArticleInApprovedOrder(int forumStatusId, String delete_flag, int prodId);

	int removeArticleFromMyPost(ArticlePost articlePost);




}
