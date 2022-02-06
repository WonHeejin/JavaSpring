package com.springfourth.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Goods;
import com.springfourth.beans.Members;
import com.springfourth.beans.OrderDetails;
import com.springfourth.beans.Orders;
import com.springfourth.services.managements.Management;
import com.springfourth.services.sales.Sales;

@RestController
@RequestMapping(value="/mgr", produces = "application/json; charset=UTF-8")
public class AjaxController {
	@Autowired
	Management mm;
	@Autowired
	Sales sale;
/*	clientData:stirng, serverData:json일때 model 방식으로 리턴
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/EmpList")
	public List<Employees> getEmpList(Model md,@ModelAttribute Employees emp) {
		mm.backController(4,md.addAttribute("emp",emp));	
		return (List<Employees>)md.getAttribute("EmpList");
	}
*/
	/*clientData:json, serverData:json일때 model 방식으로 리턴*/
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/EmpList")
	public List<Employees> getEmpList(Model md,@RequestBody List<Employees> emp) {
		mm.backController(4,md.addAttribute("emp",emp.get(0)));	
		return (List<Employees>)md.getAttribute("EmpList");
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/MMList")
	public List<Members> getMMList(Model md) {
		mm.backController(5,md);
		return (List<Members>)md.getAttribute("MMList");	
	}
	@PostMapping(value = "/RegEmpForm")
	public String getEmpMax(Model md,@RequestBody List<Employees> emp) {
		mm.backController(6,md.addAttribute("emp",emp.get(0)));
		return (String)md.getAttribute("maxEmp");	
	}
	@PostMapping(value = "/RegEmp")
	public String regEmp(Model md,@RequestBody List<Employees> emp) {
		mm.backController(7,md.addAttribute("emp",emp.get(0)));
		//System.out.println((String)md.getAttribute("result"));
		return (String)md.getAttribute("result");	
	}
	@PostMapping(value = "/getGocode")
	public Goods searchGoods(Model md, @RequestBody Goods go) {
		sale.backController(0,md.addAttribute("goods", go));
		return (Goods)md.getAttribute("goInfo");	
	}
	@PostMapping(value = "/Orders")
	public String reqOrder(Model md, @RequestBody List<OrderDetails> ot) {
		sale.backController(1,md.addAttribute("ot", ot));
		return (String)md.getAttribute("msg");	
	}
	
}
