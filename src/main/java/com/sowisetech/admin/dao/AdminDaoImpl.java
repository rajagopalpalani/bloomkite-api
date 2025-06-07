package com.sowisetech.admin.dao;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.admin.model.Account;
import com.sowisetech.admin.model.Acctype;
import com.sowisetech.admin.model.AdmFollower;
import com.sowisetech.admin.model.AdmPriority;
import com.sowisetech.admin.model.AdmRiskPortfolio;
import com.sowisetech.admin.model.Admin;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.ArticleStatus;
import com.sowisetech.admin.model.Brand;
import com.sowisetech.admin.model.CashFlowItem;
import com.sowisetech.admin.model.CashFlowItemType;
import com.sowisetech.admin.model.City;
import com.sowisetech.admin.model.InsuranceItem;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Advtypes;
import com.sowisetech.admin.model.License;
import com.sowisetech.admin.model.Party;
import com.sowisetech.admin.model.Product;
import com.sowisetech.admin.model.Remuneration;
import com.sowisetech.admin.model.Service;
import com.sowisetech.admin.model.State;
import com.sowisetech.admin.model.Urgency;
import com.sowisetech.admin.model.Votetype;
import com.sowisetech.admin.model.UserType;
import com.sowisetech.admin.model.View;
import com.sowisetech.admin.model.Workflowstatus;

