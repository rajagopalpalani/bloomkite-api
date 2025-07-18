DROP TABLE IF EXISTS `award`;
DROP TABLE IF EXISTS `certificate`;
DROP TABLE IF EXISTS `education`;
DROP TABLE IF EXISTS `experience`;
DROP TABLE IF EXISTS `promotion`;
DROP TABLE IF EXISTS `advproduct`;
DROP TABLE IF EXISTS `advbrandinfo`;
DROP TABLE IF EXISTS `advbrandrank`;
DROP TABLE IF EXISTS `advisor`;
DROP TABLE IF EXISTS `followers`;
DROP TABLE IF EXISTS `followerstatus`;
DROP TABLE IF EXISTS `usertype`;
DROP TABLE IF EXISTS `generated_otp`;
DROP TABLE IF EXISTS `serviceplan`;
DROP TABLE IF EXISTS `usertype`;
DROP TABLE IF EXISTS `chatuser`;
DROP TABLE IF EXISTS `serviceplan`;
DROP TABLE IF EXISTS `signin_verification`;

CREATE TABLE `advisor` (
	`advId` VARCHAR(20) NOT NULL,
	`advType` INT(3) NOT NULL DEFAULT '0',
	`name` VARCHAR(100) NULL DEFAULT NULL,
	`designation` VARCHAR(100) NULL DEFAULT NULL,
	`emailId` VARCHAR(250) NULL DEFAULT NULL,
	`password` VARCHAR(50) NULL DEFAULT NULL,
	`userName` VARCHAR(50) NULL DEFAULT NULL,
	`phoneNumber` VARCHAR(15) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`partyStatusId` BIGINT(1) NOT NULL DEFAULT '0',
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`displayName` VARCHAR(100) NULL DEFAULT NULL,
	`dob` VARCHAR(10) NULL DEFAULT NULL,
	`gender` VARCHAR(1) NULL DEFAULT NULL,
	`panNumber` VARCHAR(10) NULL DEFAULT NULL,
	`address1` VARCHAR(300) NULL DEFAULT NULL,
	`address2` VARCHAR(300) NULL DEFAULT NULL,
	`state` VARCHAR(50) NULL DEFAULT NULL,
	`city` VARCHAR(50) NULL DEFAULT NULL,
	`pincode` VARCHAR(6) NULL DEFAULT NULL,
	`aboutme` VARCHAR(350) NULL DEFAULT NULL,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`parentPartyId` BIGINT(20) NOT NULL DEFAULT '0',
	`firmType` VARCHAR(200) NULL DEFAULT NULL,
	`corporateLable` VARCHAR(200) NULL DEFAULT NULL,
	`website` VARCHAR(200) NULL DEFAULT NULL,
	`isVerified` INT(2) NULL DEFAULT '0',
	`verifiedBy` VARCHAR(50) NULL DEFAULT NULL,
	`verified` TIMESTAMP NULL DEFAULT NULL,
	`workFlowStatus` INT(11) NULL DEFAULT NULL,
	`approvedDate` TIMESTAMP NULL DEFAULT NULL,
	`approvedBy` VARCHAR(50) NULL DEFAULT NULL,
	`revokedDate` TIMESTAMP NULL DEFAULT NULL,
	`revokedBy` VARCHAR(50) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`reason_for_revoked` VARCHAR(150) NULL DEFAULT NULL,
	`isMobileVerified` INT(2) NULL DEFAULT '0',
	`gst` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advId`)
);

DROP TABLE IF EXISTS `secret_key`;
CREATE TABLE `secret_key` (
	`key` VARCHAR(50) NULL DEFAULT NULL
);
DROP TABLE IF EXISTS `advisortype`;
CREATE TABLE `advisortype` (
	`id` INT(11) NULL DEFAULT NULL,
	`advType` VARCHAR(20) NULL DEFAULT NULL
);
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
	`ID` INT(3) NOT NULL AUTO_INCREMENT,
	`NAME` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`ID`)
);
DROP TABLE IF EXISTS `party`;
DROP TABLE IF EXISTS `partystatus`;
CREATE TABLE `partystatus` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
--CREATE TABLE `party` (
--	`partyId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`partyStatusId` INT(1) NULL DEFAULT NULL,
--	`roleId` INT(3) NULL DEFAULT NULL,
--	`roleBasedId` VARCHAR(20) NULL DEFAULT NULL,
--	`created` TIMESTAMP NULL DEFAULT NULL,
--	`updated` TIMESTAMP NULL DEFAULT NULL,
--	`parentId` VARCHAR(20) NULL DEFAULT NULL,
--	`emailId` VARCHAR(200) NULL DEFAULT NULL,
--	`password` VARCHAR(300) NULL DEFAULT NULL,
--	`userName` VARCHAR(50) NULL DEFAULT NULL,
--	`panNumber` VARCHAR(10) NULL DEFAULT NULL,
--	`phoneNumber` VARCHAR(10) NULL DEFAULT NULL,
--	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
--	PRIMARY KEY (`partyId`)
--);
CREATE TABLE `party` (
	`partyId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`partyStatusId` INT(1) NULL DEFAULT '0',
	`roleBasedId` VARCHAR(20) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`parentPartyId` BIGINT(20) NULL DEFAULT '0',
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`emailId` VARCHAR(200) NULL DEFAULT NULL,
	`password` VARCHAR(300) NULL DEFAULT NULL,
	`userName` VARCHAR(50) NULL DEFAULT NULL,
	`panNumber` VARCHAR(10) NULL DEFAULT NULL,
	`phoneNumber` VARCHAR(10) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`partyId`)
);

ALTER TABLE `party`
    ADD FOREIGN KEY (`partyStatusId`) 
    REFERENCES `partystatus`(`id`);
CREATE TABLE `award` (
	`awardId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`issuedBy` VARCHAR(100) NULL DEFAULT NULL,
	`title` VARCHAR(100) NULL DEFAULT NULL,
	`year` VARCHAR(4) NULL DEFAULT NULL,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`awardId`)
);
ALTER TABLE `award`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);
CREATE TABLE `certificate` (
	`certificateId` BIGINT(10) NOT NULL AUTO_INCREMENT,
	`imagePath` VARCHAR(300) NULL DEFAULT NULL,
	`issuedBy` VARCHAR(100) NULL DEFAULT NULL,
	`title` VARCHAR(100) NULL DEFAULT NULL,
	`year` VARCHAR(4) NULL DEFAULT NULL,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`certificateId`)
	);
