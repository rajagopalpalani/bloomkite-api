//package com.sowisetech.advisor.dao;
//
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.DirtiesContext.ClassMode;
//
//import com.sowisetech.admin.model.RoleAuth;
//import com.sowisetech.admin.model.User_role;
//import com.sowisetech.common.model.RoleFieldRights;
//import com.sowisetech.common.model.RoleScreenRights;
//
//@Transactional
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//public class AuthDaoImplTest {
//
//	AuthDaoImpl authDaoImpl;
//
//	EmbeddedDatabase db;
//
//	@Before
//	public void setup() {
//		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//				.addScript("db_sql/admauthschema.sql").addScript("db_sql/admauthdata.sql").build();
//		authDaoImpl = new AuthDaoImpl();
//		authDaoImpl.setDataSource(db);
//		authDaoImpl.postConstruct();
//	}
//
//	// @Test //result error
//	// public void test_addUser_role() {
//	// int result = authDaoImpl.addUser_role(2L, 2L, "ADM000000000B",
//	// "ADM000000000B", 1);
//	// Assert.assertEquals(1, result);
//	// }
//
//	@Test
//	public void test_fetchScreenRightsByUserRoleId() {
//		List<RoleScreenRights> roleScreenRights = authDaoImpl.fetchScreenRightsByRoleId(1);
//		Assert.assertEquals(1, roleScreenRights.size());
//	}
//
//	@Test
//	public void test_fetchScreenRightsByUserRoleIdError() {
//		List<RoleScreenRights> roleScreenRights = authDaoImpl.fetchScreenRightsByRoleId(3);
//		Assert.assertEquals(0, roleScreenRights.size());
//	}
//
//	@Test
//	public void test_fetchFieldRights() {
//		List<Integer> screenIds = new ArrayList<>();
//		screenIds.add(1);
//		List<RoleFieldRights> roleFieldRights = authDaoImpl.fetchFieldRights(screenIds);
//		Assert.assertEquals(1, roleFieldRights.size());
//	}
//
//	@Test
//	public void test_fetchFieldRightsError() {
//		List<Integer> screenIds = new ArrayList<>();
//		screenIds.add(100);
//		List<RoleFieldRights> roleFieldRights = authDaoImpl.fetchFieldRights(screenIds);
//		Assert.assertEquals(0, roleFieldRights.size());
//	}
//
//	@Test
//	public void test_fetchUserRoleIdByPartyId() {
//		int result = authDaoImpl.fetchUserRoleIdByPartyId(1L, 1);
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchUserRoleIdByPartyIdError() {
//		int result = authDaoImpl.fetchUserRoleIdByPartyId(1L, 3);
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchUserRoleByUserId() {
//		List<User_role> result = authDaoImpl.fetchUserRoleByUserId(1L);
//		Assert.assertEquals(1, result.size());
//	}
//
//	@Test
//	public void test_fetchUserRoleByUserId_Error() {
//		List<User_role> result = authDaoImpl.fetchUserRoleByUserId(3L);
//		Assert.assertEquals(0, result.size());
//	}
//
//	@Test
//	public void test_fetchRoleList() {
//		List<RoleAuth> result = authDaoImpl.fetchRoleList();
//		Assert.assertEquals(1, result.size());
//	}
//
//	@Test
//	public void test_fetchRoleIdByName() {
//		int result = authDaoImpl.fetchRoleIdByName("admin");
//		Assert.assertEquals(1, result);
//	}
//
//	@Test
//	public void test_fetchRoleIdByNameError() {
//		int result = authDaoImpl.fetchRoleIdByName("adv");
//		Assert.assertEquals(0, result);
//	}
//
//	@Test
//	public void test_fetchRoleByRoleId() {
//		String result = authDaoImpl.fetchRoleByRoleId(1L);
//		Assert.assertEquals("admin", result);
//	}
//
//	@Test
//	public void test_fetchRoleByRoleIdError() {
//		String result = authDaoImpl.fetchRoleByRoleId(5L);
//		Assert.assertEquals(null, result);
//	}
//
//	@Test
//	public void test_fetchScreenCodeByScreenId() {
//		String screenCode = authDaoImpl.fetchScreenCodeByScreenId(1);
//		Assert.assertEquals("S1", screenCode);
//	}
//
//	@Test
//	public void test_fetchScreenCodeByScreenId_Error() {
//		String screenCode = authDaoImpl.fetchScreenCodeByScreenId(10);
//		Assert.assertEquals(null, screenCode);
//	}
//
//	@Test
//	public void test_fetchScreenIdsByStartWithScreenCode() {
//		List<Integer> screenIds = authDaoImpl.fetchScreenIdsByStartWithScreenCode("S1");
//		Assert.assertEquals(1, screenIds.size());
//	}
//
//	@Test
//	public void test_fetchScreenIdsByStartWithScreenCode_Error() {
//		List<Integer> screenIds = authDaoImpl.fetchScreenIdsByStartWithScreenCode("S9");
//		Assert.assertEquals(0, screenIds.size());
//	}
//
//}
