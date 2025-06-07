package com.sowisetech.membership.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.razorpay.Invoice;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.Plan;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Subscription;
import com.sowisetech.membership.model.MembershipPlan;
import com.sowisetech.membership.model.SinglePayment;
import com.sowisetech.membership.model.SubscriptionPayment;
import com.sowisetech.membership.request.CancelSubRequest;
import com.sowisetech.membership.model.InvoiceSubscription;
import com.sowisetech.membership.model.LineItems;
import com.sowisetech.membership.model.MemberSubscription;
import com.sowisetech.membership.request.MembershipPlanRequest;
import com.sowisetech.membership.request.PauseAndResumeSubRequest;
import com.sowisetech.membership.request.SinglePaymentRequest;
import com.sowisetech.membership.request.SubscriptionRequest;
import com.sowisetech.membership.request.UpdateSubRequest;
import com.sowisetech.membership.request.VerifyPaymentRequest;
import com.sowisetech.membership.util.MembershipConstants;

@Service
public class RazorpayService {

	private RazorpayClient razorpayClient;

	@Value("${secretId}")
	private String secretId;
	@Value("${secretKey}")
	private String secretKey;

	@Value("${webhook_secret}")
	private String webhook_secret;

	private static final Logger logger = LoggerFactory.getLogger(RazorpayService.class);

	@PostConstruct
	private void initializeRazorpayClient() {
		try {
			this.razorpayClient = new RazorpayClient(secretId, secretKey);
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}

	}

