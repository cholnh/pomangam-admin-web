<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%	 
	response.setContentType("text/html;charset=utf-8");
	request.setCharacterEncoding("utf-8");
%>
<div class="px-content" style="padding:5px; -webkit-overflow-scrolling:touch;">
	<div class="page-header">
		<h1>
			<span class="text-muted font-weight-light"><i
				class="page-header-icon ion-person-stalker"></i>쿠폰 관리 / </span> 쿠폰 등록
		</h1>
	</div>
	<div class="container">

		<div class="table-title" style="background-color: #f5f5f5">
			<div class="row">
				<div class="col-sm-6">

				</div>
				<div class="col-sm-6">
					<a class="btn btn-info" id="export"><i
						class="ion-android-download"></i> <span>내보내기</span></a> <a
						class="btn btn-danger" id="delete"><i
						class="ion-ios-close-outline"></i><span>삭제</span></a> <a
						class="btn btn-warning" id="edit"><i class="ion-edit"></i><span>편집</span></a>
					<a class="btn btn-success" id="create"><i
						class="ion-ios-plus-outline"></i><span>등록</span></a>
				</div>
			</div>
		</div>
		<table id="table" data-toggle="table" data-show-refresh="true"
			data-mobile-responsive="true" style="background-color: white; -webkit-overflow-scrolling:touch;"
			data-search="true" data-pagination="true" class="table-responsive"
			data-url="./coupon/getlist.do?tail=order by idx desc">
			<thead>
				<tr>
					<th data-field="" data-checkbox="true"></th>
					<th data-field="cpno" data-sortable="true">쿠폰 번호</th>
					<th data-field="cpname" data-sortable="true">쿠폰 이름</th>
					<th data-field="discount_prc" data-sortable="true" data-formatter="prcFormatter">할인가격</th>
					<th data-field="availability" data-sortable="true" data-formatter="avFormatter">수량</th>
					<th data-field="reg_username" data-sortable="true">등록 유저</th>
					<th data-field="use_username" data-sortable="true">사용 유저</th>
					<th data-field="register_date" data-sortable="true">발급 날짜</th>
					<th data-field="state_active" data-sortable="true" data-formatter="statusFormatter">상태</th>
					<th data-field="start_date" data-sortable="true">시작 날짜</th>
					<th data-field="end_date" data-sortable="true">만료 날짜</th>
				</tr>
			</thead>
		</table>

	</div>
</div>

<!-- Modal-Create -->
<div class="modal fade" id="couponInfo" tabindex="-1">
	<div class="modal-dialog">
		<form class="form-horizontal" id="modalform">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4 class="modal-title" id="myModalLabel">쿠폰 정보</h4>
				</div>

				<div class="modal-body">
					<div class="form-group">
						<label class="col-md-3 control-label">분류코드</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="ctgcode" name="ctgcode" placeholder="ex)분류코드-XXXX-XXXX">
						</div>
						<div class="col-md-3">
							<label class="btn btn-primary" id="genbtn">발급</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">쿠폰번호</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="cpno" name="cpno" disabled>
						</div>
					</div>
					<hr>
					<div class="form-group">
						<label class="col-md-3 control-label">쿠폰 이름</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="cpname" name="cpname" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">할인 가격</label>
						<div class="col-md-9">
							<input type="number" min=0 value=0 class="form-control" id="discount_prc" name="discount_prc" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">사용 가능 수량</label>
						<div class="col-md-9">
							<input type="number" min=0 value=1 class="form-control" id="availability" name="availability" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">등록 유저</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="reg_username" name="reg_username">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-3 control-label">상태</label>
						<div class="col-md-9"> 
							<label class="switcher switcher-rounded switcher-lg switcher-primary switcher-blank">
								<input type="checkbox" id="state_active" name="state_active" checked>
								<div class="switcher-indicator">
									<div class="switcher-yes">ON</div>
									<div class="switcher-no">OFF</div>
								</div>
							</label>
						</div>
					</div>
					
					<hr>
					<div class="form-group" id="makecnt_el">
						<label class="col-md-3 control-label">생성 수량</label>
						<div class="col-md-9">
							<input type="number" min=1 value=1 class="form-control" id="makecnt" name="makecnt" required>
						</div>
					</div>
					
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">확 인</button>
					<button type="button" class="btn" data-dismiss="modal">취 소</button>
					<input type="hidden" id="idx">
				</div>
			</div>
		</form>
		
	</div>
</div>

<script src="js/rsa/jsbn.js"></script>
<script src="js/rsa/rsa.js"></script>
<script src="js/rsa/prng4.js"></script>
<script src="js/rsa/rng.js"></script>
<script src='js/sha512.js'></script>

<script>
	// Authority
	if(!hasRole("ROLE_COUPON_EDIT")) {
		$('#delete').hide();
		$('#edit').hide();
		$('#create').hide();
		isAuthable = false;
	}
	if(mobilecheck()) {
		$('#export').hide();
	}
</script>

<script>
	
function statusFormatter(value, row) {
	if(value == 0) {
		return "비활성화";
	} else if (value == 1) {
		return "활성화";
	}
	
}

function prcFormatter(value, row) {
	return value+"원";
}

