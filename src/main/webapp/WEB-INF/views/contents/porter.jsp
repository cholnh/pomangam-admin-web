<%@page import="com.mrporter.pomangam.restaurant.vo.RestaurantBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.mrporter.pomangam.member.vo.AdminBean"%>
<%
	String paramtime = request.getParameter("time");
	String paramres = request.getParameter("res");

	String curTarget = session.getAttribute("curTarget") == null ? "0" : session.getAttribute("curTarget")+"";
	
	@SuppressWarnings({"unchecked"})
	List<Integer> orderedRestaurantList = (List<Integer>) request.getAttribute("orderedRestaurantList");
	@SuppressWarnings({"unchecked"})
	List<RestaurantBean> restaurantBeanList = (List<RestaurantBean>) request.getAttribute("restaurantBeanList");
	
%>
<div class="px-content" style="padding:5px">
	<div class="page-header">
		<h1>
			<span class="text-muted font-weight-light"><i
				class="page-header-icon ion-person-stalker"></i>배달 관리 / </span>주문 현황
		</h1>
	</div>
	<div class="container" style="-webkit-overflow-scrolling:touch;">

		<div class="table-title" style="background-color: #f5f5f5">
			<div style="color:black; text-align:right; margin-botton:12px">
				<%if(orderedRestaurantList!=null && orderedRestaurantList.size()>0){ %>
				<select id="query_restaurant">
					<option value="-1">전체</option>
					<%for(Integer res : orderedRestaurantList) {%>
					<option value="<%=res %>" <%if(paramres!=null){if(res.intValue() == Integer.parseInt(paramres)){out.print("selected");}} %>>
					<%
						for(RestaurantBean bean : restaurantBeanList) {
							if(bean.getIdx().intValue() == res) {
								out.print(bean.getName());
								break;
							}
						}
					%>
					</option>
					<%} %>
				</select>
				<%} %>
				<select id="query_time">
					<option value="-1">전체</option>
				</select>
				<select id="query_loc">
					<%if(curTarget.equals("0")) { %>
						<option value="-1">전체</option>
						<option value="1">학생회관 뒤</option>
						<option value="2">기숙사 식당</option>
						<option value="3">아카데미홀</option>
						<option value="4">제2학생회관</option> 
						<option value="5">기숙사 정문</option>
					<%} else if(curTarget.equals("1")) {%>
						<option value="-1">전체</option>
						<option value="1">학생회관 뒤</option>
						<option value="2">기숙사 식당</option>
					<%} else if(curTarget.equals("2")) {%>
						<option value="-1">전체</option>
						<option value="1">아카데미홀</option>
						<option value="2">제2학생회관</option>
						<option value="3">기숙사 정문</option>
					<%} %>
					
				</select>
			</div>
			<br>
			<div class="row">
				<div class="col-sm-6">
				</div>
				<div class="col-sm-6">
					<a class="btn btn-primary" id="deliveryarrive"><span>배달도착</span></a>
					<a class="btn btn-primary" id="deliverydelay"><span>배달지연</span></a>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
				</div>
				<div class="col-sm-6">
					<!-- <a class="btn btn-info" id="export"><i
						class="ion-android-download"></i> <span>내보내기</span></a> -->
					<a class="btn btn-info" id="copypn"><span>번호복사</span></a>
					<a class="btn btn-info" id="total"><span>전체보기</span></a>
					<a class="btn btn-info" id="viewdetail"><span>상세보기</span></a>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
				</div>
				<div class="col-sm-6">
					<a class="btn btn-primary" id="paycancel"><span>주문취소</span></a>
					<a class="btn btn-primary" id="payrefund"><span>주문환불</span></a>
					<a class="btn btn-primary" id="paydone"><span>입금처리</span></a>
				</div>
			</div>
		</div>
		<table id="table" class="table-responsive" data-toggle="table" data-show-refresh="true"
			data-minimum-count-columns="2"
			data-mobile-responsive="true" style="background-color: white"
			data-search="true" data-pagination="true" style="-webkit-overflow-scrolling:touch;"
			data-url="./porter/gettotaylist.do
				<%
				if(paramtime!=null){out.print("?time="+paramtime);}
				if(paramres!=null){out.print("&res="+paramres);}%>">
			<colgroup>
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
				<col style="width:auto">
			</colgroup>
			<textarea id="copyarea"></textarea>
			<thead>
				<tr>
					<th data-field="" data-checkbox="true"></th>
					<th data-field="status" data-sortable="true" data-formatter="statusFormatter">주문상태</th>
					<th data-field="idx_box" data-sortable="true" data-cell-style="boxStyle">bn</th>
					<th data-field="idx" data-sortable="true">pn</th>
					<!-- <th data-field="idxes_payment" data-sortable="true">개별번호</th> -->
					<th data-field="receive_date" data-sortable="true">받는날짜</th>
					<th data-field="receive_time" data-sortable="true">받는시간</th>
					<th data-field="where" data-sortable="true">음식받는곳</th>
					<th data-field="member_name" data-sortable="true">이름</th>
					<th data-field="phonenumber" data-sortable="true">핸드폰번호</th>
					<th data-field="guestname" data-sortable="true">비회원이름</th>
					<th data-field="totalprice" data-sortable="true">최종 가격</th>
					<th data-field="password" data-sortable="true">비밀번호</th>
					<th data-field="timestamp" data-sortable="true">주문생성시간</th>
					<th data-field="cpno" data-sortable="true">쿠폰</th>
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
							<div class="col-sm-6" style="color:black;font-size:15px">
								<b>주문번호 : <span id="idx_payment"></span></b><br> (<span id="order_status"></span>)
							</div>
							<div class="col-sm-6">
								<a class="btn btn-primary" id="copy" onclick="copy()"><span>복사하기</span></a>
								<a class="btn btn-primary" id="order" onclick="order()"><span>주문처리</span></a>
							</div>
						</div>
					</div>
					<textarea id="test"></textarea>
					<table id="table2" class="table table-responsive" data-toggle="table" data-show-refresh="true"
						data-mobile-responsive="true" style="background-color: white; -webkit-overflow-scrolling:touch;"
						data-search="true" data-pagination="true">
						<thead>
							<tr>
								<th data-field="" data-checkbox="true"></th>
								<th data-field="status" data-sortable="true" data-formatter="orderFormatter">주문상태</th>
								<th data-field="idx_restaurant" data-sortable="true" data-formatter="restaurantFormatter">음식점</th>
								<th data-field="idx_product" data-sortable="true" data-formatter="productFormatter">제품명</th>
								<th data-field="amount" data-sortable="true">개수</th>
								<th data-field="additional" data-sortable="true" data-formatter="additionalFormatter">추가사항</th>
								<th data-field="requirement" data-sortable="true">요청사항</th>
								<th data-field="idx" data-sortable="true">번호</th>
							</tr>
						</thead>
					</table>
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

