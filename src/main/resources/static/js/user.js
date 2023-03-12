function confirmDelete(btnConfirm){
	if (confirm(btnConfirm.getAttribute("data-message-confirm-delete"))) {
	    var form = document.createElement("form");
	    var userIdEle = document.createElement("input"); 
	    
	    form.method = "POST";
	    form.action = btnConfirm.getAttribute("data-url");
	    document.body.appendChild(form);
	    form.submit();
	}
}

function changeStatePasswordField(checkbox){
	let passwordField = document.getElementsByClassName("input_password").item(0);
	if(checkbox.checked){
		passwordField.removeAttribute("readonly");
	}
	else{
		passwordField.setAttribute("readonly", "readonly");
	}
}
