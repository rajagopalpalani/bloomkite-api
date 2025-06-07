package com.sowisetech.advisor.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sowisetech.admin.model.User_role;
import com.sowisetech.admin.service.AdminService;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.response.AdvResponse;
import com.sowisetech.advisor.response.AdvResponseMessage;
import com.sowisetech.advisor.security.JwtOtpRequest;
import com.sowisetech.advisor.security.JwtRequest;
import com.sowisetech.advisor.security.JwtResponse;
import com.sowisetech.advisor.security.JwtTokenUtil;
import com.sowisetech.advisor.security.JwtUserDetailsService;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.util.AdvAppMessages;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.advisor.util.AdvisorConstants;
import com.sowisetech.common.model.RoleScreenRights;
import com.sowisetech.common.service.CommonService;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.common.util.SmsConstants;
import com.sowisetech.investor.model.Investor;
import com.sowisetech.investor.service.InvestorService;
import com.sowisetech.investor.util.InvestorConstants;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtAuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	private AdvisorService advisorService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AdvAppMessages appMessages;
	@Autowired
	AdvTableFields advTableFields;
	@Autowired
	private AdminSignin adminSignin;
	@Autowired
	private AdminService adminService;
	@Autowired
	SmsConstants smsConstants;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	/**
	 * Signin
	 * 
	 * @param JwtRequest
	 * @return Token and Id or ErrorResponse
	 * 
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
			HttpServletRequest request) throws Exception {
		String username = authenticationRequest.getUsername();
		String username_lc = username.toLowerCase();
		Party party = advisorService.fetchPartyForSignIn(username_lc);
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
//		Party partyPresent = advisorService.fetchPartyForSignIn(authenticationRequest.getUsername());
		if (party == null) {
			AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE, appMessages.getUser_not_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			int result = authenticate(username_lc, authenticationRequest.getPassword());
			if (result == 0) {
				AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
						appMessages.getPassword_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		String role = advisorService.fetchRoleByRoleId(roleId);
		if (!role.equals(advTableFields.getRoleName_admin())) {
			if (role != null) {
				if (role.equals(advTableFields.getRoleName_nonIndividual())
						|| role.equals(advTableFields.getRoleName())) {
					Advisor adv = advisorService.fetchByAdvisorId(party.getRoleBasedId());
					if (adv.getIsVerified() == 0) {
						// return
						// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(appMessages.getAccount_not_verified());
						AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								appMessages.getCannot_login_not_verified());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else if (role.equals(advTableFields.getRoleName_inv())) {
					Investor inv = investorService.fetchByInvestorId(party.getRoleBasedId());
					if (inv.getIsVerified() == 0) {
						// return
						// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(appMessages.getAccount_not_verified());
						AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								appMessages.getCannot_login_not_verified());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				}
			}
			// Session
			// List<RoleScreenRights> roleScreenRights =
			// commonService.fetchScreenRightsByRoleId(roleId);
			Map<Integer, RoleScreenRights> role_screen = new HashMap<>();
			for (User_role userRole : user_role) {
				List<RoleScreenRights> roleScreenRights = commonService
						.fetchScreenRightsByRoleId(userRole.getRole_id());
				for (RoleScreenRights roleScreenRight : roleScreenRights) {
					role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
				}
			}
			for (User_role userRole : user_role) {
				if (userRole.getIsPrimaryRole() == 1) {
					List<RoleScreenRights> roleScreenRights = commonService
							.fetchScreenRightsByRoleId(userRole.getRole_id());
					for (RoleScreenRights roleScreenRight : roleScreenRights) {
						role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
					}
				}
			}
			final UserDetails userDetails = userDetailsService.loadUserByUsername(username_lc);
			final String token = jwtTokenUtil.generateToken(userDetails, advTableFields.getCommon_application());
			String roleBasedId = party.getRoleBasedId();
			// Advisor adv
			// =advisorService.fetchAdvisorByEmailId(authenticationRequest.getUsername());
			long partyId = advisorService.fetchPartyIdByRoleBasedId(roleBasedId);
			return ResponseEntity.ok(new JwtResponse(token, roleBasedId, partyId, roleId, role_screen));
		} else {
			AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE, appMessages.getLogin_failed());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@RequestMapping(value = "/adminSignin", method = RequestMethod.POST)
	public ResponseEntity<?> adminSigninCreateAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
			HttpServletRequest request) throws Exception {
		if (authenticationRequest.getUsername().equals(adminSignin.getEmailid())) {
			int result = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			if (result == 0) {
				AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
						appMessages.getPassword_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			int roleId = advisorService.fetchRoleIdByName(advTableFields.getRoleName_admin());
			List<RoleScreenRights> roleScreenRights = commonService.fetchScreenRightsByRoleId(roleId);
			Map<Integer, RoleScreenRights> role_screen = new HashMap<>();
			for (RoleScreenRights roleScreenRight : roleScreenRights) {
				role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
			}
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails, advTableFields.getAdmin_application());
			return ResponseEntity.ok(new JwtResponse(token, null, 0, 0, role_screen));
		} else if (adminService.fetchAdminByEmailId(authenticationRequest.getUsername()) != null) {
			Party party = advisorService.fetchPartyForSignIn(authenticationRequest.getUsername());
			List<User_role> user_role = null;
			if (party != null) {
				user_role = commonService.fetchUserRoleByUserId(party.getPartyId());
			}
			int result = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			if (result == 0) {
				AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
						appMessages.getPassword_not_found());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			Map<Integer, RoleScreenRights> role_screen = new HashMap<>();
			for (User_role userRole : user_role) {
				List<RoleScreenRights> roleScreenRights = commonService
						.fetchScreenRightsByRoleId(userRole.getRole_id());
				for (RoleScreenRights roleScreenRight : roleScreenRights) {
					role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
				}
			}
			for (User_role userRole : user_role) {
				if (userRole.getIsPrimaryRole() == 1) {
					List<RoleScreenRights> roleScreenRights = commonService
							.fetchScreenRightsByRoleId(userRole.getRole_id());
					for (RoleScreenRights roleScreenRight : roleScreenRights) {
						role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
					}
				}
			}
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails, advTableFields.getAdmin_application());
			return ResponseEntity.ok(new JwtResponse(token, null, 0, 0, role_screen));
		} else {
			AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE, appMessages.getUser_not_found());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private int authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return 1;
		} catch (DisabledException e) {
			logger.error(e.getMessage());
			// e.printStackTrace();
			// throw new Exception("USER_DISABLED", e);
			return 0;
		} catch (BadCredentialsException e) {
			logger.error(e.getMessage());
			// throw new Exception("INVALID_CREDENTIALS", e);
			return 0;
		} catch (AuthenticationException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	private AdvResponse messageResponse(long code, String message) {
		AdvResponseMessage responseMessage = new AdvResponseMessage();
		responseMessage.setResponseCode(code);
		responseMessage.setResponseDescription(message);
		AdvResponse response = new AdvResponse();
		response.setResponseMessage(responseMessage);
		return response;
	}

	@ApiOperation(value = "refresh token", authorizations = @Authorization(value = "Bearer"))
	@RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
	public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		final String requestTokenHeader = request.getHeader("Authorization");
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
		}
		Claims claims = jwtTokenUtil.getAllClaimsFromTokenAfterExpiration(jwtToken);
		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		expectedMap.put("auth_key", advTableFields.getCommon_application());
		String username = expectedMap.get("sub").toString();
		Party party = advisorService.fetchPartyForSignIn(username);
		long roleId = 0;
		List<User_role> user_role = null;
		if (party != null) {
			user_role = commonService.fetchUserRoleByUserId(party.getPartyId());
		}
		if (user_role.size() == 1) {
			roleId = user_role.get(0).getRole_id();
		} else {
			for (User_role userRole : user_role) {
				if (userRole.getIsPrimaryRole() == 1) {
					roleId = userRole.getRole_id();
				}
			}

		}
		String roleBasedId = party.getRoleBasedId();
		// Session
		Map<Integer, RoleScreenRights> role_screen = new HashMap<>();
		for (User_role userRole : user_role) {
			List<RoleScreenRights> roleScreenRights = commonService.fetchScreenRightsByRoleId(userRole.getRole_id());
			for (RoleScreenRights roleScreenRight : roleScreenRights) {
				role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
			}
		}
		for (User_role userRole : user_role) {
			if (userRole.getIsPrimaryRole() == 1) {
				List<RoleScreenRights> roleScreenRights = commonService
						.fetchScreenRightsByRoleId(userRole.getRole_id());
				for (RoleScreenRights roleScreenRight : roleScreenRights) {
					role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
				}
			}
		}
		String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, username);
		return ResponseEntity.ok(new JwtResponse(token, roleBasedId, party.getPartyId(), roleId, role_screen));
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(Claims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

	@RequestMapping(value = "/otpSignin", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtOtpRequest authenticationRequest,
			HttpServletRequest request) throws Exception {
		String phoneNumber = authenticationRequest.getPhoneNumber();
		Party party = advisorService.fetchPartyByPhoneNumberAndDeleteFlag(phoneNumber);
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
		if (party != null) {
			boolean isValid = advisorService.validateOtp(phoneNumber, authenticationRequest.getOtp());
			if (isValid) {
				logger.info("Fetching generated otp");
				GeneratedOtp generatedOtp = advisorService.fetchGeneratedOtp(phoneNumber,
						authenticationRequest.getOtp());
				Timestamp created = generatedOtp.getCreated();
				long minutes = diffOfTimeStampInMin(created);
				if (minutes < smsConstants.getOtp_validity()) {
					String role = advisorService.fetchRoleByRoleId(roleId);
					if (!role.equals(advTableFields.getRoleName_admin())) {
						if (role != null) {
							if (role.equals(advTableFields.getRoleName_nonIndividual())
									|| role.equals(advTableFields.getRoleName())) {
								Advisor adv = advisorService.fetchByAdvisorId(party.getRoleBasedId());
								if (adv.getIsVerified() == 0) {
									// return
									// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(appMessages.getAccount_not_verified());
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getCannot_login_not_verified());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							} else if (role.equals(advTableFields.getRoleName_inv())) {
								Investor inv = investorService.fetchByInvestorId(party.getRoleBasedId());
								if (inv.getIsVerified() == 0) {
									// return
									// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(appMessages.getAccount_not_verified());
									AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
											appMessages.getCannot_login_not_verified());
									return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
								}
							}
						}
						Map<Integer, RoleScreenRights> role_screen = new HashMap<>();
						for (User_role userRole : user_role) {
							List<RoleScreenRights> roleScreenRights = commonService
									.fetchScreenRightsByRoleId(userRole.getRole_id());
							for (RoleScreenRights roleScreenRight : roleScreenRights) {
								role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
							}
						}
						for (User_role userRole : user_role) {
							if (userRole.getIsPrimaryRole() == 1) {
								List<RoleScreenRights> roleScreenRights = commonService
										.fetchScreenRightsByRoleId(userRole.getRole_id());
								for (RoleScreenRights roleScreenRight : roleScreenRights) {
									role_screen.put(roleScreenRight.getScreen_id(), roleScreenRight);
								}
							}
						}
						final UserDetails userDetails = userDetailsService
								.loadUserByUsername(authenticationRequest.getPhoneNumber());
						final String token = jwtTokenUtil.generateToken(userDetails,
								advTableFields.getCommon_application());
						String roleBasedId = party.getRoleBasedId();
						// Advisor adv
						// =advisorService.fetchAdvisorByEmailId(authenticationRequest.getUsername());
						long partyId = advisorService.fetchPartyIdByRoleBasedId(roleBasedId);
						return ResponseEntity.ok(new JwtResponse(token, roleBasedId, partyId, roleId, role_screen));
					} else {
						AdvResponse response = messageResponse(InvestorConstants.ERROR_CODE,
								appMessages.getLogin_failed());
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
					}
				} else {
					logger.error("OTP expired");
					AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getOtp_expired());
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} else {
				logger.error("Wrong OTP");
				AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getOtp_not_verified());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			AdvResponse response = messageResponse(AdvisorConstants.ERROR_CODE, appMessages.getLogin_failed());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
}