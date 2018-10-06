<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dduck.argentina.common.security.model.domain.User"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.reflect.TypeToken"%>
<%@ page import="java.util.List"%>
<%@ page import="com.dduck.argentina.common.security.model.domain.Role"%>

<%!	
	List<Role> authlist;
	boolean hasRole(String role) {
		for(Role r : authlist) {
			if(r.getName().equals(role)) {
				return true;
			}
		}
		return false;
	}
	int howManyMatch(String...roles) {
		int result = 0;
		for(String role : roles) {
			if(hasRole(role)) {
				result++;
			}
		}
		return result;
	}
%>
<%
	String userjson = (String) request.getSession().getAttribute("user");
	User user = new Gson().fromJson(userjson, new TypeToken<User>() {}.getType());
	authlist = user.getAuthorities();
	
	int n = 0;
	String[] ROLE_SETTOP = {"ROLE_SETTOP_VIEW"};
	String[] ROLE_BROADCAST = {"ROLE_VOD_VIEW","ROLE_LIVE_VIEW","ROLE_ADVERTISE_VIEW","ROLE_CATEGORY_VIEW"};
	String[] ROLE_GIFT = {"ROLE_GIFT_VIEW","ROLE_GIFTLOG_VIEW"};
	String[] ROLE_ADMIN = {"ROLE_ADMIN_VIEW","ROLE_AUTHORITY_VIEW"};
	String[] ROLE_SETTING = {"ROLE_SERVERGROUP_VIEW","ROLE_SERVERINFO_VIEW","ROLE_SETTOPGROUP_VIEW","ROLE_APP_VIEW"};
	String[] ROLE_NOTICE = {"ROLE_NOTICE_VIEW"};
	
%>

<!-- Nav -->
<nav class="px-nav px-nav-left">
	<button type="button" class="px-nav-toggle" data-toggle="px-nav">
		<span class="px-nav-toggle-arrow"></span> <span
			class="navbar-toggle-icon"></span> <span
			class="px-nav-toggle-label font-size-11">HIDE MENU</span>
	</button>

	<ul class="px-nav-content">
		
		<%if(howManyMatch(ROLE_SETTOP) > 0) {%>
		<li class="px-nav-item"><a href="./settop.do"><i
				class="px-nav-icon ion-monitor"></i><span class="px-nav-label">셋탑관리</span></a>
		</li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_BROADCAST)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-videocamera"></i><span class="px-nav-label">방송관리<span
					class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
				
				<%if(hasRole(ROLE_BROADCAST[0])) {%>
				<li class="px-nav-item"><a href="./vod.do"><span
						class="px-nav-label">VOD 관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_BROADCAST[1])) {%>
				<li class="px-nav-item"><a href="./live.do"><span
						class="px-nav-label">LIVE 관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_BROADCAST[2])) {%>
				<li class="px-nav-item"><a href="./advertise.do"><span
						class="px-nav-label">광고관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_BROADCAST[3])) {%>
				<li class="px-nav-item"><a href="./category.do"><span
						class="px-nav-label">카테고리관리</span></a></li>
				<%} %>
				
			</ul></li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_GIFT)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-card"></i><span class="px-nav-label">상품권관리<span
					class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
			
				<%if(hasRole(ROLE_GIFT[0])) {%>
				<li class="px-nav-item"><a href="./gift.do"><span
						class="px-nav-label">상품권 리스트</span></a></li>
				<%} %>		
				<%if(hasRole(ROLE_GIFT[1])) {%>
				<li class="px-nav-item"><a href="./giftlog.do"><span
						class="px-nav-label">상품권 사용로그</span></a></li>
				<%} %>
						
			</ul></li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_ADMIN)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-android-person"></i><span
				class="px-nav-label">운영자관리<span 
				class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
			
				<%if(hasRole(ROLE_ADMIN[0])) {%>
				<li class="px-nav-item"><a href="./admin.do"><span
						class="px-nav-label">운영자 리스트</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_ADMIN[1])) {%>
				<li class="px-nav-item"><a href="./authority.do"><span
						class="px-nav-label">권한 설정</span></a></li>
				<%} %>
			</ul></li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_SETTING)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-ios-gear"></i><span class="px-nav-label">환경관리<span
					class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
			
				<%if(hasRole(ROLE_SETTING[0])) {%>
				<li class="px-nav-item"><a href="./servergroup.do"><span
						class="px-nav-label">서버 그룹 정보</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_SETTING[1])) {%>		
				<li class="px-nav-item"><a href="./serverinfo.do"><span
						class="px-nav-label">서버 정보</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_SETTING[2])) {%>		
				<li class="px-nav-item"><a href="./settopgroup.do"><span
						class="px-nav-label">셋탑 그룹 정보</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_SETTING[3])) {%>		
				<li class="px-nav-item"><a href="./app.do"><span
						class="px-nav-label">앱관리</span></a></li>
				<%} %>	
					
			</ul></li>
		<%} %>
		
		<%if(howManyMatch(ROLE_NOTICE) > 0) { %>
		<li class="px-nav-item"><a href="./notice.do"><i
				class="px-nav-icon ion-ios-paper"></i><span class="px-nav-label">공지사항관리</span></a>
		</li>
		<%} %>
		
		<li class="px-nav-item"><a href="./test.do"><i
				class="px-nav-icon ion-ios-paper"></i><span class="px-nav-label">테스트</span></a>
		</li>
	</ul>
</nav>