function sendAjaxJson(action, data, fn, content) {
	const ajax = new XMLHttpRequest();
	ajax.onreadystatechange = function() {
		if (ajax.readyState == 4 && ajax.status == 200) {
			window[fn](ajax.responseText); //json 데이터 parse하면 배열로 접근									
		}
	};
	ajax.open("post", action, true);
	if(content){
		ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
	}
	
	ajax.send(data);
}
function toHTMLfromEMP(data) {
	const jsonData = JSON.parse(data);
	let message = "<table>";
	message += "<tr>";
	message += "<td>매장코드</td><td>직원코드</td><td>직원이름</td><td>등급</td>";
	message += "</tr>";

	for (let index = 0; index < jsonData.length; index++) {
		message += "<tr><td>";
		message += jsonData[index].stCode;
		message += "</td>";
		message += "<td>";
		message += jsonData[index].elCode;
		message += "</td>";
		message += "<td>";
		message += jsonData[index].elName;
		message += "</td>";
		message += "<td>";
		message += jsonData[index].elLevel;
		message += "</td></tr>";

	}
	message += "</table>";
	document.getElementById("ajaxData").innerHTML = message;
}
function toHTMLfromMM(data) {
	const jsonData = JSON.parse(data);
	let message = "<table>";
	message += "<tr>";
	message += "<td>회원코드</td><td>회원이름</td>";
	message += "</tr>";

	for (let index = 0; index < jsonData.length; index++) {
		message += "<tr><td>";
		message += jsonData[index].mmCode;
		message += "</td><td>";
		message += jsonData[index].mmName;
		message += "</td></tr>";

	}
	message += "</table>";
	document.getElementById("ajaxData").innerHTML = message;
}
let jsonData;
function toHTMLfromGO(data) {
	while(ajaxData.hasChildNodes()){
		ajaxData.removeChild(ajaxData.lastChild);
	}
	jsonData = JSON.parse(data);
	let div;
	for(idx=0;idx<jsonData.length;idx++){
		div=document.createElement("div");
		div.setAttribute("onClick","disGoods("+idx+")");
		let span=document.createElement("span");
		span.innerText=jsonData[idx].goCode;
		div.appendChild(span);
		let span1=document.createElement("span");
		span1.innerText=jsonData[idx].goName;
		div.appendChild(span1);
		let span2=document.createElement("span");
		span2.innerText=jsonData[idx].goPrice;
		div.appendChild(span2);
		let span3=document.createElement("span");
		span3.innerHTML=jsonData[idx].goDiscount;
		div.appendChild(span3);	
		ajaxData.appendChild(div);	
	}
	
}function disGoods(idx){

	let mheader=document.getElementById("mheader");
	let mbody=document.getElementById("mbody");
	let command=document.getElementsByName("command")[0];
	/* command button 셋팅 */
	command.setAttribute("onClick", "updGoodsInfo("+idx+")");
	command.setAttribute("value","상품정보 수정요청");
	/* Modal Header 교체 */
	let status=(jsonData[idx].goStatus=="S")?"판매가능":(jsonData[idx].goStatus=="D")?"판매불가":"일시품절";
	mheader.innerText=jsonData[idx].goName+"상세정보";
	/* Form Data 생성 */
	let obj="<table><tr><td>"+jsonData[idx].goCode+"</td><td>상품명</td><td colspan='3'>"+jsonData[idx].goName+"</td></tr>"
			+"<tr><td rowspan='3'><img src=\"resources/"+jsonData[idx].goImgLoc+"\"></td><td>매입가격</td><td>"+jsonData[idx].goCost+"</td><td>판매가격</td><td>"+jsonData[idx].goPrice+"</td></tr>"
			+"<tr><td>할인율</td><td>"+jsonData[idx].goDiscount+"%</td><td>재고수량<br>(단위:개)</td><td>"+jsonData[idx].goStock+"</td></tr>"
			+"<tr><td colspan='4'>"+status+"</td></tr></table>";
	mbody.innerHTML=obj;
	openModal();
}
/* 상품정보 업데이트 */
function updGoodsInfo(idx){
	/* form정보 갱신*/
	let form=document.getElementsByName("dynamicFormData")[0];
	form.setAttribute("action","final/UpdGoodsInfo");
	form.setAttribute("method","post");
	form.setAttribute("enctype","multipart/form-data");
	
	/* 파일정보 가져오기 */
	let files = document.getElementsByName("file")[0].files;
	let formData=new FormData(form);
	formData.append("goCode",jsonData[idx].goCode);
	formData.append("goName",jsonData[idx].goName);
	formData.append("goCost",jsonData[idx].goCost);
	formData.append("goPrice",jsonData[idx].goPrice);
	formData.append("goStatus",jsonData[idx].goStatus);
	formData.append("goDiscount",jsonData[idx].goDiscount);
	formData.append("goImgLoc",files.length>0?"images/"+jsonData[idx].goCode+"."+files[0].type.substring(files[0].type.indexOf("/")+1):jsonData[idx].goImgLoc);
	formData.append("goStock",jsonData[idx].goStock);
	
	sendAjaxJson(form.getAttribute("action"), formData, "compliteUpdGoodsInfo", false);
}
function compliteUpdGoodsInfo(data){
	closeModal();
	toHTMLfromGO(data);
}
/*Modal*/
function openModal() {
	uploadFile.innerHTML="<input type='file' class='mbtn' name='file' value='파일수정'/>";
	let container = document.getElementById("container");
	container.style.filter = "alpha(Opacity=50)";
	container.style.display = "block";
}
function closeModal() {
	let container = document.getElementById("container");
	container.style.display = "none";
}
