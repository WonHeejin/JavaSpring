<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Main Page :: Dynamic :: AccessInfo</title>
 <script src="resources/js/resource.js"></script>
<script>
	//get방식은 location.href를 이용하는게 더 간편하다.
 function accessOut(stCode,elCode){
	location.href="AccessOut?stCode="+stCode+"&elCode="+elCode;
} 
 function mouseOver(obj){
/* 	 let styleName = (obj.id == "mgt")? "mgtOver": "salesOver";
		obj.className = styleName; */
		obj.style.color = "#FFFFFF"
		obj.style.backgroundColor=(obj.id == "mgt")? "#7F7CC9": "#EDCE7A"
 }
 function mouseLeave(obj){
	 let fColor = (obj.id == "mgt")? "#6D6AB7": "#E0B94F";
	/*  obj.className="select"; */
	 obj.style.color = fColor;
	 obj.style.backgroundColor="#F6F6F6";
 }
 function moveService(action, stCode, elCode){
	 const form= makeForm("",action,"post");
	 const pStCode = makeInputElement("hidden","stCode",stCode,"");
	 const pElCode = makeInputElement("hidden","elCode",elCode,"");
	
 	 form.appendChild(pStCode);
 	 form.appendChild(pElCode);
 
 	 document.body.appendChild(form);
 	 form.submit();
 }
</script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css"/>
<style>
	body {font-family: 'Gowun Dodum', sans-serif;}
	span {margin:5px;}
	#info {
	width: 60%;
	height: 25px;
	line-height: 25px;
	background-color: #81BA7B;
	color: #3A3A3A;
	font-size: 10pt;
	text-align: right;
	float: right;
	padding: 0px 4px;
}
	#infoLogo {width:100%;height: 25px;background:#81BA7B;}
#logo {
	padding: 0px 4px;
	width: 10%;
	height: 25px;
	line-height: 25px;
	background-color: #81BA7B;
	color: #3A3A3A;
	font-size: 10pt;
	text-align: left;
	font-weight: 800;
	font-size: 15px;
	float: left;
}
	 
	.select,.mgtOver,.salesOver {width:40%;height:200px;text-align:center;
			 					 line-height:200px;font-size:26pt;font-weight:800;
			 					 background-color: #EDCE7A}	 
	
	.btn {width:70px; height:20px; background-color:#EAEAEA;font-family: 'Gowun Dodum', sans-serif;
		  border: 0px; color:#3A3A3A; font-size:12px;cursor:pointer;}	
	#buttonWrap {width:55%;height:200px;
				 position:absolute; top:50%; left:50%; transform: translate(-50%, -50%);}
	#mgt {border:10px solid #7F7CC9;float:left;
		  background-color:#F6F6F6;color: #6D6AB7;cursor:pointer;}
	.mgtOver {background-color: #7F7CC9;}
	#sales {border:10px solid #EDCE7A;float:right;
			background-color:#F6F6F6;color: #E0B94F;cursor:pointer;}
	.salesOver {background-color: #00B700;
						border: 2px solid #00B700;}
	#footer {position:absolute; top:93%;width: 98.7%; height: 25px; line-height: 30px;
		  background-color: #81BA7B; border:2px solid #81BA7B;
		  color: #3A3A3A; font-size:12pt;
		  text-align:right;}  	 
</style>
</head>
<body>
	<div id="infoLogo">
		<div id="logo">WEB POS</div>
	<div id="info">
		${accessInfo.stName}(${accessInfo.stCode}) <span>${accessInfo.elName}(${accessInfo.elCode})</span>
		최근 접속 일자 ${accessInfo.lastTime} <span><input type="button"
			class="btn"
			onClick="accessOut('${accessInfo.stCode}','${accessInfo.elCode}')"
			value="로그아웃" /></span>
	</div>
	</div>
	<div id="buttonWrap">
		<div id="mgt" class="select" onMouseOver="mouseOver(this)" onMouseOut="mouseLeave(this)" onClick="moveService('Management','${accessInfo.stCode}','${accessInfo.elCode}')">Management</div>
		<div id="sales" class="select" onMouseOver="mouseOver(this)" onMouseOut="mouseLeave(this)" onClick="moveService('Sales','${accessInfo.stCode}','${accessInfo.elCode}')">Sales</div>
	</div>
		<input type="hidden" name="stCode" value="${accessInfo.stCode}"/>
		<input type="hidden" name="elCode" value="${accessInfo.elCode}"/>
	<div id="footer">made by ${name}</div>	
</body>
</html>