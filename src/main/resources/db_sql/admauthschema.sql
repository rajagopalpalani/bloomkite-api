DROP TABLE IF EXISTS `role_field_rights`;
DROP TABLE IF EXISTS `role_screen_rights`;
DROP TABLE IF EXISTS `screen_field_reference`;
DROP TABLE IF EXISTS `screen_reference`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `activation_link`;

CREATE TABLE `role` (
	`id` INT(3) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
CREATE TABLE `user_role` (
	`user_role_id` INT(5) NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT(20) NOT NULL DEFAULT '0',
	`role_id` INT(5) NOT NULL DEFAULT '0',
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`user_role_id`)	
);
ALTER TABLE `user_role`
    ADD FOREIGN KEY (role_id) 
    REFERENCES `user_role`(role_id);
    
CREATE TABLE `screen_reference` (
	`screen_id` INT(5) NOT NULL AUTO_INCREMENT,
	`screen_code` VARCHAR(50) NULL DEFAULT NULL,
	`screen_name` VARCHAR(50) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	`subscreen_id` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`screen_id`)
);
CREATE TABLE `screen_field_reference` (
	`screen_field_id` INT(5) NOT NULL AUTO_INCREMENT,
	`screen_id` INT(5) NULL DEFAULT NULL,
	`field_id` INT(5) NULL DEFAULT NULL,
	`field_name` VARCHAR(50) NULL DEFAULT NULL,
	`field_code` VARCHAR(50) NULL DEFAULT NULL,
	`is_mandatory` INT(5) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`screen_field_id`)
);
CREATE TABLE `role_screen_rights` (
	`role_screen_rights_id` INT(5) NOT NULL AUTO_INCREMENT,
	`user_role_id` INT(5) NULL DEFAULT NULL,
	`screen_id` INT(5) NULL DEFAULT NULL,
	`add_rights` INT(5) NULL DEFAULT NULL,
	`edit_rights` INT(5) NULL DEFAULT NULL,
	`view_rights` INT(5) NULL DEFAULT NULL,
	`delete_rights` INT(5) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`role_screen_rights_id`)
);
ALTER TABLE `role_screen_rights`
    ADD FOREIGN KEY (screen_id) 
    REFERENCES `screen_reference`(screen_id);
ALTER TABLE `role_screen_rights`
    ADD FOREIGN KEY (user_role_id) 
    REFERENCES `user_role`(user_role_id);
CREATE TABLE `role_field_rights` (
	`role_field_rights_id` INT(5) NOT NULL AUTO_INCREMENT,
	`role_screen_rights_id` INT(5) NULL DEFAULT NULL,
	`field_id` INT(5) NULL DEFAULT NULL,
	`add_rights` INT(5) NULL DEFAULT NULL,
	`edit_rights` INT(5) NULL DEFAULT NULL,
	`view_rights` INT(5) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`created_date` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`role_field_rights_id`)
);

ALTER TABLE `role_field_rights`
    ADD FOREIGN KEY (role_screen_rights_id) 
    REFERENCES `role_screen_rights`(role_screen_rights_id);
ALTER TABLE `role_field_rights`
    ADD FOREIGN KEY (field_id) 
    REFERENCES `screen_field_reference`(screen_field_id);

CREATE TABLE `activation_link` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`emailId` VARCHAR(50) NULL DEFAULT NULL,
	`subject` VARCHAR(50) NULL DEFAULT NULL,
	`key` VARCHAR(750) NULL DEFAULT NULL,
	`link` VARCHAR(750) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);


