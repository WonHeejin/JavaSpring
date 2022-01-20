package com.springfourth.db;

import org.apache.ibatis.annotations.Select;

public interface OracleMapper {
	@Select("SELECT SYSDATE FROM DUAL") //mybatis가 있어야 제대로 작동
	public String sysdate();
	
	public String sysdate2();
}
