<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.mrporter.pomangam.common.security.model.domain.User"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.reflect.TypeToken"%>
<%@ page import="java.util.List"%>
<%@ page import="com.mrporter.pomangam.common.security.model.domain.Role"%>

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
	String[] ROLE_RESTOURANT = {"ROLE_ORDER_VIEW", "ROLE_PRODUCT_VIEW", "ROLE_PROFIT_VIEW"};
	String[] ROLE_PORTER = {"ROLE_PORTER_VIEW", "ROLE_SETTLEMENT_VIEW"};
	String[] ROLE_MEMBER = {"ROLE_MEMBER_VIEW","ROLE_OWNER_VIEW","ROLE_ADMIN_VIEW","ROLE_AUTHORITY_VIEW"};
	String[] ROLE_TARGET = {"ROLE_TARGET_VIEW","ROLE_RESTAURANT_VIEW"};
	
	
%>

<!-- Nav -->
<nav class="px-nav px-nav-left">
	<button type="button" class="px-nav-toggle" data-toggle="px-nav">
		<span class="px-nav-toggle-arrow"></span> <span
			class="navbar-toggle-icon"></span> <span
			class="px-nav-toggle-label font-size-11">HIDE MENU</span>
	</button>

	<ul class="px-nav-content">
		
		<%if( (n = howManyMatch(ROLE_RESTOURANT)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-android-restaurant"></i><span class="px-nav-label">매장관리<span
					class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
				
				<%if(hasRole(ROLE_RESTOURANT[0])) {%>
				<li class="px-nav-item"><a href="./order.do"><span
						class="px-nav-label">주문현황</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_RESTOURANT[1])) {%>
				<li class="px-nav-item"><a href="./product.do"><span
						class="px-nav-label">메뉴관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_RESTOURANT[2])) {%>
				<li class="px-nav-item"><a href="./profit.do"><span
						class="px-nav-label">매출관리</span></a></li>
				<%} %>
				
			</ul></li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_PORTER)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-android-car"></i><span class="px-nav-label">배달관리<span
					class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
			
				<%if(hasRole(ROLE_PORTER[0])) {%>
				<li class="px-nav-item"><a href="./porter.do"><span
						class="px-nav-label">주문현황</span></a></li>
				<%} %>		
				<%if(hasRole(ROLE_PORTER[1])) {%>
				<li class="px-nav-item"><a href="./settlement.do"><span
						class="px-nav-label">정산관리</span></a></li>
				<%} %>		
			</ul></li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_MEMBER)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-android-contacts"></i><span
				class="px-nav-label">회원관리<span 
				class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
				<%if(hasRole(ROLE_MEMBER[0])) {%>
				<li class="px-nav-item"><a href="./member.do"><span
						class="px-nav-label">회원관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_MEMBER[1])) {%>
				<li class="px-nav-item"><a href="./owner.do"><span
						class="px-nav-label">점주관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_MEMBER[2])) {%>
				<li class="px-nav-item"><a href="./admin.do"><span
						class="px-nav-label">운영자관리</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_MEMBER[3])) {%>
				<li class="px-nav-item"><a href="./authority.do"><span
						class="px-nav-label">권한 설정</span></a></li>
				<%} %>
			</ul></li>
		<%} %>
		
		<%if( (n = howManyMatch(ROLE_TARGET)) > 0) { %>
		<li class="px-nav-item px-nav-dropdown"><a href="#"><i
				class="px-nav-icon ion-ios-location"></i><span
				class="px-nav-label">지역관리<span 
				class="label label-danger"><%=n %></span></span></a>
			<ul class="px-nav-dropdown-menu">
				<%if(hasRole(ROLE_TARGET[0])) {%>
				<li class="px-nav-item"><a href="./target.do"><span
						class="px-nav-label">지역 설정</span></a></li>
				<%} %>
				<%if(hasRole(ROLE_TARGET[1])) {%>
				<li class="px-nav-item"><a href="./restaurant.do"><span
						class="px-nav-label">매장 설정</span></a></li>
				<%} %>
			</ul></li>
		<%} %>
		
	</ul>
</nav>