package com.sowisetech.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.service.AdvisorService;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.common.model.RoleScreenRights;
import com.sowisetech.common.service.CommonService;

@Component
public class ScreenRightsCommon {

	@Autowired
	ScreenAuthIds screenAuthIds;
	@Autowired
	CommonService commonService;
	@Autowired
	AdminSignin adminSignin;
	@Autowired
	private AdvisorService advisorService;
	@Autowired
	private AdvTableFields advTableFields;

	public List<Integer> screenRights(int screenId, HttpServletRequest request, String method) {
		boolean result = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Map<Integer, RoleScreenRights> role_screenMap = new HashMap<Integer, RoleScreenRights>();
		if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
			int roleId = advisorService.fetchRoleIdByName(advTableFields.getRoleName_admin());
			List<RoleScreenRights> roleScreenRights = commonService.fetchScreenRightsByRoleId(roleId);
			for (RoleScreenRights roleScreenRight : roleScreenRights) {
				role_screenMap.put(roleScreenRight.getScreen_id(), roleScreenRight);
			}
		} else {
			Party party = advisorService.fetchPartyForSignIn(userDetails.getUsername());
			List<User_role> user_role = null;
			if (party != null) {
				user_role = commonService.fetchUserRoleByUserId(party.getPartyId());
			}
			for (User_role userRole : user_role) {
				List<RoleScreenRights> roleScreenRights = commonService
						.fetchScreenRightsByRoleId(userRole.getRole_id());
				for (RoleScreenRights roleScreenRight : roleScreenRights) {
					role_screenMap.put(roleScreenRight.getScreen_id(), roleScreenRight);
				}
			}
			for (User_role userRole : user_role) {
				if (userRole.getIsPrimaryRole() == 1) {
					List<RoleScreenRights> roleScreenRights = commonService
							.fetchScreenRightsByRoleId(userRole.getRole_id());
					for (RoleScreenRights roleScreenRight : roleScreenRights) {
						role_screenMap.put(roleScreenRight.getScreen_id(), roleScreenRight);
					}
				}
			}
		}
		if (role_screenMap != null) {
			RoleScreenRights roleScreenRights = role_screenMap.get(screenId);
			if (roleScreenRights != null) {
				if (method.equals("ADD")) {
					if (roleScreenRights.getAdd_rights() == 1) {
						result = true;
					}

				} else if (method.equals("EDIT")) {
					if (roleScreenRights.getEdit_rights() == 1) {
						result = true;
					}

				} else if (method.equals("VIEW")) {
					if (roleScreenRights.getView_rights() == 1) {
						result = true;
					}

				} else if (method.equals("DELETE")) {
					if (roleScreenRights.getDelete_rights() == 1) {
						result = true;
					}
				}
				if (result == true) {
					String screenCode = commonService.fetchScreenCodeByScreenId(screenId);
					List<Integer> screenIds = commonService.fetchScreenIdsByStartWithScreenCode(screenCode);
					List<Integer> roleScreenRightsId = new ArrayList<>();
					for (int screen_id : screenIds) {
						RoleScreenRights roleRight = role_screenMap.get(screen_id);
						if (roleRight != null) {
							roleScreenRightsId.add(roleRight.getRole_screen_rights_id());
						}
					}
					return roleScreenRightsId;
				}
			}
		}
		return null;
	}

	public boolean isAuthNeedForScreenId(int screenId) {
		String numbersArray = screenAuthIds.getScreen_id();
		List<Integer> convertedIdList = Stream.of(numbersArray.split(",")).map(String::trim).map(Integer::parseInt)
				.collect(Collectors.toList());
		if (convertedIdList.contains(screenId)) {
			return true;
		} else {
			return false;
		}
	}

}
