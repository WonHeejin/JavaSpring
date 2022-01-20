package com.springfourth.www;

import static org.junit.Assert.fail;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springfourth.db.OracleMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;


@RunWith(SpringJUnit4ClassRunner.class)
/* @RunWith :: junit 테스트 실행방법 확장
 SpringJUnit4ClassRunner ::  jUnit이 테스트를 진행하는 중에 ApplicationContext를 만들고 관리하는 작업을 진행
 @RunWith 어노테이션은 각각의 테스트 별로 객체가 새성되더라도 싱글톤(Singletone)의 ApplicationContext를 보장한다.
 출처:https://shlee0882.tistory.com/202*/ 
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml") //스피링 빈 설정파일 위치 지정
@Log4j
public class TestDBCP {
	@Setter(onMethod_ = {@Autowired})
	
	private OracleMapper om;
	
	
	@Test
	public void test() {
		try{
			log.info(om.getClass().getName());
			log.info(om.sysdate());
			log.info(om.sysdate2());
		}catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
}
