package com.sowisetech.forum.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Custom application messages.
 * 
 * @author Arunagiri
 */
@PropertySource("classpath:forumappmessages.properties")
@Component
public class ForumAppMessages {

	@Value("${success}")
	public String success;

	@Value("${fields_empty}")
	public String fields_empty;

	@Value("${party_not_found}")
	public String party_not_found;

	@Value("${forumsubcategory_not_found}")
	public String forumsubcategory_not_found;

	@Value("${forumthread_added_successfully}")
	public String forumthread_added_successfully;

	@Value("${no_record_found}")
	public String no_record_found;

	@Value("${thread_moderated_successfully}")
	public String thread_moderated_successfully;

	@Value("${moderaterequest_is_empty}")
	public String moderaterequest_is_empty;

	@Value("${record_deleted_successfully}")
	public String record_deleted_successfully;

	@Value("${forumpost_added_successfully}")
	public String forumpost_added_successfully;

	@Value("${forumpost_is_empty}")
	public String forumpost_is_empty;

	@Value("${post_moderated_successfully}")
	public String post_moderated_successfully;

	@Value("${forumvote_is_empty}")
	public String forumvote_is_empty;

	@Value("${forumvote_added_successfully}")
	public String forumvote_added_successfully;

	@Value("${articlepost_added_successfully}")
	public String articlepost_added_successfully;

	@Value("${articlepost_is_empty}")
	public String articlepost_is_empty;

	@Value("${article_moderated_successfully}")
	public String article_moderated_successfully;

	@Value("${articlevote_is_empty}")
	public String articlevote_is_empty;

	@Value("${articlevote_added_successfully}")
	public String articlevote_added_successfully;

	@Value("${articlevote_removed_successfully}")
	public String articlevote_removed_successfully;

	@Value("${articlecomment_is_empty}")
	public String articlecomment_is_empty;

	@Value("${articlecomment_added_successfully}")
	public String articlecomment_added_successfully;

	@Value("${articlecomment_moderated_successfully}")
	public String articlecomment_moderated_successfully;

	@Value("${error_occured}")
	public String error_occured;

	@Value("${error_occured_remove}")
	public String error_occured_remove;

	@Value("${advisor_can_post}")
	public String advisor_can_post;

	@Value("${required_reason}")
	public String required_reason;

	@Value("${changed_articlestatus_successfully}")
	public String changed_articlestatus_successfully;

	@Value("${invalid_pagenum}")
	public String invalid_pagenum;

	@Value("${access_denied}")
	public String access_denied;

	@Value("${unauthorized}")
	public String unauthorized;

	@Value("${favorite_added_successfully}")
	public String favorite_added_successfully;

	@Value("${cant_add_own_article_as_favorite}")
	public String cant_add_own_article_as_favorite;

	@Value("${record_modified_successfully}")
	public String record_modified_successfully;

	@Value("${mandatory_fields_partyId}")
	private String mandatory_fields_partyId;

	@Value("${mandatory_fields_commentId}")
	private String mandatory_fields_commentId;

	@Value("${mandatory_fields_postId}")
	private String mandatory_fields_postId;

	@Value("${mandatory_fields_threadId}")
	private String mandatory_fields_threadId;

	@Value("${mandatory_fields_article_post}")
	public String mandatory_fields_article_post;

	@Value("${mandatory_fields_moderate_article}")
	public String mandatory_fields_moderate_article;

	@Value("${mandatory_fields_moderate_comment}")
	public String mandatory_fields_moderate_comment;

	@Value("${mandatory_fields_moderat_thread}")
	public String mandatory_fields_moderat_thread;

	@Value("${mandatory_fields_modify_article}")
	public String mandatory_fields_modify_article;

	@Value("${mandatory_fields_moderat_post}")
	public String mandatory_fields_moderat_post;

	@Value("${mandatory_fields_articleId}")
	public String mandatory_fields_articleId;
	
