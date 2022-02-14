package com.springfourth.services.sales;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Goods;
import com.springfourth.beans.OrderDetails;
import com.springfourth.beans.Orders;
import com.springfourth.db.OracleMapper;
import com.springfourth.utils.Encryption;
import com.springfourth.utils.ProjectUtils;

@Service
public class Sales {
	@Autowired
	private OracleMapper om;
	@Autowired
	private ProjectUtils pu;
	@Autowired
	private Encryption enc;
	@Autowired
	private DataSourceTransactionManager tx;
	private TransactionStatus txStatus;
	private DefaultTransactionDefinition txDef;
	public Sales() {		
	};
	public ModelAndView backController(int serviceCode, Employees emp) {
		ModelAndView mav= new ModelAndView();
		switch(serviceCode) {
		case 3 :
			mav=this.getSalePage(emp);
			break;
		}
		return mav;
	}
	public void backController(int serviceCode, Model md) {
		switch(serviceCode) {
		case 0 :
			this.searchGoodsCtl(md);
			break;
		case 1 :
			this.reqOrder(md);
			break;	
		}
	}
	private void reqOrder(Model md) {
		Orders od= new Orders();
		String message="결제실패";
		od.setOtList((List<OrderDetails>)md.getAttribute("ot"));
		boolean tran=false;
		try {
			od.setStCode(((Employees)pu.getAttribute("sessionInfo")).getStCode());
			od.setElCode(((Employees)pu.getAttribute("sessionInfo")).getElCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//transaction 수동으로 설정
		this.setTransactionConf(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		//OL 테이블에 데이터 Ins
		if(this.convertToBoolean(om.insOrder(od))) {
			//OL 테이블로부터 OLCODE 갖고오기
			od.setOlCode(om.getOlCode(od));
			message="OlCode 가져오기 성공";
			//LOOP >> GOCODE > GODISCOUNT > ORDERS
			List<OrderDetails> otList= this.om.getGoDiscount(od);
			message="할인정보 가져오기 성공";
			for(OrderDetails ot: od.getOtList()) {
				for(int idx=0;idx<otList.size();idx++) {
					if(ot.getGoCode().equals(otList.get(idx).getGoCode())) {
						ot.setGoDiscount(otList.get(idx).getGoDiscount());
					}
				}
			}message="할인 적용 성공";
			//OrderDetail 테이블에 Ins
			for(int idx=0; idx<od.getOtList().size();idx++) {
				od.getOtList().get(idx).setOlCode(od.getOlCode());
				od.getOtList().get(idx).setStCode(od.getStCode());
				od.getOtList().get(idx).setElCode(od.getElCode());
				if(this.convertToBoolean(om.insOrderDetail(od.getOtList().get(idx)))) {					
					tran=true;
					message="주문입력 성공";
				}else {message=idx+"번째 주문 실패" ;break; }
			}
			/* oracle은 foreach로 다중삽입 불가!
			if(this.convertToBoolean(om.insOrderDetail(od.getOtList()))) {
				tran=true;
				message="주문입력 성공";
			}else {message=" 주문 실패" ;}*/
			//Order 테이블 status update
			if(tran) {
				od.setOlStatus("C");
				if(this.convertToBoolean(om.updOrderStatus(od))) {
					tran=true;
					message="결제성공";
				}else {tran=false;}
			}
		}
		md.addAttribute("msg", message);
		this.setTransactionEnd(tran);
	}
	private void searchGoodsCtl(Model md) {
		md.addAttribute("goInfo",om.getGoodsInfo((Goods)md.getAttribute("goods")));
	}
	private ModelAndView getSalePage(Employees emp) {
		ModelAndView mav= new ModelAndView();
		String page="redirect:/";
		/* Session Check*/
		if(this.sessionCheck(emp)) {		
			/* DB AccessHisory Check */
			Employees accessInfo = this.om.getAccessInfo(emp);
			try {
				if(accessInfo.getAccessType() == 1) {
					//if(this.enc.aesDecode(accessInfo.getPublicIp(), emp.getStCode()+"/"+emp.getElCode()).equals(emp.getPublicIp())) {
						mav.addObject("accessInfo", accessInfo);
						page = "sale";
					//}else {this.pu.removeAttribute("sessionInfo");}
				}else {	this.pu.removeAttribute("sessionInfo");}			
			}catch(Exception e) {e.printStackTrace();}
		}
		mav.setViewName(page);
		return mav;
	}
	private boolean sessionCheck(Employees emp) {
		boolean isSession = false;
		try {
			if(this.pu.getAttribute("sessionInfo") != null) {
				if(emp.getStCode().equals(
						((Employees)this.pu.getAttribute("sessionInfo")).getStCode()) 
						&& emp.getElCode().equals(
								((Employees)this.pu.getAttribute("sessionInfo")).getElCode())) {
					isSession = true;
				}
			}		
		}catch(Exception e) {e.printStackTrace();}

		return isSession;
	}
	//Transaction propagation
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
	private boolean convertToBoolean(int number) {
		return number>0? true: false;
	}
}
