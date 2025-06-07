package com.sowisetech.forum.service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumPostVote;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumSubCategory;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.model.VoteType;
import com.sowisetech.forum.util.ForumTableFields;
import com.sowisetech.investor.model.Investor;

@Service("ForumService")
@Transactional(readOnly = true)
public class ForumServiceImpl implements ForumService {
	@Autowired
	private ForumDao forumDao;
	@Autowired
	private ForumTableFields forumTableFields;
	@Autowired
	private AdvTableFields advTableFields;
	@Autowired
	private AuthDao authDao;
	@Autowired
	private AdvisorDao advisorDao;
	@Autowired
	AdminSignin adminSignin;

	// Article
	@Transactional
	public int createArticlePost(ArticlePost articlePost, String url) {
		String desc = forumTableFields.getForumstatus_inprogress();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
		if (party.getRoleBasedId().startsWith("ADV")) {
			Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
			if (advisor.getDisplayName() != null) {
				articlePost.setName(advisor.getDisplayName());
			} else {
				articlePost.setName(advisor.getName());
			}
			String signedUserId = getSignedInUser();
			articlePost.setDesignation(advisor.getDesignation());
			articlePost.setImagePath(advisor.getImagePath());
			articlePost.setUserName(advisor.getUserName());
			articlePost.setRoleBasedId(advisor.getAdvId());
			articlePost.setCreated_by(signedUserId);
			articlePost.setUpdated_by(signedUserId);
			articlePost.setForumStatusId(forumStatusId);
			articlePost.setDelete_flag(forumTableFields.getDelete_flag_N());
		}
		String articleDesc = forumTableFields.getArticlestatus_active();
		int articleStatusId = forumDao.fetchArticleStatusIdByDesc(articleDesc);
		articlePost.setArticleStatusId(articleStatusId);
		String validUrl = null;
		if (url != null) {
			validUrl = url.replace(' ', '-');
			System.out.println("u" + validUrl);
			int count = forumDao.fetchCountForUrl(validUrl);
			String url1 = (count > 0) ? validUrl + "-" + count : validUrl;
			String url1_lc = url1.toLowerCase();
			articlePost.setUrl(url1_lc);
		} else {
			validUrl = articlePost.getTitle().replace(' ', '-');
			String title_lc = validUrl.toLowerCase();
			articlePost.setUrl(title_lc);
		}
		return forumDao.createArticlePost(articlePost, encryptPass);
	}