<!-- Modal-arrive -->
<div class="modal fade" id="arriveInfo" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="myModalLabel">배달도착 알림 전송</h4>
			</div>

			<div class="modal-body">
				<div class="form-group">
					<label class="col-md-3 control-label">장소</label>
					<div class="col-md-9">
						<label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="" checked>
			                <span class="custom-control-indicator"></span>
			               	 전체
			            </label>
			            
			            <%if(curTarget.equals("1")) { %>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="학생회관 뒤">
			                <span class="custom-control-indicator"></span>
			               	 학생회관 뒤
			            </label>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="기숙사 식당 (도착시간 +10분)">
			                <span class="custom-control-indicator"></span>
							기숙사 식당
			            </label>
			            <%} else if(curTarget.equals("2")) { %>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="아카데미홀">
			                <span class="custom-control-indicator"></span>
			               	 아카데미홀
			            </label>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="제2학생회관 (도착시간 +5분)">
			                <span class="custom-control-indicator"></span>
							제2학생회관
			            </label>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="기숙사 정문 (도착시간 +10분)">
			                <span class="custom-control-indicator"></span>
							기숙사 정문
			            </label>
			            <%} %>
			            
					</div>
				</div>
				<hr>
				<div class="form-group">
					<label class="col-md-3 control-label">시간</label>
					<div class="col-md-9">
						<select id="arrivesel">
						</select>
					</div>
				</div>
				<br><br>
			</div>
			
			<div class="modal-footer">
				<button onclick="arrive()" class="btn btn-primary">전 송</button>
				<button type="button" class="btn" data-dismiss="modal">취 소</button>
			</div>
		</div>
	</div>
