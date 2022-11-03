<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MainPage::static</title>	
 <script src="resources/js/resource.js"></script>
<script>

/*1. 서버와의 통신 제어
 * 		- HTML 개체의 제어 : form의 동적 생성
 *2. 데이터의 유효성 검사(ORACLE의 CHECK 제약조건 역할)
 *3. AJAX(비동기 타입..?) + Json(데이터타입) >> 앱 때문에 자주 사용
 */
function authentication(){

	 //1. HTML 개체와의 연결 : name, id 속성 이용
	
	 //2. 사용자 데이터 수집
	 const form = document.getElementsByName("login")[0];
 	 
	 const message=["상점코드 입력", "직원코드 입력", "비밀번호 입력","PublicIp 조회 실패"];
 	 const userData=[document.getElementsByName("stCode")[0],
 		 			 document.getElementsByName("elCode")[0],
 		 			 document.getElementsByName("elPassword")[0],
 		 			publicIp];
	 //3. 유효성 검사
	 for(let index=0;index<userData.length;index++){
		 if(!isEmpty(userData[index])){
			 alert(message[index]);
			 return;
		 }
	 }
	 //4. 서버로 전송
	 const hidden = makeInputElement("hidden", "publicIp", publicIp, "");
	 form.appendChild(hidden);
	 form.submit();
}

</script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css"/>
<style>
	table {position:absolute; top:50%; left:50%; transform: translate(-50%, -50%); width:52%}
	td {text-align:center;}
	input {width:98%; height:48px; text-align:center;
			font-size:14pt; color:#81BA7B;}
	#state {text-align:justify; padding-left:20px;}
	#ips {text-align:right; padding-right:20px;}
	#btn {background-color: #81BA7B; color:#EAEAEA; height: 78px;
			border:2px solid #81BA7B; font-weight:800;}
	.title {height:80px;
			background-color: #81BA7B; color:#EAEAEA; height: 78px;
			border:2px solid #81BA7B; font-weight:800;font-size:14pt}
	.line {height:70px;}		
</style>
</head>
<body onLoad="initIp('${msg}')">
<form action="Access" name="login"method="post">
	<table>
		<tr class="title">
			<td  colspan="2">WEB POS</td>
		</tr>
		<tr class="line">
			<td colspan="2">
				<input type="text" name="stCode"placeholder="매장코드"/>
			</td>	
		</tr>
		<tr class="line">
			<td colspan="2">
				<input type="text" name="elCode"placeholder="직원코드"/>
			</td>	
		</tr>
		<tr class="line">
			<td colspan="2">
				<input type="password" name="elPassword"placeholder="비밀번호"/>
			</td>	
		</tr>
		<tr class="line">
			<td id="state">로그인상태유지</td>
			<td id="ips">IP보안</td>
		</tr>	
		<tr class="title" >
			<td colspan="2">
				<input type="button" id="btn" onClick="authentication()" value="로그인"/>
			</td>	
		</tr>
	</table>
</form>	
</body>
</html>