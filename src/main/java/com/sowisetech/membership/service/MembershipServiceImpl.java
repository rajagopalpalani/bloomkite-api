package com.sowisetech.membership.service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.calc.model.Party;
import com.sowisetech.common.util.AdminSignin;
import com.sowisetech.membership.dao.MembershipDao;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubPaymentCard;
import com.sowisetech.membership.model.SubscriptionPayment;
import com.sowisetech.membership.request.CardPaymentRequest;
import com.sowisetech.membership.request.PaymentEntityRequest;
import com.sowisetech.membership.request.SubscriptionChargedRequest;
import com.sowisetech.membership.request.SubscriptionEntityRequest;
import com.sowisetech.membership.util.RazorpayProperties;

@Transactional(readOnly = true)
@Service("MembershipService")
public class MembershipServiceImpl implements MembershipService {

	@Autowired
	private MembershipDao membershipDao;
	@Autowired
	private RazorpayProperties razorpayProperties;
	@Autowired
	AdvTableFields advTableFields;
	@Autowired
	AdminSignin adminSignin;

	private static final Logger logger = LoggerFactory.getLogger(MembershipServiceImpl.class);
	@Value("${secretId}")
	private String secretId;

	@Transactional
	public int addMembershipPlan(MembershipPlan membershipPlan) {
		String delete_flag = advTableFields.getDelete_flag_N();
		membershipPlan.setDelete_flag(delete_flag);
		int result = membershipDao.addMembershipPlan(membershipPlan);
		return result;
	}

	@Transactional
	public int addSubscription(String advId, MemberSubscription membershipSub) {
		membershipSub.setAdvId(advId);
		int result1 = membershipDao.addSubscription(membershipSub);
		return result1;
	}

	@Transactional
	public int updateSubscription(String advId, MemberSubscription membershipSub) {
		membershipSub.setAdvId(advId);
		int result1 = membershipDao.updateSubscription(membershipSub);
		return result1;
	}

	@Transactional
	public int updateSubscriptionWebHook(SubscriptionChargedRequest subscriptionChargedRequest) {
		String encryptPass = razorpayProperties.getEncryption_password();
		MemberSubscription memberSubscription = getSubscriptionValue(
				subscriptionChargedRequest.getPayload().getSubscription().getEntity());
		int result1 = membershipDao.updateSubscription(memberSubscription);
		if (result1 == 0) {
			return result1;
		}
		if (subscriptionChargedRequest.getPayload().getPayment() != null) {
			SubscriptionPayment subscriptionPayment = getSubPaymentValue(
					subscriptionChargedRequest.getPayload().getPayment().getEntity());
			subscriptionPayment.setRazorpaySubId(memberSubscription.getRazorpaySubId());
			int count = membershipDao.fetchPaymentIdFromSubPay(subscriptionPayment.getRazorpayPaymentId());
			int result2 = 0;
			if (count == 0) {
				result2 = membershipDao.updateSubscriptionPayment(subscriptionPayment, encryptPass);
				if (result2 == 0) {
					return result2;
				}
				if (subscriptionChargedRequest.getPayload().getPayment().getEntity().getCard() != null) {
					SubPaymentCard subPaymentCard = getSubPaymentCardValue(
							subscriptionChargedRequest.getPayload().getPayment().getEntity().getCard());
					subPaymentCard.setRazorpayPaymentId(subscriptionPayment.getRazorpayPaymentId());
					int result3 = membershipDao.updateSubPaymentCard(subPaymentCard, encryptPass);
					if (result3 == 0) {
						return result3;
					}
					return result3;
				}
			}

		}
		return result1;
	}

	@Transactional
	private SubPaymentCard getSubPaymentCardValue(CardPaymentRequest card) {
		SubPaymentCard subPaymentCard = new SubPaymentCard();
		subPaymentCard.setRazorpayCardId(card.getId());
		subPaymentCard.setName(card.getName());
		subPaymentCard.setLast4(card.getLast4());
		subPaymentCard.setNetwork(card.getNetwork());
		subPaymentCard.setType(card.getType());
		subPaymentCard.setIssuer(card.getIssuer());
		subPaymentCard.setInternational(card.isInternational());
		subPaymentCard.setEmi(card.isEmi());
		subPaymentCard.setExpiry_month(card.getExpiry_month());
		subPaymentCard.setExpiry_year(card.getExpiry_year());
		return subPaymentCard;
	}

