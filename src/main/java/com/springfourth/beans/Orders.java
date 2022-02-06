package com.springfourth.beans;

import java.util.List;

import lombok.Data;

@Data
public class Orders {
	private String olCode;
	private String stCode;
	private String elCode;
	private String olStatus;
	private List<OrderDetails> otList;
}
