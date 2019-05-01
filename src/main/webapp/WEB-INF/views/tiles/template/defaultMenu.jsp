<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navbar -->

<%
	String curTarget = session.getAttribute("curTarget") == null ? "0" : session.getAttribute("curTarget")+"";
	String curTargetName = "전체";
	switch(curTarget) {
	case "0":
		curTargetName = "전체";
		break;
	case "1":
		curTargetName = "항공대학교";
		break;
	case "2":
		curTargetName = "군산대학교";
		break;
	}
	
%>

<nav class="navbar px-navbar">
	<div class="navbar-header">
		<a class="navbar-brand" href="./">Pomangam CMS</a>
	</div>

	<button type="button" class="navbar-toggle collapsed"
		data-toggle="collapse" data-target="#px-navbar-collapse"
		aria-expanded="false">
		<i class="navbar-toggle-icon"></i>
	</button>

	<div class="collapse navbar-collapse" id="px-navbar-collapse">
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown"><a class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="false" id="target"><%=curTargetName %></a>
				<ul class="dropdown-menu">
					<li><a href="./session/setItem.do?key=curTarget&value=0" >전체</a></li>
					<li><a href="./session/setItem.do?key=curTarget&value=1" >항공대학교</a></li>
					<li><a href="./session/setItem.do?key=curTarget&value=2" >군산대학교</a></li>
				</ul>
			</li>
			<li class="dropdown"><a class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="false" id="nickname"></a>
				<ul class="dropdown-menu">
					<li><a href="./logout.do">로그아웃</a></li>
					<li><a href="#">로그인 시각</a></li>
					<li class="divider"></li>
					<li><a href="#" id="curtime"></a></li>
				</ul>
			</li>
		</ul>
	</div>
	
	<script>
	//onclick="setTarget(this, 0)
	/*
	var curTargetName = sessionStorage.getItem('curTargetName');
	if(curTargetName) {
		$('#target').text(curTargetName);
	}
	
	function setTarget(e, idx) {
		sessionStorage.setItem('curTarget', idx);
		sessionStorage.setItem('curTargetName', $(e).text());
		$('#target').text($(e).text());
	}
	*/
	</script>
	
</nav>
