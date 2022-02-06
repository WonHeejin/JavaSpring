package com.springfourth.beans;

import lombok.Data;

@Data
public class OrderDetails {
	private String olCode;
	private String stCode;
	private String elCode;
	String goCode;
	int goQty;
	int goDiscount;
}
