package com.sowisetech.advisor.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sowisetech.admin.model.Admin;
import com.sowisetech.admin.service.AdminService;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.common.util.AdminSignin;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	AdvisorService advisorService;
	@Autowired
	AdminService adminService;
	@Autowired
	private AdminSignin adminSignin;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// Advisor adv=advisorService.fetchAdvisorByEmailId(username);
		Party party = advisorService.fetchPartyForSignIn(username);
		String emailid = adminSignin.getEmailid();
		String password = adminSignin.getPassword();
		Admin admin = adminService.fetchAdminByEmailId(username);
		if (party != null) {
			if ((party.getEmailId()).equals(username)) {
				return new User(party.getEmailId(), party.getPassword(), new ArrayList<>());
			} else if ((party.getUserName()).equals(username)) {
				return new User(party.getUserName(), party.getPassword(), new ArrayList<>());
			} else if ((party.getPhoneNumber()).equals(username)) {
				return new User(party.getPhoneNumber(), party.getPassword(), new ArrayList<>());
			} else if ((party.getPanNumber()).equals(username)) {
				return new User(party.getPanNumber(), party.getPassword(), new ArrayList<>());
			} else {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}
		} else if (admin != null) {
			return new User(admin.getEmailId(), admin.getPassword(), new ArrayList<>());
		} else {
			if (emailid.equals(username)) {
				return new User(emailid, password, new ArrayList<>());
			} else {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}
		}
	}
}
