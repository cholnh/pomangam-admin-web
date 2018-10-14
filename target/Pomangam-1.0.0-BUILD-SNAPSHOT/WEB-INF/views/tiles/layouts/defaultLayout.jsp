<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page import="com.mrporter.pomangam.common.security.model.domain.User"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.reflect.TypeToken"%>
<%
	String userjson = (String) request.getSession().getAttribute("user");
	User user = new Gson().fromJson(userjson, new TypeToken<User>() {}.getType());
%>
<html>

<head>
<title><tiles:getAsString name="title" /></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<link href="images/favicon.ico" rel="shortcut icon">
<link href="css/fonts.css" rel="stylesheet" type="text/css">
<link href="css/ionicons.min.css" rel="stylesheet" type="text/css">
<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- Core stylesheets -->
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/pixeladmin.min.css" rel="stylesheet" type="text/css">
<link href="css/widgets.min.css" rel="stylesheet" type="text/css">

<!-- Theme -->
<link href="css/themes/candy-blue.min.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/file-upload-with-preview.css" rel="stylesheet" type="text/css">
<link href="css/bootstrap-table-reorder-rows.css" rel="stylesheet" >
<script type="text/javascript" src="js/holder.js"></script>

<!-- Pace.js -->
<script src="pace/pace.min.js"></script>
<style>
    .page-header-form .input-group-addon,
    .page-header-form .form-control {
      background: rgba(0,0,0,.05);
    }
</style>
</head>

<body>

	<!-- Your scripts -->
	<script src="js/jquery-3.3.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/pixeladmin.min.js"></script>
	<script src="js/bootstrap-table.min.js"></script>
	<script src="js/bootstrap-table-ko-KR.min.js"></script>
	<script src="js/tableExport.min.js"></script>
	<script src="js/bootstrap-table-export.js"></script>
	<script src="js/common-crud.js"></script>
	<script src="js/jquery.formautofill.min.js"></script>
	<script>
		var user = <%=userjson%>;
		
		function hasRole(role) {
			var authlist = user.authorities;
			for(var i=0; i<authlist.length; i++) {
				if(authlist[i].name === role) {
					return true;
				}
			}
			return false;
		}
	</script>
	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="menu" />
	<tiles:insertAttribute name="body" />
	<tiles:insertAttribute name="footer" />
	
	<script>
		function getTime() {
			var currentdate = new Date(); 
			var datetime =  currentdate.getFullYear() +"-"
							+ (currentdate.getMonth()+1) + "-"
			                + currentdate.getDate() + " " 
			                + currentdate.getHours() + ":"  
			                + currentdate.getMinutes() + ":" 
			                + currentdate.getSeconds();
			return datetime;
		}
		$(document).ready(function() {
			document.getElementById('curtime').textContent = getTime();
			document.getElementById('nickname').textContent = user.nickname;
		});
	</script>
</body>
</html>