ALTER TABLE `certificate`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);	
CREATE TABLE `education` (
	`eduId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`degree` VARCHAR(150) NULL DEFAULT NULL,
	`field` VARCHAR(100) NULL DEFAULT NULL,
	`fromYear` VARCHAR(20) NULL DEFAULT NULL,
	`toYear` VARCHAR(20) NULL DEFAULT NULL,
	`institution` VARCHAR(250) NULL DEFAULT NULL,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`eduId`)
);
ALTER TABLE `education`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);
CREATE TABLE `experience` (
	`expId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`company` VARCHAR(200) NULL DEFAULT NULL,
	`designation` VARCHAR(100) NULL DEFAULT NULL,
	`fromYear` VARCHAR(20) NULL DEFAULT NULL,
	`toYear` VARCHAR(20) NULL DEFAULT NULL,
	`location` VARCHAR(100) NULL DEFAULT NULL,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`expId`)
);
ALTER TABLE `experience`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);
--CREATE TABLE `promotion` (
--	`promotionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`title` VARCHAR(50) NULL DEFAULT NULL,
--	`video` VARCHAR(350) NULL DEFAULT NULL,
--	`advId` VARCHAR(20) NULL DEFAULT NULL,
--	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
--	`aboutVideo` VARCHAR(350) NULL DEFAULT NULL,
--	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
--	PRIMARY KEY (`promotionId`)
--);
CREATE TABLE `promotion` (
	`promotionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(50) NULL DEFAULT NULL,
	`video` VARCHAR(350) NULL DEFAULT NULL,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`aboutVideo` VARCHAR(350) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`promotionId`)
);
ALTER TABLE `promotion`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);
CREATE TABLE `advproduct` (
	`advProdId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	`serviceId` VARCHAR(100) NULL DEFAULT NULL,
	`remId` BIGINT(20) NULL DEFAULT NULL,
	`licId` BIGINT(20) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`licImage` VARCHAR(350) NULL DEFAULT NULL,
	`licNumber` VARCHAR(50) NULL DEFAULT NULL,
	`validity` VARCHAR(20) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advProdId`)
);
ALTER TABLE `advproduct`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
	`prodId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`product` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`prodId`)
);
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
	`serviceId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`service` VARCHAR(100) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`serviceId`)
);
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand` (
	`brandId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`brand` VARCHAR(100) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`brandId`)
);

