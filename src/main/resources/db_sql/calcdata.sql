INSERT INTO `party` (`partyStatusId`,`roleId`,`roleBasedId`,`created`,`updated`,`parentPartyId`,`delete_flag`,`emailId`,`password`,`userName`,`panNumber`,`phoneNumber`) VALUES (1,2,'ADV000000000A','2019-09-20 10:06:56','2019-09-20 10:06:56',1,'N','abc@gmail.com','abc@123','abc','AAAPL1234C','9874561230');

INSERT INTO `goal` (`referenceId`,`goalName`, `tenure`,`tenureType`, `goalAmount`, `inflationRate`,`currentAmount`,`growthRate`,`rateOfReturn`,`annualInvestmentRate`,`futureCost`,`futureValue`,`finalCorpus`,`monthlyInv`,`annualInv`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000','Education',8,'Yr',3000000,0.04,900000,0.15,0.15,0.25,4105707.15,2753120.58,1352586.57,2823.15,40537.17,'2020-12-08 15:05:22','2020-12-08 15:05:50','ADV0000000001','ADV0000000001');


INSERT INTO `cashflowitem` (`cashFlowItem`,`cashFlowItemTypeId`) VALUES ('House Rent',1);
INSERT INTO `cashflowitem` (`cashFlowItem`,`cashFlowItemTypeId`) VALUES ('School Fees',2);
INSERT INTO `cashflowitem` (`cashFlowItem`,`cashFlowItemTypeId`) VALUES ('Home Loan EMI',3);
INSERT INTO `cashflowitem` (`cashFlowItem`,`cashFlowItemTypeId`) VALUES ('Mutual Funds SIP',4);
INSERT INTO `cashflowitem` (`cashFlowItem`,`cashFlowItemTypeId`) VALUES ('Estimated Holiday Expenditures during the year',5);

INSERT INTO `cashFlowitemtype` (`cashFlowItemType`) VALUES ('Mandatory Household Expense');
INSERT INTO `cashFlowitemtype` (`cashFlowItemType`) VALUES ('Life Style Expenses');
INSERT INTO `cashflowitemtype` (`cashFlowItemType`) VALUES ('Recurring Loan Repayments');
INSERT INTO `cashflowitemtype` (`cashFlowItemType`) VALUES ('Recurring Investments');
INSERT INTO `cashflowitemtype` (`cashFlowItemType`) VALUES ('Recurring Income');

INSERT INTO `cashflow` (`referenceId`,`cashFlowItemId`, `budgetAmt`,`actualAmt`, `date`, `cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000', 1, 90000, 15000, '10-07-2000', 1,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `cashflow` (`referenceId`,`cashFlowItemId`, `budgetAmt`,`actualAmt`, `date`, `cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000', 2, 90000, 15000, '10-07-2000', 2,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `cashflow` (`referenceId`,`cashFlowItemId`, `budgetAmt`,`actualAmt`, `date`, `cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000', 3, 90000, 15000, '10-07-2000', 3,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `cashflow` (`referenceId`,`cashFlowItemId`, `budgetAmt`,`actualAmt`, `date`, `cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000', 4, 90000, 15000, '10-07-2000', 4,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `cashflow` (`referenceId`,`cashFlowItemId`, `budgetAmt`,`actualAmt`, `date`, `cashFlowItemTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000', 5, 90000, 15000, '10-07-2000', 5,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `cashflowsummary` (`referenceId`,`monthlyExpense`,`yearlyExpense`,`monthlyIncome`,`yearlyIncome`,`monthlyNetCashFlow`,`yearlyNetCashFlow`,`created`,`updated`,`created_by`,`updated_by`) VALUES('P00000', 140000.00,1680000.00 , 140000.00, 1680000.00, 0.00,0.00,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `account` (`accountEntry`,`accountTypeId`) VALUES ('Savings Account Balance as on date',1);
INSERT INTO `account` (`accountEntry`,`accountTypeId`) VALUES ('Two Wheeler Loan Out Standing',2);

INSERT INTO `accounttype` (`accountType`) VALUES ('Assets');
INSERT INTO `accounttype` (`accountType`) VALUES ('Liabilities');

INSERT INTO `networth` (`accountEntryId`,`value`,`futureValue`,`referenceId`,`accountTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES (1, 150000, 180000, 'P00000', 1,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `networth` (`accountEntryId`,`value`,`futureValue`,`referenceId`,`accountTypeId`,`created`,`updated`,`created_by`,`updated_by`) VALUES (2, 150000, 180000, 'P00000', 2,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `networthsummary` (`referenceId`,`current_assetValue`,`current_liability`,`networth`,`future_assetValue`,`future_liability`,`future_networth`,`created`,`updated`,`created_by`,`updated_by`) VALUES('P00000', 12000.00, 17000.00, 18000.00, 25600.00, 35000.00, 196000.00,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `insurance` (`referenceId`,`annualIncome`,`stability`,`predictability`,`requiredInsurance`,`existingInsurance`,`additionalInsurance`,`created`,`updated`,`created_by`,`updated_by`) VALUES('P00000', 15000.25, 'Stable', 'Predictable', 145000.00, 156000.21, 19000.52,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `urgency` (`value`) VALUES ('High');
INSERT INTO `urgency` (`value`) VALUES ('Medium');
INSERT INTO `urgency` (`value`) VALUES ('Low');

INSERT INTO `priorityitem` (`priorityItem`) VALUES ('Investing in Stocks');
INSERT INTO `priorityitem` (`priorityItem`) VALUES ('Starting a Mutual Funds Sip');
INSERT INTO `priorityitem` (`priorityItem`) VALUES ('Buying Insurance Policy');
INSERT INTO `priorityitem` (`priorityItem`) VALUES ('Accumulating Down Payment for a own house');
INSERT INTO `priorityitem` (`priorityItem`) VALUES ('Investing in second house');
INSERT INTO `priorityitem` (`priorityItem`) VALUES ('Home renovation / extension related amount');

INSERT INTO `priority` (`referenceId`,`priorityItemId`,`urgencyId`,`priorityOrder`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000', 1, 3, 1,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `riskquestionaire` (`answerId`,`answer`,`question`,`questionId`,`score`) VALUES (1,'I enjoy taking very high risk as the same rewards high return','In Comparison to your peer groups, how would you rate your willingness to take risk while making  financial decisions?','1',1);
INSERT INTO `riskquestionaire` (`answerId`,`answer`,`question`,`questionId`,`score`) VALUES (7,'Direct shares','What is the most aggressive investment you have made?','5',1);	

INSERT INTO `riskportfolio` (`points`,`behaviour`,`equity`,`debt`,`cash`) VALUES ('30 or less','High Growth Investor',80,10,10);
INSERT INTO `riskportfolio` (`points`,`behaviour`,`equity`,`debt`,`cash`) VALUES ('31 - 40','Growth Investor',70,20,10);
INSERT INTO `riskportfolio` (`points`,`behaviour`,`equity`,`debt`,`cash`) VALUES ('41 - 51','Balanced Investor',60,30,10);
INSERT INTO `riskportfolio` (`points`,`behaviour`,`equity`,`debt`,`cash`) VALUES ('52 - 61','Moderately Defensive Investor',50,40,10);
INSERT INTO `riskportfolio` (`points`,`behaviour`,`equity`,`debt`,`cash`) VALUES ('62 or more','Defensive Investor',40,50,10);

INSERT INTO `riskprofile` (`referenceId`,`questionId`,`answerId`,`score`,`created`,`updated`,`created_by`,`updated_by`) VALUES('P00000','1',1,5,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `risksummary` (`referenceId`,`behaviour`,`eqty_alloc`,`debt_alloc`,`cash_alloc`,`created`,`updated`,`created_by`,`updated_by`) VALUES('P00000', 'High Growth Investor', 80, 10,10,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');

INSERT INTO `emicalculator` (`referenceId`,`loanAmount`,`tenure`,`interestRate`,`date`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000',5000000,15,10,'20/2020','2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `emicapacity` (`referenceId`,`currentAge`,`retirementAge`,`stability`,`backUp`,`netFamilyIncome`,`existingEmi`,`houseHoldExpense`,`additionalIncome`,`interestRate`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000',38,58,'HIGH','YES',140000,7000,80000,19000,10,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `partialpayment` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`loanDate`,`partPayDate`,`partPayAmount`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000',8000000,10,15,'01-01-2015','26-03-2019',300000,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `interestchange` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`loanDate`,`changedRate`,`interestChangedDate`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000',5000000,10.5,20,'10-02-2018',9,'10-02-2022','2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `emichange` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`loanDate`,`increasedEmi`,`emiChangedDate`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000',5000000,10,20,'01-01-2015',10000,'01-01-2019','2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `emiinterestchange` (`referenceId`,`loanAmount`,`interestRate`,`tenure`,`loanDate`,`increasedEmi`,`changedRate`,`changedDate`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P00000',500000,7,5,'01-02-2019',5000,9,'01-01-2020','2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');


INSERT INTO `insuranceitem` (`insuranceItem`,`value`) VALUES ('annualIncome','Approximate Annual Income');
INSERT INTO `insuranceitem` (`insuranceItem`,`value`) VALUES ('stability','Income Stability For Last Three Years');

INSERT INTO `plan` (`partyId`,`parentPartyId`,`referenceId`,`name`,`age`,`selectedPlan`,`spouse`,`father`,`mother`,`child1`,`child2`,`child3`,`inLaws`,`grandParent`,`sibilings`,`others`,`created`,`updated`,`created_by`,`updated_by`) VALUES (1,1,'P00000','Adv',25,'Financial Planning','yes','yes','yes','yes','yes','yes','yes','yes','yes','yes','2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `planreferenceid` (`referenceId`) VALUES ('P00000');
INSERT INTO `calcquery` (`queryId`,`query`,`referenceId`,`partyId`,`postedToPartyId`,`forumSubCategoryId`,`forumCategoryId`,`url`,`created`,`updated`,`created_by`,`updated_by`,`delete_flag`) VALUES (1,'where is query','P0000',1,2,1,1,'www.google.com','2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001','N');

INSERT INTO `futurevalue` (`referenceId`,`invType`,`invAmount`,`duration`,`durationType`,`annualGrowth`,`totalPayment`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P000','INV00',50000,5,'minute',50000,40000,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
INSERT INTO `targetvalue` (`referenceId`,`invType`,`futureValue`,`rateOfInterest`,`duration`,`durationType`,`totalPayment`,`created`,`updated`,`created_by`,`updated_by`) VALUES ('P000','INV00',6000,500,5,'minute',40000,'2020-11-30 14:21:00','2020-11-30 14:21:00','ADV0000000001','ADV0000000001');
