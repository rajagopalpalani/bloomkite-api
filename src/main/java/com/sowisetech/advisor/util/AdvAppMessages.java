package com.sowisetech.advisor.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:advappmessages.properties")
@Component
public class AdvAppMessages {

	@Value("${empty_record}")
	public String empty_record;

	@Value("${no_record_found}")
	public String no_record_found;

	@Value("${no_record_found_with_emailid}")
	public String no_record_found_with_emailid;

	@Value("${advisor_added_successfully}")
	public String advisor_added_successfully;

	@Value("${advisor_info_added_successfully}")
	public String advisor_info_added_successfully;

	@Value("${advisor_detail_empty}")
	public String advisor_detail_empty;

	@Value("${advisor_updated_successfully}")
	public String advisor_updated_successfully;

	@Value("${advisor_deleted_successfully}")
	public String advisor_deleted_successfully;

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

	@Value("${pan_error}")
	public String pan_error;

	@Value("${text_format_error}")
	public String text_format_error;

	@Value("${expertise_level_error}")
	public String expertise_level_error;

	@Value("${remuneration_error}")
	public String remuneration_error;

	@Value("${validtill_date_error}")
	public String validtill_date_error;

	@Value("${legalno_error}")
	public String legalno_error;

	@Value("${aboutme_error}")
	public String aboutme_error;

	@Value("${year_error}")
	public String year_error;

	@Value("${value_is_not_image}")
	public String value_is_not_image;

	@Value("${degree_error}")
	public String degree_error;

	@Value("${from_date_error}")
	public String from_date_error;

	@Value("${to_date_error}")
	public String to_date_error;

	@Value("${fields_cannot_be_empty}")
	public String fields_cannot_be_empty;

	@Value("${advisor_already_present}")
	public String advisor_already_present;

	@Value("${advisor_already_present_not_verified}")
	public String advisor_already_present_not_verified;

	@Value("${advisor_already_present_phone}")
	public String advisor_already_present_phone;

	@Value("${advisor_already_present_phone_not_verified}")
	public String advisor_already_present_phone_not_verified;

	@Value("${advisor_already_present_pan}")
	public String advisor_already_present_pan;

	@Value("${advisor_already_present_pan_not_verified}")
	public String advisor_already_present_pan_not_verified;

	@Value("${from_to_error}")
	public String from_to_error;

	@Value("${password_changed_successfully}")
	public String password_changed_successfully;

	@Value("${incorrect_password}")
	public String incorrect_password;

	@Value("${value_is_not_video}")
	public String value_is_not_video;

	@Value("${certificate_year_error}")
	public String certificate_year_error;

	@Value("${advproduct_not_found}")
	public String advproduct_not_found;

	@Value("${award_not_found}")
	public String award_not_found;

	@Value("${certificate_not_found}")
	public String certificate_not_found;

	@Value("${education_not_found}")
	public String education_not_found;

	@Value("${experience_not_found}")
	public String experience_not_found;

	@Value("${brand_should_be_added}")
	public String brand_should_be_added;

	@Value("${error_occured}")
	public String error_occured;

	@Value("${error_occured_remove}")
	public String error_occured_remove;

	@Value("${error_occured_brandrank}")
	public String error_occured_brandrank;

	@Value("${success}")
	public String success;

	@Value("${error}")
	public String error;

	@Value("${investor_added_successfully}")
	public String investor_added_successfully;

	// @Value("${investor_already_present}")
	// public String investor_already_present;
	//
	// @Value("${investor_already_present_not_verified}")
	// public String investor_already_present_not_verified;

	@Value("${user_already_present_username}")
	public String user_already_present_username;

	@Value("${user_already_present_username_not_verified}")
	public String user_already_present_username_not_verified;
	//
	// @Value("${investor_already_present_phone}")
	// public String investor_already_present_phone;
	//
	// @Value("${investor_already_present_phone_not_verified}")
	// public String investor_already_present_phone_not_verified;

	@Value("${roleId_not_found}")
	public String roleId_not_found;

	@Value("${login_failed}")
	public String login_failed;

	@Value("${value_is_not_alphaNumeric}")
	public String value_is_not_alphaNumeric;

	@Value("${error_occured_upload}")
	public String error_occured_upload;

	@Value("${keyPeople_added_successfully}")
	public String keyPeople_added_successfully;

	@Value("${file_deleted_successfully}")
	public String file_deleted_successfully;

	@Value("${invalid_pagenum}")
	public String invalid_pagenum;

	@Value("${account_already_verified}")
	public String account_already_verified;

	@Value("${account_verified_successfully}")
	public String account_verified_successfully;

	@Value("${account_cannot_verified}")
	public String account_cannot_verified;

	@Value("${access_denied}")
	public String access_denied;

	@Value("${cannot_login_not_verified}")
	public String cannot_login_not_verified;

	@Value("${reset_password_mail_sent}")
	public String reset_password_mail_sent;

	@Value("${link_expired}")
	public String link_expired;

	@Value("${verification_mail_sent}")
	public String verification_mail_sent;

	@Value("${team_Member_Deactivated}")
	public String team_Member_Deactivated;

	@Value("${token_not_valid}")
	public String token_not_valid;

	@Value("${cannot_reset_password}")
	public String cannot_reset_password;

	@Value("${workFlowStatus_added_successfully}")
	public String workFlowStatus_added_successfully;

