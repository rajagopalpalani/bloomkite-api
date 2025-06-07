DROP TABLE IF EXISTS `goal`;
DROP TABLE IF EXISTS `cashflow`;
DROP TABLE IF EXISTS `cashflowsummary`;
DROP TABLE IF EXISTS `cashflowitem`;
DROP TABLE IF EXISTS `cashflowitemtype`;
DROP TABLE IF EXISTS `networth`;
DROP TABLE IF EXISTS `networthsummary`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `accounttype`;
DROP TABLE IF EXISTS `priority`;
DROP TABLE IF EXISTS `priorityitem`;
DROP TABLE IF EXISTS `urgency`;
DROP TABLE IF EXISTS `insuranceitem`;
DROP TABLE IF EXISTS `insurance`;
DROP TABLE IF EXISTS `riskprofile`;
DROP TABLE IF EXISTS `risksummary`;
DROP TABLE IF EXISTS `riskquestionaire`;
DROP TABLE IF EXISTS `riskportfolio`;
DROP TABLE IF EXISTS `emicapacity`;
DROP TABLE IF EXISTS `party`;
DROP TABLE IF EXISTS `emichange`;
DROP TABLE IF EXISTS `emicalculator`;
DROP TABLE IF EXISTS `partialpayment`;
DROP TABLE IF EXISTS `emiinterestchange`;
DROP TABLE IF EXISTS `interestchange`;
DROP TABLE IF EXISTS `plan`;
DROP TABLE IF EXISTS `planreferenceid`;
DROP TABLE IF EXISTS `calcquery`;
DROP TABLE IF EXISTS `futurevalue`;
DROP TABLE IF EXISTS `targetvalue`;



--CREATE TABLE `goal` (
--	`goalId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`goalName` VARCHAR(50) NULL DEFAULT NULL,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`tenure` INT(3) NOT NULL,
--	`tenureType` VARCHAR(5) NULL DEFAULT NULL,
--	`goalAmount` DECIMAL(15,2) NOT NULL,
--	`inflationRate` DOUBLE NOT NULL,
--	`currentAmount` DECIMAL(15,2) NOT NULL,
--	`growthRate` DOUBLE NOT NULL,
--	`rateOfReturn` DOUBLE NOT NULL,
--	`annualInvestmentRate` DOUBLE NOT NULL,
--	`futureCost` DECIMAL(15,2) NOT NULL,
--	`futureValue` DECIMAL(15,2) NOT NULL,
--	`finalCorpus` DECIMAL(15,2) NOT NULL,
--	`monthlyInv` DECIMAL(15,2) NOT NULL,
--	`annualInv` DECIMAL(15,2) NOT NULL,
--	PRIMARY KEY (`goalId`)
--);
CREATE TABLE `goal` (
	`goalId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`goalName` VARCHAR(50) NULL DEFAULT NULL,
	`referenceId` VARCHAR(50) NOT NULL,
	`tenure` INT(3) NOT NULL,
	`tenureType` VARCHAR(5) NULL DEFAULT NULL,
	`goalAmount` DECIMAL(15,2) NOT NULL,
	`inflationRate` DOUBLE NOT NULL,
	`currentAmount` DECIMAL(15,2) NOT NULL,
	`growthRate` DOUBLE NOT NULL,
	`rateOfReturn` DOUBLE NOT NULL,
	`annualInvestmentRate` DOUBLE NOT NULL,
	`futureCost` DECIMAL(15,2) NOT NULL,
	`futureValue` DECIMAL(15,2) NOT NULL,
	`finalCorpus` DECIMAL(15,2) NOT NULL,
	`monthlyInv` DECIMAL(15,2) NOT NULL,
	`annualInv` DECIMAL(15,2) NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`goalId`)
);

CREATE TABLE `cashflowitem` (
	`cashFlowItemId` INT(3) NOT NULL AUTO_INCREMENT,
	`cashFlowItem` VARCHAR(200) NULL DEFAULT NULL,
	`cashFlowItemTypeId` INT(3) NOT NULL,
	PRIMARY KEY (`cashFlowItemId`)	
);

