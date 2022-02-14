package com.springfourth.services.managements;


import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.springfourth.beans.Employees;
import com.springfourth.beans.Goods;
import com.springfourth.db.OracleMapper;
import com.springfourth.utils.Encryption;

@Service
public class Management{
	@Autowired
	private Encryption enc;
	@Autowired
	OracleMapper om;
	@Autowired
	private DataSourceTransactionManager tx;
	private TransactionStatus txStatus;
	private DefaultTransactionDefinition txDef;
	public Management() {
	}
	
	public void backController(int servCode,Model md){	
		
		switch(servCode) {
		case 4 :
			this.getEmpList(md);
			break;
		case 5 :
			getMMList(md);
			break;
		case 6 :
			getEmpForm(md);
			break;
		case 7 :
			regEmp(md);
			break;
		case 8 :
			getGoList(md);
			break;	
		case 9 :
			updGoodsInfo(md);
			break;		
		}
	}
	private void updGoodsInfo(Model md) {
		boolean tran=false;
		//propagation, isolation 설정
		this.setTransactionConf(TransactionDefinition.PROPAGATION_REQUIRED, TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		//상품정보 업데이트
		if(this.convertToBoolean(this.om.updGoodsInfo((Goods)md.getAttribute("go")))) {
			//File 저장
			File saveFile=null;
			for(MultipartFile mf:(MultipartFile[])md.getAttribute("file")) {
				saveFile=new File(((Goods)md.getAttribute("go")).getGoImgLoc());
				try {mf.transferTo(saveFile);} catch (Exception e) {e.printStackTrace();}
			}
			tran=true;
		}
		//transaction end
		this.setTransactionEnd(tran);
		this.getGoList(md);	
	}
	private void getGoList(Model md) {
		md.addAttribute("goList", om.getGoList());
	}
	private void getEmpList(Model md) {		
		md.addAttribute("EmpList", om.getEmplist((Employees)md.getAttribute("emp")));
	}
	private void getMMList(Model md) {
		md.addAttribute("MMList", om.getMMlist());
	}
	private void getEmpForm(Model md) {
			
		md.addAttribute("maxEmp",om.getMaxEmp((Employees)md.getAttribute("emp")));
	}
	private void regEmp(Model md) {
		((Employees)md.getAttribute("emp")).setElPassword((enc.encode(((Employees)md.getAttribute("emp")).getElPassword())));
		boolean tran=false;
		String result="등록실패";
		//Explicit Transaction configuration
		this.setTransactionConf(TransactionDefinition.PROPAGATION_REQUIRED,
				TransactionDefinition.ISOLATION_READ_COMMITTED, false);
		if(this.convertToBoolean(om.insEmp((Employees)md.getAttribute("emp")))) {
			tran=true;
			result="EmpList";
		}
		md.addAttribute("result",result);
		//Transaction End
		this.setTransactionEnd(tran);
		
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
	protected boolean convertToBoolean(int number) {
		return number>0?true:false;
	}
}
