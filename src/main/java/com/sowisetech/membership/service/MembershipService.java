package com.sowisetech.membership.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.OrderDetail;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubscriptionPayment;
import com.sowisetech.membership.request.SubscriptionChargedRequest;

@Service
public interface MembershipService {

	int addMembershipPlan(MembershipPlan membershipPlan);

	int addSubscription(String advId, MemberSubscription memberSubscription);

	int updateSubscription(String advId, MemberSubscription membershipSub);

	int updateSubscriptionWebHook(SubscriptionChargedRequest subscriptionChargedRequest);

	MemberSubscription fetchMemberSubByadvId(String advId);

	List<String> fetchInvoiceIdFromSubPayment(String sub_id);

	List<MembershipPlan> fetchAllMembershipPlan();

	int fetchMemPlanTotalList();

	List<MemberSubscription> fetchAllMemberSubscription(int pageNum, int records);

	int fetchMemSubscriptionTotalList();

	int checkAdvisorIsPresent(String advId);

	int addSinglePaymentOrder(SinglePayment singlePayment);

	SinglePayment fetchSinglePaymentByPrimaryKey(int singlePaymentId);

	int updatePaymentDetails(SubscriptionPayment subscriptionPayment, int singlePaymentId, SinglePayment singlePayment);

	OrderDetail addOrder(OrderDetail invoice);

	String generateSubOrderNumber();

	String generatePaymentOrderNumber();

	OrderDetail fetchOrderDetail(String orderNumber);

	OrderDetail updateOrderDetail(OrderDetail orderDetail);

	SinglePayment fetchMemberSinglePaySubByAdvId(String advId, String singlePaidStatus, String subType);

}
