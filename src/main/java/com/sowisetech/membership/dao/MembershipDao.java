package com.sowisetech.membership.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.sowisetech.calc.model.Party;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubPaymentCard;
import com.sowisetech.membership.model.SubscriptionPayment;

public interface MembershipDao {

	int addMembershipPlan(MembershipPlan membershipPlan);

	int addSubscription(MemberSubscription membershipSub);

	int updateSubscription(MemberSubscription membershipSub);

	int updateSubscriptionPayment(SubscriptionPayment subscriptionPayment, String encryptPass);

	int updateSubPaymentCard(SubPaymentCard subPaymentCard, String encryptPass);

	List<String> fetchInvoiceIdFromSubPayment(String sub_id);

	MemberSubscription fetchMemberSubByAdvId(String advId, String statusCancelled, String statusCompleted);

	int fetchPaymentIdFromSubPay(String razorpayPaymentId);

	List<MembershipPlan> fetchAllMembershipPlan(String delete_flag);

	int fetchMemPlanTotalList(String delete_flag);

	List<MemberSubscription> fetchAllMemberSubscription(Pageable pageable, int pageNum, int records);

	int fetchMemSubscriptionTotalList();

	int checkAdvisorIsPresent(String advId, String deleteflag);

	int addSinglePaymentOrder(SinglePayment singlePayment);

	SinglePayment fetchSinglePaymentByPrimaryKey(int singlePaymentId);

	int updatePaymentDetails(SubscriptionPayment subscriptionPayment, int singlePaymentId, SinglePayment singlePayment);

	String fetchSubOrderNumber();

	int addSubOrderNumber(String newId);

	Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass);

	int addOrder(OrderDetail invoice, String encryptPass);

	String fetchPaymentOrderNumber();

	int addPaymentOrderNumber(String newId);

	OrderDetail fetchOrderDetail(String orderNumber, String encryptPass);

	int updateOrderDetail(OrderDetail orderDetail);

	SinglePayment fetchMemberSinglePaySubByAdvId(String advId, String singlePaidStatus, String type);

}
