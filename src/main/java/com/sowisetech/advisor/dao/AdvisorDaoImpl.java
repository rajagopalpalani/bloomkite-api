package com.sowisetech.advisor.dao;

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
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.advisor.model.Adv;
import com.sowisetech.advisor.model.AdvBrandInfo;
import com.sowisetech.advisor.model.AdvBrandRank;
import com.sowisetech.advisor.model.AdvProduct;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.AdvisorType;
import com.sowisetech.advisor.model.ArticleStatus;
import com.sowisetech.advisor.model.Award;
import com.sowisetech.advisor.model.Brand;
import com.sowisetech.advisor.model.BrandsComment;
import com.sowisetech.advisor.model.Category;
import com.sowisetech.advisor.model.CategoryType;
import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.model.ChatMessage;
import com.sowisetech.advisor.model.ChatUser;
import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.CommentVote;
import com.sowisetech.advisor.model.CommentVoteAddress;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.FollowerStatus;
import com.sowisetech.advisor.model.Followers;
import com.sowisetech.advisor.model.ForumCategory;
import com.sowisetech.advisor.model.ForumStatus;
import com.sowisetech.advisor.model.ForumSubCategory;
import com.sowisetech.advisor.model.GeneratedOtp;
import com.sowisetech.advisor.model.KeyPeople;
import com.sowisetech.advisor.model.License;
import com.sowisetech.advisor.model.Party;
import com.sowisetech.advisor.model.PartyStatus;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Promotion;
import com.sowisetech.advisor.model.Remuneration;
import com.sowisetech.advisor.model.RiskQuestionaire;
import com.sowisetech.advisor.model.Service;
import com.sowisetech.advisor.model.ServicePlan;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.UserType;
import com.sowisetech.advisor.model.WorkFlowStatus;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.Blogger;
import com.sowisetech.investor.dao.InvestorExtractor;
import com.sowisetech.investor.dao.InvestorListExtractor;
import com.sowisetech.investor.model.Investor;

@Transactional
@Repository
public class AdvisorDaoImpl implements AdvisorDao {

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

	private static final Logger logger = LoggerFactory.getLogger(AdvisorDaoImpl.class);

