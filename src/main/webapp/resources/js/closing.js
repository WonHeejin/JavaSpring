
document.onkeydown = function(e){
	// 새로고침 Ctrl+R Ctrl+N
	const k = e.keyCode;
	//F5
	if(k == 116 || (e.ctrlKey && k == 82)){
		const refStCode=document.getElementById("refStCode").value;
		const refElCode=document.getElementById("refElCode").value;
		refresh(refStCode,refElCode);
		e.preventDefault();
		e.returnValue = '';
	}
	
}

function refresh(stCode, elCode) {
	
	const form = makeForm("", "Refresh", "post");
	const clientData = [makeInputElement("hidden", "stCode", stCode, ""), makeInputElement("hidden", "elCode", elCode, ""), makeInputElement("hidden", "publicIp", publicIp, "")];
	
	for(idx=0; idx<clientData.length;idx++){
		form.appendChild(clientData[idx]);
	}		

	document.body.appendChild(form);
	form.submit();
}






/**
 * Window의 Closing 인식 및 처리
 */

/* keydown 인식 :: 전역화 
window.addEventListener('click', function(e){
		console.log(e.clientY);
});

window.addEventListener('beforeunload', function(event){
	console(document.readyState);
	//closeWindow(e, '1006', '1006');
	event.preventDefault();
	event.returnValue = '';
});


window.onbeforeunload = function(e){
	//console.log(document.readyState);
	accessOut('1006','1006');
	e.preventDefault();
	e.returnValue = '';
}
*/