CREATE TABLE `cashflowitemtype` (
	`cashFlowItemTypeId` INT(3) NOT NULL AUTO_INCREMENT,
	`cashFlowItemType` VARCHAR(200) NULL DEFAULT NULL,
	PRIMARY KEY (`cashFlowItemTypeId`)
);

--CREATE TABLE `cashflow` (
--	`cashFlowId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`cashFlowItemId` INT(3) NOT NULL,
--	`budgetAmt` DECIMAL(15,2) NOT NULL,
--	`actualAmt` DECIMAL(15,2) NOT NULL,
--	`date` VARCHAR(20) NULL DEFAULT NULL,
--	`cashFlowItemTypeId` INT(3) NOT NULL,
--	PRIMARY KEY (`cashFlowId`)
--);
CREATE TABLE `cashflow` (
`cashFlowId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`referenceId` VARCHAR(50) NULL DEFAULT NULL,
`cashFlowItemId` INT(3) NULL DEFAULT NULL,
`budgetAmt` DECIMAL(15,2) NULL DEFAULT NULL,
`actualAmt` DECIMAL(15,2) NULL DEFAULT NULL,
`date` VARCHAR(20) NULL DEFAULT NULL,
`cashFlowItemTypeId` INT(3) NULL DEFAULT NULL,
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (`cashFlowId`)
);

ALTER TABLE `cashflow`
    ADD FOREIGN KEY (cashFlowItemId) 
    REFERENCES `cashflowitem`(cashFlowItemId);

--CREATE TABLE `cashflowsummary` (
--	`cashFlowSummaryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NULL DEFAULT NULL,
--	`monthlyExpense` DECIMAL(15,2) NULL DEFAULT NULL,
--	`yearlyExpense` DECIMAL(15,2) NULL DEFAULT NULL,
--	`monthlyIncome` DECIMAL(15,2) NULL DEFAULT NULL,
--	`yearlyIncome` DECIMAL(15,2) NULL DEFAULT NULL,
--	`monthlyNetCashFlow` DECIMAL(15,2) NULL DEFAULT NULL,
--	`yearlyNetCashFlow` DECIMAL(15,2) NULL DEFAULT NULL,
--	PRIMARY KEY (`cashFlowSummaryId`)
--);

 CREATE TABLE `cashflowsummary` (
`cashFlowSummaryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`referenceId` VARCHAR(50) NULL DEFAULT NULL,
`monthlyExpense` DECIMAL(15,2) NULL DEFAULT NULL,
`yearlyExpense` DECIMAL(15,2) NULL DEFAULT NULL,
`monthlyIncome` DECIMAL(15,2) NULL DEFAULT NULL,
`yearlyIncome` DECIMAL(15,2) NULL DEFAULT NULL,
`monthlyNetCashFlow` DECIMAL(15,2) NULL DEFAULT NULL,
`yearlyNetCashFlow` DECIMAL(15,2) NULL DEFAULT NULL,
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (`cashFlowSummaryId`)
);


CREATE TABLE `accounttype` (
	`accountTypeId` INT(3) NOT NULL AUTO_INCREMENT,
	`accountType` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`accountTypeId`)
);
CREATE TABLE `account` (
	`accountEntryId` INT(3) NOT NULL AUTO_INCREMENT,
	`accountEntry` VARCHAR(200) NULL DEFAULT NULL,
	`accountTypeId` INT(3) NOT NULL,
	PRIMARY KEY (`accountEntryId`)
);
--CREATE TABLE `networth` (
--	`networthId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`accountEntryId` INT(3) NOT NULL,
--	`value` DECIMAL(15,2) NOT NULL,
--	`futureValue` DECIMAL(15,2) NOT NULL,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`accountTypeId` INT(3) NOT NULL,
--	PRIMARY KEY (`networthId`)
--);
CREATE TABLE `networth` (
`networthId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`accountEntryId` INT(3) NOT NULL,
`value` DECIMAL(15,2) NOT NULL,
`futureValue` DECIMAL(15,2) NOT NULL,
`referenceId` VARCHAR(50) NOT NULL,
`accountTypeId` INT(3) NOT NULL,
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (`networthId`)
);