	public MembershipPlan createPlan(MembershipPlanRequest membershipPlanRequest) {
		JSONObject request = new JSONObject();
		MembershipPlan membershipPlan = new MembershipPlan();
		try {
			request.put("period", membershipPlanRequest.getPeriod());
			request.put("interval", membershipPlanRequest.getInterval());

			JSONObject item = new JSONObject();
			item.put("name", membershipPlanRequest.getItem().getName());
			item.put("description", membershipPlanRequest.getItem().getDescription());
			item.put("amount", convertRupeeToPaise(membershipPlanRequest.getItem().getAmount()));
			item.put("currency", membershipPlanRequest.getItem().getCurrency());
			request.put("item", item);

		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Plan plan = null;
		try {
			plan = razorpayClient.Plans.create(request);
			if (plan == null) {
				return null;
			}
			if (!org.json.JSONObject.NULL.equals(plan.get("id"))) {
				membershipPlan.setRazorpayPlanId((String) plan.get("id"));
			}
			if (!org.json.JSONObject.NULL.equals(plan.get("period"))) {
				membershipPlan.setPeriod((String) plan.get("period"));
			}
			if (!org.json.JSONObject.NULL.equals(plan.get("interval"))) {
				membershipPlan.setInterval((int) plan.get("interval"));
			}
			JSONObject item = (JSONObject) plan.get("item");
			try {
				if (!org.json.JSONObject.NULL.equals(item.get("id"))) {
					membershipPlan.setItemId((String) item.get("id"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("active"))) {
					membershipPlan.setActive((boolean) item.get("active"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("name"))) {
					membershipPlan.setName((String) item.get("name"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("description"))) {
					membershipPlan.setDescription((String) item.get("description"));
				}
				// convert paise to rupee
				if (!org.json.JSONObject.NULL.equals(item.get("amount"))) {
					membershipPlan.setAmount((int) item.get("amount") / 100);
				}
				if (!org.json.JSONObject.NULL.equals(item.get("currency"))) {
					membershipPlan.setCurrency((String) item.get("currency"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("created_at"))) {
					membershipPlan.setCreated_at((int) item.get("created_at"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("updated_at"))) {
					membershipPlan.setUpdated_at((int) item.get("updated_at"));
				}
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		return membershipPlan;
	}

	public List<MembershipPlan> fetchAllPlan() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		razorpayClient.addHeaders(headers);
		List<Plan> plans = new ArrayList<>();
		try {
			plans = razorpayClient.Plans.fetchAll();
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		List<MembershipPlan> membershipPlans = new ArrayList<>();
		for (Plan plan : plans) {
			MembershipPlan membershipPlan = new MembershipPlan();
			if (!org.json.JSONObject.NULL.equals(plan.get("id"))) {
				membershipPlan.setRazorpayPlanId((String) plan.get("id"));
			}
			if (!org.json.JSONObject.NULL.equals(plan.get("period"))) {
				membershipPlan.setPeriod((String) plan.get("period"));
			}
			if (!org.json.JSONObject.NULL.equals(plan.get("interval"))) {
				membershipPlan.setInterval((int) plan.get("interval"));
			}
			JSONObject item = (JSONObject) plan.get("item");
			try {
				if (!org.json.JSONObject.NULL.equals(item.get("id"))) {
					membershipPlan.setItemId((String) item.get("id"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("active"))) {
					membershipPlan.setActive((boolean) item.get("active"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("name"))) {
					membershipPlan.setName((String) item.get("name"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("description"))) {
					membershipPlan.setDescription((String) item.get("description"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("amount"))) {
					membershipPlan.setAmount((int) item.get("amount"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("currency"))) {
					membershipPlan.setCurrency((String) item.get("currency"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("created_at"))) {
					membershipPlan.setCreated_at((int) item.get("created_at"));
				}
				if (!org.json.JSONObject.NULL.equals(item.get("updated_at"))) {
					membershipPlan.setUpdated_at((int) item.get("updated_at"));
				}
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
			membershipPlans.add(membershipPlan);
		}
		return membershipPlans;
	}

	public List<InvoiceSubscription> fetchAllInvoiceSub(List<String> invoice_id) {
		List<InvoiceSubscription> invoiceSubList = new ArrayList<>();
		List<Invoice> invoices = new ArrayList<>();
		Invoice invoice = null;
		JSONObject request = new JSONObject();
		try {
			// request.put("sub_id", sub_id);
			request.put("invoice_id", invoice_id);
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		for (String inv_id : invoice_id) {
			try {
				// invoices = razorpayClient.Invoices.post("invoices?subscription_id=" + sub_id,
				// request);
				invoice = razorpayClient.Invoices.fetch(inv_id);
			} catch (RazorpayException e) {
				logger.error(e.getMessage());
			}

			InvoiceSubscription invoiceSub = new InvoiceSubscription();
			if (!org.json.JSONObject.NULL.equals(invoice.get("id"))) {
				invoiceSub.setItemId((String) invoice.get("id"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("entity"))) {
				invoiceSub.setEntity((String) invoice.get("entity"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("receipt"))) {
				invoiceSub.setReceipt((String) invoice.get("receipt"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("invoice_number"))) {
				invoiceSub.setInvoice_number((String) invoice.get("invoice_number"));
			}
			JSONObject customer_details = (JSONObject) invoice.get("customer_details");
			try {
				if (!org.json.JSONObject.NULL.equals(customer_details.get("id"))) {
					invoiceSub.setCustomer_id((String) customer_details.get("id"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("name"))) {
					invoiceSub.setName((String) customer_details.get("name"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("email"))) {
					invoiceSub.setEmail((String) customer_details.get("email"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("contact"))) {
					invoiceSub.setContact((String) customer_details.get("contact"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("billing_address"))) {
					invoiceSub.setBilling_address((String) customer_details.get("billing_address"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("shipping_address"))) {
					invoiceSub.setShipping_address((String) customer_details.get("shipping_address"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("customer_name"))) {
					invoiceSub.setCustomer_name((String) customer_details.get("customer_name"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("customer_email"))) {
					invoiceSub.setCustomer_email((String) customer_details.get("customer_email"));
				}
				if (!org.json.JSONObject.NULL.equals(customer_details.get("customer_contact"))) {
					invoiceSub.setCustomer_contact((String) customer_details.get("customer_contact"));
				}
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("order_id"))) {
				invoiceSub.setOrder_id((String) invoice.get("order_id"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("subscription_id"))) {
				invoiceSub.setSub_id((String) invoice.get("subscription_id"));
			}
			JSONArray line_items = (JSONArray) invoice.get("line_items");
			List<LineItems> lineItems = new ArrayList<>();
			for (int i = 0; i < line_items.length(); i++) {
				JSONObject obj = null;
				try {
					obj = line_items.getJSONObject(i);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				try {
					LineItems lineItem = new LineItems();
					if (!org.json.JSONObject.NULL.equals(obj.get("id"))) {
						lineItem.setLine_items_id((String) obj.get("id"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("name"))) {
						lineItem.setLine_items_name((String) obj.get("name"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("description"))) {
						lineItem.setDescription((String) obj.get("description"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("amount"))) {
						lineItem.setAmount((int) obj.get("amount"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("unit_amount"))) {
						lineItem.setUnit_amount((int) obj.get("unit_amount"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("gross_amount"))) {
						lineItem.setGross_amount((int) obj.get("gross_amount"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("tax_amount"))) {
						lineItem.setTax_amount((int) obj.get("tax_amount"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("taxable_amount"))) {
						lineItem.setTaxable_amount((int) obj.get("taxable_amount"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("net_amount"))) {
						lineItem.setNet_amount((int) obj.get("net_amount"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("currency"))) {
						lineItem.setCurrency((String) obj.get("currency"));
					}
					if (!org.json.JSONObject.NULL.equals(obj.get("type"))) {
						lineItem.setType((String) obj.get("type"));
					}
					lineItems.add(lineItem);
				} catch (JSONException e) {
					logger.error(e.getMessage());
				}
			}
			invoiceSub.setLine_items(lineItems);
			if (!org.json.JSONObject.NULL.equals(invoice.get("payment_id"))) {
				invoiceSub.setPayment_id((String) invoice.get("payment_id"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("status"))) {
				invoiceSub.setStatus((String) invoice.get("status"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("expire_by"))) {
				invoiceSub.setExpire_by((int) invoice.get("expire_by"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("issued_at"))) {
				invoiceSub.setIssued_at((int) invoice.get("issued_at"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("paid_at"))) {
				invoiceSub.setPaid_at((int) invoice.get("paid_at"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("cancelled_at"))) {
				invoiceSub.setCancelled_at((int) invoice.get("cancelled_at"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("expired_at"))) {
				invoiceSub.setExpired_at((int) invoice.get("expired_at"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("sms_status"))) {
				invoiceSub.setSms_status((String) invoice.get("sms_status"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("email_status"))) {
				invoiceSub.setEmail_status((String) invoice.get("email_status"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("date"))) {
				invoiceSub.setDate((int) invoice.get("date"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("amount"))) {
				invoiceSub.setPayment_amount((int) invoice.get("amount"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("amount_paid"))) {
				invoiceSub.setPayment_amount_paid((int) invoice.get("amount_paid"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("amount_due"))) {
				invoiceSub.setPayment_amount_due((int) invoice.get("amount_due"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("currency"))) {
				invoiceSub.setPayment_currency((String) invoice.get("currency"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("currency_symbol"))) {
				invoiceSub.setPayment_currency_symbol((String) invoice.get("currency_symbol"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("short_url"))) {
				invoiceSub.setShort_url((String) invoice.get("short_url"));
			}
			if (!org.json.JSONObject.NULL.equals(invoice.get("type"))) {
				invoiceSub.setPayment_type((String) invoice.get("type"));
			}
			invoiceSubList.add(invoiceSub);
		}
		// List<InvoiceSubscription> invoiceSubList = new ArrayList<>();
		// for (Invoice invoice : invoices) {

		// if (!org.json.JSONObject.NULL.equals(invoice.get("created_at"))) {
		// invoiceSub.setCreated_at((int) invoice.get("created_at"));
		// }
		// invoiceSubList.add(invoiceSub);
		// }
		return invoiceSubList;
	}

	public MembershipPlan fetchPlanByPlanId(String plan_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("plan_id", plan_id);
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Plan plan = null;
		try {
			plan = razorpayClient.Plans.fetch(plan_id);
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		MembershipPlan membershipPlan = new MembershipPlan();
		if (!org.json.JSONObject.NULL.equals(plan.get("id"))) {
			membershipPlan.setRazorpayPlanId((String) plan.get("id"));
		}
		if (!org.json.JSONObject.NULL.equals(plan.get("period"))) {
			membershipPlan.setPeriod((String) plan.get("period"));
		}
		if (!org.json.JSONObject.NULL.equals(plan.get("interval"))) {
			membershipPlan.setInterval((int) plan.get("interval"));
		}
		JSONObject item = (JSONObject) plan.get("item");
		try {
			if (!org.json.JSONObject.NULL.equals(item.get("id"))) {
				membershipPlan.setItemId((String) item.get("id"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("active"))) {
				membershipPlan.setActive((boolean) item.get("active"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("name"))) {
				membershipPlan.setName((String) item.get("name"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("description"))) {
				membershipPlan.setDescription((String) item.get("description"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("amount"))) {
				membershipPlan.setAmount((int) item.get("amount"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("currency"))) {
				membershipPlan.setCurrency((String) item.get("currency"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("created_at"))) {
				membershipPlan.setCreated_at((int) item.get("created_at"));
			}
			if (!org.json.JSONObject.NULL.equals(item.get("updated_at"))) {
				membershipPlan.setUpdated_at((int) item.get("updated_at"));
			}
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		return membershipPlan;
	}

	public MemberSubscription createSubscription(SubscriptionRequest subscriptionRequest) {
		JSONObject request = new JSONObject();
		try {
			request.put("plan_id", subscriptionRequest.getPlan_id());
			request.put("customer_notify", 1);
			request.put("total_count", subscriptionRequest.getTotal_count());
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Subscription subscription = null;
		try {
			subscription = razorpayClient.Subscriptions.create(request);
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		if (subscription == null) {
			return null;
		}
		MemberSubscription membershipSub = new MemberSubscription();
		if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
			membershipSub.setRazorpaySubId((String) subscription.get("id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
			membershipSub.setRazorpayPlanId((String) subscription.get("plan_id"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("customer_id"))) {
		// membershipSub.setCustomer_id((String) subscription.get("customer_id"));
		// }
		if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
			membershipSub.setStatus((String) subscription.get("status"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
			membershipSub.setCurrent_start((int) subscription.get("current_start"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
			membershipSub.setCurrent_end((int) subscription.get("current_end"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
			membershipSub.setEnded_at((int) subscription.get("ended_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
			membershipSub.setQuantity((int) subscription.get("quantity"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
			membershipSub.setCharge_at((int) subscription.get("charge_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
			membershipSub.setStart_at((int) subscription.get("start_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
			membershipSub.setEnd_at((int) subscription.get("end_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
			membershipSub.setAuth_attempts((int) subscription.get("auth_attempts"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
			membershipSub.setTotal_count((int) subscription.get("total_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
			membershipSub.setPaid_count((int) subscription.get("paid_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
			membershipSub.setCustomer_notify((boolean) subscription.get("customer_notify"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
			membershipSub.setCreated_at((Date) subscription.get("created_at"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("paused_at"))) {
		// membershipSub.setPaused_at((int) subscription.get("paused_at"));
		// }
		// if (!org.json.JSONObject.NULL.equals(subscription.get("pause_initiated_by")))
		// {
		// membershipSub.setPause_initiated_by((String)
		// subscription.get("pause_initiated_by"));
		// }
		// if
		// (!org.json.JSONObject.NULL.equals(subscription.get("cancel_initiated_by"))) {
		// membershipSub.setCancel_initiated_by((String)
		// subscription.get("cancel_initiated_by"));
		// }
		if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
			membershipSub.setExpire_by((int) subscription.get("expire_by"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
			membershipSub.setShort_url((String) subscription.get("short_url"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes"))) {
			membershipSub.setHas_scheduled_changes((boolean) subscription.get("has_scheduled_changes"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("change_scheduled_at"))) {
			membershipSub.setChange_scheduled_at((String) subscription.get("change_scheduled_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
			membershipSub.setSource((String) subscription.get("source"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
			membershipSub.setRemaining_count((int) subscription.get("remaining_count"));
		}
		membershipSub.setSecretId(secretId);
		// if (!org.json.JSONObject.NULL.equals(subscription.get("payment_method"))) {
		// membershipSub.setRemaining_count((int) subscription.get("payment_method"));
		// }
		return membershipSub;
	}

	public MemberSubscription pauseAndResumeSubscription(PauseAndResumeSubRequest pauseAndResumeSubRequest) {
		Subscription subscription = null;
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		razorpayClient.addHeaders(headers);

		if (pauseAndResumeSubRequest.getKey().equals(MembershipConstants.PAUSE)) {
			JSONObject request = new JSONObject();
			try {
				request.put("pause_at", "now");
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
			try {
				String key = pauseAndResumeSubRequest.getRazorpaySubId() + "/" + pauseAndResumeSubRequest.getKey();
				subscription = razorpayClient.Subscriptions.post("subscriptions/" + key, request);
			} catch (RazorpayException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else if (pauseAndResumeSubRequest.getKey().equals(MembershipConstants.RESUME)) {
			JSONObject request = new JSONObject();
			try {
				request.put("resume_at", "now");
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
			try {
				String key = pauseAndResumeSubRequest.getRazorpaySubId() + "/" + pauseAndResumeSubRequest.getKey();
				subscription = razorpayClient.Subscriptions.post("subscriptions/" + key, request);
			} catch (RazorpayException e) {
				logger.error(e.getMessage());
			}
		}
		if (subscription == null) {
			return null;
		}
		MemberSubscription membershipSub = new MemberSubscription();
		if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
			membershipSub.setRazorpaySubId((String) subscription.get("id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
			membershipSub.setRazorpayPlanId((String) subscription.get("plan_id"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("customer_id"))) {
		// membershipSub.setCustomer_id((String) subscription.get("customer_id"));
		// }
		if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
			membershipSub.setStatus((String) subscription.get("status"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
			membershipSub.setCurrent_start((int) subscription.get("current_start"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
			membershipSub.setCurrent_end((int) subscription.get("current_end"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
			membershipSub.setEnded_at((int) subscription.get("ended_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
			membershipSub.setQuantity((int) subscription.get("quantity"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
			membershipSub.setCharge_at((int) subscription.get("charge_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
			membershipSub.setStart_at((int) subscription.get("start_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
			membershipSub.setEnd_at((int) subscription.get("end_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
			membershipSub.setAuth_attempts((int) subscription.get("auth_attempts"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
			membershipSub.setTotal_count((int) subscription.get("total_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
			membershipSub.setPaid_count((int) subscription.get("paid_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
			membershipSub.setCustomer_notify((boolean) subscription.get("customer_notify"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
			membershipSub.setCreated_at((Date) subscription.get("created_at"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("paused_at"))) {
		// membershipSub.setPaused_at((int) subscription.get("paused_at"));
		// }
		// if (!org.json.JSONObject.NULL.equals(subscription.get("pause_initiated_by")))
		// {
		// membershipSub.setPause_initiated_by((String)
		// subscription.get("pause_initiated_by"));
		// }
		// if
		// (!org.json.JSONObject.NULL.equals(subscription.get("cancel_initiated_by"))) {
		// membershipSub.setCancel_initiated_by((String)
		// subscription.get("cancel_initiated_by"));
		// }
		if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
			membershipSub.setExpire_by((int) subscription.get("expire_by"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
			membershipSub.setShort_url((String) subscription.get("short_url"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes"))) {
			membershipSub.setHas_scheduled_changes((boolean) subscription.get("has_scheduled_changes"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("change_scheduled_at"))) {
			membershipSub.setChange_scheduled_at((String) subscription.get("change_scheduled_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
			membershipSub.setSource((String) subscription.get("source"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
			membershipSub.setRemaining_count((int) subscription.get("remaining_count"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("payment_method"))) {
		// membershipSub.setRemaining_count((int) subscription.get("payment_method"));
		// }
		return membershipSub;
	}

	public MemberSubscription cancelSubscription(CancelSubRequest cancelSubscriptionReq) {

		JSONObject request = new JSONObject();
		try {
			request.put("sub_id", cancelSubscriptionReq.getSub_id());
			request.put("cancel_at_cycle_end", cancelSubscriptionReq.getCancel_at_cycle_end());

		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Subscription subscription = null;
		try {
			subscription = razorpayClient.Subscriptions.cancel(cancelSubscriptionReq.getSub_id());
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		if (subscription == null) {
			return null;
		}
		MemberSubscription membershipSub = new MemberSubscription();

		if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
			membershipSub.setRazorpaySubId((String) subscription.get("id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
			membershipSub.setRazorpayPlanId((String) subscription.get("plan_id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_id"))) {
			membershipSub.setCustomer_id((String) subscription.get("customer_id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
			membershipSub.setStatus((String) subscription.get("status"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
			membershipSub.setCurrent_start((int) subscription.get("current_start"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
			membershipSub.setCurrent_end((int) subscription.get("current_end"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
			membershipSub.setEnded_at((int) subscription.get("ended_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
			membershipSub.setQuantity((int) subscription.get("quantity"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
			membershipSub.setCharge_at((int) subscription.get("charge_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
			membershipSub.setStart_at((int) subscription.get("start_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
			membershipSub.setEnd_at((int) subscription.get("end_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
			membershipSub.setAuth_attempts((int) subscription.get("auth_attempts"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
			membershipSub.setTotal_count((int) subscription.get("total_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
			membershipSub.setPaid_count((int) subscription.get("paid_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
			membershipSub.setCustomer_notify((boolean) subscription.get("customer_notify"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
			membershipSub.setCreated_at((Date) subscription.get("created_at"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("paused_at"))) {
		// membershipSub.setPaused_at((int) subscription.get("paused_at"));
		// }
		// if (!org.json.JSONObject.NULL.equals(subscription.get("pause_initiated_by")))
		// {
		// membershipSub.setPause_initiated_by((String)
		// subscription.get("pause_initiated_by"));
		// }
		// if
		// (!org.json.JSONObject.NULL.equals(subscription.get("cancel_initiated_by"))) {
		// membershipSub.setCancel_initiated_by((String)
		// subscription.get("cancel_initiated_by"));
		// }
		if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
			membershipSub.setExpire_by((int) subscription.get("expire_by"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
			membershipSub.setShort_url((String) subscription.get("short_url"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes"))) {
			membershipSub.setHas_scheduled_changes((boolean) subscription.get("has_scheduled_changes"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("change_scheduled_at"))) {
			membershipSub.setChange_scheduled_at((String) subscription.get("change_scheduled_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
			membershipSub.setSource((String) subscription.get("source"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
			membershipSub.setRemaining_count((int) subscription.get("remaining_count"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("payment_method"))) {
		// membershipSub.setRemaining_count((int) subscription.get("payment_method"));
		// }
		return membershipSub;
	}

	public MemberSubscription updateSubscription(UpdateSubRequest updateSubRequest) {
		JSONObject request = new JSONObject();
		try {
			// request.put("sub_id", subscriptionRequest.getSub_id());
			request.put("plan_id", updateSubRequest.getPlan_id());
			// request.put("quantity", updateSubRequest.getQuantity());
			request.put("remaining_count", updateSubRequest.getRemaining_count());

		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Subscription subscription = null;
		try {
			subscription = razorpayClient.Subscriptions.patch("subscriptions/" + updateSubRequest.getSub_id(), request);
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		if (subscription == null) {
			return null;
		}
		MemberSubscription membershipSub = new MemberSubscription();

		if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
			membershipSub.setRazorpaySubId((String) subscription.get("id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
			membershipSub.setRazorpayPlanId((String) subscription.get("plan_id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_id"))) {
			membershipSub.setCustomer_id((String) subscription.get("customer_id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
			membershipSub.setStatus((String) subscription.get("status"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
			membershipSub.setCurrent_start((int) subscription.get("current_start"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
			membershipSub.setCurrent_end((int) subscription.get("current_end"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
			membershipSub.setEnded_at((int) subscription.get("ended_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
			membershipSub.setQuantity((int) subscription.get("quantity"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
			membershipSub.setCharge_at((int) subscription.get("charge_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
			membershipSub.setStart_at((int) subscription.get("start_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
			membershipSub.setEnd_at((int) subscription.get("end_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
			membershipSub.setAuth_attempts((int) subscription.get("auth_attempts"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
			membershipSub.setTotal_count((int) subscription.get("total_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
			membershipSub.setPaid_count((int) subscription.get("paid_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
			membershipSub.setCustomer_notify((boolean) subscription.get("customer_notify"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
			membershipSub.setCreated_at((Date) subscription.get("created_at"));
		}
		// if (!org.json.JSONObject.NULL.equals(subscription.get("paused_at"))) {
		// membershipSub.setPaused_at((int) subscription.get("paused_at"));
		// }
		// if (!org.json.JSONObject.NULL.equals(subscription.get("pause_initiated_by")))
		// {
		// membershipSub.setPause_initiated_by((String)
		// subscription.get("pause_initiated_by"));
		// }
		// if
		// (!org.json.JSONObject.NULL.equals(subscription.get("cancel_initiated_by"))) {
		// membershipSub.setCancel_initiated_by((String)
		// subscription.get("cancel_initiated_by"));
		// }
		if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
			membershipSub.setExpire_by((int) subscription.get("expire_by"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
			membershipSub.setShort_url((String) subscription.get("short_url"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes"))) {
			membershipSub.setHas_scheduled_changes((boolean) subscription.get("has_scheduled_changes"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("change_scheduled_at"))) {
			membershipSub.setChange_scheduled_at((String) subscription.get("change_scheduled_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
			membershipSub.setSource((String) subscription.get("source"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
			membershipSub.setRemaining_count((int) subscription.get("remaining_count"));
		}
		membershipSub.setSecretId(secretId);
		// if (!org.json.JSONObject.NULL.equals(subscription.get("payment_method"))) {
		// membershipSub.setRemaining_count((int) subscription.get("payment_method"));
		// }
		return membershipSub;
	}

	public List<MemberSubscription> fetchAllMemberSubscriptions() {
		List<Subscription> subscriptions = new ArrayList<>();
		try {
			subscriptions = razorpayClient.Subscriptions.fetchAll();
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		List<MemberSubscription> memberSubscriptions = new ArrayList<>();
		for (Subscription subscription : subscriptions) {
			MemberSubscription memberSubscription = new MemberSubscription();
			if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
				memberSubscription.setRazorpayPlanId((String) subscription.get("plan_id"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
				memberSubscription.setRazorpaySubId((String) subscription.get("id"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
				memberSubscription.setStatus((String) subscription.get("status"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
				memberSubscription.setCurrent_start((int) subscription.get("current_start"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
				memberSubscription.setCurrent_end((int) subscription.get("current_end"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
				memberSubscription.setEnded_at((int) subscription.get("ended_at"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
				memberSubscription.setQuantity((int) subscription.get("quantity"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
				memberSubscription.setCharge_at((int) subscription.get("charge_at"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
				memberSubscription.setStart_at((int) subscription.get("start_at"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
				memberSubscription.setEnd_at((int) subscription.get("end_at"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
				memberSubscription.setAuth_attempts((int) subscription.get("auth_attempts"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
				memberSubscription.setTotal_count((int) subscription.get("total_count"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
				memberSubscription.setPaid_count((int) subscription.get("paid_count"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
				memberSubscription.setCustomer_notify((boolean) subscription.get("customer_notify"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
				memberSubscription.setCreated_at((Date) subscription.get("created_at"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
				memberSubscription.setExpire_by((int) subscription.get("expire_by"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
				memberSubscription.setShort_url((String) subscription.get("short_url"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes"))) {
				memberSubscription.setHas_scheduled_changes((boolean) subscription.get("has_scheduled_changes"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("change_scheduled_at"))) {
				memberSubscription.setChange_scheduled_at((String) subscription.get("change_scheduled_at"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
				memberSubscription.setSource((String) subscription.get("source"));
			}
			if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
				memberSubscription.setRemaining_count((int) subscription.get("remaining_count"));
			}
			memberSubscriptions.add(memberSubscription);
		}
		return memberSubscriptions;
	}

	public MemberSubscription fetchMemberSubById(String sub_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("sub_id", sub_id);
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Subscription subscription = null;
		try {
			subscription = razorpayClient.Subscriptions.fetch(sub_id);
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		MemberSubscription memberSubscription = new MemberSubscription();
		if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
			memberSubscription.setRazorpayPlanId((String) subscription.get("plan_id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
			memberSubscription.setRazorpaySubId((String) subscription.get("id"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
			memberSubscription.setStatus((String) subscription.get("status"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
			memberSubscription.setCurrent_start((int) subscription.get("current_start"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
			memberSubscription.setCurrent_end((int) subscription.get("current_end"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
			memberSubscription.setEnded_at((int) subscription.get("ended_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
			memberSubscription.setQuantity((int) subscription.get("quantity"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
			memberSubscription.setCharge_at((int) subscription.get("charge_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
			memberSubscription.setStart_at((int) subscription.get("start_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
			memberSubscription.setEnd_at((int) subscription.get("end_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
			memberSubscription.setAuth_attempts((int) subscription.get("auth_attempts"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
			memberSubscription.setTotal_count((int) subscription.get("total_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
			memberSubscription.setPaid_count((int) subscription.get("paid_count"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
			memberSubscription.setCustomer_notify((boolean) subscription.get("customer_notify"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
			memberSubscription.setCreated_at((Date) subscription.get("created_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
			memberSubscription.setExpire_by((int) subscription.get("expire_by"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
			memberSubscription.setShort_url((String) subscription.get("short_url"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes"))) {
			memberSubscription.setHas_scheduled_changes((boolean) subscription.get("has_scheduled_changes"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("change_scheduled_at"))) {
			memberSubscription.setChange_scheduled_at((String) subscription.get("change_scheduled_at"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
			memberSubscription.setSource((String) subscription.get("source"));
		}
		if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
			memberSubscription.setRemaining_count((int) subscription.get("remaining_count"));
		}
		return memberSubscription;
	}

	private String convertRupeeToPaise(String paise) {
		BigDecimal b = new BigDecimal(paise);
		BigDecimal value = b.multiply(new BigDecimal("100"));
		return value.setScale(0, RoundingMode.UP).toString();
	}

	public SinglePayment createSinglePaymentOrder(SinglePaymentRequest singlePaymentRequest) {
		SinglePayment singlePayment = new SinglePayment();
		singlePayment.setRoleBasedId(singlePaymentRequest.getId());
		singlePayment.setName(singlePaymentRequest.getName());
		singlePayment.setEmailId(singlePaymentRequest.getEmailId());
		singlePayment.setPhoneNumber(singlePaymentRequest.getPhoneNumber());
		Order order = null;
		try {
			JSONObject options = new JSONObject();
			try {
				options.put("amount", convertRupeeToPaise(singlePaymentRequest.getAmount()));
				options.put("currency", "INR");				
				options.put("payment_capture", 1); 
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
			order = razorpayClient.Orders.create(options);
			if (order == null) {
				return null;
			}
			if (!org.json.JSONObject.NULL.equals(order.get("id"))) {
				singlePayment.setOrder_id((String) order.get("id"));
			}
			if (!org.json.JSONObject.NULL.equals(order.get("entity"))) {
				singlePayment.setEntity((String) order.get("entity"));
			}
			// convert paise to rupee
			if (!org.json.JSONObject.NULL.equals(order.get("amount"))) {
				singlePayment.setAmount(((int) order.get("amount") / 100));
			}
			// convert paise to rupee
			if (!org.json.JSONObject.NULL.equals(order.get("amount_paid"))) {
				singlePayment.setAmount_paid(((int) order.get("amount_paid")) / 100);
			}
			// convert paise to rupee
			if (!org.json.JSONObject.NULL.equals(order.get("amount_due"))) {
				singlePayment.setAmount_due(((int) order.get("amount_due")) / 100);
			}
			if (!org.json.JSONObject.NULL.equals(order.get("currency"))) {
				singlePayment.setCurrency((String) order.get("currency"));
			}
			if (!org.json.JSONObject.NULL.equals(order.get("receipt"))) {
				singlePayment.setReceipt((String) order.get("receipt"));
			}
			if (!org.json.JSONObject.NULL.equals(order.get("offer_id"))) {
				singlePayment.setOffer_id((String) order.get("offer_id"));
			}
			if (!org.json.JSONObject.NULL.equals(order.get("status"))) {
				singlePayment.setStatus((String) order.get("status"));
			}
			if (!org.json.JSONObject.NULL.equals(order.get("attempts"))) {
				singlePayment.setAttempts((int) order.get("attempts"));
			}
			if (!org.json.JSONObject.NULL.equals(order.get("created_at"))) {
				singlePayment.setCreated_at(order.get("created_at"));
			}
			singlePayment.setSecretId(secretId);
			return singlePayment;
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public boolean verifySinglePayment(VerifyPaymentRequest verifyPaymentRequest, SinglePayment singlePayment) {

		String generated_signature = "";
		try {
			generated_signature = Signature.calculateRFC2104HMAC(
					singlePayment.getOrder_id() + "|" + verifyPaymentRequest.getRazorpay_payment_id(), secretKey);
		} catch (SignatureException e) {
			logger.error(e.getMessage());
		}
		if (generated_signature.equals(verifyPaymentRequest.getSignature())) {
			return true;
		}
		return false;
	}

	public SubscriptionPayment fetchPaymentDetailsByPaymentId(String razorpay_payment_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("razorpay_payment_id", razorpay_payment_id);
		} catch (JSONException e) {
			logger.error(e.getMessage());
		}
		Payment payment = null;
		try {
			payment = razorpayClient.Payments.fetch(razorpay_payment_id);
		} catch (RazorpayException e) {
			logger.error(e.getMessage());
		}
		SubscriptionPayment subscriptionPayment = new SubscriptionPayment();
		if (!org.json.JSONObject.NULL.equals(payment.get("id"))) {
			subscriptionPayment.setRazorpayPaymentId((String) payment.get("id"));
		}
		// convert paise to rupee
		if (!org.json.JSONObject.NULL.equals(payment.get("amount"))) {
			subscriptionPayment.setAmount((int) payment.get("amount") / 100);
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("currency"))) {
			subscriptionPayment.setCurrency((String) payment.get("currency"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("status"))) {
			subscriptionPayment.setStatus((String) payment.get("status"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("order_id"))) {
			subscriptionPayment.setOrder_id((String) payment.get("order_id"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("invoice_id"))) {
			subscriptionPayment.setInvoice_id((String) payment.get("invoice_id"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("international"))) {
			subscriptionPayment.setInternational((boolean) payment.get("international"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("method"))) {
			subscriptionPayment.setMethod((String) payment.get("method"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("amount_refunded"))) {
			subscriptionPayment.setAmount_refunded((int) payment.get("amount_refunded"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("refund_status"))) {
			subscriptionPayment.setRefund_status((String) payment.get("refund_status"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("captured"))) {
			subscriptionPayment.setSinglepay_captured((boolean) payment.get("captured"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("description"))) {
			subscriptionPayment.setDescription((String) payment.get("description"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("card_id"))) {
			subscriptionPayment.setCard_id((String) payment.get("card_id"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("bank"))) {
			subscriptionPayment.setBank((String) payment.get("bank"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("wallet"))) {
			subscriptionPayment.setWallet((String) payment.get("wallet"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("vpa"))) {
			subscriptionPayment.setVpa((String) payment.get("vpa"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("email"))) {
			subscriptionPayment.setEmailId((String) payment.get("email"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("contact"))) {
			subscriptionPayment.setContact((String) payment.get("contact"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("fee"))) {
			subscriptionPayment.setFee((int) payment.get("fee"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("tax"))) {
			subscriptionPayment.setTax((int) payment.get("tax"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("error_code"))) {
			subscriptionPayment.setError_code((String) payment.get("error_code"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("error_description"))) {
			subscriptionPayment.setError_description((String) payment.get("error_description"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("error_source"))) {
			subscriptionPayment.setError_source((String) payment.get("error_source"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("error_step"))) {
			subscriptionPayment.setError_step((String) payment.get("error_step"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("error_reason"))) {
			subscriptionPayment.setError_reason((String) payment.get("error_reason"));
		}
		if (!org.json.JSONObject.NULL.equals(payment.get("created_at"))) {
			subscriptionPayment.setPay_created_at((Date) payment.get("created_at"));
		}
		return subscriptionPayment;
	}

	public boolean verifySubscriptionPayment(VerifyPaymentRequest verifyPaymentRequest) {
		String generated_signature = "";
		try {
			generated_signature = Signature.calculateRFC2104HMAC(
					verifyPaymentRequest.getSubscription_id() + "|" + verifyPaymentRequest.getRazorpay_payment_id(),
					secretKey);
		} catch (SignatureException e) {
			logger.error(e.getMessage());
		}
		if (generated_signature.equals(verifyPaymentRequest.getSignature())) {
			return true;
		}
		return false;

	}

	// private MemberSubscription getResponseFromRazorpay(Subscription subscription)
	// {
	// MemberSubscription membershipSub = new MemberSubscription();
	//
	// if (subscription != null) {
	// if (!org.json.JSONObject.NULL.equals(subscription.get("id"))) {
	// membershipSub.setRazorpaySubId((String) subscription.get("id"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("plan_id"))) {
	// membershipSub.setRazorpayPlanId((String) subscription.get("plan_id"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("customer_id"))) {
	// membershipSub.setCustomer_id((String) subscription.get("customer_id"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("status"))) {
	// membershipSub.setStatus((String) subscription.get("status"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("current_start"))) {
	// membershipSub.setCurrent_start((int) subscription.get("current_start"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("current_end"))) {
	// membershipSub.setCurrent_end((int) subscription.get("current_end"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("ended_at"))) {
	// membershipSub.setEnded_at((int) subscription.get("ended_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("quantity"))) {
	// membershipSub.setQuantity((int) subscription.get("quantity"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("charge_at"))) {
	// membershipSub.setCharge_at((int) subscription.get("charge_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("start_at"))) {
	// membershipSub.setStart_at((int) subscription.get("start_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("end_at"))) {
	// membershipSub.setEnd_at((int) subscription.get("end_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("auth_attempts"))) {
	// membershipSub.setAuth_attempts((int) subscription.get("auth_attempts"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("total_count"))) {
	// membershipSub.setTotal_count((int) subscription.get("total_count"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("paid_count"))) {
	// membershipSub.setPaid_count((int) subscription.get("paid_count"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("customer_notify"))) {
	// membershipSub.setCustomer_notify((boolean)
	// subscription.get("customer_notify"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("created_at"))) {
	// membershipSub.setCreated_at((Date) subscription.get("created_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("paused_at"))) {
	// membershipSub.setPaused_at((int) subscription.get("paused_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("pause_initiated_by")))
	// {
	// membershipSub.setPause_initiated_by((String)
	// subscription.get("pause_initiated_by"));
	// }
	// if
	// (!org.json.JSONObject.NULL.equals(subscription.get("cancel_initiated_by"))) {
	// membershipSub.setCancel_initiated_by((String)
	// subscription.get("cancel_initiated_by"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("expire_by"))) {
	// membershipSub.setExpire_by((int) subscription.get("expire_by"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("short_url"))) {
	// membershipSub.setShort_url((String) subscription.get("short_url"));
	// }
	// if
	// (!org.json.JSONObject.NULL.equals(subscription.get("has_scheduled_changes")))
	// {
	// membershipSub.setHas_scheduled_changes((String)
	// subscription.get("has_scheduled_changes"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("schedule_change_at")))
	// {
	// membershipSub.setSchedule_change_at((String)
	// subscription.get("schedule_change_at"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("source"))) {
	// membershipSub.setSource((String) subscription.get("source"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("remaining_count"))) {
	// membershipSub.setRemaining_count((int) subscription.get("remaining_count"));
	// }
	// if (!org.json.JSONObject.NULL.equals(subscription.get("payment_method"))) {
	// membershipSub.setRemaining_count((int) subscription.get("payment_method"));
	// }
	// return membershipSub;
	// } else {
	// return null;
	// }
	// }

}
