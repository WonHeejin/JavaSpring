<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Management</title>
<script src="resources/js/resource.js"></script>
<script src="resources/js/closing.js"></script>
<script src="resources/js/management.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
	
	function getList(action, pStCode) {
		let fn = null;
		let jsonData = [];
		jsonData.push({
			stCode : pStCode
		});
		if (action == "mgr/EmpList") {
			fn = "toHTMLfromEMP";
		} else if (action == "mgr/MMList") {
			fn = "toHTMLfromMM";
		} else {
			fn = "toHTMLfromGO";
		}
		const data = JSON.stringify(jsonData);
		sendAjaxJson(action, data, fn, false);
	}
	function init(objName) {
		/* let container=document.getElementById("container");
		container.style.display="none";	 */	
	}
	/*JQUERY를 이용한 Ajax방식*/
	function getAjaxJsonUsingJquery(action, clientData, fn) {
		$.ajax({
			async : true,//true:비동기, false:동기 안써도 기본값이 true
			type : "post",//기본값 :get
			url : action,//클라이언트에서 서버로 요청한 값
			data : clientData,//클라이언트에서 서버로 요청하는 데이터
			contentType : "application/x-www-form-urlencoded; charset=utf-8",//클라이언트가 요청한 데이터의 종류, 안써도 기본값으로 되어있음
			dataType : "json",//서버에서 클라이언트로 전달할 데이터의 종류, 기본값이 텍스트
			success : function(jsonObject) {//성공했을 때 실행되는 이벤트
				//alert("AJAX 통신 성공으로 서버 데이터가 도착했습니다.");
				//alert(JSON.stringify(jsonObject));
				window[fn](jsonObject);
			},
		/*		beforeSend : function(){
		 // AJAX 통신 요청 전 호출되는 이벤트
		 alert("AJAX 통신을 시작합니다.");
		 },
		 complete : function(){
		 // AJAX 통신이 완료 될 때 호출되는 이벤트
		 alert("AJAX 통신을 종료합니다.");
		 },
		 err : function(error){
		 // 통신 실패시 호출되는 이벤트
		 alert("AJAX 통신실패했습니다.");
		 },
		 fail: function(){
		 //통신 응답이 없을 때 실행되는 이벤트
		 alert("서버에서 응답이 없습니다.");
		 },		
		 timeout : 10000
		 //서버 응답을 기다리는 최대시간. 이 시간 안에 응답 없으면 fail일 경우의 이벤트 실행. 1000이 1초
		 */
		});
	}
	/* 	function getList(action, stCode) {
	 let fn=null;
	 const data= "stCode="+encodeURIComponent(stCode);
	 if(action=="mgr/EmpList"){
	 fn="toHTMLfromEMP";
	 }else if(action=="mgr/MMList"){
	 fn="toHTMLfromMM";
	 }
	 getAjaxJsonUsingJquery(action, data,fn);
	 }
	 */
	function getEmpMax(action, pStCode) {
		let jsonData = [];
		jsonData.push({
			stCode : pStCode
		});
		const data = JSON.stringify(jsonData);
		sendAjaxJson(action, data, "getEmpForm", false);
	}
	function getEmpForm(maxEmp) {
		const max = parseInt(maxEmp);
		let form = "<input type=\"text\" name=\"stCode\" value=\""
				+ document.getElementById("refStCode").value + "\" readOnly />";
		form += "<input type=\"text\" name=\"elCode\" value=\"" + (max + 1)
				+ "\" readOnly />";
		form += "<input type=\"text\" name=\"elName\" placeholder=\"직원이름\" />";
		form += "<input type=\"password\" name=\"elPassword\" placeholder=\"비밀번호\" />";
		form += "<input type=\"text\" name=\"elLevel\" placeholder=\"등급코드\" />";
		form += "<input type=\"button\" value=\"직원등록\" onClick=\"RegEmp('"
				+ document.getElementById("refStCode").value + "','"
				+ (max + 1) + "')\" />";
		document.getElementById("ajaxData").innerHTML = form;
	}
	function RegEmp(pStCode, pElCode) {
		let jsonData = [];
		jsonData.push({
			stCode : pStCode,
			elCode : pElCode,
			elName : document.getElementsByName("elName")[0].value,
			elPassword : document.getElementsByName("elPassword")[0].value,
			elLevel : document.getElementsByName("elLevel")[0].value
		});
		const data = JSON.stringify(jsonData);
		sendAjaxJson("mgr/RegEmp", data, "loadList", false);
	}
	function loadList(data) {
		if (data.substr(0, 1) == "등") {
			alert(data);
		} else {
			document.getElementById(data).click();
		}
	}
	function getMmbForm(action) {
		getAjaxData(action, null);
	}
	function RegMmb(cuCode) {
		const cuName = document.getElementsByName("cuName")[0].value;
		const cuPhone = document.getElementsByName("cuPhone")[0].value;
		const cuClCode = document.getElementsByName("cuClCode")[0].value;
		const data = "cuCode=" + encodeURIComponent(cuCode) + "&cuName="
				+ encodeURIComponent(cuName) + "&cuPhone="
				+ encodeURIComponent(cuPhone) + "&cuClCode="
				+ encodeURIComponent(cuClCode);
		getAjaxData("RegMmb", data);

	}
	function getGoForm(action) {
		getAjaxData(action, null);
	}
	function RegGo() {
		const goCode = document.getElementsByName("goCode")[0].value;
		const goName = document.getElementsByName("goName")[0].value;
		const goCost = document.getElementsByName("goCost")[0].value;
		const goPrice = document.getElementsByName("goPrice")[0].value;
		const goStocks = document.getElementsByName("goStocks")[0].value;
		const goDiscount = document.getElementsByName("goDiscount")[0].value;
		const goCaCode = document.getElementsByName("goCaCode")[0].value;
		const goState = document.getElementsByName("goState")[0].value;
		const data = "goCode=" + encodeURIComponent(goCode) + "&goName="
				+ encodeURIComponent(goName) + "&goCost="
				+ encodeURIComponent(goCost) + "&goPrice="
				+ encodeURIComponent(goPrice) + "&goStocks="
				+ encodeURIComponent(goStocks) + "&goDiscount="
				+ encodeURIComponent(goDiscount) + "&goCaCode="
				+ encodeURIComponent(goCaCode) + "&goState="
				+ encodeURIComponent(goState);
		getAjaxData("RegGo", data);
	}
	function getModEmpForm(action, stCode) {
		const data = "stCode=" + encodeURIComponent(stCode);
		getAjaxData(action, data);
	}
	function activateEmp(obj, idx) {
		const emPassword = document.getElementsByName("emPassword")[idx];
		const work = document.getElementsByName("work")[idx];
		const emBtn = document.getElementsByName("emBtn")[idx];
		if (obj.value == "수정") {
			obj.value = "수정취소";
			emPassword.readOnly = false;
			work.disabled = false;
			emBtn.disabled = false;

		} else {
			obj.value = "수정";
			emPassword.readOnly = true;
			work.disabled = true;
			emBtn.disabled = true;
		}
	}
	function updEmp(stCode, elCode, idx) {
		const emPassword = document.getElementsByName("emPassword")[idx];
		const work = document.getElementsByName("work")[idx];

		const data = "stCode=" + encodeURIComponent(stCode) + "&elCode="
				+ encodeURIComponent(elCode) + "&emPassword="
				+ encodeURIComponent(emPassword.value) + "&emState="
				+ encodeURIComponent(work.value);
		getAjaxData("ModEmp", data);

	}

	function getModForm(action) {
		getAjaxData(action, null);
	}
	function activateMmb(obj, idx) {
		const work = document.getElementsByName("work")[idx];
		const mmbBtn = document.getElementsByName("mmbBtn")[idx];
		if (obj.value == "수정") {
			obj.value = "수정취소";
			work.disabled = false;
			mmbBtn.disabled = false;
		} else {
			obj.value = "수정";
			work.disabled = true;
			mmbBtn.disabled = true;
		}
	}
	function updMmb(cuCode, idx) {
		const cuClCode = document.getElementsByName("work")[idx];

		const data = "cuCode=" + encodeURIComponent(cuCode) + "&cuClCode="
				+ encodeURIComponent(cuClCode.value);
		getAjaxData("ModMmb", data);
	}
	function activateGo(obj, idx) {
		const go1 = document.getElementsByName("goCost")[idx];
		const go2 = document.getElementsByName("goPrice")[idx];
		const go3 = document.getElementsByName("goDiscount")[idx];
		const go4 = document.getElementsByName("goStocks")[idx];
		const st = document.getElementsByName("st")[idx];
		const ca = document.getElementsByName("ca")[idx];
		const goBtn = document.getElementsByName("goBtn")[idx];
		if (obj.value == "수정") {
			obj.value = "수정취소";
			go1.readOnly = false;
			go2.readOnly = false;
			go3.readOnly = false;
			go4.readOnly = false;
			st.disabled = false;
			ca.disabled = false;
			goBtn.disabled = false;
		} else {
			obj.value = "수정";
			go1.readOnly = true;
			go2.readOnly = true;
			go3.readOnly = true;
			go4.readOnly = true;
			st.disabled = true;
			ca.disabled = true;
			goBtn.disabled = true;
		}
	}
	function updGo(goCode, idx) {
		const goCost = document.getElementsByName("goCost")[idx].value;
		const goPrice = document.getElementsByName("goPrice")[idx].value;
		const goStocks = document.getElementsByName("goStocks")[idx].value;
		const goDiscount = document.getElementsByName("goDiscount")[idx].value;
		const goCaCode = document.getElementsByName("ca")[idx].value;
		const goState = document.getElementsByName("st")[idx].value;
		const data = "goCode=" + encodeURIComponent(goCode) + "&goCost="
				+ encodeURIComponent(goCost) + "&goPrice="
				+ encodeURIComponent(goPrice) + "&goStocks="
				+ encodeURIComponent(goStocks) + "&goDiscount="
				+ encodeURIComponent(goDiscount) + "&goCaCode="
				+ encodeURIComponent(goCaCode) + "&goState="
				+ encodeURIComponent(goState);
		getAjaxData("ModGo", data);
	}
	function getAjaxJson(action, data, fn) {
		const ajax = new XMLHttpRequest();
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4 && ajax.status == 200) {
				window[fn](JSON.parse(ajax.responseText)); //json 데이터 parse하면 배열로 접근			
			}
		};
		ajax.open("post", action, true);
		ajax.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		ajax.send(data);
	}
	function getThisMonthSalesInfo(stCode) {
		data = "stCode=" + encodeURIComponent(stCode);
		getAjaxJson("MSI", data, "toHTMLfromMSI");
	}
	function toHTMLfromMSI(jsonData) {

		let message = "<table>";
		message += "<tr>";
		message += "<td>날짜</td><td>매장명</td><td>매출액</td><td>구매원가</td><td>순이익</td>";
		message += "</tr>";

		for (let index = 0; index < jsonData.length; index++) {
			message += "<tr><td>";
			message += jsonData[index].monthly;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].stName;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].amount;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].goCost;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].profit;
			message += "</td></tr>";
		}

		message += "</table>";
		document.getElementById("ajaxData").innerHTML = message;
	}
	function getGoodsSalesInfo(stCode) {
		const data = "stCode=" + encodeURIComponent(stCode);
		getAjaxJson("GSI", data, "toHTMLfromGSI");
	}
	function toHTMLfromGSI(jsonData) {
		let message = "<table>";
		message += "<tr>";
		message += "<td>날짜</td><td>매장명</td><td>상품코드</td><td>매출액</td><td>구매원가</td><td>순이익</td>";
		message += "</tr>";

		for (let index = 0; index < jsonData.length; index++) {
			message += "<tr><td>";
			message += jsonData[index].monthly;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].stName;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].goCode;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].amount;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].goCost;
			message += "</td>";
			message += "<td>";
			message += jsonData[index].profit;
			message += "</td></tr>";
		}

		message += "</table>";
		document.getElementById("ajaxData").innerHTML = message;
	}

	