	@Transactional
	private SubscriptionPayment getSubPaymentValue(PaymentEntityRequest entity) {
		SubscriptionPayment subscriptionPayment = new SubscriptionPayment();
		subscriptionPayment.setRazorpayPaymentId(entity.getId());
		subscriptionPayment.setAmount(entity.getAmount());
		subscriptionPayment.setCurrency(entity.getCurrency());
		subscriptionPayment.setStatus(entity.getStatus());
		subscriptionPayment.setOrder_id(entity.getOrder_id());
		subscriptionPayment.setInvoice_id(entity.getInvoice_id());
		subscriptionPayment.setInternational(entity.isInternational());
		subscriptionPayment.setAmount_refunded(entity.getAmount_refunded());
		subscriptionPayment.setAmount_transferred(entity.getAmount_transferred());
		subscriptionPayment.setRefund_status(entity.getRefund_status());
		subscriptionPayment.setCaptured(entity.getCaptured());
		subscriptionPayment.setDescription(entity.getDescription());
		subscriptionPayment.setCard_id(entity.getCard_id());
		subscriptionPayment.setBank(entity.getBank());
		subscriptionPayment.setWallet(entity.getWallet());
		subscriptionPayment.setVpa(entity.getVpa());
		subscriptionPayment.setEmailId(entity.getEmail());
		subscriptionPayment.setContact(entity.getContact());
		subscriptionPayment.setCustomer_id(entity.getCustomer_id());
		subscriptionPayment.setToken_id(entity.getToken_id());
		subscriptionPayment.setFee(entity.getFee());
		subscriptionPayment.setTax(entity.getTax());
		subscriptionPayment.setError_code(entity.getError_code());
		subscriptionPayment.setError_description(entity.getError_description());
		subscriptionPayment.setCreated_at(entity.getCreated_at());
		return subscriptionPayment;
	}

	@Transactional
	private MemberSubscription getSubscriptionValue(SubscriptionEntityRequest subscriptionEntityRequest) {
		MemberSubscription memberSubscription = new MemberSubscription();
		memberSubscription.setRazorpaySubId(subscriptionEntityRequest.getId());
		memberSubscription.setRazorpayPlanId(subscriptionEntityRequest.getPlan_id());
		memberSubscription.setCustomer_id(subscriptionEntityRequest.getCustomer_id());
		memberSubscription.setStatus(subscriptionEntityRequest.getStatus());
		memberSubscription.setCurrent_start(subscriptionEntityRequest.getCurrent_start());
		memberSubscription.setCurrent_end(subscriptionEntityRequest.getCurrent_end());
		memberSubscription.setEnded_at(subscriptionEntityRequest.getEnded_at());
		memberSubscription.setQuantity(subscriptionEntityRequest.getQuantity());
		memberSubscription.setCharge_at(subscriptionEntityRequest.getCharge_at());
		memberSubscription.setStart_at(subscriptionEntityRequest.getStart_at());
		memberSubscription.setEnd_at(subscriptionEntityRequest.getEnd_at());
		memberSubscription.setAuth_attempts(subscriptionEntityRequest.getAuth_attempts());
		memberSubscription.setTotal_count(subscriptionEntityRequest.getTotal_count());
		memberSubscription.setPaid_count(subscriptionEntityRequest.getPaid_count());
		memberSubscription.setCustomer_notify(subscriptionEntityRequest.isCustomer_notify());
		memberSubscription.setCreated_at(subscriptionEntityRequest.getCreated_at());
		memberSubscription.setExpire_by(subscriptionEntityRequest.getExpire_by());
		memberSubscription.setHas_scheduled_changes(subscriptionEntityRequest.isHas_scheduled_changes());
		memberSubscription.setChange_scheduled_at(subscriptionEntityRequest.getChange_scheduled_at());
		memberSubscription.setRemaining_count(subscriptionEntityRequest.getRemaining_count());
		memberSubscription.setSource(subscriptionEntityRequest.getSource());
		memberSubscription.setShort_url(subscriptionEntityRequest.getShort_url());
		return memberSubscription;
	}

	@Transactional
	public MemberSubscription fetchMemberSubByadvId(String advId) {
		MemberSubscription memberSubscription = membershipDao.fetchMemberSubByAdvId(advId,
				razorpayProperties.getStatus_cancelled(), razorpayProperties.getStatus_completed());
		if (memberSubscription != null) {
			memberSubscription.setSecretId(secretId);
		}
		return memberSubscription;
	}

	@Transactional
	public List<String> fetchInvoiceIdFromSubPayment(String sub_id) {
		return membershipDao.fetchInvoiceIdFromSubPayment(sub_id);
	}

	@Transactional
	public List<MembershipPlan> fetchAllMembershipPlan() {
		String delete_flag = advTableFields.getDelete_flag_N();
		List<MembershipPlan> plans = membershipDao.fetchAllMembershipPlan(delete_flag);
		return plans;
	}