ALTER TABLE `networth`
    ADD FOREIGN KEY (accountEntryId) 
    REFERENCES `account`(accountEntryId);
ALTER TABLE `networth`
    ADD FOREIGN KEY (accountTypeId) 
    REFERENCES `accounttype`(accountTypeId);

--CREATE TABLE `networthsummary` (
--	`networthSummaryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`current_assetValue` DECIMAL(15,2) NOT NULL,
--	`current_liability` DECIMAL(15,2) NOT NULL,
--	`networth` DECIMAL(15,2) NOT NULL,
--	`future_assetValue` DECIMAL(15,2) NOT NULL,
--	`future_liability` DECIMAL(15,2) NOT NULL,
--	`future_networth` DECIMAL(15,2) NOT NULL,
--	PRIMARY KEY (`networthSummaryId`)
--);

    CREATE TABLE `networthsummary` (
`networthSummaryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`referenceId` VARCHAR(50) NOT NULL DEFAULT '0',
`current_assetValue` DECIMAL(15,2) NOT NULL DEFAULT '0',
`current_liability` DECIMAL(15,2) NOT NULL DEFAULT '0',
`networth` DECIMAL(15,2) NOT NULL DEFAULT '0',
`future_assetValue` DECIMAL(15,2) NOT NULL DEFAULT '0',
`future_liability` DECIMAL(15,2) NOT NULL DEFAULT '0',
`future_networth` DECIMAL(15,2) NOT NULL DEFAULT '0',
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (`networthSummaryId`)
);

--CREATE TABLE `insurance` (
--	`insuranceId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`annualIncome` DECIMAL(15,2) NOT NULL,
--	`stability` VARCHAR(20) NULL DEFAULT NULL,
--	`predictability` VARCHAR(20) NULL DEFAULT NULL,
--	`requiredInsurance` DECIMAL(15,2) NOT NULL,
--	`existingInsurance` DECIMAL(15,2) NOT NULL,
--	`additionalInsurance` DECIMAL(15,2) NOT NULL,
--	PRIMARY KEY (`insuranceId`)
--);
CREATE TABLE `insurance` (
`insuranceId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`referenceId` VARCHAR(50) NULL DEFAULT NULL,
`annualIncome` DECIMAL(15,2) NULL DEFAULT NULL,
`stability` VARCHAR(20) NULL DEFAULT NULL,
`predictability` VARCHAR(20) NULL DEFAULT NULL,
`requiredInsurance` DECIMAL(15,2) NULL DEFAULT NULL,
`existingInsurance` DECIMAL(15,2) NULL DEFAULT NULL,
`additionalInsurance` DECIMAL(15,2) NULL DEFAULT NULL,
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (`insuranceId`)
);

CREATE TABLE `priorityitem` (
	`priorityItemId` INT(3) NOT NULL AUTO_INCREMENT,
	`priorityItem` VARCHAR(200) NULL DEFAULT NULL,
	PRIMARY KEY (`priorityItemId`)
);