	@Value("${workFlowStatus_added_asApproved_successfully}")
	public String workFlowStatus_added_asApproved_successfully;

	@Value("${followers_added_successfully}")
	public String followers_added_successfully;

	@Value("${followers_already_present}")
	public String followers_already_present;

	@Value("${Cannot_follow_the_user}")
	public String Cannot_follow_the_user;

	@Value("${follower_blocked}")
	public String follower_blocked;

	@Value("${otp_verified}")
	public String otp_verified;

	@Value("${otp_not_verified}")
	public String otp_not_verified;

	@Value("${otp_expired}")
	public String otp_expired;

	@Value("${Cannot_follow_the_investor}")
	public String Cannot_follow_the_investor;

	@Value("${follower_already_blocked}")
	public String follower_already_blocked;

	@Value("${link_not_valid}")
	public String link_not_valid;

	@Value("${unauthorized}")
	public String unauthorized;

	@Value("${refollowers_added_successfully}")
	public String refollowers_added_successfully;

	@Value("${follower_approved}")
	public String follower_approved;

	@Value("${chatuser_added_successfully}")
	public String chatuser_added_successfully;

	@Value("${chatuser_updated_successfully}")
	public String chatuser_updated_successfully;

	@Value("${chatuser_already_present}")
	public String chatuser_already_present;

	@Value("${chatuser_approved_successfully}")
	public String chatuser_approved_successfully;

	@Value("${chatuser_blocked_successfully}")
	public String chatuser_blocked_successfully;

	@Value("${user_already_present_emailid}")
	public String user_already_present_emailid;

	@Value("${user_already_present_phone}")
	public String user_already_present_phone;

	@Value("${user_already_present_with_username}")
	public String user_already_present_with_username;

	@Value("${user_already_present_pan}")
	public String user_already_present_pan;

	@Value("${user_already_present_emailid_disabled}")
	public String user_already_present_emailid_disabled;

	@Value("${user_already_present_phone_disabled}")
	public String user_already_present_phone_disabled;

	@Value("${user_already_present_username_disabled}")
	public String user_already_present_username_disabled;

	@Value("${user_already_present_pan_disabled}")
	public String user_already_present_pan_disabled;

	@Value("${city_error}")
	public String city_error;

	@Value("${chat_saved_successfully}")
	public String chat_saved_successfully;

	@Value("${otp_sent_successfully}")
	public String otp_sent_successfully;

	@Value("${otp_not_sent}")
	public String otp_not_sent;

	@Value("${user_not_found}")
	public String user_not_found;

	@Value("${password_not_found}")
	public String password_not_found;

	@Value("${advisor_revoked_successfully}")
	public String advisor_revoked_successfully;
	
	@Value("${profile_changed_successfully}")
	public String profile_changed_successfully;

	@Value("${required_reason_revoked}")
	public String required_reason_revoked;

	@Value("${follower_unfollowed}")
	public String follower_unfollowed;

	@Value("${user_not_available}")
	public String user_not_available;

	@Value("${mandatory_fields}")
	public String mandatory_fields;

	@Value("${mandatory_fields_parentPartyId}")
	public String mandatory_fields_parentPartyId;

	@Value("${mandatory_fields_userName}")
	public String mandatory_fields_userName;

	@Value("${mandatory_fields_signup}")
	public String mandatory_fields_signup;

	@Value("${mandatory_fields_advSignup}")
	public String mandatory_fields_advSignup;

	@Value("${mandatory_fields_role}")
	public String mandatory_fields_role;

	@Value("${mandatory_fields_password}")
	public String mandatory_fields_password;

	@Value("${mandatory_fields_emailId}")
	public String mandatory_fields_emailId;

	@Value("${mandatory_fields_key}")
	public String mandatory_fields_key;

	@Value("${mandatory_fields_keyPeopleId}")
	public String mandatory_fields_keyPeopleId;

	@Value("${mandatory_fields_status}")
	public String mandatory_fields_status;

	@Value("${mandatory_fields_phone}")
	public String mandatory_fields_phone;

	@Value("${mandatory_fields_otp}")
	public String mandatory_fields_otp;

	@Value("${mandatory_fields_followers}")
	public String mandatory_fields_followers;

	@Value("${mandatory_fields_block}")
	public String mandatory_fields_block;

	@Value("${mandatory_fields_uploadPdf}")
	public String mandatory_fields_uploadPdf;

	@Value("${mandatory_fields_city}")
	public String mandatory_fields_city;

	@Value("${mandatory_fields_validatePassword}")
	public String mandatory_fields_validatePassword;

	@Value("${mandatory_fields_userId}")
	public String mandatory_fields_userId;

	@Value("${mandatory_fields_advisor}")
	public String mandatory_fields_advisor;

	@Value("${mandatory_fields_explore}")
	public String mandatory_fields_explore;

	@Value("${mandatory_fields_keyFields}")
	public String mandatory_fields_keyFields;

	@Value("${mandatory_fields_file}")
	public String mandatory_fields_file;
	
	@Value("${party_not_found}")
	public String party_not_found;

	@Value("${brandscomment_added_successfully}")
	public String brandscomment_added_successfully;
	
	@Value("${brandscomment_moderated_successfully}")
	public String brandscomment_moderated_successfully;
	
	@Value("${record_deleted_successfully}")
	public String record_deleted_successfully;
	