function avFormatter(value, row) {
	return value+"개";
}

function genCode() {
	var ctgcode = $('#ctgcode').val();
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type : "POST",
		url : './coupon/gencode.do',
		data : {
			ctgcode : ctgcode
		},
		beforeSend : function(request) {
			request.setRequestHeader(header, token);
		},
		success : function(status) {
			if (status.code / 100 == 2) {
				$('#cpno').val(status.message);
			} else { // fail
				alert(status.message);
			}
		},
		error : function(msg) {
			alert('ajax error' + msg);
		}
	});
}

$(document).ready(function() {
	
	$('#genbtn').on('click', function() {
		genCode();
	});
	
	$("#modalform").submit(function() {
		var where = $(':submit').attr('id');
		if(!where) return;
		
		if($('#makecnt').val() <= 1 && $('#cpno').val().length == 0) {
			alert('쿠폰 번호를 발급해주세요.');
			return false;
		}
		
		if($('#cpname').val().length == 0 ) {
			alert('쿠폰 이름을 입력해주세요.');
			return false;
		}

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		$.ajax({
			type : "POST",
			url : './coupon/' + where + '.do',
			data : {
				idx : $('#idx').val(),
				cpno : $('#cpno').val(),
				cpname :  $('#cpname').val(),
				reg_username :  $('#reg_username').val()==""?null:$('#reg_username').val(),
				state_active :  $('#state_active').prop("checked")?1:0,
				availability :  $('#availability').val(),
				discount_prc :  $('#discount_prc').val(),
				makecnt : $('#makecnt').val(),
				ctgcode : $('#ctgcode').val()
			},
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			success : function(status) {
				if (status.code / 100 == 2) {
					$('#modalform')[0].reset();
					$('#couponInfo').modal('hide');
					$('#table').bootstrapTable('refresh');
				} else { // fail
					alert(status.message);
				}
			},
			error : function(msg) {
				alert('ajax error' + msg);
			}
		});
		return false;
	});
	
	// insert button event
	$('#create').off('click').on('click',
		function(e) {
			$('#makecnt_el').show();
			$('#modalform')[0].reset();
			$(':submit').attr('id','insert');
			
			$('#idx').val(null);
			$('#state_active').prop("checked", true);
			
			$('#couponInfo').modal({backdrop: 'static'});
			$('#couponInfo').modal('show');
		}
	);
	
	// update button event
	$('#edit').off('click').on('click', 
		function(e) {
			$('#makecnt_el').hide();
			var selections = $('#table').bootstrapTable('getSelections');
			
			if (selections.length > 1)
				alert('한 개만 선택하세요.');
			else if (selections.length == 0)
				alert('수정할 데이터를 선택하세요.');
			else {
				var bean = selections[0];
				setModalInfo(bean);
				$(':submit').attr('id','update');
				$('#couponInfo').modal({backdrop: 'static'});
				$('#couponInfo').modal('show');
			}
		}
	);
	$('#table').off('dbl-click-row.bs.table').on('dbl-click-row.bs.table',
		function(e, row, $element) {
			if(isAuthable) {
				$('#makecnt_el').hide();
				setModalInfo(row);
				$(':submit').attr('id','update');
				$('#couponInfo').modal({backdrop: 'static'});
				$('#couponInfo').modal('show');
			}
		}
	);
	
	$('#table').off('click-row.bs.table').on('click-row.bs.table',
		function(e, row, $element) {
			if( isChecked4table(row.idx) ) {
				var n = $($element[0])[0].rowIndex-1;
				$('#table').bootstrapTable('uncheck', n);
			} else {
				var n = $($element[0])[0].rowIndex-1;
				$('#table').bootstrapTable('check', n);
			}
		}
	);
	
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
	
	// export event
	$('#export').off('click').on('click', function(e) {
		$('#table').bootstrapTable('togglePagination');
		$('#table').tableExport({
		      type: 'excel'
        });
	});
	
	function setModalInfo(row) {
		$('#modalform')[0].reset();
		$('#idx').val(row.idx);
		$('#cpno').val(row.cpno);
		$('#cpname').val(row.cpname);
		$('#discount_prc').val(row.discount_prc);
		$('#availability').val(row.availability);
		$('#reg_username').val(row.reg_username);
		$('#state_active').prop("checked", row.state_active=='0'?false:true);
	}
	
	$('#delete').off('click').on('click', function(e) {
		var selections = $('#table').bootstrapTable('getSelections');
		if(selections.length==0) {
			alert('선택된 노드가 없습니다.');
			return;
		}

		if(confirm('삭제 하시겠습니까?')) {
			var selectedIdxes = '';
			for(var index = 0 ; index < selections.length ; index++) {
				selectedIdxes += selections[index].idx + ',';
			}
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");

			$.ajax({
				type : "POST",
				beforeSend : function(request) {
					request.setRequestHeader(header, token);
				},
				url : "./coupon/delete.do",
				data : {
					idxes: selectedIdxes
				},
				success : function(status) {
					if (status.code / 100 == 2) {
						$('#table').bootstrapTable('refresh');
					} else { // fail
						alert(status.message);
					}
				},
				error : function() {
					alert('ajax error');
				}
			});
		}
	});
});
</script>