	private String getSignedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
			return adminSignin.getAdmin_name();
		} else {
			Party party = forumDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
			return party.getRoleBasedId();
		}
	}

	@Transactional
	public int fetchArticlePost(long articleId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchArticlePost(articleId, delete_flag);
	}

	@Transactional
	public boolean checkForumStatusIsRejected(long forumStatusId) {
		String status = forumDao.fetchForumStatus(forumStatusId);
		if (status.equals(forumTableFields.getForum_status_rejected())) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public int moderateArticlePost(long articleId, long forumStatusId, String adminId, String reason) {
		String signedUserId = getSignedInUser();
		return forumDao.moderateArticlePost(articleId, forumStatusId, adminId, reason, signedUserId);

	}

	@Transactional
	public int createArticlePostComment(ArticleComment comment) {
		String desc = forumTableFields.getForumstatus_inprogress();
		String signedUserId = getSignedInUser();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		Party party = forumDao.fetchParty(comment.getPartyId(), encryptPass);
		if (party.getRoleBasedId().startsWith("ADV")) {
			Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
			if (advisor.getDisplayName() != null) {
				comment.setName(advisor.getDisplayName());
			} else {
				comment.setName(advisor.getName());
			}
			comment.setDesignation(advisor.getDesignation());
			comment.setImagePath(advisor.getImagePath());
		} else if (party.getRoleBasedId().startsWith("INV")) {
			Investor investor = advisorDao.fetchInvestorByInvId(party.getRoleBasedId(), delete_flag, encryptPass);
			comment.setName(investor.getFullName());
			comment.setImagePath(investor.getImagePath());
		}else if(party.getRoleBasedId().startsWith("BLG")) {
			Blogger blogger = advisorDao.fetchBloggerByBloggerId(party.getRoleBasedId(), delete_flag, encryptPass);
			comment.setName(blogger.getFullName());
			comment.setImagePath(blogger.getImagePath());
		}
		comment.setForumStatusId(forumStatusId);
		comment.setDelete_flag(forumTableFields.getDelete_flag_N());
		comment.setCreated_by(signedUserId);
		comment.setUpdated_by(signedUserId);
		return forumDao.createArticlePostComment(comment, encryptPass);

	}

	@Transactional
	public int fetchArticleComment(long commentId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchArticleComment(commentId, delete_flag);
	}

	@Transactional
	public int moderateArticleComment(long commentId, long forumStatusId, String adminId) {
		String signedUserId = getSignedInUser();
		return forumDao.moderateArticleComment(commentId, forumStatusId, adminId, signedUserId);

	}

	@Transactional
	public int createArticleVote(long voteType, long articleId) {
		ArticleVote articleVote = forumDao.fetchArticleVoteByArticleId(articleId);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = forumDao.fetchUpVoteId(up);
		int downVote = forumDao.fetchDownVoteId(down);
		if (articleVote == null) {
			ArticleVote articleVote1 = new ArticleVote();
			if (voteType == upVote) {
				articleVote1.setArticleId(articleId);
				articleVote1.setUp_count(1);
				articleVote1.setDown_count(0);
			}
			if (voteType == downVote) {
				articleVote1.setArticleId(articleId);
				articleVote1.setUp_count(0);
				articleVote1.setDown_count(1);
			}
			int result = forumDao.firstArticleVote(articleVote1);
			return result;
		} else {
			int result = 0;
			if (voteType == upVote) {
				int upCount = forumDao.fetchUpCountByArticleId(articleId);
				upCount++;
				result = forumDao.updateArticleUpVote(upCount, articleId);
			}
			if (voteType == downVote) {
				int downCount = forumDao.fetchDownCountByArticleId(articleId);
				downCount++;
				result = forumDao.updateArticleDownVote(downCount, articleId);
			}
			return result;
		}
	}

	@Transactional
	public ForumSubCategory fetchForumSubCategory(long forumSubCategoryId) {
		return forumDao.fetchForumSubCategory(forumSubCategoryId);
	}

	@Transactional
	public Party fetchParty(long partyId) {
		String encryptPass = advTableFields.getEncryption_password();
		return forumDao.fetchParty(partyId, encryptPass);

	}

	@Transactional
	public int removeArticle(long articleId) {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		return forumDao.removeArticle(articleId, delete_flag, signedUserId);
	}

	@Transactional
	public int removeArticleComment(long commentId) {
		String delete_flag = forumTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		return forumDao.removeArticleComment(commentId, delete_flag, signedUserId);
	}

	@Transactional
	public int saveArticleVote(long voteType, long articleId, long partyId) {
		String signedUserId = getSignedInUser();
		return forumDao.saveArticleVote(voteType, articleId, partyId, signedUserId);
	}

	@Transactional
	public boolean checkPartyIsAdvisor(long partyId) {
		int partyRoleId = getRoleIdFromUserRole(partyId);
		int roleId = authDao.fetchRoleIdByName(forumTableFields.getRole_advisor());
		int nonindividual_roleId = authDao.fetchRoleIdByName(forumTableFields.getNonindividual_role());
		if (partyRoleId == roleId) {
			return true;
		} else if (partyRoleId == nonindividual_roleId) {
			return true;
		} else {
			return false;
		}
	}

	private int getRoleIdFromUserRole(long partyId) {
		List<User_role> user_role = authDao.fetchUserRoleByUserId(partyId);
		int roleId = 0;
		if (user_role.size() == 1) {
			roleId = user_role.get(0).getRole_id();
		} else {
			for (User_role userRole : user_role) {
				if (userRole.getIsPrimaryRole() == 1) {
					roleId = userRole.getRole_id();
				}
			}
		}
		return roleId;
	}

	@Transactional
	public List<ArticlePost> fetchRecentArticlePostList() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<ArticlePost> articlePostList = forumDao.fetchRecentArticlePostList(delete_flag, encryptPass);
		for (ArticlePost articlePost : articlePostList) {
			Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
			if (party != null) {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
				if (advisor != null) {
					articlePost.setUserName(advisor.getUserName());
				}
			}
		}
		return articlePostList;
	}

	@Transactional
	public List<ForumCategory> fetchAllForumCategory() {
		return forumDao.fetchAllForumCategory();
	}

	@Transactional
	public ArticlePost fetchRecentApprovedArticle() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();
		String encryptPass = advTableFields.getEncryption_password();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		ArticlePost articlePost = forumDao.fetchRecentApprovedArticle(forumStatusId, delete_flag, encryptPass);
		if (articlePost != null) {
			Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
			if (party != null) {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
				if (advisor != null) {
					articlePost.setUserName(advisor.getUserName());
				}
			}
		}
		return articlePost;
	}

	@Transactional
	public List<ArticlePost> fetchAllArticleInApprovedOrder() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();
		String encryptPass = advTableFields.getEncryption_password();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		String articleStatus = forumTableFields.getArticlestatus_active();
		int articleStatusId = forumDao.fetchArticleStatusIdByDesc(articleStatus);
		List<ArticlePost> articlePostList = forumDao.fetchAllArticleInApprovedOrder(forumStatusId, articleStatusId,
				delete_flag, encryptPass);
		return articlePostList;
	}

	@Transactional
	public List<ArticleComment> fetchArticleCommentByArticleId(long articleId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<ArticleComment> articleCommentList = forumDao.fetchArticleCommentByArticleId(articleId, delete_flag,
				encryptPass);
		if (articleCommentList != null) {
			for (ArticleComment articleComment : articleCommentList) {
				List<ArticleComment> articleCommentLists = forumDao.fetchArticleCommentByParentId(
						articleComment.getArticleId(), articleComment.getCommentId(), delete_flag, encryptPass);
				if (articleCommentLists != null) {
					for (ArticleComment articleComment1 : articleCommentLists) {
						Party party = forumDao.fetchParty(articleComment1.getPartyId(), encryptPass);
						if (party != null && party.getRoleBasedId().startsWith("ADV")) {
							Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag,
									encryptPass);
							articleComment1.setUserName(advisor.getUserName());
						}
					}
				}
				articleComment.setReplyComments(articleCommentLists);
				Party party = forumDao.fetchParty(articleComment.getPartyId(), encryptPass);
				if (party != null && party.getRoleBasedId().startsWith("ADV")) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
					articleComment.setUserName(advisor.getUserName());
				}
			}
		}
		return articleCommentList;
	}

	@Transactional
	public List<ArticlePost> fetchArticlePostList(int pageNum, int records) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<ArticlePost> articlePostList = forumDao.fetchArticlePostList(pageable, delete_flag, encryptPass);
		if (articlePostList != null) {
			for (ArticlePost articlePost : articlePostList) {
				Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
				if (party != null) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
					if (advisor != null) {
						articlePost.setUserName(advisor.getUserName());
					}
				}
			}
		}
		return articlePostList;
	}

	@Transactional
	public ArticlePost fetchArticlePostByarticleId(long articleId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		ArticlePost articlePost = forumDao.fetchArticlePostByarticleId(articleId, delete_flag_N, encryptPass);
		if (articlePost != null) {
			Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
			if (party != null) {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag_N, encryptPass);
				if (advisor != null) {
					articlePost.setUserName(advisor.getUserName());
				}
			}
		}
		return articlePost;
	}

	@Transactional
	public List<ArticlePost> fetchArticleListByPartyId(long partyId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();

		List<ArticlePost> articleList = forumDao.fetchArticleListByPartyId(partyId, delete_flag_N, encryptPass);
		if (articleList != null && articleList.size() != 0) {
			for (ArticlePost articlePost : articleList) {
				String forumStatus = forumDao.fetchForumStatus(articlePost.getForumStatusId());
				String articleStatus = forumDao.fetchArticleStatus(articlePost.getArticleStatusId());
				articlePost.setForumStatus(forumStatus);
				articlePost.setArticleStatus(articleStatus);
				Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
				if (party != null) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag_N,
							encryptPass);
					if (advisor != null) {
						articlePost.setUserName(advisor.getUserName());
					}
				}
			}
		}
		return articleList;
	}

	@Transactional
	public int changeArticleStatus(long articleId, long articleStatusId) {
		String signedUserId = getSignedInUser();
		return forumDao.changeArticleStatus(articleId, articleStatusId, signedUserId);
	}

	@Transactional
	public String fetchEmailIdByPartyId(long partyId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		return forumDao.fetchEmailIdByPartyId(partyId, delete_flag, encryptPass);
	}

	@Transactional
	public int createThread(ForumThread thread) {
		String desc = forumTableFields.getForumstatus_inprogress();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		String signedUserId = getSignedInUser();
		thread.setCreated_by(signedUserId);
		thread.setUpdated_by(signedUserId);
		thread.setForumStatusId(forumStatusId);
		thread.setDelete_flag(forumTableFields.getDelete_flag_N());
		return forumDao.createForumThread(thread);
	}

	@Transactional
	public ForumThread fetchThread(long threadId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchThread(threadId, delete_flag);
	}

	@Transactional
	public int moderateThread(long threadId, long forumStatusId, String adminId) {
		String signedUserId = getSignedInUser();
		return forumDao.moderateThread(threadId, forumStatusId, adminId, signedUserId);
	}

	@Transactional
	public int removeThread(long threadId) {
		String signedUserId = getSignedInUser();
		String delete_flag = forumTableFields.getDelete_flag_Y();
		return forumDao.removeThread(threadId, delete_flag, signedUserId);
	}

	@Transactional
	public int createPost(ForumPost fpost) {
		String desc = forumTableFields.getForumstatus_inprogress();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		String signedUserId = getSignedInUser();
		fpost.setForumStatusId(forumStatusId);
		fpost.setCreated_by(signedUserId);
		fpost.setUpdated_by(signedUserId);
		fpost.setDelete_flag(forumTableFields.getDelete_flag_N());
		return forumDao.createForumPost(fpost);
	}

	@Transactional
	public ForumPost fetchPost(long postId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchPost(postId, delete_flag);

	}

	@Transactional
	public int moderatePost(long postId, long forumStatusId, String adminId) {
		String signedUserId = getSignedInUser();
		return forumDao.moderatePost(postId, forumStatusId, adminId, signedUserId);
	}

	@Transactional
	public int createForumPostVote(long voteType, long postId) {
		ForumPostVote forumPostVote = forumDao.fetchForumPostVoteByPostId(postId);
		String up = forumTableFields.getUp();
		String down = forumTableFields.getDown();
		int upVote = forumDao.fetchUpVoteId(up);
		int downVote = forumDao.fetchDownVoteId(down);
		if (forumPostVote == null) {
			ForumPostVote forumPostUpVote = new ForumPostVote();
			if (voteType == upVote) {
				forumPostUpVote.setPostId(postId);
				forumPostUpVote.setUp_count(1);
				forumPostUpVote.setDown_count(0);
			}
			if (voteType == downVote) {
				forumPostUpVote.setPostId(postId);
				forumPostUpVote.setUp_count(0);
				forumPostUpVote.setDown_count(1);
			}
			int result = forumDao.firstForumPostVote(forumPostUpVote);
			return result;
		} else {
			int result = 0;
			if (voteType == upVote) {
				int upCount = forumDao.fetchUpCountByPostId(postId);
				upCount++;
				result = forumDao.updateForumPostUpVote(upCount, postId);
			}
			if (voteType == downVote) {
				int downCount = forumDao.fetchDownCountByPostId(postId);
				downCount++;
				result = forumDao.updateForumPostDownVote(downCount, postId);
			}
			return result;
		}
	}

	@Transactional
	public int savePostVote(long voteType, long postId, long partyId) {
		String signedUserId = getSignedInUser();
		return forumDao.savePostVote(voteType, postId, partyId, signedUserId);
	}

	@Transactional
	public int removePost(long postId) {
		String signedUserId = getSignedInUser();
		String delete_flag = forumTableFields.getDelete_flag_Y();
		return forumDao.removePost(postId, delete_flag, signedUserId);
	}

	@Transactional
	public int createQuery(List<ForumQuery> questionList) {
		int result = 0;
		for (ForumQuery question : questionList) {
			String signedUserId = getSignedInUser();
			question.setDelete_flag(forumTableFields.getDelete_flag_N());
			question.setCreated_by(signedUserId);
			question.setUpdated_by(signedUserId);
			result = forumDao.createQuery(question);
			if (result == 0) {
				return result;
			}
		}
		return result;
	}

	@Transactional
	public ForumQuery fetchForumQueryByPostedToPartyId(long questionId, long partyId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchForumQueryByPostedToPartyId(questionId, partyId, delete_flag);
	}

	@Transactional
	public int createForumAnswer(ForumAnswer answer) {
		String signedUserId = getSignedInUser();
		answer.setDelete_flag(forumTableFields.getDelete_flag_N());
		answer.setCreated_by(signedUserId);
		answer.setUpdated_by(signedUserId);
		return forumDao.createForumAnswer(answer);
	}

	@Transactional
	public List<ForumQuery> fetchQueryListByPostedToPartyId(long partyId, int pageNum, int records) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchQueryListByPostedToPartyId(partyId, pageable, delete_flag_N);
	}

	@Transactional
	public List<ForumAnswer> fetchAnswerListByQueryId(long id, int pageNum, int records) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchAnswerListByQueryId(id, pageable, delete_flag_N);
	}

	@Transactional
	public List<VoteType> fetchVoteTypeList() {
		return forumDao.fetchVoteTypeList();
	}

	@Transactional
	public int addArticleToFavorite(ArticleFavorite articleFavorite) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		String signedUserId = getSignedInUser();
		articleFavorite.setCreated(timestamp);
		articleFavorite.setUpdated(timestamp);
		articleFavorite.setCreated_by(signedUserId);
		articleFavorite.setUpdated_by(signedUserId);
		articleFavorite.setDelete_flag(delete_flag_N);
		return forumDao.addArticleToFavorite(articleFavorite);
	}

	@Transactional
	public List<ArticlePost> fetchFavoriteArticlesByPartyId(long partyId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<Long> articleIdList = forumDao.fetchFavoriteArticlesByPartyId(partyId, delete_flag_N);
		List<ArticlePost> articleList = forumDao.fetchArticleListByArticleIdList(articleIdList, delete_flag_N,
				encryptPass);
		if (articleList != null) {
			for (ArticlePost articlePost : articleList) {
				Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
				if (party != null) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag_N,
							encryptPass);
					if (advisor != null) {
						articlePost.setUserName(advisor.getUserName());
					}
				}
			}
		}
		return articleList;
	}

	@Transactional
	public int fetchTotalRecentArticlePostList() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchTotalRecentArticlePostList(delete_flag);
	}

	@Transactional
	public int fetchTotalAllArticleInApprovedOrder() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		return forumDao.fetchTotalAllArticleInApprovedOrder(forumStatusId, delete_flag);
	}

	@Transactional
	public int fetchTotalArticlePostList() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchTotalArticlePostList(delete_flag);
	}

	@Transactional
	public int removeArticleFromFavorite(ArticleFavorite articleFavorite) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String delete_flag_Y = forumTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		articleFavorite.setUpdated(timestamp);
		articleFavorite.setUpdated_by(signedUserId);
		articleFavorite.setDelete_flag(delete_flag_Y);
		return forumDao.removeArticleFromFavorite(articleFavorite);
	}

	@Transactional
	public int fetchTotalArticleCommentByArticleId(long id) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchTotalArticleCommentByArticleId(id, delete_flag);
	}

	@Transactional
	public int fetchTotalArticleListByPartyId(long partyId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchTotalArticleListByPartyId(partyId, delete_flag);
	}

	@Transactional
	public int fetchTotalQueryListByPostedToPartyId(long partyId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchTotalQueryListByPostedToPartyId(partyId, delete_flag_N);
	}

	@Transactional
	public int fetchTotalAnswerListByQueryId(long id) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchTotalAnswerListByQueryId(id, delete_flag_N);
	}

	@Transactional
	public int fetchTotalFavoriteArticlesByPartyId(long partyId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		// Pageable pageable = PageRequest.of(pageNum, records);
		return forumDao.fetchTotalFavoriteArticlesByPartyId(partyId, delete_flag_N);
	}

	@Transactional
	public int checkPartyIsPresent(long partyId) {
		return forumDao.checkPartyIsPresent(partyId);
	}

	@Transactional
	public int checkForumSubCategoryIsPresent(long forumSubCategoryId) {
		return forumDao.checkForumSubCategoryIsPresent(forumSubCategoryId);
	}

	@Transactional
	public int checkThreadIsPresent(long threadId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		return forumDao.checkThreadIsPresent(threadId, delete_flag_N);
	}

	@Transactional
	public int checkPostIsPresent(long postId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.checkPostIsPresent(postId, delete_flag);
	}

	@Transactional
	public int checkForumQueryByPostedToPartyId(long questionId, long partyId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.checkForumQueryByPostedToPartyId(questionId, partyId, delete_flag);
	}

	@Transactional
	public int fetchTotalArticleCommentByParentId(long articleId, long parentId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchTotalArticleCommentByParentId(articleId, parentId, delete_flag);
	}

	@Transactional
	public List<ArticleComment> fetchArticleCommentByParentId(long articleId, long parentId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		List<ArticleComment> articleCommentList = forumDao.fetchArticleCommentByParentId(articleId, parentId,
				delete_flag, encryptPass);
		if (articleCommentList != null) {
			for (ArticleComment articleComment : articleCommentList) {
				Party party = forumDao.fetchParty(articleComment.getPartyId(), encryptPass);
				if (party != null && party.getRoleBasedId().startsWith("ADV")) {
					Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
					articleComment.setUserName(advisor.getUserName());
				}
			}
		}
		return articleCommentList;
	}

	@Transactional
	public int fetchArticleUpCount(long articleId) {
		return forumDao.fetchArticleUpCount(articleId);
	}

	@Transactional
	public int modifyArticlePost(long articleId, ArticlePost articlePost) {
		String desc = forumTableFields.getForumstatus_inprogress();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = (forumTableFields.getDelete_flag_N());
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		ArticlePost articlePost1 = forumDao.fetchArticlePostByarticleId(articleId, delete_flag, encryptPass);

		String signedUserId = getSignedInUser();
		articlePost1.setCreated_by(signedUserId);
		articlePost1.setUpdated_by(signedUserId);
		articlePost1.setForumStatusId(forumStatusId);
		articlePost1.setDelete_flag(delete_flag);
		if (articlePost.getImagePath() != null) {
			articlePost1.setImagePath(articlePost.getImagePath());
		}
		if (articlePost.getTitle() != null) {
			articlePost1.setImagePath(articlePost.getTitle());
		}
		if (articlePost.getContent() != null) {
			articlePost1.setContent(articlePost.getContent());
		}
		if (articlePost.getForumStatusId() != 0) {
			articlePost1.setForumStatusId(articlePost.getForumStatusId());
		}
		if (articlePost.getProdId() != 0) {
			articlePost1.setProdId(articlePost.getProdId());
		}
		return forumDao.modifyArticlePost( articlePost1, encryptPass);
	}

	@Transactional
	public int fetchTotalArticleListByStatusId(long forumStatusId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchTotalArticleListByStatusId(forumStatusId, delete_flag);
	}

	@Transactional
	public List<ArticlePost> fetchArticleListByStatusId(int pageNum, int records, long forumStatusId) {
		String delete_flag_N = forumTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<ArticlePost> articleList = forumDao.fetchArticleListByStatusId(pageable, forumStatusId, delete_flag_N,
				encryptPass);
		if (articleList != null && articleList.size() != 0) {
			for (ArticlePost articlePost : articleList) {
				String forumStatus = forumDao.fetchForumStatus(articlePost.getForumStatusId());
				String articleStatus = forumDao.fetchArticleStatus(articlePost.getArticleStatusId());
				articlePost.setForumStatus(forumStatus);
				articlePost.setArticleStatus(articleStatus);
			}
		}
		return articleList;
	}

	@Transactional
	public ArticleVoteAddress fetchArticleVoteAddress(long articleId, long partyId) {
		return forumDao.fetchArticleVoteAddress(articleId, partyId);
	}

	@Transactional
	public int decreaseLikeCount(long articleId) {
		int upCount = forumDao.fetchUpCountByArticleId(articleId);
		int count = upCount - 1;
		int result = forumDao.updateArticleUpVote(count, articleId);
		return result;
	}

	@Transactional
	public int removeArticleVoteAddress(long articleId, long partyId) {
		int result = forumDao.removeArticleVoteAddress(articleId, partyId);
		return result;
	}

	@Transactional
	public Advisor fetchByPublicAdvisorID(String roleBasedId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Advisor advisor = forumDao.fetchPublicAdvisorByAdvId(roleBasedId, deleteflag, encryptPass);
		return advisor;
	}

	@Transactional
	public List<ArticlePost> searchArticleByTitle(int pageNum, int records, String title, String forumStatusId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Pageable pageable = PageRequest.of(pageNum, records);
		List<ArticlePost> articlePosts = forumDao.searchArticleByTitle(pageable, title, forumStatusId, deleteflag,
				encryptPass);
		return articlePosts;
	}

	@Transactional
	public int fetchTotalSearchArticleByTitle(String title, String forumStatusId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		int searchArticleList = forumDao.fetchTotalSearchArticleByTitle(deleteflag, title, forumStatusId);
		return searchArticleList;
	}

	@Transactional
	public List<ArticlePost> fetchAllArticleInApprovedOrderWithoutToken() {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();
		String encryptPass = advTableFields.getEncryption_password();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		String articleStatus = forumTableFields.getArticlestatus_active();
		int articleStatusId = forumDao.fetchArticleStatusIdByDesc(articleStatus);
		List<ArticlePost> articlePostList = forumDao.fetchAllArticleInApprovedOrderWithoutToken(forumStatusId,
				articleStatusId, delete_flag, encryptPass);
		for (ArticlePost articlePost : articlePostList) {
			Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
			if (party != null) {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
				if (advisor != null) {
					articlePost.setUserName(advisor.getUserName());
				}
			}
		}
		return articlePostList;
	}

	@Transactional
	public List<ArticlePost> fetchAllArticleInApprovedOrderWithoutToken(int prodId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();
		String encryptPass = advTableFields.getEncryption_password();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		String articleStatus = forumTableFields.getArticlestatus_active();
		int articleStatusId = forumDao.fetchArticleStatusIdByDesc(articleStatus);
		List<ArticlePost> articlePostList = forumDao.fetchAllArticleInApprovedOrderWTByProdId(prodId, forumStatusId,
				articleStatusId, delete_flag, encryptPass);
		for (ArticlePost articlePost : articlePostList) {
			Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
			if (party != null) {
				Advisor advisor = advisorDao.fetchAdvisorByAdvId(party.getRoleBasedId(), delete_flag, encryptPass);
				if (advisor != null) {
					articlePost.setUserName(advisor.getUserName());
				}
			}
		}
		return articlePostList;
	}

	@Transactional
	public String fetchArticlePostByTitle(String title) {
		String validUrl = title.replace(' ', '-');
		String url1_lc = validUrl.toLowerCase();
		String delete_flag = forumTableFields.getDelete_flag_N();
		return forumDao.fetchArticlePostByTitle(url1_lc, delete_flag);
	}

	@Transactional
	public int fetchTotalAllArticleInApprovedOrder(int prodId) {
		String delete_flag = forumTableFields.getDelete_flag_N();
		String desc = forumTableFields.getForumstatus_approved();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		return forumDao.fetchTotalAllArticleInApprovedOrder(forumStatusId, delete_flag, prodId);
	}

	@Override
	public int removeArticleFromMyPost(ArticlePost articlePost) {
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String delete_flag_Y = forumTableFields.getDelete_flag_Y();
		String signedUserId = getSignedInUser();
		articlePost.setUpdated(timestamp);
		articlePost.setUpdated_by(signedUserId);
		return forumDao.removeArticleFromMyPost(articlePost);
	}

	@Override
	public Blogger fetchBloggerByBloggerId(String roleBasedId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		String encryptPass = advTableFields.getEncryption_password();
		Blogger blogger = advisorDao.fetchBloggerByBloggerId(roleBasedId, deleteflag, encryptPass);
		if (blogger.getBloggerId() == null && blogger.getEmailId() == null) {
			return null;
		}
		return blogger;
	}

	@Override
	public int createBloggerArticlePost(ArticlePost articlePost, String url) {
		String desc = forumTableFields.getForumstatus_inprogress();
		String encryptPass = advTableFields.getEncryption_password();
		String delete_flag = advTableFields.getDelete_flag_N();
		int forumStatusId = forumDao.fetchForumStatusIdByDesc(desc);
		Party party = forumDao.fetchParty(articlePost.getPartyId(), encryptPass);
		Blogger blog = advisorDao.fetchBloggerByBloggerId(party.getRoleBasedId(), delete_flag, encryptPass);
		String signedUserId = getSignedInUser();
		articlePost.setCreated_by(signedUserId);
		articlePost.setUpdated_by(signedUserId);
		articlePost.setForumStatusId(forumStatusId);
		articlePost.setDelete_flag(forumTableFields.getDelete_flag_N());
		articlePost.setName(blog.getFullName());
		articlePost.setImagePath(blog.getImagePath());
		articlePost.setUserName(blog.getUserName());
		articlePost.setRoleBasedId(blog.getBloggerId());
		String articleDesc = forumTableFields.getArticlestatus_active();
		int articleStatusId = forumDao.fetchArticleStatusIdByDesc(articleDesc);
		articlePost.setArticleStatusId(articleStatusId);
		String validUrl = null;
		if (url != null) {
			validUrl = url.replace(' ', '-');
			System.out.println("u" + validUrl);
			int count = forumDao.fetchCountForUrl(validUrl);
			String url1 = (count > 0) ? validUrl + "-" + count : validUrl;
			String url1_lc = url1.toLowerCase();
			articlePost.setUrl(url1_lc);
		} else {
			validUrl = articlePost.getTitle().replace(' ', '-');
			String title_lc = validUrl.toLowerCase();
			articlePost.setUrl(title_lc);
		}
		return forumDao.createArticlePost(articlePost, encryptPass);
	}

}