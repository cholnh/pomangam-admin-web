<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.mrporter.pomangam.member.vo.AdminBean"%>

<div class="px-content" style="padding:5px; ">
	<div class="page-header">
		<h1>
			<span class="text-muted font-weight-light"><i
				class="page-header-icon ion-person-stalker"></i>배달 관리 / </span>매출 정산 
		</h1>
	</div>
	<div class="container" style="-webkit-overflow-scrolling:touch;">

		<div class="table-title" style="background-color: #f5f5f5">
			<div class="row">
				<div class="col-sm-6">
					<div style="color:black">
						<input id="ob-gdate" type="text" placeholder="날짜 입력 : yyyy-mm-dd" pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}">
					</div>
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
			data-url="./settlement/getlist.do">
			<thead>
				<tr>
					<th data-field="" data-checkbox="true"></th>
					<th data-field="idx" data-sortable="true">개별번호</th>
					<th data-field="pi_idx" data-sortable="true">주문번호</th>
					<th data-field="res_name" data-sortable="true">음식점</th>
					<th data-field="amount" data-sortable="true">수량</th>
					<th data-field="additional" data-sortable="true">추가사항</th>
					<th data-field="pro_name" data-sortable="true" data-formatter="nameFormatter">제품명</th>
					<th data-field="price" data-sortable="true" data-formatter="priceFormatter">총 가격</th>
					<th data-field="status" data-sortable="true">처리상태</th>
					<th data-field="cashreceipt" data-sortable="true">현금영수증</th>
					<th data-field="c_commission_prc" data-sortable="true">고객수수료</th>
					<th data-field="s_commission_prc" data-sortable="true">업체수수료</th>
					
				</tr>
			</thead>
		</table>

	</div>
</div>

<script>
function nameFormatter(value, row) {
	return value + '(' + row.price + ')';
}
function priceFormatter(value, row) {
	var partTotal = 0;
	if(row.additional) {
		var parts = row.additional.split(',');
		for(var i=0; i<parts.length; i++) {
			var part = parts[i];
			var amount = part.split('-')[1];
			var price = part.split('-')[2];
			partTotal += (parseInt(amount) * parseInt(price));
		}
	}
	return parseInt(value) + partTotal;
}

$('#ob-gdate').change(function() {
	var $this = $(this);
	
	if($this.val().length > 10) {
		alert('20xx-xx-xx 형식에 맞추세요.\n예시 : 2018-09-12');
	} else if($this.val().length == 10) {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	
		$.ajax({
			type : "POST",
			url : './settlement/getlist.do',
			data : {
				date : $this.val()
			},
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			success : function(data) {
				$('#table').bootstrapTable('load', data);
				alert('데이터 로드 완료');
				$('#ob-gdate').val('');
			},
			error : function(msg) {
				alert('ajax error' + msg);
			}
		});
	}
});

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