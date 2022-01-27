package com.springfourth.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Members;
import com.springfourth.services.managements.Management;

@RestController
@RequestMapping(value="/mgr", produces = "application/json; charset=UTF-8")
public class AjaxController {
	@Autowired
	Management mm;
	
	@PostMapping(value = "/EmpList")
	public String getEmpList(@ModelAttribute Employees emp) {
		return mm.backController(4,emp);	
	}
	@PostMapping(value = "/MMList")
	public List<Members> getMMList() {
		return mm.ArrbackController(5);	
	}
}