</script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<link rel="stylesheet" type="text/css" href="resources/css/management.css" />

</head>
<body onLoad="initIp('${msg}')">
	<div id="infoLogo">
		<div id="logo">WEB POS</div>
		<div id="info">
			${accessInfo.stName}(${accessInfo.stCode}) <span>${accessInfo.elName}(${accessInfo.elCode})</span>
			최근 접속 일자 ${accessInfo.lastTime} <span><input type="button"
				class="btn"
				onClick="accessOut('${accessInfo.stCode}','${accessInfo.elCode}')"
				value="로그아웃" /></span> <input type="hidden" id="refStCode"
				value='${accessInfo.stCode}' /> <input type="hidden" id="refElCode"
				value='${accessInfo.elCode}' />
		</div>
	</div>

	<div id="index">
		<div class="menuContainer">
			<h2>${accessInfo.stName}</h2>
			<section class="menuContainer">
				<article class="managements Active">
					<!-- Open&Close, DashBoard -->
					<p class="menuTitle">Daily Report</p>
				</article>
				<article class="managements">
					<p class="menuTitle" id="salesManagements">영업관리</p>
					<div class="items">
						<p>
							<span onClick="getThisMonthSalesInfo('${accessInfo.stCode}')">금월매출정보</span>
						</p>
						<p>
							<span onClick="getGoodsSalesInfo('${accessInfo.stCode}')">상품매출정보</span>
						</p>
						<p>요일매출정보</p>
						<p>회원매출정보</p>
					</div>
				</article>
				<article class="managements">
					<p class="menuTitle" id="empManagements">직원관리</p>
					<div class="items">
						<p>
							<span id="EmpList"
								onClick="getList('mgr/EmpList','${accessInfo.stCode}')">직원리스트</span>
						</p>
						<p>
							<span
								onClick="getEmpMax('mgr/RegEmpForm','${accessInfo.stCode}')">직원정보등록</span>
						</p>
						<p>
							<span
								onClick="getModEmpForm('ModEmpForm','${accessInfo.stCode}')">직원정보수정</span>
						</p>
					</div>
				</article>
				<article class="managements">
					<p class="menuTitle" id="cuManagements">회원관리</p>
					<div class="items">
						<p>
							<span id="MmbList"
								onClick="getList('mgr/MMList','${accessInfo.stCode}','${accessInfo.elCode}')">회원리스트</span>
						</p>
						<p>
							<span onClick="getMmbForm('RegMmbForm')">회원정보등록</span>
						</p>
						<p>
							<span onClick="getModForm('ModMmbForm')">회원정보수정</span>
						</p>
					</div>
				</article>
				<article class="managements">
					<p class="menuTitle" id="goManagements">상품관리</p>
					<div class="items">
						<p>
							<span id="GoList"
								onClick="getList('final/GoList','${accessInfo.stCode}','${accessInfo.elCode}')">상품리스트</span>
						</p>
						<p>
							<span onClick="getGoForm('RegGoForm')">상품정보등록</span>
						</p>
						<p>
							<span onClick="getModForm('ModGoForm')">상품정보수정</span>
						</p>
					</div>
				</article>
			</section>
		</div>
	</div>
	<div id="ajaxData">${list}</div>
	<div id="footer" onClick="openModal(this)">made by jean</div>
	<form name='dynamicFormData'>
	<!-- The Modal Background -->
	<div class="container" id="container">
		<div class="mdialog">
			<div class="mcontent">
				<!-- Modal Header -->
				<div class="mheader">
					<h4 id="mheader" class="mtitle">Modal Header</h4>
				</div>
				<!-- Modal body -->
				<div class="mbody" id="mbody" value="">Modal Body</div>
				<!-- Modal footer -->
				<div class="mfooter">
					<input type="button" class="mbtn" value="닫기" onclick="closeModal(this)"/>
					<input type="button" class="mbtn" name="command"/>
					<span id="uploadFile"><input type="file" class="mbtn" name="file" value="파일수정" /></span>>
				</div>
			</div>
		</div>
	</div>
	</form>