--CREATE TABLE `advbrandinfo` (
--	`advBrandId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`advId` VARCHAR(20) NULL DEFAULT NULL,
--	`prodId` BIGINT(20) NULL DEFAULT NULL,
--	`serviceId` VARCHAR(100) NULL DEFAULT NULL,
--	`brandId` BIGINT(20) NULL DEFAULT NULL,
--	`priority` INT(3) NULL DEFAULT NULL,
--	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
--	PRIMARY KEY (`advBrandId`)
--);
CREATE TABLE `advbrandinfo` (
	`advBrandId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(20) NOT NULL DEFAULT '0',
	`prodId` BIGINT(20) NOT NULL DEFAULT '0',
	`serviceId` BIGINT(20) NOT NULL DEFAULT '0',
	`brandId` BIGINT(20) NOT NULL DEFAULT '0',
	`delete_flag` VARCHAR(1) NOT NULL DEFAULT '0',
	`priority` INT(3) NOT NULL DEFAULT '0',
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advBrandId`)
);

ALTER TABLE `advbrandinfo`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);

--CREATE TABLE `advbrandrank` (
--	`advBrandRankId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`advId` VARCHAR(20) NULL DEFAULT NULL,
--	`prodId` BIGINT(20) NULL DEFAULT NULL,
--	`brandId` BIGINT(20) NULL DEFAULT NULL,
--	`ranking` BIGINT(3) NULL DEFAULT NULL,
--	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
--	PRIMARY KEY (`advBrandRankId`)
--);
CREATE TABLE `advbrandrank` (
	`advBrandRankId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(20) NOT NULL DEFAULT '0',
	`prodId` BIGINT(20) NOT NULL DEFAULT '0',
	`brandId` BIGINT(20) NOT NULL DEFAULT '0',
	`ranking` BIGINT(20) NOT NULL DEFAULT '0',
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advBrandRankId`)
);

ALTER TABLE `advbrandrank`
    ADD FOREIGN KEY (advId) 
    REFERENCES `advisor`(advId);