CREATE TABLE `urgency` (
	`urgencyId` INT(3) NOT NULL AUTO_INCREMENT,
	`value` VARCHAR(20) NULL DEFAULT NULL,
	PRIMARY KEY (`urgencyId`)
);
--CREATE TABLE `priority` (
--	`priorityId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`priorityItemId` INT(3) NOT NULL,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`urgencyId` INT(3) NOT NULL,
--	`priorityOrder` INT(3) NOT NULL,
--	PRIMARY KEY (`priorityId`)
--);

CREATE TABLE `priority` (
	`priorityId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`priorityItemId` INT(3) NULL DEFAULT NULL,
	`referenceId` VARCHAR(50) NULL DEFAULT NULL,
	`urgencyId` INT(3) NULL DEFAULT NULL,
	`priorityOrder` INT(3) NULL DEFAULT NULL,
	`created` TIMESTAMP NOT NULL DEFAULT '',
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated` TIMESTAMP NOT NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`priorityId`)
);


ALTER TABLE `priority`
    ADD FOREIGN KEY (priorityItemId) 
    REFERENCES `priorityitem`(priorityItemId);

CREATE TABLE `party` (
	`partyId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`partyStatusId` INT(1) NULL DEFAULT '0',
	`roleId` INT(3) NULL DEFAULT '0',
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
	PRIMARY KEY (`partyId`)
);

CREATE TABLE `riskportfolio` (
	`riskPortfolioId` INT(3) NOT NULL AUTO_INCREMENT,
	`points` VARCHAR(20) NULL DEFAULT NULL,
	`behaviour` VARCHAR(50) NULL DEFAULT NULL,
	`equity` INT(3) NOT NULL,
	`debt` INT(3) NOT NULL,
	`cash` INT(3) NOT NULL,
	PRIMARY KEY (`riskPortfolioId`)
);

CREATE TABLE `riskquestionaire` (
	`answerId` INT(5) NOT NULL AUTO_INCREMENT,
	`answer` VARCHAR(350) NULL DEFAULT NULL,
	`question` VARCHAR(350) NULL DEFAULT NULL,
	`questionId` VARCHAR(5) NOT NULL,
	`score` INT(3) NOT NULL,
	PRIMARY KEY (`answerId`)
);

--CREATE TABLE `riskprofile` (
--	`riskProfileId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`questionId` VARCHAR(5) NOT NULL,
--	`answerId` INT(5) NOT NULL,
--	`score` INT(3) NOT NULL,
--	PRIMARY KEY (`riskProfileId`)
--);
CREATE TABLE `riskprofile` (
	`riskProfileId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`questionId` VARCHAR(5) NOT NULL DEFAULT '',
	`answerId` INT(5) NOT NULL,
	`score` INT(3) NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`riskProfileId`)
);

ALTER TABLE `riskprofile`
    ADD FOREIGN KEY (answerId) 
    REFERENCES `riskquestionaire`(answerId);
--CREATE TABLE `risksummary` (
--	`riskSummaryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`behaviour` VARCHAR(50) NULL DEFAULT NULL,
--	`eqty_alloc` INT(3) NOT NULL,
--	`debt_alloc` INT(3) NOT NULL,
--	`cash_alloc` INT(3) NOT NULL,
--	PRIMARY KEY (`riskSummaryId`)
--);
CREATE TABLE `risksummary` (
	`riskSummaryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`behaviour` VARCHAR(50) NULL DEFAULT NULL,
	`eqty_alloc` INT(3) NOT NULL,
	`debt_alloc` INT(3) NOT NULL,
	`cash_alloc` INT(3) NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`riskSummaryId`)
);


--CREATE TABLE `emicalculator` (
--	`emiCalculatorId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`loanAmount` DECIMAL(15,2) NOT NULL,
--	`tenure` INT(3) NOT NULL,
--	`interestRate` DOUBLE NOT NULL,
--	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
--	`date` VARCHAR(10) NULL DEFAULT NULL,
--	PRIMARY KEY (`emiCalculatorId`)
--);
CREATE TABLE `emicalculator` (
	`emiCalculatorId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`loanAmount` DECIMAL(15,2) NOT NULL,
	`tenure` INT(3) NOT NULL,
	`interestRate` DOUBLE NOT NULL,
	`date` VARCHAR(20) NULL DEFAULT NULL,
	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`emiCalculatorId`)
);


