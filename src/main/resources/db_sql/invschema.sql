DROP TABLE IF EXISTS `party`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `partystatus`;
DROP TABLE IF EXISTS `investorsmartid`;
DROP TABLE IF EXISTS `invinterest`;
DROP TABLE IF EXISTS `investor`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `categorytype`;
DROP TABLE IF EXISTS `invriskprofile`;
DROP TABLE IF EXISTS `secret_key`;

CREATE TABLE `investor` (
	`invId` VARCHAR(20) NOT NULL,
	`fullName` VARCHAR(100) NOT NULL,
	`displayName` VARCHAR(100) NOT NULL,
	`dob` VARCHAR(10) NOT NULL,
	`emailId` VARCHAR(250) NOT NULL,
	`gender` VARCHAR(1) NOT NULL,
	`password` VARCHAR(100) NOT NULL,
	`phoneNumber` VARCHAR(15) NOT NULL,
	`pincode` VARCHAR(6) NOT NULL,
	`partyStatusId` BIGINT(1) NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`isVerified` INT(2) NULL DEFAULT '0',
	`verifiedBy` VARCHAR(50) NULL DEFAULT NULL,
	`verified` TIMESTAMP NULL DEFAULT NULL,
	`isMobileVerified` INT(2) NULL DEFAULT '0',
	PRIMARY KEY (`invId`)
);

CREATE TABLE `secret_key` (
	`key` VARCHAR(50) NULL DEFAULT NULL
);
CREATE TABLE `role` (
	`id` INT(3) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `partystatus` (
	`id` INT(2) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
CREATE TABLE `party` (
	`partyId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`partyStatusId` INT(1) NOT NULL,
	`id` INT(3) NOT NULL,
	`roleBasedId` VARCHAR(20) NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`parentId` VARCHAR(20) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	PRIMARY KEY (`partyId`)
);
ALTER TABLE `party`
    ADD FOREIGN KEY (id) 
    REFERENCES `role`(id);
--ALTER TABLE `party`
--    ADD FOREIGN KEY (partyStatusId) 
--    REFERENCES `partystatus`(partyStatusId);
--     

CREATE TABLE `invinterest` (
	`interestId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`prodId` INT(3) NOT NULL,
	`invId` VARCHAR(20) NOT NULL,
	`scale` INT(1) NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`interestId`)
);


ALTER TABLE `invinterest`
    ADD FOREIGN KEY (invId) 
    REFERENCES `investor`(invId);
    
CREATE TABLE `categorytype` (
	`categoryTypeId` INT(3) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`categoryTypeId`)
);

CREATE TABLE `category` (
	`categoryId` INT(3) NOT NULL AUTO_INCREMENT,
	`categoryTypeId` INT(3) NULL DEFAULT NULL,
	`desc` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`categoryId`)
);
ALTER TABLE `category`
    ADD FOREIGN KEY (categoryTypeId) 
    REFERENCES `categorytype`(categoryTypeId);

CREATE TABLE `invriskprofile` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`answerId` INT(11) NULL DEFAULT NULL,
	`invId` VARCHAR(20) NULL DEFAULT NULL,
	`questionId` VARCHAR(50) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `investorsmartid` (
	`s_no` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`id` VARCHAR(20) NOT NULL,
	PRIMARY KEY (`s_no`)
);



