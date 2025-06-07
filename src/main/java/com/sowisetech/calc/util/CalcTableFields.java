package com.sowisetech.calc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:calctablefields.properties")
public class CalcTableFields {

	@Value("${mandatory_household_expense}")
	public String mandatory_household_expense;

	@Value("${life_style_expenses}")
	public String life_style_expenses;

	@Value("${recurring_loan_repayments}")
	public String recurring_loan_repayments;

	@Value("${recurring_investments}")
	public String recurring_investments;

	@Value("${recurring_income}")
	public String recurring_income;

	@Value("${assets}")
	public String assets;

	@Value("${liabilities}")
	public String liabilities;

	@Value("${delete_flag_N}")
	public String delete_flag_N;

	@Value("${annualIncome}")
	public String annualIncome;

	@Value("${stability}")
	public String stability;

	@Value("${predictability}")
	public String predictability;

	@Value("${requiredInsurance}")
	public String requiredInsurance;

	@Value("${existingInsurance}")
	public String existingInsurance;

	@Value("${additionalInsurance}")
	public String additionalInsurance;

	@Value("${thirty_or_less}")
	public String thirty_or_less;

	@Value("${thirtyone_to_fourty}")
	public String thirtyone_to_fourty;

	@Value("${fourtyone_to_fiftyone}")
	public String fourtyone_to_fiftyone;

	@Value("${fiftytwo_to_sixtyone}")
	public String fiftytwo_to_sixtyone;

	@Value("${sixtytwo_or_more}")
	public String sixtytwo_or_more;

	public String getMandatory_household_expense() {
		return mandatory_household_expense;
	}

	public void setMandatory_household_expense(String mandatory_household_expense) {
		this.mandatory_household_expense = mandatory_household_expense;
	}

	public String getLife_style_expenses() {
		return life_style_expenses;
	}

	public void setLife_style_expenses(String life_style_expenses) {
		this.life_style_expenses = life_style_expenses;
	}

	public String getRecurring_loan_repayments() {
		return recurring_loan_repayments;
	}

	public void setRecurring_loan_repayments(String recurring_loan_repayments) {
		this.recurring_loan_repayments = recurring_loan_repayments;
	}

	public String getRecurring_investments() {
		return recurring_investments;
	}

	public void setRecurring_investments(String recurring_investments) {
		this.recurring_investments = recurring_investments;
	}

	public String getRecurring_income() {
		return recurring_income;
	}

	public void setRecurring_income(String recurring_income) {
		this.recurring_income = recurring_income;
	}

	public String getAssets() {
		return assets;
	}

	public void setAssets(String assets) {
		this.assets = assets;
	}

	public String getLiabilities() {
		return liabilities;
	}

	public void setLiabilities(String liabilities) {
		this.liabilities = liabilities;
	}

	public String getDelete_flag_N() {
		return delete_flag_N;
	}

	public void setDelete_flag_N(String delete_flag_N) {
		this.delete_flag_N = delete_flag_N;
	}

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getStability() {
		return stability;
	}

	public void setStability(String stability) {
		this.stability = stability;
	}

	public String getPredictability() {
		return predictability;
	}

	public void setPredictability(String predictability) {
		this.predictability = predictability;
	}

	public String getRequiredInsurance() {
		return requiredInsurance;
	}

	public void setRequiredInsurance(String requiredInsurance) {
		this.requiredInsurance = requiredInsurance;
	}

	public String getExistingInsurance() {
		return existingInsurance;
	}

	public void setExistingInsurance(String existingInsurance) {
		this.existingInsurance = existingInsurance;
	}

	public String getAdditionalInsurance() {
		return additionalInsurance;
	}

	public void setAdditionalInsurance(String additionalInsurance) {
		this.additionalInsurance = additionalInsurance;
	}

	public String getThirty_or_less() {
		return thirty_or_less;
	}

	public void setThirty_or_less(String thirty_or_less) {
		this.thirty_or_less = thirty_or_less;
	}

	public String getThirtyone_to_fourty() {
		return thirtyone_to_fourty;
	}

	public void setThirtyone_to_fourty(String thirtyone_to_fourty) {
		this.thirtyone_to_fourty = thirtyone_to_fourty;
	}

	public String getFourtyone_to_fiftyone() {
		return fourtyone_to_fiftyone;
	}

	public void setFourtyone_to_fiftyone(String fourtyone_to_fiftyone) {
		this.fourtyone_to_fiftyone = fourtyone_to_fiftyone;
	}

	public String getFiftytwo_to_sixtyone() {
		return fiftytwo_to_sixtyone;
	}

	public void setFiftytwo_to_sixtyone(String fiftytwo_to_sixtyone) {
		this.fiftytwo_to_sixtyone = fiftytwo_to_sixtyone;
	}

	public String getSixtytwo_or_more() {
		return sixtytwo_or_more;
	}

	public void setSixtytwo_or_more(String sixtytwo_or_more) {
		this.sixtytwo_or_more = sixtytwo_or_more;
	}

}