--CREATE TABLE `emicapacity` (
--	`emiCapacityId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`currentAge` INT(3) NOT NULL,
--	`retirementAge` INT(3) NOT NULL,
--	`stability` VARCHAR(10) NULL DEFAULT NULL,
--	`backUp` VARCHAR(10) NULL DEFAULT NULL,
--	`netFamilyIncome` DECIMAL(15,2) NOT NULL,
--	`existingEmi` DECIMAL(15,2) NOT NULL,
--	`houseHoldExpense` DECIMAL(15,2) NOT NULL,
--	`additionalIncome` DECIMAL(15,2) NOT NULL,
--	`interestRate` DOUBLE NOT NULL,
--	PRIMARY KEY (`emiCapacityId`)
--);
CREATE TABLE `emicapacity` (
	`emiCapacityId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`currentAge` INT(3) NOT NULL,
	`retirementAge` INT(3) NOT NULL,
	`stability` VARCHAR(10) NULL DEFAULT NULL,
	`backUp` VARCHAR(10) NULL DEFAULT NULL,
	`netFamilyIncome` DECIMAL(15,2) NOT NULL,
	`existingEmi` DECIMAL(15,2) NOT NULL,
	`houseHoldExpense` DECIMAL(15,2) NOT NULL,
	`additionalIncome` DECIMAL(15,2) NOT NULL,
	`interestRate` DOUBLE NOT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`emiCapacityId`)
);

--CREATE TABLE `partialpayment` (
--	`partialPaymentId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`loanAmount` DECIMAL(15,2) NOT NULL,
--	`interestRate` DOUBLE NOT NULL,
--	`tenure` INT(5) NOT NULL,
--		`tenureType` VARCHAR(10) NULL DEFAULT NULL,
--	`loanDate` VARCHAR(20) NULL DEFAULT NULL,
--	`partPayDate` VARCHAR(20) NULL DEFAULT NULL,
--	`partPayAmount` DECIMAL(15,2) NOT NULL,
--	PRIMARY KEY (`partialPaymentId`)
--);
CREATE TABLE `partialpayment` (
	`partialPaymentId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`loanAmount` DECIMAL(15,2) NOT NULL,
	`interestRate` DOUBLE NOT NULL,
	`tenure` INT(5) NOT NULL,
	`loanDate` VARCHAR(20) NULL DEFAULT NULL,
	`partPayDate` VARCHAR(20) NULL DEFAULT NULL,
	`partPayAmount` DECIMAL(15,2) NOT NULL,
	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`partialPaymentId`)
);


--CREATE TABLE `emichange` (
--	`emiChangeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`loanAmount` DECIMAL(15,2) NOT NULL,
--	`interestRate` DOUBLE NOT NULL,
--	`tenure` DOUBLE NOT NULL,
--		`tenureType` VARCHAR(10) NULL DEFAULT NULL,
--	`loanDate` VARCHAR(10) NULL DEFAULT NULL,
--	`increasedEmi` DOUBLE NOT NULL,
--	`emiChangedDate` VARCHAR(10) NULL DEFAULT NULL,
--	PRIMARY KEY (`emiChangeId`)
--);
CREATE TABLE `emichange` (
	`emiChangeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`loanAmount` DECIMAL(15,2) NOT NULL,
	`interestRate` DOUBLE NOT NULL,
	`tenure` DOUBLE NOT NULL,
	`loanDate` VARCHAR(20) NULL DEFAULT NULL,
	`increasedEmi` DOUBLE NULL DEFAULT NULL,
	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
	`emiChangedDate` VARCHAR(20) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`emiChangeId`)
);


--CREATE TABLE `interestchange` (
--	`interestChangeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`loanAmount` DECIMAL(15,2) NOT NULL,
--	`interestRate` DOUBLE NOT NULL,
--	`tenure` DOUBLE NOT NULL,
--		`tenureType` VARCHAR(10) NULL DEFAULT NULL,
--	`loanDate` VARCHAR(10) NULL DEFAULT NULL,
--	`changedRate` DOUBLE NOT NULL,
--	`interestChangedDate` VARCHAR(10) NULL DEFAULT NULL,
--	PRIMARY KEY (`interestChangeId`)
--);
CREATE TABLE `interestchange` (
	`interestChangeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`loanAmount` DECIMAL(15,2) NOT NULL,
	`interestRate` DOUBLE NOT NULL,
	`tenure` DOUBLE NOT NULL,
	`loanDate` VARCHAR(20) NULL DEFAULT NULL,
	`changedRate` DOUBLE NOT NULL,
	`interestChangedDate` VARCHAR(20) NULL DEFAULT NULL,
	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`interestChangeId`)
);


