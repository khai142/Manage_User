function hideShowJapanInfor() {
	let elements = document.querySelectorAll(".japan_infor");
	elements.forEach(element => {
		element.classList.toggle("show");
	});
	
	let showJPEle = document.getElementById("showJP");
	if(showJPEle != null){
		showJPEle.value = (elements[0].classList.contains("show")) ? 'show' : 'hide';
	}
}

function confirmDelete(userId, msg){
	if (confirm(msg)) {	
	    var form = document.createElement("form");
	    var userIdEle = document.createElement("input"); 
	    
	    form.method = "POST";
	    form.action = "DeleteUser.do";   
	    
	    userIdEle.value=userId;
	    userIdEle.name="userId";
	    userIdEle.type='hidden';
	    form.appendChild(userIdEle);  
	
	    document.body.appendChild(form);
	    form.submit();
	}
}