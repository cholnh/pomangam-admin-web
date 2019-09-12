<%@page import="com.mrporter.pomangam.target.vo.TargetBean"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.mrporter.pomangam.target.dao.TargetCrudDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navbar -->

<%
	String curTarget = session.getAttribute("curTarget") == null ? "0" : session.getAttribute("curTarget")+"";
	List<TargetBean> targetList = new TargetCrudDAO().getCompactBeanList();
	String curTargetName = "전체";
	if(targetList != null) {
		for(TargetBean bean : targetList) {
			if(bean.getIdx().intValue() == Integer.parseInt(curTarget)) {
				curTargetName = bean.getName();
				break;
			}
		}
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
					<%if(targetList != null) { 
						for(TargetBean bean : targetList) {
					%>
						<li><a href="./session/setItem.do?key=curTarget&value=<%=bean.getIdx() %>" ><%=bean.getName() %></a></li>
					<%}} %>
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