	@Override
	public int fetchTypeIdByAdvtype(String advType) {
		try {
			String sqlType = "SELECT `id` FROM `advisortype` WHERE `advType`= ?";
			int typeId = jdbcTemplate.queryForObject(sqlType, Integer.class, advType);
			return typeId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int advSignup(Advisor advisor, String encryptPass, int promoCount) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `advisor` (`advId`,`name`, `emailId`,`panNumber`, `password`,`userName`, `phoneNumber`,`partyStatusId`,`created`,`updated`,`delete_flag`,`advType`,`parentPartyId`,`corporateLable`,`workFlowStatus`,`created_by`,`updated_by`,`promoCount`) values (?,ENCODE(?, ?),ENCODE(LOWER(?), ?),ENCODE(LOWER(?), ?),?,ENCODE(LOWER(?), ?),ENCODE(LOWER(?), ?),?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
			int result = jdbcTemplate.update(sql, advisor.getAdvId(), advisor.getName(), encryptPass,
					advisor.getEmailId(), encryptPass, advisor.getPanNumber(), encryptPass, advisor.getPassword(),
					advisor.getUserName(), encryptPass, advisor.getPhoneNumber(), encryptPass,
					advisor.getPartyStatusId(), timestamp, timestamp, advisor.getDelete_flag(), advisor.getAdvType(),
					advisor.getParentPartyId(), advisor.getCorporateLable(), advisor.getWorkFlowStatus(),
					advisor.getCreated_by(), advisor.getUpdated_by(), promoCount);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addParty(Advisor advisor, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlForPartyInsert = "INSERT INTO `party` (`partyStatusId`,`roleBasedId`,`emailId`,`password`,`panNumber`,`phoneNumber`,`userName`,`created`,`updated`,`delete_flag`,`parentPartyId`,`created_by`,`updated_by`) values (?,?,ENCODE(LOWER(?), ?), ?,ENCODE(LOWER(?), ?),ENCODE(LOWER(?), ?), ENCODE(LOWER(?), ?), ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, advisor.getPartyStatusId(), advisor.getAdvId(),
					advisor.getEmailId(), encryptPass, advisor.getPassword(), advisor.getPanNumber(), encryptPass,
					advisor.getPhoneNumber(), encryptPass, advisor.getUserName(), encryptPass, timestamp, timestamp,
					advisor.getDelete_flag(), advisor.getParentPartyId(), advisor.getCreated_by(),
					advisor.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<AdvBrandInfo> fetchAdvBrandInfoByAdvId(String advId, String deleteflag) {
		try {
			String sqlBrandInfo = "SELECT `advBrandId`, `advId`, `prodId`, `serviceId`, `brandId`, `delete_flag`, `priority`,`created`,`updated`,`created_by`,`updated_by` FROM `advbrandinfo` WHERE `advId`=? AND `delete_flag`=?";
			RowMapper<AdvBrandInfo> brandInfoMapper = new BeanPropertyRowMapper<AdvBrandInfo>(AdvBrandInfo.class);
			return jdbcTemplate.query(sqlBrandInfo, brandInfoMapper, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchBrandByBrandId(long brandId) {
		try {
			String sql1 = "SELECT `brand` FROM `brand` WHERE `brandId` = ?";
			return jdbcTemplate.queryForObject(sql1, String.class, brandId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchAdvisorList(Pageable pageable, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM advisor WHERE delete_flag =? ORDER BY created ASC LIMIT  "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ " ) adv LEFT JOIN experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM advisor WHERE delete_flag =? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advproduct prod ON (adv.advId = prod.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM advisor WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			List<Advisor> advisors = jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag);
			return advisors;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int update(String advId, Advisor advisor, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql1 = "UPDATE `advisor` SET `name`= ENCODE(?, ?), `emailId`=ENCODE(?, ?), `phoneNumber`=ENCODE(?, ?),`userName`=ENCODE(?, ?),`designation`=?,`displayName`=?,`dob`=ENCODE(?, ?),`gender`=?,`panNumber`=ENCODE(?, ?),`address1`=?,`address2`=?,`state`=?,`city`=?,`aboutme`=?, `pincode`=?, `partyStatusId`=? ,`updated`=?,`updated_by`=?, `firmType`=?, `parentPartyId`=?, `corporateLable`=?,`website`=?,`imagePath`=?,`gst`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sql1, advisor.getName(), encryptPass, advisor.getEmailId(), encryptPass,
					advisor.getPhoneNumber(), encryptPass, advisor.getUserName(), encryptPass, advisor.getDesignation(),
					advisor.getDisplayName(), advisor.getDob(), encryptPass, advisor.getGender(),
					advisor.getPanNumber(), encryptPass, advisor.getAddress1(), advisor.getAddress2(),
					advisor.getState(), advisor.getCity(), advisor.getAboutme(), advisor.getPincode(),
					advisor.getPartyStatusId(), timestamp, advisor.getUpdated_by(), advisor.getFirmType(),
					advisor.getParentPartyId(), advisor.getCorporateLable(), advisor.getWebsite(),
					advisor.getImagePath(), advisor.getGst(), advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateAdvisorTimeStamp(String advId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlUpdate = "UPDATE `advisor` SET `updated`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sqlUpdate, timestamp, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Advisor fetchAdvisorByAdvId(String advId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, exp.`expId` COL_A, exp.`company` COL_B,  exp.`designation` COL_C,exp.`location` COL_D,exp.`fromYear` COL_E,exp.`toYear` COL_F,exp.`advId` COL_ADVID,exp.`delete_flag` COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM `advisor` adv LEFT JOIN `experience` exp ON (adv.`advId` = exp.`advId`) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, edu.`eduId`,edu.`degree`,edu.`field`,edu.`institution`,edu.`fromYear`,edu.`toYear`,edu.`advId`,edu.`delete_flag`,'','','','','','', 'edu' VALUE FROM `advisor` adv LEFT JOIN `education` edu ON (adv.`advId` = edu.`advId`)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, awd.`awardId`, awd.`imagePath`,awd.`issuedBy`,awd.`title`,awd.`year`,'',awd.`advId`,awd.`delete_flag`,'','','','','','', 'awd' VALUE FROM `advisor` adv LEFT JOIN `award` awd ON (adv.`advId` = awd.`advId`)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, cert.`certificateId`, cert.`imagePath`,cert.`issuedBy`,cert.`title`,cert.`year`,'',cert.`advId`,cert.`delete_flag`,'' ,'','','','','', 'cert' VALUE FROM `advisor` adv LEFT JOIN `certificate` cert ON (adv.`advId` = cert.`advId`)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, prod.`advProdId`,prod.`licImage`, prod.`prodId`,prod.`serviceId`,prod.`remId`,prod.`licId`,prod.`advId`,prod.`delete_flag`,prod.`licNumber`,prod.`validity`,prod.`created`,prod.`updated`,prod.`created_by`,prod.`updated_by`,'prod' VALUE FROM `advisor` adv LEFT JOIN `advproduct` prod ON (adv.`advId` = prod.`advId`)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, promo.`promotionId`,promo.`title`,promo.`video`,promo.`imagePath`,promo.`aboutVideo`,'',promo.`advId`,promo.`delete_flag`,'','','','','','', 'promo' VALUE FROM `advisor` adv LEFT JOIN `promotion` promo ON (adv.`advId` = promo.`advId`) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, brandinfo.`advBrandId`,brandinfo.`prodId`,brandinfo.`serviceId`,brandinfo.`brandId`,brandinfo.`priority`,'',brandinfo.`advId`,brandinfo.`delete_flag`,'','','','','','', 'brandinfo' VALUE FROM `advisor` adv LEFT JOIN `advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,adv.`promoCount` promoCount, brandrank.`advBrandRankId`,brandrank.`prodId`,brandrank.`brandId`,brandrank.`ranking`,'','',brandrank.`advId`,brandrank.`delete_flag`,'','','','','','', 'brandrank'VALUE FROM `advisor` adv LEFT JOIN `advbrandrank` brandrank ON (adv.`advId` = brandrank.`advId`)) AS `advisor` WHERE `advId` = ? AND `delete_flag` = ?";

			return jdbcTemplate.query(sql, new AdvisorExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, advId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Advisor fetchPublicAdvisorByAdvId(String advId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.`expId` COL_A, exp.`company` COL_B,  exp.`designation` COL_C,exp.`location` COL_D,exp.`fromYear` COL_E,exp.`toYear` COL_F,exp.`advId` COL_ADVID,exp.`delete_flag` COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM `public_advisor` adv LEFT JOIN `public_experience` exp ON (adv.`advId` = exp.`advId`) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.`eduId`,edu.`degree`,edu.`field`,edu.`institution`,edu.`fromYear`,edu.`toYear`,edu.`advId`,edu.`delete_flag`,'','','','','','', 'edu' VALUE FROM `public_advisor` adv LEFT JOIN `public_education` edu ON (adv.`advId` = edu.`advId`)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.`awardId`, awd.`imagePath`,awd.`issuedBy`,awd.`title`,awd.`year`,'',awd.`advId`,awd.`delete_flag`,'','','','','','', 'awd' VALUE FROM `public_advisor` adv LEFT JOIN `public_award` awd ON (adv.`advId` = awd.`advId`)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.`certificateId`, cert.`imagePath`,cert.`issuedBy`,cert.`title`,cert.`year`,'',cert.`advId`,cert.`delete_flag`,'' ,'','','','','', 'cert' VALUE FROM `public_advisor` adv LEFT JOIN `public_certificate` cert ON (adv.`advId` = cert.`advId`)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.`advProdId`,prod.`licImage`, prod.`prodId`,prod.`serviceId`,prod.`remId`,prod.`licId`,prod.`advId`,prod.`delete_flag`,prod.`licNumber`,prod.`validity`,prod.`created`,prod.`updated`,prod.`created_by`,prod.`updated_by`,'prod' VALUE FROM `public_advisor` adv LEFT JOIN `public_advproduct` prod ON (adv.`advId` = prod.`advId`)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.`promotionId`,promo.`title`,promo.`video`,promo.`imagePath`,promo.`aboutVideo`,'',promo.`advId`,promo.`delete_flag`,'','','','','','', 'promo' VALUE FROM `public_advisor` adv LEFT JOIN `public_promotion` promo ON (adv.`advId` = promo.`advId`) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.`advBrandId`,brandinfo.`prodId`,brandinfo.`serviceId`,brandinfo.`brandId`,brandinfo.`priority`,'',brandinfo.`advId`,brandinfo.`delete_flag`,'','','','','','', 'brandinfo' VALUE FROM `public_advisor` adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.`advBrandRankId`,brandrank.`prodId`,brandrank.`brandId`,brandrank.`ranking`,'','',brandrank.`advId`,brandrank.`delete_flag`,'','','','','','', 'brandrank'VALUE FROM `public_advisor` adv LEFT JOIN `public_advbrandrank` brandrank ON (adv.`advId` = brandrank.`advId`)) AS `advisor` WHERE `advId` = ? AND `delete_flag` = ?";

			return jdbcTemplate.query(sql, new AdvisorExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, advId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addAdvProductInfo(String advId, AdvProduct advProduct, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlInsert = "INSERT INTO `advproduct` (`advId`, `prodId`, `serviceId`,`remId`,`licId`,`licNumber`,`validity`,`delete_flag`,`licImage`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, advId, advProduct.getProdId(), advProduct.getServiceId(),
					advProduct.getRemId(), advProduct.getLicId(), advProduct.getLicNumber(), advProduct.getValidity(),
					deleteflag, advProduct.getLicImage(), timestamp, timestamp, advProduct.getCreated_by(),
					advProduct.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Award fetchAward(long awardId, String deleteflag) {
		try {
			String sql = "SELECT `awardId`, `imagePath`, `issuedBy`, `title`, `year`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `award` WHERE `awardId` = ? AND `delete_flag`=?";
			RowMapper<Award> rowMapper = new BeanPropertyRowMapper<Award>(Award.class);
			Award award = jdbcTemplate.queryForObject(sql, rowMapper, awardId, deleteflag);
			return award;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Certificate fetchCertificate(long certificateId, String deleteflag) {
		try {
			String sql = "SELECT `certificateId`, `imagePath`, `issuedBy`, `title`, `year`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `certificate` WHERE `certificateId` = ? AND `delete_flag`=?";
			RowMapper<Certificate> rowMapper = new BeanPropertyRowMapper<Certificate>(Certificate.class);
			Certificate certificate = jdbcTemplate.queryForObject(sql, rowMapper, certificateId, deleteflag);
			return certificate;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Education fetchEducation(long educationId, String deleteflag) {
		try {
			String sql = "SELECT `eduId`, `degree`, `field`, `institution`, `fromYear`, `toYear`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `education` WHERE `eduId` = ? AND `delete_flag`=?";
			RowMapper<Education> rowMapper = new BeanPropertyRowMapper<Education>(Education.class);
			Education education = jdbcTemplate.queryForObject(sql, rowMapper, educationId, deleteflag);
			return education;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Experience fetchExperience(long expId, String deleteflag) {
		try {
			String sql = "SELECT `expId`, `company`, `designation`, `location`, `fromYear`, `toYear`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `experience` WHERE `expId` = ? AND `delete_flag`=?";
			RowMapper<Experience> rowMapper = new BeanPropertyRowMapper<Experience>(Experience.class);
			Experience experience = jdbcTemplate.queryForObject(sql, rowMapper, expId, deleteflag);
			return experience;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int removeAdvAward(long awardId, String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `award` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `awardId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, awardId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvCertificate(long certificateId, String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `certificate` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `certificateId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, certificateId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvEducation(long eduId, String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `education` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `eduId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, eduId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvExperience(long expId, String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `experience` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `expId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, expId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Advisor fetchAdvisorByEmailId(String emailId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name, DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`,`created_by`,`updated_by`,`displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme`, `firmType`, `parentPartyId`, `corporateLable`, `website` FROM `advisor` WHERE DECODE(`emailId`,?) = ? AND `delete_flag`=?";
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, emailId, encryptPass, encryptPass, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchAdvisorSmartId() {
		try {
			String sql1 = "SELECT `id` FROM `advisorsmartid` ORDER BY `s_no` DESC LIMIT 1";
			String id = jdbcTemplate.queryForObject(sql1, String.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addAdvSmartId(String newId) {
		try {
			String sqlInsert = "INSERT INTO `advisorsmartid` (`id`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// Password Encrypt
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
	public int addAdvPersonalInfo(String advId, Advisor adv, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlUpdate = "UPDATE `advisor` SET `username`=ENCODE(?,?),`name`=ENCODE(?,?),`panNumber`=ENCODE(?,?),`displayName`=?,`firmType`=?,`corporateLable`=?,`website`=?,`imagePath`=?,`dob`=ENCODE(?,?),`designation`=?,`gender`=?,`address1`=?,`address2`=?,`state`=?,`city`=?,`pincode`=?,`aboutme`=?,`updated`=?,`updated_by`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sqlUpdate, adv.getUserName(), encryptPass, adv.getName(), encryptPass,
					adv.getPanNumber(), encryptPass, adv.getDisplayName(), adv.getFirmType(), adv.getCorporateLable(),
					adv.getWebsite(), adv.getImagePath(), adv.getDob(), encryptPass, adv.getDesignation(),
					adv.getGender(), adv.getAddress1(), adv.getAddress2(), adv.getState(), adv.getCity(),
					adv.getPincode(), adv.getAboutme(), timestamp, adv.getUpdated_by(), advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public boolean checkForPasswordMatch(String advId, String currentPassword,
	// String deleteflag) {
	// try {
	// String sql = "SELECT * FROM `advisor` WHERE `advId` = ? AND `delete_flag`=?";
	// RowMapper<Advisor> rowMapper = new
	// BeanPropertyRowMapper<Advisor>(Advisor.class);
	// Advisor advisor = jdbcTemplate.queryForObject(sql, rowMapper, advId,
	// deleteflag);
	// // System.out.println(currentPassword);
	// String password = decrypt(advisor.getPassword());
	// if (password.equals(currentPassword)) {
	// return true;
	// } else {
	// return false;
	// }
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return false;
	// }
	// }

	// // Password Decrypt
	// @Override
	// public String decrypt(String encryptedText) {
	// try {
	// Security.addProvider(new BouncyCastleProvider());
	// StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
	// String sql1 = "SELECT * FROM `secret_key`";
	// String key = jdbcTemplate.queryForObject(sql1, String.class);
	// cryptor.setPassword(key);
	// cryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
	// String plainText = cryptor.decrypt(encryptedText);
	// // System.out.println(plainText);
	// return plainText;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public int changeAdvPassword(String roleBasedId, String newPassword) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlUpdate = "UPDATE `advisor` SET `password`=?,`updated`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sqlUpdate, newPassword, timestamp, roleBasedId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public AdvProduct fetchAdvProduct(long advProdId, String deleteflag) {
		try {
			String sql = "SELECT `advProdId`, `advId`, `prodId`, `serviceId`, `remId`, `licId`, `delete_flag`, `licImage`, `licNumber`, `validity`,`created`,`updated`,`created_by`,`updated_by` FROM `advproduct` WHERE `advProdId` = ? AND `delete_flag`=?";
			RowMapper<AdvProduct> rowMapper = new BeanPropertyRowMapper<AdvProduct>(AdvProduct.class);
			AdvProduct product = jdbcTemplate.queryForObject(sql, rowMapper, advProdId, deleteflag);
			return product;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyAdvisorProduct(AdvProduct advProduct, String advId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql1 = "UPDATE `advproduct` SET `prodId`=?, `serviceId`=?,`remId`=?,`licId`=?,`licNumber`=?,`validity`=?,`licImage`=?,`updated`=?,`updated_by`=? WHERE `advProdId`=? AND `advId`=?";
			return jdbcTemplate.update(sql1, advProduct.getProdId(), advProduct.getServiceId(), advProduct.getRemId(),
					advProduct.getLicId(), advProduct.getLicNumber(), advProduct.getValidity(),
					advProduct.getLicImage(), timestamp, advProduct.getUpdated_by(), advProduct.getAdvProdId(), advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addAdvBrandInfo(String advId, AdvBrandInfo advBrandInfo, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `advbrandinfo` (`prodId`, `serviceId`,`brandId`, `delete_flag`, `priority`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, advBrandInfo.getProdId(), advBrandInfo.getServiceId(),
					advBrandInfo.getBrandId(), deleteflag, advBrandInfo.getPriority(), advId, timestamp, timestamp,
					advBrandInfo.getCreated_by(), advBrandInfo.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Category> fetchCategoryList() {
		try {
			String sql = "SELECT `categoryId`, `categoryTypeId`, `desc` FROM `category`";
			RowMapper<Category> rowMapper = new BeanPropertyRowMapper<Category>(Category.class);
			List<Category> category = jdbcTemplate.query(sql, rowMapper);
			return category;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<CategoryType> fetchCategoryTypeList() {
		try {
			String sql = "SELECT `categoryTypeId`, `desc` FROM `categorytype`";
			RowMapper<CategoryType> rowMapper = new BeanPropertyRowMapper<CategoryType>(CategoryType.class);
			List<CategoryType> categoryType = jdbcTemplate.query(sql, rowMapper);
			return categoryType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ForumCategory> fetchForumCategoryList() {
		try {
			String sql = "SELECT `forumCategoryId`, `name` FROM `forumcategory`";
			RowMapper<ForumCategory> rowMapper = new BeanPropertyRowMapper<ForumCategory>(ForumCategory.class);
			List<ForumCategory> forumCategory = jdbcTemplate.query(sql, rowMapper);
			return forumCategory;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<RiskQuestionaire> fetchAllRiskQuestionaire() {
		try {
			String sql = "SELECT `answerId`, `answer`, `question`, `questionId`, `score` FROM `riskquestionaire`";
			RowMapper<RiskQuestionaire> rowMapper = new BeanPropertyRowMapper<RiskQuestionaire>(RiskQuestionaire.class);
			List<RiskQuestionaire> riskQuestionaire = jdbcTemplate.query(sql, rowMapper);
			return riskQuestionaire;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Product> fetchProductList() {
		try {
			return jdbcTemplate.query(
					"SELECT prod.*, serv.serviceId COL_A, serv.service COL_B, serv.prodId COL_C, 1 IS_SERVICE FROM product prod LEFT JOIN service serv ON (prod.prodId =serv.prodId) UNION ALL SELECT prod.*, bran.brandId, bran.brand, bran.prodId, 0 IS_SERVICE FROM product prod LEFT JOIN brand bran ON (prod.prodId = bran.prodId)",
					// "SELECT * FROM (SELECT prod.`prodId` prodId,prod.`product`
					// product,serv.`service` COL_A,serv.`serviceId` COL_SERVID,serv.`prodId`
					// COL_PRODID, '' COL_BRANDID,'' COL_C, '' COL_D, '' COL_E, '' COL_F,'' COL_G,
					// '' COL_H, 'serv' VALUE FROM `product` prod LEFT JOIN `service` serv ON
					// (prod.`prodId` = serv.`prodId`)\r\n"
					// + "UNION ALL SELECT prod.`prodId` prodId,prod.`product` product,
					// bran.`brand`,'',bran.`prodId`,bran.`brandId`,'','','','','','','brand' VALUE
					// FROM `product` prod LEFT JOIN `brand` bran ON (prod.`prodId` = bran.`prodId`)
					// \r\n"
					// + "UNION ALL SELECT prod.`prodId` prodId,prod.`product`
					// product,servplan.`servicePlan`,
					// servplan.`serviceId`,servplan.`prodId`,servplan.`brandId`,
					// servplan.`serviceplanId`,servplan.`servicePlanLink`,servplan.`created`,servplan.`updated`,servplan.`created_by`,servplan.`updated_by`,'servplan'
					// VALUE FROM `product` prod LEFT JOIN `serviceplan` servplan ON (prod.`prodId`
					// = servplan.`prodId`)) AS `product`",
					new ProductExtractor());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ForumSubCategory> fetchForumSubCategoryList() {
		try {
			String sql = "SELECT `forumSubCategoryId`, `forumCategoryId`, `name` FROM `forumsubcategory`";
			RowMapper<ForumSubCategory> rowMapper = new BeanPropertyRowMapper<ForumSubCategory>(ForumSubCategory.class);
			List<ForumSubCategory> forumSubCategory = jdbcTemplate.query(sql, rowMapper);
			return forumSubCategory;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ForumStatus> fetchForumStatusList() {
		try {
			String sql = "SELECT `id`, `desc` FROM `forumstatus`";
			RowMapper<ForumStatus> rowMapper = new BeanPropertyRowMapper<ForumStatus>(ForumStatus.class);
			List<ForumStatus> forumStatus = jdbcTemplate.query(sql, rowMapper);
			return forumStatus;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<PartyStatus> fetchPartyStatusList() {
		try {
			String sql = "SELECT `id`, `desc` FROM `partystatus`";
			RowMapper<PartyStatus> rowMapper = new BeanPropertyRowMapper<PartyStatus>(PartyStatus.class);
			List<PartyStatus> partyStatus = jdbcTemplate.query(sql, rowMapper);
			return partyStatus;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Service> fetchServiceList() {
		// try {
		// String sql = "SELECT `serviceId`, `service`, `prodId` FROM `service`"
		// RowMapper<Service> rowMapper = new
		// BeanPropertyRowMapper<Service>(Service.class);
		// List<Service> service = jdbcTemplate.query(sql, rowMapper);
		// return service;
		// } catch (EmptyResultDataAccessException e) {
		// logger.error(e.getMessage());
		// return null;
		// }

		try {
			return jdbcTemplate.query(
					"SELECT serv.*, servplan.servicePlanId COL_A, servplan.servicePlan COL_B, servplan.serviceId COL_C, 1 IS_SERVICEPLAN FROM service serv LEFT JOIN serviceplan servplan ON (serv.serviceId = servplan.serviceId)",
					new ServiceExtractor());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Brand> fetchBrandList() {
		try {
			String sql = "SELECT `brandId`, `prodId`, `brand` FROM `brand`";
			RowMapper<Brand> rowMapper = new BeanPropertyRowMapper<Brand>(Brand.class);
			List<Brand> brand = jdbcTemplate.query(sql, rowMapper);
			return brand;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<License> fetchLicenseList() {
		try {
			String sql = "SELECT `licId`, `license`, `issuedBy`, `prodId` FROM `license`";
			RowMapper<License> rowMapper = new BeanPropertyRowMapper<License>(License.class);
			List<License> license = jdbcTemplate.query(sql, rowMapper);
			return license;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Remuneration> fetchRemunerationList() {
		try {
			String sql = "SELECT `remId`, `remuneration` FROM `remuneration`";
			RowMapper<Remuneration> rowMapper = new BeanPropertyRowMapper<Remuneration>(Remuneration.class);
			List<Remuneration> remuneration = jdbcTemplate.query(sql, rowMapper);
			return remuneration;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<AdvBrandInfo> fetchAdvBrandInfoByAdvIdAndProdId(String advId, long prodId, String deleteflag) {
		try {
			String sql = "SELECT `advBrandId`, `advId`, `prodId`, `serviceId`, `brandId`, `delete_flag`, `priority`,`created`,`updated`,`created_by`,`updated_by` FROM `advbrandinfo` WHERE `advId` = ? AND `prodId`=? AND `delete_flag`=?";
			RowMapper<AdvBrandInfo> rowMapper = new BeanPropertyRowMapper<AdvBrandInfo>(AdvBrandInfo.class);
			List<AdvBrandInfo> advBrandInfo = jdbcTemplate.query(sql, rowMapper, advId, prodId, deleteflag);
			return advBrandInfo;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Long> fetchPriorityByBrandIdAndAdvId(String advId, long prodId, long brandId, String deleteflag) {
		try {
			String sqlPriority = "SELECT `priority` FROM `advbrandinfo` WHERE `brandId`= ? AND `advId`=? AND `prodId`=? AND `delete_flag`=?";
			List<Long> priorities = jdbcTemplate.queryForList(sqlPriority, Long.class, brandId, advId, prodId,
					deleteflag);
			return priorities;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public AdvBrandRank fetchAdvBrandRank(String advId, long prodId, int rank, String deleteflag) {
		try {
			String sql = "SELECT `advBrandRankId`, `advId`, `prodId`, `brandId`, `ranking`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `advbrandrank` WHERE `ranking`= ? AND `advId`=? AND `prodId`=? AND `delete_flag`=?";
			RowMapper<AdvBrandRank> rowMapper = new BeanPropertyRowMapper<AdvBrandRank>(AdvBrandRank.class);
			AdvBrandRank advBrandRank = jdbcTemplate.queryForObject(sql, rowMapper, rank, advId, prodId, deleteflag);
			return advBrandRank;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	@Override
	public int addAdvBrandAndRank(long brand, int rank, String advId, long prodId, String deleteflag,
			String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `advbrandrank` (`advId`,`prodId`,`brandId`,`ranking`,`delete_flag`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, advId, prodId, brand, rank, deleteflag, timestamp, timestamp,
					signedUserId, signedUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateBrandAndRank(long brand, int rank, String advId, long prodId, String deleteflag,
			String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `advbrandrank` SET `advId`=?,  `prodId`=?,`brandId`=?, `ranking`=?, `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `advId`=? AND `prodId`=? AND `ranking`=? AND `delete_flag`=?";
			int result = jdbcTemplate.update(sql1, advId, prodId, brand, rank, deleteflag, timestamp, signedUserId,
					advId, prodId, rank, deleteflag);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<AdvProduct> fetchAdvProductByAdvId(String advId, String deleteflag) {
		try {
			String sql = "SELECT `advProdId`, `advId`, `prodId`, `serviceId`, `remId`, `licId`, `delete_flag`, `licImage`, `licNumber`, `validity`,`created`,`updated`,`created_by`,`updated_by` FROM `advproduct` WHERE `delete_flag`=? AND `advId`=?";
			RowMapper<AdvProduct> rowMapper = new BeanPropertyRowMapper<AdvProduct>(AdvProduct.class);
			List<AdvProduct> advProducts = jdbcTemplate.query(sql, rowMapper, deleteflag, advId);
			return advProducts;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int removeAdvProduct(long advProdId, String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `advproduct` SET `delete_flag`=?,`updated` =?,`updated_by`=? WHERE `advProdId`=? AND `advId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, advProdId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvBrandInfo(long prodId, String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `advbrandinfo` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `prodId`=? AND `advId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, prodId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeFromBrandRank(String advId, long prodId) {
		try {
			String sql = "DELETE FROM `advbrandrank` WHERE `advId`=? AND `prodId`=?";
			int result = jdbcTemplate.update(sql, advId, prodId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public AdvProduct fetchAdvProductByAdvIdAndAdvProdId(String advId, long advProdId, String deleteflag) {
		try {
			String sql = "SELECT `advProdId`, `advId`, `prodId`, `serviceId`, `remId`, `licId`, `delete_flag`, `licImage`, `licNumber`, `validity`,`created`,`updated`,`created_by`,`updated_by` FROM `advproduct` WHERE `advId` = ? AND `advProdId`=? AND `delete_flag`=?";
			RowMapper<AdvProduct> rowMapper = new BeanPropertyRowMapper<AdvProduct>(AdvProduct.class);
			AdvProduct advProduct = jdbcTemplate.queryForObject(sql, rowMapper, advId, advProdId, deleteflag);
			return advProduct;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int removeAdvBrandInfoByAdvId(String advId) {
		try {
			String sql = "DELETE FROM `advbrandinfo` WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvBrandRankByAdvId(String advId) {
		try {
			String sql = "DELETE FROM `advbrandrank` WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Product> fetchAllServiceAndBrand() {
		try {
			return jdbcTemplate.query(
					"SELECT prod.*, serv.serviceId COL_A, serv.service COL_B, serv.prodId COL_C, 1 IS_SERVICE FROM product prod LEFT JOIN service serv ON (prod.prodId = serv.prodId) UNION ALL SELECT prod.*, bran.brandId, bran.brand, bran.prodId, 0 IS_SERVICE FROM product prod LEFT JOIN brand bran ON (prod.prodId = bran.prodId)",
					new ProductExtractor());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public List<Award> fetchAwardByadvId(String advId, String deleteflag) {
		try {
			String sqlAward = "SELECT `awardId`, `imagePath`, `issuedBy`, `title`, `year`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `award` WHERE `advId`=? AND `delete_flag` = ?";
			RowMapper<Award> awardMapper = new BeanPropertyRowMapper<Award>(Award.class);
			return jdbcTemplate.query(sqlAward, awardMapper, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Certificate> fetchCertificateByadvId(String advId, String deleteflag) {
		try {
			String sqlCertificate = "SELECT `certificateId`, `imagePath`, `issuedBy`, `title`, `year`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `certificate` WHERE `advId`=? AND `delete_flag` =?";
			RowMapper<Certificate> certificateMapper = new BeanPropertyRowMapper<Certificate>(Certificate.class);
			return jdbcTemplate.query(sqlCertificate, certificateMapper, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Experience> fetchExperienceByadvId(String advId, String deleteflag) {
		try {
			String sqlExperience = "SELECT `expId`, `company`, `designation`, `location`, `fromYear`, `toYear`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `experience` WHERE `advId`=? AND `delete_flag` = ?";
			RowMapper<Experience> experienceMapper = new BeanPropertyRowMapper<Experience>(Experience.class);
			return jdbcTemplate.query(sqlExperience, experienceMapper, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Education> fetchEducationByadvId(String advId, String deleteflag) {
		try {
			String sqlEducation = "SELECT `eduId`, `degree`, `field`, `institution`, `fromYear`, `toYear`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `education` WHERE `advId`=? AND `delete_flag`=?";
			RowMapper<Education> educationMapper = new BeanPropertyRowMapper<Education>(Education.class);
			return jdbcTemplate.query(sqlEducation, educationMapper, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyAdvisorAward(long awardId, Award award, String advId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `award` SET `title`=?, `issuedBy`=?,`year`=?, `imagePath`=?,`updated`=?,`updated_by`=? WHERE `awardId`=? AND `advId`=?";
			return jdbcTemplate.update(sql1, award.getTitle(), award.getIssuedBy(), award.getYear(),
					award.getImagePath(), timestamp, award.getUpdated_by(), awardId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int modifyAdvisorCertificate(long certificateId, Certificate certificate, String advId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `certificate` SET `title`=?, `issuedBy`=?,`year`=?, `imagePath`=?,`updated`=?,`updated_by`=? WHERE `certificateId`=? AND `advId`=?";
			return jdbcTemplate.update(sql1, certificate.getTitle(), certificate.getIssuedBy(), certificate.getYear(),
					certificate.getImagePath(), timestamp, certificate.getUpdated_by(), certificateId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int modifyAdvisorExperience(long expId, Experience experience, String advId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `experience` SET `company`=?, `designation`=?, `location`=?, `fromYear`=?, `toYear`=?,`updated`=?,`updated_by`=? WHERE `expId`=? AND `advId`=?";
			return jdbcTemplate.update(sql1, experience.getCompany(), experience.getDesignation(),
					experience.getLocation(), experience.getFromYear(), experience.getToYear(), timestamp,
					experience.getUpdated_by(), expId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int modifyAdvisorEducation(long eduId, Education education, String advId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `education` SET `institution`=?, `degree`=?, `field`=?, `fromYear`=?, `toYear`=?,`updated`=?,`updated_by`=? WHERE `eduId`=? AND `advId` = ?";
			return jdbcTemplate.update(sql1, education.getInstitution(), education.getDegree(), education.getField(),
					education.getFromYear(), education.getToYear(), timestamp, education.getUpdated_by(), eduId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addAdvAwardInfo(String advId, Award award, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `award` (`title`, `issuedBy`,`imagePath`, `year`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, award.getTitle(), award.getIssuedBy(), award.getImagePath(),
					award.getYear(), deleteflag, advId, timestamp, timestamp, award.getCreated_by(),
					award.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addAdvCertificateInfo(String advId, Certificate certificate, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `certificate` (`title`, `issuedBy`,`imagePath`, `year`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, certificate.getTitle(), certificate.getIssuedBy(),
					certificate.getImagePath(), certificate.getYear(), deleteflag, advId, timestamp, timestamp,
					certificate.getCreated_by(), certificate.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addAdvExperienceInfo(String advId, Experience experience, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `experience` (`company`, `designation`,`location`, `fromYear`, `toYear`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, experience.getCompany(), experience.getDesignation(),
					experience.getLocation(), experience.getFromYear(), experience.getToYear(), deleteflag, advId,
					timestamp, timestamp, experience.getCreated_by(), experience.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int addAdvEducationInfo(String advId, Education education, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `education` (`degree`, `field`,`institution`, `fromYear`, `toYear`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, education.getDegree(), education.getField(),
					education.getInstitution(), education.getFromYear(), education.getToYear(), deleteflag, advId,
					timestamp, timestamp, education.getCreated_by(), education.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Award fetchAdvAwardByAdvIdAndAwardId(long awardId, String advId, String deleteflag) {
		try {
			String sql = "SELECT `awardId`, `imagePath`, `issuedBy`, `title`, `year`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `award` WHERE `awardId` = ? AND `advId` = ? AND `delete_flag`=?";
			RowMapper<Award> rowMapper = new BeanPropertyRowMapper<Award>(Award.class);
			Award award = jdbcTemplate.queryForObject(sql, rowMapper, awardId, advId, deleteflag);
			return award;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Certificate fetchAdvCertificateByAdvIdAndCertificateId(long certificateId, String advId, String deleteflag) {
		try {
			String sql = "SELECT `certificateId`, `imagePath`, `issuedBy`, `title`, `year`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `certificate` WHERE `certificateId` = ? AND `advId` = ? AND `delete_flag`=?";
			RowMapper<Certificate> rowMapper = new BeanPropertyRowMapper<Certificate>(Certificate.class);
			Certificate certificate = jdbcTemplate.queryForObject(sql, rowMapper, certificateId, advId, deleteflag);
			return certificate;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Education fetchAdvEducationByAdvIdAndEduId(long eduId, String advId, String deleteflag) {
		try {
			String sql = "SELECT `eduId`, `degree`, `field`, `institution`, `fromYear`, `toYear`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `education` WHERE `eduId` = ? AND `advId` = ? AND `delete_flag`=?";
			RowMapper<Education> rowMapper = new BeanPropertyRowMapper<Education>(Education.class);
			Education education = jdbcTemplate.queryForObject(sql, rowMapper, eduId, advId, deleteflag);
			return education;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Experience fetchAdvExperienceByAdvIdAndExpId(long expId, String advId, String deleteflag) {
		try {
			String sql = "SELECT `expId`, `company`, `designation`, `location`, `fromYear`, `toYear`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `experience` WHERE `expId` = ? AND `advId` = ? AND `delete_flag`=?";
			RowMapper<Experience> rowMapper = new BeanPropertyRowMapper<Experience>(Experience.class);
			Experience experience = jdbcTemplate.queryForObject(sql, rowMapper, expId, advId, deleteflag);
			return experience;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int removeAwardByAdvId(String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `award` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `advId`=?";
			return jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeCertificateByAdvId(String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `certificate` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `advId`=?";
			return jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeExperienceByAdvId(String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `experience` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `advId`=?";
			return jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeEducationByAdvId(String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `education` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `advId`=?";
			return jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public List<StateCity> fetchAllStateCityPincode() {
	// return jdbcTemplate.query(
	// "SELECT * FROM (SELECT st.*,ct.cityId COL_A,ct.stateId COL_B,ct.city
	// COL_C,ct.pincode COL_D, 1 IS_CITY FROM state st LEFT JOIN city ct ON
	// (st.stateId = ct.stateId)) AS stateList\r\n",
	// new StateExtractor());
	// }
	@Override
	public List<State> fetchAllStateCityPincode() {
		return jdbcTemplate.query(
				"SELECT * FROM (SELECT st.*,ct.cityId COL_A,ct.stateId COL_B,ct.city COL_C,ct.pincode COL_D, 1 IS_CITY FROM state st LEFT JOIN city ct ON (st.stateId = ct.stateId)) AS stateList\r\n",
				new StateExtractor());
	}

	@Override
	public List<AdvBrandRank> fetchAdvBrandRankByAdvId(String advId, String deleteflag) {
		try {
			String sqlRank = "SELECT `advBrandRankId`, `advId`, `prodId`, `brandId`, `ranking`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `advbrandrank` WHERE `advId` = ? AND `delete_flag`= ?";
			RowMapper<AdvBrandRank> rankMapper = new BeanPropertyRowMapper<AdvBrandRank>(AdvBrandRank.class);
			List<AdvBrandRank> advBrandRank = jdbcTemplate.query(sqlRank, rankMapper, advId, deleteflag);
			return advBrandRank;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchPartyIdByRoleBasedId(String roleBasedId, String delete_flag) {
		try {
			String sqlRole = "SELECT `partyId` FROM `party` WHERE `roleBasedId`= ? AND `delete_flag` = ?";
			Long partyId = jdbcTemplate.queryForObject(sqlRole, Long.class, roleBasedId, delete_flag);
			return partyId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeAdvisor(String advId, String deleteflag, String signedUserId, int deactivate) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `advisor` LEFT JOIN `award` ON award.advId = advisor.advId LEFT JOIN `experience` ON experience.advId = advisor.advId LEFT JOIN `education` ON education.advId = advisor.advId LEFT JOIN `certificate` ON certificate.advId = advisor.advId LEFT JOIN `advproduct` ON advproduct.advId = advisor.advId LEFT JOIN `promotion` ON promotion.advId = advisor.advId LEFT JOIN `advbrandinfo` ON advbrandinfo.advId = advisor.advId LEFT JOIN `advbrandrank` ON advbrandrank.advId = advisor.advId LEFT JOIN `party` ON party.roleBasedId = advisor.advId SET advisor.delete_flag = ?, advisor.updated = ?, advisor.updated_by = ?, advisor.workFlowStatus = ?, award.delete_flag = ?, award.updated = ?, award.updated_by = ?, experience.delete_flag = ?,experience.updated = ?, experience.updated_by = ?, education.delete_flag = ?,education.updated = ?, education.updated_by = ?, certificate.delete_flag = ?,certificate.updated = ?, certificate.updated_by = ?, advproduct.delete_flag = ?, advproduct.updated = ?, advproduct.updated_by = ?, promotion.delete_flag = ?, promotion.updated = ?, promotion.updated_by = ?, advbrandinfo.delete_flag = ?,advbrandinfo.updated = ?, advbrandinfo.updated_by = ?, advbrandrank.delete_flag = ?,advbrandrank.updated = ?, advbrandrank.updated_by = ?, party.delete_flag = ?,party.updated = ?, party.updated_by = ? WHERE advisor.advId = ?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, deactivate, deleteflag,
					timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId,
					deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp,
					signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag,
					timestamp, signedUserId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ArticleStatus> fetchArticleStatusList() {
		try {
			String sql = "SELECT `id`, `desc` FROM `articlestatus`";
			RowMapper<ArticleStatus> rowMapper = new BeanPropertyRowMapper<ArticleStatus>(ArticleStatus.class);
			List<ArticleStatus> articleStatus = jdbcTemplate.query(sql, rowMapper);
			return articleStatus;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Advisor checkEmailAvailability(String emailId, String encryptPass) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name,DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `advisor` WHERE DECODE(`emailId`,) = ?";
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, emailId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/* Fetch PartyStatusId By Desc */
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

	// Add to Investor Table
	@Override
	public int addInv(Investor investor, String deleteflag, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `investor` (`invId`,`fullName`, `displayName`, `dob`, `gender`, `emailId`, `password`,`userName`, `phoneNumber`, `pincode`, `partyStatusId`, `created`, `updated`,`delete_flag`,`created_by`,`updated_by`) values (?,ENCODE(?,?),?,ENCODE(?,?),?,ENCODE(LOWER(?),?), ?, ENCODE(LOWER(?),?), ENCODE(LOWER(?),?), ?, ?, ?, ?, ?,?,?)";
			int result = jdbcTemplate.update(sql, investor.getInvId(), investor.getFullName(), encryptPass,
					investor.getDisplayName(), investor.getDob(), encryptPass, investor.getGender(),
					investor.getEmailId(), encryptPass, investor.getPassword(), investor.getUserName(), encryptPass,
					investor.getPhoneNumber(), encryptPass, investor.getPincode(), investor.getPartyStatusId(),
					timestamp, timestamp, deleteflag, investor.getCreated_by(), investor.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	// Add to Party Table
	@Override
	public int addPartyInv(Investor investor, String deleteflag, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlForPartyInsert = "INSERT INTO `party` (`partyStatusId`,`roleBasedId`,`emailId`,`password`,`userName`,`phoneNumber`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,ENCODE(LOWER(?),?),?,ENCODE(LOWER(?),?), ENCODE(LOWER(?),?), ?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, investor.getPartyStatusId(), investor.getInvId(),
					investor.getEmailId(), encryptPass, investor.getPassword(), investor.getUserName(), encryptPass,
					investor.getPhoneNumber(), encryptPass, timestamp, timestamp, deleteflag, investor.getCreated_by(),
					investor.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	/* Fetch investor by email id */
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
	public String fetchInvestorSmartId() {
		try {
			String sql1 = "SELECT `id` FROM `investorsmartid` ORDER BY `s_no` DESC LIMIT 1";
			return jdbcTemplate.queryForObject(sql1, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addInvSmartId(String newId) {
		try {
			String sqlInsert = "INSERT INTO `investorsmartid` (`id`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public long fetchTeamMemberByName(String name) {
	// try {
	// String sqlRole = "SELECT `id` FROM `role` WHERE `name`= ?";
	// long roleId = jdbcTemplate.queryForObject(sqlRole, Long.class, name);
	// return roleId;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	@Override
	public Party fetchPartyByRoleBasedId(String roleBasedId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `roleBasedId` = ? AND `delete_flag` = ?";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					roleBasedId, delete_flag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int changeInvPassword(String roleBasedId, String newPassword) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlUpdate = "UPDATE `investor` SET `password`=?,`updated`=? WHERE `invId`=?";
			int result = jdbcTemplate.update(sqlUpdate, newPassword, timestamp, roleBasedId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int changePartyPassword(String roleBasedId, String password) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlUpdate = "UPDATE `party` SET `password`=?,`updated`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sqlUpdate, password, timestamp, roleBasedId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchTeamByParentPartyId(long parentPartyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM advisor adv LEFT JOIN experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM advisor adv LEFT JOIN education edu ON (adv.advId = edu.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM advisor adv LEFT JOIN award awd ON (adv.advId = awd.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM advisor adv LEFT JOIN certificate cert ON (adv.advId = cert.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM advisor adv LEFT JOIN advproduct prod ON (adv.advId = prod.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM advisor adv LEFT JOIN promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM advisor adv LEFT JOIN advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM advisor adv LEFT JOIN advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisor WHERE parentPartyId = ? AND delete_flag = ?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, parentPartyId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addKeyPeople(KeyPeople keyPeople, String deleteflag, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `keypeople` (`fullName`,`designation`, `image`,`parentPartyId`, `created`, `updated`,`delete_flag`,`created_by`,`updated_by`) values (ENCODE(?,?),?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, keyPeople.getFullName(), encryptPass, keyPeople.getDesignation(),
					keyPeople.getImage(), keyPeople.getParentPartyId(), timestamp, timestamp, deleteflag,
					keyPeople.getCreated_by(), keyPeople.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<KeyPeople> fetchKeyPeopleByParentId(long parentPartyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `keyPeopleId`, DECODE(`fullName`,?) fullName, `designation`, `image`, `parentPartyId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `keypeople` WHERE `parentPartyId` = ? AND `delete_flag`=?";
			RowMapper<KeyPeople> rowMapper = new BeanPropertyRowMapper<KeyPeople>(KeyPeople.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, parentPartyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addPromotion(String advId, Promotion promo, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlForPartyInsert = "INSERT INTO `promotion` (`promotionId`,`title`,`video`, `advId`,`imagePath`,`aboutVideo`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by`) values (?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, promo.getPromotionId(), promo.getTitle(),
					promo.getVideo(), advId, promo.getImagePath(), promo.getAboutVideo(), deleteflag, timestamp,
					timestamp, promo.getCreated_by(), promo.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Promotion> fetchPromotionByAdvId(String advId, String deleteflag) {
		try {
			String sql = "SELECT `promotionId`, `title`, `video`, `imagePath`, `aboutVideo`,`created`,`updated`,`created_by`,`updated_by` FROM `promotion` WHERE `advId` = ? AND `delete_flag` = ?";
			RowMapper<Promotion> rowMapper = new BeanPropertyRowMapper<Promotion>(Promotion.class);
			return jdbcTemplate.query(sql, rowMapper, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Promotion fetchPromotionByAdvIdAndPromotionId(long promotionId, String advId, String deleteflag) {
		try {
			String sql = "SELECT `promotionId`,`title`,`aboutVideo`,`video`, `imagePath`, `advId`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by` FROM `promotion` WHERE `promotionId` = ? AND `advId` = ? AND `delete_flag`=?";
			RowMapper<Promotion> rowMapper = new BeanPropertyRowMapper<Promotion>(Promotion.class);
			Promotion promotion = jdbcTemplate.queryForObject(sql, rowMapper, promotionId, advId, deleteflag);
			return promotion;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyPromotion(long promotionId, Promotion promotion, String advId, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql1 = "UPDATE `promotion` SET `title`=?, `aboutVideo`=?,`video`=?, `imagePath`=?, `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `promotionId`=? AND `advId`=?";
			return jdbcTemplate.update(sql1, promotion.getTitle(), promotion.getAboutVideo(), promotion.getVideo(),
					promotion.getImagePath(), deleteflag, timestamp, promotion.getUpdated_by(), promotionId, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePromotion(long promotionId, String delete_flag_Y, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `promotion` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `promotionId`=?";
			int result = jdbcTemplate.update(sql, delete_flag_Y, timestamp, signedUserId, promotionId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public Advisor fetchAdvisorByParentPartyId(long parentPartyId) {
	// try {
	// String sql = "SELECT * FROM `advisor` WHERE `parentPartyId` = ?";
	// RowMapper<Advisor> rowMapper = new
	// BeanPropertyRowMapper<Advisor>(Advisor.class);
	// return jdbcTemplate.queryForObject(sql, rowMapper, parentPartyId);
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public Party fetchPartyByPartyId(long parentPartyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `partyId` = ? AND `delete_flag` =?";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					parentPartyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addSigninVerification(String emailId, String encryptPass) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sqlForPartyInsert = "INSERT INTO `signin_verification` (`mailId`,`created`) values (ENCODE(LOWER(?),?), ?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, emailId, encryptPass, timestamp);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Investor fetchInvestorByInvId(String roleBasedId, String deleteflag, String encryptPass) {
		try {

			String sql = "SELECT * FROM (SELECT inv.`invId` invId,DECODE(inv.`fullName`,?) fullName,inv.`displayName` displayName,DECODE(inv.`dob`,?) dob,DECODE(inv.`emailId`,?) emailId,inv.`gender` gender,inv.`password` password,DECODE(inv.`userName`,?) userName,DECODE(inv.`phoneNumber`,?) phoneNumber,inv.`pincode` pincode,inv.`imagePath` imagePath,inv.`partyStatusId` partyStatusId,inv.`created` created,inv.`updated` updated, inv.`created_by` created_by,inv.`updated_by` updated_by, inv.`isVerified` isVerified,inv.`verifiedBy` verifiedBy,inv.`verified` verified,inv.`isMobileVerified` isMobileVerified,inv.`delete_flag` delete_flag,invInt.interestId COL_A,invInt.prodId COL_B,invInt.invId COL_INVID,invInt.scale COL_D,invInt.created COL_E,invInt.updated COL_F,  invInt.created_by COL_G,invInt.updated_by COL_H  ,invInt.delete_flag COL_DELETEFLAG, 'invInt' VALUE FROM investor inv LEFT JOIN invinterest invInt ON (inv.invId = invInt.invId)) AS investor WHERE invId=? AND delete_flag=?";
			return jdbcTemplate.query(sql, new InvestorExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, roleBasedId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateAdvisorAccountAsVerified(String advId, int accountVerified) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `advisor` SET `isVerified`=?,`verified`=?,`updated`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, accountVerified, timestamp, timestamp, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateInvestorAccountAsVerified(String invId, int accountVerified) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `investor` SET `isVerified`=?,`verified`=?,`updated`=? WHERE `invId`=?";
			int result = jdbcTemplate.update(sql, accountVerified, timestamp, timestamp, invId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Party fetchPartyByPhoneNumber(String phoneNumber, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE DECODE(`phoneNumber`,?)=?";
			RowMapper<Party> partyMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, phoneNumber);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Party fetchPartyByPAN(String panNumber, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE DECODE(`panNumber`,?)= ?";
			RowMapper<Party> partyMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, panNumber);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Party fetchPartyByUserName(String userName, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE DECODE(`userName`,?)=?";
			RowMapper<Party> partyMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, userName);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
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
	public int teamMemberDeactive(String advId, String deleteflag, String signedUserId, int deactivate) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `advisor` LEFT JOIN `award` ON award.advId = advisor.advId LEFT JOIN `experience` ON experience.advId = advisor.advId LEFT JOIN `education` ON education.advId = advisor.advId LEFT JOIN `certificate` ON certificate.advId = advisor.advId LEFT JOIN `advproduct` ON advproduct.advId = advisor.advId LEFT JOIN `advbrandinfo` ON advbrandinfo.advId = advisor.advId LEFT JOIN `advbrandrank` ON advbrandrank.advId = advisor.advId LEFT JOIN `party` ON party.roleBasedId = advisor.advId SET advisor.delete_flag = ?, advisor.updated = ?, advisor.updated_by = ?, advisor.workFlowStatus = ?, award.delete_flag = ?, award.updated = ?, award.updated_by = ?, experience.delete_flag = ?,experience.updated = ?, experience.updated_by = ?,education.delete_flag = ?,education.updated = ?, education.updated_by = ?,certificate.delete_flag = ?,certificate.updated = ?, certificate.updated_by = ?,advproduct.delete_flag = ?, advproduct.updated = ?, advproduct.updated_by = ?,advbrandinfo.delete_flag = ?,advbrandinfo.updated = ?, advbrandinfo.updated_by = ?,advbrandrank.delete_flag = ?, advbrandrank.updated = ?, advbrandrank.updated_by = ?,party.delete_flag = ? , party.updated = ?, party.updated_by = ? WHERE advisor.advId = ?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, deactivate, deleteflag,
					timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId,
					deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp,
					signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Advisor fetchAdvisorByUserName(String userName, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM public_advisor adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM public_advisor adv LEFT JOIN public_education edu ON (adv.advId = edu.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM public_advisor adv LEFT JOIN public_award awd ON (adv.advId = awd.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM public_advisor adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM public_advisor adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','', 'promo' VALUE FROM public_advisor adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM public_advisor adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM public_advisor adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisor WHERE userName = ? AND delete_flag = ?";

			return jdbcTemplate.query(sql, new AdvisorExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, userName, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addWorkFlowStatusApprovedByAdvId(String advId, int status_approved, String signedUserId, String reason) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sql = "UPDATE `advisor` SET`workFlowStatus`=?,`approvedDate`=?,`approvedBy`=?,`revokedDate`=?,`revokedBy`=?,`updated`=?,`updated_by`=?,`reason_for_revoked`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, status_approved, timestamp, advId, null, null, timestamp,
					signedUserId, null, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addWorkFlowStatusRevokedByAdvId(String advId, int status_revoke, String signedUserId, String reason) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sql = "UPDATE `advisor` SET `workFlowStatus`=?,`approvedDate`=?,`approvedBy`=?,`revokedDate`=?,`revokedBy`=?,`updated`=?,`updated_by`=?, `reason_for_revoked`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, status_revoke, null, null, timestamp, advId, timestamp, signedUserId,
					reason, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addWorkFlowStatusByAdvId(String advId, int status, String signedUserId, String reason) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `advisor` SET `workFlowStatus`=?,`approvedDate`=?,`approvedBy`=?,`revokedDate`=?,`revokedBy`=?,`updated`=?,`updated_by`=?, `reason_for_revoked`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, status, null, null, null, null, timestamp, signedUserId, null, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public Party fetchPartyByRoleId(long roleId, String deleteflag) {
	// try {
	// String sql = "SELECT * FROM `party` WHERE `roleId` = ? AND `delete_flag` =
	// ?";
	// RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
	// return jdbcTemplate.queryForObject(sql, rowMapper, roleId, deleteflag);
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public int fetchWorkFlowStatusIdByDesc(String workFlow_Default) {
		// fetch WorkFlowStatusId query //
		try {
			String sqlWorkFlowStatus = "SELECT `workflowId` FROM `workflowstatus` WHERE `status`=?";
			int WorkFlowStatusId = jdbcTemplate.queryForObject(sqlWorkFlowStatus, Integer.class, workFlow_Default);
			return WorkFlowStatusId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPublicAdvisor(Advisor adv, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `public_advisor` (`advId`,`name`, `emailId`,`panNumber`, `password`,`userName`, `phoneNumber`,`partyStatusId`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag`,`advType`,`parentPartyId`,`corporateLable`,`workFlowStatus`) values (?,ENCODE(?, ?),ENCODE(?, ?),ENCODE(?, ?),?,ENCODE(?, ?),ENCODE(?, ?),?, ?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, adv.getAdvId(), adv.getName(), encryptPass, adv.getEmailId(),
					encryptPass, adv.getPanNumber(), encryptPass, adv.getPassword(), adv.getUserName(), encryptPass,
					adv.getPhoneNumber(), encryptPass, adv.getPartyStatusId(), timestamp, timestamp,
					adv.getCreated_by(), adv.getUpdated_by(), adv.getDelete_flag(), adv.getAdvType(),
					adv.getParentPartyId(), adv.getCorporateLable(), adv.getWorkFlowStatus());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addAdvPublicAwardInfo(String advId, Award award, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `public_award` (`title`, `issuedBy`,`imagePath`, `year`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, award.getTitle(), award.getIssuedBy(), award.getImagePath(),
					award.getYear(), deleteflag, advId, timestamp, timestamp, award.getCreated_by(),
					award.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addAdvPublicCertificateInfo(String advId, Certificate certificate, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `public_certificate` (`title`, `issuedBy`,`imagePath`, `year`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, certificate.getTitle(), certificate.getIssuedBy(),
					certificate.getImagePath(), certificate.getYear(), deleteflag, advId, timestamp, timestamp,
					certificate.getCreated_by(), certificate.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addAdvPublicEducationInfo(String advId, Education education, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `public_education` (`degree`, `field`,`institution`, `fromYear`, `toYear`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, education.getDegree(), education.getField(),
					education.getInstitution(), education.getFromYear(), education.getToYear(), deleteflag, advId,
					timestamp, timestamp, education.getCreated_by(), education.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addAdvPublicExperienceInfo(String advId, Experience experience, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `public_experience` (`company`, `designation`,`location`, `fromYear`, `toYear`, `delete_flag`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, experience.getCompany(), experience.getDesignation(),
					experience.getLocation(), experience.getFromYear(), experience.getToYear(), deleteflag, advId,
					timestamp, timestamp, experience.getCreated_by(), experience.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPublicAdvProductInfo(String advId, AdvProduct advProduct, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlInsert = "INSERT INTO `public_advproduct` (`advId`, `prodId`, `serviceId`,`remId`,`licId`,`licNumber`,`validity`,`delete_flag`,`licImage`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, advId, advProduct.getProdId(), advProduct.getServiceId(),
					advProduct.getRemId(), advProduct.getLicId(), advProduct.getLicNumber(), advProduct.getValidity(),
					deleteflag, advProduct.getLicImage(), timestamp, timestamp, advProduct.getCreated_by(),
					advProduct.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPublicAdvBrandInfo(String advId, AdvBrandInfo advBrandInfo, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `public_advbrandinfo` (`prodId`, `serviceId`,`brandId`, `delete_flag`, `priority`,`advId`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, advBrandInfo.getProdId(), advBrandInfo.getServiceId(),
					advBrandInfo.getBrandId(), deleteflag, advBrandInfo.getPriority(), advId, timestamp, timestamp,
					advBrandInfo.getCreated_by(), advBrandInfo.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPublicAdvBrandAndRank(long brandId, long ranking, String advId, long prodId, String deleteflag,
			String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "INSERT INTO `public_advbrandrank` (`advId`,`prodId`,`brandId`,`ranking`,`delete_flag`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, advId, prodId, brandId, ranking, deleteflag, timestamp, timestamp,
					signedUserId, signedUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPublicPromotion(String advId, Promotion promotion, String deleteflag) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sqlForPartyInsert = "INSERT INTO `public_promotion` (`promotionId`,`title`,`video`, `advId`,`imagePath`,`aboutVideo`, `delete_flag`,`created`,`updated`,`created_by`,`updated_by`) values (?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, promotion.getPromotionId(), promotion.getTitle(),
					promotion.getVideo(), advId, promotion.getImagePath(), promotion.getAboutVideo(), deleteflag,
					timestamp, timestamp, promotion.getCreated_by(), promotion.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePublicAdvisorChild(String advId) {
		try {
			String sql = "DELETE awd,cert,expr,edu,aprod,abrand,brandrank,promo FROM `public_advisor` adv\r\n"
					+ "LEFT JOIN `public_award` awd ON (adv.advId = awd.advId)\r\n"
					+ "LEFT JOIN `public_certificate` cert ON (adv.advId = cert.advId)\r\n"
					+ "LEFT JOIN `public_experience` expr ON (adv.advId = expr.advId)\r\n"
					+ "LEFT JOIN `public_education` edu ON (adv.advId = edu.advId)\r\n"
					+ "LEFT JOIN `public_advproduct` aprod ON (adv.advId = aprod.advId)\r\n"
					+ "LEFT JOIN `public_advbrandinfo` abrand ON (adv.advId = abrand.advId)\r\n"
					+ "LEFT JOIN `public_advbrandrank` brandrank ON (adv.advId = brandrank.advId)\r\n"
					+ "LEFT JOIN `public_promotion` promo ON (adv.advId = promo.advId)\r\n" + "WHERE adv.`advId` =?";
			int result = jdbcTemplate.update(sql, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePublicAdvisor(String advId) {
		try {
			String sqlUpdate = "DELETE FROM `public_advisor` WHERE `advId` =?";
			int result = jdbcTemplate.update(sqlUpdate, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPublicAdvPersonalInfo(String advId, Advisor adv, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlUpdate = "UPDATE `public_advisor` SET `displayName`=?,`firmType`=?,`corporateLable`=?,`website`=?,`imagePath`=?,`dob`=ENCODE(?,?),`designation`=?,`gender`=?,`address1`=?,`address2`=?,`state`=?,`city`=?,`pincode`=?,`aboutme`=?,`isVerified`=?,`isMobileVerified`=?,`updated`=?,`updated_by`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sqlUpdate, adv.getDisplayName(), adv.getFirmType(),
					adv.getCorporateLable(), adv.getWebsite(), adv.getImagePath(), adv.getDob(), encryptPass,
					adv.getDesignation(), adv.getGender(), adv.getAddress1(), adv.getAddress2(), adv.getState(),
					adv.getCity(), adv.getPincode(), adv.getAboutme(), adv.getIsVerified(), adv.getIsMobileVerified(),
					timestamp, adv.getUpdated_by(), advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchAllPublicAdvisor(Pageable pageable, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ " ) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by, adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	// @Override
	// public List<AdvBrandInfo> fetchAdvBrandInfoByProdIdAndServiceId(long
	// prodId,
	// long serviceId, String advId) {
	// String sql = "SELECT * FROM `advbrandinfo` WHERE `delete_flag`=? AND
	// `prodId`=? AND `serviceId`=? AND `advId`=?";
	// RowMapper<AdvBrandInfo> rowMapper = new
	// BeanPropertyRowMapper<AdvBrandInfo>(AdvBrandInfo.class);
	// List<AdvBrandInfo> advBrandInfo = jdbcTemplate.query(sql, rowMapper, "N",
	// prodId, serviceId, advId);
	// return advBrandInfo;
	// }

	@Override
	public int removePublicAdvisorDeleteflag(String advId, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `public_advisor` LEFT JOIN `public_award` ON public_award.advId = public_advisor.advId LEFT JOIN `public_experience` ON public_experience.advId = public_advisor.advId LEFT JOIN `public_education` ON public_education.advId = public_advisor.advId LEFT JOIN `public_certificate` ON public_certificate.advId = public_advisor.advId LEFT JOIN `public_advproduct` ON public_advproduct.advId = public_advisor.advId LEFT JOIN `public_promotion` ON public_promotion.advId = public_advisor.advId LEFT JOIN `public_advbrandinfo` ON public_advbrandinfo.advId = public_advisor.advId LEFT JOIN `public_advbrandrank` ON public_advbrandrank.advId = public_advisor.advId SET public_advisor.delete_flag = ?, public_advisor.updated = ?, public_advisor.updated_by = ?, public_award.delete_flag = ?,public_award.updated = ?, public_award.updated_by = ?,public_experience.delete_flag = ?,public_experience.updated = ?, public_experience.updated_by = ?, public_education.delete_flag = ?,public_education.updated = ?, public_education.updated_by = ?, public_certificate.delete_flag = ?, public_certificate.updated = ?, public_certificate.updated_by = ?,public_advproduct.delete_flag = ?, public_advproduct.updated = ?, public_advproduct.updated_by = ?,public_promotion.delete_flag = ?,public_promotion.updated = ?, public_promotion.updated_by = ?, public_advbrandinfo.delete_flag = ?,public_advbrandinfo.updated = ?, public_advbrandinfo.updated_by = ?, public_advbrandrank.delete_flag = ?,public_advbrandrank.updated = ?, public_advbrandrank.updated_by = ? WHERE public_advisor.advId = ?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, deleteflag, timestamp,
					signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag,
					timestamp, signedUserId, deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId,
					deleteflag, timestamp, signedUserId, deleteflag, timestamp, signedUserId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public KeyPeople fetchKeyPeopleByKeyPeopleId(long keyPeopleId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `keyPeopleId`,DECODE(`fullName`,?) fullName,`designation`,`image`,`parentPartyId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `keypeople` WHERE `delete_flag`=? AND `keyPeopleId`=?";
			RowMapper<KeyPeople> partyMapper = new BeanPropertyRowMapper<KeyPeople>(KeyPeople.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, deleteflag, keyPeopleId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyKeyPeople(long keyPeopleId, KeyPeople keyPeople, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `keypeople` SET `fullName`= ENCODE(?, ?), `designation` =?, `image`=?,`parentPartyId`=?,`updated`=?,`updated_by`=? WHERE `keyPeopleId`=?";
			int result = jdbcTemplate.update(sql1, keyPeople.getFullName(), encryptPass, keyPeople.getDesignation(),
					keyPeople.getImage(), keyPeople.getParentPartyId(), timestamp, keyPeople.getUpdated_by(),
					keyPeopleId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeKeyPeople(long id, String deleteflag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `keypeople` SET `delete_flag`=?,`updated`=?,`updated_by`=? WHERE `keyPeopleId`=?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, id);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Followers> fetchFollowersByUserId(String userId, long statusId) {
		try {
			String sql = "SELECT `followersId`,`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by` FROM `followers` WHERE `userId` = ? AND `status` = ?";
			RowMapper<Followers> rowMapper = new BeanPropertyRowMapper<Followers>(Followers.class);
			return jdbcTemplate.query(sql, rowMapper, userId, statusId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addFollowers(Followers followers) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `followers` (`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by`,`byWhom`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, followers.getUserId(), followers.getUserType(), followers.getStatus(),
					followers.getAdvId(), timestamp, followers.getCreated_by(), timestamp, followers.getUpdated_by(),
					followers.getUserId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Followers fetchFollowersByFollowersId(long followersId, long statusId) {
		try {
			String sql = "SELECT `followersId`,`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by` FROM `followers` WHERE `followersId` = ? AND `status` = ?";
			RowMapper<Followers> rowMapper = new BeanPropertyRowMapper<Followers>(Followers.class);
			Followers followers = jdbcTemplate.queryForObject(sql, rowMapper, followersId, statusId);
			return followers;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	// @Override
	// public int blockFollowers(long followersId, long statusId) {
	// try {
	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	// String sql = "UPDATE `followers` SET `status`=?,`updated`=? WHERE
	// `followersId`=?";
	// int result = jdbcTemplate.update(sql, statusId, timestamp, followersId);
	// return result;
	// } catch (DataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	@Override
	public List<Integer> fetchFollowersCount(String advId, long statusId) {
		try {
			String sql = "SELECT COUNT(*) FROM `followers` WHERE `advId`=? AND `status`=? GROUP BY `userType`";
			List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, advId, statusId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchFollowersId(String advId, long statusId, long userType) {
		try {
			String sql = "SELECT `userId` FROM `followers` WHERE `advId`=? AND `userType`=? AND `status`=?";
			List<String> result = jdbcTemplate.queryForList(sql, String.class, advId, userType, statusId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	// @Override
	// public List<Advisor> fetchAdvisorsByAdvIds(List<String> advIds, String
	// deleteflag, String encryptPass) {
	// String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// exp.`expId` COL_A, exp.`company` COL_B, exp.`designation`
	// COL_C,exp.`location` COL_D,exp.`fromYear` COL_E,exp.`toYear`
	// COL_F,exp.`advId` COL_ADVID,exp.`delete_flag` COL_DELETEFLAG, '' COL_I,''
	// COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM `advisor` adv
	// LEFT JOIN `experience` exp ON (adv.`advId` = exp.`advId`) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// edu.`eduId`,edu.`degree`,edu.`field`,edu.`institution`,edu.`fromYear`,edu.`toYear`,edu.`advId`,edu.`delete_flag`,'','','','','','',
	// 'edu' VALUE FROM advisor adv LEFT JOIN education edu ON (adv.`advId` =
	// edu.`advId`)\r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// awd.`awardId`,
	// awd.`imagePath`,awd.`issuedBy`,awd.`title`,awd.`year`,'',awd.`advId`,awd.`delete_flag`,'','','','','','','awd'
	// VALUE FROM `advisor` adv LEFT JOIN `award` awd ON (adv.`advId` = awd.`advId`)
	// \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// cert.`certificateId`,
	// cert.`imagePath`,cert.`issuedBy`,cert.`title`,cert.`year`,'',cert.`advId`,cert.`delete_flag`,''
	// ,'','','','','','cert' VALUE FROM `advisor` adv LEFT JOIN `certificate` cert
	// ON (adv.`advId` = cert.`advId`)\r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// prod.`advProdId`,prod.`licImage`,
	// prod.`prodId`,prod.`serviceId`,prod.`remId`,prod.`licId`,prod.`advId`,prod.`delete_flag`,prod.`licNumber`,prod.`validity`,prod.`created`,prod.`updated`,prod.`created_by`,prod.`updated_by`,'prod'
	// VALUE FROM `advisor` adv LEFT JOIN `advproduct` prod ON (adv.`advId` =
	// prod.`advId`)\r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// promo.`promotionId`,promo.`title`,promo.`video`,promo.`imagePath`,promo.`aboutVideo`,'',promo.`advId`,promo.`delete_flag`,'','','','','','','promo'
	// VALUE FROM `advisor` adv LEFT JOIN `promotion` promo ON (adv.`advId` =
	// promo.`advId`) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// brandinfo.`advBrandId`,brandinfo.`prodId`,brandinfo.`serviceId`,brandinfo.`brandId`,brandinfo.`priority`,'',brandinfo.`advId`,brandinfo.`delete_flag`,'','','','','','',
	// 'brandinfo' VALUE FROM `advisor` adv LEFT JOIN `advbrandinfo` brandinfo ON
	// (adv.`advId` = brandinfo.`advId`) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// brandrank.`advBrandRankId`,brandrank.`prodId`,brandrank.`brandId`,brandrank.`ranking`,'','',brandrank.`advId`,brandrank.`delete_flag`,'','','','','','',
	// 'brandrank'VALUE FROM `advisor` adv LEFT JOIN `advbrandrank` brandrank ON
	// (adv.`advId` = brandrank.`advId`)) AS `advisor` WHERE `advId` IN (%1$s) AND
	// `delete_flag` = ?";
	//
	// String inSql = String.join("','", advIds);
	// String str = "'" + inSql + "'";
	// String sqlAdv = String.format(sql, str);
	// return jdbcTemplate.query(sqlAdv, new AdvisorListExtractor(deleteflag),
	// encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag);
	// }
	//
	// @Override
	// public List<Investor> fetchInvestorsByInvIds(List<String> invIds, String
	// deleteflag, String encryptPass) {
	// try {
	// String sql = "SELECT * FROM (SELECT inv.`invId`
	// invId,DECODE(inv.`fullName`,?) fullName,inv.`displayName`
	// displayName,DECODE(inv.`dob`,?) dob,DECODE(inv.`emailId`,?)
	// emailId,inv.`gender` gender,inv.`password` password,DECODE(inv.`userName`,?)
	// userName,DECODE(inv.`phoneNumber`,?) phoneNumber,inv.`pincode`
	// pincode,inv.`imagePath` imagePath,inv.`partyStatusId`
	// partyStatusId,inv.`created` created,inv.`updated` updated,inv.`isVerified`
	// isVerified,inv.`verifiedBy` verifiedBy,inv.`verified`
	// verified,inv.`delete_flag` delete_flag,invInt.INTERESTID
	// COL_A,invInt.CATEGORYID COL_B,invInt.INVID COL_INVID,invInt.SCALE
	// COL_D,invInt.CREATED COL_E,invInt.UPDATED COL_F,invInt.DELETE_FLAG
	// COL_DELETEFLAG, 'invInt' VALUE FROM `investor` inv LEFT JOIN `invinterest`
	// invInt ON (inv.`invId` = invInt.`invId`)) AS `investor` WHERE `invId` IN
	// (%1$s) AND `delete_flag`=?";
	// String inSql = String.join("','", invIds);
	// String str = "'" + inSql + "'";
	// String sqlInv = String.format(sql, str);
	// return jdbcTemplate.query(sqlInv, new InvestorListExtractor(deleteflag),
	// encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag);
	// } catch (DataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public long fetchFollowerStatusIdByDesc(String status) {
		try {
			String sqlFollowerStatus = "SELECT `followerStatusId` FROM `followerstatus` WHERE `status`=?";
			Long followerStatusId = jdbcTemplate.queryForObject(sqlFollowerStatus, Long.class, status);
			return followerStatusId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public long fetchUserTypeIdByDesc(String user_type_advisor) {
		try {
			String sqlUserType = "SELECT `id` FROM `usertype` WHERE `desc`=?";
			Long userTypeId = jdbcTemplate.queryForObject(sqlUserType, Long.class, user_type_advisor);
			return userTypeId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<String> fetchActiveFollowersListByUserId(String userId, long statusId) {
		try {
			String sql = "SELECT `advId` FROM `followers` WHERE `userId`=? AND `status`=?";
			List<String> result = jdbcTemplate.queryForList(sql, String.class, userId, statusId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addOtpForPhoneNumber(String phoneNumber, String otp) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `generated_otp` (`phoneNumber`,`otp`,`created`) values (?,?,?)";
			int result = jdbcTemplate.update(sql, phoneNumber, otp, timestamp);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchOtpByPhoneNumber(String phoneNumber) {
		try {
			String sql1 = "SELECT `otp` FROM `generated_otp` WHERE `phoneNumber` = ? ORDER BY `created` DESC LIMIT 1";
			String savedOtp = jdbcTemplate.queryForObject(sql1, String.class, phoneNumber);
			return savedOtp;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Followers fetchFollowersByUserIdWithAdvId(String advId, String userId) {
		try {
			String sql = "SELECT `followersId`,`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by` FROM `followers` WHERE `advId` = ? AND `userId`=?";
			RowMapper<Followers> rowMapper = new BeanPropertyRowMapper<Followers>(Followers.class);
			Followers followers = jdbcTemplate.queryForObject(sql, rowMapper, advId, userId);
			return followers;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public GeneratedOtp fetchGeneratedOtp(String phoneNumber, String otp) {
		try {
			String sql = "SELECT `id`,`phoneNumber`,`otp`,`created` FROM `generated_otp` WHERE `phoneNumber` = ? AND `otp`=? ORDER BY `created` DESC LIMIT 1";
			RowMapper<GeneratedOtp> rowMapper = new BeanPropertyRowMapper<GeneratedOtp>(GeneratedOtp.class);
			GeneratedOtp generatedOtp = jdbcTemplate.queryForObject(sql, rowMapper, phoneNumber, otp);
			return generatedOtp;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	// @Override
	// public List<Award> fetchAwardByadvId(long advId) {
	// try {
	// String sqlAward = "SELECT *,`advId` FROM `award` WHERE `advId`=?";
	// RowMapper<Award> awardMapper = new
	// BeanPropertyRowMapper<Award>(Award.class);
	// return jdbcTemplate.query(sqlAward, awardMapper, advId);
	// } catch (EmptyResultDataAccessException e) {
	// System.out.println(e);
	// return null;
	// }
	// }
	//
	// @Override
	// public List<Education> fetchEducationByadvId(long advid) {
	// try {
	// String sqlEducation = "SELECT * FROM `education` WHERE `advId`=?";
	// RowMapper<Education> educationMapper = new
	// BeanPropertyRowMapper<Education>(Education.class);
	// return jdbcTemplate.query(sqlEducation, educationMapper, advid);
	// } catch (EmptyResultDataAccessException e) {
	// System.out.println(e);
	// return null;
	// }
	// }
	//
	// @Override
	// public List<Experience> fetchExperienceByadvId(long advid) {
	// try {
	// String sqlExperience = "SELECT * FROM `experience` WHERE `advId`=?";
	// RowMapper<Experience> experienceMapper = new
	// BeanPropertyRowMapper<Experience>(Experience.class);
	// return jdbcTemplate.query(sqlExperience, experienceMapper, advid);
	// } catch (EmptyResultDataAccessException e) {
	// System.out.println(e);
	// return null;
	// }
	// }

	@Override
	public List<FollowerStatus> fetchFollowerStatusList() {
		try {
			String sql = "SELECT `followerStatusId`, `status` FROM `followerstatus`";
			RowMapper<FollowerStatus> rowMapper = new BeanPropertyRowMapper<FollowerStatus>(FollowerStatus.class);
			List<FollowerStatus> followerstatus = jdbcTemplate.query(sql, rowMapper);
			return followerstatus;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<WorkFlowStatus> fetchWorkFlowStatusList() {
		try {
			String sql = "SELECT `workflowId`, `status` FROM `workflowstatus`";
			RowMapper<WorkFlowStatus> rowMapper = new BeanPropertyRowMapper<WorkFlowStatus>(WorkFlowStatus.class);
			List<WorkFlowStatus> workFlowStatus = jdbcTemplate.query(sql, rowMapper);
			return workFlowStatus;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorList(Pageable pageable, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String deleteflag, String encryptPass,
			String signedUserId) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT  "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ?  AND advId != ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY DECODE(name,?) ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =? ORDER BY name ASC";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, signedUserId, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, signedUserId,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, signedUserId, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, signedUserId, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, signedUserId, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					signedUserId, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, signedUserId, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, signedUserId, encryptPass, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Product> fetchExploreProductList(String productName, String serviceName, String brandName) {
		try {
			return jdbcTemplate.query(
					"SELECT * FROM (SELECT prod.*, serv.serviceId COL_A, serv.service COL_B, serv.prodId COL_C, 1 IS_SERVICE FROM product prod LEFT JOIN service serv ON (prod.prodId = serv.prodId) AND `service` LIKE '%"
							+ serviceName
							+ "%' UNION ALL SELECT prod.*, bran.brandId, bran.brand, bran.prodId, 0 IS_SERVICE FROM product prod LEFT JOIN brand bran ON (prod.prodId = bran.prodId) And `brand` LIKE '%"
							+ brandName + "%') AS `product` WHERE `product` LIKE '%" + productName + "%'",
					new ProductExtractor());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ServicePlan> fetchServicePlanByServiceId(long serviceId) {
		try {
			String sql = "SELECT `servicePlanId`,`servicePlan`,`servicePlanLink`,`serviceId`,`prodId`,`brandId` FROM `serviceplan` WHERE `serviceId` = ?";
			RowMapper<ServicePlan> rowMapper = new BeanPropertyRowMapper<ServicePlan>(ServicePlan.class);
			return jdbcTemplate.query(sql, rowMapper, serviceId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorByProduct(Pageable pageable, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String productId, String serviceId, String brandId,
			String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ " ) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN (SELECT * FROM `public_advproduct` WHERE 1 = (CASE WHEN '" + serviceId
					+ "' IS NULL OR '" + serviceId
					+ "' = '' THEN 1 ELSE 0 END) AND `prodId`=? AND `delete_flag` = ?\r\n " + " UNION ALL\r\n"
					+ " SELECT * FROM `public_advproduct` WHERE 1 = (CASE WHEN '" + serviceId
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND `prodId` = ? AND (FIND_IN_SET ('" + serviceId
					+ "', `serviceId`)) AND `delete_flag` = ? UNION ALL SELECT * FROM `public_advproduct` WHERE 1= (CASE WHEN '"
					+ productId + "'='' AND '" + serviceId
					+ "'='' THEN 1 ELSE 0 END) AND `delete_flag` = ?) prod ON (adv.advId = prod.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN (SELECT * FROM `public_advbrandinfo` WHERE 1 = (CASE WHEN '" + brandId
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND `brandId`=? AND `delete_flag` = ? UNION ALL SELECT * FROM `public_advbrandinfo` WHERE 1= (CASE WHEN '"
					+ brandId
					+ "'='' THEN 1 ELSE 0 END) AND `delete_flag` = ?) brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, productId, deleteflag, productId, deleteflag,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, brandId, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updatePersonalInfoInParty(String emailId, String phoneNumber, String panNumber, String userName,
			String advId, String encryptPass, String signedUserId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `party` SET `emailId`=ENCODE(?,?),`phoneNumber`=ENCODE(?,?), `panNumber`=ENCODE(?,?), `userName`=ENCODE(?,?), `updated`=?,`updated_by`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sql, emailId, encryptPass, phoneNumber, encryptPass, panNumber,
					encryptPass, userName, encryptPass, timestamp, signedUserId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchSearchAdvisorList(Pageable pageable, String panNumber, String emailId, String phoneNumber,
			String userName, String deleteflag, String workFlowStatusId, String encryptPass, String advType) {
		try {

			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM advisor WHERE delete_flag =? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ " ) adv LEFT JOIN experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM advisor WHERE delete_flag =? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advproduct prod ON (adv.advId = prod.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM advisor WHERE delete_flag = ? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%' ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<UserType> fetchUserTypeList() {
		try {
			String sql = "SELECT `id`, `desc` FROM `usertype`";
			RowMapper<UserType> rowMapper = new BeanPropertyRowMapper<UserType>(UserType.class);
			List<UserType> userType = jdbcTemplate.query(sql, rowMapper);
			return userType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<AdvisorType> fetchAdvisorTypeList() {
		try {
			String sql = "SELECT `id`, `advType` FROM `advisortype`";
			RowMapper<AdvisorType> rowMapper = new BeanPropertyRowMapper<AdvisorType>(AdvisorType.class);
			List<AdvisorType> advisorType = jdbcTemplate.query(sql, rowMapper);
			return advisorType;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateFollowers(long followersId, long statusId, String blockedBy, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `followers` SET `status`=?,`updated`=?,`updated_by`=?,`byWhom`=? WHERE `followersId`=?";
			int result = jdbcTemplate.update(sql, statusId, timestamp, signedUserId, blockedBy, followersId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ChatUser> fetchChatUserListByUserId(String userId) {
		try {
			String sql = "SELECT `chatUserId`,`advId`,`userId`,`userType`,`status`,`created`,`created_by`,`updated`,`updated_by`,`byWhom` FROM `chatuser` WHERE `userId` = ?";
			RowMapper<ChatUser> rowMapper = new BeanPropertyRowMapper<ChatUser>(ChatUser.class);
			return jdbcTemplate.query(sql, rowMapper, userId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addChatUser(ChatUser chatUser) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `chatuser` (`advId`,`userId`,`userType`,`status`,`created`,`created_by`,`updated`,`updated_by`,`byWhom`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, chatUser.getAdvId(), chatUser.getUserId(), chatUser.getUserType(),
					chatUser.getStatus(), timestamp, chatUser.getCreatedBy(), timestamp, chatUser.getUpdatedBy(),
					chatUser.getUserId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ChatUser fetchChatUserByUserIdWithAdvId(String advId, String userId, long statusId) {
		try {
			String sql = "SELECT `chatUserId`,`advId`,`userId`,`userType`,`status`,`created`,`created_by`,`updated`,`updated_by`,`byWhom` FROM `chatuser` WHERE `advId` = ? AND `userId`=? AND `status` = ?";
			RowMapper<ChatUser> rowMapper = new BeanPropertyRowMapper<ChatUser>(ChatUser.class);
			ChatUser chatUser = jdbcTemplate.queryForObject(sql, rowMapper, advId, userId, statusId);
			return chatUser;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int modifyChat(long chatUserId, long statusId, String blockedBy, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `chatuser` SET `status`=?,`updated`=?,`updated_by`=?,`byWhom`=? WHERE `chatUserId`=?";
			int result = jdbcTemplate.update(sql, statusId, timestamp, signedUserId, blockedBy, chatUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Followers fetchFollowersByUserIdWithAdvId(String advId, String userId, long statusId) {
		try {
			String sql = "SELECT `followersId`,`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by` FROM `followers` WHERE `advId` = ? AND `userId`=? AND `status` =?";
			RowMapper<Followers> rowMapper = new BeanPropertyRowMapper<Followers>(Followers.class);
			Followers followers = jdbcTemplate.queryForObject(sql, rowMapper, advId, userId, statusId);
			return followers;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateChatUser(long chatUserId, long statusId, String userId, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `chatuser` SET `status`=?,`updated`=?,`updated_by`=?,`byWhom`=? WHERE `chatUserId`=?";
			int result = jdbcTemplate.update(sql, statusId, timestamp, signedUserId, userId, chatUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchAdvisorTotalList(String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `advisor` WHERE delete_flag =?";
			int advList = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);
			return advList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchAllTotalPublicAdvisor(String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `public_advisor` WHERE delete_flag =?";
			int totalRecords = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);
			return totalRecords;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorList(String sortByState, String sortByCity, String sortByPincode,
			String sortByDisplayName, String deleteflag, String signedUserId) {
		try {
			String sql = "SELECT COUNT(*) FROM `public_advisor` WHERE `delete_flag` =? AND `advId` != ?  AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%'";
			int totalRecords = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag, signedUserId);
			return totalRecords;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	// @Override
	// public List<Advisor> fetchTotalExploreAdvisorList(String sortByState, String
	// sortByCity, String sortByPincode,
	// String sortByDisplayName, String deleteflag, String encryptPass) {
	// try {
	// String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId
	// COL_A, exp.company COL_B, exp.designation COL_C,exp.location
	// COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag
	// COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp'
	// VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? AND `state`
	// LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','',
	// 'edu' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND
	// `state` LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// awd.awardId,
	// awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd'
	// VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `state`
	// LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// cert.certificateId,
	// cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,''
	// ,'','','','','','cert' VALUE FROM (SELECT * FROM public_advisor WHERE
	// delete_flag =? AND `state` LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId)
	// \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// prod.advProdId,prod.licImage,
	// prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod'
	// VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `state`
	// LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo'
	// VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `state`
	// LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId)
	// \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','',
	// 'brandinfo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ?
	// AND `state` LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId =
	// brandinfo.advId) \r\n"
	// + "UNION ALL SELECT adv.`advId` advId,adv.`advType`
	// advType,DECODE(adv.`name`,?) name,adv.`designation`
	// designation,DECODE(adv.`emailId`,?) emailId,adv.`password`
	// password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?)
	// phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId`
	// partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by`
	// created_by,adv.`updated_by` updated_by,adv.`displayName`
	// displayName,DECODE(adv.`dob`,?) dob,adv.`gender`
	// gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1`
	// address1,adv.`address2` address2,adv.`state` state,adv.`city`
	// city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType`
	// firmType,adv.`corporateLable` corporateLable,adv.`website`
	// website,adv.`parentPartyId` parentPartyId,adv.`imagePath`
	// imagePath,adv.`isVerified` isVerified,adv.`verifiedBy`
	// verifiedBy,adv.`verified` verified,adv.`workFlowStatus`
	// workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy`
	// approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy,
	// brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','',
	// 'brandrank'VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND
	// `state` LIKE '%"
	// + sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE
	// '%" + sortByPincode
	// + "%' AND `displayName` LIKE '%" + sortByDisplayName
	// + "%') adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId =
	// brandrank.advId)) AS advisorlist WHERE delete_flag =?";
	// return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag),
	// encryptPass, encryptPass, encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
	// encryptPass,
	// encryptPass, encryptPass, encryptPass, deleteflag, deleteflag);
	//
	// } catch (DataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public int fetchTotalExploreAdvisorByProduct(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId, String serviceId, String brandId, String deleteflag) {
		try {
			String sqlAdv = "";
			if (stateCityPincodeList.size() > 0) {
				String sql = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag = ? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =?) UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag = ? AND pub_brandinfo.`prodId` = '"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "')) AS advisorlist WHERE `pincode` IN (%1$s)";
				String inSql = String.join("','", stateCityPincodeList);
				String str = "'" + inSql + "'";
				sqlAdv = String.format(sql, str, str);
			} else {
				sqlAdv = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag = ? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =?) UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag = ? AND pub_brandinfo.`prodId` = '"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "')) AS advisorlist";
			}
			return jdbcTemplate.queryForObject(sqlAdv, Integer.class, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalSearchAdvisorList(String panNumber, String emailId, String phoneNumber, String userName,
			String deleteflag, String workFlowStatusId, String encryptPass, String advType) {
		try {
			String sql = "SELECT COUNT(*) FROM `advisor` WHERE delete_flag =? AND DECODE(`panNumber`,?) LIKE '%"
					+ panNumber + "%' AND DECODE(`emailId`,?) LIKE '%" + emailId
					+ "%' AND DECODE(`phoneNumber`,?) LIKE '%" + phoneNumber + "%' AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND `advType` LIKE '%" + advType + "%'";

			int count = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag, encryptPass, encryptPass,
					encryptPass);
			return count;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Product> searchProductList(String productId, String serviceId, String servicePlanId) {
		try {
			return jdbcTemplate.query(
					"SELECT * FROM (SELECT prod.*, serv.serviceId COL_A, serv.service COL_B, serv.prodId COL_C, 1 IS_SERVICE FROM product prod LEFT JOIN service serv ON (prod.prodId = serv.prodId) AND `serviceId` LIKE '%"
							+ serviceId
							+ "%' UNION ALL SELECT prod.*, bran.brandId, bran.brand, bran.prodId, 0 IS_SERVICE FROM product prod LEFT JOIN brand bran ON (prod.prodId = bran.prodId)) AS `product` WHERE `prodId` LIKE '%"
							+ productId + "%'",
					new ProductExtractor());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchProductNameByProdId(int prodId) {
		try {
			String sql = "SELECT `product` FROM `product` WHERE `prodId` = ?";
			String productName = jdbcTemplate.queryForObject(sql, String.class, prodId);
			return productName;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public ServicePlan fetchServicePlanByServicePlanId(String servicePlanId) {
		try {
			String sql = "SELECT `servicePlanId`,`servicePlan`,`serviceId` FROM `serviceplan` WHERE `servicePlanId` = ?";
			RowMapper<ServicePlan> rowMapper = new BeanPropertyRowMapper<ServicePlan>(ServicePlan.class);
			ServicePlan ServicePlans = jdbcTemplate.queryForObject(sql, rowMapper, servicePlanId);
			return ServicePlans;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchServiceNameByProdIdAndServiceId(int prodId, int serviceId) {
		try {
			String sql = "SELECT `service` FROM `service` WHERE `prodId` = ? AND `serviceId` = ?";
			String productName = jdbcTemplate.queryForObject(sql, String.class, prodId, serviceId);
			return productName;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchProductByProductName(String productName) {
		try {
			String sql = "SELECT `prodId` FROM `product` WHERE `product`=?";
			long productId = jdbcTemplate.queryForObject(sql, Long.class, productName);
			return productId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchBrandNameByProdIdAndBrandId(int prodId, int brandId) {
		try {
			String sql = "SELECT `brand` FROM `brand` WHERE `prodId` = ? AND `brandId` = ?";
			String productName = jdbcTemplate.queryForObject(sql, String.class, prodId, brandId);
			return productName;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchServiceByServiceName(String serviceName, long productId) {
		try {
			String sql = "SELECT `serviceId` FROM `service` WHERE `service`=? AND `prodId`=?";
			long serviceId = jdbcTemplate.queryForObject(sql, Long.class, serviceName, productId);
			return serviceId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ServicePlan fetchServicePlan(int prodId, int serviceId, int brandId, String planName) {
		try {
			String sql = "SELECT `servicePlanId`,`prodId`,`serviceId`,`brandId`,`servicePlan`,`servicePlanLink` FROM `serviceplan` WHERE `prodId` = ? AND `serviceId`=? AND `brandId` =? AND `servicePlan`=?";
			RowMapper<ServicePlan> rowMapper = new BeanPropertyRowMapper<ServicePlan>(ServicePlan.class);
			ServicePlan servicePlan = jdbcTemplate.queryForObject(sql, rowMapper, prodId, serviceId, brandId, planName);
			return servicePlan;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchBrandByBrandName(String brandName, long productId) {
		try {
			String sql = "SELECT `brandId` FROM `brand` WHERE `brand`=? AND `prodId`=?";
			long serviceId = jdbcTemplate.queryForObject(sql, Long.class, brandName, productId);
			return serviceId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addServicePlan(int prodId, int serviceId, int brandId, String planName, String url,
			String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `serviceplan` (`prodId`,`serviceId`,`brandId`,`servicePlan`,`servicePlanLink`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, prodId, serviceId, brandId, planName, url, timestamp, timestamp,
					signedUserId, signedUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public long fetchServicePlanIdByName(String servicePlanName, long productId, long serviceId, long brandId) {
		try {
			String sql = "SELECT `servicePlanId` FROM `serviceplan` WHERE `servicePlan`=? AND `prodId`=? AND `serviceId`=? AND `brandId`=?";
			long servicePlanId = jdbcTemplate.queryForObject(sql, Long.class, servicePlanName, productId, serviceId,
					brandId);
			return servicePlanId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateServicePlan(int prodId, int serviceId, int brandId, String planName, String url,
			String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `serviceplan` SET `servicePlanLink`=?,`updated`=?,`updated_by`=? WHERE `prodId`=? AND `serviceId`=? AND `brandId`=? AND `servicePlan`=?";
			int result = jdbcTemplate.update(sql, url, timestamp, signedUserId, prodId, serviceId, brandId, planName);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ServicePlan> fetchServicePlanByProdIdAndServiceIdAndBrandIdAndServicePlanId(long productId,
			long serviceId, long brandId, long servicePlanId) {
		try {
			String sql = "SELECT `servicePlanId`,`servicePlan`,`servicePlanLink`,`serviceId`,`prodId`,`brandId`,`created`,`updated`,`created_by`,`updated_by` FROM `serviceplan` WHERE 1 = (CASE WHEN "
					+ servicePlanId
					+ " = 0 THEN 1 ELSE 0 END) AND `prodId` = ? AND `serviceId` = ? AND `brandId` = ? UNION ALL SELECT * FROM serviceplan WHERE 1 = (CASE WHEN "
					+ servicePlanId
					+ " > 0 THEN 1 ELSE 0 END) AND `prodId` =? AND `serviceId`=? AND `brandId`=? AND `servicePlanId`=?";
			RowMapper<ServicePlan> rowMapper = new BeanPropertyRowMapper<ServicePlan>(ServicePlan.class);
			return jdbcTemplate.query(sql, rowMapper, productId, serviceId, brandId, productId, serviceId, brandId,
					servicePlanId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchListAdvIdByProdIdServiceIdBrandId(String productId, String serviceId, String brandId,
			String deleteflag) {
		try {
			String sql = "SELECT DISTINCT advId FROM (\r\n"
					+ "SELECT adv.advId FROM `advisor` adv INNER JOIN advproduct prod ON (adv.`advId` = prod.`advId` AND adv.delete_flag=? AND prod.delete_flag=? AND prod.prodId=? AND FIND_IN_SET(?, `serviceId`))"
					+ "UNION ALL SELECT adv.advId FROM `advisor` adv INNER JOIN `advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId` AND adv.`delete_flag` = ? AND brandinfo.delete_flag=? AND brandinfo.`brandId` = ? )\r\n"
					+ ") AS `advisor`";
			return jdbcTemplate.queryForList(sql, String.class, deleteflag, deleteflag, productId, serviceId,
					deleteflag, deleteflag, brandId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchCityByPincode(String sortByPincode) {
		try {
			String sql = "SELECT `city` FROM `city` WHERE `pincode`=?";
			String city = jdbcTemplate.queryForObject(sql, String.class, sortByPincode);
			return city;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchPincodesByCity(String city) {
		try {
			String sql = "SELECT `pincode` FROM `city` WHERE `city`=?";
			List<String> pincodes = jdbcTemplate.queryForList(sql, String.class, city);
			return pincodes;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchDistrictByPincode(String sortByPincode) {
		try {
			String sql = "SELECT `district` FROM `city` WHERE `pincode`=?";
			String city = jdbcTemplate.queryForObject(sql, String.class, sortByPincode);
			return city;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchPincodesByDistrict(String district) {
		try {
			String sql = "SELECT `pincode` FROM `city` WHERE `district`=?";
			List<String> pincodes = jdbcTemplate.queryForList(sql, String.class, district);
			return pincodes;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchStateIdByState(String sortByState) {
		try {
			String sql = "SELECT `stateId` FROM `state` WHERE `state`=?";
			long stateId = jdbcTemplate.queryForObject(sql, Long.class, sortByState);
			return stateId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<String> fetchPincodeByState(long stateId) {
		try {
			String sql = "SELECT `pincode` FROM `city` WHERE `stateId`=?";
			List<String> pincodes = jdbcTemplate.queryForList(sql, String.class, stateId);
			return pincodes;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchPincodeByCity(String sortByCity) {
		try {
			String sql = "SELECT `pincode` FROM `city` WHERE `city`=?";
			List<String> pincodes = jdbcTemplate.queryForList(sql, String.class, sortByCity);
			return pincodes;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchDistrictByCity(String sortByCity) {
		try {
			String sql = "SELECT DISTINCT `district` FROM `city` WHERE `city`=?";
			String district = jdbcTemplate.queryForObject(sql, String.class, sortByCity);
			return district;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public long fetchStateIdByPincode(String pincode) {
		try {
			String sql = "SELECT `stateId` FROM `city` WHERE `pincode`=?";
			long stateId = jdbcTemplate.queryForObject(sql, Long.class, pincode);
			return stateId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<String> fetchPincodeByStateId(long stateId) {
		try {
			String sql = "SELECT `pincode` FROM `city` WHERE `stateId`=?";
			List<String> pincodes = jdbcTemplate.queryForList(sql, String.class, stateId);
			return pincodes;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorListByProduct(Pageable pageable, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId, String brandId, String deleteflag,
			String encryptPass) {
		try {
			String sqlAdv = "";
			if (stateCityPincodeList.size() > 0) {
				String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT	"
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =? AND `pincode` IN (%1$s) ORDER BY FIELD(`pincode`,%2$s)";
				String inSql = String.join("','", stateCityPincodeList);
				String str = "'" + inSql + "'";
				// System.out.println("str===============" + str);
				sqlAdv = String.format(sql, str, str);
			} else {
				sqlAdv = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT	"
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
						+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId
						+ "' AND pub_brandinfo.`brandId` = '" + brandId + "') WHERE 1 = (CASE WHEN '"
						+ sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			}
			// System.out.println("sqlAdv===============" + sqlAdv);
			return jdbcTemplate.query(sqlAdv, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass,
					deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag,
					encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag,
					deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag,
					deleteflag, encryptPass, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorListByProductWithoutBrand(Pageable pageable,
			List<String> stateCityPincodeList, String sortByDisplayName, String productId, String serviceId,
			String deleteflag, String encryptPass) {
		try {
			String sqlAdv = "";
			if (stateCityPincodeList.size() > 0) {
				String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT	"
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =? AND `pincode` IN (%1$s) ORDER BY FIELD(`pincode`,%2$s)";
				String inSql = String.join("','", stateCityPincodeList);
				String str = "'" + inSql + "'";
				sqlAdv = String.format(sql, str, str);
			} else {
				sqlAdv = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT	"
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			}
			return jdbcTemplate.query(sqlAdv, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass,
					deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag,
					encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag,
					deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag,
					deleteflag, encryptPass, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProductWithoutBrandId(List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String serviceId, String deleteflag) {
		try {
			String sqlAdv = "";
			if (stateCityPincodeList.size() > 0) {
				String sql = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag= ? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ?) UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag = ? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "')) AS advisorlist WHERE `pincode` IN (%1$s)";

				String inSql = String.join("','", stateCityPincodeList);
				String str = "'" + inSql + "'";
				sqlAdv = String.format(sql, str, str);
			} else {
				sqlAdv = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag= ? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ?) UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag = ? AND pub_advproduct.`prodId` ='"
						+ productId + "' AND (FIND_IN_SET ('" + serviceId
						+ "', pub_advproduct.`serviceId`))) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "')) AS advisorlist";
			}
			return jdbcTemplate.queryForObject(sqlAdv, Integer.class, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<CityList> searchStateCityPincodeByCity(String cityName) {
		String sql = "SELECT * FROM (SELECT st.*,ct.cityId COL_A,ct.stateId COL_B,ct.city COL_C,ct.pincode COL_D, 1 IS_CITY FROM state st LEFT JOIN city ct ON (st.stateId = ct.stateId)) AS stateList WHERE COL_C LIKE '"
				+ cityName + "%' ORDER BY CHAR_LENGTH(COL_C),COL_C ASC";
		List<CityList> cityList = jdbcTemplate.query(sql, new CityExtractor());
		return cityList;
	}

	@Override
	public int saveChatMessage(ChatMessage chatMessage) {
		try {
			String sql = "INSERT INTO `chatmessage` (`senderId`,`recipientId`,`senderName`,`recipientName`,`content`,`created`) VALUES (?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, chatMessage.getSenderId(), chatMessage.getRecipientId(),
					chatMessage.getSenderName(), chatMessage.getRecipientName(), chatMessage.getContent(),
					chatMessage.getCreated());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkAdvisorIsPresent(String advId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `advisor` WHERE `advId`= ? AND `delete_flag` = ?";
			int advisor = jdbcTemplate.queryForObject(sql, Integer.class, advId, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkAdvProductIsPresent(String advId, long advProdId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `advproduct` WHERE `advId` = ? AND `advProdId`=? AND `delete_flag`=?";
			int advProduct = jdbcTemplate.queryForObject(sql, Integer.class, advId, advProdId, deleteflag);
			return advProduct;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkAdvBrandRankIsPresent(String advId, long prodId, int rank, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `advbrandrank` WHERE `ranking`= ? AND `advId`=? AND `prodId`=? AND `delete_flag`=?";
			int advBrandRank = jdbcTemplate.queryForObject(sql, Integer.class, rank, advId, prodId, deleteflag);
			return advBrandRank;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	@Override
	public int checkKeyPeopleIsPresent(long keyPeopleId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `keypeople` WHERE `delete_flag`=? AND `keyPeopleId`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag, keyPeopleId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkPartyIsPresent(long partyId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `party` WHERE `partyId` = ? AND `delete_flag` =?";
			return jdbcTemplate.queryForObject(sql, Integer.class, partyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkChatUserIsPresent(String userId, long statusId) {
		try {
			String sql = "SELECT count(*) FROM `chatuser` WHERE `userId` = ? AND `status` = ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, userId, statusId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorDESCListOrder(Pageable pageable, String sortByState, String sortByCity,
			String sortByPincode, String sortByDisplayName, String deleteflag, String encryptPass,
			String signedUserId) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? AND `advId`!= ?  AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT  "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM public_advisor  WHERE delete_flag = ? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag =? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM (SELECT * FROM public_advisor WHERE delete_flag = ? AND `advId`!= ? AND `state` LIKE '%"
					+ sortByState + "%' AND `city` LIKE '%" + sortByCity + "%' AND `pincode` LIKE '%" + sortByPincode
					+ "%' AND `displayName` LIKE '%" + sortByDisplayName + "%' ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =? ORDER BY created DESC";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, signedUserId, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, signedUserId, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, signedUserId,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					signedUserId, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, signedUserId, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, signedUserId, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, signedUserId, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, signedUserId, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Advisor fetchAdvisorByUserNameWithOutToken(String userName, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM public_advisor adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM public_advisor adv LEFT JOIN public_education edu ON (adv.advId = edu.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM public_advisor adv LEFT JOIN public_award awd ON (adv.advId = awd.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM public_advisor adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM public_advisor adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','', 'promo' VALUE FROM public_advisor adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM public_advisor adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM public_advisor adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisor WHERE userName = ? AND delete_flag = ?";

			return jdbcTemplate.query(sql, new AdvisorByUserNameExtractor(deleteflag), encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, userName, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Party fetchPartyByPhoneNumberAndDeleteFlag(String phoneNumber, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE DECODE(`phoneNumber`,?)=? AND `delete_flag`=?";
			RowMapper<Party> partyMapper = new BeanPropertyRowMapper<Party>(Party.class);
			return jdbcTemplate.queryForObject(sql, partyMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, phoneNumber, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int deleteAdvisorChild(String advId) {
		try {
			String sql = "DELETE awd,cert,expr,edu,aprod,abrand,brandrank,promo,party FROM `advisor` adv\r\n"
					+ "LEFT JOIN `award` awd ON (adv.advId = awd.advId)\r\n"
					+ "LEFT JOIN `certificate` cert ON (adv.advId = cert.advId)\r\n"
					+ "LEFT JOIN `experience` expr ON (adv.advId = expr.advId)\r\n"
					+ "LEFT JOIN `education` edu ON (adv.advId = edu.advId)\r\n"
					+ "LEFT JOIN `advproduct` aprod ON (adv.advId = aprod.advId)\r\n"
					+ "LEFT JOIN `advbrandinfo` abrand ON (adv.advId = abrand.advId)\r\n"
					+ "LEFT JOIN `advbrandrank` brandrank ON (adv.advId = brandrank.advId)\r\n"
					+ "LEFT JOIN `promotion` promo ON (adv.advId = promo.advId)\r\n"
					+ "LEFT JOIN `party` party ON (adv.advId = party.roleBasedId)" + "WHERE adv.`advId` =?";
			int result = jdbcTemplate.update(sql, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int deleteAdvisor(String advId) {
		try {
			String sqlUpdate = "DELETE FROM `advisor` WHERE `advId` =?";
			int result = jdbcTemplate.update(sqlUpdate, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ChatUser> fetchChatUserListByAdvId(String advId) {
		try {
			String sql = "SELECT `chatUserId`,`advId`,`userId`,`userType`,`status`,`created`,`created_by`,`updated`,`updated_by`,`byWhom` FROM `chatuser` WHERE `advId`=?";
			RowMapper<ChatUser> rowMapper = new BeanPropertyRowMapper<ChatUser>(ChatUser.class);
			return jdbcTemplate.query(sql, rowMapper, advId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public ChatUser fetchChatUser(String advId, String userId) {
		try {
			String sql = "SELECT `chatUserId`,`advId`,`userId`,`userType`,`status`,`created`,`created_by`,`updated`,`updated_by`,`byWhom` FROM `chatuser` WHERE `advId` = ? AND `userId`=?";
			RowMapper<ChatUser> rowMapper = new BeanPropertyRowMapper<ChatUser>(ChatUser.class);
			ChatUser chatUser = jdbcTemplate.queryForObject(sql, rowMapper, advId, userId);
			return chatUser;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Integer> fetchChatUserCount(String advId, long statusId) {
		try {
			String sql = "SELECT COUNT(*) FROM `chatuser` WHERE `advId`=? AND `status`=? GROUP BY `userType`";
			List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, advId, statusId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public int checkFollowersIsPresent(String userId, String advId, long statusId) {
		try {
			String sql = "SELECT count(*) FROM `followers` WHERE `userId` = ? AND `advId` = ? AND `status` = ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, userId, advId, statusId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkChatUserIsPresent(String userId, String advId, long statusId) {
		try {
			String sql = "SELECT count(*) FROM `chatuser` WHERE `userId` = ? AND `advId`=? AND `status` = ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, userId, advId, statusId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Followers> fetchFollowersListByUserId(String userId) {
		try {
			String sql = "SELECT `followersId`,`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by` FROM `followers` WHERE `userId` = ?";
			RowMapper<Followers> rowMapper = new BeanPropertyRowMapper<Followers>(Followers.class);
			return jdbcTemplate.query(sql, rowMapper, userId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Followers> fetchFollowers(String advId) {
		try {
			String sql = "SELECT `followersId`,`userId`,`userType`,`status`,`advId`,`created`,`created_by`,`updated`,`updated_by` FROM `followers` WHERE `advId` = ?";
			RowMapper<Followers> rowMapper = new BeanPropertyRowMapper<Followers>(Followers.class);
			return jdbcTemplate.query(sql, rowMapper, advId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public long fetchFollowersStatus(String statusId) {
		try {
			String sql = "SELECT `followerStatusId` FROM `followerstatus` WHERE `status`= ?";
			int roleId = jdbcTemplate.queryForObject(sql, Integer.class, statusId);
			return roleId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override

	public int fetchFollowersCountByUserId(String userId, long statusId) {
		try {
			String sql = "SELECT COUNT(*) FROM `followers` WHERE `userId` = ? AND `status` = ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, userId, statusId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchSharedPlanCountPartyId(long partyId, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `calcquery` WHERE `postedToPartyId` = ? AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, partyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchPlannedUserCountPartyId(long partyId) {
		try {
			String sql = "SELECT COUNT(*) FROM `plan` WHERE `partyId` = ? OR `parentPartyId`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, partyId, partyId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public List<Advisor> fetchExploreAdvisorListByProdId(Pageable pageable, List<String> stateCityPincodeList,
			String sortByDisplayName, String productId, String deleteflag, String encryptPass) {

		try {
			String sqlAdv = "";
			if (stateCityPincodeList.size() > 0) {
				String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT	"
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "' ) WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =? AND `pincode` IN (%1$s) ORDER BY FIELD(`pincode`,%2$s)";
				String inSql = String.join("','", stateCityPincodeList);
				String str = "'" + inSql + "'";
				sqlAdv = String.format(sql, str, str);
			} else {
				sqlAdv = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT	"
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"
						+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT "
						+ pageable.getPageSize() + " OFFSET " + pageable.getOffset()
						+ ") UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag=? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT " + pageable.getPageSize()
						+ " OFFSET " + pageable.getOffset()
						+ ")) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			}
			return jdbcTemplate.query(sqlAdv, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass,
					deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					deleteflag, deleteflag, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag,
					encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag,
					deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, encryptPass, deleteflag, deleteflag,
					deleteflag, encryptPass, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public int fetchTotalExploreAdvisorListByProdId(List<String> stateCityPincodeList, String sortByDisplayName,
			String productId, String deleteflag) {
		try {
			String sqlAdv = "";
			if (stateCityPincodeList.size() > 0) {
				String sql = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag= ? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ?) UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag = ? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "')) AS advisorlist WHERE `pincode` IN (%1$s)";
				String inSql = String.join("','", stateCityPincodeList);
				String str = "'" + inSql + "'";
				sqlAdv = String.format(sql, str, str);
			} else {
				sqlAdv = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag= ? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1=(CASE WHEN '" + sortByDisplayName
						+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ?) UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advproduct` pub_advproduct ON (pub_adv.`advId` = pub_advproduct.`advId` AND pub_adv.`delete_flag` = ? AND pub_advproduct.delete_flag = ? AND pub_advproduct.`prodId` ='"
						+ productId + "') WHERE 1 = (CASE WHEN '" + sortByDisplayName
						+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag = ? AND pub_adv.`displayName`= '"
						+ sortByDisplayName + "')) AS advisorlist";
			}
			return jdbcTemplate.queryForObject(sqlAdv, Integer.class, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public List<Advisor> fetchPublishTeamByParentPartyId(long parentPartyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM public_advisor adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM public_advisor adv LEFT JOIN public_education edu ON (adv.advId = edu.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM public_advisor adv LEFT JOIN public_award awd ON (adv.advId = awd.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM public_advisor adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM public_advisor adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM public_advisor adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM public_advisor adv LEFT JOIN public_advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM public_advisor adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisor WHERE parentPartyId = ? AND delete_flag = ?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, parentPartyId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateAdvisorMobileAsVerified(String roleBasedId, int accountVerified) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `advisor` SET `isMobileVerified`=?,`updated`=? WHERE `advId`=?";
			int result = jdbcTemplate.update(sql, accountVerified, timestamp, roleBasedId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateInvestorMobileAsVerified(String roleBasedId, int accountVerified) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `investor` SET `isMobileVerified`=?,`updated`=? WHERE `invId`=?";
			int result = jdbcTemplate.update(sql, accountVerified, timestamp, roleBasedId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateUsernameInParty(String advId, Advisor adv, String signedUserId, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `party` SET `userName`=ENCODE(?,?), `updated`=?,`updated_by`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sql, adv.getUserName(), encryptPass, timestamp, signedUserId, advId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalSearchAdvisorListWithEmptyValues(String pan_lc, String email_lc, String phone_lc,
			String userName_lc, String deleteflag, String workFlowStatusId, String encryptPass) {
		try {
			String sql = "SELECT COUNT(*) FROM advisor WHERE delete_flag =? AND workFlowStatus LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(panNumber,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(emailId,?) LIKE '%" + email_lc + "%' AND DECODE(phoneNumber,?) LIKE '%" + phone_lc
					+ "%'AND DECODE(userName,?) LIKE '%" + userName_lc + "%' OR DECODE(userName,?) IS NULL";

			int count = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass);
			return count;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchSearchAdvisorListWithEmptyValues(Pageable pageable, String pan_lc, String email_lc,
			String phone_lc, String userName_lc, String deleteflag, String workFlowStatusId, String encryptPass) {
		try {

			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM (SELECT * FROM advisor WHERE delete_flag =?  AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ " ) adv LEFT JOIN experience exp ON (adv.advId = exp.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN education edu ON (adv.advId = edu.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset() + ") adv LEFT JOIN award awd ON (adv.advId = awd.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM (SELECT * FROM advisor WHERE delete_flag =? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN certificate cert ON (adv.advId = cert.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advproduct prod ON (adv.advId = prod.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM (SELECT * FROM advisor WHERE delete_flag = ? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN promotion promo ON (adv.advId = promo.advId) \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advbrandinfo brandinfo ON (adv.advId = brandinfo.advId)  \r\n"
					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank'VALUE FROM (SELECT * FROM advisor  WHERE delete_flag = ? AND `workFlowStatus` LIKE '%"
					+ workFlowStatusId + "%' AND DECODE(`panNumber`,?) LIKE '%" + pan_lc
					+ "%' AND DECODE(`emailId`,?) LIKE '%" + email_lc + "%' AND DECODE(`phoneNumber`,?) LIKE '%"
					+ phone_lc + "%' AND DECODE(`userName`,?) LIKE '%" + userName_lc
					+ "%' OR DECODE(userName,?) IS NULL ORDER BY created ASC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset()
					+ ") adv LEFT JOIN advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Advisor fetchAdvisorGstByAdvId(String advId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name, DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`,`created_by`,`updated_by`,`displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme`, `firmType`, `parentPartyId`, `corporateLable`, `website`,`gst` FROM `advisor` WHERE `advId` = ? AND `delete_flag`=?";

			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, advId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchBrands(long brandId) {
		try {
			String sql = "SELECT count(*) FROM `brand` WHERE `brandId` = ?";
			int brands = jdbcTemplate.queryForObject(sql, Integer.class, brandId);
			return brands;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Party fetchParty(long partyId, String encryptPass) {
		try {
			String sql = "SELECT `partyId`,`partyStatusId`,`roleBasedId`,`parentPartyId`,DECODE(`emailId`,?) emailId,`password`,DECODE(`userName`,?) userName,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`panNumber`,?) panNumber,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `party` WHERE `partyId` = ?";
			RowMapper<Party> rowMapper = new BeanPropertyRowMapper<Party>(Party.class);
			Party party = jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, partyId);
			return party;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int createBrandsComment(BrandsComment comment, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `brandscomment` (`content`,`partyId`,`parentCommentId`,`name`,`designation`,`imagePath`,`created`,`paramId`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,ENCODE(?,?),?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, comment.getContent(), comment.getPartyId(),
					comment.getParentCommentId(), comment.getName(), encryptPass, comment.getDesignation(),
					comment.getImagePath(), timestamp, comment.getParamId(), timestamp, comment.getDelete_flag(),
					comment.getCreated_by(), comment.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeBrandsComment(long commentId, String delete_flag, String signedUserId) {
		try {
			String sql1 = "DELETE FROM `brandscomment` WHERE `commentId`=?";
			int result = jdbcTemplate.update(sql1, commentId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public List<BrandsComment> fetchBrandsCommentByParentId(String paramId, long
	// parentId, String delete_flag,
	// String encryptPass) {
	// try {
	// String sql = "SELECT `commentId`,
	// `parentCommentId`,`created`,`partyId`,DECODE(`name`,?)
	// name,`designation`,`imagePath`, `content`,`updated`,`paramId`,`adminId`,
	// `delete_flag`,`created_by`,`updated_by` FROM `brandscomment` WHERE `brandId`
	// = ? AND `parentCommentId` = ? AND `delete_flag`=? ORDER BY created ASC";
	// RowMapper<BrandsComment> rowMapper = new
	// BeanPropertyRowMapper<BrandsComment>(BrandsComment.class);
	// List<BrandsComment> brandsComment = jdbcTemplate.query(sql, rowMapper,
	// encryptPass, paramId, parentId,
	// delete_flag);
	// return brandsComment;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	public List<AdvBrandInfo> fetchExploreAdvisorList(long brandId, String deleteflag) {
		try {
			String sqlBrandInfo = "SELECT `advBrandId`, `advId`, `prodId`, `serviceId`, `brandId`, `delete_flag`, `priority`,`created`,`updated`,`created_by`,`updated_by` FROM `advbrandinfo` WHERE `brandId`=? AND `delete_flag`=?";
			RowMapper<AdvBrandInfo> brandInfoMapper = new BeanPropertyRowMapper<AdvBrandInfo>(AdvBrandInfo.class);
			return jdbcTemplate.query(sqlBrandInfo, brandInfoMapper, brandId, deleteflag);

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	// @Override
	// public int fetchTotalBrandsCommentByParentId(long brandId, long
	// parentCommentId, String delete_flag) {
	// try {
	// String sql = "SELECT COUNT(*) FROM `brandscomment` WHERE `paramId` = ? AND
	// `parentCommentId` = ? AND `delete_flag`=?";
	// int brandsComment = jdbcTemplate.queryForObject(sql, Integer.class, brandId,
	// parentCommentId, delete_flag);
	// return brandsComment;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	@Override
	public int modifyComment(BrandsComment brandsComment, String deleteflag) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `brandscomment` SET `content`=?,`updated`=? WHERE `commentId` = ? AND `delete_flag` =?";
			int result = jdbcTemplate.update(sql, brandsComment.getContent(), timestamp, brandsComment.getCommentId(),
					deleteflag);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchBloggerSmartId() {
		try {
			String sql1 = "SELECT `id` FROM `bloggersmartid` ORDER BY `s_no` DESC LIMIT 1";
			return jdbcTemplate.queryForObject(sql1, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addBloggerSmartId(String newId) {
		try {
			String sqlInsert = "INSERT INTO `bloggersmartid` (`id`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addBlogger(Blogger blog, String deleteflag, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `blogger` (`bloggerId`,`fullName`, `displayName`, `dob`, `gender`, `emailId`, `password`,`userName`, `phoneNumber`, `pincode`, `imagePath`,`partyStatusId`, `created`, `updated`,`delete_flag`,`created_by`,`updated_by`,`blgAdminId`) values (?,ENCODE(?,?),?,ENCODE(?,?),?,ENCODE(LOWER(?),?), ?, ENCODE(LOWER(?),?), ENCODE(LOWER(?),?), ?,?, ?, ?, ?, ?,?,?,?)";
			int result = jdbcTemplate.update(sql, blog.getBloggerId(), blog.getFullName(), encryptPass,
					blog.getDisplayName(), blog.getDob(), encryptPass, blog.getGender(), blog.getEmailId(), encryptPass,
					blog.getPassword(), blog.getUserName(), encryptPass, blog.getPhoneNumber(), encryptPass,
					blog.getPincode(), blog.getImagePath(), blog.getPartyStatusId(), timestamp, timestamp, deleteflag,
					blog.getCreated_by(), blog.getUpdated_by(), blog.getBlgAdminId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addPartyBlogger(Blogger blog, String delete_flag_N, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sqlForPartyInsert = "INSERT INTO `party` (`partyStatusId`,`roleBasedId`,`emailId`,`password`,`userName`,`phoneNumber`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,ENCODE(LOWER(?),?),?,ENCODE(LOWER(?),?), ENCODE(LOWER(?),?), ?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlForPartyInsert, blog.getPartyStatusId(), blog.getBloggerId(),
					blog.getEmailId(), encryptPass, blog.getPassword(), blog.getUserName(), encryptPass,
					blog.getPhoneNumber(), encryptPass, timestamp, timestamp, delete_flag_N, blog.getCreated_by(),
					blog.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Blogger fetchBloggerByBloggerId(String roleBasedId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `bloggerId`, DECODE(`fullName`,?) fullName, `displayName`, DECODE(`dob`,?) dob, DECODE(`emailId`,?) emailId, `gender`,`password`, DECODE(`userName`,?)userName ,DECODE(`phoneNumber`,?) phoneNumber, DECODE(`pincode`,?) pincode,`imagePath`, `partyStatusId`, `created`, `updated`,`delete_flag`,`created_by`,`updated_by`,`blgAdminId` FROM `blogger` WHERE `bloggerId` = ? AND `delete_flag`=?";

			RowMapper<Blogger> rowMapper = new BeanPropertyRowMapper<Blogger>(Blogger.class);
			return jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, roleBasedId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<BrandsComment> fetchBrandsComment(String brandId, String delete_flag, String encryptPass) {
		try {
			String sqlBrandInfo = "SELECT commentId, content, partyId, parentCommentId, DECODE(name,?) name, designation, imagePath, created,paramId,updated,delete_flag,created_by,updated_by FROM brandscomment WHERE paramId=? AND delete_flag=?";
			RowMapper<BrandsComment> rowMapper = new BeanPropertyRowMapper<BrandsComment>(BrandsComment.class);
			return jdbcTemplate.query(sqlBrandInfo, rowMapper, encryptPass, brandId, delete_flag);

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchBloggerTotalList(String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `blogger` WHERE delete_flag =?";
			int bloggerList = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);
			return bloggerList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Blogger> fetchBlogger(Pageable pageable, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `bloggerId`, DECODE(`fullName`,?) fullName, `displayName` , DECODE(`dob`,?) dob, DECODE(`emailId`,?) emailId, `gender`,`imagePath`,`pincode`,`password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`,`created_by`,`updated_by` FROM `blogger` WHERE `delete_flag`=?";
			RowMapper<Blogger> rowMapper = new BeanPropertyRowMapper<Blogger>(Blogger.class);
			List<Blogger> blogger = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, delete_flag);
			return blogger;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int checkBloggerIsPresent(String bloggerId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `blogger` WHERE `bloggerId`= ? AND `delete_flag` = ?";
			int blogger = jdbcTemplate.queryForObject(sql, Integer.class, bloggerId, deleteflag);
			return blogger;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public int fetchTotalExploreAdvisorByBrand(long brandId, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `advbrandinfo` WHERE `brandId`=? AND `delete_flag`=?";
			int advList = jdbcTemplate.queryForObject(sql, Integer.class, brandId, deleteflag);
			return advList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int update(String bloggerId, Blogger blogger, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql1 = "UPDATE `blogger` SET `fullName`= ENCODE(?, ?),`displayName`=?, `emailId`=ENCODE(?, ?), `phoneNumber`=ENCODE(?, ?),`userName`=ENCODE(?, ?),`dob`=ENCODE(?, ?),`gender`=?,`updated`=?,`updated_by`=?,`imagePath`=? WHERE `bloggerId`=?";
			int result = jdbcTemplate.update(sql1, blogger.getFullName(), encryptPass, blogger.getDisplayName(),
					blogger.getEmailId(), encryptPass, blogger.getPhoneNumber(), encryptPass, blogger.getUserName(),
					encryptPass, blogger.getDob(), encryptPass, blogger.getGender(), timestamp, blogger.getUpdated_by(),
					blogger.getImagePath(), bloggerId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updatePersonalInfoInParty(String emailId, String phoneNumber, String userName, String bloggerId,
			String encryptPass, String signedUserId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `party` SET `emailId`=ENCODE(?,?),`phoneNumber`=ENCODE(?,?),`userName`=ENCODE(?,?), `updated`=?,`updated_by`=? WHERE `roleBasedId`=?";
			int result = jdbcTemplate.update(sql, emailId, encryptPass, phoneNumber, encryptPass, encryptPass, userName,
					timestamp, signedUserId, bloggerId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeBlogger(String bloggerId, String deleteflag, String signedUserId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE blogger LEFT JOIN  party ON party.roleBasedId = blogger.bloggerId SET blogger.delete_flag = ?, blogger.updated=?,blogger.updated_by=?,  party.delete_flag=?,party.updated=?,party.updated_by=? WHERE blogger.bloggerId = ?";
			int result = jdbcTemplate.update(sql, deleteflag, timestamp, signedUserId, deleteflag, timestamp,
					signedUserId, bloggerId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorByBrandAndCity(String brand, String city, String deleteflag,
			String encryptPass) {
		try {
			String sql = "SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy FROM advisor adv INNER JOIN advbrandinfo brandinfo ON (adv.`advId` = brandinfo.`advId` AND adv.`delete_flag` = ? AND brandinfo.`delete_flag` = ? AND adv.`city` = '"
					+ city + "' OR brandinfo.`brandId` = '" + brand + "')";

			RowMapper<Advisor> advMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			return jdbcTemplate.query(sql, advMapper, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int changeToCorporate(String deleteflag, String advId, int advType_corp) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql1 = "UPDATE `advisor` SET `advType`=?,`updated`=? WHERE `delete_flag`=? AND `advId`=?";
			return jdbcTemplate.update(sql1, advType_corp, timestamp, deleteflag, advId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchBrandsTotalComment(String brandId, String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM brandscomment WHERE paramId = ? AND delete_flag=?";
			int brandComment = jdbcTemplate.queryForObject(sql, Integer.class, brandId, delete_flag);
			return brandComment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}

	}

	@Override
	public int fetchTotalExploreAdvisorByProduct(String sortByCity, String productId, String serviceId, String brandId,
			String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag = ? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') AND pub_adv.delete_flag =? AND pub_adv.city = '" + sortByCity
					+ "')) AS advisorlist";
			int advisor = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag, deleteflag, deleteflag);
			return advisor;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorListByProduct(String sortByCity, String productId, String serviceId,
			String brandId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT * FROM (SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, exp.expId COL_A, exp.company COL_B,  exp.designation COL_C,exp.location COL_D,exp.fromYear COL_E,exp.toYear COL_F,exp.advId COL_ADVID,exp.delete_flag COL_DELETEFLAG, '' COL_I,'' COL_J,'' COL_K,'' COL_L,'' COL_M,'' COL_N, 'exp' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT)"

					+ " UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN public_experience exp ON (adv.advId = exp.advId) \r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, edu.eduId,edu.degree,edu.field,edu.institution,edu.fromYear,edu.toYear,edu.advId,edu.delete_flag,'','','','','','', 'edu' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT)"

					+ " UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN public_education edu ON (adv.advId = edu.advId) \r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, awd.awardId, awd.imagePath,awd.issuedBy,awd.title,awd.year,'',awd.advId,awd.delete_flag,'','','','','','','awd' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT)"

					+ " UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN public_award awd ON (adv.advId = awd.advId) \r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, cert.certificateId, cert.imagePath,cert.issuedBy,cert.title,cert.year,'',cert.advId,cert.delete_flag,'' ,'','','','','','cert' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT)"

					+ " UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY DECODE(pub_adv.`name`,?) ASC LIMIT)) adv LEFT JOIN public_certificate cert ON (adv.advId = cert.advId) \r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, prod.advProdId,prod.licImage, prod.prodId,prod.serviceId,prod.remId,prod.licId,prod.advId,prod.delete_flag,prod.licNumber,prod.validity,prod.created,prod.updated,prod.created_by,prod.updated_by,'prod' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT)"

					+ "UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN public_advproduct prod ON (adv.advId = prod.advId)\r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, promo.promotionId,promo.title,promo.video,promo.imagePath,promo.aboutVideo,'',promo.advId,promo.delete_flag,'','','','','','','promo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT) "

					+ "UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN public_promotion promo ON (adv.advId = promo.advId) \r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandinfo.advBrandId,brandinfo.prodId,brandinfo.serviceId,brandinfo.brandId,brandinfo.priority,'',brandinfo.advId,brandinfo.delete_flag,'','','','','','', 'brandinfo' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT )"

					+ " UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN `public_advbrandinfo` brandinfo ON (adv.`advId` = brandinfo.`advId`)\r\n"

					+ "UNION ALL SELECT adv.`advId` advId,adv.`advType` advType,DECODE(adv.`name`,?) name,adv.`designation` designation,DECODE(adv.`emailId`,?) emailId,adv.`password` password,DECODE(adv.`userName`,?) userName,DECODE(adv.`phoneNumber`,?) phoneNumber,adv.`delete_flag` delete_flag,adv.`partyStatusId` partyStatusId,adv.`created` created,adv.`updated` updated,adv.`created_by` created_by,adv.`updated_by` updated_by,adv.`displayName` displayName,DECODE(adv.`dob`,?) dob,adv.`gender` gender,DECODE(adv.`panNumber`,?) panNumber,adv.`address1` address1,adv.`address2` address2,adv.`state` state,adv.`city` city,adv.`pincode` pincode,adv.`aboutme` aboutme,adv.`firmType` firmType,adv.`corporateLable` corporateLable,adv.`website` website,adv.`parentPartyId` parentPartyId,adv.`imagePath` imagePath,adv.`isVerified` isVerified,adv.`verifiedBy` verifiedBy,adv.`verified` verified,adv.`isMobileVerified` isMobileVerified,adv.`workFlowStatus` workFlowStatus,adv.`approvedDate` approvedDate,adv.`approvedBy` approvedBy,adv.`revokedDate` revokedDate,adv.`revokedBy` revokedBy, brandrank.advBrandRankId,brandrank.prodId,brandrank.brandId,brandrank.ranking,'','',brandrank.advId,brandrank.delete_flag,'','','','','','', 'brandrank' VALUE FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1=(CASE WHEN '" + sortByCity
					+ "' = '' THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? ORDER BY pub_adv.`city` ASC LIMIT)"

					+ " UNION ALL (SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag=? AND pub_brandinfo.`prodId` ='"
					+ productId + "' AND pub_brandinfo.`serviceId` ='" + serviceId + "' AND pub_brandinfo.`brandId` = '"
					+ brandId + "') WHERE 1 = (CASE WHEN '" + sortByCity
					+ "' IS NOT NULL THEN 1 ELSE 0 END) AND pub_adv.delete_flag =? AND pub_adv.`city`= '" + sortByCity
					+ "' ORDER BY pub_adv.`city` ASC LIMIT)) adv LEFT JOIN public_advbrandrank brandrank ON (adv.advId = brandrank.advId)) AS advisorlist WHERE delete_flag =?";
			return jdbcTemplate.query(sql, new AdvisorListExtractor(deleteflag), encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					encryptPass, deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, deleteflag,
					deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass, encryptPass,
					deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, deleteflag, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, encryptPass, deleteflag, deleteflag, deleteflag, deleteflag,
					deleteflag, deleteflag, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorListByProdId(String productId, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM ((SELECT pub_adv.* FROM public_advisor pub_adv INNER JOIN `public_advbrandinfo` pub_brandinfo ON (pub_adv.`advId` = pub_brandinfo.`advId` AND pub_adv.`delete_flag` = ? AND pub_brandinfo.delete_flag = ? AND pub_brandinfo.`prodId` ='"
					+ productId + "') AND pub_adv.delete_flag =?)) AS advisorlist";
			int advisor = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag, deleteflag, deleteflag);
			return advisor;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchAdvisorPromoCount(String advId, String deleteflag) {
		try {
			String sql = "SELECT `promoCount` FROM `advisor` WHERE `advId`=? AND `delete_flag`=?";
			int promoCount = jdbcTemplate.queryForObject(sql, Integer.class, advId, deleteflag);
			return promoCount;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateArticlePostInfoByPartyId(String imagePath, long partyId, String deleteflag) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());

			String sql = "UPDATE `articlepost` SET `imagePath`=? ,`updated`=? WHERE `partyId`=?";
			return jdbcTemplate.update(sql, imagePath, timestamp, partyId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ArticlePost> fetchArticlePostList(long partyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`,DECODE(`name`,?) name,`designation`,`imagePath`,`title`, `content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `partyId` = ? AND `delete_flag` = ?";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, partyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<AdvBrandInfo> fetchAdvBrandInfoByProdServBrand(Pageable pageable, long productId, long serviceId,
			long brandId, String deleteflag) {
		try {
			String sql = "SELECT * from `public_advbrandinfo` WHERE `prodId`=? AND `serviceId`=?  AND `delete_flag`=? AND `brandId`=?  ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			RowMapper<AdvBrandInfo> brandInfoMapper = new BeanPropertyRowMapper<AdvBrandInfo>(AdvBrandInfo.class);
			return jdbcTemplate.query(sql, brandInfoMapper, productId, serviceId, deleteflag, brandId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<AdvProduct> fetchAdvBrandInfoByProdServ(Pageable pageable, long productId, long serviceId,
			String deleteflag) {
		try {
			String sql = "SELECT * from `public_advproduct` WHERE `prodId`=? AND FIND_IN_SET(?, `serviceId`)  AND `delete_flag`=?  ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			RowMapper<AdvProduct> brandInfoMapper = new BeanPropertyRowMapper<AdvProduct>(AdvProduct.class);
			return jdbcTemplate.query(sql, brandInfoMapper, productId, serviceId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorByCityList(Pageable pageable, String state, String city, String pincode,
			String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name,DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `state` LIKE '"
					+ state + "%' AND `city` LIKE '" + city
					+ "%' AND `pincode`=? AND `delete_flag`=? ORDER BY created DESC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, pincode, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorByCityListWithoutPin(Pageable pageable, String state, String city,
			String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name,DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `state` LIKE '"
					+ state + "%' AND `city` LIKE '" + city + "%'  AND `delete_flag`=? ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByProdAndState(Pageable pageable, long prodId, String state,
			String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND public_advisor.`state` = ? AND public_advisor.`delete_flag`=?) AS `advisor`  ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, state, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorListByProdId(String productId, String deleteflag, String encryptPass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> fetchExploreAdvisorByProdServBrandAndState(Pageable pageable, long prodId, long serviceId,
			long brandId, String state, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.*, public_advbrandinfo.`advBrandId`, public_advbrandinfo.`prodId` FROM public_advisor INNER JOIN public_advbrandinfo ON public_advisor.`advId`=public_advbrandinfo.`advId` AND public_advisor.`delete_flag` = public_advbrandinfo.`delete_flag` WHERE public_advbrandinfo.`prodId` = ? AND public_advbrandinfo.`serviceId` = ? AND public_advbrandinfo.`brandId` = ? AND public_advisor.`state` = ? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, serviceId, brandId, state, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByProdStateAndCity(Pageable pageable, long prodId, String state, String city,
			String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, state, city, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByProdServStateAndCity(Pageable pageable, long prodId, String state,
			long serviceId, String city, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND  FIND_IN_SET(?, `serviceId`) AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, serviceId, state, city, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByProdServStateAndCity(Pageable pageable, long prodId, long serviceId,
			long brandId, String state, String city, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.*, public_advbrandinfo.`advBrandId`, public_advbrandinfo.`prodId` FROM public_advisor INNER JOIN public_advbrandinfo ON public_advisor.`advId`=public_advbrandinfo.`advId` AND public_advisor.`delete_flag` = public_advbrandinfo.`delete_flag` WHERE public_advbrandinfo.`prodId` = ? AND public_advbrandinfo.`serviceId` = ? AND public_advbrandinfo.`brandId` = ? AND public_advisor.`state` = ? AND public_advisor.`city` = ? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, serviceId, brandId, state, city, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByproduct(Pageable pageable, long productId, String deleteflag,
			String encryptPass) {
		try {
			String sql = "SELECT `advId` from `public_advproduct` WHERE `prodId`=?  AND `delete_flag`=? ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, productId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public List<Advisor> fetchExploreAdvisorByState(Pageable pageable, String state, String deleteflag,
			String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name,DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `state` LIKE '"
					+ state + "%' AND `delete_flag`=? ORDER BY created DESC LIMIT " + pageable.getPageSize() + " OFFSET "
					+ pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByprodServAndStateDetails(Pageable pageable, long prodId, long serviceId,
			String state, String city, String pincode, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND FIND_IN_SET(?, `serviceId`) AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`pincode`=? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, serviceId, state, city, pincode, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByProdDetailsAndStateDetails(Pageable pageable, long prodId, long serviceId,
			long brandId, String state, String city, String pincode, String deleteflag, String encryptPass,
			String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.*, public_advbrandinfo.advBrandId, public_advbrandinfo.prodId FROM public_advisor INNER JOIN public_advbrandinfo ON public_advisor.advId=public_advbrandinfo.advId AND public_advisor.delete_flag = public_advbrandinfo.delete_flag WHERE public_advbrandinfo.prodId = ? AND public_advbrandinfo.serviceId = ? AND public_advbrandinfo.brandId = ? AND public_advisor.state = ?  AND public_advisor.city = ?  AND public_advisor.pincode = ? AND public_advisor.delete_flag=?)AS `advisor`  ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, serviceId, brandId, state, city, pincode,
					deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public List<String> fetchExploreAdvisorByProdServAndState(Pageable pageable, long prodId, long serviceId,
			String state, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND  FIND_IN_SET(?, `serviceId`) AND public_advisor.`state` = ? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, serviceId, state, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchExploreAdvisorByProdStateCityAndPincode(Pageable pageable, long prodId, String state,
			String city, String pincode, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT DISTINCT advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`pincode`=? AND public_advisor.`delete_flag`=?) AS `advisor` ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, prodId, state, city, pincode, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<BrandsComment> fetchBrandsCommentByParentId(String paramId, long parentId, String delete_flag,
			String encryptPass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int fetchTotalBrandsCommentByParentId(long brandId, long parentCommentId, String delete_flag) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateArticleCommentByPartyId(String displayName, String imagePath, long partyId, String signedUserId,
			String deleteflag, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `brandscomment` SET `name`= ENCODE(?, ?),`imagepath`=?,`updated`=?,`updated_by`=? WHERE `partyId`=?";
			int result = jdbcTemplate.update(sql, displayName, encryptPass, imagePath, timestamp, signedUserId,
					partyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<BrandsComment> fetchBrandsCommentByPartyId(long partyId, String deleteflag, String encryptPass) {
		try {
			String sqlBrandInfo = "SELECT `commentId`, `content`, `partyId`, `parentCommentId`, DECODE(`name`,?) `name`, `designation`, `imagePath`, `created`, `paramId`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `brandscomment` WHERE `partyId`=? AND `delete_flag`=?";
			RowMapper<BrandsComment> rowMapper = new BeanPropertyRowMapper<BrandsComment>(BrandsComment.class);
			return jdbcTemplate.query(sqlBrandInfo, rowMapper, encryptPass, partyId, deleteflag);

		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchCommentsTotalList(String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) FROM `brandscomment` WHERE delete_flag =?";
			int bloggerList = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);
			return bloggerList;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	public List<Advisor> fetchExploreAdvisorByDisplayName(Pageable pageable, String displayName, String deleteflag,
			String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name,DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `displayName` LIKE '"
					+ displayName + "%' AND `delete_flag`=? ORDER BY created DESC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public List<Advisor> fetchExploreAdvisorByCityDetailsAndDisplayName(Pageable pageable, String state, String city,
			String pincode, String displayName, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) name,DECODE(`userName`,?) userName, `designation`, DECODE(`emailId`,?) emailId, `password`, DECODE(`phoneNumber`,?) phoneNumber, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) dob, `gender`, DECODE(`panNumber`,?) panNumber, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `state` LIKE '"
					+ state + "%' AND `city` LIKE '" + city + "%' AND `pincode`=? AND `displayName` LIKE '"
					+ displayName + "%'  AND `delete_flag`=? ORDER BY created DESC LIMIT " + pageable.getPageSize()
					+ " OFFSET " + pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, pincode, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<BrandsComment> fetchBrandsCommentsList(Pageable pageable, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `commentId`,`partyId`,`parentCommentId`,DECODE(`name`,?) name, `designation` ,`imagepath`,`content`,`paramId`,`adminId`,`delete_flag`, `created`,			 `updated`,`created_by`,`updated_by` FROM `brandscomment` WHERE			 `delete_flag`=?";
			// String sql = "SELECT `brandscomment`.*,`commentvote`.`upCount` FROM
			// `brandscomment` INNER JOIN `commentvote` ON
			// `brandscomment`.`commentId`=`commentvote`.`commentId` WHERE
			// `brandscomment`.`delete_flag`=?";
			RowMapper<BrandsComment> rowMapper = new BeanPropertyRowMapper<BrandsComment>(BrandsComment.class);
			List<BrandsComment> brandsComment = jdbcTemplate.query(sql, rowMapper, encryptPass, deleteflag);
			return brandsComment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<String> fetchAllExploreAdvisorList(Pageable pageable, String deleteflag, String encryptPass,
			String signedUserId) {
		try {
			String sql = "SELECT `advId` FROM public_advisor WHERE delete_flag=? ORDER BY created DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset();
			return jdbcTemplate.queryForList(sql, String.class, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public CommentVote fetchCommentsVote(long commentId) {
		try {
			String sql = "SELECT * from `commentvote` WHERE `commentId`=?";
			RowMapper<CommentVote> commentMapper = new BeanPropertyRowMapper<CommentVote>(CommentVote.class);
			return jdbcTemplate.queryForObject(sql, commentMapper, commentId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<Advisor> fetchExploreAdvisorByProdStateCityAndDisplayName(Pageable pageable, String state, String city,
			String displayName, String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) `name`,DECODE(`userName`,?) `userName`, `designation`, DECODE(`emailId`,?) `emailId`, `password`, DECODE(`phoneNumber`,?) `phoneNumber`, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) `dob`, `gender`, DECODE(`panNumber`,?) `panNumber`, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `state` LIKE '"
					+ state + "%' AND `city` LIKE '" + city + "%' AND `displayName` LIKE '" + displayName
					+ "%'  AND `delete_flag`=? ORDER BY created DESC LIMIT " + pageable.getPageSize() + " OFFSET "
					+ pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<Advisor> fetchExploreAdvisorByStateAndDisplayName(Pageable pageable, String state, String displayName,
			String deleteflag, String encryptPass, String signedUserId) {
		try {
			String sql = "SELECT `advId`, `advType`, DECODE(`name`,?) `name`,DECODE(`userName`,?) `userName`, `designation`, DECODE(`emailId`,?) `emailId`, `password`, DECODE(`phoneNumber`,?) `phoneNumber`, `delete_flag`, `partyStatusId`, `created`, `updated`, `created_by`, `updated_by`, `displayName`, DECODE(`dob`,?) `dob`, `gender`, DECODE(`panNumber`,?) `panNumber`, `address1`, `address2`, `state`, `city`, `pincode`, `aboutme` FROM `public_advisor` WHERE `state` LIKE '"
					+ state + "%' AND `displayName` LIKE '" + displayName
					+ "%'  AND `delete_flag`=? ORDER BY created DESC LIMIT " + pageable.getPageSize() + " OFFSET "
					+ pageable.getOffset();
			RowMapper<Advisor> rowMapper = new BeanPropertyRowMapper<Advisor>(Advisor.class);
			List<Advisor> advisor = jdbcTemplate.query(sql, rowMapper, encryptPass, encryptPass, encryptPass,
					encryptPass, encryptPass, encryptPass, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;

		}
	}

	@Override
	public int fetchAllExploreAdvisorTotalList(String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM public_advisor WHERE delete_flag=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByDisplayName(String displayName, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `displayName` LIKE '" + displayName
					+ "%' AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByState(String state, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `state` LIKE '" + state
					+ "%' AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchExploreAdvisorByproductTotal(long product, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` from `public_advproduct` WHERE `prodId`=?  AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, product, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdDetailsWithoutBrand(long productId, long serviceId, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` from `public_advproduct` WHERE `prodId`=? AND FIND_IN_SET(?, `serviceId`)  AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdAndState(long productId, String state, String deleteflag) {

		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND public_advisor.`state` = ? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, state, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByStateAndDisplayName(String state, String displayName, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `state` LIKE '" + state
					+ "%' AND `displayName` LIKE '" + displayName + "%'  AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByCityDetailsWithoutPin(String state, String city, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `state` LIKE '" + state
					+ "%' AND `city` LIKE '" + city + "%'  AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByStateCityAndDisplayName(String state, String city, String displayName,
			String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `state` LIKE '" + state
					+ "%' AND `city` LIKE '" + city + "%' AND `displayName` LIKE '" + displayName
					+ "%'  AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByCityDetails(String state, String city, String pincode, String deleteflag) {

		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `state` LIKE '" + state
					+ "%' AND `city` LIKE '" + city + "%' AND `pincode`=? AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, pincode, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdDetails(long productId, long serviceId, long brandId, String deleteflag) {

		try {
			String sql = "SELECT COUNT(*) `advId` from `public_advbrandinfo` WHERE `prodId`=? AND `serviceId`=?  AND `delete_flag`=? AND `brandId`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, deleteflag, brandId);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdServAndState(long productId, long serviceId, String state,
			String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND  FIND_IN_SET(?, `serviceId`) AND public_advisor.`state` = ? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, state, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdStateAndCity(long productId, String state, String city,
			String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, state, city, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdStateCityAndPincode(long productId, String state, String city,
			String pincode, String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`pincode`=? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, state, city, pincode, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdServStateAndCity(long productId, long serviceId, String state, String city,
			String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND  FIND_IN_SET(?, `serviceId`) AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, state, city, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdServBrandAndState(long productId, long serviceId, long brandId,
			String state, String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.*, public_advbrandinfo.`advBrandId`, public_advbrandinfo.`prodId` FROM public_advisor INNER JOIN public_advbrandinfo ON public_advisor.`advId`=public_advbrandinfo.`advId` AND public_advisor.`delete_flag` = public_advbrandinfo.`delete_flag` WHERE public_advbrandinfo.`prodId` = ? AND public_advbrandinfo.`serviceId` = ? AND public_advbrandinfo.`brandId` = ? AND public_advisor.`state` = ? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, brandId, state, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByCityDetailsAndDisplayName(String state, String city, String pincode,
			String displayName, String deleteflag) {
		try {
			String sql = "SELECT COUNT(*) `advId` FROM `public_advisor` WHERE `state` LIKE '" + state
					+ "%' AND `city` LIKE '" + city + "%' AND `pincode`=? AND `displayName` LIKE '" + displayName
					+ "%'  AND `delete_flag`=?";
			return jdbcTemplate.queryForObject(sql, Integer.class, pincode, deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByprodServAndStateDetails(long productId, long serviceId, String state,
			String city, String pincode, String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.* FROM public_advisor INNER JOIN public_advproduct ON public_advisor.`advId`=public_advproduct.`advId` AND public_advisor.`delete_flag` = public_advproduct.`delete_flag` WHERE public_advproduct.`prodId` = ? AND FIND_IN_SET(?, `serviceId`) AND public_advisor.`state` = ? AND public_advisor.`city`=? AND public_advisor.`pincode`=? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, state, city, pincode,
					deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdServBrandStateAndCity(long productId, long serviceId, long brandId,
			String state, String city, String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.*, public_advbrandinfo.`advBrandId`, public_advbrandinfo.`prodId` FROM public_advisor INNER JOIN public_advbrandinfo ON public_advisor.`advId`=public_advbrandinfo.`advId` AND public_advisor.`delete_flag` = public_advbrandinfo.`delete_flag` WHERE public_advbrandinfo.`prodId` = ? AND public_advbrandinfo.`serviceId` = ? AND public_advbrandinfo.`brandId` = ? AND public_advisor.`state` = ? AND public_advisor.`city` = ? AND public_advisor.`delete_flag`=?) AS `advisor`";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, brandId, state, city,
					deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalExploreAdvisorByProdDetailsAndStateDetails(long productId, long serviceId, long brandId,
			String state, String city, String pincode, String deleteflag) {
		try {
			String sql = "SELECT DISTINCT COUNT(*) advId FROM(SELECT public_advisor.*, public_advbrandinfo.advBrandId, public_advbrandinfo.prodId FROM public_advisor INNER JOIN public_advbrandinfo ON public_advisor.advId=public_advbrandinfo.advId AND public_advisor.delete_flag = public_advbrandinfo.delete_flag WHERE public_advbrandinfo.prodId = ? AND public_advbrandinfo.serviceId = ? AND public_advbrandinfo.brandId = ? AND public_advisor.state = ?  AND public_advisor.city = ?  AND public_advisor.pincode = ? AND public_advisor.delete_flag=?) AS advisor";
			return jdbcTemplate.queryForObject(sql, Integer.class, productId, serviceId, brandId, state, city, pincode,
					deleteflag);

		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public CommentVote fetchCommentVoteByCommentId(long CommentVote) {
		try {
			String sql = "SELECT `commentVoteId`,`commentId`,`downCount`,`upCount` FROM `commentvote` WHERE `commentId` = ?";
			RowMapper<CommentVote> rowMapper = new BeanPropertyRowMapper<CommentVote>(CommentVote.class);
			CommentVote commentVote = jdbcTemplate.queryForObject(sql, rowMapper, CommentVote);
			return commentVote;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int firstCommentVote(CommentVote commentVote1) {
		try {
			String sqlInsert = "INSERT INTO `commentvote` (`upcount`,`downcount`,`commentId`) values (?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, commentVote1.getUpCount(), commentVote1.getDownCount(),
					commentVote1.getCommentId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchUpCountByCommentId(long commentId) {
		try {
			String sql = "SELECT `upcount` FROM `commentvote` WHERE `commentId`= ?";
			int up = jdbcTemplate.queryForObject(sql, Integer.class, commentId);
			return up;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateCommentUpVote(int upCount, long commentId) {
		try {
			String sql1 = "UPDATE `commentvote` SET `upcount`=? WHERE `commentId`=?";
			int result = jdbcTemplate.update(sql1, upCount, commentId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchDownCountByCommentId(long commentId) {
		try {
			String sql = "SELECT `downcount` FROM `commentvote` WHERE `commentId`= ?";
			int down = jdbcTemplate.queryForObject(sql, Integer.class, commentId);
			return down;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateCommentDownVote(int downCount, long commentId) {
		try {
			String sql1 = "UPDATE `commentvote` SET `downcount`=? WHERE `commentId`=?";
			int result = jdbcTemplate.update(sql1, downCount, commentId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int saveCommentVote(long voteType, long commentId, long partyId, String signedUserId) {
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String vote = null;
		if (voteType == 1) {
			vote = "UP";
		} else {
			vote = "DOWN";
		}
		try {
			String sqlInsert = "INSERT INTO `commentvoteaddress` (`commentId`,`partyId`,`voteType`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, commentId, partyId, vote, timestamp, timestamp, signedUserId,
					signedUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeCommentVoteAddress(long commentId, long partyId) {
		try {
			String sql = "DELETE FROM `commentvoteaddress` WHERE `commentId`=? AND `partyId`=?";
			int result = jdbcTemplate.update(sql, commentId, partyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<CommentVoteAddress> fetchCommentVoteAddress(long partyId) {
		try {
			String sql = "SELECT `partyId`,`commentId`, `voteType`,`created`,`updated`,`created_by`,`updated_by`FROM `commentvoteaddress` WHERE `partyId`=?";
			RowMapper<CommentVoteAddress> rowMapper = new BeanPropertyRowMapper<CommentVoteAddress>(
					CommentVoteAddress.class);
			List<CommentVoteAddress> commentVoteAddress = jdbcTemplate.query(sql, rowMapper, partyId);
			return commentVoteAddress;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchCommentPost(long commentId, String delete_flag) {
		try {
			String sql = "SELECT count(*) FROM `brandscomment` WHERE `commentId` = ? AND `delete_flag`= ?";
			int articlePost = jdbcTemplate.queryForObject(sql, Integer.class, commentId, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateArticlePostNameByPartyId(String displayName, String imagePath, long partyId, String signedUserId,
			String deleteflag, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `articlepost` SET `name`= ENCODE(?, ?),`imagepath`=?,`updated`=?,`updated_by`=? WHERE `partyId`=?";
			int result = jdbcTemplate.update(sql, displayName, encryptPass, imagePath, timestamp, signedUserId,
					partyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ArticleComment> fetchArticleCommentByParyId(long partyId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `commentId`,`partyId`, `parentCommentId`,DECODE(`name`,?) name,`designation`,`imagePath`, `content`,`created`, `forumStatusId`,`articleId`, `updated`, `adminId`, `delete_flag`,`created_by`,`updated_by` FROM `articlecomment` WHERE `partyId` = ? AND `delete_flag` = ?";
			RowMapper<ArticleComment> rowMapper = new BeanPropertyRowMapper<ArticleComment>(ArticleComment.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, partyId, deleteflag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateArticlePostCommentByPartyId(String displayName, String imagePath, long partyId,
			String signedUserId, String deleteflag, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `articlecomment` SET `name`= ENCODE(?, ?),`imagepath`=?,`updated`=?,`updated_by`=? WHERE `partyId`=?";
			int result = jdbcTemplate.update(sql, displayName, encryptPass, imagePath, timestamp, signedUserId,
					partyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateCalcQuery(long partyId, String displayName, String signedUserId) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `calcquery` SET `displayName`=?,`updated`=?,`updated_by`=? WHERE `partyId`=?";
			int result = jdbcTemplate.update(sql, displayName, timestamp, signedUserId, partyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

}
