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
				class="page-header-icon ion-person-stalker"></i>배달 관리 / </span>주문 현황
		</h1>
	</div>
	<div class="container" style="-webkit-overflow-scrolling:touch;">

		<div class="table-title" style="background-color: #f5f5f5">
			<div class="row">
				<div class="col-sm-6" style="color:black;font-size:18px">
					 <span id="totalnum"></span>
				</div>
				<div class="col-sm-6">
					<a class="btn btn-info" id="export"><i
						class="ion-android-download"></i> <span>내보내기</span></a>
					<a class="btn btn-primary" id="total"><span>전체보기</span></a>
					<a class="btn btn-primary" id="paydone"><span>입금처리</span></a>
				</div>
			</div>
		</div>
		<table id="table" class="table-responsive" data-toggle="table" data-show-refresh="true"
			data-minimum-count-columns="2"
			data-mobile-responsive="true" style="background-color: white"
			data-search="true" data-pagination="true" style="-webkit-overflow-scrolling:touch;"
			data-url="./porter/gettotaylist.do">
			<colgroup>
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:200px">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				<col style="width:auto">
				
			</colgroup>
			<thead>
				<tr>
					<th data-field="" data-checkbox="true"></th>
					<th data-field="status" data-sortable="true" data-formatter="statusFormatter">주문상태</th>
					<th data-field="idx_box" data-sortable="true" data-cell-style="boxStyle">bn</th>
					<th data-field="idx" data-sortable="true">pn</th>
					<th data-field="idxes_payment" data-sortable="true">개별번호</th>
					<th data-field="receive_date" data-sortable="true">받는날짜</th>
					<th data-field="receive_time" data-sortable="true">받는시간</th>
					<th data-field="where" data-sortable="true">음식받는곳</th>
					<th data-field="member_name" data-sortable="true">이름</th>
					<th data-field="phonenumber" data-sortable="true">핸드폰번호</th>
					<th data-field="guestname" data-sortable="true">비회원이름</th>
					<th data-field="totalprice" data-sortable="true">총가격</th>
					<th data-field="password" data-sortable="true">비밀번호</th>
					<th data-field="timestamp" data-sortable="true">주문생성시간</th>
				</tr>
			</thead>
		</table>
	</div>
</div>


<!-- Modal-Create -->
<div class="modal fade" id="detailInfo" tabindex="-1" style="-webkit-overflow-scrolling:touch;">
	<div class="modal-dialog">
		<form class="form-horizontal" id="detailForm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4 class="modal-title" id="myModalLabel">상세 정보</h4>
				</div>
				<div class="modal-body">
					<div class="table-title" style="background-color: white">
						<div class="row">
							<div class="col-sm-6">
							</div>
							<div class="col-sm-6">
								<a class="btn btn-primary" id="copy" onclick="copy()"><span>복사하기</span></a>
							</div>
						</div>
					</div>
					
					<table id="table2" class="table table-responsive" data-toggle="table" data-show-refresh="true"
						data-mobile-responsive="true" style="background-color: white; -webkit-overflow-scrolling:touch;"
						data-search="true" data-pagination="true">
						<thead>
							<tr>
								<th data-field="" data-checkbox="true"></th>
								<th data-field="idx" data-sortable="true">번호</th>
								<th data-field="idx_restaurant" data-sortable="true" data-formatter="restaurantFormatter">음식점</th>
								<th data-field="idx_product" data-sortable="true" data-formatter="productFormatter">제품명</th>
								<th data-field="additional" data-sortable="true" data-formatter="additionalFormatter">추가사항</th>
								<th data-field="requirement" data-sortable="true">요청사항</th>
								<th data-field="amount" data-sortable="true">개수</th>
							</tr>
						</thead>
					</table>
					<textarea id="test"></textarea>
				</div>
				<div class="modal-footer">
					<button type="button submit" class="btn btn-primary">확 인</button>
					<button type="button" class="btn" data-dismiss="modal">취 소</button>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="hidden" name="username" id="username"/>
					<input type="hidden" id="regdate" name="regdate" />
				</div>
			</div>
		</form>
	</div>
</div>

<script>
//$('.fixed-table-body').css('height', 'auto'); 