--CREATE TABLE `emiinterestchange` (
--	`emiIntChangeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`referenceId` VARCHAR(50) NOT NULL,
--	`loanAmount` DECIMAL(15,2) NOT NULL,
--	`interestRate` DOUBLE NOT NULL,
--	`tenure` DOUBLE NOT NULL,
--	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
--	`loanDate` VARCHAR(10) NULL DEFAULT NULL,
--	`increasedEmi` DOUBLE NOT NULL,
--	`changedRate` DOUBLE NOT NULL,
--	`changedDate` VARCHAR(10) NULL DEFAULT NULL,
--	PRIMARY KEY (`emiIntChangeId`)
--);
CREATE TABLE `emiinterestchange` (
	`emiIntChangeId` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(50) NOT NULL,
	`loanAmount` DECIMAL(15,2) NOT NULL,
	`interestRate` DOUBLE NOT NULL,
	`tenure` DOUBLE NOT NULL,
	`loanDate` VARCHAR(20) NULL DEFAULT NULL,
	`increasedEmi` DOUBLE NOT NULL,
	`tenureType` VARCHAR(10) NULL DEFAULT NULL,
	`changedRate` DOUBLE NOT NULL,
	`changedDate` VARCHAR(20) NULL DEFAULT NULL,
	`created` TIMESTAMP NULL DEFAULT NULL,
	`updated` TIMESTAMP NULL DEFAULT NULL,
	`created_by` VARCHAR(50) NULL DEFAULT NULL,
	`updated_by` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`emiIntChangeId`)
);

CREATE TABLE `insuranceitem` (
	`insuranceItemId` INT(11) NOT NULL AUTO_INCREMENT,
	`insuranceItem` VARCHAR(50) NULL DEFAULT NULL,
	`value` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`insuranceItemId`)
);

--CREATE TABLE `plan` (
--	`planId` BIGINT(20) NOT NULL AUTO_INCREMENT,
--	`partyId` BIGINT(20) NOT NULL DEFAULT '0',
--	`parentPartyId` BIGINT(20) NOT NULL DEFAULT '0',
--	`superParentId` BIGINT(20) NOT NULL DEFAULT '0',
--	`referenceId` VARCHAR(20) NULL DEFAULT NULL,
--	`name` VARCHAR(100) NULL DEFAULT NULL,
--	`age` INT(3) NOT NULL DEFAULT '0',
--	`selectedPlan` VARCHAR(300) NULL DEFAULT NULL,
--	`spouse` VARCHAR(5) NULL DEFAULT NULL,
--	`father` VARCHAR(5) NULL DEFAULT NULL,
--	`mother` VARCHAR(5) NULL DEFAULT NULL,
--	`others` VARCHAR(5) NULL DEFAULT NULL,
--	`inLaws` VARCHAR(5) NULL DEFAULT NULL,
--	`grandParent` VARCHAR(5) NULL DEFAULT NULL,
--	`child1` VARCHAR(5) NULL DEFAULT NULL,
--	`child2` VARCHAR(5) NULL DEFAULT NULL,
--	`child3` VARCHAR(5) NULL DEFAULT NULL,
--	`sibilings` VARCHAR(5) NULL DEFAULT NULL,
--	PRIMARY KEY (`planId`)
--);

CREATE TABLE `plan` (
`planId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`partyId` BIGINT(20) NOT NULL DEFAULT '0',
`parentPartyId` BIGINT(20) NOT NULL DEFAULT '0',
`referenceId` VARCHAR(20) NULL DEFAULT NULL,
`name` VARCHAR(100) NULL DEFAULT NULL,
`age` INT(3) NOT NULL DEFAULT '0',
`selectedPlan` VARCHAR(300) NULL DEFAULT NULL,
`spouse` VARCHAR(5) NULL DEFAULT NULL,
`father` VARCHAR(5) NULL DEFAULT NULL,
`mother` VARCHAR(5) NULL DEFAULT NULL,
`others` VARCHAR(5) NULL DEFAULT NULL,
`inLaws` VARCHAR(5) NULL DEFAULT NULL,
`grandParent` VARCHAR(5) NULL DEFAULT NULL,
`child1` VARCHAR(5) NULL DEFAULT NULL,
`child2` VARCHAR(50) NULL DEFAULT NULL,
`child3` VARCHAR(5) NULL DEFAULT NULL,
`sibilings` VARCHAR(5) NULL DEFAULT NULL,
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (`planId`)
);

