<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/loginform.css"> 
	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
	<link href='http://fonts.googleapis.com/css?family=Varela+Round' rel='stylesheet' type='text/css'>
	<link href="images/favicon.ico" rel="shortcut icon">
	<link href="css/style.css" rel="stylesheet" type="text/css">
	<link href="css/widgets.min.css" rel="stylesheet" type="text/css">
	<link href="css/pixeladmin.min.css" rel="stylesheet" type="text/css">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>Pomangam CMS</title>
</head>

<body>
	<%	
		String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
		String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
	%>
	<!-- LOGIN FORM -->
	<center>
		<div class="text-center" style="padding:50px 0">
			<div class="logo">login</div>
			<!-- Main Form -->
			<div class="login-form-1">
				<form id="login-form" class="text-left">
					<div class="login-form-main-message"></div>
					<div class="main-login-form">
						<div class="login">
							<div class="login-group" style="text-align:left;">
								<div class="form-group">
									<input type="text" class="form-control" id="username" name="user" placeholder="username">
								</div>
								<div class="form-group">
									<input type="password" class="form-control" id="password" name="password" placeholder="password">
								</div>
							</div>
							<button type="submit" class="login-button"><i class="fa fa-chevron-right"></i></button>
						</div>
					</div>
					
					<div class="etc-login-form">
						<center>
						로그인 상태 유지
						<label class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
							<input type="checkbox" id="ob-remember" value="1" checked>
							<div class="switcher-indicator">
								<div class="switcher-yes">ON</div>
								<div class="switcher-no">OFF</div>
							</div>
						</label>
						<center>
						<br>
						<p>Inquire of Admin about Access 
							<br>
							<center><a onClick="$('#inquire_modal').modal('show');">click here</a><center>
						</p>
					</div>
				</form>
			</div>
			<!-- end:Main Form -->
		</div>
		<input type="hidden" id="rsaPublicKeyModulus" value="<%=publicKeyModulus%>" />
		<input type="hidden" id="rsaPublicKeyExponent" value="<%=publicKeyExponent%>" />
        <form id="securedLoginForm" name="securedLoginForm" action="./j_spring_security_check" method="post" style="display: none;">
            <input type="hidden" name="securedUsername" id="securedUsername" value="" />
            <input type="hidden" name="securedPassword" id="securedPassword" value="" />
            <input type="hidden" name="remember" id="remember" value="" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
	</center>
	
	<div class="modal fade" id="inquire_modal">
		<div class="modal-dialog modal-sm">
		<form class="form" action=" " method="post" id="inquireform">
			<div class="modal-content">
				<div class="modal-header" style="padding-bottom:25px;">
					<button  type="button" class="close" data-dismiss="modal">&times;</button>
				</div>		
				<div class="modal-body">
						
					<!-- MODAL INQUIRE FORM -->
					<div class="text-center" style="padding:15px 0">
						<div class="logo">INQUIRE</div>
						<!-- Main Form -->
						<div class="login-form-1">
							<form id="register-form" class="text-left">
								<div class="login-form-main-message"></div>
								<div class="main-login-form">
									<div class="login-group">
										<div class="form-group">
											<label for="reg_fullname" class="sr-only">Full Name</label>
											<input type="text" class="form-control" id="reg_fullname" name="reg_fullname" placeholder="Full name">
										</div>
										<div class="form-group">
											<label for="reg_username" class="sr-only">Email address</label>
											<input type="text" class="form-control" id="reg_username" name="reg_username" placeholder="Email address">
										</div>
										<div class="form-group" style="padding-right:0px;">
											<label for="reg_contents" class="sr-only">Write Here...</label>
											<textarea style="margin-top:20px; border:dotted 2px; width:200px;" 
												 rows="8" id="reg_contents" name="reg_contents" placeholder="Write Here..."></textarea>
										</div>
									</div>
									<button type="submit" style="right:40%; top:110%;" class="login-button"><i class="fa fa-chevron-right"></i></button>
								</div>
							</form>
						</div>
						<!-- end:Main Form -->
					</div>
						
				</div>
			</div>
			</form>
		</div>	
	</div>
	
	<script src='js/jquery-3.3.1.min.js'></script>
	<script src='js/bootstrap.min.js'></script>
	<script src="js/prefixfree.min.js"></script>
	<script src="js/rsa/jsbn.js"></script>
	<script src="js/rsa/rsa.js"></script>
	<script src="js/rsa/prng4.js"></script>
	<script src="js/rsa/rng.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.13.1/jquery.validate.min.js"></script>
	<script src="js/loginform.js"></script>
	<script src='js/sha512.js'></script>
	<script src='js/common.js'></script>
	
	<script>
		var userId = getCookie("cookieUsername"); 
	    $("#username").val(userId); 
	     
	    if($("#username").val() != ""){
	        $("#remember").attr("checked", true);
	    }
	
	
		var status = <%=new Gson().toJson(request.getSession().getAttribute("status")) %>;
		var error = '${error}';
		var msg = '${msg}';
		
		if(error != '') {
			alert(error);
		}
		if(msg != '') {
			alert(msg);
		}
	</script>

</body>
</html>