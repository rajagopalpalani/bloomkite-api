package com.sowisetech.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

import com.sowisetech.admin.model.User_role;
import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.common.model.RoleFieldRights;
import com.sowisetech.common.model.RoleScreenRights;

@Transactional
@Repository
public class CommonDaoImpl implements CommonDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	DataSource dataSource;

	@Autowired
	@Qualifier("authJdbcTemplate")
	private JdbcTemplate jdbcTemplateAuth;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void postConstruct() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private static final Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

	@Override
	public long addMailMessage(String to, String subject, String originalContent, String fromUser, int noOfAttempt,
			String encryptPass) {
		try {
			String sql = "INSERT INTO `mailmessage` (`to`,`subject`,`message`,`fromUser`,`noOfAttempt`) values (ENCODE(?,?),?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, to);
					statement.setString(2, encryptPass);
					statement.setString(3, subject);
					statement.setString(4, originalContent);
					statement.setString(5, fromUser);
					statement.setInt(6, noOfAttempt);
					return statement;
				}
			}, holder);
			long primaryKey = holder.getKey().longValue();
			return primaryKey;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateMailMessage_ifFailed(long messageId, long ifFailed, int noOfAttempt, String reason) {
		try {
			String sql = "UPDATE `mailmessage` SET `ifFailed` =?,`noOfAttempt`=?, `reason`=? WHERE `mailMessageId`=?";
			int result = jdbcTemplate.update(sql, ifFailed, noOfAttempt, reason, messageId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addActivationLink(String emailId, String url, String verifykey, String subject, String encryptPass) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			String sql = "INSERT INTO `activation_link` (`emailId`,`link`,`created`,`key`,`subject`) values (ENCODE(?,?),?,?,?,?)";
			int result = jdbcTemplate.update(sql, emailId, encryptPass, url, timestamp, verifykey, subject);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchLatestKeyByEmailIdAndSub(String emailId, String mailSub, String encryptPass) {
		try {
			String sql = "SELECT `key` FROM `activation_link` WHERE DECODE(`emailId`,?)= ? AND `subject` = ? ORDER BY `created` DESC LIMIT 1";
			String key = jdbcTemplate.queryForObject(sql, String.class, encryptPass, emailId, mailSub);
			return key;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
}
