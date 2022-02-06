package com.springfourth.db;


import java.util.List;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Goods;
import com.springfourth.beans.Members;
import com.springfourth.beans.OrderDetails;
import com.springfourth.beans.Orders;

public interface OracleMapper {
	public String isEmployee(Employees emp);
	public int insAccessHistory(Employees emp);
	public Employees getAccessInfo(Employees emp);
	public int insAccessOut(Employees emp);
	public List<Employees> getEmplist(Employees emp);
	public List<Members> getMMlist();
	public String getMaxEmp(Employees emp);	
	public int insEmp(Employees emp);
	
	public Goods getGoodsInfo(Goods go);
	public int insOrder(Orders od);
	public String getOlCode(Orders od);
	public List<OrderDetails> getGoDiscount(Orders od);
	public int insOrderDetail(OrderDetails ot);
	public int updOrderStatus(Orders od);
}
