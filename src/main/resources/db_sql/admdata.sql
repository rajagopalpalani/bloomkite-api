INSERT INTO `admin` (`adminId`,`name`,`password` ,`emailId`, `partyStatusId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) VALUES ('ADM000000000B','admin two', '!@AS12as','admintwo@admin.com',1, '2019-08-22 12:37:03', '2019-08-22 12:37:03', 'N','ADM000000000B','ADM000000000B');
INSERT INTO `secret_key` (`key`) VALUES('key');
INSERT INTO `party`(`partyStatusId`,`roleBasedId`,`created`,`updated`,`delete_flag`,`created_by`,`updated_by`) VALUES (1,'ADM000000000B','2019-08-22 12:37:03', '2019-08-22 12:37:03', 'N','ADM000000000B','ADM000000000B');
INSERT INTO `adminsmartid`(`s_no`, `id`) VALUES(1,'ADM000000000B');
INSERT INTO `acctype` (`AccType`) VALUES ('Financial');
INSERT INTO `remuneration` (`remuneration`) VALUES ('Accounting');
INSERT INTO `state` (`state`) VALUES ('karnataka');
INSERT INTO `workflowstatus` (`status`) VALUES ('private');
INSERT INTO `service` (`service`) VALUES ('tax');
INSERT INTO `advtypes` (`advtype`) values ('advisor');
INSERT INTO `followerstatus` (`followerStatusId`,`status`) values (1,'follow');
INSERT INTO `priorityitem` (`priorityItem`) values ('mutualfunds');
INSERT INTO `riskportfolio` (`riskPortfolioId`,`points`,`behaviour`,`equity`,`debt`,`cash`) values (1,'4','mutal',90,87,87);
INSERT INTO `license` (`license`,`issuedBy`,`prodId`) values ('bank','sbi',14);
INSERT INTO `products` (`product`) VALUES ('Financial planning');
INSERT INTO `articlestatus` (`desc`) VALUES ('active');
INSERT INTO `cashflowitemtype` (`cashFlowItemType`) VALUES ('House hold expenses');
INSERT INTO `cashflowitem` (`cashFlowItem`, `cashFlowItemTypeId`) VALUES ('home allowance', '1');
INSERT INTO `city` (`stateId`, `city`, `pincode`, `district`) VALUES ('1', 'madurai', '641687', 'madurai');
INSERT INTO `urgency` (`value`) VALUES ('active');
INSERT INTO `account` (`accountEntry`, `accountTypeId`) VALUES ('mutualfunds', '1');
INSERT INTO `brand` (`brand`) VALUES ('microsoft');
INSERT INTO `votetype` (`desc`) VALUES ('bottom');
INSERT INTO `insuranceitem` (`insuranceItem`,`value`) VALUES ('home loan', 'allowance');
INSERT INTO `usertype` (`desc`) VALUES ('active');

