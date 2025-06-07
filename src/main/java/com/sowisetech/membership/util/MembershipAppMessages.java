package com.sowisetech.membership.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:membershipappmessages.properties")
@Component
public class MembershipAppMessages {

	@Value("${access_denied}")
	public String access_denied;

	@Value("${unauthorized}")
	public String unauthorized;

	@Value("${success}")
	public String success;

	@Value("${error}")
	public String error;

	@Value("${plan_created}")
	public String plan_created;

	@Value("${error_occurred_plan}")
	public String error_occurred_plan;

	@Value("${subscription_created}")
	public String subscription_created;

	@Value("${error_occurred_subscripton}")
	public String error_occurred_subscripton;

	@Value("${subscription_status_changed}")
	public String subscription_status_changed;

	@Value("${error_occurred_subscripton_status}")
	public String error_occurred_subscripton_status;

	@Value("${failed_to_verify_webhook_signature}")
	public String failed_to_verify_webhook_signature;

	@Value("${cancel_subscription}")
	public String cancel_subscription;

	@Value("${update_subscription}")
	public String update_subscription;

	@Value("${error_occurred_order}")
	public String error_occurred_order;

	@Value("${no_record_found}")
	public String no_record_found;

	@Value("${error_occurred_adding_table}")
	public String error_occurred_adding_table;

	@Value("${verified_successfully}")
	public String verified_successfully;

	@Value("${verification_failed}")
	public String verification_failed;

	@Value("${subscription_already_created}")
	public String subscription_already_created;

	@Value("${single_pay_order_created}")
	public String single_pay_order_created;

	@Value("${order_num_created}")
	public String order_num_created;

	@Value("${update_order_detail_status_changed}")
	public String update_order_detail_status_changed;

	@Value("${mandatory_fields_fetchAllInvoice}")
	public String mandatory_fields_fetchAllInvoice;

	@Value("${mandatory_fields_fetchMembershipPlan}")
	public String mandatory_fields_fetchMembershipPlan;

	@Value("${mandatory_fields_fetchMemberSubByAdvId}")
	public String mandatory_fields_fetchMemberSubByAdvId;

	@Value("${mandatory_fields_fetchMemberSubById}")
	public String mandatory_fields_fetchMemberSubById;

	@Value("${mandatory_fields_verifySinglePayment}")
	public String mandatory_fields_verifySinglePayment;

	@Value("${mandatory_fields_verifySubscriptionPayment}")
	public String mandatory_fields_verifySubscriptionPayment;

	@Value("${mandatory_fields_updateSubscription}")
	public String mandatory_fields_updateSubscription;

	@Value("${mandatory_fields_updateOrder}")
	public String mandatory_fields_updateOrder;

	@Value("${mandatory_fields_advId}")
	public String mandatory_fields_advId;

	@Value("${mandatory_fields_planName_content}")
	public String mandatory_fields_planName_content;

	@Value("${mandatory_fields_orderDetailId_type_roleBasedId}")
	public String mandatory_fields_orderDetailId_type_roleBasedId;

	@Value("${mandatory_fields_type_planId_period__totalCount}")
	public String mandatory_fields_type_planId_period__totalCount;

	public String getError_occurred_order() {
		return error_occurred_order;
	}

	public void setError_occurred_order(String error_occurred_order) {
		this.error_occurred_order = error_occurred_order;
	}

	public String getError_occurred_adding_table() {
		return error_occurred_adding_table;
	}

	public void setError_occurred_adding_table(String error_occurred_adding_table) {
		this.error_occurred_adding_table = error_occurred_adding_table;
	}

	public String getAccess_denied() {
		return access_denied;
	}

	public void setAccess_denied(String access_denied) {
		this.access_denied = access_denied;
	}

	public String getUnauthorized() {
		return unauthorized;
	}

