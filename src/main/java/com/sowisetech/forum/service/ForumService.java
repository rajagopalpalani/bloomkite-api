package com.sowisetech.forum.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumSubCategory;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.model.VoteType;

@Service
public interface ForumService {

	ForumSubCategory fetchForumSubCategory(long forumSubCategoryId);

	// Article
	int createArticlePost(ArticlePost articlePost, String title);

	int fetchArticlePost(long articleId);

	boolean checkForumStatusIsRejected(long forumStatusId);

	int moderateArticlePost(long articleId, long forumStatusId, String adminId, String reason);

	int createArticlePostComment(ArticleComment comment);

	int fetchArticleComment(long commentId);

	int moderateArticleComment(long commentId, long forumStatusId, String string);

	int createArticleVote(long voteType, long articleId);

	Party fetchParty(long partyId);

	int removeArticle(long articleId);

	int removeArticleComment(long commentId);

	int saveArticleVote(long voteType, long articleId, long partyId);

	boolean checkPartyIsAdvisor(long partyId);

	List<ArticlePost> fetchRecentArticlePostList();

	List<ForumCategory> fetchAllForumCategory();

	ArticlePost fetchRecentApprovedArticle();

	List<ArticlePost> fetchAllArticleInApprovedOrder();

	List<ArticleComment> fetchArticleCommentByArticleId(long id);

	List<ArticlePost> fetchArticlePostList(int pageNum, int records);

	ArticlePost fetchArticlePostByarticleId(long articleId);

	List<ArticlePost> fetchArticleListByPartyId(long partyId);

	int changeArticleStatus(long articleId, long articleStatusId);

	String fetchEmailIdByPartyId(long partyId);

	int createThread(ForumThread thread);

	ForumThread fetchThread(long threadId);

	int moderateThread(long threadId, long forumStatusId, String adminId);

	int removeThread(long threadId);

	int createPost(ForumPost post);

	ForumPost fetchPost(long postId);

	int moderatePost(long postId, long forumStatusId, String adminId);

	int createForumPostVote(long voteType, long postId);

	int savePostVote(long voteType, long postId, long partyId);

	int removePost(long postId);

	int createQuery(List<ForumQuery> queryList);

	ForumQuery fetchForumQueryByPostedToPartyId(long questionId, long partyId);

	int createForumAnswer(ForumAnswer answer);

	List<ForumQuery> fetchQueryListByPostedToPartyId(long id, int pageNum, int records);

	List<ForumAnswer> fetchAnswerListByQueryId(long id, int pageNum, int records);

	List<VoteType> fetchVoteTypeList();

	int addArticleToFavorite(ArticleFavorite articleFavorite);

	List<ArticlePost> fetchFavoriteArticlesByPartyId(long partyId);

	int fetchTotalRecentArticlePostList();

	int fetchTotalAllArticleInApprovedOrder();

	int fetchTotalArticlePostList();

	int removeArticleFromFavorite(ArticleFavorite articleFavorite);

	int fetchTotalArticleCommentByArticleId(long id);

	int fetchTotalArticleListByPartyId(long partyId);

	int fetchTotalQueryListByPostedToPartyId(long partyId);

	int fetchTotalAnswerListByQueryId(long id);

	int fetchTotalFavoriteArticlesByPartyId(long id);

	int checkPartyIsPresent(long partyId);

	int checkForumSubCategoryIsPresent(long forumSubCategoryId);

	int checkThreadIsPresent(long threadId);

	int checkPostIsPresent(long postId);

	int checkForumQueryByPostedToPartyId(long questionId, long partyId);

	int fetchTotalArticleCommentByParentId(long articleId, long parentId);

	List<ArticleComment> fetchArticleCommentByParentId(long articleId, long parentId);

	int fetchArticleUpCount(long articleId);

	int modifyArticlePost(long articleId,ArticlePost articlePost);

	int fetchTotalArticleListByStatusId(long forumStatusId);

	List<ArticlePost> fetchArticleListByStatusId(int pageNum, int records, long forumStatusId);

	ArticleVoteAddress fetchArticleVoteAddress(long articleId, long partyId);

	int decreaseLikeCount(long articleId);

	int removeArticleVoteAddress(long articleId, long partyId);

	Advisor fetchByPublicAdvisorID(String roleBasedId);

	List<ArticlePost> searchArticleByTitle(int pageNum, int records, String title, String forumStatusId);

	int fetchTotalSearchArticleByTitle(String title, String forumStatusId);

	List<ArticlePost> fetchAllArticleInApprovedOrderWithoutToken();

	List<ArticlePost> fetchAllArticleInApprovedOrderWithoutToken(int prodId);

	String fetchArticlePostByTitle(String title);

	int fetchTotalAllArticleInApprovedOrder(int prodId);

	int removeArticleFromMyPost(ArticlePost articlePost);

	Blogger fetchBloggerByBloggerId(String roleBasedId);

	int createBloggerArticlePost(ArticlePost articlePost, String url);
}
