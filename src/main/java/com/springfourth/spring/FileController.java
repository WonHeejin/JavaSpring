package com.springfourth.spring;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springfourth.beans.Files;
import com.springfourth.beans.Goods;


@Controller
//@RequestMapping("/upload")
public class FileController {

	@GetMapping(value = "/Form")
	public String uploadForm() {		
		return "upload";	
	}
	@PostMapping(value="/MultiPart")
	public String fileUpload(@RequestParam("files")MultipartFile[] uploadFiles, @ModelAttribute Files file) {
		
		for(MultipartFile mf:uploadFiles) {
			File saveFile=new File(mf.getOriginalFilename());
			try {
				mf.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return "upload";
	}
	
	@PostMapping("/MultiPart2")
	@ResponseBody
	public void ajaxFileUpload(@RequestParam("files")MultipartFile[] uploadFiles, @ModelAttribute Files file) {
		for(MultipartFile mf:uploadFiles) {
			File saveFile=new File(mf.getOriginalFilename());
			try {
				mf.transferTo(saveFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
