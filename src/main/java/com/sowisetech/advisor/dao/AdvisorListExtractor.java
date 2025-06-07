package com.sowisetech.advisor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.advisor.model.AdvBrandInfo;
import com.sowisetech.advisor.model.AdvBrandRank;
import com.sowisetech.advisor.model.AdvProduct;
import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.Award;
import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.model.Education;
import com.sowisetech.advisor.model.Experience;
import com.sowisetech.advisor.model.Promotion;

public class AdvisorListExtractor implements ResultSetExtractor<List<Advisor>> {

	String deleteflag;

	public AdvisorListExtractor(String delete_flag) {
		deleteflag = delete_flag;
	}

	@Override
	public List<Advisor> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Advisor> map = new LinkedHashMap<String, Advisor>();
		while (rs.next()) {
			String advId = rs.getString("advId");
			Advisor advisor = map.get(advId);
			if (advisor == null) {
				advisor = new Advisor();
				advisor.setAdvId(advId);
				advisor.setName(rs.getString("name"));
				advisor.setDesignation(rs.getString("designation"));
				advisor.setEmailId(rs.getString("emailId"));
				advisor.setPassword(rs.getString("password"));
				advisor.setPhoneNumber(rs.getString("phoneNumber"));
				advisor.setDelete_flag(rs.getString("delete_flag"));
				advisor.setGender(rs.getNString("gender"));
				advisor.setPartyStatusId(rs.getInt("partyStatusId"));
				advisor.setAdvType(rs.getInt("advType"));
				advisor.setDisplayName(rs.getString("displayName"));
				advisor.setDob(rs.getString("dob"));
				advisor.setPanNumber(rs.getString("panNumber"));
				advisor.setAddress1(rs.getString("address1"));
				advisor.setAddress2(rs.getString("address2"));
				advisor.setState(rs.getString("state"));
				advisor.setCity(rs.getString("city"));
				advisor.setPincode(rs.getString("pincode"));
				advisor.setAboutme(rs.getString("aboutme"));
				advisor.setCreated(rs.getTimestamp("created"));
				advisor.setUpdated(rs.getTimestamp("updated"));
				advisor.setCreated_by(rs.getString("created_by"));
				advisor.setUpdated_by(rs.getString("updated_by"));
				advisor.setFirmType(rs.getString("firmType"));
				advisor.setCorporateLable(rs.getString("corporateLable"));
				advisor.setIsVerified(rs.getInt("isVerified"));
				advisor.setVerified(rs.getTimestamp("verified"));
				advisor.setIsMobileVerified(rs.getInt("isMobileVerified"));
				advisor.setWebsite(rs.getString("website"));
				advisor.setParentPartyId(rs.getLong("parentPartyId"));
				advisor.setImagePath(rs.getString("imagePath"));
				advisor.setUserName(rs.getString("userName"));
				advisor.setWorkFlowStatus(rs.getInt("workFlowStatus"));
				advisor.setApprovedDate(rs.getTimestamp("approvedDate"));
				advisor.setApprovedBy(rs.getString("approvedBy"));
				advisor.setRevokedDate(rs.getTimestamp("revokedDate"));
				advisor.setRevokedBy(rs.getString("revokedBy"));
// 			    advisor.setPromoCount(rs.getInt("promoCount"));

				map.put(advId, advisor);
			}
			String type = rs.getString("VALUE");
			String delete_flag = rs.getString("COL_DELETEFLAG");
			if (type.equals("awd") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<Award> awardList = advisor.getAwards();
				if (awardList == null) {
					awardList = new ArrayList<Award>();
					advisor.setAwards(awardList);
				}
				Award awd = new Award();
				awd.setAdvId(rs.getString("COL_ADVID"));
				awd.setAwardId(rs.getLong("COL_A"));
				awd.setTitle(rs.getString("COL_D"));
				awd.setYear(rs.getString("COL_E"));
				awd.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				awd.setImagePath(rs.getString("COL_B"));
				awd.setIssuedBy(rs.getString("COL_C"));

				awardList.add(awd);
			}
			// String type = rs.getString("VALUE");
			if (type.equals("cert") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<Certificate> certificateList = advisor.getCertificates();
				if (certificateList == null) {
					certificateList = new ArrayList<Certificate>();
					advisor.setCertificates(certificateList);
				}
				Certificate cert = new Certificate();
				cert.setCertificateId(rs.getLong("COL_A"));
				cert.setImagePath(rs.getString("COL_B"));
				cert.setIssuedBy(rs.getString("COL_C"));
				cert.setTitle(rs.getString("COL_D"));
				cert.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				cert.setYear(rs.getString("COL_E"));
				cert.setAdvId(rs.getString("COL_ADVID"));
				certificateList.add(cert);
			}
			if (type.equals("edu") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<Education> educationList = advisor.getEducations();
				if (educationList == null) {
					educationList = new ArrayList<Education>();
					advisor.setEducations(educationList);
				}
				Education edu = new Education();
				edu.setEduId(rs.getLong("COL_A"));
				edu.setDegree(rs.getString("COL_B"));
				edu.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				edu.setAdvId(rs.getString("COL_ADVID"));
				edu.setField(rs.getString("COL_C"));
				edu.setFromYear(rs.getString("COL_E"));
				edu.setInstitution(rs.getString("COL_D"));
				edu.setToYear(rs.getString("COL_F"));

				educationList.add(edu);
			}
			if (type.equals("exp") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<Experience> experienceList = advisor.getExperiences();
				if (experienceList == null) {
					experienceList = new ArrayList<Experience>();
					advisor.setExperiences(experienceList);
				}
				Experience exp = new Experience();
				exp.setAdvId(rs.getString("COL_ADVID"));
				exp.setExpId(rs.getLong("COL_A"));
				exp.setCompany(rs.getString("COL_B"));
				exp.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				exp.setDesignation(rs.getString("COL_C"));
				exp.setFromYear(rs.getString("COL_E"));
				exp.setLocation(rs.getString("COL_D"));
				exp.setToYear(rs.getString("COL_F"));
				experienceList.add(exp);
			}
			if (type.equals("prod") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<AdvProduct> advProductList = advisor.getAdvProducts();
				if (advProductList == null) {
					advProductList = new ArrayList<AdvProduct>();
					advisor.setAdvProducts(advProductList);
				}
				AdvProduct prod = new AdvProduct();
				prod.setAdvId(rs.getString("COL_ADVID"));
				prod.setAdvProdId(rs.getLong("COL_A"));
				prod.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				prod.setLicId(rs.getLong("COL_F"));
				prod.setLicImage(rs.getString("COL_B"));
				prod.setLicNumber(rs.getString("COL_I"));
				prod.setProdId(rs.getLong("COL_C"));
				prod.setRemId(rs.getLong("COL_E"));
				prod.setServiceId(rs.getString("COL_D"));
				prod.setValidity(rs.getString("COL_J"));
				prod.setCreated(rs.getTimestamp("COL_K"));
				prod.setUpdated(rs.getTimestamp("COL_L"));
				prod.setCreated_by(rs.getString("COL_M"));
				prod.setUpdated_by(rs.getString("COL_N"));

				advProductList.add(prod);
			}
			if (type.equals("promo") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<Promotion> promotionList = advisor.getPromotions();
				if (promotionList == null) {
					promotionList = new ArrayList<Promotion>();
					advisor.setPromotions(promotionList);
				}
				Promotion promotion = new Promotion();
				promotion.setAdvId(rs.getString("COL_ADVID"));
				promotion.setPromotionId(rs.getLong("COL_A"));
				;
				promotion.setTitle(rs.getString("COL_B"));
				promotion.setVideo(rs.getString("COL_C"));
				promotion.setDeleteflag(rs.getString("COL_DELETEFLAG"));
				promotion.setAboutVideo(rs.getString("COL_E"));
				promotion.setImagePath(rs.getString("COL_D"));
				promotionList.add(promotion);
			}
			if (type.equals("brandinfo") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<AdvBrandInfo> advBrandInfoList = advisor.getAdvBrandInfo();
				if (advBrandInfoList == null) {
					advBrandInfoList = new ArrayList<AdvBrandInfo>();
					advisor.setAdvBrandInfo(advBrandInfoList);
				}
				AdvBrandInfo brandinfo = new AdvBrandInfo();
				brandinfo.setAdvBrandId(rs.getLong("COL_A"));
				brandinfo.setBrandId(rs.getLong("COL_D"));
				brandinfo.setAdvId(rs.getString("COL_ADVID"));
				brandinfo.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				brandinfo.setPriority(rs.getLong("COL_E"));
				brandinfo.setProdId(rs.getLong("COL_B"));
				brandinfo.setServiceId(rs.getString("COL_C"));
				advBrandInfoList.add(brandinfo);
			}
			if (type.equals("brandrank") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<AdvBrandRank> advBrandRankList = advisor.getAdvBrandRank();
				if (advBrandRankList == null) {
					advBrandRankList = new ArrayList<AdvBrandRank>();
					advisor.setAdvBrandRank(advBrandRankList);
				}
				AdvBrandRank brandrank = new AdvBrandRank();
				brandrank.setBrandId(rs.getLong("COL_C"));
				brandrank.setAdvId(rs.getString("COL_ADVID"));
				brandrank.setAdvBrandRankId(rs.getLong("COL_A"));
				// brandrank.setBrand(rs.getString("COL_G"));
				brandrank.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				brandrank.setProdId(rs.getLong("COL_B"));
				brandrank.setRanking(rs.getLong("COL_D"));
				advBrandRankList.add(brandrank);
			}
		}
		return new ArrayList<Advisor>(map.values());
	}

}