	@Value("${mandatory_fields_bloggerId}")
	public String mandatory_fields_bloggerId;

	@Value("${blogger_detail_empty}")
	public String blogger_detail_empty;
	
	@Value("${mandatory_fields_brandId}")
	public String mandatory_fields_brandId;
	
	@Value("${mandatory_fields_comment}")
	public String mandatory_fields_comment;
	
	@Value("${mandatory_fields_commentId}")
	public String mandatory_fields_commentId;
	
	@Value("${mandatory_fields_commentId}")
	public String mandatory_fields_replyComments;
	
	@Value("${commentVote_added_successfully}")
	public String commentVote_added_successfully;
	
	@Value("${mandatory_fields_voteComment}")
	public String mandatory_fields_voteComment;
	
	@Value("${mandatory_fields_voteRemoveComment}")
	public String mandatory_fields_voteRemoveComment;
	
	@Value("${commentvote_removed_successfully}")
	public String commentvote_removed_successfully;
	
	@Value("${mandatory_fields_fetchVoteAddress}")
	public String mandatory_fields_fetchVoteAddress;
	
	public String getMandatory_fields_voteComment() {
		return mandatory_fields_voteComment;
	}

	public void setMandatory_fields_voteComment(String mandatory_fields_voteComment) {
		this.mandatory_fields_voteComment = mandatory_fields_voteComment;
	}

	public String getEmpty_record() {
		return empty_record;
	}

	public void setEmpty_record(String empty_record) {
		this.empty_record = empty_record;
	}

	public String getNo_record_found() {
		return no_record_found;
	}

	public void setNo_record_found(String no_record_found) {
		this.no_record_found = no_record_found;
	}

	public String getNo_record_found_with_emailid() {
		return no_record_found_with_emailid;
	}

	public void setNo_record_found_with_emailid(String no_record_found_with_emailid) {
		this.no_record_found_with_emailid = no_record_found_with_emailid;
	}

	public String getAdvisor_added_successfully() {
		return advisor_added_successfully;
	}

	public void setAdvisor_added_successfully(String advisor_added_successfully) {
		this.advisor_added_successfully = advisor_added_successfully;
	}

	public String getAdvisor_info_added_successfully() {
		return advisor_info_added_successfully;
	}

	public void setAdvisor_info_added_successfully(String advisor_info_added_successfully) {
		this.advisor_info_added_successfully = advisor_info_added_successfully;
	}

	public String getAdvisor_detail_empty() {
		return advisor_detail_empty;
	}

	public void setAdvisor_detail_empty(String advisor_detail_empty) {
		this.advisor_detail_empty = advisor_detail_empty;
	}

	public String getAdvisor_updated_successfully() {
		return advisor_updated_successfully;
	}

	public void setAdvisor_updated_successfully(String advisor_updated_successfully) {
		this.advisor_updated_successfully = advisor_updated_successfully;
	}

	public String getAdvisor_deleted_successfully() {
		return advisor_deleted_successfully;
	}

