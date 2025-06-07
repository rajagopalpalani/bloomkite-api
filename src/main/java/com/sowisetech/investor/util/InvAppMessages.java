package com.sowisetech.investor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:invappmessages.properties")
@Component
public class InvAppMessages {

	@Value("${investor_added_successfully}")
	public String investor_added_successfully;

	@Value("${investor_detail_empty}")
	public String investor_detail_empty;

	@Value("${investor_updated_successfully}")
	public String investor_updated_successfully;

	@Value("${no_record_found}")
	public String no_record_found;

	@Value("${investor_deleted_successfully}")
	public String investor_deleted_successfully;

	@Value("${json_request_error}")
	public String json_request_error;

	@Value("${value_null_or_empty}")
	public String value_null_or_empty;

	@Value("${value_not_number}")
	public String value_not_number;

	@Value("${value_not_positive}")
	public String value_not_positive;

	@Value("${value_invalid_percent}")
	public String value_invalid_percent;

	@Value("${value_is_not_alpha}")
	public String value_is_not_alpha;

	@Value("${value_is_not_numeric}")
	public String value_is_not_numeric;

	@Value("${value_is_not_emailid}")
	public String value_is_not_emailid;

	@Value("${phonenumber_length_error}")
	public String phonenumber_length_error;

	@Value("${pincode_length_error}")
	public String pincode_length_error;

	@Value("${password_format_error}")
	public String password_format_error;

	@Value("${dob_error}")
	public String dob_error;

	@Value("${gender_error}")
	public String gender_error;

	@Value("${scale_error}")
	public String scale_error;

	@Value("${invid_and_categoryid_combination_present}")
	public String invid_and_categoryid_combination_present;

	@Value("${no_categoryid_found}")
	public String no_categoryid_found;

	@Value("${fields_cannot_be_empty}")
	public String fields_cannot_be_empty;

	@Value("${investor_already_present}")
	public String investor_already_present;

	@Value("${pan_error}")
	public String pan_error;

	@Value("${error_occured}")
	public String error_occured;

	@Value("${error_occured_remove}")
	public String error_occured_remove;

	@Value("${success}")
	public String success;

	@Value("${error}")
	public String error;

	@Value("${invalid_pagenum}")
	public String invalid_pagenum;

	@Value("${access_denied}")
	public String access_denied;

	@Value("${unauthorized}")
	public String unauthorized;

	@Value("${mandatory_fields_invId}")
	public String mandatory_fields_invId;

	@Value("${mandatory_fields_intId}")
	public String mandatory_fields_intId;

	public String getInvestor_added_successfully() {
		return investor_added_successfully;
	}

	public void setInvestor_added_successfully(String investor_added_successfully) {
		this.investor_added_successfully = investor_added_successfully;
	}

	public String getInvestor_detail_empty() {
		return investor_detail_empty;
	}

	public void setInvestor_detail_empty(String investor_detail_empty) {
		this.investor_detail_empty = investor_detail_empty;
	}

	public String getInvestor_updated_successfully() {
		return investor_updated_successfully;
	}

	public void setInvestor_updated_successfully(String investor_updated_successfully) {
		this.investor_updated_successfully = investor_updated_successfully;
	}

	public String getNo_record_found() {
		return no_record_found;
	}

	public void setNo_record_found(String no_record_found) {
		this.no_record_found = no_record_found;
	}

	public String getInvestor_deleted_successfully() {
		return investor_deleted_successfully;
	}

	public void setInvestor_deleted_successfully(String investor_deleted_successfully) {
		this.investor_deleted_successfully = investor_deleted_successfully;
	}

	public String getJson_request_error() {
		return json_request_error;
	}

	public void setJson_request_error(String json_request_error) {
		this.json_request_error = json_request_error;
	}

	public String getValue_null_or_empty() {
		return value_null_or_empty;
	}

	public void setValue_null_or_empty(String value_null_or_empty) {
		this.value_null_or_empty = value_null_or_empty;
	}

	public String getValue_not_number() {
		return value_not_number;
	}

	public void setValue_not_number(String value_not_number) {
		this.value_not_number = value_not_number;
	}

