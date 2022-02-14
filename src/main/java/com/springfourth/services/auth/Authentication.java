package com.springfourth.services.auth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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
	@Autowired
	JavaMailSender javaMail;
	@Autowired
	private DataSourceTransactionManager tx;
	private TransactionStatus txStatus;
	private DefaultTransactionDefinition txDef;
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
			case -2 :
				this.accessRefresh(emp[0]);
				break;		
			case 2 :
				this.getManagementPage(emp[0]);
				break;
			case 4 :
				this.sendEmail(emp[0]);;
				break;
			case 5 :
				this.authEmail(emp[0]);;
				break;
			case 6 :
				this.modPassword(emp[0]);;
				break;		
			}
		}
		return mav;
	}
	private void modPassword(Employees emp) {
		String message="암호변경에 실패하였습니다.";
		String page="password";
		boolean tran=false;
		this.setTransactionConf(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		emp.setElPassword(this.enc.encode(emp.getElPassword()));
		System.out.println(emp.getElPassword());
		if(this.convertToBoolean(this.om.updPassword(emp))) {
			tran=true;
			page="index";
			message="비밀번호 변경에 성공하였습니다.";
		}
		this.setTransactionEnd(tran);
		this.mav.addObject("msg", message);
		this.mav.setViewName(page);
	}
	private void authEmail(Employees emp) {
		String code=null;
		this.mav.addObject("msg", "변경할 패스워드를 입력해 주세요");

		try {
			code=this.enc.aesDecode(emp.getAuthCode(),"changePWD");		
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.mav.addObject("stCode", code.substring(0, code.indexOf(",")));
		this.mav.addObject("elCode", code.substring( code.indexOf(",")+1));
		this.mav.setViewName("modPassword");
	}
	private void sendEmail(Employees emp) {
		boolean isSendMail = false;
		String page="password";
		String message="등록된 정보와 일치하지 않습니다.";
		/* 해당사원의 Email 일치 여부 확인 : DATABASE 작업 
		 * stCode, elCode, email*/
		if(this.convertToBoolean(this.om.isMember(emp))) {
			String auth=emp.getStCode()+","+emp.getElCode();
			try {
				auth=this.enc.aesEncode(auth, "changePWD");
			} catch (Exception e) {e.printStackTrace();}
			/* Email Info */
			String subject="비밀번호를 수정해주세요.";
			String contents="<a href='http://localhost/EmailAuth?authCode="+auth+"'>인증 작업을 위해 이동해 주세요.</a>";
			String from="ohj5535967@naver.com";
			String to=emp.getEmail();
			/* Creation MimeMessage */
			MimeMessage mail=javaMail.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail,"UTF-8");
			try {
				helper.setFrom(from);
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setText(contents,true);
				javaMail.send(mail);
				isSendMail=true;
			} catch (MessagingException e) {
				isSendMail=false;
				e.printStackTrace();
			}
			message=isSendMail?"요청하신 메일주소로 인증코드를 발송하였습니다.":"메일 발송에 실패했습니다. 다시 시도해주세요";
			page="index";
		}
		
		/* message 작성*/
		this.mav.addObject("msg",message);
		/* page */
		this.mav.setViewName("index");
	}
	
	private void getManagementPage(Employees emp) {
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
	private void accessForm() {
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
	private void access(Employees emp) {
		mav.addObject("name","wonzzang");
		boolean isAccessCheck=false;
		String page="index";
		String message=null;
		//기존 Session의 유지 여부 판단 >>세션 없으면 main, 있으면? 
		try {

			mav.getModel().remove("msg");
			String pw=null;
			if((pw=om.isEmployee(emp))!=null) {
				if(enc.matches(emp.getElPassword(), pw)) {
//					try {
//						emp.setPublicIp(enc.aesEncode(emp.getPublicIp(), emp.getStCode()+"/"+emp.getElCode()));
//					} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
//							| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
//							| BadPaddingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
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


			if(isAccessCheck) {
				//Session LogIn :: ProjectUtil - ContextHolder
				mav.addObject("accessInfo",om.getAccessInfo(emp));
				pu.setAttribute("sessionInfo", mav.getModel().get("accessInfo"));
				page="main";
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//Database LogIn :: myBatis


		mav.addObject("msg", message);
		mav.setViewName(page);	

	}
	private void accessOut(Employees emp) {
		mav.addObject("name","wonzzang");
		String page="redirect:/";	
		try {
			//if() {}
//			try {
//				emp.setPublicIp(enc.aesEncode(emp.getPublicIp(), emp.getStCode()+"/"+emp.getElCode()));
//			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
//					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
//					| BadPaddingException e) {
//				e.printStackTrace();
//			}
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
	private void accessRefresh(Employees emp) {
		String page="redirect:/";
		String message="다시 로그인해주세요.";
		try {
			if(pu.getAttribute("sessionInfo")!=null) {
				message="새로고침";
				page=mav.getViewName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("msg",message);
		mav.setViewName(page);
	}
	private boolean convertToBoolean(int number) {
		return number>0?true:false;
	}
	private void setTransactionConf(int propa, int iso, boolean isRead) {
		this.txDef= new DefaultTransactionDefinition();
		this.txDef.setPropagationBehavior(propa);
		this.txDef.setIsolationLevel(iso);
		this.txDef.setReadOnly(isRead);
		this.txStatus=this.tx.getTransaction(txDef);
	}
	private void setTransactionEnd(boolean tran){
		if(tran)this.tx.commit(txStatus);
		else this.tx.rollback(txStatus);
	}
}
