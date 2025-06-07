INSERT INTO `role` (`id`,`name`, `created_by`, `created_date`,`updated_by`,`updated_date`) values (1,'admin','ADM000000000B','2020-07-06 15:37:54','ADM000000000B','2020-07-06 15:37:54');
INSERT INTO `user_role` (`user_role_id`,`user_id`, `role_id`, `created_by`, `created_date`,`updated_by`,`updated_date`) values (1,1,1,'ADM000000000B','2020-07-06 15:37:54','ADM000000000B','2020-07-06 15:37:54');

INSERT INTO `screen_reference` (`screen_code`,`screen_name`,`created_by`,`created_date`,`updated_by`,`updated_date`,`subscreen_id`) values ('S1','profile','ADM000000000B','2020-07-06 15:37:54','ADM000000000B','2020-07-06 15:37:54',1);
INSERT INTO `role_screen_rights` (`user_role_id`,`screen_id`,`add_rights`,`edit_rights`,`view_rights`,`delete_rights`,`created_by`,`created_date`,`updated_by`,`updated_date`) values (1,1,1,1,1,1,'ADM000000000B','2020-10-01 13:20:00','ADM000000000B','2020-10-01 13:20:00');

INSERT INTO `screen_field_reference` (`screen_id`,`field_id`,`field_name`,`field_code`,`is_mandatory`,`created_by`,`created_date`,`updated_by`,`updated_date`) values (2,1,'fullName','S2-F1-1',1,'ADM000000000B','2020-07-06 15:37:54','ADM000000000B','2020-07-06 15:37:54');

INSERT INTO `role_field_rights` (`role_screen_rights_id`,`field_id`,`add_rights`,`edit_rights`,`view_rights`,`created_by`,`created_date`,`updated_by`,`updated_date`) values (1,1,1,1,1,'ADM000000000B','2020-07-06 15:37:54','ADM000000000B','2020-07-06 15:37:54');
INSERT INTO `activation_link` (`emailId`,`subject`,`key`,`link`,`created`) values ('info@gmail.com','Activation Email','c3RhcnQsdmlub3RodmFnYWkxMjlAZ21haWwuY29tLG1pZGRsZSx2aW5vdGgsOTg5ODU0MTIzNCwyMDIwLTEwLTIyIDE3OjU4OjE2Ljc1NSxlbmQ=','http://development.bloomkite.com/mail-verification?key=c3RhcnQsdmlub3RodmFnYWkxMjlAZ21haWwuY29tLG1pZGRsZSx2aW5vdGgsOTg5ODU0MTIzNCwyMDIwLTEwLTIyIDE3OjU4OjE2Ljc1NSxlbmQ=','2020-10-22 17:58:16');
