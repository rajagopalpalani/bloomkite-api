package com.sowisetech.investor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;

@Service
public interface InvestorService {

	Investor fetchByInvestorId(String invId);

	List<Investor> fetchInvestorList(int pageNum, int records);

//	int addInvestor(Investor investor);

	int modifyInvestor(String invId, Investor investor);

	int removeInvestor(String invId);

	// //Investor Interest

	int addInvestorInterest(String invId, InvInterest invInterest);

	int modifyInvestorInterest(long interestId, InvInterest invInterest);

	InvInterest fetchByInvInterestById(long interestId);

	boolean findDuplicate(String invId, long categoryId);

	Category fetchCategoryById(long categoryId);

	int removeInvestorInterest(long interestId);

//	Investor fetchInvByEmailId(String emailId);

//	String generateId();

	String encrypt(String rawPassword);

	String decrypt(String encodedPassword);

	int fetchTotalInvestorList();

	int addAndModifyInvestorInterest(String invId, List<InvInterest> invInterestList);

	int CheckInvestorIsPresent(String invId);

	int CheckCategoryIsPresent(long categoryId);

	int CheckInvInterestIsPresent(long interestId);

}