	public void setAdvisor_deleted_successfully(String advisor_deleted_successfully) {
		this.advisor_deleted_successfully = advisor_deleted_successfully;
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

	public String getPan_error() {
		return pan_error;
	}

	public void setPan_error(String pan_error) {
		this.pan_error = pan_error;
	}

	public String getText_format_error() {
		return text_format_error;
	}

	public void setText_format_error(String text_format_error) {
		this.text_format_error = text_format_error;
	}

	public String getExpertise_level_error() {
		return expertise_level_error;
	}

	public void setExpertise_level_error(String expertise_level_error) {
		this.expertise_level_error = expertise_level_error;
	}

	public String getRemuneration_error() {
		return remuneration_error;
	}

	public void setRemuneration_error(String remuneration_error) {
		this.remuneration_error = remuneration_error;
	}

	public String getValidtill_date_error() {
		return validtill_date_error;
	}

	public void setValidtill_date_error(String validtill_date_error) {
		this.validtill_date_error = validtill_date_error;
	}

	public String getLegalno_error() {
		return legalno_error;
	}

	public void setLegalno_error(String legalno_error) {
		this.legalno_error = legalno_error;
	}

	public String getAboutme_error() {
		return aboutme_error;
	}

	public void setAboutme_error(String aboutme_error) {
		this.aboutme_error = aboutme_error;
	}

	public String getYear_error() {
		return year_error;
	}

	public void setYear_error(String year_error) {
		this.year_error = year_error;
	}

	public String getValue_is_not_image() {
		return value_is_not_image;
	}

	public void setValue_is_not_image(String value_is_not_image) {
		this.value_is_not_image = value_is_not_image;
	}

	public String getDegree_error() {
		return degree_error;
	}

	public void setDegree_error(String degree_error) {
		this.degree_error = degree_error;
	}

	public String getFrom_date_error() {
		return from_date_error;
	}

	public void setFrom_date_error(String from_date_error) {
		this.from_date_error = from_date_error;
	}

	public String getTo_date_error() {
		return to_date_error;
	}

	public void setTo_date_error(String to_date_error) {
		this.to_date_error = to_date_error;
	}

	public String getFields_cannot_be_empty() {
		return fields_cannot_be_empty;
	}

	public void setFields_cannot_be_empty(String fields_cannot_be_empty) {
		this.fields_cannot_be_empty = fields_cannot_be_empty;
	}

	public String getAdvisor_already_present() {
		return advisor_already_present;
	}

	public void setAdvisor_already_present(String advisor_already_present) {
		this.advisor_already_present = advisor_already_present;
	}

	public String getAdvisor_already_present_not_verified() {
		return advisor_already_present_not_verified;
	}

	public void setAdvisor_already_present_not_verified(String advisor_already_present_not_verified) {
		this.advisor_already_present_not_verified = advisor_already_present_not_verified;
	}

	public String getAdvisor_already_present_phone() {
		return advisor_already_present_phone;
	}

	public void setAdvisor_already_present_phone(String advisor_already_present_phone) {
		this.advisor_already_present_phone = advisor_already_present_phone;
	}

	public String getAdvisor_already_present_phone_not_verified() {
		return advisor_already_present_phone_not_verified;
	}

	public void setAdvisor_already_present_phone_not_verified(String advisor_already_present_phone_not_verified) {
		this.advisor_already_present_phone_not_verified = advisor_already_present_phone_not_verified;
	}

	public String getAdvisor_already_present_pan() {
		return advisor_already_present_pan;
	}

	public void setAdvisor_already_present_pan(String advisor_already_present_pan) {
		this.advisor_already_present_pan = advisor_already_present_pan;
	}

	public String getAdvisor_already_present_pan_not_verified() {
		return advisor_already_present_pan_not_verified;
	}

	public void setAdvisor_already_present_pan_not_verified(String advisor_already_present_pan_not_verified) {
		this.advisor_already_present_pan_not_verified = advisor_already_present_pan_not_verified;
	}

	public String getFrom_to_error() {
		return from_to_error;
	}

	public void setFrom_to_error(String from_to_error) {
		this.from_to_error = from_to_error;
	}

	public String getPassword_changed_successfully() {
		return password_changed_successfully;
	}

	public void setPassword_changed_successfully(String password_changed_successfully) {
		this.password_changed_successfully = password_changed_successfully;
	}

	public String getIncorrect_password() {
		return incorrect_password;
	}

	public void setIncorrect_password(String incorrect_password) {
		this.incorrect_password = incorrect_password;
	}

	public String getValue_is_not_video() {
		return value_is_not_video;
	}

	public void setValue_is_not_video(String value_is_not_video) {
		this.value_is_not_video = value_is_not_video;
	}

	public String getCertificate_year_error() {
		return certificate_year_error;
	}

	public void setCertificate_year_error(String certificate_year_error) {
		this.certificate_year_error = certificate_year_error;
	}

	public String getAdvproduct_not_found() {
		return advproduct_not_found;
	}

	public void setAdvproduct_not_found(String advproduct_not_found) {
		this.advproduct_not_found = advproduct_not_found;
	}

	public String getAward_not_found() {
		return award_not_found;
	}

	public void setAward_not_found(String award_not_found) {
		this.award_not_found = award_not_found;
	}

	public String getCertificate_not_found() {
		return certificate_not_found;
	}

	public void setCertificate_not_found(String certificate_not_found) {
		this.certificate_not_found = certificate_not_found;
	}

	public String getEducation_not_found() {
		return education_not_found;
	}

	public void setEducation_not_found(String education_not_found) {
		this.education_not_found = education_not_found;
	}

	public String getExperience_not_found() {
		return experience_not_found;
	}

	public void setExperience_not_found(String experience_not_found) {
		this.experience_not_found = experience_not_found;
	}

	public String getBrand_should_be_added() {
		return brand_should_be_added;
	}

	public void setBrand_should_be_added(String brand_should_be_added) {
		this.brand_should_be_added = brand_should_be_added;
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

	public String getError_occured_brandrank() {
		return error_occured_brandrank;
	}

	public void setError_occured_brandrank(String error_occured_brandrank) {
		this.error_occured_brandrank = error_occured_brandrank;
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

	public String getInvestor_added_successfully() {
		return investor_added_successfully;
	}

	public void setInvestor_added_successfully(String investor_added_successfully) {
		this.investor_added_successfully = investor_added_successfully;
	}

	// public String getInvestor_already_present() {
	// return investor_already_present;
	// }
	//
	// public void setInvestor_already_present(String investor_already_present) {
	// this.investor_already_present = investor_already_present;
	// }
	//
	// public String getInvestor_already_present_not_verified() {
	// return investor_already_present_not_verified;
	// }
	//
	// public void setInvestor_already_present_not_verified(String
	// investor_already_present_not_verified) {
	// this.investor_already_present_not_verified =
	// investor_already_present_not_verified;
	// }

	public String getUser_already_present_username() {
		return user_already_present_username;
	}

	public void setUser_already_present_username(String user_already_present_username) {
		this.user_already_present_username = user_already_present_username;
	}

	public String getUser_already_present_username_not_verified() {
		return user_already_present_username_not_verified;
	}

	public void setUser_already_present_username_not_verified(String user_already_present_username_not_verified) {
		this.user_already_present_username_not_verified = user_already_present_username_not_verified;
	}

	// public String getInvestor_already_present_phone() {
	// return investor_already_present_phone;
	// }
	//
	// public void setInvestor_already_present_phone(String
	// investor_already_present_phone) {
	// this.investor_already_present_phone = investor_already_present_phone;
	// }
	//
	// public String getInvestor_already_present_phone_not_verified() {
	// return investor_already_present_phone_not_verified;
	// }
	//
	// public void setInvestor_already_present_phone_not_verified(String
	// investor_already_present_phone_not_verified) {
	// this.investor_already_present_phone_not_verified =
	// investor_already_present_phone_not_verified;
	// }

	public String getRoleId_not_found() {
		return roleId_not_found;
	}

	public void setRoleId_not_found(String roleId_not_found) {
		this.roleId_not_found = roleId_not_found;
	}

	public String getLogin_failed() {
		return login_failed;
	}

	public void setLogin_failed(String login_failed) {
		this.login_failed = login_failed;
	}

	public String getValue_is_not_alphaNumeric() {
		return value_is_not_alphaNumeric;
	}

	public void setValue_is_not_alphaNumeric(String value_is_not_alphaNumeric) {
		this.value_is_not_alphaNumeric = value_is_not_alphaNumeric;
	}

	public String getError_occured_upload() {
		return error_occured_upload;
	}

	public void setError_occured_upload(String error_occured_upload) {
		this.error_occured_upload = error_occured_upload;
	}

	public String getKeyPeople_added_successfully() {
		return keyPeople_added_successfully;
	}

	public void setKeyPeople_added_successfully(String keyPeople_added_successfully) {
		this.keyPeople_added_successfully = keyPeople_added_successfully;
	}

	public String getFile_deleted_successfully() {
		return file_deleted_successfully;
	}

	public void setFile_deleted_successfully(String file_deleted_successfully) {
		this.file_deleted_successfully = file_deleted_successfully;
	}

	public String getInvalid_pagenum() {
		return invalid_pagenum;
	}

	public void setInvalid_pagenum(String invalid_pagenum) {
		this.invalid_pagenum = invalid_pagenum;
	}

	public String getAccount_already_verified() {
		return account_already_verified;
	}

	public void setAccount_already_verified(String account_already_verified) {
		this.account_already_verified = account_already_verified;
	}

	public String getAccount_verified_successfully() {
		return account_verified_successfully;
	}

	public void setAccount_verified_successfully(String account_verified_successfully) {
		this.account_verified_successfully = account_verified_successfully;
	}

	public String getAccount_cannot_verified() {
		return account_cannot_verified;
	}

	public void setAccount_cannot_verified(String account_cannot_verified) {
		this.account_cannot_verified = account_cannot_verified;
	}

	public String getAccess_denied() {
		return access_denied;
	}

	public void setAccess_denied(String access_denied) {
		this.access_denied = access_denied;
	}

	public String getCannot_login_not_verified() {
		return cannot_login_not_verified;
	}

	public void setCannot_login_not_verified(String cannot_login_not_verified) {
		this.cannot_login_not_verified = cannot_login_not_verified;
	}

	public String getReset_password_mail_sent() {
		return reset_password_mail_sent;
	}

	public void setReset_password_mail_sent(String reset_password_mail_sent) {
		this.reset_password_mail_sent = reset_password_mail_sent;
	}

	public String getLink_expired() {
		return link_expired;
	}

	public void setLink_expired(String link_expired) {
		this.link_expired = link_expired;
	}

	public String getVerification_mail_sent() {
		return verification_mail_sent;
	}

	public void setVerification_mail_sent(String verification_mail_sent) {
		this.verification_mail_sent = verification_mail_sent;
	}

	public String getTeam_Member_Deactivated() {
		return team_Member_Deactivated;
	}

	public void setTeam_Member_Deactivated(String team_Member_Deactivated) {
		this.team_Member_Deactivated = team_Member_Deactivated;
	}

	public String getToken_not_valid() {
		return token_not_valid;
	}

	public void setToken_not_valid(String token_not_valid) {
		this.token_not_valid = token_not_valid;
	}

	public String getCannot_reset_password() {
		return cannot_reset_password;
	}

	public void setCannot_reset_password(String cannot_reset_password) {
		this.cannot_reset_password = cannot_reset_password;
	}

	public String getWorkFlowStatus_added_successfully() {
		return workFlowStatus_added_successfully;
	}

	public void setWorkFlowStatus_added_successfully(String workFlowStatus_added_successfully) {
		this.workFlowStatus_added_successfully = workFlowStatus_added_successfully;
	}

	public String getFollowers_added_successfully() {
		return followers_added_successfully;
	}

	public void setFollowers_added_successfully(String followers_added_successfully) {
		this.followers_added_successfully = followers_added_successfully;
	}

	public String getFollowers_already_present() {
		return followers_already_present;
	}

	public void setFollowers_already_present(String followers_already_present) {
		this.followers_already_present = followers_already_present;
	}

	public String getCannot_follow_the_user() {
		return Cannot_follow_the_user;
	}

	public void setCannot_follow_the_user(String cannot_follow_the_user) {
		Cannot_follow_the_user = cannot_follow_the_user;
	}

	public String getFollower_blocked() {
		return follower_blocked;
	}

	public void setFollower_blocked(String follower_blocked) {
		this.follower_blocked = follower_blocked;
	}

	public String getOtp_verified() {
		return otp_verified;
	}

	public void setOtp_verified(String otp_verified) {
		this.otp_verified = otp_verified;
	}

	public String getOtp_not_verified() {
		return otp_not_verified;
	}

	public void setOtp_not_verified(String otp_not_verified) {
		this.otp_not_verified = otp_not_verified;
	}

	public String getOtp_expired() {
		return otp_expired;
	}

	public void setOtp_expired(String otp_expired) {
		this.otp_expired = otp_expired;
	}

	public String getCannot_follow_the_investor() {
		return Cannot_follow_the_investor;
	}

	public void setCannot_follow_the_investor(String cannot_follow_the_investor) {
		Cannot_follow_the_investor = cannot_follow_the_investor;
	}

	public String getFollower_already_blocked() {
		return follower_already_blocked;
	}

	public void setFollower_already_blocked(String follower_already_blocked) {
		this.follower_already_blocked = follower_already_blocked;
	}

	public String getLink_not_valid() {
		return link_not_valid;
	}

	public void setLink_not_valid(String link_not_valid) {
		this.link_not_valid = link_not_valid;
	}

	public String getUnauthorized() {
		return unauthorized;
	}

	public void setUnauthorized(String unauthorized) {
		this.unauthorized = unauthorized;
	}

	public String getWorkFlowStatus_added_asApproved_successfully() {
		return workFlowStatus_added_asApproved_successfully;
	}

	public void setWorkFlowStatus_added_asApproved_successfully(String workFlowStatus_added_asApproved_successfully) {
		this.workFlowStatus_added_asApproved_successfully = workFlowStatus_added_asApproved_successfully;
	}

	public String getRefollowers_added_successfully() {
		return refollowers_added_successfully;
	}

	public void setRefollowers_added_successfully(String refollowers_added_successfully) {
		this.refollowers_added_successfully = refollowers_added_successfully;
	}

	public String getFollower_approved() {
		return follower_approved;
	}

	public void setFollower_approved(String follower_approved) {
		this.follower_approved = follower_approved;
	}

	public String getChatuser_added_successfully() {
		return chatuser_added_successfully;
	}

	public void setChatuser_added_successfully(String chatuser_added_successfully) {
		this.chatuser_added_successfully = chatuser_added_successfully;
	}

	public String getChatuser_already_present() {
		return chatuser_already_present;
	}

	public void setChatuser_already_present(String chatuser_already_present) {
		this.chatuser_already_present = chatuser_already_present;
	}

	public String getChatuser_approved_successfully() {
		return chatuser_approved_successfully;
	}

	public void setChatuser_approved_successfully(String chatuser_approved_successfully) {
		this.chatuser_approved_successfully = chatuser_approved_successfully;
	}

	public String getChatuser_blocked_successfully() {
		return chatuser_blocked_successfully;
	}

	public void setChatuser_blocked_successfully(String chatuser_blocked_successfully) {
		this.chatuser_blocked_successfully = chatuser_blocked_successfully;
	}

	public String getUser_already_present_emailid() {
		return user_already_present_emailid;
	}

	public void setUser_already_present_emailid(String user_already_present_emailid) {
		this.user_already_present_emailid = user_already_present_emailid;
	}

	public String getUser_already_present_phone() {
		return user_already_present_phone;
	}

	public void setUser_already_present_phone(String user_already_present_phone) {
		this.user_already_present_phone = user_already_present_phone;
	}

	public String getUser_already_present_with_username() {
		return user_already_present_with_username;
	}

	public void setUser_already_present_with_username(String user_already_present_with_username) {
		this.user_already_present_with_username = user_already_present_with_username;
	}

	public String getUser_already_present_pan() {
		return user_already_present_pan;
	}

	public void setUser_already_present_pan(String user_already_present_pan) {
		this.user_already_present_pan = user_already_present_pan;
	}

	public String getUser_already_present_emailid_disabled() {
		return user_already_present_emailid_disabled;
	}

	public void setUser_already_present_emailid_disabled(String user_already_present_emailid_disabled) {
		this.user_already_present_emailid_disabled = user_already_present_emailid_disabled;
	}

	public String getUser_already_present_phone_disabled() {
		return user_already_present_phone_disabled;
	}

	public void setUser_already_present_phone_disabled(String user_already_present_phone_disabled) {
		this.user_already_present_phone_disabled = user_already_present_phone_disabled;
	}

	public String getUser_already_present_username_disabled() {
		return user_already_present_username_disabled;
	}

	public void setUser_already_present_username_disabled(String user_already_present_username_disabled) {
		this.user_already_present_username_disabled = user_already_present_username_disabled;
	}

	public String getUser_already_present_pan_disabled() {
		return user_already_present_pan_disabled;
	}

	public void setUser_already_present_pan_disabled(String user_already_present_pan_disabled) {
		this.user_already_present_pan_disabled = user_already_present_pan_disabled;
	}

	public String getCity_error() {
		return city_error;
	}

	public void setCity_error(String city_error) {
		this.city_error = city_error;
	}

	public String getChat_saved_successfully() {
		return chat_saved_successfully;
	}

	public void setChat_saved_successfully(String chat_saved_successfully) {
		this.chat_saved_successfully = chat_saved_successfully;
	}

	public String getOtp_sent_successfully() {
		return otp_sent_successfully;
	}

	public void setOtp_sent_successfully(String otp_sent_successfully) {
		this.otp_sent_successfully = otp_sent_successfully;
	}

	public String getOtp_not_sent() {
		return otp_not_sent;
	}

	public void setOtp_not_sent(String otp_not_sent) {
		this.otp_not_sent = otp_not_sent;
	}

	public String getUser_not_found() {
		return user_not_found;
	}

	public void setUser_not_found(String user_not_found) {
		this.user_not_found = user_not_found;
	}

	public String getPassword_not_found() {
		return password_not_found;
	}

	public void setPassword_not_found(String password_not_found) {
		this.password_not_found = password_not_found;
	}

	public String getChatuser_updated_successfully() {
		return chatuser_updated_successfully;
	}

	public void setChatuser_updated_successfully(String chatuser_updated_successfully) {
		this.chatuser_updated_successfully = chatuser_updated_successfully;
	}

	public String getAdvisor_revoked_successfully() {
		return advisor_revoked_successfully;
	}

	public void setAdvisor_revoked_successfully(String advisor_revoked_successfully) {
		this.advisor_revoked_successfully = advisor_revoked_successfully;
	}

	public String getRequired_reason_revoked() {
		return required_reason_revoked;
	}

	public void setRequired_reason_revoked(String required_reason_revoked) {
		this.required_reason_revoked = required_reason_revoked;
	}

	public String getFollower_unfollowed() {
		return follower_unfollowed;
	}

	public void setFollower_unfollowed(String follower_unfollowed) {
		this.follower_unfollowed = follower_unfollowed;
	}

	public String getUser_not_available() {
		return user_not_available;
	}

	public void setUser_not_available(String user_not_available) {
		this.user_not_available = user_not_available;
	}

	public String getMandatory_fields() {
		return mandatory_fields;
	}

	public void setMandatory_fields(String mandatory_fields) {
		this.mandatory_fields = mandatory_fields;
	}

	public String getMandatory_fields_parentPartyId() {
		return mandatory_fields_parentPartyId;
	}

	public void setMandatory_fields_parentPartyId(String mandatory_fields_parentPartyId) {
		this.mandatory_fields_parentPartyId = mandatory_fields_parentPartyId;
	}

	public String getMandatory_fields_userName() {
		return mandatory_fields_userName;
	}

	public void setMandatory_fields_userName(String mandatory_fields_userName) {
		this.mandatory_fields_userName = mandatory_fields_userName;
	}

	public String getMandatory_fields_signup() {
		return mandatory_fields_signup;
	}

	public void setMandatory_fields_signup(String mandatory_fields_signup) {
		this.mandatory_fields_signup = mandatory_fields_signup;
	}

	public String getMandatory_fields_advSignup() {
		return mandatory_fields_advSignup;
	}

	public void setMandatory_fields_advSignup(String mandatory_fields_advSignup) {
		this.mandatory_fields_advSignup = mandatory_fields_advSignup;
	}

	public String getMandatory_fields_role() {
		return mandatory_fields_role;
	}

	public void setMandatory_fields_role(String mandatory_fields_role) {
		this.mandatory_fields_role = mandatory_fields_role;
	}

	public String getMandatory_fields_password() {
		return mandatory_fields_password;
	}

	public void setMandatory_fields_password(String mandatory_fields_password) {
		this.mandatory_fields_password = mandatory_fields_password;
	}

	public String getMandatory_fields_emailId() {
		return mandatory_fields_emailId;
	}

	public void setMandatory_fields_emailId(String mandatory_fields_emailId) {
		this.mandatory_fields_emailId = mandatory_fields_emailId;
	}

	public String getMandatory_fields_key() {
		return mandatory_fields_key;
	}

	public void setMandatory_fields_key(String mandatory_fields_key) {
		this.mandatory_fields_key = mandatory_fields_key;
	}

	public String getMandatory_fields_keyPeopleId() {
		return mandatory_fields_keyPeopleId;
	}

	public void setMandatory_fields_keyPeopleId(String mandatory_fields_keyPeopleId) {
		this.mandatory_fields_keyPeopleId = mandatory_fields_keyPeopleId;
	}

	public String getMandatory_fields_status() {
		return mandatory_fields_status;
	}

	public void setMandatory_fields_status(String mandatory_fields_status) {
		this.mandatory_fields_status = mandatory_fields_status;
	}

	public String getMandatory_fields_phone() {
		return mandatory_fields_phone;
	}

	public void setMandatory_fields_phone(String mandatory_fields_phone) {
		this.mandatory_fields_phone = mandatory_fields_phone;
	}

	public String getMandatory_fields_otp() {
		return mandatory_fields_otp;
	}

	public void setMandatory_fields_otp(String mandatory_fields_otp) {
		this.mandatory_fields_otp = mandatory_fields_otp;
	}

	public String getMandatory_fields_followers() {
		return mandatory_fields_followers;
	}

	public void setMandatory_fields_followers(String mandatory_fields_followers) {
		this.mandatory_fields_followers = mandatory_fields_followers;
	}

	public String getMandatory_fields_block() {
		return mandatory_fields_block;
	}

	public void setMandatory_fields_block(String mandatory_fields_block) {
		this.mandatory_fields_block = mandatory_fields_block;
	}

	public String getMandatory_fields_uploadPdf() {
		return mandatory_fields_uploadPdf;
	}

	public void setMandatory_fields_uploadPdf(String mandatory_fields_uploadPdf) {
		this.mandatory_fields_uploadPdf = mandatory_fields_uploadPdf;
	}

	public String getMandatory_fields_city() {
		return mandatory_fields_city;
	}

	public void setMandatory_fields_city(String mandatory_fields_city) {
		this.mandatory_fields_city = mandatory_fields_city;
	}

	public String getMandatory_fields_validatePassword() {
		return mandatory_fields_validatePassword;
	}

	public void setMandatory_fields_validatePassword(String mandatory_fields_validatePassword) {
		this.mandatory_fields_validatePassword = mandatory_fields_validatePassword;
	}

	public String getMandatory_fields_userId() {
		return mandatory_fields_userId;
	}

	public void setMandatory_fields_userId(String mandatory_fields_userId) {
		this.mandatory_fields_userId = mandatory_fields_userId;
	}

	public String getMandatory_fields_advisor() {
		return mandatory_fields_advisor;
	}

	public void setMandatory_fields_advisor(String mandatory_fields_advisor) {
		this.mandatory_fields_advisor = mandatory_fields_advisor;
	}

	public String getMandatory_fields_explore() {
		return mandatory_fields_explore;
	}

	public void setMandatory_fields_explore(String mandatory_fields_explore) {
		this.mandatory_fields_explore = mandatory_fields_explore;
	}

	public String getMandatory_fields_keyFields() {
		return mandatory_fields_keyFields;
	}

	public void setMandatory_fields_keyFields(String mandatory_fields_keyFields) {
		this.mandatory_fields_keyFields = mandatory_fields_keyFields;
	}

	public String getMandatory_fields_file() {
		return mandatory_fields_file;
	}

	public void setMandatory_fields_file(String mandatory_fields_file) {
		this.mandatory_fields_file = mandatory_fields_file;
	}

	public String getParty_not_found() {
		return party_not_found;
	}

	public void setParty_not_found(String party_not_found) {
		this.party_not_found = party_not_found;
	}

	public String getBrandscomment_added_successfully() {
		return brandscomment_added_successfully;
	}

	public void setBrandscomment_added_successfully(String brandscomment_added_successfully) {
		this.brandscomment_added_successfully = brandscomment_added_successfully;
	}

	public String getBrandscomment_moderated_successfully() {
		return brandscomment_moderated_successfully;
	}

	public void setBrandscomment_moderated_successfully(String brandscomment_moderated_successfully) {
		this.brandscomment_moderated_successfully = brandscomment_moderated_successfully;
	}

	public String getRecord_deleted_successfully() {
		return record_deleted_successfully;
	}

	public void setRecord_deleted_successfully(String record_deleted_successfully) {
		this.record_deleted_successfully = record_deleted_successfully;
	}

	public String getMandatory_fields_bloggerId() {
		return mandatory_fields_bloggerId;
	}

	public void setMandatory_fields_bloggerId(String mandatory_fields_bloggerId) {
		this.mandatory_fields_bloggerId = mandatory_fields_bloggerId;
	}

	public String getMandatory_fields_brandId() {
		return mandatory_fields_brandId;
	}

	public void setMandatory_fields_brandId(String mandatory_fields_brandId) {
		this.mandatory_fields_brandId = mandatory_fields_brandId;
	}

	public String getBlogger_detail_empty() {
		return blogger_detail_empty;
	}

	public void setBlogger_detail_empty(String blogger_detail_empty) {
		this.blogger_detail_empty = blogger_detail_empty;
	}

	public String getMandatory_fields_comment() {
		return mandatory_fields_comment;
	}

	public void setMandatory_fields_comment(String mandatory_fields_comment) {
		this.mandatory_fields_comment = mandatory_fields_comment;
	}

	public String getMandatory_fields_commentId() {
		return mandatory_fields_commentId;
	}

	public void setMandatory_fields_commentId(String mandatory_fields_commentId) {
		this.mandatory_fields_commentId = mandatory_fields_commentId;
	}

	public String getMandatory_fields_replyComments() {
		return mandatory_fields_replyComments;
	}

	public void setMandatory_fields_replyComments(String mandatory_fields_replyComments) {
		this.mandatory_fields_replyComments = mandatory_fields_replyComments;
	}

	public String getProfile_changed_successfully() {
		return profile_changed_successfully;
	}

	public void setProfile_changed_successfully(String profile_changed_successfully) {
		this.profile_changed_successfully = profile_changed_successfully;
	}

	public String getCommentVote_added_successfully() {
		return commentVote_added_successfully;
	}

	public void setCommentVote_added_successfully(String commentVote_added_successfully) {
		this.commentVote_added_successfully = commentVote_added_successfully;
	}

	public String getMandatory_fields_voteRemoveComment() {
		return mandatory_fields_voteRemoveComment;
	}

	public void setMandatory_fields_voteRemoveComment(String mandatory_fields_voteRemoveComment) {
		this.mandatory_fields_voteRemoveComment = mandatory_fields_voteRemoveComment;
	}

	public String getCommentvote_removed_successfully() {
		return commentvote_removed_successfully;
	}

	public void setCommentvote_removed_successfully(String commentvote_removed_successfully) {
		this.commentvote_removed_successfully = commentvote_removed_successfully;
	}

	public String getMandatory_fields_fetchVoteAddress() {
		return mandatory_fields_fetchVoteAddress;
	}

	public void setMandatory_fields_fetchVoteAddress(String mandatory_fields_fetchVoteAddress) {
		this.mandatory_fields_fetchVoteAddress = mandatory_fields_fetchVoteAddress;
	}

        
	
}
