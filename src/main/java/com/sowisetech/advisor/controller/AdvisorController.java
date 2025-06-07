package com.sowisetech.advisor.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.request.BrandRequest;
import com.sowisetech.advisor.model.Adv;
import com.sowisetech.advisor.model.AdvBrandInfo;
import com.sowisetech.advisor.model.AdvBrandRank;
import com.sowisetech.advisor.model.AdvProduct;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.AdvisorType;
import com.sowisetech.advisor.model.ArticleStatus;
import com.sowisetech.advisor.model.Award;
import com.sowisetech.advisor.model.Brand;
import com.sowisetech.advisor.model.BrandsComment;
import com.sowisetech.advisor.model.Category;
import com.sowisetech.advisor.model.CategoryType;
import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.model.ChatMessage;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.CommentVoteAddress;
import com.sowisetech.advisor.model.Dashboard;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.ExploreAdvisor;
import com.sowisetech.advisor.model.FollowerStatus;
import com.sowisetech.advisor.model.Followers;
import com.sowisetech.advisor.model.FollowersList;
import com.sowisetech.advisor.model.ForumCategory;
import com.sowisetech.advisor.model.ForumStatus;
import com.sowisetech.advisor.model.ForumSubCategory;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.KeyPeople;
import com.sowisetech.advisor.model.License;
import com.sowisetech.advisor.model.LookUp;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.model.PartyStatus;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.Service;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.StateCity;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.sowisetech.advisor.request.AdvBrandInfoReq;
import com.sowisetech.advisor.request.AdvBrandInfoRequest;
import com.sowisetech.advisor.request.AdvIdRequest;
import com.sowisetech.advisor.request.AdvPersonalInfoRequest;
import com.sowisetech.advisor.request.AdvPersonalInfoRequestValidator;
import com.sowisetech.advisor.request.AdvProductInfoRequest;
import com.sowisetech.advisor.request.AdvProductInfoRequestValidator;
import com.sowisetech.advisor.request.AdvProductRequest;
import com.sowisetech.advisor.request.AdvProfessionalInfoRequest;
import com.sowisetech.advisor.request.AdvProfessionalInfoRequestValidator;
import com.sowisetech.advisor.request.AdvisorRequest;
import com.sowisetech.advisor.request.AdvisorRequestValidator;
import com.sowisetech.advisor.request.AwardReq;
import com.sowisetech.advisor.request.Blog;
import com.sowisetech.advisor.request.BloggerIdRequest;
import com.sowisetech.advisor.request.BloggerRequestValidator;
import com.sowisetech.advisor.request.BrandCommentList;
import com.sowisetech.advisor.request.BrandCommentRequest;
import com.sowisetech.advisor.request.CertificateReq;
import com.sowisetech.advisor.request.ChatMessageRequest;
import com.sowisetech.advisor.request.ChatRequest;
import com.sowisetech.advisor.request.CustomerRequest;
import com.sowisetech.advisor.request.EducationReq;
import com.sowisetech.advisor.request.EducationRequest;
import com.sowisetech.advisor.request.ExperienceReq;
import com.sowisetech.advisor.request.FollowerRequest;
import com.sowisetech.advisor.request.ForgetPasswordRequest;
import com.sowisetech.advisor.request.ForgetPasswordRequestValidator;
import com.sowisetech.advisor.request.IdRequest;
import com.sowisetech.advisor.request.KeyPeopleRequest;
import com.sowisetech.advisor.request.KeyPeopleRequestValidator;
import com.sowisetech.advisor.request.MailTemplateRequest;
import com.sowisetech.advisor.request.ModerateCommentRequest;
import com.sowisetech.advisor.request.ModifyAdvReqValidator;
import com.sowisetech.advisor.request.ModifyAdvRequest;
import com.sowisetech.advisor.request.OtpRequest;
import com.sowisetech.advisor.request.ParamIdRequest;
import com.sowisetech.advisor.request.PasswordChangeRequest;
import com.sowisetech.advisor.request.PasswordValidator;
import com.sowisetech.advisor.request.PromotionReq;
import com.sowisetech.advisor.request.PromotionRequest;
import com.sowisetech.advisor.request.PromotionRequestValidator;
import com.sowisetech.advisor.request.ResendMailRequest;
import com.sowisetech.advisor.request.ResetPasswordRequest;
import com.sowisetech.advisor.request.ResetPasswordRequestValidator;
import com.sowisetech.advisor.request.StatusRequest;
import com.sowisetech.advisor.request.UniqueFieldRequest;
import com.sowisetech.advisor.response.AdvProductList;
import com.sowisetech.advisor.response.AdvResponse;
import com.sowisetech.advisor.response.AdvResponseData;
import com.sowisetech.advisor.response.AdvResponseMessage;
import com.sowisetech.advisor.response.AdvTotalList;
import com.sowisetech.advisor.response.BloggerTotalList;
import com.sowisetech.advisor.response.BrandsCommentList;
import com.sowisetech.advisor.response.ExploreListAdvisor;
import com.sowisetech.advisor.response.PartyIdResponse;
import com.sowisetech.advisor.security.JwtTokenUtil;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.service.AmazonClient;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.calc.request.ChatMessageReq;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.request.ScreenIdRequest;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.MailConstants;
import com.sowisetech.common.util.ScreenRightsCommon;
import com.sowisetech.common.util.ScreenRightsConstants;
import com.sowisetech.common.util.SendMail;
import com.sowisetech.common.util.SendSms;
import com.sowisetech.common.util.SmsConstants;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.forum.request.ArticleVoteRequest;
//import com.sowisetech.forum.request.ArticleVoteRequest;
import com.sowisetech.forum.request.BloggerRequest;
import com.sowisetech.forum.response.ForumResponse;
import com.sowisetech.forum.util.ForumConstants;
import com.sowisetech.investor.model.Investor;
import com.sowisetech.investor.util.InvestorConstants;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdvisorController {

	@Autowired
	AdvAppMessages appMessages;
	@Autowired
	AdvisorRequestValidator advisorRequestValidator;
	@Autowired
	BloggerRequestValidator bloggerRequestValidator;
	@Autowired
	AdvisorService advisorService;
	@Autowired
	AwardReq awardRequest;
	@Autowired
	AdvPersonalInfoRequestValidator advPersonalInfoRequestValidator;
	@Autowired
	AdvProductInfoRequestValidator advProductInfoRequestValidator;
	@Autowired
	AdvProfessionalInfoRequestValidator advProfessionalInfoRequestValidator;
	@Autowired
	ModifyAdvReqValidator modifyAdvReqValidator;
	@Autowired
	PasswordValidator passwordValidator;
	@Autowired
	ResetPasswordRequestValidator resetPasswordRequestValidator;
	@Autowired
	ForgetPasswordRequestValidator forgetPasswordRequestValidator;
	@Autowired
	PromotionRequestValidator promotionRequestValidator;
	@Autowired
	AmazonClient amazonClient;
	@Autowired
	KeyPeopleRequestValidator keyPeopleRequestValidator;
	@Autowired
	MailConstants mailConstants;
	@Autowired
	SendMail sendMail;
	@Autowired
	AdvTableFields advTableFields;
	@Autowired
	ScreenRightsConstants screenRightsConstants;
	@Autowired
	ScreenRightsCommon screenRightsCommon;
	@Autowired
	CommonService commonService;
	@Autowired
	SendSms sendSms;
	@Autowired
	SmsConstants smsConstants;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final Logger logger = LoggerFactory.getLogger(AdvisorController.class);

	/**
	 * Extended Content Verification - Health check
	 * 
	 * @return ResponseEntity - HttpStatus.OK
	 */
	@GetMapping(value = "/ecv")
	public ResponseEntity getEcv() {
		logger.info("Advisor module running..");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Fetch All Advisor List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */
	/*---Fetch all Records ---*/
	@ApiOperation(value = "fetch all Advisors", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/fetch-all", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchAdvisorList(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {

				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int advList = advisorService.fetchAdvisorTotalList();
		logger.info("Fetching advisor list");
		List<Advisor> advisors = advisorService.fetchAdvisorList(pageNum, records);
		AdvTotalList advTotalList = new AdvTotalList();
		if (advisors != null) {
			advTotalList.setAdvisors(advisors);
			advTotalList.setTotalRecords(advList);
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advTotalList,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	private List<RoleFieldRights> rightsAuthentication(int screenId, HttpServletRequest request, String accessType) {
		List<RoleFieldRights> roleFieldRights = new ArrayList<>();
		List<Integer> roleScreenId = screenRightsCommon.screenRights(screenId, request, accessType);
		if (roleScreenId == null || roleScreenId.isEmpty()) {
			return roleFieldRights;
		} else {
			roleFieldRights = commonService.fetchFieldRights(roleScreenId);
			return roleFieldRights;
		}
	}

	/**
	 * Fetch All Advisor List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */
	/*---Fetch all Records ---*/
	@ApiOperation(value = "fetch all Blogger", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/fetchAllBlogger", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchBloggerList(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {

				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int bloggerList = advisorService.fetchBloggerTotalList();
		logger.info("Fetching blogger list");
		List<Blogger> blogger = advisorService.fetchBloggerList(pageNum, records);
		BloggerTotalList bloggerTotalList = new BloggerTotalList();
		if (blogger != null) {
			bloggerTotalList.setBlogger(blogger);
			bloggerTotalList.setTotalRecords(bloggerList);
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				bloggerTotalList, roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	/*---- fetch a Record by advId---*/
	/**
	 * Fetch Advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<Advisor> or ErrorResponse
	 */
	@ApiOperation(value = "Fetch the advisor by advId", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/fetch", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchByAdvisorID(@RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		AdvResponse response = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			String advId = advIdRequest.getAdvId();
			if (advId == null) {
				logger.info("advisorId is Mandatory");
				response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(), null,
						roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Fetching advisor by advId");
				Advisor advisor = advisorService.fetchByAdvisorId(advId);
				if (advisor != null) {
					boolean isCommon = checkIsCommonApplication(token);
					if (isCommon) {
						Adv adv = commonfetch(advisor);
						response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), adv,
								roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisor,
								roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else {
					response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), null,
							roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
		response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getValue_null_or_empty());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

	}

	private Adv commonfetch(Advisor advisor) {
		Adv adv = new Adv();
		adv.setAdvId(advisor.getAdvId());
		adv.setName(advisor.getName());
		adv.setDesignation(advisor.getDesignation());
		adv.setEmailId(advisor.getEmailId());
		adv.setPhoneNumber(advisor.getPhoneNumber());
		adv.setPassword(advisor.getPassword());
		adv.setUserName(advisor.getUserName());
		adv.setDelete_flag(advisor.getDelete_flag());
		adv.setAdvType(advisor.getAdvType());
		adv.setDisplayName(advisor.getDisplayName());
		adv.setFirmType(advisor.getFirmType());
		adv.setCorporateLable(advisor.getCorporateLable());
		adv.setWebsite(advisor.getWebsite());
		adv.setDob(advisor.getDob());
		adv.setGender(advisor.getGender());
		adv.setPanNumber(advisor.getPanNumber());
		adv.setAddress1(advisor.getAddress1());
		adv.setAddress2(advisor.getAddress2());
		adv.setState(advisor.getState());
		adv.setCity(advisor.getCity());
		adv.setPincode(advisor.getPincode());
		adv.setAboutme(advisor.getAboutme());
		adv.setImagePath(advisor.getImagePath());
		adv.setIsVerified(advisor.getIsVerified());
		adv.setIsMobileVerified(advisor.getIsMobileVerified());
		adv.setWorkFlowStatus(advisor.getWorkFlowStatus());
		adv.setPartyId(advisor.getPartyId());
		adv.setParentPartyId(advisor.getParentPartyId());
		adv.setPartyStatusId(advisor.getPartyStatusId());
		adv.setAwards(advisor.getAwards());
		adv.setCertificates(advisor.getCertificates());
		adv.setEducations(advisor.getEducations());
		adv.setExperiences(advisor.getExperiences());
		adv.setAdvProducts(advisor.getAdvProducts());
		adv.setAdvBrandInfo(advisor.getAdvBrandInfo());
		adv.setAdvBrandRank(advisor.getAdvBrandRank());
		adv.setPromotions(advisor.getPromotions());
		adv.setPlanDetailList(advisor.getPlanDetailList());
		adv.setCorporateUsername(advisor.getCorporateUsername());
		return adv;
	}

	private boolean checkIsCommonApplication(String token) {
		String jwtToken = token.substring(7);
		Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
		String keyValue = (String) claims.get("auth_key");
		if (keyValue.equals(advTableFields.getCommon_application())) {
			return true;
		} else {
			return false;
		}
	}

	@ApiOperation(value = "Fetch the blogger by bloggerId", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/fetchByBloggerId", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchByBloggerId(@RequestBody BloggerIdRequest bloggerIdRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		AdvResponse response = null;
		if (bloggerIdRequest != null) {
			int screenId = bloggerIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			String bloggerId = bloggerIdRequest.getBloggerId();
			if (bloggerId == null) {
				logger.info("bloggerId is Mandatory");
				response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields_bloggerId(),
						null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Fetching blogger by bloggerId");
				Blogger blogger = advisorService.fetchByBloggerId(bloggerId);
				if (blogger != null) {
					boolean isCommon = checkIsCommonApplication(token);
					if (isCommon) {
						Blog blog = commonfetch(blogger);
						response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), blog,
								roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), blogger,
								roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else {
					response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), null,
							roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
		response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getValue_null_or_empty());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

	}

	private Blog commonfetch(Blogger blogger) {
		Blog blog = new Blog();
		blog.setBloggerId(blogger.getBloggerId());
		blog.setFullName(blogger.getFullName());
		blog.setEmailId(blogger.getEmailId());
		blog.setPhoneNumber(blogger.getPhoneNumber());
		blog.setPassword(blogger.getPassword());
		blog.setUserName(blogger.getUserName());
		blog.setDelete_flag(blogger.getDelete_flag());
		blog.setDisplayName(blogger.getDisplayName());
		blog.setDob(blogger.getDob());
		blog.setGender(blogger.getGender());
		blog.setImagePath(blogger.getImagePath());
		blog.setIsVerified(blogger.getIsVerified());
		blog.setPartyId(blogger.getPartyId());
		blog.setPartyStatusId(blogger.getPartyStatusId());
		return blog;
	}

	/**
	 * Fetch Public Advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<Advisor> or ErrorResponse
	 */
	@ApiOperation(value = "Fetch the public advisor by advId", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/fetchPublicAdvisor", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchPublicAdvisor(@RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		AdvResponse response = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			String advId = advIdRequest.getAdvId();
			if (advId == null) {
				logger.info("advisorId is Mandatory");
				response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(), null,
						roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.info("Fetching public advisor by advId");
				Advisor advisor = advisorService.fetchByPublicAdvisorID(advId);
				if (advisor != null) {
					boolean isCommon = checkIsCommonApplication(token);
					if (isCommon) {
						Adv adv = commonfetch(advisor);
						response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), adv,
								roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisor,
								roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else {
					response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), null,
							roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
		response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getValue_null_or_empty());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	/**
	 * Fetch team by id
	 * 
	 * @param RequestBody
	 *            contains the IdRequest
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 */
	@ApiOperation(value = "fetch the team member by parentId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchTeam", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchTeamByParentPartyId(@NonNull @RequestBody IdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long parentPartyId = idRequest.getId();
		if (parentPartyId == 0) {
			logger.info("parentPartyId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_parentPartyId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching Team member advisor");
			List<Advisor> advisor = advisorService.fetchTeamByParentPartyId(parentPartyId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisor,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Fetch Publish Team by id
	 * 
	 * @param RequestBody
	 *            contains the IdRequest
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 */
	@RequestMapping(value = "/fetchPublishTeam", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchPublishTeamByParentPartyId(@NonNull @RequestBody IdRequest idRequest) {
		long parentPartyId = idRequest.getId();
		if (parentPartyId == 0) {
			logger.info("parentPartyId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_parentPartyId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching published Team member advisor");
			List<Advisor> advisor = advisorService.fetchPublishTeamByParentPartyId(parentPartyId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisor,
					null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Fetch Advisor by userName
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<Advisor> or ErrorResponse
	 */
	@ApiOperation(value = "fetch the Advisor by userName", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAdvisorByUserName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> fetchAdvisorByUserName(@NonNull @RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request, @RequestHeader(name = "Authorization", required = false) String token) {
		List<RoleFieldRights> roleFieldRights = null;
		AdvResponse response = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String userName = advIdRequest.getUserName();
		if (userName == null) {
			logger.info("userName is Mandatory");
			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields_userName(), null,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			String userName_lc = userName.toLowerCase();
			logger.info("Fetching advisor by username");
			Advisor advisor = advisorService.fetchAdvisorByUserName(userName_lc);
			if (advisor != null) {
				long partyId = advisorService.fetchPartyIdByRoleBasedId(advisor.getAdvId());
				List<KeyPeople> keyPeopleList = advisorService.fetchKeyPeopleByParentId(partyId);
				advisor.setKeyPeopleList(keyPeopleList);
				List<Advisor> teamMemberlist = advisorService.fetchPublishTeamByParentPartyId(partyId);
				advisor.setTeamMemberList(teamMemberlist);
				boolean isCommon = checkIsCommonApplication(token);
				if (isCommon) {
					Adv adv = commonfetch(advisor);
					adv.setKeyPeopleList(keyPeopleList);
					adv.setTeamMemberList(teamMemberlist);
					response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), adv,
							roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisor,
							roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			} else {
				response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getUser_not_available(), null,
						roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Method to Remove advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<Advisor> contains the either the Result of advisor
	 *         remove or ErrorResponse
	 */
	/*---Remove a Record by advId---*/
	// TODO : need to change RequestMethod.DELETE
	@ApiOperation(value = "delete the advisor", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> deleteAdvisor(@NonNull @RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advIdRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching advisor by advId");
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.info("No record Found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Removing advisor");
				int deactivate = advisorService
						.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_deactivated());
				int result = advisorService.removeAdvisor(advId, deactivate);
				if (result == 0) {
					logger.info("Error occurred while removing data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
	}

	/**
	 * Method to Signup advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvisorRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         advisor or ErrorResponse
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> advSignup(@RequestBody AdvisorRequest advisorRequest) {

		long roleId = advisorRequest.getRoleId();
		long blgAdminId = advisorRequest.getBlgAdminId();
		long roleIdAdv = advisorService.fetchAdvRoleIdByName();
		long roleIdNonAdv = advisorService.fetchNonAdvRoleIdByName();
		long roleIdInv = advisorService.fetchInvRoleIdByName();
		long roleIdBlogger = advisorService.fetchBloggerIdByName();

		if (roleId == roleIdInv) {
			return investorSignup(advisorRequest, roleId, roleIdInv);
		} else if (roleIdNonAdv == roleId || roleIdAdv == roleId) {
			return advisorSignup(advisorRequest, roleId, roleIdAdv, roleIdNonAdv);
		} else if (roleId == roleIdBlogger) {

			return bloggerSignup(advisorRequest, roleId, roleIdBlogger, blgAdminId);
		} else {
			logger.error("Invalid roleId");
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getRoleId_not_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	private ResponseEntity<?> advisorSignup(AdvisorRequest advisorRequest, long roleId, long roleIdAdv,
			long roleIdNonAdv) {
		// Advisor Signup
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// Validating Advisor Request
		errors = advisorRequestValidator.validate(advisorRequest);
		if (errors.isEmpty() == true) {
			if (advisorRequest.getName() == null || advisorRequest.getEmailId() == null
					|| advisorRequest.getPassword() == null || advisorRequest.getPhoneNumber() == null) {
				logger.info("Some fields are empty");
				AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
						appMessages.getMandatory_fields_advSignup());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				String emailId = advisorRequest.getEmailId();
				String emailId_lc = emailId.toLowerCase();

				// fetch advisor by emailId
				Party partyEmail = advisorService.fetchPartyByEmailId(emailId_lc);
				if (partyEmail == null) {
					String panNumber = advisorRequest.getPanNumber();
					String panNumber_lc = panNumber.toLowerCase();
					Party partyPan = advisorService.fetchPartyByPAN(panNumber_lc);
					if (partyPan == null) {
						String phoneNumber = advisorRequest.getPhoneNumber();
						String phoneNumber_lc = phoneNumber.toLowerCase();
						Party partyPhone = advisorService.fetchPartyByPhoneNumber(phoneNumber_lc);
						if (partyPhone == null) {
							Advisor adv = getValueAdv(advisorRequest);// get value Method call
							if (roleIdNonAdv == roleId) {
								adv.setAdvType(advisorService.fetchTypeIdByCorporateAdvtype());
							} else if (roleIdAdv == roleId) {
								adv.setAdvType(advisorService.fetchTypeIdByIndividualAdvtype());
							}
							// Generate Advisor Id
							logger.info("Generating advisorId");
							String advId = advisorService.generateId();
							if (advId != null) {
								adv.setAdvId(advId);
							} else {
								logger.error("Error occured while generating advisorId");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
							Party corpparty = new Party();
							Advisor corpadvisor = new Advisor();
							if (advisorRequest.getParentPartyId() != 0) {
								corpparty = advisorService.fetchPartyByPartyId(advisorRequest.getParentPartyId());
								corpadvisor = advisorService.fetchByAdvisorId(corpparty.getRoleBasedId());
								adv.setCorporateLable(corpadvisor.getCorporateLable());
							}
							logger.info("New advisor signup");
							int result = advisorService.advSignup(adv, roleId);
							if (advisorRequest.getParentPartyId() != 0) {
								List<String> toUsers = new ArrayList<>();
								toUsers.add(advisorRequest.getEmailId());
								sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
										advisorRequest.getName(), advisorRequest.getPhoneNumber(),
										advisorRequest.getPassword(), "TEAM", corpadvisor.getName());
							} else {
								List<String> toUsers = new ArrayList<>();
								toUsers.add(advisorRequest.getEmailId());
								sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
										advisorRequest.getName(), advisorRequest.getPhoneNumber(),
										advisorRequest.getPassword());
							}
							if (result == 0) {
								logger.error("Error Occured while adding data into table");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								long partyId = advisorService.fetchPartyIdByRoleBasedId(advId);
								PartyIdResponse idResponse = new PartyIdResponse();
								idResponse.setPartyId(partyId);
								logger.info("New advisor details added into table");
								AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
										appMessages.getAdvisor_added_successfully(), idResponse, null);
								return ResponseEntity.ok().body(response);
							}
						} else {
							int role = getRoleIdFromUserRole(partyPhone.getPartyId());
							if (role == roleIdAdv) {
								Advisor advisor = advisorService.fetchByAdvisorId(partyPhone.getRoleBasedId());
								if (advisor.getIsVerified() == advTableFields.getAccount_not_verified()) {
									logger.info("Advisor already present with this Phone number");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getAdvisor_already_present_phone_not_verified());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								} else {
									logger.info("Advisor already present with this Phone number");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getAdvisor_already_present_phone());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							} else {
								logger.info("Advisor already present with this Phone number");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getAdvisor_already_present_phone());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						}
					} else {
						int role = getRoleIdFromUserRole(partyPan.getPartyId());
						if (role == roleIdAdv) {
							Advisor advisor = advisorService.fetchByAdvisorId(partyPan.getRoleBasedId());
							if (advisor.getIsVerified() == advTableFields.getAccount_not_verified()) {
								logger.info("Advisor already present with PAN number");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getAdvisor_already_present_pan_not_verified());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								logger.info("Advisor already present with PAN number");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getAdvisor_already_present_pan());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						} else {
							logger.info("Advisor already present with PAN number");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present_pan());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					int role = getRoleIdFromUserRole(partyEmail.getPartyId());
					if (role == roleIdAdv) {
						Advisor advisor = advisorService.fetchByAdvisorId(partyEmail.getRoleBasedId());
						if (advisor.getIsVerified() == advTableFields.getAccount_not_verified()) {
							logger.info("Advisor already present but account is not verified");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present_not_verified());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							logger.info("Advisor already present");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					} else {
						logger.info("Advisor already present");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getAdvisor_already_present());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		} else if (errors.isEmpty() == false) {
			logger.error("Validation Error");
			AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private int getRoleIdFromUserRole(long partyId) {
		List<User_role> user_role = commonService.fetchUserRoleByUserId(partyId);
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

	private ResponseEntity<?> investorSignup(AdvisorRequest advisorRequest, long roleId, long roleIdInv) {
		// Investor Signup
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// validate investor request
		errors = advisorRequestValidator.validate(advisorRequest);
		if (errors.isEmpty() == true) {
			if (advisorRequest.getFullName() != null && advisorRequest.getEmailId() != null
					&& advisorRequest.getPassword() != null && advisorRequest.getUserName() != null
					&& advisorRequest.getPhoneNumber() != null && advisorRequest.getPincode() != null) {

				String emailId = advisorRequest.getEmailId();
				String emailId_lc = emailId.toLowerCase();
				// fetch investor by emailId
				Party party = advisorService.fetchPartyByEmailId(emailId_lc);
				if (party == null) {
					String username = advisorRequest.getUserName();
					String username_lc = username.toLowerCase();
					Party partyUserName = advisorService.fetchPartyByUserName(username_lc);
					if (partyUserName == null) {
						String phoneNumber = advisorRequest.getPhoneNumber();
						String phoneNumber_lc = phoneNumber.toLowerCase();
						Party partyPhone = advisorService.fetchPartyByPhoneNumber(phoneNumber_lc);
						if (partyPhone == null) {
							Investor inv = getValueInv(advisorRequest);// get value Method call for input values
							// Generate investor Id
							logger.info("Generating investorId");
							String invId = advisorService.generateIdInv();
							if (invId != null) {
								inv.setInvId(invId);
							} else {
								logger.error("Error occured while generating investorId");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
							logger.info("New investor signup");
							int result = advisorService.addInvestor(inv, roleId);// add investor method call
							List<String> toUsers = new ArrayList<>();
							toUsers.add(advisorRequest.getEmailId());
							sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
									advisorRequest.getFullName(), advisorRequest.getPhoneNumber(),
									advisorRequest.getPassword(), MailConstants.INV_SIGN);
							if (result != 0) {
								long partyId = advisorService.fetchPartyIdByRoleBasedId(invId);
								PartyIdResponse idResponse = new PartyIdResponse();
								idResponse.setPartyId(partyId);
								logger.info("New investor details added into table");
								AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
										appMessages.getInvestor_added_successfully(), idResponse, null);
								return ResponseEntity.ok().body(response);
							} else {
								logger.error("Error Occured while adding data into table");
								AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						} else {
							int role = getRoleIdFromUserRole(partyPhone.getPartyId());
							if (role == roleIdInv) {
								Investor investor = advisorService.fetchInvestorByInvId(partyPhone.getRoleBasedId());
								if (investor.getIsVerified() == advTableFields.getAccount_not_verified()) {
									logger.info("Investor Already Present with phone number");
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getAdvisor_already_present_phone_not_verified());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								} else {
									logger.info("Investor Already Present with phone number");
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getAdvisor_already_present_phone());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							} else {
								logger.info("Investor Already Present with username");
								AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
										appMessages.getAdvisor_already_present_phone());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						}
					} else {
						int role = getRoleIdFromUserRole(partyUserName.getPartyId());
						if (role == roleIdInv) {
							Investor investor = advisorService.fetchInvestorByInvId(partyUserName.getRoleBasedId());
							if (investor.getIsVerified() == advTableFields.getAccount_not_verified()) {
								logger.info("Investor Already Present with username");
								AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
										appMessages.getUser_already_present_username_not_verified());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								logger.info("Investor Already Present with username");
								AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
										appMessages.getUser_already_present_username());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						} else {
							logger.info("Investor Already Present with username");
							AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
									appMessages.getUser_already_present_username());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					int role = getRoleIdFromUserRole(party.getPartyId());
					if (role == roleIdInv) {
						Investor investor = advisorService.fetchInvestorByInvId(party.getRoleBasedId());
						if (investor.getIsVerified() == advTableFields.getAccount_not_verified()) {
							logger.info("Investor already present but account is not verified");
							AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present_not_verified());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							logger.info("Investor Already Present");
							AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					} else {
						logger.info("Investor Already Present");
						AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								appMessages.getAdvisor_already_present());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Some fields are empty");
				AdvResponse response = messageResponse(InvestorConstants.SUCCESS_CODE,
						appMessages.getMandatory_fields_signup());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.error("Validation Error");
			AdvResponse response = responseWithData(InvestorConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	public Investor getValueInv(AdvisorRequest advisorRequest) {
		Investor inv = new Investor();
		if (advisorRequest != null && advisorRequest.getFullName() != null) {
			inv.setFullName(advisorRequest.getFullName());
		}
		// if (advisorRequest != null && advisorRequest.getDisplayName() != null) {
		// inv.setDisplayName(advisorRequest.getDisplayName());
		// }
		// if (advisorRequest != null && advisorRequest.getDob() != null) {
		// inv.setDob(advisorRequest.getDob());
		// }
		// if (advisorRequest != null && advisorRequest.getGender() != null) {
		// inv.setGender(advisorRequest.getGender());
		// }
		if (advisorRequest != null && advisorRequest.getEmailId() != null) {
			inv.setEmailId(advisorRequest.getEmailId());
		}
		if (advisorRequest != null && advisorRequest.getPhoneNumber() != null) {
			inv.setPhoneNumber(advisorRequest.getPhoneNumber());
		}
		if (advisorRequest != null && advisorRequest.getPassword() != null) {
			inv.setPassword(advisorRequest.getPassword());
		}
		if (advisorRequest != null && advisorRequest.getUserName() != null) {
			inv.setUserName(advisorRequest.getUserName());
		}
		if (advisorRequest != null && advisorRequest.getPincode() != null) {
			inv.setPincode(advisorRequest.getPincode());
		}
		return inv;
	}

	public Advisor getValueAdv(AdvisorRequest advisorRequest) {
		Advisor adv = new Advisor();
		adv.setName(advisorRequest.getName());
		adv.setEmailId(advisorRequest.getEmailId());
		adv.setPanNumber(advisorRequest.getPanNumber());
		adv.setPhoneNumber(advisorRequest.getPhoneNumber());
		adv.setPassword(advisorRequest.getPassword());
		// adv.setUserName(advisorRequest.getUserName());
		adv.setParentPartyId(advisorRequest.getParentPartyId());
		return adv;
	}

	private ResponseEntity<?> bloggerSignup(AdvisorRequest advisorRequest, long roleId, long roleIdBlogger,
			long blgAdminId) {
		// Blogger Signup
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// validate blogger request
		errors = advisorRequestValidator.validate(advisorRequest);
		if (errors.isEmpty() == true) {
			if (advisorRequest.getFullName() != null && advisorRequest.getEmailId() != null
					&& advisorRequest.getPassword() != null && advisorRequest.getUserName() != null
					&& advisorRequest.getPhoneNumber() != null) {

				String emailId = advisorRequest.getEmailId();
				String emailId_lc = emailId.toLowerCase();
				// fetch blogger by emailId
				Party party = advisorService.fetchPartyByEmailId(emailId_lc);
				if (party == null) {
					String username = advisorRequest.getUserName();
					String username_lc = username.toLowerCase();
					Party partyUserName = advisorService.fetchPartyByUserName(username_lc);
					if (partyUserName == null) {
						String phoneNumber = advisorRequest.getPhoneNumber();
						String phoneNumber_lc = phoneNumber.toLowerCase();
						Party partyPhone = advisorService.fetchPartyByPhoneNumber(phoneNumber_lc);
						if (partyPhone == null) {
							Blogger blog = getValueBlogger(advisorRequest);// get value Method call for input values
							// Generate blogger Id
							logger.info("Generating bloggerId");
							String bloggerId = advisorService.generateIdBlogger();
							if (bloggerId != null) {
								blog.setBloggerId(bloggerId);
							} else {
								logger.error("Error occured while generating BloggerId");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
							logger.info("New blogger signup");

							if (blgAdminId == 1) {
								int result = advisorService.addBlogger(blog, roleId, blgAdminId);// add blogger method
																									// call
								if (result != 0) {
									long partyId = advisorService.fetchPartyIdByRoleBasedId(bloggerId);
									PartyIdResponse idResponse = new PartyIdResponse();
									idResponse.setPartyId(partyId);
									logger.info("New blogger details added into table");
									AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
											appMessages.getAdvisor_added_successfully(), idResponse, null);
									return ResponseEntity.ok().body(response);
								} else {
									logger.error("Error Occured while adding data into table");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getError_occured());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							} else if (blgAdminId == 2) {
								int result = advisorService.addBlogger(blog, roleIdBlogger, blgAdminId);
								List<String> toUsers = new ArrayList<>();
								toUsers.add(advisorRequest.getEmailId());
								sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
										advisorRequest.getFullName(), advisorRequest.getPhoneNumber(),
										advisorRequest.getPassword(), MailConstants.BLOGGER_SIGN);
								if (result != 0) {
									long partyId = advisorService.fetchPartyIdByRoleBasedId(bloggerId);
									PartyIdResponse idResponse = new PartyIdResponse();
									idResponse.setPartyId(partyId);
									logger.info("New blogger details added into table");
									AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
											appMessages.getAdvisor_added_successfully(), idResponse, null);
									return ResponseEntity.ok().body(response);
								} else {
									logger.error("Error Occured while adding data into table");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getError_occured());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							}
						} else {
							int role = getRoleIdFromUserRole(partyPhone.getPartyId());
							if (role == roleIdBlogger) {
								Blogger blog = advisorService.fetchBloggerByBloggerId(partyPhone.getRoleBasedId());
								if (blog.getIsVerified() == advTableFields.getAccount_not_verified()) {
									logger.info("Blogger Already Present with phone number");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getAdvisor_already_present_phone_not_verified());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								} else {
									logger.info("Blogger Already Present with phone number");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getAdvisor_already_present_phone());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							} else {
								logger.info("Blogger Already Present with username");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getAdvisor_already_present_phone());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						}
					} else {
						int role = getRoleIdFromUserRole(partyUserName.getPartyId());
						if (role == roleIdBlogger) {
							Blogger blog = advisorService.fetchBloggerByBloggerId(partyUserName.getRoleBasedId());
							if (blog.getIsVerified() == advTableFields.getAccount_not_verified()) {
								logger.info("Blogger Already Present with username");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getUser_already_present_username_not_verified());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								logger.info("Blogger Already Present with username");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getUser_already_present_username());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						} else {
							logger.info("Blogger Already Present with username");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getUser_already_present_username());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				} else {
					int role = getRoleIdFromUserRole(party.getPartyId());
					if (role == roleIdBlogger) {
						Blogger blog = advisorService.fetchBloggerByBloggerId(party.getRoleBasedId());
						if (blog.getIsVerified() == advTableFields.getAccount_not_verified()) {
							logger.info("Blogger already present but account is not verified");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present_not_verified());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							logger.info("Blogger Already Present");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getAdvisor_already_present());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					} else {
						logger.info("Blogger Already Present");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getAdvisor_already_present());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else {
				logger.info("Some fields are empty");
				AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
						appMessages.getMandatory_fields_signup());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
		} else if (errors.isEmpty() == false) {
			logger.error("Validation Error");
			AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	public Blogger getValueBlogger(AdvisorRequest advisorRequest) {
		Blogger blog = new Blogger();
		if (advisorRequest != null && advisorRequest.getFullName() != null) {
			blog.setFullName(advisorRequest.getFullName());
		}
		if (advisorRequest != null && advisorRequest.getEmailId() != null) {
			blog.setEmailId(advisorRequest.getEmailId());
		}
		if (advisorRequest != null && advisorRequest.getPhoneNumber() != null) {
			blog.setPhoneNumber(advisorRequest.getPhoneNumber());
		}
		if (advisorRequest != null && advisorRequest.getPassword() != null) {
			blog.setPassword(advisorRequest.getPassword());
		}
		if (advisorRequest != null && advisorRequest.getUserName() != null) {
			blog.setUserName(advisorRequest.getUserName());
		}
		if (advisorRequest != null && advisorRequest.getDisplayName() != null) {
			blog.setDisplayName(advisorRequest.getDisplayName());
		}
		if (advisorRequest != null && advisorRequest.getImagePath() != null) {
			blog.setImagePath(advisorRequest.getImagePath());
		}
		if (advisorRequest != null && advisorRequest.getDob() != null) {
			blog.setDob(advisorRequest.getDob());
		}
		if (advisorRequest != null && advisorRequest.getGender() != null) {
			blog.setGender(advisorRequest.getGender());
		}

		return blog;
	}

	/**
	 * Method to verify
	 * 
	 * @param RequestParam
	 *            contains the key
	 * @return ResponseEntity<String> contains the either the Result of verified
	 *         signup or ErrorResponse
	 */
	@RequestMapping(value = "/verify/signup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> verifySignup(@RequestParam String key) {
		if (key == null) {
			logger.info("Key is mandatory");
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_keyFields());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		String decodedKey = decodeKey(key);
		List<String> values = Arrays.asList(decodedKey.split(","));
		String startKey = values.get(0);
		String emailId = values.get(1);
		String midKey = values.get(2);
		// String phoneNumber = values.get(3);
		// String password = values.get(4);
		String timestamp = values.get(5);
		String endKey = values.get(6);
		Timestamp ts = Timestamp.valueOf(timestamp);
		long minutes = diffOfTimeStampInMin(ts);
		String mailSub = sendMail.getMailText(MailConstants.SIGN + "_SUBJECT");
		System.out.println(emailId);
		String emailId_lc = emailId.toLowerCase();

		Party party = advisorService.fetchPartyByEmailId(emailId_lc);
		boolean isVerified = verifyKey(startKey, midKey, endKey);
		if (isVerified) {
			int roleId = getRoleIdFromUserRole(party.getPartyId());
			String role = advisorService.fetchRoleByRoleId(roleId);
			if (role.equals(advTableFields.getRoleName_nonIndividual()) || role.equals(advTableFields.getRoleName())) {
				Advisor adv = advisorService.fetchByAdvisorId(party.getRoleBasedId());
				if (adv.getIsVerified() == 1) {
					logger.info("Account already verified");
					return ResponseEntity.ok().body(appMessages.getAccount_already_verified());
				} else {
					if (minutes > mailConstants.getLink_validity()) {
						logger.error("Activation link expired");
						return ResponseEntity.ok().body(appMessages.getLink_expired());
					} else {
						String latestKey = commonService.fetchLatestKeyByEmailIdAndSub(emailId, mailSub);
						if (key.equals(latestKey)) {
							logger.info("Verifing advisor account");
							int result = advisorService.updateAdvisorAccountAsVerified(adv.getAdvId());
							List<String> toUsers = new ArrayList<>();
							toUsers.add(adv.getEmailId());
							sendMail.sendMailMessage(MailConstants.CONFIRMATION, toUsers, mailConstants.getFromUser(),
									null, adv.getName());
							if (result == 0) {
								logger.error("cannot verify account");
								return ResponseEntity.ok().body(appMessages.getAccount_cannot_verified());
							}
						} else {
							logger.error("Invalid link");
							return ResponseEntity.ok().body(appMessages.getLink_not_valid());
						}
					}
				}
			} else if (role.equals(advTableFields.getRoleName_inv())) {
				Investor inv = advisorService.fetchInvestorByInvId(party.getRoleBasedId());
				if (inv.getIsVerified() == 1) {
					logger.error("Account verified");
					return ResponseEntity.ok().body(appMessages.getAccount_already_verified());
				} else {
					if (minutes > mailConstants.getLink_validity()) {
						logger.error("Activation link expired");
						return ResponseEntity.ok().body(appMessages.getLink_expired());
					} else {
						String latestKey = commonService.fetchLatestKeyByEmailIdAndSub(emailId, mailSub);
						if (key.equals(latestKey)) {
							logger.info("Verifing investor account");
							int result = advisorService.updateInvestorAccountAsVerified(inv.getInvId());
							List<String> toUsers = new ArrayList<>();
							toUsers.add(inv.getEmailId());
							sendMail.sendMailMessage(MailConstants.CONFIRMATION, toUsers, mailConstants.getFromUser(),
									null, inv.getFullName(), MailConstants.INV_SIGN);
							if (result == 0) {
								logger.error("cannot verify account");
								return ResponseEntity.ok().body(appMessages.getAccount_cannot_verified());
							}
						} else {
							logger.error("Invalid link");
							return ResponseEntity.ok().body(appMessages.getLink_not_valid());
						}
					}
				}
			}
			return ResponseEntity.ok().body(appMessages.getAccount_verified_successfully());

		} else {
			logger.error("cannot verify account");
			return ResponseEntity.ok().body(appMessages.getAccount_cannot_verified());
		}
	}

	private long diffOfTimeStampInMin(Timestamp ts) {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		long milliseconds1 = ts.getTime();
		long milliseconds2 = currentTime.getTime();
		long diff = milliseconds2 - milliseconds1;
		long diffMinutes = diff / (60 * 1000);
		return diffMinutes;
	}

	private boolean verifyKey(String startKey, String midKey, String endKey) {
		if (startKey.equals(mailConstants.getStartkey()) && midKey.equals(mailConstants.getMidkey())
				&& endKey.equals(mailConstants.getEndkey())) {
			return true;
		} else {
			return false;
		}
	}

	private String decodeKey(String key) {
		byte[] decodedBytes = Base64.getDecoder().decode(key);
		String decodedString = new String(decodedBytes);
		return decodedString;
	}

	/**
	 * Method to modify advisor
	 * 
	 * @param RequestBody
	 *            contains the ModifyAdvRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         advisor or ErrorResponse
	 */
	// This service api is not used in normal advisor flow, but it may be used in
	// admin flow
	@ApiOperation(value = "modify the Advisor", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modify", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAdvisor(@NonNull @RequestBody ModifyAdvRequest modifyAdvRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (modifyAdvRequest != null) {
			int screenId = modifyAdvRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = modifyAdvRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			String username_lc = null;
			if (modifyAdvRequest.getUserName() != null) {
				String username = modifyAdvRequest.getUserName();
				username_lc = username.toLowerCase();
			}
			Party partyUserName = advisorService.fetchPartyByUserName(username_lc);
			if (partyUserName == null
					|| (partyUserName != null && modifyAdvRequest.getAdvId().equals(partyUserName.getRoleBasedId()))) {
				HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
				logger.info("Fetching advisor by advId");
				int advisor = advisorService.checkAdvisorIsPresent(advId);
				if (advisor == 0) {
					logger.info("No record found");
					AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
							appMessages.getNo_record_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// Validating advisor request
					errors = modifyAdvReqValidator.validate(modifyAdvRequest);
					if (errors.isEmpty() == true) {
						Advisor adv = getModifiedValue(modifyAdvRequest);// get value Method call
						logger.info("Modifying advisor");
						int result = advisorService.modifyAdvisor(advId, adv);
						if (result == 0) {
							logger.info("Error occurred while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
									appMessages.getAdvisor_updated_successfully(), null, roleFieldRights);
							return ResponseEntity.ok().body(response);
						}
					} else if (errors.isEmpty() == false) {
						AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(),
								errors, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else if (partyUserName != null && !modifyAdvRequest.getAdvId().equals(partyUserName.getRoleBasedId())) {
				logger.info("Advisor already present with this username");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
						appMessages.getUser_already_present_username());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private Advisor getModifiedValue(ModifyAdvRequest modifyAdvRequest) {
		Advisor adv = new Advisor();
		adv.setName(modifyAdvRequest.getName());
		adv.setDesignation(modifyAdvRequest.getDesignation());
		adv.setEmailId(modifyAdvRequest.getEmailId());
		adv.setPhoneNumber(modifyAdvRequest.getPhoneNumber());
		adv.setUserName(modifyAdvRequest.getUserName());
		adv.setDisplayName(modifyAdvRequest.getDisplayName());
		adv.setDob(modifyAdvRequest.getDob());
		adv.setGender(modifyAdvRequest.getGender());
		adv.setPanNumber(modifyAdvRequest.getPanNumber());
		adv.setAddress1(modifyAdvRequest.getAddress1());
		adv.setAddress2(modifyAdvRequest.getAddress2());
		adv.setState(modifyAdvRequest.getState());
		adv.setCity(modifyAdvRequest.getCity());
		adv.setPincode(modifyAdvRequest.getPincode());
		adv.setAboutme(modifyAdvRequest.getAboutme());
		adv.setImagePath(modifyAdvRequest.getImagePath());
		adv.setGst(modifyAdvRequest.getGst());
		return adv;
	}

	/**
	 * modifyBlogger
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE or ERROR_CODE
	 * @param AdvisorRequest
	 */
	@ApiOperation(value = "modify blogger", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyBlogger", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyBlogger(@NonNull @RequestBody BloggerRequest bloggerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (bloggerRequest != null) {
			int screenId = bloggerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String bloggerId = bloggerRequest.getBloggerId();
		if (bloggerId == null) {
			logger.info("bloggerId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_bloggerId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			logger.info("Fetching blogger by bloggerId");
			int blogger = advisorService.CheckBloggerIsPresent(bloggerId);// fetch blogger by id
			if (blogger == 0) {
				logger.info("No record Found");
				AdvResponse response = responseWithData(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// validate blogger request
				errors = bloggerRequestValidator.validate(bloggerRequest);
				if (errors.isEmpty() == true) {
					Blogger blog = getValue(bloggerRequest); // get value Method call for input values
					logger.info("Updating blogger");
					int result = advisorService.modifyBlogger(bloggerId, blog); // modify blogger method call
					if (result != 0) {
						AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
								appMessages.getAdvisor_updated_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					} else {
						logger.info("Error occured while adding data into table");
						AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE,
								appMessages.getError_occured(), null, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (errors.isEmpty() == false) {
					logger.info("Validation Errors");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

	public Blogger getValue(BloggerRequest bloggerRequest) {
		Blogger blog = new Blogger();
		if (bloggerRequest != null && bloggerRequest.getFullName() != null) {
			blog.setFullName(bloggerRequest.getFullName());
		}
		if (bloggerRequest != null && bloggerRequest.getDob() != null) {
			blog.setDob(bloggerRequest.getDob());
		}
		if (bloggerRequest != null && bloggerRequest.getGender() != null) {
			blog.setGender(bloggerRequest.getGender());
		}

		if (bloggerRequest != null && bloggerRequest.getImagePath() != null) {
			blog.setImagePath(bloggerRequest.getImagePath());
		}

		if (bloggerRequest != null && bloggerRequest.getPhoneNumber() != null) {
			blog.setPhoneNumber(bloggerRequest.getPhoneNumber());
		}

		if (bloggerRequest != null && bloggerRequest.getDisplayName() != null) {
			blog.setDisplayName(bloggerRequest.getDisplayName());
		}

		if (bloggerRequest != null && bloggerRequest.getUserName() != null) {
			blog.setUserName(bloggerRequest.getUserName());
		}
		return blog;
	}

	/**
	 * deleteBlogger
	 * 
	 * @return ResponseEntity<InvResponse> SUCCESS_CODE or ERROR_CODE
	 * @param BloggerIdRequest
	 */
	// TODO change RequestMethod.POST to DELETE
	@ApiOperation(value = "remove Blogger", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeBlogger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeBlogger(@NonNull @RequestBody BloggerIdRequest bloggerIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (bloggerIdRequest != null) {
			int screenId = bloggerIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String bloggerId = bloggerIdRequest.getBloggerId();
		if (bloggerId == null) {
			logger.info("bloggerId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_bloggerId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching blogger by bloggerId");
			int blogger = advisorService.CheckBloggerIsPresent(bloggerId); // fetch blogger by id
			if (blogger == 0) {
				logger.info("No record found");
				AdvResponse response = responseWithData(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found(), null, null);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Modifying blogger delete_flag as 'Y'");
				int result = advisorService.removeBlogger(bloggerId); // remove blogger by id
				if (result != 0) {
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.info("Error Occured while removing data into table");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE,
							appMessages.getError_occured_remove(), null, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to add the personal information for advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvPersonalInfoRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         advisor personal info or ErrorResponse
	 */

	// Add Advisor Personal Information in advisor table
	@ApiOperation(value = "add advisor personal information", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAdvPersonalInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addAdvPersonalInfo(@NonNull @RequestBody AdvPersonalInfoRequest advPersonalInfoRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advPersonalInfoRequest != null) {
			int screenId = advPersonalInfoRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advPersonalInfoRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			String username_lc = null;
			if (advPersonalInfoRequest.getUserName() != null) {
				String username = advPersonalInfoRequest.getUserName();
				username_lc = username.toLowerCase();
			}
			Party partyUserName = advisorService.fetchPartyByUserName(username_lc);
			if (partyUserName == null || (partyUserName != null
					&& advPersonalInfoRequest.getAdvId().equals(partyUserName.getRoleBasedId()))) {
				HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
				logger.info("Fetching advisor by advId");
				// Advisor advisor = advisorService.fetchByAdvisorId(advId);
				int advisor = advisorService.checkAdvisorIsPresent(advId);
				if (advisor == 0) {
					logger.info("No record found");
					AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
							appMessages.getNo_record_found());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// Validating advPersonalInfoReq
					errors = advPersonalInfoRequestValidator.validate(advPersonalInfoRequest);
					if (errors.isEmpty() == true) {
						Advisor adv = getValuePersonalInfo(advPersonalInfoRequest);// get value Method call
						logger.info("Adding advisor personal info into DB");
						int result = advisorService.addAdvPersonalInfo(advId, adv);
						if (result == 0) {
							logger.info("Error occurred while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
									appMessages.getAdvisor_info_added_successfully(), null, roleFieldRights);
							return ResponseEntity.ok().body(response);
						}
					} else if (errors.isEmpty() == false) {
						AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(),
								errors, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			} else if (partyUserName != null
					&& !advPersonalInfoRequest.getAdvId().equals(partyUserName.getRoleBasedId())) {
				logger.info("Advisor already present with this username");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
						appMessages.getUser_already_present_username());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	private Advisor getValuePersonalInfo(AdvPersonalInfoRequest advPersonalInfoRequest) {
		Advisor adv = new Advisor();
		if (advPersonalInfoRequest.getUserName() != null) {
			adv.setUserName(advPersonalInfoRequest.getUserName().toLowerCase());
		}
		adv.setDisplayName(advPersonalInfoRequest.getDisplayName());
		adv.setFirmType(advPersonalInfoRequest.getFirmType());
		adv.setCorporateLable(advPersonalInfoRequest.getCorporateLable());
		adv.setWebsite(advPersonalInfoRequest.getWebsite());
		adv.setDesignation(advPersonalInfoRequest.getDesignation());
		adv.setDob(advPersonalInfoRequest.getDob());
		adv.setGender(advPersonalInfoRequest.getGender());
		adv.setAddress1(advPersonalInfoRequest.getAddress1());
		adv.setAddress2(advPersonalInfoRequest.getAddress2());
		adv.setState(advPersonalInfoRequest.getState());
		adv.setCity(advPersonalInfoRequest.getCity());
		adv.setPincode(advPersonalInfoRequest.getPincode());
		adv.setAboutme(advPersonalInfoRequest.getAboutme());
		adv.setImagePath(advPersonalInfoRequest.getImagePath());
		adv.setName(advPersonalInfoRequest.getName());
		adv.setPanNumber(advPersonalInfoRequest.getPanNumber());
		return adv;
	}

	/**
	 * Method to add the product information for advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvProductInfoRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         advisor product info or ErrorResponse
	 */

	// Add Advisor Product Info
	@ApiOperation(value = "add advisor prodcut information", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAdvProdInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addAdvProdInfo(@NonNull @RequestBody AdvProductInfoRequest advProductInfoRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advProductInfoRequest != null) {
			int screenId = advProductInfoRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advProductInfoRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			Advisor advisor = advisorService.fetchByAdvisorId(advId);
			if (advisor == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// validating advProductInfoRequest
				errors = advProductInfoRequestValidator.validate(advProductInfoRequest);
				if (errors.isEmpty() == true) {
					// Advisor adv = getValueProdInfo(advProductInfoRequest);
					List<AdvProduct> advProductList = getValueAdvProductListInfo(advProductInfoRequest);// get value
																										// Method
					logger.info("Adding advisor product info into DB");
					int result = advisorService.addAdvProductInfoList(advId, advProductList);
					if (result == 0) {
						logger.info("Error occurred while adding data into table");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
					if (mailConstants.getProduct_mail_flag().equals(MailConstants.ON)) {
						List<String> toUsers = new ArrayList<>();
						toUsers.add(advisor.getEmailId());
						sendMail.sendMailMessage(MailConstants.PRODUCT_ADD, toUsers, mailConstants.getFromUser(), null,
								advisor.getName());
					}
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_info_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else if (errors.isEmpty() == false) {
					logger.error("Validation error");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	private List<AdvProduct> getValueAdvProductListInfo(AdvProductInfoRequest advProductInfoRequest) {
		List<AdvProduct> advProductlist = new ArrayList<>();
		for (AdvProductRequest advProductRequest : advProductInfoRequest.getAdvProducts()) {
			AdvProduct advProduct = new AdvProduct();
			advProduct.setAdvProdId(advProductRequest.getAdvProdId());
			if (advProductRequest.getProdId() != 0) {
				advProduct.setProdId(advProductRequest.getProdId());
			}
			if (advProductRequest.getServiceId() != null) {
				advProduct.setServiceId(advProductRequest.getServiceId());
			}
			if (advProductRequest.getRemId() != 0) {
				advProduct.setRemId(advProductRequest.getRemId());
			}
			if (advProductRequest.getLicId() != 0) {
				advProduct.setLicId(advProductRequest.getLicId());
			}
			if (advProductRequest.getLicNumber() != null) {
				advProduct.setLicNumber(advProductRequest.getLicNumber());
			}
			if (advProductRequest.getValidity() != null) {
				advProduct.setValidity(advProductRequest.getValidity());
			}
			if (advProductRequest.getLicImage() != null) {
				advProduct.setLicImage(advProductRequest.getLicImage());
			}
			advProductlist.add(advProduct);
		}
		return advProductlist;
	}

	/**
	 * Method to modify product info
	 * 
	 * @param RequestBody
	 *            contains the AdvProductInfoRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         advisor product info or ErrorResponse
	 */

	// Modify Advisor Product info
	@ApiOperation(value = "modify advisor product information", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyAdvProdInfo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAdvisorProduct(@RequestBody AdvProductInfoRequest advProductInfoRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advProductInfoRequest != null) {
			int screenId = advProductInfoRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		String advId = advProductInfoRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			Advisor advisor = advisorService.fetchByAdvisorId(advId);
			if (advisor == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// validating advProductInfoRequest
				errors = advProductInfoRequestValidator.validate(advProductInfoRequest);
				if (errors.isEmpty() == true) {
					// remove from advProduct
					logger.info("Fetching advProduct by advId");
					List<AdvProduct> advProducts = advisorService.fetchAdvProductByAdvId(advId);
					List<Long> advProdIdList = new ArrayList<>();
					// If advProduct is not available in the request. we remove the corresponding
					// data in table
					for (AdvProductRequest advProdReq : advProductInfoRequest.getAdvProducts()) {
						advProdIdList.add(advProdReq.getAdvProdId());
					}
					for (AdvProductRequest advProductRequest : advProductInfoRequest.getAdvProducts()) {
						if (advProductRequest.getAdvProdId() != 0) {
							int advProd = advisorService.checkAdvProductIsPresent(advId,
									advProductRequest.getAdvProdId());
							if (advProd == 0) {
								logger.error("No Record Found");
								AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
										appMessages.getAdvproduct_not_found());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						}
					}
					for (AdvProduct advProd : advProducts) {
						if (advProdIdList.contains(advProd.getAdvProdId()) == false) {
							logger.info("Removing advisor product");
							int resultRemoveProd = advisorService.removeAdvProduct(advProd.getAdvProdId(), advId);
							logger.info("Removing advisor brandInfo");
							advisorService.removeAdvBrandInfo(advProd.getProdId(), advId);
							logger.info("Removing advisor brandRank");
							advisorService.removeFromBrandRank(advId, advProd.getProdId());
							addBrandrank(advId, advProd.getProdId());
							if (resultRemoveProd == 0) {
								logger.info("Error occurred while adding data into table");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						}
					}
					// modify and add advProduct
					List<AdvProduct> advProductList = getValueAdvProductListInfo(advProductInfoRequest);
					// System.out.println("advProduct Size:" + advProductList.size());
					if (advProductList.size() != 0) {
						int result = advisorService.addAndModifyProductInfo(advId, advProductList);
						if (result == 0) {
							logger.error("Error occurred while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
						if (mailConstants.getProduct_mail_flag().equals(MailConstants.ON)) {
							List<String> toUsers = new ArrayList<>();
							toUsers.add(advisor.getEmailId());
							sendMail.sendMailMessage(MailConstants.PRODUCT_MODIFY, toUsers, mailConstants.getFromUser(),
									null, advisor.getName());
						}
					}
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_updated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else if (errors.isEmpty() == false) {
					logger.error("Validation error");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							roleFieldRights);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}

	/**
	 * Method to add the professional information for advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvProfessionalInfoRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         advisor professional info or ErrorResponse
	 */

	// Add Advisor Professional Info
	@ApiOperation(value = "add advisor professional information", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAdvProfInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addAdvProfessionalInfo(@NonNull @RequestBody AdvProfessionalInfoRequest advProfInfoRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advProfInfoRequest != null) {
			int screenId = advProfInfoRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advProfInfoRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			// Advisor advisor = advisorService.fetchByAdvisorId(advId);
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Convert input json request to AdvisorRequest object.
				// validating advProfInfoRequest
				errors = advProfessionalInfoRequestValidator.validate(advProfInfoRequest);
				if (errors.isEmpty() == true) {
					Advisor adv = getValueProfessionalInfo(advProfInfoRequest);// get value Method call
					// Add advisor professional info
					logger.info("Adding advisor professional info into DB");
					int result = advisorService.addAdvProfessionalInfo(advId, adv);
					if (result == 0) {
						logger.error("Error occurred while adding data into table");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
								appMessages.getAdvisor_added_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else if (errors.isEmpty() == false) {
					logger.error("Validation error");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

	/**
	 * Method to modify professional information
	 * 
	 * @param RequestBody
	 *            contains the AdvProfessionalInfoRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         advisor professional info or ErrorResponse
	 */
	// Modify Professional Info
	@ApiOperation(value = "modify advisor professional information", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyAdvProfInfo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyAdvProfessionalInfo(@RequestBody AdvProfessionalInfoRequest advProfInfoRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advProfInfoRequest != null) {
			int screenId = advProfInfoRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advProfInfoRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			logger.info("Fetching advisor by advId");
			// Advisor advisor = advisorService.fetchByAdvisorId(advId);
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			List<AwardReq> awardReq = advProfInfoRequest.getAwards();
			List<CertificateReq> certificateReq = advProfInfoRequest.getCertificates();
			List<ExperienceReq> experienceReq = advProfInfoRequest.getExperiences();
			List<EducationReq> educationReq = advProfInfoRequest.getEducations();
			if (advisor == 0) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				errors = advProfessionalInfoRequestValidator.validate(advProfInfoRequest);
				if (errors.isEmpty() == true) {
					// we removing the record by deleteAll (array of string)
					if (advProfInfoRequest.getDeleteAll() != null && advProfInfoRequest.getDeleteAll().size() != 0) {
						for (String value : advProfInfoRequest.getDeleteAll()) {
							int result = 0;
							if (value.equals(AdvisorConstants.AWARDPROF)) {
								result = advisorService.removeAwardByAdvId(advId);
							} else if (value.equals(AdvisorConstants.CERTIFICATEPROF)) {
								result = advisorService.removeCertificateByAdvId(advId);
							} else if (value.equals(AdvisorConstants.EXPERIENCEPROF)) {
								result = advisorService.removeExperienceByAdvId(advId);
							} else if (value.equals(AdvisorConstants.EDUCATIONPROF)) {
								result = advisorService.removeEducationByAdvId(advId);
							}
							if (result == 0) {
								logger.error("Error occurred while removing data into table");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured_remove());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							}
						}
					}
					if (awardReq != null) {
						List<Award> awards = getValueAwardListInfo(awardReq);
						logger.info("modifying award");
						int result = advisorService.modifyAward(advId, awards);
						if (result == 0) {
							logger.error("Error occured while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
					if (certificateReq != null) {
						List<Certificate> certificateList = getValueCertificateListInfo(certificateReq);
						logger.info("modifying certificate");
						int result1 = advisorService.modifyCertificate(advId, certificateList);
						if (result1 == 0) {
							logger.info("Error occured while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
					if (experienceReq != null) {
						logger.info("modifying experience");
						List<Experience> experienceList = getValueExperienceListInfo(experienceReq);
						int result2 = advisorService.modifyExperience(advId, experienceList);
						if (result2 == 0) {
							logger.info("Error occured while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
					if (educationReq != null) {
						logger.info("modifying education");
						List<Education> educationList = getValueEducationListInfo(educationReq);
						int result3 = advisorService.modifyEducation(advId, educationList);
						if (result3 == 0) {
							logger.info("Error occured while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_updated_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else if (errors.isEmpty() == false) {
					logger.error("Validation error");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

	private List<Education> getValueEducationListInfo(List<EducationReq> educationReqList) {
		List<Education> educationList = new ArrayList<>();
		for (EducationReq educationReq : educationReqList) {
			Education edu = new Education();
			edu.setEduId(educationReq.getEduId());
			if (educationReq.getDegree() != null) {
				edu.setDegree(educationReq.getDegree());
			}
			if (educationReq.getField() != null) {
				edu.setField(educationReq.getField());
			}
			if (educationReq.getInstitution() != null) {
				edu.setInstitution(educationReq.getInstitution());
			}
			if (educationReq.getFromYear() != null) {
				edu.setFromYear(educationReq.getFromYear());
			}
			if (educationReq.isTillDate()) {
				edu.setToYear("Till Date");
			} else {
				if (educationReq.getToYear() != null) {
					edu.setToYear(educationReq.getToYear());
				}
			}
			educationList.add(edu);
		}
		return educationList;
	}

	private List<Experience> getValueExperienceListInfo(List<ExperienceReq> experienceReqList) {
		List<Experience> experienceList = new ArrayList<>();
		for (ExperienceReq experienceReq : experienceReqList) {
			Experience exp = new Experience();
			exp.setExpId(experienceReq.getExpId());
			if (experienceReq.getCompany() != null) {
				exp.setCompany(experienceReq.getCompany());
			}
			if (experienceReq.getDesignation() != null) {
				exp.setDesignation(experienceReq.getDesignation());
			}
			if (experienceReq.getLocation() != null) {
				exp.setLocation(experienceReq.getLocation());
			}
			if (experienceReq.getFromYear() != null) {
				exp.setFromYear(experienceReq.getFromYear());
			}
			if (experienceReq.isTillDate()) {
				exp.setToYear("Till Date");
			} else {
				if (experienceReq.getToYear() != null) {
					exp.setToYear(experienceReq.getToYear());
				}
			}
			experienceList.add(exp);
		}
		return experienceList;
	}

	private List<Certificate> getValueCertificateListInfo(List<CertificateReq> certificateReqList) {
		List<Certificate> certificateList = new ArrayList<>();
		for (CertificateReq certificateReq : certificateReqList) {
			Certificate cert = new Certificate();
			cert.setCertificateId(certificateReq.getCertificateId());
			if (certificateReq.getTitle() != null) {
				cert.setTitle(certificateReq.getTitle());
			}
			if (certificateReq.getIssuedBy() != null) {
				cert.setIssuedBy(certificateReq.getIssuedBy());
			}
			if (certificateReq.getImagePath() != null) {
				cert.setImagePath(certificateReq.getImagePath());
			}
			if (certificateReq.getYear() != null) {
				cert.setYear(certificateReq.getYear());
			}
			certificateList.add(cert);
		}
		return certificateList;
	}

	private List<Award> getValueAwardListInfo(List<AwardReq> awardReqlist) {
		List<Award> awardList = new ArrayList<>();
		for (AwardReq awardReq : awardReqlist) {
			Award awd = new Award();
			awd.setAwardId(awardReq.getAwardId());
			if (awardReq.getTitle() != null) {
				awd.setTitle(awardReq.getTitle());
			}
			if (awardReq.getIssuedBy() != null) {
				awd.setIssuedBy(awardReq.getIssuedBy());
			}
			if (awardReq.getImagePath() != null) {
				awd.setImagePath(awardReq.getImagePath());
			}
			if (awardReq.getYear() != null) {
				awd.setYear(awardReq.getYear());
			} //
			awardList.add(awd);
		}
		return awardList;
	}

	public Advisor getValueProfessionalInfo(AdvProfessionalInfoRequest advProfInfoRequest) {
		Advisor adv = new Advisor();
		// Awards
		if (advProfInfoRequest != null && advProfInfoRequest.getAwards() != null) {
			List<AwardReq> awardsReq = advProfInfoRequest.getAwards();
			List<Award> awards = new ArrayList<Award>();

			for (AwardReq award : awardsReq) {
				Award awd = new Award();
				if (award.getTitle() != null) {
					awd.setTitle(award.getTitle());
				}
				if (award.getImagePath() != null) {
					awd.setImagePath(award.getImagePath());
				}
				if (award.getIssuedBy() != null) {
					awd.setIssuedBy(award.getIssuedBy());
				}
				if (award.getYear() != null) {
					awd.setYear(award.getYear());
				}
				awards.add(awd);
			}
			adv.setAwards(awards);

		}

		// Certificate

		if (advProfInfoRequest != null && advProfInfoRequest.getCertificates() != null) {
			List<CertificateReq> certificatesReq = advProfInfoRequest.getCertificates();
			List<Certificate> certificates = new ArrayList<Certificate>();

			for (CertificateReq certificate : certificatesReq) {
				Certificate cert = new Certificate();
				if (certificate.getTitle() != null) {
					cert.setTitle(certificate.getTitle());
				}
				if (certificate.getImagePath() != null) {
					cert.setImagePath(certificate.getImagePath());
				}
				if (certificate.getIssuedBy() != null) {
					cert.setIssuedBy(certificate.getIssuedBy());
				}
				if (certificate.getYear() != null) {
					cert.setYear(certificate.getYear());
				}
				certificates.add(cert);
			}
			adv.setCertificates(certificates);

		}
		// Experience

		if (advProfInfoRequest != null && advProfInfoRequest.getExperiences() != null) {
			List<ExperienceReq> experiencesReq = advProfInfoRequest.getExperiences();
			List<Experience> experiences = new ArrayList<Experience>();

			for (ExperienceReq experience : experiencesReq) {
				Experience exp = new Experience();
				if (experience.getCompany() != null) {
					exp.setCompany(experience.getCompany());
				}
				if (experience.getDesignation() != null) {
					exp.setDesignation(experience.getDesignation());
				}
				if (experience.getFromYear() != null) {
					exp.setFromYear(experience.getFromYear());
				}
				if (experience.isTillDate()) {
					exp.setToYear("Till Date");
				} else {
					if (experience.getToYear() != null) {
						exp.setToYear(experience.getToYear());
					}
				}
				if (experience.getLocation() != null) {
					exp.setLocation(experience.getLocation());
				}
				experiences.add(exp);
			}
			adv.setExperiences(experiences);
		}

		// Education

		if (advProfInfoRequest != null && advProfInfoRequest.getEducations() != null) {
			List<EducationReq> educationsReq = advProfInfoRequest.getEducations();
			List<Education> educations = new ArrayList<Education>();

			for (EducationReq education : educationsReq) {
				Education edu = new Education();
				if (education.getDegree() != null) {
					edu.setDegree(education.getDegree());
				}
				if (education.getInstitution() != null) {
					edu.setInstitution(education.getInstitution());
				}
				if (education.getField() != null) {
					edu.setField(education.getField());
				}
				if (education.getFromYear() != null) {
					edu.setFromYear(education.getFromYear());
				}
				if (education.isTillDate()) {
					edu.setToYear("Till Date");
				} else {
					if (education.getToYear() != null) {
						edu.setToYear(education.getToYear());
					}
				}
				educations.add(edu);
			}
			adv.setEducations(educations);
		}

		return adv;
	}

	/**
	 * Method to change the password
	 * 
	 * @param RequestBody
	 *            contains the PasswordChangeRequest
	 * @return ResponseEntity<String> contains the either the Result of change the
	 *         password or ErrorResponse
	 */
	// Change Password
	@ApiOperation(value = "change the password", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/changePassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> changePassword(@NonNull @RequestBody PasswordChangeRequest passwordChangeRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (passwordChangeRequest != null) {
			int screenId = passwordChangeRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String roleBasedId = passwordChangeRequest.getRoleBasedId();
		if (roleBasedId == null) {
			logger.info("roleBasedId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_role(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			// roleId_Advisor=1 //
			long roleIdAdv = advisorService.fetchAdvRoleIdByName();
			// roleId_NonIndividual_Advisor=3 //
			long roleIdNonAdv = advisorService.fetchNonAdvRoleIdByName();
			// roleId_Investor=2 //
			long roleIdInv = advisorService.fetchInvRoleIdByName();

			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			Party party = advisorService.fetchPartyByRoleBasedId(roleBasedId);

			if (party == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// check a password match current password and new password
				logger.info("Checking password match");
				if (advisorService.checkForPasswordMatch(roleBasedId,
						passwordChangeRequest.getCurrentPassword()) == false) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getIncorrect_password());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					errors = passwordValidator.validate(passwordChangeRequest);
					if (errors.isEmpty() == true) {
						int roleId = getRoleIdFromUserRole(party.getPartyId());
						if (roleId == roleIdInv) {
							logger.info("Changing Investor password");
							int result = advisorService.changeInvPassword(roleBasedId,
									passwordChangeRequest.getNewPassword());

							if (result == 0) {
								logger.info("Error occurred while adding data into table");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
										appMessages.getPassword_changed_successfully(), null, roleFieldRights);
								return ResponseEntity.ok().body(response);
							}

						} else if (roleIdNonAdv == roleId || roleIdAdv == roleId) {
							logger.info("Changing Advisor password");
							int result = advisorService.changeAdvPassword(roleBasedId,
									passwordChangeRequest.getNewPassword());
							if (result == 0) {
								logger.info("Error occurred while adding data into table");
								AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured());
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
										appMessages.getPassword_changed_successfully(), null, roleFieldRights);
								return ResponseEntity.ok().body(response);
							}

						} else {
							logger.error("Invalid roleId");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getRoleId_not_found());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					} else {
						logger.error("Validation Errors");
						AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(),
								errors, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
		}
	}

	/**
	 * Method to resetPassword
	 * 
	 * @param RequestBody
	 *            contains the PasswordChangeRequest
	 * @return ResponseEntity<String> contains the either the success or
	 *         ErrorResponse
	 */
	// Reset Password
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> resetPassword(@RequestParam String key,
			@RequestBody ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
		String decodedKey = decodeKey(key);
		List<String> values = Arrays.asList(decodedKey.split(","));
		String startKey = values.get(0);
		// String name = values.get(1);
		String midKey = values.get(2);
		String emailId = values.get(3);
		String phoneNumber = values.get(4);
		String timestamp = values.get(5);
		String endKey = values.get(6);
		Timestamp ts = Timestamp.valueOf(timestamp);
		String mailSub = sendMail.getMailText(MailConstants.FORGET_PASSWRD + "_SUBJECT");
		long minutes = diffOfTimeStampInMin(ts);
		if (minutes > mailConstants.getLink_validity()) {
			logger.error("ResetPassword link expired");
			AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE, appMessages.getLink_expired());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			long roleIdAdv = advisorService.fetchAdvRoleIdByName();
			long roleIdNonAdv = advisorService.fetchNonAdvRoleIdByName();
			long roleIdInv = advisorService.fetchInvRoleIdByName();

			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			String emailId_lc = emailId.toLowerCase();
			Party party = advisorService.fetchPartyByEmailId(emailId_lc);
			if (party == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				boolean isVerified = verifyKey(startKey, midKey, endKey);
				if (isVerified && party.getPhoneNumber().equals(phoneNumber) && party.getEmailId().equals(emailId)) {
					String latestKey = commonService.fetchLatestKeyByEmailIdAndSub(emailId, mailSub);
					if (key.equals(latestKey)) {
						String roleBasedId = party.getRoleBasedId();
						// check a password match current password and new password
						logger.info("Validating password");
						errors = resetPasswordRequestValidator.validate(resetPasswordRequest);
						if (errors.isEmpty() == true) {
							int roleId = getRoleIdFromUserRole(party.getPartyId());
							if (roleId == roleIdInv) {
								if (resetPasswordRequest.getNewPassword() == null) {
									logger.info("New Password is Mandatory");
									AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
											appMessages.getMandatory_fields_password(), null, null);
									return ResponseEntity.ok().body(response);
								} else {
									logger.info("Changing Investor password");
									int result = advisorService.changeInvPassword(roleBasedId,
											resetPasswordRequest.getNewPassword());
									if (result == 0) {
										logger.info("Error occurred while adding data into table");
										AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
												appMessages.getError_occured());
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
									} else {
										AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
												appMessages.getPassword_changed_successfully());
										return ResponseEntity.ok().body(response);
									}
								}
							} else if (roleIdNonAdv == roleId || roleIdAdv == roleId) {
								logger.info("Changing Advisor password");
								int result = advisorService.changeAdvPassword(roleBasedId,
										resetPasswordRequest.getNewPassword());
								if (result == 0) {
									logger.info("Error occurred while adding data into table");
									AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
											appMessages.getError_occured());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								} else {
									AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
											appMessages.getPassword_changed_successfully());
									return ResponseEntity.ok().body(response);
								}
							}
						} else {
							logger.error("Validation Errors");
							AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(),
									errors, null);
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					} else {
						logger.error("Invalid link");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getLink_not_valid());
						return ResponseEntity.ok().body(response);
					}
				} else {
					logger.error("Cannot reset password");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getCannot_reset_password());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	/**
	 * Method to forgetPassword
	 * 
	 * @param RequestBody
	 *            contains the PasswordChangeRequest
	 * @return ResponseEntity<String> contains the either the success or
	 *         ErrorResponse
	 */
	// Reset Password
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest,
			HttpServletRequest request) {
		long roleIdAdv = advisorService.fetchAdvRoleIdByName();
		long roleIdNonAdv = advisorService.fetchNonAdvRoleIdByName();
		long roleIdInv = advisorService.fetchInvRoleIdByName();
		String emailId = forgetPasswordRequest.getEmailId();
		if (emailId == null) {
			logger.info("emailId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_emailId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			String emailId_lc = emailId.toLowerCase();
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			Party party = advisorService.fetchPartyByEmailId(emailId_lc);
			if (party == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found_with_emailid());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				String roleBasedId = party.getRoleBasedId();
				// check a password match current password and new password
				logger.info("Validating password");
				errors = forgetPasswordRequestValidator.validate(forgetPasswordRequest);
				if (errors.isEmpty() == true) {
					int roleId = getRoleIdFromUserRole(party.getPartyId());
					if (roleId == roleIdInv) {
						logger.info("Fetching investor");
						Investor inv = advisorService.fetchInvestorByInvId(roleBasedId);
						List<String> toUsers = new ArrayList<>();
						toUsers.add(inv.getEmailId());
						// FileSystemResource file = new FileSystemResource("D:\\test.txt");
						sendMail.sendMailMessage(MailConstants.FORGET_PASSWRD, toUsers, mailConstants.getFromUser(),
								null, inv.getFullName(), inv.getEmailId(), inv.getPhoneNumber());
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getReset_password_mail_sent());
						return ResponseEntity.ok().body(response);

					} else if (roleIdNonAdv == roleId || roleIdAdv == roleId) {
						logger.info("Fetching advisor");
						Advisor adv = advisorService.fetchByAdvisorId(roleBasedId);
						List<String> toUsers = new ArrayList<>();
						toUsers.add(adv.getEmailId());
						// FileSystemResource file = new FileSystemResource("D:\\test.txt");
						sendMail.sendMailMessage(MailConstants.FORGET_PASSWRD, toUsers, mailConstants.getFromUser(),
								null, adv.getName(), adv.getEmailId(), adv.getPhoneNumber());
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getReset_password_mail_sent());
						return ResponseEntity.ok().body(response);
					} else if (roleIdAdv == roleId) {
						logger.info("Fetching advisor");
						Blogger blog = advisorService.fetchByBloggerId(roleBasedId);
						List<String> toUsers = new ArrayList<>();
						toUsers.add(blog.getEmailId());
						// FileSystemResource file = new FileSystemResource("D:\\test.txt");
						sendMail.sendMailMessage(MailConstants.FORGET_PASSWRD, toUsers, mailConstants.getFromUser(),
								null, blog.getUserName(), blog.getEmailId(), blog.getPhoneNumber());
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getReset_password_mail_sent());
						return ResponseEntity.ok().body(response);
					}
				} else {
					logger.error("Validation Errors");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/resendMail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> resendMail(@RequestBody ResendMailRequest resendMailRequest) {
		long roleIdAdv = advisorService.fetchAdvRoleIdByName();
		long roleIdNonAdv = advisorService.fetchNonAdvRoleIdByName();
		long roleIdInv = advisorService.fetchInvRoleIdByName();
		long reoleIdBlogger = advisorService.fetchBloggerIdByName();
		String emailId = null;
		boolean isVerified = false;
		if (resendMailRequest != null && resendMailRequest.getTeamEmail() != null) {
			String mailSubTeam = sendMail.getMailText(MailConstants.SIGN + "_SUBJECT");
			String latestKeyTeam = commonService.fetchLatestKeyByEmailIdAndSub(resendMailRequest.getTeamEmail(),
					mailSubTeam);
			String decodedKey = decodeKey(latestKeyTeam);
			List<String> values = Arrays.asList(decodedKey.split(","));
			String startKey = values.get(0);
			emailId = values.get(1);
			String midKey = values.get(2);
			String endKey = values.get(6);
			isVerified = verifyKey(startKey, midKey, endKey);
		} else if (resendMailRequest != null && resendMailRequest.getTeamEmail() == null) {
			if (resendMailRequest.getKey().equals(AdvisorConstants.SIGNUP_RESEND)) {
				String decodedKey = decodeKey(resendMailRequest.getToken());
				List<String> values = Arrays.asList(decodedKey.split(","));
				String startKey = values.get(0);
				emailId = values.get(1);
				String midKey = values.get(2);
				String endKey = values.get(6);
				isVerified = verifyKey(startKey, midKey, endKey);
				String mailSub = sendMail.getMailText(MailConstants.SIGN + "_SUBJECT");
				String latestKey = commonService.fetchLatestKeyByEmailIdAndSub(emailId, mailSub);
				if (!resendMailRequest.getToken().equals(latestKey)) {
					logger.error("Invalid link");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getLink_not_valid());
					return ResponseEntity.ok().body(response);
				}
			} else if (resendMailRequest.getKey().equals(AdvisorConstants.PASSWRD_RESEND)) {
				String decodedKey = decodeKey(resendMailRequest.getToken());
				List<String> values = Arrays.asList(decodedKey.split(","));
				String startKey = values.get(0);
				String midKey = values.get(2);
				emailId = values.get(3);
				String endKey = values.get(6);
				isVerified = verifyKey(startKey, midKey, endKey);
				String mailSub = sendMail.getMailText(MailConstants.FORGET_PASSWRD + "_SUBJECT");
				String latestKey = commonService.fetchLatestKeyByEmailIdAndSub(emailId, mailSub);
				if (!resendMailRequest.getToken().equals(latestKey)) {
					logger.error("Invalid link");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getLink_not_valid());
					return ResponseEntity.ok().body(response);
				}
			}
		}
		if (isVerified) {
			String emailId_lc = emailId.toLowerCase();
			Party party = advisorService.fetchPartyByEmailId(emailId_lc);
			if (party == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found_with_emailid());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int roleId = getRoleIdFromUserRole(party.getPartyId());
				if (roleId == roleIdAdv || roleId == roleIdNonAdv) {
					Advisor adv = advisorService.fetchByAdvisorId(party.getRoleBasedId());
					if (resendMailRequest.getKey().equals(AdvisorConstants.SIGNUP_RESEND)) {
						if (resendMailRequest.getTeamEmail() != null) {
							Party corpparty = advisorService.fetchPartyByPartyId(adv.getParentPartyId());
							Advisor corpadvisor = advisorService.fetchByAdvisorId(corpparty.getRoleBasedId());
							List<String> toUsers = new ArrayList<>();
							toUsers.add(emailId);
							sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
									adv.getName(), adv.getPhoneNumber(), adv.getPassword(), "TEAM",
									corpadvisor.getName());
						} else {
							List<String> toUsers = new ArrayList<>();
							toUsers.add(emailId);
							sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
									adv.getName(), adv.getPhoneNumber(), adv.getPassword());
						}
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getVerification_mail_sent());
						return ResponseEntity.ok().body(response);
					} else if (resendMailRequest.getKey().equals(AdvisorConstants.PASSWRD_RESEND)) {
						List<String> toUsers = new ArrayList<>();
						toUsers.add(adv.getEmailId());
						// FileSystemResource file = new FileSystemResource("D:\\test.txt");
						sendMail.sendMailMessage(MailConstants.FORGET_PASSWRD, toUsers, mailConstants.getFromUser(),
								null, adv.getName(), adv.getEmailId(), adv.getPhoneNumber());
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getReset_password_mail_sent());
						return ResponseEntity.ok().body(response);
					}
				} else if (roleId == roleIdInv) {
					Investor inv = advisorService.fetchInvestorByInvId(party.getRoleBasedId());
					if (resendMailRequest.getKey().equals("signup")) {
						List<String> toUsers = new ArrayList<>();
						toUsers.add(inv.getEmailId());
						sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
								inv.getFullName(), inv.getPhoneNumber(), inv.getPassword(), MailConstants.INV_SIGN);
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getVerification_mail_sent());
						return ResponseEntity.ok().body(response);
					} else if (resendMailRequest.getKey().equals("password")) {
						List<String> toUsers = new ArrayList<>();
						toUsers.add(inv.getEmailId());
						// FileSystemResource file = new FileSystemResource("D:\\test.txt");
						sendMail.sendMailMessage(MailConstants.FORGET_PASSWRD, toUsers, mailConstants.getFromUser(),
								null, inv.getFullName(), inv.getEmailId(), inv.getPhoneNumber());
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getReset_password_mail_sent());
						return ResponseEntity.ok().body(response);
					} else if (roleId == reoleIdBlogger) {
						Blogger blog = advisorService.fetchBloggerByBloggerId(party.getRoleBasedId());
						if (resendMailRequest.getKey().equals("signup")) {
							List<String> toUsers = new ArrayList<>();
							toUsers.add(blog.getEmailId());
							sendMail.sendMailMessage(MailConstants.SIGN, toUsers, mailConstants.getFromUser(), null,
									blog.getFullName(), blog.getPhoneNumber(), blog.getPassword(),
									MailConstants.INV_SIGN);
							AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
									appMessages.getVerification_mail_sent());
							return ResponseEntity.ok().body(response);
						} else if (resendMailRequest.getKey().equals("password")) {
							List<String> toUsers = new ArrayList<>();
							toUsers.add(blog.getEmailId());
							// FileSystemResource file = new FileSystemResource("D:\\test.txt");
							sendMail.sendMailMessage(MailConstants.FORGET_PASSWRD, toUsers, mailConstants.getFromUser(),
									null, blog.getFullName(), blog.getEmailId(), blog.getPhoneNumber());
							AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
									appMessages.getReset_password_mail_sent());
							return ResponseEntity.ok().body(response);
						}
					}
				}
			}
		} else {
			logger.error("Invalid token");
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getToken_not_valid());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * Method to add the promotion for advisor
	 * 
	 * @param RequestBody
	 *            contains the promotionRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         advisor promotion or ErrorResponse
	 */
	// Add Advisor Video
	@ApiOperation(value = "add the promotion", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addPromotion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addPromotion(@NonNull @RequestBody PromotionRequest promotionRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (promotionRequest != null) {
			int screenId = promotionRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = promotionRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			List<PromotionReq> promotionReq = promotionRequest.getPromotionReq();
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			// Advisor advisor = advisorService.fetchByAdvisorId(advId);
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				errors = promotionRequestValidator.validate(promotionRequest);
				if (errors.isEmpty() == true) {
					if (promotionReq != null) {
						logger.info("Modifying promotion");
						List<Promotion> promotionList = getValuePromotionList(promotionReq);
						int result = advisorService.addAndModifyPromotion(advId, promotionList);
						if (result == 0) {
							logger.info("Error occured while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							logger.info("Deleted SuccessFully");
							AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
									appMessages.getAdvisor_updated_successfully());
							return ResponseEntity.status(HttpStatus.OK).body(response);
						}
					}
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else if (errors.isEmpty() == false) {
					logger.error("Validation error");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
	}

	private List<Promotion> getValuePromotionList(List<PromotionReq> promotionReqList) {
		List<Promotion> promotionList = new ArrayList<>();
		for (PromotionReq promotionReq : promotionReqList) {
			Promotion promo = new Promotion();
			promo.setPromotionId(promotionReq.getPromotionId());
			if (promotionReq.getTitle() != null) {
				promo.setTitle(promotionReq.getTitle());
			}
			if (promotionReq.getAboutVideo() != null) {
				promo.setAboutVideo(promotionReq.getAboutVideo());
			}
			if (promotionReq.getVideo() != null) {
				promo.setVideo(promotionReq.getVideo());
			}
			if (promotionReq.getImagePath() != null) {
				promo.setImagePath(promotionReq.getImagePath());
			}
			promotionList.add(promo);
		}
		return promotionList;
	}

	/**
	 * Method to add the brand info for advisor
	 * 
	 * @param RequestBody
	 *            contains the AdvBrandInfoRequest
	 * @return ResponseEntity<String> contains the either the Result of addition of
	 *         advisor brand info or ErrorResponse
	 */
	@ApiOperation(value = "add advisor brand information", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addAdvBrandInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addAdvBrandInfo(@RequestBody AdvBrandInfoRequest advBrandInfoRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advBrandInfoRequest != null) {
			int screenId = advBrandInfoRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advBrandInfoRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			// Advisor advisor = advisorService.fetchByAdvisorId(advId);
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Removing brandRank and brandInfo from table and add new info
				advisorService.removeAdvBrandInfoByAdvId(advId);
				advisorService.removeAdvBrandRankByAdvId(advId);
				// Add brandInfo
				for (AdvBrandInfoReq brandInfoReq : advBrandInfoRequest.getBrandInfoReqList()) {
					if (brandInfoReq.getBrandId1() == 0) {
						logger.error("Some fields are empty");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getBrand_should_be_added());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						List<Long> brands = new ArrayList<Long>();
						if (brandInfoReq.getBrandId1() != 0) {
							brands.add(brandInfoReq.getBrandId1());
						}
						if (brandInfoReq.getBrandId2() != 0) {
							brands.add(brandInfoReq.getBrandId2());
						}
						if (brandInfoReq.getBrandId3() != 0) {
							brands.add(brandInfoReq.getBrandId3());
						}
						List<AdvBrandInfo> advBrandInfoList = getValueBrandInfo(brands, brandInfoReq);
						logger.info("Adding advisor brand info into DB");
						int resultBrandInfo = advisorService.addAdvBrandInfo(advId, advBrandInfoList);
						if (resultBrandInfo == 0) {
							logger.info("Error occured while adding data into table");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
						// calculate brandRank using brandInfo
						int result = addBrandrank(advId, brandInfoReq.getProdId());
						if (result == 0) {
							logger.error("Error occurred while adding Brand Rank");
							AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
									appMessages.getError_occured_brandrank());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						}
					}
				}
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
						appMessages.getAdvisor_added_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	public List<AdvBrandInfo> getValueBrandInfo(List<Long> brands, AdvBrandInfoReq brandInfoReq) {
		List<AdvBrandInfo> advBrandInfoList = new ArrayList<AdvBrandInfo>();
		int count = 0;
		for (long brandId : brands) {
			AdvBrandInfo advBrandInfo = new AdvBrandInfo();
			if (brandInfoReq.getProdId() != 0) {
				advBrandInfo.setProdId(brandInfoReq.getProdId());
			}
			if (brandInfoReq.getServiceId() != null) {
				advBrandInfo.setServiceId(brandInfoReq.getServiceId());
			}
			if (brandId != 0) {
				advBrandInfo.setBrandId(brandId);
			}
			advBrandInfo.setPriority(++count);

			advBrandInfoList.add(advBrandInfo);
		}
		return advBrandInfoList;
	}

	// calculate brandRank
	int addBrandrank(String advId, long prodId) {
		HashMap<Long, Integer> brandIdAndRank = new HashMap<>();
		int rank = 0;
		// fetch brandId by advId and prodId
		List<AdvBrandInfo> advBrandInfoList = advisorService.fetchAdvBrandInfoByAdvIdAndProdId(advId, prodId);
		List<Long> brandIdList = new ArrayList<Long>();
		for (AdvBrandInfo advBrandInfo : advBrandInfoList) {
			brandIdList.add(advBrandInfo.getBrandId());
		}
		// store barandId and its equivalent count in hashmap
		HashMap<Long, Integer> brandIdAndCount = new HashMap<Long, Integer>();
		for (long brandId : brandIdList) {
			Integer count = brandIdAndCount.get(brandId);
			if (count == null) {
				brandIdAndCount.put(brandId, 1);
			} else {
				brandIdAndCount.put(brandId, ++count);
			}
		}
		// sort the brandIdAndCount map by count in reverse order and store in
		// sorted
		// map
		LinkedHashMap<Long, Integer> sorted = new LinkedHashMap<Long, Integer>();
		brandIdAndCount.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
		// iterating the sorted map
		Iterator<Map.Entry<Long, Integer>> iterator = sorted.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, Integer> entry = iterator.next();
			// count the occurance of value in map
			int count = Collections.frequency(sorted.values(), entry.getValue());
			if (count == 1) {
				brandIdAndRank.put(entry.getKey(), ++rank);
			} else {
				List<Long> keys = new ArrayList<>();
				for (Entry<Long, Integer> entryKey : sorted.entrySet()) {
					if (Objects.equals(entry.getValue(), entryKey.getValue())) {
						keys.add(entryKey.getKey());
					}
				}
				// if count repeated for more than one brandId check the
				// priority
				LinkedHashMap<Long, Long> sortedMap = priorityRanking(keys, advId, prodId);
				for (Map.Entry<Long, Long> entry1 : sortedMap.entrySet()) {
					brandIdAndRank.put(entry1.getKey(), ++rank);
				}
				for (int i = 1; i < keys.size(); i++) {
					iterator.next();
				}
			}
		}
		// sortedBrandAndRank contains key -- brand , value -- rank
		HashMap<Long, Integer> sortedBrandAndRank = new LinkedHashMap<>();
		brandIdAndRank.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.naturalOrder())).limit(5)
				.forEachOrdered(x -> sortedBrandAndRank.put(x.getKey(), x.getValue()));
		return advisorService.addBrandRankIntoTable(sortedBrandAndRank, advId, prodId);
	}

	// private int addBrandRankIntoTable(HashMap<Long, Integer> sortedBrandAndRank,
	// String advId, long prodId) {
	// for (Map.Entry<Long, Integer> entry : sortedBrandAndRank.entrySet()) {
	// int result = 0;
	// if (advisorService.fetchAdvBrandRank(advId, prodId, entry.getValue()) ==
	// null) {
	// // Add brandRank
	// result = advisorService.addAdvBrandAndRank(entry.getKey(), entry.getValue(),
	// advId, prodId);
	// } else {
	// // Update brandRank
	// result = advisorService.updateBrandAndRank(entry.getKey(), entry.getValue(),
	// advId, prodId);
	// }
	// if (result == 0) {
	// return result;
	// }
	// }
	// return 1;
	// }

	private LinkedHashMap<Long, Long> priorityRanking(List<Long> brands, String advId, long prodId) {
		Map<Long, Long> brandIdAndPriority = new HashMap<>();
		for (long brandId : brands) {
			// Sorting by priority
			// adding the priority value for the brandId
			List<Long> priorityList = advisorService.fetchPriorityByBrandIdAndAdvId(advId, prodId, brandId);
			long priority = 0;
			for (long priority1 : priorityList) {
				priority = priority + priority1;
			}
			brandIdAndPriority.put(brandId, priority);
		}
		LinkedHashMap<Long, Long> sortedMap = sortAndRank(brandIdAndPriority);
		return sortedMap;
	}

	private LinkedHashMap<Long, Long> sortAndRank(Map<Long, Long> brandIdAndPriority) {
		// sortedMap -- key - brand, value- priority sum
		LinkedHashMap<Long, Long> sortedMap = new LinkedHashMap<>();
		brandIdAndPriority.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		return sortedMap;
	}

	/**
	 * Fetch Advisor Brand Rank
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<AdvBrandRank> or ErrorResponse
	 */
	@ApiOperation(value = "fetch advisor brand rank by advId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAdvBrandRank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAdvBrandRankByAdvId(@NonNull @RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advIdRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching brandRank by advId");
			List<AdvBrandRank> advBrandRank = advisorService.fetchAdvBrandRankByAdvId(advId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
					advBrandRank, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	// LookUp Table Fetch Services

	/**
	 * Fetch All Product List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Product>> or ErrorResponse
	 * 
	 */
	@RequestMapping(value = "/fetch-all-product", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchProductList() {
		logger.info("Fetching product list");
		List<Product> productList = advisorService.fetchProductList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), productList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Role List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Role>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all role", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-role", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRoleList() {
		logger.info("Fetching role list");
		List<RoleAuth> roleList = advisorService.fetchRoleList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), roleList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Party Status List
	 * 
	 * @param null
	 * @return ResponseEntity<List<PartyStatus>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all partyStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-partystatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchPartyStatusList() {
		logger.info("Fetching partystatus list");
		List<PartyStatus> partyStatusList = advisorService.fetchPartyStatusList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				partyStatusList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Service List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Service>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all service", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-service", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchServiceList() {
		logger.info("Fetching service list");
		List<Service> serviceList = advisorService.fetchServiceList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), serviceList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Brand List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Brand>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-brand", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchBrandList() {
		logger.info("Fetching brand list");
		List<Brand> brandList = advisorService.fetchBrandList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), brandList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All License List
	 * 
	 * @param null
	 * @return ResponseEntity<List<License>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all license", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-license", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchLicenseList() {
		logger.info("Fetching license list");
		List<License> licenseList = advisorService.fetchLicenseList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), licenseList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Remuneration List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Remuneration>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all remuneration", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-remuneration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRemunerationList() {
		logger.info("Fetching remuneration list");
		List<Remuneration> remunerationList = advisorService.fetchRemunerationList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				remunerationList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All productServBrand List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Product>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all product-service-brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-productServBrand", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllServiceAndBrand() {
		logger.info("Fetching product-service-brand list");
		List<Product> productList = advisorService.fetchAllServiceAndBrand();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), productList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All stateCityPincode List
	 * 
	 * @param null
	 * @return ResponseEntity<List<State>> or ErrorResponse
	 * 
	 */
	@RequestMapping(value = "/fetch-all-stateCityPincode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllStateCityPincode() {
		logger.info("Fetching state-city-pincode list");
		List<StateCity> stateCity = advisorService.fetchAllStateCityPincode();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), stateCity,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Category List
	 * 
	 * @param null
	 * @return ResponseEntity<List<Category>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all category", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-Category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchCategoryList() {
		logger.info("Fetching category list");
		List<Category> category = advisorService.fetchCategoryList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), category,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Category Type List
	 * 
	 * @param null
	 * @return ResponseEntity<List<CategoryType>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all categoryType", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-CategoryType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchCategoryTypeList() {
		logger.info("Fetching CategoryType list");
		List<CategoryType> categoryType = advisorService.fetchCategoryTypeList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), categoryType,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Forum Category List
	 * 
	 * @param null
	 * @return ResponseEntity<List<ForumCategory>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all ForumCategory", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-ForumCategory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchForumCategoryList() {
		logger.info("Fetching ForumCategory list");
		List<ForumCategory> forumCategory = advisorService.fetchForumCategoryList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), forumCategory,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All RiskQuestionaire List
	 * 
	 * @param null
	 * @return ResponseEntity<List<RiskQuestionaire>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all RiskQuestionaire", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-RiskQuestionaire", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchRiskQuestionaireList() {
		logger.info("Fetching RiskQuestionaire list");
		List<RiskQuestionaire> riskQuestionaire = advisorService.fetchRiskQuestionaireList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				riskQuestionaire, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Forum SubCategory List
	 * 
	 * @param null
	 * @return ResponseEntity<List<ForumSubCategory>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all ForumSubCategory", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-ForumSubCategory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchForumSubCategoryList() {
		logger.info("Fetching ForumSubCategory list");
		List<ForumSubCategory> forumSubCategoryList = advisorService.fetchForumSubCategoryList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				forumSubCategoryList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Forum Status List
	 * 
	 * @param null
	 * @return ResponseEntity<List<ForumStatus>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch all ForumStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-ForumStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchForumStatusList() {
		logger.info("Fetching ForumStatus list");
		List<ForumStatus> forumStatusList = advisorService.fetchForumStatusList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				forumStatusList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All Article Status List
	 * 
	 * @param null
	 * @return ResponseEntity<List<ArticleStatus>> or ErrorResponse
	 * 
	 */
	@ApiOperation(value = "fetch articleStatus list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-articleStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleStatusList() {
		logger.info("Fetching ArticleStatus list");
		List<ArticleStatus> articleStatusList = advisorService.fetchArticleStatusList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				articleStatusList, null);
		return ResponseEntity.ok().body(response);
	}

	@ApiOperation(value = "File Upload", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
		if (file == null) {
			logger.info("file is mandatory");
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_file());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			logger.info("Uploading file");
			String url = amazonClient.uploadFile(file);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), url, null);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "Delete the Uploaded File", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteFile(@RequestParam(value = "fileName") String fileName) {
		if (fileName == null) {
			logger.info("file is mandatory");
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_file());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			logger.info("Deleting file");
			amazonClient.deleteFile(fileName);
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getFile_deleted_successfully());
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "keyPeople signup", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/keyPeopleSignup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> keyPeopleSignup(@NonNull @RequestBody KeyPeopleRequest keyPeopleRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (keyPeopleRequest != null) {
			int screenId = keyPeopleRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
		// validate keyPeople request
		logger.info("Validating keyPeopleRequest");
		errors = keyPeopleRequestValidator.validate(keyPeopleRequest);
		if (errors.isEmpty() == true) {
			if (keyPeopleRequest.getFullName() != null && keyPeopleRequest.getDesignation() != null) {
				KeyPeople keyPeople = getValueKeyPeople(keyPeopleRequest);
				int result = advisorService.addKeyPeople(keyPeople);
				if (result != 0) {
					logger.info("Adding keyPeople data into db");
					AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
							appMessages.getKeyPeople_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else {
				logger.error("Some fields are empty");
				AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
						appMessages.getMandatory_fields_key());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		} else if (errors.isEmpty() == false) {
			logger.error("Validation Error");
			AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	// getValue keyPeople method //
	private KeyPeople getValueKeyPeople(KeyPeopleRequest keyPeopleRequest) {
		KeyPeople keyPeople = new KeyPeople();

		if (keyPeopleRequest != null && keyPeopleRequest.getFullName() != null) {
			keyPeople.setFullName(keyPeopleRequest.getFullName());
		}
		if (keyPeopleRequest != null && keyPeopleRequest.getDesignation() != null) {
			keyPeople.setDesignation(keyPeopleRequest.getDesignation());
		}
		if (keyPeopleRequest != null && keyPeopleRequest.getImage() != null) {
			keyPeople.setImage(keyPeopleRequest.getImage());
		}
		if (keyPeopleRequest != null && keyPeopleRequest.getParentPartyId() != 0) {
			keyPeople.setParentPartyId(keyPeopleRequest.getParentPartyId());
		}
		return keyPeople;
	}

	/**
	 * Method to modify KeyPeople
	 * 
	 * @param RequestBody
	 *            contains the KeyPeopleRequest
	 * @return ResponseEntity<String> contains the either the Result of changes of
	 *         KeyPeople or ErrorResponse
	 */
	// This service api is not used in normal advisor flow, but it may be used in
	// admin flow
	@ApiOperation(value = "modify the keypeople", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/modifyKeyPeople", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> modifyKeyPeople(@NonNull @RequestBody KeyPeopleRequest keyPeopleRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (keyPeopleRequest != null) {
			int screenId = keyPeopleRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long keyPeopleId = keyPeopleRequest.getKeyPeopleId();
		if (keyPeopleId == 0) {
			logger.info("keyPeopleId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_keyPeopleId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			HashMap<String, HashMap<String, String>> errors = new HashMap<String, HashMap<String, String>>();
			// KeyPeople keyPeople =
			// advisorService.fetchKeyPeopleByKeyPeopleId(keyPeopleId);
			int keyPeople = advisorService.checkKeyPeopleIsPresent(keyPeopleId);
			if (keyPeople == 0) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// Validating advisor request
				errors = keyPeopleRequestValidator.validate(keyPeopleRequest);
				if (errors.isEmpty() == true) {
					KeyPeople key = getModifyKeyPeopleValue(keyPeopleRequest);// get value Method call
					logger.info("Modifying keyPeople");
					int result = advisorService.modifyKeyPeople(keyPeopleRequest.getKeyPeopleId(), key);
					if (result == 0) {
						logger.info("Error occurred while adding data into table");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
								appMessages.getError_occured());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
								appMessages.getAdvisor_updated_successfully(), null, roleFieldRights);
						return ResponseEntity.ok().body(response);
					}
				} else if (errors.isEmpty() == false) {
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), errors,
							null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}

	private KeyPeople getModifyKeyPeopleValue(KeyPeopleRequest keyPeopleRequest) {
		KeyPeople key = new KeyPeople();
		key.setFullName(keyPeopleRequest.getFullName());
		key.setDesignation(keyPeopleRequest.getDesignation());
		key.setImage(keyPeopleRequest.getImage());
		return key;
	}

	/**
	 * Method to Remove KeyPeople
	 * 
	 * @param RequestBody
	 *            contains the IdRequest
	 * @return ResponseEntity<KeyPeople> contains the either the Result of keyPeople
	 *         remove or ErrorResponse
	 */
	// TODO : need to change RequestMethod.DELETE
	/*---Remove a Record by advId---*/
	@ApiOperation(value = "remove the keypeople", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeKeyPeople", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteKeypeople(@NonNull @RequestBody IdRequest idRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long id = idRequest.getId();
		if (id == 0) {
			logger.info("keyPeopleId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_keyPeopleId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching keyPeople");
			// KeyPeople keyPeople = advisorService.fetchKeyPeopleByKeyPeopleId(id);
			int keyPeople = advisorService.checkKeyPeopleIsPresent(id);
			if (keyPeople == 0) {
				logger.info("No record Found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Removing keyPeople");
				int result = advisorService.removeKeyPeople(id);
				if (result == 0) {
					logger.info("Error occurred while removing data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getAdvisor_deleted_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
	}

	/**
	 * Method to fetch KeyPeople
	 * 
	 * @param RequestBody
	 *            contains the IdRequest
	 * @return ResponseEntity<?> contains the either the fetch of keyPeople or empty
	 *         array
	 */
	@ApiOperation(value = "fetch the keyPeople by parentId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchkeyPeopleByParentId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchKeyPeopleByParentId(@NonNull @RequestBody IdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long parentPartyId = idRequest.getId();
		if (parentPartyId == 0) {
			logger.info("parentId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_parentPartyId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching keyPeople by parentId");
			List<KeyPeople> keyPeople = advisorService.fetchKeyPeopleByParentId(parentPartyId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), keyPeople,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to deactivate teamMember
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<?> contains the either the Result of deactivate
	 *         teamMember or ErrorResponse
	 */
	@ApiOperation(value = "Deactive the team member", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/teamMemberDeactivate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> teamMemberDeactivate(@NonNull @RequestBody AdvIdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = idRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int deactivate = advisorService
						.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_deactivated());
				int result = advisorService.teamMemberDeactivate(advId, deactivate);
				if (result != 0) {
					logger.info("Updating Team Member delete_Flag as Y");
					AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
							appMessages.getTeam_Member_Deactivated(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while updating data into table");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to add workFlowStatus
	 * 
	 * @param RequestBody
	 *            contains the StatusRequest
	 * @return ResponseEntity<?> contains the either the workFlowStatus add or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "add workFlowStatus", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/workFlowStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> workFlowStatus(@RequestBody StatusRequest statusRequest, HttpServletRequest request) {
		String advId = statusRequest.getAdvId();
		int status = statusRequest.getStatus();
		List<RoleFieldRights> roleFieldRights = null;
		if (statusRequest != null) {
			int screenId = statusRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (advId == null && status == 0) {
			logger.info("advisorId and status is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_status(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.error("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int status_revoked = advisorService
						.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_revoked());
				if (status_revoked == status && statusRequest.getReason() == null) {
					logger.info("Required reason for revoke");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getRequired_reason_revoked());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				int result = advisorService.addWorkFlowStatusByAdvId(advId, status, statusRequest.getReason());
				if (result != 0) {
					int status_draft = advisorService
							.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_drafted());
					int status_created = advisorService
							.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_created());
					int status_pending = advisorService
							.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_pending());
					int status_deleted = advisorService
							.fetchWorkFlowStatusIdByDesc(advTableFields.getWorkflow_status_deleted());

					if (status_draft == status || status_created == status || status_pending == status) {
						logger.info("Adding workFlowStatus into db");
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getWorkFlowStatus_added_successfully());
						return ResponseEntity.ok().body(response);
					} else if (status_deleted == status) {
						logger.info("Removed advisor from db");
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getAdvisor_deleted_successfully());
						return ResponseEntity.ok().body(response);
					} else {
						logger.info("Adding approved workFlowStatus into db"); /// to be Changed
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getWorkFlowStatus_added_asApproved_successfully());
						return ResponseEntity.ok().body(response);
					}
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to fetch all approvedAdvisor
	 * 
	 * @param RequestBody
	 *            contains null
	 * @return ResponseEntity<?> contains the either the approvedAdvisorList or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "fetch all the approved advisors", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllApprovedAdvisor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllApprovedAdvisor(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int advList = advisorService.fetchTotalApprovedAdv();

		// if (pageNum == 0) {
		// AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
		// appMessages.getInvalid_pagenum());
		// return ResponseEntity.ok().body(response);
		// } else {
		// if (pageNum == 1) {
		// } else {
		// pageNum = (pageNum - 1) * records + 1
		// }
		List<Advisor> advisors = advisorService.fetchApprovedAdv(pageNum, records);
		AdvTotalList advTotalList = new AdvTotalList();
		if (advisors != null) {
			advTotalList.setAdvisors(advisors);
			advTotalList.setTotalRecords(advList);
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advTotalList,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
		// }
	}

	@ApiOperation(value = "fetch the count of followers", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchFollowersCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchFollowersCount(@NonNull @RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advIdRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Fetching count of followers");
				List<Integer> counts = advisorService.fetchFollowersCount(advId);
				int total = 0;
				for (int count : counts) {
					total = total + count;
				}
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), total,
						roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	@ApiOperation(value = "fetch the count of dashboard", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchDashboardCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchDashboardCount(@NonNull @RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String roleBasedId = advIdRequest.getRoleBasedId();
		if (roleBasedId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			Party party = advisorService.fetchPartyByRoleBasedId(roleBasedId);
			Dashboard dashBoard = new Dashboard();
			logger.info("Fetching count of followers");
			if (party.getRoleBasedId().startsWith("ADV")) {
				List<Integer> counts = advisorService.fetchFollowersCount(roleBasedId);
				int followers = 0;
				for (int count : counts) {
					followers = followers + count;
				}
				dashBoard.setFollowers(followers);
				int sharedPlan = advisorService.fetchSharedPlanCountPartyId(party.getPartyId());
				dashBoard.setSharedPlan(sharedPlan);

			}
			int following = advisorService.fetchFollowersCountByUserId(roleBasedId);
			int plannedUser = advisorService.fetchPlannedUserCountPartyId(party.getPartyId());
			dashBoard.setFollowing(following);
			dashBoard.setPlannedUser(plannedUser);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), dashBoard,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "fetch the followers by advId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchFollowersByAdvId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchFollowersByAdvId(@NonNull @RequestBody FollowerRequest followerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (followerRequest != null) {
			int screenId = followerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = followerRequest.getAdvId();
		if (advId == null) {
			logger.info("advisorId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching followers");
			List<Followers> followers = advisorService.fetchFollowersByAdvId(advId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), followers,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	@RequestMapping(value = "/sendOtp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> sendOtp(@RequestBody OtpRequest otpRequest) {
		String phoneNumber = otpRequest.getPhoneNumber();
		if (phoneNumber == null) {
			logger.info("phoneNumber is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_phone(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			Party party = advisorService.fetchPartyByPhoneNumberAndDeleteFlag(phoneNumber);
			if (party == null) {
				logger.error("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				List<String> phoneNumbers = new ArrayList<>();
				String phone = smsConstants.getIndia_code() + phoneNumber;
				phoneNumbers.add(phone);
				double randNumber = Math.floor(100000 + Math.random() * 900000);
				long otpNum = Math.round(randNumber);
				String otp = Long.toString(otpNum);
				String key = SmsConstants.OTP;
				String smsresponse = sendSms.sendSms(phoneNumbers, key, otp);
				String res = "";
				try {
					JSONObject jsonObject = new JSONObject(smsresponse);
					res = jsonObject.getString("status");
				} catch (JSONException e) {
					logger.error(e.getMessage());
				}
				if (res.equals("success")) {
					int result = advisorService.addOtpForPhoneNumber(phoneNumber, otp);
					if (result == 0) {
						logger.error("Error occur while adding into table");
						AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.error_occured);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
								appMessages.getOtp_sent_successfully());
						return ResponseEntity.ok().body(response);
					}
				} else {
					logger.error("SMS error response : " + smsresponse);
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getOtp_not_sent());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	@RequestMapping(value = "/verifyOtp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {

		String phoneNumber = otpRequest.getPhoneNumber();
		String otp = otpRequest.getOtp();
		if (phoneNumber == null || otp == null) {
			logger.info("phoneNumber&otp is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_otp(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			Party party = advisorService.fetchPartyByPhoneNumberAndDeleteFlag(phoneNumber);
			if (party == null) {
				logger.error("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				boolean isValid = advisorService.validateOtp(phoneNumber, otpRequest.getOtp());
				if (isValid) {
					logger.info("Fetching generated otp");
					GeneratedOtp generatedOtp = advisorService.fetchGeneratedOtp(phoneNumber, otpRequest.getOtp());
					Timestamp created = generatedOtp.getCreated();
					long minutes = diffOfTimeStampInMin(created);
					if (minutes < smsConstants.getOtp_validity()) {
						if (otpRequest.getType() != null
								&& otpRequest.getType().equals(AdvisorConstants.MOBILE_VERIFICATION)) {
							int result = advisorService.updateMobileAsVerified(party);
							if (result == 0) {
								logger.error("Error occured while updating as verified");
								AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE,
										appMessages.getError_occured(), false, null);
								return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
										appMessages.getOtp_verified(), true, null);
								return ResponseEntity.ok().body(response);
							}
						}
						AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
								appMessages.getOtp_verified(), true, null);
						return ResponseEntity.ok().body(response);
					} else {
						logger.error("OTP expired");
						AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE,
								appMessages.getOtp_expired(), false, null);
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else {
					logger.error("Wrong OTP");
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE,
							appMessages.getOtp_not_verified(), false, null);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Fetch All FollowerStatus List
	 * 
	 * @param null
	 * @return ResponseEntity<List<FollowerStatus>> or SuccessCode with Empty Array
	 * 
	 */
	@ApiOperation(value = "fetch follower status list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-followerStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchFollowerStatusList() {
		logger.info("Fetching followerStatus List");
		List<FollowerStatus> followerStatusList = advisorService.fetchFollowerStatusList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				followerStatusList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All WorkFlow Status List
	 * 
	 * @param null
	 * @return ResponseEntity<List<WorkFlowStatus>> or SuccessCode with Empty Array
	 * 
	 */
	@ApiOperation(value = "fetch workFlowStatus list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-workFlowStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchWorkFlowStatusList() {
		logger.info("Fetching workFlowStatus List");
		List<WorkFlowStatus> workFlowStatusList = advisorService.fetchWorkFlowStatusList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				workFlowStatusList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Explore Advisor
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */
	/*---Explore Advisor ---*/
	@ApiOperation(value = "explore the advisor", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/exploreAdvisor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreAdvisorList(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String sortByState,
			@RequestParam(defaultValue = "") String sortByCity, @RequestParam(defaultValue = "") String sortByPincode,
			@RequestParam(defaultValue = "") String sortByDisplayName,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		int advList = advisorService.fetchTotalExploreAdvisorList(sortByState, sortByCity, sortByPincode,
				sortByDisplayName);
		List<Advisor> advisors = new ArrayList<>();
		logger.info("Fetching explore Advisor list");

		if (sortByState.equals("") && sortByCity.equals("") && sortByPincode.equals("")
				&& sortByDisplayName.equals("")) {
			// If param is empty
			// Fetch By Recent(created) Order
			advisors = advisorService.fetchExploreAdvisorDESCListOrder(pageNum, records, sortByState, sortByCity,
					sortByPincode, sortByDisplayName);
		} else {
			// Fetch By alphabetic order by name
			advisors = advisorService.fetchExploreAdvisorList(pageNum, records, sortByState, sortByCity, sortByPincode,
					sortByDisplayName);
		}
		AdvTotalList advTotalList = new AdvTotalList();
		if (advisors != null) {
			advTotalList.setAdvisors(advisors);
			advTotalList.setTotalRecords(advList);
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advTotalList,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Explore Product
	 * 
	 * @param null
	 * @return ResponseEntity<List<Product>> or ErrorResponse
	 * 
	 */
	/*---Explore Product ---*/
	@RequestMapping(value = "/exploreProduct", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreProduct(@RequestParam(defaultValue = "") String productName,
			@RequestParam(defaultValue = "") String serviceName, @RequestParam(defaultValue = "") String brandName,
			@RequestParam(defaultValue = "") String servicePlanName,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (productName.equals("") && serviceName.equals("") && brandName.equals("") && servicePlanName.equals("")) {
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_explore(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching explore product list");
			List<ServicePlan> servicePlanList = advisorService.fetchExploreProductList(productName, serviceName,
					brandName, servicePlanName);
			AdvProductList advProductList = new AdvProductList();
			if (servicePlanList != null) {
				advProductList.setServicePlanList(servicePlanList);
				advProductList.setTotalRecords(servicePlanList.size());
			}
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
					advProductList, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Explore AdvisorByProduct
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */
	/*---Explore Advisor ---*/
	@ApiOperation(value = "explore the Advisor By Product", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/exploreAdvisorByProduct", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreAdvisorByProduct(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String sortByState,
			@RequestParam(defaultValue = "") String sortByCity, @RequestParam(defaultValue = "") String sortByPincode,
			@RequestParam(defaultValue = "") String sortByDisplayName,
			@RequestParam(defaultValue = "") String productId, @RequestParam(defaultValue = "") String serviceId,
			@RequestParam(defaultValue = "") String brandId, @RequestParam(defaultValue = "") String servicePlanName,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		List<String> stateCityPincodeList = new ArrayList<>();
		if (sortByState != null && !sortByState.equals("") && sortByCity.equals("") && sortByPincode.equals("")) {
			// System.out.println("state not null");
			List<String> pincodes = advisorService.fetchPincodeByState(sortByState);
			for (String pincode : pincodes) {
				if (!stateCityPincodeList.contains(pincode)) {
					stateCityPincodeList.add(pincode);
				}
			}
			stateCityPincodeList.addAll(pincodes);
			// System.out.println("From State ======= " + stateCityPincodeList.toString());
		}
		if (sortByState != null && !sortByState.equals("") && sortByCity != null && !sortByCity.equals("")
				&& sortByPincode.equals("")) {
			// System.out.println("state,city not null");
			List<String> pincodes = advisorService.fetchPincodeByStateAndCity(sortByState, sortByCity);
			for (String pincode : pincodes) {
				if (!stateCityPincodeList.contains(pincode)) {
					stateCityPincodeList.add(pincode);
				}
			}
			// System.out.println("From State City not null======= " +
			// stateCityPincodeList.toString());
		}
		if (sortByPincode != null && !sortByPincode.equals("")) {
			// System.out.println("state,city,pincode not null");
			List<String> pincodes = advisorService.fetchPincodeListByPincode(sortByPincode);
			for (String pincode : pincodes) {
				if (!stateCityPincodeList.contains(pincode)) {
					stateCityPincodeList.add(pincode);
				}
			}
			// System.out.println("From State City Pincode not null ======= " +
			// stateCityPincodeList.toString());
		}
		List<Advisor> advisorList = new ArrayList<>();
		List<ServicePlan> servicePlanList = new ArrayList<>();
		int advList = 0;
		if (productId != null && !productId.equals("") && serviceId != null && !serviceId.equals("") && brandId != null
				&& !brandId.equals("")) {
			logger.info("Fetching explore Advisor by product with Brand");
			servicePlanList = advisorService.fetchExploreProductListById(productId, serviceId, brandId,
					servicePlanName);
			advisorList = advisorService.fetchExploreAdvisorListByProduct(pageNum, records, stateCityPincodeList,
					sortByDisplayName, productId, serviceId, brandId);
			advList = advisorService.fetchTotalExploreAdvisorByProduct(stateCityPincodeList, sortByDisplayName,
					productId, serviceId, brandId);
		} else if (productId != null && !productId.equals("") && serviceId != null && !serviceId.equals("")
				&& (brandId == null || brandId.equals(""))) {
			// advproduct table
			logger.info("Fetching explore Advisor by product without Brand");
			advisorList = advisorService.fetchExploreAdvisorListByProductWithoutBrand(pageNum, records,
					stateCityPincodeList, sortByDisplayName, productId, serviceId);
			advList = advisorService.fetchTotalExploreAdvisorByProductWithoutBrandId(stateCityPincodeList,
					sortByDisplayName, productId, serviceId);
		} else if (productId != null && !productId.equals("") && serviceId.equals("") && brandId.equals("")) {
			logger.info("Fetching explore Advisor");
			advisorList = advisorService.fetchExploreAdvisorListByProdId(pageNum, records, stateCityPincodeList,
					sortByDisplayName, productId);
			advList = advisorService.fetchTotalExploreAdvisorListByProdId(stateCityPincodeList, sortByDisplayName,
					productId);
		} else if (productId.equals("") && serviceId.equals("") && brandId.equals("")) {
			// Fetch By Recent(created) Order
			advisorList = advisorService.fetchExploreAdvisorDESCListOrder(pageNum, records, sortByState, sortByCity,
					sortByPincode, sortByDisplayName);
			advList = advisorService.fetchTotalExploreAdvisorList(sortByState, sortByCity, sortByPincode,
					sortByDisplayName);
		}
		AdvTotalList advTotalList = new AdvTotalList();
		if (advisorList != null) {
			advTotalList.setAdvisors(advisorList);
			advTotalList.setServicePlanList(servicePlanList);
			advTotalList.setTotalRecords(advList);
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advTotalList,
				roleFieldRights);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * 
	 * Explore AdvisorByProduct
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */

	/*---Explore Advisor ---*/
	@RequestMapping(value = "/exploreAdvisorByProductWithOutToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreAdvisorByProductWithOutToken(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String sortByState,
			@RequestParam(defaultValue = "") String sortByCity, @RequestParam(defaultValue = "") String sortByPincode,
			@RequestParam(defaultValue = "") String sortByDisplayName,
			@RequestParam(defaultValue = "") String productId, @RequestParam(defaultValue = "") String serviceId,
			@RequestParam(defaultValue = "") String brandId, @RequestParam(defaultValue = "") String servicePlanName) {

		logger.info("Fetching explore Advisor by product");
		List<String> stateCityPincodeList = new ArrayList<>();
		if (sortByState != null && !sortByState.equals("") && sortByCity.equals("") && sortByPincode.equals("")) {
			List<String> pincodes = advisorService.fetchPincodeByState(sortByState);
			for (String pincode : pincodes) {
				if (!stateCityPincodeList.contains(pincode)) {
					stateCityPincodeList.add(pincode);
				}
			}
			stateCityPincodeList.addAll(pincodes);
		}
		if (sortByState != null && !sortByState.equals("") && sortByCity != null && !sortByCity.equals("")
				&& sortByPincode.equals("")) {
			List<String> pincodes = advisorService.fetchPincodeByStateAndCity(sortByState, sortByCity);
			for (String pincode : pincodes) {
				if (!stateCityPincodeList.contains(pincode)) {
					stateCityPincodeList.add(pincode);
				}
			}
		}
		if (sortByPincode != null && !sortByPincode.equals("")) {
			List<String> pincodes = advisorService.fetchPincodeListByPincode(sortByPincode);
			for (String pincode : pincodes) {
				if (!stateCityPincodeList.contains(pincode)) {
					stateCityPincodeList.add(pincode);
				}
			}
		}
		List<Advisor> advisorList = new ArrayList<>();
		List<ServicePlan> servicePlanList = new ArrayList<>();
		int advList = 0;
		if (productId != null && !productId.equals("") && serviceId != null && !serviceId.equals("") && brandId != null
				&& !brandId.equals("")) {
			logger.info("Fetching explore Advisor by product with Brand");
			servicePlanList = advisorService.fetchExploreProductListById(productId, serviceId, brandId,
					servicePlanName);
			advisorList = advisorService.fetchExploreAdvisorListByProduct(pageNum, records, stateCityPincodeList,
					sortByDisplayName, productId, serviceId, brandId);
			advList = advisorService.fetchTotalExploreAdvisorByProduct(stateCityPincodeList, sortByDisplayName,
					productId, serviceId, brandId);
		} else if (productId != null && !productId.equals("") && serviceId != null && !serviceId.equals("")
				&& (brandId == null || brandId.equals(""))) {
			// advproduct table
			logger.info("Fetching explore Advisor by product without Brand");
			advisorList = advisorService.fetchExploreAdvisorListByProductWithoutBrand(pageNum, records,
					stateCityPincodeList, sortByDisplayName, productId, serviceId);
			advList = advisorService.fetchTotalExploreAdvisorByProductWithoutBrandId(stateCityPincodeList,
					sortByDisplayName, productId, serviceId);
		} else if (productId != null && !productId.equals("") && serviceId.equals("") && brandId.equals("")) {
			logger.info("Fetching explore Advisor");
			advisorList = advisorService.fetchExploreAdvisorListByProdId(pageNum, records, stateCityPincodeList,
					sortByDisplayName, productId);
			advList = advisorService.fetchTotalExploreAdvisorListByProdId(stateCityPincodeList, sortByDisplayName,
					productId);
		} else if (productId.equals("") && serviceId.equals("") && brandId.equals("")) {
			logger.info("Fetching explore Advisor");
			advisorList = advisorService.fetchExploreAdvisorDESCListOrder(pageNum, records, sortByState, sortByCity,
					sortByPincode, sortByDisplayName);
			advList = advisorService.fetchTotalExploreAdvisorList(sortByState, sortByCity, sortByPincode,
					sortByDisplayName);
		}
		// List<Advisor> advisors = advisorService.fetchExploreAdvisorByProduct(pageNum,
		// records, sortByState, sortByCity,
		// sortByPincode, sortByDisplayName, productId, serviceId, brandId);
		List<ExploreAdvisor> exploreAdvisors = new ArrayList<>();
		AdvResponse response = new AdvResponse();
		if (advisorList != null) {
			for (Advisor adv : advisorList) {
				ExploreAdvisor expAdvisor = new ExploreAdvisor();
				expAdvisor.setDisplayName(adv.getDisplayName());
				expAdvisor.setUserName(adv.getUserName());
				expAdvisor.setState(adv.getState());
				expAdvisor.setCity(adv.getCity());
				expAdvisor.setPincode(adv.getPincode());
				expAdvisor.setImagePath(adv.getImagePath());
				exploreAdvisors.add(expAdvisor);
			}

			ExploreListAdvisor exploreListAdvisor = new ExploreListAdvisor();
			if (exploreAdvisors != null) {
				exploreListAdvisor.setAdvisors(exploreAdvisors);
				exploreListAdvisor.setTotalRecords(advList);
			}
			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), exploreListAdvisor,
					null);
		} else {
			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisorList, null);
		}
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Search Advisor
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */
	/*---Explore Advisor ---*/
	@ApiOperation(value = "search the advisor", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/searchAdvisor", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> searchAdvisor(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String panNumber,
			@RequestParam(defaultValue = "") String advType, @RequestParam(defaultValue = "") String emailId,
			@RequestParam(defaultValue = "") String phoneNumber, @RequestParam(defaultValue = "") String userName,
			@RequestParam(defaultValue = "") String workFlowStatusId,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		String pan_lc = panNumber.toLowerCase();
		String email_lc = emailId.toLowerCase();
		String phone_lc = phoneNumber.toLowerCase();
		String userName_lc = userName.toLowerCase();
		int pageNumber = pageNum;
		int record = records;
		int advList;
		List<Advisor> advisors;
		if (pageNumber == 0 && record == 0) {
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_advisor(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
			// int PageNumber = pageNum;
			// int Records = records;
			// List<Advisor> advisors = advisorService.fetchAdvisorList(pageNum, records);
			// int advList;
			// List<Advisor> advisors;
			// if (pageNum == 0 && records == 10) {
			// AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
			// appMessages.getMandatory_fields_advisor(), null, roleFieldRights);
			// return ResponseEntity.ok().body(response);
			// if (pageNum == 0 && records == 12) {
			// logger.info("pageNumber and records is Mandatory");
			// AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
			// appMessages.getMandatory_fields_advisor(), null, roleFieldRights);
			// return ResponseEntity.ok().body(response);
		} else {
			advList = advisorService.fetchTotalSearchAdvisorList(pan_lc, email_lc, phone_lc, userName_lc,
					workFlowStatusId, advType);
			logger.info("Fetching Advisor List by search");
			advisors = advisorService.fetchSearchAdvisorList(pageNumber, record, pan_lc, email_lc, phone_lc,
					userName_lc, workFlowStatusId, advType);

			AdvTotalList advTotalList = new AdvTotalList();
			if (advisors != null) {
				advTotalList.setAdvisors(advisors);
				advTotalList.setTotalRecords(advList);
			}
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
					advTotalList, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to add Followers
	 * 
	 * @param RequestBody
	 *            contains FollowerRequest
	 * @return ResponseEntity<?> contains the either the add of followers or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "add the followers", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addFollowers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addFollowers(@NonNull @RequestBody FollowerRequest followerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (followerRequest != null) {
			int screenId = followerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = followerRequest.getAdvId();
		String userId = followerRequest.getUserId();
		if (advId == null && userId == null) {
			logger.info("advisorId and userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_followers(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			if (advId.startsWith(AdvisorConstants.INV)) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
						appMessages.getCannot_follow_the_investor());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// Advisor advisor = advisorService.fetchByAdvisorId(advId);
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				if (advId.equals(userId)) {
					logger.error("cannot follow the same follower");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getCannot_follow_the_user());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					// int follower = advisorService.checkFollowersIsPresent(userId);
					int follower = advisorService.checkFollowersIsPresent(userId, advId);
					if (follower != 0) {
						logger.error("follower already following this account with this userId");
						AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								appMessages.getFollowers_already_present());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						// List<Followers> reFollower = advisorService.fetchReFollowersByUserId(userId);
						int reFollower = advisorService.checkReFollowersIsPresent(userId, advId);
						if (reFollower != 0) {
							logger.error("refollower already present with this userId");
							AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
									appMessages.getFollowers_already_present());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							Followers followers = advisorService.fetchBlockedFollowersByUserIdWithAdvId(advId, userId);
							if (followers != null) {
								logger.info("follower already blocked");
								int result = advisorService.updateFollowers(followers.getFollowersId(), userId);
								if (result != 0) {
									logger.info("Adding reFollowers data into db");
									AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
											appMessages.getRefollowers_added_successfully(), null, null);
									return ResponseEntity.ok().body(response);
								} else {
									logger.error("Error Occured while adding data into table");
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getError_occured());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}

							} else {
								Followers unfollow = advisorService.fetchUnFollowersByUserIdWithAdvId(advId, userId);
								if (unfollow != null) {
									logger.info("follower already blocked");
									int result = advisorService.updateUnFollowers(unfollow.getFollowersId(), userId);
									if (result != 0) {
										logger.info("Adding Followers data into db");
										AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
												appMessages.getFollowers_added_successfully(), null, null);
										return ResponseEntity.ok().body(response);
									} else {
										logger.error("Error Occured while adding data into table");
										AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
												appMessages.getError_occured());
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
									}

								} else {
									int result = advisorService.addFollowers(advId, userId);
									if (result != 0) {
										logger.info("Adding followers data into db");
										AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
												appMessages.getFollowers_added_successfully(), null, roleFieldRights);
										return ResponseEntity.ok().body(response);
									} else {
										logger.error("Error Occured while adding data into table");
										AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
												appMessages.getError_occured());
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
									}
								}
							}
						}
					}
					// }
				}
			}
		}
	}

	/**
	 * Method to block Followers
	 * 
	 * @param RequestBody
	 *            contains StatusRequest
	 * @return ResponseEntity<?> contains the either the update of followers status
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "block the followers", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/blockFollowers", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> blockFollowers(@NonNull @RequestBody FollowerRequest followerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (followerRequest != null) {
			int screenId = followerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = followerRequest.getAdvId();
		String userId = followerRequest.getUserId();
		String blockedBy = followerRequest.getBlockedBy();
		if (advId == null && userId == null && blockedBy == null) {
			logger.info("advisorId and userId and blockBy is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_block(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching followers by userId and advId");
			Followers followers = advisorService.fetchFollowersByUserIdWithAdvId(advId, userId);
			if (followers == null) {
				logger.error("No Record Found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int result = advisorService.blockFollowers(followers.getFollowersId(), blockedBy);
				if (result != 0) {
					logger.info("modify followers data into db");
					AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
							appMessages.getFollower_blocked(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to unFollow the Following
	 * 
	 * @param RequestBody
	 *            contains StatusRequest
	 * @return ResponseEntity<?> contains the either the update of followers status
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "unFollow the followers", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/unFollowByUserId", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> UnFollowByUserId(@NonNull @RequestBody FollowerRequest followerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (followerRequest != null) {
			int screenId = followerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = followerRequest.getAdvId();
		String userId = followerRequest.getUserId();
		if (advId == null && userId == null) {
			logger.info("advisorId and userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_followers(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching followers by userId and advId");
			Followers followers = advisorService.fetchFollowersByUserIdWithAdvId(advId, userId);
			if (followers == null) {
				logger.error("No Record Found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int result = advisorService.unFollowByUserId(followers.getFollowersId(), userId);
				if (result != 0) {
					logger.info("modify followers data into db");
					AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
							appMessages.getFollower_unfollowed(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to approve Followers
	 * 
	 * @param RequestBody
	 *            contains FollowerRequest
	 * @return ResponseEntity<?> contains the either the successCode of
	 *         approveFollowers or ErrorResponse
	 */
	@ApiOperation(value = "approve the followers", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/approveFollowers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> approveFollowers(@NonNull @RequestBody FollowerRequest followerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (followerRequest != null) {
			int screenId = followerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		String advId = followerRequest.getAdvId();
		String userId = followerRequest.getUserId();
		if (advId == null && userId == null) {
			logger.info("advisorId and userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_followers(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching followers by userId and advId");
			Followers followers = advisorService.fetchFollowersByUserIdWithAdvId(advId, userId);
			if (followers == null) {
				logger.error("No Record Found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int result = advisorService.approveFollowers(followers.getFollowersId(), advId);
				if (result != 0) {
					logger.info("modify followers data into db");
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getFollower_approved(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Fetch All Advisor Type List
	 * 
	 * @param null
	 * @return ResponseEntity<List<AdvisorType>> or SuccessCode with Empty Array
	 * 
	 */
	@ApiOperation(value = "fetch advisor type list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-advisorType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAdvisorType() {
		logger.info("Fetching advisor type list");
		List<AdvisorType> advisorTypeList = advisorService.fetchAdvisorTypeList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				advisorTypeList, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch All User Type List
	 * 
	 * @param null
	 * @return ResponseEntity<List<UserType>> or SuccessCode with Empty Array
	 * 
	 */
	@ApiOperation(value = "fetch userType list", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetch-all-userType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchuserType() {
		logger.info("Fetching user type list");
		List<UserType> userTypeList = advisorService.fetchUserTypeList();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), userTypeList,
				null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to add Chat
	 * 
	 * @param RequestBody
	 *            contains ChatRequest
	 * @return ResponseEntity<?> contains the either the add of Chat or
	 *         ErrorResponse
	 */
	@ApiOperation(value = "add the chat", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/addChat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> addChat(@NonNull @RequestBody ChatRequest chatRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (chatRequest != null) {
			int screenId = chatRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = chatRequest.getAdvId();
		String userId = chatRequest.getUserId();
		if (advId == null && userId == null) {
			logger.info("advisorId and userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_followers(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			if (advId.startsWith(AdvisorConstants.INV)) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
						appMessages.getCannot_follow_the_investor());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				if (advId.equals(userId)) {
					logger.error("cannot chat the same user");
					AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
							appMessages.getCannot_follow_the_user());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					int chatUserList = advisorService.checkChatUserIsPresent(userId, advId);

					if (chatUserList != 0) {
						logger.error("ChatUser already present with this userId");
						AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								appMessages.getChatuser_already_present());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					} else {
						int inActivechatUser = advisorService.checkInActiveChatUserIsPresent(userId, advId);
						if (inActivechatUser != 0) {
							logger.error("ChatUser already present with this userId");
							AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
									appMessages.getChatuser_already_present());
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
						} else {
							// int blockedchatUser = advisorService.checkBlockedChatUserIsPresent(userId);
							ChatUser blockedchatUser = advisorService.fetchBlockedChatUsersByUserIdWithAdvId(advId,
									userId);
							if (blockedchatUser != null) {
								logger.error("ChatUser already present with this userId");
								int result = advisorService.updateChatUser(blockedchatUser.getChatUserId(), userId);
								if (result != 0) {
									logger.info("Updating chatUser data into db");
									AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
											appMessages.getChatuser_updated_successfully(), null, roleFieldRights);
									return ResponseEntity.ok().body(response);
								} else {
									logger.error("Error Occured while adding data into table");
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getError_occured());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
								// AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								// appMessages.getChatuser_already_present());
								// return
								// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
							} else {
								int result = advisorService.addChatUser(advId, userId);
								if (result != 0) {
									logger.info("Adding chatUser data into db");
									AdvResponse response = responseWithData(InvestorConstants.SUCCESS_CODE,
											appMessages.getChatuser_added_successfully(), null, roleFieldRights);
									return ResponseEntity.ok().body(response);
								} else {
									logger.error("Error Occured while adding data into table");
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getError_occured());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Method to block Chat
	 *
	 * @param RequestBody
	 *            contains ChatRequest
	 * @return ResponseEntity<?> contains the either the successCode of UpdateChat
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "block the chat", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/blockChat", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> blockChat(@NonNull @RequestBody ChatRequest chatRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (chatRequest != null) {
			int screenId = chatRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		String advId = chatRequest.getAdvId();
		String userId = chatRequest.getUserId();
		String blockedBy = chatRequest.getBlockedBy();
		if (advId == null && userId == null && blockedBy == null) {
			logger.info("advisorId and userId and blockBy is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_block(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching Active chatUSer by userId and advId");
			ChatUser chatUser = advisorService.fetchActiveChatUser(advId, userId);// fetch active chatUsers//
			if (chatUser == null) {
				logger.error("No Record Found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int result = advisorService.blockChat(chatUser.getChatUserId(), blockedBy);
				if (result != 0) {
					logger.info("modify followers data into db");
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getChatuser_blocked_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to modify Chat
	 *
	 * @param RequestBody
	 *            contains ChatRequest
	 * @return ResponseEntity<?> contains the either the successCode of UpdateChat
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "approve the chat", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/approveChat", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> approveChat(@NonNull @RequestBody ChatRequest chatRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (chatRequest != null) {
			int screenId = chatRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		String advId = chatRequest.getAdvId();
		String userId = chatRequest.getUserId();
		if (advId == null && userId == null) {
			logger.info("advisorId and userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_followers(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching InActiveChatUser by userId and advId");
			ChatUser chatUser = advisorService.fetchChatUser(advId, userId);
			if (chatUser == null) {
				logger.error("No Record Found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				int result = advisorService.approveChat(chatUser.getChatUserId(), advId);
				if (result != 0) {
					logger.info("approve chatUser data into db");
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getChatuser_approved_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	/**
	 * Method to fetch approved Chat
	 *
	 * @param RequestBody
	 *            contains ChatRequest
	 * @return ResponseEntity<?> contains the either the successCode of
	 *         approvedChatList or emptyArray
	 */
	@ApiOperation(value = "fetch ApprovedChatUser List", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchChatUserByAdvId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchChatUserByAdvId(@NonNull @RequestBody ChatRequest chatRequest,
			HttpServletRequest request) {

		List<RoleFieldRights> roleFieldRights = null;
		if (chatRequest != null) {
			int screenId = chatRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = chatRequest.getAdvId();
		if (advId == null) {
			logger.info("advId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			List<ChatUser> chatUserList = advisorService.fetchChatUserListByAdvId(advId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
					chatUserList, null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Explore Advisor
	 * 
	 * @param null
	 * @return ResponseEntity<List<Advisor>> or ErrorResponse
	 * 
	 */
	/*---Explore Advisor ---*/
	@RequestMapping(value = "/exploreAdvisorWithOutToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreAdvisorWithOutToken(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String sortByState,
			@RequestParam(defaultValue = "") String sortByCity, @RequestParam(defaultValue = "") String sortByPincode,
			@RequestParam(defaultValue = "") String sortByDisplayName) {

		List<Advisor> advisors = new ArrayList<>();
		int advList = advisorService.fetchTotalExploreAdvisorList(sortByState, sortByCity, sortByPincode,
				sortByDisplayName);
		logger.info("Fetching explore Advisor list");
		if (sortByState.equals("") && sortByCity.equals("") && sortByPincode.equals("")
				&& sortByDisplayName.equals("")) {
			// Fetch By Recent(created) Order
			advisors = advisorService.fetchExploreAdvisorDESCListOrder(pageNum, records, sortByState, sortByCity,
					sortByPincode, sortByDisplayName);
		} else {
			// Fetch By alphabetic order by name
			advisors = advisorService.fetchExploreAdvisorList(pageNum, records, sortByState, sortByCity, sortByPincode,
					sortByDisplayName);
		}
		List<ExploreAdvisor> exploreAdvisors = new ArrayList<>();
		AdvResponse response = new AdvResponse();
		if (advisors != null) {
			for (Advisor adv : advisors) {
				ExploreAdvisor expAdvisor = new ExploreAdvisor();
				expAdvisor.setDisplayName(adv.getDisplayName());
				expAdvisor.setUserName(adv.getUserName());
				expAdvisor.setState(adv.getState());
				expAdvisor.setCity(adv.getCity());
				expAdvisor.setPincode(adv.getPincode());
				expAdvisor.setImagePath(adv.getImagePath());
				exploreAdvisors.add(expAdvisor);
			}

			ExploreListAdvisor exploreListAdvisor = new ExploreListAdvisor();
			if (exploreAdvisors != null) {
				exploreListAdvisor.setAdvisors(exploreAdvisors);
				exploreListAdvisor.setTotalRecords(advList);
			}
			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), exploreListAdvisor,
					null);
		} else {

			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisors, null);
		}
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Fetch Advisor by userName
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<Advisor> or ErrorResponse
	 */
	@RequestMapping(value = "/fetchAdvisorByUserNameWithOutToken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAdvisorByUserNameWithOutToken(@RequestBody AdvIdRequest advIdRequest) {
		String userName = advIdRequest.getUserName();
		if (userName == null) {
			logger.info("userName is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_userName(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			String userName_lc = userName.toLowerCase();
			logger.info("Fetching advisor by userName WithOut Token");
			Advisor advisor = advisorService.fetchAdvisorByUserNameWithOutToken(userName_lc);
			if (advisor != null) {
				long partyId = advisorService.fetchPartyIdByRoleBasedId(advisor.getAdvId());
				advisor.setKeyPeopleList(advisorService.fetchKeyPeopleByParentId(partyId));
				advisor.setTeamMemberList(advisorService.fetchPublishTeamByParentPartyId(partyId));
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
						advisor, null);
				return ResponseEntity.ok().body(response);
			} else {
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
						appMessages.getUser_not_available(), null, null);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Method to validate that the unique fields already present in DB
	 * 
	 * @param RequestBody
	 *            contains the UniqueFieldRequest
	 * @return ResponseEntity<String> contains the either the Result of presence of
	 *         unique fields
	 */
	@ApiOperation(value = "validate the unique fields", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/validateUniqueFields", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> validateUniqueFields(@RequestBody UniqueFieldRequest uniqueFieldRequest,
			HttpServletRequest request) {
		HashMap<String, String> errors = new HashMap<String, String>();
		if (uniqueFieldRequest.getEmailId() != null) {
			String emailId = uniqueFieldRequest.getEmailId();
			String emailId_lc = emailId.toLowerCase();
			Party party = advisorService.fetchPartyByEmailId(emailId_lc);
			if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_N())) {
				errors.put(AdvisorConstants.EMAILID, appMessages.getUser_already_present_emailid());
			} else if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_Y())) {
				errors.put(AdvisorConstants.EMAILID, appMessages.getUser_already_present_emailid_disabled());
			}
		}
		if (uniqueFieldRequest.getPanNumber() != null) {
			String pan = uniqueFieldRequest.getPanNumber();
			String pan_lc = pan.toLowerCase();
			Party party = advisorService.fetchPartyByPAN(pan_lc);
			if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_N())) {
				errors.put(AdvisorConstants.PAN, appMessages.getUser_already_present_pan());
			} else if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_Y())) {
				errors.put(AdvisorConstants.PAN, appMessages.getUser_already_present_pan_disabled());
			}
		}
		if (uniqueFieldRequest.getPhoneNumber() != null) {
			String phone = uniqueFieldRequest.getPhoneNumber();
			String phone_lc = phone.toLowerCase();
			Party party = advisorService.fetchPartyByPhoneNumber(phone_lc);
			if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_N())) {
				errors.put(AdvisorConstants.PHONENUMBER, appMessages.getUser_already_present_phone());
			} else if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_Y())) {
				errors.put(AdvisorConstants.PHONENUMBER, appMessages.getUser_already_present_phone_disabled());
			}
		}
		if (uniqueFieldRequest.getUserName() != null) {
			String username = uniqueFieldRequest.getUserName();
			String username_lc = username.toLowerCase();
			Party party = advisorService.fetchPartyByUserName(username_lc);
			if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_N())) {
				errors.put(AdvisorConstants.USERNAME, appMessages.getUser_already_present_with_username());
			} else if (party != null && party.getDelete_flag().equals(advTableFields.getDelete_flag_Y())) {
				errors.put(AdvisorConstants.USERNAME, appMessages.getUser_already_present_username_disabled());
			}
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), errors, null);
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to upload PDF file
	 * 
	 * @param RequestBody
	 *            contains the PDF file
	 * @return ResponseEntity<String> contains SuccessResponse with url of uploading
	 *         PDF
	 * 
	 */
	@ApiOperation(value = "PDF File upload", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/uploadPdfFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "prodId") int prodId, @RequestParam(value = "serviceId") int serviceId,
			@RequestParam(value = "brandId") int brandId) {
		if (prodId == 0 && serviceId == 0 && brandId == 0 && file == null) {
			logger.info("prodId,serviceId,brandId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_uploadPdf(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			String productName = advisorService.fetchProductNameByProdId(prodId);
			String serviceName = advisorService.fetchServiceNameByProdIdAndServiceId(prodId, serviceId);
			String brandName = advisorService.fetchBrandNameByProdIdAndBrandId(prodId, brandId);
			String fileName = file.getOriginalFilename();
			String planName = FilenameUtils.removeExtension(fileName);
			String product_str = productName.replace(" ", "_");
			String service_str = serviceName.replace(" ", "_");
			String brand_str = brandName.replace(" ", "_");
			String plan_str = fileName.replace(" ", "_");
			String createdPdfName = product_str + "_" + service_str + "_" + brand_str + "_" + plan_str;
			ServicePlan servicePlan = advisorService.fetchServicePlan(prodId, serviceId, brandId, planName);
			String url = null;
			if (servicePlan == null) {
				logger.info("Uploading PDF File");
				url = amazonClient.uploadPdfFile(file, createdPdfName);
				logger.info("Adding service plan");
				int result = advisorService.addServicePlan(prodId, serviceId, brandId, planName, url);
				if (result == 0) {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (servicePlan != null) {
				amazonClient.deleteFile(servicePlan.getServicePlanLink());
				url = amazonClient.uploadPdfFile(file, createdPdfName);
				logger.info("updating service plan");
				int result = advisorService.updateServicePlan(prodId, serviceId, brandId, planName, url);
				if (result == 0) {
					logger.error("Error Occured while adding data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), url, null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Search stateCityPincode By cityName
	 * 
	 * @param city
	 * @return ResponseEntity<List<StateCity>>
	 */
	@ApiOperation(value = "Search City by CityName", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/searchCity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> searchCity(@RequestParam(defaultValue = "") String cityName) {
		AdvResponse response = new AdvResponse();
		if (cityName.equals("")) {
			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields_city(), null,
					null);
		} else {
			logger.info("Searching state-city-pincode by cityName");
			List<CityList> stateCityList = advisorService.searchStateCityPincodeByCity(cityName);
			response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), stateCityList, null);
		}
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Method to save the chat messages
	 * 
	 * @param RequestBody
	 *            contains the ChatMessageRequest
	 * @return ResponseEntity<String> contains the either the Result of saving the
	 *         messages or ErrorResponse
	 */
	@ApiOperation(value = "save the chat message", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/saveChatMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> saveChatMessage(@RequestBody ChatMessageRequest chatMessageRequest,
			HttpServletRequest request) {
		List<ChatMessage> chatMessageList = getValueOfChatMessage(chatMessageRequest);
		int result = advisorService.saveChatMessage(chatMessageList);
		if (result == 0) {
			logger.error("Error Occured while adding data into table");
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getChat_saved_successfully(), null, null);
			return ResponseEntity.ok().body(response);
		}

	}

	/**
	 * Method to check the password match
	 * 
	 * @param RequestBody
	 *            contains the PasswordChangeRequest
	 * @return ResponseEntity<String> contains the either the Result of change the
	 *         password or ErrorResponse
	 */
	// Change Password
	@ApiOperation(value = "validate the password", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/validatePassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> validatePassword(@NonNull @RequestBody PasswordChangeRequest passwordChangeRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (passwordChangeRequest != null) {
			int screenId = passwordChangeRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String roleBasedId = passwordChangeRequest.getRoleBasedId();
		String currentPassword = passwordChangeRequest.getCurrentPassword();
		if (roleBasedId == null && currentPassword == null) {
			logger.info("roleBasedId and password is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_validatePassword(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			Party party = advisorService.fetchPartyByRoleBasedId(roleBasedId);
			if (party == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// check a password match current password and new password
				logger.info("Checking password match");
				if (advisorService.checkForPasswordMatch(roleBasedId, currentPassword) == false) {
					AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE,
							appMessages.getIncorrect_password(), false, roleFieldRights);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				} else {
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
							true, roleFieldRights);
					return ResponseEntity.ok().body(response);
				}
			}
		}
	}

	private List<ChatMessage> getValueOfChatMessage(ChatMessageRequest chatMessageRequest) {
		List<ChatMessage> chatMessageList = new ArrayList<>();
		for (ChatMessageReq chatMessageReq : chatMessageRequest.getChatMessageReqList()) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSenderId(chatMessageReq.getSenderId());
			chatMessage.setRecipientId(chatMessageReq.getRecipientId());
			chatMessage.setSenderName(chatMessageReq.getSenderName());
			chatMessage.setRecipientName(chatMessageReq.getRecipientName());
			chatMessage.setContent(chatMessageReq.getContent());
			chatMessage.setCreated(chatMessageReq.getCreated());
			chatMessageList.add(chatMessage);
		}
		return chatMessageList;
	}

	/**
	 * Method to fetch chatList by userId
	 * 
	 * @param RequestBody
	 *            contains the ChatRequest
	 * @return ResponseEntity<String> contains the either the Result of chatUserList
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "fetch ChatUserList ByUserId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchChatUserListByUserId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchChatUserListByUserId(@RequestBody ChatRequest chatRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (chatRequest != null) {
			int screenId = chatRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String userId = chatRequest.getUserId();
		if (userId == null) {
			logger.info("userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_userId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			List<ChatUser> chatUser = advisorService.fetchChatUserListByUserId(userId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), chatUser,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch followers by userId
	 * 
	 * @param RequestBody
	 *            contains the ChatRequest
	 * @return ResponseEntity<String> contains the either the Result of chatUserList
	 *         or ErrorResponse
	 */
	@ApiOperation(value = "fetch FollowersList ByUserId", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchFollowersListByUserId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchFollowersListByUserId(@RequestBody FollowerRequest followerRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (followerRequest != null) {
			int screenId = followerRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		String userId = followerRequest.getUserId();
		if (userId == null) {
			logger.info("userId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_userId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			List<Followers> followers = new ArrayList<Followers>();
			followers = advisorService.fetchFollowersListByUserId(userId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), followers,
					roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch chatUser count by advId
	 * 
	 * @param RequestBody
	 *            contains the AdvIdRequest
	 * @return ResponseEntity<String> contains the either the Result of chatUser
	 *         count or ErrorResponse
	 */
	@ApiOperation(value = "fetch the chat user count", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchChatUserCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchChatUserCount(@NonNull @RequestBody AdvIdRequest advIdRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (advIdRequest != null) {
			int screenId = advIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String advId = advIdRequest.getAdvId();
		if (advId == null) {
			logger.info("advId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			int advisor = advisorService.checkAdvisorIsPresent(advId);
			if (advisor == 0) {
				logger.error("No record found");
				AdvResponse response = messageResponse(InvestorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				logger.info("Fetching count of followers");
				List<Integer> counts = advisorService.fetchChatUserCount(advId);
				int total = 0;
				for (int count : counts) {
					total = total + count;
				}
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), total,
						roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	/**
	 * Method to test mail template
	 * 
	 * @param RequestBody
	 *            contains the MailTemplate Request
	 * @return ResponseEntity<String> contains the success or failer response of
	 *         mail sending
	 */
	@ApiOperation(value = "Test mail template", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/testMailTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> testMailTemplate(@RequestBody MailTemplateRequest mailTemplateRequest,
			HttpServletRequest request) {
		List<String> toUsers = new ArrayList<>();
		if (mailTemplateRequest.getEmailId() == null) {
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_emailId(), null, null);
			return ResponseEntity.ok().body(response);
		} else {
			toUsers.add(mailTemplateRequest.getEmailId());
			sendMail.sendMailMessage(MailConstants.TEST, toUsers, mailConstants.getFromUser(), null,
					mailTemplateRequest.getTemplate());
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), null,
					null);
			return ResponseEntity.ok().body(response);
		}
	}

	/**
	 * Method to fetch gst
	 * 
	 * @param RequestBody
	 *            contains the MailTemplate Request
	 * @return ResponseEntity<String> contains the success or failer response of
	 *         mail sending
	 */
	@ApiOperation(value = "Fetch GST", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchGst", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchGst(@RequestBody AdvIdRequest advIdRequest) {
		if (advIdRequest.getAdvId() == null) {
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getMandatory_fields(),
					null, null);
			return ResponseEntity.ok().body(response);
		} else {
			Advisor advisor = advisorService.fetchAdvisorGstByAdvId(advIdRequest.getAdvId());
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advisor,
					null);
			return ResponseEntity.ok().body(response);
		}
	}

	private AdvResponse responseWithData(long code, String message, Object data,
			List<RoleFieldRights> roleFieldRights) {
		AdvResponseMessage responseMessage = new AdvResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		AdvResponseData responseData = new AdvResponseData();
		responseData.setData(data);
		responseData.setRoleFieldRights(roleFieldRights);
		AdvResponse response = new AdvResponse();
		response.setResponseMessage(responseMessage);
		response.setResponseData(responseData);
		return response;
	}

	private AdvResponse messageResponse(long code, String message) {
		AdvResponseMessage responseMessage = new AdvResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		AdvResponse response = new AdvResponse();
		response.setResponseMessage(responseMessage);
		return response;
	}

	@ApiOperation(value = "fetch all lookup", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchAllLookup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllLookup() {
		logger.info("Fetching Lookup");
		LookUp lookUp = advisorService.fetchAllLookUp();
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), lookUp, null);
		return ResponseEntity.ok().body(response);

	}

	@ApiOperation(value = "create the Brand comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/brandsComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createBrandsComment(@NonNull @RequestBody BrandCommentRequest brandCommentReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (brandCommentReq != null) {
			int screenId = brandCommentReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getAdd());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String brandId = brandCommentReq.getParamId();
		if (brandCommentReq.getContent() == null || brandCommentReq.getPartyId() == 0) {
			logger.info("Mandatory fields");
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_comment());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			logger.info("Checking partyId");
			Party party = advisorService.fetchPartyByPartyId(brandCommentReq.getPartyId());
			// Checking party is present //
			if (party == null) {
				logger.info("No record found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getParty_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				// get value Method call for input values //
				BrandsComment comment = getValueOfComment(brandId, brandCommentReq);
				logger.info("Adding comment into DB");
				int result = advisorService.createBrandsComment(comment); // creating brandsComment by given
				// values//
				if (result != 0) {
					AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
							appMessages.getBrandscomment_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while adding data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError_occured());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			}
		}
	}

	private BrandsComment getValueOfComment(String paramId, BrandCommentRequest brandCommentReq) {
		BrandsComment commentvalue = new BrandsComment();
		if (brandCommentReq != null && brandCommentReq.getContent() != null) {
			commentvalue.setContent(brandCommentReq.getContent());
		}
		if (brandCommentReq != null && brandCommentReq.getPartyId() != 0) {
			commentvalue.setPartyId(brandCommentReq.getPartyId());
		}
		if (brandCommentReq != null && brandCommentReq.getParentCommentId() != 0) {
			commentvalue.setParentCommentId(brandCommentReq.getParentCommentId());
		}
		commentvalue.setParamId(paramId);
		return commentvalue;
	}

	@ApiOperation(value = "explore Advisor by brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/exploreAdvisorByBrandWithoutToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreAdvisorByBrandWithoutToken(@RequestParam(defaultValue = "") String brand) {
		logger.info("Fetching explore Advisor list By Brand");
		List<Advisor> advisor = advisorService.fetchExploreAdvisorByBrand(brand);
		int advList = advisorService.fetchTotalExploreAdvisorByBrand(brand);
		AdvTotalList advTotalList = new AdvTotalList();
		if (advisor != null) {
			advTotalList.setAdvisors(advisor);
			advTotalList.setTotalRecords(advList);
		}

		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advTotalList,
				null);
		return ResponseEntity.ok().body(response);

	}

	@ApiOperation(value = "explore Advisor by brand", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/exploreAdvisorByBrand", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> exploreAdvisorByBrand(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records, @RequestParam(defaultValue = "") String product,
			@RequestParam(defaultValue = "") String service, @RequestParam(defaultValue = "") String brand,
			@RequestParam(defaultValue = "") String city, @RequestParam(defaultValue = "") String state,
			@RequestParam(defaultValue = "") String pincode, @RequestParam(defaultValue = "") String displayName,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {

		AdvResponse response = null;
		List<Advisor> advisor = new ArrayList<>();
		int totalAdvisor = 0;
		if (!product.equals("") && !service.equals("") && !brand.equals("") && !state.equals("") && !city.equals("")
				&& !pincode.equals("")) {

			logger.info("Fetching explore Advisor list by prodServBrand and stateCityPincode");
			advisor = advisorService.fetchExploreAdvisorByProdDetailsAndStateDetails(pageNum, records, product, service,
					brand, state, city, pincode);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdDetailsAndStateDetails(product, service, brand,
					state, city, pincode);

		} else if (!product.equals("") && !service.equals("") && !brand.equals("") && !state.equals("")
				&& !city.equals("")) {

			logger.info("Fetching explore Advisor list By product,service,brand,state and city");
			advisor = advisorService.fetchExploreAdvisorByProdServBrandStateAndCity(pageNum, records, product, service,
					brand, state, city);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdServBrandStateAndCity(product, service, brand,
					state, city);

		} else if (!product.equals("") && !service.equals("") && !state.equals("") && !city.equals("")
				&& !pincode.equals("")) {

			logger.info("Fetching explore Advisor list by product,service and stateCityPincode");
			advisor = advisorService.fetchExploreAdvisorByprodServAndStateDetails(pageNum, records, product, service,
					state, city, pincode);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByprodServAndStateDetails(product, service, state,
					city, pincode);

		} else if (!state.equals("") && !city.equals("") && !pincode.equals("") && !displayName.equals("")) {

			logger.info("Fetching explore Advisor list By stateDetials and displayName");
			advisor = advisorService.fetchExploreAdvisorByCityDetailsAndDisplayName(pageNum, records, state, city,
					pincode, displayName);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByCityDetailsAndDisplayName(state, city, pincode,
					displayName);

		} else if (!product.equals("") && !service.equals("") && !brand.equals("") && !state.equals("")) {

			logger.info("Fetching explore Advisor list By product,service,brand and state");
			advisor = advisorService.fetchExploreAdvisorByProdServBrandAndState(pageNum, records, product, service,
					brand, state);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdServBrandAndState(product, service, brand,
					state);
		} else if (!product.equals("") && !service.equals("") && !state.equals("") && !city.equals("")) {

			logger.info("Fetching explore Advisor list By product,service,state and city");
			advisor = advisorService.fetchExploreAdvisorByProdServStateAndCity(pageNum, records, product, service,
					state, city);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdServStateAndCity(product, service, state, city);
		} else if (!product.equals("") && !state.equals("") && !city.equals("") && !pincode.equals("")) {

			logger.info("Fetching explore Advisor list By product state city and pincode");
			advisor = advisorService.fetchExploreAdvisorByProdStateCityAndPincode(pageNum, records, product, state,
					city, pincode);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdStateCityAndPincode(product, state, city,
					pincode);
		} else if (!product.equals("") && !state.equals("") && !city.equals("")) {

			logger.info("Fetching explore Advisor list By product,state and city");
			advisor = advisorService.fetchExploreAdvisorByProdStateAndCity(pageNum, records, product, state, city);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdStateAndCity(product, state, city);

		} else if (!product.equals("") && !service.equals("") && !state.equals("")) {

			logger.info("Fetching explore Advisor list By product,service and state");
			advisor = advisorService.fetchExploreAdvisorByProdServAndState(pageNum, records, product, service, state);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdServAndState(product, service, state);

		} else if (!product.equals("") && !service.equals("") && !brand.equals("")) {

			logger.info("Fetching explore Advisor list By productDetails");
			advisor = advisorService.fetchExploreAdvisorByProdDetails(pageNum, records, product, service, brand);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdDetails(product, service, brand);

		} else if (!state.equals("") && !city.equals("") && !pincode.equals("")) {

			logger.info("Fetching explore Advisor list By stateDetials");
			advisor = advisorService.fetchExploreAdvisorByCityDetails(pageNum, records, state, city, pincode);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByCityDetails(state, city, pincode);

		} else if (!state.equals("") && !city.equals("") && !displayName.equals("")) {

			logger.info("Fetching explore Advisor list By state,city,displayName");
			advisor = advisorService.fetchExploreAdvisorByStateCityAndDisplayName(pageNum, records, state, city,
					displayName);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByStateCityAndDisplayName(state, city, displayName);

		} else if (!state.equals("") && !city.equals("")) {

			logger.info("Fetching explore Advisor list By state and city");
			advisor = advisorService.fetchExploreAdvisorByCityDetailsWithoutPin(pageNum, records, state, city);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByCityDetailsWithoutPin(state, city);
		} else if (!state.equals("") && !displayName.equals("")) {
			logger.info("Fetching explore Advisor list By state and displayName");
			advisor = advisorService.fetchExploreAdvisorByStateAndDisplayName(pageNum, records, state, displayName);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByStateAndDisplayName(state, displayName);
		} else if (!product.equals("") && !state.equals("")) {

			logger.info("Fetching explore Advisor list By product and state");
			advisor = advisorService.fetchExploreAdvisorByProdAndState(pageNum, records, product, state);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdAndState(product, state);

		} else if (!product.equals("") && !service.equals("")) {

			logger.info("Fetching explore Advisor list By product and service");
			advisor = advisorService.fetchExploreAdvisorByProdDetailsWithoutBrand(pageNum, records, product, service);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByProdDetailsWithoutBrand(product, service);

		} else if (!product.equals("")) {

			logger.info("Fetching explore Advisor list by product");
			advisor = advisorService.fetchExploreAdvisorByproduct(pageNum, records, product);
			totalAdvisor = advisorService.fetchExploreAdvisorByproductTotal(product);

		} else if (!state.equals("")) {

			logger.info("Fetching explore Advisor list by state");
			advisor = advisorService.fetchExploreAdvisorByState(pageNum, records, state);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByState(state);

		} else if (!displayName.equals("")) {
			logger.info("Fetching explore Advisor list by display name");
			advisor = advisorService.fetchExploreAdvisorByDisplayName(pageNum, records, displayName);
			totalAdvisor = advisorService.fetchTotalExploreAdvisorByDisplayName(displayName);
		} else {
			logger.info("Fetching All publish Advisor List");
			advisor = advisorService.fetchAllExploreAdvisorList(pageNum, records);
			totalAdvisor = advisorService.fetchAllExploreAdvisorTotalList();
		}
		AdvTotalList advTotalList = new AdvTotalList();
		if (advisor != null && advisor.size() != 0) {
			advTotalList.setAdvisors(advisor);
			advTotalList.setTotalRecords(totalAdvisor);
		}

		response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(), advTotalList, null);
		return ResponseEntity.ok().body(response);

	}

	@ApiOperation(value = "moderate the brands comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/moderateBrandsComment", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> moderateBrandsComment(@NonNull @RequestBody ModerateCommentRequest moderateCommentReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (moderateCommentReq != null) {
			int screenId = moderateCommentReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getEdit());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		if (moderateCommentReq.getCommentId() == 0) {
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_commentId());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			BrandsComment brandsComment = getModifiedComment(moderateCommentReq);
			int result = advisorService.modifyComment(brandsComment);
			if (result == 0) {
				logger.error("Error Occured while adding data into table");
				AdvResponse response = responseWithData(AdvisorConstants.ERROR_CODE, appMessages.getError(), null,
						roleFieldRights);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} else {
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
						appMessages.getBrandscomment_moderated_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			}
		}
	}

	private BrandsComment getModifiedComment(ModerateCommentRequest moderateCommentReq) {
		BrandsComment brandsComment = new BrandsComment();
		// brandsComment.setParamId(moderateCommentReq.getParamId());
		brandsComment.setCommentId(moderateCommentReq.getCommentId());
		brandsComment.setContent(moderateCommentReq.getContent());
		return brandsComment;

	}

	@ApiOperation(value = "remove the brands comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeBrandsComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> deleteBrandsComment(@NonNull @RequestBody IdRequest idRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long commentId = idRequest.getId();
		if (commentId == 0) {
			logger.info("CommentId is Mandatory");
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_commentId());
			return ResponseEntity.ok().body(response);
		} else {
			// removing brandsComment by id //
			logger.info("Removing brands Comment");
			int result = advisorService.removeBrandsComment(commentId);
			if (result != 0) {
				AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
						appMessages.getRecord_deleted_successfully(), null, roleFieldRights);
				return ResponseEntity.ok().body(response);
			} else {
				logger.error("Error occured while removing data into table");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	@ApiOperation(value = "change profile as corporate", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/changeToCorporate", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<AdvResponse> changeToCorporate(@RequestBody AdvIdRequest advIdRequest) {
		logger.info("change to corporate");
		String emailId = advIdRequest.getEmailId();
		String emailId_lc = emailId.toLowerCase();
		Party party = advisorService.fetchPartyForSignIn(emailId_lc);
		long roleId = 0;
		List<User_role> user_role = null;
		if (party != null) {
			user_role = commonService.fetchUserRoleByUserId(party.getPartyId());
		}
		if (user_role != null) {
			if (user_role.size() == 1) {
				roleId = user_role.get(0).getRole_id();
			} else {
				for (User_role userRole : user_role) {
					if (userRole.getIsPrimaryRole() == 1) {
						roleId = userRole.getRole_id();
					}
				}
			}
		}
		int result = advisorService.changeToCorporate(advIdRequest.getAdvId(), roleId);
		if (result != 0) {
			AdvResponse response = messageResponse(AdvisorConstants.SUCCESS_CODE,
					appMessages.getProfile_changed_successfully());
			return ResponseEntity.ok().body(response);
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getError());
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "fetch all brands comments", authorizations = @Authorization(value = "Bearer"))
	@PostMapping(value = "/fetchAllComments", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchAllComments(@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "10") int records,
			@RequestBody(required = false) ScreenIdRequest screenIdRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (screenIdRequest != null) {
			int screenId = screenIdRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {

				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getView());
				if (roleFieldRights.isEmpty()) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

		int commentList = advisorService.fetchCommentsTotalList();
		logger.info("Fetching brands comments list");
		List<BrandsComment> brandsComment = advisorService.fetchCommentsList(pageNum, records);
		BrandsCommentList BrandsCommentTotalList = new BrandsCommentList();
		if (brandsComment != null) {
			BrandsCommentTotalList.setBrandsCommentList(brandsComment);
			BrandsCommentTotalList.setTotalRecords(commentList);
		}
		AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
				BrandsCommentTotalList, roleFieldRights);
		return ResponseEntity.ok().body(response);

	}

	@ApiOperation(value = "fetch brands comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchBrandsComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchBrandsComment(@RequestBody ParamIdRequest idRequest, HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (idRequest != null) {
			int screenId = idRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String paramId = idRequest.getParamId();
		if (paramId == null) {
			logger.info("paramId is Mandatory");
			AdvResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_brandId(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);

		} else {
			int brandComments = advisorService.fetchBrandsTotalComment(idRequest.getParamId());
			List<BrandsComment> brandComment = advisorService.fetchBrandsComment(idRequest.getParamId());

			BrandCommentList brandCommentList = new BrandCommentList();
			if (brandComment != null) {
				brandCommentList.setBrandCommentList(brandComment);
				brandCommentList.setTotalRecords(brandComments);
			}
			AdvResponse response = responseWithData(ForumConstants.SUCCESS_CODE, appMessages.getSuccess(),
					brandCommentList, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}
	
	@ApiOperation(value = "vote the comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/createLikeComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createCommentVote(@NonNull @RequestBody ArticleVoteRequest  articleVoteReq,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleVoteReq != null) {
			int screenId = articleVoteReq.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long commentId = articleVoteReq.getCommentId();
		if (articleVoteReq.getVoteType() == 0 || commentId == 0 || articleVoteReq.getPartyId() == 0) {
			logger.info("commentId,PartyId,voteType is Mandatory");
			AdvResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_voteComment(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetching comment");
			int commentPost = advisorService.fetchCommentPost(commentId); // Checking articlePost is present//
			if (commentPost == 0) {
				logger.info("No record Found");
				AdvResponse response = messageResponse(AdvisorConstants.NO_RECORD_FOUND,
						appMessages.getNo_record_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			// creating articleVote //
			logger.info("Voting the comment");
			int result1 = advisorService.createCommentVote(articleVoteReq.getVoteType(), commentId);
			// save articleVote //
			if (result1 != 0) {
				int result2 = advisorService.saveCommentVote(articleVoteReq.getVoteType(), commentId,
						articleVoteReq.getPartyId());
				logger.info("Save vote address");
				if (result2 != 0) {
					AdvResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
							appMessages.getCommentVote_added_successfully(), null, roleFieldRights);
					return ResponseEntity.ok().body(response);
				} else {
					logger.error("Error occured while saving data into table");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
							appMessages.getError_occured_remove());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else {
				logger.error("Error occured while adding data into table");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}
	
	@ApiOperation(value = "remove like from comment", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/removeCommentLike", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> removeCommentLike(@NonNull @RequestBody ArticleVoteRequest  articleVoteRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleVoteRequest != null) {
			int screenId = articleVoteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}

		long commentId = articleVoteRequest.getCommentId();
		long partyId = articleVoteRequest.getPartyId();
		if (commentId == 0 || articleVoteRequest.getPartyId() == 0) {
			logger.info("commentId,PartyId is Mandatory");
			AdvResponse response = responseWithData(ForumConstants.SUCCESS_CODE,
					appMessages.getCommentvote_removed_successfully(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Decrease like count from aricle");
			int unLikeResult = advisorService.decreaseLikeCount(commentId);
			if (unLikeResult == 0) {
				logger.error("Error occured while updating vote count");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			logger.info("Remove from comment vote address");
			int removeResult = advisorService.removeCommentVoteAddress(commentId, partyId);
			if (removeResult == 0) {
				logger.error("Error occured while removing from article vote address");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE,
						appMessages.getError_occured_remove());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getCommentvote_removed_successfully(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "fetch comment vote detail", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/fetchCommentVoteAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> fetchArticleVoteAddress(@NonNull @RequestBody ArticleVoteRequest  articleVoteRequest,
			HttpServletRequest request) {
		List<RoleFieldRights> roleFieldRights = null;
		if (articleVoteRequest != null) {
			int screenId = articleVoteRequest.getScreenId();
			if (screenId != 0 && screenRightsCommon.isAuthNeedForScreenId(screenId)) {
				roleFieldRights = rightsAuthentication(screenId, request, screenRightsConstants.getDelete());
				if (roleFieldRights == null) {
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getAccess_denied());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else if (screenId == 0) {
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getUnauthorized());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		long partyId = articleVoteRequest.getPartyId();
		if (articleVoteRequest.getPartyId() == 0) {
			logger.info("fetch address commentId,PartyId is Mandatory");
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE,
					appMessages.getMandatory_fields_fetchVoteAddress(), null, roleFieldRights);
			return ResponseEntity.ok().body(response);
		} else {
			logger.info("Fetch comment vote address");
			List<CommentVoteAddress> commentVoteAddress = advisorService.fetchCommentVoteAddress( partyId);
			AdvResponse response = responseWithData(AdvisorConstants.SUCCESS_CODE, appMessages.getSuccess(),
					commentVoteAddress, roleFieldRights);
			return ResponseEntity.ok().body(response);
		}
	}
}