	@Value("${mandatory_fields_articlePost_articleId}")
	public String mandatory_fields_articlePost_articleId;

	@Value("${mandatory_fields_forumStatusId}")
	public String mandatory_fields_forumStatusId;

	@Value("${mandatory_fields_fetchArticleList_By_PartyId}")
	public String mandatory_fields_fetchArticleList_By_PartyId;

	@Value("${mandatory_fields_queryId}")
	public String mandatory_fields_queryId;

	@Value("${mandatory_fields_articleComment}")
	public String mandatory_fields_articleComment;

	@Value("${mandatory_fields_removeArticleFavorite}")
	public String mandatory_fields_removeArticleFavorite;

	@Value("${mandatory_fields_removeArticleLike}")
	public String mandatory_fields_removeArticleLike;

	@Value("${mandatory_fields_searchArticle}")
	public String mandatory_fields_searchArticle;

	@Value("${mandatory_fields_ArticleUpCount}")
	public String mandatory_fields_ArticleUpCount;

	@Value("${mandatory_fields_ArticleVoteAddress}")
	public String mandatory_fields_ArticleVoteAddress;

	@Value("${mandatory_fields_FavoriteArticlesByPartyId}")
	public String mandatory_fields_FavoriteArticlesByPartyId;

	@Value("${mandatory_fields_createQuery}")
	public String mandatory_fields_createQuery;

	@Value("${mandatory_fields_createAnswer}")
	public String mandatory_fields_createAnswer;

	@Value("${mandatory_fields_forumPostVote}")
	public String mandatory_fields_forumPostVote;

	@Value("${mandatory_fields_voteArticle}")
	public String mandatory_fields_voteArticle;

	@Value("${mandatory_fields_addArticleFavorite}")
	public String mandatory_fields_addArticleFavorite;

	@Value("${mandatory_fields_add_post}")
	public String mandatory_fields_add_post;

	@Value("${mandatory_fields_add_thread}")
	public String mandatory_fields_add_thread;

	@Value("${mandatory_fields_articleId_articleStatusId}")
	public String mandatory_fields_articleId_articleStatusId;

	@Value("${mandatory_fields_addArticle}")
	public String mandatory_fields_addArticle;

	@Value("${mandatory_fields_articlePostComment}")
	public String mandatory_fields_articlePostComment;
	
	@Value("${articlePost_deleted_successfully}")
	public String articlePost_deleted_successfully;

	@Value("${favorite_removed_successfully}")
	public String favorite_removed_successfully;

	public String getRecord_modified_successfully() {
		return record_modified_successfully;
	}

