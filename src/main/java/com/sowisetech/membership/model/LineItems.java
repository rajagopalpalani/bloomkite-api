package com.sowisetech.membership.model;

public class LineItems {

	String line_items_id;
	String line_items_name;
	String description;
	int amount;
	int unit_amount;
	int gross_amount;
	int tax_amount;
	int taxable_amount;
	int net_amount;
	String currency;
	String type;

	public String getLine_items_id() {
		return line_items_id;
	}

	public void setLine_items_id(String line_items_id) {
		this.line_items_id = line_items_id;
	}

	public String getLine_items_name() {
		return line_items_name;
	}

	public void setLine_items_name(String line_items_name) {
		this.line_items_name = line_items_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getUnit_amount() {
		return unit_amount;
	}

	public void setUnit_amount(int unit_amount) {
		this.unit_amount = unit_amount;
	}

	public int getGross_amount() {
		return gross_amount;
	}

	public void setGross_amount(int gross_amount) {
		this.gross_amount = gross_amount;
	}

	public int getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(int tax_amount) {
		this.tax_amount = tax_amount;
	}

	public int getTaxable_amount() {
		return taxable_amount;
	}

	public void setTaxable_amount(int taxable_amount) {
		this.taxable_amount = taxable_amount;
	}

	public int getNet_amount() {
		return net_amount;
	}

	public void setNet_amount(int net_amount) {
		this.net_amount = net_amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
