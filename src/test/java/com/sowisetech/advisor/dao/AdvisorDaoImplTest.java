//package com.sowisetech.advisor.dao;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
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
//import com.sowisetech.advisor.model.AdvBrandInfo;
//import com.sowisetech.advisor.model.AdvBrandRank;
//import com.sowisetech.advisor.model.AdvProduct;
//import com.sowisetech.advisor.model.Advisor;
//import com.sowisetech.advisor.model.AdvisorType;
//import com.sowisetech.advisor.model.ArticleStatus;
//import com.sowisetech.advisor.model.Award;
//import com.sowisetech.advisor.model.Brand;
//import com.sowisetech.advisor.model.Category;
//import com.sowisetech.advisor.model.CategoryType;
//import com.sowisetech.advisor.model.Certificate;
//import com.sowisetech.advisor.model.ChatUser;
//import com.sowisetech.advisor.model.CityList;
//import com.sowisetech.advisor.model.Education;
//import com.sowisetech.advisor.model.Experience;
//import com.sowisetech.advisor.model.FollowerStatus;
//import com.sowisetech.advisor.model.Followers;
//import com.sowisetech.advisor.model.ForumCategory;
//import com.sowisetech.advisor.model.ForumStatus;
//import com.sowisetech.advisor.model.ForumSubCategory;
//import com.sowisetech.advisor.model.KeyPeople;
//import com.sowisetech.advisor.model.License;
//import com.sowisetech.advisor.model.PartyStatus;
//import com.sowisetech.advisor.model.Product;
//import com.sowisetech.advisor.model.Promotion;
//import com.sowisetech.advisor.model.Remuneration;
//import com.sowisetech.advisor.model.RiskQuestionaire;
//import com.sowisetech.advisor.model.Service;
//import com.sowisetech.advisor.model.ServicePlan;
//import com.sowisetech.advisor.model.State;
//import com.sowisetech.advisor.model.UserType;
//import com.sowisetech.advisor.model.WorkFlowStatus;
//import com.sowisetech.advisor.util.AdvTableFields;
//import com.sowisetech.investor.model.Investor;
//
//@Transactional
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//public class AdvisorDaoImplTest {
//
//	AdvisorDaoImpl advisorDaoImpl;
//
//	EmbeddedDatabase db;
//
//	@Autowired(required = true)
//	@Spy
//	AdvTableFields advTableFields;
//
//	@Before
//	public void setup() {
//		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//				.addScript("db_sql/advschema.sql").addScript("db_sql/advdata.sql").build();
//		advisorDaoImpl = new AdvisorDaoImpl();
//		advisorDaoImpl.setDataSource(db);
//		advisorDaoImpl.postConstruct();
//	}
//
//	@Test
//	public void test_fetchTypeIdByAdvtype_Success() {
//		String type = "individual";
//		int id = advisorDaoImpl.fetchTypeIdByAdvtype(type);
//		Assert.assertEquals(1, id);
//	}
//
//	@Test
//	public void test_fetchTypeIdByAdvtype_Error() {
//		String type = "individual_error";
//		int id = advisorDaoImpl.fetchTypeIdByAdvtype(type);
//		Assert.assertEquals(0, id);
//	}
//
//	// // @Test // Encode decode
//	// // public void tes_advSignUp_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// // Advisor advisor = new Advisor();
//	// // advisor.setAdvId("ADV000000000C");
//	// // advisor.setName("adv");
//	// // advisor.setDesignation("Eng");
//	// // advisor.setEmailId("adv@adv.com");
//	// // advisor.setPassword("!@AS12as");
//	// // advisor.setPhoneNumber("9876541230");
//	// // advisor.setPartyStatusId(1);
//	// // advisor.setCreated(timestamp);
//	// // advisor.setUpdated(timestamp);
//	// // advisor.setDelete_flag("N");
//	// // advisor.setAdvType(1);
//	// // int result = advisorDaoImpl.advSignup(advisor, encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void tes_advSignUp_Error() {
//		Advisor advisor = new Advisor();
//		String encryptPass = "Sowise@Ever21";
//		int result = advisorDaoImpl.advSignup(advisor, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	// // TODO
//	// // @Test
//	// // public void test_fetchAdvRoleIdByName_Success() {
//	// // String name = "advisor";
//	// // long id = advisorDaoImpl.fetchAdvRoleIdByName(name);
//	// // Assert.assertEquals(1, id);
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchAdvRoleIdByName_Error() {
//	// // String name = "advisor_error";
//	// // long id = advisorDaoImpl.fetchAdvRoleIdByName(name);
//	// // Assert.assertEquals(0, id);
//	// // }
//	//
//	// // @Test
//	// // public void test_addParty_Success() {
//	// // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// // Advisor advisor = new Advisor();
//	// // advisor.setAdvId("ADV000000000C");
//	// //// advisor.setName("adv");
//	// //// advisor.setDesignation("Eng");
//	// // advisor.setEmailId("adv@adv.com");
//	// // advisor.setPassword("!@AS12as");
//	// //// advisor.setPhoneNumber("9876541230");
//	// // advisor.setPartyStatusId(1);
//	// // advisor.setCreated(timestamp);
//	// // advisor.setUpdated(timestamp);
//	// // advisor.setDelete_flag("N");
//	// //// advisor.setAdvType(1);
//	// // long roleId = 1;
//	// // int result = advisorDaoImpl.addParty(advisor, roleId);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_addParty_Error() {
//		String encryptPass = "Sowise@Ever21";
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000C");
//		advisor.setName("adv");
//		advisor.setDesignation("Eng");
//		advisor.setEmailId("adv@adv.com");
//		advisor.setPassword("!@AS12as");
//		advisor.setPhoneNumber("9876541230");
//		advisor.setPartyStatusId(10);
//		advisor.setCreated(timestamp);
//		advisor.setUpdated(timestamp);
//		advisor.setDelete_flag("N");
//		advisor.setAdvType(1);
//		int result = advisorDaoImpl.addParty(advisor, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchAdvBrandInfoByAdvId_Success() {
//		List<AdvBrandInfo> advBrandInfos = advisorDaoImpl.fetchAdvBrandInfoByAdvId("ADV000000000A", "N");
//		Assert.assertEquals(2, advBrandInfos.size());
//	}
//
//	@Test
//	public void test_fetchAdvBrandInfoByAdvId_Error() {
//		List<AdvBrandInfo> advBrandInfos = advisorDaoImpl.fetchAdvBrandInfoByAdvId("ADV000000000A", "NO");
//		Assert.assertEquals(0, advBrandInfos.size());
//	}
//
//	@Test
//	public void test_fetchBrandByBrandId_Success() {
//		String brand = advisorDaoImpl.fetchBrandByBrandId(1);
//		Assert.assertEquals("Aditya Birla Sun Life", brand);
//	}
//
//	@Test
//	public void test_fetchBrandByBrandId_Error() {
//		String brand = advisorDaoImpl.fetchBrandByBrandId(5);
//		Assert.assertEquals(null, brand);
//	}
//
//	// // @Test
//	// // public void test_fetchAdvisorList_Success() {
//	// // List<Advisor> advisors = advisorDaoImpl.fetchAdvisorList("N");
//	// // Assert.assertEquals(1, advisors.size());
//	// // }
//	//
//	// // @Test // Encode decode
//	// // public void test_update_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Advisor advisor = new Advisor();
//	// // advisor.setName("advisor");
//	// // advisor.setDesignation("Engineer");
//	// // int result = advisorDaoImpl.update("ADV000000000A", advisor,encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_update_Error() {
//		String encryptPass = "Sowise@Ever21";
//		Advisor advisor = new Advisor();
//		advisor.setName("advisor");
//		advisor.setDesignation("Engineer");
//		int result = advisorDaoImpl.update("ADV", advisor, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_updateAdvisorTimeStamp_Success() {
//		int result = advisorDaoImpl.updateAdvisorTimeStamp("ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_updateAdvisorTimeStamp_Error() {
//		int result = advisorDaoImpl.updateAdvisorTimeStamp("ADV");
//		Assert.assertEquals(0, result);
//	}
//
//	//
//	// // @Test
//	// // public void test_fetchAdvisorByAdvId() {
//	// // Advisor advisor = advisorDaoImpl.fetchAdvisorByAdvId("ADV000000000A",
//	// // "N");
//	// // Assert.assertEquals("adv", advisor.getName());
//	// // }
//	//
//	@Test
//	public void test_addAdvProductInfo_Success() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("advisor");
//		advisor.setDesignation("Engineer");
//		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
//		AdvProduct advProduct = new AdvProduct();
//		advProduct.setProdId(1);
//		advProducts.add(advProduct);
//		advisor.setAdvProducts(advProducts);
//		int result = advisorDaoImpl.addAdvProductInfo("ADV000000000A", advProduct, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvProductInfo_Error() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("advisor");
//		advisor.setDesignation("Engineer");
//		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
//		AdvProduct advProduct = new AdvProduct();
//		advProduct.setProdId(1);
//		advProducts.add(advProduct);
//		advisor.setAdvProducts(advProducts);
//		int result = advisorDaoImpl.addAdvProductInfo("ADV", advProduct, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchAward_Success() {
//		Award award = advisorDaoImpl.fetchAward(1, "N");
//		Assert.assertEquals("Award", award.getTitle());
//	}
//
//	@Test
//	public void test_fetchAward_Error() {
//		Award award = advisorDaoImpl.fetchAward(1, "NO");
//		Assert.assertEquals(null, award);
//	}
//
//	@Test
//	public void test_fetchCertificate_Success() {
//		Certificate certificate = advisorDaoImpl.fetchCertificate(1, "N");
//		Assert.assertEquals("Certificate", certificate.getTitle());
//	}
//
//	@Test
//	public void test_fetchCertificate_Error() {
//		Certificate certificate = advisorDaoImpl.fetchCertificate(1, "NO");
//		Assert.assertEquals(null, certificate);
//	}
//
//	@Test
//	public void test_fetchEducation_Success() {
//		Education education = advisorDaoImpl.fetchEducation(1, "N");
//		Assert.assertEquals("B.E", education.getDegree());
//	}
//
//	@Test
//	public void test_fetchEducation_Error() {
//		Education education = advisorDaoImpl.fetchEducation(1, "NO");
//		Assert.assertEquals(null, education);
//	}
//
//	@Test
//	public void test_fetchExperience_Success() {
//		Experience experience = advisorDaoImpl.fetchExperience(1, "N");
//		Assert.assertEquals("chennai", experience.getLocation());
//	}
//
//	@Test
//	public void test_fetchExperience_Error() {
//		Experience experience = advisorDaoImpl.fetchExperience(1, "NO");
//		Assert.assertEquals(null, experience);
//	}
//
//	@Test
//	public void test_removeAdvAward_Success() {
//		int result = advisorDaoImpl.removeAdvAward(1, "ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeAdvAward_Error() {
//		int result = advisorDaoImpl.removeAdvAward(1, "ADV000000000A", "YO", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeAdvCertificate_Success() {
//		int result = advisorDaoImpl.removeAdvCertificate(1, "ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeAdvCertificate_Error() {
//		int result = advisorDaoImpl.removeAdvCertificate(1, "ADV000000000A", "YO", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeAdvEducation_Success() {
//		int result = advisorDaoImpl.removeAdvEducation(1, "ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeAdvEducation_Error() {
//		int result = advisorDaoImpl.removeAdvEducation(1, "ADV000000000A", "YO", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeAdvExperience_Success() {
//		int result = advisorDaoImpl.removeAdvExperience(1, "ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeAdvExperience_Error() {
//		int result = advisorDaoImpl.removeAdvExperience(1, "ADV000000000A", "YO", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test // Encode decode
//	// // public void test_fetchAdvisorByEmailId_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Advisor adv = advisorDaoImpl.fetchAdvisorByEmailId("adv@adv.com",
//	// // "N",encryptPass);
//	// // Assert.assertEquals("adv", adv.getName());
//	// // }
//	//
//	// // @Test // Encode decode
//	// // public void test_fetchAdvisorByEmailId_Error() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Advisor adv = advisorDaoImpl.fetchAdvisorByEmailId("adv@a.com",
//	// // "N",encryptPass);
//	// // Assert.assertEquals(null, adv);
//	// // }
//
//	@Test
//	public void test_fetchfetchAdvisorSmartId_Success() {
//		String advId = advisorDaoImpl.fetchAdvisorSmartId();
//		Assert.assertEquals("ADV000000000B", advId);
//	}
//
//	@Test
//	public void test_addAdvSmartId_Success() {
//		int result = advisorDaoImpl.addAdvSmartId("ADV000000000C");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchEncryptionSecretKey_Success() {
//		String key = advisorDaoImpl.fetchEncryptionSecretKey();
//		Assert.assertEquals("Key", key);
//	}
//	//
//	// // @Test // Encode decode
//	// // public void test_addAdvPersonalInfo_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Advisor adv = new Advisor();
//	// // adv.setAdvId("ADV000000000A");
//	// // adv.setDisplayName("advisor96");
//	// // adv.setPincode("654123");
//	// // int result = advisorDaoImpl.addAdvPersonalInfo("ADV000000000A",
//	// // adv,encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//
//	@Test
//	public void test_addAdvPersonalInfo_Error() {
//		Advisor adv = new Advisor();
//		adv.setAdvId("ADV000000000A");
//		adv.setDisplayName("advisor96");
//		adv.setPincode("654123");
//		String encryptPass = "Sowise@Ever21";
//		int result = advisorDaoImpl.addAdvPersonalInfo("ADV", adv, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_changeAdvPassword_Success() {
//		int result = advisorDaoImpl.changeAdvPassword("ADV000000000A", "SA@!sa21");
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void test_changeAdvPassword_Error() {
//		int result = advisorDaoImpl.changeAdvPassword("ADV000000000U", "SA@!sa21");
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void test_fetchAdvProduct_Success() {
//		AdvProduct advProduct = advisorDaoImpl.fetchAdvProduct(1, "N");
//		Assert.assertEquals("1", advProduct.getServiceId());
//	}
//
//	@Test
//	public void test_fetchAdvProduct_Error() {
//		AdvProduct advProduct = advisorDaoImpl.fetchAdvProduct(10, "N");
//		Assert.assertEquals(null, advProduct);
//	}
//
//	@Test
//	public void test_modifyAdvisorProduct_Success() {
//		AdvProduct advProduct = new AdvProduct();
//		advProduct.setAdvProdId(1);
//		advProduct.setServiceId("2");
//		int result = advisorDaoImpl.modifyAdvisorProduct(advProduct, "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorProduct_Error() {
//		AdvProduct advProduct = new AdvProduct();
//		advProduct.setAdvProdId(1);
//		advProduct.setServiceId("2");
//		int result = advisorDaoImpl.modifyAdvisorProduct(advProduct, "ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvBrandInfo_Success() {
//		AdvBrandInfo advBrandInfo = new AdvBrandInfo();
//		advBrandInfo.setAdvId("ADV000000000A");
//		advBrandInfo.setBrandId(1);
//		advBrandInfo.setServiceId("1");
//		advBrandInfo.setProdId(2);
//		int result = advisorDaoImpl.addAdvBrandInfo("ADV000000000A", advBrandInfo, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvBrandInfo_Error() {
//		AdvBrandInfo advBrandInfo = new AdvBrandInfo();
//		advBrandInfo.setAdvId("ADV000000000A");
//		advBrandInfo.setBrandId(1);
//		advBrandInfo.setProdId(2);
//		int result = advisorDaoImpl.addAdvBrandInfo("ADV000000000U", advBrandInfo, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchCategoryList_Success() {
//		List<Category> category = advisorDaoImpl.fetchCategoryList();
//		Assert.assertEquals(2, category.size());
//		Assert.assertEquals("general insurance", category.get(1).getDesc());
//	}
//
//	@Test
//	public void test_fetchCategoryTypeList_Success() {
//		List<CategoryType> categoryType = advisorDaoImpl.fetchCategoryTypeList();
//		Assert.assertEquals(2, categoryType.size());
//		Assert.assertEquals("accounting", categoryType.get(1).getDesc());
//	}
//
//	@Test
//	public void test_fetchForumCategoryList_Success() {
//		List<ForumCategory> forumCategory = advisorDaoImpl.fetchForumCategoryList();
//		Assert.assertEquals(2, forumCategory.size());
//		Assert.assertEquals("stock", forumCategory.get(1).getName());
//	}
//
//	@Test
//	public void testfetchAllRiskQuestionaire_Success() {
//		List<RiskQuestionaire> riskQuestionaire = advisorDaoImpl.fetchAllRiskQuestionaire();
//		Assert.assertEquals(1, riskQuestionaire.size());
//		Assert.assertEquals(2, riskQuestionaire.get(0).getAnswerId());
//	}
//
//	// // @Test //join query
//	// // public void test_fetchProductList_Success() {
//	// // List<Product> product = advisorDaoImpl.fetchProductList();
//	// // Assert.assertEquals(1, product.size());
//	// // Assert.assertEquals(1, product.get(0).getServices().size());
//	// // Assert.assertEquals(2, product.get(0).getBrands().size());
//	// // Assert.assertEquals(2, product.get(0).getServicePlans().size());
//	// // }
//	//
//	// // TODO
//	// // @Test
//	// // public void test_fetchRoleList_Success() {
//	// // List<RoleAuth> role = advisorDaoImpl.fetchRoleList();
//	// // Assert.assertEquals(3, role.size());
//	// // Assert.assertEquals("nonindividual", role.get(2).getName());
//	// // }
//
//	@Test
//	public void test_fetchForumSubCategoryList_Success() {
//		List<ForumSubCategory> forum = advisorDaoImpl.fetchForumSubCategoryList();
//		Assert.assertEquals(1, forum.size());
//		Assert.assertEquals("equity fund", forum.get(0).getName());
//	}
//
//	@Test
//	public void test_fetchForumStatusList_Success() {
//		List<ForumStatus> status = advisorDaoImpl.fetchForumStatusList();
//		Assert.assertEquals(1, status.size());
//		Assert.assertEquals("init", status.get(0).getDesc());
//	}
//
//	@Test
//	public void test_fetchPartyStatusList_Success() {
//		List<PartyStatus> party = advisorDaoImpl.fetchPartyStatusList();
//		Assert.assertEquals(2, party.size());
//		Assert.assertEquals("active", party.get(0).getDesc());
//		Assert.assertEquals("inactive", party.get(1).getDesc());
//	}
//
//	@Test
//	public void test_fetchServiceList_Success() {
//		List<Service> services = advisorDaoImpl.fetchServiceList();
//		Assert.assertEquals(1, services.size());
//	}
//
//	@Test
//	public void test_fetchBrandList_Success() {
//		List<Brand> brands = advisorDaoImpl.fetchBrandList();
//		Assert.assertEquals(2, brands.size());
//	}
//
//	@Test
//	public void test_fetchLicenseList_Success() {
//		List<License> licenses = advisorDaoImpl.fetchLicenseList();
//		Assert.assertEquals(1, licenses.size());
//	}
//
//	@Test
//	public void test_fetchRemunerationList_Success() {
//		List<Remuneration> remunerations = advisorDaoImpl.fetchRemunerationList();
//		Assert.assertEquals(1, remunerations.size());
//	}
//
//	@Test
//	public void test_fetchAdvBrandInfoByAdvIdAndProdId_Success() {
//		List<AdvBrandInfo> advBrandInfo = advisorDaoImpl.fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000A", 1, "N");
//		Assert.assertEquals(2, advBrandInfo.size());
//		Assert.assertEquals("2", advBrandInfo.get(1).getServiceId());
//	}
//
//	@Test
//	public void test_fetchAdvBrandInfoByAdvIdAndProdId_Error() {
//		List<AdvBrandInfo> advBrandInfo = advisorDaoImpl.fetchAdvBrandInfoByAdvIdAndProdId("ADV000000000U", 1, "N");
//		Assert.assertEquals(0, advBrandInfo.size());
//	}
//
//	@Test
//	public void test_fetchPriorityByBrandIdAndAdvId_Success() {
//		List<Long> priority = advisorDaoImpl.fetchPriorityByBrandIdAndAdvId("ADV000000000A", 1, 1, "N");
//		Assert.assertEquals(1, priority.size());
//	}
//
//	@Test
//	public void test_fetchPriorityByBrandIdAndAdvId_Error() {
//		List<Long> priority = advisorDaoImpl.fetchPriorityByBrandIdAndAdvId("ADV000000000U", 1, 1, "N");
//		Assert.assertEquals(0, priority.size());
//	}
//
//	@Test
//	public void test_fetchAdvBrandRank_Success() {
//		AdvBrandRank advBrandRank = advisorDaoImpl.fetchAdvBrandRank("ADV000000000A", 1, 1, "N");
//		Assert.assertEquals(1, advBrandRank.getBrandId());
//	}
//
//	@Test
//	public void test_fetchAdvBrandRank_Error() {
//		AdvBrandRank advBrandRank = advisorDaoImpl.fetchAdvBrandRank("ADV000000000U", 1, 1, "N");
//		Assert.assertEquals(null, advBrandRank);
//	}
//
//	@Test
//	public void test_addAdvBrandAndRank_Success() {
//		AdvBrandRank advBrandRank = new AdvBrandRank();
//		advBrandRank.setAdvId("ADV000000000A");
//		advBrandRank.setBrandId(1);
//		advBrandRank.setProdId(2);
//		int result = advisorDaoImpl.addAdvBrandAndRank(1, 1, "ADV000000000A", 1, "N", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvBrandAndRank_Error() {
//		AdvBrandRank advBrandRank = new AdvBrandRank();
//		advBrandRank.setAdvId("ADV000000000A");
//		advBrandRank.setBrandId(1);
//		advBrandRank.setProdId(2);
//		int result = advisorDaoImpl.addAdvBrandAndRank(1, 1, "ADV000000000U", 1, "N", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_updateBrandAndRank_Success() {
//		int result = advisorDaoImpl.updateBrandAndRank(2, 1, "ADV000000000A", 1, "N", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_updateBrandAndRank_Error() {
//		int result = advisorDaoImpl.updateBrandAndRank(2, 1, "ADV000000000U", 1, "N", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchAdvProductByAdvId_success() {
//		List<AdvProduct> advProducts = advisorDaoImpl.fetchAdvProductByAdvId("ADV000000000A", "N");
//		Assert.assertEquals(1, advProducts.size());
//	}
//
//	@Test
//	public void test_fetchAdvProductByAdvId_error() {
//		List<AdvProduct> advProducts = advisorDaoImpl.fetchAdvProductByAdvId("ADV000000000U", "N");
//		Assert.assertEquals(0, advProducts.size());
//	}
//
//	// // @Test
//	// // public void test_removeAdvProduct_Success() {
//	// // int result = advisorDaoImpl.removeAdvProduct(1, "ADV000000000A", "Y", 1);
//	// // Assert.assertEquals(1, result);
//	// // }
//
//	@Test
//	public void test_removeAdvProduct_Error() {
//		int result = advisorDaoImpl.removeAdvProduct(1, "ADV000000000U", "Y", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeAdvBrandInfo_Success() {
//		int result = advisorDaoImpl.removeAdvBrandInfo(1, "ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(2, result);
//	}
//
//	@Test
//	public void test_removeAdvBrandInfo_Error() {
//		int result = advisorDaoImpl.removeAdvBrandInfo(1, "ADV000000000U", "Y", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeFromBrandRank_Success() {
//		int result = advisorDaoImpl.removeFromBrandRank("ADV000000000A", 1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeFromBrandRank_Error() {
//		int result = advisorDaoImpl.removeFromBrandRank("ADV000000000U", 1);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchAdvProductByAdvIdAndAdvProdId_Success() {
//		AdvProduct advProduct = advisorDaoImpl.fetchAdvProductByAdvIdAndAdvProdId("ADV000000000A", 1, "N");
//		Assert.assertEquals(1, advProduct.getProdId());
//	}
//
//	@Test
//	public void test_fetchAdvProductByAdvIdAndAdvProdId_Error() {
//		AdvProduct advProduct = advisorDaoImpl.fetchAdvProductByAdvIdAndAdvProdId("ADV000000000U", 1, "N");
//		Assert.assertEquals(null, advProduct);
//	}
//
//	@Test
//	public void test_removeAdvBrandInfoByAdvId_Success() {
//		int result = advisorDaoImpl.removeAdvBrandInfoByAdvId("ADV000000000A");
//		Assert.assertEquals(2, result);
//	}
//
//	@Test
//	public void test_removeAdvBrandInfoByAdvId_Error() {
//		int result = advisorDaoImpl.removeAdvBrandInfoByAdvId("ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeAdvBrandRankByAdvId_Success() {
//		int result = advisorDaoImpl.removeAdvBrandRankByAdvId("ADV000000000A");
//		Assert.assertEquals(2, result);
//	}
//
//	@Test
//	public void test_removeAdvBrandRankByAdvId_Error() {
//		int result = advisorDaoImpl.removeAdvBrandRankByAdvId("ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test //join query
//	// // public void test_fetchAllServiceAndBrand_Success() {
//	// // List<Product> products = advisorDaoImpl.fetchAllServiceAndBrand();
//	// // Assert.assertEquals(1, products.size());
//	// // }
//	//
//	@Test
//	public void test_fetchAwardByadvId_Success() {
//		List<Award> awards = advisorDaoImpl.fetchAwardByadvId("ADV000000000A", "N");
//		Assert.assertEquals(1, awards.size());
//	}
//
//	@Test
//	public void test_fetchAwardByadvId_Error() {
//		List<Award> awards = advisorDaoImpl.fetchAwardByadvId("ADV000000000U", "N");
//		Assert.assertEquals(0, awards.size());
//	}
//
//	@Test
//	public void test_fetchCertificateByadvId_Success() {
//		List<Certificate> certificates = advisorDaoImpl.fetchCertificateByadvId("ADV000000000A", "N");
//		Assert.assertEquals(1, certificates.size());
//	}
//
//	@Test
//	public void test_fetchCertificateByadvId_Error() {
//		List<Certificate> certificates = advisorDaoImpl.fetchCertificateByadvId("ADV000000000U", "N");
//		Assert.assertEquals(0, certificates.size());
//	}
//
//	@Test
//	public void test_fetchExperienceByadvId_Success() {
//		List<Experience> experiences = advisorDaoImpl.fetchExperienceByadvId("ADV000000000A", "N");
//		Assert.assertEquals(1, experiences.size());
//	}
//
//	@Test
//	public void test_fetchExperienceByadvId_Error() {
//		List<Experience> experiences = advisorDaoImpl.fetchExperienceByadvId("ADV000000000U", "N");
//		Assert.assertEquals(0, experiences.size());
//	}
//
//	@Test
//	public void test_fetchEducationByadvId_Success() {
//		List<Education> educations = advisorDaoImpl.fetchEducationByadvId("ADV000000000A", "N");
//		Assert.assertEquals(1, educations.size());
//	}
//
//	@Test
//	public void test_fetchEducationByadvId_Error() {
//		List<Education> educations = advisorDaoImpl.fetchEducationByadvId("ADV000000000U", "N");
//		Assert.assertEquals(0, educations.size());
//	}
//
//	@Test
//	public void test_modifyAdvisorAward_Success() {
//		Award award = new Award();
//		award.setAwardId(1);
//		award.setYear("1996");
//		int result = advisorDaoImpl.modifyAdvisorAward(1, award, "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorAward_Error() {
//		Award award = new Award();
//		award.setAwardId(1);
//		award.setYear("1996");
//		int result = advisorDaoImpl.modifyAdvisorAward(1, award, "ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorCertificate_Success() {
//		Certificate certificate = new Certificate();
//		certificate.setCertificateId(1);
//		certificate.setYear("1996");
//		int result = advisorDaoImpl.modifyAdvisorCertificate(1, certificate, "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorCertificate_Error() {
//		Certificate certificate = new Certificate();
//		certificate.setCertificateId(1);
//		certificate.setYear("1996");
//		int result = advisorDaoImpl.modifyAdvisorCertificate(1, certificate, "ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorExperience_Success() {
//		Experience experience = new Experience();
//		experience.setExpId(1);
//		experience.setLocation("nellai");
//		int result = advisorDaoImpl.modifyAdvisorExperience(1, experience, "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorExperience_Error() {
//		Experience experience = new Experience();
//		experience.setExpId(1);
//		experience.setLocation("nellai");
//		int result = advisorDaoImpl.modifyAdvisorExperience(1, experience, "ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorEducation_Success() {
//		Education education = new Education();
//		education.setEduId(1);
//		education.setField("CSE");
//		int result = advisorDaoImpl.modifyAdvisorEducation(1, education, "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyAdvisorEducation_Error() {
//		Education education = new Education();
//		education.setEduId(1);
//		education.setField("CSE");
//		int result = advisorDaoImpl.modifyAdvisorEducation(1, education, "ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvAwardInfo_Success() {
//		List<Award> awards = new ArrayList<Award>();
//		Award award = new Award();
//		award.setAdvId("ADV000000000A");
//		award.setAwardId(1);
//		award.setYear("1996");
//		awards.add(award);
//		int result = advisorDaoImpl.addAdvAwardInfo("ADV000000000A", award, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvAwardInfo_Error() {
//		List<Award> awards = new ArrayList<Award>();
//		Award award = new Award();
//		award.setAdvId("ADV000000000A");
//		award.setAwardId(1);
//		award.setYear("1996");
//		awards.add(award);
//		int result = advisorDaoImpl.addAdvAwardInfo("ADV000000000U", award, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvCertificateInfo_Success() {
//		List<Certificate> certificates = new ArrayList<Certificate>();
//		Certificate certificate = new Certificate();
//		certificate.setAdvId("ADV000000000A");
//		certificate.setCertificateId(1);
//		certificate.setYear("1996");
//		certificates.add(certificate);
//		int result = advisorDaoImpl.addAdvCertificateInfo("ADV000000000A", certificate, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvCertificateInfo_Error() {
//		List<Certificate> certificates = new ArrayList<Certificate>();
//		Certificate certificate = new Certificate();
//		certificate.setAdvId("ADV000000000A");
//		certificate.setCertificateId(1);
//		certificate.setYear("1996");
//		certificates.add(certificate);
//		int result = advisorDaoImpl.addAdvCertificateInfo("ADV000000000U", certificate, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvExperienceInfo_Success() {
//		List<Experience> experiences = new ArrayList<Experience>();
//		Experience experience = new Experience();
//		experience.setAdvId("ADV000000000A");
//		experience.setExpId(1);
//		experience.setFromYear("MAR-1996");
//		experiences.add(experience);
//		int result = advisorDaoImpl.addAdvExperienceInfo("ADV000000000A", experience, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvExperienceInfo_Error() {
//		List<Experience> experiences = new ArrayList<Experience>();
//		Experience experience = new Experience();
//		experience.setAdvId("ADV000000000A");
//		experience.setExpId(1);
//		experience.setFromYear("MAR-1996");
//		experiences.add(experience);
//		int result = advisorDaoImpl.addAdvExperienceInfo("ADV000000000U", experience, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvEducationInfo_Success() {
//		List<Education> educations = new ArrayList<Education>();
//		Education education = new Education();
//		education.setAdvId("ADV000000000A");
//		education.setEduId(1);
//		education.setDegree("B.E");
//		educations.add(education);
//		int result = advisorDaoImpl.addAdvEducationInfo("ADV000000000A", education, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvEducationInfo_Error() {
//		List<Education> educations = new ArrayList<Education>();
//		Education education = new Education();
//		education.setAdvId("ADV000000000A");
//		education.setEduId(1);
//		education.setDegree("B.E");
//		educations.add(education);
//		int result = advisorDaoImpl.addAdvEducationInfo("ADV000000000U", education, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchAdvAwardByAdvIdAndAwardId_Success() {
//		Award award = advisorDaoImpl.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000A", "N");
//		Assert.assertEquals("Award", award.getTitle());
//	}
//
//	@Test
//	public void test_fetchAdvAwardByAdvIdAndAwardId_Error() {
//		Award award = advisorDaoImpl.fetchAdvAwardByAdvIdAndAwardId(1, "ADV000000000U", "N");
//		Assert.assertEquals(null, award);
//	}
//
//	@Test
//	public void test_fetchAdvCertificateByAdvIdAndCertificateId_Success() {
//		Certificate cert = advisorDaoImpl.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000A", "N");
//		Assert.assertEquals("Certificate", cert.getTitle());
//	}
//
//	@Test
//	public void test_fetchAdvCertificateByAdvIdAndCertificateId_Error() {
//		Certificate cert = advisorDaoImpl.fetchAdvCertificateByAdvIdAndCertificateId(1, "ADV000000000U", "N");
//		Assert.assertEquals(null, cert);
//	}
//
//	@Test
//	public void test_fetchAdvEducationByAdvIdAndEduId_Success() {
//		Education edu = advisorDaoImpl.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000A", "N");
//		Assert.assertEquals("IT", edu.getField());
//	}
//
//	@Test
//	public void test_fetchAdvEducationByAdvIdAndEduId_Error() {
//		Education edu = advisorDaoImpl.fetchAdvEducationByAdvIdAndEduId(1, "ADV000000000U", "N");
//		Assert.assertEquals(null, edu);
//	}
//
//	@Test
//	public void test_fetchAdvExperienceByAdvIdAndExpId_Success() {
//		Experience exp = advisorDaoImpl.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000A", "N");
//		Assert.assertEquals("engineer", exp.getDesignation());
//	}
//
//	@Test
//	public void test_fetchAdvExperienceByAdvIdAndExpId_Error() {
//		Experience exp = advisorDaoImpl.fetchAdvExperienceByAdvIdAndExpId(1, "ADV000000000U", "N");
//		Assert.assertEquals(null, exp);
//	}
//
//	@Test
//	public void test_removeAwardByAdvId_Success() {
//		int result = advisorDaoImpl.removeAwardByAdvId("ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeAwardByAdvId_Error() {
//		int result = advisorDaoImpl.removeAwardByAdvId("ADV000000000U", "Y", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeCertificateByAdvId_Success() {
//		int result = advisorDaoImpl.removeCertificateByAdvId("ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeCertificateByAdvId_Error() {
//		int result = advisorDaoImpl.removeCertificateByAdvId("ADV000000000U", "Y", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeExperienceByAdvId_Success() {
//		int result = advisorDaoImpl.removeExperienceByAdvId("ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeExperienceByAdvId_Error() {
//		int result = advisorDaoImpl.removeExperienceByAdvId("ADV000000000U", "Y", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removeEducationByAdvId_Success() {
//		int result = advisorDaoImpl.removeEducationByAdvId("ADV000000000A", "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removeEducationByAdvId_Error() {
//		int result = advisorDaoImpl.removeEducationByAdvId("ADV000000000U", "Y", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchAllStateCityPincode() {
//		List<State> states = advisorDaoImpl.fetchAllStateCityPincode();
//		Assert.assertEquals(1, states.size());
//		Assert.assertEquals(2, states.get(0).getCities().size());
//
//	}
//
//	@Test
//	public void test_fetchAdvBrandRankByAdvId_Success() {
//		List<AdvBrandRank> advBrandRank = advisorDaoImpl.fetchAdvBrandRankByAdvId("ADV000000000A", "N");
//		Assert.assertEquals(2, advBrandRank.size());
//	}
//
//	@Test
//	public void test_fetchAdvBrandRankByAdvId_Error() {
//		List<AdvBrandRank> advBrandRank = advisorDaoImpl.fetchAdvBrandRankByAdvId("ADV000000000U", "N");
//		Assert.assertEquals(0, advBrandRank.size());
//	}
//
//	@Test
//	public void test_fetchPartyIdByRoleBasedId_Success() {
//		long result = advisorDaoImpl.fetchPartyIdByRoleBasedId("ADV000000000A", "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchPartyIdByRoleBasedId_Error() {
//		long result = advisorDaoImpl.fetchPartyIdByRoleBasedId("ADV000000000U", "N");
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test
//	// // public void test_removeAdvisor_Success() {
//	// // long result = advisorDaoImpl.removeAdvisor("ADV000000000A", "Y", 1);
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_removePublicAdvisor_Success() {
//	// // long result =advisorDaoImpl.removePublicAdvisorDeleteflag("ADV000000000A",
//	// // "Y", 1);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_fetchArticleStatusList_Success() {
//		List<ArticleStatus> advBrandRank = advisorDaoImpl.fetchArticleStatusList();
//		Assert.assertEquals(1, advBrandRank.size());
//	}
//
//	@Test
//	public void test_fetchPartyStatusIdByDesc_Success() {
//		String desc = "active";
//		long id = advisorDaoImpl.fetchPartyStatusIdByDesc(desc);
//		Assert.assertEquals(1, id);
//
//	}
//
//	@Test
//	public void test_fetchPartyStatusIdByDesc_Error() {
//		String desc = "active_error";
//		long id = advisorDaoImpl.fetchPartyStatusIdByDesc(desc);
//		Assert.assertEquals(0, id);
//
//	}
//
//	// // @Test // Encode decode
//	// // public void test_addInvestor_Success() {
//	// // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// //
//	// // Investor inv = new Investor();
//	// // inv.setInvId("INV000000000B");
//	// // inv.setEmailId("inv@gmail.com");
//	// // inv.setFullName("investor");
//	// // inv.setDisplayName("inv");
//	// // inv.setDob("07-07-2000");
//	// // inv.setGender("m");
//	// // inv.setPassword("!@AS12as");
//	// // inv.setPhoneNumber("1234567890");
//	// // inv.setPincode("123456");
//	// // inv.setCreated(timestamp);
//	// // inv.setUpdated(timestamp);
//	// // inv.setPartyStatusId(1);
//	// // String encryptPass = "Sowise@Ever21";
//	// // String delete_flag = "N";
//	// // int result = advisorDaoImpl.addInv(inv, delete_flag,encryptPass);
//	// // Assert.assertEquals(1, result);
//	// //
//	// // }
//
//	@Test
//	public void test_addInvestor_Error() {
//		Investor inv = new Investor();
//		String delete_flag = "N";
//		String encryptPass = "Sowise@Ever21";
//		int result = advisorDaoImpl.addInv(inv, delete_flag, encryptPass);
//		Assert.assertEquals(0, result);
//
//	}
//
//	// // @Test // Encode decode
//	// // public void test_addPartyInv_Success() {
//	// // String delete_flag = "N";
//	// // long roleId = 2;
//	// // Investor inv = new Investor();
//	// // inv.setEmailId("inv@gmail.com");
//	// // inv.setPassword("!@AS12as");
//	// // inv.setPartyStatusId(1);
//	// // inv.setInvId("INV000000000A");
//	// // String encryptPass = "Sowise@Ever21";
//	// // int result = advisorDaoImpl.addPartyInv(inv, roleId,
//	// // delete_flag,encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_addPartyInv_Error() {
//		String delete_flag = "N";
//		long roleId = 2;
//		Investor inv = new Investor();
//		// inv.setEmailId("inv@gmail.com");
//		// inv.setPassword("!@AS12as");
//		// inv.setPartyStatusId(1);
//		// inv.setInvId("INV000000000A");
//		String encryptPass = "Sowise@Ever21";
//		int result = advisorDaoImpl.addPartyInv(inv, delete_flag, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test // Encode decode
//	// // public void test_fetchPartyByEmailId_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Party party
//	// // =advisorDaoImpl.fetchPartyByEmailId("adv@adv.com",encryptPass);
//	// // Assert.assertEquals("adv@adv.com", party.getEmailId());
//	// // }
//	//
//	// // @Test // Encode decode
//	// // public void test_fetchPartyByEmailId_Error() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Party party = advisorDaoImpl.fetchPartyByEmailId("aaa",encryptPass);
//	// // Assert.assertEquals(null, party);
//	// // }
//
//	@Test
//	public void test_fetchInvestorSmartId_Success() {
//		String invId = advisorDaoImpl.fetchInvestorSmartId();
//		Assert.assertEquals("INV000000000A", invId);
//	}
//
//	@Test
//	public void test_fetchInvestorSmartId_Error() {
//		String invId = advisorDaoImpl.fetchInvestorSmartId();
//		Assert.assertEquals("INV000000000A", invId);
//
//	}
//
//	@Test
//	public void test_addInvSmartId_Success() {
//		int result = advisorDaoImpl.addInvSmartId("INV000000000B");
//		Assert.assertEquals(1, result);
//	}
//
//	// // @Test
//	// // public void test_addInvSmartId_Error() {
//	// // int result = advisorDaoImpl.addInvSmartId("aaa");
//	// // Assert.assertEquals(0, result);
//	// // }
//	//
//	// // @Test // Encode decode
//	// // public void test_fetchPartyByRoleBasedId_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Party party = advisorDaoImpl.fetchPartyByRoleBasedId("ADV000000000A",
//	// // "N",encryptPass);
//	// // Assert.assertEquals("N", party.getDelete_flag());
//	// // }
//	//
//	// // @Test // Encode decode
//	// // public void test_fetchPartyByRoleBasedId_Error() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Party party = advisorDaoImpl.fetchPartyByRoleBasedId("INV000000000A",
//	// // "Y",encryptPass);
//	// // // Assert.assertEquals("N", party.getDelete_flag());
//	// // }
//	//
//	@Test
//	public void test_changeInvPassword_Success() {
//		int result = advisorDaoImpl.changeInvPassword("INV000000000A", "!@AS12as");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_changeInvPassword_Error() {
//		int result = advisorDaoImpl.changeInvPassword("INV0000000000", "!@AS12as");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_changePartyPassword_Success() {
//		int result = advisorDaoImpl.changeInvPassword("INV000000000A", "!@QW12qw");
//		Assert.assertEquals(1, result);
//	}
//
//	// @Test
//	// public void test_changePartyPassword_Error() {
//	// int result = advisorDaoImpl.changeInvPassword("INV0000000000", "!@AS12as");
//	// Assert.assertEquals(0, result);
//	// }
//	//
//	// // @Test //Encode decode
//	// // public void test_addKeyPeople_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // KeyPeople key = new KeyPeople();
//	// // key.setFullName("david");
//	// // key.setDesignation("backend");
//	// // key.setImage("aaa.jpg");
//	// // key.setParentPartyId(2);
//	// // int result = advisorDaoImpl.addKeyPeople(key, "N",encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	// @Test
//	// public void test_addKeyPeople_Error() {
//	// String encryptPass = "Sowise@Ever21";
//	// KeyPeople key = new KeyPeople();
//	// int result = advisorDaoImpl.addKeyPeople(key, "aaa", encryptPass);
//	// Assert.assertEquals(0, result);
//	// }
//	//
//	// // @Test // Encode decode
//	// // public void test_fetchKeyPeopleByParentId_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // List<KeyPeople> result = advisorDaoImpl.fetchKeyPeopleByParentId(1,
//	// // "N",encryptPass);
//	// // Assert.assertEquals(1, result.size());
//	// // }
//	// //
//	// // @Test // Encode decode
//	// // public void test_fetchKeyPeopleByParentId_Error() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // List<KeyPeople> result = advisorDaoImpl.fetchKeyPeopleByParentId(2,
//	// // "N",encryptPass);
//	// // Assert.assertEquals(0, result.size());
//	// // }
//	//
////	@Test
////	public void test_teamMemberDeactive_Success() {
////		long result = advisorDaoImpl.teamMemberDeactive("ADV000000000B", "Y", "ADV000000000A", 1);
////		Assert.assertEquals(1, result);
////	}
//
//	@Test
//	public void test_teamMemberDeactive_Error() {
//		long result = advisorDaoImpl.teamMemberDeactive("null", "N", "ADV000000000A", 0);
//		Assert.assertEquals(0, result);
//	}
//
//	//
//	// // @Test
//	// // public void test_addWorkFlowStatusApprovedByAdvId_Success() {
//	// // int result =
//	// advisorDaoImpl.addWorkFlowStatusApprovedByAdvId("ADV000000000A",
//	// // 4, "ADV000000000A");
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_addWorkFlowStatusApprovedByAdvId_Error() {
//	// // int result = advisorDaoImpl.addWorkFlowStatusApprovedByAdvId("null", 4,
//	// // "ADV000000000A");
//	// // Assert.assertEquals(0, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_addWorkFlowStatusRevokedByAdvId_Success() {
//	// // int result =
//	// advisorDaoImpl.addWorkFlowStatusRevokedByAdvId("ADV000000000A",
//	// // 6, "ADV000000000B");
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_addWorkFlowStatusRevokedByAdvId_Error() {
//	// // int result = advisorDaoImpl.addWorkFlowStatusRevokedByAdvId("null", 6,
//	// // "ADV000000000A");
//	// // Assert.assertEquals(0, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_addWorkFlowStatusByAdvId_Success() {
//	// // int result = advisorDaoImpl.addWorkFlowStatusByAdvId("ADV000000000A", 5,
//	// // "ADV000000000B");
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_addWorkFlowStatusByAdvId_Error() {
//	// // int result = advisorDaoImpl.addWorkFlowStatusByAdvId("null", 0,
//	// // "ADV000000000A");
//	// // Assert.assertEquals(0, result);
//	// // }
//	//
//	@Test
//	public void test_fetchWorkFlowStatusIdByDesc_Success() {
//		int workFlowStatusId = advisorDaoImpl.fetchWorkFlowStatusIdByDesc("default");
//		Assert.assertEquals(1, workFlowStatusId);
//
//	}
//
//	@Test
//	public void test_fetchWorkFlowStatusIdByDesc_Error() {
//		int workFlowStatusId = advisorDaoImpl.fetchWorkFlowStatusIdByDesc("null");
//		Assert.assertEquals(0, workFlowStatusId);
//
//	}
//
//	//
//	// // @Test // Encode decode
//	// // public void test_advPublicAdvisor_Success() {
//	// // String encryptPass = "Sowise@Ever21";
//	// // Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// // Advisor advisor = new Advisor();
//	// // advisor.setAdvId("ADV000000000C");
//	// // advisor.setName("adv");
//	// // advisor.setDesignation("Eng");
//	// // advisor.setEmailId("adv@adv.com");
//	// // advisor.setPassword("!@AS12as");
//	// // advisor.setPhoneNumber("9876541230");
//	// // advisor.setPartyStatusId(1);
//	// // advisor.setCreated(timestamp);
//	// // advisor.setUpdated(timestamp);
//	// // advisor.setDelete_flag("N");
//	// // advisor.setAdvType(1);
//	// // int result = advisorDaoImpl.addPublicAdvisor(advisor,encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_advPublicAdvisor_Error() {
//		String encryptPass = "Sowise@Ever21";
//		Advisor advisor = new Advisor();
//		int result = advisorDaoImpl.addPublicAdvisor(advisor, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvPublicAwardInfo_Success() {
//		List<Award> awards = new ArrayList<Award>();
//		Award award = new Award();
//		award.setAdvId("ADV000000000A");
//		award.setAwardId(1);
//		award.setYear("1996");
//		awards.add(award);
//		int result = advisorDaoImpl.addAdvPublicAwardInfo("ADV000000000A", award, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvPublicAwardInfo_Error() {
//		List<Award> awards = new ArrayList<Award>();
//		Award award = new Award();
//		award.setAdvId("ADV000000000A");
//		award.setAwardId(1);
//		award.setYear("1996");
//		awards.add(award);
//		int result = advisorDaoImpl.addAdvPublicAwardInfo("ADV000000000U", award, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvPublicCertificateInfo_Success() {
//		List<Certificate> certificates = new ArrayList<Certificate>();
//		Certificate certificate = new Certificate();
//		certificate.setAdvId("ADV000000000A");
//		certificate.setCertificateId(1);
//		certificate.setYear("1996");
//		certificates.add(certificate);
//		int result = advisorDaoImpl.addAdvPublicCertificateInfo("ADV000000000A", certificate, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvPublicCertificateInfo_Error() {
//		List<Certificate> certificates = new ArrayList<Certificate>();
//		Certificate certificate = new Certificate();
//		certificate.setAdvId("ADV000000000A");
//		certificate.setCertificateId(1);
//		certificate.setYear("1996");
//		certificates.add(certificate);
//		int result = advisorDaoImpl.addAdvPublicCertificateInfo("ADV000000000U", certificate, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvPublicExperienceInfo_Success() {
//		List<Experience> experiences = new ArrayList<Experience>();
//		Experience experience = new Experience();
//		experience.setAdvId("ADV000000000A");
//		experience.setExpId(1);
//		experience.setFromYear("MAR-1996");
//		experiences.add(experience);
//		int result = advisorDaoImpl.addAdvExperienceInfo("ADV000000000A", experience, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvPublicExperienceInfo_Error() {
//		List<Experience> experiences = new ArrayList<Experience>();
//		Experience experience = new Experience();
//		experience.setAdvId("ADV000000000A");
//		experience.setExpId(1);
//		experience.setFromYear("MAR-1996");
//		experiences.add(experience);
//		int result = advisorDaoImpl.addAdvExperienceInfo("ADV000000000U", experience, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addAdvPublicEducationInfo_Success() {
//		List<Education> educations = new ArrayList<Education>();
//		Education education = new Education();
//		education.setAdvId("ADV000000000A");
//		education.setEduId(1);
//		education.setDegree("B.E");
//		educations.add(education);
//		int result = advisorDaoImpl.addAdvPublicEducationInfo("ADV000000000A", education, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addAdvPublicEducationInfo_Error() {
//		List<Education> educations = new ArrayList<Education>();
//		Education education = new Education();
//		education.setAdvId("ADV000000000A");
//		education.setEduId(1);
//		education.setDegree("B.E");
//		educations.add(education);
//		int result = advisorDaoImpl.addAdvPublicEducationInfo("ADV000000000U", education, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addPublicAdvProductInfo_Success() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("advisor");
//		advisor.setDesignation("Engineer");
//		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
//		AdvProduct advProduct = new AdvProduct();
//		advProduct.setProdId(1);
//		advProducts.add(advProduct);
//		advisor.setAdvProducts(advProducts);
//		int result = advisorDaoImpl.addPublicAdvProductInfo("ADV000000000A", advProduct, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addPublicAdvProductInfo_Error() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("advisor");
//		advisor.setDesignation("Engineer");
//		List<AdvProduct> advProducts = new ArrayList<AdvProduct>();
//		AdvProduct advProduct = new AdvProduct();
//		advProduct.setProdId(1);
//		advProducts.add(advProduct);
//		advisor.setAdvProducts(advProducts);
//		int result = advisorDaoImpl.addPublicAdvProductInfo("ADV", advProduct, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addPublicAdvBrandInfo_Success() {
//		AdvBrandInfo advBrandInfo = new AdvBrandInfo();
//		advBrandInfo.setAdvId("ADV000000000A");
//		advBrandInfo.setBrandId(1);
//		advBrandInfo.setProdId(2);
//		int result = advisorDaoImpl.addPublicAdvBrandInfo("ADV000000000A", advBrandInfo, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addPublicAdvBrandInfo_Error() {
//		AdvBrandInfo advBrandInfo = new AdvBrandInfo();
//		advBrandInfo.setAdvId("ADV000000000A");
//		advBrandInfo.setBrandId(1);
//		advBrandInfo.setProdId(2);
//		int result = advisorDaoImpl.addPublicAdvBrandInfo("ADV000000000U", advBrandInfo, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addPublicAdvBrandAndRank_Success() {
//		AdvBrandRank advBrandRank = new AdvBrandRank();
//		advBrandRank.setAdvId("ADV000000000A");
//		advBrandRank.setBrandId(1);
//		advBrandRank.setProdId(2);
//		int result = advisorDaoImpl.addPublicAdvBrandAndRank(1, 1, "ADV000000000A", 1, "N", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addPublicAdvBrandAndRank_Error() {
//		AdvBrandRank advBrandRank = new AdvBrandRank();
//		advBrandRank.setAdvId("ADV000000000A");
//		advBrandRank.setBrandId(1);
//		advBrandRank.setProdId(2);
//		int result = advisorDaoImpl.addPublicAdvBrandAndRank(1, 1, "ADV000000000U", 1, "N", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addPromotion_Success() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("adv");
//		Promotion promo = new Promotion();
//		promo.setTitle("advisorVDO");
//		int result = advisorDaoImpl.addPromotion("ADV000000000A", promo, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addPromotion_Error() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000Z");
//		advisor.setName("adv");
//		Promotion promo = new Promotion();
//		int result = advisorDaoImpl.addPromotion("ADV000000000Z", promo, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_addPublicPromotion_Success() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("adv");
//		Promotion promo = new Promotion();
//		promo.setTitle("advisorVDO");
//		int result = advisorDaoImpl.addPublicPromotion("ADV000000000A", promo, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addPublicPromotion_Error() {
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000Z");
//		advisor.setName("adv");
//		Promotion promo = new Promotion();
//		int result = advisorDaoImpl.addPublicPromotion("ADV000000000Z", promo, "N");
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test
//	// // public void test_removePublicAdvisorChild_Success() {
//	// // int result = advisorDaoImpl.removePublicAdvisorChild("ADV000000000A");
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_removePublicAdvisorChild_Error() {
//		int result = advisorDaoImpl.removePublicAdvisorChild("ADV000000000U");
//		Assert.assertEquals(0, result);
//	}
//
//	//
//	// // @Test // Encode decode
//	// // public void test_addPublicAdvPersonalInfo_Success() {
//	// // Advisor adv = new Advisor();
//	// // adv.setAdvId("ADV000000000A");
//	// // adv.setDisplayName("advisor96");
//	// // adv.setPincode("654123");
//	// // String encryptPass = "Sowise@Ever21";
//	// // int result = advisorDaoImpl.addPublicAdvPersonalInfo("ADV000000000A",
//	// // adv,encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	@Test
//	public void test_addPublicAdvPersonalInfo_Error() {
//		String encryptPass = "Sowise@Ever21";
//		Advisor adv = new Advisor();
//		adv.setAdvId("ADV000000000A");
//		adv.setDisplayName("advisor96");
//		adv.setPincode("654123");
//		int result = advisorDaoImpl.addPublicAdvPersonalInfo("ADV", adv, encryptPass);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchFollowersByUserId_Success() {
//		List<Followers> followers = advisorDaoImpl.fetchFollowersByUserId("ADV000000000B", 1L);
//		Assert.assertEquals(1, followers.size());
//	}
//
//	@Test
//	public void test_fetchFollowersByUserId_Error() {
//		List<Followers> followers = advisorDaoImpl.fetchFollowersByUserId("ADV000000000A", 2L);
//		Assert.assertEquals(0, followers.size());
//	}
//
//	// // @Test
//	// // public void test_addFollowers_Success() {
//	// // Timestamp timestamp = new Timestamp(0);
//	// // Followers followers = new Followers();
//	// // followers.setAdvId("ADV000000000B");
//	// // followers.setStatus(1);
//	// // followers.setUserType(1);
//	// // followers.setUserId("INV000000000V");
//	// // followers.setCreated(timestamp);
//	// // followers.setUpdated(timestamp);
//	// // int result = advisorDaoImpl.addFollowers(followers);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//	// // @Test
//	// // public void test_addFollowers_Error() {
//	// // Followers followers = new Followers();
//	// // int result = advisorDaoImpl.addFollowers(followers);
//	// // Assert.assertEquals(0, result);
//	// // }
//	//
//	// @Test
//	// public void test_fetchFollowersByFollowersId_Success() {
//	// Followers result = advisorDaoImpl.fetchFollowersByFollowersId(1, 1);
//	// Assert.assertEquals(1, result.getStatus());
//	// }
//	//
//	@Test
//	public void test_fetchFollowersByFollowersId_Error() {
//		Followers result = advisorDaoImpl.fetchFollowersByFollowersId(3, 1);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchFollowerStatusIdByDesc_Success() {
//		long result = advisorDaoImpl.fetchFollowerStatusIdByDesc("active");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchFollowerStatusIdByDesc_Error() {
//		long result = advisorDaoImpl.fetchFollowerStatusIdByDesc("inactive");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchUserTypeIdByDesc_Success() {
//		long result = advisorDaoImpl.fetchUserTypeIdByDesc("advisor");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchUserTypeIdByDesc_Error() {
//		long result = advisorDaoImpl.fetchUserTypeIdByDesc("inv");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchFollowersListByUserId_Success() {
//		List<Followers> result = advisorDaoImpl.fetchFollowersListByUserId("ADV000000000B");
//		Assert.assertEquals(1, result.size());
//	}
//
//	@Test
//	public void test_fetchFollowersListByUserId_Error() {
//		List<Followers> result = advisorDaoImpl.fetchFollowersListByUserId("ADV000000000N");
//		Assert.assertEquals(0, result.size());
//	}
//
//	@Test
//	public void test_fetchFollowersCount_Success() {
//		List<Integer> followersCount = advisorDaoImpl.fetchFollowersCount("ADV000000000A", 1);
//		Assert.assertEquals(2, followersCount.size());
//	}
//
//	@Test
//	public void test_fetchFollowersCount_Error() {
//		List<Integer> followersCount = advisorDaoImpl.fetchFollowersCount("ADV000000000A", 3);
//		Assert.assertEquals(0, followersCount.size());
//	}
//
//	@Test
//	public void test_fetchFollowersId_Success() {
//		List<String> followersId = advisorDaoImpl.fetchFollowersId("ADV000000000A", 1, 1);
//		Assert.assertEquals(1, followersId.size());
//	}
//
//	@Test
//	public void test_fetchFollowersId_Error() {
//		List<String> followersId = advisorDaoImpl.fetchFollowersId("ADV000000000A", 1, 3);
//		Assert.assertEquals(0, followersId.size());
//	}
//
//	@Test
//	public void test_fetchPromotionByAdvId_Success() {
//		List<Promotion> promo = advisorDaoImpl.fetchPromotionByAdvId("ADV000000000A", "N");
//		Assert.assertEquals(1, promo.size());
//	}
//
//	@Test
//	public void test_fetchPromotionByAdvId_Error() {
//		List<Promotion> promo = advisorDaoImpl.fetchPromotionByAdvId("ADV000000000A", "NO");
//		Assert.assertEquals(0, promo.size());
//	}
//
//	@Test
//	public void test_fetchPromotionByAdvIdAndPromotionId_Success() {
//		Promotion promo = advisorDaoImpl.fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", "N");
//		Assert.assertEquals(1, promo.getPromotionId());
//	}
//
//	@Test
//	public void test_fetchPromotionByAdvIdAndPromotionId_Error() {
//		Promotion promo = advisorDaoImpl.fetchPromotionByAdvIdAndPromotionId(1, "ADV000000000A", "NO");
//		Assert.assertEquals(null, promo);
//	}
//
//	@Test
//	public void test_modifyPromotion_Success() {
//		Promotion promotion = new Promotion();
//		promotion.setTitle("promotion");
//		promotion.setPromotionId(1);
//		promotion.setAdvId("ADV000000000A");
//		promotion.setAboutVideo("This is my promotion details");
//		promotion.setDeleteflag("N");
//		promotion.setImagePath("promo.jpg");
//		int result = advisorDaoImpl.modifyPromotion(1, promotion, "ADV000000000A", "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyPromotion_Error() {
//		Promotion promotion = new Promotion();
//		promotion.setTitle("promotion");
//		promotion.setPromotionId(1);
//		promotion.setAdvId("ADV000000000A");
//		promotion.setAboutVideo("This is my promotion details");
//		promotion.setDeleteflag("N");
//		promotion.setImagePath("promo.jpg");
//		int result = advisorDaoImpl.modifyPromotion(1, promotion, "ADV000000000A", "NO");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_removePromotion_Success() {
//		int result = advisorDaoImpl.removePromotion(1L, "Y", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_removePromotion_Error() {
//		int result = advisorDaoImpl.removePromotion(1L, "YO", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	//
//	// // public void test_fetchPartyByPartyId_Success() { //Encode//decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party party = advisorDaoImpl.fetchPartyByPartyId(1, "N", encryptPass);
//	// // Assert.assertEquals(1, promo.getPromotionId());
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchPartyByPartyId_Error() { //Encode//decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party party = advisorDaoImpl.fetchPartyByPartyId(1, "NO", encryptPass);
//	// // Assert.assertEquals(null, party);
//	// // }
//	//
//	// // TODO
//	// // @Test
//	// // public void test_fetchRoleByRoleId_Success() {
//	// // String result = advisorDaoImpl.fetchRoleByRoleId(1);
//	// // Assert.assertEquals("advisor", result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchRoleByRoleId_Error() {
//	// // String result = advisorDaoImpl.fetchRoleByRoleId(8);
//	// // Assert.assertEquals(null, result);
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchInvestorByInvId_Success() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Investor result = advisorDaoImpl.fetchInvestorByInvId("ADV000000000A","N",
//	// // encryptPass);
//	// // Assert.assertEquals("advisor", result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchInvestorByInvId_Error() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Investor result =
//	// // advisorDaoImpl.fetchInvestorByInvId("ADV000000000A","NO",
//	// // encryptPass);
//	// // Assert.assertEquals(null, result);
//	// // }
//	//
//	@Test
//	public void test_updateAdvisorAccountAsVerified_Success() {
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000A");
//		advisor.setName("adv");
//		advisor.setDesignation("Eng");
//		advisor.setEmailId("adv@adv.com");
//		advisor.setPassword("!@AS12as");
//		advisor.setPhoneNumber("9876541230");
//		advisor.setPartyStatusId(10);
//		advisor.setCreated(timestamp);
//		advisor.setUpdated(timestamp);
//		advisor.setDelete_flag("N");
//		advisor.setAdvType(1);
//		advisor.setIsVerified(1);
//		advisor.setVerified(timestamp);
//		int result = advisorDaoImpl.updateAdvisorAccountAsVerified("ADV000000000A", 1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_updateAdvisorAccountAsVerified_Error() {
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		Advisor advisor = new Advisor();
//		advisor.setAdvId("ADV000000000C");
//		advisor.setName("adv");
//		advisor.setDesignation("Eng");
//		advisor.setEmailId("adv@adv.com");
//		advisor.setPassword("!@AS12as");
//		advisor.setPhoneNumber("9876541230");
//		advisor.setPartyStatusId(10);
//		advisor.setCreated(timestamp);
//		advisor.setUpdated(timestamp);
//		advisor.setDelete_flag("N");
//		advisor.setAdvType(1);
//		advisor.setIsVerified(1);
//		advisor.setVerified(timestamp);
//		int result = advisorDaoImpl.updateAdvisorAccountAsVerified("ADV000000000C", 5);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_updateInvestorAccountAsVerified_Success() {
//		int result = advisorDaoImpl.updateInvestorAccountAsVerified("INV000000000A", 1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_updateInvestorAccountAsVerified_Error() {
//		int result = advisorDaoImpl.updateInvestorAccountAsVerified("INV000000000K", 5);
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test
//	// // public void test_blockFollowers_Success() {
//	// // int result = advisorDaoImpl.blockFollowers(1, 2);
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_blockFollowers_Error() {
//	// // int result = advisorDaoImpl.blockFollowers(3, 3);
//	// // Assert.assertEquals(0, result);
//	// // }
//
//	@Test
//	public void test_fetchWorkFlowStatusList_Success() {
//		List<WorkFlowStatus> result = advisorDaoImpl.fetchWorkFlowStatusList();
//		Assert.assertEquals(4, result.size());
//	}
//
//	@Test
//	public void test_fetchFollowerStatusList_Success() {
//		List<FollowerStatus> result = advisorDaoImpl.fetchFollowerStatusList();
//		Assert.assertEquals(1, result.size());
//	}
//
//	//
//	// // @Test
//	// // public void test_fetchExploreAdvisorList_Success() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // List<Advisor> advisors = advisorDaoImpl.fetchExploreAdvisorList(1, 3,
//	// // "tamilnadu", "chennai", "627002", "adv",
//	// // "N", encryptPass);
//	// // Assert.assertEquals(1, advisors.size());
//	// // }
//	//
//	// // @Test //join query
//	// // public void test_fetchExploreProductList_Success() {
//	// // List<Product> product = advisorDaoImpl.fetchExploreProductList("Mutual
//	// // funds", "GoalPlanning","Aditya Birla Sun Life");
//	// // Assert.assertEquals(1, product.size());
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchExploreAdvisorByProduct_Success() { // Encode
//	// // Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // List<Advisor> advisors = advisorDaoImpl.fetchExploreAdvisorByProduct(1, 3,
//	// // "tamilnadu", "chennai", "627002", "adv1", "1", "N", encryptPass);
//	// // Assert.assertEquals(1, advisors.size());
//	// // }
//	//
//	@Test
//	public void test_fetchServicePlanByServiceId_Success() {
//		List<ServicePlan> servicePlans = advisorDaoImpl.fetchServicePlanByServiceId(1);
//		Assert.assertEquals(1, servicePlans.size());
//	}
//
//	@Test
//	public void test_fetchServicePlanByServiceId_Error() {
//		List<ServicePlan> servicePlans = advisorDaoImpl.fetchServicePlanByServiceId(4);
//		Assert.assertEquals(0, servicePlans.size());
//	}
//
//	@Test
//	public void test_fetchFollowersByUserIdWithAdvId_Success() {
//		Followers result = advisorDaoImpl.fetchFollowersByUserIdWithAdvId("ADV000000000A", "ADV000000000B");
//		Assert.assertEquals(1, result.getStatus());
//	}
//
//	@Test
//	public void test_fetchFollowersByUserIdWithAdvId_Error() {
//		Followers result = advisorDaoImpl.fetchFollowersByUserIdWithAdvId("ADV000000000A", "ADV000000000A");
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchUserTypeList_Success() {
//		List<UserType> userType = advisorDaoImpl.fetchUserTypeList();
//		Assert.assertEquals(2, userType.size());
//	}
//
//	@Test
//	public void test_fetchAdvisorTypeList_Success() {
//		List<AdvisorType> advisorType = advisorDaoImpl.fetchAdvisorTypeList();
//		Assert.assertEquals(2, advisorType.size());
//	}
//
//	@Test
//	public void test_updateFollowers_Success() {
//		int result = advisorDaoImpl.updateFollowers(1, 1, "ADV000000000A", "ADV000000000A");
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void test_updateFollowers_Error() {
//		int result = advisorDaoImpl.updateFollowers(10, 1, "null", "ADV000000000A");
//		Assert.assertEquals(0, result);
//
//	}
//
//	@Test
//	public void test_fetchChatUserListByUserId_Success() {
//		List<ChatUser> result = advisorDaoImpl.fetchChatUserListByUserId("INV000000000A");
//		Assert.assertEquals(1, result.size());
//
//	}
//
//	@Test
//	public void test_fetchChatUserListByUserId_Error() {
//		List<ChatUser> result = advisorDaoImpl.fetchChatUserListByUserId("ADV000000000B");
//		Assert.assertEquals(0, result.size());
//
//	}
//
//	@Test
//	public void test_addChatUser_Success() {
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		ChatUser chatUser = new ChatUser();
//		chatUser.setAdvId("ADV000000000A");
//		chatUser.setUserId("ADV000000000B");
//		chatUser.setStatus(1);
//		chatUser.setUserType(1);
//		chatUser.setByWhom("ADV000000000A");
//		chatUser.setCreated(timestamp);
//		chatUser.setUpdated(timestamp);
//		chatUser.setCreatedBy("ADV000000000A");
//		chatUser.setUpdatedBy("ADV000000000A");
//		int result = advisorDaoImpl.addChatUser(chatUser);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchChatUserByUserIdWithAdvIdWithStatus_Success() {
//		ChatUser result = advisorDaoImpl.fetchChatUserByUserIdWithAdvId("ADV000000000A", "INV000000000A", 1);
//		Assert.assertEquals(1, result.getStatus());
//	}
//
//	@Test
//	public void test_fetchChatUserByUserIdWithAdvIdWithStatus_Error() {
//		ChatUser result = advisorDaoImpl.fetchChatUserByUserIdWithAdvId("ADV000000000A", "ADV000000000A", 1);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_modifyChat_Success() {
//		int result = advisorDaoImpl.modifyChat(1, 1, "ADV000000000B", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_modifyChat_Error() {
//		int result = advisorDaoImpl.modifyChat(10, 1, "ADV000000000B", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchFollowersByUserIdWithAdvIdWithStatus_Success() {
//		Followers result = advisorDaoImpl.fetchFollowersByUserIdWithAdvId("ADV000000000A", "ADV000000000B", 1);
//		Assert.assertEquals(1, result.getStatus());
//	}
//
//	@Test
//	public void test_fetchFollowersByUserIdWithAdvIdWithStatus_Error() {
//		Followers result = advisorDaoImpl.fetchFollowersByUserIdWithAdvId("ADV000000000A", "ADV000000000A", 1);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_updateChatUser_Success() {
//		int result = advisorDaoImpl.updateChatUser(1, 1, "ADV000000000A", "ADV000000000A");
//		Assert.assertEquals(1, result);
//
//	}
//
//	@Test
//	public void test_updateChatUser_Error() {
//		int result = advisorDaoImpl.updateChatUser(10, 1, "null", "ADV000000000A");
//		Assert.assertEquals(0, result);
//
//	}
//
//	// // @Test
//	// // public void test_fetchPartyByPhoneNumber_Success() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party result = advisorDaoImpl.fetchPartyByPhoneNumber("9874563210",
//	// // encryptPass);
//	// // Assert.assertEquals(1, result.getPartyId());
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchPartyByPhoneNumber_Error() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party result = advisorDaoImpl.fetchPartyByPhoneNumber("987456321052",
//	// // encryptPass);
//	// // Assert.assertEquals(null, result);
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchPartyByPAN_Success() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party result = advisorDaoImpl.fetchPartyByPAN("ABCDE1234F", encryptPass);
//	// // Assert.assertEquals("adv@adv.com", result.getEmailId());
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchPartyByPAN_Error() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party result = advisorDaoImpl.fetchPartyByPAN("ABCDE1234FER",
//	// // encryptPass);
//	// // Assert.assertEquals(null, result);
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchPartyByUserName_Success() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party result = advisorDaoImpl.fetchPartyByUserName("user", encryptPass);
//	// // Assert.assertEquals("adv@adv.com", result.getEmailId());
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchPartyByUserName_Error() { //Encode //Decode
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // Party result = advisorDaoImpl.fetchPartyByUserName("advisor",encryptPass);
//	// // Assert.assertEquals(null, result);
//	// // }
//	// // @Test
//	// // public void test_fetchAdvisorsByAdvIds_Success() {
//	// // List<String> advIdList = new ArrayList<String>();
//	// // advIdList.add("ADV000000000A");
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // List<Advisor> advisor =
//	// // advisorDaoImpl.fetchAdvisorsByAdvIds(advIdList,"N",
//	// // encryptPass);
//	// // Assert.assertEquals(1, advisor.size());
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchInvestorsByInvIds_Success() {
//	// // List<String> invIdList = new ArrayList<String>();
//	// // invIdList.add("INV000000000A");
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // List<Investor> investor = advisorDaoImpl.fetchInvestorsByInvIds(invIdList,
//	// // "N", encryptPass);
//	// // Assert.assertEquals(1, investor.size());
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchUserTypeByDesc_Success() {
//	// // long userTypeId = advisorDaoImpl.fetchUserTypeIdByDesc("advisor");
//	// // Assert.assertEquals(1, userTypeId);
//	// //
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchUserTypeByDesc_Error() {
//	// // long userTypeId = advisorDaoImpl.fetchUserTypeIdByDesc("null");
//	// // Assert.assertEquals(0, userTypeId);
//	// //
//	// // }
//	// // @Test
//	// // public void test_fetchKeyPeopleByKeyPeopleId_Success() {
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // KeyPeople keyPeople = advisorDaoImpl.fetchKeyPeopleByKeyPeopleId(1, "N",
//	// // encryptPass);
//	// // Assert.assertEquals("xyz", keyPeople.getFullName());
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchKeyPeopleByKeyPeopleId_Error() {
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // KeyPeople keyPeople = advisorDaoImpl.fetchKeyPeopleByKeyPeopleId(1,
//	// // "NO",encryptPass);
//	// // Assert.assertEquals(null, keyPeople);
//	// // }
//	//
//	// // @Test
//	// // public void test_modifyKeyPeople_Success() {
//	// // KeyPeople keyPeople = new KeyPeople();
//	// // keyPeople.setKeyPeopleId(1);
//	// // keyPeople.setFullName("XYZ");
//	// // String encryptPass =advTableFields.getEncryption_password();
//	// // int result = advisorDaoImpl.modifyKeyPeople(1, keyPeople, encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_modifyKeyPeople_Error() {
//	// // KeyPeople keyPeople = new KeyPeople();
//	// // keyPeople.setKeyPeopleId(1);
//	// // keyPeople.setFullName("XYZ");
//	// // String encryptPass =advTableFields.getEncryption_password();
//	// // int result = advisorDaoImpl.modifyKeyPeople(1, keyPeople, encryptPass);
//	// // Assert.assertEquals(0, result);
//	// // }
//	//
//	// // @Test
//	// // public void test_fetchAllPublicAdvisor_Success() {
//	// // List<Advisor> advisors = advisorDaoImpl.fetchAllPublicAdvisor(1,1,"N");
//	// // Assert.assertEquals(1, advisors.size());
//	// // }
//	// //// @Test
//	// //// public void test_checkForPasswordMatch() {
//	// //// Advisor advisor = new Advisor();
//	// //// advisor.setAdvId("ADV000000000C");
//	// //// advisor.setName("adv");
//	// //// advisor.setDesignation("Eng");
//	// //// advisor.setEmailId("adv@adv.com");
//	// //// advisor.setPassword("!@AS12as");
//	// //// advisorDaoImpl.advSignup(advisor);
//	// //// boolean result = advisorDaoImpl.checkForPasswordMatch("ADV000000000C",
//	// // "!@AS12as");
//	// //// Assert.assertEquals(true, result);
//	// //// }
//	//
//	// // @Test
//	// // public void test_fetchAdvisorByUsername() {
//	// // Advisor advisor = advisorDaoImpl.fetchAdvisorByUserName("user", "N");
//	// // Assert.assertEquals("adv", advisor.getName());
//	// // }
//	//
//	@Test
//	public void test_fetchProductNameByProdId_Success() {
//		String result = advisorDaoImpl.fetchProductNameByProdId(3);
//		Assert.assertEquals("Mutual funds", result);
//	}
//
//	@Test
//	public void test_fetchProductNameByProdId_Error() {
//		String result = advisorDaoImpl.fetchProductNameByProdId(4);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchServiceNameByProdIdAndServiceId_Success() {
//		String result = advisorDaoImpl.fetchServiceNameByProdIdAndServiceId(3, 1);
//		Assert.assertEquals("GoalPlanning", result);
//	}
//
//	@Test
//	public void test_fetchServiceNameByProdIdAndServiceId_Error() {
//		String result = advisorDaoImpl.fetchServiceNameByProdIdAndServiceId(4, 1);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchBrandNameByProdIdAndBrandId_Success() {
//		String result = advisorDaoImpl.fetchBrandNameByProdIdAndBrandId(3, 2);
//		Assert.assertEquals("axis", result);
//	}
//
//	@Test
//	public void test_fetchBrandNameByProdIdAndBrandId_Error() {
//		String result = advisorDaoImpl.fetchBrandNameByProdIdAndBrandId(4, 2);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchServicePlan_Success() {
//		ServicePlan result = advisorDaoImpl.fetchServicePlan(3, 1, 1, "Retirement Plan");
//		Assert.assertEquals("URL", result.getServicePlanLink());
//	}
//
//	@Test
//	public void test_fetchServicePlan_Error() {
//		ServicePlan result = advisorDaoImpl.fetchServicePlan(4, 1, 1, "Plan");
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_addServicePlan_Success() {
//		int result = advisorDaoImpl.addServicePlan(3, 1, 2, "plan", "url", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_addServicePlan_Error() {
//		int result = advisorDaoImpl.addServicePlan(1, 1, 5, "plan", "url", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_updateServicePlan_Success() {
//		int result = advisorDaoImpl.updateServicePlan(3, 1, 1, "Retirement Plan", "url", "ADV000000000A");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_updateServicePlan_Error() {
//		int result = advisorDaoImpl.updateServicePlan(3, 1, 5, "Retirement Plan", "url", "ADV000000000A");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_searchStateCityPincodeByCity_Success() {
//		List<CityList> result = advisorDaoImpl.searchStateCityPincodeByCity("Achampalle");
//		Assert.assertEquals(1, result.size());
//		Assert.assertEquals("Andhra Pradesh", result.get(0).getState());
//		Assert.assertEquals("1", result.get(0).getStateId());
//		Assert.assertEquals(1, result.get(0).getPincodes().size());
//	}
//
//	@Test
//	public void test_searchStateCityPincodeByCity_Error() {
//		List<CityList> result = advisorDaoImpl.searchStateCityPincodeByCity("tirunelveli");
//		Assert.assertEquals(0, result.size());
//	}
//
//	@Test
//	public void test_checkAdvisorIsPresent_Success() {
//		int result = advisorDaoImpl.checkAdvisorIsPresent("ADV000000000A", "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkAdvProductIsPresent_Success() {
//		int result = advisorDaoImpl.checkAdvProductIsPresent("ADV000000000A", 1, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkAdvBrandRankIsPresent_Success() {
//		int result = advisorDaoImpl.checkAdvBrandRankIsPresent("ADV000000000A", 1, 1, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkKeyPeopleIsPresent_Success() {
//		int result = advisorDaoImpl.checkKeyPeopleIsPresent(1, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkPartyIsPresent_Success() {
//		int result = advisorDaoImpl.checkPartyIsPresent(1, "N");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkFollowersIsPresent_Success() {
//		int result = advisorDaoImpl.checkFollowersIsPresent("ADV000000000B", "ADV000000000A", 1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkFollowersIsPresent_Error() {
//		int result = advisorDaoImpl.checkFollowersIsPresent("ADV000000000A", "ADV000000000A", 1);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_checkChatUserIsPresent_Success() {
//		int result = advisorDaoImpl.checkChatUserIsPresent("INV000000000A", 1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_checkAdvisorIsPresent_Error() {
//		int result = advisorDaoImpl.checkAdvisorIsPresent("ADV000000000A", "Y");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_checkAdvProductIsPresent_Error() {
//		int result = advisorDaoImpl.checkAdvProductIsPresent("ADV000000000A", 1, "Y");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_checkAdvBrandRankIsPresent_Error() {
//		int result = advisorDaoImpl.checkAdvBrandRankIsPresent("ADV000000000A", 1, 1, "Y");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_checkKeyPeopleIsPresent_Error() {
//		int result = advisorDaoImpl.checkKeyPeopleIsPresent(1, "Y");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_checkPartyIsPresent_Error() {
//		int result = advisorDaoImpl.checkPartyIsPresent(1, "Y");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_checkChatUserIsPresent_Error() {
//		int result = advisorDaoImpl.checkChatUserIsPresent("ADV000000000A", 1);
//		Assert.assertEquals(0, result);
//	}
//
//	// // @Test //encode decode error
//	// // public void test_addSigninVerification_Success() {
//	// // String encryptPass = advTableFields.getEncryption_password();
//	// // int result =
//	// // advisorDaoImpl.addSigninVerification("jeyanthi@sowisetech.com",
//	// // encryptPass);
//	// // Assert.assertEquals(1, result);
//	// // }
//
//	@Test
//	public void test_fetchChatUserListByAdvId_Success() {
//		List<ChatUser> chatUser = advisorDaoImpl.fetchChatUserListByAdvId("ADV000000000A");
//		Assert.assertEquals(1, chatUser.size());
//	}
//
//	@Test
//	public void test_fetchChatUserListByAdvId_Error() {
//		List<ChatUser> chatUser = advisorDaoImpl.fetchChatUserListByAdvId("ADV000000000Y");
//		Assert.assertEquals(0, chatUser.size());
//	}
//
//	@Test
//	public void test_fetchChatUser_Success() {
//		ChatUser result = advisorDaoImpl.fetchChatUser("ADV000000000A", "INV000000000A");
//		Assert.assertEquals(1, result.getStatus());
//	}
//
//	@Test
//	public void test_fetchChatUser_Error() {
//		ChatUser result = advisorDaoImpl.fetchChatUser("ADV000000000A", "INV000000000B");
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchChatUserCount_Success() {
//		List<Integer> result = advisorDaoImpl.fetchChatUserCount("ADV000000000A", 1L);
//		Assert.assertEquals(1, result.size());
//	}
//
//	@Test
//	public void test_fetchChatUserCount_Error() {
//		List<Integer> result = advisorDaoImpl.fetchChatUserCount("ADV000000000A", 10L);
//		Assert.assertEquals(0, result.size());
//	}
//
//	@Test
//	public void test_fetchFollowers_Success() {
//		List<Followers> result = advisorDaoImpl.fetchFollowers("ADV000000000A");
//		Assert.assertEquals(2, result.size());
//	}
//
//	@Test
//	public void test_fetchFollowers_Error() {
//		List<Followers> result = advisorDaoImpl.fetchFollowers("ADV000000000C");
//		Assert.assertEquals(0, result.size());
//	}
//
//	@Test
//	public void test_fetchFollowersStatus_Success() {
//		long result = advisorDaoImpl.fetchFollowersStatus("active");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchFollowersStatus_Error() {
//		long result = advisorDaoImpl.fetchFollowersStatus("aaa");
//		Assert.assertEquals(0, result);
//
//	}
////Null pointer Exception
////	@Test
////	public void test_fetchAdvisorGstByAdvId_Success() {
////		String encryptPass = advTableFields.getEncryption_password();
////		String deleteflag = advTableFields.getDelete_flag_N();
////		Advisor result = advisorDaoImpl.fetchAdvisorGstByAdvId("ADV000000000A", encryptPass, deleteflag);
////		Assert.assertEquals(10, result.getGst());
////	}
//}
