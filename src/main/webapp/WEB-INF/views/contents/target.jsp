<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.mrporter.pomangam.member.vo.AdminBean"%>

<div class="px-content" style="padding:5px">
	<div class="page-header">
		<h1>
			<span class="text-muted font-weight-light"><i
				class="page-header-icon ion-person-stalker"></i>지역 관리 / </span>지역 설정
		</h1>
	</div>
	<div class="container">

		<div class="table-title" style="background-color: #f5f5f5">
			<div class="row">
				<div class="col-sm-6">

				</div>
				<div class="col-sm-6">
					<a class="btn btn-info" id="export"><i
						class="ion-android-download"></i> <span>내보내기</span></a>
				</div>
			</div>
		</div>
		<table id="table" data-toggle="table" data-show-refresh="true"
			data-mobile-responsive="true" style="background-color: white; -webkit-overflow-scrolling:touch;"
			data-search="true" data-pagination="true"
			data-url="./admin/">
			<thead>
				<tr>
					<th data-field="" data-checkbox="true"></th>
					<th data-field="idx" data-sortable="true">번호</th>
					
				</tr>
			</thead>
		</table>

	</div>
</div>

<script>
	
$(document).ready(function() {
	// export event
	$('#export').off('click').on('click', function(e) {
		$('#table').bootstrapTable('togglePagination');
		$('#table').tableExport({
		      type: 'excel'
        });
	});
});
</script>