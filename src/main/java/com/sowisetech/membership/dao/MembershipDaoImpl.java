package com.sowisetech.membership.dao;

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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.calc.model.Party;
import com.sowisetech.forum.dao.ForumDaoImpl;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubPaymentCard;
import com.sowisetech.membership.model.SubscriptionPayment;

@Transactional
@Repository
public class MembershipDaoImpl implements MembershipDao {

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
	public int addMembershipPlan(MembershipPlan membershipPlan) {
		try {
			String sql = "INSERT INTO `membershipplan` (`razorpayPlanId`,`period`,`interval`,`itemId`,`active`,`name`,`description`,`amount`,`currency`,`planName`,`content`,`created_at`,`updated_at`,`delete_flag`) values (?,?,?,?, ?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, membershipPlan.getRazorpayPlanId(), membershipPlan.getPeriod(),
					membershipPlan.getInterval(), membershipPlan.getItemId(), membershipPlan.getActive(),
					membershipPlan.getName(), membershipPlan.getDescription(), membershipPlan.getAmount(),
					membershipPlan.getCurrency(), membershipPlan.getPlanName(), membershipPlan.getContent(),
					membershipPlan.getCreated_at(), membershipPlan.getUpdated_at(), membershipPlan.getDelete_flag());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addSubscription(MemberSubscription membershipSub) {
		try {
			String sql = "INSERT INTO `subscription` (`advId`,`razorpaySubId`,`razorpayPlanId`,`customer_id`,`status`,`current_start`,`current_end`,`ended_at`,`quantity`,`charge_at`,`start_at`,`end_at`,`auth_attempts`,`total_count`,`paid_count`,`customer_notify`,`created_at`,`expire_by`,`short_url`,`has_scheduled_changes`,`change_scheduled_at`,`source`,`remaining_count`,`payment_method`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, membershipSub.getAdvId(), membershipSub.getRazorpaySubId(),
					membershipSub.getRazorpayPlanId(), membershipSub.getCustomer_id(), membershipSub.getStatus(),
					membershipSub.getCurrent_start(), membershipSub.getCurrent_end(), membershipSub.getEnded_at(),
					membershipSub.getQuantity(), membershipSub.getCharge_at(), membershipSub.getStart_at(),
					membershipSub.getEnd_at(), membershipSub.getAuth_attempts(), membershipSub.getTotal_count(),
					membershipSub.getPaid_count(), membershipSub.isCustomer_notify(), membershipSub.getCreated_at(),
					membershipSub.getExpire_by(), membershipSub.getShort_url(), membershipSub.isHas_scheduled_changes(),
					membershipSub.getChange_scheduled_at(), membershipSub.getSource(),
					membershipSub.getRemaining_count(), membershipSub.getPayment_method());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateSubscription(MemberSubscription membershipSub) {
		try {
			String sql = "UPDATE `subscription` SET `razorpayPlanId`=?,`customer_id`=?,`status`=?,`current_start`=?,`current_end`=?,`ended_at`=?,`quantity`=?,`charge_at`=?,`start_at`=?,`end_at`=?,`auth_attempts`=?,`total_count`=?,`paid_count`=?,`customer_notify`=?,`expire_by`=?,`has_scheduled_changes`=?,`change_scheduled_at`=?,`source`=?,`remaining_count`=?,`payment_method`=? WHERE `razorpaySubId`=?";
			int result = jdbcTemplate.update(sql, membershipSub.getRazorpayPlanId(), membershipSub.getCustomer_id(),
					membershipSub.getStatus(), membershipSub.getCurrent_start(), membershipSub.getCurrent_end(),
					membershipSub.getEnded_at(), membershipSub.getQuantity(), membershipSub.getCharge_at(),
					membershipSub.getStart_at(), membershipSub.getEnd_at(), membershipSub.getAuth_attempts(),
					membershipSub.getTotal_count(), membershipSub.getPaid_count(), membershipSub.isCustomer_notify(),
					membershipSub.getExpire_by(), membershipSub.isHas_scheduled_changes(),
					membershipSub.getChange_scheduled_at(), membershipSub.getSource(),
					membershipSub.getRemaining_count(), membershipSub.getPayment_method(),
					membershipSub.getRazorpaySubId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateSubscriptionPayment(SubscriptionPayment subscriptionPayment, String encryptPass) {
		try {
			String sql = "INSERT IGNORE INTO `subscription_payment` (`razorpaySubId`,`razorpayPaymentId`,`amount`,`currency`,`status`,`order_id`,`invoice_id`,`international`,`method`,`amount_refunded`,`amount_transferred`,`refund_status`,`captured`,`description`,`card_id`,`bank`,`wallet`,`vpa`,`emailId`,`contact`,`customer_id`,`token_id`,`fee`,`tax`,`error_code`,`error_description`,`created_at`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,ENCODE(?,?),ENCODE(?,?),?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, subscriptionPayment.getRazorpaySubId(),
					subscriptionPayment.getRazorpayPaymentId(), subscriptionPayment.getAmount(),
					subscriptionPayment.getCurrency(), subscriptionPayment.getStatus(),
					subscriptionPayment.getOrder_id(), subscriptionPayment.getInvoice_id(),
					subscriptionPayment.isInternational(), subscriptionPayment.getMethod(),
					subscriptionPayment.getAmount_refunded(), subscriptionPayment.getAmount_transferred(),
					subscriptionPayment.getRefund_status(), subscriptionPayment.getCaptured(),
					subscriptionPayment.getDescription(), subscriptionPayment.getCard_id(),
					subscriptionPayment.getBank(), subscriptionPayment.getWallet(), subscriptionPayment.getVpa(),
					subscriptionPayment.getEmailId(), encryptPass, subscriptionPayment.getContact(), encryptPass,
					subscriptionPayment.getCustomer_id(), subscriptionPayment.getToken_id(),
					subscriptionPayment.getFee(), subscriptionPayment.getTax(), subscriptionPayment.getError_code(),
					subscriptionPayment.getError_description(), subscriptionPayment.getCreated_at());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateSubPaymentCard(SubPaymentCard subPaymentCard, String encryptPass) {
		try {
			String sql = "INSERT INTO `sub_payment_card` (`razorpayPaymentId`,`razorpayCardId`,`name`,`last4`,`network`,`type`,`issuer`,`international`,`emi`,`expiry_month`,`expiry_year`) values (?,?,?,ENCODE(?,?),?,?,?,?,?,ENCODE(?,?),ENCODE(?,?))";
			int result = jdbcTemplate.update(sql, subPaymentCard.getRazorpayPaymentId(),
					subPaymentCard.getRazorpayCardId(), subPaymentCard.getName(), subPaymentCard.getLast4(),
					encryptPass, subPaymentCard.getNetwork(), subPaymentCard.getType(), subPaymentCard.getIssuer(),
					subPaymentCard.isInternational(), subPaymentCard.isEmi(), subPaymentCard.getExpiry_month(),
					encryptPass, subPaymentCard.getExpiry_year(), encryptPass);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public MemberSubscription fetchMemberSubByAdvId(String advId, String statusCancelled, String statusCompleted) {
		try {
			String sql = "SELECT * FROM `subscription` WHERE `advId` = ? AND `status`!=? AND `status`!=?";
			RowMapper<MemberSubscription> memberMapper = new BeanPropertyRowMapper<MemberSubscription>(
					MemberSubscription.class);
			return jdbcTemplate.queryForObject(sql, memberMapper, advId, statusCancelled, statusCompleted);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<String> fetchInvoiceIdFromSubPayment(String sub_id) {
		try {
			String sqlType = "SELECT `invoice_id` FROM `subscription_payment` WHERE `razorpaySubId`= ?";
			List<String> invoice_id = jdbcTemplate.queryForList(sqlType, String.class, sub_id);
			return invoice_id;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public int fetchPaymentIdFromSubPay(String razorpayPaymentId) {
		try {
			String sql1 = "SELECT COUNT(*) FROM `subscription_payment` WHERE `razorpayPaymentId` = ?";
			return jdbcTemplate.queryForObject(sql1, Integer.class, razorpayPaymentId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<MembershipPlan> fetchAllMembershipPlan(String delete_flag) {
		try {
			String sql = "SELECT `membershipPlanId`, `razorpayPlanId`, `period`, `interval`, `itemId`,`active`,`name`,`description`, `amount`, `currency`,`planName`,`content`, `created_at`, `updated_at` FROM `membershipplan` WHERE `delete_flag`=?";
			RowMapper<MembershipPlan> rowMapper = new BeanPropertyRowMapper<MembershipPlan>(MembershipPlan.class);
			List<MembershipPlan> membershipPlans = jdbcTemplate.query(sql, rowMapper, delete_flag);
			return membershipPlans;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchMemPlanTotalList(String delete_flag) {
		try {
			String sql = "SELECT COUNT(*) FROM `membershipplan` WHERE `delete_flag`=?";
			int plans = jdbcTemplate.queryForObject(sql, Integer.class, delete_flag);
			return plans;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public List<MemberSubscription> fetchAllMemberSubscription(Pageable pageable, int pageNum, int records) {
		try {
			String sql = "SELECT `sub_id`, `advId`, `razorpaySubId`, `razorpayPlanId`, `customer_id`,`status`,`current_start`,`current_end`, `ended_at`, `quantity`, `charge_at`, `start_at`,`end_at`, `auth_attempts`, `total_count`, `paid_count`, `customer_notify`, `created_at`, `expire_by`, `short_url`, `has_scheduled_changes`, `change_scheduled_at`, `source`, `remaining_count`, `payment_method` FROM `subscription` ORDER BY `created_at` DESC LIMIT "
					+ pageable.getPageSize() + " OFFSET " + pageable.getOffset() + " ";
			RowMapper<MemberSubscription> rowMapper = new BeanPropertyRowMapper<MemberSubscription>(
					MemberSubscription.class);
			List<MemberSubscription> memberSubscriptions = jdbcTemplate.query(sql, rowMapper);
			return memberSubscriptions;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int fetchMemSubscriptionTotalList() {
		try {
			String sql = "SELECT COUNT(*) FROM `subscription`";
			int subs = jdbcTemplate.queryForObject(sql, Integer.class);
			return subs;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int checkAdvisorIsPresent(String advId, String deleteflag) {
		try {
			String sql = "SELECT count(*) FROM `party` WHERE `roleBasedId`= ? AND `delete_flag` = ?";
			int advisor = jdbcTemplate.queryForObject(sql, Integer.class, advId, deleteflag);
			return advisor;
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public int addSinglePaymentOrder(SinglePayment singlePayment) {
		try {
			String sql = "INSERT INTO `singlepayment` (`roleBasedId`,`name`,`emailId`,`phoneNumber`,`order_id`,`entity`,`amount`,`amount_paid`,`amount_due`,`currency`,`receipt`,`offer_id`,`status`,`attempts`,`created_at`,`type`,`plan_id`,`period`,`total_count`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					statement.setString(1, singlePayment.getRoleBasedId());
					statement.setString(2, singlePayment.getName());
					statement.setString(3, singlePayment.getEmailId());
					statement.setString(4, singlePayment.getPhoneNumber());
					statement.setString(5, singlePayment.getOrder_id());
					statement.setString(6, singlePayment.getEntity());
					statement.setInt(7, singlePayment.getAmount());
					statement.setInt(8, singlePayment.getAmount_paid());
					statement.setInt(9, singlePayment.getAmount_due());
					statement.setString(10, singlePayment.getCurrency());
					statement.setString(11, singlePayment.getReceipt());
					statement.setString(12, singlePayment.getOffer_id());
					statement.setString(13, singlePayment.getStatus());
					statement.setInt(14, singlePayment.getAttempts());
					statement.setObject(15, singlePayment.getCreated_at());
					statement.setString(16, singlePayment.getType());
					statement.setString(17, singlePayment.getPlan_id());
					statement.setString(18, singlePayment.getPeriod());
					statement.setInt(19, singlePayment.getTotal_count());
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
	public SinglePayment fetchSinglePaymentByPrimaryKey(int singlePaymentId) {
		try {
			String sql = "SELECT * FROM `singlepayment` WHERE `singlePaymentId`=?";
			RowMapper<SinglePayment> singlePayment = new BeanPropertyRowMapper<SinglePayment>(SinglePayment.class);
			return jdbcTemplate.queryForObject(sql, singlePayment, singlePaymentId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updatePaymentDetails(SubscriptionPayment subscriptionPayment, int singlePaymentId,
			SinglePayment singlePayment) {
		try {
			String sql = "UPDATE `singlepayment` SET `razor_payment_id`=?,`payment_amount`=?,`status`=?,`invoice_id`=?,`international`=?,`method`=?,`amount_refunded`=?,`refund_status`=?,`captured`=?,`description`=?,`card_id`=?,`bank`=?,`wallet`=?,`vpa`=?,`fee`=?,`tax`=?,`error_code`=?,`error_description`=?,`error_source`=?,`error_step`=?,`error_reason`=?,`payment_created_at`=?,`subStartedAt`=?,`subEndAt`=? WHERE `singlePaymentId`=?";
			int result = jdbcTemplate.update(sql, subscriptionPayment.getRazorpayPaymentId(),
					subscriptionPayment.getAmount(), subscriptionPayment.getStatus(),
					subscriptionPayment.getInvoice_id(), subscriptionPayment.isInternational(),
					subscriptionPayment.getMethod(), subscriptionPayment.getAmount_refunded(),
					subscriptionPayment.getRefund_status(), subscriptionPayment.isSinglepay_captured(),
					subscriptionPayment.getDescription(), subscriptionPayment.getCard_id(),
					subscriptionPayment.getBank(), subscriptionPayment.getWallet(), subscriptionPayment.getVpa(),
					subscriptionPayment.getFee(), subscriptionPayment.getTax(), subscriptionPayment.getError_code(),
					subscriptionPayment.getError_description(), subscriptionPayment.getError_source(),
					subscriptionPayment.getError_step(), subscriptionPayment.getError_reason(),
					subscriptionPayment.getPay_created_at(), singlePayment.getSubStartedAt(),
					singlePayment.getSubEndAt(), singlePaymentId);
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchSubOrderNumber() {
		try {
			String sql1 = "SELECT `id` FROM `subordernumber` ORDER BY `s_no` DESC LIMIT 1";
			return jdbcTemplate.queryForObject(sql1, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addSubOrderNumber(String newId) {
		try {
			String sqlInsert = "INSERT INTO `subordernumber` (`id`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
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
	public int addOrder(OrderDetail invoice, String encryptPass) {
		try {
			String sql = "INSERT INTO `orderdetail` (`orderDetailId`,`roleBasedId`,`emailId`,`phoneNumber`,`name`,`razorpay_plan_id`,`subscription_id`,`razorpay_order_id`,`type`,`status`,`created`,`created_by`, `updated`,`updated_by` ) VALUES (?,?,ENCODE(?,?),ENCODE(?,?),ENCODE(?,?),?,?,?,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(sql, invoice.getOrderDetailId(), invoice.getRoleBasedId(),
					invoice.getEmailId(), encryptPass, invoice.getPhoneNumber(), encryptPass, invoice.getName(),
					encryptPass, invoice.getRazorpay_plan_id(), invoice.getSubscription_id(),
					invoice.getRazorpay_order_id(), invoice.getType(), invoice.getStatus(), invoice.getCreated(),
					invoice.getCreated_by(), invoice.getUpdated(), invoice.getUpdated_by());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public String fetchPaymentOrderNumber() {
		try {
			String sql1 = "SELECT `id` FROM `payordernumber` ORDER BY `s_no` DESC LIMIT 1";
			return jdbcTemplate.queryForObject(sql1, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int addPaymentOrderNumber(String newId) {
		try {
			String sqlInsert = "INSERT INTO `payordernumber` (`id`) values (?)";
			return jdbcTemplate.update(sqlInsert, newId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public OrderDetail fetchOrderDetail(String orderNumber, String encryptPass) {
		try {
			String sql = "SELECT `orderDetailId`,`roleBasedId`,DECODE(`emailId`,?) emailId,DECODE(`phoneNumber`,?) phoneNumber,DECODE(`name`,?) name,`razorpay_plan_id`,`status`,`type`,`subscription_id`,`razorpay_order_id`,`created`,`created_by`, `updated`,`updated_by` FROM `orderdetail` WHERE `orderDetailId`=?";
			RowMapper<OrderDetail> singlePayment = new BeanPropertyRowMapper<OrderDetail>(OrderDetail.class);
			return jdbcTemplate.queryForObject(sql, singlePayment, encryptPass, encryptPass, encryptPass, orderNumber);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int updateOrderDetail(OrderDetail orderDetail) {
		try {
			String sql = "UPDATE `orderdetail` SET `razorpay_plan_id`=?,`status`=?,`subscription_id`=?, `razorpay_order_id`=?, `razorpay_payment_id`=?,`updated`=?,`updated_by`=? WHERE `orderDetailId`=?";
			int result = jdbcTemplate.update(sql, orderDetail.getRazorpay_plan_id(), orderDetail.getStatus(),
					orderDetail.getSubscription_id(), orderDetail.getRazorpay_order_id(),
					orderDetail.getRazorpay_payment_id(), orderDetail.getUpdated(), orderDetail.getUpdated_by(),
					orderDetail.getOrderDetailId());
			return result;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	@Override
	public SinglePayment fetchMemberSinglePaySubByAdvId(String advId, String singlePaidStatus, String type) {
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		try {
			String sql = "SELECT * FROM `singlepayment` WHERE `roleBasedId` = ? AND `status`=? AND type=?  AND `subEndAt`> ? ORDER BY created_at DESC LIMIT 1";
			RowMapper<SinglePayment> memberMapper = new BeanPropertyRowMapper<SinglePayment>(SinglePayment.class);
			return jdbcTemplate.queryForObject(sql, memberMapper, advId, singlePaidStatus, type, timestamp);
		} catch (EmptyResultDataAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