var targetList = ${targetList};
var restaurantList = ${restaurantList};
var productList = ${productList};
//var additionalList = ${additionalList};
$('#test').hide();

var targetMap;
targetMap = targetList.reduce(function(map, obj) {
	map[obj.idx] = obj.name;
	return map;
}, {});
var restaurantMap;
restaurantMap = restaurantList.reduce(function(map, obj) {
	map[obj.idx] = obj;
	return map;
}, {});
var productMap;
productMap = productList.reduce(function(map, obj) {
	map[obj.idx] = obj.name;
	return map;
}, {});
/*
var additionalMap;
additionalMap = additionalList.reduce(function(map, obj) {
	map[obj.idx] = obj.name;
	return map;
}, {});
*/
function boxStyle(value, row, index, field) {
	return {
		css: {"color": "black", "font-weight": "bold"}
	};
}

function statusFormatter(value, row) {
	var result;
	switch(value) {
	case 0:
		result = '결제대기';
		break;
	case 1:
		result = '결제완료';
		break;
	case 2:
		result = '결제실패';
		break;
	case 3:
		result = '배달완료';
		break;
	}
	return result;
}

function targetFormatter(value, row) {
	return targetMap[value];
}
function restaurantFormatter(value, row) {
	return restaurantMap[value].name + '(' + restaurantMap[value].tel + ')';
}
function productFormatter(value, row) {
	return productMap[value];
}
function additionalFormatter(value, row) {
	var result = '';
	var parts = value.split(',');
	for(var i=0; i<parts.length; i++) {
		var p = parts[i].split('-');
		if(p[3]) {
			result += p[3] + '('+p[1]+'개)';
			if(i!=parts.length-1) {
				result += ', ';
			}
		}
	}
	return result.length>0?result:'-';
}

function copy() {
	var datas = $('#table2').bootstrapTable('getSelections');
	if(datas.length <= 0) return;
	var result = '';
	
	result +=
		'박스번호 : ' + bean.idx_box + '\n' +
		'주문번호 : ' + bean.idx + '\n' +
		'고객 연락처 : ' + bean.phonenumber + '\n' +
		'배달시간 : ' + bean.receive_date + ' (' + bean.receive_time + ')\n' +
		' ※ 배달시간 15~20분전에 포터가 음식점에 도착합니다\n' +
		'----------------------------------\n';
	
	for(var j=0; j<datas.length; j++) {
		var data = datas[j];
		var add = '';
		var parts = data.additional.split(',');
		for(var i=0; i<parts.length; i++) {
			var p = parts[i].split('-');
			if(p[3]) {
				add += p[3] + '('+p[1]+'개)';
				if(i!=parts.length-1) {
					add += ', ';
				}
			}
		}
		result +=
			'주문 : [' + productMap[data.idx_product]+ ']' + (add.length>0?' + [ ' + add + ']' :'') + '\n' +
			'개수 : ' + data.amount + '\n' +
			(data.requirement.length>0? '요청사항 : ' + data.requirement : '') + '\n\n';
	} 
	
	$('#test').show();
	$('#test').val(result);
	$('#test')[0].select();
	//document.execCommand('copy');
	select_all_and_copy($('#test')[0]);
	$('#test').hide();
}

function select_all_and_copy(el) {
	if (document.body.createTextRange) {
        // IE
		var textRange = document.body.createTextRange();
		textRange.moveToElementText(el);
		textRange.select();
		textRange.execCommand("Copy");   
	} else if (window.getSelection && document.createRange) {
        // non-IE
		var editable = el.contentEditable; // Record contentEditable status of element
		var readOnly = el.readOnly; // Record readOnly status of element
		el.contentEditable = true; // iOS will only select text on non-form elements if contentEditable = true;
		el.readOnly = false; // iOS will not select in a read only form element
		var range = document.createRange();
		range.selectNodeContents(el);
		var sel = window.getSelection();
		sel.removeAllRanges();
		sel.addRange(range); // Does not work for Firefox if a textarea or input
		if (el.nodeName == "TEXTAREA" || el.nodeName == "INPUT")
			el.select(); // Firefox will only select a form element with select()
		if (el.setSelectionRange && navigator.userAgent.match(/ipad|ipod|iphone/i))
			el.setSelectionRange(0, 999999); // iOS only selects "form" elements with SelectionRange
		el.contentEditable = editable; // Restore previous contentEditable status
		el.readOnly = readOnly; // Restore previous readOnly status
		if (document.queryCommandSupported("copy")) {
			var successful = document.execCommand('copy'); 
		}  else {
			if (!navigator.userAgent.match(/ipad|ipod|iphone|android|silk/i))
				tooltip(el, "Press CTRL+C to copy");
		}
	}
} 