	public void setRecord_modified_successfully(String record_modified_successfully) {
		this.record_modified_successfully = record_modified_successfully;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getFields_empty() {
		return fields_empty;
	}

	public void setFields_empty(String fields_empty) {
		this.fields_empty = fields_empty;
	}

	public String getParty_not_found() {
		return party_not_found;
	}

	public void setParty_not_found(String party_not_found) {
		this.party_not_found = party_not_found;
	}

	public String getForumsubcategory_not_found() {
		return forumsubcategory_not_found;
	}

	public void setForumsubcategory_not_found(String forumsubcategory_not_found) {
		this.forumsubcategory_not_found = forumsubcategory_not_found;
	}

	public String getForumthread_added_successfully() {
		return forumthread_added_successfully;
	}

	public void setForumthread_added_successfully(String forumthread_added_successfully) {
		this.forumthread_added_successfully = forumthread_added_successfully;
	}

	public String getNo_record_found() {
		return no_record_found;
	}

	public void setNo_record_found(String no_record_found) {
		this.no_record_found = no_record_found;
	}

	public String getThread_moderated_successfully() {
		return thread_moderated_successfully;
	}

	public void setThread_moderated_successfully(String thread_moderated_successfully) {
		this.thread_moderated_successfully = thread_moderated_successfully;
	}

	public String getModeraterequest_is_empty() {
		return moderaterequest_is_empty;
	}

	public void setModeraterequest_is_empty(String moderaterequest_is_empty) {
		this.moderaterequest_is_empty = moderaterequest_is_empty;
	}

	public String getRecord_deleted_successfully() {
		return record_deleted_successfully;
	}

	public void setRecord_deleted_successfully(String record_deleted_successfully) {
		this.record_deleted_successfully = record_deleted_successfully;
	}

	public String getForumpost_added_successfully() {
		return forumpost_added_successfully;
	}

	public void setForumpost_added_successfully(String forumpost_added_successfully) {
		this.forumpost_added_successfully = forumpost_added_successfully;
	}

	public String getForumpost_is_empty() {
		return forumpost_is_empty;
	}

	public void setForumpost_is_empty(String forumpost_is_empty) {
		this.forumpost_is_empty = forumpost_is_empty;
	}

	public String getPost_moderated_successfully() {
		return post_moderated_successfully;
	}

	public void setPost_moderated_successfully(String post_moderated_successfully) {
		this.post_moderated_successfully = post_moderated_successfully;
	}

	public String getForumvote_is_empty() {
		return forumvote_is_empty;
	}

	public void setForumvote_is_empty(String forumvote_is_empty) {
		this.forumvote_is_empty = forumvote_is_empty;
	}

	public String getForumvote_added_successfully() {
		return forumvote_added_successfully;
	}

	public void setForumvote_added_successfully(String forumvote_added_successfully) {
		this.forumvote_added_successfully = forumvote_added_successfully;
	}

	public String getArticlepost_added_successfully() {
		return articlepost_added_successfully;
	}

	public void setArticlepost_added_successfully(String articlepost_added_successfully) {
		this.articlepost_added_successfully = articlepost_added_successfully;
	}

	public String getArticlepost_is_empty() {
		return articlepost_is_empty;
	}

	public void setArticlepost_is_empty(String articlepost_is_empty) {
		this.articlepost_is_empty = articlepost_is_empty;
	}

	public String getArticle_moderated_successfully() {
		return article_moderated_successfully;
	}

	public void setArticle_moderated_successfully(String article_moderated_successfully) {
		this.article_moderated_successfully = article_moderated_successfully;
	}

	public String getArticlevote_is_empty() {
		return articlevote_is_empty;
	}

	public void setArticlevote_is_empty(String articlevote_is_empty) {
		this.articlevote_is_empty = articlevote_is_empty;
	}

	public String getArticlevote_added_successfully() {
		return articlevote_added_successfully;
	}

	public void setArticlevote_added_successfully(String articlevote_added_successfully) {
		this.articlevote_added_successfully = articlevote_added_successfully;
	}

	public String getArticlecomment_is_empty() {
		return articlecomment_is_empty;
	}

	public void setArticlecomment_is_empty(String articlecomment_is_empty) {
		this.articlecomment_is_empty = articlecomment_is_empty;
	}

	public String getArticlecomment_added_successfully() {
		return articlecomment_added_successfully;
	}

	public String getArticlevote_removed_successfully() {
		return articlevote_removed_successfully;
	}

	public void setArticlevote_removed_successfully(String articlevote_removed_successfully) {
		this.articlevote_removed_successfully = articlevote_removed_successfully;
	}

	public void setArticlecomment_added_successfully(String articlecomment_added_successfully) {
		this.articlecomment_added_successfully = articlecomment_added_successfully;
	}

	public String getArticlecomment_moderated_successfully() {
		return articlecomment_moderated_successfully;
	}

	public void setArticlecomment_moderated_successfully(String articlecomment_moderated_successfully) {
		this.articlecomment_moderated_successfully = articlecomment_moderated_successfully;
	}

	public String getError_occured() {
		return error_occured;
	}

	public void setError_occured(String error_occured) {
		this.error_occured = error_occured;
	}

	public String getError_occured_remove() {
		return error_occured_remove;
	}

	public void setError_occured_remove(String error_occured_remove) {
		this.error_occured_remove = error_occured_remove;
	}

	public String getAdvisor_can_post() {
		return advisor_can_post;
	}

	public void setAdvisor_can_post(String advisor_can_post) {
		this.advisor_can_post = advisor_can_post;
	}

	public String getRequired_reason() {
		return required_reason;
	}

	public void setRequired_reason(String required_reason) {
		this.required_reason = required_reason;
	}

	public String getChanged_articlestatus_successfully() {
		return changed_articlestatus_successfully;
	}

	public void setChanged_articlestatus_successfully(String changed_articlestatus_successfully) {
		this.changed_articlestatus_successfully = changed_articlestatus_successfully;
	}

	public String getInvalid_pagenum() {
		return invalid_pagenum;
	}

	public void setInvalid_pagenum(String invalid_pagenum) {
		this.invalid_pagenum = invalid_pagenum;
	}

	public String getAccess_denied() {
		return access_denied;
	}

	public void setAccess_denied(String access_denied) {
		this.access_denied = access_denied;
	}

	public String getUnauthorized() {
		return unauthorized;
	}

	public void setUnauthorized(String unauthorized) {
		this.unauthorized = unauthorized;
	}

	public String getFavorite_added_successfully() {
		return favorite_added_successfully;
	}

	public void setFavorite_added_successfully(String favorite_added_successfully) {
		this.favorite_added_successfully = favorite_added_successfully;
	}

	public String getCant_add_own_article_as_favorite() {
		return cant_add_own_article_as_favorite;
	}

	public void setCant_add_own_article_as_favorite(String cant_add_own_article_as_favorite) {
		this.cant_add_own_article_as_favorite = cant_add_own_article_as_favorite;
	}

	public String getMandatory_fields_article_post() {
		return mandatory_fields_article_post;
	}

	public void setMandatory_fields_article_post(String mandatory_fields_article_post) {
		this.mandatory_fields_article_post = mandatory_fields_article_post;
	}

	public String getMandatory_fields_moderate_article() {
		return mandatory_fields_moderate_article;
	}

	public void setMandatory_fields_moderate_article(String mandatory_fields_moderate_article) {
		this.mandatory_fields_moderate_article = mandatory_fields_moderate_article;
	}

	public String getMandatory_fields_moderate_comment() {
		return mandatory_fields_moderate_comment;
	}

	public void setMandatory_fields_moderate_comment(String mandatory_fields_moderate_comment) {
		this.mandatory_fields_moderate_comment = mandatory_fields_moderate_comment;
	}

	public String getMandatory_fields_moderat_thread() {
		return mandatory_fields_moderat_thread;
	}

	public void setMandatory_fields_moderat_thread(String mandatory_fields_moderat_thread) {
		this.mandatory_fields_moderat_thread = mandatory_fields_moderat_thread;
	}

	public String getMandatory_fields_modify_article() {
		return mandatory_fields_modify_article;
	}

	public void setMandatory_fields_modify_article(String mandatory_fields_modify_article) {
		this.mandatory_fields_modify_article = mandatory_fields_modify_article;
	}

	public String getMandatory_fields_moderat_post() {
		return mandatory_fields_moderat_post;
	}

	public void setMandatory_fields_moderat_post(String mandatory_fields_moderat_post) {
		this.mandatory_fields_moderat_post = mandatory_fields_moderat_post;
	}

	public String getMandatory_fields_commentId() {
		return mandatory_fields_commentId;
	}

	public void setMandatory_fields_commentId(String mandatory_fields_commentId) {
		this.mandatory_fields_commentId = mandatory_fields_commentId;
	}

	public String getMandatory_fields_partyId() {
		return mandatory_fields_partyId;
	}

	public void setMandatory_fields_partyId(String mandatory_fields_partyId) {
		this.mandatory_fields_partyId = mandatory_fields_partyId;
	}

	public String getMandatory_fields_articleId() {
		return mandatory_fields_articleId;
	}

	public void setMandatory_fields_articleId(String mandatory_fields_articleId) {
		this.mandatory_fields_articleId = mandatory_fields_articleId;
	}

	public String getMandatory_fields_postId() {
		return mandatory_fields_postId;
	}

	public void setMandatory_fields_postId(String mandatory_fields_postId) {
		this.mandatory_fields_postId = mandatory_fields_postId;
	}

	public String getMandatory_fields_threadId() {
		return mandatory_fields_threadId;
	}

	public void setMandatory_fields_threadId(String mandatory_fields_threadId) {
		this.mandatory_fields_threadId = mandatory_fields_threadId;
	}

	public String getMandatory_fields_forumStatusId() {
		return mandatory_fields_forumStatusId;
	}

	public void setMandatory_fields_forumStatusId(String mandatory_fields_forumStatusId) {
		this.mandatory_fields_forumStatusId = mandatory_fields_forumStatusId;
	}

	public String getMandatory_fields_queryId() {
		return mandatory_fields_queryId;
	}

	public void setMandatory_fields_queryId(String mandatory_fields_queryId) {
		this.mandatory_fields_queryId = mandatory_fields_queryId;
	}

	public String getMandatory_fields_articleComment() {
		return mandatory_fields_articleComment;
	}

	public void setMandatory_fields_articleComment(String mandatory_fields_articleComment) {
		this.mandatory_fields_articleComment = mandatory_fields_articleComment;
	}

	public String getMandatory_fields_articlePost_articleId() {
		return mandatory_fields_articlePost_articleId;
	}

	public void setMandatory_fields_articlePost_articleId(String mandatory_fields_articlePost_articleId) {
		this.mandatory_fields_articlePost_articleId = mandatory_fields_articlePost_articleId;
	}

	public String getMandatory_fields_removeArticleFavorite() {
		return mandatory_fields_removeArticleFavorite;
	}

	public void setMandatory_fields_removeArticleFavorite(String mandatory_fields_removeArticleFavorite) {
		this.mandatory_fields_removeArticleFavorite = mandatory_fields_removeArticleFavorite;
	}

	public String getMandatory_fields_removeArticleLike() {
		return mandatory_fields_removeArticleLike;
	}

	public void setMandatory_fields_removeArticleLike(String mandatory_fields_removeArticleLike) {
		this.mandatory_fields_removeArticleLike = mandatory_fields_removeArticleLike;
	}

	public String getMandatory_fields_searchArticle() {
		return mandatory_fields_searchArticle;
	}

	public void setMandatory_fields_searchArticle(String mandatory_fields_searchArticle) {
		this.mandatory_fields_searchArticle = mandatory_fields_searchArticle;
	}

	public String getMandatory_fields_fetchArticleList_By_PartyId() {
		return mandatory_fields_fetchArticleList_By_PartyId;
	}

	public void setMandatory_fields_fetchArticleList_By_PartyId(String mandatory_fields_fetchArticleList_By_PartyId) {
		this.mandatory_fields_fetchArticleList_By_PartyId = mandatory_fields_fetchArticleList_By_PartyId;
	}

	public String getMandatory_fields_ArticleUpCount() {
		return mandatory_fields_ArticleUpCount;
	}

	public void setMandatory_fields_ArticleUpCount(String mandatory_fields_ArticleUpCount) {
		this.mandatory_fields_ArticleUpCount = mandatory_fields_ArticleUpCount;
	}

	public String getMandatory_fields_ArticleVoteAddress() {
		return mandatory_fields_ArticleVoteAddress;
	}

	public void setMandatory_fields_ArticleVoteAddress(String mandatory_fields_ArticleVoteAddress) {
		this.mandatory_fields_ArticleVoteAddress = mandatory_fields_ArticleVoteAddress;
	}

	public String getMandatory_fields_FavoriteArticlesByPartyId() {
		return mandatory_fields_FavoriteArticlesByPartyId;
	}

	public void setMandatory_fields_FavoriteArticlesByPartyId(String mandatory_fields_FavoriteArticlesByPartyId) {
		this.mandatory_fields_FavoriteArticlesByPartyId = mandatory_fields_FavoriteArticlesByPartyId;
	}

	public String getMandatory_fields_createQuery() {
		return mandatory_fields_createQuery;
	}

	public void setMandatory_fields_createQuery(String mandatory_fields_createQuery) {
		this.mandatory_fields_createQuery = mandatory_fields_createQuery;
	}

	public String getMandatory_fields_createAnswer() {
		return mandatory_fields_createAnswer;
	}

	public void setMandatory_fields_createAnswer(String mandatory_fields_createAnswer) {
		this.mandatory_fields_createAnswer = mandatory_fields_createAnswer;
	}

	public String getMandatory_fields_forumPostVote() {
		return mandatory_fields_forumPostVote;
	}

	public void setMandatory_fields_forumPostVote(String mandatory_fields_forumPostVote) {
		this.mandatory_fields_forumPostVote = mandatory_fields_forumPostVote;
	}

	public String getMandatory_fields_voteArticle() {
		return mandatory_fields_voteArticle;
	}

	public void setMandatory_fields_voteArticle(String mandatory_fields_voteArticle) {
		this.mandatory_fields_voteArticle = mandatory_fields_voteArticle;
	}

	public String getMandatory_fields_addArticleFavorite() {
		return mandatory_fields_addArticleFavorite;
	}

	public void setMandatory_fields_addArticleFavorite(String mandatory_fields_addArticleFavorite) {
		this.mandatory_fields_addArticleFavorite = mandatory_fields_addArticleFavorite;
	}

	public String getMandatory_fields_add_post() {
		return mandatory_fields_add_post;
	}

	public void setMandatory_fields_add_post(String mandatory_fields_add_post) {
		this.mandatory_fields_add_post = mandatory_fields_add_post;
	}

	public String getMandatory_fields_add_thread() {
		return mandatory_fields_add_thread;
	}

	public void setMandatory_fields_add_thread(String mandatory_fields_add_thread) {
		this.mandatory_fields_add_thread = mandatory_fields_add_thread;
	}

	public String getMandatory_fields_articleId_articleStatusId() {
		return mandatory_fields_articleId_articleStatusId;
	}

	public void setMandatory_fields_articleId_articleStatusId(String mandatory_fields_articleId_articleStatusId) {
		this.mandatory_fields_articleId_articleStatusId = mandatory_fields_articleId_articleStatusId;
	}

	public String getMandatory_fields_addArticle() {
		return mandatory_fields_addArticle;
	}

	public void setMandatory_fields_addArticle(String mandatory_fields_addArticle) {
		this.mandatory_fields_addArticle = mandatory_fields_addArticle;
	}

	public String getMandatory_fields_articlePostComment() {
		return mandatory_fields_articlePostComment;
	}

	public void setMandatory_fields_articlePostComment(String mandatory_fields_articlePostComment) {
		this.mandatory_fields_articlePostComment = mandatory_fields_articlePostComment;
	}

	public String getArticlePost_deleted_successfully() {
		return articlePost_deleted_successfully;
	}

	public void setArticlePost_deleted_successfully(String articlePost_deleted_successfully) {
		this.articlePost_deleted_successfully = articlePost_deleted_successfully;
	}

	public String getFavorite_removed_successfully() {
		return favorite_removed_successfully;
	}

	public void setFavorite_removed_successfully(String favorite_removed_successfully) {
		this.favorite_removed_successfully = favorite_removed_successfully;
	}

}