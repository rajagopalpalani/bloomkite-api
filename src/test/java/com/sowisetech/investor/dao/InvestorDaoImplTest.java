package com.sowisetech.investor.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.sowisetech.advisor.util.AdvTableFields;
import com.sowisetech.investor.model.Category;
import com.sowisetech.investor.model.InvInterest;
import com.sowisetech.investor.model.Investor;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class InvestorDaoImplTest {

	InvestorDaoImpl investorDaoImpl;

	EmbeddedDatabase db;

	@Before
	public void setup() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("db_sql/invschema.sql").addScript("db_sql/invdata.sql").build();
		investorDaoImpl = new InvestorDaoImpl();
		investorDaoImpl.setDataSource(db);
		investorDaoImpl.postConstruct();
	}

	// Fetch Investor List Test//
	// @Test //Encode decode
	// public void testfetchInvestor() {
	// String encryptPass ="Sowise@Ever21";
	// List<Investor> investors = investorDaoImpl.fetchInvestor(1, 1,
	// "N",encryptPass);
	// Assert.assertEquals(1, investors.size());
	// }

	// Fetch Investor List Negative Test//
	// @Test //Encode decode
	// public void testfetchInvestorNull() {
	// String encryptPass ="Sowise@Ever21";
	// List<Investor> investors = investorDaoImpl.fetchInvestor(1, 1,
	// "Y",encryptPass);
	// Assert.assertEquals(0, investors.size());
	// }

	// Fetch InvInterest List Test//
	@Test
	public void testFetchInvInterestByInvId() {
		List<InvInterest> invInterest = investorDaoImpl.fetchInvInterestByInvId("INV000000000A", "N");
		Assert.assertEquals(1, invInterest.size());
	}

	// Fetch InvInterest List Negative Test//
	@Test
	public void testFetchInvInterestByInvIdNull() {
		List<InvInterest> invInterest = investorDaoImpl.fetchInvInterestByInvId("INV000000000C", "N");
		Assert.assertEquals(0, invInterest.size());
	}

	// Delete Investor Negative Test //
	@Test
	public void testdeleteInvestorError() {
		int result = investorDaoImpl.deleteInvestor("INV000000000C", "Y", "INV000000000A");
		Assert.assertEquals(0, result);
	}

	// Delete Investor Interest Test //
	@Test
	public void testdeleteInvestorInterestByInvId() {
		int result = investorDaoImpl.deleteInvestorInterestByInvId("INV000000000A", "Y");
		Assert.assertEquals(1, result);
	}

	// Delete Investor Interest Negative Test //
	@Test
	public void testdeleteInvestorInterestByInvIdError() {
		long roleId = 2;
		int result = investorDaoImpl.deleteInvestorInterestByInvId("INV000000000C", "Y");
		Assert.assertEquals(0, result);
	}

	// Delete Party Negative Test //
	@Test
	public void testdeletePartyByInvIdError() {
		long roleId = 2;
		int result = investorDaoImpl.deletePartyByInvId("INV000000000C", "Y");
		Assert.assertEquals(0, result);
	}

	// @Test //Encode decode
	// public void testfetchInvestorByInvId() {
	// String encryptPass ="Sowise@Ever21";
	// Investor result = investorDaoImpl.fetchInvestorByInvId("INV000000000A",
	// "N",encryptPass);
	// Assert.assertEquals("investor", result.getFullName());
	//
	// }

	// fetchInvestor by InvId Negative Test //
	// @Test //Encode decode
	// public void testfetchInvestorByInvIdNull() {
	// String encryptPass ="Sowise@Ever21";
	// Investor result = investorDaoImpl.fetchInvestorByInvId("aaa",
	// "Y",encryptPass);
	// Assert.assertEquals(null, result.getInvId());
	// }

	// Modify Investor Test //
	// @Test //Encode decode
	// public void testInvUpdate() {
	// String encryptPass ="Sowise@Ever21";
	// Investor investor = new Investor();
	// investor.setFullName("advisor_Manager");
	// investor.setDisplayName("adv_manager");
	// investor.setDob("1996-08-11");
	// investor.setGender("m");
	// investor.setEmailId("investor@gmail.com");
	// investor.setPhoneNumber("9876540321");
	// investor.setPassword("!@AS12qw");
	// investor.setPincode("123456");
	// investor.setPartyStatusId(3);
	// int result = investorDaoImpl.update("INV000000000A", investor,encryptPass);
	// Assert.assertEquals(1, result);
	// }

	// Modify Investor Negative Test //
	@Test
	public void testInvUpdateError() {
		String encryptPass = "Sowise@Ever21";
		Investor investor = new Investor();
		// investor.setFullName("advisor_Manager");
		// investor.setDisplayName("adv_manager");
		int result = investorDaoImpl.update("INV000000000C", investor, encryptPass);
		Assert.assertEquals(0, result);
	}

	// Fetch Category Test //
	@Test
	public void testFetchCategoryById() {
		Category categ = investorDaoImpl.fetchCategoryById(1);
		Assert.assertEquals(1, categ.getCategoryId());
	}

	// Fetch Category Negative Test //
	@Test
	public void testFetchCategoryByIdNull() {
		Category categ = investorDaoImpl.fetchCategoryById(7);
		Assert.assertEquals(null, categ);
	}

	// Find Duplicate Test //
	@Test
	public void testFindDuplicate() {
		boolean invInterest = investorDaoImpl.findDuplicate("INV000000000A", 1);
		Assert.assertEquals(invInterest, true);
	}

	// Find Duplicate Negative Test //
	@Test
	public void testFindDuplicateError() {
		boolean invInterest = investorDaoImpl.findDuplicate("INV000000000B", 1);
		Assert.assertEquals(invInterest, false);
	}

	// Add InvInterest Test//
	@Test
	public void testAddInvestorInterest() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		InvInterest invInterest = new InvInterest();
		invInterest.setInvId("INV000000000A");
		invInterest.setProdId(2);
		invInterest.setScale("2");
		invInterest.setCreated(timestamp);
		invInterest.setUpdated(timestamp);
		invInterest.setCreated_by("INV000000000A");
		invInterest.setUpdated_by("INV000000000A");

		int result = investorDaoImpl.addInvestorInterest("INV000000000A", invInterest, "N");
		Assert.assertEquals(1, result);
	}

	// Add InvInterest Negative Test//
	@Test
	public void testAddInvestorInterestError() {
		InvInterest invInterest = new InvInterest();
		int result = investorDaoImpl.addInvestorInterest("INV000000000C", invInterest, "N");
		Assert.assertEquals(0, result);
	}

	// Modify InvInterest Test //
	@Test
	public void testUpdateInvestorInterest() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		InvInterest invInterest = new InvInterest();
		invInterest.setScale("3");
		invInterest.setInvId("INV000000000A");
		invInterest.setProdId(2);
		invInterest.setUpdated(timestamp);
		invInterest.setUpdated_by("INV000000000A");
		int result = investorDaoImpl.updateInvestorInterest(1, invInterest);
		Assert.assertEquals(1, result);
	}

	// Modify InvInterest Negative Test //
	@Test
	public void testUpdateInvestorInterestError() {
		InvInterest invInterest = new InvInterest();
		invInterest.setScale("3");
		invInterest.setInvId("INV000000000B");
		invInterest.setProdId(2);
		int result = investorDaoImpl.updateInvestorInterest(7, invInterest);
		Assert.assertEquals(0, result);
	}

	// Fetch InvInterest Test //
	@Test
	public void testfetchInvInterest() {
		InvInterest result = investorDaoImpl.fetchInvInterest(1, "N");
		Assert.assertEquals("1", result.getScale());
	}

	// Fetch InvInterest Negative Test //
	@Test
	public void testfetchInvInterestNull() {
		InvInterest result = investorDaoImpl.fetchInvInterest(4, "N");
		Assert.assertEquals(null, result);
	}

	// Delete InvInterest Test //
	@Test
	public void testdeleteInvestorInterest() {
		int result = investorDaoImpl.deleteInvestorInterest(1, "N", "INV000000000A");
		Assert.assertEquals(1, result);
	}

	// Delete InvInterest Negative Test //
	@Test
	public void testdeleteInvestorInterestError() {
		int result = investorDaoImpl.deleteInvestorInterest(4, "N", "INV000000000A");
		Assert.assertEquals(0, result);
	}

	// Encrypt and Decrypt Test //
	@Test
	public void testEncryptAndDecrypt() {
		String encryptedText = investorDaoImpl.encrypt("!@AS12as");
		String plainText = investorDaoImpl.decrypt(encryptedText);
		Assert.assertEquals("!@AS12as", plainText);
	}

	@Test
	public void test_checkInvestorIsPresent() {
		int result = investorDaoImpl.checkInvestorIsPresent("INV000000000A", "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_checkInvestorIsPresent_Error() {
		int result = investorDaoImpl.checkInvestorIsPresent("INV000000000A", "Y");
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_CheckCategoryIsPresent() {
		int result = investorDaoImpl.CheckCategoryIsPresent(1);
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_CheckCategoryIsPresent_Error() {
		int result = investorDaoImpl.CheckCategoryIsPresent(100);
		Assert.assertEquals(0, result);
	}

	@Test
	public void test_CheckInvInterestIsPresent() {
		int result = investorDaoImpl.CheckInvInterestIsPresent(1L, "N");
		Assert.assertEquals(1, result);
	}

	@Test
	public void test_CheckInvInterestIsPresent_Error() {
		int result = investorDaoImpl.CheckInvInterestIsPresent(100L, "Y");
		Assert.assertEquals(0, result);
	}
}
