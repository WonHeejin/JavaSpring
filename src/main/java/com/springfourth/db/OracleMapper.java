package com.springfourth.db;



import java.util.ArrayList;

import com.springfourth.beans.Employees;

public interface OracleMapper {
	public String isEmployee(Employees emp);
	public int insAccessHistory(Employees emp);
	public Employees getAccessInfo(Employees emp);
	public int insAccessOut(Employees emp);
	public ArrayList<Employees> getEmplist(Employees emp);
}
