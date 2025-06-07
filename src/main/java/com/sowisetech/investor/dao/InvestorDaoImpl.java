package com.sowisetech.investor.dao;

import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.model.Party;
import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;

import ch.qos.logback.core.net.SyslogOutputStream;

@Transactional
@Repository
public class InvestorDaoImpl implements InvestorDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	DataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(InvestorDaoImpl.class);

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void postConstruct() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// @Override
	// public List<Investor> fetchInvestor(String deleteflag) {
	// try {
	// String sql = "SELECT * FROM `investor` where `delete_flag`=?";
	// RowMapper<Investor> rowMapper = new
	// BeanPropertyRowMapper<Investor>(Investor.class);
	// List<Investor> investors = jdbcTemplate.query(sql, rowMapper, deleteflag);
	// return investors;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }
	@Override
	public List<Investor> fetchInvestor(Pageable pageable, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT inv.`invId` invId,DECODE(inv.`fullName`,?) fullName,inv.`displayName` displayName,DECODE(inv.`dob`,?) dob,DECODE(inv.`emailId`,?) emailId,inv.`gender` gender,inv.`password` password,DECODE(inv.`userName`,?) userName,DECODE(inv.`phoneNumber`,?) phoneNumber,inv.`pincode` pincode,inv.`imagePath` imagePath,inv.`partyStatusId` partyStatusId,inv.`created` created,inv.`updated` updated,inv.`isVerified` isVerified,inv.`verifiedBy` verifiedBy,inv.`verified` verified,inv.`isMobileVerified` isMobileVerified,inv.`delete_flag` delete_flag,invInt.interestId COL_A,invInt.prodId COL_B,invInt.invId COL_INVID,invInt.scale COL_D,invInt.created COL_E,invInt.updated COL_F,invInt.delete_flag COL_DELETEFLAG, 'invInt' VALUE FROM (SELECT * FROM investor WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") inv LEFT JOIN invinterest invInt ON (inv.invId = invInt.invId)) AS investorList WHERE delete_flag=?";
			return jdbcTemplate.query(sql, new InvestorListExtractor(deleteflag) {
			}, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<InvInterest> fetchInvInterestByInvId(String invId, String deleteflag) {
		try {
			String sqlInvInterest = "SELECT `interestId`,`prodId`,`invId`,`scale`,`created`,`updated`,`delete_flag` FROM `invinterest` WHERE `invId`=? and `delete_flag`=?";
			RowMapper<InvInterest> interestMapper = new BeanPropertyRowMapper<InvInterest>(InvInterest.class);
			return jdbcTemplate.query(sqlInvInterest, interestMapper, invId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/* delete investor by id */
	@Override
	public int deleteInvestor(String invId, String deleteflag, String signedUserId) {
		// remove investor query //
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			
			// String sql = "UPDATE `investor` SET `delete_flag`=?,`updated`=? WHERE
			// `invId`=?";
			String sql = "UPDATE investor LEFT JOIN invinterest ON investor.invId = invinterest.invId LEFT JOIN party ON party.roleBasedId = investor.invId SET investor.delete_flag = ?, investor.updated=?,investor.updated_by=?, invinterest.delete_flag =?,invinterest.updated=?,invinterest.updated_by=?,  party.delete_flag=?,party.updated=?,party.updated_by=? WHERE investor.invId = ?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, deleteflag, timestamp,
					signedUserId, deleteflag, timestamp, signedUserId, invId);
			// System.out.println("result " + result);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int deleteInvestorInterestByInvId(String invId, String deleteflag) {
		// remove investor interest query //
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			
			String sqlDeleteInvInterest = "UPDATE `invinterest` SET `delete_flag`=?,`updated`=? WHERE `invId`=?";
			int result = jdbcTemplate.update(sqlDeleteInvInterest, deleteflag, timestamp, invId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int deletePartyByInvId(String invId, String deleteflag) {
		// party table delete query for given invId //
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			
			String sqlDeleteFromParty = "UPDATE `party` SET `delete_flag`=?,`updated`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sqlDeleteFromParty, deleteflag, timestamp, invId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	// /* Fetch PartyStatusId By Desc */
	// @Override
	// public long fetchPartyStatusIdByDesc(String desc) {
	// // fetch partyStatusId query //
	// try {
	// String sqlPartyStatus = "SELECT `id` FROM `partystatus` WHERE `desc`=?";
	// Long partyStatusId = jdbcTemplate.queryForObject(sqlPartyStatus, Long.class,
	// desc);
	// return partyStatusId;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	// /* Fetch RoleId By Name */
	// @Override
	// public long fetchRoleIdByName(String name) {
	// // fetch roleId query //
	// try {
	// String sqlRole = "SELECT `id` FROM `role` WHERE `name`= ?";
	// Long roleId = jdbcTemplate.queryForObject(sqlRole, Long.class, name);
	// return roleId;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	// // Add to Party Table
	// @Override
	// public int addParty(Investor investor, Long roleId, String deleteflag) {
	// try {
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	// String sqlForPartyInsert = "INSERT INTO `party`
	// (`partyStatusId`,`roleId`,`roleBasedId`,`delete_flag`,`created`,`updated`)
	// values (?,?,?,?, ?, ?)";
	// int result = jdbcTemplate.update(sqlForPartyInsert,
	// investor.getPartyStatusId(), roleId,
	// investor.getInvId(), deleteflag, timestamp, timestamp);
	// return result;
	// } catch (DataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	//
	// }

	// // Add to Investor Table
	// @Override
	// public int add(Investor investor, String deleteflag) {
	// try {
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	// String sql = "INSERT INTO `investor` (`invId`,`fullName`, `displayName`,
	// `dob`, `gender`, `emailId`, `password`, `phoneNumber`, `pincode`,
	// `partyStatusId`, `created`, `updated`,`delete_flag`) values (?,?,?,?,?, ?, ?,
	// ?, ?, ?, ?, ?, ?)";
	// int result = jdbcTemplate.update(sql, investor.getInvId(),
	// investor.getFullName(),
	// investor.getDisplayName(), investor.getDob(), investor.getGender(),
	// investor.getEmailId(),
	// investor.getPassword(), investor.getPhoneNumber(), investor.getPincode(),
	// investor.getPartyStatusId(), timestamp, timestamp, deleteflag);
	// return result;
	// } catch (DataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	//
	// }

	
	@Override
	public Investor fetchInvestorByInvId(String invId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT inv.`invId` invId,DECODE(inv.`fullName`,?) fullName,inv.`displayName` displayName,DECODE(inv.`dob`,?) dob,DECODE(inv.`emailId`,?) emailId,inv.`gender` gender,inv.`password` password,DECODE(inv.`userName`,?) userName,DECODE(inv.`phoneNumber`,?) phoneNumber,inv.`pincode` pincode,inv.`imagePath` imagePath,inv.`partyStatusId` partyStatusId,inv.`created` created,inv.`updated` updated, inv.`created_by` created_by,inv.`updated_by` updated_by, inv.`isVerified` isVerified,inv.`verifiedBy` verifiedBy,inv.`verified` verified,inv.`isMobileVerified` isMobileVerified,inv.`delete_flag` delete_flag,invInt.interestId COL_A,invInt.prodId COL_B,invInt.invId COL_INVID,invInt.scale COL_D,invInt.created COL_E,invInt.updated COL_F,  invInt.created_by COL_G,invInt.updated_by COL_H  ,invInt.delete_flag COL_DELETEFLAG, 'invInt' VALUE FROM investor inv LEFT JOIN invinterest invInt ON (inv.invId = invInt.invId)) AS investor WHERE invId=? AND delete_flag=?";
			return jdbcTemplate.query(sql, new InvestorExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, invId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	/* Modify Investor */
	@Override
	public int update(String invId, Investor investor, String encryptPass) {
		try {
			String sql1 = "UPDATE `investor` SET `fullName`=ENCODE(?,?), `displayName`=?, `dob`=ENCODE(?,?), `gender`=?, `pincode`=?, `imagePath`=?, `partyStatusId`=? ,`updated`=?,`updated_by`=? WHERE `invId`=?";
			int result = jdbcTemplate.update(sql1, investor.getFullName(), encryptPass, investor.getDisplayName(),
					investor.getDob(), encryptPass, investor.getGender(), investor.getPincode(), investor.getImagePath(),
					investor.getPartyStatusId(), investor.getUpdated(), investor.getUpdated_by(), invId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	/* fetch category by id */
	@Override
	public Category fetchCategoryById(long categoryId) {
		try {
			String sql = "SELECT * FROM `category` WHERE `categoryId`= ?";
			RowMapper<Category> rowMapper = new BeanPropertyRowMapper<Category>(Category.class);

			Category category = jdbcTemplate.queryForObject(sql, rowMapper, categoryId);
			return category;

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/* Find combination of invId and prodId */
	@Override
	public boolean findDuplicate(String invId, long categoryId) {
		try {
			String sql = "SELECT * FROM `invinterest` WHERE `prodId`=? and `invId` =?";
			RowMapper<InvInterest> rowMapper = new BeanPropertyRowMapper<InvInterest>(InvInterest.class);
			List<InvInterest> invInterests = jdbcTemplate.query(sql, rowMapper, categoryId, invId);
			if (invInterests.isEmpty() == true) {
				return false;
			} else {
				return true;
			}
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	// To add investor interest //
	@Override
	public int addInvestorInterest(String invId, InvInterest invInterest, String deleteflag) {
		try {
			String sql = "INSERT INTO `invinterest` (`prodId`,`invId`,`scale`,`delete_flag`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?, ?, ?,?,?)";
			int result = jdbcTemplate.update(sql, invInterest.getProdId(), invId, invInterest.getScale(),
					deleteflag, invInterest.getCreated(), invInterest.getUpdated(), invInterest.getCreated_by(),
					invInterest.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	/* Modify investor interest */
	@Override
	public int updateInvestorInterest(long interestId, InvInterest invInterests) {
		try {
			String sql1 = "UPDATE `invinterest` SET `prodId`=?, `scale`=?,`invId`=?,`updated`=?,`updated_by`=? WHERE `interestId`=?";
			int result = jdbcTemplate.update(sql1, invInterests.getProdId(), invInterests.getScale(),
					invInterests.getInvId(), invInterests.getUpdated(), invInterests.getUpdated_by(), interestId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	/* Fetch investor interest */
	@Override
	public InvInterest fetchInvInterest(long interestId, String deleteflag) {
		try {
			String sql = "SELECT * FROM `invinterest` WHERE `interestId` = ? AND `delete_flag`=?";
			RowMapper<InvInterest> rowMapper = new BeanPropertyRowMapper<InvInterest>(InvInterest.class);
			InvInterest invInterest = jdbcTemplate.queryForObject(sql, rowMapper, interestId, deleteflag);
			return invInterest;

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/* Delete investor interest */
	@Override
	public int deleteInvestorInterest(long interestId, String deleteflag, String signedUserId) {
		try {
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			
			String sql = "UPDATE `invinterest` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `interestId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, interestId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public String fetchInvestorSmartId() {
	// try {
	// String sql1 = "SELECT `id` FROM `investorsmartid` ORDER BY `s_no` DESC LIMIT
	// 1";
	// return jdbcTemplate.queryForObject(sql1, String.class);
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	// @Override
	// public int addInvSmartId(String newId) {
	// try {
	// String sqlInsert = "INSERT INTO `investorsmartid` (`id`) values (?)";
	// return jdbcTemplate.update(sqlInsert, newId);
	// } catch (DataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	// /* Fetch investor by email id */
	// @Override
	// public Investor fetchInvByEmailId(String emailId) {
	// try {
	// String sql = "SELECT * FROM `investor` WHERE `emailId` = ?";
	// RowMapper<Investor> rowMapper = new
	// BeanPropertyRowMapper<Investor>(Investor.class);
	// return jdbcTemplate.queryForObject(sql, rowMapper, emailId);
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// } catch (IncorrectResultSizeDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	/* To encrypt the plain text */
	@Override
	public String encrypt(String plainText) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
			String sql1 = "SELECT * FROM `secret_key`";
			String key = jdbcTemplate.queryForObject(sql1, String.class);
			cryptor.setPassword(key);
			cryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");

			String encryptedText = cryptor.encrypt(plainText);
			return encryptedText;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	/* Decrypt the encrypted text */
	@Override
	public String decrypt(String encryptedText) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
			String sql1 = "SELECT * FROM `secret_key`";
			String key = jdbcTemplate.queryForObject(sql1, String.class);
			cryptor.setPassword(key);
			cryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
			String plainText = cryptor.decrypt(encryptedText);
			// System.out.println(plainText);
			return plainText;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchPartyIdByRoleBasedId(String invId, String deleteflag) {
		try {
			String sqlRole = "SELECT `partyId` FROM `party` WHERE `roleBasedId`= ? AND `delete_flag` = ?";
			Long partyId = jdbcTemplate.queryForObject(sqlRole, Long.class, invId, deleteflag);
			return partyId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updatePersonalInfoInParty(Investor investor, String advId, String encryptPass) {
		try {
			String sql = "UPDATE `party` SET `emailId`=ENCODE(?,?),`phoneNumber`=ENCODE(?,?), `updated`=?,`updated_by`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sql, investor.getEmailId(), encryptPass, investor.getPhoneNumber(),
					encryptPass, investor.getUpdated(), investor.getUpdated_by(), advId);
			// System.out.println(result);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `delete_flag`=? AND (DECODE(`emailId`,?) = ? OR DECODE(`panNumber`,?)=? OR DECODE(`phoneNumber`,?)=? OR DECODE(`userName`,?)=?)";
			RowMapper<Party> partyMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					delete_flag, encryptPass, username, encryptPass, username, encryptPass, username, encryptPass,
					username);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchTotalInvestorList(String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `investor` WHERE delete_flag = ? ";
			int investor = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);
			return investor;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkInvestorIsPresent(String invId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `investor` WHERE `invId`=? AND `delete_flag`=?";
			int investor = jdbcTemplate.queryForObject(sql, Integer.class, invId, deleteflag);
			return investor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int CheckCategoryIsPresent(long categoryId) {
		try {
			String sql = "SELECT count(*) FROM `category` WHERE `categoryId`= ?";
			int category = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
			return category;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int CheckInvInterestIsPresent(long interestId,String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `invinterest` WHERE `interestId` = ? AND `delete_flag`=?";
			int invInt = jdbcTemplate.queryForObject(sql, Integer.class, interestId,deleteflag);
			return invInt;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

}
