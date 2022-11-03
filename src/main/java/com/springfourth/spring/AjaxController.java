package com.springfourth.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springfourth.beans.Employees;
import com.springfourth.services.managements.Management;

@RestController
public class AjaxController {

	@Autowired
	Management mm;
	@RequestMapping(value = "/EmpList", produces = "text/json; charset=UTF-8")
	public String getEmpList(@ModelAttribute Employees emp) {
		return mm.backController(4,emp);	
	}
}
