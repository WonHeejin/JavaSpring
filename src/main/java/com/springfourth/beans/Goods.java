package com.springfourth.beans;

import lombok.Data;

@Data
public class Goods {
	private String goCode;
	private String goName;
	private int goQty;
	private int goPrice;
	private int goDiscount;
	private String goStatus;
}
