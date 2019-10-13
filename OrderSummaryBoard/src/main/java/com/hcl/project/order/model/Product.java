package com.hcl.project.order.model;

import java.math.BigDecimal;

public class Product {

	private int prodId;
	private String prodName;
	private BigDecimal prodPrice;

	public Product(int prodId, String prodName, BigDecimal prodPrice) {
		this.prodId = prodId;
		this.prodName = prodName;
		this.prodPrice = prodPrice;
	}

	public int getProdId() {
		return prodId;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}

	@Override
	public String toString() {
		return "Product [prodId=" + prodId + ", prodName=" + prodName
				+ ", prodPrice=" + prodPrice + "]";
	}
}
