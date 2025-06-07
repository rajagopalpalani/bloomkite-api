package com.sowisetech.advisor.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sowisetech.advisor.util.AdvTableFields;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AdvTableFields advTableFields;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
				String keyValue = (String) claims.get("auth_key");
				String requestURL = request.getRequestURL().toString();
				if (keyValue.equals(advTableFields.getCommon_application())) {
					if (!requestURL.contains("uploadFile") && !requestURL.contains("/uploadPdfFile")) {
						request = new RequestWrapper((HttpServletRequest) request);
						String body = ((RequestWrapper) request).getBody();
						if (!"".equals(body)) {
							try {
								JSONObject jsonObject = new JSONObject(body.toString());
								if (jsonObject.isNull("screenId")) {
									jsonObject.put("screenId", 99999);
								}
								((RequestWrapper) request).resetInputStream(jsonObject.toString().getBytes());
							} catch (Exception e) {
								logger.info(e.getMessage());
							}
						} else {
							JSONObject jsonObject = new JSONObject();
							try {
								jsonObject.put("screenId", 99999);
							} catch (JSONException e) {
								logger.warn(e.getMessage());
							}
							((RequestWrapper) request).resetInputStream(jsonObject.toString().getBytes());
						}
					}
				}
			} catch (IllegalArgumentException e) {
				logger.warn("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired");
				String isRefreshToken = request.getHeader("isRefreshToken");
				String requestURL = request.getRequestURL().toString();
				// allow for Refresh Token creation if following conditions are true.
				if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshToken")) {
					allowForRefreshToken(e, request);
				} else
					request.setAttribute("exception", e);
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	public void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}

}