	@Transactional
	public int fetchMemPlanTotalList() {
		String delete_flag = advTableFields.getDelete_flag_N();
		return membershipDao.fetchMemPlanTotalList(delete_flag);
	}

	@Transactional
	public List<MemberSubscription> fetchAllMemberSubscription(int pageNum, int records) {
		Pageable pageable = PageRequest.of(pageNum, records);
		List<MemberSubscription> subscriptions = membershipDao.fetchAllMemberSubscription(pageable, pageNum, records);
		return subscriptions;
	}

	@Transactional
	public int fetchMemSubscriptionTotalList() {
		return membershipDao.fetchMemSubscriptionTotalList();
	}

	@Transactional
	public int checkAdvisorIsPresent(String advId) {
		String deleteflag = advTableFields.getDelete_flag_N();
		return membershipDao.checkAdvisorIsPresent(advId, deleteflag);
	}

	@Transactional
	public int addSinglePaymentOrder(SinglePayment singlePayment) {
		int id = membershipDao.addSinglePaymentOrder(singlePayment);
		singlePayment.setSinglePaymentId(id);
		return id;
	}

	@Transactional
	public SinglePayment fetchSinglePaymentByPrimaryKey(int singlePaymentId) {
		return membershipDao.fetchSinglePaymentByPrimaryKey(singlePaymentId);
	}

	@Transactional
	public int updatePaymentDetails(SubscriptionPayment subscriptionPayment, int singlePaymentId,
			SinglePayment singlePayment) {
		return membershipDao.updatePaymentDetails(subscriptionPayment, singlePaymentId, singlePayment);
	}

	@Transactional
	public OrderDetail addOrder(OrderDetail invoice) {
		String encryptPass = advTableFields.getEncryption_password();
		String signedUserId = getSignedInUser();
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		invoice.setCreated(timestamp);
		invoice.setCreated_by(signedUserId);
		invoice.setUpdated(timestamp);
		invoice.setUpdated_by(signedUserId);
		int inv = membershipDao.addOrder(invoice, encryptPass);
		return invoice;
	}

	@Transactional
	public String generateSubOrderNumber() {
		String id = membershipDao.fetchSubOrderNumber();
		if (id != null) {
			String newId = referenceIdIncrement(id);
			int result = membershipDao.addSubOrderNumber(newId);
			if (result == 0) {
				return null;
			} else {
				return newId;
			}
		} else {
			String newId = "ORDS00000000";
			membershipDao.addSubOrderNumber(newId);
			return newId;
		}
	}

	@Transactional
	public String generatePaymentOrderNumber() {
		String id = membershipDao.fetchPaymentOrderNumber();
		if (id != null) {
			String newId = referenceIdIncrement(id);
			int result = membershipDao.addPaymentOrderNumber(newId);
			if (result == 0) {
				return null;
			} else {
				return newId;
			}
		} else {
			String newId = "ORDP00000000";
			membershipDao.addPaymentOrderNumber(newId);
			return newId;
		}
	}

	private String referenceIdIncrement(String id) {
		String initial = id.substring(0, 4);
		String subNumber = id.substring(4, 12);
		long number = Long.parseLong(subNumber);
		String num = String.format("%08d", number + 1);
		String newId = initial + num;
		return newId;
	}

	private String getSignedInUser() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String encryptPass = advTableFields.getEncryption_password();
			String delete_flag = advTableFields.getDelete_flag_N();
			if (userDetails.getUsername().equals(adminSignin.getEmailid())) {
				return adminSignin.getAdmin_name();
			} else {
				Party party = membershipDao.fetchPartyForSignIn(userDetails.getUsername(), delete_flag, encryptPass);
				return party.getRoleBasedId();
			}
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public OrderDetail fetchOrderDetail(String orderNumber) {
		String encryptPass = advTableFields.getEncryption_password();
		OrderDetail orderDetail = membershipDao.fetchOrderDetail(orderNumber, encryptPass);
		return orderDetail;
	}

	@Override
	public OrderDetail updateOrderDetail(OrderDetail orderDetail) {
		String signedUserId = getSignedInUser();
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Calcutta"));
		Timestamp timestamp = Timestamp.valueOf(zdt.toLocalDateTime());
		orderDetail.setUpdated(timestamp);
		orderDetail.setUpdated_by(signedUserId);
		int result = membershipDao.updateOrderDetail(orderDetail);
		return orderDetail;
	}

	@Override
	public SinglePayment fetchMemberSinglePaySubByAdvId(String advId, String singlePaidStatus, String type) {
		SinglePayment singlePayment = membershipDao.fetchMemberSinglePaySubByAdvId(advId, singlePaidStatus, type);
		return singlePayment;
	}
}