	public void setUnauthorized(String unauthorized) {
		this.unauthorized = unauthorized;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPlan_created() {
		return plan_created;
	}

	public void setPlan_created(String plan_created) {
		this.plan_created = plan_created;
	}

	public String getError_occurred_plan() {
		return error_occurred_plan;
	}

	public void setError_occurred_plan(String error_occurred_plan) {
		this.error_occurred_plan = error_occurred_plan;
	}

	public String getSubscription_created() {
		return subscription_created;
	}

	public void setSubscription_created(String subscription_created) {
		this.subscription_created = subscription_created;
	}

	public String getError_occurred_subscripton() {
		return error_occurred_subscripton;
	}

	public void setError_occurred_subscripton(String error_occurred_subscripton) {
		this.error_occurred_subscripton = error_occurred_subscripton;
	}

	public String getSubscription_status_changed() {
		return subscription_status_changed;
	}

	public void setSubscription_status_changed(String subscription_status_changed) {
		this.subscription_status_changed = subscription_status_changed;
	}

	public String getError_occurred_subscripton_status() {
		return error_occurred_subscripton_status;
	}

	public void setError_occurred_subscripton_status(String error_occurred_subscripton_status) {
		this.error_occurred_subscripton_status = error_occurred_subscripton_status;
	}

	public String getFailed_to_verify_webhook_signature() {
		return failed_to_verify_webhook_signature;
	}

	public void setFailed_to_verify_webhook_signature(String failed_to_verify_webhook_signature) {
		this.failed_to_verify_webhook_signature = failed_to_verify_webhook_signature;
	}

	public String getCancel_subscription() {
		return cancel_subscription;
	}

	public void setCancel_subscription(String cancel_subscription) {
		this.cancel_subscription = cancel_subscription;
	}

	public String getUpdate_subscription() {
		return update_subscription;
	}

	public void setUpdate_subscription(String update_subscription) {
		this.update_subscription = update_subscription;
	}

	public String getNo_record_found() {
		return no_record_found;
	}

	public void setNo_record_found(String no_record_found) {
		this.no_record_found = no_record_found;
	}

	public String getVerified_successfully() {
		return verified_successfully;
	}

	public void setVerified_successfully(String verified_successfully) {
		this.verified_successfully = verified_successfully;
	}

	public String getVerification_failed() {
		return verification_failed;
	}

	public void setVerification_failed(String verification_failed) {
		this.verification_failed = verification_failed;
	}

	public String getSubscription_already_created() {
		return subscription_already_created;
	}

	public void setSubscription_already_created(String subscription_already_created) {
		this.subscription_already_created = subscription_already_created;
	}

	public String getSingle_pay_order_created() {
		return single_pay_order_created;
	}

	public void setSingle_pay_order_created(String single_pay_order_created) {
		this.single_pay_order_created = single_pay_order_created;
	}

	public String getOrder_num_created() {
		return order_num_created;
	}

	public void setOrder_num_created(String order_num_created) {
		this.order_num_created = order_num_created;
	}

	public String getUpdate_order_detail_status_changed() {
		return update_order_detail_status_changed;
	}

	public void setUpdate_order_detail_status_changed(String update_order_detail_status_changed) {
		this.update_order_detail_status_changed = update_order_detail_status_changed;
	}

	public String getMandatory_fields_fetchAllInvoice() {
		return mandatory_fields_fetchAllInvoice;
	}

	public void setMandatory_fields_fetchAllInvoice(String mandatory_fields_fetchAllInvoice) {
		this.mandatory_fields_fetchAllInvoice = mandatory_fields_fetchAllInvoice;
	}

	public String getMandatory_fields_fetchMembershipPlan() {
		return mandatory_fields_fetchMembershipPlan;
	}

	public void setMandatory_fields_fetchMembershipPlan(String mandatory_fields_fetchMembershipPlan) {
		this.mandatory_fields_fetchMembershipPlan = mandatory_fields_fetchMembershipPlan;
	}

	public String getMandatory_fields_fetchMemberSubByAdvId() {
		return mandatory_fields_fetchMemberSubByAdvId;
	}

	public void setMandatory_fields_fetchMemberSubByAdvId(String mandatory_fields_fetchMemberSubByAdvId) {
		this.mandatory_fields_fetchMemberSubByAdvId = mandatory_fields_fetchMemberSubByAdvId;
	}

	public String getMandatory_fields_fetchMemberSubById() {
		return mandatory_fields_fetchMemberSubById;
	}

	public void setMandatory_fields_fetchMemberSubById(String mandatory_fields_fetchMemberSubById) {
		this.mandatory_fields_fetchMemberSubById = mandatory_fields_fetchMemberSubById;
	}

	public String getMandatory_fields_verifySinglePayment() {
		return mandatory_fields_verifySinglePayment;
	}

	public void setMandatory_fields_verifySinglePayment(String mandatory_fields_verifySinglePayment) {
		this.mandatory_fields_verifySinglePayment = mandatory_fields_verifySinglePayment;
	}

	public String getMandatory_fields_verifySubscriptionPayment() {
		return mandatory_fields_verifySubscriptionPayment;
	}

	public void setMandatory_fields_verifySubscriptionPayment(String mandatory_fields_verifySubscriptionPayment) {
		this.mandatory_fields_verifySubscriptionPayment = mandatory_fields_verifySubscriptionPayment;
	}

	public String getMandatory_fields_updateSubscription() {
		return mandatory_fields_updateSubscription;
	}

	public void setMandatory_fields_updateSubscription(String mandatory_fields_updateSubscription) {
		this.mandatory_fields_updateSubscription = mandatory_fields_updateSubscription;
	}

	public String getMandatory_fields_updateOrder() {
		return mandatory_fields_updateOrder;
	}

	public void setMandatory_fields_updateOrder(String mandatory_fields_updateOrder) {
		this.mandatory_fields_updateOrder = mandatory_fields_updateOrder;
	}

	public String getMandatory_fields_advId() {
		return mandatory_fields_advId;
	}

	public void setMandatory_fields_advId(String mandatory_fields_advId) {
		this.mandatory_fields_advId = mandatory_fields_advId;
	}

	public String getMandatory_fields_planName_content() {
		return mandatory_fields_planName_content;
	}

	public void setMandatory_fields_planName_content(String mandatory_fields_planName_content) {
		this.mandatory_fields_planName_content = mandatory_fields_planName_content;
	}

	public String getMandatory_fields_orderDetailId_type_roleBasedId() {
		return mandatory_fields_orderDetailId_type_roleBasedId;
	}

	public void setMandatory_fields_orderDetailId_type_roleBasedId(
			String mandatory_fields_orderDetailId_type_roleBasedId) {
		this.mandatory_fields_orderDetailId_type_roleBasedId = mandatory_fields_orderDetailId_type_roleBasedId;
	}

	public String getMandatory_fields_type_planId_period__totalCount() {
		return mandatory_fields_type_planId_period__totalCount;
	}

	public void setMandatory_fields_type_planId_period__totalCount(
			String mandatory_fields_type_planId_period__totalCount) {
		this.mandatory_fields_type_planId_period__totalCount = mandatory_fields_type_planId_period__totalCount;
	}

}