</body>
<script>
	/* 메뉴에 관련 항목을 클래스 이름으로 연결 */
	let menuZone = document.getElementsByClassName("managements");
	let menuItems = document.getElementsByClassName("items");
	let menuTitle = document.getElementsByClassName("menuTitle");
	/* menuZone의 Tag는 표식인 <article>로써 기본 이벤트가 없으므로 이벤트를 부여
	:addEventListener('이벤트명',function(e){이벤트 발생 시 실행 구문}) */
	for (let titleIdx = 0; titleIdx < menuTitle.length; titleIdx++) {
		menuTitle[titleIdx].addEventListener("click", function(e) {
			for (let zoneIdx = 0; zoneIdx < menuZone.length; zoneIdx++) {
				menuZone[zoneIdx].classList.remove("Active");
			}
			e.target.parentNode.classList.add("Active");
			activateItems();
		});
	}
	/* 각 메뉴의 items가 안 보이도록 display: none
	주메뉴의 items만 보이도록 display: block */
	function activateItems() {
		for (let itemsIdx = 0; itemsIdx < menuItems.length; itemsIdx++) {
			menuItems[itemsIdx].style.display = "none";
		}
		const activeItems = document
				.querySelector(".managements.Active .items");
		if (activeItems != null) {
			activeItems.style.display = "block";
		}
	}
	activateItems();
</script>
</html>