package com.sowisetech.investor.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sowisetech.calc.model.Party;
import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;

public interface InvestorDao {
	// int add(Investor investor, String deleteflag);

	// Investor fetch(String invId);

	// List<Investor> fetchAll();

	int update(String invId, Investor investor, String encryptPass);

	// void delete(String invId);

	// Investor Intrest

	int addInvestorInterest(String invId, InvInterest invinterest, String deleteflag);

	int updateInvestorInterest(long interestId, InvInterest invInterest);

	boolean findDuplicate(String invId, long categoryId);

	Category fetchCategoryById(long categoryId);

	InvInterest fetchInvInterest(long interestId, String deleteflag);

	int deleteInvestorInterest(long interestId, String deleteflag, String signedUserId);

	// Investor fetchInvByEmailId(String emailId);

	// String generateId();

	String encrypt(String rawPassword);

	String decrypt(String encodedPassword);

	// long fetchPartyStatusIdByDesc(String desc);

	// long fetchRoleIdByName(String name);

	// int addParty(Investor investor, Long roleId, String deleteflag);

	int deleteInvestor(String invId, String deleteflag, String signedUserId);

	int deleteInvestorInterestByInvId(String invId, String deleteflag);

	int deletePartyByInvId(String invId, String deleteflag);

	Investor fetchInvestorByInvId(String invId, String deleteflag, String encryptPass);

	List<Investor> fetchInvestor(Pageable pageable, String deleteflag, String encryptPass);

	List<InvInterest> fetchInvInterestByInvId(String invId, String deleteflag);

	long fetchPartyIdByRoleBasedId(String invId, String deleteflag);

//	int updatePersonalInfoInParty(String emailId, String phoneNumber, String invId,String encryptPass);
	
	int updatePersonalInfoInParty(Investor investor, String invId,String encryptPass);

	Party fetchPartyForSignIn(String username, String delete_flag, String encryptPass);

	int fetchTotalInvestorList(String deleteflag);

	int checkInvestorIsPresent(String invId,String deleteflag);

	int CheckCategoryIsPresent(long categoryId);

	int CheckInvInterestIsPresent(long interestId, String deleteflag);

	// String fetchInvestorSmartId();

	// int addInvSmartId(String newId);

}
