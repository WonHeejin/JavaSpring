package com.springfourth.services.auth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.springfourth.beans.Employees;
import com.springfourth.db.OracleMapper;
import com.springfourth.utils.Encryption;
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
	@Autowired
	private Encryption enc;
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
			case 2 :
				this.getManagementPage(emp[0]);
				break;	
			case 3 :
				this.getSalePage(emp[0]);
				break;		
			}
		}
		return mav;
	}
	public void getSalePage(Employees emp) {
		String page="redirect:/";
		try {
			if(pu.getAttribute("sessionInfo")!=null) {
				page="sale";
			}
		}catch(Exception e) {e.printStackTrace();}
		mav.setViewName(page);
	}
	public void getManagementPage(Employees emp) {
		String page="redirect:/";
		String message=null;
		try {
			if(pu.getAttribute("sessionInfo")!=null) {
				if(emp.getElLevel().equals("AD")) {
					page="management";
				}else {
					page="main";
					message="접근권한이 없습니다.";
				}
			}
		}catch(Exception e) {e.printStackTrace();}
		mav.addObject("msg", message);
		mav.setViewName(page);
	}
	public void accessForm() {
		//세션 유무 판단 >> 있음 : main /없음 : index
		try {
			String page="index";
			Employees emp;
			if((emp=(Employees)pu.getAttribute("sessionInfo"))!=null) {
				//세션 있음>> (Employee에 session 담기)세션 정보 가지고 DB에서 accessInfo 가져오기
				mav.addObject("accessInfo", om.getAccessInfo(emp));
				mav.addObject("msg", "새로운 탭으로 접근");
				page="main";
			}
			mav.setViewName(page);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void access(Employees emp) {
		mav.addObject("name","wonzzang");
		boolean isAccessCheck=false;
		String page="index";
		String message=null;
		//기존 Session의 유지 여부 판단 >>세션 없으면 main, 있으면? 
		try {
			if(pu.getAttribute("sessionInfo")==null) {
				mav.getModel().remove("msg");
				String pw=null;
				if(pu.getAttribute("accessOut")==null||(boolean)pu.getAttribute("accessOut")) {
					if((pw=om.isEmployee(emp))!=null) {
						if(enc.matches(emp.getElPassword(), pw)) {
							try {
								emp.setPublicIp(enc.aesEncode(emp.getPublicIp(), emp.getStCode()+"/"+emp.getElCode()));
							} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
									| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
									| BadPaddingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(this.convertToBoolean(om.insAccessHistory(emp))) {
								//로그인 성공--> AccessHistory-insert-commit >> isAccessCheck=true;
								isAccessCheck=true;
								message="로그인이 성공적으로 이루어졌습니다.";
							}else {message="네트워크가 불안정합니다.";}
						}else {
							message="비밀번호가 일치하지 않습니다.";
						}
					}else {
						message="존재하지 않는 매장코드 혹은 직원코드입니다.";
					}
				}else {
					page="index";
					message="이미 로그아웃되었습니다.";
					pu.setAttribute("accessOut", true);
				}
				
				if(isAccessCheck) {
					//Session LogIn :: ProjectUtil - ContextHolder
					mav.addObject("accessInfo",om.getAccessInfo(emp));
					pu.setAttribute("sessionInfo", mav.getModel().get("accessInfo"));
					pu.setAttribute("accessOut", !isAccessCheck);
					page="main";
				}
			}else {
				page="main";
				message="새로고침";
				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//Database LogIn :: myBatis


		mav.addObject("msg", message);
		mav.setViewName(page);	

	}
	public void accessOut(Employees emp) {
		mav.addObject("name","wonzzang");
		String page="redirect:/";
		String message=null;	
		try {
			//if() {}
			try {
				emp.setPublicIp(enc.aesEncode(emp.getPublicIp(), emp.getStCode()+"/"+emp.getElCode()));
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {
				e.printStackTrace();
			}
			//Database LogOut :: myBatis, jdbc가 자동커밋
			om.insAccessOut(emp);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//화면에 뜨는 로그인 정보 없애기
			mav.clear();
			//세션값 없애기
			try {pu.removeAttribute("sessionInfo");
			} catch (Exception e) {e.printStackTrace();}	
		}
		mav.setViewName(page);	
	}
	private boolean convertToBoolean(int number) {
		return number>0?true:false;
	}
}
