package com.sowisetech.advisor.dao;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

@Transactional
@Repository
public class AuthDaoImpl implements AuthDao {

	@Autowired
	@Qualifier("authJdbcTemplate")
	private JdbcTemplate jdbcTemplateAuth;

	@Autowired
	DataSource authDataSource;

	@Autowired
	public void setDataSource(@Qualifier("authDataSource") DataSource dataSource) {
		this.authDataSource = dataSource;
	}

	@PostConstruct
	public void postConstruct() {
		jdbcTemplateAuth = new JdbcTemplate(authDataSource);
	}

	private static final Logger logger = LoggerFactory.getLogger(AuthDaoImpl.class);

	@Override
	public int addUser_role(long partyId, long roleId, String signedUserId, int isPrimaryRole) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			String sql = "INSERT INTO `user_role` (`user_id`,`role_id`,`created_by`,`created_date`,`updated_by`,`updated_date`,`isPrimaryRole`) values (?,?,?,?,?,?,?)";
			int result = jdbcTemplateAuth.update(sql, partyId, roleId, signedUserId, timestamp, signedUserId, timestamp,
					isPrimaryRole);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<RoleScreenRights> fetchScreenRightsByRoleId(long user_role_id) {
		try {
			String sqlRole = "SELECT `role_screen_rights_id`,`user_role_id`,`screen_id`,`add_rights`,`edit_rights`,`view_rights`,`delete_rights` FROM `role_screen_rights` WHERE `user_role_id`= ?";
			RowMapper<RoleScreenRights> rowMapper = new BeanPropertyRowMapper<RoleScreenRights>(RoleScreenRights.class);
			List<RoleScreenRights> roleScreenRights = jdbcTemplateAuth.query(sqlRole, rowMapper, user_role_id);
			return roleScreenRights;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<RoleFieldRights> fetchFieldRights(List<Integer> roleScreenId) {
		try {
			String sql = "SELECT `role_field_rights_id`,`role_screen_rights_id`,`field_id`,`add_rights`,`edit_rights`,`view_rights`,`created_by`,`created_date`,`updated_by`,`updated_date` FROM `role_field_rights` WHERE `role_screen_rights_id` IN (%1$s)";
			String inSql = roleScreenId.stream().map(String::valueOf).collect(Collectors.joining(","));
			String sqlInv = String.format(sql, inSql);
			RowMapper<RoleFieldRights> rowMapper = new BeanPropertyRowMapper<RoleFieldRights>(RoleFieldRights.class);
			List<RoleFieldRights> roleScreenRights = jdbcTemplateAuth.query(sqlInv, rowMapper);
			return roleScreenRights;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchUserRoleIdByPartyId(long partyId, int roleId) {
		try {
			String sqlRole = "SELECT `user_role_id` FROM `user_role` WHERE `user_id`= ? AND `role_id`=?";
			int user_role_id = jdbcTemplateAuth.queryForObject(sqlRole, Integer.class, partyId, roleId);
			return user_role_id;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<User_role> fetchUserRoleByUserId(long partyId) {
		try {
			String sqlRole = "SELECT * FROM `user_role` WHERE `user_id` = ?";
			RowMapper<User_role> rowMapper = new BeanPropertyRowMapper<User_role>(User_role.class);
			List<User_role> userRole = jdbcTemplateAuth.query(sqlRole, rowMapper, partyId);
			return userRole;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<RoleAuth> fetchRoleList() {
		try {
			String sql = "SELECT * FROM `role`";
			RowMapper<RoleAuth> rowMapper = new BeanPropertyRowMapper<RoleAuth>(RoleAuth.class);
			List<RoleAuth> roleList = jdbcTemplateAuth.query(sql, rowMapper);
			return roleList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchRoleIdByName(String name) {
		try {
			String sqlRole = "SELECT `id` FROM `role` WHERE `name`= ?";
			int roleId = jdbcTemplateAuth.queryForObject(sqlRole, Integer.class, name);
			return roleId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchRoleByRoleId(long roleId) {
		try {
			String sqlRole = "SELECT `name` FROM `role` WHERE `id`= ?";
			String role = jdbcTemplateAuth.queryForObject(sqlRole, String.class, roleId);
			return role;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchScreenCodeByScreenId(int screenId) {
		try {
			String sqlRole = "SELECT `screen_code` FROM `screen_reference` WHERE `screen_id`= ?";
			String role = jdbcTemplateAuth.queryForObject(sqlRole, String.class, screenId);
			return role;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Integer> fetchScreenIdsByStartWithScreenCode(String screenCode) {
		try {
			String sqlRole = "SELECT `screen_id` FROM `screen_reference` WHERE `screen_code` LIKE '" + screenCode
					+ "%'";
			List<Integer> screenId = jdbcTemplateAuth.queryForList(sqlRole, Integer.class);
			return screenId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public int changeToCorporateRoleId(int roleIdCorp, long partyId, String signedUserId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql1 = "UPDATE `user_role` SET `role_id`=? ,`updated_by`=?,`updated_date`=? WHERE `user_id`=?";
			return jdbcTemplateAuth.update(sql1, roleIdCorp,signedUserId,timestamp,partyId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}
}