</div>

<!-- Modal-delay -->
<div class="modal fade" id="delayInfo" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title" id="myModalLabel">배달지연 알림 전송</h4>
			</div>

			<div class="modal-body">
				<div class="form-group">
					<label class="col-md-3 control-label">장소</label>
					<div class="col-md-9">
						<label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="" checked>
			                <span class="custom-control-indicator"></span>
			               	 전체
			            </label>
			            
			            <%if(curTarget.equals("1")) { %>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="학생회관 뒤">
			                <span class="custom-control-indicator"></span>
			               	 학생회관 뒤
			            </label>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="기숙사 식당 (도착시간 +10분)">
			                <span class="custom-control-indicator"></span>
							기숙사 식당
			            </label>
			            <%} else if(curTarget.equals("2")) { %>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="아카데미홀">
			                <span class="custom-control-indicator"></span>
			               	 아카데미홀
			            </label>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="제2학생회관 (도착시간 +5분)">
			                <span class="custom-control-indicator"></span>
							제2학생회관
			            </label>
			            <label class="custom-control custom-checkbox checkbox-inline">
			                <input type="radio" name="where" class="custom-control-input" value="기숙사 정문 (도착시간 +10분)">
			                <span class="custom-control-indicator"></span>
							기숙사 정문
			            </label>
			            <%} %>
			            
					</div>
				</div>
				<br>
				<div class="form-group">
					<label class="col-md-3 control-label">시간</label>
					<div class="col-md-9">
						<select id="arrivesel2">
						</select>
					</div>
				</div>
				<br>
				<hr>
				<br>
				<div class="form-group">
					<label class="col-md-3 control-label">사유</label>
					<div class="col-md-9">
						<input type="text" id="delay_reason" placeholder="ex) 교통 혼잡">
					</div>
				</div>
				<br>
				<div class="form-group">
					<label class="col-md-3 control-label">지연 시간 (분 단위)</label>
					<div class="col-md-6">
						<input type="number" id="delay_min" min=0 value=0>분
					</div>
				</div>
				<br><br>
			</div>
			
			<div class="modal-footer">
				<button onclick="delay()" class="btn btn-primary">전 송</button>
				<button type="button" class="btn" data-dismiss="modal">취 소</button>
			</div>
		</div>
	</div>
</div>

<script>
//$('.fixed-table-body').css('height', 'auto'); 

var targetList = ${targetList};
var restaurantList = ${restaurantList};
var productList = ${productList};
var curTarget = ${curTarget==null?"0":curTarget};

//var additionalList = ${additionalList};
$('#test').hide();
$('#copyarea').hide();

var paramtime = <%=paramtime%>;
var time_list = [12,13,14,17,18,19,21,22];
time_list.forEach(function(e){
	var tf = false;
	if(paramtime != null && (paramtime == e)) {
		tf = true;
	}
	$('#query_time').append($('<option>', {
	    text: e+'시',
	    value: e,
	    selected: tf
	}));
	$('#arrivesel').append($('<option>', {
	    text: e+'시',
	    value: e+'시',
	    selected: tf
	}));
	$('#arrivesel2').append($('<option>', {
	    text: e+'시',
	    value: e+'시',
	    selected: tf
	}));
});

$('#query_time').change(function() {
	var t = $(this).val();
	var url = './porter.do';
	if(t != -1) {
		url += '?time='+t;
	}
	location.href = url;
});

$('#query_loc').change(function() {
	var loc = $(this).val();
	$('#table').bootstrapTable('filterBy');
	
	if(curTarget == 0) {
		if(loc == 1) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['학생회관 뒤']
			});
		} else if(loc == 2) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['기숙사 식당 (도착시간 +10분)']
			});
		} else if(loc == 3) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['아카데미홀']
			});
		} else if(loc == 4) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['제2학생회관 (도착시간 +5분)']
			});
		} else if(loc == 5) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['기숙사 정문 (도착시간 +10분)']
			});
		}
	} else if(curTarget == 1) {
		if(loc == 1) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['학생회관 뒤']
			});
		} else if(loc == 2) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['기숙사 식당 (도착시간 +10분)']
			});
		}
	} else if(curTarget == 2) {
		if(loc == 1) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['아카데미홀']
			});
		} else if(loc == 2) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['제2학생회관 (도착시간 +5분)']
			});
		} else if(loc == 3) {
			$('#table').bootstrapTable('filterBy', {
			    where : ['기숙사 정문 (도착시간 +10분)']
			});
		}
	}
});

