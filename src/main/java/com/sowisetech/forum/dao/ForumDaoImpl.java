package com.sowisetech.forum.dao;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.advisor.dao.AdvisorExtractor;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.forum.model.ArticleComment;
import com.sowisetech.forum.model.ArticleFavorite;
import com.sowisetech.forum.model.ArticlePost;
import com.sowisetech.forum.model.ArticleVote;
import com.sowisetech.forum.model.ArticleVoteAddress;
import com.sowisetech.forum.model.ForumAnswer;
import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumPost;
import com.sowisetech.forum.model.ForumPostVote;
import com.sowisetech.forum.model.ForumQuery;
import com.sowisetech.forum.model.ForumSubCategory;
import com.sowisetech.forum.model.ForumThread;
import com.sowisetech.forum.model.Party;
import com.sowisetech.forum.model.VoteType;

@Transactional
@Repository
public class ForumDaoImpl implements ForumDao {

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

	private static final Logger logger = LoggerFactory.getLogger(ForumDaoImpl.class);

	@Override
	public int fetchForumStatusIdByDesc(String desc) {
		try {
			String sqlForStatusId = "SELECT `id` FROM `forumstatus` WHERE `desc`=?";
			int forumStatusId = jdbcTemplate.queryForObject(sqlForStatusId, Integer.class, desc);
			return forumStatusId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int createArticlePost(ArticlePost articlePost, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `articlepost` (`title`,`url`,`content`,`forumStatusId`,`prodId`,`partyId`,`name`,`designation`,`imagePath`,`created`,`updated`,`delete_flag`,`articleStatusId`,`created_by`,`updated_by`,`userName`,`roleBasedId`) values (?,?,?,?,?,?,ENCODE(?,?),?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, articlePost.getTitle(), articlePost.getUrl(),
					articlePost.getContent(), articlePost.getForumStatusId(), articlePost.getProdId(),
					articlePost.getPartyId(), articlePost.getName(), encryptPass, articlePost.getDesignation(),
					articlePost.getImagePath(), timestamp, timestamp, articlePost.getDelete_flag(),
					articlePost.getArticleStatusId(), articlePost.getCreated_by(), articlePost.getUpdated_by(),articlePost.getUserName(),articlePost.getRoleBasedId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// fetch article by articleId //
	@Override
	public int fetchArticlePost(long articleId, String delete_flag) {
		try {
			String sql = "SELECT count(*) FROM `articlepost` WHERE `articleId` = ? AND `delete_flag`= ?";
			int articlePost = jdbcTemplate.queryForObject(sql, Integer.class, articleId, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// fetch status by forumstatusId
	@Override
	public String fetchForumStatus(long forumStatusId) {
		try {
			String sqlForumStatus = "SELECT `desc` FROM `forumstatus` WHERE `id` = ?";
			String result = jdbcTemplate.queryForObject(sqlForumStatus, String.class, forumStatusId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	// moderate articlePost //
	@Override
	public int moderateArticlePost(long articleId, long forumStatusId, String adminId, String reason,
			String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `articlepost` SET `forumstatusId`=?,`updated`=?,`adminId`=?, `reason`=?,`updated_by`=? WHERE `articleId`=?";
			int result = jdbcTemplate.update(sql1, forumStatusId, timestamp, adminId, reason, signedUserId, articleId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// create articlePostComment //
	@Override
	public int createArticlePostComment(ArticleComment comment, String encryptPass) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `articlecomment` (`content`,`forumStatusId`,`partyId`,`parentCommentId`,`name`,`designation`,`imagePath`,`created`,`articleId`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,?,ENCODE(?,?),?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, comment.getContent(), comment.getForumStatusId(),
					comment.getPartyId(), comment.getParentCommentId(), comment.getName(), encryptPass,
					comment.getDesignation(), comment.getImagePath(), timestamp, comment.getArticleId(), timestamp,
					comment.getDelete_flag(), comment.getCreated_by(), comment.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// fetch articleComment by id //
	@Override
	public int fetchArticleComment(long commentId, String delete_flag) {
		try {
			String sql = "SELECT count(*) FROM `articlecomment` WHERE `commentId` = ? AND `delete_flag`=?";
			int comment = jdbcTemplate.queryForObject(sql, Integer.class, commentId, delete_flag);
			return comment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int moderateArticleComment(long commentId, long forumStatusId, String adminId, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `articlecomment` SET `forumstatusId`=?,`updated`=?,`adminId`=?,`updated_by`=? WHERE `commentId`=?";
			int result = jdbcTemplate.update(sql1, forumStatusId, timestamp, adminId, signedUserId, commentId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ForumSubCategory fetchForumSubCategory(long id) {
		try {
			String sql = "SELECT `forumSubCategoryId`,`forumCategoryId`,`name` FROM `forumsubcategory` WHERE `forumSubCategoryId` = ?";
			RowMapper<ForumSubCategory> rowMapper = new BeanPropertyRowMapper<ForumSubCategory>(ForumSubCategory.class);
			ForumSubCategory forumSubCategory = jdbcTemplate.queryForObject(sql, rowMapper, id);
			return forumSubCategory;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
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
	public int removeArticle(long articleId, String delete_flag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "DELETE FROM `articlepost` WHERE `articleId`=?";
			int result = jdbcTemplate.update(sql1, articleId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeArticleComment(long commentId, String delete_flag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "DELETE FROM `articlecomment` WHERE `commentId`=?";
			int result = jdbcTemplate.update(sql1, commentId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int saveArticleVote(long voteType, long articleId, long partyId, String signedUserId) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String vote = null;
		if (voteType == 1) {
			vote = "UP";
		} else {
			vote = "DOWN";
		}
		try {
			String sqlInsert = "INSERT INTO `articlevoteaddress` (`articleId`,`partyId`,`voteType`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, articleId, partyId, vote, timestamp, timestamp, signedUserId,
					signedUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchUpVoteId(String up) {
		try {
			String sqlVoteId = "SELECT `id` FROM `votetype` WHERE `desc`=?";
			int voteId = jdbcTemplate.queryForObject(sqlVoteId, Integer.class, up);
			return voteId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchDownVoteId(String down) {
		try {
			String sqlVoteId = "SELECT `id` FROM `votetype` WHERE `desc`=?";
			int voteId = jdbcTemplate.queryForObject(sqlVoteId, Integer.class, down);
			return voteId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ArticleVote fetchArticleVoteByArticleId(long articleId) {
		try {
			String sql = "SELECT `voteId`,`articleId`,`downCount`,`upCount` FROM `articlevote` WHERE `articleId` = ?";
			RowMapper<ArticleVote> rowMapper = new BeanPropertyRowMapper<ArticleVote>(ArticleVote.class);
			ArticleVote articleVote = jdbcTemplate.queryForObject(sql, rowMapper, articleId);
			return articleVote;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int firstArticleVote(ArticleVote articleVote) {
		try {
			String sqlInsert = "INSERT INTO `articlevote` (`upcount`,`downcount`,`articleId`) values (?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, articleVote.getUp_count(), articleVote.getDown_count(),
					articleVote.getArticleId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchUpCountByArticleId(long articleId) {
		try {
			String sql = "SELECT `upcount` FROM `articlevote` WHERE `articleId`= ?";
			int up = jdbcTemplate.queryForObject(sql, Integer.class, articleId);
			return up;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateArticleUpVote(int upCount, long articleId) {
		try {
			String sql1 = "UPDATE `articlevote` SET `upcount`=? WHERE `articleId`=?";
			int result = jdbcTemplate.update(sql1, upCount, articleId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchDownCountByArticleId(long articleId) {
		try {
			String sql = "SELECT `downcount` FROM `articlevote` WHERE `articleId`= ?";
			int down = jdbcTemplate.queryForObject(sql, Integer.class, articleId);
			return down;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateArticleDownVote(int downCount, long articleId) {
		try {
			String sql1 = "UPDATE `articlevote` SET `downcount`=? WHERE `articleId`=?";
			int result = jdbcTemplate.update(sql1, downCount, articleId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public int fetchRoleIdByPartyId(long partyId) {
	// try {
	// String sqlRole = "SELECT `roleId` FROM `party` WHERE `partyId`= ?";
	// int roleId = jdbcTemplate.queryForObject(sqlRole, Integer.class, partyId);
	// return roleId;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	// @Override
	// public int fetchRoleIdByRole(String role_advisor) {
	// try {
	// String sqlRole = "SELECT `id` FROM `role` WHERE `name`= ?";
	// int roleId = jdbcTemplate.queryForObject(sqlRole, Integer.class,
	// role_advisor);
	// return roleId;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return 0;
	// }
	// }

	@Override
	public List<ArticlePost> fetchRecentArticlePostList(String delete_flag, String encryptPass) {
		try {
			String sql1 = "SELECT `articleId`, `created`, `articleStatusId`, `prodId`, `partyId`, DECODE(`name`,?) name,`designation`,`imagePath`,`title`,`content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `delete_flag` = ? ORDER BY created DESC";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			return jdbcTemplate.query(sql1, rowMapper, encryptPass, delete_flag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ForumCategory> fetchAllForumCategory() {
		try {
			String sql1 = "SELECT category.`forumCategoryId`,category.`name`, subcategory.`forumSubCategoryId` COL_A, subcategory.`name` COL_B, subcategory.forumCategoryId COL_C FROM `forumcategory` category LEFT JOIN `forumsubcategory` subcategory ON (category.`forumCategoryId` = subcategory.`forumCategoryId`)";
			return jdbcTemplate.query(sql1, new ForumCategoryListExtractor());
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public ArticlePost fetchRecentApprovedArticle(int forumStatusId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`,DECODE(`name`,?) name,`designation`,`imagePath`,`title`, `content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `forumStatusId` =? AND `delete_flag` = ?  ORDER BY `updated` DESC LIMIT 1";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			ArticlePost articlePost = jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, forumStatusId,
					delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticlePost> fetchAllArticleInApprovedOrder(int forumStatusId, int articleStatusId, String delete_flag,
			String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`,DECODE(`name`,?) name,`designation`,`imagePath`, `title`,`content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by`,`userName`,`roleBasedId` FROM `articlepost` WHERE `forumStatusId` =? AND `articleStatusId` =? AND `delete_flag` = ? ORDER BY `updated` DESC";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			List<ArticlePost> articlePost = jdbcTemplate.query(sql, rowMapper, encryptPass, forumStatusId,
					articleStatusId, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticleComment> fetchArticleCommentByArticleId(long articleId, String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `commentId`, `parentCommentId`,`created`,`partyId`,DECODE(`name`,?) name,`designation`,`imagePath`, `content`, `forumStatusId`, `updated`,`articleId`,`adminId`, `delete_flag`,`created_by`,`updated_by` FROM `articlecomment` WHERE `articleId` = ? AND `delete_flag`=? AND `parentCommentId`=? ORDER BY updated DESC";
			RowMapper<ArticleComment> rowMapper = new BeanPropertyRowMapper<ArticleComment>(ArticleComment.class);
			List<ArticleComment> articleComment = jdbcTemplate.query(sql, rowMapper, encryptPass, articleId, deleteflag,
					0);
			return articleComment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticlePost> fetchArticlePostList(Pageable pageable, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`,DECODE(`name`,?) name,`designation`,`imagePath`,`title`, `content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `delete_flag` = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset() + "";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			List<ArticlePost> articlePost = jdbcTemplate.query(sql, rowMapper, encryptPass, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public ArticlePost fetchArticlePostByarticleId(long articleId, String delete_flag_N, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`, DECODE(`name`,?) name,`designation`,`imagePath`,`title`,`content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `articleId` = ? AND `delete_flag` = ?";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			ArticlePost article = jdbcTemplate.queryForObject(sql, rowMapper, encryptPass, articleId, delete_flag_N);
			return article;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticlePost> fetchArticleListByPartyId(long partyId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`, DECODE(`name`,?) name,`designation`,`imagePath`,`title`,`content`, `forumStatusId`, `updated`, `adminId`,`reason`, `delete_flag`,`articleStatusId`,`created_by`,`updated_by` FROM `articlepost` WHERE `delete_flag` = ? AND `partyId` = ? ORDER BY created DESC";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, delete_flag, partyId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int changeArticleStatus(long articleId, long articleStatusId, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `articlepost` SET `articlestatusId`=?,`updated`=?,`updated_by`=? WHERE `articleId`=?";
			int result = jdbcTemplate.update(sql1, articleStatusId, timestamp, signedUserId, articleId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchArticleStatusIdByDesc(String articleDesc) {
		try {
			String sqlForStatusId = "SELECT `id` FROM `articlestatus` WHERE `desc`=?";
			int articleStatusId = jdbcTemplate.queryForObject(sqlForStatusId, Integer.class, articleDesc);
			return articleStatusId;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchEmailIdByPartyId(long partyId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT DECODE(`emailId`,?) FROM `party` WHERE `partyId` = ? AND `delete_flag` = ?";
			String result = jdbcTemplate.queryForObject(sql, String.class, encryptPass, partyId, delete_flag);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int createForumThread(ForumThread fthread) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `forumthread` (`subject`,`partyId`,`forumSubCategoryId`,`forumCategoryId`,`forumStatusId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,?, ?, ?, ?,?,?,?)";
			// jdbcTemplate.update return number of rows affected
			int result = jdbcTemplate.update(sql, fthread.getSubject(), fthread.getPartyId(),
					fthread.getForumSubCategoryId(), fthread.getForumCategoryId(), fthread.getForumStatusId(),
					timestamp, timestamp, fthread.getDelete_flag(), fthread.getCreated_by(), fthread.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ForumThread fetchThread(long threadId, String delete_flag) {
		try {
			String sql = "SELECT `threadId`,`subject`,`partyId`,`forumSubCategoryId`,`forumCategoryId`,`adminId`,`forumStatusId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `forumthread` WHERE `threadId` = ? AND `delete_flag`=?";
			RowMapper<ForumThread> rowMapper = new BeanPropertyRowMapper<ForumThread>(ForumThread.class);
			ForumThread category = jdbcTemplate.queryForObject(sql, rowMapper, threadId, delete_flag);
			return category;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int moderateThread(long threadId, long forumStatusId, String adminId, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `forumthread` SET `forumstatusId`=?,`updated`=?,`adminId`=?,`updated_by`=? WHERE `threadId`=?";
			int result = jdbcTemplate.update(sql1, forumStatusId, timestamp, adminId, signedUserId, threadId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeThread(long threadId, String delete_flag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "DELETE FROM `forumthread` WHERE `threadId`=?";
			int result = jdbcTemplate.update(sql1, threadId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int createForumPost(ForumPost fpost) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `forumpost` (`content`,`partyId`,`forumStatusId`,`created`,`threadId`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?, ?, ?, ?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, fpost.getContent(), fpost.getPartyId(), fpost.getForumStatusId(),
					timestamp, fpost.getThreadId(), timestamp, fpost.getDelete_flag(), fpost.getCreated_by(),
					fpost.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ForumPost fetchPost(long postId, String delete_flag) {
		try {
			String sql = "SELECT `threadId`,`content`,`partyId`,`adminId`,`forumStatusId`,`created`,`threadId`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `forumpost` WHERE `postId` = ? AND `delete_flag`=?";
			RowMapper<ForumPost> rowMapper = new BeanPropertyRowMapper<ForumPost>(ForumPost.class);
			ForumPost forumPost = jdbcTemplate.queryForObject(sql, rowMapper, postId, delete_flag);
			return forumPost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int moderatePost(long postId, long forumStatusId, String adminId, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "UPDATE `forumpost` SET `forumstatusId`=?,`updated`=?,`adminId`=?,`updated_by`=? WHERE `postId`=?";
			int result = jdbcTemplate.update(sql1, forumStatusId, timestamp, adminId, signedUserId, postId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ForumPostVote fetchForumPostVoteByPostId(long postId) {
		try {
			String sql = "SELECT * FROM `forumpostvote` WHERE `postId` = ?";
			RowMapper<ForumPostVote> rowMapper = new BeanPropertyRowMapper<ForumPostVote>(ForumPostVote.class);
			ForumPostVote forumPostVote = jdbcTemplate.queryForObject(sql, rowMapper, postId);
			return forumPostVote;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int firstForumPostVote(ForumPostVote forumPostUpVote) {
		try {
			String sqlInsert = "INSERT INTO `forumpostvote` (`upcount`,`downcount`,`postId`) values (?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, forumPostUpVote.getUp_count(), forumPostUpVote.getDown_count(),
					forumPostUpVote.getPostId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchUpCountByPostId(long postId) {
		try {
			String sql = "SELECT `upcount` FROM `forumpostvote` WHERE `postId`= ?";
			int up = jdbcTemplate.queryForObject(sql, Integer.class, postId);
			return up;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateForumPostUpVote(int upCount, long postId) {
		try {
			String sql1 = "UPDATE `forumpostvote` SET `upcount`=? WHERE `postId`=?";
			int result = jdbcTemplate.update(sql1, upCount, postId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchDownCountByPostId(long postId) {
		try {
			String sql = "SELECT `downcount` FROM `forumpostvote` WHERE `postId`= ?";
			int up = jdbcTemplate.queryForObject(sql, Integer.class, postId);
			return up;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateForumPostDownVote(int downCount, long postId) {
		try {
			String sql1 = "UPDATE `forumpostvote` SET `downcount`=? WHERE `postId`=?";
			int result = jdbcTemplate.update(sql1, downCount, postId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int savePostVote(long voteType, long postId, long partyId, String signedUserId) {
		// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		String vote = null;
		if (voteType == 1) {
			vote = "UP";
		} else {
			vote = "DOWN";
		}
		try {
			String sqlInsert = "INSERT INTO `postvoteaddress` (`postId`,`partyId`,`voteType`,`created`,`updated`,`created_by`,`updated_by`) values (?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sqlInsert, postId, partyId, vote, timestamp, timestamp, signedUserId,
					signedUserId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removePost(long postId, String delete_flag, String signedUserId) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "DELETE FROM `forumpost` WHERE `postId`=?";
			int result = jdbcTemplate.update(sql1, postId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int createQuery(ForumQuery question) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `forumquery` (`query`,`partyId`,`postedToPartyId`,`forumSubCategoryId`,`forumCategoryId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,?, ?, ?, ?,?,?,?)";
			int result = jdbcTemplate.update(sql, question.getQuery(), question.getPartyId(),
					question.getPostedToPartyId(), question.getForumSubCategoryId(), question.getForumCategoryId(),
					timestamp, timestamp, question.getDelete_flag(), question.getCreated_by(),
					question.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// TODO
	@Override
	public ForumQuery fetchForumQueryByPostedToPartyId(long questionId, long partyId, String delete_flag) {
		try {
			String sql = "SELECT `queryId`,`query`,`partyId`,`postedToPartyId`,`forumSubCategoryId`,`forumCategoryId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `forumquery` WHERE `queryId` = ? AND `postedToPartyId` = ? AND `delete_flag` = ?";
			RowMapper<ForumQuery> rowMapper = new BeanPropertyRowMapper<ForumQuery>(ForumQuery.class);
			ForumQuery forumQuestion = jdbcTemplate.queryForObject(sql, rowMapper, questionId, partyId, delete_flag);
			return forumQuestion;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int createForumAnswer(ForumAnswer answer) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "INSERT INTO `forumanswer` (`answer`,`queryId`,`partyId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) values (?,?,?,?, ?,?,?,?)";
			int result = jdbcTemplate.update(sql, answer.getAnswer(), answer.getQueryId(), answer.getPartyId(),
					timestamp, timestamp, answer.getDelete_flag(), answer.getCreated_by(), answer.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ForumQuery> fetchQueryListByPostedToPartyId(long partyId, Pageable pageable, String delete_flag_N) {
		try {
			String sql = "SELECT `queryId`,`query`,`partyId`,`postedToPartyId`,`forumSubCategoryId`,`forumCategoryId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `forumquery` WHERE `postedToPartyId` = ? AND `delete_flag` = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset() + "";
			RowMapper<ForumQuery> rowMapper = new BeanPropertyRowMapper<ForumQuery>(ForumQuery.class);
			List<ForumQuery> forumQuestion = jdbcTemplate.query(sql, rowMapper, partyId, delete_flag_N);
			return forumQuestion;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ForumAnswer> fetchAnswerListByQueryId(long id, Pageable pageable, String delete_flag_N) {
		try {
			String sql = "SELECT `answerId`,`answer`,`queryId`,`partyId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by` FROM `forumanswer` WHERE `queryId` = ? AND `delete_flag` = ? ORDER BY created ASC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset() + "";
			RowMapper<ForumAnswer> rowMapper = new BeanPropertyRowMapper<ForumAnswer>(ForumAnswer.class);
			List<ForumAnswer> forumAnswer = jdbcTemplate.query(sql, rowMapper, id, delete_flag_N);
			return forumAnswer;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<VoteType> fetchVoteTypeList() {
		try {
			String sql = "SELECT `id`, `desc` FROM `votetype`";
			RowMapper<VoteType> rowMapper = new BeanPropertyRowMapper<VoteType>(VoteType.class);
			List<VoteType> voteType = jdbcTemplate.query(sql, rowMapper);
			return voteType;
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
	public int fetchTotalRecentArticlePostList(String delete_flag) {
		try {
			String sql1 = "SELECT COUNT(*) FROM `articlepost` WHERE `delete_flag` = ? ";
			return jdbcTemplate.queryForObject(sql1, Integer.class, delete_flag);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addArticleToFavorite(ArticleFavorite articleFavorite) {
		try {
			String sql = "INSERT INTO `articlefavorite` (`partyId`,`articleId`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag`) values (?,?,?,?, ?,?,?)";
			int result = jdbcTemplate.update(sql, articleFavorite.getPartyId(), articleFavorite.getArticleId(),
					articleFavorite.getCreated(), articleFavorite.getUpdated(), articleFavorite.getCreated_by(),
					articleFavorite.getUpdated_by(), articleFavorite.getDelete_flag());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalAllArticleInApprovedOrder(int forumStatusId, String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlepost` WHERE `forumStatusId` =? AND `delete_flag` = ? ";
			int articlePost = jdbcTemplate.queryForObject(sql, Integer.class, forumStatusId, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Long> fetchFavoriteArticlesByPartyId(long partyId, String delete_flag_N) {
		try {
			String sql = "SELECT `articleId` FROM `articlefavorite` WHERE `partyId`=? AND `delete_flag`=? ORDER BY created DESC";
			List<Long> articleFavorite = jdbcTemplate.queryForList(sql, Long.class, partyId, delete_flag_N);
			return articleFavorite;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchTotalArticlePostList(String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlepost` WHERE `delete_flag` = ?";
			int articlePost = jdbcTemplate.queryForObject(sql, Integer.class, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeArticleFromFavorite(ArticleFavorite articleFavorite) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "DELETE FROM `articlefavorite` WHERE `partyId`=? AND `articleId`=?";
			int result = jdbcTemplate.update(sql1, articleFavorite.getPartyId(), articleFavorite.getArticleId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalArticleCommentByArticleId(long id, String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlecomment` WHERE `articleId` = ? AND `delete_flag`=?";
			int articleComment = jdbcTemplate.queryForObject(sql, Integer.class, id, delete_flag);
			return articleComment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalArticleListByPartyId(long partyId, String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlepost` WHERE `delete_flag` = ? AND `partyId` = ? ";
			return jdbcTemplate.queryForObject(sql, Integer.class, delete_flag, partyId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalQueryListByPostedToPartyId(long partyId, String delete_flag_N) {
		try {
			String sql = "SELECT COUNT(*) FROM `forumquery` WHERE `postedToPartyId` = ? AND `delete_flag` = ?";
			int forumQuestion = jdbcTemplate.queryForObject(sql, Integer.class, partyId, delete_flag_N);
			return forumQuestion;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalAnswerListByQueryId(long id, String delete_flag_N) {
		try {
			String sql = "SELECT COUNT(*) FROM `forumanswer` WHERE `queryId` = ? AND `delete_flag` = ?";
			int forumAnswer = jdbcTemplate.queryForObject(sql, Integer.class, id, delete_flag_N);
			return forumAnswer;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalFavoriteArticlesByPartyId(long partyId, String delete_flag_N) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlefavorite` WHERE `partyId`=? AND `delete_flag`=?";
			int articleFavorite = jdbcTemplate.queryForObject(sql, Integer.class, partyId, delete_flag_N);
			return articleFavorite;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchArticleStatus(long articleStatusId) {
		try {
			String sqlArticleStatus = "SELECT `desc` FROM `articlestatus` WHERE `id` = ?";
			String result = jdbcTemplate.queryForObject(sqlArticleStatus, String.class, articleStatusId);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticlePost> fetchArticleListByArticleIdList(List<Long> articleIdList, String delete_flag,
			String encryptPass) {
		try {

			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`, DECODE(`name`,?) name,`designation`,`imagePath`,`title`,`content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE delete_flag =? AND `articleId` IN (%1$s) ORDER BY FIELD(`articleId`,%2$s)";
			String inSql = StringUtils.join(articleIdList, ',');
			// String str = "'" + inSql + "'";
			String sqlArticle = String.format(sql, inSql, inSql);
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			return jdbcTemplate.query(sqlArticle, rowMapper, encryptPass, delete_flag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int checkPartyIsPresent(long partyId) {
		try {
			String sql = "SELECT count(*) FROM `party` WHERE `partyId` = ?";
			int party = jdbcTemplate.queryForObject(sql, Integer.class, partyId);
			return party;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkForumSubCategoryIsPresent(long forumSubCategoryId) {
		try {
			String sql = "SELECT count(*) FROM `forumsubcategory` WHERE `forumSubCategoryId` = ?";
			int forumSubCategory = jdbcTemplate.queryForObject(sql, Integer.class, forumSubCategoryId);
			return forumSubCategory;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkThreadIsPresent(long threadId, String delete_flag_N) {
		try {
			String sql = "SELECT count(*) FROM `forumthread` WHERE `threadId` = ? AND `delete_flag`=?";
			int category = jdbcTemplate.queryForObject(sql, Integer.class, threadId, delete_flag_N);
			return category;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkPostIsPresent(long postId, String delete_flag) {
		try {
			String sql = "SELECT count(*) FROM `forumpost` WHERE `postId` = ? AND `delete_flag`=?";
			int forumPost = jdbcTemplate.queryForObject(sql, Integer.class, postId, delete_flag);
			return forumPost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkForumQueryByPostedToPartyId(long questionId, long partyId, String delete_flag) {
		try {
			String sql = "SELECT count(*) FROM `forumquery` WHERE `queryId` = ? AND `postedToPartyId` = ? AND `delete_flag` = ?";
			int forumPost = jdbcTemplate.queryForObject(sql, Integer.class, questionId, partyId, delete_flag);
			return forumPost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	// @Override
	// public List<ArticleComment> fetchArticleReplyCommentByParentId(long
	// parentCommentId, Pageable pageable,
	// String delete_flag, String encryptPass) {
	// try {
	// String sql = "SELECT `commentId`,
	// `parentCommentId`,`created`,`partyId`,DECODE(`name`,?)
	// name,`designation`,`imagePath`, `content`, `forumStatusId`,
	// `updated`,`articleId`,`adminId`, `delete_flag`,`created_by`,`updated_by` FROM
	// `articlecomment` WHERE `parentCommentId` = ? AND `delete_flag`=? ORDER BY
	// created ASC LIMIT "
	// + pageable.getPageSize() + " OFFSET " + pageable.getOffset() + "";
	// RowMapper<ArticleComment> rowMapper = new
	// BeanPropertyRowMapper<ArticleComment>(ArticleComment.class);
	// List<ArticleComment> articleComment = jdbcTemplate.query(sql, rowMapper,
	// encryptPass, parentCommentId,
	// delete_flag);
	// return articleComment;
	// } catch (EmptyResultDataAccessException e) {
	// logger.error(e.getMessage());
	// return null;
	// }
	// }

	@Override
	public int fetchTotalArticleCommentByParentId(long id, long parentId, String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlecomment` WHERE `articleId` = ?  AND `parentCommentId` = ? AND `delete_flag`=?";
			int articleComment = jdbcTemplate.queryForObject(sql, Integer.class, id, parentId, delete_flag);
			return articleComment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ArticleComment> fetchArticleCommentByParentId(long id, long parentId, String delete_flag,
			String encryptPass) {
		try {
			String sql = "SELECT `commentId`, `parentCommentId`,`created`,`partyId`,DECODE(`name`,?) name,`designation`,`imagePath`, `content`, `forumStatusId`, `updated`,`articleId`,`adminId`, `delete_flag`,`created_by`,`updated_by` FROM `articlecomment` WHERE  `articleId` = ? AND `parentCommentId` = ? AND `delete_flag`=? ORDER BY created ASC";
			RowMapper<ArticleComment> rowMapper = new BeanPropertyRowMapper<ArticleComment>(ArticleComment.class);
			List<ArticleComment> articleComment = jdbcTemplate.query(sql, rowMapper, encryptPass, id, parentId,
					delete_flag);
			return articleComment;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchArticleUpCount(long articleId) {
		try {
			String sql = "SELECT `upCount` FROM `articlevote` WHERE `articleId`=?";
			int upCount = jdbcTemplate.queryForObject(sql, Integer.class, articleId);
			return upCount;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	
	
	@Override
	public int modifyArticlePost(ArticlePost modifypost, String encryptPass) {
		try {
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql = "UPDATE `articlepost` SET `title`=? ,`imagePath`=?, `content`=?,`forumStatusId`=?,`prodId`=?,`updated`=?,`updated_by`=? WHERE `articleId`=?";
			int result = jdbcTemplate.update(sql, modifypost.getTitle(), modifypost.getImagePath(),modifypost.getContent(),
					modifypost.getForumStatusId(), modifypost.getProdId(), timestamp, modifypost.getUpdated_by(),
					modifypost.getArticleId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public ArticleVoteAddress fetchArticleVoteAddress(long articleId, long partyId) {
		try {
			String sql = "SELECT * FROM `articlevoteaddress` WHERE `articleId`=? AND `partyId`=?";
			RowMapper<ArticleVoteAddress> rowMapper = new BeanPropertyRowMapper<ArticleVoteAddress>(
					ArticleVoteAddress.class);
			ArticleVoteAddress articleVoteAddress = jdbcTemplate.queryForObject(sql, rowMapper, articleId, partyId);
			return articleVoteAddress;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int removeArticleVoteAddress(long articleId, long partyId) {
		try {
			String sql = "DELETE FROM `articlevoteaddress` WHERE `articleId`=? AND `partyId`=?";
			int result = jdbcTemplate.update(sql, articleId, partyId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public Advisor fetchPublicAdvisorByAdvId(String roleBasedId, String deleteflag, String encryptPass) {
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
					encryptPass, encryptPass, encryptPass, roleBasedId, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchTotalArticleListByStatusId(long forumStatusId, String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlepost` WHERE `delete_flag` = ? AND `forumStatusId` = ?";
			return jdbcTemplate.queryForObject(sql, Integer.class, delete_flag, forumStatusId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ArticlePost> fetchArticleListByStatusId(Pageable pageable, long forumStatusId, String delete_flag_N,
			String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`, DECODE(`name`,?) name, `title`, `designation`,`imagePath`,`content`, `forumStatusId`, `updated`, `adminId`,`reason`, `delete_flag`,`articleStatusId`,`created_by`,`updated_by` FROM `articlepost` WHERE `delete_flag` = ? AND `forumStatusId` = ? ORDER BY updated DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset() + "";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, delete_flag_N, forumStatusId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticlePost> searchArticleByTitle(Pageable pageable, String title, String forumStatusId,
			String deleteflag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`, DECODE(`name`,?) name, `title`,`designation`,`imagePath`,`content`, `forumStatusId`, `updated`, `adminId`,`reason`, `delete_flag`,`articleStatusId`,`created_by`,`updated_by` FROM `articlepost` WHERE `delete_flag` = ? AND `title` LIKE '%"
					+ title + "%' AND `forumStatusId` LIKE '%" + forumStatusId + "%' ORDER BY updated DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset() + "";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			return jdbcTemplate.query(sql, rowMapper, encryptPass, deleteflag);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchTotalSearchArticleByTitle(String deleteflag, String title, String forumStatusId) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlepost` WHERE delete_flag =? AND `title` LIKE '%" + title
					+ "%' AND `forumStatusId` LIKE '%" + forumStatusId + "%'";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, deleteflag);
			return count;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<ArticlePost> fetchAllArticleInApprovedOrderWithoutToken(int forumStatusId, int articleStatusId,
			String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `prodId`, `partyId`,DECODE(`name`,?) name,`designation`,`imagePath`, `title`,`url`,`content`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `forumStatusId` =? AND `articleStatusId` =? AND `delete_flag` = ? ORDER BY `updated` DESC LIMIT 10 ";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			List<ArticlePost> articlePost = jdbcTemplate.query(sql, rowMapper, encryptPass, forumStatusId,
					articleStatusId, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<ArticlePost> fetchAllArticleInApprovedOrderWTByProdId(int prodId, int forumStatusId,
			int articleStatusId, String delete_flag, String encryptPass) {
		try {
			String sql = "SELECT `articleId`, `created`, `partyId`,DECODE(`name`,?) name,`designation`,`imagePath`, `title`,`url`,`content`,`prodId`, `forumStatusId`, `updated`, `adminId`, `reason`, `delete_flag`,`created_by`,`updated_by` FROM `articlepost` WHERE `prodId` = ? AND `forumStatusId` =? AND `articleStatusId` =? AND `delete_flag` = ? ORDER BY `updated` DESC LIMIT 10 ";
			RowMapper<ArticlePost> rowMapper = new BeanPropertyRowMapper<ArticlePost>(ArticlePost.class);
			List<ArticlePost> articlePost = jdbcTemplate.query(sql, rowMapper, encryptPass, prodId, forumStatusId,
					articleStatusId, delete_flag);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String fetchArticlePostByTitle(String title, String deleteflag) {
		try {
			String sql = "SELECT `url` FROM `articlepost` WHERE `url` = ? AND `delete_flag`=?";
			String result = jdbcTemplate.queryForObject(sql, String.class, title, deleteflag);
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchCountForUrl(String url) {
		try {
			String sqlId = "SELECT count(*) FROM `articlepost` WHERE `url` LIKE '%" + url + "%'";
			int urlCount = jdbcTemplate.queryForObject(sqlId, Integer.class);
			return urlCount;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int fetchTotalAllArticleInApprovedOrder(int forumStatusId, String delete_flag, int prodId) {
		try {
			String sql = "SELECT COUNT(*) FROM `articlepost` WHERE `forumStatusId` =? AND `delete_flag` = ? AND `prodId` = ?";
			int articlePost = jdbcTemplate.queryForObject(sql, Integer.class, forumStatusId, delete_flag, prodId);
			return articlePost;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int removeArticleFromMyPost(ArticlePost articlePost) {
		try {
			// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
			Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
			String sql1 = "DELETE FROM `articlepost` WHERE `partyId`=? AND `articleId`=?";
			int result = jdbcTemplate.update(sql1, articlePost.getPartyId(), articlePost.getArticleId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}
}