DROP TABLE IF EXISTS `license`;
CREATE TABLE `license` (
	`licId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`license` VARCHAR(100) NULL DEFAULT NULL,
	`issuedBy` VARCHAR(100) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`licId`)
);
DROP TABLE IF EXISTS `remuneration`;
CREATE TABLE `remuneration` (
	`remId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`remuneration` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`remId`)
);

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
	`categoryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`categoryTypeId` BIGINT(20) NULL DEFAULT NULL,
	`desc` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`categoryId`)
);
DROP TABLE IF EXISTS `categorytype`;
CREATE TABLE `categorytype` (
	`categoryTypeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`categoryTypeId`)
);
DROP TABLE IF EXISTS `forumsubcategory`;
DROP TABLE IF EXISTS `forumcategory`;
CREATE TABLE `forumcategory` (
	`forumCategoryId` INT(3) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`forumCategoryId`)
);
DROP TABLE IF EXISTS `riskquestionaire`;
CREATE TABLE `riskquestionaire` (
	`answerId` INT(5) NOT NULL AUTO_INCREMENT,
	`answer` VARCHAR(350) NULL DEFAULT NULL,
	`question` VARCHAR(350) NULL DEFAULT NULL,
	`questionId` VARCHAR(50) NOT NULL,
	`score` INT(3) NOT NULL,
	PRIMARY KEY (`answerId`)
);
CREATE TABLE `forumsubcategory` (
	`forumSubCategoryId` INT(3) NOT NULL AUTO_INCREMENT,
	`forumCategoryId` INT(3) NULL DEFAULT NULL,
	`name` VARCHAR(100) NULL DEFAULT NULL,
	PRIMARY KEY (`forumSubCategoryId`)
);
DROP TABLE IF EXISTS `forumstatus`;
CREATE TABLE `forumstatus` (
	`id` INT(2) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `state`;
CREATE TABLE `state` (
	`stateId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`state` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`stateId`)
);
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
	`cityId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`stateId` BIGINT(20) NULL DEFAULT NULL,
	`city` VARCHAR(50) NULL DEFAULT NULL,
	`pincode` VARCHAR(6) NULL DEFAULT NULL,
	PRIMARY KEY (`cityId`)
);
DROP TABLE IF EXISTS `advisorsmartid`;
CREATE TABLE `advisorsmartid` (
	`s_no` INT(20) NOT NULL AUTO_INCREMENT,
	`id` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`s_no`)
);
DROP TABLE IF EXISTS `articlestatus`;
CREATE TABLE `articlestatus` (
	`id` INT(2) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `investorsmartid`;
CREATE TABLE `investorsmartid` (
	`s_no` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`id` VARCHAR(20) NOT NULL,
	PRIMARY KEY (`s_no`)
);
DROP TABLE IF EXISTS `investor`;
CREATE TABLE `investor` (
	`invId` VARCHAR(50) NOT NULL,
	`fullName` VARCHAR(100) NULL DEFAULT NULL,
	`displayName` VARCHAR(100) NULL DEFAULT NULL,
	`dob` VARCHAR(10) NULL DEFAULT NULL,
	`emailId` VARCHAR(250) NULL DEFAULT NULL,
	`gender` VARCHAR(1) NULL DEFAULT NULL,
	`password` VARCHAR(100) NULL DEFAULT NULL,
	`userName` VARCHAR(50) NULL DEFAULT NULL,
	`phoneNumber` VARCHAR(15) NULL DEFAULT NULL,
	`pincode` VARCHAR(6) NULL DEFAULT NULL,
	`partyStatusId` BIGINT(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`isVerified` INT(11) NULL DEFAULT '0',
	`verifiedBy` VARCHAR(50) NULL DEFAULT NULL,
	`verified` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`isMobileVerified` INT(2) NULL DEFAULT '0',
	PRIMARY KEY (`invId`)
);
DROP TABLE IF EXISTS `keypeople`;
CREATE TABLE `keypeople` (
	`keyPeopleId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`fullName` VARCHAR(100) NULL DEFAULT NULL,
	`designation` VARCHAR(100) NULL DEFAULT NULL,
	`image` MEDIUMTEXT NULL DEFAULT NULL,
	`parentPartyId` BIGINT(20) NULL DEFAULT '0',
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`keyPeopleId`)
);
DROP TABLE IF EXISTS `workflowstatus`;
CREATE TABLE `workflowstatus` (
	`workflowId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`status` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`workflowId`)
);
DROP TABLE IF EXISTS `public_award`;
DROP TABLE IF EXISTS `public_certificate`;
DROP TABLE IF EXISTS `public_education`;
DROP TABLE IF EXISTS `public_experience`;
DROP TABLE IF EXISTS `public_promotion`;
DROP TABLE IF EXISTS `public_advproduct`;
DROP TABLE IF EXISTS `public_advbrandinfo`;
DROP TABLE IF EXISTS `public_advbrandrank`;
DROP TABLE IF EXISTS `public_advisor`;
CREATE TABLE `public_advisor` (
	`advId` VARCHAR(20) NOT NULL,
	`advType` INT(3) NOT NULL DEFAULT '0',
	`name` VARCHAR(100) NULL DEFAULT NULL,
	`designation` VARCHAR(100) NULL DEFAULT NULL,
	`emailId` VARCHAR(250) NULL DEFAULT NULL,
	`password` VARCHAR(50) NULL DEFAULT NULL,
	`userName` VARCHAR(50) NULL DEFAULT NULL,
	`phoneNumber` VARCHAR(15) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`partyStatusId` BIGINT(1) NOT NULL DEFAULT '0',
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`displayName` VARCHAR(100) NULL DEFAULT NULL,
	`dob` VARCHAR(10) NULL DEFAULT NULL,
	`gender` VARCHAR(1) NULL DEFAULT NULL,
	`panNumber` VARCHAR(10) NULL DEFAULT NULL,
	`address1` VARCHAR(300) NULL DEFAULT NULL,
	`address2` VARCHAR(300) NULL DEFAULT NULL,
	`state` VARCHAR(50) NULL DEFAULT NULL,
	`city` VARCHAR(50) NULL DEFAULT NULL,
	`pincode` VARCHAR(6) NULL DEFAULT NULL,
	`aboutme` VARCHAR(350) NULL DEFAULT NULL,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`parentPartyId` BIGINT(20) NOT NULL DEFAULT '0',
	`firmType` VARCHAR(200) NULL DEFAULT NULL,
	`corporateLable` VARCHAR(200) NULL DEFAULT NULL,
	`website` VARCHAR(200) NULL DEFAULT NULL,
	`isVerified` INT(2) NULL DEFAULT '0',
	`verifiedBy` VARCHAR(50) NULL DEFAULT NULL,
	`verified` TIMESTAMP NULL DEFAULT NULL,
	`workFlowStatus` INT(11) NULL DEFAULT NULL,
	`approvedDate` TIMESTAMP NULL DEFAULT NULL,
	`approvedBy` VARCHAR(50) NULL DEFAULT NULL,
	`revokedDate` TIMESTAMP NULL DEFAULT NULL,
	`revokedBy` VARCHAR(50) NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`isMobileVerified` INT(2) NULL DEFAULT '0',
	PRIMARY KEY (`advId`)
);
CREATE TABLE `public_award` (
	`awardId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`issuedBy` VARCHAR(100) NULL DEFAULT NULL,
	`title` VARCHAR(100) NULL DEFAULT NULL,
	`year` VARCHAR(4) NULL DEFAULT NULL,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`awardId`)
);
ALTER TABLE `public_award`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `public_certificate` (
	`certificateId` BIGINT(10) NOT NULL AUTO_INCREMENT,
	`imagePath` VARCHAR(300) NULL DEFAULT NULL,
	`issuedBy` VARCHAR(100) NULL DEFAULT NULL,
	`title` VARCHAR(100) NULL DEFAULT NULL,
	`year` VARCHAR(4) NULL DEFAULT NULL,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`certificateId`)
	);
ALTER TABLE `public_certificate`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);	
CREATE TABLE `public_education` (
	`eduId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`degree` VARCHAR(150) NULL DEFAULT NULL,
	`field` VARCHAR(100) NULL DEFAULT NULL,
	`fromYear` VARCHAR(20) NULL DEFAULT NULL,
	`toYear` VARCHAR(20) NULL DEFAULT NULL,
	`institution` VARCHAR(250) NULL DEFAULT NULL,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`eduId`)
);
ALTER TABLE `public_education`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `public_experience` (
	`expId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`company` VARCHAR(200) NULL DEFAULT NULL,
	`designation` VARCHAR(100) NULL DEFAULT NULL,
	`fromYear` VARCHAR(20) NULL DEFAULT NULL,
	`toYear` VARCHAR(20) NULL DEFAULT NULL,
	`location` VARCHAR(100) NULL DEFAULT NULL,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`expId`)
);
ALTER TABLE `public_experience`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `public_promotion` (
	`promotionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(50) NULL DEFAULT NULL,
	`video` VARCHAR(350) NULL DEFAULT NULL,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`imagePath` VARCHAR(350) NULL DEFAULT NULL,
	`aboutVideo` VARCHAR(350) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`promotionId`)
);
ALTER TABLE `public_promotion`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `public_advproduct` (
	`advProdId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	`serviceId` VARCHAR(100) NULL DEFAULT NULL,
	`remId` BIGINT(20) NULL DEFAULT NULL,
	`licId` BIGINT(20) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`licImage` VARCHAR(350) NULL DEFAULT NULL,
	`licNumber` VARCHAR(50) NULL DEFAULT NULL,
	`validity` VARCHAR(20) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advProdId`)
);
ALTER TABLE `public_advproduct`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `public_advbrandinfo` (
	`advBrandId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	`serviceId` VARCHAR(100) NULL DEFAULT NULL,
	`brandId` BIGINT(20) NULL DEFAULT NULL,
	`priority` INT(3) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advBrandId`)
);
ALTER TABLE `public_advbrandinfo`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `public_advbrandrank` (
	`advBrandRankId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(20) NULL DEFAULT NULL,
	`prodId` BIGINT(20) NULL DEFAULT NULL,
	`brandId` BIGINT(20) NULL DEFAULT NULL,
	`ranking` BIGINT(3) NULL DEFAULT NULL,
	`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`advBrandRankId`)
);
ALTER TABLE `public_advbrandrank`
    ADD FOREIGN KEY (advId) 
    REFERENCES `public_advisor`(advId);
