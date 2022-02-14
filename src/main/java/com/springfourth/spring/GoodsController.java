package com.springfourth.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Goods;
import com.springfourth.services.managements.Management;

@RestController
@RequestMapping("/final")   
public class GoodsController {
	@Autowired
	Management mm;
	
	@SuppressWarnings("unchecked")
	@PostMapping("/GoList")
	public List<Goods> getGoodsList(Model md){
		mm.backController(8, md);
		return (List<Goods>)md.getAttribute("goList");
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/UpdGoodsInfo")
	public List<Goods> updGoodsInfo(Model md, @ModelAttribute Goods go, @RequestParam("file") MultipartFile[] file){
		md.addAttribute("file",file);
		md.addAttribute("go",go);
		mm.backController(9, md);
		return (List<Goods>)md.getAttribute("goList");
	}

}