$('#query_restaurant').change(function() {
	var t = paramtime;
	var r = $(this).val();
	var url = './porter.do?time='+t;
	if(r != -1) {
		url += '&res='+r;
	}
	location.href = url;
});

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
	return statusMap(value);
}

function orderFormatter(value, row) {
	var result;
	switch(value) {
	case 0:
		result = '<b>주문대기</b>';
		break;
	case 1:
		result = '<del>주문완료</del>';
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

function order() {
	var selections = $('#table2').bootstrapTable('getSelections');
	if(selections.length <= 0) {
		alert('상태를 변경할 데이터를 선택하세요.');
		return;
	}

	var selectedIdxes = '';
	for(var index = 0 ; index < selections.length ; index++){
		selectedIdxes += selections[index].idx + ',';
	}
	
	if(bean.status == 0) {
		if(!confirm('결제대기 상태입니다.\n계속 하시겠습니까?')) {
			return;
		}
	}
	
	if(confirm('주문완료 상태로 변경 하시겠습니까?')) {
		copy();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			url : './porter/setorderdone.do',
			data : {
				idxes: selectedIdxes
			},
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			success : function(data) {
				//$('#table2').bootstrapTable('refresh');
				$('#table2').bootstrapTable('removeAll');
				$('#detailInfo').modal({backdrop: 'static'});
				$('#detailInfo').modal('show');
			
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");	
				$.ajax({
					type : "POST",
					url : './porter/getdetail.do',
					data : {
						idxes_payment : bean.idxes_payment
					},
					beforeSend : function(request) {
						request.setRequestHeader(header, token);
					},
					success : function(e) {
						$('#table2').bootstrapTable('load', e);
						
					},
					error : function(msg) {
						alert('ajax error' + msg);
					}
				});
			},
			error : function(msg) {
				alert('ajax error' + msg);
			}
		});
	}
}