CREATE TABLE `followers` (
	`followersId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`userId` VARCHAR(50) NULL DEFAULT NULL,
	`userType` INT(5) NULL DEFAULT NULL,
	`status` INT(5) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`byWhom` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`followersId`)
);
CREATE TABLE `followerstatus` (
	`followerStatusId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`status` VARCHAR(50) NOT NULL DEFAULT '0',
	PRIMARY KEY (`followerStatusId`)
);
CREATE TABLE `usertype` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`desc` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
CREATE TABLE `generated_otp` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`partyId` BIGINT(20) NULL DEFAULT NULL,
	`otp` VARCHAR(6) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
);
CREATE TABLE `chatuser` (
	`chatUserId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`advId` VARCHAR(50) NULL DEFAULT NULL,
	`userId` VARCHAR(50) NULL DEFAULT NULL,
	`userType` INT(5) NULL DEFAULT NULL,
	`status` INT(5) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	`byWhom` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`chatUserId`)
);
CREATE TABLE `serviceplan` (
	`servicePlanId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`prodId` BIGINT(20) NOT NULL DEFAULT '0',
	`serviceId` BIGINT(20) NOT NULL DEFAULT '0',
	`brandId` BIGINT(20) NOT NULL DEFAULT '0',
	`servicePlan` VARCHAR(50) NULL DEFAULT NULL,
	`servicePlanLink` VARCHAR(750) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`servicePlanId`)
);
ALTER TABLE `serviceplan`
    ADD FOREIGN KEY (prodId) REFERENCES `product`(prodId);
ALTER TABLE `serviceplan`
    ADD FOREIGN KEY (serviceId) REFERENCES `service`(serviceId);
ALTER TABLE `serviceplan`
    ADD FOREIGN KEY (brandId) REFERENCES `brand`(brandId);
    
CREATE TABLE `signin_verification` (
	`verificationId` INT(11) NOT NULL AUTO_INCREMENT,
	`mailId` VARCHAR(300) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`verificationId`)
);






