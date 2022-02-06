/**
 * client에서 Node를 활용하여 데이터 조정
 */
const itemName=["no","prName","prQuantity","prPrice","prAmount","prDiscount","goCode"];
let no =0;
let currentRecord=null;

function payment(){	
	const list= document.getElementById("list");
	if(list.childNodes.length==0){ return;} //자바스크립트의 return은 break의 기능을 수행하기도 함
	const prCode=document.getElementsByName("goCode");
	const prQuantity=document.getElementsByName("prQuantity");
	let clientData=[];
	for(idx=0;idx<prCode.length;idx++){
		clientData.push({goCode:prCode[idx].innerText,goQty:prQuantity[idx].innerText});
	}
	getAjaxData2("mgr/Orders",JSON.stringify(clientData),"cancle");
}

function goodsInfoCtl(pPrCode){ 
	const prCode= document.getElementsByName(pPrCode)[0].value;
	if(prCode!=""){
		if(!comparePrCode(prCode,"record")){
		const data=JSON.stringify({goCode:prCode});
		getAjaxJsonUsingJquery('mgr/getGocode',data,'addGoods');
		}
	}else{
		alert("상품코드 입력");
	}
	goCode.value="";
	goCode.focus();

}
/*JQUERY를 이용한 Ajax방식*/
function getAjaxJsonUsingJquery(action, clientData, fn) {
	$.ajax({
		async : true,
		type : "post",
		url : action,
		data : clientData,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(jsonObject){
			window[fn](jsonObject);
		},
	});
}
function getAjaxData2(action,data,fn){
	const ajax = new XMLHttpRequest();
		ajax.onreadystatechange = function() {
			if ( ajax.readyState== 4 && ajax.status == 200) {			
				window[fn](ajax.responseText);						
			}
		};
		ajax.open("post", action, true);
		ajax.setRequestHeader("Content-type","application/json");	
		ajax.send(data);
}
function comparePrCode(prCode,list){
	let check = false;
	const record=document.getElementsByName(list);
	for(idx=0;idx<record.length;idx++){
		if(record[idx].childNodes[6].innerText==prCode){
			const qty=record[idx].childNodes[2].innerText;
			record[idx].childNodes[2].innerText=parseInt(qty)+1;
			record[idx].childNodes[4].innerText=parseInt(record[idx].childNodes[2].innerText)*parseInt(record[idx].childNodes[3].innerText);
			record[idx].childNodes[5].innerText=parseInt(record[idx].childNodes[2].innerText)*(parseInt(record[idx].childNodes[5].innerText)/qty);
			orderList();
			orderList2();
			check=true;
		}
	}
	return check;
}
function addGoods(goodsInfo){
	const list = document.getElementById("list");
	let jsonData=goodsInfo;
	if(jsonData.goStatus=="S"&&jsonData!=""){
		no++;
		let record=createDiv("record","record");
		record.setAttribute("onClick","selectBotton(this)");
		for(colIdx=0;colIdx<7;colIdx++){
		let item = createDiv(itemName[colIdx],"goods "+itemName[colIdx]);
		item.innerHTML=(colIdx==0)?no:(colIdx==1)?jsonData.goName:(colIdx==2)?jsonData.goQty:(colIdx==3)?jsonData.goPrice:(colIdx==4)?parseInt(jsonData.goQty)*parseInt(jsonData.goPrice):(colIdx==5)?jsonData.goDiscount:jsonData.goCode;
		record.appendChild(item);
		}
		list.appendChild(record);
		document.getElementsByName("prCode")[0].placeholder="상품코드 입력";	
		orderList();
		orderList2();			
	}else if(jsonData.goStatus=="T"){
		document.getElementsByName("prCode")[0].placeholder="일시품절 상품";			
	}else if(jsonData.goStatus=="D"){
		document.getElementsByName("prCode")[0].placeholder="판매불가 상품";			
	}else{
		document.getElementsByName("prCode")[0].placeholder="존재하지 않는 상품코드";
	}	
		selectBotton(list.lastChild);		
}
function createDiv(name,className){
	const div=document.createElement("div"); //<div></div>
	div.setAttribute("name",name);
	div.setAttribute("class",className);
	
	return div;
}
function orderList(){
	const quantity=document.getElementsByClassName("prQuantity");
	const price=document.getElementsByClassName("prPrice");
	const discount=document.getElementsByClassName("prDiscount");
	let sumObj=document.getElementsByName("sum");
	let sum=[null,null,null];
	for(idx=0;idx<quantity.length;idx++){	//적용하려는 곳에 공백 있으면 innerText 적용이 안됨
		sum[0]+=parseInt(quantity[idx].innerText);
		sum[1]+=parseInt(price[idx].innerText)*parseInt(quantity[idx].innerText);
		sum[2]+=parseInt(discount[idx].innerText);
	}
	for(idx=0;idx<sum.length;idx++){
		sumObj[idx].innerText=sum[idx];
	}
	
}
function orderList2(){
	const quantity=document.getElementsByClassName("prQuantity");
	const price=document.getElementsByClassName("prPrice");
	const discount=document.getElementsByClassName("prDiscount");
	let sumObj=document.getElementsByClassName("box");
	let sum=[null,null,null];
	for(idx=0;idx<quantity.length;idx++){	
		sum[0]+=parseInt(price[idx].innerText)*parseInt(quantity[idx].innerText);
		sum[1]+=parseInt(discount[idx].innerText);
		sum[2]+=(parseInt(price[idx].innerText)*parseInt(quantity[idx].innerText))-(parseInt(discount[idx].innerText));
	}
	for(idx=0;idx<sum.length;idx++){
		sumObj[idx].innerText=sum[idx];
	}
	
}
//hasChildNodes()
function delBotton(objName){
	if(currentRecord!=null){
		currentRecord.remove();
		no--;
		resetNo();
	}else{
		const obj=document.getElementById(objName);
		obj.removeChild(obj.lastChild);
		no--;
	}
	currentRecord=null;
	orderList();
	orderList2();
}
function selectBotton(obj){
	if(currentRecord!=null){
		currentRecord.style.color="black";
		currentRecord=null;
	}
	currentRecord=obj;
	obj.style.color="red";
}
function modQty(qty,objName){
	let childList;
	const obj=document.getElementById(objName);
	if(currentRecord!=null){
		childList=currentRecord.childNodes;
	}else{
		childList=obj.lastChild.childNodes;
	}	
	quantity=parseInt(childList[2].innerText)+qty;
	if(quantity!=0){	
		childList[2].innerText=(quantity).toString();
		childList[4].innerText=(quantity*parseInt(childList[3].innerText)).toString();
		childList[5].innerText=childList[5].innerText/(quantity-qty)*quantity;	
	}else{
		delBotton(objName);
	}
	orderList();
	orderList2();
}
function resetNo(){
	let list=document.getElementById("list").childNodes;
	for(index=0;index<list.length;index++){
		let subList=list[index].childNodes;
		subList[0].innerText=index+1;
	}
}	
function cancle(msg){
	alert(msg);
	const list=document.getElementById("list");
	while(list.hasChildNodes()){
		list.removeChild(list.lastChild);
	}
	no=0;
	orderList();
	orderList2();
}