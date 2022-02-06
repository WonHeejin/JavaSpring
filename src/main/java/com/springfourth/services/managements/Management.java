package com.springfourth.services.managements;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.springfourth.beans.Employees;
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
		}
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
