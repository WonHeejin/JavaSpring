package com.springfourth.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.springfourth.beans.Employees;
import com.springfourth.db.OracleMapper;
import com.springfourth.utils.ProjectUtils;
/*servlet-context에서 
 * <context:component-scan base-package="com.springfourth.services.auth" />를 작성했기 때문에
 * 서버가 가동되면 @Service가 붙이었는 Authentication이 실행됨
 * */
@Service 
public class Authentication {
	@Autowired
	private OracleMapper om;
	private ModelAndView mav;
	@Autowired
	private ProjectUtils pu;
	public Authentication() {
		mav= new ModelAndView();
	} 
										/*가변 파라미터 >> 배열 형태로 전달
	 									전달받은 데이터가 없어도 무조건 할당은 하기때문에 null이 안나옴 length로 비교해야함*/
	public ModelAndView backController(int servCode, Employees ...emp ){
		if(emp.length==0) {
			switch(servCode) {
			case 0 :
				this.accessForm();
			break;	
			}
		}else{
			switch(servCode) {
			case 1 :
				this.access(emp[0]);
			break;
			case -1 :
				this.accessOut(emp[0]);
			break;
		}
	}
		return mav;
	}
	public void accessForm() {
		mav.setViewName("index");		
	}
	public void access(Employees emp) {
		mav.addObject("name","wonzzang");
		boolean isAccessCheck=false;
		String page="index";
		//기존 Session의 유지 여부 판단
		
		//Database LogIn :: myBatis
		if(this.convertToBoolean(om.isEmployee(emp))) {
			if(this.convertToBoolean(om.isAccess(emp))) {
				if(this.convertToBoolean(om.insAccessHistory(emp))) {
			//로그인 성공--> AccessHistory-insert-commit >> isAccessCheck=true;
					isAccessCheck=true;
				}
			}
		}
		if(isAccessCheck) {
			//Session LogIn :: ProjectUtil - ContextHolder
			try {
				emp=om.getAccessInfo(emp);
				pu.setAttribute("accessInfo", emp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			page="main";
		}

		mav.setViewName(page);	
		
	}
	public void accessOut(Employees emp) {
		mav.setViewName("index");
		
	}
	private boolean convertToBoolean(int number) {
		return number>0?true:false;
	}
}