@Transactional
@Repository
public class AdminDaoImpl implements AdminDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@PostConstruct
	public void postConstruct() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private static final Logger logger = LoggerFactory.getLogger(AdminDaoImpl.class);

	@Override
	public List<Admin> fetchAdminList(String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `adminId`,DECODE(`emailId`,?) emailId,DECODE(`name`,?) name,`password`,`partyStatusId`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag` FROM `admin` WHERE `delete_flag`=?";
			RowMapper<Admin> rowMapper = new BeanPropertyRowMapper<Admin>(Admin.class);
			List<Admin> admin = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, delete_flag);
			return admin;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Admin fetchAdminByEmailId(String emailId, String encryptPass) {
		try {
			String sql = "SELECT `adminId`,DECODE(`emailId`,?) emailId,DECODE(`name`,?) name,`password`,`partyStatusId`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag` FROM `admin` WHERE DECODE(`emailId`,?) = ?";
			RowMapper<Admin> rowMapper = new BeanPropertyRowMapper<Admin>(Admin.class);
			Admin admin = jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, emailId);
			return admin;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Admin fetchByAdminId(String adminId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `adminId`,DECODE(`name`,?) name, DECODE(`emailId`,?) emailId, `partyStatusId`,`created`,`updated`,`created_by`,`updated_by` FROM `admin` WHERE `adminId` = ? AND `delete_flag`=?";
			RowMapper<Admin> rowMapper = new BeanPropertyRowMapper<Admin>(Admin.class);
			Admin admin = jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, adminId, delete_flag);
			return admin;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addAdmin(Admin admin, String encryptPass) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sql = "INSERT INTO `admin` (`adminId`,`name`,`password` ,`emailId`,`partyStatusId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?, ENCODE(?,?),?,ENCODE(?,?),?, ?, ?, ?,?,?)";
			int result = jdbcTemplate.update(sql, admin.getAdminId(), admin.getName(), encryptPass, admin.getPassword(),
					admin.getEmailId(), encryptPass, admin.getPartyStatusId(), timestamp, timestamp,
					admin.getDelete_flag(), admin.getCreated_by(), admin.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPartyForAdmin(Admin admin, String encryptPass) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sqlForPartyInsert = "INSERT INTO `party` (`emailId`,`password`,`partyStatusId`,`roleBasedId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (ENCODE(?,?),?,?,?,?, ?, ?,?,?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, admin.getEmailId(), encryptPass, admin.getPassword(),
					admin.getPartyStatusId(), admin.getAdminId(), timestamp, timestamp, admin.getDelete_flag(),
					admin.getCreated_by(), admin.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchEncryptionSecretKey() {
		try {
			String sql1 = "SELECT `key` FROM `secret_key`";
			String key = jdbcTemplate.queryForObject(sql1, String.class);
			return key;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyAdmin(String adminId, Admin admin, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `admin` SET `name`=ENCODE(?,?),`partyStatusId`=? ,`updated` = ?,`updated_by`=? WHERE `adminId`=?";
			int result = jdbcTemplate.update(sql1, admin.getName(), encryptPass, admin.getPartyStatusId(), timestamp,
					admin.getUpdated_by(), adminId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdmin(String adminId, String delete_flag_y, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `admin` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `adminId`=?";
			int result = jdbcTemplate.update(sql1, delete_flag_y, timestamp, signedUserId, adminId);

			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeParty(String adminId, String delete_flag_y, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlDeleteFromParty = "UPDATE `party` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sqlDeleteFromParty, delete_flag_y, timestamp, signedUserId, adminId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchAdminSmartId() {
		try {
			String sql1 = "SELECT `id` FROM `adminsmartid` ORDER BY `s_no` DESC LIMIT 1";
			String id = jdbcTemplate.queryForObject(sql1, String.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addAdmSmartId(String newId) {
		try {
			String sqlInsert = "INSERT INTO `adminsmartid` (`id`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public long fetchPartyIdByRoleBasedId(String roleBasedId, String delete_flag_N) {
		try {
			String sqlRole = "SELECT `partyId` FROM `party` WHERE `roleBasedId`= ? AND `delete_flag` = ?";
			long partyId = jdbcTemplate.queryForObject(sqlRole, Long.class, roleBasedId, delete_flag_N);
			return partyId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Party fetchPartyByEmailId(String emailId, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE DECODE(`emailId`,?) = ? ";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, emailId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchPartyStatusIdByDesc(String desc) {
		// fetch partyStatusId query //
		try {
			String sqlPartyStatus = "SELECT `id` FROM `partystatus` WHERE `desc`=?";
			Long partyStatusId = jdbcTemplate.queryForObject(sqlPartyStatus, Long.class, desc);
			return partyStatusId;
		} catch (EmptyResultDataAccessException e) {
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
	public int checkPartyIsPresent(String emailId, String encryptPass) {
		try {
			String sql = "SELECT count(*) FROM `party` WHERE DECODE(`emailId`,?) = ? ";
			return jdbcTemplate.queryForObject(sql, Integer.class, encryptPass, emailId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int checkAdminIsPresent(String adminId) {
		try {
			String sql = "SELECT count(*) FROM `admin` WHERE `adminId` = ? ";
			return jdbcTemplate.queryForObject(sql, Integer.class, adminId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int advisorTypes(Advtypes advtypes) {
		try {
			String sql = "INSERT INTO `advtypes` (`advtype`) values (?)";
			int result = jdbcTemplate.update(sql, advtypes.getAdvtype());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvisorTypes(int id) {
		try {
			String sql = "DELETE  FROM `advTypes` WHERE `id` = ? ";
			int result = jdbcTemplate.update(sql, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyAdvisorTypes(Advtypes advtypes) {
		try {
			String sql = "UPDATE `advtypes` SET `advtype`=? WHERE `id`=?";
			int result = jdbcTemplate.update(sql, advtypes.getAdvtype(), advtypes.getId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int followerStatus(AdmFollower admFollower) {
		try {
			String sql = "INSERT INTO `followerstatus` (`followerStatusId`,`status`) values (?,?)";
			int result = jdbcTemplate.update(sql, admFollower.getFollowerStatusId(), admFollower.getStatus());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyFollowerStatus(AdmFollower admFollower) {
		try {
			String sql = "UPDATE `followerstatus` SET `status`=? WHERE `followerStatusId`=?";
			int result = jdbcTemplate.update(sql, admFollower.getStatus(), admFollower.getFollowerStatusId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int saveAddAcctype(Acctype acctype) {
		try {
			String sql = "INSERT INTO `acctype` (`AccTypeId`,`AccType`) values (?,?)";
			int result = jdbcTemplate.update(sql, acctype.getAccTypeId(), acctype.getAccType());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addProducts(Product product) {
		try {
			String sql = "INSERT INTO `products` (`product`) VALUES (?)";
			int result = jdbcTemplate.update(sql, product.getProduct());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int RemoveAcctype(int AccTypeId) {
		try {
			String sql = "DELETE FROM `acctype` WHERE `AccTypeId` =? ";
			int result = jdbcTemplate.update(sql, AccTypeId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeProducts(int prodId) {
		try {
			String sql = "DELETE FROM `products` WHERE `prodId` = ? ";
			int result = jdbcTemplate.update(sql, prodId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyAcctype(Acctype acctype) {
		try {
			String sql = "UPDATE `acctype` SET `AccType` =? WHERE `AccTypeId` = ? ";
			int result = jdbcTemplate.update(sql, acctype.getAccType(), acctype.getAccTypeId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyProduct(Product product) {
		try {
			String sql = "UPDATE `products` SET `product`=?  WHERE `prodId`=?";
			int result = jdbcTemplate.update(sql, product.getProduct(), product.getProdId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addArticleStatus(ArticleStatus articleStatus) {
		try {
			String sql = "INSERT INTO `articlestatus` (`desc`) VALUES (?)";
			int result = jdbcTemplate.update(sql, articleStatus.getDesc());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addState(State state) {
		try {
			String sql = "INSERT INTO `state` (`state`) values (?)";
			int result = jdbcTemplate.update(sql, state.getState());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyArticleStatus(ArticleStatus articleStatus) {
		try {
			String sql = "UPDATE `articlestatus` SET `desc`=?  WHERE `id`=?";
			int result = jdbcTemplate.update(sql, articleStatus.getDesc(), articleStatus.getId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeState(int stateId) {
		try {
			String sql = "DELETE FROM `state` WHERE `stateId` =? ";
			int result = jdbcTemplate.update(sql, stateId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeArticleStatus(int id) {
		try {
			String sql = "DELETE FROM `articlestatus` WHERE `id` = ? ";
			int result = jdbcTemplate.update(sql, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyState(State state) {
		try {
			String sql = "UPDATE `state` SET `state` =? WHERE `stateId` = ? ";
			int result = jdbcTemplate.update(sql, state.getState(), state.getStateId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addCashFlowItemType(CashFlowItemType cashFlowItemType) {
		try {
			String sql = "INSERT INTO `cashflowitemtype` (`cashFlowItemType`) VALUES (?)";
			int result = jdbcTemplate.update(sql, cashFlowItemType.getCashFlowItemType());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyCashFlowItemType(CashFlowItemType cashFlowItemType) {
		try {
			String sql = "UPDATE `cashflowitemtype` SET `cashFlowItemType`=?  WHERE `cashFlowItemTypeId`=?";
			int result = jdbcTemplate.update(sql, cashFlowItemType.getCashFlowItemType(),
					cashFlowItemType.getCashFlowItemTypeId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeCashFlowItemType(int cashFlowItemTypeId) {
		try {
			String sql = "DELETE FROM `cashflowitemtype` WHERE `cashFlowItemTypeId` = ? ";
			int result = jdbcTemplate.update(sql, cashFlowItemTypeId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addRemuneration(Remuneration remuneration) {
		try {
			String sql = "INSERT INTO `remuneration` (`remuneration`) VALUES (?)";
			int result = jdbcTemplate.update(sql, remuneration.getRemuneration());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addCashFlowItem(CashFlowItem cashFlowItem) {
		try {
			String sql = "INSERT INTO `cashflowitem` (`cashFlowItem`, `cashFlowItemTypeId`) VALUES (?,?)";
			int result = jdbcTemplate.update(sql, cashFlowItem.getCashFlowItem(), cashFlowItem.getCashFlowItemTypeId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeRemuneration(int remId) {
		try {
			String sql = "DELETE FROM `remuneration` WHERE `remId` =? ";
			int result = jdbcTemplate.update(sql, remId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyCashFlowItem(CashFlowItem cashFlowItem) {
		try {
			String sql = "UPDATE `cashflowitem` SET `cashFlowItem`=?, `cashFlowItemTypeId` =?  WHERE `cashFlowItemId`=?";
			int result = jdbcTemplate.update(sql, cashFlowItem.getCashFlowItem(), cashFlowItem.getCashFlowItemTypeId(),
					cashFlowItem.getCashFlowItemId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeFollowerStatus(int followerStatusId) {
		try {
			String sql = "DELETE  FROM `followerstatus` WHERE `followerStatusId` = ? ";
			int result = jdbcTemplate.update(sql, followerStatusId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeCashFlowItem(int cashFlowItemId) {
		try {
			String sql = "DELETE FROM `cashflowitem` WHERE `cashFlowItemId` = ? ";
			int result = jdbcTemplate.update(sql, cashFlowItemId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyRemuneration(Remuneration remuneration) {
		try {
			String sql = "UPDATE `remuneration` SET `remuneration` =? WHERE `remId` = ? ";
			int result = jdbcTemplate.update(sql, remuneration.getRemuneration(), remuneration.getRemId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addCity(City city) {
		try {
			String sql = "INSERT INTO `city` (`stateId`, `city`, `pincode`, `district`) VALUES (?,?,?,?)";
			int result = jdbcTemplate.update(sql, city.getStateId(), city.getCity(), city.getPincode(),
					city.getDistrict());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyCity(City city) {
		try {
			String sql = "UPDATE `city` SET `stateId`=?, `city` =?, `pincode` =?, `district` =?  WHERE `cityId`=?";
			int result = jdbcTemplate.update(sql, city.getStateId(), city.getCity(), city.getPincode(),
					city.getDistrict(), city.getCityId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeCity(int cityId) {
		try {
			String sql = "DELETE FROM `city` WHERE `cityId` = ? ";
			int result = jdbcTemplate.update(sql, cityId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int priorityItem(AdmPriority admPriority) {
		try {
			String sql = "INSERT INTO `priorityitem` (`priorityItem`) values (?)";
			int result = jdbcTemplate.update(sql, admPriority.getPriorityItem());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyPriorityItem(AdmPriority admPriority) {
		try {
			String sql = "UPDATE `priorityitem` SET `priorityItem`=? WHERE `priorityItemId`=?";
			int result = jdbcTemplate.update(sql, admPriority.getPriorityItem(), admPriority.getPriorityItemId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePriorityItem(int priorityItemId) {
		try {
			String sql = "DELETE  FROM `priorityitem` WHERE `priorityItemId` = ? ";
			int result = jdbcTemplate.update(sql, priorityItemId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addWorkflowstatus(Workflowstatus workflowstatus) {
		try {
			String sql = "INSERT INTO `workflowstatus` (`status`) VALUES (?)";
			int result = jdbcTemplate.update(sql, workflowstatus.getStatus());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int riskPortfolio(AdmRiskPortfolio admRiskPortfolio) {
		try {
			String sql = "INSERT INTO `riskportfolio` (`riskPortfolioId`,`points`,`behaviour`,`equity`,`debt`,`cash`) values (?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, admRiskPortfolio.getRiskPortfolioId(), admRiskPortfolio.getPoints(),
					admRiskPortfolio.getBehaviour(), admRiskPortfolio.getEquity(), admRiskPortfolio.getDebt(),
					admRiskPortfolio.getCash());

			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyWorkflowstatus(Workflowstatus workflowstatus) {
		try {
			String sql = "UPDATE `workflowstatus` SET `status` =? WHERE `workflowId` = ? ";
			int result = jdbcTemplate.update(sql, workflowstatus.getStatus(), workflowstatus.getWorkflowId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int modifyRiskPortfolio(AdmRiskPortfolio admRiskPortfolio) {
		try {
			String sql = "UPDATE `riskportfolio` SET `points`=?, `behaviour`=?, `equity`=?, `debt`=?, `cash`=? WHERE `riskPortfolioId`=?";
			int result = jdbcTemplate.update(sql, admRiskPortfolio.getPoints(), admRiskPortfolio.getBehaviour(),
					admRiskPortfolio.getEquity(), admRiskPortfolio.getDebt(), admRiskPortfolio.getCash(),
					admRiskPortfolio.getRiskPortfolioId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override

	public int removeWorkFlowStatus(int workflowId) {
		try {
			String sql = "DELETE  FROM `workflowstatus` WHERE `workflowId` = ? ";
			int result = jdbcTemplate.update(sql, workflowId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int removeRiskPortfolio(int riskPortfolioId) {
		try {
			String sql = "DELETE  FROM `riskportfolio` WHERE `riskPortfolioId` = ? ";
			int result = jdbcTemplate.update(sql, riskPortfolioId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addService(Service service) {
		try {

			String sql = "INSERT INTO `service` (`service`,`prodId`) VALUES (?,?)";
			int result = jdbcTemplate.update(sql, service.getService(), service.getProdId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int license(License license) {
		try {
			String sql = "INSERT INTO `license` (`license`,`issuedBy`,`prodId`) values (?,?,?)";
			int result = jdbcTemplate.update(sql, license.getLicense(), license.getIssuedBy(), license.getProdId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyService(Service service) {
		try {
			String sql = "UPDATE `service` SET `service` =?,`prodId`=? WHERE `serviceId` = ? ";
			int result = jdbcTemplate.update(sql, service.getService(), service.getProdId(), service.getServiceId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyLicense(License license) {
		try {
			String sql = "UPDATE `license` SET `license`=?, `issuedBy`=?, `prodId`=? WHERE `licId`=?";
			int result = jdbcTemplate.update(sql, license.getLicense(), license.getIssuedBy(), license.getProdId(),
					license.getLicId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeService(int serviceId) {
		try {
			String sql = "DELETE  FROM `service` WHERE `serviceId` = ? ";
			int result = jdbcTemplate.update(sql, serviceId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeLicense(int licId) {
		try {
			String sql = "DELETE  FROM `license` WHERE `licId` = ? ";
			int result = jdbcTemplate.update(sql, licId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addVotetype(Votetype votetype) {
		try {
			String sql = "INSERT INTO `votetype` (`desc`) VALUES (?)";
			int result = jdbcTemplate.update(sql, votetype.getDesc());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addInsuranceItem(InsuranceItem insuranceItem) {
		try {
			String sql = "INSERT INTO `insuranceitem` (`insuranceItem`,`value`) values (?,?)";
			int result = jdbcTemplate.update(sql, insuranceItem.getInsuranceItem(), insuranceItem.getValue());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override

	public int urgency(Urgency urgency) {
		try {
			String sql = "INSERT INTO `urgency` (`value`) values (?)";
			int result = jdbcTemplate.update(sql, urgency.getValue());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int modifyVotetype(Votetype votetype) {
		try {
			String sql = "UPDATE `votetype` SET `desc` =? WHERE `id` = ? ";

			int result = jdbcTemplate.update(sql, votetype.getDesc(), votetype.getId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int modifyInsuranceItem(InsuranceItem insuranceItem) {
		try {
			String sql = "UPDATE `insuranceitem` SET `insuranceItem`=?, `value`=? WHERE `insuranceItemId`=?";
			int result = jdbcTemplate.update(sql, insuranceItem.getInsuranceItem(), insuranceItem.getValue(),
					insuranceItem.getInsuranceItemId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeVotetype(int id) {
		try {
			String sql = "DELETE  FROM `votetype` WHERE `id` = ? ";
			int result = jdbcTemplate.update(sql, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override

	public int modifyUrgency(Urgency urgency) {
		try {
			String sql = "UPDATE `urgency` SET `value`=? WHERE `urgencyId`=?";
			int result = jdbcTemplate.update(sql, urgency.getValue(), urgency.getUrgencyId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int removeInsuranceItem(int insuranceItemId) {
		try {
			String sql = "DELETE  FROM `insuranceitem` WHERE `insuranceItemId` = ? ";
			int result = jdbcTemplate.update(sql, insuranceItemId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeUrgency(int urgencyId) {
		try {
			String sql = "DELETE  FROM `urgency` WHERE `urgencyId` = ? ";
			int result = jdbcTemplate.update(sql, urgencyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int addBrand(Brand brand) {
		try {
			String sql = "INSERT INTO `brand` (`brand`,`prodId`) VALUES (?,?)";
			int result = jdbcTemplate.update(sql, brand.getBrand(), brand.getProdId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addUserType(UserType userType) {
		try {
			String sql = "INSERT INTO `usertype` (`desc`) values (?)";
			int result = jdbcTemplate.update(sql, userType.getDesc());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override

	public int account(Account account) {
		try {
			String sql = "INSERT INTO `account` (`accountEntry`,`accountTypeId`) values (?,?)";
			int result = jdbcTemplate.update(sql, account.getAccountEntry(), account.getAccountTypeId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int modifyBrand(Brand brand) {
		try {
			String sql = "UPDATE `brand` SET `brand` =?,`prodId`=? WHERE `brandId` = ? ";
			int result = jdbcTemplate.update(sql, brand.getBrand(), brand.getProdId(), brand.getBrandId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override

	public int modifyAccount(Account account) {
		try {
			String sql = "UPDATE `account` SET `accountEntry`=?, `accountTypeId`=? WHERE `accountEntryId`=?";
			int result = jdbcTemplate.update(sql, account.getAccountEntry(), account.getAccountTypeId(),
					account.getAccountEntryId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int modifyUserType(UserType userType) {
		try {
			String sql = "UPDATE `usertype` SET `desc`=? WHERE `id`=?";
			int result = jdbcTemplate.update(sql, userType.getDesc(), userType.getId());

			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override

	public int removeAccount(int accountEntryId) {
		try {
			String sql = "DELETE  FROM `account` WHERE `accountEntryId` = ? ";
			int result = jdbcTemplate.update(sql, accountEntryId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int removeBrand(int brandId) {
		try {
			String sql = "DELETE  FROM `brand` WHERE `brandId` = ? ";
			int result = jdbcTemplate.update(sql, brandId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeUserType(int id) {
		try {
			String sql = "DELETE  FROM `usertype` WHERE `id` = ? ";
			int result = jdbcTemplate.update(sql, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public View fetchView(String ownerId, String viewerId) {
		try {
			String sql = "SELECT * FROM `view` WHERE `viewerId`=? AND `ownerId`=?";
			RowMapper<View> rowMapper = new BeanPropertyRowMapper<View>(View.class);
			View view = jdbcTemplate.queryForObject(sql, rowMapper, viewerId, ownerId);
			return view;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addView(View view, String delete_flag) {
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sql = "INSERT INTO `view` (`viewerId`,`ownerId`,`count`,`created`,`updated`,`createdBy`,`updatedBy`,`deleteflag`,`lastVisit`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, view.getViewerId(), view.getOwnerId(), view.getCount(), timestamp,
					timestamp, view.getViewerId(), view.getViewerId(), delete_flag, timestamp);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateView(View view, long viewId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `view` SET `count` = ?,`updated` = ?,`updatedBy` = ?, `lastVisit`=? WHERE `viewId` = ?";
			int result = jdbcTemplate.update(sql, view.getCount(), timestamp, view.getViewerId(), timestamp, viewId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Party fetchPartyByRoleBasedId(String viewerId, String delete_flag_N) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `roleBasedId`= ? AND `delete_flag`=?";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, viewerId, delete_flag_N);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateLastView(int viewId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `view` SET `lastVisit`=? WHERE `viewId` = ?";
			int result = jdbcTemplate.update(sql, timestamp, viewId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<View> fetchViewByOwnerId(String ownerId, String delete_flag_N) {
		try {
			String sql = "SELECT * FROM `view` WHERE `ownerId`=? AND `deleteflag`=?";
			RowMapper<View> rowMapper = new BeanPropertyRowMapper<View>(View.class);
			List<View> view = jdbcTemplate.query(sql, rowMapper, ownerId, delete_flag_N);
			return view;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<View> fetchAllViewCount(String delete_flag_N) {
		try {
			String sql = "SELECT * FROM `view` WHERE `deleteflag`=?";
			RowMapper<View> rowMapper = new BeanPropertyRowMapper<View>(View.class);
			List<View> view = jdbcTemplate.query(sql, rowMapper, delete_flag_N);
			return view;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
