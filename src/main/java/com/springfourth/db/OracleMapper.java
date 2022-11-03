package com.springfourth.db;



import com.springfourth.beans.Employees;

public interface OracleMapper {
	public int isEmployee(Employees emp);
	public int isAccess(Employees emp);
	public int insAccessHistory(Employees emp);
	public Employees getAccessInfo(Employees emp);
}
