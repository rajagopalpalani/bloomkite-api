//package com.sowisetech.admin.dao;
//
//import java.sql.Timestamp;
//
//import javax.transaction.Transactional;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.annotation.DirtiesContext.ClassMode;
//
//import com.sowisetech.admin.model.FieldRights;
//import com.sowisetech.admin.model.RoleAuth;
//import com.sowisetech.admin.model.ScreenFieldRights;
//import com.sowisetech.admin.model.User_role;
//
//@Transactional
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//public class AdminAuthDaoImplTest {
//
//	AdminAuthDaoImpl adminDaoImpl;
//	EmbeddedDatabase db;
//
//	@Before
//	public void setup() {
//		EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//				.addScript("db_sql/admauthschema.sql").addScript("db_sql/admauthdata.sql").build();
//		adminDaoImpl = new AdminAuthDaoImpl();
//		adminDaoImpl.setDataSource(db);
//		adminDaoImpl.postConstruct();
//	}
//	//
//	// @Test
//	// public void test_addScreenRights_Success() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	// ScreenFieldRights screenFieldRights = new ScreenFieldRights();
//	// screenFieldRights.setUser_role_id(1);
//	// screenFieldRights.setScreen_id(1);
//	// screenFieldRights.setAdd_rights(1);
//	// screenFieldRights.setEdit_rights(1);
//	// screenFieldRights.setView_rights(1);
//	// screenFieldRights.setDelete_rights(1);
//	// screenFieldRights.setCreated_by("ADM000000000B");
//	// screenFieldRights.setUpdated_by("ADM000000000B");
//	// screenFieldRights.setCreated_date(timestamp);
//	// screenFieldRights.setUpdated_date(timestamp);
//	// int result = adminDaoImpl.addScreenRights(screenFieldRights);
//	// Assert.assertEquals(2, result);
//	// }
//	//
//	// @Test
//	// public void test_addFieldRights_Success() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	//
//	// FieldRights fieldRights = new FieldRights();
//	// fieldRights.setRole_screen_rights_id(1);
//	// fieldRights.setField_id(1);
//	// fieldRights.setAdd_rights(1);
//	// fieldRights.setEdit_rights(1);
//	// fieldRights.setView_rights(1);
//	// fieldRights.setCreated_by("ADM000000000B");
//	// fieldRights.setUpdated_by("ADM000000000B");
//	// fieldRights.setCreated_date(timestamp);
//	// fieldRights.setUpdated_date(timestamp);
//	// int result = adminDaoImpl.addFieldRights(fieldRights);
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// // // @Test
//	// // // public void test_fetchScreenRightsByUserRoleId_Success() {
//	// // // List<ScreenFieldRights> screenFieldRights =
//	// // // adminDaoImpl.fetchScreenRightsByUserRoleId(1);
//	// // // Assert.assertEquals(1, screenFieldRights.size());
//	// // // }
//	// //
//	// // @Test
//	// // public void test_deleteFieldRightsByRoleScreenRightsId_Success() {
//	// // int result = adminDaoImpl.deleteFieldRightsByRoleScreenRightsId(1);
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // // @Test
//	// // // public void test_deleteScreenRightsByUserRoleId_Success() {
//	// // // int result = adminDaoImpl.deleteScreenRightsByUserRoleId(1);
//	// // // Assert.assertEquals(1, result);
//	// // // }
//	// //
//	// @Test
//	// public void test_addRole_Success() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	//
//	// RoleAuth roleAuth = new RoleAuth();
//	// roleAuth.setName("admin");
//	// roleAuth.setCreated_date(timestamp);
//	// roleAuth.setUpdated_date(timestamp);
//	// roleAuth.setCreated_by("ADM000000000B");
//	// roleAuth.setUpdated_by("ADM000000000B");
//	// int result = adminDaoImpl.addRole(roleAuth);
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// // @Test
//	// // public void test_fetchRoleByRoleId_Success() {
//	// // RoleAuth role = adminDaoImpl.fetchRoleByRoleId(1);
//	// // Assert.assertEquals("admin", role.getName());
//	// // }
//	// //
//	// // @Test
//	// // public void test_fetchRoleByRoleId_Error() {
//	// // RoleAuth role = adminDaoImpl.fetchRoleByRoleId(10);
//	// // Assert.assertEquals(null, role);
//	// // }
//	// //
//	// @Test
//	// public void test_modifyRole_Success() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	//
//	// RoleAuth roleAuth = new RoleAuth();
//	// roleAuth.setName("admin");
//	// roleAuth.setUpdated_date(timestamp);
//	// roleAuth.setUpdated_by("ADM000000000B");
//	// int result = adminDaoImpl.modifyRole(1, roleAuth);
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// @Test
//	// public void test_modifyRole_Error() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	//
//	// RoleAuth roleAuth = new RoleAuth();
//	// roleAuth.setName("admin");
//	// roleAuth.setUpdated_date(timestamp);
//	// roleAuth.setUpdated_by("ADM000000000B");
//	// int result = adminDaoImpl.modifyRole(10, roleAuth);
//	// Assert.assertEquals(0, result);
//	// }
//	//
//	// // @Test
//	// // public void test_removeRole_Success() {
//	// // int result = adminDaoImpl.removeRole(1);
//	// // Assert.assertEquals(1, result);
//	// // }
//	// //
//	// // @Test
//	// // public void test_removeRole_Error() {
//	// // int result = adminDaoImpl.removeRole(10);
//	// // Assert.assertEquals(0, result);
//	// // }
//	// //
//	// @Test
//	// public void test_addUserRole_Success() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	//
//	// User_role userRole = new User_role();
//	// userRole.setUser_id(1);
//	// userRole.setRole_id(1);
//	// userRole.setCreated_by("ADM000000000B");
//	// userRole.setUpdated_by("ADM000000000B");
//	// userRole.setCreated_date(timestamp);
//	// userRole.setUpdated_date(timestamp);
//	// int result = adminDaoImpl.addUserRole(userRole);
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// @Test
//	// public void test_fetchUserRoleByUserRoleId_Success() {
//	// User_role userRole = adminDaoImpl.fetchUserRoleByUserRoleId(1);
//	// Assert.assertEquals(1, userRole.getUser_id());
//	// }
//	//
//	// @Test
//	// public void test_modifyUserRole_Success() {
//	// Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	//
//	// User_role userRole = new User_role();
//	// userRole.setUser_id(1);
//	// userRole.setRole_id(1);
//	// userRole.setUpdated_by("ADM000000000B");
//	// userRole.setUpdated_date(timestamp);
//	// int result = adminDaoImpl.modifyUserRole(1, userRole);
//	// Assert.assertEquals(1, result);
//	// }
//	//
//	// // @Test
//	// // public void test_removeUserRole_Success() {
//	// // int result = adminDaoImpl.removeUserRole(1);
//	// // Assert.assertEquals(1, result);
//	// // }
//	//
//
//	@Test
//	public void testcheckUserRoleIsPresent() {
//		int id = adminDaoImpl.checkUserRoleIsPresent(1);
//		Assert.assertEquals(1, id);
//
//	}
//
//	@Test
//	public void testcheckUserRoleIsPresentNegative() {
//		int id = adminDaoImpl.checkUserRoleIsPresent(10);
//		Assert.assertEquals(0, id);
//
//	}
//
//	@Test
//	public void testcheckRoleIsPresent() {
//		int id = adminDaoImpl.checkRoleIsPresent(1);
//		Assert.assertEquals(1, id);
//
//	}
//
//	@Test
//	public void testcheckRoleIsPresentNegative() {
//		int id = adminDaoImpl.checkRoleIsPresent(10);
//		Assert.assertEquals(0, id);
//
//	}
//
//	@Test
//	public void testcheckUserRoleByUserIdAndRoleId() {
//		int id = adminDaoImpl.checkUserRoleByUserIdAndRoleId(1, 1);
//		Assert.assertEquals(1, id);
//
//	}
//
//	@Test
//	public void testcheckUserRoleByUserIdAndRoleIdNegative() {
//		int id = adminDaoImpl.checkUserRoleByUserIdAndRoleId(1, 10);
//		Assert.assertEquals(0, id);
//
//	}
//
//}
