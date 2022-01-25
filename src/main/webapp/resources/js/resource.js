/**
 *Common Module 
 */
let publicIp;
 function isEmpty(obj){
	 let check= true;
	 if(obj.value==""){
		 check=false;
	 }
	 return check;
}
	//get방식은 location.href를 이용하는게 더 간편하다.
function accessOut(stCode,elCode){
	location.href="AccessOut?stCode="+stCode+"&elCode="+elCode+"&publicIp="+publicIp;
}

function makeForm(fname, faction, fmethod){
	const form = document.createElement("form");
	if(fname != ""){form.setAttribute("name", fname);}
	form.setAttribute("action", faction);
	form.setAttribute("method", fmethod);
	return form;
}

function makeInputElement(type, name, value, placeholder){
	const input = document.createElement("input");
	input.setAttribute("type", type);
	input.setAttribute("name", name);
	if(value != ""){input.setAttribute("value", value);}
	if(placeholder != ""){input.setAttribute("placeholder", placeholder);}
	
	return input;
}
function getAjaxData(action,data,fn,method){
	const ajax = new XMLHttpRequest();
		ajax.onreadystatechange = function() {
			if ( ajax.readyState== 4 && ajax.status == 200) {			
				window[fn](JSON.parse(ajax.responseText));						
			}
		};
		if(method=="get"){
			action=(data!="")?(action+"?"+data):action;
			ajax.open("get", action, true);	
			ajax.send();
		}else{
			ajax.open("post", action, true);
			ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
			ajax.send(data);
		}
		
}

function initIp(){
	/*Public Ip 조회 후 저장*/
	 getAjaxData("https://api.ipify.org","format=json","getPublicIp","get"); 
}
 function getPublicIp(jsonData){
	 publicIp=jsonData.ip;
 }