<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Multi-Part Upload</title>
<script src="resources/js/resource.js"></script>
<script>
	function uploadFile(formName) {
		 const form = document.getElementsByName(formName)[0];
		/*파일 유효성 검사*/
		
		/* 서버 전송 */
		form.submit();
	}
	function ajaxUploadFile(formName){
		 const form = document.getElementsByName(formName)[0];
		 let formData=new FormData(form);
		 
		 /* 서버 전송 */
		ajaxFormData("/MultiPart2",formData,"callback");
	}
	function callback(){
		alert("Ajax File Transfer");
	}
	
	function ajaxFormData(action,data,fn){
		const ajax = new XMLHttpRequest();
			ajax.onreadystatechange = function() {
				if ( ajax.readyState== 4 && ajax.status == 200) {		
					window[fn]();						
				}
			};
			ajax.open("post", action, true);
			//ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded");	
			ajax.send(data);
				
	}
	
</script>
<link rel="stylesheet" type="text/css" href="resources/css/common.css" />
<style>
body	{font-family: 'Black Han Sans', sans-serif;}
table {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 52%;
}

td {
	text-align: center;
}

input {
	width: 98%;
	height: 40px;
	text-align: center;
	font-size: 15pt;
	color: #0BC904;
	font-family: 'Black Han Sans', sans-serif;
}

#state {
	text-align: left;
	padding-left: 20px;
}

#ips {
	text-align: right;
	padding-right: 20px;
}

#btn {
	background-color: #0BC904;
	color: #FFFFFF;
	width: 99%;
	height: 60px;
	border: 1px solid #0BC904;
	cursor: pointer;
}

.title {
	height: 80px;
	font-size: 20pt;
	background-color: #0BC904;
	color: #FFFFFF;
}

.line {
	height: 60px;
}
</style>
</head>
<body onLoad="initIp('${msg}')">						<!-- 전달할 파일 형식 multipart 꼭 써줘야함 -->
	<form name="upload" action="MultiPart" method="post" enctype="multipart/form-data">
		<table>
			<tr class="title">
				<td>File Upload</td>
			</tr>
			<tr class="line">
				<td><input type="file" name="files" multiple/></td>
			</tr>
			<tr>
				<td><input type="text" name="fileTitle" placeholder="제목 입력" /></td>
			</tr>
			<tr>
				<td><input type="text" name="fileDesc" placeholder="내용 입력" /></td>
			</tr>
			<tr>
				<td><input type="button" id="btn" value="파일전송"
					onClick="uploadFile('upload')" /></td>
			</tr>
		</table>
	</form>
	
	<form name="ajaxUpload" action="MultiPart2" method="post" enctype="multipart/form-data">
		<table>
			<tr class="title">
				<td>File Upload</td>
			</tr>
			<tr class="line">
				<td><input type="file" name="files" multiple/></td>
			</tr>
			<tr>
				<td><input type="text" name="fileTitle" placeholder="제목 입력" /></td>
			</tr>
			<tr>
				<td><input type="text" name="fileDesc" placeholder="내용 입력" /></td>
			</tr>
			<tr>
				<td><input type="button" id="btn" value="파일전송"
					onClick="ajaxUploadFile('ajaxUpload')" /></td>
			</tr>
		</table>
	</form>
</body>
</html>