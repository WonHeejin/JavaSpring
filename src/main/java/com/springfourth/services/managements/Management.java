package com.springfourth.services.managements;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.springfourth.beans.Employees;
import com.springfourth.beans.Members;
import com.springfourth.db.OracleMapper;

@Service
public class Management {
	@Autowired
	OracleMapper om;
	public Management() {
	}
	public String backController(int servCode, Employees ...emp ){	
		String data=null;
		switch(servCode) {
		case 4 :
			data=this.getEmpList(emp[0]);
			break;
		}
		return data;
	}
	public List<Members> ArrbackController(int servCode){	
		List<Members> data=null;
		switch(servCode) {
		case 5 :
			data=getMMList();
			break;
		}

		return data;
	}
	private String getEmpList(Employees emp) {

		return new Gson().toJson(om.getEmplist(emp));
	}
	private List<Members> getMMList() {
		return om.getMMlist();
	}
}
