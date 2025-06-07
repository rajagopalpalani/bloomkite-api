package com.sowisetech.advisor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sowisetech.advisor.security.JwtAuthenticationEntryPoint;
import com.sowisetech.advisor.security.JwtRequestFilter;
import com.sowisetech.advisor.security.WebHookFilter;
import com.sowisetech.advisor.util.CustomPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private UserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// To add cors filter
		httpSecurity.cors();
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests()
				.antMatchers("/fetch-riskProfilePlanning", "/fetch-financialPlanning", "/calculateInterestChange",
						"/calculateEmiChange", "/calculatePartialPayment", "/calculateEmiCapacity",
						"/calculatePriorities", "/calculateInsurance", "/calculateNetworth", "/calculateCashFlow",
						"/validateUniqueFields", "/webHookEventUpdate", "/fetch-all-voteType", "/fetchAllForumCategory",
						"/fetch-all-riskPortfolio", "/fetch-all-urgency", "/fetch-all-priorityItem",
						"/fetch-all-cashFlowItem", "/fetch-all-cashFlowItemType", "/fetch-all-account",
						"/fetch-all-accountType", "/fetch-all-userType", "/fetch-all-advisorType",
						"/fetch-all-workFlowStatus", "/fetch-all-followerStatus", "/fetch-all-articleStatus",
						"/fetch-all-ForumStatus", "/fetch-all-ForumSubCategory", "/fetch-all-riskQuestionaire",
						"/fetch-all-ForumCategory", "/fetch-all-CategoryType", "/fetch-all-Category",
						"/fetch-all-productServBrand", "/fetch-all-remuneration", "/fetch-all-license",
						"/fetch-all-brand", "/fetch-all-service", "/fetch-all-partystatus", "/fetch-all-role",
						"/fetchAdvisorByUserNameWithOutToken", "/fetch-all-product", "/fetch-all-stateCityPincode",
						"/exploreProduct", "/adminSignup", "/exploreAdvisorByProductWithOutToken",
						"/exploreAdvisorWithOutToken", "/sendOtp", "/verifyOtp", "/adminSignin", "/resendMail",
						"/verify/signup", "/forgetPassword", "/resetPassword", "/ecv", "/admin-ecv", "/forum-ecv",
						"/investor-ecv", "/calc-ecv", "/signin", "/signup", "/swagger-ui.html",
						"/swagger-resources/configuration/security", "/swagger-resources/configuration/ui",
						"/swagger-resources", "/webjars/**", "/v2/api-docs", "/calculateGoal", "/IP-FutureValue",
						"/IP-TargetValue", "/IP-RateFinder", "/IP-TenureFinder", "/calculateEmi", "/otpSignin",
						"/sendOtp", "/calculateRiskProfile", "/fetchPublishTeam", "/testMailTemplate",
						"/fetchArticlePostByArticleIdWithoutToken", "/fetchAllRecentArticleWithoutToken",
						"/fetchRecentApprovedArticleWithoutToken", "/fetchAllArticleInApprovedOrderWihtoutToken",
						"/fetchAllLookup", "/exploreAdvisorByBrandWithoutToken", "/exploreAdvisorByProductWithOutToken",
						"/exploreAdvisorByBrand", "/fetchBrandsComment")
				.permitAll().
				// all other requests need to be authenticated
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public FilterRegistrationBean<WebHookFilter> customFilter() {
		FilterRegistrationBean<WebHookFilter> registrationBean = new FilterRegistrationBean<WebHookFilter>();

		registrationBean.setFilter(new WebHookFilter());
		registrationBean.addUrlPatterns("/webHookEventUpdate");

		return registrationBean;
	}
}