function copy() {
	var datas = $('#table2').bootstrapTable('getSelections');
	if(datas.length <= 0) return;
	var result = '';
	
	result +=
		'주문번호 : ' + bean.idx_box + ' (no.' + bean.idx + ')\n' +
		'배달시간 : ' + bean.receive_date + ' (' + bean.receive_time + ')\n' +
		' ※ 배달시간 15~20분 전에 방문 예정\n' +
		'----------------------------------\n';
	
	for(var j=0; j<datas.length; j++) {
		var data = datas[j];
		var add = '';
		var parts = data.additional.split(',');
		for(var i=0; i<parts.length; i++) {
			var p = parts[i].split('-');
			if(p[3]) {
				add += '추가' + (parts.length==1?'':(i+1)) + ' : ' + p[3] + ' - '+(p[1]*data.amount)+'개\n';
			}
		}
		result +=
			'품명 : [ ' + productMap[data.idx_product]+ ' ]' + ' - ' + data.amount + '개\n' +
			(add.length>0?add:'') + '\n' +
			(data.requirement.length>0? '요청사항 : ' + data.requirement+'\n' : '') + 
			'----------------------------------\n';
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

/*
$('#table')
.on('dbl-click-row.bs.table', function (e, row, $element) {
	viewDetail(row);
});
*/

var touchtime = 0;

var bean;

$('#table').off('click-row.bs.table').on('click-row.bs.table',
	function(e, row, $element) {
	
	if (touchtime == 0) {
        // set first click
        touchtime = new Date().getTime();
        if( isChecked4table(row.idx) ) {
    		var n = $($element[0])[0].rowIndex-1;
    		$('#table').bootstrapTable('uncheck', n);
    	} else {
    		var n = $($element[0])[0].rowIndex-1;
    		$('#table').bootstrapTable('check', n);
    	}
        
    } else {
        if (((new Date().getTime()) - touchtime) < 300) {
            // double click occurred
            touchtime = 0;
            viewDetail(row);
        } else {
            // not a double click so set as a new first click
            touchtime = new Date().getTime();
            if( isChecked4table(row.idx) ) {
        		var n = $($element[0])[0].rowIndex-1;
        		$('#table').bootstrapTable('uncheck', n);
        	} else {
        		var n = $($element[0])[0].rowIndex-1;
        		$('#table').bootstrapTable('check', n);
        	}
        }
    }
	
	
	/*
		bean = row;
		$('#table2').bootstrapTable('removeAll');
		$('#detailInfo').modal({backdrop: 'static'});
		$('#detailInfo').modal('show');
		
		$('#order_status').text(statusMap2(bean.status)); // $element[0].children[1].innerText
		
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
			success : function(e) {
				$('#idx_payment').text(bean.idx_box);
				$('#table2').bootstrapTable('load', e);
				
			},
			error : function(msg) {
				alert('ajax error' + msg);
			}
		});
	*/	
	}
);

function statusMap(value) {
	var result;
	switch(value) {
	case 0:
		result = '<b>결제대기</b>';
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
	case 4:
		result = '<del>취소</del>';
		break;
	case 5:
		result = '환불';
		break;
	case 6:
		result = '<b>주문발송실패</b>';
		break;	
	}
	
	return result;
}

function statusMap2(value) {
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
	case 4:
		result = '취소';
		break;
	case 5:
		result = '환불';
		break;
	case 6:
		result = '주문발송실패';
		break;	
	}
	
	return result;
}

function isChecked4table(idx) {
	var sels = $('#table').bootstrapTable('getSelections');
	for(var i=0; i<sels.length; i++) {
		var sel = sels[i];
		if(sel.idx == idx) {
			return true;
		}
	}
	return false;
}

function isChecked4table2(idx) {
	var sels = $('#table2').bootstrapTable('getSelections');
	for(var i=0; i<sels.length; i++) {
		var sel = sels[i];
		if(sel.idx == idx) {
			return true;
		}
	}
	return false;
}

$('#table2').off('click-row.bs.table').on('click-row.bs.table',
		function(e, row, $element) {
		
		if( isChecked4table2(row.idx) ) {
			var n = $($element[0])[0].rowIndex-1;
			$('#table2').bootstrapTable('uncheck', n);
		} else {
			var n = $($element[0])[0].rowIndex-1;
			$('#table2').bootstrapTable('check', n);
		}
		
		
		
		
		
		/*
			var result = '';
			
			result +=
				'주문번호 : ' + bean.idx_box + ' (no.' + bean.idx + ')\n' +
				'배달시간 : ' + bean.receive_date + ' (' + bean.receive_time + ')\n' +
				' ※ 배달시간 15~20분 전에 방문 예정\n' +
				'----------------------------------\n';
			
			var data = row;
			var add = '';
			var parts = data.additional.split(',');
			for(var i=0; i<parts.length; i++) {
				var p = parts[i].split('-');
				if(p[3]) {
					add += '추가' + (parts.length==1?'':(i+1)) + ' : ' + p[3] + ' - '+(p[1]*data.amount)+'개\n';
				
				}
			}
			result +=
				'품명 : [ ' + productMap[data.idx_product]+ ' ]' + ' - ' + data.amount + '개\n' +
				(add.length>0?add:'') + '\n' +
				(data.requirement.length>0? '요청사항 : ' + data.requirement + '\n' : '') + 
				'----------------------------------\n';
			
			$('#test').show();
			$('#test').val(result);
			$('#test')[0].select();
			//document.execCommand('copy');
			select_all_and_copy($('#test')[0]);
			$('#test').hide();
		*/
		}
	);

// 번호 복사
$('#copypn').off('click').on('click', function(e) {
	var selections = $('#table').bootstrapTable('getSelections');
	if(selections.length <= 0) {
		alert('번호 복사할 데이터를 선택하세요.');
		return;
	}

	var phones = '';
	for(var index = 0 ; index < selections.length ; index++){
		phones += selections[index].phonenumber;
		if(index != selections.length-1) {
			phones += ',';
		}
	}

	$('#copyarea').show();
	$('#copyarea').val(phones);
	$('#copyarea')[0].select();
	//document.execCommand('copy');
	select_all_and_copy($('#copyarea')[0]);
	$('#copyarea').hide();
	alert('복사완료');
});

function viewDetail(selection) {
	
	if(!selection) {
		alert('상세보기할 데이터를 선택하세요.');
		return;
	}
	bean = selection;
	
	$('#table2').bootstrapTable('removeAll');
	$('#detailInfo').modal({backdrop: 'static'});
	$('#detailInfo').modal('show');
	
	$('#order_status').text(statusMap2(bean.status)); // $element[0].children[1].innerText
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");	
	$.ajax({
		type : "POST",
		url : './porter/getdetail.do',
		data : {
			idxes_payment : bean.idxes_payment
		},
		beforeSend : function(request) {
			request.setRequestHeader(header, token);
		},
		success : function(e) {
			$('#idx_payment').text(bean.idx_box);
			$('#table2').bootstrapTable('load', e);
			
		},
		error : function(msg) {
			alert('ajax error' + msg);
		}
	});
}

$('#viewdetail').off('click').on('click', function(e) {
	var selections = $('#table').bootstrapTable('getSelections');
	if(selections.length == 1) {
		viewDetail(selections[0]);
	} else {
		alert('상세보기할 데이터를 하나만 선택해 주세요.');
	}
});

$('#paycancel').off('click').on('click', function(e) {
	var selections = $('#table').bootstrapTable('getSelections');
	if(selections.length <= 0) {
		alert('상태를 변경할 데이터를 선택하세요.');
		return;
	}
	
	var selectedIdxes = '';
	for(var index = 0 ; index < selections.length ; index++){
		selectedIdxes += selections[index].idx + ',';
	}
	
	if(confirm('취소 상태로 변경 하시겠습니까?')) {
		var isMsg = confirm('업체에게 전송 하시겠습니까?');
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			url : './porter/setcancel.do',
			data : {
				idxes: selectedIdxes,
				isMsg: isMsg
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

$('#payrefund').off('click').on('click', function(e) {
	var selections = $('#table').bootstrapTable('getSelections');
	if(selections.length <= 0) {
		alert('상태를 변경할 데이터를 선택하세요.');
		return;
	}
	
	var selectedIdxes = '';
	for(var index = 0 ; index < selections.length ; index++){
		selectedIdxes += selections[index].idx + ',';
	}
	
	if(confirm('환불 상태로 변경 하시겠습니까?')) {
		var isMsg = confirm('업체에게 전송 하시겠습니까?');
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			url : './porter/setrefund.do',
			data : {
				idxes: selectedIdxes,
				isMsg: isMsg
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
		var isMsg = confirm('업체에게 전송 하시겠습니까?');
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			url : './porter/setdone.do',
			data : {
				idxes: selectedIdxes,
				isMsg: isMsg
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

$('#deliveryarrive').off('click').on('click', function(e) {
	if(curTarget == 0) {
		alert('전체 모드에서는 사용할 수 없습니다.\n오른쪽 위 메뉴에서 학교를 선택해 주세요.');
		return;
	}
	$('#arriveInfo').modal();
});

$('#deliverydelay').off('click').on('click', function(e) {
	if(curTarget == 0) {
		alert('전체 모드에서는 사용할 수 없습니다.\n오른쪽 위 메뉴에서 학교를 선택해 주세요.');
		return;
	}
	$('#delayInfo').modal();
});

function arrive() {
	
	var receive_time = $('#arrivesel').val();
	var where = $(":input:radio[name=where]:checked").val();
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		type : "POST",
		url : "./porter/deliveryarrive.do",
		data : {
			receive_time: receive_time,
			where: where
		},
		beforeSend : function(request) {
			request.setRequestHeader(header, token);
		},
		success : function(data) {
			alert('전송완료');
		},
		error : function(msg) {
			alert('ajax error' + msg);
		}
	});
	
	$('#arriveInfo').modal('hide');
}

function delay(delay_min, delay_reason, receive_time, where) {
	var delay_min = $('#delay_min').val();
	var delay_reason = $('#delay_reason').val();
	var receive_time = $('#arrivesel2').val();
	var where = $(":input:radio[name=where]:checked").val();
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		type : "POST",
		url : './porter/deliverydelay.do',
		data : {
			delay_min: delay_min,
			delay_reason: delay_reason,
			receive_time: receive_time,
			where: where
		},
		beforeSend : function(request) {
			request.setRequestHeader(header, token);
		},
		success : function(data) {
			alert('전송완료');
		},
		error : function(msg) {
			alert('ajax error' + msg);
		}
	});
	
	$('#delayInfo').modal('hide');
}

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