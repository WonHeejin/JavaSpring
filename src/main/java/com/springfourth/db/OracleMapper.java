package com.springfourth.db;


import java.util.List;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Members;

public interface OracleMapper {
	public String isEmployee(Employees emp);
	public int insAccessHistory(Employees emp);
	public Employees getAccessInfo(Employees emp);
	public int insAccessOut(Employees emp);
	public List<Employees> getEmplist(Employees emp);
	public List<Members> getMMlist();
}
