package com.sowisetech.forum.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.response.AdvResponse;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.MailConstants;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.common.util.SendMail;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.model.VoteType;
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
import com.sowisetech.forum.response.ArticleCommentList;
import com.sowisetech.forum.response.ArticleFavoriteList;
import com.sowisetech.forum.response.ArticlePostList;
import com.sowisetech.forum.response.ArticleVoteCount;
import com.sowisetech.forum.response.ForumAnswerList;
import com.sowisetech.forum.response.ForumQueryList;
import com.sowisetech.forum.response.ForumResponse;
import com.sowisetech.forum.response.ForumResponseData;
import com.sowisetech.forum.response.ForumResponseMessage;
import com.sowisetech.forum.service.ForumService;
import com.sowisetech.forum.util.ForumAppMessages;
import com.sowisetech.forum.util.ForumConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ForumController {

	@Autowired
	ForumService forumService;

	@Autowired
	ForumAppMessages appMessages;

	@Autowired
	SendMail sendMail;

	@Autowired
	MailConstants mailConstants;
	@Autowired
	ScreenRightsConstants screenRightsConstants;
	@Autowired
	ScreenRightsCommon screenRightsCommon;
	@Autowired
	CommonService commonService;

	private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

	/**
	 * Extended Content Verification - Health check
	 * 
	 * @return ResponseEntity - HttpStatus.OK
	 */
	@RequestMapping(value = "/forum-ecv", method = RequestMethod.GET)
	public ResponseEntity getForumEcv() {
		logger.info("Forum module running.");
		// To view the log file location
		System.out.println(System.getProperty("user.dir"));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Method to add article to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the ArticlePostRequest
	 * @return ResponseEntity<String> contains the either the Result of article
	 *         addition or ErrorResponse
	 */
	/*---Article Post---*/
	@ApiOperation(value = "create the article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/articlePost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createArticlePost(@NonNull @RequestBody ArticlePostRequest articlePostReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articlePostReq != null) {
			int screenId = articlePostReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (articlePostReq.getContent() == null || articlePostReq.getPartyId() == 0) {
			logger.error("Mandatory fields");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_addArticle());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			Party party = forumService.fetchParty(articlePostReq.getPartyId());
			logger.error("no record found");
			if (party == null) {
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("checking party is Advisor");
				Advisor advisor = forumService.fetchByPublicAdvisorID(party.getRoleBasedId());
				if (advisor == null || advisor.getAdvId() == null) {
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getAdvisor_can_post());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// Checking party is present //
					ArticlePost articlePost = getValueArticlePost(articlePostReq);// get value Method call for input
					// values
					String url = forumService.fetchArticlePostByTitle(articlePostReq.getTitle());
					logger.info("Adding article into table");
					int result = forumService.createArticlePost(articlePost, url); // creating article post//
					if (result != 0) {
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getArticlepost_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while adding data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}
	
	/**
	 * Method to add article to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the ArticlePostRequest
	 * @return ResponseEntity<String> contains the either the Result of article
	 *         addition or ErrorResponse
	 */
	/*---Article Post---*/
	@ApiOperation(value = "create the blogger article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/BloggerarticlePost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createBloggerArticlePost(@NonNull @RequestBody ArticlePostRequest articlePostReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articlePostReq != null) {
			int screenId = articlePostReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (articlePostReq.getContent() == null || articlePostReq.getPartyId() == 0) {
			logger.error("Mandatory fields");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_addArticle());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			Party party = forumService.fetchParty(articlePostReq.getPartyId());
			logger.error("no record found");
			if (party == null) {
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("checking party is blogger");
				Blogger blog = forumService.fetchBloggerByBloggerId(party.getRoleBasedId());
				if (blog == null || blog.getBloggerId() == null) {
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getAdvisor_can_post());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// Checking party is present //
					ArticlePost articlePost = getValueArticlePost(articlePostReq);// get value Method call for input
					// values
					String url = forumService.fetchArticlePostByTitle(articlePostReq.getTitle());
					logger.info("Adding article into table");
					int result = forumService.createBloggerArticlePost(articlePost, url); // creating article post//
					if (result != 0) {
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getArticlepost_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while adding data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}


	/**
	 * Method to add article to the Maria database.
	 * 
	 * @param RequestBody
	 *            contains the ArticlePostRequest
	 * @return ResponseEntity<String> contains the either the Result of article
	 *         modification or ErrorResponse
	 */
	/*---Article Post---*/
	@ApiOperation(value = "modify the article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyArticlePost", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyArticlePost(@NonNull @RequestBody ArticlePostRequest articlePostReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articlePostReq != null) {
			int screenId = articlePostReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (articlePostReq.getContent() == null || articlePostReq.getPartyId() == 0 ) {
			logger.error("Some fields are empty");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_modify_article());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			int party = forumService.checkPartyIsPresent(articlePostReq.getPartyId());
			if (party == 0) {
				logger.error("no record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("checking party is Advisor");
				ArticlePost articlePost = getValueArticlePost(articlePostReq);
				long articleId =articlePostReq.getArticleId();
				logger.info("Modifying article into table");
				int result = forumService.modifyArticlePost(articleId, articlePost);
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getRecord_modified_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private List<RoleFieldRights> rightsAuthentication(int screenId, HttpServletRequest request, String accessType) {
		List<Integer> roleScreenId = screenRightsCommon.screenRights(screenId, request, accessType);
		if (roleScreenId == null || roleScreenId.size() == 0) {
			return null;
		} else {
			List<RoleFieldRights> roleFieldRights = commonService.fetchFieldRights(roleScreenId);
			return roleFieldRights;
		}
	}

	/**
	 * Method to remove article
	 * 
	 * @param RequestBody
	 *            contains the IdRequest
	 * @return ResponseEntity<String> contains the either the Result of article
	 *         remove or ErrorResponse
	 */
	/*---Delete Article---*/
	@ApiOperation(value = "remove the article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteArticle(@NonNull @RequestBody ForumIdRequest idRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = idRequest.getId();
		if (articleId == 0) {
			logger.info("ArticleId is Mandatory");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articleId());
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching article");
			int articlePost = forumService.fetchArticlePost(articleId); // Checking article post is present //
			if (articlePost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Removing article");
				int result = forumService.removeArticle(articleId); // removing article post by articleId //
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages. getArticlePost_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while removing data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private ArticlePost getValueArticlePost(ArticlePostRequest articlePostReq) {
		ArticlePost postval = new ArticlePost();
		if (articlePostReq != null && articlePostReq.getTitle() != null) {
			postval.setTitle(articlePostReq.getTitle());
		}
		if (articlePostReq != null && articlePostReq.getContent() != null) {
			postval.setContent(articlePostReq.getContent());
		}
		if (articlePostReq != null && articlePostReq.getPartyId() != 0) {
			postval.setPartyId(articlePostReq.getPartyId());
		}
		if (articlePostReq != null && articlePostReq.getProdId() != 0) {
			postval.setProdId(articlePostReq.getProdId());
		}
		if (articlePostReq != null && articlePostReq.getArticleId() != 0) {
			postval.setArticleId(articlePostReq.getArticleId());
		}
		return postval;
	}

	/**
	 * Method to moderate article
	 * 
	 * @param RequestBody
	 *            contains the ModerateRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         forum status or ErrorResponse
	 */
	@ApiOperation(value = "moderate the article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/moderateArticlePost", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> moderateArticlePost(@NonNull @RequestBody ModerateRequest moderateReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (moderateReq != null) {
			int screenId = moderateReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (moderateReq.getArticleId() == 0 || moderateReq.getAdminId() == null
				|| moderateReq.getForumStatusId() == 0) {
			logger.error("Some fields are empty");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_moderate_article());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			long articleId = moderateReq.getArticleId();
			logger.info("Fetching article");
			int articlePost = forumService.fetchArticlePost(articleId); // Checking article post is present //
			if (articlePost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Checking the party is advisor or not");
				if (forumService.checkForumStatusIsRejected(moderateReq.getForumStatusId()) == true
						&& moderateReq.getReason() == null) {
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getRequired_reason());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// moderating article post by given values //
					logger.info("Moderating article");
					int result = forumService.moderateArticlePost(articleId, moderateReq.getForumStatusId(),
							moderateReq.getAdminId(), moderateReq.getReason());
					if (result != 0) {
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getArticle_moderated_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while modifying data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}

	/**
	 * Method to vote the article
	 * 
	 * @param RequestBody
	 *            contains the ArticleVoteRequest
	 * @return ResponseEntity<String> contains the either the Result of voting of
	 *         article or ErrorResponse
	 */
	@ApiOperation(value = "vote the article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/voteArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createArticleVote(@NonNull @RequestBody ArticleVoteRequest articleVoteReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleVoteReq != null) {
			int screenId = articleVoteReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = articleVoteReq.getArticleId();
		if (articleVoteReq.getVoteType() == 0 || articleId == 0 || articleVoteReq.getPartyId() == 0) {
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_voteArticle());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			logger.info("Fetching article");
			int articlePost = forumService.fetchArticlePost(articleId); // Checking articlePost is present//
			if (articlePost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// creating articleVote //
			logger.info("Voting the article");
			int result1 = forumService.createArticleVote(articleVoteReq.getVoteType(), articleId);
			// save articleVote //
			if (result1 != 0) {
				int result2 = forumService.saveArticleVote(articleVoteReq.getVoteType(), articleId,
						articleVoteReq.getPartyId());
				logger.info("Save vote address");
				if (result2 != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getArticlevote_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while saving data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else {
				logger.error("Error occured while adding data into table");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	/**
	 * Method to add the comment for article
	 * 
	 * @param RequestBody
	 *            contains the ArticleCommentRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         article comment or ErrorResponse
	 */
	@ApiOperation(value = "create the article comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/articlePostComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createArticlePostComment(@NonNull @RequestBody ArticleCommentRequest articleCommentReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleCommentReq != null) {
			int screenId = articleCommentReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = articleCommentReq.getArticleId();
		if (articleCommentReq.getContent() == null || articleCommentReq.getPartyId() == 0 || articleId == 0) {
			logger.info("Mandatory fields");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articlePostComment());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			// checking party is present//
			logger.info("Fetching party");
			int party = forumService.checkPartyIsPresent(articleCommentReq.getPartyId());
			if (party == 0) {
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Fetching article");
				int articlePost = forumService.fetchArticlePost(articleId); // Checking articlePost is present //
				if (articlePost == 0) {
					logger.info("No record found");
					ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
							appMessages.getNo_record_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// get value Method call for input values //
					ArticleComment comment = getValueArticleComment(articleId, articleCommentReq);
					logger.info("Adding comment into DB");
					int result = forumService.createArticlePostComment(comment); // creating articleComment by given
					// values//
					if (result != 0) {
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getArticlecomment_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while adding data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}

	private ArticleComment getValueArticleComment(long articleId, ArticleCommentRequest articleCommentReq) {
		ArticleComment commentval = new ArticleComment();
		if (articleCommentReq != null && articleCommentReq.getContent() != null) {
			commentval.setContent(articleCommentReq.getContent());
		}
		if (articleCommentReq != null && articleCommentReq.getPartyId() != 0) {
			commentval.setPartyId(articleCommentReq.getPartyId());
		}
		if (articleCommentReq != null && articleCommentReq.getParentCommentId() != 0) {
			commentval.setParentCommentId(articleCommentReq.getParentCommentId());
		}
		commentval.setArticleId(articleId);
		return commentval;
	}

	/**
	 * Method to moderate article
	 * 
	 * @param RequestBody
	 *            contains the ModerateRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         forum status or ErrorResponse
	 */
	@ApiOperation(value = "moderate the article comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/moderateArticlePostComment", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> moderateArticlePostComment(@NonNull @RequestBody ModerateRequest moderateReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (moderateReq != null) {
			int screenId = moderateReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (moderateReq.getCommentId() == 0 || moderateReq.getAdminId() == null
				|| moderateReq.getForumStatusId() == 0) {
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_moderate_comment());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			long commentId = moderateReq.getCommentId();
			// Checking articleComment is present //
			logger.info("Fetching article comment");
			int articleComment = forumService.fetchArticleComment(commentId);
			if (articleComment == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// moderating articleComment by given values//
				logger.info("Moderating articleComment");
				int result = forumService.moderateArticleComment(commentId, moderateReq.getForumStatusId(),
						moderateReq.getAdminId());
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getArticlecomment_moderated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while modifying data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to remove article comment
	 * 
	 * @param RequestBody
	 *            contains the IdRequest
	 * @return ResponseEntity<String> contains the either the Result of article
	 *         comment remove or ErrorResponse
	 */
	@ApiOperation(value = "remove the article comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeArticleComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteArticleComment(@NonNull @RequestBody ForumIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long commentId = idRequest.getId();
		if (commentId == 0) {
			logger.info("CommentId is Mandatory");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_commentId());
			return ResponseEntity.ok().body(response);
		} else {
			// Checking articleComment is present //
			logger.info("Fetching article comment");
			int articleComment = forumService.fetchArticleComment(commentId);
			if (articleComment == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// removing articleComment by id //
				logger.info("Removing Article Comment");
				int result = forumService.removeArticleComment(commentId);
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getRecord_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while removing data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Fetch recently added Articles
	 * 
	 * @return ResponseEntity<List<ArticlePost>> or ErrorResponse
	 * @param null
	 * 
	 */
	@ApiOperation(value = "Fetch all recent article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllRecentArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRecentArticlePostList(@RequestBody(required = false) ScreenIdRequest screenIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int articlePosts = forumService.fetchTotalRecentArticlePostList();

		// if (pageNum == 0) {
		// ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
		// appMessages.getInvalid_pagenum());
		// return ResponseEntity.ok().body(response);
		// } else {
		// if (pageNum == 1) {
		// } else {
		// pageNum = (pageNum - 1) * records + 1;
		// }
		logger.info("Fetching recent articles");
		List<ArticlePost> articlePostList = forumService.fetchRecentArticlePostList();
		// if (articlePostList.size() == 0) {
		// logger.info("No record found");
		// ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
		// appMessages.getNo_record_found());
		// return
		// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		// } else {
		ArticlePostList articlePostLists = new ArticlePostList();
		if (articlePostList != null) {
			articlePostLists.setArticlePostList(articlePostList);
			articlePostLists.setTotalRecords(articlePosts);
		}
		ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
				articlePostLists, roleFieldRights);
		return ResponseEntity.ok().body(response);
		// }
		// }
	}

	/**
	 * Fetch forum category with sub category
	 * 
	 * @return ResponseEntity<List<ForumCategory>> or ErrorResponse
	 * @param null
	 * 
	 */
	@ApiOperation(value = "fetch forumCategory List", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllForumCategory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllForumCategory() {
		logger.info("Fetching forumCategory List");
		List<ForumCategory> forumCategoryList = forumService.fetchAllForumCategory();
		ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
				forumCategoryList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch most recently approved article
	 * 
	 * @return ResponseEntity<ArticlePost> or ErrorResponse
	 * @param null
	 * 
	 */
	@ApiOperation(value = "fetch recent approved article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchRecentApprovedArticle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRecentApprovedArticle(@RequestBody(required = false) ScreenIdRequest screenIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		logger.info("Fetching recent approved article");
		ArticlePost articlePost = forumService.fetchRecentApprovedArticle();
		ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(), articlePost,
				roleFieldRights);
		return ResponseEntity.ok().body(response);

	}

	/**
	 * Fetch all article in recently approved order
	 * 
	 * @return ResponseEntity<List<ArticlePost>> or ErrorResponse
	 * @param null
	 * 
	 */
	@ApiOperation(value = "fetch article List by approved order", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllArticleInApprovedOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllArticleInApprovedOrder(
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int articlePosts = forumService.fetchTotalAllArticleInApprovedOrder();
		// if (pageNum == 0) {
		// ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
		// appMessages.getInvalid_pagenum());
		// return ResponseEntity.ok().body(response);
		// } else {
		// if (pageNum == 1) {
		// } else {
		// pageNum = (pageNum - 1) * records + 1;
		// }
		logger.info("Fetching approved articles in order");
		List<ArticlePost> articlePostList = forumService.fetchAllArticleInApprovedOrder();
		ArticlePostList articlePostLists = new ArticlePostList();
		if (articlePostList != null) {
			articlePostLists.setArticlePostList(articlePostList);
			articlePostLists.setTotalRecords(articlePosts);
		}
		ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
				articlePostLists, roleFieldRights);
		return ResponseEntity.ok().body(response);

		// }
		// }

	}

	@ApiOperation(value = "fetch article List by approved order", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllArticleInApprovedOrderWihtoutToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllArticleInApprovedOrderWithoutToken(@RequestParam(defaultValue = "0") int prodId) {

		if (prodId != 0) {
			int articlePosts = forumService.fetchTotalAllArticleInApprovedOrder(prodId);
			logger.info("Fetching prodId in approved");
			List<ArticlePost> articlePostList = forumService.fetchAllArticleInApprovedOrderWithoutToken(prodId);
			ArticlePostList articlePostLists = new ArticlePostList();
			if (articlePostList != null) {
				articlePostLists.setArticlePostList(articlePostList);
				articlePostLists.setTotalRecords(articlePosts);
			}

			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articlePostLists, null);
			return ResponseEntity.ok().body(response);
		} else {
			int articlePosts = forumService.fetchTotalAllArticleInApprovedOrder();

			logger.info("Fetching approved articles in order");
			List<ArticlePost> articlePostList = forumService.fetchAllArticleInApprovedOrderWithoutToken();
			ArticlePostList articlePostLists = new ArticlePostList();
			if (articlePostList != null) {
				articlePostLists.setArticlePostList(articlePostList);
				articlePostLists.setTotalRecords(articlePosts);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articlePostLists, null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Fetch all article comment by articleId
	 * 
	 * @return ResponseEntity<List<ArticleComment>> or ErrorResponse
	 * @param ForumIdRequest
	 * 
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch article comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchArticleComment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleComment(@NonNull @RequestBody ForumIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (idRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articleComment(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching articleComment");

			int articleComments = forumService.fetchTotalArticleCommentByArticleId(idRequest.getId());

			List<ArticleComment> articleComment = forumService.fetchArticleCommentByArticleId(idRequest.getId());

			ArticleCommentList articleCommentLists = new ArticleCommentList();
			if (articleComment != null) {
				articleCommentLists.setArticleCommentList(articleComment);
				articleCommentLists.setTotalRecords(articleComments);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articleCommentLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/***
	 * fetchArticlePost
	 * 
	 * @param null
	 * @return ResponseEntity<List<ArticlePost>> or ErrorResponse
	 */
	@ApiOperation(value = "fetch article List", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllArticlePost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllArticlePost(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int articlePosts = forumService.fetchTotalArticlePostList();
		logger.info("Fetching article list");
		List<ArticlePost> articlePostList = forumService.fetchArticlePostList(pageNum, records);
		ArticlePostList articlePostLists = new ArticlePostList();
		if (articlePostList != null) {
			articlePostLists.setArticlePostList(articlePostList);
			articlePostLists.setTotalRecords(articlePosts);
		}
		ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
				articlePostLists, roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	/***
	 * fetchArticlePostByArticleId
	 * 
	 * @param ForumIdRequest
	 * @return ResponseEntity<ArticlePost> or ErrorResponse
	 */
	@ApiOperation(value = "fetch article by articleId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchArticlePostByArticleId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticlePostByArticleId(@NonNull @RequestBody ForumIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// fetch articlePost record from table by ArticleId
		if (idRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articlePost_articleId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching article");
			ArticlePost articlePost = forumService.fetchArticlePostByarticleId(idRequest.getId());
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articlePost, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/***
	 * fetchArticleListCommentByPartyId
	 * 
	 * @param ForumIdRequest
	 * @return ResponseEntity<List<ArticlePost>> or ErrorResponse
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch article by partyId", authorizations =

	@Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchArticleListBypartyId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleListByPartyId(@NonNull @RequestBody ForumIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (idRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_fetchArticleList_By_PartyId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {

			logger.info("Fetching article list by partyId");

			int articlePosts = forumService.fetchTotalArticleListByPartyId(idRequest.getId());

			List<ArticlePost> articlePostList = forumService.fetchArticleListByPartyId(idRequest.getId());
			ArticlePostList articlePostLists = new ArticlePostList();
			if (articlePostList != null) {
				articlePostLists.setArticlePostList(articlePostList);
				articlePostLists.setTotalRecords(articlePosts);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articlePostLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/***
	 * changeArticleStatus
	 * 
	 * @param ChangeStatusRequest
	 * @return ResponseEntity<String> or ErrorResponse
	 */
	@ApiOperation(value = "change article status", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/changeArticleStatus", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> changeArticleStatus(@NonNull @RequestBody ChangeStatusRequest changeStatusReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (changeStatusReq != null) {
			int screenId = changeStatusReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = changeStatusReq.getArticleId();
		int articleStatusId = changeStatusReq.getArticleStatusId();
		if (articleId == 0 || articleStatusId == 0) {
			logger.info("articleId&ArticleStatusId is Mandatory");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articleId_articleStatusId());
			return ResponseEntity.ok().body(response);
		} else {
			int articlePost = forumService.fetchArticlePost(articleId); // Checking article post is present //
			if (articlePost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// change article status //
				logger.info("Changing Article status");
				int result = forumService.changeArticleStatus(articleId, changeStatusReq.getArticleStatusId());
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getChanged_articlestatus_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private ForumResponse messageResponse(long code, String message) {
		ForumResponseMessage responseMessage = new ForumResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		ForumResponse response = new ForumResponse();
		response.setResponseMessage(responseMessage);
		return response;
	}

	private ForumResponse responseWithData(long code, String message, Object data,
			List<RoleFieldRights> roleFieldRights) {
		ForumResponseMessage responseMessage = new ForumResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		ForumResponseData responseData = new ForumResponseData();
		responseData.setData(data);
		responseData.setRoleFieldRights(roleFieldRights);
		ForumResponse response = new ForumResponse();
		response.setResponseMessage(responseMessage);
		response.setResponseData(responseData);
		return response;
	}

	/**
	 * Method to add Forum thread to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumThreadRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of Forum thread
	 *         addition or ErrorResponse
	 */
	@ApiOperation(value = "create thread", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addThread", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createForumThread(@NonNull @RequestBody ForumThreadRequest forumThreadReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (forumThreadReq != null) {
			int screenId = forumThreadReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		// if (forumService.fetchParty(forumThreadReq.getPartyId()) == null) { //
		if (forumThreadReq.getSubject() == null || forumThreadReq.getPartyId() == 0
				|| forumThreadReq.getForumSubCategoryId() == 0 || forumThreadReq.getForumCategoryId() == 0) {
			logger.error("some fields are empty");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_add_thread());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			int party = forumService.checkPartyIsPresent(forumThreadReq.getPartyId()); // Checking the partyId is
																						// present//
			if (party == 0) {
				logger.error("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int forum = forumService.checkForumSubCategoryIsPresent(forumThreadReq.getForumSubCategoryId());
				if (forum == 0) {
					logger.error("No record found");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getForumsubcategory_not_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					ForumThread thread = getValueThread(forumThreadReq);// get value Method call for input values //
					logger.info("create thread");
					int result = forumService.createThread(thread); // creating ForumThread //
					if (result != 0) {
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getForumthread_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while adding data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}

	}

	/**
	 * Method to Moderate Thread to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ModerateRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of moderate
	 *         thread addition or ErrorResponse
	 */
	@ApiOperation(value = "modify thread", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/moderateThread", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> moderateForumThread(@NonNull @RequestBody ModerateRequest moderateReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (moderateReq != null) {
			int screenId = moderateReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (moderateReq.getThreadId() == 0 || moderateReq.getAdminId() == null || moderateReq.getForumStatusId() == 0) {
			logger.error("some fields are empty");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_moderat_thread());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			long threadId = moderateReq.getThreadId();
			logger.info("Fetching thread");
			// Checking ForumThread is present//
			int forumThread = forumService.checkThreadIsPresent(threadId);
			if (forumThread == 0) {
				logger.error("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// moderating forumThread by given values //
				logger.info("moderating thread");
				int result = forumService.moderateThread(threadId, moderateReq.getForumStatusId(),
						moderateReq.getAdminId());
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getThread_moderated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to remove Thread from the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of remove
	 *         thread or ErrorResponse
	 */
	@ApiOperation(value = "remove thread", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeThread", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteThread(@NonNull @RequestBody ForumIdRequest idRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long threadId = idRequest.getId();
		if (threadId == 0) {
			logger.info("threadId is Mandatory");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_threadId());
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching thread");
			// Checking ForumThread is present //
			int forumThread = forumService.checkThreadIsPresent(threadId);
			if (forumThread == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Removing thread");
				int result = forumService.removeThread(threadId); // removing the forumThread by threadId//
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getRecord_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private ForumThread getValueThread(ForumThreadRequest forumThreadReq) {
		ForumThread thrdval = new ForumThread();
		if (forumThreadReq != null && forumThreadReq.getSubject() != null) {
			thrdval.setSubject(forumThreadReq.getSubject());
		}
		if (forumThreadReq != null && forumThreadReq.getPartyId() != 0) {
			thrdval.setPartyId(forumThreadReq.getPartyId());
		}
		if (forumThreadReq != null && forumThreadReq.getForumSubCategoryId() != 0) {
			thrdval.setForumSubCategoryId(forumThreadReq.getForumSubCategoryId());
		}
		if (forumThreadReq != null && forumThreadReq.getForumCategoryId() != 0) {
			thrdval.setForumCategoryId(forumThreadReq.getForumCategoryId());
		}
		return thrdval;
	}

	/**
	 * Method to add post against to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumPostRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of add post or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "create post", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addPost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createForumPost(@NonNull @RequestBody ForumPostRequest forumPostReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (forumPostReq != null) {
			int screenId = forumPostReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long threadId = forumPostReq.getThreadId();
		if (forumPostReq.getThreadId() == 0 || forumPostReq.getContent() == null || forumPostReq.getPartyId() == 0) {
			logger.error("some fields are empty");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_add_post());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			// if (forumService.fetchParty(forumPostReq.getPartyId()) == null) {
			int party = forumService.checkPartyIsPresent(forumPostReq.getPartyId()); // Checking party is present //
			if (party == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Fetching thread");
				// ForumThread forumThread = forumService.fetchThread(threadId); // Checking
				// ForumThread is present//
				int forumThread = forumService.checkThreadIsPresent(threadId);
				if (forumThread == 0) {
					logger.info("No record found");
					ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
							appMessages.getNo_record_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// Convert input json request to ForumPostRequest object.
					ForumPost post = getValuePost(threadId, forumPostReq);// get value Method call input values//
					int result = forumService.createPost(post); // creating forum post//
					if (result != 0) {
						logger.error("Adding post");
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getForumpost_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while adding data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}

	/**
	 * Method to moderate post against to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ModerateRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of add post or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "modify post", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/moderatePost", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> moderateForumPost(@NonNull @RequestBody ModerateRequest moderateReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (moderateReq != null) {
			int screenId = moderateReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (moderateReq.getPostId() == 0 || moderateReq.getAdminId() == null || moderateReq.getForumStatusId() == 0) {
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_moderat_post());
			logger.error("some fields are empty");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			long postId = moderateReq.getPostId();
			logger.info("Fetching post");
			// Checking forum post is present//
			int forumPost = forumService.checkPostIsPresent(postId); // Checking forum post is present//
			if (forumPost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// moderating forum post by given values //
				int result = forumService.moderatePost(postId, moderateReq.getForumStatusId(),
						moderateReq.getAdminId());
				if (result != 0) {
					logger.info("moderating post");
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getPost_moderated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to ForumPostVoteRequest against to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumPostVoteRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of vote post or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "create post vote", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/forumPostVote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createPostVote(@NonNull @RequestBody ForumPostVoteRequest forumPostVoteReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (forumPostVoteReq != null) {
			int screenId = forumPostVoteReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long postId = forumPostVoteReq.getPostId();
		if (forumPostVoteReq.getVoteType() == 0 || postId == 0 || forumPostVoteReq.getPartyId() == 0) {
			logger.error("Mandatory fields");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_forumPostVote());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			logger.info("fetching post");
			int forumPost = forumService.checkPostIsPresent(postId); // Checking forum post is present//
			if (forumPost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// creating forumPostVote //
			int result1 = forumService.createForumPostVote(forumPostVoteReq.getVoteType(), postId);
			// Save postVote //
			int result2 = forumService.savePostVote(forumPostVoteReq.getVoteType(), postId,
					forumPostVoteReq.getPartyId());

			if (result1 != 0 && result2 != 0) {
				logger.info("Forum vote added into table");
				ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
						appMessages.getForumvote_added_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.error("Error occured while adding data into table");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	/**
	 * Method to ForumIdRequest against to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of remove post
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "remove post vote", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removePost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deletePost(@NonNull @RequestBody ForumIdRequest idRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long postId = idRequest.getId();
		if (postId == 0) {
			logger.info("postId is Mandatory");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_postId());
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching post");
			// Checking forum post is present //
			int forumPost = forumService.checkPostIsPresent(postId); // Checking forum post is present//
			if (forumPost == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Removing post");
				int result = forumService.removePost(postId); // removing forum post by postId //
				if (result != 0) {
					ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getRecord_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private ForumPost getValuePost(long threadId, ForumPostRequest forumPostReq) {
		ForumPost postval = new ForumPost();
		if (forumPostReq != null && forumPostReq.getContent() != null) {
			postval.setContent(forumPostReq.getContent());
		}
		if (forumPostReq != null && forumPostReq.getPartyId() != 0) {
			postval.setPartyId(forumPostReq.getPartyId());
		}
		postval.setThreadId(threadId);
		return postval;
	}

	/**
	 * Method to add Forum Query to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>QueryRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of Forum Query
	 *         addition or ErrorResponse
	 */
	@ApiOperation(value = "create query", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createQuery", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createQuery(@NonNull @RequestBody QueryRequest queryRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (queryRequest != null) {
			int screenId = queryRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (queryRequest.getQuery() == null || queryRequest.getForumSubCategoryId() == 0
				|| queryRequest.getForumCategoryId() == 0 || queryRequest.getPartyId() == 0
				|| queryRequest.getPostedToPartyId() == null) {
			logger.error("Mandatory fields");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_createQuery());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			for (long partyId : queryRequest.getPostedToPartyId()) {
				logger.error("No record found");
				int party = forumService.checkPartyIsPresent(partyId); // Checking the partyId is present
				if (party == 0) {
					ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
							appMessages.getParty_not_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			int forumSubCategory = forumService.checkForumSubCategoryIsPresent(queryRequest.getForumSubCategoryId());
			if (forumSubCategory == 0) {
				logger.error("No record found");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
						appMessages.getForumsubcategory_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				List<ForumQuery> queryList = getValueQueryReq(queryRequest);// get value Method call for input
				int result = forumService.createQuery(queryList);
				if (result == 0) {
					logger.error("Error occured while adding data into table");
					ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getForumthread_added_successfully(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	private List<ForumQuery> getValueQueryReq(QueryRequest queryRequest) {
		List<ForumQuery> forumQueryList = new ArrayList<ForumQuery>();
		for (long postedToPartyId : queryRequest.getPostedToPartyId()) {
			ForumQuery query = new ForumQuery();
			if (queryRequest != null && queryRequest.getQuery() != null) {
				query.setQuery(queryRequest.getQuery());
			}
			if (queryRequest != null && queryRequest.getPartyId() != 0) {
				query.setPartyId(queryRequest.getPartyId());
			}
			if (queryRequest != null && queryRequest.getForumSubCategoryId() != 0) {
				query.setForumSubCategoryId(queryRequest.getForumSubCategoryId());
			}
			if (queryRequest != null && queryRequest.getForumCategoryId() != 0) {
				query.setForumCategoryId(queryRequest.getForumCategoryId());
			}
			query.setPostedToPartyId(postedToPartyId);
			forumQueryList.add(query);
		}
		return forumQueryList;
	}

	/**
	 * Method to add Forum Answer to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumAnswerRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of Forum Answer
	 *         addition or ErrorResponse
	 */
	@ApiOperation(value = "create answer", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createAnswer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createForumAnswer(@NonNull @RequestBody ForumAnswerRequest forumAnswerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (forumAnswerRequest != null) {
			int screenId = forumAnswerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long questionId = forumAnswerRequest.getQueryId();
		long partyId = forumAnswerRequest.getPartyId();
		if (forumAnswerRequest.getAnswer() == null || partyId == 0 || questionId == 0) {
			logger.error("Mandatory fields");
			ForumResponse response = messageResponse(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_createAnswer());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			int party = forumService.checkPartyIsPresent(partyId);
			if (party == 0) {
				logger.info("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int forumQuery = forumService.checkForumQueryByPostedToPartyId(questionId, partyId); // Checking
				// ForumThread is present//
				if (forumQuery == 0) {
					logger.error("No record found");
					ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
							appMessages.getNo_record_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// Convert input json request to ForumPostRequest object.
					ForumAnswer answer = getValueForumAnswer(forumAnswerRequest);// get value Method call input values//
					int result = forumService.createForumAnswer(answer); // creating forum post//
					if (result != 0) {
						logger.info("Adding forum post into db");
						ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
								appMessages.getForumpost_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("Error occured while adding data into table");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}

	private ForumAnswer getValueForumAnswer(ForumAnswerRequest forumAnswerRequest) {
		ForumAnswer forumAnswer = new ForumAnswer();
		if (forumAnswerRequest != null && forumAnswerRequest.getAnswer() != null) {
			forumAnswer.setAnswer(forumAnswerRequest.getAnswer());
		}
		if (forumAnswerRequest != null && forumAnswerRequest.getQueryId() != 0) {
			forumAnswer.setQueryId(forumAnswerRequest.getQueryId());
		}
		if (forumAnswerRequest != null && forumAnswerRequest.getPartyId() != 0) {
			forumAnswer.setPartyId(forumAnswerRequest.getPartyId());
		}
		return forumAnswer;
	}

	/**
	 * Method to fetch forum query by postedToPartyId to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of List of
	 *         Forum Query addition or ErrorResponse
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch queryList by partyId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchQueryListByPostedToPartyId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchQueryListByPostedToPartyId(@NonNull @RequestBody ForumIdRequest idRequest,
			@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int records,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		logger.info("Fetching query list by partyId");
		long PartyId = idRequest.getId();
		if (PartyId == 0) {
			logger.info("PartyId is Mandatory");
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_partyId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int questions = forumService.fetchTotalQueryListByPostedToPartyId(idRequest.getId());
			List<ForumQuery> questionList = forumService.fetchQueryListByPostedToPartyId(idRequest.getId(), pageNum,
					records);
			ForumQueryList forumQueryLists = new ForumQueryList();
			if (questionList != null) {
				forumQueryLists.setForumQueryList(questionList);
				forumQueryLists.setTotalRecords(questions);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					forumQueryLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch forum answer by queryId to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of List of
	 *         Forum Answer addition or ErrorResponse
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch answerList by queryId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAnswerListByQueryId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAnswerListByQueryId(@NonNull @RequestBody ForumIdRequest idRequest,
			@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int records,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (idRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_queryId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching answer list by queryId");
			int answers = forumService.fetchTotalAnswerListByQueryId(idRequest.getId());
			List<ForumAnswer> answerList = forumService.fetchAnswerListByQueryId(idRequest.getId(), pageNum, records);
			ForumAnswerList forumAnswerLists = new ForumAnswerList();
			if (answerList != null) {
				forumAnswerLists.setForumAnswerList(answerList);
				forumAnswerLists.setTotalRecords(answers);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					forumAnswerLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Fetch All vote Type List
	 * 
	 * @param null
	 * @return ResponseEntity<List<VoteType>> or SuccessCode with Empty Array
	 * 
	 */
	@ApiOperation(value = "fetch votetype List", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-voteType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchVoteType() {
		List<VoteType> voteTypeList = forumService.fetchVoteTypeList();
		logger.info("Fetching vote type");
		ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(), voteTypeList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to add to favorite article.
	 * 
	 * @param requestEntity
	 *            contains the <code>ArticleFavoriteRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of success
	 *         message of addition or ErrorResponse
	 */
	@ApiOperation(value = "add article to favorite", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addArticleToFavorite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addArticleToFavorite(@NonNull @RequestBody ArticleFavoriteRequest articleFavoriteRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleFavoriteRequest != null) {
			int screenId = articleFavoriteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (articleFavoriteRequest.getArticleId() == 0 || articleFavoriteRequest.getPartyId() == 0) {
			logger.error("Some fields are empty");
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_addArticleFavorite(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			// if (forumService.fetchParty(articleFavoriteRequest.getPartyId()) == null) {
			int party = forumService.checkPartyIsPresent(articleFavoriteRequest.getPartyId());// Checking the partyId is
			if (party == 0) {
				logger.error("No record found");
				ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				long articleId = articleFavoriteRequest.getArticleId();
				ArticlePost articlePost = forumService.fetchArticlePostByarticleId(articleId);
				if (articlePost == null) {
					logger.info("No record found");
					ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
							appMessages.getNo_record_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					if (articleFavoriteRequest.getPartyId() == articlePost.getPartyId()) {
						logger.info("cannot add own article as favorite");
						ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
								appMessages.getCant_add_own_article_as_favorite());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						ArticleFavorite articleFavorite = getValueArticleFavorite(articleFavoriteRequest);
						int result = forumService.addArticleToFavorite(articleFavorite); // creating ForumThread //
						if (result != 0) {
							logger.info("Adding article to favorite");
							ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
									appMessages.getFavorite_added_successfully(), null, roleFieldRights);
							return ResponseEntity.ok().body(response);
						} else {
							logger.error("Error occured while adding data into table");
							ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				}
			}

		}
	}

	/**
	 * Method to remove favorite article.
	 * 
	 * @param requestEntity
	 *            contains the <code>ArticleFavoriteRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of success
	 *         message of addition or ErrorResponse
	 */
	@ApiOperation(value = "remove article from favorite", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeArticleFromFavorite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeArticleFromFavorite(
			@NonNull @RequestBody ArticleFavoriteRequest articleFavoriteRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleFavoriteRequest != null) {
			int screenId = articleFavoriteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (articleFavoriteRequest.getArticleId() == 0 || articleFavoriteRequest.getPartyId() == 0) {
			logger.error("Some fields are empty");
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_removeArticleFavorite(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			ArticleFavorite articleFavorite = getValueArticleFavorite(articleFavoriteRequest);
			logger.info("Removing article from favorite");
			int result = forumService.removeArticleFromFavorite(articleFavorite);
			if (result != 0) {
				ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
						appMessages.getFavorite_removed_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.error("Error occured while adding data into table");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}
	}
	
	
	@ApiOperation(value = "remove article from my post", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeArticleFromMyPost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeArticleFromMyPost(
			@NonNull @RequestBody ArticleFavoriteRequest articleFavoriteRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleFavoriteRequest != null) {
			int screenId = articleFavoriteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (articleFavoriteRequest.getArticleId() == 0 || articleFavoriteRequest.getPartyId() == 0) {
			logger.error("Some fields are empty");
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articleId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			ArticlePost articlePost = getValueOfRemoveArticle(articleFavoriteRequest);
			logger.info("Removing article from favorite");
			int result = forumService.removeArticleFromMyPost(articlePost);
			if (result != 0) {
				ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
						appMessages.getArticlePost_deleted_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.error("Error occured while adding data into table");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE, appMessages.getError_occured());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		}
	}
	
	private ArticlePost getValueOfRemoveArticle(ArticleFavoriteRequest articleFavoriteRequest) {
		ArticlePost articlePost = new ArticlePost();
		if (articleFavoriteRequest != null && articleFavoriteRequest.getPartyId() != 0) {
			articlePost.setPartyId(articleFavoriteRequest.getPartyId());
		}
		if (articleFavoriteRequest != null && articleFavoriteRequest.getArticleId() != 0) {
			articlePost.setArticleId(articleFavoriteRequest.getArticleId());
		}
		return articlePost;
	}

	/**
	 * Method to fetch forum answer by queryId to the Maria database.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of List of
	 *         Forum Answer addition or ErrorResponse
	 */
	@ApiOperation(value = "fetch favorite article by partyId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchFavoriteArticlesByPartyId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchFavoriteArticlesByPartyId(@NonNull @RequestBody ForumIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (idRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_FavoriteArticlesByPartyId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching favorite article by partyId");
			// if (pageNum == 0) {
			// ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
			// appMessages.getInvalid_pagenum());
			// return ResponseEntity.ok().body(response);
			// } else {
			// if (pageNum == 1) {
			// } else {
			// pageNum = (pageNum - 1) * records + 1;
			// }
			int articleFavorites = forumService.fetchTotalFavoriteArticlesByPartyId(idRequest.getId());

			List<ArticlePost> articleFavoriteList = forumService.fetchFavoriteArticlesByPartyId(idRequest.getId());
			// if (articleFavorite.size() == 0) {
			// logger.info("No record found");
			// ForumResponse response = messageResponse(ForumConstants.NO_RECORD_FOUND,
			// appMessages.getNo_record_found());
			// return
			// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			// } else {
			ArticleFavoriteList articleFavoriteLists = new ArticleFavoriteList();
			if (articleFavoriteList != null) {
				articleFavoriteLists.setArticlePostList(articleFavoriteList);
				articleFavoriteLists.setTotalRecords(articleFavorites);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articleFavoriteLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
			// }
			// }
		}
	}

	private ArticleFavorite getValueArticleFavorite(ArticleFavoriteRequest articleFavoriteRequest) {
		ArticleFavorite articleFavorite = new ArticleFavorite();
		if (articleFavoriteRequest != null && articleFavoriteRequest.getPartyId() != 0) {
			articleFavorite.setPartyId(articleFavoriteRequest.getPartyId());
		}
		if (articleFavoriteRequest != null && articleFavoriteRequest.getArticleId() != 0) {
			articleFavorite.setArticleId(articleFavoriteRequest.getArticleId());
		}
		return articleFavorite;
	}

	/**
	 * Fetch all article comment by articleId
	 * 
	 * @return ResponseEntity<List<ArticleComment>> or ErrorResponse
	 * @param ForumIdRequest
	 * 
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch reply article comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchReplyArticleComment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchReplyArticleComment(@NonNull @RequestBody ArticleCommentRequest articleCommentRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleCommentRequest != null) {
			int screenId = articleCommentRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = articleCommentRequest.getArticleId();
		long parentId = articleCommentRequest.getParentCommentId();
		if (articleId == 0 || parentId == 0) {
			logger.info("ArticleId&&parentId is Mandatory");
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_articleId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching articleComment");
			int articleComments = forumService.fetchTotalArticleCommentByParentId(articleCommentRequest.getArticleId(),
					articleCommentRequest.getParentCommentId());

			List<ArticleComment> articleComment = forumService.fetchArticleCommentByParentId(
					articleCommentRequest.getArticleId(), articleCommentRequest.getParentCommentId());
			ArticleCommentList articleCommentLists = new ArticleCommentList();
			if (articleComment != null) {
				articleCommentLists.setArticleCommentList(articleComment);
				articleCommentLists.setTotalRecords(articleComments);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articleCommentLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch article up count.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of success
	 *         message of addition or ErrorResponse
	 */
	@ApiOperation(value = "fetch article up count", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchArticleUpCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleUpCount(@NonNull @RequestBody ForumIdRequest forumIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (forumIdRequest != null) {
			int screenId = forumIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = forumIdRequest.getId();
		if (forumIdRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_ArticleUpCount(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetch article up count");
			int count = forumService.fetchArticleUpCount(articleId);
			ArticleVoteCount articleVoteCount = new ArticleVoteCount();
			articleVoteCount.setUpCount(count);

			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articleVoteCount, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/***
	 * fetchArticleListByForumStatusId
	 * 
	 * @param ForumIdRequest
	 * @return ResponseEntity<List<ArticlePost>> or ErrorResponse
	 */
	// TODO : Need to add totalCount.
	@ApiOperation(value = "fetch article by ForumStatusId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchArticleListByForumStatusId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleListByForumStatusId(@NonNull @RequestBody ForumIdRequest idRequest,
			@RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int records,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		if (idRequest.getId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_forumStatusId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching article list by StatusId");

			long forumStatusId = idRequest.getId();
			int articlePosts = forumService.fetchTotalArticleListByStatusId(forumStatusId);

			List<ArticlePost> articlePostList = forumService.fetchArticleListByStatusId(pageNum, records,
					forumStatusId);

			ArticlePostList articlePostLists = new ArticlePostList();
			if (articlePostList != null) {
				articlePostLists.setArticlePostList(articlePostList);
				articlePostLists.setTotalRecords(articlePosts);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articlePostLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch article up count.
	 * 
	 * @param requestEntity
	 *            contains the <code>ForumIdRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of success
	 *         message of addition or ErrorResponse
	 */ 
	@ApiOperation(value = "fetch article vote detail", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchArticleVoteAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleVoteAddress(@NonNull @RequestBody ArticleVoteRequest articleVoteRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleVoteRequest != null) {
			int screenId = articleVoteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long articleId = articleVoteRequest.getArticleId();
		long partyId = articleVoteRequest.getPartyId();
		if (articleVoteRequest.getArticleId() == 0 || articleVoteRequest.getPartyId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_ArticleVoteAddress(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetch article vote address");
			ArticleVoteAddress articleVoteAddress = forumService.fetchArticleVoteAddress(articleId, partyId);
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articleVoteAddress, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to remove like from article.
	 * 
	 * @param requestEntity
	 *            contains the <code>ArticleVoteRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of success
	 *         message of addition or ErrorResponse
	 */
	@ApiOperation(value = "remove like from article", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeArticleLike", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeArticleLike(@NonNull @RequestBody ArticleVoteRequest articleVoteRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleVoteRequest != null) {
			int screenId = articleVoteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		long articleId = articleVoteRequest.getArticleId();
		long partyId = articleVoteRequest.getPartyId();
		if (articleVoteRequest.getArticleId() == 0 || articleVoteRequest.getPartyId() == 0) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_removeArticleLike(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Decrease like count from aricle");
			int unLikeResult = forumService.decreaseLikeCount(articleId);
			if (unLikeResult == 0) {
				logger.error("Error occured while updating vote count");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			logger.info("Remove from aricle vote address");
			int removeResult = forumService.removeArticleVoteAddress(articleId, partyId);
			if (removeResult == 0) {
				logger.error("Error occured while removing from article vote address");
				ForumResponse response = messageResponse(ForumConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getArticlevote_removed_successfully(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to Search article by title.
	 * 
	 * @param requestEntity
	 *            contains the <code>ArticleVoteRequest</code>
	 * @return ResponseEntity<String> contains the either the Result of success
	 *         message of addition or ErrorResponse
	 */
	@ApiOperation(value = "search article by title", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/searchArticleByTitle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> searchArticleByTitle(@NonNull @RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String title,
			@RequestParam(defaultValue = "") String forumStatusId,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				ForumResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (title.equals("") && forumStatusId.equals("")) {
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_searchArticle(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Search article by title");
			int articlePostList = forumService.fetchTotalSearchArticleByTitle(title, forumStatusId);
			List<ArticlePost> articlePost = forumService.searchArticleByTitle(pageNum, records, title, forumStatusId);
			ArticlePostList articlePostLists = new ArticlePostList();
			if (articlePost != null) {
				articlePostLists.setArticlePostList(articlePost);
				articlePostLists.setTotalRecords(articlePostList);
			}
			ForumResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					articlePostLists, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

}