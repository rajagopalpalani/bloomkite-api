package com.sowisetech.investor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;

public class InvestorListExtractor implements ResultSetExtractor<List<Investor>>{

	String deleteflag;

	public InvestorListExtractor(String deleteflag1) {
		deleteflag = deleteflag1;
	}

	@Override
	public List<Investor> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, Investor> map = new HashMap<String, Investor>();
		while (rs.next()) {
			String invId = rs.getString("invId");
			Investor investor = map.get(invId);
			if (investor == null) {
				investor = new Investor();
				investor.setInvId(invId);
				investor.setFullName(rs.getString("fullname"));
				investor.setDisplayName(rs.getString("displayName"));
				investor.setDob(rs.getString("dob"));
				investor.setEmailId(rs.getString("emailId"));
				investor.setGender(rs.getString("gender"));
				investor.setPassword(rs.getString("password"));
				investor.setPhoneNumber(rs.getString("phoneNumber"));
				investor.setPincode(rs.getString("pincode"));
				investor.setImagePath(rs.getString("imagePath"));
				investor.setPartyStatusId(rs.getLong("partyStatusId"));
				investor.setCreated(rs.getTimestamp("created"));
				investor.setUpdated(rs.getTimestamp("updated"));
				investor.setDelete_flag(rs.getString("delete_flag"));
				investor.setIsVerified(rs.getInt("isVerified"));
				investor.setVerifiedBy(rs.getString("verifiedBy"));
				investor.setVerified(rs.getTimestamp("verified"));
				investor.setIsMobileVerified(rs.getInt("isMobileVerified"));
				map.put(invId, investor);
			}
			String type = rs.getString("VALUE");
			String delete_flag = rs.getString("COL_DELETEFLAG");
			if (type.equals("invInt") && delete_flag != null && delete_flag.equals(deleteflag)) {
				List<InvInterest> invInterestList = investor.getInvInterest();
				if (invInterestList == null) {
					invInterestList = new ArrayList<InvInterest>();
					investor.setInvInterest(invInterestList);
				}
				InvInterest invInt = new InvInterest();
				invInt.setInterestId(rs.getLong("COL_A"));
				invInt.setProdId(rs.getLong("COL_B"));
				invInt.setInvId(rs.getString("COL_INVID"));
				invInt.setScale(rs.getString("COL_D"));
				invInt.setCreated(rs.getTimestamp("COL_E"));
				invInt.setUpdated(rs.getTimestamp("COL_F"));
				invInt.setDelete_flag(rs.getString("COL_DELETEFLAG"));
				invInterestList.add(invInt);
			}
				
		}
		return new ArrayList<Investor>(map.values());
	}

}