var bean;

$('#table').off('click-row.bs.table').on('click-row.bs.table',
	function(e, row, $element) {
		bean = row;
		$('#table2').bootstrapTable('removeAll');
		$('#detailInfo').modal({backdrop: 'static'});
		$('#detailInfo').modal('show');
	
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	
		$.ajax({
			type : "POST",
			url : './porter/getdetail.do',
			data : {
				idxes_payment : row.idxes_payment
			},
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			success : function(data) {
				$('#table2').bootstrapTable('load', data);
				
			},
			error : function(msg) {
				alert('ajax error' + msg);
			}
		});
		
	}
);
	
$('#table2').off('click-row.bs.table').on('click-row.bs.table',
		function(e, row, $element) {
	
			var result = '';
			
			result +=
				'박스번호 : ' + bean.idx_box + '\n' +
				'주문번호 : ' + bean.idx + '\n' +
				'고객 연락처 : ' + bean.phonenumber + '\n' +
				'배달시간 : ' + bean.receive_date + ' (' + bean.receive_time + ')\n' +
				' ※ 배달시간 15~20분전에 포터가 음식점에 도착합니다\n' +
				'----------------------------------\n';
			
			var data = row;
			var add = '';
			var parts = data.additional.split(',');
			for(var i=0; i<parts.length; i++) {
				var p = parts[i].split('-');
				if(p[3]) {
					add += p[3] + '('+p[1]+'개)';
					if(i!=parts.length-1) {
						add += ', ';
					}
				}
			}
			result +=
				'주문 : [' + productMap[data.idx_product]+ ']' + (add.length>0?' + [ ' + add + ']' :'') + '\n' +
				'개수 : ' + data.amount + '\n' +
				(data.requirement.length>0? '요청사항 : ' + data.requirement : '') + '\n\n';
			
			$('#test').show();
			$('#test').val(result);
			$('#test')[0].select();
			//document.execCommand('copy');
			select_all_and_copy($('#test')[0]);
			$('#test').hide();
			
		}
	);

// paydone event
$('#paydone').off('click').on('click', function(e) {
	var selections = $('#table').bootstrapTable('getSelections');
	if(selections.length <= 0) {
		alert('상태를 변경할 데이터를 선택하세요.');
		return;
	}

	var selectedIdxes = '';
	for(var index = 0 ; index < selections.length ; index++){
		selectedIdxes += selections[index].idx + ',';
	}
	
	if(confirm('입금완료 상태로 변경 하시겠습니까?')) {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			url : './porter/setdone.do',
			data : {
				idxes: selectedIdxes
			},
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			success : function(data) {
				$('#table').bootstrapTable('refresh');
			},
			error : function(msg) {
				alert('ajax error' + msg);
			}
		});
	}
});

// export event
$('#export').off('click').on('click', function(e) {
	$('#table').bootstrapTable('togglePagination');
	$('#table').tableExport({
	      type: 'excel'
       });
});

// export event
var isTotal = false;
$('#total').off('click').on('click', function(e) {
	var w = '';
	if(isTotal) {
		w = './porter/gettotaylist.do';
		$('#total > span').text('전체보기');
	} else {
		w = './porter/gettotallist.do';
		$('#total > span').text('당일보기');
	}
	isTotal = !isTotal;
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		type : "POST",
		url : w,
		data : {},
		beforeSend : function(request) {
			request.setRequestHeader(header, token);
		},
		success : function(data) {
			//console.log(data);
			$('#table').bootstrapTable('load', data);
			alert('데이터 로드 완료');
		},
		error : function(msg) {
			alert('ajax error' + msg);
		}
	});
	
});
	

</script>