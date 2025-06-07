//package com.sowisetech.admin.dao;
//
//import java.sql.Timestamp;
//import java.util.List;
//
//import javax.transaction.Transactional;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Spy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.DirtiesContext.ClassMode;
//import com.sowisetech.admin.model.Account;
//import com.sowisetech.admin.model.Acctype;
//import com.sowisetech.admin.model.Admin;
//import com.sowisetech.admin.model.Remuneration;
//import com.sowisetech.admin.model.Service;
//import com.sowisetech.admin.model.State;
//import com.sowisetech.admin.model.Votetype;
//import com.sowisetech.admin.model.UserType;
//import com.sowisetech.admin.model.Workflowstatus;
//import com.sowisetech.admin.model.AdmFollower;
//import com.sowisetech.admin.model.AdmPriority;
//import com.sowisetech.admin.model.AdmRiskPortfolio;
//import com.sowisetech.admin.model.Admin;
//import com.sowisetech.admin.model.Advtypes;
//import com.sowisetech.admin.model.License;
//import com.sowisetech.admin.model.ArticleStatus;
//import com.sowisetech.admin.model.Brand;
//import com.sowisetech.admin.model.CashFlowItem;
//import com.sowisetech.admin.model.CashFlowItemType;
//import com.sowisetech.admin.model.City;
//import com.sowisetech.admin.model.InsuranceItem;
//import com.sowisetech.admin.model.Product;
//import com.sowisetech.admin.model.Urgency;
//import com.sowisetech.admin.util.AdmTableFields;
//
//@Transactional
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//public class AdminDaoImplTest {
//
//	AdminDaoImpl adminDaoImpl;
//
//	EmbeddedDatabase db;
//
//	@Autowired(required = true)
//	@Spy
//	AdmTableFields admTableFields;
//
//	@Before
//	public void setup() {
//		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//				.addScript("db_sql/admschema.sql").addScript("db_sql/admdata.sql").build();
//		adminDaoImpl = new AdminDaoImpl();
//		adminDaoImpl.setDataSource(db);
//		adminDaoImpl.postConstruct();
//	}
//
//	// @Test // Encode Decode
//	// public void testfetchAdminList() {
//	// String delete_flag = "N";
//	// String encryptPass = admTableFields.getEncryption_password();
//	// List<Admin> adminList = adminDaoImpl.fetchAdminList(delete_flag,
//	// encryptPass);
//	// Assert.assertEquals(1, adminList.size());
//	// }
//	//
//	// @Test // Encode Decode
//	// public void testfetchAdminListNegative() {
//	// String delete_flag = "Y";
//	// String encryptPass = "asdf";
//	// List<Admin> adminList = adminDaoImpl.fetchAdminList(delete_flag,
//	// encryptPass);
//	// Assert.assertEquals(0, adminList.size());
//	// }
//	//
//	// @Test // Encode Decode
//	// public void testAdminFetchById() {
//	// String delete_flag = "N";
//	// String encryptPass = admTableFields.getEncryption_password();
//	// Admin admin = adminDaoImpl.fetchByAdminId("ADM000000000B", delete_flag,
//	// encryptPass);
//	// Assert.assertEquals("admin two", admin.getName());
//	// }
//	//
//	// @Test // Encode Decode
//	// public void testAdminFetchByIdNegative() {
//	// String delete_flag = "N";
//	// String encryptPass = admTableFields.getEncryption_password();
//	// Admin admin = adminDaoImpl.fetchByAdminId("ADM000000000D", delete_flag,
//	// encryptPass);
//	// Assert.assertEquals(null, admin);
//	// }
//	//
//	// @Test // Encode Decode
//	// public void testFetchAdminByEmailId() {
//	// String encryptPass = admTableFields.getEncryption_password();
//	// Admin admin = adminDaoImpl.fetchAdminByEmailId("admintwo@admin.com",
//	// encryptPass);
//	// Assert.assertEquals("admin two", admin.getName());
//	// }
//	//
//	// @Test
//	// public void testFetchAdminByEmailIdNegative() {
//	// String encryptPass = admTableFields.getEncryption_password();
//	// Admin admin = adminDaoImpl.fetchAdminByEmailId("123@admin.com", encryptPass);
//	// Assert.assertEquals(null, admin);
//	// }
//	//
//	// @Test// Encode Decode
//	// public void testAddAdmin() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// Admin admin = new Admin();
//	// admin.setAdminId("ADM0000000001");
//	// admin.setName("admin one");
//	// admin.setEmailId("admin@admin.com");
//	// admin.setPassword("!@AS12as");
//	// admin.setPartyStatusId(1);
//	// admin.setCreated(timestamp);
//	// admin.setUpdated(timestamp);
//	// admin.setDelete_flag("N");
//	// admin.setCreated_by("ADM000000000B");
//	// admin.setUpdated_by("ADM000000000B");
//	// adminDaoImpl.addAdmin(admin, "!@12ASas");
//	// }
//	//
//	// @Test// Encode Decode
//	// public void testAddAdminNegative() {
//	// Admin admin = new Admin();
//	// String encryptPass = admTableFields.getEncryption_password();
//	// int result = adminDaoImpl.addAdmin(admin, encryptPass);
//	// Assert.assertEquals(0, result);
//	// }
//	//
//	// @Test// Encode Decode
//	// public void testPartyInsert() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// Admin admin = new Admin();
//	// admin.setAdminId("ADM000000000C");
//	// admin.setPartyStatusId(1);
//	// admin.setEmailId("admin@gmail.com");
//	// admin.setCreated(timestamp);
//	// admin.setUpdated(timestamp);
//	// admin.setDelete_flag("N");
//	// admin.setCreated_by("ADM000000000C");
//	// admin.setUpdated_by("ADM000000000C");
//	// String encryptPass ="Sowise@123";
//	// int result = adminDaoImpl.addPartyForAdmin(admin, encryptPass);
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// @Test// Encode Decode
//	// public void testPartyInsertNegative() {
//	// Admin admin = new Admin();
//	// String encryptPass = admTableFields.getEncryption_password();
//	// int result = adminDaoImpl.addPartyForAdmin(admin, encryptPass);
//	// Assert.assertEquals(0, result);
//	// }
//
//	// @Test // Encode Decode
//	// public void testAdminModify() {
//	// Admin admin = new Admin();
//	// admin.setAdminId("ADM000000000B");
//	// admin.setName("Second Admin");
//	// String encryptPass = admTableFields.getEncryption_password();
//	// int result = adminDaoImpl.modifyAdmin("ADM000000000B", admin, encryptPass);
//	// Admin adm = adminDaoImpl.fetchByAdminId("ADM000000000B", "N", encryptPass);
//	// Assert.assertEquals("Second Admin", adm.getName());
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// @Test // Encode Decode
//	// public void testAdminModifyNegative() {
//	// Admin admin = new Admin();
//	// String encryptPass = admTableFields.getEncryption_password();
//	// int result = adminDaoImpl.modifyAdmin("ADM000000000D", admin, encryptPass);
//	// Assert.assertEquals(0, result);
//	// }
//
//	@Test
//	public void testAdminRemove() {
//		int result = adminDaoImpl.removeAdmin("ADM000000000B", "Y", "ADM000000000B");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testAdminRemoveNegative() {
//		int result = adminDaoImpl.removeAdmin("ADM000000000D", "Y", "ADM000000000B");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemoveParty() {
//		int result = adminDaoImpl.removeParty("ADM000000000B", "Y", "ADM000000000B");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemovePartyNegative() {
//		int result = adminDaoImpl.removeParty("ADM000000000D", "Y", "ADM000000000B");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testFetchAdminSmartId() {
//		String id = adminDaoImpl.fetchAdminSmartId();
//		Assert.assertEquals("ADM000000000B", id);
//
//	}
//
//	@Test
//	public void testaddAdvisorTypes() {
//		Advtypes advtypes = new Advtypes();
//		advtypes.setAdvtype("investor");
//		int result = adminDaoImpl.advisorTypes(advtypes);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyAdvisorTypes() {
//		Advtypes advtypes = new Advtypes();
//		advtypes.setAdvtype("investor");
//		advtypes.setId(1);
//		int result = adminDaoImpl.modifyAdvisorTypes(advtypes);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyAdvisorTypesNagative() {
//		Advtypes advtypes = new Advtypes();
//		advtypes.setAdvtype("investor");
//		advtypes.setId(10);
//		int result = adminDaoImpl.modifyAdvisorTypes(advtypes);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testremoveAdvisorTypes() {
//		Advtypes advtypes = new Advtypes();
//		// advtypes.setAdvtype("investor");
//		advtypes.setId(8);
//		int result = adminDaoImpl.removeAdvisorTypes(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testremoveAdvisorTypesNagetive() {
//		Advtypes advtypes = new Advtypes();
//		// advtypes.setAdvtype("investor");
//		advtypes.setId(10);
//		int result = adminDaoImpl.removeAdvisorTypes(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testaddFollowerStatus() {
//		AdmFollower admFollower = new AdmFollower();
//		admFollower.setStatus("follow");
//		int result = adminDaoImpl.followerStatus(admFollower);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyFollowerStatus() {
//		AdmFollower admFollower = new AdmFollower();
//		admFollower.setStatus("follow");
//		admFollower.setFollowerStatusId(1);
//		int result = adminDaoImpl.modifyFollowerStatus(admFollower);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyFollowerStatusNegative() {
//		AdmFollower admFollower = new AdmFollower();
//		admFollower.setStatus("follow");
//		admFollower.setFollowerStatusId(4);
//		int result = adminDaoImpl.modifyFollowerStatus(admFollower);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testremoveFollowerStatus() {
//		AdmFollower admFollower = new AdmFollower();
//		admFollower.setFollowerStatusId(9);
//		int result = adminDaoImpl.removeFollowerStatus(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testremoveFollowerStatusNegative() {
//		AdmFollower admFollower = new AdmFollower();
//		admFollower.setFollowerStatusId(4);
//		int result = adminDaoImpl.removeFollowerStatus(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testaddPriorityItem() {
//		AdmPriority admPriority = new AdmPriority();
//		admPriority.setPriorityItem("business");
//		int result = adminDaoImpl.priorityItem(admPriority);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyPriorityItem() {
//		AdmPriority admPriority = new AdmPriority();
//		admPriority.setPriorityItem("business");
//		admPriority.setPriorityItemId(1);
//		int result = adminDaoImpl.modifyPriorityItem(admPriority);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyPriorityItemNegative() {
//		AdmPriority admPriority = new AdmPriority();
//		admPriority.setPriorityItem("business");
//		admPriority.setPriorityItemId(9);
//		int result = adminDaoImpl.modifyPriorityItem(admPriority);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testremovePriorityItem() {
//		AdmPriority admPriority = new AdmPriority();
//		admPriority.setPriorityItemId(1);
//		int result = adminDaoImpl.removePriorityItem(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testremovePriorityItemNegative() {
//		AdmPriority admPriority = new AdmPriority();
//		// admPriority.setPriorityItem("business");
//		admPriority.setPriorityItemId(9);
//		int result = adminDaoImpl.removePriorityItem(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testaddUrgency() {
//		Urgency urgency = new Urgency();
//		urgency.setValue("business");
//		int result = adminDaoImpl.urgency(urgency);
//		Assert.assertEquals(1, result);
//	}
//	@Test
//	public void testmodifyUrgency() {
//		Urgency urgency = new Urgency();
//		urgency.setValue("business");
//		urgency.setUrgencyId(1);
//		int result  = adminDaoImpl.modifyUrgency(urgency);
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void testmodifyUrgencyNegative() {
//		Urgency urgency = new Urgency();
//		urgency.setValue("business");
//		urgency.setUrgencyId(3);
//		int result  = adminDaoImpl.modifyUrgency(urgency);
//		Assert.assertEquals(0, result);
//	}
//	@Test
//	public void testremoveUrgency() {
//		Urgency urgency = new Urgency();
//		urgency.setUrgencyId(2);
//		int result = adminDaoImpl.removeUrgency(1);
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void testremoveUrgencyNegative() {
//		Urgency urgency = new Urgency();
//		urgency.setUrgencyId(3);
//		int result = adminDaoImpl.removeUrgency(0);
//		Assert.assertEquals(0, result);
//	}
//	
//	@Test
//	public void testaddAccount() {
//		Account account	= new Account();
//		account.setAccountEntry("bike");
//		account.setAccountTypeId(1);
//		int result = adminDaoImpl.account(account);
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void testmodifyAccount() {
//		Account account	= new Account();
//		account.setAccountEntryId(1);
//		account.setAccountEntry("bike");
//		account.setAccountTypeId(1);
//		int result  = adminDaoImpl.modifyAccount(account);
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void testmodifyAccountNegative() {
//		Account account	= new Account();
//		account.setAccountEntryId(3);
//		account.setAccountEntry("bike");
//		account.setAccountTypeId(1);
//		int result  = adminDaoImpl.modifyAccount(account);
//		Assert.assertEquals(0, result);
//	}
//	@Test
//	public void testremoveAccount() {
//		Account account	= new Account();
//		account.setAccountEntryId(3);
//		int result = adminDaoImpl.removeAccount(1);
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void testremoveAccountNegative() {
//		Account account	= new Account();
//		account.setAccountEntryId(3);
//		int result = adminDaoImpl.removeAccount(0);
//		Assert.assertEquals(0, result);
//	}
//	
//	@Test
//	public void testaddRiskPortfolio() {
//		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
//		admRiskPortfolio.setPoints("90");
//		admRiskPortfolio.setBehaviour("corporate");
//		admRiskPortfolio.setEquity(1);
//		admRiskPortfolio.setDebt(1);
//		admRiskPortfolio.setCash(1);
//		int result = adminDaoImpl.riskPortfolio(admRiskPortfolio);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyRiskPortfolio() {
//		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
//		admRiskPortfolio.setRiskPortfolioId(1);
//		admRiskPortfolio.setPoints("mutual");
//		admRiskPortfolio.setBehaviour("corporate");
//		admRiskPortfolio.setEquity(1);
//		admRiskPortfolio.setDebt(1);
//		admRiskPortfolio.setCash(1);
//		int result = adminDaoImpl.modifyRiskPortfolio(admRiskPortfolio);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyRiskPortfolioNegative() {
//		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
//		admRiskPortfolio.setRiskPortfolioId(3);
//		admRiskPortfolio.setPoints("78");
//		admRiskPortfolio.setBehaviour("corporate");
//		admRiskPortfolio.setEquity(1);
//		admRiskPortfolio.setDebt(1);
//		admRiskPortfolio.setCash(1);
//		int result = adminDaoImpl.modifyRiskPortfolio(admRiskPortfolio);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testremoveRiskPortfolio() {
//		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
//		admRiskPortfolio.setRiskPortfolioId(1);
//		int result = adminDaoImpl.removeRiskPortfolio(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testremoveRiskPortfolioNegative() {
//		AdmRiskPortfolio admRiskPortfolio = new AdmRiskPortfolio();
//		admRiskPortfolio.setRiskPortfolioId(1);
//		int result = adminDaoImpl.removeRiskPortfolio(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testaddLicense() {
//		License license = new License();
//		// license.setLicId(1);
//		license.setLicense("certificate");
//		license.setIssuedBy("sebi");
//		license.setProdId(1);
//		int result = adminDaoImpl.license(license);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyLicense() {
//		License license = new License();
//		license.setLicId(1);
//		license.setLicense("certificate");
//		license.setIssuedBy("sbi");
//		license.setProdId(1);
//		int result = adminDaoImpl.modifyLicense(license);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testmodifyLicenseNegative() {
//		License license = new License();
//		license.setLicId(3);
//		license.setLicense("certificate");
//		license.setIssuedBy("sebi");
//		license.setProdId(1);
//		int result = adminDaoImpl.modifyLicense(license);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testremoveLicense() {
//		License license = new License();
//		license.setLicId(1);
//		int result = adminDaoImpl.removeLicense(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testremoveLicenseNegative() {
//		License license = new License();
//		license.setLicId(1);
//		int result = adminDaoImpl.removeLicense(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testAddProduct() {
//		Product product = new Product();
//		product.setProduct("financial product");
//		int result = adminDaoImpl.addProducts(product);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyProduct() {
//		Product product = new Product();
//		product.setProdId(1);
//		product.setProduct("financial product");
//		int result = adminDaoImpl.modifyProduct(product);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyProductNegative() {
//		Product product = new Product();
//		product.setProdId(10);
//		product.setProduct("financial product");
//		int result = adminDaoImpl.modifyProduct(product);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemoveProduct() {
//		Product product = new Product();
//		product.setProdId(1);
//		int result = adminDaoImpl.removeProducts(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemoveProductNegative() {
//		Product product = new Product();
//		product.setProdId(10);
//		product.setProduct("financial product");
//		int result = adminDaoImpl.removeProducts(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testArticleStatus() {
//		ArticleStatus articleStatus = new ArticleStatus();
//		articleStatus.setDesc("active");
//		int result = adminDaoImpl.addArticleStatus(articleStatus);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyArticleStatus() {
//		ArticleStatus articleStatus = new ArticleStatus();
//		articleStatus.setDesc("active");
//		articleStatus.setId(1);
//		int result = adminDaoImpl.modifyArticleStatus(articleStatus);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyArticleStatusNegative() {
//		ArticleStatus articleStatus = new ArticleStatus();
//		articleStatus.setDesc("active");
//		articleStatus.setId(10);
//		int result = adminDaoImpl.modifyArticleStatus(articleStatus);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemoveArticleStatus() {
//		ArticleStatus articleStatus = new ArticleStatus();
//		articleStatus.setId(1);
//		int result = adminDaoImpl.removeArticleStatus(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemoveArticleStatusNegative() {
//		ArticleStatus articleStatus = new ArticleStatus();
//		articleStatus.setId(10);
//		int result = adminDaoImpl.removeArticleStatus(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testcashFlowItemType() {
//		CashFlowItemType cashFlowItemType = new CashFlowItemType();
//		cashFlowItemType.setCashFlowItemType("House hold expenses");
//		int result = adminDaoImpl.addCashFlowItemType(cashFlowItemType);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifycashFlowItemType() {
//		CashFlowItemType cashFlowItemType = new CashFlowItemType();
//		cashFlowItemType.setCashFlowItemType("House hold expenses");
//		cashFlowItemType.setCashFlowItemTypeId(1);
//		int result = adminDaoImpl.modifyCashFlowItemType(cashFlowItemType);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifycashFlowItemTypeNegative() {
//		CashFlowItemType cashFlowItemType = new CashFlowItemType();
//		cashFlowItemType.setCashFlowItemType("House hold expenses");
//		cashFlowItemType.setCashFlowItemTypeId(10);
//		int result = adminDaoImpl.modifyCashFlowItemType(cashFlowItemType);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemovecashFlowItemType() {
//		CashFlowItemType cashFlowItemType = new CashFlowItemType();
//		cashFlowItemType.setCashFlowItemTypeId(5);
//		int result = adminDaoImpl.removeCashFlowItemType(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemovecashFlowItemTypeNegative() {
//		CashFlowItemType cashFlowItemType = new CashFlowItemType();
//		cashFlowItemType.setCashFlowItemTypeId(10);
//		int result = adminDaoImpl.removeCashFlowItemType(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testcashFlowItem() {
//		CashFlowItem cashFlowItem = new CashFlowItem();
//		cashFlowItem.setCashFlowItem("House allowance");
//		cashFlowItem.setCashFlowItemTypeId(1);
//		int result = adminDaoImpl.addCashFlowItem(cashFlowItem);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifycashFlowItem() {
//		CashFlowItem cashFlowItem = new CashFlowItem();
//		cashFlowItem.setCashFlowItem("House allowance");
//		cashFlowItem.setCashFlowItemId(1);
//		cashFlowItem.setCashFlowItemTypeId(1);
//		int result = adminDaoImpl.modifyCashFlowItem(cashFlowItem);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifycashFlowItemNegative() {
//		CashFlowItem cashFlowItem = new CashFlowItem();
//		cashFlowItem.setCashFlowItem("House hold expenses");
//		cashFlowItem.setCashFlowItemId(10);
//		cashFlowItem.setCashFlowItemTypeId(5);
//		int result = adminDaoImpl.modifyCashFlowItem(cashFlowItem);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemovecashFlowItem() {
//		CashFlowItem cashFlowItem = new CashFlowItem();
//		cashFlowItem.setCashFlowItemId(5);
//		int result = adminDaoImpl.removeCashFlowItem(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemovecashFlowItemNegative() {
//		CashFlowItem cashFlowItem = new CashFlowItem();
//		cashFlowItem.setCashFlowItemTypeId(10);
//		int result = adminDaoImpl.removeCashFlowItem(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testCity() {
//		City city = new City();
//		city.setStateId(1);
//		city.setCity("Madurai");
//		city.setPincode("632821");
//		city.setDistrict("madurai");
//		int result = adminDaoImpl.addCity(city);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifycity() {
//		City city = new City();
//		city.setCityId(1);
//		city.setStateId(1);
//		city.setCity("Madurai");
//		city.setPincode("632821");
//		city.setDistrict("madurai");
//		int result = adminDaoImpl.modifyCity(city);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifycityNegative() {
//		City city = new City();
//		city.setCityId(3);
//		city.setStateId(4);
//		city.setCity("Madurai");
//		city.setPincode("632821");
//		city.setDistrict("madurai");
//		int result = adminDaoImpl.modifyCity(city);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemovecity() {
//		City city = new City();
//		city.setCityId(1);
//		int result = adminDaoImpl.removeCity(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemovecityNegative() {
//		City city = new City();
//		city.setCityId(2);
//		int result = adminDaoImpl.removeCity(0);
//		Assert.assertEquals(0, result);
//	}
//
//	// @Test //Encode Decode //
//	// public void testcheckPartyIsPresent() {
//	// String encryptPass = admTableFields.getEncryption_password();
//	// int result = adminDaoImpl.checkPartyIsPresent("ADM000000000D", encryptPass);
//	// Assert.assertEquals(1, result);
//	//
//	// }
//	@Test
//	public void testcheckAdminIsPresent() {
//		int id = adminDaoImpl.checkAdminIsPresent("ADM000000000B");
//		Assert.assertEquals(1, id);
//
//	}
//
//	@Test
//	public void testcheckAdminIsPresentNegative() {
//		int id = adminDaoImpl.checkAdminIsPresent("ADM000000000D");
//		Assert.assertEquals(0, id);
//
//	}
//
//	@Test
//	public void testsaveAddAcctype() {
//		Acctype acctype = new Acctype();
//		acctype.setAccType("advisor");
//		int result = adminDaoImpl.saveAddAcctype(acctype);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testmodifyAcctype() {
//		Acctype acctype = new Acctype();
//		acctype.setAccType("advisor");
//		acctype.setAccTypeId(0);
//		int result = adminDaoImpl.modifyAcctype(acctype);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testModifyAcctypeNegative() {
//		Acctype acctype = new Acctype();
//		acctype.setAccTypeId(5);
//		acctype.setAccType("advisor");
//		int result = adminDaoImpl.modifyAcctype(acctype);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testRemoveAcctype() {
//		Acctype acctype = new Acctype();
//		acctype.setAccTypeId(1);
//		int result = adminDaoImpl.RemoveAcctype(1);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveAcctypeNegative() {
//		Acctype acctype = new Acctype();
//		acctype.setAccTypeId(1);
//		acctype.setAccType("advisor");
//		int result = adminDaoImpl.RemoveAcctype(0);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testaddRemuneration() {
//		Remuneration remuneration = new Remuneration();
//		remuneration.setRemuneration("Accounting");
//		int result = adminDaoImpl.addRemuneration(remuneration);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyRemuneration() {
//		Remuneration remuneration = new Remuneration();
//		remuneration.setRemId(0);
//		remuneration.setRemuneration("advisor");
//		int result = adminDaoImpl.modifyRemuneration(remuneration);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testModifyRemunerationNegative() {
//		Remuneration remuneration = new Remuneration();
//		remuneration.setRemId(1);
//		remuneration.setRemuneration("advisor");
//		int result = adminDaoImpl.modifyRemuneration(remuneration);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemunerationNegative() {
//		Remuneration remuneration = new Remuneration();
//		remuneration.setRemId(1);
//		remuneration.setRemuneration("advisor");
//		int result = adminDaoImpl.removeRemuneration(1);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveRemunerationNegative() {
//		Remuneration remuneration = new Remuneration();
//		remuneration.setRemId(4);
//		remuneration.setRemuneration("advisor");
//		int result = adminDaoImpl.removeRemuneration(4);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testAddState() {
//		State state = new State();
//		state.setState("Corporate");
//		int result = adminDaoImpl.addState(state);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testModifyState() {
//		State state = new State();
//		state.setStateId(0);
//		state.setState("Corporate");
//		int result = adminDaoImpl.modifyState(state);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testModifyStateNegative() {
//		State state = new State();
//		state.setStateId(1);
//		state.setState("Corporate");
//		int result = adminDaoImpl.modifyState(state);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveState() {
//		State state = new State();
//		state.setStateId(1);
//		int result = adminDaoImpl.removeState(1);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveStateNegative() {
//		State state = new State();
//		state.setStateId(4);
//		state.setState("Corporate");
//		int result = adminDaoImpl.removeState(4);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testAddWorkflowststatus() {
//		Workflowstatus workflowstatus = new Workflowstatus();
//		workflowstatus.setStatus("Corporate");
//		int result = adminDaoImpl.addWorkflowstatus(workflowstatus);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testModifyWorkflowststatus() {
//		Workflowstatus workflowstatus = new Workflowstatus();
//		workflowstatus.setWorkflowId(0);
//		workflowstatus.setStatus("Corporate");
//		int result = adminDaoImpl.modifyWorkflowstatus(workflowstatus);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testModifyWorkflowststatusNegative() {
//		Workflowstatus workflowstatus = new Workflowstatus();
//		workflowstatus.setWorkflowId(1);
//		workflowstatus.setStatus("Corporate");
//		int result = adminDaoImpl.modifyWorkflowstatus(workflowstatus);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveWorkflowststatus() {
//		Workflowstatus workflowstatus = new Workflowstatus();
//		workflowstatus.setWorkflowId(1);
//		int result = adminDaoImpl.removeWorkFlowStatus(1);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveWorkflowststatusNegative() {
//		Workflowstatus workflowstatus = new Workflowstatus();
//		workflowstatus.setWorkflowId(4);
//		workflowstatus.setStatus("Corporate");
//		int result = adminDaoImpl.removeWorkFlowStatus(4);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testAddService() {
//		Service service = new Service();
//		service.setService("Corporate");
//
//		int result = adminDaoImpl.addService(service);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testModifyService() {
//		Service service = new Service();
//		service.setServiceId(0);
//		service.setService("Corporate");
//		int result = adminDaoImpl.modifyService(service);
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void testModifyServiceNegative() {
//		Service service = new Service();
//		service.setServiceId(1);
//		service.setService("Corporate");
//		int result = adminDaoImpl.modifyService(service);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveService() {
//		Service service = new Service();
//		service.setServiceId(1);
//		int result = adminDaoImpl.removeService(1);
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void testRemoveServiceNegative() {
//		Service service = new Service();
//		service.setServiceId(4);
//		service.setService("Corporate");
//		int result = adminDaoImpl.removeService(4);
//		Assert.assertEquals(0, result);
//
//	}
//	
//	
//	@Test
//	public void testAddVotetype() {
//		Votetype votetype = new Votetype();
//		votetype.setDesc("Corporate");
//		
//		int result = adminDaoImpl.addVotetype(votetype);
//		Assert.assertEquals(1, result);
//
//	}
//	@Test
//	public void testModifyVotetype() {
//		Votetype votetype = new Votetype();
//		votetype.setId(0);
//		votetype.setDesc("Corporate");
//		int result = adminDaoImpl.modifyVotetype(votetype);
//		Assert.assertEquals(0, result);
//
//	}
//	@Test
//	public void testModifyVotetypeNegative() {
//		Votetype votetype = new Votetype();
//		votetype.setId(1);
//		votetype.setDesc("Corporate");
//		int result = adminDaoImpl.modifyVotetype(votetype);
//		Assert.assertEquals(1, result);
//
//	}
//	
//	@Test
//	public void testRemoveVotetype() {
//		Votetype votetype = new Votetype();
//		votetype.setId(1);
//		int result = adminDaoImpl.removeVotetype(1);
//		Assert.assertEquals(1, result);
//
//	}
//	@Test
//	public void testRemoveVotetypeNegative() {
//		Votetype votetype = new Votetype();
//		votetype.setId(4);
//		votetype.setDesc("Corporate");
//		int result = adminDaoImpl.removeVotetype(4);
//		Assert.assertEquals(0, result);
//
//	}	
//	
//		
//	@Test
//	public void testAddBrand() {
//		Brand brand = new Brand();
//		brand.setBrand("Corporate");
//		
//		int result = adminDaoImpl.addBrand(brand);
//		Assert.assertEquals(1, result);
//
//	}
//	@Test
//	public void testModifyBrand() {
//		Brand brand = new Brand();
//		brand.setBrandId(0);
//		brand.setBrand("Corporate");
//		int result = adminDaoImpl.modifyBrand(brand);
//		Assert.assertEquals(0, result);
//
//	}
//	@Test
//	public void testModifyBrandNegative() {
//		Brand brand = new Brand();
//		brand.setBrandId(1);
//		brand.setBrand("Corporate");
//		int result = adminDaoImpl.modifyBrand(brand);
//		Assert.assertEquals(1, result);
//
//	}
//	
//	@Test
//	public void testRemoveBrand() {
//		Brand brand = new Brand();
//		brand.setBrandId(1);
//		int result = adminDaoImpl.removeBrand(1);
//		Assert.assertEquals(1, result);
//
//	}
//	@Test
//	public void testRemoveBrandNegative() {
//		Brand brand = new Brand();
//		brand.setBrandId(4);
//		brand.setBrand("Corporate");
//		int result = adminDaoImpl.removeBrand(4);
//		Assert.assertEquals(0, result);
//
//	}		
//
//	@Test
//	public void testAddInsuranceItem() {
//		InsuranceItem insuranceItem = new InsuranceItem();
//		insuranceItem.setInsuranceItem("home loan");
//		insuranceItem.setValue("average");
//     	int result = adminDaoImpl.addInsuranceItem(insuranceItem);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyInsuranceItem() {
//		InsuranceItem insuranceItem = new InsuranceItem();
//		insuranceItem.setInsuranceItemId(1);
//		insuranceItem.setInsuranceItem("home loan");
//		insuranceItem.setValue("average");
//    	int result = adminDaoImpl.modifyInsuranceItem(insuranceItem);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyInsuranceItemNegative() {
//		InsuranceItem insuranceItem = new InsuranceItem();
//		insuranceItem.setInsuranceItemId(3);
//		insuranceItem.setInsuranceItem("home loan");
//		insuranceItem.setValue("average");
//    	int result = adminDaoImpl.modifyInsuranceItem(insuranceItem);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemoveInsuranceItem() {
//		InsuranceItem insuranceItem = new InsuranceItem();
//		insuranceItem.setInsuranceItemId(1);
//		int result = adminDaoImpl.removeInsuranceItem(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemoveInsuranceItemNegative() {
//		InsuranceItem insuranceItem = new InsuranceItem();
//		insuranceItem.setInsuranceItemId(3);
//		int result = adminDaoImpl.removeInsuranceItem(0);
//		Assert.assertEquals(0, result);
//	}
//	
//	@Test
//	public void testAddUserType() {
//		UserType userType = new UserType();
//		userType.setDesc("active");
//        int result = adminDaoImpl.addUserType(userType);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyUserType() {
//		UserType userType = new UserType();
//		userType.setId(1);
//		userType.setDesc("active");
//
//		int result = adminDaoImpl.modifyUserType(userType);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testModifyUserTypeNegative() {
//		UserType userType = new UserType();
//		userType.setId(3);
//		userType.setDesc("active");
//
//		int result = adminDaoImpl.modifyUserType(userType);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void testRemoveUserType() {
//		UserType userType = new UserType();
//		userType.setId(1);
//		
//		int result = adminDaoImpl.removeUserType(1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void testRemoveUserTypeNegative() {
//		UserType userType = new UserType();
//		userType.setId(3);
//		
//		int result = adminDaoImpl.removeUserType(0);
//		Assert.assertEquals(0, result);
//	}
//}
