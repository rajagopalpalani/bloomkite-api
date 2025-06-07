package com.sowisetech.admin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:admappmessages.properties")
@Component
public class AdmAppMessages {

	@Value("${admin_added_successfully}")
	public String admin_added_successfully;

	@Value("${success}")
	public String success;

	@Value("${error}")
	public String error;

	@Value("${error_occured}")
	public String error_occured;

	@Value("${no_record_found}")
	public String no_record_found;

	@Value("${admin_updated_successfully}")
	public String admin_updated_successfully;

	@Value("${admin_deleted_successfully}")
	public String admin_deleted_successfully;

	@Value("${error_occured_remove}")
	public String error_occured_remove;

	@Value("${admin_detail_empty}")
	public String admin_detail_empty;

	@Value("${value_null_or_empty}")
	public String value_null_or_empty;

	@Value("${value_is_not_alpha}")
	public String value_is_not_alpha;

	@Value("${access_denied}")
	public String access_denied;

	@Value("${already_present_for_userId}")
	public String already_present_for_userId;

	@Value("${unauthorized}")
	public String unauthorized;

	@Value("${password_format_error}")
	public String password_format_error;

	@Value("${value_is_not_emailid}")
	public String value_is_not_emailid;
	
	@Value("${value_is_not_pincode}")
	public String value_is_not_pincode;

	@Value("${json_request_error}")
	public String json_request_error;

	@Value("${admin_already_present}")
	public String admin_already_present;

	@Value("${admin_is_empty}")
	public String admin_is_empty;

	@Value("${add_accountType}")
	public String add_accountType;

	public String getAdd_accountType() {
		return add_accountType;
	}

	public void setAdd_accountType(String add_accountType) {
		this.add_accountType = add_accountType;
	}

	@Value("${remove_accountType}")
	public String remove_accountType;

	public String getRemove_accountType() {
		return remove_accountType;
	}

	public void setRemove_accountType(String remove_accountType) {
		this.remove_accountType = remove_accountType;
	}

	@Value("${modify_accountType}")
	public String modify_accountType;

	public String getModify_accountType() {
		return modify_accountType;
	}

	public void setModify_accountType(String modify_accountType) {
		this.modify_accountType = modify_accountType;
	}

	@Value("${admin_product}")
	public String admin_product;

	@Value("${remove_product}")
	public String remove_product;

	@Value("${modify_product}")
	public String modify_product;
	
	@Value("${party_not_found}")
	public String party_not_found;

	public String getAdmin_added_successfully() {
		return admin_added_successfully;
	}

	public void setAdmin_added_successfully(String admin_added_successfully) {
		this.admin_added_successfully = admin_added_successfully;
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

	public String getError_occured() {
		return error_occured;
	}

	public void setError_occured(String error_occured) {
		this.error_occured = error_occured;
	}

	public String getNo_record_found() {
		return no_record_found;
	}

	public void setNo_record_found(String no_record_found) {
		this.no_record_found = no_record_found;
	}

	public String getAdmin_updated_successfully() {
		return admin_updated_successfully;
	}

	public void setAdmin_updated_successfully(String admin_updated_successfully) {
		this.admin_updated_successfully = admin_updated_successfully;
	}

	public String getAdmin_deleted_successfully() {
		return admin_deleted_successfully;
	}

	public void setAdmin_deleted_successfully(String admin_deleted_successfully) {
		this.admin_deleted_successfully = admin_deleted_successfully;
	}

	public String getError_occured_remove() {
		return error_occured_remove;
	}

	public void setError_occured_remove(String error_occured_remove) {
		this.error_occured_remove = error_occured_remove;
	}

	public String getAdmin_detail_empty() {
		return admin_detail_empty;
	}

	public void setAdmin_detail_empty(String admin_detail_empty) {
		this.admin_detail_empty = admin_detail_empty;
	}

	public String getValue_null_or_empty() {
		return value_null_or_empty;
	}

	public void setValue_null_or_empty(String value_null_or_empty) {
		this.value_null_or_empty = value_null_or_empty;
	}

	public String getValue_is_not_alpha() {
		return value_is_not_alpha;
	}

	public void setValue_is_not_alpha(String value_is_not_alpha) {
		this.value_is_not_alpha = value_is_not_alpha;
	}

	public String getAccess_denied() {
		return access_denied;
	}

	public void setAccess_denied(String access_denied) {
		this.access_denied = access_denied;
	}

	public String getAlready_present_for_userId() {
		return already_present_for_userId;
	}

	public void setAlready_present_for_userId(String already_present_for_userId) {
		this.already_present_for_userId = already_present_for_userId;
	}

	public String getUnauthorized() {
		return unauthorized;
	}

	public void setUnauthorized(String unauthorized) {
		this.unauthorized = unauthorized;
	}

	public String getPassword_format_error() {
		return password_format_error;
	}

	public void setPassword_format_error(String password_format_error) {
		this.password_format_error = password_format_error;
	}

	public String getValue_is_not_emailid() {
		return value_is_not_emailid;
	}

	public void setValue_is_not_emailid(String value_is_not_emailid) {
		this.value_is_not_emailid = value_is_not_emailid;
	}

	public String getJson_request_error() {
		return json_request_error;
	}

	public void setJson_request_error(String json_request_error) {
		this.json_request_error = json_request_error;
	}

	public String getAdmin_already_present() {
		return admin_already_present;
	}

	public void setAdmin_already_present(String admin_already_present) {
		this.admin_already_present = admin_already_present;
	}

	public String getAdmin_is_empty() {
		return admin_is_empty;
	}

	public void setAdmin_is_empty(String admin_is_empty) {
		this.admin_is_empty = admin_is_empty;
	}

	public String getAdmin_product() {
		return admin_product;
	}

	public void setAdmin_product(String admin_product) {
		this.admin_product = admin_product;
	}

	public String getRemove_product() {
		return remove_product;
	}

	public void setRemove_product(String remove_product) {
		this.remove_product = remove_product;
	}

	public String getModify_product() {
		return modify_product;
	}

	public void setModify_product(String modify_product) {
		this.modify_product = modify_product;
	}

	public String getValue_is_not_pincode() {
		return value_is_not_pincode;
	}

	public void setValue_is_not_pincode(String value_is_not_pincode) {
		this.value_is_not_pincode = value_is_not_pincode;
	}

	public String getParty_not_found() {
		return party_not_found;
	}

	public void setParty_not_found(String party_not_found) {
		this.party_not_found = party_not_found;
	}

}
