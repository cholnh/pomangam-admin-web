<%@page import="com.mrporter.pomangam.restaurant.vo.RestaurantBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="com.mrporter.pomangam.member.vo.AdminBean"%>
<%	
	String publicKeyModulus = (String) request.getAttribute("publicKeyModulus");
	String publicKeyExponent = (String) request.getAttribute("publicKeyExponent");
	
	@SuppressWarnings({"unchecked"})
	List<RestaurantBean> restaurantList = (List<RestaurantBean>) request.getAttribute("restaurantList");
%>
<div class="px-content" style="padding:5px">
	<div class="page-header">
		<h1>
			<span class="text-muted font-weight-light"><i
				class="page-header-icon ion-person-stalker"></i>회원 관리 / </span>점주 목록
		</h1>
	</div>
	<div class="container" style="-webkit-overflow-scrolling:touch;">

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
			data-search="true" data-pagination="true"
			data-url="./admin/getownerlist.do?tail=order by idx desc">
			<thead>
				<tr>
					<th data-field="" data-checkbox="true"></th>
					<th data-field="idx" data-sortable="true">번호</th>
					<th data-field="username" data-sortable="true">ID</th>
					<th data-field="nickname" data-sortable="true">이름</th>
					<th data-field="idx_restaurant" data-sortable="true" data-formatter="restaurantFormatter">지점</th>
					<th data-field="moddate" data-sortable="true">업데이트 일자</th>
				</tr>
			</thead>
		</table>

	</div>
</div>

<!-- Modal-Create -->
<div class="modal fade" id="memberInfo" tabindex="-1">
	<div class="modal-dialog">
		<form class="form-horizontal" id="modalform">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4 class="modal-title" id="myModalLabel">운영자 정보</h4>
				</div>

				<div class="modal-body">
					<div class="form-group">
						<label class="col-md-3 control-label">아이디</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="form_username" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">비밀번호</label>
						<div class="col-md-9">
							<button type="button" class="btn btn-sm btn-warning btn-outline" 
								id="form_password_btn">변경하기</button>
							<input type="password" class="form-control" id="form_password" required>
						</div>
					</div>
					<div class="form-group" id="recheck">
						<label class="col-md-3 control-label">비밀번호 확인</label>
						<div class="col-md-9">
							<input type="password" class="form-control" id="form_password-recheck" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">이름</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="form_nickname" required>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">지점</label>
						<div class="col-md-9">
							<select class="form-control input-lg" id="form_restaurant" required>
							
								<%
								for(RestaurantBean bean : restaurantList) {%>
									<option value="<%=bean.getIdx() %>"><%=bean.getName() %> (<%=bean.getLocation() %>)</option>
								<%}%>
							</select>
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
	var isAuthable = true;
	// Authority
	if(!hasRole("ROLE_OWNER_EDIT")) {
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
	var publicKeyModulus = '<%=publicKeyModulus%>';
	var publicKeyExponent = '<%=publicKeyExponent%>';
	
	var restaurantList = <%=new Gson().toJson(restaurantList)%>;
	var restaurantmap;
	restaurantmap = restaurantList.reduce(function(map, obj) {
		map[obj.idx] = obj.name;
		return map;
	}, {});
	
	function restaurantFormatter(value, row) {
		if(!restaurantmap[value])
			return '없음';
		return restaurantmap[value];
	}
	
$(document).ready(function() {

	function recheck(e) {
		if($(e).val() === $('#form_password').val()) {
			$(e).css("color", "");
			return;
		} else {
			$(e).css("color", "red");
		}
	}
	$('#form_password-recheck').focusout(function(){
		recheck(this);
	});

	$("#modalform").submit(function() {
		var where = $(':submit').attr('id');
		if(!where) return;

		if($('#form_password').val() !== $('#form_password-recheck').val()) {
			alert('비밀번호가 서로 다릅니다.');
			return false;
		}
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		var rsa = new RSAKey();
		rsa.setPublic(publicKeyModulus, publicKeyExponent);
		
		var pw = $('#form_password').val().length === 0 ? null : 
				rsa.encrypt($('#form_password').val());
		
		$.ajax({
			type : "POST",
			url : './admin/' + where + '.do',
			data : {
				idx : $('#idx').val(),
				username : rsa.encrypt($('#form_username').val()),
				password : pw,
				nickname : $('#form_nickname').val(),
				idx_restaurant : $('#form_restaurant').val(),
				
				role: '점주',			// 하드코딩;;
				
				rsaPublicKeyModulus : publicKeyModulus,
				rsaPublicKeyExponent : publicKeyExponent
			},
			beforeSend : function(request) {
				request.setRequestHeader(header, token);
			},
			success : function(status) {
				if (status.code / 100 == 2) {
					$('#modalform')[0].reset();
					$('#memberInfo').modal('hide');
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
			$('#modalform')[0].reset();
			$(':submit').attr('id','insert');
			
			$('#idx').val(null);
			$('#form_username').prop("disabled", false);
			$('#recheck').show();
			$('#form_password').show();
			$('#form_password').prop('required', true);
			$('#form_password-recheck').prop('required', true);
			$('#form_password_btn').hide();
			$('#form_password-recheck').css("color", "");
			
			$('#memberInfo').modal({backdrop: 'static'});
			$('#memberInfo').modal('show');
		}
	);
	
	// update button event
	$('#edit').off('click').on('click', 
		function(e) {
			var selections = $('#table').bootstrapTable('getSelections');
			
			if (selections.length > 1)
				alert('한 개만 선택하세요.');
			else if (selections.length == 0)
				alert('수정할 데이터를 선택하세요.');
			else {
				var bean = selections[0];
				setModalInfo(bean);
				$(':submit').attr('id','update');
				$('#memberInfo').modal({backdrop: 'static'});
				$('#memberInfo').modal('show');
			}
		}
	);
	$('#table').off('dbl-click-row.bs.table').on('dbl-click-row.bs.table',
		function(e, row, $element) {
			if(isAuthable) {
				setModalInfo(row);
				$(':submit').attr('id','update');
				$('#memberInfo').modal({backdrop: 'static'});
				$('#memberInfo').modal('show');
			}
		}
	);
	
	// export event
	$('#export').off('click').on('click', function(e) {
		$('#table').bootstrapTable('togglePagination');
		$('#table').tableExport({
		      type: 'excel'
        });
	});
	
	$('#form_password_btn').off('click').on('click', function(e) {
		$(this).hide();
		$('#recheck').show();
		$('#form_password').show();
		$('#form_password-recheck').css("color", "");
	});
	
	function setModalInfo(row) {
		$('#idx').val(row.idx);
		$('#modalform')[0].reset();
		$('#form_username').val(row.username);
		$('#form_nickname').val(row.nickname);
		
		$('#form_username').prop("disabled", true);
		$('#recheck').hide();
		$('#form_password').prop('required', false);
		$('#form_password-recheck').prop('required', false);
		$('#form_password').hide();
		$('#form_password_btn').show();

		$('#form_restaurant').val(row.idx_restaurant).prop("selected", true);
	}
	
	$('#delete').off('click').on('click', function(e) {
		var selections = $('#table').bootstrapTable('getSelections');
		if(selections.length==0) {
			alert('선택된 노드가 없습니다.');
			return;
		}

		if(confirm('삭제 하시겠습니까?')) {
			var selectedIdxes = '';
			for(var index = 0 ; index < selections.length ; index++){
				selectedIdxes += selections[index].idx + ',';
			}
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");

			$.ajax({
				type : "POST",
				beforeSend : function(request) {
					request.setRequestHeader(header, token);
				},
				url : "./admin/delete.do",
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