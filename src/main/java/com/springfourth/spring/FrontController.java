package com.springfourth.spring;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.springfourth.beans.Employees;
import com.springfourth.services.auth.Authentication;
import com.springfourth.services.managements.Management;

/**
 * Handles requests for the application home page.
 */
/*servlet-context에서 
 * <context:component-scan base-package="com.springfourth.spring" />를 작성했기 때문에
 * 서버가 가동되면 @Controller가 붙이었는 FrontController가 실행됨
 * */
@Controller
public class FrontController {
	
	private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
	
	@Autowired //new Authentication(); << 안해도 됨
	Authentication auth;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView accessForm() {
		
		return auth.backController(0);	
	}
	@RequestMapping(value = "/Access", method = RequestMethod.POST)
	public ModelAndView access(@ModelAttribute Employees emp) {
		
		return auth.backController(1,emp);	
	}
	@RequestMapping(value = "/AccessOut", method = RequestMethod.GET)
	public ModelAndView accessOut(@ModelAttribute Employees emp) {
		
		return auth.backController(-1,emp);	
	}
	@RequestMapping(value = "/Refresh", method = RequestMethod.POST)
	public ModelAndView refresh(@ModelAttribute Employees emp) {
		return auth.backController(-2,emp);	
	}
	@RequestMapping(value = "/Management", method = RequestMethod.POST)
	public ModelAndView management(@ModelAttribute Employees emp) {
		return auth.backController(2,emp);	
	}
	@RequestMapping(value = "/Sales", method = RequestMethod.POST)
	public ModelAndView sales(@ModelAttribute Employees emp) {
		return auth.backController(3,emp);	
	}
	
}