CREATE TABLE `planreferenceid` (
	`s_no` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`referenceId` VARCHAR(20) NULL DEFAULT NULL,
	PRIMARY KEY (`s_no`)
);

CREATE TABLE `calcquery` (
`queryId` BIGINT(20) NOT NULL AUTO_INCREMENT,
`query` TEXT NULL DEFAULT NULL,
`referenceId` VARCHAR(20) NULL DEFAULT NULL,
`partyId` BIGINT(20) NOT NULL DEFAULT '0',
`postedToPartyId` BIGINT(20) NOT NULL DEFAULT '0',
`forumSubCategoryId` INT(11) NOT NULL DEFAULT '0',
`forumCategoryId` INT(11) NOT NULL DEFAULT '0',
`url` VARCHAR(500) NOT NULL DEFAULT '0',
`created` TIMESTAMP NULL DEFAULT NULL,
`updated` TIMESTAMP NULL DEFAULT NULL,
`created_by` VARCHAR(50) NULL DEFAULT NULL,
`updated_by` VARCHAR(50) NULL DEFAULT NULL,
`delete_flag` VARCHAR(1) NULL DEFAULT NULL,
PRIMARY KEY (`queryId`)
);


CREATE TABLE `futurevalue` (
	futureValueId BIGINT(20) NOT NULL AUTO_INCREMENT,
	referenceId VARCHAR(50) NULL DEFAULT NULL,
	invType VARCHAR(50) NULL DEFAULT NULL,
	invAmount DECIMAL(15,2) NOT NULL DEFAULT '0',
	duration INT(15) NOT NULL DEFAULT '0',
	durationType VARCHAR(50) NULL DEFAULT NULL,
	annualGrowth DOUBLE NOT NULL DEFAULT '0',
	totalPayment DECIMAL(15,2) NOT NULL DEFAULT '0',
	created TIMESTAMP NULL DEFAULT NULL,
	updated TIMESTAMP NULL DEFAULT NULL,
	created_by VARCHAR(50) NULL DEFAULT NULL,
	updated_by VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (futureValueId)
);


CREATE TABLE `targetvalue` (
	targetValueId BIGINT(20) NOT NULL AUTO_INCREMENT,
	referenceId VARCHAR(50) NULL DEFAULT NULL,
	invType VARCHAR(50) NULL DEFAULT NULL,
	futureValue DECIMAL(15,2) NOT NULL DEFAULT '0',
	rateOfInterest DOUBLE NOT NULL DEFAULT '0',
	duration INT(20) NOT NULL DEFAULT '0',
	durationType VARCHAR(50) NULL DEFAULT NULL,
	totalPayment DECIMAL(15,2) NOT NULL DEFAULT '0',
	created TIMESTAMP NULL DEFAULT NULL,
	updated TIMESTAMP NULL DEFAULT NULL,
	created_by VARCHAR(50) NULL DEFAULT NULL,
	updated_by VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (targetValueId)
);