	public String getValue_not_positive() {
		return value_not_positive;
	}

	public void setValue_not_positive(String value_not_positive) {
		this.value_not_positive = value_not_positive;
	}

	public String getValue_invalid_percent() {
		return value_invalid_percent;
	}

	public void setValue_invalid_percent(String value_invalid_percent) {
		this.value_invalid_percent = value_invalid_percent;
	}

	public String getValue_is_not_alpha() {
		return value_is_not_alpha;
	}

	public void setValue_is_not_alpha(String value_is_not_alpha) {
		this.value_is_not_alpha = value_is_not_alpha;
	}

	public String getValue_is_not_numeric() {
		return value_is_not_numeric;
	}

	public void setValue_is_not_numeric(String value_is_not_numeric) {
		this.value_is_not_numeric = value_is_not_numeric;
	}

	public String getValue_is_not_emailid() {
		return value_is_not_emailid;
	}

	public void setValue_is_not_emailid(String value_is_not_emailid) {
		this.value_is_not_emailid = value_is_not_emailid;
	}

	public String getPhonenumber_length_error() {
		return phonenumber_length_error;
	}

	public void setPhonenumber_length_error(String phonenumber_length_error) {
		this.phonenumber_length_error = phonenumber_length_error;
	}

	public String getPincode_length_error() {
		return pincode_length_error;
	}

	public void setPincode_length_error(String pincode_length_error) {
		this.pincode_length_error = pincode_length_error;
	}

	public String getPassword_format_error() {
		return password_format_error;
	}

	public void setPassword_format_error(String password_format_error) {
		this.password_format_error = password_format_error;
	}

	public String getDob_error() {
		return dob_error;
	}

	public void setDob_error(String dob_error) {
		this.dob_error = dob_error;
	}

	public String getGender_error() {
		return gender_error;
	}

	public void setGender_error(String gender_error) {
		this.gender_error = gender_error;
	}

	public String getScale_error() {
		return scale_error;
	}

	public void setScale_error(String scale_error) {
		this.scale_error = scale_error;
	}

	public String getInvid_and_categoryid_combination_present() {
		return invid_and_categoryid_combination_present;
	}

	public void setInvid_and_categoryid_combination_present(String invid_and_categoryid_combination_present) {
		this.invid_and_categoryid_combination_present = invid_and_categoryid_combination_present;
	}

	public String getNo_categoryid_found() {
		return no_categoryid_found;
	}

	public void setNo_categoryid_found(String no_categoryid_found) {
		this.no_categoryid_found = no_categoryid_found;
	}

	public String getFields_cannot_be_empty() {
		return fields_cannot_be_empty;
	}

	public void setFields_cannot_be_empty(String fields_cannot_be_empty) {
		this.fields_cannot_be_empty = fields_cannot_be_empty;
	}

	public String getInvestor_already_present() {
		return investor_already_present;
	}

	public void setInvestor_already_present(String investor_already_present) {
		this.investor_already_present = investor_already_present;
	}

	public String getPan_error() {
		return pan_error;
	}

	public void setPan_error(String pan_error) {
		this.pan_error = pan_error;
	}

	public String getError_occured() {
		return error_occured;
	}

	public void setError_occured(String error_occured) {
		this.error_occured = error_occured;
	}

	public String getError_occured_remove() {
		return error_occured_remove;
	}

	public void setError_occured_remove(String error_occured_remove) {
		this.error_occured_remove = error_occured_remove;
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

	public String getInvalid_pagenum() {
		return invalid_pagenum;
	}

	public void setInvalid_pagenum(String invalid_pagenum) {
		this.invalid_pagenum = invalid_pagenum;
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

	public String getMandatory_fields_invId() {
		return mandatory_fields_invId;
	}

	public void setMandatory_fields_invId(String mandatory_fields_invId) {
		this.mandatory_fields_invId = mandatory_fields_invId;
	}

	public String getMandatory_fields_intId() {
		return mandatory_fields_intId;
	}

	public void setMandatory_fields_intId(String mandatory_fields_intId) {
		this.mandatory_fields_intId = mandatory_fields_intId;
	}

}
