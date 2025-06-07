package com.sowisetech.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.admin.model.FieldRights;
import com.sowisetech.admin.model.RoleAuth;
import com.sowisetech.admin.model.ScreenFieldRights;
import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.dao.AdvisorExtractor;

@Transactional
@Repository
public class AdminAuthDaoImpl implements AdminAuthDao {

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

	private static final Logger logger = LoggerFactory.getLogger(AdminAuthDaoImpl.class);

	@Override
	public User_role fetchUserRoleByUserRoleId(int user_role_id) {
		try {
			String sql = "SELECT `user_role_id`, `user_id`,`role_id`, `created_by`, `created_date`, `updated_by`, `updated_date` FROM `user_role` WHERE `user_role_id` = ?";
			RowMapper<User_role> rowMapper = new BeanPropertyRowMapper<User_role>(User_role.class);
			return jdbcTemplateAuth.queryForObject(sql, rowMapper, user_role_id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addScreenRights(ScreenFieldRights screenFieldRights) {
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sql = "INSERT INTO `role_screen_rights` (`user_role_id`,`screen_id`,`add_rights`,`edit_rights`,`view_rights`,`delete_rights`,`created_by`,`created_date`,`updated_by`,`updated_date`) values (?,?,?,?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplateAuth.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					statement.setLong(1, screenFieldRights.getUser_role_id());
					statement.setInt(2, screenFieldRights.getScreen_id());
					statement.setInt(3, screenFieldRights.getAdd_rights());
					statement.setInt(4, screenFieldRights.getEdit_rights());
					statement.setInt(5, screenFieldRights.getView_rights());
					statement.setInt(6, screenFieldRights.getDelete_rights());
					statement.setString(7, screenFieldRights.getCreated_by());
					statement.setTimestamp(8, timestamp);
					statement.setString(9, screenFieldRights.getUpdated_by());
					statement.setTimestamp(10, timestamp);
					return statement;
				}
			}, holder);
			int primaryKey = holder.getKey().intValue();
			return primaryKey;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addFieldRights(FieldRights fieldRights) {
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `role_field_rights` (`role_screen_rights_id`,`field_id`,`add_rights`,`edit_rights`,`view_rights`,`created_by`,`created_date`,`updated_by`,`updated_date`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplateAuth.update(sql, fieldRights.getRole_screen_rights_id(), fieldRights.getField_id(),
					fieldRights.getAdd_rights(), fieldRights.getEdit_rights(), fieldRights.getView_rights(),
					fieldRights.getCreated_by(), timestamp, fieldRights.getUpdated_by(), timestamp);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ScreenFieldRights> fetchScreenRightsByUserRoleId(long user_role_id) {
		try {
			String sql = "SELECT `role_screen_rights_id`,`user_role_id`, `screen_id`,`add_rights`, `edit_rights`,`view_rights`, `delete_rights`,`created_by`,`created_date`,`updated_by`,`updated_date` FROM `role_screen_rights` WHERE `user_role_id` = ?";
			RowMapper<ScreenFieldRights> rowMapper = new BeanPropertyRowMapper<ScreenFieldRights>(
					ScreenFieldRights.class);
			return jdbcTemplateAuth.query(sql, rowMapper, user_role_id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
		// try {
		// String sql = "SELECT
		// screen.`role_screen_rights_id`,screen.`user_role_id`,screen.`screen_id`,screen.`add_rights`,screen.`edit_rights`,screen.`view_rights`,screen.`delete_rights`,screen.`created_by`,screen.`created_date`,screen.`updated_by`,screen.`updated_date`,
		// field.`role_field_rights_id` role_field_rights_id,
		// field.role_screen_rights_id role_screen_rights_id,field.field_id field_id,
		// field.add_rights add_rights,field.edit_rights edit_rights,field.view_rights
		// view_rights,field.created_by created_by,field.created_date
		// created_date,field.updated_by updated_by,field.updated_date updated_date FROM
		// (SELECT * FROM role_screen_rights WHERE user_role_id =?) screen LEFT JOIN
		// role_field_rights field ON (screen.role_screen_rights_id =
		// field.role_screen_rights_id)";
		// return jdbcTemplateAuth.query(sql, new
		// RoleScreenRightsExtractor(),user_role_id);
		// } catch (DataAccessException e) {
		// logger.error(e.getMessage());
		// return null;
		// }
	}

	@Override
	public int deleteFieldRightsByRoleScreenRightsId(int role_screen_rights_id) {
		try {
			String sql = "DELETE FROM `role_field_rights` WHERE `role_screen_rights_id`=?";
			return jdbcTemplateAuth.update(sql, role_screen_rights_id);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int deleteScreenRightsByUserRoleId(int user_role_id) {
		try {
			String sql = "DELETE FROM `role_screen_rights` WHERE `user_role_id`=?";
			return jdbcTemplateAuth.update(sql, user_role_id);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addRole(RoleAuth role) {
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlInsert = "INSERT INTO `role` (`name`, `created_by`, `created_date`,`updated_by`,`updated_date`) values (?,?,?,?,?)";
			int result = jdbcTemplateAuth.update(sqlInsert, role.getName(), role.getCreated_by(), timestamp,
					role.getUpdated_by(), timestamp);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public RoleAuth fetchRoleByRoleId(int id) {
		try {
			String sql = "SELECT `id`, `name`, `created_by`, `created_date`, `updated_by`, `updated_date` FROM `role` WHERE `id` = ?";
			RowMapper<RoleAuth> rowMapper = new BeanPropertyRowMapper<RoleAuth>(RoleAuth.class);
			return jdbcTemplateAuth.queryForObject(sql, rowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyRole(int id, RoleAuth roleAuth) {
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `role` SET `name`=?, `updated_by`=?,`updated_date`=? WHERE `id`=?";
			int result = jdbcTemplateAuth.update(sql1, roleAuth.getName(), roleAuth.getUpdated_by(), timestamp, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeRole(int roleId) {
		try {
			String sql = "DELETE FROM `role` WHERE `id` = ? ";
			int roleAuth = jdbcTemplateAuth.update(sql, roleId);
			return roleAuth;

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addUserRole(User_role userRole) {
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlInsert = "INSERT INTO `user_role` (`user_id`, `role_id`, `created_by`, `created_date`,`updated_by`,`updated_date`) values (?,?,?,?,?,?)";
			int result = jdbcTemplateAuth.update(sqlInsert, userRole.getUser_id(), userRole.getRole_id(),
					userRole.getCreated_by(), timestamp, userRole.getUpdated_by(), timestamp);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyUserRole(int id, User_role role) {
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `user_role` SET `user_id`=?, `role_id`=?, `updated_by`=?,`updated_date`=? WHERE `user_role_id`=?";
			int result = jdbcTemplateAuth.update(sql1, role.getUser_id(), role.getRole_id(), role.getUpdated_by(),
					timestamp, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeUserRole(int roleId) {
		try {
			String sql = "DELETE FROM `user_role` WHERE `user_role_id` = ? ";
			int roleAuth = jdbcTemplateAuth.update(sql, roleId);
			return roleAuth;

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public User_role fetchUserRoleByUserIdAndRoleId(long user_id, int role_id) {
		try {
			String sql = "SELECT `user_role_id`, `user_id`,`role_id`, `created_by`, `created_date`, `updated_by`, `updated_date` FROM `user_role` WHERE `user_id` = ? AND `role_id` = ?";
			RowMapper<User_role> rowMapper = new BeanPropertyRowMapper<User_role>(User_role.class);
			return jdbcTemplateAuth.queryForObject(sql, rowMapper, user_id, role_id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchRoleIdByName(String name) {
		try {
			String sql = "SELECT `id` FROM `role` WHERE `name` = ?";
			return jdbcTemplateAuth.queryForObject(sql, Long.class, name);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addUser_role(long partyId, long roleId, String adminId, int isPrimaryRole) {
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		// System.out.println("In user Role");
		try {
			String sql = "INSERT INTO `user_role` (`user_id`,`role_id`,`created_by`,`created_date`,`updated_by`,`updated_date`,`isPrimaryRole`) values (?,?,?,?,?,?,?)";
			int result = jdbcTemplateAuth.update(sql, partyId, roleId, adminId, timestamp, adminId, timestamp,
					isPrimaryRole);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkUserRoleIsPresent(int user_role_id) {
		try {
			String sql = "SELECT count(*) FROM `user_role` WHERE `user_role_id` = ? ";
			return jdbcTemplateAuth.queryForObject(sql, Integer.class, user_role_id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkRoleIsPresent(int id) {
		try {
			String sql = "SELECT count(*) FROM `role` WHERE `id` = ? ";
			return jdbcTemplateAuth.queryForObject(sql, Integer.class, id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkUserRoleByUserIdAndRoleId(long user_id, int role_id) {
		try {
			String sql = "SELECT count(*) FROM `user_role` WHERE `user_id` = ? AND `role_id` = ?";
			return jdbcTemplateAuth.queryForObject(sql, Integer.class, user_id, role_id);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public User_role fetchUserRoleByUserRoleIdAndRoleId(int user_role_id, int
	// role_id) {
	// try {
	// String sql = "SELECT `user_role_id`, `user_id`,`role_id`, `created_by`,
	// `created_date`, `updated_by`, `updated_date` FROM `user_role` WHERE
	// `user_role_id` = ? AND `role_id` = ?";
	// RowMapper<User_role> rowMapper = new
	// BeanPropertyRowMapper<User_role>(User_role.class);
	// return jdbcTemplateAuth.queryForObject(sql, rowMapper, user_role_id,
	// role_id);
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }
}
