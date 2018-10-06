<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navbar -->
<nav class="navbar px-navbar">
	<div class="navbar-header">
		<a class="navbar-brand" href="./">7StarTV CMS</a>
	</div>

	<button type="button" class="navbar-toggle collapsed"
		data-toggle="collapse" data-target="#px-navbar-collapse"
		aria-expanded="false">
		<i class="navbar-toggle-icon"></i>
	</button>

	<div class="collapse navbar-collapse" id="px-navbar-collapse">
		<!-- 
		<ul class="nav navbar-nav">
			<li><a href="#">Link</a></a>
		</ul>
		 -->
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown"><a class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="false" id="nickname"></a>
				<ul class="dropdown-menu">
					<li><a href="./signout">로그아웃</a></li>
					<li><a href="#">로그인 시각</a></li>
					<li class="divider"></li>
					<li><a href="#" id="curtime"></a></li>
				</ul>
			</li>
		</ul>
	</div>
</nav>
