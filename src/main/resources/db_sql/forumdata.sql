INSERT INTO `forumcategory` (`name`) VALUES ('Mutualfund');
INSERT INTO `forumcategory` (`name`) VALUES ('Stock');
INSERT INTO `forumsubcategory` (`forumcategoryId`,`name`) VALUES (1,'equity fund');
INSERT INTO `forumsubcategory` (`forumcategoryId`,`name`) VALUES (2,'index fund');
INSERT INTO `forumstatus` (`id`,`desc`) VALUES(1,'Inprogress');
INSERT INTO `forumstatus` (`id`,`desc`) VALUES(2,'Approved');
INSERT INTO `forumstatus` (`id`,`desc`) VALUES(3,'Rejected');
INSERT INTO `articlepost` (`content`,`forumCategoryId`,`forumSubCategoryId`,`forumStatusId`,`partyId`,`created`,`adminId`,`updated`,`delete_flag`,`url`) VALUES ('article',1,1,1,1,'2020-01-09 13:42:20','ADM000000000B','2020-01-09 13:42:35','N','fixed income');
INSERT INTO `articlepost` (`content`,`forumCategoryId`,`forumSubCategoryId`,`forumStatusId`,`partyId`,`created`,`adminId`,`updated`,`delete_flag`,`url`,`prodId`) VALUES ('article two',1,1,2,2,'2020-07-10 13:42:20','ADM000000000B','2020-07-10 13:42:35','N','finance',1);
INSERT INTO `articlecomment` (`content`,`forumStatusId`,`partyId`,`created`,`articleId`,`adminId`,`updated`,`delete_flag`) VALUES ('comment',1,1,'2020-01-09 13:42:56',1,'ADM000000000A','2020-01-09 13:43:10','N');
INSERT INTO `articlevote` (`downCount`,`upcount`,`articleId`) VALUES (0,1,1);
INSERT INTO `articlevote` (`downCount`,`upcount`,`articleId`) VALUES (1,0,2);
INSERT INTO `party` (`partyId`,`partyStatusId`,`roleId`,`roleBasedId`,`created`,`updated`,`parentPartyId`,`delete_flag`,`emailId`,`password`) VALUES (1,1,1,'ADV0000000001','2019-12-12 14:37:09','2019-12-12 14:37:10',1,'N','abc@gmail.com','Abcdef@123');
INSERT INTO `articlevoteaddress` (`articleId`,`partyId`,`voteType`) VALUES (1,1,'UP');
INSERT INTO `role` (`id`,`name`) VALUES (1,'advisor');
INSERT INTO `role` (`id`,`name`) VALUES (2,'investor');
INSERT INTO `role` (`id`,`name`) VALUES (3,'admin');
INSERT INTO `votetype` (`id`,`desc`) VALUES (1,'up');
INSERT INTO `votetype` (`id`,`desc`) VALUES (2,'down');
INSERT INTO `articlestatus` (`id`,`desc`) VALUES (1,'Active');
INSERT INTO `forumthread` (`subject`,`partyId`,`forumSubCategoryId`,`forumCategoryId`,`forumStatusId`,`created`,`adminId`,`updated`,`delete_flag`) VALUES ('Question',1,1,1,1, '2020-01-09 13:31:29', 'ADM000000000A', '2020-01-09 13:31:48','N');
INSERT INTO `forumthread` (`subject`,`partyId`,`forumSubCategoryId`,`forumCategoryId`,`forumStatusId`,`created`,`adminId`,`updated`,`delete_flag`) VALUES ('Question2',2,2,1,1, '2020-01-09 13:31:29', 'ADM000000000A', '2020-01-09 13:31:48','N');
INSERT INTO `forumpost` (`content`,`partyId`,`forumStatusId`,`created`,`threadId`,`adminId`,`updated`,`delete_flag`) VALUES ('Answer',1,1,'2020-01-09 13:32:32',1,'ADM000000000A','2020-01-09 13:32:45','N');
INSERT INTO `forumpostvote` (`downCount`,`upCount`,`postId`) VALUES (1,1,1);
INSERT INTO `postvoteaddress` (`postId`,`partyId`,`voteType`) VALUES (1,1,'UP');
INSERT INTO `forumquery` (`query`,`partyId`,`postedToPartyId`,`forumSubCategoryId`,`forumCategoryId`,`created`,`updated`,`delete_flag`) values ('query',1,1,1,1,'2020-01-09 13:32:32','2020-01-09 13:32:32','N');
INSERT INTO `forumanswer` (`answer`,`queryId`,`partyId`,`created`,`updated`,`delete_flag`) values ('answer',1,1,'2020-01-09 13:32:32','2020-01-09 13:32:32','N');
INSERT INTO `articlecomment` (`content`,`forumStatusId`,`partyId`,`parentCommentId`,`created`,`articleId`,`adminId`,`updated`,`delete_flag`) VALUES ('commenttwo',1,2,1,'2020-01-09 13:42:56',1,'ADM000000000A','2020-01-09 13:43:10','N');
