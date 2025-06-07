package com.sowisetech.admin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.ScreenFieldRights;

public class RoleScreenRightsExtractor implements ResultSetExtractor<List<ScreenFieldRights>> {

	@Override
	public List<ScreenFieldRights> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, ScreenFieldRights> map = new HashMap<String, ScreenFieldRights>();
		while (rs.next()) {
			String advId = rs.getString("advId");
			ScreenFieldRights screenFieldRights = map.get(advId);
			if (screenFieldRights == null) {
				screenFieldRights = new ScreenFieldRights();
				screenFieldRights.setRole_screen_rights_id(rs.getInt("role_screen_rights_id"));
				screenFieldRights.setUser_role_id(rs.getInt("user_role_id"));
				screenFieldRights.setScreen_id(rs.getInt("screen_id"));
				screenFieldRights.setScreen_id(rs.getInt("screen_id"));
				screenFieldRights.setAdd_rights(rs.getInt("add_rights"));
				screenFieldRights.setEdit_rights(rs.getInt("edit_rights"));
				screenFieldRights.setView_rights(rs.getInt("view_rights"));
				screenFieldRights.setDelete_rights(rs.getInt("delete_rights"));
				screenFieldRights.setCreated_by(rs.getString("created_by"));
				screenFieldRights.setCreated_date(rs.getTimestamp("created_date"));
				screenFieldRights.setUpdated_by(rs.getString("updated_by"));
				screenFieldRights.setUpdated_date(rs.getTimestamp("updated_date"));
				map.put(advId, screenFieldRights);
			}
			List<FieldRights> fieldRightsList = screenFieldRights.getFieldRights();
			if (fieldRightsList == null) {
				fieldRightsList = new ArrayList<FieldRights>();
				screenFieldRights.setFieldRights(fieldRightsList);
			}
			FieldRights fieldRights = new FieldRights();
			fieldRights.setRole_field_rights_id(rs.getInt("role_field_rights_id"));
			fieldRights.setRole_screen_rights_id(rs.getInt("role_screen_rights_id"));
			fieldRights.setField_id(rs.getInt("field_id"));
			fieldRights.setAdd_rights(rs.getInt("add_rights"));
			fieldRights.setEdit_rights(rs.getInt("edit_rights"));
			fieldRights.setView_rights(rs.getInt("view_rights"));
			fieldRights.setCreated_by(rs.getString("created_by"));
			fieldRights.setCreated_date(rs.getTimestamp("created_date"));
			fieldRights.setUpdated_by(rs.getString("updated_by"));
			fieldRights.setUpdated_date(rs.getTimestamp("updated_date"));
			fieldRightsList.add(fieldRights);
		}
		return new ArrayList<ScreenFieldRights>(map.values());
	}
}
